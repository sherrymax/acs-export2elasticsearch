package com.report.collector;


import java.sql.*;
import java.util.ArrayList;
import java.util.Map;

import com.report.model.ESData;

public class ReadFromPostgres {

	String url = "jdbc:postgresql://ec2-1-2-3-4.compute-1.amazonaws.com:5432/alfresco";
	ArrayList<ESData> esDataList = new ArrayList<ESData>();
	InvokeREST invoker = new InvokeREST();		
	String esURL = invoker.getElasticSearchHostName()+"/"+invoker.getElasticSearchIndex()+"/_doc";

	public static void main(String[] args) {

		ReadFromPostgres readFromPostgres = new ReadFromPostgres();
		
		ArrayList<String> instances = readFromPostgres.getProcessInstances();
		

		
		for(int i=0; i<instances.size(); i++) {
			ESData esData = new ESData();
			String instanceId = instances.get(i);
			
			System.out.println("Process Instance ID : "+ instanceId); 

			readFromPostgres.getDBValues(instanceId);
		}
		
		for(int i=0; i<readFromPostgres.esDataList.size(); i++) {

			ESData esData = readFromPostgres.esDataList.get(i);
			System.out.println("ES DataList # "+(i+1));
			System.out.println("Process Instance ID : "+ esData.getProcessInstanceId()); 
			System.out.println("BPM Package Node Id : "+ esData.getBpmPackageNodeId()); 

			System.out.println("ES >>> : "+ esData.toString());
			
			InvokeREST invokeREST = new InvokeREST();
			invokeREST.callPOST("http://boeing-aps.alfdemo.com:9200/boeing-test-2/_doc", esData);

			
		}

//		readFromPostgres.getTaskInstances("48697");
//		readFromPostgres.getAttachmentNodeId("48697");
		
//		invoker.getWritingNodeDetails(readFromPostgres.getAttachmentNodeId("48697"));
		
		
	}
	
	public ArrayList<String> getProcessInstances() {
		String processInstancesQuery = "SELECT DISTINCT public.act_hi_procinst.proc_inst_id_ as instanceId FROM public.act_hi_procinst INNER JOIN public.act_ru_variable ON (public.act_hi_procinst.proc_inst_id_ = public.act_ru_variable.proc_inst_id_) INNER JOIN public.act_hi_taskinst ON (public.act_hi_procinst.proc_inst_id_ = public.act_hi_taskinst.proc_inst_id_) AND (public.act_hi_procinst.proc_def_id_ LIKE '%wizardReview%') AND (public.act_ru_variable.name_ = 'bpm_package') GROUP BY instanceId ORDER BY instanceId DESC";
		ArrayList<String> instances = this.queryForProcessValuesFromActiviti(this.url, processInstancesQuery);

		return instances;
	}
//	
//	public void getTaskInstances(String processInstanceId) {
//		String taskInstancesQuery = "SELECT * FROM public.act_hi_taskinst where proc_inst_id_ = '"+processInstanceId+"' ORDER BY start_time_ DESC";
//		this.fetchValuesFromActiviti(this.url, taskInstancesQuery);
//	}
//	
//	public void getAttachmentVariable(String taskInstanceId) {
//		String attachmentQuery = "SELECT text_ FROM public.act_ru_variable WHERE (proc_inst_id_ = '"+taskInstanceId+"' and name_='bpm_package') ORDER BY id_ ASC";
//		this.fetchValuesFromActiviti(this.url, attachmentQuery);
//	}
//	
////	public String getValuesByTableJoin(String processInstanceId) {
////		String query = "SELECT public.act_hi_procinst.proc_inst_id_ as instanceid, public.act_ru_variable.name_ as variablename, public.act_ru_variable.text_ as value FROM public.act_hi_procinst INNER JOIN public.act_ru_variable ON (public.act_hi_procinst.proc_inst_id_ = public.act_ru_variable.proc_inst_id_) AND (act_hi_procinst.proc_inst_id_ = '"+processInstanceId+"') AND (public.act_ru_variable.name_ = 'bpm_package') ORDER BY public.act_hi_procinst.start_time_ DESC";
////		this.fetchValuesFromActiviti(this.url, query);
//	}
	
