package com.minpay.Comiqueria.utils;

import com.jayway.jsonpath.JsonPath;
import java.util.Map;
import lombok.experimental.UtilityClass;
import org.junit.jupiter.api.Assertions;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@UtilityClass
public final class TestsUtils {
    public static Long extraerIdDeResponse(String response) {
        Long id = Long.valueOf(JsonPath.read(response, "$.id").toString());
        Assertions.assertNotNull(id, "El ID no debe ser nulo");
        Assertions.assertTrue(id > 0, "El ID debe ser mayor a 0");
        return id;
    }

    public static String crearMediantePost(
            MockMvc mockMvc,
            String endpoint,
            String json,
            MediaType mediaType
    ) throws Exception {
        return mockMvc.perform(post(endpoint)
                .contentType(mediaType)
                .content(json))
                .andReturn()
                .getResponse()
                .getContentAsString();
    }
    
    public static String crearMediantePost(
            MockMvc mockMvc,
            String endpoint,
            String param1,
            String value1,
            Map<String, Object> parametrosOpcionales,
            MediaType mediaType
    ) throws Exception {
        
        MockHttpServletRequestBuilder requestBuilder = post(endpoint)
            .param(param1, value1)
            .contentType(mediaType);
        
        if (parametrosOpcionales != null) {
            parametrosOpcionales.forEach((key, value) -> {
                if (value != null) {
                    requestBuilder.param(key, String.valueOf(value));
                }
            });
        }
        
        return mockMvc.perform(requestBuilder)
                .andReturn()
                .getResponse()
                .getContentAsString();
    }
    
    public static String crearMediantePost(
        MockMvc mockMvc,
        String endpoint,
        String paramObligatorio,
        String valorObligatorio,
        MediaType mediaType
) throws Exception {
    return crearMediantePost(
        mockMvc,
        endpoint,
        paramObligatorio,
        valorObligatorio,
        null,
        mediaType
    );
}
}
