package com.github.immortaleeb.dynamodbmigrations.resolve;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MigrationNameResolverImpl implements MigrationNameResolver {

    private static final String VERSION_REGEX = "[0-9]+(_[0-9]+)*";
    private static final Pattern VERSION_REGEX_PATTERN = Pattern.compile(VERSION_REGEX);

    @Override
    public String resolveVersion(String migrationName) {
        int descriptionPrefixIndex = validateMigrationName(migrationName);

        String versionString = migrationName.substring(1, descriptionPrefixIndex);

        Matcher versionMatcher = VERSION_REGEX_PATTERN.matcher(versionString);
        if (!versionMatcher.matches()) {
            throw new IllegalArgumentException("Invalid migration name '" + migrationName + "': version does not match regex /" + VERSION_REGEX + "/");
        }

        return versionString.replaceAll("_", ".");
    }

    @Override
    public String resolveDescription(String migrationName) {
        int descriptionPrefixIndex = this.validateMigrationName(migrationName);
        return migrationName.substring(descriptionPrefixIndex + 2).replaceAll("_+", " ");
    }

    private int validateMigrationName(String migrationName) {
        if (migrationName == null || migrationName.isEmpty()) {
            throw new IllegalArgumentException("Migration name can not be empty");
        }

        if (!migrationName.startsWith("V")) {
            throw new IllegalArgumentException("Invalid migration name '" + migrationName + "': must start with a V");
        }

        int descriptionPrefixIndex = migrationName.indexOf("__");
        if (descriptionPrefixIndex < 0) {
            throw new IllegalArgumentException("Invalid migration name '" + migrationName + "': must separate version and description with '__'");
        }
        return descriptionPrefixIndex;
    }

}
