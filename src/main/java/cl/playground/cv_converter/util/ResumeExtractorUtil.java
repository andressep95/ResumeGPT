package cl.playground.cv_converter.util;

import cl.playground.cv_converter.exception.PdfProcessingException;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

public class ResumeExtractorUtil {

    public static String extractResumeContent(MultipartFile file) {
        try (InputStream pdfStream = file.getInputStream()) {

            return extractWithPDFBox(pdfStream);
        } catch (IOException e) {
            throw new PdfProcessingException("Error processing pdf", e);
        }
    }

    private static String extractWithPDFBox(InputStream pdfStream) throws IOException {
        try (PDDocument document = Loader.loadPDF(pdfStream.readAllBytes())) {
            PDFTextStripper stripper = new PDFTextStripper();
            return stripper.getText(document);
        }
    }

}
