package com.minpay.Comiqueria.repository.specification;

import com.minpay.Comiqueria.model.Domicilio;
import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.jpa.domain.Specification;

public class DomicilioSpecifications {
    public static Specification<Domicilio> byCriterios(List<Long> ids, String calle, String cp,
        Boolean estaVigente) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (ids != null && !ids.isEmpty()) {
                predicates.add(root.get("id").in(ids));
            }
            if (calle != null && !calle.trim().isEmpty()) {
                predicates.add(
                    criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("calle")), "%" + calle.toLowerCase() + "%"
                    )
                );
            }
            if (cp != null && !cp.trim().isEmpty()) {
                predicates.add(
                    criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("cp")), "%" + cp.toLowerCase() + "%"
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
