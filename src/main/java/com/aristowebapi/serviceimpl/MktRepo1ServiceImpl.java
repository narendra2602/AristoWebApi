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
import com.aristowebapi.dto.MktRepo1Ach;
import com.aristowebapi.request.MktRepo1Request;
import com.aristowebapi.response.ApiResponse;
import com.aristowebapi.response.MktRepo1AchResponse;
import com.aristowebapi.response.MktRepo1Response;
import com.aristowebapi.service.MktRepo1Service;
import com.aristowebapi.utility.AppCalculationUtils;

@Service
public class MktRepo1ServiceImpl implements MktRepo1Service{
	
	Logger logger = LoggerFactory.getLogger(MktRepo1ServiceImpl.class);
	@Autowired
	private MktRepo1Dao mktRepo1Dao;
	

	private List<BranchMasterDto> getBranchData(MktRepo1Request request)
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
	
	private String getTitle(MktRepo1Request request,MktRepo1 data)
	{
		String repname="Gross Sale";

		switch(request.getOptType())
		{
		    case 1: repname="Gross Sale";
		            break;
		    case 2: repname="Saleable";
		    		break;
		    case 3: repname="Expiry Return";
		    		break;
		    case 4: repname="Breakage";
		    		break;
		    case 5: repname="Net Sale";
    				break;
		    case 6: repname="Target";
    				break;
		    case 7: repname="Lys Sale";
					break;


		}
		
		
		
		StringBuilder title=new StringBuilder();
		title.append(request.getDepoCode()==0?"All India ":mktRepo1Dao.getBranch(request.getDepoCode())+" Branch: ");
		title.append(request.getRepType()==1?" Product/ ":" Group/ ");
		title.append(request.getUv()==1?"Unit Wise ":"Value Wise ");
		title.append(repname+" for the Month of ");
		title.append(data.getSmname());
		title.append(" To ");
		title.append(data.getEmname());
		return title.toString();

	}
	
	private List<MktRepo1> getReport(MktRepo1Request request)
	{
		List<MktRepo1> reportList = null;
	
		if(request.getOptType()==1)
				reportList=mktRepo1Dao.getWebReportGross(request.getMyear(),request.getDivCode(),request.getDepoCode()
					,request.getSmon(),request.getEmon(),request.getUtype(),request.getLoginId(),request.getRepType(),request.getDataType());
		else if(request.getOptType()==2 || request.getOptType()==3 || request.getOptType()==4)
			reportList=mktRepo1Dao.getWebReportCredit(request.getMyear(),request.getDivCode(),request.getDepoCode()
				,request.getSmon(),request.getEmon(),request.getUtype(),request.getLoginId(),request.getRepType(),request.getDataType());
		else if(request.getOptType()==5)
			reportList=mktRepo1Dao.getWebReportNet(request.getMyear(),request.getDivCode(),request.getDepoCode()
				,request.getSmon(),request.getEmon(),request.getUtype(),request.getLoginId(),request.getRepType());
		else if(request.getOptType()==6 || request.getOptType()==7)
			reportList=mktRepo1Dao.getWebReportTarget(request.getMyear(),request.getDivCode(),request.getDepoCode()
				,request.getSmon(),request.getEmon(),request.getUtype(),request.getLoginId(),request.getRepType(),request.getDataType());

		return reportList;
		
	}

	
	
