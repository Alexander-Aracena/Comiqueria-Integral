package com.minpay.Comiqueria.repository;

import com.minpay.Comiqueria.model.Venta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface IVentaRepository extends JpaRepository<Venta, Long>,
    JpaSpecificationExecutor<Venta>{
    
}
