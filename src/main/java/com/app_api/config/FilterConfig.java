package com.app_api.config;

import com.app_api.util.CurrentUserHolder;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

    private final CurrentUserHolder currentUserHolder;

    public FilterConfig(CurrentUserHolder currentUserHolder) {
        this.currentUserHolder = currentUserHolder;
    }

    @Bean
    public FilterRegistrationBean<CurrentUserFilter> loggingFilter() {
        FilterRegistrationBean<CurrentUserFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new CurrentUserFilter(currentUserHolder));
        registrationBean.addUrlPatterns("/*");
        return registrationBean;
    }
}

