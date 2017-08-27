package com.github.immortaleeb.dynamodbmigrations.resolve;

import com.github.immortaleeb.dynamodbmigrations.migration.MigrationInfo;

import java.util.List;

public interface AppliedMigrationsResolver {

    List<MigrationInfo> resolve();

}
