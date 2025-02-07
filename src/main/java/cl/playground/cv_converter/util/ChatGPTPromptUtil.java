package cl.playground.cv_converter.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

public class ChatGPTPromptUtil {

    public static String createPrompt(String resumeText, String language, String comments) {
        String targetLanguage = "es".equalsIgnoreCase(language) ? "español" : "inglés";
        String currentDate = getCurrentDate();

        return String.format("""
                Generar un JSON válido usando EXCLUSIVAMENTE datos del CV.
                Asegurate de entregar todo traducido al idioma %s
                
                REGLAS:
                - Solo datos presentes en CV
                - Omitir campos vacíos
                - No usar "Not Provided"/null/[]
                
                Estructura: {"header":{"name":"string","contact":{"email":"string","phone":"string"}},"education":[{"institution":"string","degree":"string","graduationDate":"string","achievements":["string"]}],"technicalSkills":{"skills":["string"]},"professionalExperience":[{"company":"string","position":"string","period":{"start":"string","end":"string"},"responsibilities":["string"],"location":"string"}],"certifications":[{"name":"string","dateObtained":"string"}],"projects":[{"name":"string","description":"string","technologies":["string"]}]}
                
                REGLAS ESPECIFICAS:
                - Fechas: MM YYYY (ej: 02 2020). Actual: %s
                - Skills: una por entrada, nombres exactos
                
                **CV:** %s
                **Comentarios:** %s
                """,
            targetLanguage.toUpperCase(),
            currentDate,
            resumeText,
            Optional.ofNullable(comments).orElse("Ninguno"));
    }

    private static String getCurrentDate() {
        LocalDate now = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM yyyy");
        return now.format(formatter);
    }

}