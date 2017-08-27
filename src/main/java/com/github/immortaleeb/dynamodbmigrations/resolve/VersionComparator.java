package com.github.immortaleeb.dynamodbmigrations.resolve;

import java.util.Comparator;

public class VersionComparator implements Comparator<String> {

    @Override
    public int compare(String v1, String v2) {
        String[] v1Versions = v1.split("\\.");
        String[] v2Versions = v2.split("\\.");

        for (int i = 0; i < Math.min(v1Versions.length, v2Versions.length); i++) {
            String v1Version = v1Versions[i];
            String v2Version = v2Versions[i];

            int compare = v1Version.compareTo(v2Version);
            if (compare != 0) {
                return compare;
            }
        }

        if (v1Versions.length < v2Versions.length) {
            return -1;
        } else if (v1Versions.length > v2Versions.length) {
            return 1;
        }

        return 0;
    }

}
