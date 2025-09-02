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
public class ProvinciaTest {

    @Autowired
    private MockMvc mockMvc;

    private static Long idPais, idProvincia, idLocalidad;
    private static String paisResponse, provinciaResponse, localidadResponse;

    @BeforeAll
    public static void setup() {
        idProvincia = null;
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

    private void ensureLocalidadExists() throws Exception {
        if (idLocalidad == null) {
            String nombreLocalidad = "SAN LORENZO";
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
    void shouldReturnAProvincia() throws Exception {
        mockMvc.perform(get("/provincias/" + idProvincia))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(idProvincia))
            .andExpect(jsonPath("$.nombre").value("BUENOS AIRES"))
            .andExpect(jsonPath("$.localidades").isArray())
            .andExpect(jsonPath("$.localidades").isEmpty());
    }

    @Test
    @Order(2)
    void shouldReturnAllProvincias() throws Exception {
        mockMvc.perform(get("/provincias"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$[" + (idProvincia - 1) + "].id").value(idProvincia))
            .andExpect(jsonPath("$[" + (idProvincia - 1) + "].nombre").value("BUENOS AIRES"))
            .andExpect(jsonPath("$[" + (idProvincia - 1) + "].localidades").isArray())
            .andExpect(jsonPath("$[" + (idProvincia - 1) + "].localidades").isEmpty());
    }

    @Test
    @Order(3)
    void shouldCreateAndReturnProvincia() throws Exception {
        // Crea un JSON representando un autor
        String nombreProvincia = "TUCUMAN";

        // Realiza una petición POST al controlador
        mockMvc.perform(post("/provincias")
            .param("nombre", nombreProvincia)
            .param("idPais", String.valueOf(idPais))
            .contentType(MediaType.TEXT_PLAIN))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").exists())
            .andExpect(jsonPath("$.nombre").value("TUCUMAN"))
            .andExpect(jsonPath("$.localidades").isEmpty());
    }

    @Test
    @Order(4)
    void shouldEditAndReturnProvincia() throws Exception {
        String provinciaCorregidaJson = "SANTA FE";
        this.ensureLocalidadExists();

        mockMvc.perform(patch("/provincias/" + idProvincia)
            .param("nombre", provinciaCorregidaJson)
            .param("idPais", String.valueOf(idPais))
            .param("idLocalidades", String.valueOf(idLocalidad))
            .contentType(MediaType.TEXT_PLAIN))
            .andExpect(status().isAccepted())
            .andExpect(jsonPath("$.nombre").value("SANTA FE"))
            .andExpect(jsonPath("$.localidades[0].nombre").value("SAN LORENZO"));
    }

    @Test
    @Order(5)
    void shouldDeleteAProvincia() throws Exception {
        mockMvc.perform(delete("/provincias/" + idProvincia))
            .andExpect(status().isAccepted());

        // Intenta obtener la pais eliminada y verifica el 404
        mockMvc.perform(get("/provincias/" + idProvincia))
            .andExpect(status().isNotFound());
    }
}
