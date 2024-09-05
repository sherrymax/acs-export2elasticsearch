package com.report.collector;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.CloseableHttpClient;

import com.google.gson.*;
import com.report.model.ESData;


public class InvokeREST {

	private String acsHostName = "https://source-env.sherrymax.com";
	private String elasticSearchHostName = "http://target-env.sherrymax.com:9200";
	private String elasticSearchIndex = "elasticsearch-index";
	private String writingName = "";
	private String writingNodeId = "";

	private String getBasicAuthenticationHeader() {
		String username = "hollymolly";
		String password = "h0llym0lly";
		String credentials = username + ":" + password;
		String encodedCredentials = Base64.getEncoder().encodeToString(credentials.getBytes());
		return "Basic " + encodedCredentials;
	}

	public static void main(String[] args) {
		InvokeREST invokeREST = new InvokeREST();
		ReadFromPostgres readFromPostgres = new ReadFromPostgres();

//    	readFromPostgres.fetchValuesFromActiviti();
//    	invokeREST.callGET();
		
		
		String url = invokeREST.elasticSearchHostName+"/"+invokeREST.elasticSearchIndex+"/_doc";
		
		ESData esData = new ESData();
		esData.setRevisionType("Revision");
		esData.setQueue("Q1");
		esData.setSpecialistQueue("SQ1");
		esData.setStepName("SN1");
		esData.setStatus("In Progress");
		esData.setLastModifiedDate("01-Mar-2024");
		esData.setDeadlineDate("12-Dec-2024");
		esData.setDocumentLeader("Dave Jones");
		
		invokeREST.callPOST(url, esData);

//		invokeREST.getWritingNodeDetails("9116a122-f195-479d-b2b5-cc6826741348");

	}

	public String getElasticSearchHostName() {
		return elasticSearchHostName;
	}

	public String getElasticSearchIndex() {
		return elasticSearchIndex;
	}

	public void callGET() {

		try (CloseableHttpClient httpClient = HttpClientBuilder.create().build()) {

			String url = this.elasticSearchHostName+"/_cat/indices?v";
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
			getRequest.setHeader("Authorization", this.getBasicAuthenticationHeader());

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

}
