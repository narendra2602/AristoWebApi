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
import com.aristowebapi.dao.BranchMisDao;
import com.aristowebapi.dto.BranchMisRepo5;
import com.aristowebapi.dto.BranchMisRepo6;
import com.aristowebapi.dto.BranchMisRepo8;
import com.aristowebapi.dto.MonthDto;
import com.aristowebapi.exception.ApiException;
import com.aristowebapi.request.BranchMisRepo5Request;
import com.aristowebapi.request.BranchMisRepo6Request;
import com.aristowebapi.request.BranchMisRepo8Request;
import com.aristowebapi.response.ApiResponse;
import com.aristowebapi.response.BranchMisRepo5Response;
import com.aristowebapi.response.BranchMisRepo6Response;
import com.aristowebapi.response.BranchMisRepo8Response;
import com.aristowebapi.service.BranchMisservice;
import com.aristowebapi.utility.AppCalculationUtils;

@Service
public class BranchMisServiceImpl implements BranchMisservice{

	Logger logger = LoggerFactory.getLogger(BranchMisServiceImpl.class);
	@Autowired
	private BranchMisDao branchMisDao;
	
	@Autowired
	private AristoWebMessageConstant  aristoWebMessageConstant;
	
	
	private String getTitle(BranchMisRepo5Request request,BranchMisRepo5 data)
	{
		StringBuilder title=new StringBuilder();
		title.append(aristoWebMessageConstant.divisionMap.get(String.valueOf(data.getDiv_code())));
		title.append(" H.Q. WISE/GROSS/CREDIT/NET SALE FROM ");
		title.append(data.getSmname());
		title.append(" To ");
		title.append(data.getEmname());
		return title.toString();

	}
	
	
	@Override
	public ApiResponse<BranchMisRepo5Response> getBranchMisRepo5(BranchMisRepo5Request request) {
		
		logger.info(AristoWebLogMsgConstant.BRANCH_MIS_REPORT_5_SERVICE,"getBranchMisRepo5");
		List<BranchMisRepo5> BranchMisRepo5List=null;
		int size = 0;
		try {
			BranchMisRepo5List=branchMisDao.getBranchMisRepo5(request.getMyear(),request.getDivCode(),request.getDepoCode(),request.getSmon(),request.getEmon(),request.getRepType(),request.getLoginId(),request.getUtype(),request.getCode());
			size = BranchMisRepo5List.size();
			logger.info("size of the data is {}",size);
			
			
/*			if(size==0)
				throw new DataNotFoundException(AristoWebLogMsgConstant.MKT_REPORT_SERVICE_022);
*/				
		
		BranchMisRepo5Response response=null;
		List<BranchMisRepo5Response> saleList = new ArrayList();

		boolean first=true;
		String title=null;
		int reg_code=0;
		int area_code=0;
		String reg_name="";
		String area_name="";
		String name="";
		long budget=0;
		long gross=0;
		long credit=0;
		long net=0;
		long abudget=0;
		long agross=0;
		long acredit=0;
		long anet=0;

		long gbudget=0;
		long ggross=0;
		long gcredit=0;
		long gnet=0;
		for (int i=0;i<size;i++)
		{
			BranchMisRepo5 data = BranchMisRepo5List.get(i);
			
			
			if(first)
			{
				title = getTitle(request, data);
				reg_code=data.getReg_cd();
				area_code=data.getArea_cd();
				reg_name=data.getReg_name();
				area_name=data.getArea_name();
				name=data.getDepo_name();
				first=false;
			}
			
			if(reg_code!=data.getReg_cd())
			{
				
				saleList.add(getResponse(reg_name,budget,gross,credit,net,1));
				reg_code=data.getReg_cd();
				reg_name=data.getReg_name();
				budget=0;
				gross=0;
				credit=0;
				net=0;
				
			}

			if(area_code!=data.getArea_cd())
			{
				
				saleList.add(getResponse(area_name,abudget,agross,acredit,anet,2));
				area_code=data.getArea_cd();
				area_name=data.getArea_name();
				abudget=0;
				agross=0;
				acredit=0;
				anet=0;
				
			}

			saleList.add(getResponse(data.getTer_name(),data.getBudget(),data.getGross(),data.getCredit(),data.getNet(),0));

	    	
	    	budget+=data.getBudget();
	    	gross+=data.getGross();
	    	credit+=data.getCredit();
	    	net+=data.getNet();
	    	abudget+=data.getBudget();
	    	agross+=data.getGross();
	    	acredit+=data.getCredit();
	    	anet+=data.getNet();

	    	gbudget+=data.getBudget();
	    	ggross+=data.getGross();
	    	gcredit+=data.getCredit();
	    	gnet+=data.getNet();
	    	
		} //end of for loop

		saleList.add(getResponse(reg_name,budget,gross,credit,net,1));

		saleList.add(getResponse(area_name,abudget,agross,acredit,anet,2));

		saleList.add(getResponse(name+" Branch",gbudget,ggross,gcredit,gnet,3));

		
		ApiResponse<BranchMisRepo5Response> apiResponse = new ApiResponse<>(title!=null?title.toString():"", size,saleList);
		return apiResponse;
		
		} catch (Exception e) {
			logger.error(AristoWebLogMsgConstant.BRANCH_MIS_REPORT_5_SERVICE_021,"getBranchMisRepo5");
			throw new ApiException(e.getMessage());
		}
		

	}


