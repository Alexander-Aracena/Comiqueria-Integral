package com.minpay.Comiqueria.controller;

import com.minpay.Comiqueria.dto.SubcategoriaRequestDTO;
import com.minpay.Comiqueria.dto.SubcategoriaResponseDTO;
import com.minpay.Comiqueria.service.interfaces.ISubcategoriaService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/subcategorias")
public class SubcategoriaController {

    @Autowired
    private ISubcategoriaService subcategoriaService;

    @GetMapping("/{id}")
    public ResponseEntity<SubcategoriaResponseDTO> traerSubcategoria(@PathVariable Long id) {
        SubcategoriaResponseDTO response = this.subcategoriaService.getSubcategoria(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<SubcategoriaResponseDTO>> traerSubcategorias(
        @RequestParam(required = false) List<Long> idsSubcategorias,
        @RequestParam(required = false) String nombre,
        @RequestParam(required = false) Long idCategoria,
        @RequestParam(required = false) Boolean estaVigente
    ) {
        List<SubcategoriaResponseDTO> response = this.subcategoriaService.getSubcategorias(
            idsSubcategorias, nombre, idCategoria, estaVigente
        );
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<SubcategoriaResponseDTO> guardarSubcategoria(
        @RequestBody SubcategoriaRequestDTO dto
    ) {
        SubcategoriaResponseDTO response = this.subcategoriaService.createSubcategoria(dto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<SubcategoriaResponseDTO> editarSubcategoria(
        @PathVariable Long id,
        @RequestBody SubcategoriaRequestDTO dto
    ) {
        SubcategoriaResponseDTO response = this.subcategoriaService.editSubcategoria(id, dto);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarSubcategoria(@PathVariable Long id) {
        this.subcategoriaService.deleteSubcategoria(id);
        return ResponseEntity.noContent().build();
    }
}
