package com.minpay.Comiqueria.repository.specification;

import com.minpay.Comiqueria.model.LineaVenta;
import jakarta.persistence.criteria.Predicate;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.jpa.domain.Specification;

public class LineaVentaSpecifications {
    public static Specification<LineaVenta> byCriterios(List<Long> ids, Long idVenta, Long idProducto,
        Integer minCantidad, Integer maxCantidad, BigDecimal minPrecio, BigDecimal maxPrecio) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (ids != null && !ids.isEmpty()) {
                predicates.add(root.get("id").in(ids));
            }

            // Filtro por ID de Venta (relación ManyToOne con Venta)
            if (idVenta != null) {
                predicates.add(criteriaBuilder.equal(root.get("venta").get("id"), idVenta));
            }

            // Filtro por ID de Producto (relación ManyToOne con Producto)
            if (idProducto != null) {
                predicates.add(criteriaBuilder.equal(root.get("producto").get("id"), idProducto));
            }

            // --- Filtro por Rango de Cantidad ---
            if (minCantidad != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("cantidad"), minCantidad));
            }
            if (maxCantidad != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("cantidad"), maxCantidad));
            }

            // --- Filtro por Rango de Precio ---
            if (minPrecio != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("precio"), minPrecio));
            }
            if (maxPrecio != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("precio"), maxPrecio));
            }
            
            return criteriaBuilder.and(predicates.toArray(Predicate[]::new));
        };
    }
}
