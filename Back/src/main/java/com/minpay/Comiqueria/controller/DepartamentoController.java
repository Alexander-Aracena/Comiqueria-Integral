package com.minpay.Comiqueria.controller;

import com.minpay.Comiqueria.dto.DepartamentoRequestDTO;
import com.minpay.Comiqueria.dto.DepartamentoResponseDTO;
import com.minpay.Comiqueria.service.interfaces.IDepartamentoService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/departamentos")
public class DepartamentoController {

    @Autowired
    private IDepartamentoService departamentoService;

    @GetMapping("/{id}")
    public ResponseEntity<DepartamentoResponseDTO> traerDepartamento(@PathVariable Long id) {
        DepartamentoResponseDTO response = this.departamentoService.getDepartamento(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<DepartamentoResponseDTO>> traerDepartamentos(
        @RequestParam(required = false) List<Long> idsDepartamentos,
        @RequestParam(required = false) String nombre,
        @RequestParam(required = false) Long idProvincia,
        @RequestParam(required = false) String nombreProvincia,
        @RequestParam(required = false) Long idPais,
        @RequestParam(required = false) String nombrePais,
        @RequestParam(required = false) Boolean estaVigente
    ) {
        List<DepartamentoResponseDTO> response = this.departamentoService.getDepartamentos(
            idsDepartamentos, nombre, idProvincia, nombreProvincia, idPais, nombrePais, estaVigente
        );
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<DepartamentoResponseDTO> guardarDepartamento(@RequestBody DepartamentoRequestDTO dto) {
        DepartamentoResponseDTO response = this.departamentoService.createDepartamento(dto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<DepartamentoResponseDTO> editarDepartamento(
        @PathVariable Long id,
        @RequestBody DepartamentoRequestDTO dto
    ) {
        DepartamentoResponseDTO response = this.departamentoService.editDepartamento(id, dto);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarDepartamento(@PathVariable Long id) {
        this.departamentoService.deleteDepartamento(id);
        return ResponseEntity.noContent().build();
    }
}
