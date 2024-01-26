package com.aristowebapi.serviceimpl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aristowebapi.constant.AristoWebLogMsgConstant;
import com.aristowebapi.dao.HOMarketingRepo1Dao;
import com.aristowebapi.dto.HOMarketingRepo1;
import com.aristowebapi.exception.ApiException;
import com.aristowebapi.request.HOMarketingRepo1Request;
import com.aristowebapi.response.ApiResponse;
import com.aristowebapi.response.HOMarketingRepo1Response;
import com.aristowebapi.service.HOMarketingRepo1Service;
import com.aristowebapi.utility.AppCalculationUtils;

@Service
public class HOMarketingRepo1ServiceImpl implements HOMarketingRepo1Service{

	Logger logger = LoggerFactory.getLogger(HOMarketingRepo1ServiceImpl.class);
	@Autowired
	private HOMarketingRepo1Dao hOMarketingRepo1Dao;

	public String lupdate="";
	
	private String getTitle(HOMarketingRepo1Request request,HOMarketingRepo1 data)
	{
		StringBuilder title=new StringBuilder();
		if(request.getDataType()<3)
			title.append(request.getDataType()==1?"Product-> ":"Group-> ");
		title.append(data.getPname());
		title.append(" ACHIEVEMENT UPTO ");
		title.append(data.getEmname());
		return title.toString();

	}

	
	@Override
	public ApiResponse<HOMarketingRepo1Response> getHoMarketingRepo1(HOMarketingRepo1Request request) {

		logger.info(AristoWebLogMsgConstant.HO_MARKEING_REPO1_SERVICE,"getHOMarketingRepo1");
		List<HOMarketingRepo1> HOMarketingRepo1List=null;
		int size = 0;
		try {
			
			System.out.println("data type "+request.getDataType());
			
			if(request.getDataType()==1)
			{
			HOMarketingRepo1List=hOMarketingRepo1Dao.getHoMarketingRepo1Product(request.getMyear(),request.getDivCode(),request.getCode(),request.getSmon(),request.getEmon()
					,request.getUtype(),request.getLoginId(),request.getRepType());
			}
		
			else if(request.getDataType()==2)
			{
				HOMarketingRepo1List=hOMarketingRepo1Dao.getHoMarketingRepo1Group(request.getMyear(),request.getDivCode(),request.getCode(),request.getSmon(),request.getEmon()
						,request.getUtype(),request.getLoginId(),request.getRepType());
			}

			else if(request.getDataType()==3)
			{
				HOMarketingRepo1List=hOMarketingRepo1Dao.getHoMarketingRepo1All(request.getMyear(),request.getDivCode(),request.getSmon(),request.getEmon()
						,request.getUtype(),request.getLoginId(),request.getRepType());
			}

			
			size = HOMarketingRepo1List.size();
			logger.info("size of the data is {}",size);
		
			HOMarketingRepo1Response response=null;
		List<HOMarketingRepo1Response> saleList = new ArrayList();

		boolean first=true;
		String title=null;
		
		double monsal=0.00;
		double montar=0.00;
		double cumsale=0.0;
		double cumtar=0.00;
		double lyssale=0.00;

		double gmonsal=0.00;
		double gmontar =0.00;
		double gcumsale=0.0;
		double gcumtar=0.00;
		double glyssale=0.00;

		int depo_code=0;
		int fs=0;
		int gfs=0;
		String name="";
		for (int i=0;i<size;i++)
		{
			HOMarketingRepo1 data = HOMarketingRepo1List.get(i);
			
			if(request.getRepType()==1 && data.getDepo_code()==0)
				continue ;
			
			if(first)
			{
				title = getTitle(request, data);
				depo_code=data.getSdepo_code();
				name=data.getDepo_name();
				fs=data.getFs();
				first=false;
			}
			
			if(depo_code!=data.getSdepo_code() && request.getRepType()==1)
			{
				response=new HOMarketingRepo1Response();
				response.setName(name+" Br");
				response.setMonTgt(montar);
				response.setMonSale(Math.round(monsal));
				response.setAch(AppCalculationUtils.calculateAch(monsal, montar));
				response.setCumTgt(Math.round(cumtar));
				response.setCumSale(Math.round(cumsale));
				response.setLastYear(lyssale);
				response.setCumAch(AppCalculationUtils.calculateAch(cumsale, cumtar));
				response.setCumGth(AppCalculationUtils.calculateGth(cumsale, lyssale));
				response.setPmr(AppCalculationUtils.calculatePmr(cumsale, fs));
		    	saleList.add(response);


		    	
				depo_code=data.getSdepo_code();
				name=data.getDepo_name();
				fs=data.getFs();
				montar=0.00;
				monsal=0.00;
				lyssale=0.00;
				cumtar=0.00;
				cumsale=0.00;
				

			}
			response=new HOMarketingRepo1Response();
			response.setName(request.getRepType()==1?data.getTerr_name():data.getDepo_name());
			response.setMonTgt(data.getMontar());
			response.setMonSale(data.getMonsal());
			response.setAch(AppCalculationUtils.calculateAch(data.getMonsal(), data.getMontar()));
			response.setCumTgt(data.getTargetval());
			response.setCumSale(data.getSaleval());
			response.setLastYear(data.getLysval());
			response.setCumAch(AppCalculationUtils.calculateAch(data.getSaleval(), data.getTargetval()));
			response.setCumGth(AppCalculationUtils.calculateGth(data.getSaleval(), data.getLysval()));
			response.setPmr(AppCalculationUtils.calculatePmr(data.getSaleval(), data.getFs()));
	    	saleList.add(response);
	    	montar+=data.getMontar();
	    	monsal+=data.getMonsal();
	    	cumsale+=data.getSaleval();
	    	cumtar+=data.getTargetval();
	    	lyssale+=data.getLysval();
	    	gmontar+=data.getMontar();
	    	gmonsal+=data.getMonsal();
//	    	gcumtar+=data.getTargetval();
//	    	gcumsale+=data.getSaleval();
	    	gcumtar=AppCalculationUtils.addDouble(gcumtar, data.getTargetval());
	    	gcumsale=AppCalculationUtils.addDouble(gcumsale, data.getSaleval());
	    	
	    	glyssale+=data.getLysval();
	    	
	    	gfs+=data.getFs();
		} //end of for loop

		
		if(request.getRepType()==1)
		{
			response=new HOMarketingRepo1Response();
			response.setName(name+" Br");
			response.setMonTgt(montar);
			response.setMonSale(Math.round(monsal));
			response.setAch(AppCalculationUtils.calculateAch(monsal, montar));
			response.setCumTgt(Math.round(cumtar));
			response.setCumSale(Math.round(cumsale));
			response.setLastYear(lyssale);
			response.setCumAch(AppCalculationUtils.calculateAch(cumsale, cumtar));
			response.setCumGth(AppCalculationUtils.calculateGth(cumsale, lyssale));
			response.setPmr(AppCalculationUtils.calculatePmr(cumsale, fs));
	    	saleList.add(response);
		}
//		java.text.DecimalFormat df = new java.text.DecimalFormat("#.00");
		
//		System.out.println("gcumtar "+df.format(gcumtar));
		response=new HOMarketingRepo1Response();
		response.setName("GRAND TOTAL");
		response.setMonTgt(gmontar);
		response.setMonSale(Math.round(gmonsal));
		response.setAch(AppCalculationUtils.calculateAch(gmonsal, gmontar));
		response.setCumTgt(Math.round(gcumtar));
		response.setCumSale(Math.round(gcumsale));
		response.setLastYear(glyssale);
		response.setCumAch(AppCalculationUtils.calculateAch(gcumsale, gcumtar));
		response.setCumGth(AppCalculationUtils.calculateGth(gcumsale, glyssale));
		response.setPmr(AppCalculationUtils.calculatePmr(gcumsale, gfs));
    	saleList.add(response);
		
		
		ApiResponse<HOMarketingRepo1Response> apiResponse = new ApiResponse<>(title!=null?title.toString():"", size,lupdate,saleList);
		return apiResponse;
		
		} catch (Exception e) {
			logger.error(AristoWebLogMsgConstant.HO_MARKEING_REPO1_SERVICE_01,"getHoMarketingRepo1");
			throw new ApiException(e.getMessage());
		}
		

	}




}
