package com.minpay.Comiqueria.repository.specification;

import com.minpay.Comiqueria.model.EstadoVenta;
import com.minpay.Comiqueria.model.Venta;
import jakarta.persistence.criteria.Predicate;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.jpa.domain.Specification;

public class VentaSpecifications {
    public static Specification<Venta> byCriterios(List<Long> ids, LocalDateTime minFechaVenta, 
        LocalDateTime maxFechaVenta, BigDecimal minTotal, BigDecimal maxTotal, Long idCliente,
        EstadoVenta estado) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (ids != null && !ids.isEmpty()) {
                predicates.add(root.get("id").in(ids));
            }
            if (minFechaVenta != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("fechaVenta"), minFechaVenta));
            }
            if (maxFechaVenta != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("fechaVenta"), maxFechaVenta));
            }
            if (minTotal != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("total"), minTotal));
            }
            if (maxTotal != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("total"), maxTotal));
            }
            if (idCliente != null) {
                predicates.add(criteriaBuilder.equal(root.get("cliente").get("id"), idCliente));
            }
            if (estado != null) {
                predicates.add(criteriaBuilder.equal(root.get("estado"), estado));
            }
            return criteriaBuilder.and(predicates.toArray(Predicate[]::new));
        };
    }
}