	private BranchMisRepo5Response getResponse(String name,long budget,long gross,long credit,long net, int color)
	{
		BranchMisRepo5Response response=new BranchMisRepo5Response();
		response.setName(name);;

/*		response.setBudget(AppCalculationUtils.roundToDecimal2Places(budget));
		response.setGross(AppCalculationUtils.roundToDecimal2Places(gross));
		response.setCredit(AppCalculationUtils.roundToDecimal2Places(credit));
		response.setNet(AppCalculationUtils.roundToDecimal2Places(net));
		response.setSurdef(AppCalculationUtils.roundToDecimal2Places(net-budget));
*/
		response.setBudget(budget);
		response.setGross(gross);
		response.setCredit(credit);
		response.setNet(net);
		response.setAch(AppCalculationUtils.calculateAch(net, budget));
		response.setSurdef(net-budget);

		response.setColor(color);
		return response;

	}

	
	
	private String getTitle(BranchMisRepo6Request request,BranchMisRepo6 data)
	{
		StringBuilder title=new StringBuilder();
		title.append("PRODUCT - > ");
		title.append(data.getPname());
		title.append(request.getRepType()==1?" H.Q.WISE ":request.getRepType()==2?" REGION WISE ":request.getRepType()==3?" AREA WISE ":" BRANCH WISE ");
		title.append("SALES DETAIL FROM ");
		title.append(data.getSmname());
		title.append(" TO ");
		title.append(data.getEmname());
		return title.toString();

	}


