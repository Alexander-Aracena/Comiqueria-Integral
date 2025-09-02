package com.minpay.Comiqueria.service;

import com.minpay.Comiqueria.dto.AutorRequestDTO;
import com.minpay.Comiqueria.dto.AutorResponseDTO;
import com.minpay.Comiqueria.exceptions.InvalidOperationException;
import com.minpay.Comiqueria.mapper.IAutorMapper;
import com.minpay.Comiqueria.service.interfaces.IAutorService;
import com.minpay.Comiqueria.model.Autor;
import com.minpay.Comiqueria.model.Producto;
import com.minpay.Comiqueria.repository.IAutorRepository;
import com.minpay.Comiqueria.repository.IProductoRepository;
import com.minpay.Comiqueria.repository.specification.AutorSpecifications;
import com.minpay.Comiqueria.utils.Utils;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AutorService implements IAutorService {
    
    @Autowired
    private IAutorRepository autorRepository;
    
    @Autowired
    private IProductoRepository productoRepository;
    
    @Autowired
    private IAutorMapper autorMapper;
    
    @Override
    @Transactional(readOnly = true)
    public AutorResponseDTO getAutor(Long id) {
        Autor autor = Utils.findByIdOrThrow(autorRepository, id, Autor.class);
        return this.autorMapper.toAutorResponseDTO(autor);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AutorResponseDTO> getAutores(List<Long> ids, String nombre, String apellido, Boolean estaVigente) {
        Specification<Autor> spec = AutorSpecifications.byCriterios(ids, nombre, apellido, estaVigente);
        List<Autor> autores = this.autorRepository.findAll(spec);
        return Utils.mapearListaA(autores, autorMapper::toAutorResponseDTO);
    }

    @Override
    public AutorResponseDTO createAutor(AutorRequestDTO autorDTO) {
        Autor autor = this.autorMapper.toAutor(autorDTO);
        autor = this.autorRepository.save(autor);
        return this.autorMapper.toAutorResponseDTO(autor);
    }

    @Override
    public AutorResponseDTO editAutor(Long id, AutorRequestDTO autorDTO) {
        Autor autorModificado = Utils.findByIdOrThrow(autorRepository, id, Autor.class);
        this.autorMapper.updateAutorFromDTO(autorDTO, autorModificado);
        autorModificado = this.autorRepository.save(autorModificado);
        return this.autorMapper.toAutorResponseDTO(autorModificado);
    }

    @Override
    public void deleteAutor(Long id) {
        Autor autor = Utils.findByIdOrThrow(autorRepository, id, Autor.class);
        if (autor.getFechaBaja() != null) {
            throw new InvalidOperationException("El autor ya está dado de baja.");
        }
        autor.setFechaBaja(LocalDate.now());
        autor.setEstaVigente(Boolean.FALSE);
        this.autorRepository.save(autor);
    }
    
    @Override
    public void addProductos(Long idAutor, Set<Long> idsProductos) {
        Autor autor = Utils.findByIdOrThrow(autorRepository, idAutor, Autor.class);
        List<Producto> productos = this.productoRepository.findAllById(idsProductos);
        if (!autor.getEstaVigente()) {
            throw new InvalidOperationException("El autor ya está dado de baja.");
        }
        Set<Producto> productosOrdenados = Utils.ordenarPorIds(idsProductos, productos, Producto::getId);
        autor.getProductos().addAll(productosOrdenados);
        productos.forEach(producto -> producto.getAutores().add(autor));
        this.autorRepository.save(autor);
    }
    
    @Override
    public void deleteProductos(Long idAutor, Set<Long> idsProductos) {
        Autor autor = Utils.findByIdOrThrow(autorRepository, idAutor, Autor.class);
        List<Producto> productos = this.productoRepository.findAllById(idsProductos);
        if (!autor.getEstaVigente()) {
            throw new InvalidOperationException("El autor ya está dado de baja.");
        }
        autor.getProductos().removeAll(productos);
        productos.forEach(producto -> producto.getAutores().remove(autor));
        this.autorRepository.save(autor);
    }
}