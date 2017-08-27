package com.github.immortaleeb.dynamodbmigrations;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.github.immortaleeb.dynamodbmigrations.config.Configuration;
import com.github.immortaleeb.dynamodbmigrations.resolve.AppliedMigrationsResolver;
import com.github.immortaleeb.dynamodbmigrations.resolve.AppliedMigrationsResolverImpl;
import com.github.immortaleeb.dynamodbmigrations.migration.MigrationInfo;
import com.github.immortaleeb.dynamodbmigrations.table.SchemaVersionTableCreator;

import java.util.List;

public class DynamoDBMigrations {

    private final Configuration configuration;
    private final AmazonDynamoDB client;

    private final AppliedMigrationsResolver appliedMigrationsResolver;

    public DynamoDBMigrations(Configuration configuration) {
        this.configuration = configuration;
        this.client = this.createClient();

        this.appliedMigrationsResolver = new AppliedMigrationsResolverImpl(client,
                this.configuration.getDynamoDBSchemaVersionTableName());
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

    public void info() {
        this.createSchemaVersionTableIfNotExists();

        List<MigrationInfo> appliedMigrations = this.getAppliedMigrations();
        appliedMigrations.forEach(System.out::println);
    }

    private List<MigrationInfo> getAppliedMigrations() {
        return this.appliedMigrationsResolver.resolve();
    }

}
