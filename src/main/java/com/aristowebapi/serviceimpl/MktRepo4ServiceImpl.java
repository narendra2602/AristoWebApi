package com.aristowebapi.serviceimpl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aristowebapi.constant.AristoWebMessageConstant;
import com.aristowebapi.dao.MktRepo4Dao;
import com.aristowebapi.dto.MktRepo4;
import com.aristowebapi.request.MktRepo4Request;
import com.aristowebapi.response.ApiResponse;
import com.aristowebapi.response.MktRepo4Response;
import com.aristowebapi.service.MktRepo4Service;
import com.aristowebapi.utility.AppCalculationUtils;

import static java.util.stream.Collectors.*;
import static java.util.Map.Entry.*;

@Service
public class MktRepo4ServiceImpl implements MktRepo4Service{
	

	
	Logger logger = LoggerFactory.getLogger(MktRepo4ServiceImpl.class);
	
	@Autowired
	private MktRepo4Dao mktRepo4Dao;
	
	@Autowired
	private AristoWebMessageConstant  aristoWebMessageConstant;

	public String lupdate="";
	
	private String getTitle(MktRepo4Request request,MktRepo4 data)
	{
		
		StringBuilder title=new StringBuilder();

		title.append(aristoWebMessageConstant.divisionMap.get(String.valueOf(data.getDiv_code())));
		title.append(request.getDepoCode()==0?"All India":mktRepo4Dao.getBranch(request.getDepoCode())+" Branch: ");
		title.append(request.getGpCode()!=0?" Group -> "+data.getGp_name():"");
		title.append(" Rupeewise Performance for the Month of ");
		title.append(data.getSmname());
		title.append(" To ");
		title.append(data.getEmname());
		return title.toString();

	}

	
	
