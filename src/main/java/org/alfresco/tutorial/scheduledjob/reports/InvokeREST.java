package org.alfresco.tutorial.scheduledjob.reports;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.alfresco.tutorial.scheduledjob.model.ESData;
import org.alfresco.tutorial.scheduledjob.util.GlobalPropertiesHandler;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;


public class InvokeREST {
    /*
    private String acsHostName = "https://boeing.alfdemo.com";
    private String elasticSearchPort = "443";
    private String elasticSearchHostName = "https://de9a5f04c88144b0a4214a156f811ad9.us-east-2.aws.elastic-cloud.com";
    private String elasticSearchURL = elasticSearchHostName + ":" + elasticSearchPort;
    private String elasticSearchIndex = "boeing-test-2";
    private String elasticSearchAPIValue = "YkRLSzI1RUI3Nksyb1hTQ2ZVNjg6OWE5SERYTDBSRFdaT2tLLWEzZzY2dw==";
    */

    private String acsHostName = "";
    private String elasticSearchPort = "";
    private String elasticSearchHostName = "";
    private String elasticSearchURL = "";
    private String elasticSearchIndex = "";
    private String elasticSearchAPIValue = "";
    private String writingName = "";
    private String writingNodeId = "";

    public InvokeREST() {
        acsHostName = new GlobalPropertiesHandler().getAlfrescoHostName();
        elasticSearchHostName = new GlobalPropertiesHandler().getElasticSearchHostName();
        elasticSearchPort = new GlobalPropertiesHandler().getElasticSearchPort();
        elasticSearchURL = elasticSearchHostName + ":" + elasticSearchPort;
        elasticSearchIndex = new GlobalPropertiesHandler().getElasticSearchIndex();
        elasticSearchAPIValue = new GlobalPropertiesHandler().getElasticSearchAuthAPIValue();
    }

    private String getACSAuthenticationHeader() {
        String username = "demo";
        String password = "demo";
        String credentials = username + ":" + password;
        String encodedCredentials = Base64.getEncoder().encodeToString(credentials.getBytes());
        return "Basic " + encodedCredentials;
    }

    private String getElasticSearchAuthenticationHeader() {
        return "ApiKey "+this.elasticSearchAPIValue;
    }


//    public static void main(String[] args) {
//        InvokeREST invokeREST = new InvokeREST();
//        ReadFromPostgres readFromPostgres = new ReadFromPostgres();
//
//        invokeREST.deleteAllContentFromElasticIndex();

//    	readFromPostgres.fetchValuesFromActiviti();
//    	invokeREST.callGET();


//		String url = invokeREST.elasticSearchHostName+"/"+invokeREST.elasticSearchIndex+"/_doc";
//
//		ESData esData = new ESData();
//		esData.setRevisionType("Revision");
//		esData.setQueue("Q1");
//		esData.setSpecialistQueue("SQ1");
//		esData.setStepName("SN1");
//		esData.setStatus("In Progress");
//		esData.setLastModifiedDate("01-Mar-2024");
//		esData.setDeadlineDate("12-Dec-2024");
//		esData.setDocumentLeader("Dave Jones");
//
//		invokeREST.callPOST(url, esData);

//		invokeREST.getWritingNodeDetails("9116a122-f195-479d-b2b5-cc6826741348");

//    }

//    public String getElasticSearchURL() {
//        return this.elasticSearchURL;
//    }

    public String getElasticSearchIndex() {
        return elasticSearchIndex;
    }
    public String getElasticSearchURL() {
        return elasticSearchURL;
    }

