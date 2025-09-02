package com.minpay.Comiqueria;

import com.jayway.jsonpath.JsonPath;
import static com.minpay.Comiqueria.utils.TestsUtils.crearMediantePost;
import static com.minpay.Comiqueria.utils.TestsUtils.extraerIdDeResponse;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("classpath:application-test.properties")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ProductoTest {

    @Autowired
    private MockMvc mockMvc;

    private static Long idProducto, idCategoria, idSubcategoria, idEditorial;

    @BeforeAll
    public static void setUpClass() {
        idProducto = null;
        idCategoria = null;
        idSubcategoria = null;
        idEditorial = null;
    }

    @BeforeEach
    public void setUp() throws Exception {
        if (idCategoria == null) {
            String nombreCategoria = "COMICS";
            String response = crearMediantePost(mockMvc, "/categorias", nombreCategoria,
                MediaType.TEXT_PLAIN);
            idCategoria = extraerIdDeResponse(response);
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

        if (idEditorial == null) {
            String nombreEditorial = "OVNI PRESS DC";
            String response = crearMediantePost(
                mockMvc,
                "/editoriales",
                "nombre",
                nombreEditorial,
                MediaType.TEXT_PLAIN
            );
            idEditorial = extraerIdDeResponse(response);
        }

        if (idProducto == null) {
            String titulo = "LA MUERTE DE SUPERMAN";
            Double precio = 1200.00d;
            String descripcion = "¡El evento épico que conmovió al mundo y cambió a superman para siempre! "
                + "Doomsday,una criatura cuyo único propósito es la destrucción, ha aterrizado en la Tierra. "
                + "La Liga de la Justicia hizo un valiente y desesperado intento por detenerlo, pero cuando "
                + "la bestia se acercó a Metrópolis fue Superman quien respondió a la llamada para "
                + "enfrentarlo. Y entonces sucedió lo impensable. El Hombre de Acero... ¡murió!";
            String tapa = "https://tap-multimedia-1172.nyc3.digitaloceanspaces.com/productimage/18235/"
                + "9789877245882.jpg?size=4&h=610";
            String isbn = "978-987-724-588-2";
            int peso = 383;
            String dimensiones = "17x24";
            int paginas = 224;
            Boolean esNovedad = true;
            Boolean esOferta = false;
            Boolean esMasVendido = false;
            Boolean index = false;

            String productoJson = String.format(
                Locale.US,
                """
                {
                    "titulo": "%s",
                    "precio": %.2f,
                    "descripcion": "%s",
                    "tapa": "%s",
                    "isbn": "%s",
                    "peso": %d,
                    "dimensiones": "%s",
                    "paginas": %d,
                    "idSubcategoria": %d,
                    "idEditorial": %d,
                    "esNovedad": %b,
                    "esOferta": %b,
                    "esMasVendido": %b,
                    "index": %b
                }
                """, titulo, precio, descripcion, tapa, isbn, peso, dimensiones, paginas,
                idSubcategoria, idEditorial, esNovedad, esOferta, esMasVendido, index);

            String response = crearMediantePost(
                mockMvc,
                "/productos",
                productoJson,
                MediaType.APPLICATION_JSON
            );
            idProducto = extraerIdDeResponse(response);
        }
    }

    @Test
    @Order(1)
    void shouldReturnAProducto() throws Exception {
        mockMvc.perform(get("/productos/" + idProducto))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(idProducto))
            .andExpect(jsonPath("$.titulo").value("LA MUERTE DE SUPERMAN"))
            .andExpect(jsonPath("$.precio").value(1200.00))
            .andExpect(jsonPath("$.descripcion").value(
                "¡El evento épico que conmovió al mundo y cambió a superman para siempre! "
                + "Doomsday,una criatura cuyo único propósito es la destrucción, ha aterrizado en la "
                + "Tierra. La Liga de la Justicia hizo un valiente y desesperado intento por detenerlo, "
                + "pero cuando la bestia se acercó a Metrópolis fue Superman quien respondió a la llamada "
                + "para enfrentarlo. Y entonces sucedió lo impensable. El Hombre de Acero... ¡murió!"
            ))
            .andExpect(jsonPath("$.tapa").value(
                "https://tap-multimedia-1172.nyc3.digitaloceanspaces.com/productimage/18235/"
                + "9789877245882.jpg?size=4&h=610"
            ))
            .andExpect(jsonPath("$.isbn").value("978-987-724-588-2"))
            .andExpect(jsonPath("$.peso").value(383))
            .andExpect(jsonPath("$.dimensiones").value("17x24"))
            .andExpect(jsonPath("$.paginas").value(224))
            .andExpect(jsonPath("$.idSubcategoria").value(idSubcategoria))
            .andExpect(jsonPath("$.idEditorial").value(idEditorial))
            .andExpect(jsonPath("$.esNovedad").value(true))
            .andExpect(jsonPath("$.esOferta").value(false))
            .andExpect(jsonPath("$.esMasVendido").value(false))
            .andExpect(jsonPath("$.index").value(false));
    }

    @Test
    @Order(2)
    void shouldReturnAllProductos() throws Exception {
        mockMvc.perform(get("/productos"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$[" + (idProducto - 1) + "].id").value(idProducto))
            .andExpect(jsonPath("$[" + (idProducto - 1) + "].titulo").value("LA MUERTE DE SUPERMAN"))
            .andExpect(jsonPath("$[" + (idProducto - 1) + "].precio").value(1200.00))
            .andExpect(jsonPath("$[" + (idProducto - 1) + "].descripcion").value(
                "¡El evento épico que conmovió al mundo y cambió a superman para siempre! "
                + "Doomsday,una criatura cuyo único propósito es la destrucción, ha aterrizado en la "
                + "Tierra. La Liga de la Justicia hizo un valiente y desesperado intento por detenerlo, "
                + "pero cuando la bestia se acercó a Metrópolis fue Superman quien respondió a la llamada "
                + "para enfrentarlo. Y entonces sucedió lo impensable. El Hombre de Acero... ¡murió!"
            ))
            .andExpect(jsonPath("$[" + (idProducto - 1) + "].tapa").value(
                "https://tap-multimedia-1172.nyc3.digitaloceanspaces.com/productimage/18235/"
                + "9789877245882.jpg?size=4&h=610"
            ))
            .andExpect(jsonPath("$[" + (idProducto - 1) + "].isbn").value("978-987-724-588-2"))
            .andExpect(jsonPath("$[" + (idProducto - 1) + "].peso").value(383))
            .andExpect(jsonPath("$[" + (idProducto - 1) + "].dimensiones").value("17x24"))
            .andExpect(jsonPath("$[" + (idProducto - 1) + "].paginas").value(224))
            .andExpect(jsonPath("$[" + (idProducto - 1) + "].idSubcategoria").value(idSubcategoria))
            .andExpect(jsonPath("$[" + (idProducto - 1) + "].idEditorial").value(idEditorial))
            .andExpect(jsonPath("$[" + (idProducto - 1) + "].esNovedad").value(true))
            .andExpect(jsonPath("$[" + (idProducto - 1) + "].esOferta").value(false))
            .andExpect(jsonPath("$[" + (idProducto - 1) + "].esMasVendido").value(false))
            .andExpect(jsonPath("$[" + (idProducto - 1) + "].index").value(false));
    }

    @Test
    @Order(3)
    void shouldCreateAndReturnProducto() throws Exception {
        String titulo = "SUPERMAN: REINO DE LOS SUPERMANES";
        Double precio = 1450.00d;
        String descripcion = "SUPERMAN HA MUERTO. Pero ahora aparecen cuatro seres misteriosos, "
            + "¡todos con los poderes y habilidades del Hombre de Acero! Uno afirma que es un clon del "
            + "ADN de Superman. Otro, mitad hombre y mitad máquina, dice que es Superman con un cuerpo "
            + "cyborg. Otro más, un frío redentor de la justicia, afirma que solo él tiene derecho a "
            + "usar el escudo de la “S”. Y, finalmente, una figura blindada dice que lucha con el corazón "
            + "y el alma de Superman. ¿Quién es el verdadero? Corresponde a Action Comics #687-688, "
            + "Adventures of Superman #500-502, Superman #78-79, Superman Annual #5, Superman: The Man "
            + "of Steel #22-23 Y Superman: The Man of Steel Annual #2.";
        String tapa = "https://d3ugyf2ht6aenh.cloudfront.net/stores/001/184/069/products/"
            + "reino-de-los-supermanes-cov1-77983b8b7fb40ec32b15997760948652-1024-1024.jpg";
        String isbn = "978-987-724-632-2";
        int peso = 800;
        String dimensiones = "17x24";
        int paginas = 328;
        Boolean esNovedad = true;
        Boolean esOferta = false;
        Boolean esMasVendido = false;
        Boolean index = false;

        String productoJson = String.format(
            Locale.US,
            """
            {
                 "titulo": "%s",
                 "precio": %.2f,
                 "descripcion": "%s",
                 "tapa": "%s",
                 "isbn": "%s",
                 "peso": %d,
                 "dimensiones": "%s",
                 "paginas": %d,
                 "idSubcategoria": %d,
                 "idEditorial": %d,
                 "esNovedad": %b,
                 "esOferta": %b,
                 "esMasVendido": %b,
                 "index": %b
            }
            """, titulo, precio, descripcion, tapa, isbn, peso, dimensiones, paginas,
            idSubcategoria, idEditorial, esNovedad, esOferta, esMasVendido, index);

        // Realiza una petición POST al controlador
        mockMvc.perform(post("/productos")
            .contentType(MediaType.APPLICATION_JSON)
            .content(productoJson))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").exists())
            .andExpect(jsonPath("$.titulo").value("SUPERMAN: REINO DE LOS SUPERMANES"))
            .andExpect(jsonPath("$.precio").value(1450.00))
            .andExpect(jsonPath("$.descripcion").value(
                "SUPERMAN HA MUERTO. Pero ahora aparecen cuatro seres misteriosos, "
                + "¡todos con los poderes y habilidades del Hombre de Acero! Uno afirma que es un clon del "
                + "ADN de Superman. Otro, mitad hombre y mitad máquina, dice que es Superman con un cuerpo "
                + "cyborg. Otro más, un frío redentor de la justicia, afirma que solo él tiene derecho a "
                + "usar el escudo de la “S”. Y, finalmente, una figura blindada dice que lucha con el corazón "
                + "y el alma de Superman. ¿Quién es el verdadero? Corresponde a Action Comics #687-688, "
                + "Adventures of Superman #500-502, Superman #78-79, Superman Annual #5, Superman: The Man "
                + "of Steel #22-23 Y Superman: The Man of Steel Annual #2."
            ))
            .andExpect(jsonPath("$.tapa").value(
                "https://d3ugyf2ht6aenh.cloudfront.net/stores/001/184/069/products/"
                + "reino-de-los-supermanes-cov1-77983b8b7fb40ec32b15997760948652-1024-1024.jpg"
            ))
            .andExpect(jsonPath("$.isbn").value("978-987-724-632-2"))
            .andExpect(jsonPath("$.peso").value(800))
            .andExpect(jsonPath("$.dimensiones").value("17x24"))
            .andExpect(jsonPath("$.paginas").value(328))
            .andExpect(jsonPath("$.idSubcategoria").value(idSubcategoria))
            .andExpect(jsonPath("$.idEditorial").value(idEditorial))
            .andExpect(jsonPath("$.esNovedad").value(true))
            .andExpect(jsonPath("$.esOferta").value(false))
            .andExpect(jsonPath("$.esMasVendido").value(false))
            .andExpect(jsonPath("$.index").value(false));
    }

    @Test
    @Order(4)
    void shouldEditAndReturnProducto() throws Exception {
        String titulo = "LA MUERTE DE SUPERMAN";
        Double nuevoPrecio = 1500.00d;
        String descripcion = "¡El evento épico que conmovió al mundo y cambió a superman para "
            + "siempre! Doomsday,una criatura cuyo único propósito es la destrucción, ha aterrizado "
            + "en la Tierra. La Liga de la Justicia hizo un valiente y desesperado intento por "
            + "detenerlo, pero cuando la bestia se acercó a Metrópolis fue Superman quien respondió "
            + "a la llamada para enfrentarlo. Y entonces sucedió lo impensable. El Hombre de "
            + "Acero... ¡murió!";
        String tapa = "https://tap-multimedia-1172.nyc3.digitaloceanspaces.com/productimage/18235/"
            + "9789877245882.jpg?size=4&h=610";
        String isbn = "978-987-724-588-2";
        int peso = 383;
        String dimensiones = "17x24";
        int paginas = 224;
        Boolean esNovedad = true;
        Boolean esOferta = false;
        Boolean esMasVendido = false;
        Boolean index = false;

        String productoJsonEditado = String.format(
            Locale.US,
            """
            {
                "titulo": "%s",
                "precio": %.2f,
                "descripcion": "%s",
                "tapa": "%s",
                "isbn": "%s",
                "peso": %d,
                "dimensiones": "%s",
                "paginas": %d,
                "idSubcategoria": %d,
                "idEditorial": %d,
                "esNovedad": %b,
                "esOferta": %b,
                "esMasVendido": %b,
                "index": %b
            }
            """, titulo, nuevoPrecio, descripcion, tapa, isbn, peso, dimensiones, paginas,
            idSubcategoria, idEditorial, esNovedad, esOferta, esMasVendido, index);

        mockMvc.perform(patch("/productos/" + idProducto)
            .contentType(MediaType.APPLICATION_JSON)
            .content(productoJsonEditado))
            .andExpect(status().isAccepted())
            .andExpect(jsonPath("$.titulo").value("LA MUERTE DE SUPERMAN"))
            .andExpect(jsonPath("$.precio").value(1500.00))
            .andExpect(jsonPath("$.descripcion").value(
                "¡El evento épico que conmovió al mundo y cambió a superman para siempre! "
                + "Doomsday,una criatura cuyo único propósito es la destrucción, ha aterrizado en la "
                + "Tierra. La Liga de la Justicia hizo un valiente y desesperado intento por detenerlo, "
                + "pero cuando la bestia se acercó a Metrópolis fue Superman quien respondió a la llamada "
                + "para enfrentarlo. Y entonces sucedió lo impensable. El Hombre de Acero... ¡murió!"
            ))
            .andExpect(jsonPath("$.tapa").value(
                "https://tap-multimedia-1172.nyc3.digitaloceanspaces.com/productimage/18235/"
                + "9789877245882.jpg?size=4&h=610"
            ))
            .andExpect(jsonPath("$.isbn").value("978-987-724-588-2"))
            .andExpect(jsonPath("$.peso").value(383))
            .andExpect(jsonPath("$.dimensiones").value("17x24"))
            .andExpect(jsonPath("$.paginas").value(224))
            .andExpect(jsonPath("$.idSubcategoria").value(idSubcategoria))
            .andExpect(jsonPath("$.idEditorial").value(idEditorial))
            .andExpect(jsonPath("$.esNovedad").value(true))
            .andExpect(jsonPath("$.esOferta").value(false))
            .andExpect(jsonPath("$.esMasVendido").value(false))
            .andExpect(jsonPath("$.index").value(false));
    }

    @Test
    @Order(5)
    void shouldDeleteAProducto() throws Exception {
        mockMvc.perform(delete("/productos/" + idProducto))
            .andExpect(status().isAccepted());

        // Intenta obtener la producto eliminada y verifica el 404
        mockMvc.perform(get("/productos/" + idProducto))
            .andExpect(status().isNotFound());
    }
}
