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
import com.aristowebapi.constant.AristoWebMessageConstant;
import com.aristowebapi.dao.MktRepo9Dao;
import com.aristowebapi.dto.MktRepo9;
import com.aristowebapi.dto.MonthDto;
import com.aristowebapi.request.MktRepo9Request;
import com.aristowebapi.response.ApiResponse;
import com.aristowebapi.response.MktRepo9Response;
import com.aristowebapi.service.MktRepo9Service;
import com.aristowebapi.utility.AppCalculationUtils;

@Service
public class MktRepo9ServiceImpl  implements MktRepo9Service{


	
    Logger logger = LoggerFactory.getLogger(MktRepo9ServiceImpl.class);
	
	@Autowired
	private MktRepo9Dao mktRepo9Dao;
	
	@Autowired
	private AristoWebMessageConstant  aristoWebMessageConstant;

	public String lupdate="";
	
	private String getTitle(MktRepo9Request request,MktRepo9 data)
	{
		StringBuilder title=new StringBuilder();
		title.append(aristoWebMessageConstant.divisionMap.get(String.valueOf(data.getDiv_code())));
		if(request.getHqCode()==0)
			title.append(request.getDepoCode()==0?"All India":mktRepo9Dao.getBranch(request.getDepoCode())+" Branch: ");
		else
		{
			title.append(mktRepo9Dao.getBranch(request.getDepoCode())+" Branch: ");
			title.append(" ");
			title.append(data.getBrname());
		}	
		title.append(request.getGpCode()!=0?" Group -> "+data.getGp_name():"");
		title.append(request.getUv()==1?" Unit Wise Trend ":" Value Wise Trend ");
		title.append(" For The Marketing Year - ");
		title.append(request.getMyear());
		return title.toString();

	}

	
	@Override
	public ApiResponse<MktRepo9Response> getMktRepo9(MktRepo9Request request) {

		logger.info(AristoWebLogMsgConstant.MKT_REPORT_SERVICE_09,"getMktRepo9");
		String title=null;
		
		List<MonthDto> monthData = mktRepo9Dao.getAllMonth(request.getMyear());
		int sz=monthData.size();
		sz=request.getEmon();
		int k=0;
		int z=0;
		long tgtColumnTotal1=0;
		long salesColumnTotal1=0;
		long lysColumnTotal1=0;
		int fsColumnTotal1=0;
		List<MktRepo9> MktRepo9SaleList=null;

			System.out.println("request type "+request.getRepType());
			if(request.getRepType()==2)
			MktRepo9SaleList=mktRepo9Dao.getWebReportGroupProductSummary(request.getMyear(),request.getDivCode(),request.getDepoCode()
					,request.getSmon(),request.getEmon(),request.getUtype(),request.getLoginId(),request.getGpCode(),request.getHqCode());
			else
				MktRepo9SaleList=mktRepo9Dao.getWebReportGroupSummary(request.getMyear(),request.getDivCode(),request.getDepoCode()
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
		Map<String, Integer> pmr=null;
		Map<String, Long> sd=null;

		
		boolean first=true;
		int size = MktRepo9SaleList.size();
		
		logger.info("size of the data is {}",size);
		//create ReportTitleResponse class object and set title with Report heading
		int i=0;
		int pcode=0;
		String mname="";
		String gpname="";
		for (i=0;i<size;i++)
		{
			MktRepo9 data = MktRepo9SaleList.get(i);
			if(first)
			{
				response=new MktRepo9Response();
				pcode=data.getSprd_cd();
				if(request.getRepType()==1)
					mname=data.getGp_name();
				else
					mname=data.getMname()+","+data.getPack();
				gpname=data.getGp_name();	
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
					pmr.put(mn.getMnth_abbr(), 0);
					sd.put(mn.getMnth_abbr(), 0L);
					k++;
				}

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

//				ach.put("TOTAL", Math.round(((salesColumnTotal*1.0/tgtColumnTotal)*100)*100.0)/100.0);
				ach.put("TOTAL", AppCalculationUtils.calculateAch(salesColumnTotal, tgtColumnTotal));
				response.setAchPer(ach);

//				gth.put("TOTAL", Math.round((((salesColumnTotal*1.0/lysColumnTotal)*100)-100)*100.0)/100.0);
				gth.put("TOTAL",AppCalculationUtils.calculateGth(salesColumnTotal, lysColumnTotal));
				response.setGthPer(gth);

//				pmr.put("TOTAL", fsColumnTotal!=0?Math.round((salesColumnTotal*1.0/fsColumnTotal)*100.0)/100.0:0.00);
				pmr.put("TOTAL", AppCalculationUtils.calculatePmr(salesColumnTotal,fsColumnTotal));
				response.setPmr(pmr);

				sd.put("TOTAL", salesColumnTotal-tgtColumnTotal);
				response.setSurSlashdef(sd);
				
				

				response.setName(mname);
				response.setFs(fs);
				response.setBudget(tgt);
				response.setSales(sales);
				response.setLys(lys);
				response.setIncrSale(incr);
				response.setAchPer(ach);
				response.setGthPer(gth);
				response.setPmr(pmr);
				response.setSurSlashdef(sd);
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
					if(request.getUv()==1)
					{
						fs.put(data.getMnth_abbr(), data.getFs());
						tgt.put(data.getMnth_abbr(), data.getTqty());
						sales.put(data.getMnth_abbr(), data.getSqty());
						lys.put(data.getMnth_abbr(), data.getLqty());
						incr.put(data.getMnth_abbr(), data.getIncr_qty());
						ach.put(data.getMnth_abbr(), AppCalculationUtils.calculateAch(data.getSqty(), data.getTqty()));
						gth.put(data.getMnth_abbr(), AppCalculationUtils.calculateGth(data.getSqty(), data.getTqty()));
						pmr.put(data.getMnth_abbr(), AppCalculationUtils.calculatePmr(data.getSqty(), data.getFs()));
						sd.put(data.getMnth_abbr(), data.getSqty()-data.getTqty());
					}
					else
					{	
						fs.put(data.getMnth_abbr(), data.getFs());
						tgt.put(data.getMnth_abbr(), data.getTgt_val());
						sales.put(data.getMnth_abbr(), data.getSales_val());
						lys.put(data.getMnth_abbr(), data.getLys_val());
						incr.put(data.getMnth_abbr(), data.getIncr_val());
						ach.put(data.getMnth_abbr(), AppCalculationUtils.calculateAch(data.getSales_val(), data.getTgt_val()));
						gth.put(data.getMnth_abbr(), AppCalculationUtils.calculateGth(data.getSales_val(), data.getLys_val()));
						pmr.put(data.getMnth_abbr(), AppCalculationUtils.calculatePmr(data.getSales_val(), data.getFs()));
						sd.put(data.getMnth_abbr(), data.getSales_val()-data.getTgt_val());
					}	
					
					
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
					pmr.put(data.getMnth_abbr(), 0);
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
			pmr.put(mn.getMnth_abbr(), 0);
			sd.put(mn.getMnth_abbr(), 0L);
			k++;
		}

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

//		ach.put("TOTAL", Math.round(((salesColumnTotal*1.0/tgtColumnTotal)*100)*100.0)/100.0);
		ach.put("TOTAL", AppCalculationUtils.calculateAch(salesColumnTotal, tgtColumnTotal));
		response.setAchPer(ach);

//		gth.put("TOTAL", Math.round((((salesColumnTotal*1.0/lysColumnTotal)*100)-100)*100.0)/100.0);
		gth.put("TOTAL",AppCalculationUtils.calculateGth(salesColumnTotal, lysColumnTotal));
		response.setGthPer(gth);

//		pmr.put("TOTAL", fsColumnTotal!=0?Math.round((salesColumnTotal*1.0/fsColumnTotal)*100.0)/100.0:0.00);
		pmr.put("TOTAL", AppCalculationUtils.calculatePmr(salesColumnTotal,fsColumnTotal));
		response.setPmr(pmr);

		sd.put("TOTAL", salesColumnTotal-tgtColumnTotal);
		response.setSurSlashdef(sd);
		

		response.setName(mname);
		response.setFs(fs);
		response.setBudget(tgt);
		response.setSales(sales);
		response.setLys(lys);
		response.setIncrSale(incr);
		response.setAchPer(ach);
		response.setGthPer(gth);
		response.setPmr(pmr);
		response.setSurSlashdef(sd);
		saleList.add(response);

/*		if(request.getRepType()==2)
		{

			response.setName(gpname);

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


			ach.put("TOTAL", AppCalculationUtils.calculateAch(salesColumnTotal, tgtColumnTotal));
			response.setAch(ach);

			gth.put("TOTAL",AppCalculationUtils.calculateGth(salesColumnTotal, lysColumnTotal));
			response.setGth(gth);

			pmr.put("TOTAL", AppCalculationUtils.calculatePmr(salesColumnTotal,fsColumnTotal));
			response.setPmr(pmr);

			sd.put("TOTAL", salesColumnTotal-tgtColumnTotal);
			response.setSd(sd);
			response.setColor(1);

			saleList.add(response);
		}
*/		
		return new ApiResponse<MktRepo9Response>(title.toString(),size,lupdate,saleList);
	}

}
