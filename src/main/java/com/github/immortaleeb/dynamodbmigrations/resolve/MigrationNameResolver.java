package com.github.immortaleeb.dynamodbmigrations.resolve;

public interface MigrationNameResolver {

    String resolveVersion(String migrationName);

    String resolveDescription(String migrationName);

}
