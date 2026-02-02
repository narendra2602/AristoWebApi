package com.aristowebapi.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aristowebapi.request.FileUploadRequest;
import com.aristowebapi.response.FileUploadResponse;
import com.aristowebapi.service.FieldSetupUploadService;
import com.aristowebapi.utility.AppRequestParameterUtils;

@RestController
@CrossOrigin 
@RequestMapping("${mrc_base_path}")
public class FieldSetupUploadController {
	
	@Autowired
	private AppRequestParameterUtils appRequestParameterUtils;
	
	@Autowired
	private FieldSetupUploadService fieldSetupUploadService;
	
	   @PostMapping("/uploadfieldsetup")
	    public ResponseEntity<FileUploadResponse> uploadFiles(@RequestBody FileUploadRequest request,HttpServletRequest req) {
	        try {
	            

	        	int requestValues[]=getRequestData(req);
	    		request.setLoginId(requestValues[0]);
	    			return ResponseEntity.status(HttpStatus.OK).body(fieldSetupUploadService.uploadFile(request));
	        } catch (Exception e) {
	            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED)
	                    .body(new FileUploadResponse("Error while uploading file!",0,0));
	        }
	    }
	   private int[] getRequestData(HttpServletRequest req)
		{
			String authHeader = req.getHeader("Authorization");
			int requestValues[]=appRequestParameterUtils.getRequestBodyParameters(authHeader);
			return requestValues;
		}


}
