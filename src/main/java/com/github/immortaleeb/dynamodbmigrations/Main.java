package com.github.immortaleeb.dynamodbmigrations;

import com.github.immortaleeb.dynamodbmigrations.config.Configuration;
import com.github.immortaleeb.dynamodbmigrations.config.PropertiesConfigurationReader;

public class Main {

    public static void main(String[] args) {
        PropertiesConfigurationReader configurationReader = new PropertiesConfigurationReader();
        Configuration configuration = configurationReader.read();

        DynamoDBMigrations dynamoDBMigrations = new DynamoDBMigrations(configuration);
        dynamoDBMigrations.info();
    }

}
