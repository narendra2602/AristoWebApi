package com.aristowebapi.serviceimpl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aristowebapi.constant.AristoWebLogMsgConstant;
import com.aristowebapi.constant.AristoWebMessageConstant;
import com.aristowebapi.dao.NearExpiryDao;
import com.aristowebapi.dto.NearExpiry;
import com.aristowebapi.exception.ApiException;
import com.aristowebapi.request.NearExpiryRequest;
import com.aristowebapi.response.ApiResponse;
import com.aristowebapi.response.NearExpiryBranchWiseResponse;
import com.aristowebapi.response.NearExpiryResponse;
import com.aristowebapi.service.NearExpiryService;
import com.aristowebapi.utility.AppCalculationUtils;

@Service
public class NearExpiryServiceImpl implements NearExpiryService{

	
	Logger logger = LoggerFactory.getLogger(NearExpiryServiceImpl.class);
	
	@Autowired
	private NearExpiryDao nearExpiryDao;

	@Autowired
	private AristoWebMessageConstant  aristoWebMessageConstant;

	
	public String lupdate="";
	
	private String getTitle(NearExpiryRequest request,NearExpiry data)
	{
		StringBuilder title=new StringBuilder();
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		title.append(aristoWebMessageConstant.divisionMap.get(String.valueOf(request.getDivCode())));

		title.append("[");
		title.append(request.getDepoCode()>0?data.getBranch_name():data.getPname());
		title.append(",");
		title.append(data.getPack());
		title.append("]- NEAR EXPIRY/EXPIRED BATCHWISE STOCK AS ON : ");
//		title.append(sdf.format(data.getFiledate()));
		title.append((data.getFiledate()));
		
		return title.toString();

	}


	
	@Override
	public ApiResponse<NearExpiryResponse> getExpiryReport(NearExpiryRequest request) {
		logger.info(AristoWebLogMsgConstant.NEAR_EXPIRY_SERVICE,"getExpiryReport");
		List<NearExpiry> nearExpiryList=null;
		int size = 0;
		try {

			System.out.println(request.getDepoCode()+" "+request.getLoginId()+" "+request.getDivCode()+" "+request.getCode()+" "+request.getUserType()+" "+request.getReportType());
			
			nearExpiryList=nearExpiryDao.getNearExpiryReport(request.getDepoCode(),request.getLoginId(),request.getDivCode(),request.getCode(),request.getUserType(),request.getReportType());
			


			
			size=nearExpiryList.size();
		logger.info("size of the Near expiry data data is {}",size);

		NearExpiryResponse response=null;
		List<NearExpiryResponse> saleList = new ArrayList();

		boolean first=true;
		String title=null;
		String branch="";
		int expired_stock=0;
		int expired90=0;
		int expired180=0;
		int expiredabove180=0;
		int expiredabove365=0;
		double total=0.00;

		int gexpired_stock=0;
		int gexpired90=0;
		int gexpired180=0;
		int gexpiredabove180=0;
		int gexpiredabove365=0;
		double gtotal=0.00;

		for (int i=0;i<size;i++)
		{
			NearExpiry data = nearExpiryList.get(i);
			
			
			if(first)
			{
				title = getTitle(request, data);
				branch=data.getBranch_name();
				first=false;
			}
			
			if(!branch.equalsIgnoreCase(data.getBranch_name()))
			{
				response=new NearExpiryResponse();
				response.setBranch(branch+" Total");
				response.setPname("");
				response.setBatchNo("");
				response.setExpiryDate("");
				response.setNetRate(0.00);
				response.setExpiredStock(expired_stock);
				response.setExpiry_31To_90Days(expired90);
				response.setExpiry_91To_180Days(expired180);
				response.setExpiry_181To_365Days(expiredabove180);
				response.setAbove_365Days(expiredabove365);
				response.setTotalValue(total);
				response.setColor(1);
		    	saleList.add(response);

				expired_stock=0;
				expired90=0;
				expired180=0;
				expiredabove180=0;
				expiredabove365=0;
				total=0.00;

				branch=data.getBranch_name();
				
			}
			
			response=new NearExpiryResponse();
			response.setBranch(data.getBranch_name());
			response.setPname("");
			response.setBatchNo(data.getBatch_no());
			response.setExpiryDate(data.getExpiry_date());
			response.setNetRate(data.getNet_rate());
			response.setExpiredStock(data.getExpired_stock());
			response.setExpiry_31To_90Days(data.getExpired90());
			response.setExpiry_91To_180Days(data.getExpired180());
			response.setExpiry_181To_365Days(data.getExpiredAbove180());
			response.setAbove_365Days(data.getAbove365());
			response.setTotalValue(data.getTotal_value());
			
			expired_stock+=data.getExpired_stock();
			expired90+=data.getExpired90();
			expired180+=data.getExpired180();
			expiredabove180+=data.getExpiredAbove180();
			expiredabove365+=data.getAbove365();
			total+=data.getTotal_value();

			gexpired_stock+=data.getExpired_stock();
			gexpired90+=data.getExpired90();
			gexpired180+=data.getExpired180();
			gexpiredabove180+=data.getExpiredAbove180();
			gexpiredabove365+=data.getAbove365();
			gtotal=AppCalculationUtils.addDouble(gtotal, data.getTotal_value());
//			gtotal+=data.getTotal_value();

			saleList.add(response);

		} //end of for loop

		response=new NearExpiryResponse();
		response.setBranch(branch+" Total");
		response.setPname("");
		response.setBatchNo("");
		response.setExpiryDate("");
		response.setNetRate(0.00);
		response.setExpiredStock(expired_stock);
		response.setExpiry_31To_90Days(expired90);
		response.setExpiry_91To_180Days(expired180);
		response.setExpiry_181To_365Days(expiredabove180);
		response.setAbove_365Days(expiredabove365);
		response.setTotalValue(total);
		response.setColor(1);
    	saleList.add(response);
    	
		response=new NearExpiryResponse();
		response.setBranch("Grand Total");
		response.setPname("");
		response.setBatchNo("");
		response.setExpiryDate("");
		response.setNetRate(0.00);
		response.setExpiredStock(gexpired_stock);
		response.setExpiry_31To_90Days(gexpired90);
		response.setExpiry_91To_180Days(gexpired180);
		response.setExpiry_181To_365Days(gexpiredabove180);
		response.setAbove_365Days(gexpiredabove365);
		response.setTotalValue(gtotal);
		response.setColor(2);
    	saleList.add(response);

		
		ApiResponse<NearExpiryResponse> apiResponse = new ApiResponse<>(title!=null?title.toString():"", size,lupdate,saleList);
		return apiResponse;
		
		} catch (Exception e) {
			logger.error(AristoWebLogMsgConstant.NEAR_EXPIRY_SERVICE,"getExpiryReport");
			throw new ApiException(e.getMessage());
		}
		
	}



