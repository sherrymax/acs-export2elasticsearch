package org.alfresco.tutorial.scheduledjob.actions;

import org.alfresco.service.ServiceRegistry;
import org.alfresco.tutorial.scheduledjob.reports.Orchestrator;
import org.alfresco.tutorial.scheduledjob.util.GlobalPropertiesHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

public class ScheduledJobExecuter {

    private static final Logger LOG = LoggerFactory.getLogger(ScheduledJobExecuter.class);

    /**
     * Public API access
     */
    private ServiceRegistry serviceRegistry;

//  FETCHING VALUES FROM alfresco-global.properties - START
    @Value("${boeing.reports.elasticsearch.hostname}")
    private String elasticSearch_hostname;

    @Value("${boeing.reports.elasticsearch.port}")
    private String elasticSearch_port;

    @Value("${boeing.reports.elasticsearch.index}")
    private String elasticSearch_index;

    @Value("${boeing.reports.elasticsearch.authentication.api.value}")
    private String elasticSearch_authApiValue;

    @Value("${boeing.reports.postgresql.url}")
    private String postgres_url;

    @Value("${boeing.reports.alfresco.hostname}")
    private String acs_hostname;

    //  FETCHING VALUES FROM alfresco-global.properties - END

    public void setServiceRegistry(ServiceRegistry serviceRegistry) {
        this.serviceRegistry = serviceRegistry;
    }

    public void execute() {
        LOG.info("Running the scheduled job");
        System.out.println("-----------------------> RUNNING JOB >>> !!! ");

        GlobalPropertiesHandler globalPropertiesHandler = new GlobalPropertiesHandler();
        globalPropertiesHandler.setElasticSearchHostName(this.elasticSearch_hostname);
        globalPropertiesHandler.setElasticSearchPort(this.elasticSearch_port);
        globalPropertiesHandler.setElasticSearchIndex(this.elasticSearch_index);
        globalPropertiesHandler.setElasticSearchAuthAPIValue(this.elasticSearch_authApiValue);
        globalPropertiesHandler.setPostgresURL(this.postgres_url);
        globalPropertiesHandler.setAlfrescoHostName(this.acs_hostname);

        // Work/Job implementation starts from here...
        new Orchestrator().executeCalls();

    }

}

