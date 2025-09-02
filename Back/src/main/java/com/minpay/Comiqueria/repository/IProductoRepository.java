package com.minpay.Comiqueria.repository;

import com.minpay.Comiqueria.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface IProductoRepository extends JpaRepository<Producto, Long>,
    JpaSpecificationExecutor<Producto> {
    
}
