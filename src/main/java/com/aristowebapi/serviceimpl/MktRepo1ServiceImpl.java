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
import com.aristowebapi.dao.MktRepo1Dao;
import com.aristowebapi.dto.BranchMasterDto;
import com.aristowebapi.dto.MktRepo1;
import com.aristowebapi.request.GrossSaleRequest;
import com.aristowebapi.response.ApiResponse;
import com.aristowebapi.response.GrossSaleResponse;
import com.aristowebapi.service.MktRepo1Service;

@Service
public class MktRepo1ServiceImpl implements MktRepo1Service{
	
	Logger logger = LoggerFactory.getLogger(MktRepo1ServiceImpl.class);
	@Autowired
	private MktRepo1Dao mktRepo1Dao;
	

	private List<BranchMasterDto> getBranchData(GrossSaleRequest request)
	{
		List<BranchMasterDto> branchData = null;
		if(request.getUtype()==4 && request.getDepoCode()==0)
			branchData =mktRepo1Dao.getUtype4Hq(request.getMyear(),request.getDivCode(), request.getLoginId());
		else if(request.getUtype()==5 && request.getDepoCode()==0)
			branchData =mktRepo1Dao.getUtype5Branch(request.getLoginId());
		else if(request.getDepoCode()==0)
			branchData =mktRepo1Dao.getAllBranch();
		else
			branchData =mktRepo1Dao.getAllHq(request.getMyear(),request.getDivCode(), request.getDepoCode());	
		
//		branchData.stream().forEach(e->System.out.print(e.getDepo_name()));
		return branchData;
		
	}
	
	private String getTitle(GrossSaleRequest request,MktRepo1 data)
	{
		StringBuilder title=new StringBuilder();
		title.append(request.getDepoCode()==0?"All India ":data.getDepo_name());
		title.append(request.getRepType()==1?" Product/ ":" Group/ ");
		title.append(request.getUv()==1?"Unit Wise ":"Value Wise ");
		title.append("Gross Sale for the Month of ");
		title.append(data.getSmname());
		title.append(" To ");
		title.append(data.getEmname());
		return title.toString();

	}
	

