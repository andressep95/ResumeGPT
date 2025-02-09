package cl.playground.cv_converter.util;

public class StaticsPrompts {

    public static final String SYSTEM_CONTEXT = """
            Eres un asistente especializado en estructurar currículums en formato JSON.
            Tu fuerte es extraer la informacion de los curriculums y estructurarla en el formato requerido
            permitiendo que tengan una relevancia correcta.
        
            📌 **Reglas de generación**:
            - **NO inventes información**.
            - **NO incluyas valores nulos, vacíos, "Not Provided", [], {}, null**.
            - **Si falta un dato en el CV, omítelo** en lugar de agregar un valor incorrecto.
            - **Las fechas deben seguir el formato "MM YYYY"** (Ejemplo: "06 2015").
            - **SOLO usar Present o Presente en caso de que los periodos de fechas sigan en curso.
            - **Los nombres de habilidades técnicas deben coincidir con los del CV** (ejemplo: "Java", "React").
            - **Las respuestas deben estar en el idioma solicitado** sin mezclar idiomas.
        
            📌 **Formato JSON esperado**:
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
        """;

    public static final String EXAMPLE_OUTPUT = """
            📌 **Ejemplo de salida esperada**:
        
            ```json
            {
                "header": {
                  "name": "Andres Sepulveda",
                  "contact": {
                    "email": "andressep.95@gmail.com",
                    "phone": "+56 9 3707 8878",
                    "linkedin": "linkedin.com/in/andressep",
                    "location": "La Florida, Santiago de Chile, Región Metropolitana"
                  }
                },
                "education": [
                  {
                    "institution": "Instituto Profesional AIEP",
                    "degree": "Analista Programador Computacional",
                    "graduationDate": "12 2025",
                    "achievements": ["Graduado con honores en desarrollo de software y arquitecturas modernas"]
                  },
                  {
                    "institution": "Universidad de Chile",
                    "degree": "Ingeniería en Informática",
                    "graduationDate": "12 2020",
                    "achievements": ["Proyecto destacado en inteligencia artificial aplicada"]
                  }
                ],
                "technicalSkills": {
                  "skills": ["Java", "Spring Boot", "APIs REST", "Microservicios", "React", "Angular", "Thymeleaf", "Bootstrap", "PostgreSQL", "MySQL", "Redis", "Docker", "Git", "Scrum", "AWS", "Kubernetes", "Jenkins", "GraphQL"]
                },
                "professionalExperience": [
                  {
                    "company": "Edutecno",
                    "position": "Instructor de Bootcamp Full Stack Java (Diurno)",
                    "period": {
                      "start": "07 2024",
                      "end": "11 2024"
                    },
                    "responsibilities": [
                      "Diseñé e implementé un programa integral de desarrollo Full Stack con Java, JSP y Spring Boot",
                      "Instruí a estudiantes en tecnologías web frontend como Thymeleaf y Bootstrap",
                      "Implementé proyectos prácticos con APIs REST y bases de datos relacionales",
                      "Promoví metodologías ágiles (Scrum) y mejores prácticas en el desarrollo colaborativo"
                    ],
                    "location": "Santiago, Chile"
                  },
                  {
                    "company": "Edutecno",
                    "position": "Instructor de Bootcamp Full Stack Java (Vespertino)",
                    "period": {
                      "start": "10 2024",
                      "end": "Presente"
                    },
                    "responsibilities": [
                      "Capacito en desarrollo de aplicaciones Full Stack utilizando Java y Spring Boot",
                      "Facilito la construcción de bases de datos con PostgreSQL y MySQL",
                      "Lidero proyectos prácticos con énfasis en APIs REST y microservicios"
                    ],
                    "location": "Santiago, Chile"
                  },
                  {
                    "company": "Instituto Praxis",
                    "position": "Instructor de Bootcamp Aplicaciones Java (Vespertino)",
                    "period": {
                      "start": "04 2024",
                      "end": "09 2024"
                    },
                    "responsibilities": [
                      "Impartí formación técnica en desarrollo Full Stack con enfoque en Spring Boot",
                      "Guié a estudiantes en la implementación de aplicaciones web y control de versiones con Git",
                      "Diseñé casos prácticos para fortalecer habilidades de desarrollo ágil"
                    ],
                    "location": "Santiago, Chile"
                  },
                  {
                    "company": "Empresa XYZ",
                    "position": "Desarrollador Full Stack",
                    "period": {
                      "start": "01 2021",
                      "end": "06 2024"
                    },
                    "responsibilities": [
                      "Desarrollo de aplicaciones web utilizando Java, Spring Boot y Angular",
                      "Optimización de consultas SQL en bases de datos PostgreSQL y MySQL",
                      "Implementación de pipelines CI/CD con Jenkins y Docker",
                      "Desarrollo de microservicios escalables para aplicaciones empresariales"
                    ],
                    "location": "Santiago, Chile"
                  }
                ],
                "certifications": [
                  {
                    "name": "AWS Certified Developer",
                    "dateObtained": "05 2022"
                  },
                  {
                    "name": "Scrum Master Certified",
                    "dateObtained": "08 2023"
                  }
                ],
                "projects": [
                  {
                    "name": "SQLift - Generador de Clases JPA",
                    "description": "Herramienta CLI en Java para generar clases JPA/Hibernate desde DDL de PostgreSQL",
                    "technologies": ["Java", "GraalVM", "Docker", "Hibernate", "Spring Boot"]
                  },
                  {
                    "name": "E-commerce API",
                    "description": "Desarrollo de una API REST para una tienda en línea, soportando pagos y gestión de inventario",
                    "technologies": ["Spring Boot", "GraphQL", "Redis", "PostgreSQL"]
                  },
                  {
                    "name": "Sistema de Gestión de Bootcamps",
                    "description": "Plataforma web para administrar cursos, inscripciones y evaluaciones de estudiantes",
                    "technologies": ["Angular", "Node.js", "MongoDB", "Docker"]
                  }
                ]
              }
            ```
        """;

    public static final String VALIDATION_INSTRUCTIONS = """
            📌 **Verificación de respuesta**:
            Antes de devolver la respuesta, revisa que:
            - El JSON **sea válido** y estructurado correctamente.
            - **NO contenga campos vacíos ni valores nulos**.
            - **Las fechas sigan el formato "MM YYYY"**.
            - **Los nombres de habilidades coincidan con los originales en el CV**.
            - **Toda la respuesta esté en el idioma solicitado**.
        """;


}
