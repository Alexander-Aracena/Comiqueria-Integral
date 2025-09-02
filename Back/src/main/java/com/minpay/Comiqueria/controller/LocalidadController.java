package com.minpay.Comiqueria.controller;

import com.minpay.Comiqueria.dto.LocalidadResponseDTO;
import com.minpay.Comiqueria.dto.LocalidadRequestDTO;
import com.minpay.Comiqueria.service.interfaces.ILocalidadService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/localidades")
public class LocalidadController {
    
    @Autowired
    private ILocalidadService localidadService;
    
    @GetMapping("/{id}")
    public ResponseEntity<LocalidadResponseDTO> traerLocalidad(@PathVariable Long id) {
        LocalidadResponseDTO response = this.localidadService.getLocalidad(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<LocalidadResponseDTO>> traerLocalidads(
            @RequestParam(required = false) List<Long> idsLocalidades,
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) Long idDepartamento,
            @RequestParam(required = false) Boolean estaVigente
    ) {
        List<LocalidadResponseDTO> response = this.localidadService.getLocalidades(
                idsLocalidades, nombre, idDepartamento, estaVigente
        );
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<LocalidadResponseDTO> guardarLocalidad(@RequestBody LocalidadRequestDTO dto) {
        LocalidadResponseDTO response = this.localidadService.createLocalidad(dto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<LocalidadResponseDTO> editarLocalidad(
            @PathVariable Long id,
            @RequestBody LocalidadRequestDTO dto
    ) {
        LocalidadResponseDTO response = this.localidadService.editLocalidad(id, dto);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarLocalidad(@PathVariable Long id) {
        this.localidadService.deleteLocalidad(id);
        return ResponseEntity.noContent().build();
    }
}