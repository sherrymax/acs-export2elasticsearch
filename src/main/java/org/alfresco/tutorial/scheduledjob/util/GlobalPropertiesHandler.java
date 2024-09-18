package org.alfresco.tutorial.scheduledjob.util;

public class GlobalPropertiesHandler {
    private static String alfrescoHostName;
    private static String postgresURL;
    private static String elasticSearchHostName;
    private static String elasticSearchPort;
    private static String elasticSearchIndex;
    private static String elasticSearchAuthAPIValue;

    public String getAlfrescoHostName() {
        return alfrescoHostName;
    }
    public void setAlfrescoHostName(String hostName) {
        this.alfrescoHostName = hostName;
    }

    public String getPostgresURL() {
        return postgresURL;
    }

    public void setPostgresURL(String postgresURL) {
        this.postgresURL = postgresURL;
    }

    public String getElasticSearchHostName() {
        return elasticSearchHostName;
    }

    public void setElasticSearchHostName(String elasticSearchHostName) {
        this.elasticSearchHostName = elasticSearchHostName;
    }

    public String getElasticSearchPort() {
        return elasticSearchPort;
    }

    public void setElasticSearchPort(String elasticSearchPort) {
        this.elasticSearchPort = elasticSearchPort;
    }

    public String getElasticSearchIndex() {
        return elasticSearchIndex;
    }

    public void setElasticSearchIndex(String elasticSearchIndex) {
        this.elasticSearchIndex = elasticSearchIndex;
    }

    public String getElasticSearchAuthAPIValue() {
        return elasticSearchAuthAPIValue;
    }

    public void setElasticSearchAuthAPIValue(String elasticSearchAuthAPIValue) {
        this.elasticSearchAuthAPIValue = elasticSearchAuthAPIValue;
    }

}