	@Override
	public ApiResponse<MktRepo4Response> getMktRepo4(MktRepo4Request request) {
		
		
		String title=null;
		List<MktRepo4> MktRepo4SaleList=null;

		if(request.getGpCode()>0)
		{
			MktRepo4SaleList=mktRepo4Dao.getWebReportAllGroupValue(request.getMyear(),request.getDivCode(),request.getDepoCode()
					,request.getSmon(),request.getEmon(),request.getUtype(),request.getLoginId(),request.getGpCode());
		}
		else
		{	
			MktRepo4SaleList=mktRepo4Dao.getWebReportAllValue(request.getMyear(),request.getDivCode(),request.getDepoCode()
					,request.getSmon(),request.getEmon(),request.getUtype(),request.getLoginId(),request.getGpCode());
		}
		
		MktRepo4Response response=null;
		
		
		
		List<MktRepo4Response> saleList = new ArrayList();

		Map<String, Long> sales=null;
		Map<String, Long> tgt=null;
		Map<String, Long> lys=null;
		Map<String, Integer> fs=null;
		Map<String, Long> incr=null;
		Map<String, Double> ach=null;
		Map<String, Double> gth=null;
		Map<String, Integer> pmr=null;
		Map<String, Double> sd=null;
		Map<String, Integer> rankach=null;
		Map<String, Integer> rankpmr=null;

		boolean first=true;
		int size = MktRepo4SaleList.size();
		
		logger.info("size of the data is {}",size);
		//create ReportTitleResponse class object and set title with Report heading
		int i=0;
		ArrayList<String> decimalKeys = new ArrayList<>();
		boolean second=true;
		for (i=0;i<size;i++)
		{
			MktRepo4 data = MktRepo4SaleList.get(i);
			if(data.getDepo_code()==0)
				continue;
			if(first)
			{
				response=new MktRepo4Response();
				rankach=new LinkedHashMap();
				rankpmr=new LinkedHashMap();
				sales=new LinkedHashMap();
				tgt=new LinkedHashMap();
				lys=new LinkedHashMap();
				fs=new LinkedHashMap();
				incr=new LinkedHashMap();
				ach=new LinkedHashMap();
				gth=new LinkedHashMap();
				pmr=new LinkedHashMap();
				sd=new LinkedHashMap();
				first=false;
				title = getTitle(request, data); 
			}
			
			rankach.put(data.getDepo_name(), 0);
			rankpmr.put(data.getDepo_name(), 0);
			fs.put(data.getDepo_name(), data.getFs());
			tgt.put(data.getDepo_name(), data.getTgt_val());
			sales.put(data.getDepo_name(), data.getSales_val());
			lys.put(data.getDepo_name(), data.getLys_val());
			incr.put(data.getDepo_name(), data.getIncr_val());
			ach.put(data.getDepo_name(), data.getTgt_val()!=0?Math.round(((data.getSales_val()*1.0/data.getTgt_val())*100)*100.0)/100.0:0.00);
			gth.put(data.getDepo_name(), data.getLys_val()!=0?Math.round((((data.getSales_val()*1.0/data.getLys_val())*100)-100)*100.0)/100.0:0.00);
			pmr.put(data.getDepo_name(), data.getFs()!=0?Math.round((data.getSales_val()/data.getFs())):0);
			sd.put(data.getDepo_name(), Math.round(data.getSales_val()*1.0-data.getTgt_val())*100.00/100.00);
		}		

		
		

		rankach=getRank(ach, rankach);
		rankpmr=getRankPmr(pmr, rankpmr);
	
		int fsColumnTotal = fs.values().stream().mapToInt(d -> d).sum();
		fs.put("TOTAL",fsColumnTotal);
		response.setFs(fs);

		long tgtColumnTotal = tgt.values().stream().mapToLong(d -> d).sum();
		tgt.put("TOTAL",tgtColumnTotal);
		response.setBudget(tgt);

		
		long salesColumnTotal = sales.values().stream().mapToLong(d -> d).sum();
		sales.put("TOTAL",salesColumnTotal);
		response.setSales(sales);

		long lysColumnTotal = lys.values().stream().mapToLong(d -> d).sum();
		lys.put("TOTAL",lysColumnTotal);
		response.setLys(lys);

		long incrColumnTotal = incr.values().stream().mapToLong(d -> d).sum();
		incr.put("TOTAL",incrColumnTotal);
		response.setIncrSale(incr);

		ach.put("TOTAL", Math.round(((salesColumnTotal*1.0/tgtColumnTotal)*100)*100.0)/100.0);
		response.setAchPer(ach);

		gth.put("TOTAL", Math.round((((salesColumnTotal*1.0/lysColumnTotal)*100)-100)*100.0)/100.0);
		response.setGthPer(gth);

		pmr.put("TOTAL", fsColumnTotal!=0?Math.round((salesColumnTotal/fsColumnTotal)):0);
		response.setPmr(pmr);

		sd.put("TOTAL", Math.round(salesColumnTotal*1.0-tgtColumnTotal)*100.00/100.00);
		response.setSurSlashdef(sd);

		rankach.put("TOTAL",0);
		response.setRankAch(rankach);
		
		rankpmr.put("TOTAL",0);
		response.setRankPmr(rankpmr);
		
		saleList.add(response);
		return new ApiResponse<MktRepo4Response>(title.toString(),size,saleList);
	}
	
	
	private Map<String,Integer> getRank(Map<String,Double> rank,Map rankMap)
	{
		// let's sort this map by values first 
		//Map<String, Double> sorted = ach.entrySet().stream().sorted(comparingByValue()).collect(toMap(e -> e.getKey(), e -> e.getValue(), (e1, e2) -> e2, LinkedHashMap::new));
		Map<String, Double> sorted  = rank.entrySet().stream().sorted(Collections.reverseOrder(Map.Entry.comparingByValue())).collect(toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2, LinkedHashMap::new));

		 // get all entries from the LinkedHashMap
        Set<Map.Entry<String, Double> > entrySet  = sorted.entrySet();
 
        // create an iterator
        Iterator<Map.Entry<String, Double> > iterator = entrySet.iterator();
 
        int index = 1;
        String key = null;
 
        while (iterator.hasNext()) {
 
        	key = (String) iterator.next().getKey();
        	rankMap.put(key, index);
 
            //iterator.next();
            index++;
        }
        return rankMap;
	}

	private Map<String,Integer> getRankPmr(Map<String,Integer> rank,Map rankMap)
	{
		// let's sort this map by values first 
		//Map<String, Double> sorted = ach.entrySet().stream().sorted(comparingByValue()).collect(toMap(e -> e.getKey(), e -> e.getValue(), (e1, e2) -> e2, LinkedHashMap::new));
		Map<String, Integer> sorted  = rank.entrySet().stream().sorted(Collections.reverseOrder(Map.Entry.comparingByValue())).collect(toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2, LinkedHashMap::new));

		 // get all entries from the LinkedHashMap
        Set<Map.Entry<String, Integer> > entrySet  = sorted.entrySet();
 
        // create an iterator
        Iterator<Map.Entry<String, Integer> > iterator = entrySet.iterator();
 
        int index = 1;
        String key = null;
 
        while (iterator.hasNext()) {
 
        	key = (String) iterator.next().getKey();
        	rankMap.put(key, index);
 
            //iterator.next();
            index++;
        }
        return rankMap;
	}

	
}
