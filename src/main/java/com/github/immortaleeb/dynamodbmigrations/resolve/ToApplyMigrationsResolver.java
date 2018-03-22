package com.github.immortaleeb.dynamodbmigrations.resolve;

import com.github.immortaleeb.dynamodbmigrations.migration.MigrationInfo;

import java.util.List;
import java.util.stream.Collectors;

/**
 * User: ebral {@literal <enver.bral@unifiedpost.com>}
 */
public class ToApplyMigrationsResolver implements MigrationsResolver {

    private final List<String> appliedMigrationScripts;
    private final List<MigrationInfo> availableMigrations;

    public ToApplyMigrationsResolver(List<MigrationInfo> appliedMigrations, List<MigrationInfo> availableMigrations) {
        this.availableMigrations = availableMigrations;
        this.appliedMigrationScripts = appliedMigrations.stream()
                .map(MigrationInfo::getScript)
                .collect(Collectors.toList());
    }

    @Override
    public List<MigrationInfo> resolve() {
        return this.availableMigrations
                .stream()
                .filter(this::isMigrationNotApplied)
                .collect(Collectors.toList());
    }

    private boolean isMigrationNotApplied(MigrationInfo migrationInfo) {
        return !isMigrationApplied(migrationInfo);
    }

    private boolean isMigrationApplied(MigrationInfo migrationInfo) {
        return this.appliedMigrationScripts.contains(migrationInfo.getScript());
    }

}
