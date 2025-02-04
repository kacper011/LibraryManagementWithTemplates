package com.example.library.config;

import org.springframework.beans.factory.annotation.Value;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;

@Component
public class AppConfig {

    private static final Logger logger = LoggerFactory.getLogger(AppConfig.class);

    @Value("${app.name}")
    private String appName;

    @Value("${app.version}")
    private String appVersion;

    @Value(("${app.owner}"))
    private String appOwner;

    public void printAppInfo() {
        logger.info("Application Name: {}", appName);
        logger.info("Application Version: {}", appVersion);
        logger.info("Application Owner: {}", appOwner);
    }
}
