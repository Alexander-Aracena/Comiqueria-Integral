package com.minpay.Comiqueria.repository;

import com.minpay.Comiqueria.model.Departamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface IDepartamentoRepository extends JpaRepository<Departamento, Long>,
    JpaSpecificationExecutor<Departamento> {
    
}
