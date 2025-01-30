package cl.playground.cv_converter.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
public class OpenAIConfig {

    @Bean("openAiRestTemplate") // Nombre único para el bean
    public RestTemplate openAiRestTemplate(
        @Value("${openai.api.key}") String apiKey,
        @Value("${openai.api.timeout:60000}") int timeout) {

        RestTemplate restTemplate = new RestTemplate();

        // Configurar timeouts
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(timeout);
        factory.setReadTimeout(timeout);
        restTemplate.setRequestFactory(factory);

        // Interceptor para autenticación
        restTemplate.getInterceptors().add((request, body, execution) -> {
            request.getHeaders().setBearerAuth(apiKey);
            return execution.execute(request, body);
        });

        return restTemplate;
    }
}
