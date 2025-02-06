package cl.playground.cv_converter.service;

import cl.playground.cv_converter.model.Resume;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ResumeServiceImplTest {

    @Autowired
    private ResumeService resumeService;

    @Test
    void testProcessResume_RealIntegration() throws Exception {
        // 1. Cargar archivo PDF
        ClassPathResource resource = new ClassPathResource("static/Andres_Sepulveda_CV.pdf");

        // 2. Crear MultipartFile
        MockMultipartFile multipartFile = new MockMultipartFile(
            "file",
            resource.getFilename(),
            "application/pdf",
            resource.getInputStream()
        );

        // 3. Iniciar medición de tiempo
        Instant start = Instant.now();

        // 4. Procesar CV y esperar resultado
        CompletableFuture<Resume> futureCv = resumeService.processResumeData(
            multipartFile,
            "en",
            "Testing CV processing with additional skills in: Java, Spring Boot"
                                                                            );

        // 5. Obtener resultado con timeout
        Resume result = futureCv.get(30, TimeUnit.SECONDS);

        // 6. Calcular tiempo transcurrido
        Duration duration = Duration.between(start, Instant.now());
        System.out.println("Tiempo total de procesamiento: " + duration.toSeconds() + " segundos");

        // 7. Generar y guardar el PDF
        byte[] pdfBytes = resumeService.processResume(multipartFile, "en", "Testing CV processing");

        // Crear directorio si no existe
        Path outputDir = Paths.get("target", "test-output");
        Files.createDirectories(outputDir);

        // Guardar PDF
        Path pdfPath = outputDir.resolve("generated_cv_" + System.currentTimeMillis() + ".pdf");
        Files.write(pdfPath, pdfBytes);

        System.out.println("PDF generado en: " + pdfPath.toAbsolutePath());

        // 8. Verificaciones básicas
        assertNotNull(result, "El CV no debería ser null");
        assertTrue(pdfBytes.length > 0, "El PDF generado no debería estar vacío");
        assertTrue(Files.exists(pdfPath), "El archivo PDF debería existir");

        // 9. Imprimir información del resultado
        System.out.println("\nInformación del CV procesado:");
        System.out.println("Nombre: " + result.getHeader().getName());
        if (result.getProfessionalExperience() != null) {
            System.out.println("Experiencias profesionales: " + result.getProfessionalExperience().size());
        }
        if (result.getTechnicalSkills() != null && result.getTechnicalSkills().getSkills() != null) {
            System.out.println("Habilidades técnicas: " + result.getTechnicalSkills().getSkills().size());
        }
    }

    @Test
    void testProcessResume_PDFGeneration() throws Exception {
        // 1. Cargar archivo PDF desde classpath
        ClassPathResource resource = new ClassPathResource("static/Andres_Sepulveda_CV.pdf");

        // 2. Crear MultipartFile
        MockMultipartFile multipartFile = new MockMultipartFile(
            "file",
            resource.getFilename(),
            "application/pdf",
            resource.getInputStream()
        );

        // 3. Procesar CV y generar PDF
        byte[] pdfResult = resumeService.processResume(
            multipartFile,
            "en",
            "Tengo una educacion adicional como ingeniero en construccion civil en la Universidad de Oriente " +
                "culminada en Abril del 2018" +
                ".\n" +
                "Tengo certificaciones de Github Foundation en donde obtuve distintas habilidades como manejo de " +
                "repositorios, manejo de CI/CD y gestion de proyectos Octubre 2024.\n" +
                "Certificacion de Java Associate de Oracle tomando conocimientos basicos de programacion en el " +
                "lenguaje Enero 2025." +
                "Tengo tambien un proyecto llamado SQLift que toma codigo de sentencias DDL de bases de datos y construye las entidades en Java con JPA e Hibernate");

        // 4. Verificaciones básicas
        assertNotNull(pdfResult);
        assertTrue(pdfResult.length > 0);

        // 5. Verificar que el contenido es un PDF válido
        assertTrue(isPdfContent(pdfResult));

        // 6. Opcional: Guardar el PDF generado para inspección visual
        savePdfForInspection(pdfResult);
    }

    private boolean isPdfContent(byte[] content) {
        // Un PDF válido comienza con la firma "%PDF-"
        if (content.length < 5) return false;

        String header = new String(content, 0, 5);
        return header.startsWith("%PDF-");
    }

    private void savePdfForInspection(byte[] pdfContent) {
        try {
            // Crear directorio test-output si no existe
            Path outputDir = Paths.get("test-output");
            if (!Files.exists(outputDir)) {
                Files.createDirectories(outputDir);
            }

            // Guardar el archivo
            Path outputPath = outputDir.resolve("generated_test_resume.pdf");
            Files.write(outputPath, pdfContent);

            System.out.println("PDF guardado para inspección en: " + outputPath.toAbsolutePath());
        } catch (IOException e) {
            System.err.println("No se pudo guardar el PDF para inspección: " + e.getMessage());
        }
    }
}