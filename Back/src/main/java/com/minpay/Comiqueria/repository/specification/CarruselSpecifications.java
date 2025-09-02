package com.minpay.Comiqueria.repository.specification;

import com.minpay.Comiqueria.model.Carrusel;
import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.jpa.domain.Specification;

public class CarruselSpecifications {
    public static Specification<Carrusel> byCriterios(List<Long> ids, String subtitulo, String texto,
        Boolean estaActivo) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (ids != null && !ids.isEmpty()) {
                predicates.add(root.get("id").in(ids));
            }
            if (subtitulo != null && !subtitulo.trim().isEmpty()) {
                predicates.add(
                    criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("subtitulo")), "%" + subtitulo.toLowerCase() + "%"
                    )
                );
            }
            if (texto != null && !texto.trim().isEmpty()) {
                predicates.add(
                    criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("texto")), "%" + texto.toLowerCase() + "%"
                    )
                );
            }
            if (estaActivo != null) {
                predicates.add(criteriaBuilder.equal(root.get("estaActivo"), estaActivo));
            }
            return criteriaBuilder.and(predicates.toArray(Predicate[]::new));
        };
    }
}
