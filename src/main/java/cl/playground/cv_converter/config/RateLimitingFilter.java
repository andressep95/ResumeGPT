package cl.playground.cv_converter.config;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class RateLimitingFilter implements Filter {

    private final Map<String, String> requestData = new ConcurrentHashMap<>();
    private final int MAX_REQUEST = 3;
    private final int TIME_WINDOW = 60000; // 60 segundos

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
        throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String clientKey = httpRequest.getRemoteAddr() + ":" + httpRequest.getRequestURI();
        long currentTime = System.currentTimeMillis();

        requestData.compute(clientKey, (key, value) -> {
            if (value == null) {
                return "1:" + currentTime;
            }

            String[] parts = value.split(":");
            int count = Integer.parseInt(parts[0]);
            long lastRequestTime = Long.parseLong(parts[1]);

            if (currentTime - lastRequestTime > TIME_WINDOW) {
                return "1:" + currentTime;
            } else {
                return (count + 1) + ":" + lastRequestTime;
            }
        });

        String[] currentValues = requestData.get(clientKey).split(":");
        int currentCount = Integer.parseInt(currentValues[0]);

        if (currentCount > MAX_REQUEST) {
            ((HttpServletResponse) response).setStatus(429);
            response.getWriter().write("Too many requests");
            return;
        }

        chain.doFilter(request, response);
    }
}