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
import com.aristowebapi.dao.StkRepo2Dao;
import com.aristowebapi.dto.MonthDto;
import com.aristowebapi.dto.StkRepo2;
import com.aristowebapi.request.StkRepo2Request;
import com.aristowebapi.response.ApiResponse;
import com.aristowebapi.response.BranchMisRepo8Response;
import com.aristowebapi.response.StkRepo2Response;
import com.aristowebapi.service.StkRepo2Service;
import com.aristowebapi.utility.AppCalculationUtils;

@Service
public class StkRepo2ServiceImpl implements StkRepo2Service{

	
	Logger logger = LoggerFactory.getLogger(StkRepo2ServiceImpl.class);
	@Autowired
	private StkRepo2Dao stkRepo2Dao;
	
	@Autowired
	private AristoWebMessageConstant  aristoWebMessageConstant;
	
	private String lupdate=""; 
	Map<String, Object> grandTotalMap=null;
	Map<String, Integer> grandTotalIntMap=null;

	Map<String, Double> grandTotalDoubleMap=null;
	double grandColumnTotalDouble=0.00;

	
	private String getTitle(StkRepo2Request request,StkRepo2 data)
	{

		String crtype="";
		
		StringBuilder title=new StringBuilder();
		title.append(aristoWebMessageConstant.divisionMap.get(String.valueOf(data.getDiv_code())));
		title.append(" STOCKIEST : ");
		title.append(data.getStk_name());
		title.append(" ");
		switch(request.getRepType())
		{
		    case 1:	title.append("GROSS SALE");
		            break;
		    case 2: title.append("CREDIT - ");
		    		break;
		    case 3: title.append("NET SALE ");
		    		break;

		}
//		String crtype="";
		if (request.getCreditNoteType()==1)
				crtype="Saleable";
		else if(request.getCreditNoteType()==2)
			crtype="Spoiled";
		else if(request.getCreditNoteType()==3)
			crtype="Breakage";
		else if(request.getCreditNoteType()==4)
			crtype="Expired";
		else if(request.getCreditNoteType()==6)
			crtype="Price Diff";
		else if(request.getCreditNoteType()==7)
			crtype="Short Received";
		else if(request.getCreditNoteType()==9)
			crtype="Loss in Transit";

		title.append(request.getCreditNoteType()>0?crtype:"");
		title.append(request.getRepTypePgwise()==1?" - PRODUCT WISE ":" GROUP WISE ");
		title.append(request.getUv()==1?" UNIT TREND ":request.getUv()==2?" VALUE TREND ":" UNIT/VALUE TREND FROM ");
		title.append(data.getSmname());
		title.append(" To ");
		title.append(data.getEmname());

		return title.toString();

	}

	
	@Override
	public ApiResponse<StkRepo2Response> getStkRepo2(StkRepo2Request request) {
		logger.info(AristoWebLogMsgConstant.STK_REPO2_SERVICE,"getStkRepo2Unit");


		List<MonthDto> monthData = stkRepo2Dao.getAllMonth(request.getMyear());
		int sz=monthData.size();
		int k=0;
		int z=0;
		sz=request.getEmon();

		
		String title=null;
		if(request.getRepTypePgwise()==2 && request.getRepType()==1)
			request.setRepType(12);
		if(request.getRepTypePgwise()==2 && request.getRepType()==3)
			request.setRepType(32);
		
		if(request.getRepTypePgwise()==2)
			request.setUv(2);
		
		System.out.println(request.getMyear()+" "+request.getDivCode()+" "+request.getDepoCode()+" "+request.getSmon()+" "+request.getEmon()+" "+request.getRepType()+" "+request.getStkCode()+" "+request.getLoginId());

		List<StkRepo2> stkRepo2SaleList=null;
		if(request.getRepType()==2 && request.getRepTypePgwise()==2 ) // credit note
			stkRepo2SaleList=stkRepo2Dao.getStockiestRepo2CreditGroup(request.getMyear(),request.getDivCode(),request.getDepoCode()
					,request.getSmon(),request.getEmon(),request.getCreditNoteType(),request.getStkCode(),request.getLoginId());
		else if(request.getRepType()==2 && request.getRepTypePgwise()==1 ) // credit note
			stkRepo2SaleList=stkRepo2Dao.getStockiestRepo2Credit(request.getMyear(),request.getDivCode(),request.getDepoCode()
					,request.getSmon(),request.getEmon(),request.getCreditNoteType(),request.getStkCode(),request.getLoginId());
		else
			stkRepo2SaleList=stkRepo2Dao.getStockiestRepo2(request.getMyear(),request.getDivCode(),request.getDepoCode()
				,request.getSmon(),request.getEmon(),request.getRepType(),request.getStkCode(),request.getLoginId());
	
		
		
		StkRepo2Response response=null;

		Map<String, Long> months=null;
		Map<String, Long> total=null;
		Map<String, Long> group=null;
		
		List<StkRepo2Response> saleList = new ArrayList();

		int size=0;
		if(stkRepo2SaleList!=null)
		   size = stkRepo2SaleList.size();
		System.out.println("size is "+size);
		String mname="";
		if(size==0)
			mname=stkRepo2Dao.getPartyName(request.getDivCode(), request.getDepoCode(), request.getStkCode());
		long grandColumnTotal=0;
		long grandColumnTotalVal=0;
		long vgrandColumnTotal=0;

		long columnTotal=0;
		long columnTotalVal=0;
		long groupColumnTotal=0;
		boolean first=true;
		int depo_code=0;
		int mgrp=0;
		String name="";
		String gname="";
		int fs=0;
		int gfs=0;
		int pcode=0;
		for (int i=0;i<size;i++)
		{
			StkRepo2 data = stkRepo2SaleList.get(i);
			
			if(first)
			{
				response=new StkRepo2Response();
				pcode=data.getMcode();
				name=data.getMname();
				mgrp=data.getMgrp();
				gname=data.getGp_name();
				months=new LinkedHashMap();
				group=new LinkedHashMap();
				total=new LinkedHashMap();
				first=false;
				
				title = getTitle(request, data); 
			}

			if(pcode!=data.getMcode())
			{
				response.setName(name);
				z=k;
				for(int b=k;b<sz;b++)
				{
					MonthDto mn=monthData.get(b);
					if(request.getUv()==1)
					months.put((mn.getMnth_abbr()+" UNITS"), 0L);
					else if(request.getUv()==2)
					months.put((mn.getMnth_abbr()+" VALUE"), 0L);
					else
					{
						months.put((mn.getMnth_abbr()+" UNITS"), 0L);
						months.put((mn.getMnth_abbr()+" VALUE"), 0L);
					}
					k++;
				}

				
				if(request.getUv()==1)
					months.put("TOTAL UNITS", columnTotal);
				else if(request.getUv()==2)
					months.put("TOTAL VALUE", columnTotalVal);
				else
				{
					months.put("TOTAL UNITS", columnTotal);
					months.put("TOTAL VALUE", columnTotalVal);
				}
				response.setMonths(months);
				if (pcode==9999)
						response.setColor(1);
				else
						response.setColor(0);
				saleList.add(response);
				pcode=data.getMcode();
				name=data.getMname();
				columnTotal=0;
				columnTotalVal=0;
				k=0;
				response=new StkRepo2Response();
				months=new LinkedHashMap();

			}

			
/*			if(mgrp!=data.getMgrp())
			{
				response.setName(gname);
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

				
				mgrp=data.getMgrp();
				gname=data.getGp_name();
				
				z=0;
				groupColumnTotal=0;
				response=new StkRepo2Response();
				months=new LinkedHashMap();
				group=new LinkedHashMap();
				
			}
*/
			
			// before put please check depo code in branch list if not found put 0 value in map otherwise actual zero
			for(int b=k;b<sz;b++)
			{
				MonthDto mn=monthData.get(b);
				if(mn.getMnth_code()==data.getMnth_code())
				{
					
					if(request.getUv()==1)
					{
						months.put((data.getMnth_abbr()+" UNITS"),data.getSales());
						columnTotal+=data.getSales();
						grandColumnTotal+=data.getSales();
					}
					else if(request.getUv()==2)
					{
						months.put((data.getMnth_abbr()+" VALUE"),data.getSales_val());
						columnTotalVal+=data.getSales_val();
						grandColumnTotalVal+=data.getSales_val();
						
					}
					else if(request.getUv()==3)
					{
						months.put((data.getMnth_abbr()+" UNITS"),data.getSales());
						months.put((data.getMnth_abbr()+" VALUE"),data.getSales_val());
						columnTotal+=data.getSales();
						columnTotalVal+=data.getSales_val();
						grandColumnTotal+=data.getSales();
						grandColumnTotalVal+=data.getSales_val();
						
					}

//					System.out.println("vgrandColumnTotal IS "+grandColumnTotalVal);
					
					if(group.containsKey(data.getMnth_abbr()))
					{
						long gval = group.get(data.getMnth_abbr())+data.getSales_val();
						group.put(data.getMnth_abbr(), gval);
					}
					else
					{
						group.put(data.getMnth_abbr(), data.getSales_val());
					}
					
					if(request.getUv()==1)
					{
						if(total.containsKey((data.getMnth_abbr()+" UNITS")))
						{
							long ggval = total.get(data.getMnth_abbr()+" UNITS")+data.getSales();
							total.put((data.getMnth_abbr()+" UNITS"), ggval);
						}
						else
						{
							total.put((data.getMnth_abbr()+" UNITS"), data.getSales());
						}
					}
					if(request.getUv()==2)
					{
						if(total.containsKey((data.getMnth_abbr()+" VALUE")))
						{
							long ggval = total.get((data.getMnth_abbr()+" VALUE"))+data.getSales_val();
							total.put((data.getMnth_abbr()+" VALUE"), ggval);
						}
						else
						{
							total.put((data.getMnth_abbr()+" VALUE"), data.getSales_val());
						}
						System.out.println("total ke liye yeha per aaya "+data.getSales_val()+" "+data.getMnth_abbr());

					}
					if(request.getUv()==3)
					{
						if(total.containsKey((data.getMnth_abbr()+" UNITS")))
						{
							long ggval = total.get(data.getMnth_abbr()+" UNITS")+data.getSales();
							total.put((data.getMnth_abbr()+" UNITS"), ggval);
						}
						else
						{
							total.put((data.getMnth_abbr()+" UNITS"), data.getSales());
						}

						if(total.containsKey((data.getMnth_abbr()+" VALUE")))
						{
							long ggval = total.get((data.getMnth_abbr()+" VALUE"))+data.getSales_val();
							total.put((data.getMnth_abbr()+" VALUE"), ggval);
						}
						else
						{
							total.put((data.getMnth_abbr()+" VALUE"), data.getSales_val());
						}
					}


					k++;
					break;
				}
				else
				{
					if(request.getUv()==1)
						months.put((mn.getMnth_abbr()+" UNITS"), 0L);
					else if(request.getUv()==2)
						months.put((mn.getMnth_abbr()+" VALUE"), 0L);
					else
					{
						months.put((mn.getMnth_abbr()+" UNITS"), 0L);
						months.put((mn.getMnth_abbr()+" VALUE"), 0L);
						
					}
					if(group.containsKey(mn.getMnth_abbr()))
					{
						// do nothing
					}
					else
					{
						group.put(mn.getMnth_abbr(), 0L);

					}

					if(total.containsKey(mn.getMnth_abbr()+" UNITS"))
					{
						// do nothing
					}
					else if(total.containsKey(mn.getMnth_abbr()+" VALUE"))
					{
						// do nothing
					}
					else
					{
						if(request.getUv()==1)
							total.put(mn.getMnth_abbr()+" UNITS", 0L);
						if(request.getUv()==2)
							total.put(mn.getMnth_abbr()+" VALUE", 0L);
						if(request.getUv()==3)
						{
							total.put(mn.getMnth_abbr()+" UNITS", 0L);
							total.put(mn.getMnth_abbr()+" VALUE", 0L);
						}

					}
					
					
					k++;
				}
			}
			
		}		
		
			if(!first)
			{
			response=new StkRepo2Response();
			response.setName(name);
			z=k;
			for(int b=k;b<sz;b++)
			{
				MonthDto mn=monthData.get(b);
				if(request.getUv()==1)
				months.put((mn.getMnth_abbr()+" UNITS"), 0L);
				else if(request.getUv()==2)
				months.put((mn.getMnth_abbr()+" VALUE"), 0L);
				else
				{
					months.put((mn.getMnth_abbr()+" UNITS"), 0L);
					months.put((mn.getMnth_abbr()+" VALUE"), 0L);
				}
				k++;
			}
			if(request.getUv()==1)
				months.put("TOTAL UNITS", columnTotal);
			else if(request.getUv()==2)
				months.put("TOTAL VALUE", columnTotalVal);
			else
			{
				months.put("TOTAL UNITS", columnTotal);
				months.put("TOTAL VALUE", columnTotalVal);
				
			}

			response.setMonths(months);
			if (pcode==9999)
				response.setColor(1);
		else
				response.setColor(0);

			saleList.add(response);

			
			

			response=new StkRepo2Response();
			months=new LinkedHashMap();
			
			if(request.getUv()==1)
				total.put("TOTAL UNITS", grandColumnTotal);
			else if(request.getUv()==2)
				total.put("TOTAL VALUE", grandColumnTotalVal);
			else
			{
				total.put("TOTAL UNITS", grandColumnTotal);
				total.put("TOTAL VALUE", grandColumnTotalVal);
				
			}


			response.setName("Total :");
			months.putAll(total);
			response.setMonths(months);
			response.setColor(2);
			saleList.add(response);


	



			
			
			
			return new ApiResponse<StkRepo2Response>(title.toString(),size,lupdate,saleList);
			}
			else 
				return new ApiResponse<StkRepo2Response>(mname.toString(),size,lupdate,saleList);

	}
	
	

