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
import com.aristowebapi.dao.SampleSm01Dao;
import com.aristowebapi.dto.BranchMasterDto;
import com.aristowebapi.dto.BranchMisRepo8;
import com.aristowebapi.dto.MktRepo1;
import com.aristowebapi.dto.MonthDto;
import com.aristowebapi.dto.SampleSalesRatioDto;
import com.aristowebapi.request.SampleSm01Request;
import com.aristowebapi.response.ApiResponse;
import com.aristowebapi.response.SampleSm01Response;
import com.aristowebapi.response.SampleSm02Response;
import com.aristowebapi.response.SampleSm03Response;
import com.aristowebapi.service.SampleSm01Service;

@Service
public class SampleSm01ServiceImpl implements SampleSm01Service {

	Logger logger = LoggerFactory.getLogger(SampleSm01ServiceImpl.class);
	@Autowired
	private SampleSm01Dao sampleSm01Dao;
	
	@Autowired
	private AristoWebMessageConstant  aristoWebMessageConstant;
	
	private List<BranchMasterDto> getBranchData(SampleSm01Request request)
	{
		List<BranchMasterDto> branchData = null;
		
		branchData =sampleSm01Dao.getAllBranch();
		
		System.out.println("size of branchata is ****** "+branchData.size());
		return branchData;
		
	}

	
	private String getTitle(SampleSm01Request request,MktRepo1 data)
	{
		
		
		StringBuilder title=new StringBuilder();
		title.append(aristoWebMessageConstant.divisionMap.get(String.valueOf(data.getDiv_code())));
		if(request.getRepType()==1)
			title.append("All India Productwise Sample Isuue to FS Report for the Month of ");
		if(request.getRepType()==2)
			title.append("All India Productwise Sample Allocation Report for the Month of ");
		if(request.getRepType()==3)
			title.append("All India Productwise Sample Allocation/Issue  Report for the Month of ");
		
		title.append(data.getSmname());
		title.append(" To ");
		title.append(data.getEmname());
		return title.toString();

	}

