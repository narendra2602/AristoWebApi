package com.aristowebapi.serviceimpl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aristowebapi.constant.AristoWebLogMsgConstant;
import com.aristowebapi.constant.AristoWebMessageConstant;
import com.aristowebapi.dao.MKtRepo2Dao;
import com.aristowebapi.dto.MktRepo2;
import com.aristowebapi.exception.ApiException;
import com.aristowebapi.request.MktRepo2Request;
import com.aristowebapi.response.ApiResponse;
import com.aristowebapi.response.MktRepo2Response;
import com.aristowebapi.service.MktRepo2Service;
import com.aristowebapi.utility.AppCalculationUtils;

@Service
public class MktRepo2ServiceImpl implements MktRepo2Service {
	


	
	Logger logger = LoggerFactory.getLogger(MktRepo2ServiceImpl.class);
	@Autowired
	private MKtRepo2Dao mktRepo2Dao;

	@Autowired
	private AristoWebMessageConstant  aristoWebMessageConstant;

	
	public String lupdate="";
	
	private String getTitle(MktRepo2Request request,MktRepo2 data)
	{

		
		StringBuilder title=new StringBuilder();
		title.append(aristoWebMessageConstant.divisionMap.get(String.valueOf(data.getDiv_code())));
		title.append(request.getDepoCode()==0?"All India ":data.getBrname());
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
			
			
			if(request.getUtype()==2 && request.getDepoCode()==0)
				{
				MktRepo2List=mktRepo2Dao.getWebMkt2All(request.getMyear(),request.getDivCode(),request.getDepoCode()
						,request.getSmon(),request.getEmon(),request.getUtype(),request.getLoginId(),request.getHqCode());
				}
			else if((request.getUtype()==2 || request.getUtype()==1 ) && request.getDepoCode()>0 && request.getHqCode()==0)
			{
			MktRepo2List=mktRepo2Dao.getWebMkt2(request.getMyear(),request.getDivCode(),request.getDepoCode()
					,request.getSmon(),request.getEmon(),request.getUtype(),request.getLoginId(),request.getHqCode());
			}

			else if((request.getUtype()==5 || request.getUtype()==2 ) && request.getDepoCode()>0 && request.getHqCode()>0)
			{
				MktRepo2List=mktRepo2Dao.getWebMkt2Hq(request.getMyear(),request.getDivCode(),request.getDepoCode()
						,request.getSmon(),request.getEmon(),request.getUtype(),request.getLoginId(),request.getHqCode());
			}

			
			else if(request.getUtype()==5 && request.getDepoCode()>0)
			{
			MktRepo2List=mktRepo2Dao.getWebMkt2(request.getMyear(),request.getDivCode(),request.getDepoCode()
					,request.getSmon(),request.getEmon(),request.getUtype(),request.getLoginId(),request.getHqCode());
			}

			
			else if(request.getUtype()==3 && request.getDepoCode()==0)
			{
			MktRepo2List=mktRepo2Dao.getWebMkt2AllPmt(request.getMyear(),request.getDivCode(),request.getDepoCode()
					,request.getSmon(),request.getEmon(),request.getUtype(),request.getLoginId(),request.getHqCode());
			}

			else if(request.getUtype()==3 && request.getHqCode()>0)
			{
			MktRepo2List=mktRepo2Dao.getWebMkt2HqPmt(request.getMyear(),request.getDivCode(),request.getDepoCode()
					,request.getSmon(),request.getEmon(),request.getUtype(),request.getLoginId(),request.getHqCode());
			}

			else if(request.getUtype()==3 && request.getDepoCode()>0)
			{
			MktRepo2List=mktRepo2Dao.getWebMkt2Pmt(request.getMyear(),request.getDivCode(),request.getDepoCode()
					,request.getSmon(),request.getEmon(),request.getUtype(),request.getLoginId(),request.getHqCode());
			}

			else if(request.getUtype()==5 && request.getDepoCode()==0)
			{
			MktRepo2List=mktRepo2Dao.getWebMkt5(request.getMyear(),request.getDivCode(),request.getDepoCode()
					,request.getSmon(),request.getEmon(),request.getUtype(),request.getLoginId(),request.getHqCode());
			}

			else if(request.getUtype()==4 && request.getDepoCode()==0)
			{
			MktRepo2List=mktRepo2Dao.getWebMkt2Hq4(request.getMyear(),request.getDivCode(),request.getDepoCode()
					,request.getSmon(),request.getEmon(),request.getUtype(),request.getLoginId(),request.getHqCode());
			}

			else if(request.getUtype()==4 && request.getDepoCode()>0 && request.getHqCode()==0)
			{
			MktRepo2List=mktRepo2Dao.getWebMkt2Depo4(request.getMyear(),request.getDivCode(),request.getDepoCode()
					,request.getSmon(),request.getEmon(),request.getUtype(),request.getLoginId(),request.getHqCode());
			}

			else if(request.getHqCode()>0)
			{
			MktRepo2List=mktRepo2Dao.getWebMkt2Hq(request.getMyear(),request.getDivCode(),request.getDepoCode()
					,request.getSmon(),request.getEmon(),request.getUtype(),request.getLoginId(),request.getHqCode());
			}

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
		    	response.setAch(tval!=0?AppCalculationUtils.calculateAch(sval, tval):0);
		    	response.setSd(sval-tval);
		    	response.setCummTgt(cumtval);
		    	response.setCummSale(cumsval);
 				response.setCummAch(cumtval!=0?AppCalculationUtils.calculateAch(cumsval, cumtval):0);
		    	response.setCummSd(cumsval-cumtval);
		    	response.setLyr(cumlysval);
		    	response.setGrowth(cumlysval!=0?AppCalculationUtils.calculateGth(cumsval, cumlysval):0.0);
		    	response.setPmr(nrep!=0?AppCalculationUtils.calculatePmr(cumsval, nrep):0);
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
			response.setCode(data.getMcode());
			response.setName(data.getMname());
			response.setPack(data.getPack());

		    if (request.getUv()==1)
		    {
		    	response.setTgt(data.getTargetqty());
		    	response.setSale(data.getSaleqty());
		    	response.setAch(data.getTargetqty()!=0?AppCalculationUtils.calculateAch(data.getSaleqty(), data.getTargetqty()):0);
		    	response.setSd(data.getSaleqty()-data.getTargetqty());
		    	response.setCummTgt(data.getCumtarqty());
		    	response.setCummSale(data.getCummsaleqty());
 				response.setCummAch(data.getCumtarqty()!=0?AppCalculationUtils.calculateAch(data.getCummsaleqty(), data.getCumtarqty()):0);
		    	response.setCummSd(data.getCummsaleqty()-data.getCumtarqty());
		    	response.setLyr(data.getCumlysqty());
		    	response.setGrowth(data.getCumlysqty()!=0?AppCalculationUtils.calculateGth(data.getCummsaleqty(), data.getCumlysqty()):0.0);
		    	response.setPmr(data.getnrep()!=0?AppCalculationUtils.calculatePmr(data.getCummsaleqty(), data.getnrep()):0);
		    }
		    else if (request.getUv()==2)
		    {
		    	response.setTgt(data.getTargetval());
		    	response.setSale(data.getSaleval());
		    	response.setAch(data.getTargetval()!=0?AppCalculationUtils.calculateAch(data.getSaleval(), data.getTargetval()):0);
		    	response.setSd(data.getSaleval()-data.getTargetval());
		    	response.setCummTgt(data.getCumtarval());
		    	response.setCummSale(data.getCummsaleval());
 				response.setCummAch(data.getCumtarval()!=0?AppCalculationUtils.calculateAch(data.getCummsaleval(), data.getCumtarval()):0);
		    	response.setCummSd(data.getCummsaleval()-data.getCumtarval());
		    	response.setLyr(data.getCumlysval());
		    	response.setGrowth(data.getCumlysval()!=0?AppCalculationUtils.calculateGth(data.getCummsaleval(), data.getCumlysval()):0.0);
		    	response.setPmr(data.getnrep()!=0?AppCalculationUtils.calculatePmr(data.getCummsaleval(), data.getnrep()):0);
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
			response.setAch(tval!=0?AppCalculationUtils.calculateAch(sval, tval):0);
			response.setSd(sval-tval);
			response.setCummTgt(cumtval);
			response.setCummSale(cumsval);
			response.setCummAch(cumtval!=0?AppCalculationUtils.calculateAch(cumsval, cumtval):0);
			response.setCummSd(cumsval-cumtval);
			response.setLyr(cumlysval);
			response.setGrowth(cumlysval!=0?AppCalculationUtils.calculateGth(cumsval, cumlysval):0.0);
			response.setPmr(nrep!=0?AppCalculationUtils.calculatePmr(cumsval, nrep):0);
			response.setColor(1);

			saleList.add(response);


			response=new MktRepo2Response();
			response.setCode(0);
			response.setName("GRAND TOTAL");
			response.setPack("");
			response.setTgt(gtval);
			response.setSale(gsval);
			response.setAch(gtval!=0?AppCalculationUtils.calculateAch(gsval, gtval):0);
			response.setSd(gsval-gtval);
			response.setCummTgt(gcumtval);
			response.setCummSale(gcumsval);
			response.setCummAch(gcumtval!=0?AppCalculationUtils.calculateAch(gcumsval, gcumtval):0);
			response.setCummSd(gcumsval-gcumtval);
			response.setLyr(gcumlysval);
			response.setGrowth(gcumlysval!=0?AppCalculationUtils.calculateGth(gcumsval, gcumlysval):0.0);
			response.setPmr(nrep!=0?AppCalculationUtils.calculatePmr(gcumsval, nrep):0);
			response.setColor(2);

			saleList.add(response);
		}

		
		ApiResponse<MktRepo2Response> apiResponse = new ApiResponse<>(title!=null?title.toString():"", size,lupdate,saleList);
		return apiResponse;
		
		} catch (Exception e) {
			logger.error(AristoWebLogMsgConstant.MKT_REPORT_SERVICE_021,"getMktRepo2");
			throw new ApiException(e.getMessage());
		}
		

	}


}
