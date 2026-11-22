package com.losalgarrobos.socioclub.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

// CORS: el navegador bloquea las peticiones de una página (la app React en localhost:3000)
// a otro origen (este backend en localhost:8080) salvo que el servidor lo autorice.
// Acá autorizamos a la app de socios a usar la API.
@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOrigins("http://localhost:3000")
                .allowedMethods("GET", "POST", "PUT", "DELETE")
                .allowedHeaders("*");
    }
}
