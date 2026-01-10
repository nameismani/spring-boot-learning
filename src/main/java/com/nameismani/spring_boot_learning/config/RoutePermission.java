package com.nameismani.spring_boot_learning.config;

public enum RoutePermission {
   // ✅ ONLY these are PUBLIC
    PUBLIC_AUTH("/api/auth/**", "ALL"),
    PUBLIC_POST_USERS("/api/users", "POST"),
    

    
    // Admin only
    ADMIN_DELETE("/api/users/**", "DELETE"),
    ADMIN_PUT("/api/users/**", "PUT"),
    ADMIN_GET_ALL("/api/users", "GET");
    
    private final String pathPattern;
    private final String method;
    
    RoutePermission(String pathPattern, String method) {
        this.pathPattern = pathPattern;
        this.method = method;
    }
    
    public String getPathPattern() { return pathPattern; }
    public String getMethod() { return method; }
    
    public boolean matches(String path, String method) {
        boolean methodMatches = "ALL".equals(this.method) || this.method.equals(method);
        if (!methodMatches) return false;
        
        if (this.pathPattern.endsWith("/**")) {
            String prefix = this.pathPattern.replace("/**", "");
            return path.startsWith(prefix);
        }
        return path.equals(this.pathPattern);
    }
}
