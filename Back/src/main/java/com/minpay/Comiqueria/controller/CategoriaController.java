package com.minpay.Comiqueria.controller;

import com.minpay.Comiqueria.dto.CategoriaRequestDTO;
import com.minpay.Comiqueria.dto.CategoriaResponseDTO;
import com.minpay.Comiqueria.service.interfaces.ICategoriaService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/categorias")
public class CategoriaController {

    @Autowired
    private ICategoriaService categoriaService;

    @GetMapping("/{id}")
    public ResponseEntity<CategoriaResponseDTO> traerCategoria(@PathVariable Long id) {
        CategoriaResponseDTO response = this.categoriaService.getCategoria(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<CategoriaResponseDTO>> traerCategoriaes(
        @RequestParam(required = false) List<Long> ids,
        @RequestParam(required = false) String nombre,
        @RequestParam(required = false) Boolean estaVigente
    ) {
        List<CategoriaResponseDTO> response = this.categoriaService.getCategorias(ids, nombre, estaVigente);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<CategoriaResponseDTO> guardarCategoria(@RequestBody CategoriaRequestDTO dto) {
        CategoriaResponseDTO response = this.categoriaService.createCategoria(dto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<CategoriaResponseDTO> editarCategoria(
        @PathVariable Long id,
        @RequestBody CategoriaRequestDTO dto
    ) {
        CategoriaResponseDTO response = this.categoriaService.editCategoria(id, dto);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarCategoria(@PathVariable Long id) {
        this.categoriaService.deleteCategoria(id);
        return ResponseEntity.noContent().build();
    }
}
