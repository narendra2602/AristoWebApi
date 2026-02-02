package com.aristowebapi.request;

public class EmployeeUploadRequest {

    private String fileName;
    private String fileData; // Base64 CSV data

    public String getFileName() {
        return fileName;
    }
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
    public String getFileData() {
        return fileData;
    }
    public void setFileData(String fileData) {
        this.fileData = fileData;
    }
}
