package cl.playground.cv_converter.service;

import cl.playground.cv_converter.config.OpenAIProperties;
import cl.playground.cv_converter.exception.OpenAIException;
import cl.playground.cv_converter.model.Resume;
import cl.playground.cv_converter.util.ChatGPTPromptUtil;
import cl.playground.cv_converter.util.ClearJsonUtil;
import cl.playground.cv_converter.util.ResumeExtractorUtil;
import cl.playground.cv_converter.util.ResumeGeneratorUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

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
            String langCode = language.toLowerCase().startsWith("es") ? "es" : "en";
            Resume resume = processResumeData(file, langCode, comments);
            return ResumeGeneratorUtil.generateResumePDF(resume, language);
        } catch (Exception e) {
            throw new RuntimeException("Error procesando el CV: " + e.getMessage(), e);
        }
    }

    @Override
    public Resume processResumeData(MultipartFile file, String language, String comments) {
        try {
            String extractedText = ResumeExtractorUtil.extractResumeContent(file);
            Map<String, Object> requestBody = createOpenAiRequest(extractedText, language, comments);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            Map<String, Object> response = openAiRestTemplate.postForObject(
                openAIProperties.getUrl(),
                new HttpEntity<>(requestBody, headers),
                Map.class
                                                                           );

            return parseOpenAiResponse(response);
        } catch (Exception e) {
            throw new RuntimeException("Error procesando los datos del CV: " + e.getMessage(), e);
        }
    }

    private Map<String, Object> createOpenAiRequest(String extractedText, String language, String comments) {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", openAIProperties.getModel());
        requestBody.put("temperature", openAIProperties.getTemperature());

        Map<String, String> systemMessage = new HashMap<>();
        systemMessage.put("role", "system");
        systemMessage.put("content", "Eres un asistente que genera solo respuestas en formato JSON válido, sin marcado adicional ni caracteres de formato.");

        Map<String, String> userMessage = new HashMap<>();
        userMessage.put("role", "user");
        userMessage.put("content", ChatGPTPromptUtil.createPrompt(extractedText, language, comments));

        requestBody.put("messages", new Map[]{systemMessage, userMessage});
        return requestBody;
    }

    private Resume parseOpenAiResponse(Map<String, Object> response) {
        if (response == null || !response.containsKey("choices")) {
            throw new OpenAIException("No se pudo obtener una respuesta válida de OpenAI");
        }

        try {
            Map<String, Object> firstChoice = (Map<String, Object>) ((java.util.List) response.get("choices")).get(0);
            Map<String, Object> message = (Map<String, Object>) firstChoice.get("message");
            String jsonResponse = (String) message.get("content");

            String cleanedJson = ClearJsonUtil.cleanJsonResponse(jsonResponse);
            return objectMapper.readValue(cleanedJson, Resume.class);
        } catch (Exception e) {
            throw new OpenAIException("Error parseando la respuesta de OpenAI", e);
        }
    }
}