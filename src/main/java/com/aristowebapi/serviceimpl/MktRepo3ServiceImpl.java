package com.aristowebapi.serviceimpl;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aristowebapi.dao.MktRepo3Dao;
import com.aristowebapi.dto.MktRepo3;
import com.aristowebapi.dto.MonthDto;
import com.aristowebapi.request.MktRepo3Request;
import com.aristowebapi.response.ApiResponse;
import com.aristowebapi.response.MktRepo3Response;
import com.aristowebapi.service.MktRepo3Service;

@Service
public class MktRepo3ServiceImpl implements MktRepo3Service {
	
	@Autowired
	private MktRepo3Dao mktRepo3Dao;

	public String lupdate="";
	
	private String getTitle(MktRepo3Request request,MktRepo3 data)
	{
		StringBuilder title=new StringBuilder();
		title.append(request.getDepoCode()==0?"All India ":data.getBrname());
		title.append(request.getRepType()==1?" Product/ ":" Group/ ");
		title.append(request.getUv()==1?"Unit Wise ":"Value Wise ");
		title.append(request.getOption()==1?"Sales Trend ":"Target Trend ");
		return title.toString();

	}

	@Override
	public ApiResponse<MktRepo3Response> getMktRepo3(MktRepo3Request request) {
		
		
		List<MonthDto> monthData = mktRepo3Dao.getAllMonth(request.getMyear());
		int sz=monthData.size();
		int k=0;
		int z=0;

		
		String title=null;
		List<MktRepo3> mktRepo3List=null;
		
		if(request.getOption()==1) /// sale trend
		{
		 mktRepo3List=mktRepo3Dao.getWebReportNetTrend(request.getMyear(),request.getDivCode(),request.getDepoCode()
				,request.getSmon(),request.getEmon(),request.getUtype(),request.getLoginId(),request.getRepType(),request.getHqCode());
		}
		else if(request.getOption()==2) /// target trend
		{
		 mktRepo3List=mktRepo3Dao.getWebReportTargetTrend(request.getMyear(),request.getDivCode(),request.getDepoCode()
				,request.getSmon(),request.getEmon(),request.getUtype(),request.getLoginId(),request.getRepType(),request.getHqCode());
		}
		
		MktRepo3Response response=null;
		List<MktRepo3Response> saleList = new ArrayList();
		Map<String, Long> months=null;
		Map<String, Long> group=null;
		Map<String, Long> total=null;

		boolean first=true;

		int size = mktRepo3List.size();
		int pcode=0;
		int mgrp=0;
		long columnTotal=0;
		long groupColumnTotal=0;
		long grandColumnTotal=0;

		String pname=null;
		String pack=null;
		String gname=null;
		for (int i=0;i<size;i++)
		{
			MktRepo3 data = mktRepo3List.get(i);
			
			if(first)
			{
				pcode=data.getMcode();
				pname=data.getMname();
				pack=data.getPack();
				mgrp=data.getMgrp();
				gname=data.getGp_name();
				response=new MktRepo3Response();
				months=new LinkedHashMap();
				group=new LinkedHashMap();
				total=new LinkedHashMap();
				first=false;
				
				title = getTitle(request, data); 
			}

			if(pcode!=data.getMcode())
			{
				response.setPcode(pcode);
				response.setPname(pname);
				response.setPack(pack);
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
				pcode=data.getMcode();
				pname=data.getMname();
				pack=data.getPack();
				columnTotal=0;
				
				k=0;
				response=new MktRepo3Response();
				months=new LinkedHashMap();

			}
			if(mgrp!=data.getMgrp())
			{
				response.setPcode(mgrp);
				response.setPname(gname);
				response.setPack("");
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
				response=new MktRepo3Response();
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
					groupColumnTotal+=data.getSales_val();
					//grandColumnTotal+=data.getSales_val();
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
			response=new MktRepo3Response();
			response.setPcode(pcode);
			response.setPname(pname);
			response.setPack(pack);
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
			response=new MktRepo3Response();
			response.setPcode(mgrp);
			response.setPname(gname);
			response.setPack("");
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
//			total.keySet().stream().forEach(d->System.out.print(d));

			months.putAll(total);
			response=new MktRepo3Response();
			response.setPcode(0);
			response.setPname("Grand Total");
			response.setPack("");
			response.setMonths(months);
			response.setColor(2);
			saleList.add(response);

			return new ApiResponse<MktRepo3Response>(title.toString(),size,lupdate,saleList);


			
		}


}
