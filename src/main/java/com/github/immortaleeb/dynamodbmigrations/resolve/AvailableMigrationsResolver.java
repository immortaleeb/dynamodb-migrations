package com.github.immortaleeb.dynamodbmigrations.resolve;

import com.github.immortaleeb.dynamodbmigrations.migration.DynamoDBMigration;
import com.github.immortaleeb.dynamodbmigrations.migration.MigrationInfo;
import org.reflections.Reflections;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class AvailableMigrationsResolver implements MigrationsResolver {

    private final String migrationsPath;
    private final MigrationNameResolver migrationNameResolver;
    private final MigrationNameVersionComparator migrationNameVersionComparator;

    public AvailableMigrationsResolver(MigrationNameResolver migrationNameResolver, String migrationsPath) {
        this.migrationsPath = migrationsPath;
        this.migrationNameResolver = migrationNameResolver;
        this.migrationNameVersionComparator = new MigrationNameVersionComparator(migrationNameResolver);
    }

    @Override
    public List<MigrationInfo> resolve() {
        Reflections reflections = new Reflections(migrationsPath);

        return reflections.getSubTypesOf(DynamoDBMigration.class)
                .stream()
                .sorted(Comparator.comparing(Class::getSimpleName, migrationNameVersionComparator))
                .map(clazz -> ClassMigrationInfo.of(clazz, migrationNameResolver))
                .collect(Collectors.toList());
    }

}
