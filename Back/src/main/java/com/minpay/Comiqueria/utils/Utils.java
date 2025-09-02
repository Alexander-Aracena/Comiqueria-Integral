package com.minpay.Comiqueria.utils;

import com.minpay.Comiqueria.exceptions.ResourceNotFoundException;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.data.jpa.repository.JpaRepository;

public class Utils {
    
    /**
     * Busca una entidad por su ID utilizando un repositorio de JPA. Si la entidad no se encuentra,
     * lanza una {@link ResourceNotFoundException} con un mensaje descriptivo.
     * Este método simplifica la lógica de verificación de existencia en los servicios.
     *
     * @param repository El {@link JpaRepository} a utilizar para la búsqueda de la entidad.
     * @param id El identificador único de la entidad que se desea buscar.
     * @param entityClass La clase de la entidad (ej. Usuario, Clase) que se incluirá en el mensaje
     * de la excepción si la entidad no es encontrada, para mayor claridad.
     * @param <T> El tipo de la entidad que se está buscando (ej. {@code Usuario}, {@code Clase}).
     * @return La entidad encontrada, del tipo {@code T}.
     * @throws ResourceNotFoundException Si la entidad con el ID dado no se encuentra en el repositorio.
     */
    public static <T> T findByIdOrThrow(JpaRepository<T, Long> repository, Long id, Class<T> entityClass) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        entityClass.getSimpleName() + " no encontrado con ID: " + id)
                );
    }
    
    /**
     * Mapea una lista de entidades de un tipo (E) a una lista de DTOs de otro tipo (D)
     * aplicando una función de mapeo proporcionada. Este método es útil para transformar
     * colecciones de entidades de la capa de persistencia a colecciones de DTOs
     * para la capa de presentación o de servicio, de manera genérica.
     *
     * @param entities La {@link List} de entidades de origen (tipo {@code E}) a ser mapeadas.
     * Puede ser {@code null}, en cuyo caso el método también retornará {@code null}.
     * @param mapperFunction La {@link Function} que define cómo cada entidad individual
     * (tipo {@code E}) se transforma en un DTO (tipo {@code D}).
     * Por ejemplo, una referencia a un método de mapper como {@code usuarioMapper::toUsuarioResponseDTO}.
     * @param <E> El tipo de la entidad de origen (ej. {@code Usuario}, {@code Clase}).
     * @param <D> El tipo del DTO de destino (ej. {@code UsuarioResponseDTO}, {@code ClaseDTO}).
     * @return Una nueva {@link List} de DTOs (tipo {@code D}) resultante del mapeo.
     * Retorna {@code null} si la lista de entidades de entrada es {@code null}.
     */
    public static <E, D> List<D> mapearListaA(List<E> entities, Function<E, D> mapperFunction) {
        if (entities == null) {
            return null;
        }
        return entities.stream()
                       .map(mapperFunction)
                       .collect(Collectors.toList());
    }

    public static <T, R> Set<R> mapearSetA(Set<T> entidad, Function<T, R> mapperFunction) {
        if (entidad == null) {
            return null;
        }
        return entidad.stream().map(mapperFunction).collect(Collectors.toSet());
    }
    
    public static <T> Set<T> findAllById(JpaRepository<T, Long> repository, Set<Long> ids) {
        return convertirListaASet(repository.findAllById(ids));
    }

    public static <T> Set<T> convertirListaASet(List<T> entidad) {
        return entidad.stream().collect(Collectors.toSet());
    }

    public static <T> Set<T> ordenarPorIds(Set<Long> ids, List<T> entities, Function<T, Long> getIdFunction) {
        // Convertimos la lista de entidades a un Map de ID -> Entidad
        Map<Long, T> entityMap = entities.stream()
            .collect(Collectors.toMap(getIdFunction, Function.identity()));

        // Creamos un Set respetando el orden de los IDs originales
        return ids.stream()
            .map(entityMap::get)
            .filter(Objects::nonNull) // Para evitar nulls si algún ID no existe
            .collect(Collectors.toCollection(LinkedHashSet::new));
    }
}
