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
import com.aristowebapi.dao.StkRepo3Dao;
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
	double groupColumnTotal=0;
	Map<String, Double> grandTotalMap=null;
	double grandColumnTotal=0;
	
	String lupdate;
	
	@Autowired
	private StkRepo3Dao stkRepo3Dao;
	
	
	
	private String getTitle(StkRepo3Request request,StkRepo3 data)
	{

		
		StringBuilder title=new StringBuilder();
		title.append(" STOCKIEST/RUPEES/SALES WISE : ");
		switch(request.getRepType())
		{
		    case 1:	title.append("GROSS SALE ");
		            break;
		    case 2: title.append("CREDIT ");
		    		break;
		    case 3: title.append("NET SALE");
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
		
		
		List<StkRepo3> stkRepo3SaleList=stkRepo3Dao.getStockiestRepo2(request.getMyear(),request.getDivCode(),request.getDepoCode()
				,request.getSmon(),request.getEmon(),request.getRepType());
		
		StkRepo3Response response=null;
		
		List<StkRepo3Response> saleList = new ArrayList();

		int size = stkRepo3SaleList.size();
		boolean first=true;
		int terr_code=0;
		String ter_name="";
		int cont=0;
		
		for (int i=0;i<size;i++)
		{
			
			StkRepo3 data = stkRepo3SaleList.get(i);

			
			if(first)
			{
				first=false;
				title = getTitle(request, data);
				terr_code=data.getMterr_code();
				ter_name=data.getTer_name();
				groupMap=new LinkedHashMap();
				grandTotalMap=new LinkedHashMap();
				lupdate=data.getLastupdate();
				
				System.out.println("lupdate "+lupdate);
			}



			
			if(terr_code!=data.getMterr_code())
			{
				
				groupColumnTotal = groupMap.values().stream().mapToDouble(d -> d).sum();
				groupMap.put("Total", Math.round(groupColumnTotal*100)/100.00);
				if(groupMap.get("Total")>0)
				{
					response=new StkRepo3Response();
					response.setName(ter_name);
					response.setMonths(groupMap);
					saleList.add(response);
				}
				terr_code=data.getMterr_code();
				ter_name=data.getTer_name();
				groupMap=new LinkedHashMap();
				groupColumnTotal=0;

			}
			

			Map<String,Double> dataMap = getMonthlyData(request.getSmon(), request.getEmon(),data);

			
			if(dataMap.get("Total")>0)
			{
				response=new StkRepo3Response();
				response.setName(data.getMac_name());
				response.setMonths(dataMap);
				saleList.add(response);
				cont++;
			}

		}
		
		if(!first)
		{
			groupColumnTotal = groupMap.values().stream().mapToDouble(d -> d).sum();
			groupMap.put("Total", Math.round(groupColumnTotal*100)/100.00);
			if(groupMap.get("Total")>0)
			{
				response=new StkRepo3Response();
				response.setName(ter_name);
				response.setMonths(groupMap);
				saleList.add(response);
			}
			grandColumnTotal = grandTotalMap.values().stream().mapToDouble(d -> d).sum();
			grandTotalMap.put("Grand Total", Math.round(grandColumnTotal*100)/100.00);
			if(grandTotalMap.get("Grand Total")>0)
			{
				response=new StkRepo3Response();
				response.setName(ter_name);
				response.setMonths(grandTotalMap);
				saleList.add(response);
			}

		}
		
		System.out.println("lupdate ki value last mai "+lupdate);
		return new ApiResponse<StkRepo3Response>(title.toString(),cont,lupdate,saleList);

	}
	
	
	
	private Map getMonthlyData(int smon, int emon,StkRepo3 data)
	{
		Map<String,Double> monthMap = new LinkedHashMap<String,Double>();
		double colTotal=0.00;
		for(int j=smon;j<=emon;j++)
		{
				double total=0.00;

				switch(j)
				{
					case 1 : monthMap.put(monthArray[j], data.getCvoct());
							 colTotal+=data.getCvoct();
					         total = groupMap.containsKey(monthArray[j])?groupMap.get(monthArray[j])+data.getCvoct():data.getCvoct();
					         groupMap.put(monthArray[j], Math.round(total*100)/100.00);
					         total = grandTotalMap.containsKey(monthArray[j])?grandTotalMap.get(monthArray[j])+data.getCvoct():data.getCvoct();
					         grandTotalMap.put(monthArray[j], Math.round(total*100)/100.00);
					break;
					
					case 2 : monthMap.put(monthArray[j], data.getCvnov());
							colTotal+=data.getCvnov();
							total = groupMap.containsKey(monthArray[j])?groupMap.get(monthArray[j])+data.getCvnov():data.getCvnov();
					         groupMap.put(monthArray[j], Math.round(total*100)/100.00);
					         total = grandTotalMap.containsKey(monthArray[j])?grandTotalMap.get(monthArray[j])+data.getCvnov():data.getCvnov();
					         grandTotalMap.put(monthArray[j], Math.round(total*100)/100.00);

					break;

					case 3 : monthMap.put(monthArray[j], data.getCvdec());
							 colTotal+=data.getCvdec();
							 total = groupMap.containsKey(monthArray[j])?groupMap.get(monthArray[j])+data.getCvdec():data.getCvdec();
					         groupMap.put(monthArray[j], Math.round(total*100)/100.00);
					         total = grandTotalMap.containsKey(monthArray[j])?grandTotalMap.get(monthArray[j])+data.getCvdec():data.getCvdec();
					         grandTotalMap.put(monthArray[j], Math.round(total*100)/100.00);


					break;

					case 4 : monthMap.put(monthArray[j], data.getCvjan());
					         colTotal+=data.getCvjan();
					         total = groupMap.containsKey(monthArray[j])?groupMap.get(monthArray[j])+data.getCvjan():data.getCvjan();
					         groupMap.put(monthArray[j], Math.round(total*100)/100.00);
					         total = grandTotalMap.containsKey(monthArray[j])?grandTotalMap.get(monthArray[j])+data.getCvjan():data.getCvjan();
					         grandTotalMap.put(monthArray[j], Math.round(total*100)/100.00);


					break;

					case 5 : monthMap.put(monthArray[j], data.getCvfeb());
			         colTotal+=data.getCvfeb();
			         total = groupMap.containsKey(monthArray[j])?groupMap.get(monthArray[j])+data.getCvfeb():data.getCvfeb();
			         groupMap.put(monthArray[j], Math.round(total*100)/100.00);
			         total = grandTotalMap.containsKey(monthArray[j])?grandTotalMap.get(monthArray[j])+data.getCvfeb():data.getCvfeb();
			         grandTotalMap.put(monthArray[j], Math.round(total*100)/100.00);

		
					break;

					case 6 : monthMap.put(monthArray[j], data.getCvmar());
			         colTotal+=data.getCvmar();
			         total = groupMap.containsKey(monthArray[j])?groupMap.get(monthArray[j])+data.getCvmar():data.getCvmar();
			         groupMap.put(monthArray[j], Math.round(total*100)/100.00);
			         total = grandTotalMap.containsKey(monthArray[j])?grandTotalMap.get(monthArray[j])+data.getCvmar():data.getCvmar();
			         grandTotalMap.put(monthArray[j], Math.round(total*100)/100.00);



					break;
					
					case 7 : monthMap.put(monthArray[j], data.getCvapr());
			         colTotal+=data.getCvapr();
			         total = groupMap.containsKey(monthArray[j])?groupMap.get(monthArray[j])+data.getCvapr():data.getCvapr();
			         groupMap.put(monthArray[j], Math.round(total*100)/100.00);
			         total = grandTotalMap.containsKey(monthArray[j])?grandTotalMap.get(monthArray[j])+data.getCvapr():data.getCvapr();
			         grandTotalMap.put(monthArray[j], Math.round(total*100)/100.00);


					break;

					case 8 : monthMap.put(monthArray[j], data.getCvmay());
			         colTotal+=data.getCvmay();
			         total = groupMap.containsKey(monthArray[j])?groupMap.get(monthArray[j])+data.getCvmay():data.getCvmay();
			         groupMap.put(monthArray[j], Math.round(total*100)/100.00);
			         total = grandTotalMap.containsKey(monthArray[j])?grandTotalMap.get(monthArray[j])+data.getCvmay():data.getCvmay();
			         grandTotalMap.put(monthArray[j], Math.round(total*100)/100.00);


					break;
					
					case 9 : monthMap.put(monthArray[j], data.getCvjun());
			         colTotal+=data.getCvjun();
			         total = groupMap.containsKey(monthArray[j])?groupMap.get(monthArray[j])+data.getCvjun():data.getCvjun();
			         groupMap.put(monthArray[j], Math.round(total*100)/100.00);
			         total = grandTotalMap.containsKey(monthArray[j])?grandTotalMap.get(monthArray[j])+data.getCvjun():data.getCvjun();
			         grandTotalMap.put(monthArray[j], Math.round(total*100)/100.00);


					break;
					
					case 10 : monthMap.put(monthArray[j], data.getCvjul());
			         colTotal+=data.getCvjul();
			         total = groupMap.containsKey(monthArray[j])?groupMap.get(monthArray[j])+data.getCvjul():data.getCvjul();
			         groupMap.put(monthArray[j], Math.round(total*100)/100.00);
			         total = grandTotalMap.containsKey(monthArray[j])?grandTotalMap.get(monthArray[j])+data.getCvjul():data.getCvjul();
			         grandTotalMap.put(monthArray[j], Math.round(total*100)/100.00);


					break;


					case 11 : monthMap.put(monthArray[j], data.getCvaug());
			         colTotal+=data.getCvaug();
			         total = groupMap.containsKey(monthArray[j])?groupMap.get(monthArray[j])+data.getCvaug():data.getCvaug();
			         groupMap.put(monthArray[j], Math.round(total*100)/100.00);
			         total = grandTotalMap.containsKey(monthArray[j])?grandTotalMap.get(monthArray[j])+data.getCvaug():data.getCvaug();
			         grandTotalMap.put(monthArray[j], Math.round(total*100)/100.00);

					break;

					case 12 : monthMap.put(monthArray[j], data.getCvsep());
			         colTotal+=data.getCvsep();
			         total = groupMap.containsKey(monthArray[j])?groupMap.get(monthArray[j])+data.getCvsep():data.getCvsep();
			         groupMap.put(monthArray[j], Math.round(total*100)/100.00);
			         total = grandTotalMap.containsKey(monthArray[j])?grandTotalMap.get(monthArray[j])+data.getCvsep():data.getCvsep();
			         grandTotalMap.put(monthArray[j], Math.round(total*100)/100.00);

					break;

					
				}
		}
      				 monthMap.put("Total", Math.round(colTotal*100)/100.00);

                   return monthMap;
	}
}
