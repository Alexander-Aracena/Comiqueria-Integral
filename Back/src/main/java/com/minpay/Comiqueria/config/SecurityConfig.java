package com.minpay.Comiqueria.config;

import com.minpay.Comiqueria.security.JwtAuthenticationFilter;
import java.util.Arrays;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;
    
    @Bean
    public AuthenticationManager authenticationManager(
        AuthenticationConfiguration authenticationConfiguration
    ) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
    
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        
        http
            // Deshabilita la protección CSRF (Cross-Site Request Forgery)
            // ya que se asume que la aplicación es una API REST que usa JWT
            // y no sesiones basadas en cookies, lo que hace CSRF innecesario.
            .csrf(csrf -> csrf.disable())
            
            // Configura la gestión de sesiones para que sea sin estado (STATELESS).
            // Esto significa que Spring Security nunca creará una sesión HTTP ni la utilizará
            // para almacenar el contexto de seguridad, lo cual es ideal para JWT.
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            
            // Configura las reglas de autorización para las solicitudes HTTP.
            .authorizeHttpRequests(authorize -> authorize
              
                // Permite el acceso sin autenticación a los endpoints de login y registro.
                .requestMatchers("/auth/**").permitAll()
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                
                 //Permite el acceso sólo para obtener información (GET)
                .requestMatchers(HttpMethod.GET, "/api/autores").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/productos").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/carrusel").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/editoriales").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/categorias").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/subcategorias").permitAll()
                
                // Cualquier otra solicitud HTTP debe estar autenticada.
                .anyRequest().authenticated()
            )
        
        // Configura el proveedor de autenticación que Spring Security usará
        .authenticationProvider(authenticationProvider)
        // Agrega tu filtro JWT ANTES del filtro estándar de autenticación por usuario/contraseña de Spring Security.
        // Esto es CRUCIAL: asegura que el JWT se procese primero para autenticar la petición.
        .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        
        // Construye y retorna la cadena de filtros de seguridad.
        return http.build();
    }
    
    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        
        // Permite solicitudes desde 'http://localhost:4200', 'http://127.0.0.1:5500' o cualquier otro puerto de tu frontend
        config.setAllowedOrigins(Arrays.asList("http://localhost:4200", "http://127.0.0.1:5500")); 
        
        // Permite los métodos HTTP que vas a usar (GET, POST, PUT, DELETE, etc.)
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        
        // Permite todos los headers en las solicitudes
        config.setAllowedHeaders(Arrays.asList("*"));
        
        // Permite el envío de credenciales (cookies, headers de autorización)
        config.setAllowCredentials(true);
        
        // Aplica esta configuración a todas las rutas de tu API
        source.registerCorsConfiguration("/**", config); 
        
        return new CorsFilter(source);
    }
}
