package com.github.immortaleeb.dynamodbmigrations.config;

public class ConfigurationReadException extends RuntimeException {

    public ConfigurationReadException(String message) {
        super(message);
    }

    public ConfigurationReadException(String message, Throwable cause) {
        super(message, cause);
    }

}
