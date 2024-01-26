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
import com.aristowebapi.dao.StkRepo2Dao;
import com.aristowebapi.dto.StkRepo2;
import com.aristowebapi.request.StkRepo2Request;
import com.aristowebapi.response.ApiResponse;
import com.aristowebapi.response.StkRepo2UnitResponse;
import com.aristowebapi.response.StkRepo2UVResponse;
import com.aristowebapi.response.StkRepo2ValueResponse;
import com.aristowebapi.service.StkRepo2Service;

@Service
public class StkRepo2ServiceImpl implements StkRepo2Service{

	
	Logger logger = LoggerFactory.getLogger(StkRepo2ServiceImpl.class);
	private final String monthArray[] = new String[] {"","OCT","NOV","DEC","JAN","FEB","MAR","APR","MAY","JUN","JUL","AUG","SEP"};
	@Autowired
	private StkRepo2Dao stkRepo2Dao;
	
	public String lupdate="";
	Map<String, Object> grandTotalMap=null;
	Map<String, Integer> grandTotalIntMap=null;
	int grandColumnTotal=0;

	Map<String, Double> grandTotalDoubleMap=null;
	double grandColumnTotalDouble=0.00;

	
	private String getTitle(StkRepo2Request request,StkRepo2 data)
	{

		
		StringBuilder title=new StringBuilder();
		title.append(" STOCKIEST : ");
		title.append(data.getStkname());
		title.append(" ");
		switch(request.getRepType())
		{
		    case 1:	title.append("GROSS SALE");
		            break;
		    case 2: title.append("CREDIT");
		    		break;
		    case 3: title.append("NET SALE");
		    		break;

		}
		title.append(" - PRODUCT WISE ");
		title.append(request.getUv()==1?" UNIT TREND ":" VALUE TREND FROM ");
		title.append(data.getSmname());
		title.append(" To ");
		title.append(data.getEmname());

		return title.toString();

	}

	
	@Override
	public ApiResponse<StkRepo2UnitResponse> getStkRepo2Unit(StkRepo2Request request) {
		logger.info(AristoWebLogMsgConstant.STK_REPO2_SERVICE,"getStkRepo2Unit");


		

		
		String title=null;
		
		
		List<StkRepo2> stkRepo2SaleList=stkRepo2Dao.getStockiestRepo2(request.getMyear(),request.getDivCode(),request.getDepoCode()
				,request.getSmon(),request.getEmon(),request.getRepType(),request.getStkCode(),request.getUv());
		
		StkRepo2UnitResponse response=null;
		
		List<StkRepo2UnitResponse> saleList = new ArrayList();

		int size = stkRepo2SaleList.size();
		int columnTotal=0;
		boolean first=true;
		int depo_code=0;
		String name="";
		int fs=0;
		int gfs=0;
		for (int i=0;i<size;i++)
		{
			
			StkRepo2 data = stkRepo2SaleList.get(i);

			if(first)
			{
				first=false;
				title = getTitle(request, data); 
				lupdate=data.getLastupdate();
				grandTotalMap=new LinkedHashMap();
			}

			
			Map<String,Object> dataMap = getMonthlyData(request.getSmon(), request.getEmon(),data);
			columnTotal = dataMap.values().stream().mapToInt(d -> (Integer) d).sum();

			if(columnTotal>0)
			{
				response=new StkRepo2UnitResponse();
				response.setName(data.getName());
				response.setMonths(dataMap);
				saleList.add(response);
			}

		}
		
		grandColumnTotal = grandTotalMap.values().stream().mapToInt(d -> (Integer) d).sum();
		grandTotalMap.put("Grand Total", grandColumnTotal);
		if((Integer) grandTotalMap.get("Grand Total")>0)
		{
			response=new StkRepo2UnitResponse();
			response.setName("GRAND TOTAL");
			response.setMonths(grandTotalMap);
			saleList.add(response);
		}

		
		return new ApiResponse<StkRepo2UnitResponse>(title.toString(),size,lupdate,saleList);

	}
	
	
	private Map getMonthlyData(int smon, int emon,StkRepo2 data)
	{
		Map<String,Integer> monthMap = new LinkedHashMap<String,Integer>();
		int colTotal=0;
		for(int j=smon;j<=emon;j++)
		{
				int total=0;

				switch(j)
				{
					case 1 : monthMap.put(monthArray[j], data.getQtyoct());
							 colTotal+=data.getQtyoct();
					         total = grandTotalMap.containsKey(monthArray[j])?(Integer) grandTotalMap.get(monthArray[j])+data.getQtyoct():data.getQtyoct();
					         grandTotalMap.put(monthArray[j], total);
					break;
					
					case 2 : monthMap.put(monthArray[j], data.getQtynov());
							colTotal+=data.getQtynov();
					         total = grandTotalMap.containsKey(monthArray[j])?(Integer) grandTotalMap.get(monthArray[j])+data.getQtynov():data.getQtynov();
					         grandTotalMap.put(monthArray[j], total);

					break;

					case 3 : monthMap.put(monthArray[j], data.getQtydec());
							 colTotal+=data.getQtydec();
					         total = grandTotalMap.containsKey(monthArray[j])?(Integer) grandTotalMap.get(monthArray[j])+data.getQtydec():data.getQtydec();
					         grandTotalMap.put(monthArray[j], total);


					break;

					case 4 : monthMap.put(monthArray[j], data.getQtyjan());
					         colTotal+=data.getQtyjan();
					         total = grandTotalMap.containsKey(monthArray[j])?(Integer) grandTotalMap.get(monthArray[j])+data.getQtyjan():data.getQtyjan();
					         grandTotalMap.put(monthArray[j], total);


					break;

					case 5 : monthMap.put(monthArray[j], data.getQtyfeb());
			         colTotal+=data.getQtyfeb();
			         total = grandTotalMap.containsKey(monthArray[j])?(Integer) grandTotalMap.get(monthArray[j])+data.getQtyfeb():data.getQtyfeb();
			         grandTotalMap.put(monthArray[j], total);

		
					break;

					case 6 : monthMap.put(monthArray[j], data.getQtymar());
			         colTotal+=data.getQtymar();
			         total = grandTotalMap.containsKey(monthArray[j])?(Integer) grandTotalMap.get(monthArray[j])+data.getQtymar():data.getQtymar();
			         grandTotalMap.put(monthArray[j], total);



					break;
					
					case 7 : monthMap.put(monthArray[j], data.getQtyapr());
			         colTotal+=data.getQtyapr();
			         total = grandTotalMap.containsKey(monthArray[j])?(Integer) grandTotalMap.get(monthArray[j])+data.getQtyapr():data.getQtyapr();
			         grandTotalMap.put(monthArray[j], total);


					break;

					case 8 : monthMap.put(monthArray[j], data.getQtymay());
			         colTotal+=data.getQtymay();
			         total = grandTotalMap.containsKey(monthArray[j])?(Integer) grandTotalMap.get(monthArray[j])+data.getQtymay():data.getQtymay();
			         grandTotalMap.put(monthArray[j], total);


					break;
					
					case 9 : monthMap.put(monthArray[j], data.getQtyjun());
			         colTotal+=data.getQtyjun();
			         total = grandTotalMap.containsKey(monthArray[j])?(Integer) grandTotalMap.get(monthArray[j])+data.getQtyjun():data.getQtyjun();
			         grandTotalMap.put(monthArray[j], total);


					break;
					
					case 10 : monthMap.put(monthArray[j], data.getQtyjul());
			         colTotal+=data.getQtyjul();
			         total = grandTotalMap.containsKey(monthArray[j])?(Integer) grandTotalMap.get(monthArray[j])+data.getQtyjul():data.getQtyjul();
			         grandTotalMap.put(monthArray[j], total);


					break;


					case 11 : monthMap.put(monthArray[j], data.getQtyaug());
			         colTotal+=data.getQtyaug();
			         total = grandTotalMap.containsKey(monthArray[j])?(Integer) grandTotalMap.get(monthArray[j])+data.getQtyaug():data.getQtyaug();
			         grandTotalMap.put(monthArray[j], total);

					break;

					case 12 : monthMap.put(monthArray[j], data.getQtysep());
			         colTotal+=data.getQtysep();
			         total = grandTotalMap.containsKey(monthArray[j])?(Integer) grandTotalMap.get(monthArray[j])+data.getQtysep():data.getQtysep();
			         grandTotalMap.put(monthArray[j], total);

					break;

					
				}
		}
      				 monthMap.put("Total", colTotal);

                   return monthMap;
	}
	@Override
	public ApiResponse<StkRepo2ValueResponse> getStkRepo2Value(StkRepo2Request request) {
		logger.info(AristoWebLogMsgConstant.STK_REPO2_SERVICE,"getStkRepo2Value");


		

		
		String title=null;
		
		
		List<StkRepo2> stkRepo2SaleList=stkRepo2Dao.getStockiestRepo2(request.getMyear(),request.getDivCode(),request.getDepoCode()
				,request.getSmon(),request.getEmon(),request.getRepType(),request.getStkCode(),request.getUv());
		
		StkRepo2ValueResponse response=null;
		
		List<StkRepo2ValueResponse> saleList = new ArrayList();

		int size = stkRepo2SaleList.size();
		double columnTotal=0;
		boolean first=true;
		int depo_code=0;
		String name="";
		int fs=0;
		int gfs=0;
		for (int i=0;i<size;i++)
		{
			
			StkRepo2 data = stkRepo2SaleList.get(i);

			if(first)
			{
				first=false;
				title = getTitle(request, data); 
				lupdate=data.getLastupdate();
				grandTotalDoubleMap=new LinkedHashMap();
			}

			
			Map<String,Double> dataMap = getMonthlyDataValue(request.getSmon(), request.getEmon(),data);
			columnTotal = dataMap.values().stream().mapToDouble(d -> d).sum();

			if(columnTotal>0)
			{
				response=new StkRepo2ValueResponse();
				response.setName(data.getName());
				response.setMonths(dataMap);
				saleList.add(response);
			}

		}
		
		grandColumnTotalDouble = grandTotalDoubleMap.values().stream().mapToDouble(d -> d).sum();
		grandTotalDoubleMap.put("Grand Total", Math.round(grandColumnTotalDouble*100)/100.00);
		if(grandTotalDoubleMap.get("Grand Total")>0)
		{
			response=new StkRepo2ValueResponse();
			response.setName("GRAND TOTAL");
			response.setMonths(grandTotalDoubleMap);
			saleList.add(response);
		}

		
		return new ApiResponse<StkRepo2ValueResponse>(title.toString(),size,lupdate,saleList);

	}
	
	
	private Map getMonthlyDataValue(int smon, int emon,StkRepo2 data)
	{
		Map<String,Double> monthMap = new LinkedHashMap<String,Double>();
		double  colTotal=0.00;
		for(int j=smon;j<=emon;j++)
		{
				double total=0.00;

				switch(j)
				{
					case 1 : monthMap.put(monthArray[j], data.getValoct());
							 colTotal+=data.getValoct();
					         total = grandTotalDoubleMap.containsKey(monthArray[j])?grandTotalDoubleMap.get(monthArray[j])+data.getValoct():data.getValoct();
					         grandTotalDoubleMap.put(monthArray[j], Math.round(total*100)/100.00);
					break;
					
					case 2 : monthMap.put(monthArray[j], data.getValnov());
							colTotal+=data.getValnov();
					         total = grandTotalDoubleMap.containsKey(monthArray[j])?grandTotalDoubleMap.get(monthArray[j])+data.getValnov():data.getValnov();
					         grandTotalDoubleMap.put(monthArray[j], Math.round(total*100)/100.00);

					break;

					case 3 : monthMap.put(monthArray[j], data.getValdec());
							 colTotal+=data.getValdec();
					         total = grandTotalDoubleMap.containsKey(monthArray[j])?grandTotalDoubleMap.get(monthArray[j])+data.getValdec():data.getValdec();
					         grandTotalDoubleMap.put(monthArray[j], Math.round(total*100)/100.00);


					break;

					case 4 : monthMap.put(monthArray[j], data.getValjan());
					         colTotal+=data.getValjan();
					         total = grandTotalDoubleMap.containsKey(monthArray[j])?grandTotalDoubleMap.get(monthArray[j])+data.getValjan():data.getValjan();
					         grandTotalDoubleMap.put(monthArray[j], Math.round(total*100)/100.00);


					break;

					case 5 : monthMap.put(monthArray[j], data.getValfeb());
			         colTotal+=data.getValfeb();
			         total = grandTotalDoubleMap.containsKey(monthArray[j])?grandTotalDoubleMap.get(monthArray[j])+data.getValfeb():data.getValfeb();
			         grandTotalDoubleMap.put(monthArray[j], Math.round(total*100)/100.00);

		
					break;

					case 6 : monthMap.put(monthArray[j], data.getValmar());
			         colTotal+=data.getValmar();
			         total = grandTotalDoubleMap.containsKey(monthArray[j])?grandTotalDoubleMap.get(monthArray[j])+data.getValmar():data.getValmar();
			         grandTotalDoubleMap.put(monthArray[j], Math.round(total*100)/100.00);



					break;
					
					case 7 : monthMap.put(monthArray[j], data.getValapr());
			         colTotal+=data.getValapr();
			         total = grandTotalDoubleMap.containsKey(monthArray[j])?grandTotalDoubleMap.get(monthArray[j])+data.getValapr():data.getValapr();
			         grandTotalDoubleMap.put(monthArray[j], Math.round(total*100)/100.00);


					break;

					case 8 : monthMap.put(monthArray[j], data.getValmay());
			         colTotal+=data.getValmay();
			         total = grandTotalDoubleMap.containsKey(monthArray[j])?grandTotalDoubleMap.get(monthArray[j])+data.getValmay():data.getValmay();
			         grandTotalDoubleMap.put(monthArray[j], Math.round(total*100)/100.00);


					break;
					
					case 9 : monthMap.put(monthArray[j], data.getValjun());
			         colTotal+=data.getValjun();
			         total = grandTotalDoubleMap.containsKey(monthArray[j])?grandTotalDoubleMap.get(monthArray[j])+data.getValjun():data.getValjun();
			         grandTotalDoubleMap.put(monthArray[j], Math.round(total*100)/100.00);


					break;
					
					case 10 : monthMap.put(monthArray[j], data.getValjul());
			         colTotal+=data.getValjul();
			         total = grandTotalDoubleMap.containsKey(monthArray[j])?grandTotalDoubleMap.get(monthArray[j])+data.getValjul():data.getValjul();
			         grandTotalDoubleMap.put(monthArray[j],Math.round(total*100)/100.00);


					break;


					case 11 : monthMap.put(monthArray[j], data.getValaug());
			         colTotal+=data.getValaug();
			         total = grandTotalDoubleMap.containsKey(monthArray[j])?grandTotalDoubleMap.get(monthArray[j])+data.getValaug():data.getValaug();
			         grandTotalDoubleMap.put(monthArray[j], Math.round(total*100)/100.00);

					break;

					case 12 : monthMap.put(monthArray[j], data.getValsep());
			         colTotal+=data.getValsep();
			         total = grandTotalDoubleMap.containsKey(monthArray[j])?grandTotalDoubleMap.get(monthArray[j])+data.getValsep():data.getValsep();
			         grandTotalDoubleMap.put(monthArray[j], Math.round(total*100)/100.00);

					break;

					
				}
		}
      				 monthMap.put("Total", Math.round(colTotal*100)/100.00);

                   return monthMap;
	}


