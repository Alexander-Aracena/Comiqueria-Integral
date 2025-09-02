package com.minpay.Comiqueria.service;

import com.minpay.Comiqueria.dto.ClienteRequestDTO;
import com.minpay.Comiqueria.dto.ClienteResponseDTO;
import com.minpay.Comiqueria.exceptions.InvalidOperationException;
import com.minpay.Comiqueria.mapper.IClienteMapper;
import com.minpay.Comiqueria.model.Cliente;
import com.minpay.Comiqueria.model.Domicilio;
import com.minpay.Comiqueria.model.Producto;
import com.minpay.Comiqueria.model.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.minpay.Comiqueria.repository.IClienteRepository;
import com.minpay.Comiqueria.repository.IDomicilioRepository;
import com.minpay.Comiqueria.repository.IProductoRepository;
import com.minpay.Comiqueria.repository.IUsuarioRepository;
import com.minpay.Comiqueria.repository.specification.ClienteSpecifications;
import com.minpay.Comiqueria.service.interfaces.IClienteService;
import com.minpay.Comiqueria.utils.Utils;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ClienteService implements IClienteService {

    @Autowired
    private IClienteRepository clienteRepository;

    @Autowired
    private IClienteMapper clienteMapper;

    @Autowired
    private IUsuarioRepository usuarioRepository;

    @Autowired
    private IDomicilioRepository domicilioRepository;

    @Autowired
    private IProductoRepository productoRepository;

    @Override
    @Transactional(readOnly = true)
    public ClienteResponseDTO getCliente(Long id) {
        Cliente cliente = Utils.findByIdOrThrow(clienteRepository, id, Cliente.class);
        return this.clienteMapper.toClienteResponseDTO(cliente);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ClienteResponseDTO> getClientes(List<Long> ids, String nombre, String apellido, String nroDoc, Boolean estaVigente) {
        Specification<Cliente> specs = ClienteSpecifications.byCriterios(
            ids, nombre, apellido, nroDoc, estaVigente
        );
        List<Cliente> clientes = this.clienteRepository.findAll(specs);
        return Utils.mapearListaA(clientes, this.clienteMapper::toClienteResponseDTO);
    }

    @Override
    public ClienteResponseDTO createCliente(ClienteRequestDTO clienteDTO) {
        Cliente cliente = this.clienteMapper.toCliente(clienteDTO);
        Usuario nuevoUsuario = Utils.findByIdOrThrow(
            usuarioRepository, clienteDTO.getIdUsuario(), Usuario.class
        );
        if (!nuevoUsuario.getEstaActivo()) {
            throw new InvalidOperationException("El usuario no está vigente");
        }
        cliente.setUsuario(nuevoUsuario);
        nuevoUsuario.setCliente(cliente);
        cliente = this.clienteRepository.save(cliente);
        return this.clienteMapper.toClienteResponseDTO(cliente);
    }

    @Override
    public ClienteResponseDTO editCliente(Long id, ClienteRequestDTO clienteDTO) {
        Cliente clienteModificado = Utils.findByIdOrThrow(clienteRepository, id, Cliente.class);
        this.clienteMapper.updateClienteFromDTO(clienteDTO, clienteModificado);
        clienteModificado = this.clienteRepository.save(clienteModificado);

        return this.clienteMapper.toClienteResponseDTO(clienteModificado);
    }

    @Override
    public void deleteCliente(Long id) {
        Cliente clienteEliminado = Utils.findByIdOrThrow(clienteRepository, id, Cliente.class);
        if (!clienteEliminado.getEstaVigente()) {
            throw new InvalidOperationException("El cliente ya está dado de baja");
        }
        clienteEliminado.setFechaBaja(LocalDateTime.now());
        clienteEliminado.setEstaVigente(Boolean.FALSE);
        this.clienteRepository.save(clienteEliminado);
    }

    @Override
    public void addFavoritos(Long idCliente, Set<Long> idsProductos) {
        Cliente cliente = Utils.findByIdOrThrow(clienteRepository, idCliente, Cliente.class);
        Set<Producto> productosFavoritos = Utils.findAllById(productoRepository, idsProductos);
        cliente.getFavoritos().addAll(productosFavoritos);
        productosFavoritos.forEach(producto -> producto.getClientes().add(cliente));
        this.clienteRepository.save(cliente);
    }

    @Override
    public void deleteFavoritos(Long idCliente, Set<Long> idsProductos) {
        Cliente cliente = Utils.findByIdOrThrow(clienteRepository, idCliente, Cliente.class);
        Set<Producto> productosFavoritos = Utils.findAllById(productoRepository, idsProductos);
        cliente.getFavoritos().removeAll(productosFavoritos);
        productosFavoritos.forEach(producto -> producto.getClientes().remove(cliente));
        this.clienteRepository.save(cliente);
    }

    @Override
    public void addDomicilio(Long idCliente, Long idDomicilio) {
        Cliente cliente = Utils.findByIdOrThrow(clienteRepository, idCliente, Cliente.class);
        Domicilio nuevoDomicilio = Utils.findByIdOrThrow(
            domicilioRepository, idDomicilio, Domicilio.class
        );
        if (!nuevoDomicilio.getEstaVigente()) {
            throw new InvalidOperationException(
                "El domicilio con ID " + idDomicilio + " no está vigente y no puede ser asignado."
            );
        }
        if (nuevoDomicilio.getCliente() != null && !nuevoDomicilio.getCliente().equals(cliente)) {
            throw new InvalidOperationException(
                "El domicilio con ID " + idDomicilio + " ya está asociado a otro cliente."
            );
        }
        cliente.getDomicilios().add(nuevoDomicilio);
        nuevoDomicilio.setCliente(cliente);
        this.domicilioRepository.save(nuevoDomicilio);
    }

    @Override
    public void deleteDomicilio(Long idCliente, Long idDomicilio) {
        Cliente cliente = Utils.findByIdOrThrow(clienteRepository, idCliente, Cliente.class);
        Domicilio nuevoDomicilio = Utils.findByIdOrThrow(
            domicilioRepository, idDomicilio, Domicilio.class
        );
        if (!cliente.getDomicilios().contains(nuevoDomicilio)) {
            throw new InvalidOperationException(
                "El domicilio con ID " + idDomicilio + " no está asociado al cliente con ID "
                    + idCliente + "."
            );
        }
        if (!nuevoDomicilio.getEstaVigente()) {
            throw new InvalidOperationException(
                "El domicilio con ID " + idDomicilio + " ya fue dado de baja."
            );
        }
        nuevoDomicilio.setEstaVigente(Boolean.FALSE);
        nuevoDomicilio.setFechaBaja(LocalDate.now());
        this.domicilioRepository.save(nuevoDomicilio);
    }
}
