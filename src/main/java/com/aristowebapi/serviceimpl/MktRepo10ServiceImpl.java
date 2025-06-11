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
import com.aristowebapi.dao.MktRepo10Dao;
import com.aristowebapi.dto.MktRepo10;
import com.aristowebapi.dto.MonthDto;
import com.aristowebapi.request.MktRepo10Request;
import com.aristowebapi.response.ApiResponse;
import com.aristowebapi.response.MktRepo9Response;
import com.aristowebapi.service.MktRepo10Service;
import com.aristowebapi.utility.AppCalculationUtils;

@Service
public class MktRepo10ServiceImpl implements MktRepo10Service {

Logger logger = LoggerFactory.getLogger(MktRepo9ServiceImpl.class);
	
	@Autowired
	private MktRepo10Dao mktRepo10Dao;

	@Autowired
	private AristoWebMessageConstant  aristoWebMessageConstant;


	
	public String lupdate="";
	
	private String getTitle(MktRepo10Request request,MktRepo10 data)
	{
		StringBuilder title=new StringBuilder();

		title.append(aristoWebMessageConstant.divisionMap.get(String.valueOf(data.getDiv_code())));
		title.append(request.getDepoCode()==0?"All India":mktRepo10Dao.getBranch(request.getDepoCode())+" Branch: ");
		title.append(" Rupees Wise Sales Analysis Trend ");
		title.append(" For The Marketing Year - ");
		title.append(request.getMyear());
		title.append("-");
		title.append(request.getMyear()+1);

		return title.toString();

	}


	
	
	@Override
	public ApiResponse<MktRepo9Response> getMktRepo10(MktRepo10Request request) {

		logger.info(AristoWebLogMsgConstant.MKT_REPORT_SERVICE_10,"getMktRepo10");
		String title=null;
		
		List<MonthDto> monthData = mktRepo10Dao.getAllMonth(request.getMyear());
		int sz=monthData.size();
		sz=request.getEmon();
		int k=0;
		int z=0;


		List<MktRepo10> MktRepo10SaleList=null;

			MktRepo10SaleList=mktRepo10Dao.getWebReportBrHqTrend(request.getMyear(),request.getDivCode(),request.getDepoCode()
					,request.getSmon(),request.getEmon(),request.getUtype(),request.getLoginId());
		
			System.out.println(request.getMyear()+" "+request.getDivCode()+" "+request.getDepoCode()
			+" "+request.getSmon()+" "+request.getEmon()+" "+request.getUtype()+" "+request.getLoginId());
		MktRepo9Response response=null;   // response is same for repor 9 and 10
		
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
		int size = MktRepo10SaleList.size();
		
		logger.info("size of the data is {}",size);
		//create ReportTitleResponse class object and set title with Report heading
		int i=0;
		int depo_code=0;
		String mname="";

		for (i=0;i<size;i++)
		{
			MktRepo10 data = MktRepo10SaleList.get(i);
			if(data.getDepo_code()==0)
				continue;
			if(first)
			{
				response=new MktRepo9Response();
				depo_code=data.getDepo_code();
				mname=data.getDepo_Name();
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

			if(depo_code!=data.getDepo_code())
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

				ach.put("TOTAL", Math.round(((salesColumnTotal*1.0/tgtColumnTotal)*100)*100.0)/100.0);
				response.setAchPer(ach);

				gth.put("TOTAL", Math.round((((salesColumnTotal*1.0/lysColumnTotal)*100)-100)*100.0)/100.0);
				response.setGthPer(gth);

				pmr.put("TOTAL", fsColumnTotal!=0?Math.round((salesColumnTotal/fsColumnTotal)):0);
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
				depo_code=data.getDepo_code();
				mname=data.getDepo_Name();
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
//					pmr.put(data.getMnth_abbr(), data.getFs()!=0?Math.round((data.getSales_val()/data.getFs())):0);
					pmr.put(data.getMnth_abbr(), AppCalculationUtils.calculatePmr(data.getSales_val(),data.getFs()));
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

//		pmr.put("TOTAL", fsColumnTotal!=0?Math.round((salesColumnTotal*1.0/fsColumnTotal)*100.0)/100.0:0.00);
		pmr.put("TOTAL", AppCalculationUtils.calculatePmr(salesColumnTotal,fsColumnTotal));

		response.setPmr(pmr);

		sd.put("TOTAL", salesColumnTotal-tgtColumnTotal);
		response.setSurSlashdef(sd);

		saleList.add(response);
		return new ApiResponse<MktRepo9Response>(title.toString(),size,lupdate,saleList);
	}

}
