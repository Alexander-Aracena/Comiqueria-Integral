package com.minpay.Comiqueria;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import static com.minpay.Comiqueria.utils.TestsUtils.*;
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
public class CategoriaTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    private static Long idCategoria;
    
    @BeforeEach
    public void ensureCategoriaExists() throws Exception {
        if (idCategoria == null) {
            String nombreCategoria = "COMICS";
            String response = crearMediantePost(mockMvc, "/categorias", nombreCategoria,
                    MediaType.TEXT_PLAIN);
            idCategoria = extraerIdDeResponse(response);
        }
    }
    
    @Test
    @Order(1)
    void shouldReturnACategoria() throws Exception {
        mockMvc.perform(get("/categorias/" + idCategoria))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(idCategoria))
                .andExpect(jsonPath("$.nombre").value("COMICS"));
    }
    
    @Test
    @Order(2)
    void shouldReturnAllCategorias() throws Exception {
        mockMvc.perform(get("/categorias"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[" + (idCategoria - 1) + "].id").value(idCategoria))
                .andExpect(jsonPath("$[" + (idCategoria - 1) + "].nombre").value("COMICS"));
    }
    
    @Test
    @Order(3)
    void shouldCreateAndReturnCategoria() throws Exception {
        String nombreCategoria = "COMICS";

        mockMvc.perform(post("/categorias")
                .contentType(MediaType.TEXT_PLAIN)
                .content(nombreCategoria))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.nombre").value("COMICS"));
    }

    @Test
    @Order(4)
    void shouldEditAndReturnCategoria() throws Exception {
        String categoriaCorregidoJson = "MANGA";

        mockMvc.perform(patch("/categorias/" + idCategoria)
                .contentType(MediaType.TEXT_PLAIN)
                .content(categoriaCorregidoJson))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.nombre").value("MANGA"));
    }

    @Test
    @Order(5)
    void shouldDeleteACategoria() throws Exception {
        mockMvc.perform(delete("/categorias/" + idCategoria))
                .andExpect(status().isAccepted());

        // Intenta obtener la categoria eliminada y verifica el 404
        mockMvc.perform(get("/categorias/" + idCategoria))
                .andExpect(status().isNotFound());
    }
}
