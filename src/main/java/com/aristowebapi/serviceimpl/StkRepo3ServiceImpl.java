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
import com.aristowebapi.constant.AristoWebMessageConstant;
import com.aristowebapi.dao.StkRepo3Dao;
import com.aristowebapi.dto.MonthDto;
import com.aristowebapi.dto.StkRepo3;
import com.aristowebapi.request.StkRepo3Request;
import com.aristowebapi.response.ApiResponse;
import com.aristowebapi.response.StkRepo3Response;
import com.aristowebapi.service.StkRepo3Service;

@Service
public class StkRepo3ServiceImpl implements StkRepo3Service{

	Logger logger = LoggerFactory.getLogger(StkRepo3ServiceImpl.class);

	private final String monthArray[] = new String[] {"","OCT","NOV","DEC","JAN","FEB","MAR","APR","MAY","JUN","JUL","AUG","SEP"};
	Map<String, Double> groupMap=null;
	long groupColumnTotal=0;
	Map<String, Double> grandTotalMap=null;
	long grandColumnTotal=0;
	
	String lupdate;
	
	@Autowired
	private StkRepo3Dao stkRepo3Dao;
	
	@Autowired
	private AristoWebMessageConstant  aristoWebMessageConstant;

	
	private String getTitle(StkRepo3Request request,StkRepo3 data)
	{

		
		StringBuilder title=new StringBuilder();
		title.append(aristoWebMessageConstant.divisionMap.get(String.valueOf(request.getDivCode())));
		title.append(" STOCKIEST/RUPEES/SALES WISE : ");
		switch(request.getRepType())
		{
		    case 1:	title.append("GROSS SALE ");
		            break;
		    case 2: title.append("CREDIT ");
		    		break;
		    case 3: title.append("NET SALE ");
		    		break;

		}
		title.append("TREND FROM ");
		title.append(data.getSmname());
		title.append(" To ");
		title.append(data.getEmname());

		return title.toString();

	}

	
	@Override
	public ApiResponse<StkRepo3Response> getStkRepo3(StkRepo3Request request) {
		logger.info(AristoWebLogMsgConstant.STK_REPO3_SERVICE,"getStkRepo3");
		
		String title=null;
		List<MonthDto> monthData = stkRepo3Dao.getAllMonth(request.getMyear());
		int sz=monthData.size();
		int k=0;
		int z=0;
		sz=request.getEmon();

		
		Map<String, Long> months=null;
		Map<String, Long> total=null;
		Map<String, Long> group=null;

		
		List<StkRepo3> stkRepo3SaleList=stkRepo3Dao.getStockiestRepo3(request.getMyear(),request.getDivCode(),request.getDepoCode()
				,request.getSmon(),request.getEmon(),request.getRepType(),request.getLoginId());
		System.out.println("depo code "+request.getDepoCode());
		
		StkRepo3Response response=null;
		
		List<StkRepo3Response> saleList = new ArrayList();

		int size = stkRepo3SaleList.size();
		long columnTotal=0;
		long groupColumnTotal=0;

		boolean first=true;
		int terr_code=0;
		String ter_name="";
		String partyCode="";
		String partyName="";
		for (int i=0;i<size;i++)
		{
			StkRepo3 data = stkRepo3SaleList.get(i);
			
			if(first)
			{
				response=new StkRepo3Response();
				partyCode=data.getSprt_cd();
				partyName=data.getMac_name();
				terr_code=data.getTerr_cd();
				ter_name=data.getTer_name();
				months=new LinkedHashMap();
				group=new LinkedHashMap();
				total=new LinkedHashMap();
				first=false;
				
				title = getTitle(request, data); 
			}

			if(!partyCode.equalsIgnoreCase(data.getSprt_cd()))
			{
				response.setName(partyName);
				z=k;
				for(int b=k;b<sz;b++)
				{
					MonthDto mn=monthData.get(b);
					months.put(mn.getMnth_abbr(), 0L);
					k++;
				}

				months.put("TOTAL", columnTotal);
				response.setMonths(months);
				
				saleList.add(response);
				partyCode=data.getSprt_cd();
				partyName=data.getMac_name();
				columnTotal=0;
				
				k=0;
				response=new StkRepo3Response();
				months=new LinkedHashMap();

			}

			
			if(terr_code!=data.getTerr_cd())
			{
				response.setName(ter_name);
				
				for(int b=z;b<sz;b++)
				{
					MonthDto mn=monthData.get(b);
					group.put(mn.getMnth_abbr(), 0L);
					z++;
				}

				group.put("TOTAL", groupColumnTotal);

				months.putAll(group);

				
				response.setMonths(months);
				response.setColor(1);
				saleList.add(response);

				
				terr_code=data.getTerr_cd();
				ter_name=data.getTer_name();
				partyCode=data.getSprt_cd();
				partyName=data.getMac_name();

				z=0;
				groupColumnTotal=0;
				response=new StkRepo3Response();
				months=new LinkedHashMap();
				group=new LinkedHashMap();
				
			}

			
			// before put please check depo code in branch list if not found put 0 value in map otherwise actual zero
			for(int b=k;b<sz;b++)
			{
				MonthDto mn=monthData.get(b);
				if(mn.getMnth_code()==data.getMnth_code())
				{
					months.put(data.getMnth_abbr(), data.getSales_val());
					columnTotal+=data.getSales_val();
					groupColumnTotal+=data.getSales_val();
					if(group.containsKey(data.getMnth_abbr()))
					{
						long gval = group.get(data.getMnth_abbr())+data.getSales_val();
						group.put(data.getMnth_abbr(), gval);
					}
					else
					{
						group.put(data.getMnth_abbr(), data.getSales_val());
					}
					
					if(total.containsKey(data.getMnth_abbr()))
					{
						long ggval = total.get(data.getMnth_abbr())+data.getSales_val();
						total.put(data.getMnth_abbr(), ggval);
					}
					else
					{
						total.put(data.getMnth_abbr(), data.getSales_val());
					}

					k++;
					break;
				}
				else
				{
					months.put(mn.getMnth_abbr(), 0L);
					if(group.containsKey(mn.getMnth_abbr()))
					{
						// do nothing
					}
					else
					{
						group.put(mn.getMnth_abbr(), 0L);

					}

					if(total.containsKey(mn.getMnth_abbr()))
					{
						// do nothing
					}
					else
					{
						total.put(mn.getMnth_abbr(), 0L);

					}

					
					k++;
				}
			}
			
		}			
			response=new StkRepo3Response();
			response.setName("");
			z=k;
			for(int b=k;b<sz;b++)
			{
				MonthDto mn=monthData.get(b);
				months.put(mn.getMnth_abbr(), 0L);
				k++;
			}
			months.put("TOTAL", columnTotal);

			response.setMonths(months);
			saleList.add(response);

			
			months=new LinkedHashMap();
			response=new StkRepo3Response();
			response.setName("");
			for(int b=z;b<sz;b++)
			{
				MonthDto mn=monthData.get(b);
				group.put(mn.getMnth_abbr(), 0L);
				total.put(mn.getMnth_abbr(), 0L);
				z++;
			}

			group.put("TOTAL", groupColumnTotal);

			months.putAll(group);
			response.setMonths(months);
			response.setColor(1);
			saleList.add(response);
			
			
			grandColumnTotal = total.values().stream().mapToLong(d -> d).sum();
			
			months=new LinkedHashMap();
			total.put("GRAND TOTAL", grandColumnTotal);


			months.putAll(total);
			response=new StkRepo3Response();
			response.setName("Total");
			response.setMonths(months);
			response.setColor(2);
			saleList.add(response);		
		return new ApiResponse<StkRepo3Response>(title.toString(),size,lupdate,saleList);

	}
	
	
	
}
