package com.aristowebapi.controller;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aristowebapi.constant.AristoWebLogMsgConstant;
import com.aristowebapi.request.MktRepo11Request;
import com.aristowebapi.request.MktRepo1Request;
import com.aristowebapi.request.MktRepo2Request;
import com.aristowebapi.request.MktRepo3Request;
import com.aristowebapi.request.MktRepo4Request;
import com.aristowebapi.request.MktRepo5Request;
import com.aristowebapi.request.MktRepo6Request;
import com.aristowebapi.request.MktRepo7Request;
import com.aristowebapi.request.MktRepo8Request;
import com.aristowebapi.request.MktRepo9Request;
import com.aristowebapi.request.ViewRequest;
import com.aristowebapi.response.ApiResponse;
import com.aristowebapi.response.MktRepo11Response;
import com.aristowebapi.response.MktRepo1AchResponse;
import com.aristowebapi.response.MktRepo1Response;
import com.aristowebapi.response.MktRepo2Response;
import com.aristowebapi.response.MktRepo3Response;
import com.aristowebapi.response.MktRepo4Response;
import com.aristowebapi.response.MktRepo5Response;
import com.aristowebapi.response.MktRepo6Response;
import com.aristowebapi.response.MktRepo7Response;
import com.aristowebapi.response.MktRepo8Response;
import com.aristowebapi.response.MktRepo9Response;
import com.aristowebapi.service.MktRepo10Service;
import com.aristowebapi.service.MktRepo11Service;
import com.aristowebapi.service.MktRepo1Service;
import com.aristowebapi.service.MktRepo2Service;
import com.aristowebapi.service.MktRepo3Service;
import com.aristowebapi.service.MktRepo4Service;
import com.aristowebapi.service.MktRepo5Service;
import com.aristowebapi.service.MktRepo6Service;
import com.aristowebapi.service.MktRepo7Service;
import com.aristowebapi.service.MktRepo8Service;
import com.aristowebapi.service.MktRepo9Service;
import com.aristowebapi.serviceimpl.JwtService;
import com.aristowebapi.utility.AppRequestParameterUtils;

@RestController
@CrossOrigin
@RequestMapping("${mrc_base_path}")
public class MktReportController {
	
	
	Logger logger = LoggerFactory.getLogger(MktReportController.class);

	@Autowired
	private AppRequestParameterUtils appRequestParameterUtils;

	
	@Autowired
	private JwtService jwtService;
	
	@Autowired
	private MktRepo1Service mktRepo1Service;
	
	@Autowired
	private MktRepo2Service mktRepo2Service;
	
	@Autowired
	private MktRepo3Service mktRepo3Service;
	
	@Autowired
	private MktRepo4Service mktRepo4Service;
	
	@Autowired
	private MktRepo5Service mktRepo5Service;

	@Autowired
	private MktRepo6Service mktRepo6Service;
	
	@Autowired
	private MktRepo7Service mktRepo7Service;

	@Autowired
	private MktRepo8Service mktRepo8Service;
	
	@Autowired
	private MktRepo9Service mktRepo9Service;

	@Autowired
	private MktRepo10Service mktRepo10Service;

	@Autowired
	private MktRepo11Service mktRepo11Service;

	
	@GetMapping("${mrc_repo1_path}")
	public ResponseEntity<ApiResponse<MktRepo1Response>> getMktRepo1(@RequestBody MktRepo1Request request)
	{
		logger.info(AristoWebLogMsgConstant.MKT_REPORT_CONTROLLER_01,"getMktRepo1", request.getMyear(),request.getDivCode());

		return new ResponseEntity<ApiResponse<MktRepo1Response>>(mktRepo1Service.getMktRepo1(request), HttpStatus.OK);
	
	}

	@GetMapping("${mrc_repo1ach_path}")
	public ResponseEntity<ApiResponse<MktRepo1AchResponse>> getMktRepo1Ach(@RequestBody MktRepo1Request request)
	{
		logger.info(AristoWebLogMsgConstant.MKT_REPORT_CONTROLLER_01,"getMktRepo1", request.getMyear(),request.getDivCode());

		return new ResponseEntity<ApiResponse<MktRepo1AchResponse>>(mktRepo1Service.getMktRepo1Ach(request), HttpStatus.OK);
	
	}

	@GetMapping("${mrc_repo2_path}")
	public ResponseEntity<ApiResponse<MktRepo2Response>> getMktRepo2(@RequestBody MktRepo2Request request)
	{
		logger.info(AristoWebLogMsgConstant.MKT_REPORT_CONTROLLER_02,"getMktRepo2", request.getMyear(),request.getDivCode());
		return new ResponseEntity<ApiResponse<MktRepo2Response>>(mktRepo2Service.getMktRepo2(request), HttpStatus.OK);
	
	}
	
