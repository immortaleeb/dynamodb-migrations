package com.github.immortaleeb.dynamodbmigrations.migration;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;

public interface DynamoDBMigration {

    void migrate(AmazonDynamoDB dynamo);

}
