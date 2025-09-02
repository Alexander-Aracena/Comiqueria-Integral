package com.minpay.Comiqueria;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import static com.minpay.Comiqueria.utils.TestsUtils.*;
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
public class EditorialTest {
    @Autowired
    private MockMvc mockMvc;
    
    private static Long idEditorial;
    private static String editorialResponse;
    
    @BeforeAll
    static void setup() {
        idEditorial = null;
        editorialResponse = "";
    }
    
    @BeforeEach
    void ensureEditorialExists() throws Exception {
        if(idEditorial == null) {
            String nombreEditorial = "OVNI PRESS DC";
            editorialResponse = crearMediantePost(
                mockMvc,
                "/editoriales",
                "nombre",
                nombreEditorial,
                MediaType.TEXT_PLAIN
            );
            idEditorial = extraerIdDeResponse(editorialResponse);
        }
    }
    
    @Test
    @Order(1)
    void shouldReturnAEditorial() throws Exception {
        mockMvc.perform(get("/editoriales/" + idEditorial))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(idEditorial))
                .andExpect(jsonPath("$.nombre").value("OVNI PRESS DC"));
    }
    
    @Test
    @Order(2)
    void shouldReturnAllEditoriales() throws Exception {
        mockMvc.perform(get("/editoriales"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[" + (idEditorial - 1) + "].id").value(idEditorial))
                .andExpect(jsonPath("$[" + (idEditorial - 1) + "].nombre").value("OVNI PRESS DC"));
    }
    
    @Test
    @Order(3)
    void shouldCreateAndReturnEditorial() throws Exception {
        // Crea un JSON representando un autor
        String nombreEditorial = "OVNI PRESS MARVEL";

        // Realiza una petición POST al controlador
        mockMvc.perform(post("/editoriales")
                .param("nombre", nombreEditorial)
                .contentType(MediaType.TEXT_PLAIN))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.nombre").value("OVNI PRESS MARVEL"));
    }

    @Test
    @Order(4)
    void shouldEditAndReturnEditorial() throws Exception {
        String editorialCorregidoJson = "UTOPIA EDITORIAL";

        mockMvc.perform(patch("/editoriales/" + idEditorial)
                .param("nombre", editorialCorregidoJson)
                .contentType(MediaType.TEXT_PLAIN))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.nombre").value("UTOPIA EDITORIAL"));
    }

    @Test
    @Order(5)
    void shouldDeleteAEditorial() throws Exception {
        mockMvc.perform(delete("/editoriales/" + idEditorial))
                .andExpect(status().isAccepted());

        // Intenta obtener la editorial eliminada y verifica el 404
        mockMvc.perform(get("/editoriales/" + idEditorial))
                .andExpect(status().isNotFound());
    }
}
