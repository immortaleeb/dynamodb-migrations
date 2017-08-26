package com.github.immortaleeb.dynamodbmigrations.config;

public class Configuration {

    private String dynamoDBAccessKey;
    private String dynamoDBSecretKey;
    private String dynamoDBRegion;
    private String dynamoDBEndpointUrl;

    private Configuration(Builder builder) {
        dynamoDBAccessKey = builder.dynamoDBAccessKey;
        dynamoDBSecretKey = builder.dynamoDBSecretKey;
        dynamoDBRegion = builder.dynamoDBRegion;
        dynamoDBEndpointUrl = builder.dynamoDBEndpointUrl;
    }

    public String getDynamoDBAccessKey() {
        return dynamoDBAccessKey;
    }

    public String getDynamoDBSecretKey() {
        return dynamoDBSecretKey;
    }

    public String getDynamoDBRegion() {
        return dynamoDBRegion;
    }

    public String getDynamoDBEndpointUrl() {
        return dynamoDBEndpointUrl;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private String dynamoDBAccessKey;
        private String dynamoDBSecretKey;
        private String dynamoDBRegion;
        private String dynamoDBEndpointUrl;

        private Builder() {
        }

        public Builder withDynamoDBAccessKey(String dynamoDBAccessKey) {
            this.dynamoDBAccessKey = dynamoDBAccessKey;
            return this;
        }

        public Builder withDynamoDBSecretKey(String dynamoDBSecretKey) {
            this.dynamoDBSecretKey = dynamoDBSecretKey;
            return this;
        }

        public Builder withDynamoDBRegion(String dynamoDBRegion) {
            this.dynamoDBRegion = dynamoDBRegion;
            return this;
        }

        public Builder withDynamoDBEndpointUrl(String dynamoDBEndpointUrl) {
            this.dynamoDBEndpointUrl = dynamoDBEndpointUrl;
            return this;
        }

        public Configuration build() {
            return new Configuration(this);
        }
    }

}
