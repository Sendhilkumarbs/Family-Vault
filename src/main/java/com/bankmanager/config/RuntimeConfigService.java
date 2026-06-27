package com.bankmanager.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

/**
 * Service to manage runtime configuration
 * Reads/writes to external config file (config/application.properties)
 */
@Service
public class RuntimeConfigService {

    private static final Logger logger = LoggerFactory.getLogger(RuntimeConfigService.class);
    private static final String CONFIG_DIR = "config";
    private static final String CONFIG_FILE = "application.properties";
    private final Path configFilePath;

    public RuntimeConfigService() {
        // Create config directory if it doesn't exist
        Path configDir = Paths.get(CONFIG_DIR);
        try {
            if (!Files.exists(configDir)) {
                Files.createDirectories(configDir);
                logger.info("Created config directory: {}", configDir.toAbsolutePath());
            }
        } catch (IOException e) {
            logger.error("Failed to create config directory: {}", e.getMessage());
        }
        
        this.configFilePath = configDir.resolve(CONFIG_FILE);
    }

    /**
     * Load configuration from external file
     */
    public Properties loadConfig() {
        Properties props = new Properties();
        
        if (Files.exists(configFilePath)) {
            try (InputStream input = Files.newInputStream(configFilePath)) {
                props.load(input);
                logger.debug("Loaded configuration from: {}", configFilePath.toAbsolutePath());
            } catch (IOException e) {
                logger.error("Failed to load configuration: {}", e.getMessage());
            }
        } else {
            logger.info("Config file not found, using defaults. Creating default config file...");
            createDefaultConfig();
            return loadConfig(); // Retry after creating default
        }
        
        return props;
    }

    /**
     * Save configuration to external file
     */
    public boolean saveConfig(Properties properties) {
        try {
            // Ensure directory exists
            Files.createDirectories(configFilePath.getParent());
            
            // Write to file
            try (OutputStream output = Files.newOutputStream(configFilePath)) {
                properties.store(output, "Runtime Configuration - Edit this file to change settings");
                logger.info("Configuration saved to: {}", configFilePath.toAbsolutePath());
                return true;
            }
        } catch (IOException e) {
            logger.error("Failed to save configuration: {}", e.getMessage());
            return false;
        }
    }

    /**
     * Update a specific configuration property
     */
    public boolean updateProperty(String key, String value) {
        Properties props = loadConfig();
        props.setProperty(key, value);
        return saveConfig(props);
    }

    /**
     * Get a configuration property value
     */
    public String getProperty(String key, String defaultValue) {
        Properties props = loadConfig();
        return props.getProperty(key, defaultValue);
    }

    /**
     * Create default configuration file from classpath
     */
    private void createDefaultConfig() {
        try {
            // Copy default from classpath if exists
            InputStream defaultConfig = getClass().getClassLoader()
                .getResourceAsStream("application.properties");
            
            if (defaultConfig != null) {
                Files.createDirectories(configFilePath.getParent());
                Files.copy(defaultConfig, configFilePath);
                logger.info("Created default config file: {}", configFilePath.toAbsolutePath());
            }
        } catch (IOException e) {
            logger.error("Failed to create default config file: {}", e.getMessage());
        }
    }

    /**
     * Get the path to the config file
     */
    public String getConfigFilePath() {
        return configFilePath.toAbsolutePath().toString();
    }

    /**
     * Check if config file exists
     */
    public boolean configFileExists() {
        return Files.exists(configFilePath);
    }
}
