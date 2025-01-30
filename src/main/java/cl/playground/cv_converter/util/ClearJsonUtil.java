package cl.playground.cv_converter.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ClearJsonUtil {

    public static String cleanJsonResponse(String response) {
        // Eliminar bloques de código markdown si existen
        response = response.replaceAll("```json\\s*", "").replaceAll("```\\s*", "");

        // Si el JSON está envuelto en backticks simples, los removemos
        response = response.replaceAll("`", "");

        // Eliminar espacios en blanco al inicio y final
        response = response.trim();

        // Si después de la limpieza no comienza con { o [, buscar el primer JSON válido
        if (!response.startsWith("{") && !response.startsWith("[")) {
            Pattern pattern = Pattern.compile("\\{.*\\}");
            Matcher matcher = pattern.matcher(response);
            if (matcher.find()) {
                response = matcher.group();
            }
        }

        return response;
    }
}
