package com.minpay.Comiqueria.repository.specification;

import com.minpay.Comiqueria.model.Departamento;
import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.jpa.domain.Specification;

public class DepartamentoSpecifications {
    public static Specification<Departamento> byCriterios(
        List<Long> ids,
        String nombre,
        Long idProvincia,
        String nombreProvincia,
        Long idPais,
        String nombrePais,
        Boolean estaVigente
    ) {
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
            if (idProvincia != null) {
                predicates.add(criteriaBuilder.equal(root.get("provincia").get("id"), idProvincia));
            }
            if (nombreProvincia != null && !nombreProvincia.trim().isEmpty()) {
                predicates.add(
                    criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("provincia").get("nombre")), "%" + nombreProvincia.toLowerCase() + "%"
                    )
                );
            }
            if (idPais != null) {
                predicates.add(criteriaBuilder.equal(root.get("provincia").get("pais").get("id"), idPais));
            }
            if (nombrePais != null && !nombrePais.trim().isEmpty()) {
                predicates.add(
                    criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("provincia").get("pais").get("nombre")), "%" + nombrePais.toLowerCase() + "%"
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