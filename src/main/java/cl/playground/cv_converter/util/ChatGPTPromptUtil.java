package cl.playground.cv_converter.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

public class ChatGPTPromptUtil {
    
    public static String createPrompt(String resumeText, String language, String comments) {
        String targetLanguage = "es".equalsIgnoreCase(language) ? "español" : "inglés";
        String currentDate = getCurrentDate();

        return String.format("""
                Genera un JSON válido usando EXCLUSIVAMENTE los datos presentes en el CV.
                Toda la RESPUESTA debe ser en IDIOMA %s.
                
                REGLAS GENERALES:
                - NO inventes información.
                - NO incluyas valores nulos, vacíos, "Not Provided", [], {}, null.
                - Usa el formato EXACTO requerido en cada campo.
                
                📌 FORMATO ESPERADO OBLIGATORIO:
                ```json
                {
                  "header": {
                    "name": "string",
                    "contact": {
                      "email": "string",
                      "phone": "string"
                    }
                  },
                  "education": [
                    {
                      "institution": "string",
                      "degree": "string",
                      "graduationDate": "MM YYYY",
                      "achievements": ["string"]
                    }
                  ],
                  "technicalSkills": {
                    "skills": ["string"]
                  },
                  "professionalExperience": [
                    {
                      "company": "string",
                      "position": "string",
                      "period": {
                        "start": "MM YYYY",
                        "end": "MM YYYY"
                      },
                      "responsibilities": ["string"],
                      "location": "string"
                    }
                  ],
                  "certifications": [
                    {
                      "name": "string",
                      "dateObtained": "MM YYYY"
                    }
                  ],
                  "projects": [
                    {
                      "name": "string",
                      "description": "string",
                      "technologies": ["string"]
                    }
                  ]
                }
                ```
                
                **REGLAS ESPECÍFICAS**:
                - **Las fechas SIEMPRE deben estar en el formato MM YYYY** (ejemplo: 02 2020).
                  Si no hay una fecha específica, omite el campo.
                - **No uses "Presente" ni "Actual" en lugar de fechas**, usa "MM YYYY" o simplemente no incluyas el campo.
                - **technicalSkills**:
                  - Incluir **ÚNICAMENTE** habilidades técnicas específicas como:
                    - Lenguajes de programación (ej: Java, Python)
                    - Frameworks/Librerías (ej: Spring, React)
                    - Herramientas/Plataformas (ej: AWS, Docker, Git)
                    - Bases de datos (ej: MySQL, MongoDB)
                    - Sistemas operativos (ej: Linux, Windows)
                  - **Excluir expresamente**:
                    - Idiomas (ej: inglés, español, francés)
                    - Habilidades blandas (ej: trabajo en equipo, comunicación, liderazgo)
                    - Cursos o certificaciones (deben ir en sus secciones correspondientes)
                  - Mantener el nombre original de la habilidad tal como aparece en el CV
                
                📄 **CV:**
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
