package com.minpay.Comiqueria.repository;

import com.minpay.Comiqueria.model.Carrusel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ICarruselRepository extends JpaRepository<Carrusel, Long>,
    JpaSpecificationExecutor<Carrusel> {
    
}
