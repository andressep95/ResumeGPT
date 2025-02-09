package cl.playground.cv_converter.service;

import cl.playground.cv_converter.config.OpenAIProperties;
import cl.playground.cv_converter.exception.OpenAIException;
import cl.playground.cv_converter.model.Resume;
import cl.playground.cv_converter.util.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Service
public class ResumeServiceImpl implements ResumeService {

    private final RestTemplate openAiRestTemplate;
    private final ObjectMapper objectMapper;
    private final OpenAIProperties openAIProperties;

    @Autowired
    public ResumeServiceImpl(
        @Qualifier("openAiRestTemplate")
        RestTemplate openAiRestTemplate,
        ObjectMapper objectMapper,
        OpenAIProperties openAIProperties) {
        this.openAiRestTemplate = openAiRestTemplate;
        this.objectMapper = objectMapper;
        this.openAIProperties = openAIProperties;
    }

    @Override
    public byte[] processResume(MultipartFile file, String language, String comments) {
        try {
            CompletableFuture<Resume> resumeFuture = processResumeData(file, language, comments);
            Resume resume = resumeFuture.join();

            return CompletableFuture.supplyAsync(() -> ResumeGeneratorUtil.generateResumePDF(resume, language)).join();
        } catch (Exception e) {
            throw new RuntimeException("Error procesando el CV: " + e.getMessage(), e);
        }
    }

    @Async
    @Cacheable(
        value = "resumes",
        key = "#file.hashCode()"
    )
    @Override
    public CompletableFuture<Resume> processResumeData(MultipartFile file, String language, String comments) {
        CompletableFuture<String> extractedTextFuture = CompletableFuture.supplyAsync(() ->
            ResumeExtractorUtil.extractResumeContent(file));

        CompletableFuture<Map<String, Object>> requestBodyFuture = extractedTextFuture.thenApplyAsync(extractedText ->
            createOpenAiRequest(extractedText, language, comments));

        CompletableFuture<Map<String, Object>> responseFuture = requestBodyFuture.thenApplyAsync(requestBody -> {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            ResponseEntity<Map> responseEntity = openAiRestTemplate.postForEntity(
                openAIProperties.getUrl(),
                new HttpEntity<>(requestBody, headers),
                Map.class);
            return responseEntity.getBody();
        });

        return responseFuture.thenApplyAsync(response -> {
            if (response == null) {
                throw new OpenAIException("Respuesta vacía de OpenAI");
            }
            return parseOpenAiResponse(response);
        });
    }

    private Map<String, Object> createOpenAiRequest(String extractedText, String language, String comments) {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", openAIProperties.getModel());
        requestBody.put("temperature", openAIProperties.getTemperature());

        List<Map<String, String>> messages = List.of(
            Map.of("role", "system", "content", StaticsPrompts.SYSTEM_CONTEXT),
            Map.of("role", "system", "content", StaticsPrompts.EXAMPLE_OUTPUT),
            Map.of("role", "system", "content", StaticsPrompts.VALIDATION_INSTRUCTIONS),
            Map.of("role", "user", "content", ChatGPTPromptUtil.createPrompt(extractedText, language, comments))
                                                    );

        requestBody.put("messages", messages);
        return requestBody;
    }

    private Resume parseOpenAiResponse(Map<String, Object> response) {
        if (response == null || !response.containsKey("choices")) {
            throw new OpenAIException("No se pudo obtener una respuesta válida de OpenAI");
        }

        try {
            Map<String, Object> firstChoice = (Map<String, Object>) ((List<?>) response.get("choices")).get(0);
            Map<String, Object> message = (Map<String, Object>) firstChoice.get("message");
            String jsonResponse = (String) message.get("content");

            String cleanedJson = ClearJsonUtil.cleanJsonResponse(jsonResponse);

            return objectMapper.readValue(cleanedJson, Resume.class);
        } catch (Exception e) {
            throw new OpenAIException("Error parseando la respuesta de OpenAI", e);
        }
    }

}