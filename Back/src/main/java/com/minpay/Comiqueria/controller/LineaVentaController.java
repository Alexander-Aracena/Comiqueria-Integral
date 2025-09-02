package com.minpay.Comiqueria.controller;

import com.minpay.Comiqueria.dto.LineaVentaResponseDTO;
import com.minpay.Comiqueria.service.interfaces.ILineaVentaService;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/lineasVentas")
public class LineaVentaController {

    @Autowired
    private ILineaVentaService lineaVentaService;

    @GetMapping("/{id}")
    public ResponseEntity<LineaVentaResponseDTO> traerLineaVenta(@PathVariable Long id) {
        LineaVentaResponseDTO response = this.lineaVentaService.getLineaVenta(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<LineaVentaResponseDTO>> traerLineaVentaes(
        @RequestParam(required = false) List<Long> ids,
        @RequestParam(required = false) Long idVenta,
        @RequestParam(required = false) Long idProducto,
        @RequestParam(required = false) Integer minCantidad,
        @RequestParam(required = false) Integer maxCantidad,
        @RequestParam(required = false) BigDecimal minPrecio,
        @RequestParam(required = false) BigDecimal maxPrecio
    ) {
        List<LineaVentaResponseDTO> response = this.lineaVentaService.getLineasVentas(
            ids, idVenta, idProducto, minCantidad, maxCantidad, minPrecio, maxPrecio
        );
        return ResponseEntity.ok(response);
    }
}
