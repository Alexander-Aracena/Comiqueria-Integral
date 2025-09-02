package com.minpay.Comiqueria.security;

import com.minpay.Comiqueria.jwt.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    
    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request, // La solicitud HTTP entrante
            @NonNull HttpServletResponse response, // La respuesta HTTP saliente
            @NonNull FilterChain filterChain // La cadena de filtros para continuar la ejecución
    ) throws ServletException, IOException {

        // 1. Intentar extraer el encabezado de Autorización (donde viene el JWT)
        final String authHeader = request.getHeader("Authorization"); // "Authorization: Bearer <token>"
        final String jwt;
        final String userEmail;

        // Si no hay encabezado de autorización o no empieza con "Bearer ", no hay JWT.
        // Se deja que la solicitud continúe por la cadena de filtros para que Spring Security
        // decida qué hacer (ej. 403 Forbidden para rutas protegidas).
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return; // Termina la ejecución de este filtro aquí
        }

        // 2. Extraer el token JWT (después de "Bearer ")
        jwt = authHeader.substring(7); // "Bearer ".length() es 7

        // 3. Extraer el nombre de usuario del token (aquí podría lanzar excepciones si el token es inválido/expirado)
        try {
            userEmail = jwtService.extractUsername(jwt);

            // 4. Si el nombre de usuario es válido y NO hay un usuario ya autenticado en el contexto de seguridad
            if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                // Cargar los detalles del usuario desde tu UserDetailsService
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);

                // 5. Validar el token con los detalles del usuario cargados
                if (jwtService.isTokenValid(jwt, userDetails)) {
                    // Si el token es válido, crea un objeto de autenticación
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null, // No necesitamos las credenciales aquí, ya el token las validó
                        userDetails.getAuthorities() // Roles/Autorizaciones del usuario
                    );
                    // Establece detalles de la solicitud (dirección IP, etc.)
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    // 6. Actualiza el contexto de seguridad de Spring Security
                    // Esto le dice a Spring Security que este usuario está autenticado
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
        } catch (Exception e) { // Captura cualquier excepción relacionada con JWT (ej. token expirado, firma inválida)
            // Aquí puedes decidir cómo manejar los errores de JWT.
            // Por ejemplo, podrías loguear el error y enviar un 401 Unauthorized.
            // Por ahora, simplemente logueamos y dejamos que la cadena de filtros continúe
            // (lo que podría llevar a un error 403 si la ruta es protegida y no se autenticó).
            System.err.println("Error de validación de JWT: " + e.getMessage());
            // Si quieres enviar un 401 explícitamente aquí:
            // response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            // return;
        }

        // 7. Pasa la solicitud a los siguientes filtros en la cadena
        filterChain.doFilter(request, response);
    }
}
