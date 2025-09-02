package com.minpay.Comiqueria.controller;

import com.minpay.Comiqueria.dto.EditorialRequestDTO;
import com.minpay.Comiqueria.dto.EditorialResponseDTO;
import com.minpay.Comiqueria.service.interfaces.IEditorialService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/editoriales")
public class EditorialController {

    @Autowired
    private IEditorialService editorialService;

    @GetMapping("/{id}")
    public ResponseEntity<EditorialResponseDTO> traerEditorial(@PathVariable Long id) {
        EditorialResponseDTO response = this.editorialService.getEditorial(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<EditorialResponseDTO>> traerEditoriales(
        @RequestParam(required = false) List<Long> ids,
        @RequestParam(required = false) String nombre,
        @RequestParam(required = false) Boolean estaVigente
    ) {
        List<EditorialResponseDTO> response = this.editorialService.getEditoriales(ids, nombre, estaVigente);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<EditorialResponseDTO> guardarEditorial(@RequestBody EditorialRequestDTO dto) {
        EditorialResponseDTO response = this.editorialService.createEditorial(dto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<EditorialResponseDTO> editarEditorial(
        @PathVariable Long id,
        @RequestBody EditorialRequestDTO dto
    ) {
        EditorialResponseDTO response = this.editorialService.editEditorial(id, dto);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarEditorial(@PathVariable Long id) {
        this.editorialService.deleteEditorial(id);
        return ResponseEntity.noContent().build();
    }
}
