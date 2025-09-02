package com.minpay.Comiqueria.repository;

import com.minpay.Comiqueria.model.Editorial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface IEditorialRepository extends JpaRepository<Editorial, Long>,
    JpaSpecificationExecutor<Editorial>{
    
}
