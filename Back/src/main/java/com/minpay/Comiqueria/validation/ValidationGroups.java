package com.minpay.Comiqueria.validation;

/**
 * <p>Esta interfaz de marcador define grupos de validación para ser utilizados con
 * la API de Bean Validation (JSR 380) en Spring Boot.</p>
 *
 * <p>Los grupos de validación permiten aplicar diferentes conjuntos de reglas de validación
 * a un mismo objeto DTO (Data Transfer Object) o entidad, dependiendo del contexto
 * de la operación (por ejemplo, creación o actualización).</p>
 *
 * <p>Al definir interfaces vacías, como {@code OnCreate} y {@code OnUpdate},
 * podemos usar la anotación {@code @Validated} en los controladores o servicios
 * para especificar qué grupo de validación debe ser aplicado. Esto es útil
 * para escenarios donde ciertos campos son obligatorios al crear un recurso,
 * pero opcionales o inmutables al actualizarlo.</p>
 *
 * <h3>Ejemplo de uso en un DTO:</h3>
 * <pre>{@code
 * public class MyDto {
 * @NotNull(groups = ValidationGroups.OnCreate.class)
 * private String requiredOnCreateField;
 *
 * @Size(min = 5, groups = {ValidationGroups.OnCreate.class, ValidationGroups.OnUpdate.class})
 * private String commonValidationField;
 *
 * // ... otros campos
 * }
 * }</pre>
 *
 * <h3>Ejemplo de uso en un controlador Spring:</h3>
 * <pre>{@code
 * @RestController
 * @RequestMapping("/api/resource")
 * public class MyController {
 *
 * @PostMapping
 * public ResponseEntity<MyDto> createResource(@Validated(ValidationGroups.OnCreate.class) @RequestBody MyDto dto) {
 * // ... lógica para crear
 * return ResponseEntity.ok(dto);
 * }
 *
 * @PutMapping("/{id}")
 * public ResponseEntity<MyDto> updateResource(@PathVariable Long id, @Validated(ValidationGroups.OnUpdate.class) @RequestBody MyDto dto) {
 * // ... lógica para actualizar
 * return ResponseEntity.ok(dto);
 * }
 * }
 * }</pre>
 */
public interface ValidationGroups {
    /**
     * Grupo de validación para operaciones de creación de recursos.
     * Los campos anotados con {@code groups = ValidationGroups.OnCreate.class}
     * serán validados cuando este grupo sea especificado.
     */
    public interface OnCreate {}
    
    /**
     * Grupo de validación para operaciones de actualización de recursos.
     * Los campos anotados con {@code groups = ValidationGroups.OnUpdate.class}
     * serán validados cuando este grupo sea especificado.
     */
    public interface OnUpdate {}
}
