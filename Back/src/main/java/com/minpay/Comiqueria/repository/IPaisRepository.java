package com.minpay.Comiqueria.repository;

import com.minpay.Comiqueria.model.Pais;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface IPaisRepository extends JpaRepository<Pais, Long>,
    JpaSpecificationExecutor<Pais>{
    
}
