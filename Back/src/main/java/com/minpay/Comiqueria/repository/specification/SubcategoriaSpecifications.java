package com.minpay.Comiqueria.repository.specification;

import com.minpay.Comiqueria.model.Subcategoria;
import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.jpa.domain.Specification;

public class SubcategoriaSpecifications {
    public static Specification<Subcategoria> byCriterios(List<Long> ids, String nombre, Long idCategoria,
        Boolean estaVigente) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (ids != null && !ids.isEmpty()) {
                predicates.add(root.get("id").in(ids));
            }
            if (nombre != null && !nombre.trim().isEmpty()) {
                predicates.add(
                    criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("nombre")), "%" + nombre.toLowerCase() + "%"
                    )
                );
            }
            if (idCategoria != null) {
                predicates.add(criteriaBuilder.equal(root.get("categoria").get("id"), idCategoria));
            }
            if (estaVigente != null) {
                predicates.add(criteriaBuilder.equal(root.get("estaVigente"), estaVigente));
            }
            return criteriaBuilder.and(predicates.toArray(Predicate[]::new));
        };
    }
}
