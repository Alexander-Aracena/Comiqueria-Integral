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
public class LocalidadTest {

    @Autowired
    private MockMvc mockMvc;

    private static Long idPais, idProvincia, idLocalidad;
    private static String paisResponse, provinciaResponse, localidadResponse;

    @BeforeAll
    public static void setup() {
        idLocalidad = null;
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

    @BeforeEach
    public void ensureProvinciaExists() throws Exception {
        if (idProvincia == null) {
            String nombreProvincia = "BUENOS AIRES";
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

    @BeforeEach
    public void ensureLocalidadExists() throws Exception {
        if (idLocalidad == null) {
            String nombreLocalidad = "SAN ANDRES";
            Map<String, Object> parametrosOpcionales = new HashMap<>();
            parametrosOpcionales.put("idProvincia", idProvincia);
            localidadResponse = crearMediantePost(
                mockMvc,
                "/localidades",
                "nombre",
                nombreLocalidad,
                parametrosOpcionales,
                MediaType.TEXT_PLAIN
            );
            idLocalidad = extraerIdDeResponse(localidadResponse);
        }
    }

    @Test
    @Order(1)
    void shouldReturnALocalidad() throws Exception {
        mockMvc.perform(get("/localidades/" + idLocalidad))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(idLocalidad))
            .andExpect(jsonPath("$.nombre").value("SAN ANDRES"));
    }

    @Test
    @Order(2)
    void shouldReturnAllLocalidades() throws Exception {
        mockMvc.perform(get("/localidades"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$[" + (idLocalidad - 1) + "].id").value(idLocalidad))
            .andExpect(jsonPath("$[" + (idLocalidad - 1) + "].nombre").value("SAN ANDRES"));
    }

    @Test
    @Order(3)
    void shouldCreateAndReturnLocalidad() throws Exception {
        // Crea un JSON representando un autor
        String nombreLocalidad = "GENERAL SAN MARTIN";

        // Realiza una petición POST al controlador
        mockMvc.perform(post("/localidades")
            .param("nombre", nombreLocalidad)
            .param("idProvincia", String.valueOf(idProvincia))
            .contentType(MediaType.TEXT_PLAIN))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").exists())
            .andExpect(jsonPath("$.nombre").value("GENERAL SAN MARTIN"));
    }

    @Test
    @Order(4)
    void shouldEditAndReturnLocalidad() throws Exception {
        String localidadCorregidoJson = "VILLA BALLESTER";

        mockMvc.perform(patch("/localidades/" + idLocalidad)
            .param("nombre", localidadCorregidoJson)
            .param("idProvincia", String.valueOf(idProvincia))
            .contentType(MediaType.TEXT_PLAIN))
            .andExpect(status().isAccepted())
            .andExpect(jsonPath("$.nombre").value("VILLA BALLESTER"));
    }

    @Test
    @Order(5)
    void shouldDeleteALocalidad() throws Exception {
        mockMvc.perform(delete("/localidades/" + idLocalidad))
            .andExpect(status().isAccepted());

        // Intenta obtener la localidad eliminada y verifica el 404
        mockMvc.perform(get("/localidades/" + idLocalidad))
            .andExpect(status().isNotFound());
    }
}
