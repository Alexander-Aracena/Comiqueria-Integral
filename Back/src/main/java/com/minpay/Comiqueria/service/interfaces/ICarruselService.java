package com.minpay.Comiqueria.service.interfaces;

import com.minpay.Comiqueria.dto.CarruselRequestDTO;
import com.minpay.Comiqueria.dto.CarruselResponseDTO;
import java.util.List;

public interface ICarruselService {
    public CarruselResponseDTO getCarrusel(Long id);
    public List<CarruselResponseDTO> getCarruseles(List<Long> ids, String subtitulo, String texto, Boolean estaActivo);
    public CarruselResponseDTO createCarrusel(CarruselRequestDTO carouselDTO);
    public CarruselResponseDTO editCarrusel(Long id, CarruselRequestDTO carouselDTO);
    public void deleteCarrusel(Long id);
}