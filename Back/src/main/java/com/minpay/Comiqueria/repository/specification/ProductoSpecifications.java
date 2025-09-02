package com.minpay.Comiqueria.repository.specification;

import com.minpay.Comiqueria.model.Producto;
import jakarta.persistence.criteria.Predicate;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.jpa.domain.Specification;

public class ProductoSpecifications {
    public static Specification<Producto> byCriterios(List<Long> ids, String titulo, BigDecimal minPrecio,
        BigDecimal maxPrecio, String descripcion, Long idAutor, Long idSubcategoria, Long idEditorial,
        Boolean esNovedad, Boolean esOferta, Boolean esMasVendido, Boolean esVisibleEnHome,
        Boolean estaVigente) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (ids != null && !ids.isEmpty()) {
                predicates.add(root.get("id").in(ids));
            }
            if (titulo != null && !titulo.trim().isEmpty()) {
                predicates.add(
                    criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("titulo")), "%" + titulo.toLowerCase() + "%"
                    )
                );
            }
            if (minPrecio != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("precio"), minPrecio));
            }
            if (maxPrecio != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("precio"), maxPrecio));
            }
            if (descripcion != null && !descripcion.trim().isEmpty()) {
                predicates.add(
                    criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("descripcion")),
                        "%" + descripcion.toLowerCase() + "%"
                    )
                );
            }
            if (idAutor != null) {
                predicates.add(criteriaBuilder.equal(root.get("autor").get("id"), idAutor));
            }
            if (idSubcategoria != null) {
                predicates.add(criteriaBuilder.equal(root.get("subcategoria").get("id"), idSubcategoria));
            }
            if (idEditorial != null) {
                predicates.add(criteriaBuilder.equal(root.get("editorial").get("id"), idEditorial));
            }
            if (esNovedad != null) {
                predicates.add(criteriaBuilder.equal(root.get("esNovedad"), esNovedad));
            }
            if (esOferta != null) {
                predicates.add(criteriaBuilder.equal(root.get("esOferta"), esOferta));
            }
            if (esMasVendido != null) {
                predicates.add(criteriaBuilder.equal(root.get("esMasVendido"), esMasVendido));
            }
            if (esVisibleEnHome != null) {
                predicates.add(criteriaBuilder.equal(root.get("esVisibleEnHome"), esVisibleEnHome));
            }
            if (estaVigente != null) {
                predicates.add(criteriaBuilder.equal(root.get("estaVigente"), estaVigente));
            }
            return criteriaBuilder.and(predicates.toArray(Predicate[]::new));
        };
    }
}