	@Override
	public ApiResponse<GrossSaleResponse> getMktRepo1(GrossSaleRequest request) {
//		return mktRepo1Dao.web_report_gross(2023, 1, 0, 1, 3, 2, 163,1,1);

		logger.info(AristoWebLogMsgConstant.MKT_REPORT_SERVICE_01,"getMktRepo1");
		List<BranchMasterDto> branchData = getBranchData(request);
		int sz=branchData.size();
		int k=0;
		int z=0;
		String title=null;
		
		
		List<MktRepo1> grossSaleList=mktRepo1Dao.getWebReportGross(request.getMyear(),request.getDivCode(),request.getDepoCode()
				,request.getSmon(),request.getEmon(),request.getUtype(),request.getLoginId(),request.getRepType(),request.getDataType());
		GrossSaleResponse response=null;
		List<GrossSaleResponse> saleList = new ArrayList();
		Map<String, Long> branches=null;
		Map<String, Long> group=null;
		Map<String, Long> total=null;
		boolean first=true;
		int pcode=0;
		int mgrp=0;
		long columnTotal=0;
		long groupColumnTotal=0;
		long grandColumnTotal=0;

		String pname=null;
		String pack=null;
		String gname=null;
		int size = grossSaleList.size();
		
		//create ReportTitleResponse class object and set title with Report heading
		for (int i=0;i<size;i++)
		{
			MktRepo1 data = grossSaleList.get(i);
			if(first)
			{
				pcode=data.getMcode();
				pname=data.getMname();
				pack=data.getPack();
				mgrp=data.getMgrp();
				gname=data.getGp_name();
				response=new GrossSaleResponse();
				branches=new LinkedHashMap();
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
					BranchMasterDto bm=branchData.get(b);
					branches.put(bm.getDepo_name(), 0L);
					k++;
				}

				branches.put("TOTAL", columnTotal);
				response.setBranches(branches);
				
				saleList.add(response);
				pcode=data.getMcode();
				pname=data.getMname();
				pack=data.getPack();
				columnTotal=0;
				
				k=0;
				response=new GrossSaleResponse();
				branches=new LinkedHashMap();

			}

			if(mgrp!=data.getMgrp())
			{
				response.setPcode(mgrp);
				response.setPname(gname);
				response.setPack("");
				for(int b=z;b<sz;b++)
				{
					BranchMasterDto bm=branchData.get(b);
					group.put(bm.getDepo_name(), 0L);
					z++;
				}

				group.put("TOTAL", groupColumnTotal);

				branches.putAll(group);

				
				response.setBranches(branches);
				response.setColor(1);
				saleList.add(response);

				
				mgrp=data.getMgrp();
				gname=data.getGp_name();
				
				z=0;
				groupColumnTotal=0;
				response=new GrossSaleResponse();
				branches=new LinkedHashMap();
				group=new LinkedHashMap();
				
			}

			
			// before put please check depo code in branch list if not found put 0 value in map otherwise actual zero
			for(int b=k;b<sz;b++)
			{
				BranchMasterDto bm=branchData.get(b);
				if(bm.getDepo_code()==data.getDepo_code())
				{
					branches.put(data.getDepo_name(), request.getUv()==2?data.getSales_val():data.getSales());
					columnTotal+=request.getUv()==2?data.getSales_val():data.getSales();
					groupColumnTotal+=data.getSales_val();
					//grandColumnTotal+=data.getSales_val();
					if(group.containsKey(data.getDepo_name()))
					{
						long gval = group.get(data.getDepo_name())+data.getSales_val();
						group.put(data.getDepo_name(), gval);
					}
					else
					{
						group.put(data.getDepo_name(), data.getSales_val());
					}
					
					if(total.containsKey(data.getDepo_name()))
					{
						long ggval = total.get(data.getDepo_name())+data.getSales_val();
						total.put(data.getDepo_name(), ggval);
					}
					else
					{
						total.put(data.getDepo_name(), data.getSales_val());
					}

					k++;
					break;
				}
				else
				{
					branches.put(bm.getDepo_name(), 0L);
					if(group.containsKey(bm.getDepo_name()))
					{
						// do nothing
					}
					else
					{
						group.put(bm.getDepo_name(), 0L);

					}

					if(total.containsKey(bm.getDepo_name()))
					{
						// do nothing
					}
					else
					{
						total.put(bm.getDepo_name(), 0L);

					}

					
					k++;
				}
			}
			
			
		}

		
		
		response=new GrossSaleResponse();
		response.setPcode(pcode);
		response.setPname(pname);
		response.setPack(pack);
		z=k;
		for(int b=k;b<sz;b++)
		{
			BranchMasterDto bm=branchData.get(b);
			branches.put(bm.getDepo_name(), 0L);
			k++;
		}
		branches.put("TOTAL", columnTotal);

		response.setBranches(branches);
		saleList.add(response);

		
		branches=new LinkedHashMap();
		response=new GrossSaleResponse();
		response.setPcode(mgrp);
		response.setPname(gname);
		response.setPack("");
		for(int b=z;b<sz;b++)
		{
			BranchMasterDto bm=branchData.get(b);
			group.put(bm.getDepo_name(), 0L);
			z++;
		}

		group.put("TOTAL", groupColumnTotal);

		branches.putAll(group);
		response.setBranches(branches);
		response.setColor(1);
		saleList.add(response);
		
		
		grandColumnTotal = total.values().stream().mapToLong(d -> d).sum();
		
		branches=new LinkedHashMap();
		total.put("TOTAL", grandColumnTotal);

		branches.putAll(total);
		response=new GrossSaleResponse();
		response.setPcode(0);
		response.setPname("Grand Total");
		response.setPack("");
		response.setBranches(branches);
		response.setColor(2);
		saleList.add(response);

		return new ApiResponse<GrossSaleResponse>(title.toString(),saleList);
//		return saleList;
	}


}
