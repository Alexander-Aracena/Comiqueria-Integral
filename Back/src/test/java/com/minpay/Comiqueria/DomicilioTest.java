package com.minpay.Comiqueria;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static com.minpay.Comiqueria.utils.TestsUtils.*;

import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("classpath:application-test.properties")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DomicilioTest {

    @Autowired
    private MockMvc mockMvc;

    private static Long idPais, idProvincia, idLocalidad, idDomicilio;

    @BeforeEach
    public void setup() throws Exception {
        ensurePaisExists();
        ensureProvinciaExists();
        ensureLocalidadExists();
        ensureDomicilioExists();
    }

    private void ensurePaisExists() throws Exception {
        if (idPais == null) {
            String nombrePais = "ARGENTINA";
            String paisResponse = crearMediantePost(
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
            String nombreProvincia = "BUENOS AIRES";
            Map<String, Object> parametrosOpcionales = new HashMap<>();
            parametrosOpcionales.put("idPais", idPais);
            String provinciaResponse = crearMediantePost(
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
            String nombreLocalidad = "SAN ANDRES";
            Map<String, Object> parametrosOpcionales = new HashMap<>();
            parametrosOpcionales.put("idProvincia", idProvincia);
            String localidadResponse = crearMediantePost(
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

    private void ensureDomicilioExists() throws Exception {
        if (idDomicilio == null) {
            String calle = "Corrientes";
            String altura = "2154";
            String departamento = "";
            String cp = "1642";
            String domicilioJson = String.format(
                """
                {
                    "calle": "%s",
                    "altura": "%s",
                    "departamento": "%s",
                    "cp": "%s",
                    "idLocalidad": %d
                }
                """, calle, altura, departamento, cp, idLocalidad);
            String domicilioResponse = crearMediantePost(
                mockMvc,
                "/domicilios",
                domicilioJson,
                MediaType.APPLICATION_JSON
            );
            idDomicilio = extraerIdDeResponse(domicilioResponse);
        }
    }

    @Test
    @Order(1)
    void shouldReturnADomicilio() throws Exception {
        mockMvc.perform(get("/domicilios/{idDomicilio}", idDomicilio))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(idDomicilio))
            .andExpect(jsonPath("$.calle").value("Corrientes"))
            .andExpect(jsonPath("$.altura").value("2154"))
            .andExpect(jsonPath("$.departamento").value(""))
            .andExpect(jsonPath("$.cp").value("1642"))
            .andExpect(jsonPath("$.idLocalidad").value(idLocalidad));
    }

    @Test
    @Order(2)
    void shouldReturnAllDomicilios() throws Exception {
        mockMvc.perform(get("/domicilios"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$[" + (idDomicilio - 1) + "].id").value(idDomicilio))
            .andExpect(jsonPath("$[" + (idDomicilio - 1) + "].calle").value("Corrientes"))
            .andExpect(jsonPath("$[" + (idDomicilio - 1) + "].altura").value("2154"))
            .andExpect(jsonPath("$[" + (idDomicilio - 1) + "].departamento").value(""))
            .andExpect(jsonPath("$[" + (idDomicilio - 1) + "].cp").value("1642"))
            .andExpect(jsonPath("$[" + (idDomicilio - 1) + "].idLocalidad").value(idLocalidad));
    }

    @Test
    @Order(3)
    void shouldEditAndReturnDomicilio() throws Exception {
        String calle = "Siempreviva";
        String altura = "123";
        String departamento = "";
        String cp = "1423";
        String domicilioCorregidoJson = String.format(
            """
            {
                "calle": "%s",
                "altura": "%s",
                "departamento": "%s",
                "cp": "%s",
                "idLocalidad": %d
            }
            """, calle, altura, departamento, cp, idLocalidad);

        mockMvc.perform(patch("/domicilios/{idDomicilio}", idDomicilio)
            .contentType(MediaType.APPLICATION_JSON)
            .content(domicilioCorregidoJson))
            .andExpect(status().isAccepted())
            .andExpect(jsonPath("$.id").value(idDomicilio))
            .andExpect(jsonPath("$.calle").value("Siempreviva"))
            .andExpect(jsonPath("$.altura").value("123"))
            .andExpect(jsonPath("$.departamento").value(""))
            .andExpect(jsonPath("$.cp").value("1423"))
            .andExpect(jsonPath("$.idLocalidad").value(idLocalidad));
    }

    @Test
    @Order(4)
    void shouldDeleteADomicilio() throws Exception {
        mockMvc.perform(delete("/domicilios/{idDomicilio}", idDomicilio))
            .andExpect(status().isAccepted());

        // Intenta obtener el autor eliminado y verifica el 404
        mockMvc.perform(get("/domicilios/{idDomicilio}", idDomicilio))
            .andExpect(status().isNotFound());

        idDomicilio = null;
    }
}
