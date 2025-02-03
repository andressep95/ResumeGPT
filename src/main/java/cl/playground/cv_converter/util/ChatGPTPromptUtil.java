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
                        "graduationDate": "MMM YYYY",
                        "achievements": ["string"]
                    }],
                    "technicalSkills": {"skills": ["string"]},
                    "professionalExperience": [{
                        "company": "string",
                        "position": "string",
                        "period": {"start": "MMM YYYY", "end": "MMM YYYY"},
                        "responsibilities": ["string"],
                        "location": "string"
                    }],
                    "certifications": [{"name": "string", "dateObtained": "MMM YYYY"}],
                    "projects": [{"name": "string", "description": "string", "technologies": ["string"]}]
                }
                
                2. Reglas de contenido OBLIGATORIAS:
                - Traducir TODO al %s
                - FORMATO DE FECHAS:
                  • ÚNICAMENTE formato "MMM YYYY" (Ej: Feb 2020, Mar 2024)
                  • Para fecha actual usar el mes y año actual (NO usar "present", "actual", "current" ni similares)
                  • MMM = Tres letras de mes con primera mayúscula
                  • YYYY = Año en 4 dígitos
                  • Un espacio entre MMM y YYYY
                - ORDENAMIENTO:
                  • Education: ordenar por graduationDate descendente
                  • ProfessionalExperience: ordenar por period.start descendente
                  • Certifications: ordenar por dateObtained descendente
                - DATOS:
                  • NO incluir campos vacíos, nulls o "Not Provided"
                  • NO incluir arrays vacíos []
                  • Omitir objetos completos si falta información requerida
                - INTEGRACIÓN DE COMENTARIOS solo en:
                  • education: información académica adicional
                  • certifications: nuevas certificaciones
                  • technicalSkills: habilidades mencionadas
                
                3. Validación ESTRICTA:
                - JSON debe ser 100% parseable
                - Sin texto fuera de la estructura JSON
                - Mantener precisión absoluta con datos originales
                - Cumplir ESTRICTAMENTE el formato de fechas especificado
                - Verificar ordenamiento descendente en todas las secciones requeridas
                
                CV: %s
                Comentarios: %s
                """,
            targetLanguage.toUpperCase(),
            targetLanguage.toUpperCase(),
            resumeText,
            comments != null ? comments : "Ninguno");
    }
}