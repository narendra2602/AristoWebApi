package com.aristowebapi.serviceimpl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aristowebapi.constant.AristoWebLogMsgConstant;
import com.aristowebapi.constant.AristoWebMessageConstant;
import com.aristowebapi.dao.MisRepo31Dao;
import com.aristowebapi.dto.MisRepo31;
import com.aristowebapi.request.MisRepo31Request;
import com.aristowebapi.response.ApiResponse;
import com.aristowebapi.response.MisRepo31Response;
import com.aristowebapi.service.MisRepo31Service;
import com.aristowebapi.utility.AppCalculationUtils;

@Service
public class MisRepo31ServiceImpl implements MisRepo31Service{

	Logger logger = LoggerFactory.getLogger(MisRepo31ServiceImpl.class);
	
	@Autowired
	private MisRepo31Dao misRepo31Dao;

	@Autowired
	private AristoWebMessageConstant  aristoWebMessageConstant;
	
	private String getTitle(MisRepo31Request request,MisRepo31 data)
	{
		StringBuilder title=new StringBuilder();
		title.append(aristoWebMessageConstant.divisionMap.get(String.valueOf(data.getDiv_code())));
		title.append(request.getDepoCode()==0?"ALL INDIA":misRepo31Dao.getBranch(request.getDepoCode())+" Branch: ");
		title.append(request.getHqCode()==0?"":" H.Q.: "+data.getTer_name());
		title.append(" From ");
		title.append(data.getSmname());
		title.append(" To ");
		title.append(data.getEmname());
		title.append(" For The Marketing Year - ");
		title.append(request.getMyear());
		title.append("-");
		title.append(request.getMyear()+1);

		return title.toString();

	}

	
	
	@Override
	public ApiResponse<MisRepo31Response> getMisRepo31(MisRepo31Request request) {
		logger.info(AristoWebLogMsgConstant.MIS_REPO31_SERVICE,"getMisRepo31");
		List<MisRepo31> MisRepo31List=null;
		int size = 0;

		if(request.getUtype()==3 && request.getDepoCode()==0)
		{
			
			MisRepo31List=misRepo31Dao.getMisRepo31AllIndiaPmt(request.getMyear(),request.getDivCode(),request.getDepoCode()
					,request.getSmon(),request.getEmon(),request.getUtype(),request.getLoginId(),request.getHqCode());
		}
		else if(request.getUtype()==3 && request.getDepoCode()>0)
			MisRepo31List=misRepo31Dao.getMisRepo31Pmt(request.getMyear(),request.getDivCode(),request.getDepoCode()
					,request.getSmon(),request.getEmon(),request.getUtype(),request.getLoginId(),request.getHqCode());
		else if(request.getHqCode()>0)
		MisRepo31List=misRepo31Dao.getMisRepo31Hq(request.getMyear(),request.getDivCode(),request.getDepoCode()
				,request.getSmon(),request.getEmon(),request.getUtype(),request.getLoginId(),request.getHqCode());
		else
		MisRepo31List=misRepo31Dao.getMisRepo31Branch(request.getMyear(),request.getDivCode(),request.getDepoCode()
				,request.getSmon(),request.getEmon(),request.getUtype(),request.getLoginId(),request.getHqCode());

		
		size = MisRepo31List.size();
		logger.info("size of the data is {}",size);
	
		MisRepo31Response response=null;
	List<MisRepo31Response> saleList = new ArrayList();
	long netvalue=0;
	long expvalue=0;
	boolean first=true;
	String title=null;
	for (int i=0;i<size;i++)
	{
		MisRepo31 data = MisRepo31List.get(i);
		
		
		if(first)
		{
			title = getTitle(request, data);
			first=false;
		}
		
		response=new MisRepo31Response();

		response.setProduct(data.getMname());
		response.setPack(data.getPack());
		
		response.setBudgetUnit(data.getBudget_unit());
		response.setBudgetValue(data.getBudget_val());
		response.setInvSalesUnit(data.getSale_unit());
		response.setPiSalesUnit(data.getPisale_unit());
		response.setBonusUnit(data.getBonus_unit());
		response.setGrossSaleValue(data.getGross_sale_val());
		response.setSaleableReturn(data.getSaleable_return());
		response.setExpiryReturn(data.getExpiry());
		response.setBrkgReturn(data.getBrkg_return());
		response.setRateDiffCn(data.getPd_cn());
		response.setLossInTransitCn(data.getLoss_in_transit());
		response.setOtherReturn(data.getOther_unit());
		response.setTotalCnQty(data.getTotal_cn_unit());
		response.setTotalCnValue(data.getTotal_cn_value());
		response.setNetUnit(data.getNet_unit());
		response.setNetValue(data.getNet_value());
		response.setAchPer(data.getAch());
		response.setColor(0);
		if(data.getMname().contains("Total"))
		{
			response.setColor(1);
			netvalue=data.getNet_value();
		}
		if(data.getMname().contains("200%"))
		{
			response.setColor(2);
			expvalue=data.getExp_value();
		}
		saleList.add(response);


	} //end of for loop

	response=new MisRepo31Response();

	response.setProduct("Sale for Incentive :");
	response.setPack("");
	/*	
	response.setBudgetUnit(0);
	response.setBudgetValue(0);
	response.setInvSalesUnit(0);
	response.setPiSalesUnit(0);
	response.setBonusUnit(0);
	response.setGrossSaleValue(0);
	response.setSaleableReturn(data.getSaleable_return());
	response.setExpiryReturn(data.getExpiry());
	response.setBrkgReturn(data.getBrkg_return());
	response.setRateDiffCn(data.getPd_cn());
	response.setLossInTransitCn(data.getLoss_in_transit());
	response.setOtherReturn(data.getOther_unit());
	response.setTotalCnQty(data.getTotal_cn_unit());
	response.setTotalCnValue(data.getTotal_cn_value());
	response.setNetUnit(data.getNet_unit());
	response.setNetValue(AppCalculationUtils.roundToDecimal2Places(netvalue+expvalue));
	response.setAchPer(data.getAch());*/
     response.setNetValue(netvalue+expvalue);
	response.setColor(3);
	saleList.add(response);
	
	ApiResponse<MisRepo31Response> apiResponse = new ApiResponse<>(title!=null?title.toString():"", size,saleList);
	return apiResponse;


		
	}

}
