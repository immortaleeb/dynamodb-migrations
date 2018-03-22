package com.github.immortaleeb.dynamodbmigrations.resolve;

import com.github.immortaleeb.dynamodbmigrations.migration.ClassMigrationsProvider;
import com.github.immortaleeb.dynamodbmigrations.migration.MigrationInfo;

import java.util.List;
import java.util.stream.Collectors;

public class AvailableMigrationsResolver implements MigrationsResolver {

    private final MigrationNameResolver migrationNameResolver;
    private final ClassMigrationsProvider migrationsProvider;

    public AvailableMigrationsResolver(MigrationNameResolver migrationNameResolver,
                                       ClassMigrationsProvider migrationsProvider) {
        this.migrationNameResolver = migrationNameResolver;
        this.migrationsProvider = migrationsProvider;
    }

    @Override
    public List<MigrationInfo> resolve() {
        return this.migrationsProvider.provideMigrationClasses()
                .stream()
                .map(clazz -> ClassMigrationInfo.of(clazz, migrationNameResolver))
                .collect(Collectors.toList());
    }

}