	private List<MktRepo1> getReport(SampleSm01Request request)
	{
		List<MktRepo1> reportList = null;
		reportList=sampleSm01Dao.getWebSampleSm01(request.getMyear(),request.getDivCode(),request.getDepoCode()
				,request.getSmon(),request.getEmon(),request.getUtype(),request.getLoginId(),request.getRepType(),request.getHqCode());

		System.out.println("reportlst ka size "+reportList.size());
		return reportList;
		
	}

	
	@Override
	public ApiResponse<SampleSm01Response> getSampleSm01(SampleSm01Request request) {

		
		
		logger.info(AristoWebLogMsgConstant.SAMPLE_REPORT_SERVICE_01,"getSampleSm01");
		if(request.getRepType()>1)
			request.setDepoCode(0);
		List<BranchMasterDto> branchData=null;
		if (request.getDepoCode()==0)
			branchData = getBranchData(request);
		else if (request.getHqCode()>0)
			branchData = sampleSm01Dao.getAllRm(request.getMyear(),request.getDivCode(),request.getDepoCode(),request.getHqCode(),request.getSmon(),request.getEmon());
		else
			branchData = sampleSm01Dao.getAllHq(request.getMyear(),request.getDivCode(),request.getDepoCode());

		
		int sz=branchData.size();
		int k=0;
		int z=0;
		String title=null;
		
		System.out.println("size of branch data is "+sz);
		
		List<MktRepo1> grossSaleList=getReport(request);

		
		
		SampleSm01Response response=null;
		List<SampleSm01Response> saleList = new ArrayList();
		Map<String, Long> branches=null;
		Map<String, Long> total=null;
		boolean first=true;
		int pcode=0;
		int mgrp=0;
		long columnTotal=0;
		long grandColumnTotal=0;
		int doctype=0;
		String pname=null;
		String pack=null;
		int size = grossSaleList.size();
		
		System.out.println("size of list is "+size);
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
				doctype=data.getDoc_type();
				response=new SampleSm01Response();
				branches=new LinkedHashMap();
				total=new LinkedHashMap();
				first=false;
				
				title = getTitle(request, data); 
				
			}
			if(pcode!=data.getMcode() || doctype!=data.getDoc_type() )
			{
				
				response.setDataType(doctype==1?"FS":doctype==0?"Allocation":"Issue");
				response.setCode(pcode);
				response.setName(pname);
				response.setColor(doctype==0?1:0);
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
				doctype=data.getDoc_type();
				columnTotal=0;
				
				k=0;
				response=new SampleSm01Response();
				branches=new LinkedHashMap();

			}

			
			// before put please check depo code in branch list if not found put 0 value in map otherwise actual zero
			for(int b=k;b<sz;b++)
			{
				BranchMasterDto bm=branchData.get(b);
				if(bm.getDepo_code()==data.getDepo_code())
				{
					
					branches.put(data.getDepo_name(), request.getUv()==2?data.getSales_val():data.getSales());
					columnTotal+=request.getUv()==2?data.getSales_val():data.getSales();

					//grandColumnTotal+=data.getSales_val();
					if(total.containsKey(data.getDepo_name()))
					{
						long ggval = total.get(data.getDepo_name())+request.getUv()==2?data.getSales_val():data.getSales();
						total.put(data.getDepo_name(), ggval);
					}
					else
					{
						total.put(data.getDepo_name(), request.getUv()==2?data.getSales_val():data.getSales());
					}

					k++;
					break;
				}
				else
				{
					branches.put(bm.getDepo_name(), 0L);
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

		
		
		response=new SampleSm01Response();
		response.setCode(pcode);
		response.setName(pname);
		response.setDataType(doctype==1?"FS":doctype==0?"Allocation":"Issue");
		response.setColor(doctype==0?1:0);
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

		
		
		
		grandColumnTotal = total.values().stream().mapToLong(d -> d).sum();
		
		branches=new LinkedHashMap();
		total.put("TOTAL", grandColumnTotal);

		branches.putAll(total);
		response=new SampleSm01Response();
		response.setCode(0);
		response.setName("Grand Total");
		response.setDataType("");
			response.setBranches(branches);
		response.setColor(2);
		saleList.add(response);

		return new ApiResponse<SampleSm01Response>(title.toString(),size,saleList);

	}


	


	private String getTitle(SampleSm01Request request,BranchMisRepo8 data)
	{

		
		StringBuilder title=new StringBuilder();
		title.append(" PRODUCT -> : ");
		title.append(data.getPname());
		title.append(" ");
		title.append(" BRANCH WISE ");
		title.append(request.getUv()==1?" UNIT ISSUE TO FS TREND ":" VALUE ISSUE TO FS TREND FROM ");
		title.append(data.getSmname());
		title.append(" To ");
		title.append(data.getEmname());

		return title.toString();

	}


	
	@Override
	public ApiResponse<SampleSm02Response> getSampleSm02(SampleSm01Request request) {
		logger.info(AristoWebLogMsgConstant.SAMPLE_REPORT_SERVICE_01,"getSampleSm02");


		List<MonthDto> monthData = sampleSm01Dao.getAllMonth(request.getMyear());
		int sz=monthData.size();
		int k=0;
		int z=0;
		sz=request.getEmon();

		String title=null;
		List<BranchMisRepo8> SampleSm02List=null;
		int size = 0;
		
		if(request.getDepoCode()>0)
			request.setRepType(0);
		
		SampleSm02List=sampleSm01Dao.getSampleSm02(request.getMyear(),request.getDivCode(),request.getDepoCode(),request.getSmon(),request.getEmon(),request.getLoginId(),request.getUtype(),request.getCode(),request.getRepType());

		size = SampleSm02List.size();
		logger.info("size of the data is {}",size);

		
		SampleSm02Response response=null;

		Map<String, Long> months=null;
		Map<String, Long> total=null;
		
		List<SampleSm02Response> saleList = new ArrayList();

		
		long columnTotal=0;
		long columnTotalVal=0;
		long grandColumnTotal=0;
		long grandColumnTotalVal=0;
		long vgrandColumnTotal=0;

		boolean first=true;
		int depo_code=0;
		int ter_code=0;
		String name="";
		String ter_name="";
		int fs=0;
		int gfs=0;
		String pcode="";
		String branch="";
		
		for (int i=0;i<size;i++)
		{
			BranchMisRepo8 data = SampleSm02List.get(i);
			
			if(first)
			{
				response=new SampleSm02Response();
				ter_code=data.getTerr_cd();
				ter_name=data.getTer_name();
				branch=data.getDepo_name();
				months=new LinkedHashMap();
				total=new LinkedHashMap();
				first=false;
				
				title = getTitle(request, data); 
			}


			
			if(!ter_name.trim().equalsIgnoreCase(data.getTer_name().trim()))
			{
				
				response.setBranch(branch);
				if(request.getDepoCode()>0 || request.getRepType()==1)
					response.setHqName(ter_name);
				else
					response.setHqName("");
					
			
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
				
				
				saleList.add(response);
				ter_code=data.getTerr_cd();
				ter_name=data.getTer_name();
				branch=data.getDepo_name();
				columnTotal=0;
				columnTotalVal=0;
				gfs+=fs;
				k=0;
				fs=0;
				response=new SampleSm02Response();
				months=new LinkedHashMap();

			}

			
		
			// before put please check depo code in branch list if not found put 0 value in map otherwise actual zero
			for(int b=k;b<sz;b++)
			{
				MonthDto mn=monthData.get(b);
				
				if(mn.getMnth_code()==data.getMnth_code())
				{
					fs+=data.getFs();
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

					

///					
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
			response=new SampleSm02Response();
			response.setBranch(branch);
			if(request.getDepoCode()>0 || request.getRepType()==1)
				response.setHqName(ter_name);
			else
				response.setHqName("");

			gfs+=fs;
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
			saleList.add(response);

			
			
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



			months.putAll(total);
			response=new SampleSm02Response();
			response.setBranch(request.getDepoCode()==0?"All India":branch);
			response.setHqName("");
			response.setMonths(months);
			response.setColor(2);
			saleList.add(response);		
		}
		return new ApiResponse<SampleSm02Response>(title!=null?title.toString():"",size,saleList);

	}

	private String getTitle(SampleSm01Request request,SampleSalesRatioDto data)
	{
		
		
		StringBuilder title=new StringBuilder();
		title.append(data.getDiv_name());
		title.append(" - ");
		title.append(data.getDepo_name());
		title.append(" Productwise Sales/Sample Ratio for the Month of ");
		title.append(data.getSmname());
		title.append(" To ");
		title.append(data.getEmname());
		return title.toString();

	}
	
	@Override
	public ApiResponse<SampleSm03Response> getSampleSm03(SampleSm01Request request) {
		logger.info(AristoWebLogMsgConstant.SAMPLE_REPORT_SERVICE_01,"getSampleSm03");


		

		String title=null;
		List<SampleSalesRatioDto> SampleSm03List=null;
		int size = 0;
		
		SampleSm03List=sampleSm01Dao.getSampleSm03(request.getMyear(),request.getDivCode(),request.getDepoCode(),request.getSmon(),request.getEmon());

		size = SampleSm03List.size();
		logger.info("size of the data is {}",size);

		
		SampleSm03Response response=null;

		
		List<SampleSm03Response> saleList = new ArrayList();

		

		boolean first=true;
		for (int i=0;i<size;i++)
		{
			SampleSalesRatioDto data = SampleSm03List.get(i);
			
			if(first)
			{
				
				first=false;
				
				title = getTitle(request, data); 
			}

				response=new SampleSm03Response();
				response.setGroupName(data.getGroup_name());
				response.setProductName(data.getProduct_name());
				response.setSalesValue(data.getSales_value());
				response.setSampleValue(data.getSample_value());
				response.setRatio(data.getRatio());
				saleList.add(response);

			}
			
		return new ApiResponse<SampleSm03Response>(title!=null?title.toString():"",size,saleList);
	}
}
