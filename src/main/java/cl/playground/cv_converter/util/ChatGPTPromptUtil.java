package cl.playground.cv_converter.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Optional;

public class ChatGPTPromptUtil {

    public static String createPrompt(String resumeText, String language, String comments) {
        String targetLanguage = "es".equalsIgnoreCase(language) ? "español" : "inglés";
        String currentDate = getCurrentDate();

        return String.format("""
                Generar un JSON válido en idioma %s usando EXCLUSIVAMENTE datos del CV. 
                
                 **Reglas estrictas:**
                - Solo incluir datos presentes en el CV, omitiendo cualquier campo vacío.
                - NO agregar valores como "Not Provided", `null` o arrays vacíos `[]`.
                - Mantener precisión absoluta con los datos originales.
                
                 **Estructura requerida (omitir secciones/objetos vacíos):**
                ```json
                {
                    "header": {
                        "name": "string",
                        "contact": {
                            "email": "string",
                            "phone": "string"
                        }
                    },
                    "education": [{
                        "institution": "string",
                        "degree": "string",
                        "graduationDate": "string", // Formato "MMM YYYY" (Ej: "Feb 2020")
                        "achievements": ["string"]
                    }],
                    "technicalSkills": {
                        "skills": ["string"]
                    },
                    "professionalExperience": [{
                        "company": "string",
                        "position": "string",
                        "period": {
                            "start": "string",
                            "end": "string"
                        },
                        "responsibilities": ["string"],
                        "location": "string"
                    }],
                    "certifications": [{
                        "name": "string",
                        "dateObtained": "string"
                    }],
                    "projects": [{
                        "name": "string",
                        "description": "string",
                        "technologies": ["string"]
                    }]
                }
                ```
                
                 **Reglas de contenido:**
                - **Formato de fechas:**
                  - Usar SOLO `MMM YYYY` (Ej: "Feb 2020", "Mar 2024").
                  - Si una fecha no está presente, OMITIR el campo completamente.
                  - Para la fecha actual usar exactamente: "%s".
                - **Habilidades técnicas (technicalSkills):**
                  - Cada entrada en `technicalSkills.skills` debe representar UNA habilidad, tecnología o herramienta.
                  - Se permiten nombres compuestos con mayúsculas y espacios si la tecnología lo requiere (Ej: "Spring Boot", "GitHub Actions").
                  - NO incluir descripciones largas, solo nombres de tecnologías o herramientas.
                  - Ejemplos válidos:
                    ```json
                    "technicalSkills": {
                        "skills": ["Java", "Python", "Spring Boot", "GitHub Actions", "Docker"]
                    }
                    ```
                - **Ordenamiento de datos:**
                  - `education` → Ordenar por `graduationDate` descendente.
                  - `professionalExperience` → Ordenar por `period.start` descendente.
                  - `certifications` → Ordenar por `dateObtained` descendente.
                - **Comentarios del usuario** (solo si aplican):
                  - `education`: Información académica adicional.
                  - `certifications`: Nuevas certificaciones.
                  - `technicalSkills`: Habilidades mencionadas.
                
                 **Validación final:**
                - El JSON debe ser totalmente parseable y sin errores.
                - No incluir ningún texto fuera de la estructura JSON.
                - Cumplir ESTRICTAMENTE con los formatos especificados.
                
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
        DateTimeFormatter formatter = DateTimeFormatter
            .ofPattern("MMM yyyy", new Locale("es"))
            .withLocale(Locale.forLanguageTag("es"));

        String formattedDate = now.format(formatter);
        String month = formattedDate.substring(0, 3);
        String year = formattedDate.substring(4);

        month = month.substring(0, 1).toUpperCase() + month.substring(1).toLowerCase();

        return month + " " + year;
    }
}
