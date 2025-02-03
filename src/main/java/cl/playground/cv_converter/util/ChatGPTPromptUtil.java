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
                        "graduationDate": "string",
                        "achievements": ["string"]
                    }],
                    "technicalSkills": {"skills": ["string"]},
                    "professionalExperience": [{
                        "company": "string",
                        "position": "string",
                        "period": {"start": "string", "end": "string"},
                        "responsibilities": ["string"],
                        "location": "string"
                    }],
                    "certifications": [{"name": "string", "dateObtained": "string"}],
                    "projects": [{"name": "string", "description": "string", "technologies": ["string"]}]
                }
                
                2. Reglas de contenido OBLIGATORIAS:
                - Traducir TODO al idioma %s
                - FORMATO DE FECHAS (todas como string):
                  • ÚNICAMENTE formato "MMM YYYY" como string (Ej: "Feb 2020", "Mar 2024")
                  • Las fechas deben ir entre comillas como strings
                  • Para fecha actual usar el mes y año actual como string (NO usar "present", "actual", "current" ni similares)
                  • MMM = Tres letras de mes con primera mayúscula
                  • YYYY = Año en 4 dígitos
                  • Un espacio entre MMM y YYYY
                  • Ejemplos válidos: "Ene 2024", "Feb 2023", "Dic 2022"
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
                - JSON debe ser total y estrictamente parseable
                - Sin texto fuera de la estructura JSON
                - Mantener precisión absoluta con datos originales
                - Todas las fechas deben ser strings y cumplir ESTRICTAMENTE el formato especificado
                - Verificar ordenamiento descendente en todas las secciones requeridas
                
                CV: %s
                Comentarios: %s
                """,
            targetLanguage.toUpperCase(),
            targetLanguage.toUpperCase(),
            resumeText,
            comments != null ? comments : "Ninguno");
    }}