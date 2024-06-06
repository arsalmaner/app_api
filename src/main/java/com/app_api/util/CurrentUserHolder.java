package com.app_api.util;

import com.app_api.resource.entity.User;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;

import java.security.Principal;

@Component
public class CurrentUserHolder {
    private static final ThreadLocal<Principal> currentPrincipal = new ThreadLocal<>();

    private Principal getCurrentPrincipal() {
        return currentPrincipal.get();
    }

    public User getCurrentUser() {
        return (User) ((UsernamePasswordAuthenticationToken) getCurrentPrincipal()).getPrincipal();
    }

    public void setCurrentPrincipal(Principal principal) {
        currentPrincipal.set(principal);
    }

    public void clear() {
        currentPrincipal.remove();
    }
}

