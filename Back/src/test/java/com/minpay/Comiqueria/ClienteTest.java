package com.minpay.Comiqueria;

import com.jayway.jsonpath.JsonPath;
import com.minpay.Comiqueria.model.Sexo;
import com.minpay.Comiqueria.model.TipoDoc;
import static com.minpay.Comiqueria.utils.TestsUtils.crearMediantePost;
import static com.minpay.Comiqueria.utils.TestsUtils.extraerIdDeResponse;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
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
public class ClienteTest {

    @Autowired
    private MockMvc mockMvc;

    private static Long idPais, idProvincia, idLocalidad, idDomicilio, idCliente, idCategoria,
        idSubcategoria, idEditorial, idProducto;
    private final Set<Long> idsProductos = new LinkedHashSet<>();

    @BeforeEach
    public void setup() throws Exception {
        ensurePaisExists();
        ensureProvinciaExists();
        ensureLocalidadExists();
        ensureDomicilioExists();
        ensureClienteExists();
        ensureProductoExists();
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

    private void ensureClienteExists() throws Exception {
        if (idCliente == null) {
            String nombre = "Pedro";
            String apellido = "Solis";
            LocalDate fechaNac = LocalDate.of(1992, 5, 24);
            Sexo sexo = Sexo.MASCULINO;
            TipoDoc tipoDoc = TipoDoc.DNI;
            String nroDoc = "37624865";
            String telefono = "1125120953";
            String clienteJson = String.format(
                """
                {
                    "nombre": "%s",
                    "apellido": "%s",
                    "fechaNac": "%s",
                    "sexo": "%s",
                    "tipoDoc": "%s",
                    "nroDoc": "%s",
                    "telefono": "%s"
                }
                """, nombre, apellido, fechaNac.toString(), sexo.toString(), tipoDoc.toString(),
                nroDoc, telefono);
            String clienteResponse = crearMediantePost(
                mockMvc,
                "/clientes",
                clienteJson,
                MediaType.APPLICATION_JSON
            );
            idCliente = extraerIdDeResponse(clienteResponse);
        }
    }

    private void ensureProductoExists() throws Exception {
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
            idsProductos.add(extraerIdDeResponse(responseProducto1));
            idsProductos.add(extraerIdDeResponse(responseProducto2));
        }
    }

