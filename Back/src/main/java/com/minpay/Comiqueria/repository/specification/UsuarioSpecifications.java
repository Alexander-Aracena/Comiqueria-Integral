package com.minpay.Comiqueria.repository.specification;

import com.minpay.Comiqueria.model.Rol;
import com.minpay.Comiqueria.model.Usuario;
import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.jpa.domain.Specification;

public class UsuarioSpecifications {
    public static Specification<Usuario> byCriterios(List<Long> ids, String email, Rol rol,
        Long idCliente, Boolean estaActivo) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (ids != null && !ids.isEmpty()) {
                predicates.add(root.get("id").in(ids));
            }
            if (email != null && !email.trim().isEmpty()) {
                predicates.add(
                    criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("email")), "%" + email.toLowerCase() + "%"
                    )
                );
            }
            if (rol != null) {
                predicates.add(criteriaBuilder.equal(root.get("rol"), rol));
            }
            if (idCliente != null) {
                predicates.add(
                    criteriaBuilder.equal(root.get("cliente").get("id"), idCliente)
                );
            }
            if (estaActivo != null) {
                predicates.add(criteriaBuilder.equal(root.get("estaActivo"), estaActivo));
            }
            return criteriaBuilder.and(predicates.toArray(Predicate[]::new));
        };
    }
}