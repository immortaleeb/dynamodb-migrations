package com.github.immortaleeb.dynamodbmigrations.resolve;

import com.github.immortaleeb.dynamodbmigrations.migration.DynamoDBMigration;
import com.github.immortaleeb.dynamodbmigrations.migration.MigrationInfo;

import java.time.Duration;
import java.time.LocalDateTime;

public class ClassMigrationInfo implements MigrationInfo {

    private final Class<? extends DynamoDBMigration> migrationClass;
    private final String version;
    private final String description;
    private final String script;

    public <T extends DynamoDBMigration> ClassMigrationInfo(Class<T> clazz, MigrationNameResolver migrationNameResolver) {
        String simpleName = clazz.getSimpleName();

        this.migrationClass = clazz;
        this.version = migrationNameResolver.resolveVersion(simpleName);
        this.description = migrationNameResolver.resolveDescription(simpleName);
        this.script = simpleName + ".class";
    }

    public Class<? extends DynamoDBMigration> getMigrationClass() {
        return migrationClass;
    }

    @Override
    public String getVersion() {
        return version;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public String getScript() {
        return script;
    }

    @Override
    public LocalDateTime getAppliedOn() {
        return null;
    }

    @Override
    public Duration getExecutionTime() {
        return null;
    }

    public static <T extends DynamoDBMigration> ClassMigrationInfo of(Class<T> clazz, MigrationNameResolver migrationNameResolver) {
        return new ClassMigrationInfo(clazz, migrationNameResolver);
    }

    @Override
    public String toString() {
        return "ClassMigrationInfo{" + "version='" + version + '\'' + ", description='" + description + '\'' +
                ", script='" + script + '\'' + '}';
    }

}
