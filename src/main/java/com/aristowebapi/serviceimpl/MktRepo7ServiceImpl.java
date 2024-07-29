package com.aristowebapi.serviceimpl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aristowebapi.constant.AristoWebLogMsgConstant;
import com.aristowebapi.constant.AristoWebMessageConstant;
import com.aristowebapi.dao.MktRepo7Dao;
import com.aristowebapi.dto.MktRepo7;
import com.aristowebapi.exception.ApiException;
import com.aristowebapi.request.MktRepo7Request;
import com.aristowebapi.response.ApiResponse;
import com.aristowebapi.response.MktRepo7Response;
import com.aristowebapi.service.MktRepo7Service;

@Service
public class MktRepo7ServiceImpl implements  MktRepo7Service{



	Logger logger = LoggerFactory.getLogger(MktRepo5ServiceImpl.class);
	
	@Autowired
	private MktRepo7Dao mktRepo7Dao;

	@Autowired
	private AristoWebMessageConstant  aristoWebMessageConstant;
	
	private String lupdate="";
	
	private String getTitle(MktRepo7Request request,MktRepo7 data)
	{
		
		StringBuilder title=new StringBuilder();

		title.append(aristoWebMessageConstant.divisionMap.get(String.valueOf(data.getDiv_code())));
		title.append(request.getDepoCode()==0?"All India ":mktRepo7Dao.getBranch(request.getDepoCode())+" Branch: ");
		if(request.getDepoCode()>0)
			title.append(request.getRepType()==0?" H.Q WISE ":request.getRepType()==1?" REGION WISE ":request.getRepType()==2?" AREA WISE ":" ");
		else
			title.append("BRANCH WISE ");
			
		title.append("EXPIRY/BREAKAGE/SALEABLE From  ");
		title.append(data.getSmname());
		title.append(" To ");
		title.append(data.getEmname());
		return title.toString();

	}

	
	@Override
	public ApiResponse<MktRepo7Response> getMktRepo7(MktRepo7Request request) {
		logger.info(AristoWebLogMsgConstant.MKT_REPORT_SERVICE_07,"getMktRepo7");
		
		List<MktRepo7> MktRepo7List=null;
		int size = 0;

		try {
			MktRepo7List=mktRepo7Dao.getWebReportHqwiseSalableExpiryBreakage(request.getMyear(),request.getDivCode(),request.getDepoCode()
					,request.getSmon(),request.getEmon(),request.getUtype(),request.getLoginId(),request.getRepType());
			size = MktRepo7List.size();
			
			logger.info("size of the data is {}",size);
		
		MktRepo7Response response=null;
		List<MktRepo7Response> saleList = new ArrayList();

		boolean first=true;
		String title=null;
		long  sval=0;
		long  eval=0;
		long  bval=0;
		long  rval=0;
		long  shortval=0;
		long  lossval=0;
		long  qltyval=0;
		long  tval=0;


		for (int i=0;i<size;i++)
		{
			MktRepo7 data = MktRepo7List.get(i);
			
			if(data.getDepo_code()==0)
				continue;
			
			if(first)
			{
				title = getTitle(request, data);
				first=false;
			}
			
			response=new MktRepo7Response();
			response.setBranch(data.getBranch_name());
			response.setHqName(request.getDepoCode()>0?data.getDepo_name():"");
	    	response.setSaleable(data.getSalable());
	    	response.setExpiry(data.getExpiry());
	    	response.setBreakage(data.getBreakage());
	    	response.setRateDiff(data.getRatediff());
	    	response.setShortReceived(data.getShort());
	    	response.setLossInTransit(data.getLoss());
	    	response.setQualityIssue(data.getQlty());
	    	response.setOther(0);
	    	response.setTotal(data.getTotal());

	    	saleList.add(response);

	    	sval+=data.getSalable();
	    	eval+=data.getExpiry();
	    	bval+=data.getBreakage();
	    	rval+=data.getRatediff();
	    	shortval+=data.getShort();
	    	lossval+=data.getLoss();
	    	qltyval+=data.getQlty();
	    	tval+=data.getSalable()+data.getExpiry()+data.getBreakage()+data.getRatediff()+data.getShort()+data.getLoss()+data.getQlty();

		} //end of for loop

		if(!first)
		{

			response=new MktRepo7Response();
			response.setBranch("GRAND TOTAL");
			response.setHqName("");
	    	response.setSaleable(sval);
	    	response.setExpiry(eval);
	    	response.setBreakage(bval);
	    	response.setRateDiff(rval);
	    	response.setShortReceived(shortval);
	    	response.setLossInTransit(lossval);
	    	response.setQualityIssue(qltyval);
	    	response.setTotal(tval);
			response.setColor(1);

			saleList.add(response);
		}

		
		ApiResponse<MktRepo7Response> apiResponse = new ApiResponse<>(title!=null?title.toString():"",size,lupdate,saleList);
		return apiResponse;
		
		} catch (Exception e) {
			logger.error(AristoWebLogMsgConstant.MKT_REPORT_SERVICE_071,"getMktRepo7");
			throw new ApiException(e.getMessage());
		}
		

	}

	
}