	public void getDBValues(String processInstanceId) {
		
		String query = "SELECT public.act_hi_procinst.proc_inst_id_ as instanceId, public.act_hi_procinst.proc_def_id_ as instanceDefId, public.act_hi_taskinst.id_ as taskId, public.act_hi_taskinst.name_ as taskName, public.act_hi_taskinst.assignee_ as taskAssignee, public.act_hi_taskinst.start_time_ as taskStartTime, public.act_hi_taskinst.end_time_ as taskEndTime, public.act_ru_variable.name_ as variableName, public.act_ru_variable.text_ as variableValue FROM public.act_hi_procinst INNER JOIN public.act_ru_variable ON (public.act_hi_procinst.proc_inst_id_ = public.act_ru_variable.proc_inst_id_) INNER JOIN public.act_hi_taskinst ON (public.act_hi_procinst.proc_inst_id_ = public.act_hi_taskinst.proc_inst_id_) AND (act_hi_procinst.proc_inst_id_ = '"+processInstanceId+"') AND (public.act_ru_variable.name_ = 'bpm_package') ORDER BY public.act_hi_procinst.start_time_ DESC";
		this.queryForValuesFromActiviti(this.url, query);
				
	}
	
	public void queryForValuesFromActiviti(String url, String query) {
		

		String bpmPackageNodeId = "";
		try {
//			System.out.println(query);
			
			Class.forName("org.postgresql.Driver");
			String user = "postgres";
			String password = "Alfresco01";
			Connection conn = DriverManager.getConnection(url, user, password);

			// 3. Create a statement
			Statement stmt = conn.createStatement();

			// 4. Execute a query
			ResultSet rs = stmt.executeQuery(query);

			// 5. Process the result set
			while (rs.next()) {
				
				ESData esData = new ESData();

				String nodeRef = rs.getString("variableValue");
				if(nodeRef != null) {
					bpmPackageNodeId = nodeRef.split("SpacesStore/")[1];
				}

				esData.setProcessInstanceId(rs.getString("instanceId"));
				esData.setTaskId(rs.getString("taskId"));
				esData.setTaskName(rs.getString("taskName"));
				esData.setTaskAssigne(rs.getString("taskAssignee"));
				esData.setTaskStartTime(rs.getString("taskStartTime"));
				
				String taskEndTime = rs.getString("taskEndTime");
				esData.setTaskEndTime(taskEndTime);

				String taskStatus = (taskEndTime == null)?"In Progress":"Completed";
				esData.setStatus(taskStatus);

				esData.setBpmPackageNodeId(bpmPackageNodeId);
				
				Map writingDetails = this.invoker.getWritingNodeDetails(bpmPackageNodeId);
//				String writingName = writingDetails.get("writingName").toString();
//				String writingNodeId = writingDetails.get("writingNodeId").toString();
				
				esData.setWritingName(writingDetails.get("writingName").toString());
				esData.setWritingNodeId(writingDetails.get("writingNodeId").toString());
				esData.setWritingNodeRef(writingDetails.get("writingNodeRef").toString());
				
//				esData.setRevisionType(rs.getString("instanceId"));
//				esData.setLastModifiedDate(rs.getString("instanceId"));
//				esData.setDocumentLeader(rs.getString("instanceId"));
				
				
				this.esDataList.add(esData);
				
			}

			// 6. Close the resources
			rs.close();
			stmt.close();
			conn.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	

	public ArrayList<String> queryForProcessValuesFromActiviti(String url, String query) {
		
		ArrayList<String> processInstancesList = new ArrayList<String>();

		try {
			
			System.out.println(query);
			
			// 1. Load the PostgreSQL JDBC driver
			Class.forName("org.postgresql.Driver");

			// 2. Establish a connection to the database
			String user = "postgres";
			String password = "Alfresco01";
			Connection conn = DriverManager.getConnection(url, user, password);

			// 3. Create a statement
			Statement stmt = conn.createStatement();

			// 4. Execute a query
			ResultSet rs = stmt.executeQuery(query);

			// 5. Process the result set
			while (rs.next()) {
				// Get the values from the current row
				processInstancesList.add(rs.getString("instanceId"));
				
			}

			// 6. Close the resources
			rs.close();
			stmt.close();
			conn.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return processInstancesList;
	}
	
	
}
