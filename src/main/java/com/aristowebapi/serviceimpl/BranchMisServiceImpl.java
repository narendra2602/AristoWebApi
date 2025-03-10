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
import com.aristowebapi.dto.BranchMisRepo8Ach;
import com.aristowebapi.dto.MonthDto;
import com.aristowebapi.exception.ApiException;
import com.aristowebapi.request.BranchMisRepo5Request;
import com.aristowebapi.request.BranchMisRepo6Request;
import com.aristowebapi.request.BranchMisRepo8Request;
import com.aristowebapi.response.ApiResponse;
import com.aristowebapi.response.BranchMisRepo5Response;
import com.aristowebapi.response.BranchMisRepo6Response;
import com.aristowebapi.response.BranchMisRepo8AchResponse;
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
			System.out.println("rep_type  "+request.getRepType()+" depo "+request.getDepoCode());
			
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
		long lys=0;
		long abudget=0;
		long agross=0;
		long acredit=0;
		long anet=0;
		long alys=0;

		long gbudget=0;
		long ggross=0;
		long gcredit=0;
		long gnet=0;
		long glys=0;
		String branch="";
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
				branch=data.getDepo_name();
				name=data.getTer_name();
				first=false;
			}
			
			if(reg_code!=data.getReg_cd())
			{
				
				saleList.add(getResponse(branch,reg_name,budget,gross,credit,lys,net,1));
				reg_code=data.getReg_cd();
				reg_name=data.getReg_name();
				budget=0;
				gross=0;
				credit=0;
				net=0;
				lys=0;
				
			}

			if(area_code!=data.getArea_cd())
			{
				
				saleList.add(getResponse(branch,area_name,abudget,agross,acredit,alys,anet,2));
				area_code=data.getArea_cd();
				area_name=data.getArea_name();
				abudget=0;
				agross=0;
				acredit=0;
				anet=0;
				alys=0;
				
			}


			if(!branch.equalsIgnoreCase(data.getDepo_name()) && request.getRepType()==2)
			{
				
				saleList.add(getResponse(branch,"Total",budget,gross,credit,lys,net,2));
				branch=data.getDepo_name();
				budget=0;
				gross=0;
				credit=0;
				net=0;
				lys=0;
				
			}

			
			saleList.add(getResponse(data.getDepo_name(),request.getDepoCode()>0 || request.getRepType()==2?data.getTer_name():"",data.getBudget(),data.getGross(),data.getCredit(),data.getLysval(),data.getNet(),0));

	    	
	    	budget+=data.getBudget();
	    	gross+=data.getGross();
	    	credit+=data.getCredit();
	    	lys+=data.getLysval();
	    	net+=data.getNet();
	    	
	    	abudget+=data.getBudget();
	    	agross+=data.getGross();
	    	acredit+=data.getCredit();
	    	alys+=data.getLysval();
	    	anet+=data.getNet();

	    	gbudget+=data.getBudget();
	    	ggross+=data.getGross();
	    	gcredit+=data.getCredit();
	    	glys+=data.getLysval();
	    	gnet+=data.getNet();
	    	
		} //end of for loop
		
			if(request.getDepoCode()>0)
			{
			saleList.add(getResponse(branch,reg_name,budget,gross,credit,lys,net,1));
			saleList.add(getResponse(branch,area_name,abudget,agross,acredit,alys,anet,2));
			saleList.add(getResponse(branch," Total",gbudget,ggross,gcredit,glys,gnet,3));
			}
			else if(request.getDepoCode()==0 && request.getRepType()==1)
			{
			saleList.add(getResponse("All India"," Total",gbudget,ggross,gcredit,glys,gnet,3));
			}
			else if(request.getDepoCode()==0 && request.getRepType()==2)
			{
				saleList.add(getResponse(branch," Total",budget,gross,credit,lys,net,2));
				saleList.add(getResponse("All India"," Total",gbudget,ggross,gcredit,glys,gnet,3));
			}

		ApiResponse<BranchMisRepo5Response> apiResponse = new ApiResponse<>(title!=null?title.toString():"", size,saleList);
		return apiResponse;
		
		} catch (Exception e) {
			logger.error(AristoWebLogMsgConstant.BRANCH_MIS_REPORT_5_SERVICE_021,"getBranchMisRepo5");
			throw new ApiException(e.getMessage());
		}
		

	}


	private BranchMisRepo5Response getResponse(String branch,String name,long budget,long gross,long credit,long lastYearSale,long net, int color)
	{
		BranchMisRepo5Response response=new BranchMisRepo5Response();
		response.setBranch(branch);
		response.setHqName(name);;

/*		response.setBudget(AppCalculationUtils.roundToDecimal2Places(budget));
		response.setGross(AppCalculationUtils.roundToDecimal2Places(gross));
		response.setCredit(AppCalculationUtils.roundToDecimal2Places(credit));
		response.setNet(AppCalculationUtils.roundToDecimal2Places(net));
		response.setSurdef(AppCalculationUtils.roundToDecimal2Places(net-budget));
*/
		response.setBudget(budget);
		response.setGrossSale(gross);
		response.setCreditNote(credit);
		response.setLastYearSale(lastYearSale);
		response.setNetSale(net);
		response.setAchPer(AppCalculationUtils.calculateAch(net, budget));
		response.setSurSlashdef(net-budget);

		response.setColor(color);
		return response;

	}

	
	
	private String getTitle(BranchMisRepo6Request request,BranchMisRepo6 data)
	{
		StringBuilder title=new StringBuilder();
		title.append(request.getRepTypePgwise()==1?"PRODUCT - > ":" GROUP - > ");
		title.append(data.getPname());
		title.append(request.getRepType()==2 || request.getRepType()==22 ?" H.Q.WISE ":" BRANCH WISE ");
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
		
		if(request.getRepTypePgwise()==2 && request.getRepType()==1) // branch wise group wise
			request.setRepType(12);

		if(request.getRepTypePgwise()==2 && request.getRepType()==2)  // hqwise group wise 
			request.setRepType(22);

		System.out.println(request.getMyear()+" div "+request.getDivCode()+" "+request.getDepoCode()+" "+request.getSmon()+" "+request.getEmon()+" "+request.getRepType()+" "+request.getLoginId()+" "+request.getUtype()+" "+request.getCode());

		
		try {
			if(request.getRepTypePgwise()==2)
				BranchMisRepo6List=branchMisDao.getBranchMisRepo6Group(request.getMyear(),request.getDivCode(),request.getDepoCode(),request.getSmon(),request.getEmon(),request.getRepType(),request.getLoginId(),request.getUtype(),request.getCode());
			else
				BranchMisRepo6List=branchMisDao.getBranchMisRepo6(request.getMyear(),request.getDivCode(),request.getDepoCode(),request.getSmon(),request.getEmon(),request.getRepType(),request.getLoginId(),request.getUtype(),request.getCode());

			size = BranchMisRepo6List.size();
			logger.info("size of the data is {}",size);
			
			
			
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

    	int gqltyqty=0;
    	double gqltyval=0.00;

		for (int i=0;i<size;i++)
		{
			BranchMisRepo6 data = BranchMisRepo6List.get(i);
			
			
			if(first)
			{
				title = getTitle(request, data);
				first=false;
			}
			
			response=new BranchMisRepo6Response();
			response.setBranch(data.getDepo_name());
			response.setHqName(request.getDepoCode()>0 || request.getRepType()==2 || request.getRepType()==22 ?data.getName():"");
	    	response.setInvSaleQty(data.getSaleqty());
	    	response.setInvSaleVal(data.getSalesval());

			response.setPiSaleQty(data.getPisaleqty());
	    	response.setPiSaleVal(data.getPisalesval());

			response.setGrossSaleQty(data.getGross_sale_qty());
	    	response.setGrossSaleVal(data.getGross_sale_val());

	    	response.setSaleableQty(data.getSalableqty());
	    	response.setSaleableVal(data.getSalableval());

	    	response.setExpQty(data.getExpqty());
	    	response.setExpVal(data.getExpval());

	    	response.setBrkgQty(data.getBrkqty());
	    	response.setBrkgVal(data.getBrkval());
///
	    	response.setLossInTrQty(data.getOthqty());
	    	response.setLossInTrVal(data.getOthval());
////
	    	response.setQualityQty(data.getQltyqty());
	    	response.setQualityVal(data.getQltyval());

	    	
	    	response.setOtherQty(0);
	    	response.setOtherVal(0);

	    	response.setRateDiff(data.getPdval());
	    	
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

	    	gqltyqty+=data.getQltyqty();
	    	gqltyval+=data.getQltyval();
	    	
	    	
		} //end of for loop

		if(!first)
		{

			response=new BranchMisRepo6Response();
			response.setBranch("GRAND TOTAL");
			response.setHqName(" ");
	    	response.setInvSaleQty(gsaleqty);
	    	response.setInvSaleVal(Math.round(gsaleval));

	    	response.setPiSaleQty(gpisaleqty);
	    	response.setPiSaleVal(Math.round(gpisaleval));

			response.setGrossSaleQty(ggrosssaleqty);
	    	response.setGrossSaleVal(Math.round(ggrosssaleval));

	    	
	    	response.setSaleableQty(gsalableqty);
	    	response.setSaleableVal(Math.round(gsalableval));

	    	response.setExpQty(gexpqty);
	    	response.setExpVal(Math.round(gexpval));

	    	response.setBrkgQty(gbrkqty);
	    	response.setBrkgVal(Math.round(gbrkval));

	    	response.setLossInTrQty(gothqty);
	    	response.setLossInTrVal(Math.round(gothval));

	    	
	    	response.setOtherQty(0);
	    	response.setOtherVal(0);
	    	
	    	response.setRateDiff(Math.round(gpdval));

	    	
	    	response.setNetQty(gnetqty);
	    	response.setNetVal(Math.round(gnetval));

	    	response.setQualityQty(gqltyqty);
	    	response.setQualityVal(Math.round(gqltyval));

	    	
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
		    case 4: title.append("(SALEABLE) FROM ");
		    		break;
		    case 5: title.append("(BRK/SPOILED) FROM ");
		    		break;
		    case 6: title.append("(NET) FROM ");
		    		break;
		    case 7: title.append("(TARGET) FROM ");
		    		break;
		    case 8: title.append("(LAST YEAR SALE) FROM ");
		    		break;
		    case 9: title.append("(ACHIEVEMENT) FROM ");
    				break;
		    case 10: title.append("(GROWTH) FROM ");
    				break;
		    case 11: title.append("(PMR) FROM ");
		    		break;
		    case 12: title.append("(FIELD STRENGTH) FROM ");
		    		break;
		    case 13: title.append("(EXPIRY RATIO) FROM ");

		}

		title.append(data.getSmname());
		title.append(" To ");
		title.append(data.getEmname());

		return title.toString();

	}

	
	@Override
	public ApiResponse<BranchMisRepo8Response> getBranchMisRepo8(BranchMisRepo8Request request) {
		logger.info(AristoWebLogMsgConstant.STK_REPO10_SERVICE,"getBranchMisRepo8");


		List<MonthDto> monthData = branchMisDao.getAllMonth(request.getMyear());
		int sz=monthData.size();
		int k=0;
		int z=0;
		sz=request.getEmon();

		 System.out.println(request.getMyear()+" "+request.getDivCode()+" "+request.getDepoCode()+" "+request.getSmon()+" "+request.getEmon()+" "+request.getRepType()+" "+request.getLoginId()+" "+request.getUtype()+" "+request.getCode());
		// opt==1 hq   and opt==4 branch
		String title=null;
		List<BranchMisRepo8> BranchMisRepo8List=null;
		int size = 0;
		if(request.getRepTypePgwise()==2)
			request.setUv(2);
		
		if(request.getDepoCode()==0 && request.getOpt()==1 && request.getRepTypePgwise()==2) // branch
			BranchMisRepo8List=branchMisDao.getBranchMisRepo8HQGroup(request.getMyear(),request.getDivCode(),request.getDepoCode(),request.getSmon(),request.getEmon(),request.getRepType(),request.getLoginId(),request.getUtype(),request.getCode());
		else if(request.getDepoCode()==0 && request.getOpt()==1) // branch
			BranchMisRepo8List=branchMisDao.getBranchMisRepo8HQ(request.getMyear(),request.getDivCode(),request.getDepoCode(),request.getSmon(),request.getEmon(),request.getRepType(),request.getLoginId(),request.getUtype(),request.getCode());
		else if(request.getDepoCode()==0 && request.getRepTypePgwise()==2)
			BranchMisRepo8List=branchMisDao.getBranchMisRepo8BranchGroup(request.getMyear(),request.getDivCode(),request.getDepoCode(),request.getSmon(),request.getEmon(),request.getRepType(),request.getLoginId(),request.getUtype(),request.getCode());
		else if(request.getDepoCode()==0)
			BranchMisRepo8List=branchMisDao.getBranchMisRepo8Branch(request.getMyear(),request.getDivCode(),request.getDepoCode(),request.getSmon(),request.getEmon(),request.getRepType(),request.getLoginId(),request.getUtype(),request.getCode());
		else if(request.getOpt()==1 && request.getRepTypePgwise()==2) // hqwise
			BranchMisRepo8List=branchMisDao.getBranchMisRepo8HQGroup(request.getMyear(),request.getDivCode(),request.getDepoCode(),request.getSmon(),request.getEmon(),request.getRepType(),request.getLoginId(),request.getUtype(),request.getCode());
		else if(request.getOpt()==1) // hqwise
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

		
		System.out.println("value of uv "+request.getUv());
		System.out.println("size is "+size);
		long columnTotal=0;
		long columnTotalVal=0;
		long groupColumnTotalVal=0;
		long groupColumnTotal=0;
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
			BranchMisRepo8 data = BranchMisRepo8List.get(i);
			
			if(first)
			{
				response=new BranchMisRepo8Response();
				ter_code=data.getTerr_cd();
				ter_name=data.getTer_name();
				branch=data.getDepo_name();
				months=new LinkedHashMap();
				group=new LinkedHashMap();
				total=new LinkedHashMap();
				first=false;
				
				title = getTitle(request, data); 
			}

			if(ter_code!=data.getTerr_cd())
			{
				response.setBranch(branch);
				response.setHqName(request.getDepoCode()>0 || request.getOpt()==1 ?ter_name:"");
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
				response.setCumFs(fs);
				
				saleList.add(response);
				ter_code=data.getTerr_cd();
				ter_name=data.getTer_name();
				branch=data.getDepo_name();
				columnTotal=0;
				columnTotalVal=0;
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
					//months.put(data.getMnth_abbr(), request.getUv()==2?data.getSales_val():data.getSales());
					//columnTotal+=request.getUv()==2?data.getSales_val():data.getSales();
					fs+=data.getFs();
///
					if(request.getUv()==1)
					{
						months.put((data.getMnth_abbr()+" UNITS"),data.getSales());
						columnTotal+=data.getSales();
						groupColumnTotal+=data.getSales();
						grandColumnTotal+=data.getSales();
					}
					else if(request.getUv()==2)
					{
						months.put((data.getMnth_abbr()+" VALUE"),data.getSales_val());
						columnTotalVal+=data.getSales_val();
						groupColumnTotalVal+=data.getSales_val();
						grandColumnTotalVal+=data.getSales_val();
					}
					else if(request.getUv()==3)
					{
						months.put((data.getMnth_abbr()+" UNITS"),data.getSales());
						months.put((data.getMnth_abbr()+" VALUE"),data.getSales_val());
						columnTotal+=data.getSales();
						columnTotalVal+=data.getSales_val();
						groupColumnTotal+=data.getSales();
						groupColumnTotalVal+=data.getSales_val();
						grandColumnTotal+=data.getSales();
						grandColumnTotalVal+=data.getSales_val();
					}

					
					if(group.containsKey(data.getMnth_abbr()+" UNITS"))
					{
						long gval = 0;

						if(request.getUv()==1)
						{
							gval = group.get(data.getMnth_abbr()+" UNITS")+data.getSales();
							group.put(data.getMnth_abbr()+" UNITS", gval);
						}
						if(request.getUv()==2)
						{
							gval = group.get(data.getMnth_abbr()+" VALUE")+data.getSales_val();
							group.put(data.getMnth_abbr()+" VALUE", gval);
						}
						if(request.getUv()==3)
						{
							gval = group.get(data.getMnth_abbr()+" UNITS")+data.getSales();
							group.put(data.getMnth_abbr()+" UNITS", gval);
							gval = group.get(data.getMnth_abbr()+" VALUE")+data.getSales_val();
							group.put(data.getMnth_abbr()+" VALUE", gval);
						}
						
					}
					else if(group.containsKey(data.getMnth_abbr()+" VALUE"))
					{
						long gval = 0;

						if(request.getUv()==1)
						{
							gval = group.get(data.getMnth_abbr()+" UNITS")+data.getSales();
							group.put(data.getMnth_abbr()+" UNITS", gval);
						}
						if(request.getUv()==2)
						{
							gval = group.get(data.getMnth_abbr()+" VALUE")+data.getSales_val();
							group.put(data.getMnth_abbr()+" VALUE", gval);
						}
						if(request.getUv()==3)
						{
							gval = group.get(data.getMnth_abbr()+" UNITS")+data.getSales();
							group.put(data.getMnth_abbr()+" UNITS", gval);
							gval = group.get(data.getMnth_abbr()+" VALUE")+data.getSales_val();
							group.put(data.getMnth_abbr()+" VALUE", gval);
						}
						
					}

					else
					{
						if(request.getUv()==1)
						{
							group.put(data.getMnth_abbr()+" UNITS", data.getSales());
						}
						if(request.getUv()==2)
						{
							group.put(data.getMnth_abbr()+" VALUE", data.getSales_val());
						}
						if(request.getUv()==3)
						{
							group.put(data.getMnth_abbr()+" UNITS", data.getSales());
							group.put(data.getMnth_abbr()+" VALUE", data.getSales_val());
						}
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

					if(group.containsKey(mn.getMnth_abbr()+" UNITS"))
					{
						// do nothing
					}
					else
					{
						if(request.getUv()==1)
						group.put(mn.getMnth_abbr()+" UNITS", 0L);
						if(request.getUv()==2)
							group.put(mn.getMnth_abbr()+" VALUE", 0L);
						if(request.getUv()==3)
						{
							group.put(mn.getMnth_abbr()+" UNITS", 0L);
							group.put(mn.getMnth_abbr()+" VALUE", 0L);
						}
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
			response=new BranchMisRepo8Response();
			response.setBranch(branch);
			response.setHqName(request.getDepoCode()>0 || request.getOpt()==1 ?ter_name:"");
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
			response.setCumFs(fs);
			saleList.add(response);

			
//			grandColumnTotal = total.values().stream().mapToLong(d -> d).sum();
			
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
			response=new BranchMisRepo8Response();
			response.setBranch(request.getDepoCode()==0?"All India":branch);
			response.setHqName("Total");
			response.setMonths(months);
			response.setCumFs(gfs);
			response.setColor(2);
			saleList.add(response);		
		return new ApiResponse<BranchMisRepo8Response>(title.toString(),size,saleList);
	}


	
	private String getTitle(BranchMisRepo8Request request,BranchMisRepo8Ach data)
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
		    case 4: title.append("(SALEABLE) FROM ");
		    		break;
		    case 5: title.append("(BRK/SPOILED) FROM ");
		    		break;
		    case 6: title.append("(NET) FROM ");
		    		break;
		    case 7: title.append("(TARGET) FROM ");
		    		break;
		    case 8: title.append("(LAST YEAR SALE) FROM ");
		    		break;
		    case 9: title.append("(ACHIEVEMENT) FROM ");
    				break;
		    case 10: title.append("(GROWTH) FROM ");
    				break;
		    case 11: title.append("(PMR) FROM ");
		    		break;
		    case 12: title.append("(FIELD STRENGTH) FROM ");
		    		break;
		    case 13: title.append("(EXPIRY RATIO) FROM ");

		}

		title.append(data.getSmname());
		title.append(" To ");
		title.append(data.getEmname());

		return title.toString();

	}

	
	
	@Override
	public ApiResponse<BranchMisRepo8AchResponse> getBranchMisRepo8Ach(BranchMisRepo8Request request) {
		logger.info(AristoWebLogMsgConstant.STK_REPO10_SERVICE,"getBranchMisRepo8");


		List<MonthDto> monthData = branchMisDao.getAllMonth(request.getMyear());
		int sz=monthData.size();
		int k=0;
		int z=0;
		sz=request.getEmon();

		 System.out.println(request.getMyear()+" "+request.getDivCode()+" "+request.getDepoCode()+" "+request.getSmon()+" "+request.getEmon()+" "+request.getRepType()+" "+request.getLoginId()+" "+request.getUtype()+" "+request.getCode());
		// opt==1 hq   and opt==4 branch
		String title=null;
		List<BranchMisRepo8Ach> BranchMisRepo8List=null;
		int size = 0;
		if(request.getRepTypePgwise()==2)
			request.setUv(2);
		
		if(request.getDepoCode()==0 && request.getOpt()==1 && request.getRepTypePgwise()==2) // branch
			BranchMisRepo8List=branchMisDao.getBranchMisRepo8HQGroupAch(request.getMyear(),request.getDivCode(),request.getDepoCode(),request.getSmon(),request.getEmon(),request.getRepType(),request.getLoginId(),request.getUtype(),request.getCode());
		else if(request.getDepoCode()==0 && request.getOpt()==1) // branch
			BranchMisRepo8List=branchMisDao.getBranchMisRepo8HQAch(request.getMyear(),request.getDivCode(),request.getDepoCode(),request.getSmon(),request.getEmon(),request.getRepType(),request.getLoginId(),request.getUtype(),request.getCode());
		else if(request.getDepoCode()==0 && request.getRepTypePgwise()==2)
			BranchMisRepo8List=branchMisDao.getBranchMisRepo8BranchGroupAch(request.getMyear(),request.getDivCode(),request.getDepoCode(),request.getSmon(),request.getEmon(),request.getRepType(),request.getLoginId(),request.getUtype(),request.getCode());
		else if(request.getDepoCode()==0)
			BranchMisRepo8List=branchMisDao.getBranchMisRepo8BranchAch(request.getMyear(),request.getDivCode(),request.getDepoCode(),request.getSmon(),request.getEmon(),request.getRepType(),request.getLoginId(),request.getUtype(),request.getCode());
		else if(request.getOpt()==1 && request.getRepTypePgwise()==2) // hqwise
			BranchMisRepo8List=branchMisDao.getBranchMisRepo8HQGroupAch(request.getMyear(),request.getDivCode(),request.getDepoCode(),request.getSmon(),request.getEmon(),request.getRepType(),request.getLoginId(),request.getUtype(),request.getCode());
		else if(request.getOpt()==1) // hqwise
			BranchMisRepo8List=branchMisDao.getBranchMisRepo8HQAch(request.getMyear(),request.getDivCode(),request.getDepoCode(),request.getSmon(),request.getEmon(),request.getRepType(),request.getLoginId(),request.getUtype(),request.getCode());
		else if(request.getDepoCode()>0 && request.getRepTypePgwise()==2 && request.getOpt()==4)
			BranchMisRepo8List=branchMisDao.getBranchMisRepo8BranchGroupAch(request.getMyear(),request.getDivCode(),request.getDepoCode(),request.getSmon(),request.getEmon(),request.getRepType(),request.getLoginId(),request.getUtype(),request.getCode());
		else if(request.getOpt()==4) // branch
			BranchMisRepo8List=branchMisDao.getBranchMisRepo8BranchAch(request.getMyear(),request.getDivCode(),request.getDepoCode(),request.getSmon(),request.getEmon(),request.getRepType(),request.getLoginId(),request.getUtype(),request.getCode());

		size = BranchMisRepo8List.size();
		logger.info("size of the data is {}",size);

		
		BranchMisRepo8AchResponse response=null;

		Map<String, Double> months=null;
		Map<String, Double> total=null;
		Map<String, Double> group=null;
		
		
		Map<String, Long> saletotal=null;
		Map<String, Long> targettotal=null;
		
		List<BranchMisRepo8AchResponse> saleList = new ArrayList();

		
		System.out.println("value of uv method ach"+request.getUv());
		System.out.println("size is "+size);
		
		double columnTotal=0;
		double columnTotalTarget=0;
		double columnTotalVal=0;
		double columnTotalValTarget=0;
		double groupColumnTotalVal=0;
		double groupColumnTotalValTarget=0;
		double groupColumnTotal=0;
		double groupColumnTotalTarget=0;
		double grandColumnTotal=0;
		long grandColumnTotalVal=0;
		long grandColumnTotalTarget=0;
		long grandColumnTotalValTarget=0;
		double vgrandColumnTotal=0;
		double vgrandColumnTotalTarget=0;

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
			BranchMisRepo8Ach data = BranchMisRepo8List.get(i);
			
			if(first)
			{
				response=new BranchMisRepo8AchResponse();
				ter_code=data.getTerr_cd();
				ter_name=data.getTer_name();
				branch=data.getDepo_name();
				months=new LinkedHashMap();
				group=new LinkedHashMap();
				total=new LinkedHashMap();
				saletotal=new LinkedHashMap();
				targettotal=new LinkedHashMap();
				first=false;
				
				title = getTitle(request, data); 
			}

			if(ter_code!=data.getTerr_cd())
			{
				
				response.setBranch(branch==null?ter_name:branch);
				response.setHqName(request.getDepoCode()>0 || request.getOpt()==1 ?ter_name:"");
				response.setHqName(request.getDepoCode()>0 && request.getOpt()==4 ?"":ter_name);
				z=k;
				for(int b=k;b<sz;b++)
				{
					MonthDto mn=monthData.get(b);
					if(request.getUv()==1)
						months.put((mn.getMnth_abbr()+" UNITS"), 0D);
					else if(request.getUv()==2)
						months.put((mn.getMnth_abbr()+" VALUE"), 0D);
					else
					{
						months.put((mn.getMnth_abbr()+" UNITS"), 0D);
						months.put((mn.getMnth_abbr()+" VALUE"), 0D);
					}
					k++;
				}

				
				if(request.getRepType()==9)
				{
					if(request.getUv()==1)
						columnTotal=AppCalculationUtils.calculateAch(columnTotal, columnTotalTarget);
						else
						columnTotalVal=AppCalculationUtils.calculateAch(columnTotalVal, columnTotalValTarget);
				}
				else if(request.getRepType()==10)
				{
					if(request.getUv()==1)
						columnTotal=AppCalculationUtils.calculateGth(columnTotal, columnTotalTarget);
						else
						columnTotalVal=AppCalculationUtils.calculateGth(columnTotalVal, columnTotalValTarget);
				}

				else if(request.getRepType()==13)
				{
					if(request.getUv()==1)
						columnTotal=AppCalculationUtils.calculateExpiryRatio(columnTotalTarget,columnTotal );
						else
						columnTotalVal=AppCalculationUtils.calculateGth(columnTotalValTarget,columnTotalVal );
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
				response.setCumFs(fs);
				
				saleList.add(response);
				ter_code=data.getTerr_cd();
				ter_name=data.getTer_name();
				branch=data.getDepo_name();
				columnTotal=0;
				columnTotalVal=0;
				columnTotalTarget=0;
				columnTotalValTarget=0;
				gfs+=fs;
				k=0;
				fs=0;
				response=new BranchMisRepo8AchResponse();
				months=new LinkedHashMap();

			}

			
		
			// before put please check depo code in branch list if not found put 0 value in map otherwise actual zero
			for(int b=k;b<sz;b++)
			{
				MonthDto mn=monthData.get(b);
				if(mn.getMnth_code()==data.getMnth_code())
				{
					//months.put(data.getMnth_abbr(), request.getUv()==2?data.getSales_val():data.getSales());
					//columnTotal+=request.getUv()==2?data.getSales_val():data.getSales();
					fs+=data.getFs();
///
					if(request.getUv()==1)
					{
						months.put((data.getMnth_abbr()+" UNITS"),data.getAch_qty());
						columnTotal+=data.getSales();
						groupColumnTotal+=data.getSales();
						grandColumnTotal+=data.getSales();

						columnTotalTarget+=data.getTarget_qty();
						groupColumnTotalTarget+=data.getTarget_qty();
						grandColumnTotalTarget+=data.getTarget_qty();
					}
					else if(request.getUv()==2)
					{
						months.put((data.getMnth_abbr()+" VALUE"),data.getAch_val());
						columnTotalVal+=data.getSales_val();
						groupColumnTotalVal+=data.getSales_val();
						grandColumnTotalVal+=data.getSales_val();
						columnTotalValTarget+=data.getTarget_val();
						groupColumnTotalValTarget+=data.getTarget_val();
						grandColumnTotalValTarget+=data.getTarget_val();
					}
					else if(request.getUv()==3)
					{
						months.put((data.getMnth_abbr()+" UNITS"),data.getAch_qty());
						months.put((data.getMnth_abbr()+" VALUE"),data.getAch_val());
						columnTotal+=data.getSales();
						columnTotalVal+=data.getSales_val();
						groupColumnTotal+=data.getSales();
						groupColumnTotalVal+=data.getSales_val();
						grandColumnTotal+=data.getSales();
						grandColumnTotalVal+=data.getSales_val();
						
						columnTotalTarget+=data.getTarget_qty();
						groupColumnTotalTarget+=data.getTarget_qty();
						grandColumnTotalTarget+=data.getTarget_qty();
						columnTotalValTarget+=data.getTarget_val();
						groupColumnTotalValTarget+=data.getTarget_val();
						grandColumnTotalValTarget+=data.getTarget_val();
					}

					
					long ggval=0;
					double gtval=0.00;

					if(request.getUv()==1)
					{
						if(saletotal.containsKey((data.getMnth_abbr()+" UNITS")))
						{
							
							ggval = saletotal.get((data.getMnth_abbr()+" UNITS"))+data.getSales_val();
							saletotal.put((data.getMnth_abbr()+" UNITS"), ggval);
							ggval = targettotal.get((data.getMnth_abbr()+" UNITS"))+data.getTarget_val();
							targettotal.put((data.getMnth_abbr()+" UNITS"), ggval);

						}
						else
						{
							saletotal.put((data.getMnth_abbr()+" UNITS"), data.getSales_val());
							targettotal.put((data.getMnth_abbr()+" UNITS"), data.getTarget_val());
						}
						
					}
					if(request.getUv()==2)
					{
						if(saletotal.containsKey((data.getMnth_abbr()+" VALUE")))
						{
							
							ggval = saletotal.get((data.getMnth_abbr()+" VALUE"))+data.getSales_val();
							saletotal.put((data.getMnth_abbr()+" VALUE"), ggval);
							ggval = targettotal.get((data.getMnth_abbr()+" VALUE"))+data.getTarget_val();
							targettotal.put((data.getMnth_abbr()+" VALUE"), ggval);

						}
						else
						{
							saletotal.put((data.getMnth_abbr()+" VALUE"), data.getSales_val());
							targettotal.put((data.getMnth_abbr()+" VALUE"), data.getTarget_val());
						}
						
					}
					if(request.getUv()==3)
					{
						if(saletotal.containsKey((data.getMnth_abbr()+" UNITS")))
						{
							
							ggval = saletotal.get((data.getMnth_abbr()+" UNITS"))+data.getSales_val();
							saletotal.put((data.getMnth_abbr()+" UNITS"), ggval);
							ggval = targettotal.get((data.getMnth_abbr()+" UNITS"))+data.getTarget_val();
							targettotal.put((data.getMnth_abbr()+" UNITS"), ggval);

						}
						else
						{
							saletotal.put((data.getMnth_abbr()+" UNITS"), data.getSales_val());
							targettotal.put((data.getMnth_abbr()+" UNITS"), data.getTarget_val());
						}

						if(saletotal.containsKey((data.getMnth_abbr()+" VALUE")))
						{
							
							ggval = saletotal.get((data.getMnth_abbr()+" VALUE"))+data.getSales_val();
							saletotal.put((data.getMnth_abbr()+" VALUE"), ggval);
							ggval = targettotal.get((data.getMnth_abbr()+" VALUE"))+data.getTarget_val();
							targettotal.put((data.getMnth_abbr()+" VALUE"), ggval);

						}
						else
						{
							saletotal.put((data.getMnth_abbr()+" VALUE"), data.getSales_val());
							targettotal.put((data.getMnth_abbr()+" VALUE"), data.getTarget_val());
						}
					
					}

					

///					
					k++;
					break;
				}
				else
				{
					if(request.getUv()==1)
						months.put((mn.getMnth_abbr()+" UNITS"), 0D);
					else if(request.getUv()==2)
						months.put((mn.getMnth_abbr()+" VALUE"), 0D);
					else
					{
						months.put((mn.getMnth_abbr()+" UNITS"), 0D);
						months.put((mn.getMnth_abbr()+" VALUE"), 0D);
						
					}

					
					if(saletotal.containsKey(mn.getMnth_abbr()+" UNITS"))
					{
						// do nothing
					}
					else if(saletotal.containsKey(mn.getMnth_abbr()+" VALUE"))
					{
						// do nothing
					}
					else
					{
						if(request.getUv()==1)
						{
							saletotal.put(mn.getMnth_abbr()+" UNITS", 0L);
							targettotal.put(mn.getMnth_abbr()+" UNITS", 0L);
						}
						if(request.getUv()==2)
						{
							saletotal.put(mn.getMnth_abbr()+" VALUE", 0L);
							targettotal.put(mn.getMnth_abbr()+" VALUE", 0L);
						}
						if(request.getUv()==3)
						{
							saletotal.put(mn.getMnth_abbr()+" UNITS", 0L);
							targettotal.put(mn.getMnth_abbr()+" UNITS", 0L);
							saletotal.put(mn.getMnth_abbr()+" VALUE", 0L);
							targettotal.put(mn.getMnth_abbr()+" VALUE", 0L);

						}

					}

					
					k++;
				}
			}
			
		}			
			response=new BranchMisRepo8AchResponse();
			response.setBranch(branch);
			response.setHqName(request.getDepoCode()>0 || request.getOpt()==1 ?ter_name:"");
			response.setHqName(request.getDepoCode()>0 && request.getOpt()==4 ?"":ter_name);
			gfs+=fs;
			z=k;
			for(int b=k;b<sz;b++)
			{
				MonthDto mn=monthData.get(b);
				if(request.getUv()==1)
					months.put((mn.getMnth_abbr()+" UNITS"), 0D);
				else if(request.getUv()==2)
					months.put((mn.getMnth_abbr()+" VALUE"), 0D);
				else
				{
					months.put((mn.getMnth_abbr()+" UNITS"), 0D);
					months.put((mn.getMnth_abbr()+" VALUE"), 0D);
				}
				k++;
			}
			if(request.getRepType()==9)
			{
				if(request.getUv()==1)
					columnTotal=AppCalculationUtils.calculateAch(columnTotal, columnTotalTarget);
					else
					columnTotalVal=AppCalculationUtils.calculateAch(columnTotalVal, columnTotalValTarget);
			}
			else if(request.getRepType()==10)
			{
				if(request.getUv()==1)
					columnTotal=AppCalculationUtils.calculateGth(columnTotal, columnTotalTarget);
					else
					columnTotalVal=AppCalculationUtils.calculateGth(columnTotalVal, columnTotalValTarget);
			}
			else if(request.getRepType()==13)
			{
				if(request.getUv()==1)
					columnTotal=AppCalculationUtils.calculateExpiryRatio(columnTotalTarget,columnTotal );
					else
					columnTotalVal=AppCalculationUtils.calculateGth(columnTotalValTarget,columnTotalVal );
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
			response.setCumFs(fs);
			saleList.add(response);

			

			
			months=new LinkedHashMap();
			double newtotal=0.00;
			for(int b=0;b<sz;b++)
			{
				MonthDto mn=monthData.get(b);
				
				
				
				if(request.getUv()==1)
				{
					if(request.getRepType()==9)
							newtotal=AppCalculationUtils.calculateAch(saletotal.get(mn.getMnth_abbr()+"UNITS"), targettotal.get(mn.getMnth_abbr()+" UNITS"));
					if(request.getRepType()==10)
						newtotal=AppCalculationUtils.calculateGth(saletotal.get(mn.getMnth_abbr()+"UNITS"), targettotal.get(mn.getMnth_abbr()+" UNITS"));
					if(request.getRepType()==13)
						newtotal=AppCalculationUtils.calculateExpiryRatio(targettotal.get(mn.getMnth_abbr()+" UNITS"),saletotal.get(mn.getMnth_abbr()+"UNITS") );
					
					
					months.put((mn.getMnth_abbr()+" UNITS"), newtotal);
				}
				else if(request.getUv()==2)
				{
					
					if(request.getRepType()==9)
						newtotal=AppCalculationUtils.calculateAch(saletotal.get(mn.getMnth_abbr()+" VALUE"), targettotal.get(mn.getMnth_abbr()+" VALUE"));
					if(request.getRepType()==10)
						newtotal=AppCalculationUtils.calculateGth(saletotal.get(mn.getMnth_abbr()+" VALUE"), targettotal.get(mn.getMnth_abbr()+" VALUE"));
					if(request.getRepType()==13)
						newtotal=AppCalculationUtils.calculateExpiryRatio(targettotal.get(mn.getMnth_abbr()+" VALUE"),saletotal.get(mn.getMnth_abbr()+" VALUE") );

					months.put((mn.getMnth_abbr()+" VALUE"), newtotal);
				}
				else
				{
					if(request.getRepType()==9)
						newtotal=AppCalculationUtils.calculateAch(saletotal.get(mn.getMnth_abbr()+"UNITS"), targettotal.get(mn.getMnth_abbr()+" UNITS"));
				if(request.getRepType()==10)
					newtotal=AppCalculationUtils.calculateGth(saletotal.get(mn.getMnth_abbr()+"UNITS"), targettotal.get(mn.getMnth_abbr()+" UNITS"));
				if(request.getRepType()==13)
					newtotal=AppCalculationUtils.calculateExpiryRatio(targettotal.get(mn.getMnth_abbr()+" UNITS"),saletotal.get(mn.getMnth_abbr()+"UNITS") );

					months.put((mn.getMnth_abbr()+" UNITS"), newtotal);

					if(request.getRepType()==9)
						newtotal=AppCalculationUtils.calculateAch(saletotal.get(mn.getMnth_abbr()+" VALUE"), targettotal.get(mn.getMnth_abbr()+" VALUE"));
					if(request.getRepType()==10)
						newtotal=AppCalculationUtils.calculateGth(saletotal.get(mn.getMnth_abbr()+" VALUE"), targettotal.get(mn.getMnth_abbr()+" VALUE"));
					if(request.getRepType()==13)
						newtotal=AppCalculationUtils.calculateExpiryRatio(targettotal.get(mn.getMnth_abbr()+" VALUE"),saletotal.get(mn.getMnth_abbr()+" VALUE") );

					months.put((mn.getMnth_abbr()+" VALUE"), newtotal);
				}
				
			}

			
			
			if(request.getRepType()==9)
			{
				if(request.getUv()==1)
					newtotal=AppCalculationUtils.calculateAch(grandColumnTotal, grandColumnTotalTarget);
					else
					newtotal=AppCalculationUtils.calculateAch(grandColumnTotalVal, grandColumnTotalValTarget);
			}
			else if(request.getRepType()==10)
			{
				if(request.getUv()==1)
					newtotal=AppCalculationUtils.calculateGth(grandColumnTotal, grandColumnTotalTarget);
					else
					newtotal=AppCalculationUtils.calculateGth(grandColumnTotalVal, grandColumnTotalValTarget);
			}
			else if(request.getRepType()==13)
			{
				if(request.getUv()==1)
					newtotal=AppCalculationUtils.calculateExpiryRatio(grandColumnTotalTarget,grandColumnTotal );
					else
					newtotal=AppCalculationUtils.calculateGth(grandColumnTotalValTarget,grandColumnTotalVal );
			}

			
			if(request.getUv()==1)
				total.put("TOTAL UNITS", newtotal);
			else if(request.getUv()==2)
				total.put("TOTAL VALUE", newtotal);
			else
			{
				total.put("TOTAL UNITS", newtotal);
				total.put("TOTAL VALUE", newtotal);
				
			}



			months.putAll(total);
			response=new BranchMisRepo8AchResponse();
			response.setBranch(request.getDepoCode()==0?"All India":branch);
			response.setHqName("Total");
			response.setMonths(months);
			response.setCumFs(gfs);
			response.setColor(2);
			saleList.add(response);		
		return new ApiResponse<BranchMisRepo8AchResponse>(title.toString(),size,saleList);
	}
}
