package com.minpay.Comiqueria;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static com.minpay.Comiqueria.utils.TestsUtils.*;

import com.jayway.jsonpath.JsonPath;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("classpath:application-test.properties")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AutorTest {

    @Autowired
    private MockMvc mockMvc;

    private static Long idAutor, idCategoria, idSubcategoria, idEditorial;
    private static String categoriaResponse, subcategoriaResponse, editorialResponse, productosJson;

    @BeforeAll
    public static void setup() {
        idAutor = null;
        idCategoria = null;
        idSubcategoria = null;
        idEditorial = null;
    }

    @BeforeEach
    public void ensureAutorExists() throws Exception {
        if (idAutor == null) {
            String response = crearAutor("Juan", "Pérez");
            idAutor = extraerIdDeResponse(response);
        }
    }

    @BeforeEach
    public void ensureSetProductosExists(TestInfo testInfo) throws Exception {
        String nombreTest = testInfo.getDisplayName();
        if (nombreTest.equals("shouldAddProductos()") || nombreTest.equals("shouldDeleteProductos()")){
            if (idCategoria == null) {
                categoriaResponse = crearCategoria();
                idCategoria = extraerIdDeResponse(categoriaResponse);
            }

            if (idSubcategoria == null) {
                subcategoriaResponse = crearSubcategoria(idCategoria);
                idSubcategoria = Long.valueOf(
                    JsonPath.read(subcategoriaResponse, "$.idSubcategoria").toString()
                );
            }

            if (idEditorial == null) {
                editorialResponse = crearEditorial();
                idEditorial = extraerIdDeResponse(editorialResponse);
            }
            
            if (nombreTest.equals("shouldDeleteProductos()") && productosJson == null) {
                productosJson = armarJsonProductos();
                agregarProductoMediantePost(idAutor, productosJson);
            }
        }
    }

    @Test
    @Order(1)
    void shouldReturnAnAutor() throws Exception {
        mockMvc.perform(get("/autores/" + idAutor))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(idAutor))
            .andExpect(jsonPath("$.nombre").value("Juan"))
            .andExpect(jsonPath("$.apellido").value("Pérez"))
            .andExpect(jsonPath("$.productos").isEmpty())
            .andExpect(jsonPath("$.fechaAlta").value(LocalDate.now().toString()));
    }

    @Test
    @Order(2)
    void shouldReturnAllAutores() throws Exception {
        mockMvc.perform(get("/autores"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$[" + (idAutor - 1) + "].id").value(idAutor))
            .andExpect(jsonPath("$[" + (idAutor - 1) + "].nombre").value("Juan"))
            .andExpect(jsonPath("$[" + (idAutor - 1) + "].apellido").value("Pérez"))
            .andExpect(jsonPath("$[" + (idAutor - 1) + "].productos").isEmpty())
            .andExpect(jsonPath("$[" + (idAutor - 1) + "].fechaAlta").value(LocalDate.now().toString()));
    }

    @Test
    @Order(3)
    void shouldEditAndReturnAutor() throws Exception {
        String autorCorregido = """
                                {
                                    "nombre": "Fernando",
                                    "apellido": "Solis"
                                }
                                """;

        mockMvc.perform(patch("/autores/" + idAutor)
            .contentType(MediaType.APPLICATION_JSON)
            .content(autorCorregido))
            .andExpect(status().isAccepted())
            .andExpect(jsonPath("$.nombre").value("Fernando"))
            .andExpect(jsonPath("$.apellido").value("Solis"));
    }

    @Test
    @Order(4)
    void shouldDeleteAnAutor() throws Exception {
        mockMvc.perform(delete("/autores/" + idAutor))
            .andExpect(status().isAccepted());

        // Intenta obtener el autor eliminado y verifica el 404
        mockMvc.perform(get("/autores/" + idAutor))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.fechaBaja").value(LocalDate.now().toString()));

        idAutor = null;
    }
    
    @Test
    @Order(5)
    void shouldGetProductos() throws Exception {
        mockMvc.perform(get("/autores/productos/{idAutor}", idAutor))
            .andExpect(status().isOk());
    }

    @Test
    @Order(6)
    void shouldAddProductos() throws Exception {
        productosJson = armarJsonProductos();
        agregarProductoMediantePost(idAutor, productosJson)
            .andExpect(status().isAccepted());
    }

    @Test
    @Order(7)
    void shouldDeleteProductos() throws Exception {
        mockMvc.perform(delete("/autores/productos/{idAutor}", idAutor)
            .contentType(MediaType.APPLICATION_JSON)
            .content(productosJson))
            .andExpect(status().isAccepted());

        mockMvc.perform(get("/autores/productos/{idAutor}", idAutor))
            .andExpect(jsonPath("$.productos").isEmpty());

    }

    private ResultActions agregarProductoMediantePost(Long idAutorConProductos, String productosJson) throws Exception {
        return mockMvc.perform(post("/autores/productos/{idAutorConProductos}", idAutorConProductos)
            .contentType(MediaType.APPLICATION_JSON)
            .content(productosJson));
    }

    private String crearAutor(String nombre, String apellido) throws Exception {
        String autorJson = String.format("""
                           {
                                "nombre": "%s",
                                "apellido": "%s"
                           }
                           """, nombre, apellido);
        return crearMediantePost(mockMvc, "/autores", autorJson, MediaType.APPLICATION_JSON);
    }

    private String crearCategoria() throws Exception {
        String nombreCategoria = "COMICS";
        return crearMediantePost(mockMvc, "/categorias", nombreCategoria, MediaType.TEXT_PLAIN);
    }

    private String crearSubcategoria(Long idCat) throws Exception {
        String nombreSubcategoria = "USA";
        Map<String, Object> opcionales = new HashMap<>();
        opcionales.put("idCategoria", idCat);
        
        return crearMediantePost(
            mockMvc,
            "/subcategorias",
            "nombreSubcategoria",
            nombreSubcategoria,
            opcionales,
            MediaType.TEXT_PLAIN
        );
    }

    private String crearEditorial() throws Exception {
        String nombreEditorial = "OVNI PRESS DC";
        return crearMediantePost(mockMvc, "/editoriales", "nombre", nombreEditorial, MediaType.TEXT_PLAIN);
    }

    private String armarJsonProductos() throws Exception {
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

        String productoJson1 = String.format(
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
        String responseProducto1 = crearMediantePost(mockMvc, "/productos", productoJson1, MediaType.APPLICATION_JSON);
        
        titulo = "SUPERMAN: REINO DE LOS SUPERMANES";
        precio = 1450.00d;
        descripcion = "SUPERMAN HA MUERTO. Pero ahora aparecen cuatro seres misteriosos, "
            + "¡todos con los poderes y habilidades del Hombre de Acero! Uno afirma que es un clon del "
            + "ADN de Superman. Otro, mitad hombre y mitad máquina, dice que es Superman con un cuerpo "
            + "cyborg. Otro más, un frío redentor de la justicia, afirma que solo él tiene derecho a "
            + "usar el escudo de la “S”. Y, finalmente, una figura blindada dice que lucha con el corazón "
            + "y el alma de Superman. ¿Quién es el verdadero? Corresponde a Action Comics #687-688, "
            + "Adventures of Superman #500-502, Superman #78-79, Superman Annual #5, Superman: The Man "
            + "of Steel #22-23 Y Superman: The Man of Steel Annual #2.";
        tapa = "https://d3ugyf2ht6aenh.cloudfront.net/stores/001/184/069/products/"
            + "reino-de-los-supermanes-cov1-77983b8b7fb40ec32b15997760948652-1024-1024.jpg";
        isbn = "978-987-724-632-2";
        peso = 800;
        dimensiones = "17x24";
        paginas = 328;
        esNovedad = true;
        esOferta = false;
        esMasVendido = false;
        index = false;

        String productoJson2 = String.format(
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
        String responseProducto2 = crearMediantePost(mockMvc, "/productos", productoJson2, MediaType.APPLICATION_JSON);
        Set<Long> idsProductos = new LinkedHashSet<>();
        idsProductos.add(extraerIdDeResponse(responseProducto1));
        idsProductos.add(extraerIdDeResponse(responseProducto2));
        return String.format("%s", idsProductos);
    }
}
