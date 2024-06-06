package com.app_api.config;

import com.app_api.util.CurrentUserHolder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.security.Principal;

@Component
@RequiredArgsConstructor
public class CurrentUserFilter implements Filter {

    private final CurrentUserHolder currentUserHolder;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        Principal principal = httpRequest.getUserPrincipal();
        if (principal != null) {
            currentUserHolder.setCurrentPrincipal(principal);
        }

        try {
            chain.doFilter(request, response);
        } finally {
            currentUserHolder.clear();
        }
    }

    @Override
    public void destroy() {
    }
}

