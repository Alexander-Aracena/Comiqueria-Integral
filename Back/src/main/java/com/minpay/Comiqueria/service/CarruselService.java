package com.minpay.Comiqueria.service;

import com.minpay.Comiqueria.dto.CarruselRequestDTO;
import com.minpay.Comiqueria.dto.CarruselResponseDTO;
import com.minpay.Comiqueria.exceptions.InvalidOperationException;
import com.minpay.Comiqueria.mapper.ICarruselMapper;
import com.minpay.Comiqueria.model.Carrusel;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.minpay.Comiqueria.repository.ICarruselRepository;
import com.minpay.Comiqueria.repository.specification.CarruselSpecifications;
import com.minpay.Comiqueria.service.interfaces.ICarruselService;
import com.minpay.Comiqueria.utils.Utils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CarruselService implements ICarruselService {
    
    @Autowired
    private ICarruselRepository carruselRepository;
    
    @Autowired
    private ICarruselMapper carruselMapper;

    @Override
    @Transactional(readOnly = true)
    public CarruselResponseDTO getCarrusel(Long id) {
        Carrusel carrusel = Utils.findByIdOrThrow(carruselRepository, id, Carrusel.class);
        return this.carruselMapper.toCarruselResponseDTO(carrusel);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CarruselResponseDTO> getCarruseles(
            List<Long> ids,
            String subtitulo,
            String texto,
            Boolean estaActivo
    ) {
        Specification<Carrusel> specs = CarruselSpecifications.byCriterios(ids, subtitulo, texto, estaActivo);
        List<Carrusel> carruseles = this.carruselRepository.findAll(specs);
        return Utils.mapearListaA(carruseles, carruselMapper::toCarruselResponseDTO);
    }

    @Override
    public CarruselResponseDTO createCarrusel(CarruselRequestDTO carouselDTO) {
        Carrusel carrusel = this.carruselMapper.toCarrusel(carouselDTO);
        carrusel = this.carruselRepository.save(carrusel);
        return this.carruselMapper.toCarruselResponseDTO(carrusel);
    }

    @Override
    public CarruselResponseDTO editCarrusel(Long id, CarruselRequestDTO carouselDTO) {
        Carrusel carruselModificado = Utils.findByIdOrThrow(carruselRepository, id, Carrusel.class);
        this.carruselMapper.updateCarruselFromDTO(carouselDTO, carruselModificado);
        carruselModificado = this.carruselMapper.toCarrusel(carouselDTO);
        return this.carruselMapper.toCarruselResponseDTO(carruselModificado);
    }

    @Override
    public void deleteCarrusel(Long id) {
        Carrusel carrusel = Utils.findByIdOrThrow(carruselRepository, id, Carrusel.class);
        if (!carrusel.getEstaActivo()) {
            throw new InvalidOperationException("El carrusel ya está dado de baja.");
        }
        carrusel.setEstaActivo(Boolean.FALSE);
        this.carruselRepository.save(carrusel);
    }
}