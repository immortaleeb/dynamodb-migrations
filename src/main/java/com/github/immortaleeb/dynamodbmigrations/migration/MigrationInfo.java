package com.github.immortaleeb.dynamodbmigrations.migration;

import java.time.Duration;
import java.time.LocalDateTime;

public interface MigrationInfo {

    String getVersion();

    String getDescription();

    String getScript();

    LocalDateTime getAppliedOn();

    Duration getExecutionTime();

}
