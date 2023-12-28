package com.aristowebapi.serviceimpl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aristowebapi.constant.AristoWebLogMsgConstant;
import com.aristowebapi.dao.MKtRepo2Dao;
import com.aristowebapi.dto.MktRepo2;
import com.aristowebapi.exception.ApiException;
import com.aristowebapi.exception.DataNotFoundException;
import com.aristowebapi.request.MktRepo2Request;
import com.aristowebapi.response.ApiResponse;
import com.aristowebapi.response.MktRepo2Response;
import com.aristowebapi.service.MktRepo2Service;

@Service
public class MktRepo2ServiceImpl implements MktRepo2Service {
	
	Logger logger = LoggerFactory.getLogger(MktRepo2ServiceImpl.class);
	@Autowired
	private MKtRepo2Dao mktRepo2Dao;

	
	private String getTitle(MktRepo2Request request,MktRepo2 data)
	{
		StringBuilder title=new StringBuilder();
		title.append(data.getBrname());
		title.append(" Product Wise ");
		title.append(request.getUv()==1?"Unit Wise ":"Value Wise ");
		title.append("Detail From  ");
		title.append(data.getSmname());
		title.append(" To ");
		title.append(data.getEmname());
		return title.toString();

	}

	
	@Override
	public ApiResponse<MktRepo2Response> getMktRepo2(MktRepo2Request request) {
		
		logger.info(AristoWebLogMsgConstant.MKT_REPORT_SERVICE_02,"getMktRepo2");
		List<MktRepo2> MktRepo2List=null;
		int size = 0;

		try {
			MktRepo2List=mktRepo2Dao.getWebMkt2All(request.getMyear(),request.getDivCode(),request.getDepoCode()
					,request.getSmon(),request.getEmon(),request.getUtype(),request.getLoginId(),request.getRepType());
			size = MktRepo2List.size();
			logger.info("size of the data is {}",size);
/*			if(size==0)
				throw new DataNotFoundException(AristoWebLogMsgConstant.MKT_REPORT_SERVICE_022);
*/				
		
		MktRepo2Response response=null;
		List<MktRepo2Response> saleList = new ArrayList();

		boolean first=true;
		String title=null;
		int mgrp=0;
		String gpname="";
		long tval=0;
		long  sval=0;
		long cumtval=0;
		long cumsval=0;
		long cumlysval=0;
		long gtval=0;
		long  gsval=0;
		long gcumtval=0;
		long gcumsval=0;
		long gcumlysval=0;

		int nrep=0;
		for (int i=0;i<size;i++)
		{
			MktRepo2 data = MktRepo2List.get(i);
			
			if(first)
			{
				title = getTitle(request, data);
				mgrp=data.getMgrp();
				gpname=data.getGp_name();
				nrep=data.getnrep();
				first=false;
			}
			
			if(mgrp!=data.getMgrp())
			{
				response=new MktRepo2Response();
				response.setCode(mgrp);
				response.setName(gpname);
				response.setPack("");
		    	response.setTgt(tval);
		    	response.setSale(sval);
		    	response.setAch(tval!=0?calculateAch(sval, tval):0);
		    	response.setSd(sval-tval);
		    	response.setCummTgt(cumtval);
		    	response.setCummSale(cumsval);
 				response.setCummAch(cumtval!=0?calculateAch(cumsval, cumtval):0);
		    	response.setCummSd(cumsval-cumtval);
		    	response.setLyr(cumlysval);
		    	response.setGrowth(cumlysval!=0?calculateGth(cumsval, cumlysval):0.0);
		    	response.setPmr(nrep!=0?calculatePmr(cumsval, nrep):0);
		    	response.setColor(1);
		    	saleList.add(response);

				tval=0;
				sval=0;
				cumtval=0;
				cumsval=0;
				cumlysval=0;
		    	
		    	mgrp=data.getMgrp();
				gpname=data.getGp_name();
				nrep=data.getnrep();

			}
			
			response=new MktRepo2Response();
			response.setCode(data.getSprd_cd());
			response.setName(data.getMname());
			response.setPack(data.getPack());

		    if (request.getUv()==1)
		    {
		    	response.setTgt(data.getTargetqty());
		    	response.setSale(data.getSaleqty());
		    	response.setAch(data.getTargetqty()!=0?calculateAch(data.getSaleqty(), data.getTargetqty()):0);
		    	response.setSd(data.getSaleqty()-data.getTargetqty());
		    	response.setCummTgt(data.getCumtarqty());
		    	response.setCummSale(data.getCummsaleqty());
 				response.setCummAch(data.getCumtarqty()!=0?calculateAch(data.getCummsaleqty(), data.getCumtarqty()):0);
		    	response.setCummSd(data.getCummsaleqty()-data.getCumtarqty());
		    	response.setLyr(data.getCumlysqty());
		    	response.setGrowth(data.getCumlysqty()!=0?calculateGth(data.getCummsaleqty(), data.getCumlysqty()):0.0);
		    	response.setPmr(data.getnrep()!=0?calculatePmr(data.getCummsaleqty(), data.getnrep()):0);
		    }
		    else if (request.getUv()==2)
		    {
		    	response.setTgt(data.getTargetval());
		    	response.setSale(data.getSaleval());
		    	response.setAch(data.getTargetval()!=0?calculateAch(data.getSaleval(), data.getTargetval()):0);
		    	response.setSd(data.getSaleval()-data.getTargetval());
		    	response.setCummTgt(data.getCumtarval());
		    	response.setCummSale(data.getCummsaleval());
 				response.setCummAch(data.getCumtarval()!=0?calculateAch(data.getCummsaleval(), data.getCumtarval()):0);
		    	response.setCummSd(data.getCummsaleval()-data.getCumtarval());
		    	response.setLyr(data.getCumlysval());
		    	response.setGrowth(data.getCumlysval()!=0?calculateGth(data.getCummsaleval(), data.getCumlysval()):0.0);
		    	response.setPmr(data.getnrep()!=0?calculatePmr(data.getCummsaleval(), data.getnrep()):0);
		    }
	    	saleList.add(response);

	    	tval+=data.getTargetval();
	    	sval+=data.getSaleval();
	    	cumtval+=data.getCumtarval();
	    	cumsval+=data.getCummsaleval();
	    	cumlysval+=data.getCumlysval();
	    	gtval+=data.getTargetval();
	    	gsval+=data.getSaleval();
	    	gcumtval+=data.getCumtarval();
	    	gcumsval+=data.getCummsaleval();
	    	gcumlysval+=data.getCumlysval();
		} //end of for loop

		if(!first)
		{
			response=new MktRepo2Response();
			response.setCode(mgrp);
			response.setName(gpname);
			response.setPack("");
			response.setTgt(tval);
			response.setSale(sval);
			response.setAch(tval!=0?calculateAch(sval, tval):0);
			response.setSd(sval-tval);
			response.setCummTgt(cumtval);
			response.setCummSale(cumsval);
			response.setCummAch(cumtval!=0?calculateAch(cumsval, cumtval):0);
			response.setCummSd(cumsval-cumtval);
			response.setLyr(cumlysval);
			response.setGrowth(cumlysval!=0?calculateGth(cumsval, cumlysval):0.0);
			response.setPmr(nrep!=0?calculatePmr(cumsval, nrep):0);
			response.setColor(1);

			saleList.add(response);


			response=new MktRepo2Response();
			response.setCode(0);
			response.setName("GRAND TOTAL");
			response.setPack("");
			response.setTgt(gtval);
			response.setSale(gsval);
			response.setAch(gtval!=0?calculateAch(gsval, gtval):0);
			response.setSd(gsval-gtval);
			response.setCummTgt(gcumtval);
			response.setCummSale(gcumsval);
			response.setCummAch(gcumtval!=0?calculateAch(gcumsval, gcumtval):0);
			response.setCummSd(gcumsval-gcumtval);
			response.setLyr(gcumlysval);
			response.setGrowth(gcumlysval!=0?calculateGth(gcumsval, gcumlysval):0.0);
			response.setPmr(nrep!=0?calculatePmr(gcumsval, nrep):0);
			response.setColor(2);

			saleList.add(response);
		}

		
		ApiResponse<MktRepo2Response> apiResponse = new ApiResponse<>(title!=null?title.toString():"", saleList);
		return apiResponse;
		
		} catch (Exception e) {
			logger.error(AristoWebLogMsgConstant.MKT_REPORT_SERVICE_021,"getMktRepo2");
			throw new ApiException(e.getMessage());
		}
		

	}

	
	private int calculateAch(long sale,long target)
	{
		double ach1=0.00;  
		ach1=Math.ceil((sale*1.0/target)*100);
		return ((int) ach1);
	}

	private double calculateGth(long sale,long lys)
	{
		double gth=0.00;  
		gth=Math.round((((sale*1.0/lys)*100)-100)*10.0)/10.0;
		return gth;
	}


	private int calculatePmr(long sale,int nrep)
	{
		double pmr=0.00;  
		pmr=Math.ceil((sale*1.0/nrep));
		return ((int) pmr);
	}

}
