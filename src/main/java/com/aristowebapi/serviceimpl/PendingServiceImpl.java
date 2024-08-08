package com.aristowebapi.serviceimpl;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aristowebapi.constant.AristoWebLogMsgConstant;
import com.aristowebapi.dao.PendingDao;
import com.aristowebapi.dto.BranchMasterDto;
import com.aristowebapi.dto.DivisionMasterDto;
import com.aristowebapi.dto.MktRepo1;
import com.aristowebapi.dto.PendingData;
import com.aristowebapi.request.PendingRequest;
import com.aristowebapi.response.ApiResponse;
import com.aristowebapi.response.MktRepo1Response;
import com.aristowebapi.response.PendingResponse;
import com.aristowebapi.service.PendingService;
import com.aristowebapi.utility.AppCalculationUtils;

@Service
public class PendingServiceImpl implements PendingService {

	
	Logger logger = LoggerFactory.getLogger(PendingServiceImpl.class);
	@Autowired
	private PendingDao pendingDao;
	private double grandColumnTotal;
	

	private List<DivisionMasterDto> getDivisionData(PendingRequest request)
	{
		List<DivisionMasterDto> divData = null;

		
		divData =pendingDao.getAllDivision();
		return divData;
		
	}

	
	
	private String getTitle(PendingRequest request,PendingData data)
	{
		
		
		
		StringBuilder title=new StringBuilder();
		title.append("All India Pending PI Summary as on  - ");
		title.append(data.getMonth_name());
		return title.toString();

	}

	
	@Override
	public ApiResponse<PendingResponse> getPendingReport(PendingRequest request) {

		logger.info(AristoWebLogMsgConstant.PENDING_REPORT_SERVICE,"getPendingRepoort");
		List<DivisionMasterDto> divData = getDivisionData(request);

		int sz=divData.size();
		int k=0;
		int z=0;
		String title=null;
		
		List<PendingData> reportList = null;
	

		reportList=pendingDao.getPendingPI(request.getMyear(),request.getDivCode(),request.getDepoCode(),request.getCmon(),request.getLoginId(),request.getUtype());

		
		
		PendingResponse response=null;
		List<PendingResponse> saleList = new ArrayList();
		Map<String, Double> division=null;
		Map<String, Double> total=null;
		boolean first=true;
		int size = 0;
		if(reportList!=null)
		size=reportList.size();
		System.out.println("size of pending list "+size);
		int depo_code=0;
		String branch="";
		double columnTotal=0;
		for (int i=0;i<size;i++)
		{
			PendingData data = reportList.get(i);
			if(first)
			{
				depo_code=data.getDepo_code();
				branch=data.getBranch();
				response=new PendingResponse();
				division=new LinkedHashMap();
				total=new LinkedHashMap();
				first=false;
				
				title = getTitle(request, data); 
				
			}
			if(depo_code!=data.getDepo_code())
			{
				response.setDesc(branch);
				z=k;
				for(int b=k;b<sz;b++)
				{
					DivisionMasterDto bm=divData.get(b);
					division.put(bm.getDiv_name(), 0D);
					k++;
				}

				
				division.put("TOTAL", AppCalculationUtils.roundToDecimal2Places(columnTotal));
				response.setDivision(division);
				
				saleList.add(response);
				depo_code=data.getDepo_code();
				branch=data.getBranch();
				columnTotal=0.00;
				
				k=0;
				response=new PendingResponse();
				division=new LinkedHashMap();

			}


			
			// before put please check depo code in branch list if not found put 0 value in map otherwise actual zero
			for(int b=k;b<sz;b++)
			{
				DivisionMasterDto bm=divData.get(b);
				if(bm.getDiv_code()==data.getDiv_code())
				{
					division.put(data.getDiv_name(), data.getVal());
					columnTotal+=data.getVal();
					
					if(total.containsKey(data.getDiv_name()))
					{
						double ggval = total.get(data.getDiv_name())+data.getVal();
						total.put(data.getDiv_name(),AppCalculationUtils.roundToDecimal2Places(ggval));
					}
					else
					{
						total.put(data.getDiv_name(), data.getVal());
					}

					k++;
					break;
				}
				else
				{
					division.put(bm.getDiv_name(), 0D);

					if(total.containsKey(bm.getDiv_name()))
					{
						// do nothing
					}
					else
					{
						total.put(bm.getDiv_name(), 0D);

					}

					
					k++;
				}
			}
			
			
		}
        
		
		if(!first)
		{
			response=new PendingResponse();
			response.setDesc(branch);
			z=k;
			for(int b=k;b<sz;b++)
			{
				DivisionMasterDto bm=divData.get(b);
				division.put(bm.getDiv_name(), 0D);
				k++;
			}
			division.put("TOTAL", columnTotal);

			response.setDivision(division);
			saleList.add(response);



			grandColumnTotal = total.values().stream().mapToDouble(d -> d).sum();

			division=new LinkedHashMap();
			total.put("TOTAL", grandColumnTotal);

			division.putAll(total);
			response=new PendingResponse();
			response.setDesc("Grand Total");
			response.setDivision(division);
			response.setColor(2);
			saleList.add(response);
		}
		return new ApiResponse<PendingResponse>(title!=null?title.toString():"",size,saleList);


		
	}

}
