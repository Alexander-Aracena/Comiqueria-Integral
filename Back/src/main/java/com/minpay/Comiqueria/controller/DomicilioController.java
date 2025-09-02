package com.minpay.Comiqueria.controller;

import com.minpay.Comiqueria.dto.DomicilioRequestDTO;
import com.minpay.Comiqueria.dto.DomicilioResponseDTO;
import com.minpay.Comiqueria.service.interfaces.IDomicilioService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/domicilios")
public class DomicilioController {

    @Autowired
    private IDomicilioService domicilioService;

    @GetMapping("/{id}")
    public ResponseEntity<DomicilioResponseDTO> traerDomicilio(@PathVariable Long id) {
        DomicilioResponseDTO response = this.domicilioService.getDomicilio(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<DomicilioResponseDTO>> traerDomicilios(
        @RequestParam(required = false) List<Long> ids,
        @RequestParam(required = false) String calle,
        @RequestParam(required = false) String cp,
        @RequestParam(required = false) Boolean estaVigente
    ) {
        List<DomicilioResponseDTO> response = this.domicilioService.getDomicilios(ids, calle, cp, estaVigente);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<DomicilioResponseDTO> guardarDomicilio(@RequestBody DomicilioRequestDTO dto) {
        DomicilioResponseDTO response = this.domicilioService.createDomicilio(dto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<DomicilioResponseDTO> editarDomicilio(
        @PathVariable Long id,
        @RequestBody DomicilioRequestDTO dto
    ) {
        DomicilioResponseDTO response = this.domicilioService.editDomicilio(id, dto);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarDomicilio(@PathVariable Long id) {
        this.domicilioService.deleteDomicilio(id);
        return ResponseEntity.noContent().build();
    }
}