	@GetMapping("${mrc_repo3_path}")
	public ResponseEntity<ApiResponse<MktRepo3Response>> getMktRepo3(@RequestBody MktRepo3Request request,HttpServletRequest req)
	{
		String authHeader = req.getHeader("Authorization");
		int requestValues[]=appRequestParameterUtils.getRequestBodyParameters(authHeader);
        int loginId=requestValues[0]; 
        int userType=requestValues[1];

         System.out.println("login id is "+loginId);
         request.setLoginId(loginId);
         request.setUtype(userType);
		
		return new ResponseEntity<ApiResponse<MktRepo3Response>>(mktRepo3Service.getMktRepo3(request), HttpStatus.OK);
	
	}

	@GetMapping("${mrc_repo4_path}")
	public ResponseEntity<ApiResponse<MktRepo4Response>> getMktRepo4(@RequestBody MktRepo4Request request)
	{
	    logger.info("MktRep4 Controller div_code {}", request.getDivCode() );
		return new ResponseEntity<ApiResponse<MktRepo4Response>>(mktRepo4Service.getMktRepo4(request), HttpStatus.OK);
	
	}

	@GetMapping("${mrc_repo5_path}")
	public ResponseEntity<ApiResponse<MktRepo5Response>> getMktRepo5(@RequestBody MktRepo5Request request)
	{
		logger.info(AristoWebLogMsgConstant.MKT_REPORT_CONTROLLER_05,"getMktRepo5", request.getMyear(),request.getDivCode());
		return new ResponseEntity<ApiResponse<MktRepo5Response>>(mktRepo5Service.getMktRepo5(request), HttpStatus.OK);
	
	}

	@GetMapping("${mrc_repo6_path}")
	public ResponseEntity<ApiResponse<MktRepo6Response>> getMktRepo6(@RequestBody MktRepo6Request request)
	{
		logger.info(AristoWebLogMsgConstant.MKT_REPORT_CONTROLLER_06,"getMktRepo6", request.getMyear(),request.getDivCode());

		return new ResponseEntity<ApiResponse<MktRepo6Response>>(mktRepo6Service.getMktRepo6(request), HttpStatus.OK);
	
	}
	
	@GetMapping("${mrc_repo7_path}")
	public ResponseEntity<ApiResponse<MktRepo7Response>> getMktRepo7(@RequestBody MktRepo7Request request)
	{
		logger.info(AristoWebLogMsgConstant.MKT_REPORT_CONTROLLER_07,"getMktRepo7", request.getMyear(),request.getDivCode());

		return new ResponseEntity<ApiResponse<MktRepo7Response>>(mktRepo7Service.getMktRepo7(request), HttpStatus.OK);
	
	}

	@GetMapping("${mrc_repo8_path}")
	public ResponseEntity<ApiResponse<MktRepo8Response>> getMktRepo8(@RequestBody MktRepo8Request request)
	{
		logger.info(AristoWebLogMsgConstant.MKT_REPORT_CONTROLLER_08,"getMktRepo8", request.getMyear(),request.getDivCode());

		return new ResponseEntity<ApiResponse<MktRepo8Response>>(mktRepo8Service.getMktRepo8(request), HttpStatus.OK);
	
	}
	
	@GetMapping("${mrc_repo9_path}")
	public ResponseEntity<ApiResponse<MktRepo9Response>> getMktRepo9(@RequestBody MktRepo9Request request)
	{
		logger.info(AristoWebLogMsgConstant.MKT_REPORT_CONTROLLER_09,"getMktRepo9", request.getMyear(),request.getDivCode());

		return new ResponseEntity<ApiResponse<MktRepo9Response>>(mktRepo9Service.getMktRepo9(request), HttpStatus.OK);
	
	}

	@GetMapping("${mrc_repo10_path}")
	public ResponseEntity<ApiResponse<MktRepo9Response>> getMktRepo10(@RequestBody ViewRequest request)
	{
		logger.info(AristoWebLogMsgConstant.MKT_REPORT_CONTROLLER_10,"getMktRepo10", request.getMyear(),request.getDivCode());

		return new ResponseEntity<ApiResponse<MktRepo9Response>>(mktRepo10Service.getMktRepo10(request), HttpStatus.OK);
	
	}

	@GetMapping("${mrc_repo11_path}")
	public ResponseEntity<ApiResponse<MktRepo11Response>> getMktRepo11(@RequestBody MktRepo11Request request)
	{
		logger.info(AristoWebLogMsgConstant.MKT_REPORT_CONTROLLER_11,"getMktRepo11", request.getMyear(),request.getDivCode());

		return new ResponseEntity<ApiResponse<MktRepo11Response>>(mktRepo11Service.getMktRepo11(request), HttpStatus.OK);
	
	}


}