	@Override
	public ApiResponse<BranchMisRepo6Response> getBranchMisRepo6(BranchMisRepo6Request request) {
		logger.info(AristoWebLogMsgConstant.BRANCH_MIS_REPORT_6_SERVICE,"getBranchMisRepo6");
		List<BranchMisRepo6> BranchMisRepo6List=null;
		int size = 0;
		
		try {
			BranchMisRepo6List=branchMisDao.getBranchMisRepo6(request.getMyear(),request.getDivCode(),request.getDepoCode(),request.getSmon(),request.getEmon(),request.getRepType(),request.getLoginId(),request.getUtype(),request.getCode());
			size = BranchMisRepo6List.size();
			logger.info("size of the data is {}",size);
			
			System.out.println(request.getMyear()+" "+request.getDivCode()+" "+request.getDepoCode()+" "+request.getSmon()+" "+request.getEmon()+" "+request.getRepType()+" "+request.getLoginId()+" "+request.getUtype()+" "+request.getCode());
			
			
/*			if(size==0)
				throw new DataNotFoundException(AristoWebLogMsgConstant.MKT_REPORT_SERVICE_022);
*/				
		
		BranchMisRepo6Response response=null;
		List<BranchMisRepo6Response> saleList = new ArrayList();

		boolean first=true;
		String title=null;
    	int gsaleqty=0;
    	double gsaleval=0.00;
    	int gpisaleqty=0;
    	double gpisaleval=0.00;
    	int ggrosssaleqty=0;
    	double ggrosssaleval=0.00;

    	int  gsalableqty=0;
    	double gsalableval=0.00;
    	int  gexpqty=0;
    	double gexpval=0.00;
    	int  gbrkqty=0;
    	double gbrkval=0.00;

    	int  gothqty=0;
    	double gothval=0.00;

    	double gpdval=0.00;

    	int gnetqty=0;
    	double gnetval=0.00;

		for (int i=0;i<size;i++)
		{
			BranchMisRepo6 data = BranchMisRepo6List.get(i);
			
			
			if(first)
			{
				title = getTitle(request, data);
				first=false;
			}
			
			response=new BranchMisRepo6Response();

			response.setName(data.getName());
	    	response.setInvSaleQty(data.getSaleqty());
	    	response.setInvSaleVal(data.getSalesval());

			response.setPiSaleQty(data.getSaleqty());
	    	response.setPiSaleVal(data.getSalesval());

			response.setGrossSaleQty(data.getGross_sale_qty());
	    	response.setGrossSaleVal(data.getGross_sale_val());

	    	response.setSalableQty(data.getSalableqty());
	    	response.setSalableVal(data.getSalableval());

	    	response.setExpQty(data.getExpqty());
	    	response.setExpVal(data.getExpval());

	    	response.setBrkQty(data.getBrkqty());
	    	response.setBrkVal(data.getBrkval());

	    	response.setOthQty(data.getOthqty());
	    	response.setOthVal(data.getOthval());

	    	response.setPdVal(data.getPdval());
	    	
	    	response.setNetQty(data.getNetqty());
	    	response.setNetVal(data.getNetval());

	    	saleList.add(response);

	    	gsaleqty+=data.getSaleqty();
	    	gsaleval+=data.getSalesval();
	    	gpisaleqty+=data.getPisaleqty();
	    	gpisaleval+=data.getPisalesval();

	    	ggrosssaleqty+=data.getGross_sale_qty();
	    	ggrosssaleval+=data.getGross_sale_val();

	    	
	    	gsalableqty+=data.getSalableqty();
	    	gsalableval+=data.getSalableval();
	    	gexpqty+=data.getExpqty();
	    	gexpval+=data.getExpval();
	    	gbrkqty+=data.getBrkqty();
	    	gbrkval+=data.getBrkval();
	    	gothqty+=data.getOthqty();
	    	gothval+=data.getOthval();

	    	
	    	gnetqty+=data.getNetqty();
	    	gnetval+=data.getNetval();

	    	
	    	
		} //end of for loop

		if(!first)
		{

			response=new BranchMisRepo6Response();
			response.setName("GRAND TOTAL");
	    	response.setInvSaleQty(gsaleqty);
	    	response.setInvSaleVal(Math.round(gsaleval));

	    	response.setPiSaleQty(gpisaleqty);
	    	response.setPiSaleVal(Math.round(gpisaleval));

			response.setGrossSaleQty(ggrosssaleqty);
	    	response.setGrossSaleVal(Math.round(ggrosssaleval));

	    	
	    	response.setSalableQty(gsalableqty);
	    	response.setSalableVal(Math.round(gsalableval));

	    	response.setExpQty(gexpqty);
	    	response.setExpVal(Math.round(gexpval));

	    	response.setBrkQty(gbrkqty);
	    	response.setBrkVal(Math.round(gbrkval));

	    	response.setOthQty(gothqty);
	    	response.setOthVal(Math.round(gothval));
	    	response.setPdVal(Math.round(gpdval));

	    	
	    	response.setNetQty(gnetqty);
	    	response.setNetVal(Math.round(gnetval));

			response.setColor(2);

			saleList.add(response);
		}



		
		ApiResponse<BranchMisRepo6Response> apiResponse = new ApiResponse<>(title!=null?title.toString():"", size,saleList);
		return apiResponse;
		
		} catch (Exception e) {
			logger.error(AristoWebLogMsgConstant.BRANCH_MIS_REPORT_6_SERVICE_021,"getBranchMisRepo6");
			throw new ApiException(e.getMessage());
		}
		

}


