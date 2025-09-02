package com.minpay.Comiqueria.repository.specification;

import com.minpay.Comiqueria.model.Localidad;
import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.jpa.domain.Specification;

public class LocalidadSpecifications {
    public static Specification<Localidad> byCriterios(List<Long> ids, String nombre, Long idDepartamento,
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

            if (idDepartamento != null) {
                predicates.add(criteriaBuilder.equal(root.get("departamento").get("id"), idDepartamento));
            }

            if (estaVigente != null) {
                predicates.add(criteriaBuilder.equal(root.get("estaVigente"), estaVigente));
            }
            
            return criteriaBuilder.and(predicates.toArray(Predicate[]::new));
        };
    }
}
