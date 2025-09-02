package com.minpay.Comiqueria.controller;

import com.minpay.Comiqueria.dto.ClienteResponseDTO;
import com.minpay.Comiqueria.dto.ClienteRequestDTO;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import com.minpay.Comiqueria.service.interfaces.IClienteService;
import java.util.Set;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/api/clientes")
public class ClienteController {

    @Autowired
    private IClienteService clienteService;

    @GetMapping("/{id}")
    public ResponseEntity<ClienteResponseDTO> traerCliente(@PathVariable Long id) {
        ClienteResponseDTO response = this.clienteService.getCliente(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<ClienteResponseDTO>> traerClientes(
        @RequestParam(required = false) List<Long> ids,
        @RequestParam(required = false) String nombre,
        @RequestParam(required = false) String apellido,
        @RequestParam(required = false) String nroDoc,
        @RequestParam(required = false) Boolean estaVigente
    ) {
        List<ClienteResponseDTO> response = this.clienteService.getClientes(ids, nombre, apellido, nroDoc, estaVigente);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<ClienteResponseDTO> guardarCliente(@RequestBody ClienteRequestDTO dto) {
        ClienteResponseDTO response = this.clienteService.createCliente(dto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ClienteResponseDTO> editarCliente(
        @PathVariable Long id,
        @RequestBody ClienteRequestDTO dto
    ) {
        ClienteResponseDTO response = this.clienteService.editCliente(id, dto);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarCliente(@PathVariable Long id) {
        this.clienteService.deleteCliente(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/favoritos")
    public ResponseEntity<Void> agregarFavoritos(@PathVariable Long id, @RequestParam Set<Long> idsProductos) {
        this.clienteService.addFavoritos(id, idsProductos);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}/favoritos")
    public ResponseEntity<Void> eliminarFavoritos(@PathVariable Long id, @RequestParam Set<Long> idsProductos) {
        this.clienteService.deleteFavoritos(id, idsProductos);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/domicilios")
    public ResponseEntity<Void> agregarDomicilio(@PathVariable Long id, @RequestParam Long idDomicilio) {
        this.clienteService.addDomicilio(id, idDomicilio);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}/domicilios")
    public ResponseEntity<Void> eliminarDomicilio(@PathVariable Long id, @RequestParam Long idDomicilio) {
        this.clienteService.deleteDomicilio(id, idDomicilio);
        return ResponseEntity.noContent().build();
    }
}
