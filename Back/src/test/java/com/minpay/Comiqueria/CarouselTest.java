package com.minpay.Comiqueria;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import static com.minpay.Comiqueria.utils.TestsUtils.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.http.MediaType;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("classpath:application-test.properties")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CarouselTest {
    private static Long idCarousel;
    
    @Autowired
    private MockMvc mockMvc;
    
    @BeforeEach
    void ensureCarouselExists() throws Exception {
        if (idCarousel == null) {
            String carouselJson = """
                                {
                                    "subtitulo": "ULTIMAS NOVEDADES",
                                    "texto": "YA SALIERON LOS NUEVOS NUMEROS DE TUS COMICS PREFERIDOS.",
                                    "imagen": "\\/img\\/carrusel\\/actioncomics.jpg"
                                }
                           """;
            String response = crearMediantePost(mockMvc, "/carousel", carouselJson,
                    MediaType.APPLICATION_JSON);
            idCarousel = extraerIdDeResponse(response);
        }
    }
    
    @Test
    @Order(1)
    void shouldReturnACarousel() throws Exception {
        mockMvc.perform(get("/carousel/" + idCarousel))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(idCarousel))
                .andExpect(jsonPath("$.subtitulo").value("ULTIMAS NOVEDADES"))
                .andExpect(jsonPath("$.texto").value("YA SALIERON LOS NUEVOS NUMEROS DE TUS COMICS PREFERIDOS."))
                .andExpect(jsonPath("$.imagen").value("/img/carrusel/actioncomics.jpg"));
    }
    
    @Test
    @Order(2)
    void shouldReturnAllCarouseles() throws Exception {
        mockMvc.perform(get("/carousel"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[" + (idCarousel - 1) + "].id").value(idCarousel))
                .andExpect(jsonPath("$[" + (idCarousel - 1) + "].subtitulo").value("ULTIMAS NOVEDADES"))
                .andExpect(jsonPath("$[" + (idCarousel - 1) + "].texto").value("YA SALIERON LOS NUEVOS NUMEROS DE TUS COMICS PREFERIDOS."))
                .andExpect(jsonPath("$[" + (idCarousel - 1) + "].imagen").value("/img/carrusel/actioncomics.jpg"));
    }
    
    @Test
    @Order(3)
    void shouldCreateAndReturnCarousel() throws Exception {
        String carouselJson = """
                                {
                                    "subtitulo": "ALTA NUEVA",
                                    "texto": "ESTE ES UN TEXTO DE TEST DE ALTA.",
                                    "imagen": "ACA VA LA IMAGEN DEL TEST.JPG"
                                }
                           """;
        mockMvc.perform(post("/carousel")
                .contentType(MediaType.APPLICATION_JSON)
                .content(carouselJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.subtitulo").value("ALTA NUEVA"))
                .andExpect(jsonPath("$.texto").value("ESTE ES UN TEXTO DE TEST DE ALTA."))
                .andExpect(jsonPath("$.imagen").value("ACA VA LA IMAGEN DEL TEST.JPG"));
    }

    @Test
    @Order(4)
    void shouldEditAndReturnCarousel() throws Exception {
        String carouselCorregidoJson = """
                                {
                                    "subtitulo": "TORNEO DE MAGIC THE GATHERING",
                                    "texto": "¡¡12 DE NOVIEMBRE: NUEVO TORNEO DE MAGIC THE GATHERING!!",
                                    "imagen": "\\/img\\/carrusel\\/magicthegathering.jpg"
                                }
                           """;

        mockMvc.perform(put("/carousel/" + idCarousel)
                .contentType(MediaType.APPLICATION_JSON)
                .content(carouselCorregidoJson))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.subtitulo").value("TORNEO DE MAGIC THE GATHERING"))
                .andExpect(jsonPath("$.texto").value("¡¡12 DE NOVIEMBRE: NUEVO TORNEO DE MAGIC THE GATHERING!!"))
                .andExpect(jsonPath("$.imagen").value("/img/carrusel/magicthegathering.jpg"));
    }

    @Test
    @Order(5)
    void shouldDeleteACarousel() throws Exception {
        mockMvc.perform(delete("/carousel/" + idCarousel))
                .andExpect(status().isAccepted());

        // Intenta obtener el autor eliminado y verifica el 404
        mockMvc.perform(get("/carousel/" + idCarousel))
                .andExpect(status().isNotFound());
    }
}
