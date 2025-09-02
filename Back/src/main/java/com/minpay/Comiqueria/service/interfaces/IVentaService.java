package com.minpay.Comiqueria.service.interfaces;

import com.minpay.Comiqueria.dto.ProductoResponseDTO;
import com.minpay.Comiqueria.dto.VentaRequestDTO;
import com.minpay.Comiqueria.dto.VentaResponseDTO;
import com.minpay.Comiqueria.model.EstadoVenta;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface IVentaService {
    public VentaResponseDTO getVenta(Long id);
    public List<VentaResponseDTO> getVentas(
        List<Long> ids, LocalDateTime minFechaVenta, LocalDateTime maxFechaVenta,
        BigDecimal minTotal, BigDecimal maxTotal, Long idCliente, EstadoVenta estado
    );
    public VentaResponseDTO createVenta(VentaRequestDTO ventaDTO);
    public VentaResponseDTO editVenta(Long id, VentaRequestDTO ventaDTO);
    public void deleteVenta(Long id);
}
