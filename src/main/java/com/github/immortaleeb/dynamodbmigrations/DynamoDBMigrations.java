package com.github.immortaleeb.dynamodbmigrations;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.github.immortaleeb.dynamodbmigrations.config.Configuration;
import com.github.immortaleeb.dynamodbmigrations.migration.ClassMigrationsProvider;
import com.github.immortaleeb.dynamodbmigrations.resolve.AvailableMigrationsResolver;
import com.github.immortaleeb.dynamodbmigrations.resolve.ClassMigrationsProviderFactory;
import com.github.immortaleeb.dynamodbmigrations.resolve.MigrationNameResolverImpl;
import com.github.immortaleeb.dynamodbmigrations.resolve.MigrationsResolver;
import com.github.immortaleeb.dynamodbmigrations.resolve.AppliedMigrationsResolver;
import com.github.immortaleeb.dynamodbmigrations.migration.MigrationInfo;
import com.github.immortaleeb.dynamodbmigrations.resolve.ToApplyMigrationsResolver;
import com.github.immortaleeb.dynamodbmigrations.table.SchemaVersionTableCreator;

import java.util.List;

public class DynamoDBMigrations {

    private final Configuration configuration;
    private final AmazonDynamoDB client;

    private final MigrationsResolver appliedMigrationsResolver;
    private final MigrationsResolver availableMigrationsResolver;

    public DynamoDBMigrations(Configuration configuration) {
        this.configuration = configuration;
        this.client = this.createClient();

        MigrationNameResolverImpl migrationNameResolver = new MigrationNameResolverImpl();
        ClassMigrationsProviderFactory migrationsProviderFactory = new ClassMigrationsProviderFactory(
                migrationNameResolver);
        ClassMigrationsProvider migrationsProvider = migrationsProviderFactory.createProvider(this.configuration);

        this.appliedMigrationsResolver = new AppliedMigrationsResolver(client,
                this.configuration.getDynamoDBSchemaVersionTableName());
        this.availableMigrationsResolver = new AvailableMigrationsResolver(migrationNameResolver, migrationsProvider);
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
        List<MigrationInfo> availableMigrations = this.getAvailableMigrations();
        List<MigrationInfo> toApplyMigrations = new ToApplyMigrationsResolver(appliedMigrations,
                availableMigrations).resolve();

        System.out.println("Applied migrations");
        appliedMigrations.forEach(System.out::println);
        System.out.println();

        System.out.println("Available migrations");
        availableMigrations.forEach(System.out::println);
        System.out.println();

        System.out.println("Migrations to be applied");
        toApplyMigrations.forEach(System.out::println);
        System.out.println();
    }

    private List<MigrationInfo> getAppliedMigrations() {
        return this.appliedMigrationsResolver.resolve();
    }

    private List<MigrationInfo> getAvailableMigrations() {
        return this.availableMigrationsResolver.resolve();
    }

}