	@Override
	public ApiResponse<StkRepo2Response> getStkRepo2UV(StkRepo2Request request) {
		logger.info(AristoWebLogMsgConstant.STK_REPO2_SERVICE,"getStkRepo2Unit");


		List<MonthDto> monthData = stkRepo2Dao.getAllMonth(request.getMyear());
		int sz=monthData.size();
		int k=0;
		int z=0;

		sz=request.getEmon();
		
		String title=null;
		
		List<StkRepo2> stkRepo2SaleList=null;
		if(request.getRepType()==2) // credit note
			stkRepo2SaleList=stkRepo2Dao.getStockiestRepo2Credit(request.getMyear(),request.getDivCode(),request.getDepoCode()
					,request.getSmon(),request.getEmon(),request.getCreditNoteType(),request.getStkCode(),request.getLoginId());
		else
			stkRepo2SaleList=stkRepo2Dao.getStockiestRepo2(request.getMyear(),request.getDivCode(),request.getDepoCode()
				,request.getSmon(),request.getEmon(),request.getRepType(),request.getStkCode(),request.getLoginId());

		
//		List<StkRepo2> stkRepo2SaleList=stkRepo2Dao.getStockiestRepo2(request.getMyear(),request.getDivCode(),request.getDepoCode()
//				,request.getSmon(),request.getEmon(),request.getRepType(),request.getStkCode(),request.getLoginId());
		
		StkRepo2Response response=null;

		Map<String, Long> months=null;
		Map<String, Long> total=null;
		Map<String, Long> vtotal=null;
		Map<String, Long> group=null;
		
		List<StkRepo2Response> saleList = new ArrayList();

		int size = stkRepo2SaleList.size();
		
		long grandColumnTotal=0;
		long grandColumnTotalVal=0;
		long vgrandColumnTotal=0;

		long columnTotal=0;
		long vcolumnTotal=0;

		long groupColumnTotal=0;
		boolean first=true;
		int depo_code=0;
		int mgrp=0;
		String name="";
		String gname="";
		int fs=0;
		int gfs=0;
		int pcode=0;
		for (int i=0;i<size;i++)
		{
			StkRepo2 data = stkRepo2SaleList.get(i);
			
			if(first)
			{
				response=new StkRepo2Response();
				pcode=data.getMcode();
				name=data.getMname();
				mgrp=data.getMgrp();
				gname=data.getGp_name();
				months=new LinkedHashMap();
				group=new LinkedHashMap();
				total=new LinkedHashMap();
				vtotal=new LinkedHashMap();
				first=false;
				
				title = getTitle(request, data); 
			}

			if(pcode!=data.getMcode())
			{
				response.setName(name);
				z=k;
				for(int b=k;b<sz;b++)
				{
					MonthDto mn=monthData.get(b);
					months.put(mn.getMnth_abbr()+" UNITS", 0L);
					months.put(mn.getMnth_abbr()+" VALUES", 0L);
					k++;
				}

				months.put("TOTAL UNITS", columnTotal);
				months.put("TOTAL VALUE", vcolumnTotal);
				response.setMonths(months);
				if (pcode==9999)
					response.setColor(1);
			else
					response.setColor(0);

				saleList.add(response);
				pcode=data.getMcode();
				name=data.getMname();
				columnTotal=0;
				vcolumnTotal=0;
				
				k=0;
				response=new StkRepo2Response();
				months=new LinkedHashMap();

			}

			
		
			// before put please check depo code in branch list if not found put 0 value in map otherwise actual zero
			for(int b=k;b<sz;b++)
			{
				
				MonthDto mn=monthData.get(b);
				if(mn.getMnth_code()==data.getMnth_code())
				{
					months.put(data.getMnth_abbr()+" UNITS",data.getSales());
					months.put(data.getMnth_abbr()+" VALUE",data.getSales_val());
					columnTotal+=data.getSales();
					grandColumnTotal+=data.getSales();
					vgrandColumnTotal+=data.getSales_val();
					vcolumnTotal+=data.getSales_val();
					
					if(group.containsKey(data.getMnth_abbr()))
					{
						long gval = group.get(data.getMnth_abbr())+data.getSales_val();
						group.put(data.getMnth_abbr(), gval);
					}
					else
					{
						group.put(data.getMnth_abbr(), data.getSales_val());
					}
					
					if(total.containsKey(data.getMnth_abbr()+" UNITS"))
					{
						long ggval = total.get(data.getMnth_abbr()+" UNITS")+data.getSales();
//						total.put(data.getMnth_abbr()+" UNITS", ggval);
						total.put(data.getMnth_abbr()+" UNITS", 0L);

					}
					else
					{
//						total.put(data.getMnth_abbr()+" UNITS", data.getSales());
						total.put(data.getMnth_abbr()+" UNITS", 0L);

					}

					if(total.containsKey(data.getMnth_abbr()+" VALUES"))
					{
						long ggval = total.get(data.getMnth_abbr()+" VALUES")+data.getSales_val();
						total.put(data.getMnth_abbr()+" VALUES", ggval);

					}
					else
					{
						total.put(data.getMnth_abbr()+" VALUES", data.getSales_val());

					}

					k++;
					break;
				}
				else
				{
					months.put(mn.getMnth_abbr()+" UNITS", 0L);
					months.put(mn.getMnth_abbr()+" VALUE", 0L);
					if(group.containsKey(mn.getMnth_abbr()))
					{
						// do nothing
					}
					else
					{
						group.put(mn.getMnth_abbr(), 0L);

					}

					if(total.containsKey(mn.getMnth_abbr()+" UNITS"))
					{
						// do nothing
					}
					else
					{
						total.put(mn.getMnth_abbr()+" UNITS", 0L);

					}

					if(total.containsKey(mn.getMnth_abbr()+" VALUES"))
					{
						// do nothing
					}
					else
					{
						total.put(mn.getMnth_abbr()+" VALUES", 0L);

					}
					
					k++;
				}
			}
			
		}			
		response.setName(name);
		z=k;
		for(int b=k;b<sz;b++)
		{
			MonthDto mn=monthData.get(b);
			months.put(mn.getMnth_abbr()+" UNITS", 0L);
			months.put(mn.getMnth_abbr()+" VALUES", 0L);
			k++;
		}

		months.put("TOTAL UNITS", columnTotal);
		months.put("TOTAL VALUE", vcolumnTotal);
		response.setMonths(months);
		if (pcode==9999)
			response.setColor(1);
	else
			response.setColor(0);

		saleList.add(response);
// grand total 
			
			response=new StkRepo2Response();
			response.setName("Total Value : ");
			z=k;
			for(int b=k;b<sz;b++)
			{
				MonthDto mn=monthData.get(b);
				total.put(mn.getMnth_abbr()+" UNITS", 0L);
				total.put(mn.getMnth_abbr()+" VALUES", 0L);
				k++;
			}
			total.put("TOTAL UNITS", 0L);
			total.put("TOTAL VALUE", vgrandColumnTotal);
			
			response.setMonths(total);
			response.setColor(2);
			saleList.add(response);
			
		
			return new ApiResponse<StkRepo2Response>(title.toString(),size,lupdate,saleList);
	}

}
