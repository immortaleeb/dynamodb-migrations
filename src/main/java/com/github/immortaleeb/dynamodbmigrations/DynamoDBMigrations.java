package com.github.immortaleeb.dynamodbmigrations;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.github.immortaleeb.dynamodbmigrations.config.Configuration;
import com.github.immortaleeb.dynamodbmigrations.table.SchemaVersionTableCreator;

public class DynamoDBMigrations {

    private static final String DEFAULT_SCHEMA_VERSION_TABLE = "dynamodb_schema_version";
    private static final long DEFAULT_READ_CAPACITY_UNITS = 1l;
    private static final long DEFAULT_WRITE_CAPACITY_UNITS = 1l;

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
        new SchemaVersionTableCreator(this.client).createTable(DEFAULT_SCHEMA_VERSION_TABLE,
                DEFAULT_READ_CAPACITY_UNITS, DEFAULT_WRITE_CAPACITY_UNITS);
    }

    public void migrate() {
        this.createSchemaVersionTableIfNotExists();
    }

}
