package com.minpay.Comiqueria.controller;

import com.minpay.Comiqueria.dto.AutorRequestDTO;
import com.minpay.Comiqueria.dto.AutorResponseDTO;
import com.minpay.Comiqueria.service.interfaces.IAutorService;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/autores")
public class AutorController {

    @Autowired
    private IAutorService autorService;

    @GetMapping("/{id}")
    public ResponseEntity<AutorResponseDTO> traerAutor(@PathVariable Long id) {
        AutorResponseDTO response = this.autorService.getAutor(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<AutorResponseDTO>> traerAutores(
        @RequestParam(required = false) List<Long> ids,
        @RequestParam(required = false) String nombre,
        @RequestParam(required = false) String apellido,
        @RequestParam(required = false) Boolean estaVigente
    ) {
        List<AutorResponseDTO> response = this.autorService.getAutores(ids, nombre, apellido, estaVigente);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<AutorResponseDTO> guardarAutor(@RequestBody AutorRequestDTO autorDTO) {
        AutorResponseDTO response = this.autorService.createAutor(autorDTO);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<AutorResponseDTO> editarAutor(@PathVariable Long id, @RequestBody AutorRequestDTO autorDTO) {
        AutorResponseDTO response = this.autorService.editAutor(id, autorDTO);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarAutor(@PathVariable Long id) {
        this.autorService.deleteAutor(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/productos/{id}")
    public ResponseEntity<Void> agregarProductos(@PathVariable Long id, @RequestBody Set<Long> idsProductos) {
        this.autorService.addProductos(id, idsProductos);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/productos/{id}")
    public ResponseEntity<Void> eliminarProductos(@PathVariable Long id, @RequestBody Set<Long> idsProductos) {
        this.autorService.deleteProductos(id, idsProductos);
        return ResponseEntity.noContent().build();
    }
}
