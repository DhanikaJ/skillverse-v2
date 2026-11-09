package com.skillverse.demo;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Set;

/**
 * Makes the public demo administrator read-only.
 *
 * The demo password is public, so the demo admin may look at everything but must not change
 * anything. Registered in the security chain right after the JWT filter; it does nothing
 * unless demo mode is enabled.
 */
public class DemoReadOnlyFilter extends OncePerRequestFilter {

    private static final Set<String> SAFE_METHODS = Set.of("GET", "HEAD", "OPTIONS");

    private final DemoProperties demoProperties;

    public DemoReadOnlyFilter(DemoProperties demoProperties) {
        this.demoProperties = demoProperties;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        if (demoProperties.isEnabled() && !SAFE_METHODS.contains(request.getMethod())) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && authentication.isAuthenticated()
                    && demoProperties.isDemoAdmin(authentication.getName())) {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                response.setCharacterEncoding(StandardCharsets.UTF_8.name());
                response.getWriter().write(
                        "{\"error\":\"Forbidden\",\"status\":403,"
                                + "\"message\":\"The demo administrator account is read-only\"}");
                return;
            }
        }
        filterChain.doFilter(request, response);
    }
}
