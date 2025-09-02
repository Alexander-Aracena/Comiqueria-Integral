package com.minpay.Comiqueria;

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
public class PaisTest {

    @Autowired
    private MockMvc mockMvc;

    private static Long idPais, idProvincia;
    private static String paisResponse, provinciaResponse;

    @BeforeAll
    public static void setup() {
        idPais = null;
    }

    @BeforeEach
    public void ensurePaisExists() throws Exception {
        if (idPais == null) {
            String nombrePais = "ARGENTINA";
            paisResponse = crearMediantePost(
                mockMvc,
                "/paises",
                "nombre",
                nombrePais,
                MediaType.TEXT_PLAIN
            );
            idPais = extraerIdDeResponse(paisResponse);
        }
    }

    private void ensureProvinciaExists() throws Exception {
        if (idProvincia == null) {
            String nombreProvincia = "DURAZNO";
            Map<String, Object> parametrosOpcionales = new HashMap<>();
            parametrosOpcionales.put("idPais", idPais);
            provinciaResponse = crearMediantePost(
                mockMvc,
                "/provincias",
                "nombre",
                nombreProvincia,
                parametrosOpcionales,
                MediaType.TEXT_PLAIN
            );
            idProvincia = extraerIdDeResponse(provinciaResponse);
        }
    }

    @Test
    @Order(1)
    void shouldReturnAPais() throws Exception {
        mockMvc.perform(get("/paises/" + idPais))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(idPais))
            .andExpect(jsonPath("$.nombre").value("ARGENTINA"));
    }

    @Test
    @Order(2)
    void shouldReturnAllPaises() throws Exception {
        mockMvc.perform(get("/paises"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$[" + (idPais - 1) + "].id").value(idPais))
            .andExpect(jsonPath("$[" + (idPais - 1) + "].nombre").value("ARGENTINA"));
    }

    @Test
    @Order(3)
    void shouldCreateAndReturnPais() throws Exception {
        // Crea un JSON representando un autor
        String nombrePais = "BRASIL";

        // Realiza una petición POST al controlador
        mockMvc.perform(post("/paises")
            .param("nombre", nombrePais)
            .contentType(MediaType.TEXT_PLAIN))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").exists())
            .andExpect(jsonPath("$.nombre").value("BRASIL"));
    }

    @Test
    @Order(4)
    void shouldEditAndReturnPais() throws Exception {
        this.ensureProvinciaExists();

        String paisCorregidoJson = """
        {
             "nombre": "URUGUAY",
             "provincias": [
                 {
                     "id": 1,
                     "nombre": "DURAZNO"
                 }
             ]
         }
        """;

        mockMvc.perform(patch("/paises/" + idPais)
            .contentType(MediaType.APPLICATION_JSON)
            .content(paisCorregidoJson))
            .andExpect(status().isAccepted())
            .andExpect(jsonPath("$.nombre").value("URUGUAY"));
    }

    @Test
    @Order(5)
    void shouldDeleteAPais() throws Exception {
        mockMvc.perform(delete("/paises/" + idPais))
            .andExpect(status().isAccepted());

        // Intenta obtener la pais eliminada y verifica el 404
        mockMvc.perform(get("/paises/" + idPais))
            .andExpect(status().isNotFound());
    }
}
