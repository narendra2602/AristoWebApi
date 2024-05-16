package com.aristowebapi.serviceimpl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aristowebapi.constant.AristoWebLogMsgConstant;
import com.aristowebapi.constant.AristoWebMessageConstant;
import com.aristowebapi.dao.MktRepo5Dao;
import com.aristowebapi.dto.MktRepo5;
import com.aristowebapi.exception.ApiException;
import com.aristowebapi.request.MktRepo5Request;
import com.aristowebapi.response.ApiResponse;
import com.aristowebapi.response.MktRepo5Response;
import com.aristowebapi.service.MktRepo5Service;
import com.aristowebapi.utility.AppCalculationUtils;

@Service
public class MktRepo5ServiceImpl  implements MktRepo5Service  {
	


	
	Logger logger = LoggerFactory.getLogger(MktRepo5ServiceImpl.class);

	@Autowired
	private MktRepo5Dao mktRepo5Dao;
	
	@Autowired
	private AristoWebMessageConstant  aristoWebMessageConstant;

	
	public String lupdate="";
	
	private String getTitle(MktRepo5Request request,MktRepo5 data)
	{
		
		StringBuilder title=new StringBuilder();

		title.append(aristoWebMessageConstant.divisionMap.get(String.valueOf(data.getDiv_code())));
		title.append(request.getDepoCode()==0?"All India":mktRepo5Dao.getBranch(request.getDepoCode())+" Branch: ");
		title.append(request.getUv()==1?" Unit Wise ":" Value Wise ");
		title.append(request.getGpCode()>0?data.getGp_name():"");
		title.append(" GROSS/CREDIT/NET SALE From  ");
		title.append(data.getSmname());
		title.append(" To ");
		title.append(data.getEmname());
		return title.toString();

	}

	
	@Override
	public ApiResponse<MktRepo5Response> getMktRepo5(MktRepo5Request request) {

		logger.info(AristoWebLogMsgConstant.MKT_REPORT_SERVICE_05,"getMktRepo5");
		List<MktRepo5> MktRepo5List=null;
		int size = 0;
		System.out.println("option "+request.getOption()+" value of code "+request.getGpCode()+" checkbox "+request.getPackCheckBox());
		try {

			
			if(request.getOption()==2 && request.getPackCheckBox()!=1)
			{
				MktRepo5List=mktRepo5Dao.getWebReport24all(request.getMyear(),request.getDivCode(),request.getDepoCode()
						,request.getSmon(),request.getEmon(),request.getUtype(),request.getLoginId(),request.getGpCode());
				
			}
			else if(request.getOption()==2 && request.getPackCheckBox()==1)
			{
				MktRepo5List=mktRepo5Dao.getWebReport24Packallhq(request.getMyear(),request.getDivCode(),request.getDepoCode()
						,request.getSmon(),request.getEmon(),request.getUtype(),request.getLoginId(),request.getGpCode());
				
			}

			else if(request.getPackCheckBox()==1)
			{
				MktRepo5List=mktRepo5Dao.getWebReport24Pack(request.getMyear(),request.getDivCode(),request.getDepoCode()
						,request.getSmon(),request.getEmon(),request.getUtype(),request.getLoginId(),request.getGpCode());
				
			}

			else
			{
					MktRepo5List=mktRepo5Dao.getWebReport24(request.getMyear(),request.getDivCode(),request.getDepoCode()
							,request.getSmon(),request.getEmon(),request.getUtype(),request.getLoginId(),request.getGpCode());

			}

			System.out.println(request.getMyear()+" "+request.getDivCode()+" "+request.getDepoCode()
			+" "+request.getSmon()+" "+request.getEmon()+" "+request.getUtype()+" "+request.getLoginId()+" "+request.getGpCode());		

			size = MktRepo5List.size();
			logger.info("size of the data is {} ***** ",size);
		
		MktRepo5Response response=null;
		List<MktRepo5Response> saleList = new ArrayList();

		boolean first=true;
		String title=null;
		double mtval=0;
		double  msval=0;
		double mcval=0;
		double mpval=0;
		double mlval=0;
		int mfs=0;

		double tval=0;
		double  sval=0;
		double cval=0;
		double pval=0;
		double lval=0;
		int fs=0;

		
		for (int i=0;i<size;i++)
		{
			MktRepo5 data = MktRepo5List.get(i);
			
			if(data.getDepo_code()==0)
				continue;
			
			if(first)
			{
				title = getTitle(request, data);
				first=false;
			}
			
			response=new MktRepo5Response();

			response.setName(data.getDepo_name());
			response.setMthFs(data.getMfs());
			
			if(request.getUv()==1)
			{
				
				response.setMthBudget(data.getMtargetqty());
				response.setMthGross(data.getMsaleqty());
				response.setMthCredit(data.getMcrqty());
				response.setMthNet(AppCalculationUtils.calculateSdf(data.getMsaleqty(), data.getMcrqty()));
				response.setMthAch(data.getMtargetval()!=0?AppCalculationUtils.calculateAch(response.getMthNet(), data.getMtargetqty()):0);
				response.setMthSd(AppCalculationUtils.calculateSdf(response.getMthNet(), data.getMtargetqty()));
				response.setMthLys(data.getMlysqty());
				response.setMthGth(data.getMlysqty()!=0?AppCalculationUtils.calculateGth(response.getMthNet(), data.getMlysqty()):0);
				response.setMthIncrSls(AppCalculationUtils.calculateSdf(response.getMthNet(), data.getMlysqty()));
				response.setMthPmr(AppCalculationUtils.calculatePmr(response.getMthNet(), data.getMfs()));
				response.setMthPendingPi(data.getMpisaleqty());

				response.setFs(data.getfs());
				response.setBudget(data.getTargetqty());
				response.setGross(data.getSaleqty());
				response.setCredit(data.getCrqty());
				response.setNet(AppCalculationUtils.calculateSdf(data.getSaleqty(), data.getCrqty()));
				response.setAch(data.getTargetqty()!=0?AppCalculationUtils.calculateAch(response.getNet(), data.getTargetqty()):0);

				response.setSd(AppCalculationUtils.calculateSdf(response.getNet(), data.getTargetqty()));
				response.setLys(data.getLysqty());
				response.setGth(data.getLysqty()!=0?AppCalculationUtils.calculateGth(response.getNet(), data.getLysqty()):0);
				response.setIncrSls(AppCalculationUtils.calculateSdf(response.getNet(), data.getLysqty()));
				response.setPmr(AppCalculationUtils.calculatePmr(response.getNet(), data.getfs()));
				response.setPendingPi(data.getPisaleqty());

			}
			else
			{
				response.setMthBudget(data.getMtargetval());
				response.setMthGross(data.getMsaleval());
				response.setMthCredit(data.getMcrval());
				response.setMthNet(AppCalculationUtils.calculateSdf(data.getMsaleval(), data.getMcrval()));
				response.setMthAch(data.getMtargetval()!=0?AppCalculationUtils.calculateAch(response.getMthNet(), data.getMtargetval()):0);
				response.setMthSd(AppCalculationUtils.calculateSdf(response.getMthNet(), data.getMtargetval()));
				response.setMthLys(data.getMlysval());
				response.setMthGth(data.getMlysval()!=0?AppCalculationUtils.calculateGth(response.getMthNet(), data.getMlysval()):0);
				response.setMthIncrSls(AppCalculationUtils.calculateSdf(response.getMthNet(), data.getMlysval()));
				response.setMthPmr(AppCalculationUtils.calculatePmr(response.getMthNet(), data.getMfs()));
				response.setMthPendingPi(data.getMpisale());

				response.setFs(data.getfs());
				response.setBudget(data.getTargetval());
				response.setGross(data.getSaleval());
				response.setCredit(data.getCrval());
				response.setNet(AppCalculationUtils.calculateSdf(data.getSaleval(), data.getCrval()));
				response.setAch(data.getTargetval()!=0?AppCalculationUtils.calculateAch(response.getNet(), data.getTargetval()):0);

				response.setSd(AppCalculationUtils.calculateSdf(response.getNet(), data.getTargetval()));
				response.setLys(data.getLysval());
				response.setGth(data.getLysval()!=0?AppCalculationUtils.calculateGth(response.getNet(), data.getLysval()):0);
				response.setIncrSls(AppCalculationUtils.calculateSdf(response.getNet(), data.getLysval()));
				response.setPmr(AppCalculationUtils.calculatePmr(response.getNet(), data.getfs()));
				response.setPendingPi(data.getPisale());
			}
	    	response.setColor(0);
	    	if(data.getDepo_code()==9996)
		    	response.setColor(1);
	    	if(data.getDepo_code()==9997)
		    	response.setColor(2);

	    	saleList.add(response);
	    	if(data.getDepo_code()<9996)
	    	{
	    		if(request.getUv()==1)
	    		{
	    			mtval = AppCalculationUtils.addDouble(mtval, data.getMtargetqty());
	    			msval = AppCalculationUtils.addDouble(msval, data.getMsaleqty());
	    			mcval = AppCalculationUtils.addDouble(mcval, data.getMcrqty());
	    			mpval = AppCalculationUtils.addDouble(mpval, data.getMpisaleqty());
	    			mlval = AppCalculationUtils.addDouble(mlval, data.getMlysqty());
	    			fs+=data.getfs();
	    			tval = AppCalculationUtils.addDouble(tval, data.getTargetqty());
	    			sval = AppCalculationUtils.addDouble(sval, data.getSaleqty());
	    			cval = AppCalculationUtils.addDouble(cval, data.getCrqty());
	    			pval = AppCalculationUtils.addDouble(pval, data.getPisaleqty());
	    			lval = AppCalculationUtils.addDouble(lval, data.getLysqty());
	    			fs+=data.getfs();
	    			
	    		}
	    		else
	    		{
	    			mtval = AppCalculationUtils.addDouble(mtval, data.getMtargetval());
	    			msval = AppCalculationUtils.addDouble(msval, data.getMsaleval());
	    			mcval = AppCalculationUtils.addDouble(mcval, data.getMcrval());
	    			mpval = AppCalculationUtils.addDouble(mpval, data.getMpisale());
	    			mlval = AppCalculationUtils.addDouble(mlval, data.getMlysval());
	    			fs+=data.getfs();
	    			tval = AppCalculationUtils.addDouble(tval, data.getTargetval());
	    			sval = AppCalculationUtils.addDouble(sval, data.getSaleval());
	    			cval = AppCalculationUtils.addDouble(cval, data.getCrval());
	    			pval = AppCalculationUtils.addDouble(pval, data.getPisale());
	    			lval = AppCalculationUtils.addDouble(lval, data.getLysval());
	    			fs+=data.getfs();
	    		}
	    	}

		} //end of for loop

		if(!first)
		{

			response=new MktRepo5Response();
			response.setName("GRAND TOTAL");

			response.setMthFs(mfs);
			response.setMthBudget(mtval);
			response.setMthGross(msval);
			response.setMthCredit(mcval);
	    	response.setMthNet(AppCalculationUtils.calculateSdf(msval, mcval));
			response.setMthAch(tval!=0?AppCalculationUtils.calculateAch(response.getMthNet(), mtval):0);
	    	response.setMthSd(AppCalculationUtils.calculateSdf(response.getMthNet(), mtval));
	    	response.setMthLys(mlval);
	    	response.setMthGth(mlval!=0?AppCalculationUtils.calculateGth(response.getMthNet(), mlval):0);
	    	response.setMthIncrSls(AppCalculationUtils.calculateSdf(response.getMthNet(), mlval));
	    	response.setMthPmr(AppCalculationUtils.calculatePmr(response.getMthNet(), mfs));
			response.setMthPendingPi(mpval);

			
			response.setFs(fs);
			response.setBudget(tval);
			response.setGross(sval);
			response.setCredit(cval);
	    	response.setNet(AppCalculationUtils.calculateSdf(sval, cval));
			response.setAch(tval!=0?AppCalculationUtils.calculateAch(response.getNet(), tval):0);
	    	response.setSd(AppCalculationUtils.calculateSdf(response.getNet(), tval));
	    	response.setLys(lval);
	    	response.setGth(lval!=0?AppCalculationUtils.calculateGth(response.getNet(), lval):0);
	    	response.setIncrSls(AppCalculationUtils.calculateSdf(response.getNet(), lval));
	    	response.setPmr(AppCalculationUtils.calculatePmr(response.getNet(), fs));
			response.setPendingPi(pval);
			response.setColor(3);

			saleList.add(response);
		}

		
		ApiResponse<MktRepo5Response> apiResponse = new ApiResponse<>(title!=null?title.toString():"",size,lupdate, saleList);
		return apiResponse;
		
		} catch (Exception e) {
			logger.error(AristoWebLogMsgConstant.MKT_REPORT_SERVICE_052,"getMktRepo5");
			throw new ApiException(e.getMessage());
		}
		

	}


}



