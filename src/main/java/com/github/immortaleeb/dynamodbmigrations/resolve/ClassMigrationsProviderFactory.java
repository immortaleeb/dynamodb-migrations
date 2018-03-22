package com.github.immortaleeb.dynamodbmigrations.resolve;

import com.github.immortaleeb.dynamodbmigrations.config.Configuration;
import com.github.immortaleeb.dynamodbmigrations.migration.ClassMigrationsProvider;

/**
 * User: ebral {@literal <enver.bral@unifiedpost.com>}
 */
public class ClassMigrationsProviderFactory {

    private final MigrationNameResolver migrationNameResolver;

    public ClassMigrationsProviderFactory(MigrationNameResolver migrationNameResolver) {
        this.migrationNameResolver = migrationNameResolver;
    }

    public ClassMigrationsProvider createProvider(Configuration configuration) {
        String migrationsProviderClassName = configuration.getDynamoDBMigrationsProvider();
        String migrationsPath = configuration.getDynamoDBMigrationsPath();

        return createProvider(migrationsProviderClassName, migrationsPath);
    }

    private ClassMigrationsProvider createProvider(String migrationsProviderClassName, String migrationsPath) {
        if (migrationsProviderClassName == null) {
            return new ReflectionClassMigrationsProvider(this.migrationNameResolver, migrationsPath);
        }

        return createProviderFromClassName(migrationsProviderClassName);
    }

    private ClassMigrationsProvider createProviderFromClassName(String migrationsProviderClassName) {
        try {
            Class<?> clazz = Class.forName(migrationsProviderClassName);
            ClassMigrationsProvider.class.isAssignableFrom(clazz);
            return (ClassMigrationsProvider) clazz.newInstance();
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
            throw new RuntimeException(
                    String.format("Could not create ClassMigrationsProvider with fully qualified name %s",
                            migrationsProviderClassName), e);
        }
    }

}
