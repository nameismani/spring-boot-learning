package com.nameismani.spring_boot_learning.config;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.nameismani.spring_boot_learning.entity.UserEntity;
import com.nameismani.spring_boot_learning.repository.UserRepository;

import com.nameismani.spring_boot_learning.util.ErrorResponseUtil;
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtSecurity jwtSecurity;
    @Autowired
    private UserRepository userRepository;



// Replace your doFilterInternal() with this:
@Override
protected void doFilterInternal(HttpServletRequest request, 
                              HttpServletResponse response, 
                              FilterChain filterChain)
        throws ServletException, IOException {
    
    String path = request.getRequestURI();
    String method = request.getMethod();

    // ✅ Public routes
    if (isPublicRoute(path, method)) {
        filterChain.doFilter(request, response);
        return;
    }

    // ✅ No token
    String authHeader = request.getHeader("Authorization");
    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
        ErrorResponseUtil.sendUnauthorized(response, "Missing or invalid token");
        return;
    }

    // ✅ Invalid token / user not found
    String token = authHeader.substring(7);
    UserEntity currentUser = authenticateToken(token);
    if (currentUser == null) {
        ErrorResponseUtil.sendUnauthorized(response, "Invalid or expired token");
        return;
    }
    
    request.setAttribute("currentUser", currentUser);

    // ✅ Admin check
    if (isAdminRoute(path, method) && !"ADMIN".equals(currentUser.getRole())) {
        ErrorResponseUtil.sendForbidden(response, "Admin access required");
        return;
    }

    filterChain.doFilter(request, response);
}

// ✅ Helper: Authenticate token → null if invalid
private UserEntity authenticateToken(String token) {
    try {
        Long userId = jwtSecurity.extractUserId(token);
        if (userId == null) return null;
        
        UserEntity user = userRepository.findById(userId).orElse(null);
        if (user == null) return null;
        
        return jwtSecurity.validateToken(token, user) ? user : null;
    } catch (Exception e) {
        return null;
    }
}

// ✅ Helper: Send JSON error response
private void sendError(HttpServletResponse response, int status, String message) throws IOException {
    response.setStatus(status);
    response.setContentType("application/json");
    response.getWriter().write("{\"error\":\"" + message + "\"}");
}

private boolean isAdminRoute(String path, String method) {
    return getMatchingPermissions(path, method).stream()
        .anyMatch(p -> p.name().startsWith("ADMIN_"));
}

private boolean isPublicRoute(String path, String method) {
    return getMatchingPermissions(path, method).stream()
        .anyMatch(p -> !p.name().startsWith("ADMIN_"));
}

private List<RoutePermission> getMatchingPermissions(String path, String method) {
    return Arrays.stream(RoutePermission.values())
        .filter(p -> p.matches(path, method))
        .collect(Collectors.toList());
}

}
