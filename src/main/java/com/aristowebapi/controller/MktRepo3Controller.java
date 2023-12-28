package com.aristowebapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aristowebapi.request.MktRepo3Request;
import com.aristowebapi.response.ApiResponse;
import com.aristowebapi.response.MktRepo3Response;
import com.aristowebapi.service.MktRepo3Service;

@RestController
@CrossOrigin
@RequestMapping("/api/mis")
public class MktRepo3Controller {
	
	@Autowired
	private MktRepo3Service mktRepo3Service;
	@GetMapping("/mktrepo3")
	public ResponseEntity<ApiResponse<MktRepo3Response>> getMktRepo3(@RequestBody MktRepo3Request request)
	{
		return new ResponseEntity<ApiResponse<MktRepo3Response>>(mktRepo3Service.getMktRepo3(request), HttpStatus.OK);
	
	}


}
