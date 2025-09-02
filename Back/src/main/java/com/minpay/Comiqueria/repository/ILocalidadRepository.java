package com.minpay.Comiqueria.repository;

import com.minpay.Comiqueria.model.Localidad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ILocalidadRepository extends JpaRepository<Localidad, Long>,
    JpaSpecificationExecutor<Localidad> {
    
}
