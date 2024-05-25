package com.aristowebapi.serviceimpl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aristowebapi.constant.AristoWebLogMsgConstant;
import com.aristowebapi.constant.AristoWebMessageConstant;
import com.aristowebapi.dao.IqviaDao;
import com.aristowebapi.dto.Iqvia;
import com.aristowebapi.dto.MktRepo8;
import com.aristowebapi.request.IqviaRequest;
import com.aristowebapi.response.ApiResponse;
import com.aristowebapi.response.IqviaResponse;
import com.aristowebapi.response.MktRepo8Response;
import com.aristowebapi.service.IqviaService;

@Service
public class IqviaServiceImpl implements IqviaService{

	Logger logger = LoggerFactory.getLogger(IqviaServiceImpl.class);
	private final String monthArray[] = new String[] {"","OCT","NOV","DEC","JAN","FEB","MAR","APR","MAY","JUN","JUL","AUG","SEP"};
	
	@Autowired
	private IqviaDao iqviaDao;

	@Autowired
	private AristoWebMessageConstant  aristoWebMessageConstant;

	private String getTitle(IqviaRequest request,Iqvia data)
	{

		
		StringBuilder title=new StringBuilder();
		title.append(aristoWebMessageConstant.divisionMap.get(String.valueOf(data.getDiv_code())));
		title.append(" PRODUCT MATRIX  ");
		title.append(request.getDepoCode()==0?"All India":iqviaDao.getBranch(request.getDepoCode())+" Branch: -");
		title.append(monthArray[request.getMon()]);
		title.append(" - ");
		title.append(request.getMyear());
		title.append(" - TOP 10 ");
		return title.toString();

	}

	
	
	@Override
	public ApiResponse<IqviaResponse> getIqvia(IqviaRequest request) {
		logger.info(AristoWebLogMsgConstant.IQVIA_SERVICE,"getIqvia");
		
		List<Iqvia> IqviaList=null;
		int size = 0;
		IqviaList=iqviaDao.getIqvia(request.getMyear(),request.getDivCode(),request.getDepoCode(),request.getMon());
		size = IqviaList.size();
		
		logger.info("size of the data is {}",size);
	
		IqviaResponse response=null;
	List<IqviaResponse> saleList = new ArrayList();

	boolean first=true;
	String title=null;
	long  sval=0;
	long  eval=0;
	long  bval=0;
	long  tval=0;

	for (int i=0;i<size;i++)
	{
		Iqvia data = IqviaList.get(i);
		
		if(data.getDepo_code()==0)
			continue;
		
		if(first)
		{
			title = getTitle(request, data);
			first=false;
		}
		
		response=new IqviaResponse();


		
		response.setRankLy(data.getRl());
		response.setRankCy(data.getRc());
		response.setProduct(data.getProduct());
		response.setCompany(data.getCompany());
		response.setLauch(data.getLaunch());
		response.setValInLacLy(data.getVall());
		response.setValInLacCy(data.getValc());
		response.setMsLy(data.getMsl());
		response.setMsCy(data.getMsc());
		response.setGthLy(data.getGthl());
		response.setGthCy(data.getGthc());
		response.setValInLacsQ1(data.getValq1());
		response.setValInLacsQ2(data.getValq2());
		response.setValInLacsQ3(data.getValq3());
		response.setValInLacsQ4(data.getValq4());
		response.setMsQ1(data.getMsq1());
		response.setMsQ2(data.getMsq2());
		response.setMsQ3(data.getMsq3());
		response.setMsQ4(data.getMsq4());
		response.setGthQ1(data.getGthq1());
		response.setGthQ2(data.getGthq2());
		response.setGthQ3(data.getGthq3());
		response.setGthQ4(data.getGthq4());
		if(data.getCompany().contains("ARISTO PHARMA"))
			response.setColor(1);
		else
			response.setColor(0);
			
		
		
    	saleList.add(response);

    	

	} //end of for loop

		

	ApiResponse<IqviaResponse> apiResponse = new ApiResponse<>(title!=null?title.toString():"", size,saleList);
	return apiResponse;

	}

}
