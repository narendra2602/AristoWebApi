package com.aristowebapi.serviceimpl;

import java.text.SimpleDateFormat;
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
import com.aristowebapi.dao.MktRepo1Dao;
import com.aristowebapi.dto.BranchMasterDto;
import com.aristowebapi.dto.DashBoardData;
import com.aristowebapi.dto.MktRepo1;
import com.aristowebapi.dto.MktRepo1Ach;
import com.aristowebapi.dto.MtbStockiestDto;
import com.aristowebapi.dto.NearExpiry;
import com.aristowebapi.request.MktRepo1Request;
import com.aristowebapi.request.NearExpiryRequest;
import com.aristowebapi.response.ApiResponse;
import com.aristowebapi.response.MktRepo1AchResponse;
import com.aristowebapi.response.MktRepo1Response;
import com.aristowebapi.response.MtbStockiestResponse;
import com.aristowebapi.response.NearExpiryResponse;
import com.aristowebapi.service.MktRepo1Service;
import com.aristowebapi.utility.AppCalculationUtils;

@Service
public class MktRepo1ServiceImpl implements MktRepo1Service{
	
	
	Logger logger = LoggerFactory.getLogger(MktRepo1ServiceImpl.class);
	@Autowired
	private MktRepo1Dao mktRepo1Dao;
	
	@Autowired
	private AristoWebMessageConstant  aristoWebMessageConstant;
	
	public String lupdate="";

	private List<BranchMasterDto> getBranchData(MktRepo1Request request)
	{
		List<BranchMasterDto> branchData = null;

		
		if(request.getUtype()==4 && request.getDepoCode()==0)
			branchData =mktRepo1Dao.getUtype4HqAll(request.getMyear(),request.getDivCode(), request.getLoginId());
		else if(request.getUtype()==4 && request.getDepoCode()>0)
			branchData =mktRepo1Dao.getUtype4Hq(request.getMyear(),request.getDivCode(),request.getDepoCode(), request.getLoginId());
		else if(request.getUtype()==5 && request.getDepoCode()==0)
			branchData =mktRepo1Dao.getUtype5Branch(request.getLoginId());
		else if(request.getDepoCode()==0)
			branchData =mktRepo1Dao.getAllBranch();
		else
			branchData =mktRepo1Dao.getAllHq(request.getMyear(),request.getDivCode(), request.getDepoCode());	
		
//		branchData.stream().forEach(e->System.out.print(e.getDepo_name()));
		
		System.out.println("size of branchata is ****** "+branchData.size());
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
		    case 10: repname="PMR";
		    		break;
		    case 11: repname="PI Sale";
					break;
		    case 12: repname="Gross+Free Sale";
					break;


		}
		
		
		
