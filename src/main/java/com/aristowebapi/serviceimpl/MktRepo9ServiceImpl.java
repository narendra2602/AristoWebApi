package com.aristowebapi.serviceimpl;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aristowebapi.constant.AristoWebLogMsgConstant;
import com.aristowebapi.dao.MktRepo9Dao;
import com.aristowebapi.dto.MktRepo4;
import com.aristowebapi.dto.MktRepo9;
import com.aristowebapi.dto.MonthDto;
import com.aristowebapi.request.MktRepo9Request;
import com.aristowebapi.response.ApiResponse;
import com.aristowebapi.response.MktRepo4Response;
import com.aristowebapi.response.MktRepo9Response;
import com.aristowebapi.service.MktRepo9Service;

@Service
public class MktRepo9ServiceImpl  implements MktRepo9Service{

    Logger logger = LoggerFactory.getLogger(MktRepo9ServiceImpl.class);
	
	@Autowired
	private MktRepo9Dao mktRepo9Dao;

	private String getTitle(MktRepo9Request request,MktRepo9 data)
	{
		StringBuilder title=new StringBuilder();
		title.append(request.getDepoCode()==0?"All India":mktRepo9Dao.getBranch(request.getDepoCode())+" Branch: ");
		title.append(request.getGpCode()!=0?" Group -> "+data.getGp_name():"");
		title.append(" Trend ");
		return title.toString();

	}

	
	@Override
	public ApiResponse<MktRepo9Response> getMktRepo9(MktRepo9Request request) {

		logger.info(AristoWebLogMsgConstant.MKT_REPORT_SERVICE_09,"getMktRepo9");
		String title=null;
		
		List<MonthDto> monthData = mktRepo9Dao.getAllMonth(request.getMyear());
		int sz=monthData.size();
		int k=0;
		int z=0;


		List<MktRepo9> MktRepo9SaleList=null;

			MktRepo9SaleList=mktRepo9Dao.getWebReportGroupProductSummary(request.getMyear(),request.getDivCode(),request.getDepoCode()
					,request.getSmon(),request.getEmon(),request.getUtype(),request.getLoginId(),request.getGpCode(),request.getHqCode());
		
		MktRepo9Response response=null;
		
		List<MktRepo9Response> saleList = new ArrayList();

		Map<String, Long> total=null;

		
		Map<String, Long> sales=null;
		Map<String, Long> tgt=null;
		Map<String, Long> lys=null;
		Map<String, Integer> fs=null;
		Map<String, Long> incr=null;
		Map<String, Double> ach=null;
		Map<String, Double> gth=null;
		Map<String, Double> pmr=null;
		Map<String, Long> sd=null;

		
		boolean first=true;
		int size = MktRepo9SaleList.size();
		
		logger.info("size of the data is {}",size);
		//create ReportTitleResponse class object and set title with Report heading
		int i=0;
		int pcode=0;
		String mname="";
		for (i=0;i<size;i++)
		{
			MktRepo9 data = MktRepo9SaleList.get(i);
			if(first)
			{
				response=new MktRepo9Response();
				pcode=data.getSprd_cd();
				mname=data.getMname()+","+data.getPack();
				sales=new LinkedHashMap();
				tgt=new LinkedHashMap();
				lys=new LinkedHashMap();
				fs=new LinkedHashMap();
				incr=new LinkedHashMap();
				ach=new LinkedHashMap();
				gth=new LinkedHashMap();
				pmr=new LinkedHashMap();
				sd=new LinkedHashMap();
				total=new LinkedHashMap();

				first=false;
				title = getTitle(request, data); 
			}

			if(pcode!=data.getSprd_cd())
			{
				for(int b=k;b<sz;b++)
				{
					MonthDto mn=monthData.get(b);
//					months.put(mn.getMnth_abbr(), 0L);
					fs.put(mn.getMnth_abbr(), 0);
					tgt.put(mn.getMnth_abbr(), 0L);
					sales.put(mn.getMnth_abbr(), 0L);
					lys.put(mn.getMnth_abbr(), 0L);
					incr.put(mn.getMnth_abbr(), 0L);
					ach.put(mn.getMnth_abbr(), 0.00);
					gth.put(mn.getMnth_abbr(), 0.00);
					pmr.put(mn.getMnth_abbr(), 0.00);
					sd.put(mn.getMnth_abbr(), 0L);
					k++;
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

				sd.put("TOTAL", salesColumnTotal-tgtColumnTotal);
				response.setSd(sd);
				
				

				response.setName(mname);
				response.setFs(fs);
				response.setTarget(tgt);
				response.setSales(sales);
				response.setLys(lys);
				response.setIncr(incr);
				response.setAch(ach);
				response.setGth(gth);
				response.setPmr(pmr);
				response.setSd(sd);
				saleList.add(response);
				
				response=new MktRepo9Response();
				pcode=data.getSprd_cd();
				mname=data.getMname()+","+data.getPack();
				sales=new LinkedHashMap();
				tgt=new LinkedHashMap();
				lys=new LinkedHashMap();
				fs=new LinkedHashMap();
				incr=new LinkedHashMap();
				ach=new LinkedHashMap();
				gth=new LinkedHashMap();
				pmr=new LinkedHashMap();
				sd=new LinkedHashMap();
				total=new LinkedHashMap();
				k=0;

			}
			
			// before put please check depo code in branch list if not found put 0 value in map otherwise actual zero
			for(int b=k;b<sz;b++)
			{
				MonthDto mn=monthData.get(b);
				if(mn.getMnth_code()==data.getMnth_code())
				{
					fs.put(data.getMnth_abbr(), data.getFs());
					tgt.put(data.getMnth_abbr(), data.getTgt_val());
					sales.put(data.getMnth_abbr(), data.getSales_val());
					lys.put(data.getMnth_abbr(), data.getLys_val());
					incr.put(data.getMnth_abbr(), data.getIncr_val());
					ach.put(data.getMnth_abbr(), data.getTgt_val()!=0?Math.round(((data.getSales_val()*1.0/data.getTgt_val())*100)*100.0)/100.0:0.00);
					gth.put(data.getMnth_abbr(), data.getLys_val()!=0?Math.round((((data.getSales_val()*1.0/data.getLys_val())*100)-100)*100.0)/100.0:0.00);
					pmr.put(data.getMnth_abbr(), data.getFs()!=0?Math.round((data.getSales_val()*1.0/data.getFs())*100.0)/100.0:0.00);
					sd.put(data.getMnth_abbr(), data.getSales_val()-data.getTgt_val());

					
					if(total.containsKey(data.getMnth_abbr()))
					{
						long ggval = total.get(data.getMnth_abbr())+data.getSales_val();
						total.put(data.getMnth_abbr(), ggval);
					}
					else
					{
						total.put(data.getMnth_abbr(), data.getSales_val());
					}

					k++;
					break;
				}
				else
				{
					fs.put(data.getMnth_abbr(), 0);
					tgt.put(data.getMnth_abbr(), 0L);
					sales.put(data.getMnth_abbr(), 0L);
					lys.put(data.getMnth_abbr(), 0L);
					incr.put(data.getMnth_abbr(), 0L);
					ach.put(data.getMnth_abbr(), 0.00);
					gth.put(data.getMnth_abbr(), 0.00);
					pmr.put(data.getMnth_abbr(), 0.00);
					sd.put(data.getMnth_abbr(), 0L);


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

		
		for(int b=k;b<sz;b++)
		{
			MonthDto mn=monthData.get(b);
			fs.put(mn.getMnth_abbr(), 0);
			tgt.put(mn.getMnth_abbr(), 0L);
			sales.put(mn.getMnth_abbr(), 0L);
			lys.put(mn.getMnth_abbr(), 0L);
			incr.put(mn.getMnth_abbr(), 0L);
			ach.put(mn.getMnth_abbr(), 0.00);
			gth.put(mn.getMnth_abbr(), 0.00);
			pmr.put(mn.getMnth_abbr(), 0.00);
			sd.put(mn.getMnth_abbr(), 0L);
			k++;
		}

		
		response.setName(mname);
		response.setFs(fs);
		response.setTarget(tgt);
		response.setSales(sales);
		response.setLys(lys);
		response.setIncr(incr);
		response.setAch(ach);
		response.setGth(gth);
		response.setPmr(pmr);
		response.setSd(sd);
		saleList.add(response);

		
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

		sd.put("TOTAL", salesColumnTotal-tgtColumnTotal);
		response.setSd(sd);

		saleList.add(response);
		return new ApiResponse<MktRepo9Response>(title.toString(),saleList);
	}

}