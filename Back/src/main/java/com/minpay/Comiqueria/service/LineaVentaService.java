package com.minpay.Comiqueria.service;

import com.minpay.Comiqueria.dto.LineaVentaResponseDTO;
import com.minpay.Comiqueria.mapper.ILineaVentaMapper;
import com.minpay.Comiqueria.model.LineaVenta;
import java.util.List;
import org.springframework.stereotype.Service;
import com.minpay.Comiqueria.service.interfaces.ILineaVentaService;
import org.springframework.beans.factory.annotation.Autowired;
import com.minpay.Comiqueria.repository.ILineaVentaRepository;
import com.minpay.Comiqueria.repository.specification.LineaVentaSpecifications;
import com.minpay.Comiqueria.utils.Utils;
import java.math.BigDecimal;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LineaVentaService implements ILineaVentaService {
    
    @Autowired
    private ILineaVentaRepository lineaVentaRepository;

    @Autowired
    private ILineaVentaMapper lineaVentaMapper;

    @Override
    @Transactional(readOnly = true)
    public LineaVentaResponseDTO getLineaVenta(Long id) {
        LineaVenta lineaVenta = Utils.findByIdOrThrow(lineaVentaRepository, id, LineaVenta.class);
        return lineaVentaMapper.toLineaVentaResponseDTO(lineaVenta);
    }

    @Override
    @Transactional(readOnly = true)
    public List<LineaVentaResponseDTO> getLineasVentas(
        List<Long> ids,
        Long idVenta,
        Long idProducto,
        Integer minCantidad,
        Integer maxCantidad,
        BigDecimal minPrecio,
        BigDecimal maxPrecio
    ) {
        Specification<LineaVenta> specs = LineaVentaSpecifications.byCriterios(
            ids, idVenta, idProducto, minCantidad, maxCantidad, minPrecio, maxPrecio
        );
        List<LineaVenta> lineasVenta = this.lineaVentaRepository.findAll(specs);
        return Utils.mapearListaA(lineasVenta, this.lineaVentaMapper::toLineaVentaResponseDTO);
    }
}