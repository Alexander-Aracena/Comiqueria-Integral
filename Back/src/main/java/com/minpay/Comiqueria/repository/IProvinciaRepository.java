package com.minpay.Comiqueria.repository;

import com.minpay.Comiqueria.model.Provincia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface IProvinciaRepository extends JpaRepository<Provincia, Long>,
    JpaSpecificationExecutor<Provincia>{
    
}
