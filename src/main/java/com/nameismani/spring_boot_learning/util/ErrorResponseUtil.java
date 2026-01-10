package com.nameismani.spring_boot_learning.util;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ErrorResponseUtil {
    
    public static void sendUnauthorized(HttpServletResponse response, String message) throws IOException {
        sendError(response, 401, message);
    }
    
    public static void sendForbidden(HttpServletResponse response, String message) throws IOException {
        sendError(response, 403, message);
    }
    
    public static void sendBadRequest(HttpServletResponse response, String message) throws IOException {
        sendError(response, 400, message);
    }
    
    public static void sendNotFound(HttpServletResponse response, String message) throws IOException {
        sendError(response, 404, message);
    }
    
    public static void sendInternalError(HttpServletResponse response, String message) throws IOException {
        sendError(response, 500, message);
    }
    
    // Generic error method
    private static void sendError(HttpServletResponse response, int status, String message) throws IOException {
        response.setStatus(status);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(String.format(
            "{\"timestamp\":\"%s\",\"status\":%d,\"error\":\"%s\"}",
            java.time.Instant.now(),
            status,
            message.replace("\"", "\\\"") // Escape quotes
        ));
    }
}
