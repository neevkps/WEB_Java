package com.wearetrying.space_cats_market.configuration;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class ApiKeyAuthenticationFilter extends OncePerRequestFilter {

    private static final String HEADER_NAME = "X-COSMO-KEY";
    private static final String VALID_API_KEY = "meow-secret-key-123";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String apiKey = request.getHeader(HEADER_NAME);


        if (apiKey == null || !apiKey.equals(VALID_API_KEY)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write("{\"error\": \"Access Denied: Invalid or missing Cosmo-Key. Only cats allowed!\"}");
            return;
        }

       Authentication authentication = new UsernamePasswordAuthenticationToken(
                "CosmoCat",
                apiKey,
               AuthorityUtils.createAuthorityList("ROLE_ADMIN", "ROLE_USER", "ROLE_MANAGER")
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        filterChain.doFilter(request, response);
    }
}