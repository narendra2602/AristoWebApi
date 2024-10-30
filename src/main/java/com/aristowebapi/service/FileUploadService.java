package com.aristowebapi.service;

import com.aristowebapi.request.FileUploadRequest;
import com.aristowebapi.response.FileUploadResponse;

public interface FileUploadService {
	
	FileUploadResponse uploadFile(FileUploadRequest request);
	FileUploadResponse getIqviaUploadRecord(int login_id);

}
