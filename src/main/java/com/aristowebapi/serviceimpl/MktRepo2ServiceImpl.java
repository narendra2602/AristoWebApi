package com.aristowebapi.serviceimpl;

import static java.util.stream.Collectors.toMap;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

			else if(request.getUtype()==4 && request.getDepoCode()>0)
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
			
		Map<Integer,Double> rankAchMap =null;
		Map<Integer,Integer> rankPmrMap =null;
		
		Map<Integer,Integer> rankMap =null;
		Map<Integer,Integer> rankMapPmr =null;
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
				rankAchMap = new LinkedHashMap<Integer, Double>();
				rankPmrMap = new LinkedHashMap<Integer, Integer>();
				rankMap = new LinkedHashMap<Integer, Integer>();
				rankMapPmr = new LinkedHashMap<Integer, Integer>();
				first=false;
			}
			
			if(mgrp!=data.getMgrp())
			{
				response=new MktRepo2Response();
				response.setCode(mgrp);
				response.setName(gpname);
				response.setPack("");
		    	response.setBudget(tval);
		    	response.setSale(sval);
		    	response.setAchPer(tval!=0?AppCalculationUtils.calculateAch(sval, tval):0);
		    	response.setSurSlashDef(sval-tval);
		    	response.setCumBudget(cumtval);
		    	response.setCumSale(cumsval);
 				response.setCumAchPer(cumtval!=0?AppCalculationUtils.calculateAch(cumsval, cumtval):0);
		    	response.setCumSurSlashDef(cumsval-cumtval);
		    	response.setLyr(cumlysval);
		    	response.setGthPer(cumlysval!=0?AppCalculationUtils.calculateGth(cumsval, cumlysval):0.0);
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
		    	response.setBudget(data.getTargetqty());
		    	response.setSale(data.getSaleqty());
		    	response.setAchPer(data.getTargetqty()!=0?AppCalculationUtils.calculateAch(data.getSaleqty(), data.getTargetqty()):0);
		    	response.setSurSlashDef(data.getSaleqty()-data.getTargetqty());
		    	response.setCumBudget(data.getCumtarqty());
		    	response.setCumSale(data.getCummsaleqty());
 				response.setCumAchPer(data.getCumtarqty()!=0?AppCalculationUtils.calculateAch(data.getCummsaleqty(), data.getCumtarqty()):0);
		    	response.setCumSurSlashDef(data.getCummsaleqty()-data.getCumtarqty());
		    	response.setLyr(data.getCumlysqty());
		    	response.setGthPer(data.getCumlysqty()!=0?AppCalculationUtils.calculateGth(data.getCummsaleqty(), data.getCumlysqty()):0.0);
		    	response.setPmr(data.getnrep()!=0?AppCalculationUtils.calculatePmr(data.getCummsaleqty(), data.getnrep()):0);
		    	rankAchMap.put(data.getMcode(),response.getCumAchPer());
		    	rankPmrMap.put(data.getMcode(),response.getPmr());
		    }
		    else if (request.getUv()==2)
		    {
		    	response.setBudget(data.getTargetval());
		    	response.setSale(data.getSaleval());
		    	response.setAchPer(data.getTargetval()!=0?AppCalculationUtils.calculateAch(data.getSaleval(), data.getTargetval()):0);
		    	response.setSurSlashDef(data.getSaleval()-data.getTargetval());
		    	response.setCumBudget(data.getCumtarval());
		    	response.setCumSale(data.getCummsaleval());
 				response.setCumAchPer(data.getCumtarval()!=0?AppCalculationUtils.calculateAch(data.getCummsaleval(), data.getCumtarval()):0);
		    	response.setCumSurSlashDef(data.getCummsaleval()-data.getCumtarval());
		    	response.setLyr(data.getCumlysval());
		    	response.setGthPer(data.getCumlysval()!=0?AppCalculationUtils.calculateGth(data.getCummsaleval(), data.getCumlysval()):0.0);
		    	response.setPmr(data.getnrep()!=0?AppCalculationUtils.calculatePmr(data.getCummsaleval(), data.getnrep()):0);
		    	rankAchMap.put(data.getMcode(),response.getCumAchPer());
		    	rankPmrMap.put(data.getMcode(),response.getPmr());
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
		
		

        
        rankMap=getRankMap(rankAchMap,rankMap);
        rankMapPmr=getRankMapPmr(rankPmrMap,rankMapPmr);

	        size=saleList.size();
	       
			for (int i=0;i<size;i++)
			{
				MktRepo2Response mkt = saleList.get(i);
				mkt.setRankAch(mkt.getColor()==0?rankMap.get(mkt.getCode()):0);
				mkt.setRankPmr(mkt.getColor()==0?rankMapPmr.get(mkt.getCode()):0);
				saleList.set(i,mkt);
			}		
        
		if(!first)
		{
			response=new MktRepo2Response();
			response.setCode(mgrp);
			response.setName(gpname);
			response.setPack("");
			response.setBudget(tval);
			response.setSale(sval);
			response.setAchPer(tval!=0?AppCalculationUtils.calculateAch(sval, tval):0);
			response.setSurSlashDef(sval-tval);
			response.setCumBudget(cumtval);
			response.setCumSale(cumsval);
			response.setCumAchPer(cumtval!=0?AppCalculationUtils.calculateAch(cumsval, cumtval):0);
			response.setCumSurSlashDef(cumsval-cumtval);
			response.setLyr(cumlysval);
			response.setGthPer(cumlysval!=0?AppCalculationUtils.calculateGth(cumsval, cumlysval):0.0);
			response.setPmr(nrep!=0?AppCalculationUtils.calculatePmr(cumsval, nrep):0);
			response.setColor(1);

			saleList.add(response);


			response=new MktRepo2Response();
			response.setCode(0);
			response.setName("GRAND TOTAL");
			response.setPack("");
			response.setBudget(gtval);
			response.setSale(gsval);
			response.setAchPer(gtval!=0?AppCalculationUtils.calculateAch(gsval, gtval):0);
			response.setSurSlashDef(gsval-gtval);
			response.setCumBudget(gcumtval);
			response.setCumSale(gcumsval);
			response.setCumAchPer(gcumtval!=0?AppCalculationUtils.calculateAch(gcumsval, gcumtval):0);
			response.setCumSurSlashDef(gcumsval-gcumtval);
			response.setLyr(gcumlysval);
			response.setGthPer(gcumlysval!=0?AppCalculationUtils.calculateGth(gcumsval, gcumlysval):0.0);
			response.setPmr(nrep!=0?AppCalculationUtils.calculatePmr(gcumsval, nrep):0);
			response.setColor(2);

			saleList.add(response);
		}

		
		ApiResponse<MktRepo2Response> apiResponse = new ApiResponse<>(title!=null?title.toString():"", size,lupdate,saleList);
		return apiResponse;
		
		} catch (Exception e) {
			logger.error(AristoWebLogMsgConstant.MKT_REPORT_SERVICE_021,"getMktRepo2");
			e.printStackTrace();
			throw new ApiException(e.getMessage());
		}
		

	}


	private Map<Integer,Integer> getRankMap(Map<Integer,Double> rankAchMap,Map<Integer,Integer> rankMap)
	{
		// let's sort this map by values first 
		Map<Integer, Double> sorted  = rankAchMap.entrySet().stream().sorted(Collections.reverseOrder(Map.Entry.comparingByValue())).collect(toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2, LinkedHashMap::new));
		
		// get all entries from the LinkedHashMap
        Set<Map.Entry<Integer, Double> > entrySet  = sorted.entrySet();
 
        // create an iterator
        Iterator<Map.Entry<Integer, Double> > iterator = entrySet.iterator();
 
        int index = 1;
        Integer key = null;
 
        while (iterator.hasNext()) {
 
        	key = (Integer) iterator.next().getKey();
        	rankMap.put(key, index);
            index++;
        }
        
        return rankMap;

	}
	
	private Map<Integer,Integer> getRankMapPmr(Map<Integer,Integer> rankPmrMap,Map<Integer,Integer> rankMap)
	{
		// let's sort this map by values first 
		Map<Integer, Integer> sorted  = rankPmrMap.entrySet().stream().sorted(Collections.reverseOrder(Map.Entry.comparingByValue())).collect(toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2, LinkedHashMap::new));
		
		// get all entries from the LinkedHashMap
        Set<Map.Entry<Integer, Integer> > entrySet  = sorted.entrySet();
 
        // create an iterator
        Iterator<Map.Entry<Integer, Integer> > iterator = entrySet.iterator();
 
        int index = 1;
        Integer key = null;
 
        while (iterator.hasNext()) {
 
        	key = (Integer) iterator.next().getKey();
        	rankMap.put(key, index);
            index++;
        }
        
        return rankMap;

	}

}