    @Test
    @Order(1)
    void shouldReturnACliente() throws Exception {
        mockMvc.perform(get("/clientes/{idCliente}", idCliente))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(idCliente))
            .andExpect(jsonPath("$.nombre").value("Pedro"))
            .andExpect(jsonPath("$.apellido").value("Solis"))
            .andExpect(jsonPath("$.fechaNac").value(LocalDate.of(1992, 5, 24).toString()))
            .andExpect(jsonPath("$.sexo").value(Sexo.MASCULINO.toString()))
            .andExpect(jsonPath("$.tipoDoc").value(TipoDoc.DNI.toString()))
            .andExpect(jsonPath("$.nroDoc").value("37624865"))
            .andExpect(jsonPath("$.telefono").value("1125120953"));
    }

    @Test
    @Order(2)
    void shouldReturnAllClientes() throws Exception {
        mockMvc.perform(get("/clientes"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$[" + (idCliente - 1) + "].id").value(idCliente))
            .andExpect(jsonPath("$[" + (idCliente - 1) + "].nombre").value("Pedro"))
            .andExpect(jsonPath("$[" + (idCliente - 1) + "].apellido").value("Solis"))
            .andExpect(jsonPath("$[" + (idCliente - 1) + "].fechaNac").value(
                LocalDate.of(1992, 5, 24).toString()
            ))
            .andExpect(jsonPath("$[" + (idCliente - 1) + "].sexo").value(Sexo.MASCULINO.toString()))
            .andExpect(jsonPath("$[" + (idCliente - 1) + "].tipoDoc").value(TipoDoc.DNI.toString()))
            .andExpect(jsonPath("$[" + (idCliente - 1) + "].nroDoc").value("37624865"))
            .andExpect(jsonPath("$[" + (idCliente - 1) + "].telefono").value("1125120953"));
    }

    @Test
    @Order(3)
    void shouldEditAndReturnCliente() throws Exception {
        String nombre = "Mariana";
        String apellido = "Mendoza";
        LocalDate fechaNac = LocalDate.of(1982, 3, 12);
        Sexo sexo = Sexo.FEMENINO;
        TipoDoc tipoDoc = TipoDoc.DNI;
        String nroDoc = "28315642";
        String telefono = "3516485236";
        String clienteCorregidoJson = String.format(
            """
            {
                "nombre": "%s",
                "apellido": "%s",
                "fechaNac": "%s",
                "sexo": "%s",
                "tipoDoc": "%s",
                "nroDoc": "%s",
                "telefono": "%s"
            }
            """, nombre, apellido, fechaNac.toString(), sexo.toString(), tipoDoc.toString(),
            nroDoc, telefono);

        mockMvc.perform(patch("/clientes/{idCliente}", idCliente)
            .contentType(MediaType.APPLICATION_JSON)
            .content(clienteCorregidoJson))
            .andExpect(status().isAccepted())
            .andExpect(jsonPath("$.id").value(idCliente))
            .andExpect(jsonPath("$.nombre").value("Mariana"))
            .andExpect(jsonPath("$.apellido").value("Mendoza"))
            .andExpect(jsonPath("$.fechaNac").value(LocalDate.of(1982, 3, 12).toString()))
            .andExpect(jsonPath("$.sexo").value(Sexo.FEMENINO.toString()))
            .andExpect(jsonPath("$.tipoDoc").value(TipoDoc.DNI.toString()))
            .andExpect(jsonPath("$.nroDoc").value("28315642"))
            .andExpect(jsonPath("$.telefono").value("3516485236"));
    }

    @Test
    @Order(4)
    void shouldDeleteACliente() throws Exception {
        mockMvc.perform(delete("/clientes/{idCliente}", idCliente))
            .andExpect(status().isAccepted());

        // Intenta obtener el autor eliminado y verifica el 404
        mockMvc.perform(get("/clientes/{idCliente}", idCliente))
            .andExpect(status().isNotFound());

        idCliente = null;
    }

    @Test
    @Order(5)
    void shouldAddADomicilio() throws Exception {
        mockMvc.perform(post("/clientes/domicilio/{idCliente}", idCliente)
            .param("idDomicilio", String.valueOf(idDomicilio))
            .contentType(MediaType.TEXT_PLAIN))
            .andExpect(status().isCreated());

        // Corroborar que el domicilio fue registrado bajo el cliente correctamente
        mockMvc.perform(get("/clientes/{idCliente}", idCliente))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.domicilios[" + (idDomicilio - 1) + "].id").value(idDomicilio))
            .andExpect(jsonPath("$.domicilios[" + (idDomicilio - 1) + "].calle").value("Corrientes"))
            .andExpect(jsonPath("$.domicilios[" + (idDomicilio - 1) + "].altura").value("2154"))
            .andExpect(jsonPath("$.domicilios[" + (idDomicilio - 1) + "].departamento").value(""))
            .andExpect(jsonPath("$.domicilios[" + (idDomicilio - 1) + "].cp").value("1642"))
            .andExpect(jsonPath("$.domicilios[" + (idDomicilio - 1) + "].idLocalidad").value(idLocalidad));
    }

    @Test
    @Order(6)
    void shouldDeleteADomicilio() throws Exception {
        mockMvc.perform(delete("/clientes/domicilio/{idCliente}", idCliente)
            .param("idDomicilio", String.valueOf(idDomicilio))
            .contentType(MediaType.TEXT_PLAIN))
            .andExpect(status().isAccepted());

        // Corroborar que el domicilio fue eliminado del cliente correctamente
        mockMvc.perform(get("/clientes/{idCliente}", idCliente))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.domicilios[" + (idDomicilio - 1) + "].id").doesNotExist());
    }

    @Test
    @Order(6)
    void shouldAddAProducto() throws Exception {
            mockMvc.perform(
                post("/clientes/favoritos/{idCliente}", idCliente)
                .contentType(MediaType.APPLICATION_JSON)
                .content(String.format("%s", idsProductos))
            )
                .andExpect(status().isAccepted());

        // Corroborar que el domicilio fue eliminado del cliente correctamente
        mockMvc.perform(get("/clientes/{idCliente}", idCliente))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.favoritos").isArray())
            .andExpect(jsonPath("$.favoritos").isNotEmpty())
            .andExpect(jsonPath("$.favoritos[0].titulo").value("LA MUERTE DE SUPERMAN"));
    }
}
