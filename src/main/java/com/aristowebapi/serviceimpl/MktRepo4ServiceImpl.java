     package com.aristowebapi.serviceimpl;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
		Map<String, Double> pmr=null;
		Map<String, Double> sd=null;

		boolean first=true;
		int size = MktRepo4SaleList.size();
		
		logger.info("size of the data is {}",size);
		//create ReportTitleResponse class object and set title with Report heading
		int i=0;
		for (i=0;i<size;i++)
		{
			MktRepo4 data = MktRepo4SaleList.get(i);
			if(data.getDepo_code()==0)
				continue;
			if(first)
			{
				response=new MktRepo4Response();
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

			fs.put(data.getDepo_name(), data.getFs());
			tgt.put(data.getDepo_name(), data.getTgt_val());
			sales.put(data.getDepo_name(), data.getSales_val());
			lys.put(data.getDepo_name(), data.getLys_val());
			incr.put(data.getDepo_name(), data.getIncr_val());
			ach.put(data.getDepo_name(), data.getTgt_val()!=0?Math.round(((data.getSales_val()*1.0/data.getTgt_val())*100)*100.0)/100.0:0.00);
			gth.put(data.getDepo_name(), data.getLys_val()!=0?Math.round((((data.getSales_val()*1.0/data.getLys_val())*100)-100)*100.0)/100.0:0.00);
			pmr.put(data.getDepo_name(), data.getFs()!=0?Math.round((data.getSales_val()*1.0/data.getFs())*100.0)/100.0:0.00);
			sd.put(data.getDepo_name(), Math.round(data.getSales_val()*1.0-data.getTgt_val())*100.00/100.00);
		}		

		int fsColumnTotal = fs.values().stream().mapToInt(d -> d).sum();
		fs.put("TOTAL",fsColumnTotal);
		response.setFs(fs);

		long tgtColumnTotal = tgt.values().stream().mapToLong(d -> d).sum();
		tgt.put("TOTAL",tgtColumnTotal);
		response.setTarget(tgt);

		
		long salesColumnTotal = sales.values().stream().mapToLong(d -> d).sum();
		sales.put("TOTAL",salesColumnTotal);
		response.setSales(sales);

		long lysColumnTotal = lys.values().stream().mapToLong(d -> d).sum();
		lys.put("TOTAL",lysColumnTotal);
		response.setLys(lys);

		long incrColumnTotal = incr.values().stream().mapToLong(d -> d).sum();
		incr.put("TOTAL",incrColumnTotal);
		response.setIncr(incr);

		ach.put("TOTAL", Math.round(((salesColumnTotal*1.0/tgtColumnTotal)*100)*100.0)/100.0);
		response.setAch(ach);

		gth.put("TOTAL", Math.round((((salesColumnTotal*1.0/lysColumnTotal)*100)-100)*100.0)/100.0);
		response.setGth(gth);

		pmr.put("TOTAL", fsColumnTotal!=0?Math.round((salesColumnTotal*1.0/fsColumnTotal)*100.0)/100.0:0.00);
		response.setPmr(pmr);

		sd.put("TOTAL", Math.round(salesColumnTotal*1.0-tgtColumnTotal)*100.00/100.00);
		response.setSd(sd);

		saleList.add(response);
		return new ApiResponse<MktRepo4Response>(title.toString(),size,lupdate,saleList);
	}

}
