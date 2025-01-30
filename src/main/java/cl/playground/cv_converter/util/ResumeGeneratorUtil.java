package cl.playground.cv_converter.util;

import cl.playground.cv_converter.model.*;
import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.properties.TextAlignment;

import java.io.ByteArrayOutputStream;
import java.io.IOException;


public class ResumeGeneratorUtil {

    // Encabezados de secciones
    private static final PdfFont FONT_HEADER_SECTION;
    private static final float HEADER_SIZE = 14;

    // Encabezados de elementos
    private static final PdfFont FONT_HEADER_ELEMENT;
    private static final float HEADER_SIZE_ELEMENT = 11;


    private static final PdfFont FONT_BODY;

    static {
        try {
            FONT_HEADER_SECTION = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);
            FONT_HEADER_ELEMENT = PdfFontFactory.createFont(StandardFonts.HELVETICA);
            FONT_BODY = PdfFontFactory.createFont(StandardFonts.HELVETICA);
        } catch (IOException e) {
            throw new RuntimeException("Error initializing fonts", e);
        }
    }

    private enum SectionTitles {
        EDUCATION("Education", "Educación"),
        TECHNICAL_SKILLS("Technical Skills", "Habilidades Técnicas"),
        PROFESSIONAL_EXPERIENCE("Professional Experience", "Experiencia Profesional"),
        CERTIFICATIONS("Certifications", "Certificaciones"),
        PROJECTS("Projects", "Proyectos");

        private final String english;
        private final String spanish;

        SectionTitles(String english, String spanish) {
            this.english = english;
            this.spanish = spanish;
        }

        public String getTitle(String language) {
            if (language == null) return english;
            return language.trim().equalsIgnoreCase("es") ? spanish : english;
        }
    }

    public static byte[] generateResumePDF(Resume resume, String language) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            PdfWriter writer = new PdfWriter(baos);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf, PageSize.A4);  // Página tamaño A4
            document.setMargins(20, 60, 50, 60);


            addHeader(document, resume.getHeader());
            addEducation(document, resume.getEducation(), language);
            addTechnicalSkills(document, resume.getTechnicalSkills(), language);
            addProfessionalExperience(document, resume.getProfessionalExperience(), language);
            addCertifications(document, resume.getCertifications(), language);


            document.close();
            return baos.toByteArray();

        } catch (IOException e) {
            throw new RuntimeException("Error generating PDF", e);
        }
    }

    private static void addHeader(Document document, Header header) {
        if (header == null) {
            throw new IllegalArgumentException("Header cannot be null");
        }

        // Configurar el separador con desplazamiento vertical
        Text separator = new Text(" • ")
            .setFont(FONT_BODY)
            .setFontSize(18)
            .setTextRise(-3f);  // Ajuste para centrado vertical

        Paragraph headerParagraph = new Paragraph()
            .add(new Text(header.getName() + "\n")
                .setFont(FONT_HEADER_SECTION)
                .setFontSize(16))
            .add(new Text(header.getContact().getEmail())
                .setFont(FONT_BODY)
                .setFontSize(12))
            .add(separator)
            .add(new Text(header.getContact().getPhone())
                .setFont(FONT_BODY)
                .setFontSize(12))
            .setTextAlignment(TextAlignment.CENTER)
            .setMarginBottom(20f);

        document.add(headerParagraph);
    }

    private static void addEducation(Document document, java.util.List<Education> educationList, String language) {
        // Título centrado
        String title = SectionTitles.EDUCATION.getTitle(language);
        Paragraph sectionTitle = new Paragraph(title)
            .setFont(FONT_HEADER_SECTION)
            .setFontSize(HEADER_SIZE)
            .setTextAlignment(TextAlignment.CENTER)
            .setMarginBottom(-10f);

        // Línea de subrayado
        Paragraph underline = new Paragraph("_".repeat(85))
            .setFont(FONT_BODY)
            .setFontSize(10)
            .setTextAlignment(TextAlignment.CENTER)
            .setMarginBottom(12);

        document.add(sectionTitle);
        document.add(underline);

        for (Education education : educationList) {

            // Nombre de la institución en negrita
            Paragraph institution = new Paragraph(education.getInstitution())
                .setFont(FONT_HEADER_ELEMENT)
                .setFontSize(HEADER_SIZE_ELEMENT)
                .setBold()
                .setMarginBottom(-2f)
                .setMarginTop(-10f);

            document.add(institution);

            // Crear una tabla con 2 columnas
            Table detailsTable = new Table(2)
                .useAllAvailableWidth()
                .setMarginBottom(-8);

            // Información básica (Degree y Graduation Date)
            String degree = education.getDegree();
            String graduationDate = education.getGraduationDate();

            // Celda para Degree, alineada a la izquierda
            Cell degreeCell = new Cell().add(new Paragraph(degree)
                .setFont(FONT_BODY)
                .setFontSize(10)
                .setTextAlignment(TextAlignment.LEFT)
                .setMarginTop(0)
                .setMarginBottom(-2))
                .setBorder(Border.NO_BORDER);

            // Celda para Graduation Date, alineada a la derecha
            Cell dateCell = new Cell().add(new Paragraph(graduationDate)
                .setFont(FONT_BODY)
                .setFontSize(10)
                .setTextAlignment(TextAlignment.RIGHT)
                .setMarginTop(0)
                .setMarginBottom(-2))
                .setBorder(Border.NO_BORDER);


            // Agregar las celdas a la tabla
            detailsTable.addCell(degreeCell);
            detailsTable.addCell(dateCell);

            // Agregar la tabla al documento
            document.add(detailsTable);

            // Logros (Achievements)
            if (education.getAchievements() != null && !education.getAchievements().isEmpty()) {

                for (String achievement : education.getAchievements()) {
                    Paragraph item = new Paragraph()
                        .setMarginBottom(-10)
                        .add(new Text("•   ").setFontSize(14).setTextRise(-2f))  // Punto más grande
                        .add(new Text(achievement).setFont(FONT_BODY).setFontSize(10)); // Texto normal

                    document.add(item);
                }
            }

            // Proyectos (Projects)
            if (education.getProjects() != null && !education.getProjects().isEmpty()) {
                Paragraph projectsTitle = new Paragraph(SectionTitles.PROJECTS.getTitle(language) + ":")
                    .setFont(FONT_BODY)
                    .setFontSize(10)
                    .setMarginTop(8f)
                    .setMarginBottom(-9)
                    .setBold();

                document.add(projectsTitle);

                for (String project : education.getProjects()) {
                    Paragraph item = new Paragraph()
                        .add(new Text("•   ").setFontSize(14).setTextRise(-2f))  // Punto más grande
                        .add(new Text(project).setFont(FONT_BODY).setFontSize(10)); // Texto normal

                    document.add(item);
                }
            }

            // Espacio entre elementos
            document.add(new Paragraph("\n"));
        }
    }

    private static void addTechnicalSkills(Document document, TechnicalSkills technicalSkills, String language) {
        String title = SectionTitles.TECHNICAL_SKILLS.getTitle(language);
        Paragraph sectionTitle = new Paragraph(title)
            .setFont(FONT_HEADER_SECTION)
            .setFontSize(HEADER_SIZE)
            .setTextAlignment(TextAlignment.CENTER)
            .setMarginBottom(-10f);

        // Línea de subrayado
        Paragraph underline = new Paragraph("_".repeat(85))
            .setFont(FONT_BODY)
            .setFontSize(10)
            .setTextAlignment(TextAlignment.CENTER)
            .setMarginBottom(0);

        document.add(sectionTitle);
        document.add(underline);

        // Crear una tabla con 4 columnas
        Table skillsTable = new Table(4).useAllAvailableWidth();  // Cambiar a 4 columnas

        // Recorre las categorías y las habilidades
        for (SkillCategory category : technicalSkills.getCategories()) {
            // Recorre las habilidades dentro de la categoría
            for (String skill : category.getSkills()) {
                // Crear el punto con fuente 14
                Text bullet = new Text("• ")
                    .setFont(FONT_BODY)
                    .setFontSize(14)
                    .setTextRise(-2f);  // Ajusta la altura del punto

                // Crear el contenido con fuente 10
                Text skillText = new Text(skill)
                    .setFont(FONT_BODY)
                    .setFontSize(10);

                // Crear el párrafo con el punto y el texto
                Paragraph item = new Paragraph()
                    .add(bullet)
                    .add(skillText)
                    .setMarginBottom(-10);

                // Crear una celda
                Cell cell = new Cell().add(item);
                cell.setBorder(Border.NO_BORDER);

                // Agregar la celda a la tabla
                skillsTable.addCell(cell);
            }

        }

        document.add(skillsTable);
    }

    private static void addProfessionalExperience(Document document, java.util.List<ProfessionalExperience> experiences, String language) {
        String title = SectionTitles.PROFESSIONAL_EXPERIENCE.getTitle(language);
        Paragraph sectionTitle = new Paragraph(title)
            .setFont(FONT_HEADER_SECTION)
            .setFontSize(HEADER_SIZE)
            .setTextAlignment(TextAlignment.CENTER)
            .setMarginBottom(-10f)
            .setMarginTop(28f);

        // Línea de subrayado
        Paragraph underline = new Paragraph("_".repeat(85))
            .setFont(FONT_BODY)
            .setFontSize(10)
            .setTextAlignment(TextAlignment.CENTER)
            .setMarginBottom(0);

        document.add(sectionTitle);
        document.add(underline);

        for (ProfessionalExperience exp : experiences) {


            // Añadir la empresa
            Paragraph companyParagraph = new Paragraph(exp.getCompany())
                .setFont(FONT_HEADER_SECTION)
                .setFontSize(HEADER_SIZE_ELEMENT)
                .setMarginBottom(-6f)
                .setMarginBottom(-2);

            document.add(companyParagraph);

            // Crear una tabla con 2 columnas para la posición y la fecha
            Table expTable = new Table(2)
                .useAllAvailableWidth()
                .setMarginBottom(-2f);

            // Celda para la posición
            Cell positionCell = new Cell().add(new Paragraph(exp.getPosition())
                    .setFont(FONT_BODY)
                    .setFontSize(12))
                .setBorder(Border.NO_BORDER)
                .setTextAlignment(TextAlignment.LEFT);

            // Celda para la fecha, alineada a la derecha
            String dateRange = exp.getPeriod().getStart() + " - " + exp.getPeriod().getEnd();
            Cell dateCell = new Cell().add(new Paragraph(dateRange)
                    .setFont(FONT_BODY)
                    .setFontSize(10))
                .setBorder(Border.NO_BORDER)
                .setTextAlignment(TextAlignment.RIGHT);

            // Agregar las celdas a la tabla
            expTable.addCell(positionCell);
            expTable.addCell(dateCell);

            // Agregar la tabla al documento
            document.add(expTable);

            // Agregar responsabilidades en una lista
            List responsibilities = new List()
                .setSymbolIndent(12)
                .setListSymbol("\u2022")
                .setMarginBottom(6);

            for (String responsibility : exp.getResponsibilities()) {
                responsibilities.add(new ListItem(responsibility));
            }

            document.add(responsibilities);
        }
    }

    private static void addCertifications(Document document, java.util.List<Certification> certifications, String language) {
        String title = SectionTitles.CERTIFICATIONS.getTitle(language);
        Paragraph sectionTitle = new Paragraph(title)
            .setFont(FONT_HEADER_SECTION)
            .setFontSize(HEADER_SIZE)
            .setTextAlignment(TextAlignment.CENTER)
            .setMarginBottom(-10f);

        Paragraph underline = new Paragraph("_".repeat(85))
            .setFont(FONT_BODY)
            .setFontSize(10)
            .setTextAlignment(TextAlignment.CENTER)
            .setMarginBottom(12);

        document.add(sectionTitle);
        document.add(underline);

        Table table = new Table(2).useAllAvailableWidth();

        for (Certification certification : certifications) {
            Cell nameCell = new Cell().add(new Paragraph(certification.getName())
                    .setFont(FONT_BODY)
                    .setFontSize(10)
                    .setTextAlignment(TextAlignment.LEFT))
                .setBorder(Border.NO_BORDER);

            Cell dateCell = new Cell().add(new Paragraph(certification.getDateObtained())
                    .setFont(FONT_BODY)
                    .setFontSize(10)
                    .setTextAlignment(TextAlignment.RIGHT))
                .setBorder(Border.NO_BORDER);

            table.addCell(nameCell);
            table.addCell(dateCell);
        }

        document.add(table);
    }
}
