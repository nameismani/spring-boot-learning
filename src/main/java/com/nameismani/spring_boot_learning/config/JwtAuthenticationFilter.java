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

              // ✅ HANDLE OPTIONS PREFLIGHT FIRST
    if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
        response.setStatus(HttpServletResponse.SC_OK);
        addCorsHeaders(response);
        return;
    }
    
    addCorsHeaders(response);  // ✅ Add CORS to ALL responses
    
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
    System.out.print(currentUser.getId() + "asfdadf");
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

private boolean isAdminRoute(String path, String method) {
    return getMatchingPermissions(path, method).stream()
        .anyMatch(p -> p.name().startsWith("ADMIN_"));
}

private boolean isPublicRoute(String path, String method) {
    return getMatchingPermissions(path, method).stream()
         .anyMatch(p -> p.name().startsWith("PUBLIC_"));
}

private List<RoutePermission> getMatchingPermissions(String path, String method) {
    return Arrays.stream(RoutePermission.values())
        .filter(p -> p.matches(path, method))
        .collect(Collectors.toList());
}

private void addCorsHeaders(HttpServletResponse response) {
    response.setHeader("Access-Control-Allow-Origin", "http://localhost:4200");
    response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
    response.setHeader("Access-Control-Allow-Headers", "Authorization, Content-Type, *");
    response.setHeader("Access-Control-Max-Age", "3600");
}

}
