package com.aristowebapi.service;

import com.aristowebapi.request.FileUploadRequest;
import com.aristowebapi.response.FileUploadResponse;

public interface FieldSetupUploadService {
	
	FileUploadResponse uploadFile(FileUploadRequest request);

}
