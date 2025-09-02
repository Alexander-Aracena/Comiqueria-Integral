package com.minpay.Comiqueria.controller;

import com.minpay.Comiqueria.dto.ProvinciaRequestDTO;
import com.minpay.Comiqueria.dto.ProvinciaResponseDTO;
import com.minpay.Comiqueria.service.interfaces.IProvinciaService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/provincias")
public class ProvinciaController {

    @Autowired
    private IProvinciaService provinciaService;

    @GetMapping("/{id}")
    public ResponseEntity<ProvinciaResponseDTO> traerProvincia(@PathVariable Long id) {
        ProvinciaResponseDTO response = this.provinciaService.getProvincia(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<ProvinciaResponseDTO>> traerProvincias(
        @RequestParam(required = false) List<Long> idsProvincias,
        @RequestParam(required = false) String nombre,
        @RequestParam(required = false) Long idPais,
        @RequestParam(required = false) Boolean estaVigente
    ) {
        List<ProvinciaResponseDTO> response = this.provinciaService.getProvincias(
            idsProvincias, nombre, idPais, estaVigente
        );
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<ProvinciaResponseDTO> guardarProvincia(@RequestBody ProvinciaRequestDTO dto) {
        ProvinciaResponseDTO response = this.provinciaService.createProvincia(dto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ProvinciaResponseDTO> editarProvincia(
        @PathVariable Long id,
        @RequestBody ProvinciaRequestDTO dto
    ) {
        ProvinciaResponseDTO response = this.provinciaService.editProvincia(id, dto);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarProvincia(@PathVariable Long id) {
        this.provinciaService.deleteProvincia(id);
        return ResponseEntity.noContent().build();
    }
}
