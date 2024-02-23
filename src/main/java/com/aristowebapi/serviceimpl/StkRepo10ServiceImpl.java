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
import com.aristowebapi.dao.StkRepo10Dao;
import com.aristowebapi.dto.MonthDto;
import com.aristowebapi.dto.StkRepo10;
import com.aristowebapi.request.StkRepo10Request;
import com.aristowebapi.response.ApiResponse;
import com.aristowebapi.response.StkRepo10Response;
import com.aristowebapi.service.StkRepo10Service;
@Service
public class StkRepo10ServiceImpl implements StkRepo10Service {

	Logger logger = LoggerFactory.getLogger(StkRepo10ServiceImpl.class);
	@Autowired
	private StkRepo10Dao stkRepo10Dao;

	@Autowired
	private AristoWebMessageConstant  aristoWebMessageConstant;
	
	private String lupdate="";
	Map<String, Object> grandTotalMap=null;
	Map<String, Integer> grandTotalIntMap=null;
	long grandColumnTotal=0;
	long vgrandColumnTotal=0;

	Map<String, Double> grandTotalDoubleMap=null;
	double grandColumnTotalDouble=0.00;

	
	private String getTitle(StkRepo10Request request,StkRepo10 data)
	{

		
		StringBuilder title=new StringBuilder();
		title.append(aristoWebMessageConstant.divisionMap.get(String.valueOf(request.getDivCode())));
		title.append(" PRODUCT -> : ");
		title.append(data.getPname());
		title.append(" ");
		title.append(" - STOCKIEST WISE ");
		title.append(request.getUv()==1?" UNIT TREND ":" VALUE TREND ");
		switch(request.getRepType())
		{
		    case 1:	title.append("(GROSS) FROM ");
		            break;
		    case 2: title.append("(CREDIT) FROM ");
		    		break;
		    case 3: title.append("(NET) FROM ");
		    		break;

		}

		title.append(data.getSmname());
		title.append(" To ");
		title.append(data.getEmname());

		return title.toString();

	}

	@Override
	public ApiResponse<StkRepo10Response> getStkRepo10(StkRepo10Request request) {
		logger.info(AristoWebLogMsgConstant.STK_REPO10_SERVICE,"getStkRepo10");


		List<MonthDto> monthData = stkRepo10Dao.getAllMonth(request.getMyear());
		int sz=monthData.size();
		int k=0;
		int z=0;
		sz=request.getEmon();

		
		String title=null;
		
		
		List<StkRepo10> stkRepo10SaleList=stkRepo10Dao.getStockiestRepo10(request.getMyear(),request.getDivCode(),request.getDepoCode()
				,request.getSmon(),request.getEmon(),request.getRepType(),request.getPcode(),request.getLoginId());
	
		
		StkRepo10Response response=null;

		Map<String, Long> months=null;
		Map<String, Long> total=null;
		Map<String, Long> group=null;
		
		List<StkRepo10Response> saleList = new ArrayList();

		int size = stkRepo10SaleList.size();
		
		System.out.println("size is "+size);
		long columnTotal=0;
		long groupColumnTotal=0;
		boolean first=true;
		int depo_code=0;
		int ter_code=0;
		String name="";
		String ter_name="";
		int fs=0;
		int gfs=0;
		String pcode="";
		for (int i=0;i<size;i++)
		{
			StkRepo10 data = stkRepo10SaleList.get(i);
			
			if(first)
			{
				response=new StkRepo10Response();
				pcode=data.getSprt_cd();
				name=data.getMname();
				ter_code=data.getTerr_cd();
				ter_name=data.getTer_name();
				months=new LinkedHashMap();
				group=new LinkedHashMap();
				total=new LinkedHashMap();
				first=false;
				
				title = getTitle(request, data); 
			}

			if(!pcode.equalsIgnoreCase(data.getSprt_cd()))
			{
				response.setName(name);
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
				pcode=data.getSprt_cd();
				name=data.getMname();
				columnTotal=0;
				
				k=0;
				response=new StkRepo10Response();
				months=new LinkedHashMap();

			}

			
			if(ter_code!=data.getTerr_cd())
			{
				response.setName(ter_name);
				for(int b=z;b<sz;b++)
				{
					MonthDto mn=monthData.get(b);
					group.put(mn.getMnth_abbr(), 0L);
					z++;
				}

				groupColumnTotal = group.values().stream().mapToLong(d -> d).sum();
				group.put("TOTAL", groupColumnTotal);

				months.putAll(group);

				
				response.setMonths(months);
				response.setColor(1);
				saleList.add(response);

				
				ter_code=data.getTerr_cd();
				ter_name=data.getTer_name();
				
				z=0;
				groupColumnTotal=0;
				response=new StkRepo10Response();
				months=new LinkedHashMap();
				group=new LinkedHashMap();
				
			}

			
			// before put please check depo code in branch list if not found put 0 value in map otherwise actual zero
			for(int b=k;b<sz;b++)
			{
				MonthDto mn=monthData.get(b);
				if(mn.getMnth_code()==data.getMnth_code())
				{
					months.put(data.getMnth_abbr(), request.getUv()==2?data.getSales_val():data.getSales());
					columnTotal+=request.getUv()==2?data.getSales_val():data.getSales();
					if(group.containsKey(data.getMnth_abbr()))
					{
						long gval = group.get(data.getMnth_abbr())+(request.getUv()==2?data.getSales_val():data.getSales());
						group.put(data.getMnth_abbr(), gval);
					}
					else
					{
						group.put(data.getMnth_abbr(), request.getUv()==2?data.getSales_val():data.getSales());
					}
					
					if(total.containsKey(data.getMnth_abbr()))
					{
						long ggval = total.get(data.getMnth_abbr())+(request.getUv()==2?data.getSales_val():data.getSales());
						total.put(data.getMnth_abbr(), ggval);
					}
					else
					{
						total.put(data.getMnth_abbr(), request.getUv()==2?data.getSales_val():data.getSales());
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
			response=new StkRepo10Response();
			response.setName(name);
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
			response=new StkRepo10Response();
			response.setName(ter_name);
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
			total.put("TOTAL", grandColumnTotal);

			months.putAll(total);
			response=new StkRepo10Response();
			response.setName("Total");
			response.setMonths(months);
			response.setColor(2);
			saleList.add(response);		
		return new ApiResponse<StkRepo10Response>(title.toString(),size,lupdate,saleList);
	}

}
