package com.minpay.Comiqueria.controller;

import com.minpay.Comiqueria.dto.PaisRequestDTO;
import com.minpay.Comiqueria.dto.PaisResponseDTO;
import com.minpay.Comiqueria.service.interfaces.IPaisService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/paises")
public class PaisController {
    @Autowired
    private IPaisService paisService;
    
    @GetMapping("/{id}")
    public ResponseEntity<PaisResponseDTO> traerPais(@PathVariable Long id) {
        PaisResponseDTO response = this.paisService.getPais(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<PaisResponseDTO>> traerPaiss(
            @RequestParam(required = false) List<Long> idsPaiss,
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) Boolean estaVigente
    ) {
        List<PaisResponseDTO> response = this.paisService.getPaises(
                idsPaiss, nombre, estaVigente
        );
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<PaisResponseDTO> guardarPais(@RequestBody PaisRequestDTO dto) {
        PaisResponseDTO response = this.paisService.createPais(dto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<PaisResponseDTO> editarPais(
            @PathVariable Long id,
            @RequestBody PaisRequestDTO dto
    ) {
        PaisResponseDTO response = this.paisService.editPais(id, dto);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarPais(@PathVariable Long id) {
        this.paisService.deletePais(id);
        return ResponseEntity.noContent().build();
    }
}
