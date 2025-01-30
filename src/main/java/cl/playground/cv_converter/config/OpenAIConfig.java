package cl.playground.cv_converter.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
public class OpenAIConfig {

    private final OpenAIProperties openAIProperties;

    public OpenAIConfig(OpenAIProperties openAIProperties) {
        this.openAIProperties = openAIProperties;
    }

    @Bean("openAiRestTemplate") // Nombre único para el bean
    public RestTemplate openAiRestTemplate() {
        RestTemplate restTemplate = new RestTemplate();

        // Configurar timeouts
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(openAIProperties.getTimeout());
        factory.setReadTimeout(openAIProperties.getTimeout());
        restTemplate.setRequestFactory(factory);

        // Interceptor para autenticación
        restTemplate.getInterceptors().add((request, body, execution) -> {
            request.getHeaders().setBearerAuth(openAIProperties.getKey());
            return execution.execute(request, body);
        });

        return restTemplate;
    }
}
