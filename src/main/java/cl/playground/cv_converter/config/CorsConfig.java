package cl.playground.cv_converter.config;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {

    @Value("${cors.allowed-origins}")
    private String allowedOrigins;

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/api/v1/resume")
                    .allowedOrigins(allowedOrigins)
                    .allowedMethods("POST", "OPTIONS")
                    .allowedHeaders("*");
            }
        };
    }

    @PostConstruct
    public void init() {
        System.out.println("Allowed Origins: " + allowedOrigins);
    }
}
