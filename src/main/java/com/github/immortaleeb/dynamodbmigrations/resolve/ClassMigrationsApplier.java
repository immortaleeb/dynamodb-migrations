package com.github.immortaleeb.dynamodbmigrations.resolve;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.github.immortaleeb.dynamodbmigrations.migration.DynamoDBMigration;
import com.github.immortaleeb.dynamodbmigrations.migration.MigrationInfo;
import com.github.immortaleeb.dynamodbmigrations.table.SchemaVersionConstants;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * User: ebral {@literal <enver.bral@unifiedpost.com>}
 */
public class ClassMigrationsApplier implements MigrationsApplier {

    private final AmazonDynamoDB client;
    private final Table schemaVersionTable;

    public ClassMigrationsApplier(AmazonDynamoDB client, String schemaVersionTableName) {
        this.client = client;

        DynamoDB dynamoDB = new DynamoDB(client);
        this.schemaVersionTable = dynamoDB.getTable(schemaVersionTableName);
    }

    @Override
    public void apply(List<MigrationInfo> migrations) {
        migrations.forEach(this::apply);
    }

    private void apply(MigrationInfo migrationInfo) {
        if (! (migrationInfo instanceof ClassMigrationInfo)) {
            throw new IllegalStateException(
                    String.format("Can not apply migration %s: it is not a ClassMigrationInfo", migrationInfo));
        }

        applyClassMigration((ClassMigrationInfo) migrationInfo);
    }

    private void applyClassMigration(ClassMigrationInfo migrationInfo) {
        System.out.format("Applying migration %s ...\n", migrationInfo);

        long executionStart = System.currentTimeMillis();

        DynamoDBMigration migration = createMigration(migrationInfo);
        applyMigration(migrationInfo, migration);

        long executionTime = System.currentTimeMillis() - executionStart;

        storeMigration(migrationInfo, executionTime);

        System.out.format("Succesfully applied migration %s ...\n", migrationInfo);
    }

    private DynamoDBMigration createMigration(ClassMigrationInfo migrationInfo) {
        DynamoDBMigration migration;
        try {
            Class<? extends DynamoDBMigration> migrationClass = migrationInfo.getMigrationClass();
            migration = migrationClass.newInstance();
        } catch (IllegalAccessException | InstantiationException e) {
            throw new RuntimeException("Could not create migration " + migrationInfo, e);
        }
        return migration;
    }

    private void applyMigration(ClassMigrationInfo migrationInfo, DynamoDBMigration migration) {
        try {
            migration.migrate(this.client);
        } catch (Exception e) {
            throw new RuntimeException("Error while performing migration " + migrationInfo, e);
        }
    }

    private void storeMigration(ClassMigrationInfo migrationInfo, long executionTime) {
        String appliedOn = ZonedDateTime.now().format(DateTimeFormatter.ISO_ZONED_DATE_TIME);

        Item item = new Item()
                .withString(SchemaVersionConstants.COLUMN_SCRIPT, migrationInfo.getScript())
                .withString(SchemaVersionConstants.COLUMN_VERSION, migrationInfo.getVersion())
                .withString(SchemaVersionConstants.COLUMN_DESCRIPTION, migrationInfo.getDescription())
                .withString(SchemaVersionConstants.COLUMN_APPLIED_ON, appliedOn)
                .withNumber(SchemaVersionConstants.COLUMN_EXECUTION_TIME, executionTime);

        try {
            this.schemaVersionTable.putItem(item);
        } catch (Exception e) {
            throw new RuntimeException("Could not store migration " + migrationInfo, e);
        }
    }

}
