package com.minpay.Comiqueria.repository;

import com.minpay.Comiqueria.model.LineaVenta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ILineaVentaRepository extends JpaRepository<LineaVenta, Long>,
    JpaSpecificationExecutor<LineaVenta>{
    
}
