package com.minpay.Comiqueria.controller;

import com.minpay.Comiqueria.dto.ProductoRequestDTO;
import com.minpay.Comiqueria.dto.ProductoResponseDTO;
import com.minpay.Comiqueria.service.interfaces.IProductoService;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    @Autowired
    private IProductoService productoService;

    @GetMapping("/{id}")
    public ResponseEntity<ProductoResponseDTO> traerProducto(@PathVariable Long id) {
        ProductoResponseDTO response = this.productoService.getProducto(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<ProductoResponseDTO>> traerProductos(
        @RequestParam(required = false) List<Long> idsProductos,
        @RequestParam(required = false) String titulo,
        @RequestParam(required = false) BigDecimal minPrecio,
        @RequestParam(required = false) BigDecimal maxPrecio,
        @RequestParam(required = false) String descripcion,
        @RequestParam(required = false) Long idAutor,
        @RequestParam(required = false) Long idSubcategoria,
        @RequestParam(required = false) Long idEditorial,
        @RequestParam(required = false) Boolean esNovedad,
        @RequestParam(required = false) Boolean esOferta,
        @RequestParam(required = false) Boolean esMasVendido,
        @RequestParam(required = false) Boolean esVisibleEnHome,
        @RequestParam(required = false) Boolean estaVigente
    ) {
        List<ProductoResponseDTO> response = this.productoService.getProductos(
            idsProductos, titulo, minPrecio, maxPrecio, descripcion, idAutor, idSubcategoria,
            idEditorial, esNovedad, esOferta, esMasVendido, esVisibleEnHome, estaVigente
        );
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<ProductoResponseDTO> guardarProducto(@RequestBody ProductoRequestDTO dto) {
        ProductoResponseDTO response = this.productoService.createProducto(dto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ProductoResponseDTO> editarProducto(
        @PathVariable Long id,
        @RequestBody ProductoRequestDTO dto
    ) {
        ProductoResponseDTO response = this.productoService.editProducto(id, dto);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarProducto(@PathVariable Long id) {
        this.productoService.deleteProducto(id);
        return ResponseEntity.noContent().build();
    }
}
