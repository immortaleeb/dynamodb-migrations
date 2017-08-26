package com.github.immortaleeb.dynamodbmigrations.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

public class PropertiesConfigurationReader implements ConfigurationReader {

    private static final Logger LOGGER = LoggerFactory.getLogger(PropertiesConfigurationReader.class);

    private static final String DEFAULT_CONFIGURATION_FILE_NAME = "dynamodb-migrations.properties";

    private static final String KEY_DYNAMODB_ACCESS_KEY = "dynamodb.accessKey";
    private static final String KEY_DYNAMODB_SECRET_KEY = "dynamodb.secretKey";
    private static final String KEY_DYNAMODB_REGION = "dynamodb.region";
    private static final String KEY_DYNAMODB_ENDPOINT_URL = "dynamodb.endpointUrl";

    private final String configurationFilename;

    public PropertiesConfigurationReader() {
        this(DEFAULT_CONFIGURATION_FILE_NAME);
    }

    public PropertiesConfigurationReader(String configurationFilename) {
        this.configurationFilename = configurationFilename;
    }

    @Override
    public Configuration read() {

        File configurationFile = this.findConfigurationFile();
        return this.readPropertiesFile(configurationFile);
    }

    private File findConfigurationFile() {
        String workingDir = this.getWorkingDir();
        LOGGER.debug("Looking for configuration file with name '{}' in directory '{}'...", this.configurationFilename,
                workingDir);

        Path configurationFilePath = Paths.get(workingDir, this.configurationFilename);
        if (Files.notExists(configurationFilePath)) {
            throw new ConfigurationReadException(
                    "No configuration file '" + this.configurationFilename + "' found in working directory '" +
                            workingDir + "'");
        }

        if (!Files.isRegularFile(configurationFilePath)) {
            throw new ConfigurationReadException(
                    "Configuration file at path " + configurationFilePath + " is not a regular file");
        }

        return configurationFilePath.toFile();
    }

    private String getWorkingDir() {
        return System.getProperty("user.dir");
    }

    private Configuration readPropertiesFile(File propertiesFile) {
        Properties properties = new Properties();
        try (InputStream propertiesInputStream = new FileInputStream(propertiesFile)) {
            properties.load(propertiesInputStream);
        } catch (IOException e) {
            throw new ConfigurationReadException("Error while reading properties file '" + properties + "'", e);
        }


        String dynamoDBAccessKey = this.getMandatoryProperty(properties, KEY_DYNAMODB_ACCESS_KEY);
        String dynamoDBSecretKey = this.getMandatoryProperty(properties, KEY_DYNAMODB_SECRET_KEY);
        String dynamoDBRegion = this.getMandatoryProperty(properties, KEY_DYNAMODB_REGION);
        String dynamoDBEndpointUrl = this.getOptionalProperty(properties, KEY_DYNAMODB_ENDPOINT_URL);

        Configuration.Builder configurationBuilder = Configuration.builder()
                .withDynamoDBAccessKey(dynamoDBAccessKey)
                .withDynamoDBSecretKey(dynamoDBSecretKey)
                .withDynamoDBRegion(dynamoDBRegion);

        if (dynamoDBEndpointUrl != null && !dynamoDBEndpointUrl.isEmpty()) {
            configurationBuilder = configurationBuilder.withDynamoDBEndpointUrl(dynamoDBEndpointUrl);
        }

        return configurationBuilder.build();
    }

    private String getMandatoryProperty(Properties properties, String key) {
        String value = properties.getProperty(key);
        if (value == null) {
            throw new ConfigurationReadException("Could not find mandatory property with key '" +
                    key + "' in properties file");
        }

        LOGGER.debug("Found mandatory key '{}' with value '{}'", key, value);

        return value;
    }

    private String getOptionalProperty(Properties properties, String key) {
        String value = properties.getProperty(key);

        if (value == null) {
            LOGGER.debug("No value found for optional key '{}'", key);
        } else {
            LOGGER.debug("Found optional key '{}' with value '{}'", key, value);
        }

        return value;
    }

}
