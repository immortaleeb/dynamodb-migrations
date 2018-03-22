package com.github.immortaleeb.dynamodbmigrations.resolve;

import com.github.immortaleeb.dynamodbmigrations.migration.MigrationInfo;

import java.util.List;

/**
 * User: ebral {@literal <enver.bral@unifiedpost.com>}
 */
public interface MigrationsApplier {

    void apply(List<MigrationInfo> migrations);

}
