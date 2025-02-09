package cl.playground.cv_converter.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

public class ChatGPTPromptUtil {

    public static String createPrompt(String resumeText, String language, String comments) {
        String targetLanguage = "es".equalsIgnoreCase(language) ? "español" : "inglés";

        return String.format("""
                📄 **CV a procesar (Todo el contenido debe ser devuelto en idioma: %s)**:
                %s
                
                💬 **Comentarios adicionales:** %s
                """,
            targetLanguage.toUpperCase(),
            resumeText,
            Optional.ofNullable(comments).orElse("Ninguno")
                            );
    }

    private static String getCurrentDate() {
        LocalDate now = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM yyyy");
        return now.format(formatter);
    }
}
