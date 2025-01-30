package cl.playground.cv_converter.util;

import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class ResumeExtractorUtilTest {

    @Test
    void testExtractResumeContent_Success() throws IOException {
        // Cargar un archivo PDF de prueba desde los recursos del proyecto
        File pdfFile = new File("src/main/resources/static/Andres_Sepulveda_CV.pdf");
        String language = "Español";
        String comments = "comentarios de prueba para revisar en el prompt";

        FileInputStream inputStream = new FileInputStream(pdfFile);
        MultipartFile multipartFile = new MockMultipartFile("file", pdfFile.getName(), "application/pdf", inputStream);

        // Ejecutar el método de extracción
        String extractedText = ResumeExtractorUtil.extractResumeContent(multipartFile);
        System.out.println("Contenido extraído del CV:\n" + extractedText);

        // Validar que el texto extraído no sea nulo o vacío
        assertNotNull(extractedText);
        assertFalse(extractedText.isEmpty());
    }

}