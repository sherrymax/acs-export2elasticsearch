package org.alfresco.tutorial.scheduledjob.reports;

import org.alfresco.tutorial.scheduledjob.model.ESData;

import java.util.ArrayList;

public class Orchestrator {

    ArrayList<ESData> esDataList = new ArrayList<ESData>();
    InvokeREST invoker = new InvokeREST();


    public void executeCalls(){
        System.out.println("<<< Staring executeCalls() from Orchestrator >>>");
        InvokeREST invokeREST = new InvokeREST();
        ReadFromPostgres readFromPostgres = new ReadFromPostgres();

        String esURL = invokeREST.getElasticSearchURL()+"/"+invokeREST.getElasticSearchIndex()+"/_doc";

        //Step 1 : Clear the records
        invokeREST.deleteAllContentFromElasticIndex();

        // Step 2: Query for Instances
        ArrayList<String> instances = readFromPostgres.getProcessInstances();

        // Step 3 : Loop the instances and query for task and bpm_package data. Returns an array list of all data.
        for(int i=0; i<instances.size(); i++) {
            ESData esData = new ESData();
            String instanceId = instances.get(i);

            System.out.println("Process Instance ID : "+ instanceId);

            readFromPostgres.getDBValues(instanceId);
        }

        // Step 4 : Loop through array list of all data. Push each item to ElasticSearch.
        for(int i=0; i<readFromPostgres.esDataList.size(); i++) {

            ESData esData = readFromPostgres.esDataList.get(i);

//			System.out.println("ES DataList # "+(i+1));
//			System.out.println("Process Instance ID : "+ esData.getProcessInstanceId());
//			System.out.println("BPM Package Node Id : "+ esData.getBpmPackageNodeId());
//			System.out.println("ES >>> : "+ esData.toString());

            invokeREST.callPOST(esURL, esData);


        }
        System.out.println("<<< Ending executeCalls() from Orchestrator >>>");

    }
}
