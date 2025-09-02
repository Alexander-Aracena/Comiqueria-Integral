package com.minpay.Comiqueria.service;

import com.minpay.Comiqueria.dto.LineaVentaRequestDTO;
import com.minpay.Comiqueria.dto.VentaRequestDTO;
import com.minpay.Comiqueria.dto.VentaResponseDTO;
import com.minpay.Comiqueria.exceptions.InvalidOperationException;
import com.minpay.Comiqueria.mapper.IVentaMapper;
import com.minpay.Comiqueria.model.Cliente;
import com.minpay.Comiqueria.model.EstadoVenta;
import com.minpay.Comiqueria.model.LineaVenta;
import com.minpay.Comiqueria.model.Producto;
import com.minpay.Comiqueria.model.Venta;
import com.minpay.Comiqueria.repository.IClienteRepository;
import com.minpay.Comiqueria.repository.IProductoRepository;
import com.minpay.Comiqueria.repository.IVentaRepository;
import com.minpay.Comiqueria.repository.specification.VentaSpecifications;
import com.minpay.Comiqueria.service.interfaces.IVentaService;
import com.minpay.Comiqueria.utils.Utils;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class VentaService implements IVentaService {

    @Autowired
    private IVentaRepository ventaRepository;

    @Autowired
    private IVentaMapper ventaMapper;

    @Autowired
    private IProductoRepository productoRepository;

    @Autowired
    private IClienteRepository clienteRepository;

    @Override
    @Transactional(readOnly = true)
    public VentaResponseDTO getVenta(Long id) {
        Venta venta = Utils.findByIdOrThrow(ventaRepository, id, Venta.class);
        return this.ventaMapper.toVentaResponseDTO(venta);
    }

    @Override
    @Transactional(readOnly = true)
    public List<VentaResponseDTO> getVentas(
        List<Long> ids, LocalDateTime minFechaVenta, LocalDateTime maxFechaVenta,
        BigDecimal minTotal, BigDecimal maxTotal, Long idCliente, EstadoVenta estado
    ) {
        Specification<Venta> specs = VentaSpecifications.byCriterios(
            ids, minFechaVenta, maxFechaVenta, minTotal, maxTotal, idCliente, estado
        );
        List<Venta> ventas = this.ventaRepository.findAll(specs);
        return Utils.mapearListaA(ventas, this.ventaMapper::toVentaResponseDTO);
    }

    @Override
    public VentaResponseDTO createVenta(VentaRequestDTO ventaDTO) {
        Venta venta = this.ventaMapper.toVenta(ventaDTO);
        Cliente cliente = Utils.findByIdOrThrow(
            clienteRepository, ventaDTO.getIdCliente(), Cliente.class
        );
        Set<LineaVenta> lineasVenta = Utils.mapearSetA(ventaDTO.getLineas(), this::crearLineaVenta);
        // Falta asignar 'venta' a cada línea
        venta.getLineas().addAll(lineasVenta);
        lineasVenta.forEach(linea -> linea.setVenta(venta));
        BigDecimal totalVenta = this.calcularVenta(ventaDTO);
        venta.setTotal(totalVenta);
        if (!cliente.getEstaVigente()) {
            throw new InvalidOperationException(
                "El cliente no está vigente y no puede continuar la operación"
            );
        }
        venta.setCliente(cliente);
        venta.setEstado(EstadoVenta.PENDIENTE);
        Venta ventaCreada = this.ventaRepository.save(venta);
        return this.ventaMapper.toVentaResponseDTO(ventaCreada);
    }

    @Override
    public VentaResponseDTO editVenta(Long id, VentaRequestDTO ventaDTO) {
        Venta ventaModificada = Utils.findByIdOrThrow(ventaRepository, id, Venta.class);
        if (ventaModificada.getEstado() != EstadoVenta.PENDIENTE) {
            throw new InvalidOperationException(
                "La venta ya fue procesada, por lo que no es posible modificarla"
            );
        }

        if (!ventaModificada.getCliente().getId().equals(ventaDTO.getIdCliente())) {
            Cliente nuevoCliente = Utils.findByIdOrThrow(
                clienteRepository, ventaDTO.getIdCliente(), Cliente.class
            );
            if (!nuevoCliente.getEstaVigente()) {
                throw new InvalidOperationException(
                    "El nuevo cliente seleccionado no está vigente y no puede continuar la operación."
                );
            }
            ventaModificada.setCliente(nuevoCliente);
        }
        this.sincronizarLineasVenta(ventaModificada, ventaDTO.getLineas());
        BigDecimal totalVenta = this.calcularVenta(ventaDTO);
        ventaModificada.setTotal(totalVenta.setScale(2, RoundingMode.HALF_UP));
        ventaModificada = this.ventaRepository.save(ventaModificada);
        return this.ventaMapper.toVentaResponseDTO(ventaModificada);
    }

    @Override
    public void deleteVenta(Long id) {
        Venta ventaEliminada = Utils.findByIdOrThrow(ventaRepository, id, Venta.class);
        ventaEliminada.setEstado(EstadoVenta.CANCELADA);
        this.ventaRepository.save(ventaEliminada);
    }

    private BigDecimal calcularVenta(VentaRequestDTO ventaDTO) {
        Set<LineaVentaRequestDTO> lineas = ventaDTO.getLineas();
        BigDecimal total = BigDecimal.ZERO;

        for (LineaVentaRequestDTO linea : lineas) {
            Producto producto = Utils.findByIdOrThrow(
                productoRepository, linea.getIdProducto(), Producto.class
            );
            if (!producto.getEstaVigente()) {
                throw new InvalidOperationException(
                    "El producto con ID " + linea.getIdProducto()
                    + " no está vigente y no puede ser vendido."
                );
            }
            BigDecimal cantidad = new BigDecimal(linea.getCantidad());
            BigDecimal subtotalLinea = cantidad.multiply(producto.getPrecio());
            total = total.add(subtotalLinea);
        }

        return total.setScale(2, RoundingMode.HALF_UP);
    }

    private LineaVenta crearLineaVenta(LineaVentaRequestDTO dto) {
        LineaVenta linea = new LineaVenta();
        Producto producto = Utils.findByIdOrThrow(
            productoRepository, dto.getIdProducto(), Producto.class
        );
        linea.setProducto(producto);
        linea.setCantidad(dto.getCantidad());
        linea.setPrecioUnitario(producto.getPrecio());
        return linea;
    }

    private void sincronizarLineasVenta(Venta ventaExistente, Set<LineaVentaRequestDTO> lineasDto) {
        Set<LineaVenta> lineasEnDTOComoEntidades = Utils.mapearSetA(lineasDto, this::crearLineaVenta);
        Set<LineaVenta> lineasParaPersistir = new LinkedHashSet<>();
        
        for (LineaVenta lineaExistente : ventaExistente.getLineas()) {
            LineaVentaRequestDTO lineaDtoCoincidente = lineasDto.stream()
                .filter(dto -> dto.getIdProducto().equals(lineaExistente.getProducto().getId()))
                .findFirst()
                .orElse(null);

            if (lineaDtoCoincidente != null) {
                lineaExistente.setCantidad(lineaDtoCoincidente.getCantidad());
                Producto producto = Utils.findByIdOrThrow(
                    productoRepository, lineaExistente.getProducto().getId(), Producto.class
                );
                
                if (!producto.getEstaVigente()) {
                    throw new InvalidOperationException(
                        "El producto '" + producto.getTitulo()
                        + "' (ID: " + producto.getId() + ") no está vigente y no puede ser parte de la venta."
                    );
                }
                lineaExistente.setPrecioUnitario(producto.getPrecio());

                lineasParaPersistir.add(lineaExistente);

                lineasEnDTOComoEntidades.removeIf(
                    dto -> dto.getProducto().getId().equals(lineaExistente.getProducto().getId())
                );
            }
        }

        for (LineaVenta lineaNueva : lineasEnDTOComoEntidades) {
            lineaNueva.setVenta(ventaExistente);
            lineasParaPersistir.add(lineaNueva);
        }
        ventaExistente.getLineas().clear();
        ventaExistente.getLineas().addAll(lineasParaPersistir);
    }
}
