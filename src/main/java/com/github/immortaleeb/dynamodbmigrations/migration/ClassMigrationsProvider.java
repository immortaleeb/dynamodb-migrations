package com.github.immortaleeb.dynamodbmigrations.migration;

import java.util.List;

/**
 * User: ebral {@literal <enver.bral@unifiedpost.com>}
 */
public interface ClassMigrationsProvider {

    List<Class<? extends DynamoDBMigration>> provideMigrationClasses();

}
