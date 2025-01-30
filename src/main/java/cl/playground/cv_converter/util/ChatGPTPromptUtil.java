package cl.playground.cv_converter.util;

public class ChatGPTPromptUtil {

    public static String createPrompt(String resumeText, String language, String comments) {
        String targetLanguage = "es".equalsIgnoreCase(language) ? "español" : "inglés";

        return String.format("""
                Generar JSON válido en idioma %s usando EXCLUSIVAMENTE datos del CV. Reglas estrictas:
                
                1. Estructura requerida (omitir secciones/objetos vacíos):
                {
                    "header": {"name": "string", "contact": {"email": "string", "phone": "string"}},
                    "education": [{
                        "institution": "string",
                        "degree": "string",
                        "graduationDate": "MesAbr AÑO",
                        "achievements": ["string"],
                        "projects": ["string"]
                    }],
                    "technicalSkills": {"categories": [{"skills": ["string"]}]},
                    "professionalExperience": [{
                        "company": "string",
                        "position": "string",
                        "period": {"start": "MesAbr AÑO", "end": "MesAbr AÑO"},
                        "responsibilities": ["string"],
                        "location": "string"
                    }],
                    "certifications": [{"name": "string", "dateObtained": "MesAbr AÑO"}]
                }
                
                2. Reglas de contenido:
                - Traducir TODO al %s
                - Prestar atencion a los proyectos
                - Fechas en formato MesAbr AÑO (Ej: Feb 2020)
                - Orden descendente por fechas
                - No campos vacíos, nulls, "Not Provided" o arrays vacíos
                - Si falta información obligatoria en objeto, omitirlo
                - Priorizar información CV, integrar comentarios solo en:
                  • education: información adicional académica
                  • certifications: nuevas certificaciones
                  • technicalSkills: habilidades mencionadas
                
                3. Validación:
                - JSON debe ser parseable
                - Sin comentarios/texto extraño
                - Máxima precisión con datos originales
                
                CV: %s
                Comentarios: %s
                """,
            targetLanguage.toUpperCase(),
            targetLanguage.toUpperCase(),
            resumeText,
            comments != null ? comments : "Ninguno");
    }}