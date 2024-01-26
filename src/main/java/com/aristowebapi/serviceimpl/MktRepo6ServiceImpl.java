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
import com.aristowebapi.dao.MktRepo6Dao;
import com.aristowebapi.dto.MktRepo6;
import com.aristowebapi.dto.MonthDto;
import com.aristowebapi.request.MktRepo6Request;
import com.aristowebapi.response.ApiResponse;
import com.aristowebapi.response.MktRepo6Response;
import com.aristowebapi.service.MktRepo6Service;

@Service
public class MktRepo6ServiceImpl implements MktRepo6Service  {

	Logger logger = LoggerFactory.getLogger(MktRepo6ServiceImpl.class);
	
	@Autowired
	private MktRepo6Dao mktRepo6Dao;
	
	public String lupdate="";
	
	private String getTitle(MktRepo6Request request,MktRepo6 data)
	{
		StringBuilder title=new StringBuilder();
		title.append(request.getDepoCode()==0?"All India ":mktRepo6Dao.getBranch(request.getDepoCode())+" Branch: " );
		title.append(request.getGpCode()==0?" All Groups ": data.getGp_name());
		title.append(" Branch Wise Sales Trend ");
		return title.toString();

	}

	
	@Override
	public ApiResponse<MktRepo6Response> getMktRepo6(MktRepo6Request request) {

		logger.info(AristoWebLogMsgConstant.MKT_REPORT_SERVICE_06,"getMktRepo6");
		
		List<MonthDto> monthData = mktRepo6Dao.getAllMonth(request.getMyear());
		int sz=monthData.size();
		int k=0;
		int z=0;

		
		String title=null;
		
		
		List<MktRepo6> mktRepo6SaleList=mktRepo6Dao.getWebReportHqTrend(request.getMyear(),request.getDivCode(),request.getDepoCode()
				,request.getSmon(),request.getEmon(),request.getUtype(),request.getLoginId(),request.getGpCode());
		
		MktRepo6Response response=null;
		
		List<MktRepo6Response> saleList = new ArrayList();

		Map<String, Long> months=null;
		Map<String, Long> total=null;
		
		int size = mktRepo6SaleList.size();
		long columnTotal=0;
		long grandColumnTotal=0;
		boolean first=true;
		int depo_code=0;
		String name="";
		int fs=0;
		int gfs=0;
		for (int i=0;i<size;i++)
		{
			
			MktRepo6 data = mktRepo6SaleList.get(i);

			if(data.getDepo_code()==0)
				continue;

			if(first)
			{
				response=new MktRepo6Response();
				depo_code=data.getDepo_code();
				name=data.getDepo_name();
				months=new LinkedHashMap();
				total=new LinkedHashMap();
				first=false;
				title = getTitle(request, data); 
			}
			
			if(depo_code!=data.getDepo_code())
			{
				response.setName(name);
				response.setFs(fs);
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
				depo_code=data.getDepo_code();
				name=data.getDepo_name();
				fs=0;
				columnTotal=0;
				
				k=0;
				response=new MktRepo6Response();
				months=new LinkedHashMap();

			}

			// before put please check depo code in branch list if not found put 0 value in map otherwise actual zero
			for(int b=k;b<sz;b++)
			{
				MonthDto mn=monthData.get(b);
				if(mn.getMnth_code()==data.getMnth_code())
				{
					months.put(data.getMnth_abbr(), data.getSales_val());
					columnTotal+=data.getSales_val();
					gfs+=data.getFs();
					fs+=data.getFs();
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
		
		
		response=new MktRepo6Response();
		response.setName(name);
		response.setFs(fs);
		z=k;
		for(int b=k;b<sz;b++)
		{
			MonthDto mn=monthData.get(b);
			months.put(mn.getMnth_abbr(), 0L);
			total.put(mn.getMnth_abbr(), 0L);
			k++;
		}
		months.put("TOTAL", columnTotal);

		response.setMonths(months);
		saleList.add(response);

		
		
		grandColumnTotal = total.values().stream().mapToLong(d -> d).sum();
		
		months=new LinkedHashMap();
		total.put("TOTAL", grandColumnTotal);
		total.keySet().stream().forEach(d->System.out.print(d));

		months.putAll(total);
		response=new MktRepo6Response();
		response.setName("Grand Total");
		response.setFs(gfs);
		response.setMonths(months);
		response.setColor(2);
		saleList.add(response);

		return new ApiResponse<MktRepo6Response>(title.toString(),size,lupdate,saleList);


	}
}
