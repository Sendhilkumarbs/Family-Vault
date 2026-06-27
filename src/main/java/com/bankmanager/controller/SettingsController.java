package com.bankmanager.controller;

import com.bankmanager.config.RuntimeConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Properties;

@Controller
@RequestMapping("/settings")
public class SettingsController {

    @Autowired
    private RuntimeConfigService configService;

    /**
     * Show settings page
     */
    @GetMapping
    public String showSettings(Model model) {
        Properties config = configService.loadConfig();
        
        // Email settings
        model.addAttribute("emailNotificationsEnabled", 
            config.getProperty("email.notifications.enabled", "false"));
        model.addAttribute("mailEnabled", 
            config.getProperty("spring.mail.enabled", "false"));
        model.addAttribute("mailHost", 
            config.getProperty("spring.mail.host", "smtp.gmail.com"));
        model.addAttribute("mailPort", 
            config.getProperty("spring.mail.port", "587"));
        model.addAttribute("mailUsername", 
            config.getProperty("spring.mail.username", ""));
        // Mask password for display (show asterisks)
        String password = config.getProperty("spring.mail.password", "");
        model.addAttribute("mailPassword", password.isEmpty() ? "" : "********"); // Mask password
        model.addAttribute("mailPasswordSet", !password.isEmpty());
        model.addAttribute("mailFrom", 
            config.getProperty("spring.mail.from", ""));
        
        model.addAttribute("configFilePath", configService.getConfigFilePath());
        model.addAttribute("configFileExists", configService.configFileExists());
        
        return "settings";
    }

    /**
     * Update email settings
     */
    @PostMapping("/email")
    public String updateEmailSettings(
            @RequestParam(required = false) Boolean emailNotificationsEnabled,
            @RequestParam(required = false) Boolean mailEnabled,
            @RequestParam String mailHost,
            @RequestParam String mailPort,
            @RequestParam String mailUsername,
            @RequestParam String mailPassword,
            @RequestParam String mailFrom,
            RedirectAttributes redirectAttributes) {
        
        Properties config = configService.loadConfig();
        
        // Only update password if a new one is provided (not masked with asterisks)
        String currentPassword = config.getProperty("spring.mail.password", "");
        String newPassword = mailPassword;
        if (mailPassword.equals("********") || mailPassword.isEmpty() || mailPassword.equals(currentPassword)) {
            newPassword = currentPassword; // Keep existing password
        }
        
        // Update properties
        config.setProperty("email.notifications.enabled", 
            emailNotificationsEnabled != null ? emailNotificationsEnabled.toString() : "false");
        config.setProperty("spring.mail.enabled", 
            mailEnabled != null ? mailEnabled.toString() : "false");
        config.setProperty("spring.mail.host", mailHost);
        config.setProperty("spring.mail.port", mailPort);
        config.setProperty("spring.mail.username", mailUsername);
        if (!newPassword.equals(currentPassword)) {
            config.setProperty("spring.mail.password", newPassword);
        }
        config.setProperty("spring.mail.from", mailFrom);
        
        if (configService.saveConfig(config)) {
            redirectAttributes.addFlashAttribute("successMessage", 
                "Email settings updated successfully! Restart the application for changes to take effect.");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", 
                "Failed to save email settings. Please check the logs.");
        }
        
        return "redirect:/settings";
    }

    /**
     * Get current configuration as JSON (for API)
     */
    @GetMapping("/api")
    @ResponseBody
    public Properties getConfig() {
        return configService.loadConfig();
    }
}
