package com.aristowebapi.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aristowebapi.constant.AristoWebLogMsgConstant;
import com.aristowebapi.request.UserReportRightsRequest;
import com.aristowebapi.request.UserRightsPmtRequest;
import com.aristowebapi.request.UserRightsRequest;
import com.aristowebapi.request.UserRightsTerRequest;
import com.aristowebapi.response.ApiResponse;
import com.aristowebapi.response.DashBoardDataResponse;
import com.aristowebapi.response.UserReportRightsTabResponse;
import com.aristowebapi.response.UserRightsHqResponse;
import com.aristowebapi.response.UserRightsPmtResponse;
import com.aristowebapi.response.UserRightsResponse;
import com.aristowebapi.service.UserRightsService;
import com.aristowebapi.utility.AppRequestParameterUtils;

@RestController
@CrossOrigin
@RequestMapping("${mrc_base_path}")
public class UserRightsController {
	
	
	Logger logger = LoggerFactory.getLogger(NearExpiryController.class);

	@Autowired
	private AppRequestParameterUtils appRequestParameterUtils;

	
	@Autowired
	private UserRightsService userRightsService;

	 @GetMapping("${mrc_userdivision_path}")
	public ResponseEntity<ApiResponse<UserRightsResponse>> getUserDivisionList(@PathVariable("userId") int userId)
	{
		
		return new ResponseEntity<ApiResponse<UserRightsResponse>>(userRightsService.getUserDivisionList(userId), HttpStatus.OK);
	
	}
	 @PostMapping("${mrc_saveuserdivision_path}")
		public ResponseEntity<String>  saveUserDivisionList(@RequestBody List<UserRightsRequest> request)
		{
			System.out.println(request);
			 
			if(userRightsService.saveUserDivisionList(request)>0)
	        	return ResponseEntity.ok("Division Saved successfully");
	        else
	        	return ResponseEntity.ok("Error while Save Division List");
		
		}
	 
	 @GetMapping("${mrc_userdepo_path}")
	public ResponseEntity<ApiResponse<UserRightsResponse>> getUserDepoList(@PathVariable("userId") int userId)
	{
		
		return new ResponseEntity<ApiResponse<UserRightsResponse>>(userRightsService.getUserDepoList(userId), HttpStatus.OK);
	
	}

	 @PostMapping("${mrc_saveuserdepo_path}")
		public ResponseEntity<String>  saveUserDepoList(@RequestBody List<UserRightsRequest> request)
		{
			System.out.println(request);
			 
			if(userRightsService.saveUserDepoList(request)>0)
	        	return ResponseEntity.ok("Depo Saved successfully");
	        else
	        	return ResponseEntity.ok("Error while Save Depo List");
		}

	 @GetMapping("${mrc_userreport_path}")
	public ResponseEntity<ApiResponse<UserReportRightsTabResponse>> getUserReportList(@PathVariable("userId") int userId)
	{
		
		return new ResponseEntity<ApiResponse<UserReportRightsTabResponse>>(userRightsService.getUserReportList(userId), HttpStatus.OK);
	
	} 
	 
	 @PostMapping("${mrc_saveuserreport_path}")
		public ResponseEntity<String>  saveUserRepoList(@RequestBody List<UserReportRightsRequest> request)
		{
		 System.out.println("I m in save user menu report controller");
			System.out.println(request);
			 
			if(userRightsService.saveUserReportList(request)>0)
	        	return ResponseEntity.ok("Reports Saved successfully");
	        else
	        	return ResponseEntity.ok("Error while Save Reports List");
		}

	 @GetMapping("${mrc_userpmt_path}")
	public ResponseEntity<ApiResponse<UserRightsPmtResponse>> getUserPmtGroupList(@PathVariable("userId") int userId)
	{
		
		return new ResponseEntity<ApiResponse<UserRightsPmtResponse>>(userRightsService.getUserPmtGroupList(userId), HttpStatus.OK);
	
	} 

	 @PostMapping("${mrc_saveuserpmt_path}")
		public ResponseEntity<String>  saveUserPmtGroupList(@RequestBody UserRightsPmtRequest request)
		{
			System.out.println(request);
			 
			if(userRightsService.saveUserPmtGroupList(request)>0)
	        	return ResponseEntity.ok("Pmt Group Saved successfully");
	        else
	        	return ResponseEntity.ok("Error while Save Pmt Group List");
		
		}
 
	 @GetMapping("${mrc_userter_path}")
	public ResponseEntity<ApiResponse<UserRightsHqResponse>> getUserTerList(@PathVariable("userId") int userId,@PathVariable("myear") int myear,@PathVariable("divCode") int divCode,@PathVariable("depoCode") int depoCode)
	{
		
		return new ResponseEntity<ApiResponse<UserRightsHqResponse>>(userRightsService.getUserTerList(userId,myear,divCode,depoCode), HttpStatus.OK);
	
	} 

	 @PostMapping("${mrc_saveuserter_path}")
		public ResponseEntity<String>  saveUserTerList(@RequestBody List<UserRightsTerRequest> request)
		{
			System.out.println(request);
			 
			if(userRightsService.saveUserTerList(request)>0)
	        	return ResponseEntity.ok("Hq Saved successfully");
	        else
	        	return ResponseEntity.ok("Error while Save Hq List");
		
		}

		@GetMapping("${mrc_DivCombo_path}")
		public ResponseEntity<ApiResponse<DashBoardDataResponse>> getDivisionList(@PathVariable("userId") int userId)
		{
			return new ResponseEntity<ApiResponse<DashBoardDataResponse>>(userRightsService.getDivisionList(userId), HttpStatus.OK);
		
		}

		@GetMapping("${mrc_DepoCombo_path}")
		public ResponseEntity<ApiResponse<DashBoardDataResponse>> getBranchList(@PathVariable("userId") int userId)
		{

			return new ResponseEntity<ApiResponse<DashBoardDataResponse>>(userRightsService.getBranchList(userId), HttpStatus.OK);

		
		}

}
