package com.github.immortaleeb.dynamodbmigrations;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.github.immortaleeb.dynamodbmigrations.config.Configuration;
import com.github.immortaleeb.dynamodbmigrations.table.SchemaVersionTableCreator;

public class DynamoDBMigrations {

    private final Configuration configuration;
    private final AmazonDynamoDB client;

    public DynamoDBMigrations(Configuration configuration) {
        this.configuration = configuration;
        this.client = this.createClient();
    }

    private AmazonDynamoDB createClient() {
        AWSStaticCredentialsProvider credentialsProvider = new AWSStaticCredentialsProvider(
                new BasicAWSCredentials(configuration.getDynamoDBAccessKey(), configuration.getDynamoDBSecretKey()));

        AmazonDynamoDBClientBuilder clientBuilder = AmazonDynamoDBClientBuilder.standard()
                .withCredentials(credentialsProvider)
                .withRegion(configuration.getDynamoDBRegion());

        if (configuration.getDynamoDBEndpointUrl() != null) {
            clientBuilder.withEndpointConfiguration(
                    new AwsClientBuilder.EndpointConfiguration(configuration.getDynamoDBEndpointUrl(),
                            configuration.getDynamoDBRegion()));
        }

        return clientBuilder.build();
    }

    private void createSchemaVersionTableIfNotExists() {
        new SchemaVersionTableCreator(this.client).createTable(configuration.getDynamoDBSchemaVersionTableName(),
                configuration.getDynamoDBSchemaVersionTableReadCapacity(),
                configuration.getDynamoDBSchemaVersionTableWriteCapacity());
    }

    public void migrate() {
        this.createSchemaVersionTableIfNotExists();
    }

}
