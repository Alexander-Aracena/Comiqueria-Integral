package com.minpay.Comiqueria.service.interfaces;

import com.minpay.Comiqueria.dto.LineaVentaResponseDTO;
import java.math.BigDecimal;
import java.util.List;

public interface ILineaVentaService {
    public LineaVentaResponseDTO getLineaVenta(Long id);
    public List<LineaVentaResponseDTO> getLineasVentas(
        List<Long> ids,
        Long idVenta,
        Long idProducto,
        Integer minCantidad,
        Integer maxCantidad,
        BigDecimal minPrecio,
        BigDecimal maxPrecio
    );
}