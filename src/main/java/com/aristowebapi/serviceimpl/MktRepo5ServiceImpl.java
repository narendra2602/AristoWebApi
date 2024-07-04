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
		
				System.out.println(request.getMyear()+" "+request.getDivCode()+" "+request.getDepoCode()
				+" "+request.getSmon()+" "+request.getEmon()+" "+request.getUtype()+" "+request.getLoginId()+" "+request.getGpCode());		

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

			response.setBranch(data.getBranch_name());
			if(request.getOption()==2 || request.getDepoCode()>0 )
				response.setHqName(data.getDepo_name());
			else
				response.setHqName("");
			response.setMthFs(data.getMfs());
			
			if(request.getUv()==1)
			{
				
				response.setMthBudget(data.getMtargetqty());
				response.setMthGross(data.getMsaleqty());
				response.setMthCredit(data.getMcrqty());
				response.setMthNet(AppCalculationUtils.calculateSdf(data.getMsaleqty(), data.getMcrqty()));
				response.setMthAchPer(data.getMtargetval()!=0?AppCalculationUtils.calculateAch(response.getMthNet(), data.getMtargetqty()):0);
				response.setMthSurSlashDeff(AppCalculationUtils.calculateSdf(response.getMthNet(), data.getMtargetqty()));
				response.setMthLys(data.getMlysqty());
				response.setMthGthPer(data.getMlysqty()!=0?AppCalculationUtils.calculateGth(response.getMthNet(), data.getMlysqty()):0);
				response.setMthIncrSale(AppCalculationUtils.calculateSdf(response.getMthNet(), data.getMlysqty()));
				response.setMthPmr(AppCalculationUtils.calculatePmr(response.getMthNet(), data.getMfs()));
				response.setMthPendingPi(data.getMpisaleqty());

				response.setCumFs(data.getfs());
				response.setCumBudget(data.getTargetqty());
				response.setCumGross(data.getSaleqty());
				response.setCumCredit(data.getCrqty());
				response.setCumNet(AppCalculationUtils.calculateSdf(data.getSaleqty(), data.getCrqty()));
				response.setCumAchPer(data.getTargetqty()!=0?AppCalculationUtils.calculateAch(response.getCumNet(), data.getTargetqty()):0);

				response.setCumSurSlashDef(AppCalculationUtils.calculateSdf(response.getCumNet(), data.getTargetqty()));
				response.setCumLys(data.getLysqty());
				response.setCumGthPer(data.getLysqty()!=0?AppCalculationUtils.calculateGth(response.getCumNet(), data.getLysqty()):0);
				response.setCumIncrSale(AppCalculationUtils.calculateSdf(response.getCumNet(), data.getLysqty()));
				response.setCumPmr(AppCalculationUtils.calculatePmr(response.getCumNet(), data.getfs()));
				response.setCumPendingPi(data.getPisaleqty());

			}
			else
			{
				response.setMthBudget(data.getMtargetval());
				response.setMthGross(data.getMsaleval());
				response.setMthCredit(data.getMcrval());
				response.setMthNet(AppCalculationUtils.calculateSdf(data.getMsaleval(), data.getMcrval()));
				response.setMthAchPer(data.getMtargetval()!=0?AppCalculationUtils.calculateAch(response.getMthNet(), data.getMtargetval()):0);
				response.setMthSurSlashDeff(AppCalculationUtils.calculateSdf(response.getMthNet(), data.getMtargetval()));
				response.setMthLys(data.getMlysval());
				response.setMthGthPer(data.getMlysval()!=0?AppCalculationUtils.calculateGth(response.getMthNet(), data.getMlysval()):0);
				response.setMthIncrSale(AppCalculationUtils.calculateSdf(response.getMthNet(), data.getMlysval()));
				response.setMthPmr(AppCalculationUtils.calculatePmr(response.getMthNet(), data.getMfs()));
				response.setMthPendingPi(data.getMpisale());

				response.setCumFs(data.getfs());
				response.setCumBudget(data.getTargetval());
				response.setCumGross(data.getSaleval());
				response.setCumCredit(data.getCrval());
				response.setCumNet(AppCalculationUtils.calculateSdf(data.getSaleval(), data.getCrval()));
				response.setCumAchPer(data.getTargetval()!=0?AppCalculationUtils.calculateAch(response.getCumNet(), data.getTargetval()):0);

				response.setCumSurSlashDef(AppCalculationUtils.calculateSdf(response.getCumNet(), data.getTargetval()));
				response.setCumLys(data.getLysval());
				response.setCumGthPer(data.getLysval()!=0?AppCalculationUtils.calculateGth(response.getCumNet(), data.getLysval()):0);
				response.setCumIncrSale(AppCalculationUtils.calculateSdf(response.getCumNet(), data.getLysval()));
				response.setCumPmr(AppCalculationUtils.calculatePmr(response.getCumNet(), data.getfs()));
				response.setCumPendingPi(data.getPisale());
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
	    			mfs+=data.getMfs();
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
	    			mfs+=data.getMfs();
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
			response.setBranch("Grand Total");
			response.setHqName("");
			response.setMthFs(mfs);
			response.setMthBudget(mtval);
			response.setMthGross(msval);
			response.setMthCredit(mcval);
	    	response.setMthNet(AppCalculationUtils.calculateSdf(msval, mcval));
			response.setMthAchPer(tval!=0?AppCalculationUtils.calculateAch(response.getMthNet(), mtval):0);
	    	response.setMthSurSlashDeff(AppCalculationUtils.calculateSdf(response.getMthNet(), mtval));
	    	response.setMthLys(mlval);
	    	response.setMthGthPer(mlval!=0?AppCalculationUtils.calculateGth(response.getMthNet(), mlval):0);
	    	response.setMthIncrSale(AppCalculationUtils.calculateSdf(response.getMthNet(), mlval));
	    	response.setMthPmr(AppCalculationUtils.calculatePmr(response.getMthNet(), mfs));
			response.setMthPendingPi(mpval);

			
			response.setCumFs(fs);
			response.setCumBudget(tval);
			response.setCumGross(sval);
			response.setCumCredit(cval);
	    	response.setCumNet(AppCalculationUtils.calculateSdf(sval, cval));
			response.setCumAchPer(tval!=0?AppCalculationUtils.calculateAch(response.getCumNet(), tval):0);
	    	response.setCumSurSlashDef(AppCalculationUtils.calculateSdf(response.getCumNet(), tval));
	    	response.setCumLys(lval);
	    	response.setCumGthPer(lval!=0?AppCalculationUtils.calculateGth(response.getCumNet(), lval):0);
	    	response.setCumIncrSale(AppCalculationUtils.calculateSdf(response.getCumNet(), lval));
	    	response.setCumPmr(AppCalculationUtils.calculatePmr(response.getCumNet(), fs));
			response.setCumPendingPi(pval);
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



