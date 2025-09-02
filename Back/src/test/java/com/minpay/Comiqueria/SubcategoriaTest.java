package com.minpay.Comiqueria;

import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import static com.minpay.Comiqueria.utils.TestsUtils.*;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.http.MediaType;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("classpath:application-test.properties")
@TestMethodOrder(OrderAnnotation.class)
public class SubcategoriaTest {

    @Autowired
    private MockMvc mockMvc;

    private static Long idCategoria;
    private static Long idSubcategoria;
    private static String categoriaResponse;

    @BeforeAll
    public static void setup() {
        idCategoria = null;
        idSubcategoria = null;
    }

    @BeforeEach
    public void ensureSubcategoriaExists() throws Exception {
        if (idCategoria == null) {
            String nombreCategoria = "COMICS";
            categoriaResponse = crearMediantePost(mockMvc, "/categorias", nombreCategoria,
                MediaType.TEXT_PLAIN);
            idCategoria = extraerIdDeResponse(categoriaResponse);
        }

        if (idSubcategoria == null) {
            String nombreSubcategoria = "USA";

            Map<String, Object> opcionales = new HashMap<>();
            opcionales.put("idCategoria", idCategoria);

            idSubcategoria = Long.valueOf(
                JsonPath.read(crearMediantePost(
                    mockMvc,
                    "/subcategorias",
                    "nombreSubcategoria",
                    nombreSubcategoria,
                    opcionales,
                    MediaType.APPLICATION_JSON
                ), "$.idSubcategoria"
                ).toString());
        }
    }

    @Test
    @Order(1)
    void shouldReturnASubcategoria() throws Exception {
        mockMvc.perform(get("/subcategorias/" + idSubcategoria))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.idSubcategoria").value(idSubcategoria))
            .andExpect(jsonPath("$.nombreSubcategoria").value("USA"));
    }

    @Test
    @Order(2)
    void shouldReturnAllSubcategorias() throws Exception {
        mockMvc.perform(get("/subcategorias"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$[" + (idSubcategoria - 1) + "].idSubcategoria").value(idSubcategoria))
            .andExpect(jsonPath("$[" + (idSubcategoria - 1) + "].nombreSubcategoria").value("USA"));
    }

    @Test
    @Order(3)
    void shouldCreateAndReturnSubcategoria() throws Exception {
        // Crea un JSON representando un autor
        String nombreSubcategoria = "SHONEN";

        // Realiza una petición POST al controlador
        mockMvc.perform(post("/subcategorias")
            .param("nombreSubcategoria", nombreSubcategoria)
            .param("idCategoria", String.valueOf(idCategoria))
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.idSubcategoria").exists())
            .andExpect(jsonPath("$.nombreSubcategoria").value("SHONEN"));
    }

    @Test
    @Order(4)
    void shouldEditAndReturnSubcategoria() throws Exception {
        String nombreSubcategoria = "NACIONAL";

        mockMvc.perform(patch("/subcategorias/" + idSubcategoria)
            .param("nombreSubcategoria", nombreSubcategoria)
            .param("idCategoria", String.valueOf(idCategoria))
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isAccepted())
            .andExpect(jsonPath("$.nombreSubcategoria").value("NACIONAL"));
    }

    @Test
    @Order(5)
    void shouldDeleteASubcategoria() throws Exception {
        mockMvc.perform(delete("/subcategorias/" + idSubcategoria))
            .andExpect(status().isAccepted());

        // Intenta obtener la pais eliminada y verifica el 404
        mockMvc.perform(get("/subcategorias/" + idSubcategoria))
            .andExpect(status().isNotFound());
    }
}
