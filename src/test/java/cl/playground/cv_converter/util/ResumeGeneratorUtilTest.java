package cl.playground.cv_converter.util;

import cl.playground.cv_converter.model.*;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class ResumeGeneratorUtilTest {

    public static Resume buildTestResume() {
        // Header
        Header header = new Header(
            "Andres Sepulveda",
            new Contact("andressep.95@gmail.com", "+56 9 3707 8878")
        );

        // Education 1
        Education education1 = new Education(
            "Instituto Profesional AIEP",
            "Computational Programmer Analyst",
            "Present",
            Collections.emptyList(),
            Collections.emptyList()
        );

        // Education 2 - GitHub Certification
        Education education2 = new Education(
            "GitHub Foundations Certification",
            "Git & Version Control Fundamentals",
            "Enero 2025",
            Arrays.asList(
                "Mastered essential Git commands and workflows",
                "Understood GitHub repository management",
                "Implemented CI/CD pipelines with GitHub Actions"
                         ),
            Collections.emptyList()
        );

        // Technical Skills
        SkillCategory programmingSkills = new SkillCategory(
            Arrays.asList(
                "Java", "Spring Boot", "APIs REST", "Microservices",
                "React", "Angular", "Thymeleaf", "Bootstrap",
                "PostgreSQL", "MySQL", "Redis", "Docker", "Git", "Scrum"
                         )
        );

        TechnicalSkills technicalSkills = new TechnicalSkills(
            Collections.singletonList(programmingSkills)
        );

        // Professional Experience 1
        ProfessionalExperience exp1 = new ProfessionalExperience(
            "Edutecno",
            "Not Provided",
            "Senior Instructor of Full Stack Java Bootcamp (Daytime)",
            new Period("Julio 2024", "Noviembre 2024"),
            Arrays.asList(
                "Designed and implemented a comprehensive Full Stack development program with Java, JSP, and Spring Boot.",
                "Instructed students in frontend web technologies such as Thymeleaf and Bootstrap.",
                "Implemented practical projects with REST APIs and relational databases.",
                "Promoted agile methodologies (Scrum) and best practices in collaborative development."
                         )
        );

        // Professional Experience 2
        ProfessionalExperience exp2 = new ProfessionalExperience(
            "Edutecno",
            "Not Provided",
            "Instructor of Full Stack Java Bootcamp (Evening)",
            new Period("Octubre 2024", "Present"),
            Arrays.asList(
                "I train in Full Stack application development using Java and Spring Boot.",
                "I facilitate the construction of databases with PostgreSQL and MySQL.",
                "I lead practical projects with an emphasis on REST APIs and microservices."
                         )
        );

        // Professional Experience 3
        ProfessionalExperience exp3 = new ProfessionalExperience(
            "Instituto Praxis",
            "Not Provided",
            "Instructor of Java Applications Bootcamp (Evening)",
            new Period("Abril 2024", "Agosto 2024"),
            Arrays.asList(
                "I imparted technical training in Full Stack development with a focus on Spring Boot.",
                "I guided students in the implementation of web applications and version control with Git.",
                "I designed practical cases to strengthen agile development skills."
                         )
        );

        // Certifications
        Certification cert1 = new Certification("GitHub Foundations Certification", "Julio 2024");
        Certification cert2 = new Certification("Oracle Certified Java Programmer (OCPJP)", "Enero 2025");

        return new Resume(
            header,
            Arrays.asList(education1, education2), // Se agregan ambas formaciones
            technicalSkills,
            Arrays.asList(exp1, exp2, exp3),
            Arrays.asList(cert1, cert2)
        );
    }


    @Test
    void printResume() {
        Resume resume = buildTestResume();
        System.out.println(resume);
    }

    @Test
    void generatePdfAndSaveToResources() throws Exception {
        // 1. Construir objeto Resume de prueba
        Resume testResume = buildTestResume();

        // 2. Generar PDF
        String language = "es";
        byte[] pdfBytes = ResumeGeneratorUtil.generateResumePDF(testResume, language);

        // 3. Crear directorios si no existen
        Path outputPath = Paths.get("src/test/resources/generated_cv.pdf");
        Files.createDirectories(outputPath.getParent());

        // 4. Guardar en resources para inspección
        Files.write(outputPath, pdfBytes);

        // 5. Verificaciones
        assertNotNull(pdfBytes, "El PDF no debe ser nulo");
        assertTrue(pdfBytes.length > 0, "El PDF debe tener contenido");

        System.out.println("PDF generado en: " + outputPath.toAbsolutePath());
    }

}