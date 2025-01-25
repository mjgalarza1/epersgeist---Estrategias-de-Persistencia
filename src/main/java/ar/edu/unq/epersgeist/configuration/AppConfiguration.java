package ar.edu.unq.epersgeist.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class AppConfiguration implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // Permite solicitudes CORS para todos los endpoints
        registry.addMapping("/**")
                .allowedOriginPatterns("*")  // Especifica tu frontend
                .allowedMethods("GET", "POST", "PUT", "DELETE")  // Métodos permitidos
                .allowCredentials(true)  // Permitir credenciales (cookies, headers de autenticación)
                .maxAge(3600);  // Configura el tiempo máximo de caché de la solicitud CORS
    }
}