	private String getTitleAch(MktRepo1Request request,MktRepo1Ach data)
	{
		String repname="";


		switch(request.getOptType())
		{
		    case 8: repname="Achievement";
		            break;
		    case 9: repname="Gth";
		    		break;
		    case 10: repname="PMR";
		    		break;
		    case 11: repname="PI Sale";
		    		break;
		}
		
		
		StringBuilder title=new StringBuilder();
		title.append(request.getDepoCode()==0?"All India ":mktRepo1Dao.getBranch(request.getDepoCode())+" Branch: ");
		title.append(request.getRepType()==1?" Product/ ":" Group/ ");
		title.append(request.getUv()==1?"Unit Wise ":"Value Wise ");
		title.append(repname+" for the Month of ");
		title.append(data.getSmname());
		title.append(" To ");
		title.append(data.getEmname());
		return title.toString();

	}

	
	private List<MktRepo1Ach> getReportAch(MktRepo1Request request)
	{
		List<MktRepo1Ach> reportList = null;
	
		if(request.getOptType()==8)
				reportList=mktRepo1Dao.getWebReportAch(request.getMyear(),request.getDivCode(),request.getDepoCode()
					,request.getSmon(),request.getEmon(),request.getUtype(),request.getLoginId(),request.getRepType());

		return reportList;
		
	}
	@Override
	public ApiResponse<MktRepo1Response> getMktRepo1(MktRepo1Request request) {
//		return mktRepo1Dao.web_report_gross(2023, 1, 0, 1, 3, 2, 163,1,1);

		logger.info(AristoWebLogMsgConstant.MKT_REPORT_SERVICE_01,"getMktRepo1");
		List<BranchMasterDto> branchData = getBranchData(request);
		int sz=branchData.size();
		int k=0;
		int z=0;
		String title=null;
		
		
		List<MktRepo1> grossSaleList=getReport(request);

		
		
		MktRepo1Response response=null;
		List<MktRepo1Response> saleList = new ArrayList();
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
		System.out.println("size of gross list "+size);
		//create ReportTitleResponse class object and set title with Report heading
		for (int i=0;i<size;i++)
		{
			MktRepo1 data = grossSaleList.get(i);
			if(data.getDepo_code()==0)
				continue;
			if(first)
			{
				pcode=data.getMcode();
				pname=data.getMname();
				pack=data.getPack();
				mgrp=data.getMgrp();
				gname=data.getGp_name();
				response=new MktRepo1Response();
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
				response=new MktRepo1Response();
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
				response=new MktRepo1Response();
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

		
		
		response=new MktRepo1Response();
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
		response=new MktRepo1Response();
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
		response=new MktRepo1Response();
		response.setPcode(0);
		response.setPname("Grand Total");
		response.setPack("");
		response.setBranches(branches);
		response.setColor(2);
		saleList.add(response);

		return new ApiResponse<MktRepo1Response>(title.toString(),size,saleList);
//		return saleList;
	}

	@Override
	public ApiResponse<MktRepo1AchResponse> getMktRepo1Ach(MktRepo1Request request) {
		
		logger.info(AristoWebLogMsgConstant.MKT_REPORT_SERVICE_01,"getMktRepo1");
		List<BranchMasterDto> branchData = getBranchData(request);
		int sz=branchData.size();
		int k=0;
		int z=0;
		String title=null;
		
		
		List<MktRepo1Ach> grossSaleList=getReportAch(request);

		
		
		MktRepo1AchResponse response=null;
		List<MktRepo1AchResponse> saleList = new ArrayList();
		Map<String, Double> branches=null;
		Map<String, Double> group=null;
		Map<String, Double> total=null;
		boolean first=true;
		int pcode=0;
		int mgrp=0;
		double columnTotal=0;
		double groupColumnTotal=0;
		double grandColumnTotal=0;

		long gsale=0;
		long gtarget=0;
		
		 long horizontalSalqty=0;
		 long horizontalSalval=0;
		 long horizontalTarqty=0;
		 long horizontalTarval=0;
		 Map<String, Long> grVerticalSalval = null; 
		 Map<String, Long> grVerticalTarval = null;
		 Map<String, Long> bottomVerticalSalval = null; 
		 Map<String, Long> bottomVerticalTarval = null;

		
		String pname=null;
		String pack=null;
		String gname=null;
		int size = grossSaleList.size();
		System.out.println("size of gross list "+size);
		//create ReportTitleResponse class object and set title with Report heading
		for (int i=0;i<size;i++)
		{
			MktRepo1Ach data = grossSaleList.get(i);
			if(data.getDepo_code()==0)
				continue;
			if(first)
			{
				pcode=data.getMcode();
				pname=data.getMname();
				pack=data.getPack();
				mgrp=data.getMgrp();
				gname=data.getGp_name();
				response=new MktRepo1AchResponse();
				branches=new LinkedHashMap();
				group=new LinkedHashMap();
				total=new LinkedHashMap();
				grVerticalSalval =new LinkedHashMap();
				grVerticalTarval=new LinkedHashMap();
				bottomVerticalSalval =new LinkedHashMap();
				bottomVerticalTarval =new LinkedHashMap();
				first=false;
				
				title = getTitleAch(request, data); 
				
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
					branches.put(bm.getDepo_name(), 0.0);
					k++;
				}

				if(request.getUv()==1)
					columnTotal=horizontalTarqty!=0?AppCalculationUtils.calculateAch(horizontalSalqty, horizontalTarqty):0;
				else
					columnTotal=horizontalTarval!=0?AppCalculationUtils.calculateAch(horizontalSalval, horizontalTarval):0;
		
				branches.put("TOTAL", columnTotal);
				response.setBranches(branches);
				
				saleList.add(response);
				pcode=data.getMcode();
				pname=data.getMname();
				pack=data.getPack();
				columnTotal=0;
				horizontalSalqty=0;
				horizontalSalval=0;
				horizontalTarqty=0;
				horizontalTarval=0;
				
				k=0;
				response=new MktRepo1AchResponse();
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
					group.put(bm.getDepo_name(), 0.0);
					z++;
				}


				for(int b=0;b<sz;b++)
				{
					BranchMasterDto bm=branchData.get(b);
					long sale = grVerticalSalval.get(bm.getDepo_name());
					long target = grVerticalTarval.get(bm.getDepo_name());
					gsale += grVerticalSalval.get(bm.getDepo_name());
					gtarget += grVerticalTarval.get(bm.getDepo_name());
					group.put(bm.getDepo_name(), AppCalculationUtils.calculateAch(sale, target));
					
				}

				groupColumnTotal=gtarget!=0?AppCalculationUtils.calculateAch(gsale, gtarget):0;
				
				group.put("TOTAL", groupColumnTotal);

				branches.putAll(group);

				
				response.setBranches(branches);
				response.setColor(1);
				saleList.add(response);
				gsale=0;
				gtarget=0;
				
				mgrp=data.getMgrp();
				gname=data.getGp_name();
				
				z=0;
				groupColumnTotal=0;
				response=new MktRepo1AchResponse();
				branches=new LinkedHashMap();
				group=new LinkedHashMap();
				grVerticalSalval =new LinkedHashMap();
				grVerticalTarval =new LinkedHashMap();
				
			}

			
			// before put please check depo code in branch list if not found put 0 value in map otherwise actual zero
			for(int b=k;b<sz;b++)
			{
				BranchMasterDto bm=branchData.get(b);
				if(bm.getDepo_code()==data.getDepo_code())
				{
					
					branches.put(data.getDepo_name(), request.getUv()==2?data.getAchval():data.getAchqty());
					
	        		 horizontalSalqty+= data.getSales();
	        		 horizontalSalval+= data.getSales_val();
	        		 horizontalTarqty+= data.getCr_u();
	        		 horizontalTarval+= data.getNet_u();
	        		 
					if(grVerticalSalval.containsKey(data.getDepo_name()))
					{
						if(data.getSales_val()>0)
						{
							long gval = grVerticalSalval.get(data.getDepo_name())+data.getSales_val();
							grVerticalSalval.put(data.getDepo_name(), gval);
						}

					}
					else
					{
						grVerticalSalval.put(data.getDepo_name(), data.getSales_val());
					}

					
					if(grVerticalTarval.containsKey(data.getDepo_name()))
					{
						if(data.getNet_u()>0)
						{
							long gval = grVerticalTarval.get(data.getDepo_name())+data.getNet_u();
							grVerticalTarval.put(data.getDepo_name(), gval);
						}

					}
					else
					{
						grVerticalTarval.put(data.getDepo_name(), data.getNet_u());
					}

					
					if(bottomVerticalSalval.containsKey(data.getDepo_name()))
					{
						long ggval = bottomVerticalSalval.get(data.getDepo_name())+data.getSales_val();
						bottomVerticalSalval.put(data.getDepo_name(), ggval);
						 ggval = bottomVerticalTarval.get(data.getDepo_name())+data.getNet_u();
						bottomVerticalTarval.put(data.getDepo_name(), ggval);
					}
					else
					{
						bottomVerticalSalval.put(data.getDepo_name(), data.getSales_val());
						bottomVerticalTarval.put(data.getDepo_name(), data.getNet_u());
					}

					k++;
					break;
				}
				else
				{
					branches.put(bm.getDepo_name(), 0.0);

					if(grVerticalSalval.containsKey(bm.getDepo_name()))
					{
						// do nothing
					}
					else
					{
						grVerticalSalval.put(bm.getDepo_name(), 0L);

					}

					if(grVerticalTarval.containsKey(bm.getDepo_name()))
					{
						// do nothing
					}
					else
					{
						grVerticalTarval.put(bm.getDepo_name(), 0L);

					}
					

					
					k++;
				}
			}
			
			
		}

		
		
		response=new MktRepo1AchResponse();
		response.setPcode(pcode);
		response.setPname(pname);
		response.setPack(pack);
		z=k;
		for(int b=k;b<sz;b++)
		{
			BranchMasterDto bm=branchData.get(b);
			branches.put(bm.getDepo_name(), 0.0);
			k++;
		}
		
		if(request.getUv()==1)
			columnTotal=horizontalTarqty!=0?AppCalculationUtils.calculateAch(horizontalSalqty, horizontalTarqty):0;
		else
			columnTotal=horizontalTarval!=0?AppCalculationUtils.calculateAch(horizontalSalval, horizontalTarval):0;

		branches.put("TOTAL", columnTotal);

		response.setBranches(branches);
		saleList.add(response);

		
		branches=new LinkedHashMap();
		response=new MktRepo1AchResponse();
		response.setPcode(mgrp);
		response.setPname(gname);
		response.setPack("");
		for(int b=z;b<sz;b++)
		{
			BranchMasterDto bm=branchData.get(b);
			group.put(bm.getDepo_name(), 0.0);
			z++;
		}

		for(int b=0;b<sz;b++)
		{
			BranchMasterDto bm=branchData.get(b);
			long sale = grVerticalSalval.get(bm.getDepo_name());
			long target = grVerticalTarval.get(bm.getDepo_name());
			gsale += grVerticalSalval.get(bm.getDepo_name());
			gtarget += grVerticalTarval.get(bm.getDepo_name());
			group.put(bm.getDepo_name(), AppCalculationUtils.calculateAch(sale, target));
			
		}

		groupColumnTotal=gtarget!=0?AppCalculationUtils.calculateAch(gsale, gtarget):0;
			group.put("TOTAL", groupColumnTotal);

		branches.putAll(group);
		response.setBranches(branches);
		response.setColor(1);
		saleList.add(response);
		
		
		
		branches=new LinkedHashMap();


		for(int b=0;b<sz;b++)
		{
			BranchMasterDto bm=branchData.get(b);
			long sale = bottomVerticalSalval.get(bm.getDepo_name());
			long target = bottomVerticalTarval.get(bm.getDepo_name());
			gsale += bottomVerticalSalval.get(bm.getDepo_name());
			gtarget += bottomVerticalTarval.get(bm.getDepo_name());
			total.put(bm.getDepo_name(), AppCalculationUtils.calculateAch(sale, target));
			
		}

		grandColumnTotal=gtarget!=0?AppCalculationUtils.calculateAch(gsale, gtarget):0;

		
		
		
		total.put("TOTAL", grandColumnTotal);

		branches.putAll(total);
		response=new MktRepo1AchResponse();
		response.setPcode(0);
		response.setPname("Grand Total");
		response.setPack("");
		response.setBranches(branches);
		response.setColor(2);
		saleList.add(response);

		return new ApiResponse<MktRepo1AchResponse>(title.toString(),size,saleList);
	}


}
