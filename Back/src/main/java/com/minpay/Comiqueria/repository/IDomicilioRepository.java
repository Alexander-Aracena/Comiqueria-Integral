package com.minpay.Comiqueria.repository;

import com.minpay.Comiqueria.model.Domicilio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface IDomicilioRepository extends JpaRepository<Domicilio, Long>,
    JpaSpecificationExecutor<Domicilio> {
    
}
