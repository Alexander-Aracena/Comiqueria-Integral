package com.minpay.Comiqueria.controller;

import com.minpay.Comiqueria.dto.CarruselRequestDTO;
import com.minpay.Comiqueria.dto.CarruselResponseDTO;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import com.minpay.Comiqueria.service.interfaces.ICarruselService;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/api/carrusel")
public class CarruselController {

    @Autowired
    private ICarruselService carruselService;

    @GetMapping("/{id}")
    public ResponseEntity<CarruselResponseDTO> traerCarrusel(@PathVariable Long id) {
        CarruselResponseDTO response = this.carruselService.getCarrusel(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<CarruselResponseDTO>> traerCarruseles(
        @RequestParam(required = false) List<Long> ids,
        @RequestParam(required = false) String subtitulo,
        @RequestParam(required = false) String texto,
        @RequestParam(required = false) Boolean estaActivo
    ) {
        List<CarruselResponseDTO> response = this.carruselService.getCarruseles(ids, subtitulo, texto, estaActivo);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<CarruselResponseDTO> guardarCarrusel(@RequestBody CarruselRequestDTO dto) {
        CarruselResponseDTO response = this.carruselService.createCarrusel(dto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<CarruselResponseDTO> editarCarrusel(
        @PathVariable Long id,
        @RequestBody CarruselRequestDTO dto
    ) {
        CarruselResponseDTO response = this.carruselService.editCarrusel(id, dto);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarCarrusel(@PathVariable Long id) {
        this.carruselService.deleteCarrusel(id);
        return ResponseEntity.noContent().build();
    }
}
