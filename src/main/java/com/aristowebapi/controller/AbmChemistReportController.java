package com.aristowebapi.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aristowebapi.service.ChemistAuditService;
import com.aristowebapi.utility.AppRequestParameterUtils;
import com.fasterxml.jackson.databind.JsonNode;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("${mrc_base_path}")
@RequiredArgsConstructor
public class AbmChemistReportController {
	
	
	
	
	private final ChemistAuditService service;
	 private final AppRequestParameterUtils appRequestParameterUtils;
	
	 @PostMapping("${mrc_abmchemistreport_savepath}")
	 public ResponseEntity<?> save(@RequestBody JsonNode jsonNode,HttpServletRequest req) {
		 	int requestValues[]=getRequestData(req);
		 	int loginId=requestValues[0];
	        return ResponseEntity.ok(service.saveAudit(jsonNode,loginId));
	    }	
	 @GetMapping("${mrc_abmchemistreport_path}")
	 public ResponseEntity<JsonNode> getById(@PathVariable Long id) {
	        return ResponseEntity.ok(service.getAuditById(id));
	    }
	

	 
	 private int[] getRequestData(HttpServletRequest req)
		{
			String authHeader = req.getHeader("Authorization");
			int requestValues[]=appRequestParameterUtils.getRequestBodyParameters(authHeader);
			
			return requestValues;
		}

}
