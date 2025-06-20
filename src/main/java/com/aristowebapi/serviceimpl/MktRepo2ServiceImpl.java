package com.aristowebapi.serviceimpl;

import static java.util.stream.Collectors.toMap;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.omg.CORBA.CustomMarshal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aristowebapi.constant.AristoWebLogMsgConstant;
import com.aristowebapi.constant.AristoWebMessageConstant;
import com.aristowebapi.dao.MKtRepo2Dao;
import com.aristowebapi.dto.MktRepo2;
import com.aristowebapi.dto.MktRepo2New;
import com.aristowebapi.dto.MktRepo2NewTrend;
import com.aristowebapi.dto.MktRepo3;
import com.aristowebapi.dto.MonthDto;
import com.aristowebapi.exception.ApiException;
import com.aristowebapi.request.MktRepo2Request;
import com.aristowebapi.response.ApiResponse;
import com.aristowebapi.response.MktRepo2MultipleGroupResponse;
import com.aristowebapi.response.MktRepo2MultipleGroupTrendResponse;
import com.aristowebapi.response.MktRepo2Response;
import com.aristowebapi.response.MktRepo2SelectiveItemResponse;
import com.aristowebapi.response.MktRepo3Response;
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
		ArrayList<String> decimalKeys = new ArrayList<>();
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
				decimalKeys.add("mth_ach_per");
				decimalKeys.add("cum_ach_per");
				decimalKeys.add("cum_gth_per");
				decimalKeys.add("rank_ach");
			}
			
			if(mgrp!=data.getMgrp())
			{
				response=new MktRepo2Response();
				response.setCode(mgrp);
				response.setName(gpname);
				response.setPack("");
		    	response.setMthBudget(tval);
		    	response.setMthSale(sval);
		    	response.setMthAchPer(tval!=0?AppCalculationUtils.calculateAch(sval, tval):0);
		    	response.setMthSurSlashDef(sval-tval);
		    	response.setCumBudget(cumtval);
		    	response.setCumSale(cumsval);
 				response.setCumAchPer(cumtval!=0?AppCalculationUtils.calculateAch(cumsval, cumtval):0);
		    	response.setCumSurSlashDef(cumsval-cumtval);
		    	response.setCumLys(cumlysval);
		    	response.setCumGthPer(cumlysval!=0?AppCalculationUtils.calculateGth(cumsval, cumlysval):0.0);
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
		    	response.setMthBudget(data.getTargetqty());
		    	response.setMthSale(data.getSaleqty());
		    	response.setMthAchPer(data.getTargetqty()!=0?AppCalculationUtils.calculateAch(data.getSaleqty(), data.getTargetqty()):0);
		    	response.setMthSurSlashDef(data.getSaleqty()-data.getTargetqty());
		    	response.setCumBudget(data.getCumtarqty());
		    	response.setCumSale(data.getCummsaleqty());
 				response.setCumAchPer(data.getCumtarqty()!=0?AppCalculationUtils.calculateAch(data.getCummsaleqty(), data.getCumtarqty()):0);
		    	response.setCumSurSlashDef(data.getCummsaleqty()-data.getCumtarqty());
		    	response.setCumLys(data.getCumlysqty());
		    	response.setCumGthPer(data.getCumlysqty()!=0?AppCalculationUtils.calculateGth(data.getCummsaleqty(), data.getCumlysqty()):0.0);
		    	response.setPmr(data.getnrep()!=0?AppCalculationUtils.calculatePmr(data.getCummsaleqty(), data.getnrep()):0);
		    	rankAchMap.put(data.getMcode(),response.getCumAchPer());
		    	rankPmrMap.put(data.getMcode(),response.getPmr());
		    }
		    else if (request.getUv()==2)
		    {
		    	response.setMthBudget(data.getTargetval());
		    	response.setMthSale(data.getSaleval());
		    	response.setMthAchPer(data.getTargetval()!=0?AppCalculationUtils.calculateAch(data.getSaleval(), data.getTargetval()):0);
		    	response.setMthSurSlashDef(data.getSaleval()-data.getTargetval());
		    	response.setCumBudget(data.getCumtarval());
		    	response.setCumSale(data.getCummsaleval());
 				response.setCumAchPer(data.getCumtarval()!=0?AppCalculationUtils.calculateAch(data.getCummsaleval(), data.getCumtarval()):0);
		    	response.setCumSurSlashDef(data.getCummsaleval()-data.getCumtarval());
		    	response.setCumLys(data.getCumlysval());
		    	response.setCumGthPer(data.getCumlysval()!=0?AppCalculationUtils.calculateGth(data.getCummsaleval(), data.getCumlysval()):0.0);
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
			response.setMthBudget(tval);
			response.setMthSale(sval);
			response.setMthAchPer(tval!=0?AppCalculationUtils.calculateAch(sval, tval):0);
			response.setMthSurSlashDef(sval-tval);
			response.setCumBudget(cumtval);
			response.setCumSale(cumsval);
			response.setCumAchPer(cumtval!=0?AppCalculationUtils.calculateAch(cumsval, cumtval):0);
			response.setCumSurSlashDef(cumsval-cumtval);
			response.setCumLys(cumlysval);
			response.setCumGthPer(cumlysval!=0?AppCalculationUtils.calculateGth(cumsval, cumlysval):0.0);
			response.setPmr(nrep!=0?AppCalculationUtils.calculatePmr(cumsval, nrep):0);
			response.setColor(1);

			saleList.add(response);


			response=new MktRepo2Response();
			response.setCode(0);
			response.setName("GRAND TOTAL");
			response.setPack("");
			response.setMthBudget(gtval);
			response.setMthSale(gsval);
			response.setMthAchPer(gtval!=0?AppCalculationUtils.calculateAch(gsval, gtval):0);
			response.setMthSurSlashDef(gsval-gtval);
			response.setCumBudget(gcumtval);
			response.setCumSale(gcumsval);
			response.setCumAchPer(gcumtval!=0?AppCalculationUtils.calculateAch(gcumsval, gcumtval):0);
			response.setCumSurSlashDef(gcumsval-gcumtval);
			response.setCumLys(gcumlysval);
			response.setCumGthPer(gcumlysval!=0?AppCalculationUtils.calculateGth(gcumsval, gcumlysval):0.0);
			response.setPmr(nrep!=0?AppCalculationUtils.calculatePmr(gcumsval, nrep):0);
			response.setColor(2);

			saleList.add(response);
		}

		
		ApiResponse<MktRepo2Response> apiResponse = new ApiResponse<>(title!=null?title.toString():"", size,decimalKeys,saleList);
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


	private String getTitle1(MktRepo2Request request,MktRepo2 data)
	{

		
		StringBuilder title=new StringBuilder();
		title.append(aristoWebMessageConstant.divisionMap.get(String.valueOf(data.getDiv_code())));
		title.append(request.getDepoCode()==0?"All India ":data.getBrname());
		title.append(request.getItemType()==1?" Group : "+data.getMname():" Product : "+data.getMname()+","+data.getPack());
		title.append(request.getUv()==1?" Unit Wise ":" Value Wise ");
		title.append("Detail From  ");
		title.append(data.getSmname());
		title.append(" To ");
		title.append(data.getEmname());
		return title.toString();

	}
	
	@Override
	public ApiResponse<MktRepo2SelectiveItemResponse> getMktRepo2SelectiveItem(MktRepo2Request request) {
		logger.info(AristoWebLogMsgConstant.MKT_REPORT_SERVICE_02,"getMktRepo2");
		List<MktRepo2> MktRepo2List=null;
		int size = 0;
		if(request.getItemType()==1)
			request.setUv(2);
		try {
			
			if(request.getItemType()==1)
				MktRepo2List=mktRepo2Dao.getWebMkt2SelectiveGroup(request.getMyear(),request.getDivCode(),request.getDepoCode()
						,request.getSmon(),request.getEmon(),request.getUtype(),request.getLoginId(),request.getHqCode(),request.getRepType());
				
			else
				MktRepo2List=mktRepo2Dao.getWebMkt2SelectiveCode(request.getMyear(),request.getDivCode(),request.getDepoCode()
						,request.getSmon(),request.getEmon(),request.getUtype(),request.getLoginId(),request.getHqCode(),request.getRepType());
			

			size = MktRepo2List.size();
			
			
			
			
			logger.info("size of the data is {}",size);
		
			
		
			MktRepo2SelectiveItemResponse response=null;
		List<MktRepo2SelectiveItemResponse> saleList = new ArrayList();

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
		int grep=0;

		int nrep=0;
		ArrayList<String> decimalKeys = new ArrayList<>();
		for (int i=0;i<size;i++)
		{
			MktRepo2 data = MktRepo2List.get(i);
			
			if(first)
			{
				title = getTitle1(request, data);
				mgrp=data.getMgrp();
				gpname=data.getGp_name();
				nrep=data.getnrep();
					first=false;
					decimalKeys.add("mth_ach_per");
					decimalKeys.add("cum_ach_per");
					decimalKeys.add("cum_gth_per");
					
			}
			
				
			response=new MktRepo2SelectiveItemResponse();
			if(request.getDepoCode()==0 && request.getRepType()==1)
			{
			response.setBranch(data.getGp_name());
			response.setHqName("");
			}
			else
			{
				response.setBranch(data.getBrname());
				response.setHqName(data.getGp_name());
			}				
				response.setFs(data.getnrep());
		    if (request.getUv()==1)
		    {
		    	
		    	response.setMthBudget(data.getTargetqty());
		    	response.setMthSale(data.getSaleqty());
		    	response.setMthAchPer(data.getTargetqty()!=0?AppCalculationUtils.calculateAch(data.getSaleqty(), data.getTargetqty()):0);
		    	response.setMthSurSlashDef(data.getSaleqty()-data.getTargetqty());
		    	response.setCumBudget(data.getCumtarqty());
		    	response.setCumSale(data.getCummsaleqty());
 				response.setCumAchPer(data.getCumtarqty()!=0?AppCalculationUtils.calculateAch(data.getCummsaleqty(), data.getCumtarqty()):0);
		    	response.setCumSurSlashDef(data.getCummsaleqty()-data.getCumtarqty());
		    	response.setCumLys(data.getCumlysqty());
		    	response.setCumGthPer(data.getCumlysqty()!=0?AppCalculationUtils.calculateGth(data.getCummsaleqty(), data.getCumlysqty()):0.0);
		    	response.setPmr(data.getnrep()!=0?AppCalculationUtils.calculatePmr(data.getCummsaleqty(), data.getnrep()):0);
		    }
		    else if (request.getUv()==2)
		    {
		    	response.setMthBudget(data.getTargetval());
		    	response.setMthSale(data.getSaleval());
		    	response.setMthAchPer(data.getTargetval()!=0?AppCalculationUtils.calculateAch(data.getSaleval(), data.getTargetval()):0);
		    	response.setMthSurSlashDef(data.getSaleval()-data.getTargetval());
		    	response.setCumBudget(data.getCumtarval());
		    	response.setCumSale(data.getCummsaleval());
 				response.setCumAchPer(data.getCumtarval()!=0?AppCalculationUtils.calculateAch(data.getCummsaleval(), data.getCumtarval()):0);
		    	response.setCumSurSlashDef(data.getCummsaleval()-data.getCumtarval());
		    	response.setCumLys(data.getCumlysval());
		    	response.setCumGthPer(data.getCumlysval()!=0?AppCalculationUtils.calculateGth(data.getCummsaleval(), data.getCumlysval()):0.0);
		    	response.setPmr(data.getnrep()!=0?AppCalculationUtils.calculatePmr(data.getCummsaleval(), data.getnrep()):0);
		    }
		    if(data.getBrname().contains("Total"))
		    
		    	response.setColor(1);
	    	saleList.add(response);

	    	
		    if(!data.getBrname().equalsIgnoreCase("Total : "))
		    {
				grep+=data.getnrep();

		    	if(request.getItemType()==2 && request.getUv()==1  )
		    	{
		    	//	System.out.println(data.getTargetqty()+" "+data.getBrname()+" "+data.getGp_name());
		    		tval+=data.getTargetqty();
		    		sval+=data.getSaleqty();
		    		cumtval+=data.getCumtarqty();
		    		cumsval+=data.getCummsaleqty();
		    		cumlysval+=data.getCumlysqty();
		    		gtval+=data.getTargetqty();
		    		gsval+=data.getSaleqty();
		    		gcumtval+=data.getCumtarqty();
		    		gcumsval+=data.getCummsaleqty();
		    		gcumlysval+=data.getCumlysqty();

		    	}
		    	else
		    	{
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
		    	}
		    }
		} //end of for loop
		
		

        
        
		if(!first)
		{
/*			response=new MktRepo2SelectiveItemResponse();
			response.setBranch(gpname);
			response.setHq("");
			response.setMthBudget(tval);
			response.setMthSale(sval);
			response.setMthAchPer(tval!=0?AppCalculationUtils.calculateAch(sval, tval):0);
			response.setMthSurSlashDef(sval-tval);
			response.setCumBudget(cumtval);
			response.setCumSale(cumsval);
			response.setCumAchPer(cumtval!=0?AppCalculationUtils.calculateAch(cumsval, cumtval):0);
			response.setCumSurSlashDef(cumsval-cumtval);
			response.setCumLys(cumlysval);
			response.setGthPer(cumlysval!=0?AppCalculationUtils.calculateGth(cumsval, cumlysval):0.0);
			response.setPmr(nrep!=0?AppCalculationUtils.calculatePmr(cumsval, nrep):0);
			response.setColor(1);

			saleList.add(response);
*/

			response=new MktRepo2SelectiveItemResponse();
			System.out.println("grep "+grep);
			response.setBranch("GRAND TOTAL");
			response.setHqName("");
			response.setFs(grep);
			response.setMthBudget(gtval);
			response.setMthSale(gsval);
			response.setMthAchPer(gtval!=0?AppCalculationUtils.calculateAch(gsval, gtval):0);
			response.setMthSurSlashDef(gsval-gtval);
			response.setCumBudget(gcumtval);
			response.setCumSale(gcumsval);
			response.setCumAchPer(gcumtval!=0?AppCalculationUtils.calculateAch(gcumsval, gcumtval):0);
			response.setCumSurSlashDef(gcumsval-gcumtval);
			response.setCumLys(gcumlysval);
			response.setCumGthPer(gcumlysval!=0?AppCalculationUtils.calculateGth(gcumsval, gcumlysval):0.0);
			response.setPmr(nrep!=0?AppCalculationUtils.calculatePmr(gcumsval, grep):0);
			response.setColor(2);

			saleList.add(response);
		}

		
		ApiResponse<MktRepo2SelectiveItemResponse> apiResponse = new ApiResponse<>(title!=null?title.toString():"", size,decimalKeys,saleList);
		return apiResponse;
		
		} catch (Exception e) {
			logger.error(AristoWebLogMsgConstant.MKT_REPORT_SERVICE_021,"getMktRepo2");
			e.printStackTrace();
			throw new ApiException(e.getMessage());
		}
		
	}

	private String getTitle2(MktRepo2Request request,MktRepo2New data)
	{

		
		StringBuilder title=new StringBuilder();
		title.append(aristoWebMessageConstant.divisionMap.get(String.valueOf(data.getDiv_code())));
		title.append(request.getDepoCode()==0?"All India ":data.getBrname());
		title.append(" Group Wise Detail From ");
		title.append(data.getSmname());
		title.append(" To ");
		title.append(data.getEmname());
		return title.toString();

	}

	
	@Override
	public ApiResponse<MktRepo2MultipleGroupResponse> getMktRepo2MultipleGroup(MktRepo2Request request) {
		logger.info(AristoWebLogMsgConstant.MKT_REPORT_SERVICE_02,"getMktRepo2New");
		List<MktRepo2New> MktRepo2List=null;
		int size = 0;
		try {
			
				MktRepo2List=mktRepo2Dao.getWebMkt2MultipleGroup(request.getMyear(),request.getDivCode(),request.getDepoCode()
						,request.getSmon(),request.getEmon(),request.getUtype(),request.getLoginId(),request.getGroupCode(),request.getRepType());
				

			size = MktRepo2List.size();
			
			
			
			
			logger.info("size of the data is {}",size);
		
			
		
			MktRepo2MultipleGroupResponse response=null;
		List<MktRepo2MultipleGroupResponse> saleList = new ArrayList();

		boolean first=true;
		String title=null;
		int mgrp=0;
		String gpname="";
		long tval=0;
		long  sval=0;
		long  lysval=0;
		long cumtval=0;
		long cumsval=0;
		long cumlysval=0;
		long cumexpiryval=0;
		long gtval=0;
		long  gsval=0;
		long  glysval=0;

		long gcumtval=0;
		long gcumsval=0;
		long gcumlysval=0;
		long gcumexpiryval=0;

		int grep=0;
		int depo_code=0;
		int ter_code=0;
		int nrep=0;
		ArrayList<String> decimalKeys = new ArrayList<>();
		for (int i=0;i<size;i++)
		{
			MktRepo2New data = MktRepo2List.get(i);
			
			if(first)
			{
				response=new MktRepo2MultipleGroupResponse();
				title = getTitle2(request, data);
				mgrp=data.getMgrp();
				gpname=data.getGp_name();
				
					first=false;
					decimalKeys.add("mth_ach_per");
					decimalKeys.add("mth_gth_per");
					decimalKeys.add("cum_ach_per");
					decimalKeys.add("cum_gth_per");
					decimalKeys.add("expiry_ratio");

			}
			
			if(request.getDepoCode()==0  && data.getSdepo_code()!=depo_code )
			{
				
	    		grep+=data.getNrep();
	    		nrep+=data.getNrep();
				depo_code=data.getSdepo_code();
			}
			if(request.getDepoCode()>0 && data.getTerr_cd()!=ter_code  )
			{
				
	    		grep+=data.getNrep();
	    		nrep+=data.getNrep();
				ter_code=data.getTerr_cd();
				
			}
			
			if(mgrp!=data.getMgrp())
			{
				response=new MktRepo2MultipleGroupResponse();
				response.setName(gpname);
				response.setBranch("TOTAL***");
				response.setHqName("");
				response.setFs(nrep);
				response.setMthBudget(tval);
				response.setMthSale(sval);
				response.setMthLys(lysval);
				response.setMthAchPer(tval!=0?AppCalculationUtils.calculateAch(sval, tval):0);
				response.setMthGthPer(lysval!=0?AppCalculationUtils.calculateGth(sval, lysval):0.0);
				response.setMthSurSlashDef(sval-tval);
		    	response.setMthIncr(sval-lysval);

				response.setCumBudget(cumtval);
				response.setCumSale(cumsval);
				response.setCumLys(cumlysval);
				response.setCumExpiry(cumexpiryval);
		    	response.setCumIncr(cumsval-cumlysval);

				response.setCumAchPer(cumtval!=0?AppCalculationUtils.calculateAch(cumsval, cumtval):0);
				response.setCumSurSlashDef(cumsval-cumtval);
				response.setCumGthPer(cumlysval!=0?AppCalculationUtils.calculateGth(cumsval, cumlysval):0.0);
				response.setPmr(nrep!=0?AppCalculationUtils.calculatePmr(cumsval, nrep):0);
		    	response.setLysPmr(nrep!=0?AppCalculationUtils.calculatePmr(cumlysval, nrep):0);
		    	response.setIncrPmr(response.getPmr()-response.getLysPmr());
	    		response.setExpiryRatio(AppCalculationUtils.calculateExpiryRatio(cumexpiryval,cumsval));

				
				response.setColor(1);

				saleList.add(response);
				gpname=data.getGp_name();
				mgrp=data.getMgrp();
				

				
	    		tval=0;
	    		sval=0;
	    		lysval=0;
	    		cumtval=0;
	    		cumsval=0;
	    		cumlysval=0;
	    		cumexpiryval=0;
	    		nrep=0;
			}

    		response=new MktRepo2MultipleGroupResponse();

    		if(request.getDepoCode()==0 && request.getUtype()!=4)			{
				response.setBranch(data.getTer_name());
				response.setHqName("");
				
			}
			else
			{
				response.setBranch(data.getBrname());
				response.setHqName(data.getTer_name());
			}	
    		
    		
				response.setFs(data.getNrep());
				response.setName(data.getPname());
				
				if(data.getPname().contains("TOTAL"))
					response.setColor(1);
//		    if (request.getUv()==1)
		    {
		    	
		    	response.setMthBudget(data.getTargetqty());
		    	response.setMthSale(data.getSaleqty());
		    	response.setMthLys(data.getLysqty());
		    	response.setMthAchPer(data.getTargetqty()!=0?AppCalculationUtils.calculateAch(data.getSaleqty(), data.getTargetqty()):0);
		    	response.setMthGthPer(data.getLysqty()!=0?AppCalculationUtils.calculateGth(data.getSaleqty(), data.getLysqty()):0.0);
		    	response.setMthSurSlashDef(data.getSaleqty()-data.getTargetqty());
		    	response.setMthIncr(data.getSaleqty()-data.getLysqty());

		    	response.setCumBudget(data.getCumtarqty());
		    	response.setCumSale(data.getCummsaleqty());
		    	response.setCumExpiry(data.getCumexpiry_qty());
 				response.setCumAchPer(data.getCumtarqty()!=0?AppCalculationUtils.calculateAch(data.getCummsaleqty(), data.getCumtarqty()):0);
		    	response.setCumSurSlashDef(data.getCummsaleqty()-data.getCumtarqty());
		    	response.setCumIncr(data.getCummsaleqty()-data.getCumlysqty());

		    	response.setCumLys(data.getCumlysqty());
		    	response.setCumGthPer(data.getCumlysqty()!=0?AppCalculationUtils.calculateGth(data.getCummsaleqty(), data.getCumlysqty()):0.0);
		    	response.setPmr(data.getNrep()!=0?AppCalculationUtils.calculatePmr(data.getCummsaleqty(), data.getNrep()):0);
		    	response.setLysPmr(data.getNrep()!=0?AppCalculationUtils.calculatePmr(data.getCumlysqty(), data.getNrep()):0);
		    	response.setIncrPmr(response.getPmr()-response.getLysPmr());
		    	
	    		response.setExpiryRatio(AppCalculationUtils.calculateExpiryRatio(data.getCumexpiry_qty(),data.getCummsaleqty()));
		    }
/*		    else if (request.getUv()==2)
		    {
		    	response.setMthBudget(data.getTargetval());
		    	response.setMthSale(data.getSaleval());
		    	response.setMthAchPer(data.getTargetval()!=0?AppCalculationUtils.calculateAch(data.getSaleval(), data.getTargetval()):0);
		    	response.setMthSurSlashDef(data.getSaleval()-data.getTargetval());
		    	response.setCumBudget(data.getCumtarval());
		    	response.setCumSale(data.getCummsaleval());
 				response.setCumAchPer(data.getCumtarval()!=0?AppCalculationUtils.calculateAch(data.getCummsaleval(), data.getCumtarval()):0);
		    	response.setCumSurSlashDef(data.getCummsaleval()-data.getCumtarval());
		    	response.setCumLys(data.getCumlysval());
		    	response.setGthPer(data.getCumlysval()!=0?AppCalculationUtils.calculateGth(data.getCummsaleval(), data.getCumlysval()):0.0);
		    	response.setPmr(data.getnrep()!=0?AppCalculationUtils.calculatePmr(data.getCummsaleval(), data.getnrep()):0);
		    }
*/		    if(data.getBrname().contains("Total"))
		    
		    	response.setColor(1);
	    	saleList.add(response);

	    	
	    	
		    if(!data.getBrname().equalsIgnoreCase("Total : "))
		    {
		    	
		    	if(data.getPname().contains("TOTAL"))
		    	{

		    	}
		    	else
		    	{
		    		
/*		    		grep+=data.getNrep();
		    		nrep+=data.getNrep();
*/
		    		tval+=data.getTargetval();
		    		sval+=data.getSaleval();
		    		lysval+=data.getLysval();
		    		cumtval+=data.getCumtarval();
		    		cumsval+=data.getCummsaleval();
		    		cumlysval+=data.getCumlysval();
		    		cumexpiryval+=data.getCumexpiry_val();
		    		gtval+=data.getTargetval();
		    		gsval+=data.getSaleval();
		    		glysval+=data.getLysval();
		    		gcumtval+=data.getCumtarval();
		    		gcumsval+=data.getCummsaleval();
		    		gcumlysval+=data.getCumlysval();
		    		gcumexpiryval+=data.getCumexpiry_val();
		    	}
		    }
		} //end of for loop
		
		

        
        
		if(!first)
		{

			response=new MktRepo2MultipleGroupResponse();
			response.setName(gpname);
			response.setBranch("TOTAL***");
			response.setHqName("");
			response.setFs(nrep);
			response.setMthBudget(tval);
			response.setMthSale(sval);
			response.setMthLys(lysval);
			response.setMthAchPer(tval!=0?AppCalculationUtils.calculateAch(sval, tval):0);
			response.setMthGthPer(lysval!=0?AppCalculationUtils.calculateGth(sval, lysval):0.0);
			response.setMthSurSlashDef(sval-tval);
	    	response.setMthIncr(sval-lysval);

			response.setCumBudget(cumtval);
			response.setCumSale(cumsval);
			response.setCumLys(cumlysval);
			response.setCumExpiry(cumexpiryval);
	    	response.setCumIncr(cumsval-cumlysval);

			response.setCumAchPer(cumtval!=0?AppCalculationUtils.calculateAch(cumsval, cumtval):0);
			
			response.setCumSurSlashDef(cumsval-cumtval);
			response.setCumGthPer(cumlysval!=0?AppCalculationUtils.calculateGth(cumsval, cumlysval):0.0);
			response.setPmr(nrep!=0?AppCalculationUtils.calculatePmr(cumsval, nrep):0);
	    	response.setLysPmr(nrep!=0?AppCalculationUtils.calculatePmr(cumlysval, nrep):0);
	    	response.setIncrPmr(response.getPmr()-response.getLysPmr());
    		response.setExpiryRatio(AppCalculationUtils.calculateExpiryRatio(cumexpiryval,cumsval));
			
			response.setColor(1);

			saleList.add(response);

			response=new MktRepo2MultipleGroupResponse();
			response.setName("");
			response.setBranch("GRAND TOTAL");
			response.setHqName("");
			response.setFs(grep);
			response.setMthBudget(gtval);
			response.setMthSale(gsval);
			response.setMthLys(glysval);
			response.setMthAchPer(gtval!=0?AppCalculationUtils.calculateAch(gsval, gtval):0);
			response.setMthGthPer(glysval!=0?AppCalculationUtils.calculateGth(gsval, glysval):0.0);
			response.setMthSurSlashDef(gsval-gtval);
	    	response.setMthIncr(gsval-glysval);

			response.setCumBudget(gcumtval);
			response.setCumSale(gcumsval);
			response.setCumAchPer(gcumtval!=0?AppCalculationUtils.calculateAch(gcumsval, gcumtval):0);
			response.setCumSurSlashDef(gcumsval-gcumtval);
			response.setCumLys(gcumlysval);
			response.setCumExpiry(gcumexpiryval);
	    	response.setCumIncr(gcumsval-gcumlysval);

			response.setCumGthPer(gcumlysval!=0?AppCalculationUtils.calculateGth(gcumsval, gcumlysval):0.0);
			response.setPmr(grep!=0?AppCalculationUtils.calculatePmr(gcumsval, grep):0);
	    	response.setLysPmr(grep!=0?AppCalculationUtils.calculatePmr(gcumlysval, grep):0);
	    	response.setIncrPmr(response.getPmr()-response.getLysPmr());
    		response.setExpiryRatio(AppCalculationUtils.calculateExpiryRatio(gcumexpiryval,gcumsval));
			
			response.setColor(2);

			saleList.add(response);
		}

		
		ApiResponse<MktRepo2MultipleGroupResponse> apiResponse = new ApiResponse<>(title!=null?title.toString():"", size,decimalKeys,saleList);
		return apiResponse;
		
		} catch (Exception e) {
			logger.error(AristoWebLogMsgConstant.MKT_REPORT_SERVICE_021,"getMktRepo2New");
			e.printStackTrace();
			throw new ApiException(e.getMessage());
		}
		
	}

	
	private String getTitle4(MktRepo2Request request,MktRepo2NewTrend data)
	{

		
		StringBuilder title=new StringBuilder();
		title.append(aristoWebMessageConstant.divisionMap.get(String.valueOf(data.getDiv_code())));
		title.append(request.getDepoCode()==0?"All India ":data.getBrname());
		title.append(" Group Wise Detail From : ");
		title.append(data.getSmname());
		title.append(" To ");
		title.append(data.getEmname());
		return title.toString();

	}

	

	@Override
	public ApiResponse<MktRepo2MultipleGroupTrendResponse> getMktRepo2MultipleGroupTrend(MktRepo2Request request) {
		List<MonthDto> monthData = mktRepo2Dao.getAllMonth(request.getMyear());
		int sz=monthData.size();
		sz=request.getEmon();
		int k=0;
		int z=0;

		
		String title=null;
		List<MktRepo2NewTrend> mktRepo2List=null;
		
		mktRepo2List=mktRepo2Dao.getWebMkt2MultipleGroupTrend(request.getMyear(),request.getDivCode(),request.getDepoCode()
				,request.getSmon(),request.getEmon(),request.getUtype(),request.getLoginId(),request.getGroupCode(),request.getRepType());
		
		MktRepo2MultipleGroupTrendResponse response=null;
		List<MktRepo2MultipleGroupTrendResponse> saleList = new ArrayList();
		Map<String, Long> months=null;
		Map<String, Long> group=null;
		Map<String, Long> total=null;

		boolean first=true;

		int size = mktRepo2List.size();
		int pcode=0;
		int mgrp=0;
		long columnTotal=0;
		long groupColumnTotal=0;
		long grandColumnTotal=0;

		String pname=null;
		String pack=null;
		String gname=null;
		String brname=null;
		String hqname=null;
		for (int i=0;i<size;i++)
		{
			MktRepo2NewTrend data = mktRepo2List.get(i);
			
			if(first)
			{
				pcode=data.getSprd_cd();
				pname=data.getPname();
				mgrp=data.getMgrp();
				gname=data.getGp_name();
				brname=data.getBrname();
				hqname=data.getTer_name();
				response=new MktRepo2MultipleGroupTrendResponse();
				months=new LinkedHashMap();
				group=new LinkedHashMap();
				total=new LinkedHashMap();
				first=false;
				
				title = getTitle4(request, data); 
			}

			if(pcode!=data.getSprd_cd())
			{
				response.setName(pname);
				z=k;
				for(int b=k;b<sz;b++)
				{
					MonthDto mn=monthData.get(b);
					months.put(mn.getMnth_abbr(), 0L);
					k++;
				}

				months.put("TOTAL", columnTotal);
				response.setMonths(months);
				
				saleList.add(response);
				pcode=data.getSprd_cd();
				pname=data.getPname();
				brname=data.getBrname();
				hqname=data.getTer_name();
				columnTotal=0;
				
				k=0;
				response=new MktRepo2MultipleGroupTrendResponse();
				months=new LinkedHashMap();

			}

			
			if(mgrp!=data.getMgrp())
			{
				
				response.setName(gname);
				response.setBranch("Total");
				response.setHqName("");
				for(int b=z;b<sz;b++)
				{
					MonthDto mn=monthData.get(b);
					group.put(mn.getMnth_abbr(), 0L);
					z++;
				}

				group.put("TOTAL", groupColumnTotal);

				months.putAll(group);

				
				response.setMonths(months);
				response.setColor(1);
				saleList.add(response);

				
				mgrp=data.getMgrp();
				gname=data.getGp_name();
				
				z=0;
				groupColumnTotal=0;
				response=new MktRepo2MultipleGroupTrendResponse();
				months=new LinkedHashMap();
				group=new LinkedHashMap();
				
			}

			if(request.getDepoCode()==0 && request.getUtype()!=4)
			{
				response.setBranch(data.getTer_name());
				response.setHqName("");
				
			}
			else
			{
				response.setBranch(data.getBrname());
				response.setHqName(data.getTer_name());
			}	
				response.setFs(data.getNrep());
			
			// before put please check depo code in branch list if not found put 0 value in map otherwise actual zero
			for(int b=k;b<sz;b++)
			{
				MonthDto mn=monthData.get(b);
				if(mn.getMnth_code()==data.getMnth_code())
				{
					months.put(data.getMnth_abbr(), request.getUv()==2?data.getSaleval():data.getSaleqty());
					columnTotal+=request.getUv()==2?data.getSaleval():data.getSaleqty();
					groupColumnTotal+=data.getSaleval();
					//grandColumnTotal+=data.getSales_val();
					if(group.containsKey(data.getMnth_abbr()))
					{
						long gval = group.get(data.getMnth_abbr())+data.getSaleval();
						group.put(data.getMnth_abbr(), gval);
					}
					else
					{
						group.put(data.getMnth_abbr(), data.getSaleval());
					}
					
					if(total.containsKey(data.getMnth_abbr()))
					{
						long ggval = total.get(data.getMnth_abbr())+data.getSaleval();
						total.put(data.getMnth_abbr(), ggval);
					}
					else
					{
						total.put(data.getMnth_abbr(), data.getSaleval());
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
			response=new MktRepo2MultipleGroupTrendResponse();
			response.setName(pname);
			if(request.getDepoCode()==0 && request.getUtype()!=4)
			{
				response.setBranch(hqname);
				response.setHqName("");
				
			}
			else
			{
				response.setBranch(brname);
				response.setHqName(hqname);
			}	

			z=k;
			for(int b=k;b<sz;b++)
			{
				MonthDto mn=monthData.get(b);
				months.put(mn.getMnth_abbr(), 0L);
				k++;
			}
			months.put("TOTAL", columnTotal);

			response.setMonths(months);
			saleList.add(response);

			
			months=new LinkedHashMap();
			response=new MktRepo2MultipleGroupTrendResponse();
			response.setName(gname);
			response.setBranch("Total");
			response.setHqName("");
			for(int b=z;b<sz;b++)
			{
				MonthDto mn=monthData.get(b);
				group.put(mn.getMnth_abbr(), 0L);
				total.put(mn.getMnth_abbr(), 0L);
				z++;
			}

			group.put("TOTAL", groupColumnTotal);

			months.putAll(group);
			response.setMonths(months);
			response.setColor(1);
			saleList.add(response);
			
			
			grandColumnTotal = total.values().stream().mapToLong(d -> d).sum();
			
			months=new LinkedHashMap();
			total.put("TOTAL", grandColumnTotal);
//			total.keySet().stream().forEach(d->System.out.print(d));

			months.putAll(total);
			response=new MktRepo2MultipleGroupTrendResponse();
			response.setName("Grand Total");
			response.setBranch("");
			response.setHqName("");
			response.setMonths(months);
			response.setColor(2);
//			saleList.add(response);

			return new ApiResponse<MktRepo2MultipleGroupTrendResponse>(title.toString(),size,saleList);


			
		}

	}



