package com.github.immortaleeb.dynamodbmigrations.resolve;

import com.github.immortaleeb.dynamodbmigrations.migration.ClassMigrationsProvider;
import com.github.immortaleeb.dynamodbmigrations.migration.DynamoDBMigration;
import org.reflections.Reflections;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * User: ebral {@literal <enver.bral@unifiedpost.com>}
 */
public class ReflectionClassMigrationsProvider implements ClassMigrationsProvider {

    private final MigrationNameResolver migrationNameResolver;
    private final String migrationsPath;
    private final MigrationNameVersionComparator migrationNameVersionComparator;

    public ReflectionClassMigrationsProvider(MigrationNameResolver migrationNameResolver, String migrationsPath) {
        this.migrationNameResolver = migrationNameResolver;
        this.migrationsPath = migrationsPath;
        this.migrationNameVersionComparator = new MigrationNameVersionComparator(this.migrationNameResolver);
    }

    @Override
    public List<Class<? extends DynamoDBMigration>> provideMigrationClasses() {
        Reflections reflections = new Reflections(migrationsPath);

        return reflections.getSubTypesOf(DynamoDBMigration.class)
                .stream()
                .sorted(Comparator.comparing(Class::getSimpleName, migrationNameVersionComparator))
                .collect(Collectors.toList());
    }

}
