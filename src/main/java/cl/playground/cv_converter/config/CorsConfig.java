package cl.playground.cv_converter.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "spring.cors")
public class CorsConfig {

    private List<String> allowedOrigins;

    public List<String> getAllowedOrigins() {
        return allowedOrigins;
    }

    public void setAllowedOrigins(String allowedOrigins) {
        this.allowedOrigins = List.of(allowedOrigins.split(","));
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                if (allowedOrigins == null || allowedOrigins.isEmpty()) {
                    System.out.println("⚠️ WARNING: No CORS origins defined!");
                    return;
                }
                System.out.println("✅ Allowed CORS Origins: " + allowedOrigins);

                registry.addMapping("/api/v1/resume")
                    .allowedOrigins(allowedOrigins.toArray(new String[0]))
                    .allowedMethods("POST", "OPTIONS")
                    .allowedHeaders("*");
            }
        };
    }
}
