package com.minpay.Comiqueria.repository.specification;

import com.minpay.Comiqueria.model.Autor;
import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.jpa.domain.Specification;

public class AutorSpecifications {
    public static Specification<Autor> byCriterios(List<Long> ids, String nombre, String apellido,
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
            if (apellido != null && !apellido.trim().isEmpty()) {
                predicates.add(
                    criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("apellido")), "%" + apellido.toLowerCase() + "%"
                    )
                );
            }
            if (estaVigente != null) {
                predicates.add(criteriaBuilder.equal(root.get("estaVigente"), estaVigente));
            }

            return criteriaBuilder.and(predicates.toArray(Predicate[]::new));
        };
    }
}
