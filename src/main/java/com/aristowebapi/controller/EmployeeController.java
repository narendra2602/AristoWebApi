package com.aristowebapi.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aristowebapi.request.EmployeeUploadRequest;
import com.aristowebapi.serviceimpl.EmployeeService;
import com.aristowebapi.utility.AppRequestParameterUtils;

@RestController
@CrossOrigin 
@RequestMapping("${mrc_base_path}")
public class EmployeeController {

	@Autowired
	private AppRequestParameterUtils appRequestParameterUtils;
	
    @Autowired
    private EmployeeService service;

    @PostMapping("${mrc_upload_path}")
    public ResponseEntity<String> uploadCSV(
            @RequestBody EmployeeUploadRequest request,
            HttpServletRequest req) {

        if (request.getFileData() == null || request.getFileData().isEmpty()) {
            return ResponseEntity.badRequest().body("CSV data is empty");
        }

        service.saveCSVFromJson(request);
        return ResponseEntity.ok("CSV data imported successfully");
    }    
    private int[] getRequestData(HttpServletRequest req)
   	{
   		String authHeader = req.getHeader("Authorization");
   		int requestValues[]=appRequestParameterUtils.getRequestBodyParameters(authHeader);
   		return requestValues;
   	}
}