	@Override
	public ApiResponse<StkRepo2UVResponse> getStkRepo2UV(StkRepo2Request request) {
		// TODO Auto-generated method stub
		return null;
	}

	private Map getMonthlyDataUV(int smon, int emon,StkRepo2 data)
	{
		Map<String,Double> monthMap = new LinkedHashMap<String,Double>();
		Map<String,Integer> monthunitMap = new LinkedHashMap<String,Integer>();
		double  colTotal=0.00;
		int colUnitTotal=0;
		for(int j=smon;j<=emon;j++)
		{
				double total=0.00;
				int ctotal=0;
				switch(j)
				{
					case 1 : monthMap.put(monthArray[j], data.getValoct());
							 colTotal+=data.getValoct();
					         total = grandTotalDoubleMap.containsKey(monthArray[j])?grandTotalDoubleMap.get(monthArray[j])+data.getValoct():data.getValoct();
					         grandTotalDoubleMap.put(monthArray[j], Math.round(total*100)/100.00);

					         monthunitMap.put(monthArray[j], data.getQtyoct());
							 colUnitTotal+=data.getQtyoct();
					         ctotal = grandTotalIntMap.containsKey(monthArray[j])?grandTotalIntMap.get(monthArray[j])+data.getQtyoct():data.getQtyoct();
					         grandTotalIntMap.put(monthArray[j], ctotal);
			         break;
					
					case 2 : monthMap.put(monthArray[j], data.getValnov());
							 colTotal+=data.getValnov();
							 total = grandTotalDoubleMap.containsKey(monthArray[j])?grandTotalDoubleMap.get(monthArray[j])+data.getValnov():data.getValnov();
					         grandTotalDoubleMap.put(monthArray[j], Math.round(total*100)/100.00);
					         
					         monthunitMap.put(monthArray[j], data.getQtynov());
							 colUnitTotal+=data.getQtynov();
					         ctotal = grandTotalIntMap.containsKey(monthArray[j])?grandTotalIntMap.get(monthArray[j])+data.getQtynov():data.getQtynov();
					         grandTotalIntMap.put(monthArray[j], ctotal);


					break;

					case 3 : monthMap.put(monthArray[j], data.getValdec());
							 colTotal+=data.getValdec();
					         total = grandTotalDoubleMap.containsKey(monthArray[j])?grandTotalDoubleMap.get(monthArray[j])+data.getValdec():data.getValdec();
					         grandTotalDoubleMap.put(monthArray[j], Math.round(total*100)/100.00);

					         monthunitMap.put(monthArray[j], data.getQtydec());
							 colUnitTotal+=data.getQtydec();
					         ctotal = grandTotalIntMap.containsKey(monthArray[j])?grandTotalIntMap.get(monthArray[j])+data.getQtydec():data.getQtydec();
					         grandTotalIntMap.put(monthArray[j], ctotal);

					break;

					case 4 : monthMap.put(monthArray[j], data.getValjan());
					         colTotal+=data.getValjan();
					         total = grandTotalDoubleMap.containsKey(monthArray[j])?grandTotalDoubleMap.get(monthArray[j])+data.getValjan():data.getValjan();
					         grandTotalDoubleMap.put(monthArray[j], Math.round(total*100)/100.00);


					break;

					case 5 : monthMap.put(monthArray[j], data.getValfeb());
			         colTotal+=data.getValfeb();
			         total = grandTotalDoubleMap.containsKey(monthArray[j])?grandTotalDoubleMap.get(monthArray[j])+data.getValfeb():data.getValfeb();
			         grandTotalDoubleMap.put(monthArray[j], Math.round(total*100)/100.00);

		
					break;

					case 6 : monthMap.put(monthArray[j], data.getValmar());
			         colTotal+=data.getValmar();
			         total = grandTotalDoubleMap.containsKey(monthArray[j])?grandTotalDoubleMap.get(monthArray[j])+data.getValmar():data.getValmar();
			         grandTotalDoubleMap.put(monthArray[j], Math.round(total*100)/100.00);



					break;
					
					case 7 : monthMap.put(monthArray[j], data.getValapr());
			         colTotal+=data.getValapr();
			         total = grandTotalDoubleMap.containsKey(monthArray[j])?grandTotalDoubleMap.get(monthArray[j])+data.getValapr():data.getValapr();
			         grandTotalDoubleMap.put(monthArray[j], Math.round(total*100)/100.00);


					break;

					case 8 : monthMap.put(monthArray[j], data.getValmay());
			         colTotal+=data.getValmay();
			         total = grandTotalDoubleMap.containsKey(monthArray[j])?grandTotalDoubleMap.get(monthArray[j])+data.getValmay():data.getValmay();
			         grandTotalDoubleMap.put(monthArray[j], Math.round(total*100)/100.00);


					break;
					
					case 9 : monthMap.put(monthArray[j], data.getValjun());
			         colTotal+=data.getValjun();
			         total = grandTotalDoubleMap.containsKey(monthArray[j])?grandTotalDoubleMap.get(monthArray[j])+data.getValjun():data.getValjun();
			         grandTotalDoubleMap.put(monthArray[j], Math.round(total*100)/100.00);


					break;
					
					case 10 : monthMap.put(monthArray[j], data.getValjul());
			         colTotal+=data.getValjul();
			         total = grandTotalDoubleMap.containsKey(monthArray[j])?grandTotalDoubleMap.get(monthArray[j])+data.getValjul():data.getValjul();
			         grandTotalDoubleMap.put(monthArray[j],Math.round(total*100)/100.00);


					break;


					case 11 : monthMap.put(monthArray[j], data.getValaug());
			         colTotal+=data.getValaug();
			         total = grandTotalDoubleMap.containsKey(monthArray[j])?grandTotalDoubleMap.get(monthArray[j])+data.getValaug():data.getValaug();
			         grandTotalDoubleMap.put(monthArray[j], Math.round(total*100)/100.00);

					break;

					case 12 : monthMap.put(monthArray[j], data.getValsep());
			         colTotal+=data.getValsep();
			         total = grandTotalDoubleMap.containsKey(monthArray[j])?grandTotalDoubleMap.get(monthArray[j])+data.getValsep():data.getValsep();
			         grandTotalDoubleMap.put(monthArray[j], Math.round(total*100)/100.00);

					break;

					
				}
		}
      				 monthMap.put("Total", Math.round(colTotal*100)/100.00);

                   return monthMap;
	}
}