    public void callGET() {

        try (CloseableHttpClient httpClient = HttpClientBuilder.create().build()) {

            String url = this.elasticSearchURL+"/_cat/indices?v";
            HttpGet getRequest = new HttpGet(url);
            HttpResponse response = httpClient.execute(getRequest);

            if (response.getStatusLine().getStatusCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : " + response.getStatusLine().getStatusCode());
            }

            BufferedReader br = new BufferedReader(new InputStreamReader((response.getEntity().getContent())));
            String output;
            System.out.println("Output from Server .... \n");
            while ((output = br.readLine()) != null) {

                System.out.println(output);
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    public void callPOST(String url, ESData esData) {

        Gson gson = new Gson();
        String requestBody = gson.toJson(esData);

        try (CloseableHttpClient httpClient = HttpClientBuilder.create().build()) {

            System.out.println(url);


            HttpPost postRequest = new HttpPost(url);

            StringEntity input = new StringEntity(requestBody);
            input.setContentType("application/json");
            postRequest.setEntity(input);
            postRequest.setHeader("Authorization", this.getElasticSearchAuthenticationHeader());


            HttpResponse response = httpClient.execute(postRequest);
            int statusCode = response.getStatusLine().getStatusCode();

            if ((statusCode != 200) && (statusCode != 201)) {
                throw new RuntimeException("Failed : HTTP error code : " + response.getStatusLine().getStatusCode());
            }

            BufferedReader br = new BufferedReader(new InputStreamReader((response.getEntity().getContent())));
            String output;
            System.out.println("Output from Server .... \n");
            while ((output = br.readLine()) != null) {

                System.out.println(output);
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Map getWritingNodeDetails(String nodeId) {

        String requestURL = this.acsHostName + "/alfresco/api/-default-/public/alfresco/versions/1/nodes/" + nodeId
                + "/targets?include=properties&where=(assocType='aw:workflowDocument')";
        System.out.println(requestURL);

        Gson gson = new Gson();

        Map<String, String> writingDetailsMap = new HashMap();

        try (CloseableHttpClient httpClient = HttpClientBuilder.create().build()) {

            HttpGet getRequest = new HttpGet(requestURL);
            getRequest.setHeader("Authorization", this.getACSAuthenticationHeader());

            HttpResponse response = httpClient.execute(getRequest);

            if (response.getStatusLine().getStatusCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : " + response.getStatusLine().getStatusCode());
            }

            BufferedReader br = new BufferedReader(new InputStreamReader((response.getEntity().getContent())));

            String apiResponse;

            while ((apiResponse = br.readLine()) != null) {
                System.out.println("API Response from Server .... \n");
                System.out.println(apiResponse);

                // Extraction Step 1 : Convert API Response String to JsonElement
                JsonElement je = gson.fromJson(apiResponse, JsonElement.class);

                // Extraction Step 2 : Convert JsonElement to JsonObject
                JsonObject jo = je.getAsJsonObject();

                // Extraction Step 3 : Get the property from JsonObject
                JsonObject list = jo.getAsJsonObject("list");

                // Get the list of entries from JsonObject
                JsonArray entriesArr = list.getAsJsonArray("entries");
                System.out.println(entriesArr.size());


                if(entriesArr.size()>0) {
                    JsonObject jo_first_entry = gson.fromJson(entriesArr.get(0), JsonObject.class);
                    JsonElement je_first_entry = jo_first_entry.asMap().get("entry");
                    JsonObject jo_first_entry_obj = je_first_entry.getAsJsonObject();

                    this.writingName = jo_first_entry_obj.get("name").getAsString();
                    this.writingNodeId = jo_first_entry_obj.get("id").getAsString();
                }
                // Get the first entry object and convert to JsonObject


                writingDetailsMap.put("writingName", this.writingName);
                writingDetailsMap.put("writingNodeId", this.writingNodeId);
                writingDetailsMap.put("writingNodeRef", "workspace://SpacesStore/"+this.writingNodeId);

//		        System.out.println("writingName = "+this.writingName);
//		        System.out.println("writingNodeId = "+this.writingNodeId);

            }



        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return writingDetailsMap;
    }


    public void deleteAllContentFromElasticIndex() {

        String requestBody = "{ \"query\": {\"match_all\": {} } }";
        String url = this.elasticSearchURL+"/"+this.elasticSearchIndex+"/_delete_by_query?conflicts=proceed&pretty";

        try (CloseableHttpClient httpClient = HttpClientBuilder.create().build()) {

            System.out.println(url);


            HttpPost postRequest = new HttpPost(url);

            StringEntity input = new StringEntity(requestBody);
            input.setContentType("application/json");

            postRequest.setEntity(input);
            postRequest.setHeader("Authorization", this.getElasticSearchAuthenticationHeader());

            HttpResponse response = httpClient.execute(postRequest);
            int statusCode = response.getStatusLine().getStatusCode();

            if ((statusCode != 200) && (statusCode != 201)) {
                throw new RuntimeException("Failed : HTTP error code : " + response.getStatusLine().getStatusCode());
            }

            BufferedReader br = new BufferedReader(new InputStreamReader((response.getEntity().getContent())));
            String output;
            System.out.println("Output from Server .... \n");
            while ((output = br.readLine()) != null) {
                System.out.println(output);
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