		StringBuilder title=new StringBuilder();
		System.out.println(aristoWebMessageConstant.divisionMap.get(String.valueOf(data.getDiv_code())));
		title.append(aristoWebMessageConstant.divisionMap.get(String.valueOf(data.getDiv_code())));
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
					,request.getSmon(),request.getEmon(),request.getUtype(),request.getLoginId(),request.getRepType(),1);
		else if(request.getOptType()==11)
			reportList=mktRepo1Dao.getWebReportGross(request.getMyear(),request.getDivCode(),request.getDepoCode()
				,request.getSmon(),request.getEmon(),request.getUtype(),request.getLoginId(),request.getRepType(),2);
		else if(request.getOptType()==12)
			reportList=mktRepo1Dao.getWebReportGross(request.getMyear(),request.getDivCode(),request.getDepoCode()
				,request.getSmon(),request.getEmon(),request.getUtype(),request.getLoginId(),request.getRepType(),3);
		else if(request.getOptType()==2 )
			reportList=mktRepo1Dao.getWebReportCredit(request.getMyear(),request.getDivCode(),request.getDepoCode()
			,request.getSmon(),request.getEmon(),request.getUtype(),request.getLoginId(),request.getRepType(),10);
		else if(request.getOptType()==3)
			reportList=mktRepo1Dao.getWebReportCredit(request.getMyear(),request.getDivCode(),request.getDepoCode()
			,request.getSmon(),request.getEmon(),request.getUtype(),request.getLoginId(),request.getRepType(),11);
		else if(request.getOptType()==4)
			reportList=mktRepo1Dao.getWebReportCredit(request.getMyear(),request.getDivCode(),request.getDepoCode()
			,request.getSmon(),request.getEmon(),request.getUtype(),request.getLoginId(),request.getRepType(),12);
		else if(request.getOptType()==5)
			reportList=mktRepo1Dao.getWebReportNet(request.getMyear(),request.getDivCode(),request.getDepoCode()
				,request.getSmon(),request.getEmon(),request.getUtype(),request.getLoginId(),request.getRepType());
		else if(request.getOptType()==6)
			reportList=mktRepo1Dao.getWebReportTarget(request.getMyear(),request.getDivCode(),request.getDepoCode()
				,request.getSmon(),request.getEmon(),request.getUtype(),request.getLoginId(),request.getRepType(),10);
		else if(request.getOptType()==7)
			reportList=mktRepo1Dao.getWebReportTarget(request.getMyear(),request.getDivCode(),request.getDepoCode()
				,request.getSmon(),request.getEmon(),request.getUtype(),request.getLoginId(),request.getRepType(),20);

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

		}
		
		
		
		StringBuilder title=new StringBuilder();
		title.append(aristoWebMessageConstant.divisionMap.get(String.valueOf(data.getDiv_code())));
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
	
		System.out.println(request.getMyear()+" "+request.getDivCode()+" "+request.getDepoCode()
					+" "+request.getSmon()+" "+request.getEmon()+" "+request.getUtype()+" "+request.getLoginId()+" "+request.getRepType());
		if(request.getOptType()==8)
				reportList=mktRepo1Dao.getWebReportAch(request.getMyear(),request.getDivCode(),request.getDepoCode()
					,request.getSmon(),request.getEmon(),request.getUtype(),request.getLoginId(),request.getRepType());
		else if(request.getOptType()==9)
			reportList=mktRepo1Dao.getWebReportGth(request.getMyear(),request.getDivCode(),request.getDepoCode()
				,request.getSmon(),request.getEmon(),request.getUtype(),request.getLoginId(),request.getRepType(),20);
		
		return reportList;
		
	}

	private List<MktRepo1> getReportPMR(MktRepo1Request request)
	{
		List<MktRepo1> 	reportList=mktRepo1Dao.getWebReportNet(request.getMyear(),request.getDivCode(),request.getDepoCode()
				,request.getSmon(),request.getEmon(),request.getUtype(),request.getLoginId(),request.getRepType());
		return reportList;
		
	}

	
	
	private Map<String, Long> getNoOfRepMap(MktRepo1Request request)
	{
		Map<String, Long> fsMap=null;
		List<DashBoardData> fsData = null;
		fsData =mktRepo1Dao.getNoOfRep(request.getMyear(), request.getDivCode(), request.getDepoCode(),request.getSmon(), request.getEmon(),request.getUtype(),request.getLoginId());
		int sz1=fsData.size();
	    long totalFs=0;
		fsMap=new LinkedHashMap();
		for(int b=0;b<sz1;b++)
		{
			
			DashBoardData fs=fsData.get(b);
			fsMap.put(fs.getName(),fs.getVal());
			totalFs+=fs.getVal();
			System.out.println(fsMap.get(fs.getName())+" "+fs.getVal());
		}
		
		fsMap.put("Total", totalFs);
		return fsMap;
	}
	
	@Override
	public ApiResponse<MktRepo1Response> getMktRepo1(MktRepo1Request request) {


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
				response.setCode(pcode);
				response.setName(pname);
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
				if(columnTotal>0)
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
				response.setCode(mgrp);
				response.setName(gname);
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
				if(groupColumnTotal>0)
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
		response.setCode(pcode);
		response.setName(pname);
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
		if(columnTotal>0)
			saleList.add(response);

		
		branches=new LinkedHashMap();
		response=new MktRepo1Response();
		response.setCode(mgrp);
		response.setName(gname);
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
		if(groupColumnTotal>0)
		saleList.add(response);
		
		
		grandColumnTotal = total.values().stream().mapToLong(d -> d).sum();
		
		branches=new LinkedHashMap();
		total.put("TOTAL", grandColumnTotal);

		branches.putAll(total);
		response=new MktRepo1Response();
		response.setCode(0);
		response.setName("Grand Total");
		response.setPack("");
		response.setBranches(branches);
		response.setColor(2);
		saleList.add(response);

		return new ApiResponse<MktRepo1Response>(title.toString(),size,lupdate,saleList);
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
		System.out.println("size of gross list in ach menthod "+size);
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

				if(request.getOptType()==8)
				{
					if(request.getUv()==1)
						columnTotal=AppCalculationUtils.calculateAch(horizontalSalqty, horizontalTarqty);
						else
						columnTotal=AppCalculationUtils.calculateAch(horizontalSalval, horizontalTarval);
				}
				else if(request.getOptType()==9)
				{
					if(request.getUv()==1)
						columnTotal=AppCalculationUtils.calculateGth(horizontalSalqty, horizontalTarqty);
						else
						columnTotal=AppCalculationUtils.calculateGth(horizontalSalval, horizontalTarval);
				}
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
//					double sale = grVerticalSalval.get(bm.getDepo_name());
//					double target = grVerticalTarval.get(bm.getDepo_name());
					double sale = grVerticalSalval.get(bm.getDepo_name())==null?0:grVerticalSalval.get(bm.getDepo_name());
					double target = grVerticalTarval.get(bm.getDepo_name())==null?0:grVerticalTarval.get(bm.getDepo_name());
//					System.out.println("sale "+sale+" target "+target+" branch "+bm.getDepo_name()+" group "+gname+" ach "+AppCalculationUtils.calculateAch(sale, target));
////					gsale += grVerticalSalval.get(bm.getDepo_name());
////					gtarget += grVerticalTarval.get(bm.getDepo_name());
					if(request.getOptType()==8)
						group.put(bm.getDepo_name(), AppCalculationUtils.calculateAch(sale, target));
					else
						group.put(bm.getDepo_name(), AppCalculationUtils.calculateGth(sale, target));

				}

				if(request.getOptType()==8)
					groupColumnTotal=AppCalculationUtils.calculateAch(gsale, gtarget);
				else
					groupColumnTotal=AppCalculationUtils.calculateGth(gsale, gtarget);
					
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
		
		if(request.getOptType()==8)
		{
			if(request.getUv()==1)
					columnTotal=AppCalculationUtils.calculateAch(horizontalSalqty, horizontalTarqty);
				else
					columnTotal=AppCalculationUtils.calculateAch(horizontalSalval, horizontalTarval);
		}

		else if(request.getOptType()==9)
		{
			if(request.getUv()==1)
				columnTotal=AppCalculationUtils.calculateGth(horizontalSalqty, horizontalTarqty);
				else
				columnTotal=AppCalculationUtils.calculateGth(horizontalSalval, horizontalTarval);
		}

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
			double sale = grVerticalSalval.get(bm.getDepo_name());
			double target = grVerticalTarval.get(bm.getDepo_name());
			gsale += grVerticalSalval.get(bm.getDepo_name());
			gtarget += grVerticalTarval.get(bm.getDepo_name());
			if(request.getOptType()==8)
				group.put(bm.getDepo_name(), AppCalculationUtils.calculateAch(sale, target));
			else
				group.put(bm.getDepo_name(), AppCalculationUtils.calculateGth(sale, target));
				
		}

		if(request.getOptType()==8)
			groupColumnTotal=AppCalculationUtils.calculateAch(gsale, gtarget);
		else	
			groupColumnTotal=AppCalculationUtils.calculateGth(gsale, gtarget);

			group.put("TOTAL", groupColumnTotal);

		branches.putAll(group);
		response.setBranches(branches);
		response.setColor(1);
		saleList.add(response);
		
		
		
		branches=new LinkedHashMap();

		
		for(int b=0;b<sz;b++)
		{
			BranchMasterDto bm=branchData.get(b);
			double sale =0;
			double target=0;
			System.out.println("bm ki value "+bm.getDepo_name());
			System.out.println(bottomVerticalSalval.get(bm.getDepo_name()));
			if(bottomVerticalSalval.get(bm.getDepo_name())==null)
				System.out.println(bottomVerticalSalval.get(bm.getDepo_name()));
			else
			{
				sale = bottomVerticalSalval.get(bm.getDepo_name());
				gsale += bottomVerticalSalval.get(bm.getDepo_name());

			}
			if(bottomVerticalTarval.get(bm.getDepo_name())==null)
				System.out.println(bottomVerticalTarval.get(bm.getDepo_name()));
			else
			{
				target = bottomVerticalTarval.get(bm.getDepo_name());
				gtarget += bottomVerticalTarval.get(bm.getDepo_name());
			}
			if(request.getOptType()==8)
				total.put(bm.getDepo_name(), AppCalculationUtils.calculateAch(sale, target));
			else
				total.put(bm.getDepo_name(), AppCalculationUtils.calculateGth(sale, target));
			
		}

		if(request.getOptType()==8)
			grandColumnTotal=AppCalculationUtils.calculateAch(gsale, gtarget);
		else	
			grandColumnTotal=AppCalculationUtils.calculateGth(gsale, gtarget);

		
		
		
		total.put("TOTAL", grandColumnTotal);

		branches.putAll(total);
		response=new MktRepo1AchResponse();
		response.setPcode(0);
		response.setPname("Grand Total");
		response.setPack("");
		response.setBranches(branches);
		response.setColor(2);
		saleList.add(response);

		return new ApiResponse<MktRepo1AchResponse>(title.toString(),size,lupdate,saleList);
	}

	@Override
	public ApiResponse<MktRepo1Response> getMktRepo1Pmr(MktRepo1Request request) {
		
		logger.info(AristoWebLogMsgConstant.MKT_REPORT_SERVICE_01,"getMktRepo1");
		List<BranchMasterDto> branchData = getBranchData(request);
		int sz=branchData.size();
		int k=0;
		int z=0;
		String title=null;
		
		long gfs=0;
		List<MktRepo1> grossSaleList=getReportPMR(request);

		Map<String, Long> fsMap=null;
		fsMap=getNoOfRepMap(request);
		
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
		long fs=0;
		System.out.println("size of gross list in pmr menthod "+size);
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
				grVerticalSalval =new LinkedHashMap();
				grVerticalTarval=new LinkedHashMap();
				bottomVerticalSalval =new LinkedHashMap();
				bottomVerticalTarval =new LinkedHashMap();
				first=false;
				
				title = getTitle(request, data); 
				
			}
			if(pcode!=data.getMcode())
			{
				response.setCode(pcode);
				response.setName(pname);
				response.setPack(pack);
				z=k;
				for(int b=k;b<sz;b++)
				{
					BranchMasterDto bm=branchData.get(b);
					branches.put(bm.getDepo_name(), 0L);
					k++;
				}
				
				fs = fsMap.get("Total");
				
				
					if(request.getUv()==1)
						columnTotal=AppCalculationUtils.calculatePmr(horizontalSalqty, fs);
					else
						columnTotal=AppCalculationUtils.calculatePmr(horizontalSalval, fs);
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
				response=new MktRepo1Response();
				branches=new LinkedHashMap();

			}

			if(mgrp!=data.getMgrp())
			{
				
				response.setCode(mgrp);
				response.setName(gname);
				response.setPack("");
				System.out.println("value of z and sz "+z+" "+sz+" "+gname);
				
				for(int b=z;b<sz;b++)
				{
					BranchMasterDto bm=branchData.get(b);
					group.put(bm.getDepo_name(), 0L);
					z++;
				}


				for(int b=0;b<sz;b++)
				{
					BranchMasterDto bm=branchData.get(b);
//					logger.info(grVerticalSalval.get(bm.getDepo_name())+" "+bm.getDepo_name()+" gname "+gname);
					long sale = grVerticalSalval.get(bm.getDepo_name())==null?0:grVerticalSalval.get(bm.getDepo_name());
					fs = fsMap.get(bm.getDepo_name());
					gsale += sale;
					group.put(bm.getDepo_name(), AppCalculationUtils.calculatePmr(sale, fs));

				}

				groupColumnTotal=AppCalculationUtils.calculatePmr(gsale, fs);
				
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
				response=new MktRepo1Response();
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
						fs = fsMap.get(bm.getDepo_name());
						System.out.println("value of fs "+fs+" depo_name "+bm.getDepo_name()+" value "+data.getSales_val());
						if(fs!=0)
						 branches.put(data.getDepo_name(), request.getUv()==2?AppCalculationUtils.calculatePmr(data.getSales_val(),fs):AppCalculationUtils.calculatePmr(data.getSales(),fs));
						else
							branches.put(data.getDepo_name(), 0L);							
					
	        		 horizontalSalqty+= data.getSales();
	        		 horizontalSalval+= data.getSales_val();
	        		 
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

//					System.out.println(grVerticalSalval.get(data.getDepo_name())+" "+data.getDepo_name());
					
					if(bottomVerticalSalval.containsKey(data.getDepo_name()))
					{
						long ggval = bottomVerticalSalval.get(data.getDepo_name())+data.getSales_val();
						bottomVerticalSalval.put(data.getDepo_name(), ggval);
					}
					else
					{
						bottomVerticalSalval.put(data.getDepo_name(), data.getSales_val());
					}

					k++;
					break;
				}
				else
				{
					branches.put(bm.getDepo_name(), 0L);

					if(grVerticalSalval.containsKey(bm.getDepo_name()))
					{
						// do nothing
					}
					else
					{
						grVerticalSalval.put(bm.getDepo_name(), 0L);

					}

					if(bottomVerticalSalval.containsKey(bm.getDepo_name()))
					{
						// do nothing
					}
					else
					{
						bottomVerticalSalval.put(bm.getDepo_name(), 0L);

					}

					
					k++;
				}
			}
			
		}

		
		
		response=new MktRepo1Response();
		response.setCode(pcode);
		response.setName(pname);
		response.setPack(pack);
		z=k;
		for(int b=k;b<sz;b++)
		{
			BranchMasterDto bm=branchData.get(b);
			branches.put(bm.getDepo_name(), 0L);
			k++;
		}
		
			if(request.getUv()==1)
					columnTotal=AppCalculationUtils.calculatePmr(horizontalSalqty, fs);
				else
					columnTotal=AppCalculationUtils.calculatePmr(horizontalSalval, fs);


		branches.put("TOTAL", columnTotal);

		response.setBranches(branches);
		saleList.add(response);

		
		branches=new LinkedHashMap();
		response=new MktRepo1Response();
		response.setCode(mgrp);
		response.setName(gname);
		response.setPack("");
		for(int b=z;b<sz;b++)
		{
			BranchMasterDto bm=branchData.get(b);
			group.put(bm.getDepo_name(), 0L);
			z++;
		}

		for(int b=0;b<sz;b++)
		{
			BranchMasterDto bm=branchData.get(b);
			long sale = grVerticalSalval.get(bm.getDepo_name());
			gsale += grVerticalSalval.get(bm.getDepo_name());
			fs = fsMap.get(bm.getDepo_name());
			group.put(bm.getDepo_name(), AppCalculationUtils.calculatePmr(sale, fs));
				
		}

			groupColumnTotal=AppCalculationUtils.calculatePmr(gsale, fs);

			group.put("TOTAL", groupColumnTotal);

		branches.putAll(group);
		response.setBranches(branches);
		response.setColor(1);
		saleList.add(response);
		
		
		
		branches=new LinkedHashMap();

		
		gsale=0;
		for(int b=0;b<sz;b++)
		{
			BranchMasterDto bm=branchData.get(b);
			long sale = bottomVerticalSalval.get(bm.getDepo_name());
			fs = fsMap.get(bm.getDepo_name());
			gsale += bottomVerticalSalval.get(bm.getDepo_name());
			total.put(bm.getDepo_name(), AppCalculationUtils.calculatePmr(sale, fs));
			
		}

		fs = fsMap.get("Total");
		grandColumnTotal=AppCalculationUtils.calculatePmr(gsale, fs);

		
		System.out.println("total fs "+fs);
		
		total.put("TOTAL", grandColumnTotal);

		branches.putAll(total);
		response=new MktRepo1Response();
		response.setCode(0);
		response.setName("Grand Total");
		response.setPack("");
		response.setBranches(branches);
		response.setColor(2);
		saleList.add(response);

		return new ApiResponse<MktRepo1Response>(title.toString(),size,lupdate,saleList);
	}

	private String getTitle(MktRepo1Request request,MtbStockiestDto data)
	{
		StringBuilder title=new StringBuilder();
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

		title.append("All India Stockiest/Invoicewise MTB Report from  ");
		title.append(data.getSmname());
		title.append(" To ");
		title.append(data.getEmname());
		return title.toString();

	}


	
	
	@Override
	public ApiResponse<MtbStockiestResponse> getMtbStockiestReport(MktRepo1Request request) {
		
		logger.info(AristoWebLogMsgConstant.MTB_STOCKIEST_SERVICE,"getMtbStockiestReport");
		List<MtbStockiestDto> mtbstockiestList=null;
		int size = 0;
		mtbstockiestList=mktRepo1Dao.getWebStockiestMtb(request.getMyear(),request.getSmon(),request.getEmon(),request.getLoginId());
		size=mtbstockiestList.size();
		logger.info("size of the data is {}",size);
		
		MtbStockiestResponse response=null;
		List<MtbStockiestResponse> saleList = new ArrayList();
		
		boolean first=true;
		String title=null;
		
		for (int i=0;i<size;i++)
		{
			MtbStockiestDto data = mtbstockiestList.get(i);
			
			
			if(first)
			{
				title = getTitle(request, data);
				
				first=false;
			}
			
			response=new MtbStockiestResponse();
			response.setDivName(data.getDiv_name());
			response.setDepoName(data.getDepo_name());
			response.setStockiestName(data.getCust_name());
			response.setCity(data.getMcity());
			response.setGstNo(data.getGst_no());
			response.setPanNo(data.getPan_no());
			response.setProductName(data.getPname());
			response.setInvoiceNo(data.getSinv_no());
			response.setSapDocNo(data.getSap_doc_no());
			response.setInvoiceDate(data.getSinv_dt());
			response.setBatchNo(data.getSbatch_no());
			response.setQuantity(data.getSaleqty());
			response.setSchemeQty(data.getScheme_qty());
			response.setValue(data.getSalesval());
			response.setPaymentTag(data.getPay_tag());
			saleList.add(response);

		} //end of for loop


		ApiResponse<MtbStockiestResponse> apiResponse = new ApiResponse<>(title!=null?title.toString():"", size,saleList);
		return apiResponse;

	}


}
