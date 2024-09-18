package org.alfresco.tutorial.scheduledjob.model;

public class ESData {
    private String processInstanceId = "";
    private String taskId = "";
    private String taskName = "";
    private String taskAssigne = "";
    private String taskStartTime = "";
    private String taskEndTime = "";
    private String bpmPackageNodeId = "";
    private String writingName = "";
    private String writingNodeId = "";
    private String writingNodeRef = "";
    private String revisionType = "";
    private String queue = "";
    private String specialistQueue = "";
    private String stepName = "";
    private String status = "";
    private String lastModifiedDate = "";
    private String deadlineDate = "";
    private String documentLeader = "";

    public String getProcessInstanceId() {
        return processInstanceId;
    }
    public void setProcessInstanceId(String processInstanceId) {
        this.processInstanceId = processInstanceId;
    }
    public String getTaskId() {
        return taskId;
    }
    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }
    public String getTaskName() {
        return taskName;
    }
    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }
    public String getTaskAssigne() {
        return taskAssigne;
    }
    public void setTaskAssigne(String taskAssigne) {
        this.taskAssigne = taskAssigne;
    }
    public String getTaskStartTime() {
        return taskStartTime;
    }
    public void setTaskStartTime(String taskStartTime) {
        this.taskStartTime = taskStartTime;
    }
    public String getTaskEndTime() {
        return taskEndTime;
    }
    public void setTaskEndTime(String taskEndTime) {
        this.taskEndTime = taskEndTime;
    }
    public String getBpmPackageNodeId() {
        return bpmPackageNodeId;
    }
    public void setBpmPackageNodeId(String bpmPackageNodeId) {
        this.bpmPackageNodeId = bpmPackageNodeId;
    }
    public String getWritingNodeId() {
        return writingNodeId;
    }
    public void setWritingNodeId(String writingNodeId) {
        this.writingNodeId = writingNodeId;
    }
    public String getWritingNodeRef() {
        return writingNodeRef;
    }
    public void setWritingNodeRef(String writingNodeRef) {
        this.writingNodeRef = writingNodeRef;
    }
    public void setWritingName(String writingName) {
        this.writingName = writingName;
    }
    public String getWritingName() {
        return writingName;
    }

    public String getRevisionType() {
        return revisionType;
    }
    public void setRevisionType(String revisionType) {
        this.revisionType = revisionType;
    }
    public String getQueue() {
        return queue;
    }
    public void setQueue(String queue) {
        this.queue = queue;
    }
    public String getSpecialistQueue() {
        return specialistQueue;
    }
    public void setSpecialistQueue(String specialistQueue) {
        this.specialistQueue = specialistQueue;
    }
    public String getStepName() {
        return stepName;
    }
    public void setStepName(String stepName) {
        this.stepName = stepName;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public String getLastModifiedDate() {
        return lastModifiedDate;
    }
    public void setLastModifiedDate(String lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }
    public String getDeadlineDate() {
        return deadlineDate;
    }
    public void setDeadlineDate(String deadlineDate) {
        this.deadlineDate = deadlineDate;
    }
    public String getDocumentLeader() {
        return documentLeader;
    }
    public void setDocumentLeader(String documentLeader) {
        this.documentLeader = documentLeader;
    }

    @Override
    public String toString() {
        return "ESData [processInstanceId=" + processInstanceId + ", taskId=" + taskId + ", taskName=" + taskName
                + ", taskAssigne=" + taskAssigne + ", bpmPackageNodeId=" + bpmPackageNodeId + ", writingName="
                + writingName + ", writingNodeId=" + writingNodeId + ", revisionType=" + revisionType + ", queue="
                + queue + ", specialistQueue=" + specialistQueue + ", stepName=" + stepName + ", status=" + status
                + ", lastModifiedDate=" + lastModifiedDate + ", deadlineDate=" + deadlineDate + ", documentLeader="
                + documentLeader + "]";
    }
}
