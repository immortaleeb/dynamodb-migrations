package com.github.immortaleeb.dynamodbmigrations.resolve;

import com.github.immortaleeb.dynamodbmigrations.migration.DynamoDBMigration;
import com.github.immortaleeb.dynamodbmigrations.migration.MigrationInfo;
import org.reflections.Reflections;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class AvailableMigrationsResolver implements MigrationsResolver {

    private static final String DEFAULT_MIGRATIONS_PATH = "db.migrations";

    private final MigrationNameResolver migrationNameResolver;
    private final MigrationNameVersionComparator migrationNameVersionComparator;

    public AvailableMigrationsResolver(MigrationNameResolver migrationNameResolver) {
        this.migrationNameResolver = migrationNameResolver;
        this.migrationNameVersionComparator = new MigrationNameVersionComparator(migrationNameResolver);
    }

    @Override
    public List<MigrationInfo> resolve() {
        Reflections reflections = new Reflections(DEFAULT_MIGRATIONS_PATH);

        return reflections.getSubTypesOf(DynamoDBMigration.class)
                .stream()
                .sorted(Comparator.comparing(Class::getSimpleName, migrationNameVersionComparator))
                .map(clazz -> ClassMigrationInfo.of(clazz, migrationNameResolver))
                .collect(Collectors.toList());
    }

}
