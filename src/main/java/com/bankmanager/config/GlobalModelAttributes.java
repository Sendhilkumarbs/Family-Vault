package com.bankmanager.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalModelAttributes {

    @Autowired
    private AppProperties appProperties;

    @ModelAttribute("appName")
    public String appName() {
        return appProperties.getDisplayName();
    }

    @ModelAttribute("appDescription")
    public String appDescription() {
        return appProperties.getDescription();
    }
}
