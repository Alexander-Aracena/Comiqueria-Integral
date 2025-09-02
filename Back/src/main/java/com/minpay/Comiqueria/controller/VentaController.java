package com.minpay.Comiqueria.controller;

import com.minpay.Comiqueria.dto.VentaRequestDTO;
import com.minpay.Comiqueria.dto.VentaResponseDTO;
import com.minpay.Comiqueria.model.EstadoVenta;
import com.minpay.Comiqueria.service.interfaces.IVentaService;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ventas")
public class VentaController {

    @Autowired
    private IVentaService ventaService;

    @GetMapping("/{id}")
    public ResponseEntity<VentaResponseDTO> traerVenta(@PathVariable Long id) {
        VentaResponseDTO response = this.ventaService.getVenta(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<VentaResponseDTO>> traerVentas(
        @RequestParam(required = false) List<Long> idsVentas,
        @RequestParam(required = false) LocalDateTime minFechaVenta,
        @RequestParam(required = false) LocalDateTime maxFechaVenta,
        @RequestParam(required = false) BigDecimal minTotal,
        @RequestParam(required = false) BigDecimal maxTotal,
        @RequestParam(required = false) Long idCliente,
        @RequestParam(required = false) EstadoVenta estado
    ) {
        List<VentaResponseDTO> response = this.ventaService.getVentas(
            idsVentas, minFechaVenta, maxFechaVenta, minTotal, maxTotal, idCliente, estado
        );
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<VentaResponseDTO> guardarVenta(@RequestBody VentaRequestDTO dto) {
        VentaResponseDTO response = this.ventaService.createVenta(dto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<VentaResponseDTO> editarVenta(
        @PathVariable Long id,
        @RequestBody VentaRequestDTO dto
    ) {
        VentaResponseDTO response = this.ventaService.editVenta(id, dto);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarVenta(@PathVariable Long id) {
        this.ventaService.deleteVenta(id);
        return ResponseEntity.noContent().build();
    }
}