	@Override
	public ApiResponse<NearExpiryBranchWiseResponse> getExpiryReporBranchwise(NearExpiryRequest request) {
		logger.info(AristoWebLogMsgConstant.NEAR_EXPIRY_SERVICE,"getExpiryReporBranchwise");
		List<NearExpiry> nearExpiryList=null;
		int size = 0;
		
		try {

			System.out.println(request.getDepoCode()+" "+request.getLoginId()+" "+request.getDivCode()+" "+request.getReportType()+" "+request.getUserType());
			
			nearExpiryList=nearExpiryDao.getNearExpiryReportBranchwise(request.getDepoCode(),request.getLoginId(),request.getDivCode(),request.getReportType(),request.getUserType());
			
			
			size=nearExpiryList.size();
		logger.info("size of the data is {}",size);

		NearExpiryBranchWiseResponse response=null;
		List<NearExpiryBranchWiseResponse> saleList = new ArrayList();

		boolean first=true;
		String title=null;
		String pname="";
		int expired_stock=0;
		int expired90=0;
		int expired180=0;
		int expiredabove180=0;
		int expiredabove365=0;
		double total=0.00;

		int gexpired_stock=0;
		int gexpired90=0;
		int gexpired180=0;
		int gexpiredabove180=0;
		int gexpiredabove365=0;
		double gtotal=0.00;
		String branch="";
		for (int i=0;i<size;i++)
		{
			NearExpiry data = nearExpiryList.get(i);
			
			if(first)
			{
				title = getTitle(request, data);
				pname=data.getPname();
				branch=data.getBranch_name();
				first=false;
			}
			
			if(!pname.equalsIgnoreCase(data.getPname()) && request.getReportType()==1)
			{
				response=new NearExpiryBranchWiseResponse();
				response.setBranch(data.getBranch_name());
				response.setProduct(pname+" Total");
				response.setPack("");
				response.setBatchNo("");
				response.setExpiryDate("");
				response.setNetRate(0.00);
				response.setExpiredStock(expired_stock);
				response.setExpiry_31To_90Days(expired90);
				response.setExpiry_91To_180Days(expired180);
				response.setExpiry_181To_365Days(expiredabove180);
				response.setAbove_365Days(expiredabove365);
				response.setTotalValue(total);
				response.setColor(1);
		    	saleList.add(response);

				expired_stock=0;
				expired90=0;
				expired180=0;
				expiredabove180=0;
				expiredabove365=0;
				total=0.00;

				pname=data.getPname();
				branch=data.getBranch_name();
				
			}

			response=new NearExpiryBranchWiseResponse();
			response.setBranch(data.getBranch_name());
			response.setProduct(data.getPname());
			response.setPack(data.getPack());
			response.setBatchNo(data.getBatch_no());
			response.setExpiryDate(data.getExpiry_date());
			response.setNetRate(data.getNet_rate());
			response.setExpiredStock(data.getExpired_stock());
			response.setExpiry_31To_90Days(data.getExpired90());
			response.setExpiry_91To_180Days(data.getExpired180());
			response.setExpiry_181To_365Days(data.getExpiredAbove180());
			response.setAbove_365Days(data.getAbove365());
			response.setTotalValue(data.getTotal_value());
			
			expired_stock+=data.getExpired_stock();
			expired90+=data.getExpired90();
			expired180+=data.getExpired180();
			expiredabove180+=data.getExpiredAbove180();
			expiredabove365+=data.getAbove365();
			total+=data.getTotal_value();

			gexpired_stock+=data.getExpired_stock();
			gexpired90+=data.getExpired90();
			gexpired180+=data.getExpired180();
			gexpiredabove180+=data.getExpiredAbove180();
			gexpiredabove365+=data.getAbove365();
			gtotal=AppCalculationUtils.addDouble(gtotal, data.getTotal_value());
//			gtotal+=data.getTotal_value();

			saleList.add(response);


		} //end of for loop

		if(request.getReportType()==1)
		{
			response=new NearExpiryBranchWiseResponse();
			response.setBranch(branch);
			response.setProduct(pname+" Total");
			response.setPack("");
			response.setBatchNo("");
			response.setExpiryDate("");
			response.setNetRate(0.00);
			response.setExpiredStock(expired_stock);
			response.setExpiry_31To_90Days(expired90);
			response.setExpiry_91To_180Days(expired180);
			response.setExpiry_181To_365Days(expiredabove180);
			response.setAbove_365Days(expiredabove365);
			response.setTotalValue(total);
			response.setColor(1);
			saleList.add(response);
		}
    	
		response=new NearExpiryBranchWiseResponse();
		response.setBranch(branch);
		response.setProduct("Grand Total");
		response.setPack("");
		response.setBatchNo("");
		response.setExpiryDate("");
		response.setNetRate(0.00);
		response.setExpiredStock(gexpired_stock);
		response.setExpiry_31To_90Days(gexpired90);
		response.setExpiry_91To_180Days(gexpired180);
		response.setExpiry_181To_365Days(gexpiredabove180);
		response.setAbove_365Days(gexpiredabove365);
		response.setTotalValue(gtotal);
		response.setColor(2);
    	saleList.add(response);

		
		ApiResponse<NearExpiryBranchWiseResponse> apiResponse = new ApiResponse<>(title!=null?title.toString():"", size,lupdate,saleList);
		return apiResponse;
		
		} catch (Exception e) {
			logger.error(AristoWebLogMsgConstant.NEAR_EXPIRY_SERVICE,"getExpiryReport");
			throw new ApiException(e.getMessage());
		}
	}
}
