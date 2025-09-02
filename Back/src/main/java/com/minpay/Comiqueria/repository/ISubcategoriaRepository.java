package com.minpay.Comiqueria.repository;

import com.minpay.Comiqueria.model.Subcategoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ISubcategoriaRepository extends JpaRepository<Subcategoria, Long>,
    JpaSpecificationExecutor<Subcategoria> {
    
}
