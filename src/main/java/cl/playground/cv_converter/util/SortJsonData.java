package cl.playground.cv_converter.util;

import cl.playground.cv_converter.model.Certification;
import cl.playground.cv_converter.model.Education;
import cl.playground.cv_converter.model.Resume;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

public class SortJsonData {

    private static final String DEFAULT_INVALID_DATE = "000000";
    private static final Set<String> PRESENT_KEYWORDS = new HashSet<>(Arrays.asList("present", "presente"));
    private static final DateTimeFormatter INPUT_FORMATTER = DateTimeFormatter.ofPattern("MM yyyy");
    private static final DateTimeFormatter OUTPUT_FORMATTER = DateTimeFormatter.ofPattern("yyyyMM");

    public static void sortData(Resume resume, String language) {
        if (resume == null) {
            return;
        }

        if (resume.getEducation() != null) {
            // dar formato para ordenar
            resume.getEducation().forEach(edu ->
                edu.setGraduationDate(convertDateToSortableFormat(edu.getGraduationDate()))
            );
            // ordenar
            resume.getEducation().sort(Comparator.comparing(
                Education::getGraduationDate, Comparator.reverseOrder()
            ));
            // retornar a formato legible
            resume.getEducation().forEach(edu ->
                edu.setGraduationDate(convertToReadableFormat(edu.getGraduationDate(), language))
            );
        }

        if (resume.getProfessionalExperience() != null) {
            // dar formato a la data
            resume.getProfessionalExperience().forEach(exp -> {
                exp.getPeriod().setStart(convertDateToSortableFormat(exp.getPeriod().getStart()));
                exp.getPeriod().setEnd(convertDateToSortableFormat(exp.getPeriod().getEnd()));
            });
            // ordenar data
            resume.getProfessionalExperience().sort(Comparator.comparing(
                exp ->
                    exp.getPeriod().getStart(),
                    Comparator.reverseOrder()
            ));
            // reordenar a formato legible
            resume.getProfessionalExperience().forEach(exp -> {
                    exp.getPeriod().setStart(convertToReadableFormat(exp.getPeriod().getStart(), language));
                    exp.getPeriod().setEnd(convertToReadableFormat(exp.getPeriod().getEnd(), language));
                }
            );
        }

        if (resume.getCertifications() != null) {
            // dar formato a la data
            resume.getCertifications().forEach(cert ->
                cert.setDateObtained(convertDateToSortableFormat(cert.getDateObtained()))
            );

            resume.getCertifications().sort(Comparator.comparing(
                Certification::getDateObtained,
                Comparator.reverseOrder()
            ));

            resume.getCertifications().forEach(cert ->
                    cert.setDateObtained(convertToReadableFormat(cert.getDateObtained(), language))
            );
        }
    }

    private static String convertDateToSortableFormat(String date) {
        if (date == null || date.trim().isEmpty()) {
            return getCurrentMonthAndYear();
        }

        // Normaliza la fecha si es "Present" o "Presente"
        String normalizedDate = date.trim().toLowerCase();
        if (PRESENT_KEYWORDS.contains(normalizedDate)) {
            normalizedDate = getCurrentMonthAndYear();
        }

        try {
            // Parsea la fecha y la convierte al formato deseado
            LocalDate parsedDate = LocalDate.parse("01 " + normalizedDate,
                DateTimeFormatter.ofPattern("dd MM yyyy"));
            return parsedDate.format(OUTPUT_FORMATTER);
        } catch (Exception e) {
            return DEFAULT_INVALID_DATE;
        }
    }

    private static String convertToReadableFormat(String date, String language) {
        if (date == null || date.trim().isEmpty()) {
            return date;
        }

        String normalizedDate = date.trim();

        // Si es Present/Presente, primero convertimos a formato sortable
        if ("present".equalsIgnoreCase(normalizedDate) ||
            "presente".equalsIgnoreCase(normalizedDate)) {
            normalizedDate = convertDateToSortableFormat(normalizedDate);
        }

        String[] monthsEn = {"Jan", "Feb", "Mar", "Apr", "May", "Jun",
            "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};

        String[] monthsEs = {"Ene", "Feb", "Mar", "Abr", "May", "Jun",
            "Jul", "Ago", "Sep", "Oct", "Nov", "Dic"};

        try {
            String month;
            String year;

            if (normalizedDate.matches("\\d{6}")) { // Formato yyyyMM
                year = normalizedDate.substring(0, 4);
                month = normalizedDate.substring(4);
            } else if (normalizedDate.matches("\\d{2} \\d{4}")) { // Formato MM yyyy
                String[] parts = normalizedDate.split(" ");
                month = parts[0];
                year = parts[1];
            } else {
                return date;
            }

            int monthNum = Integer.parseInt(month);
            if (monthNum < 1 || monthNum > 12) {
                return date;
            }

            String[] months = language.equalsIgnoreCase("es") ? monthsEs : monthsEn;
            return months[monthNum - 1] + " " + year;
        } catch (Exception e) {
            return date;
        }
    }
    private static String getCurrentMonthAndYear() {
        return LocalDate.now().format(INPUT_FORMATTER);
    }
}
