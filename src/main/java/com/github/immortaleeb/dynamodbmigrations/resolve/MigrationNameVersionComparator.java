package com.github.immortaleeb.dynamodbmigrations.resolve;

import java.util.Comparator;

public class MigrationNameVersionComparator implements Comparator<String> {

    private final MigrationNameResolver migrationNameResolver;
    private final VersionComparator versionComparator;

    public MigrationNameVersionComparator(MigrationNameResolver migrationNameResolver) {
        this.migrationNameResolver = migrationNameResolver;
        this.versionComparator = new VersionComparator();
    }

    @Override
    public int compare(String m1, String m2) {
        String v1 = this.migrationNameResolver.resolveVersion(m1);
        String v2 = this.migrationNameResolver.resolveVersion(m2);

        return this.versionComparator.compare(v1, v2);
    }

}