	private String getTitle(BranchMisRepo8Request request,BranchMisRepo8 data)
	{

		
		StringBuilder title=new StringBuilder();
		title.append(" PRODUCT -> : ");
		title.append(data.getPname());
		title.append(" ");
		title.append(request.getOpt()==1?" - H.Q. WISE ":request.getOpt()==2?" REGION WISE ":request.getOpt()==3?" AREA WISE ":" BRANCH WISE ");
		title.append(request.getUv()==1?" UNIT SALES TREND ":" VALUE SALES TREND ");
		switch(request.getRepType())
		{
		    case 1:	title.append("(GROSS) FROM ");
		            break;
		    case 2: title.append("(Total Credit) FROM ");
		    		break;
		    case 3: title.append("(EXPIRY) FROM ");
		    		break;
		    case 4: title.append("(SALABLE) FROM ");
		    		break;
		    case 5: title.append("(BRK/SPOILED) FROM ");
		    		break;
		    case 6: title.append("(NET) FROM ");
		}

		title.append(data.getSmname());
		title.append(" To ");
		title.append(data.getEmname());

		return title.toString();

	}

	
	@Override
	public ApiResponse<BranchMisRepo8Response> getBranchMisRepo8(BranchMisRepo8Request request) {
		logger.info(AristoWebLogMsgConstant.STK_REPO10_SERVICE,"getStkRepo10");


		List<MonthDto> monthData = branchMisDao.getAllMonth(request.getMyear());
		int sz=monthData.size();
		int k=0;
		int z=0;
		sz=request.getEmon();

		
		String title=null;
		List<BranchMisRepo8> BranchMisRepo8List=null;
		int size = 0;

		if(request.getOpt()==1) // hqwise
			BranchMisRepo8List=branchMisDao.getBranchMisRepo8HQ(request.getMyear(),request.getDivCode(),request.getDepoCode(),request.getSmon(),request.getEmon(),request.getRepType(),request.getLoginId(),request.getUtype(),request.getCode());
		else if(request.getOpt()==2) // region
			BranchMisRepo8List=branchMisDao.getBranchMisRepo8Region(request.getMyear(),request.getDivCode(),request.getDepoCode(),request.getSmon(),request.getEmon(),request.getRepType(),request.getLoginId(),request.getUtype(),request.getCode());
		else if(request.getOpt()==3) // area
			BranchMisRepo8List=branchMisDao.getBranchMisRepo8Area(request.getMyear(),request.getDivCode(),request.getDepoCode(),request.getSmon(),request.getEmon(),request.getRepType(),request.getLoginId(),request.getUtype(),request.getCode());
		else if(request.getOpt()==4) // branch
			BranchMisRepo8List=branchMisDao.getBranchMisRepo8Branch(request.getMyear(),request.getDivCode(),request.getDepoCode(),request.getSmon(),request.getEmon(),request.getRepType(),request.getLoginId(),request.getUtype(),request.getCode());

		size = BranchMisRepo8List.size();
		logger.info("size of the data is {}",size);

		
		BranchMisRepo8Response response=null;

		Map<String, Long> months=null;
		Map<String, Long> total=null;
		Map<String, Long> group=null;
		
		List<BranchMisRepo8Response> saleList = new ArrayList();

		
		
		System.out.println("size is "+size);
		long columnTotal=0;
		long groupColumnTotal=0;
		long grandColumnTotal=0;
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
			BranchMisRepo8 data = BranchMisRepo8List.get(i);
			
			if(first)
			{
				response=new BranchMisRepo8Response();
				ter_code=data.getTerr_cd();
				ter_name=data.getTer_name();
				months=new LinkedHashMap();
				group=new LinkedHashMap();
				total=new LinkedHashMap();
				first=false;
				
				title = getTitle(request, data); 
			}

			if(ter_code!=data.getTerr_cd())
			{
				response.setName(ter_name);
				z=k;
				for(int b=k;b<sz;b++)
				{
					MonthDto mn=monthData.get(b);
					months.put(mn.getMnth_abbr(), 0L);
					k++;
				}

				months.put("TOTAL", columnTotal);
				response.setMonths(months);
				response.setFs(fs);
				
				saleList.add(response);
				ter_code=data.getTerr_cd();
				ter_name=data.getTer_name();
				columnTotal=0;
				gfs+=fs;
				k=0;
				fs=0;
				response=new BranchMisRepo8Response();
				months=new LinkedHashMap();

			}

			
		
			// before put please check depo code in branch list if not found put 0 value in map otherwise actual zero
			for(int b=k;b<sz;b++)
			{
				MonthDto mn=monthData.get(b);
				if(mn.getMnth_code()==data.getMnth_code())
				{
					months.put(data.getMnth_abbr(), request.getUv()==2?data.getSales_val():data.getSales());
					columnTotal+=request.getUv()==2?data.getSales_val():data.getSales();
					fs+=data.getFs();
					
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
			response=new BranchMisRepo8Response();
			response.setName(ter_name);
			z=k;
			for(int b=k;b<sz;b++)
			{
				MonthDto mn=monthData.get(b);
				months.put(mn.getMnth_abbr(), 0L);
				k++;
			}
			months.put("TOTAL", columnTotal);

			response.setMonths(months);
			response.setFs(fs);
			saleList.add(response);

			
			grandColumnTotal = total.values().stream().mapToLong(d -> d).sum();
			
			months=new LinkedHashMap();
			total.put("TOTAL", grandColumnTotal);

			months.putAll(total);
			response=new BranchMisRepo8Response();
			response.setName("Total");
			response.setMonths(months);
			response.setFs(gfs);
			response.setColor(2);
			saleList.add(response);		
		return new ApiResponse<BranchMisRepo8Response>(title.toString(),size,saleList);
	}
}
