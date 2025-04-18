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
import com.aristowebapi.dao.MktRepo6Dao;
import com.aristowebapi.dto.MktRepo6;
import com.aristowebapi.dto.MonthDto;
import com.aristowebapi.request.MktRepo6Request;
import com.aristowebapi.response.ApiResponse;
import com.aristowebapi.response.MktRepo6Response;
import com.aristowebapi.service.MktRepo6Service;
import com.aristowebapi.utility.AppCalculationUtils;

@Service
public class MktRepo6ServiceImpl implements MktRepo6Service  {


	Logger logger = LoggerFactory.getLogger(MktRepo6ServiceImpl.class);
	
	@Autowired
	private MktRepo6Dao mktRepo6Dao;
	
	@Autowired
	private AristoWebMessageConstant  aristoWebMessageConstant;

	
	public String lupdate="";
	
	private String getTitle(MktRepo6Request request,MktRepo6 data)
	{
		
		StringBuilder title=new StringBuilder();

		title.append(aristoWebMessageConstant.divisionMap.get(String.valueOf(data.getDiv_code())));
		title.append(request.getDepoCode()==0?"All India ":mktRepo6Dao.getBranch(request.getDepoCode())+" Branch: " );
		title.append(request.getUv()==1?" Unit Wise ":" Value Wise ");
		title.append(request.getGpCode()==0?" All Groups ": data.getGp_name());
		title.append(" Branch Wise Sales Trend ");
		title.append(" For The Marketing Year - ");
		title.append(request.getMyear());
		title.append("-");
		title.append(request.getMyear()+1);

		return title.toString();

	}

	
	@Override
	public ApiResponse<MktRepo6Response> getMktRepo6(MktRepo6Request request) {

		logger.info(AristoWebLogMsgConstant.MKT_REPORT_SERVICE_06,"getMktRepo6");
		
		List<MonthDto> monthData = mktRepo6Dao.getAllMonth(request.getMyear());
		int sz=monthData.size();
		sz=request.getEmon();
		int k=0;
		int z=0;

		
		String title=null;
		List<MktRepo6> mktRepo6SaleList=null;
		
		if(request.getRepType()==1)
		{
			if(request.getOption()==2 && request.getPackCheckBox()==1)
				mktRepo6SaleList=mktRepo6Dao.getWebReportHqSaleTrendPackAll(request.getMyear(),request.getDivCode(),request.getDepoCode()
						,request.getSmon(),request.getEmon(),request.getUtype(),request.getLoginId(),request.getGpCode());

			else if(request.getOption()==2 && request.getPackCheckBox()!=1)
				mktRepo6SaleList=mktRepo6Dao.getWebReportHqSaleTrendAll(request.getMyear(),request.getDivCode(),request.getDepoCode()
						,request.getSmon(),request.getEmon(),request.getUtype(),request.getLoginId(),request.getGpCode());
			else if(request.getPackCheckBox()==1)
				mktRepo6SaleList=mktRepo6Dao.getWebReportHqSaleTrendPack(request.getMyear(),request.getDivCode(),request.getDepoCode()
						,request.getSmon(),request.getEmon(),request.getUtype(),request.getLoginId(),request.getGpCode());
			else
			{
			mktRepo6SaleList=mktRepo6Dao.getWebReportHqSaleTrend(request.getMyear(),request.getDivCode(),request.getDepoCode()
					,request.getSmon(),request.getEmon(),request.getUtype(),request.getLoginId(),request.getGpCode());
			System.out.println("iske andar aaya");
			
			}
		}
		else
		{
			mktRepo6SaleList=mktRepo6Dao.getWebReportHqTargetTrend(request.getMyear(),request.getDivCode(),request.getDepoCode()
					,request.getSmon(),request.getEmon(),request.getUtype(),request.getLoginId(),request.getGpCode());
		}	
		
		MktRepo6Response response=null;
		
		List<MktRepo6Response> saleList = new ArrayList();

		Map<String, Double> months=null;
		Map<String, Double> branchtotal=null;
		Map<String, Double> total=null;
		
		int size = mktRepo6SaleList.size();
		double columnTotal=0;
		double targetTotal=0;
		double lysTotal=0;
		double achPer=0;
		double gthPer=0;
		double surdef=0;
		double icrsls=0;
		double pmr=0;
		double branchcolumnTotal=0;
		double branchtargetTotal=0;
		double branchlysTotal=0;

		double grandColumnTotal=0;
		double grandtargetTotal=0;
		double grandlysTotal=0;
		
		
		boolean first=true;
		int depo_code=0;
		int sdepo_code=0;
		String name="";
		int fs=0;
		int gfs=0;
		int bfs=0;
		System.out.println("size is "+size);
		String branch="";
		for (int i=0;i<size;i++)
		{
			
			MktRepo6 data = mktRepo6SaleList.get(i);
			

			if(first)
			{
				response=new MktRepo6Response();
				depo_code=data.getDepo_code();
				sdepo_code=data.getSdepo_code();
				name=data.getDepo_name();
				branch=data.getBranch_name();
				months=new LinkedHashMap();
				branchtotal=new LinkedHashMap();
				total=new LinkedHashMap();
				first=false;
				title = getTitle(request, data); 
			}
			
			if(depo_code!=data.getDepo_code())
			{
				
				response.setBranch(branch);
				if(request.getOption()==2 || request.getDepoCode()>0)
					response.setHqName(name);
				else
					response.setHqName("");

				response.setFs(fs);
				z=k;
				for(int b=k;b<sz;b++)
				{
					MonthDto mn=monthData.get(b);
					months.put(mn.getMnth_abbr(), 0.0);
					k++;
				}

				columnTotal=Math.round(columnTotal*100.0)/100.00;
				months.put(request.getUv()==1?"TOTAL":"total", columnTotal);

				if(request.getRepType()==1)
				{
					targetTotal=Math.round(targetTotal*100.0)/100.00;
					months.put(request.getUv()==1?"cumBudget":"cumBudget", targetTotal);
					columnTotal=Math.round(columnTotal*100.0)/100.00;
					months.put(request.getUv()==1?"cumSale":"cumSale", columnTotal);
					lysTotal=Math.round(lysTotal*100.0)/100.00;
					months.put(request.getUv()==1?"cumLys":"cumLys", lysTotal);
					achPer=AppCalculationUtils.calculateAch(columnTotal, targetTotal);
					months.put(request.getUv()==1?"cumAchPe":"cumAchPer", achPer);
					gthPer=AppCalculationUtils.calculateGth(columnTotal, lysTotal);
					months.put(request.getUv()==1?"cumGthPer":"cumGthPer", gthPer);
					surdef=AppCalculationUtils.calculateSdf(columnTotal, targetTotal);
					months.put(request.getUv()==1?"cumSurSlashDef":"cumSurSlashDef", surdef);
					icrsls=Math.round(columnTotal-lysTotal);
					months.put(request.getUv()==1?"cumIncrSale":"cumIncrSale", icrsls);
					pmr=AppCalculationUtils.calculatePmr(columnTotal, fs);
					months.put(request.getUv()==1?"cumPmr":"cumPmr", pmr);
				}
				response.setMonths(months);
				saleList.add(response);
				depo_code=data.getDepo_code();
				name=data.getDepo_name();
				
				fs=0;
				columnTotal=0;
				targetTotal=0;
				lysTotal=0;
				
				k=0;
				response=new MktRepo6Response();
				months=new LinkedHashMap();
				if(request.getOption()==1)
					branch=data.getBranch_name();

			}

			if(sdepo_code!=data.getSdepo_code() && request.getOption()==2)
			{
				response.setBranch(branch);
				response.setHqName("Total");
				response.setFs(bfs);

				
				branchcolumnTotal = branchtotal.values().stream().mapToDouble(d -> d).sum();
				branchcolumnTotal=Math.round(branchcolumnTotal*100.0)/100.00;

				branchtotal.put(request.getUv()==1?"TOTAL":"total", branchcolumnTotal);
				
				if(request.getRepType()==1)
				{
					targetTotal=Math.round(branchtargetTotal*100.0)/100.00;
					branchtotal.put(request.getUv()==1?"cumBudget":"cumBudget", targetTotal);
					columnTotal=Math.round(branchcolumnTotal*100.0)/100.00;
					branchtotal.put(request.getUv()==1?"cumSale":"cumSale", columnTotal);
					lysTotal=Math.round(branchlysTotal*100.0)/100.00;
					branchtotal.put(request.getUv()==1?"cumLys":"cumLys", lysTotal);
					achPer=AppCalculationUtils.calculateAch(columnTotal, targetTotal);
					branchtotal.put(request.getUv()==1?"cumAchPer":"cumAchPer", achPer);
					gthPer=AppCalculationUtils.calculateGth(columnTotal, lysTotal);
					branchtotal.put(request.getUv()==1?"cumGthPer":"cumGthPer", gthPer);
					surdef=AppCalculationUtils.calculateSdf(columnTotal, targetTotal);
					branchtotal.put(request.getUv()==1?"cumSurSlashDef":"cumSurSlashDef", surdef);
					icrsls=columnTotal-lysTotal;
					icrsls=AppCalculationUtils.roundToDecimal2Places(icrsls);
					branchtotal.put(request.getUv()==1?"cumIncrSale":"cumIncrSale", icrsls);
					pmr=AppCalculationUtils.calculatePmr(columnTotal, bfs);
					branchtotal.put(request.getUv()==1?"cumPmr":"cumPmr", pmr);
				}
				response.setMonths(branchtotal);
				response.setColor(1);
				saleList.add(response);
				
				sdepo_code=data.getSdepo_code();
				bfs=0;
				branchcolumnTotal=0;
				branchtargetTotal=0;
				branchlysTotal=0;
				response=new MktRepo6Response();
				branchtotal=new LinkedHashMap();

				branch=data.getBranch_name();


			}

			
			// before put please check depo code in branch list if not found put 0 value in map otherwise actual zero
			for(int b=k;b<sz;b++)
			{
				MonthDto mn=monthData.get(b);
				if(mn.getMnth_code()>data.getMnth_code())
					break;
				if(mn.getMnth_code()==data.getMnth_code())
				{
					if(request.getUv()==1)
					{
						months.put(data.getMnth_abbr(), data.getSales_qty());
						columnTotal = AppCalculationUtils.addDouble(columnTotal, data.getSales_qty());
						targetTotal = AppCalculationUtils.addDouble(targetTotal, data.getTarget_qty());
						branchtargetTotal= AppCalculationUtils.addDouble(branchtargetTotal, data.getTarget_qty());
						lysTotal = AppCalculationUtils.addDouble(lysTotal, data.getLys_qty());
						branchlysTotal = AppCalculationUtils.addDouble(branchlysTotal, data.getLys_qty());
						grandtargetTotal= AppCalculationUtils.addDouble(grandtargetTotal, data.getTarget_qty());
						grandlysTotal = AppCalculationUtils.addDouble(grandlysTotal, data.getLys_qty());

					}
					else
					{
						months.put(data.getMnth_abbr(), data.getSales_val());
						columnTotal = AppCalculationUtils.addDouble(columnTotal, data.getSales_val());
						targetTotal = AppCalculationUtils.addDouble(targetTotal, data.getTarget_val());
						branchtargetTotal= AppCalculationUtils.addDouble(branchtargetTotal, data.getTarget_val());
						lysTotal = AppCalculationUtils.addDouble(lysTotal, data.getLys_val());
						branchlysTotal = AppCalculationUtils.addDouble(branchlysTotal, data.getLys_val());
						grandtargetTotal= AppCalculationUtils.addDouble(grandtargetTotal, data.getTarget_val());
						grandlysTotal = AppCalculationUtils.addDouble(grandlysTotal, data.getLys_val());


					}
					gfs+=data.getFs();
					fs+=data.getFs();
					bfs+=data.getFs();
					if(request.getOption()==2)
					{
						if(branchtotal.containsKey(data.getMnth_abbr()))
						{
							double ggval =0;
							if (request.getUv()==1)
								ggval = AppCalculationUtils.addDouble(branchtotal.get(data.getMnth_abbr()), data.getSales_qty());
							else
								ggval = AppCalculationUtils.addDouble(branchtotal.get(data.getMnth_abbr()), data.getSales_val());
							branchtotal.put(data.getMnth_abbr(), ggval);

							
						}
						else
						{
							if (request.getUv()==1)
								branchtotal.put(data.getMnth_abbr(), data.getSales_qty());
							else
								branchtotal.put(data.getMnth_abbr(), data.getSales_val());
						}
					}
					if(request.getUv()==1)
					{
						if(total.containsKey(data.getMnth_abbr()))
						{
							double ggval =0;
								ggval = AppCalculationUtils.addDouble(total.get(data.getMnth_abbr()), data.getSales_qty());
								total.put(data.getMnth_abbr(), ggval);
						}
						else
						{
								total.put(data.getMnth_abbr(), data.getSales_qty());
						}
					}
					if(request.getUv()==2)
					{
						if(request.getUv()==2 && total.containsKey(data.getMnth_abbr()))
						{
							double ggval =0;
							ggval = AppCalculationUtils.addDouble(total.get(data.getMnth_abbr()), data.getSales_val());
							total.put(data.getMnth_abbr(), ggval);
							
						}
						else
						{
								total.put(data.getMnth_abbr(), data.getSales_val());
						}
					}
				
					
					k++;
					break;
				}
				else
				{
					months.put(mn.getMnth_abbr(), 0.0);
					if(request.getOption()==2)
					{
						if (request.getUv()==1)
						{
							if(branchtotal.containsKey(mn.getMnth_abbr()))
							{
								// do nothing
							}
							else
							{
								branchtotal.put(mn.getMnth_abbr(), 0.0);

							}
						}
						
						if (request.getUv()==2)
						{
							if(branchtotal.containsKey(mn.getMnth_abbr()))
							{
								// do nothing
							}
							else
							{
								branchtotal.put(mn.getMnth_abbr(), 0.0);

							}
						}

					}
					if (request.getUv()==1)
					{

						if(total.containsKey(mn.getMnth_abbr()))
						{
							// do nothing
						}
						else
						{
							total.put(mn.getMnth_abbr(), 0.0);

						}
					}
					if (request.getUv()==2)
					{

						if(total.containsKey(mn.getMnth_abbr()))
						{
							// do nothing
						}
						else
						{
							total.put(mn.getMnth_abbr(), 0.0);
							

						}
					}				

					k++;
				}
			}
		}
		
		
		response=new MktRepo6Response();
		response.setBranch(branch);
		if(request.getOption()==2 || request.getDepoCode()>0)
			response.setHqName(name);
		else
			response.setHqName("");
		response.setFs(fs);
		z=k;
		for(int b=k;b<sz;b++)
		{
			MonthDto mn=monthData.get(b);
			months.put(mn.getMnth_abbr(), 0.0);
			total.put(mn.getMnth_abbr(), 0.0);
			k++;
		}
		columnTotal=Math.round(columnTotal*100.0)/100.00;
		months.put(request.getUv()==1?"TOTAL":"total", columnTotal);
		if(request.getRepType()==1)
		{
			targetTotal=Math.round(targetTotal*100.0)/100.00;
			months.put(request.getUv()==1?"cumBudget":"cumBudget", targetTotal);
			columnTotal=Math.round(columnTotal*100.0)/100.00;
			months.put(request.getUv()==1?"cumSale":"cumSale", columnTotal);
			lysTotal=Math.round(lysTotal*100.0)/100.00;
			months.put(request.getUv()==1?"cumLys":"cumLys", lysTotal);
			achPer=AppCalculationUtils.calculateAch(columnTotal, targetTotal);
			months.put(request.getUv()==1?"cumAchPe":"cumAchPer", achPer);
			gthPer=AppCalculationUtils.calculateGth(columnTotal, lysTotal);
			months.put(request.getUv()==1?"cumGthPer":"cumGthPer", gthPer);
			surdef=AppCalculationUtils.calculateSdf(columnTotal, targetTotal);
			months.put(request.getUv()==1?"cumSurSlashDef":"cumSurSlashDef", surdef);
			icrsls=Math.round(columnTotal-lysTotal);
			months.put(request.getUv()==1?"cumIncrSale":"cumIncrSale", icrsls);
			pmr=AppCalculationUtils.calculatePmr(columnTotal, fs);
			months.put(request.getUv()==1?"cumPmr":"cumPmr", pmr);
		}
		response.setMonths(months);
		saleList.add(response);
		
		if(request.getOption()==2)
		{
			response=new MktRepo6Response();
			response.setBranch(branch);
			response.setHqName("Total");
			response.setFs(bfs);
			branchcolumnTotal = branchtotal.values().stream().mapToDouble(d -> d).sum();
			branchcolumnTotal=Math.round(branchcolumnTotal*100.0)/100.00;

			branchtotal.put(request.getUv()==1?"TOTAL":"total", branchcolumnTotal);
			if(request.getRepType()==1)
			{
				targetTotal=Math.round(branchtargetTotal*100.0)/100.00;
				branchtotal.put(request.getUv()==1?"cumBudget":"cumBudget", targetTotal);
				columnTotal=Math.round(branchcolumnTotal*100.0)/100.00;
				branchtotal.put(request.getUv()==1?"cumSale":"cumSale", columnTotal);
				lysTotal=Math.round(branchlysTotal*100.0)/100.00;
				branchtotal.put(request.getUv()==1?"cumLys":"cumLys", lysTotal);
				achPer=AppCalculationUtils.calculateAch(columnTotal, targetTotal);
				branchtotal.put(request.getUv()==1?"cumAchPer":"cumAchPer", achPer);
				gthPer=AppCalculationUtils.calculateGth(columnTotal, lysTotal);
				branchtotal.put(request.getUv()==1?"cumGthPer":"cumGthPer", gthPer);
				surdef=AppCalculationUtils.calculateSdf(columnTotal, targetTotal);
				branchtotal.put(request.getUv()==1?"cumSurSlashDef":"cumSurSlashDef", surdef);
				icrsls=columnTotal-lysTotal;
				icrsls=AppCalculationUtils.roundToDecimal2Places(icrsls);
				branchtotal.put(request.getUv()==1?"cumIncrSale":"cumIncrSale", icrsls);
				pmr=AppCalculationUtils.calculatePmr(columnTotal, bfs);
				branchtotal.put(request.getUv()==1?"cumPmr":"cumPmr", pmr);
			}			

			response.setMonths(branchtotal);
			response.setColor(1);
			saleList.add(response);
		}
		
		
		// Final row for "Grand Total"
		response=new MktRepo6Response();
		response.setBranch("Grand Total");
		response.setHqName("");
		response.setFs(gfs);

		
		grandColumnTotal = total.values().stream().mapToDouble(d -> d).sum();
		grandColumnTotal=Math.round(grandColumnTotal*100.0)/100.00;
		
		total.put(request.getUv()==1?"TOTAL":"total", grandColumnTotal);
		if(request.getRepType()==1)
		{
			targetTotal=Math.round(grandtargetTotal*100.0)/100.00;
			total.put(request.getUv()==1?"cumBudget":"cumBudget", targetTotal);
			columnTotal=Math.round(grandColumnTotal*100.0)/100.00;
			total.put(request.getUv()==1?"cumSale":"cumSale", columnTotal);
			lysTotal=Math.round(grandlysTotal*100.0)/100.00;
			total.put(request.getUv()==1?"cumLys":"cumLys", lysTotal);
			achPer=AppCalculationUtils.calculateAch(columnTotal, targetTotal);
			total.put(request.getUv()==1?"cumAchPer":"cumAchPer", achPer);
			gthPer=AppCalculationUtils.calculateGth(columnTotal, lysTotal);
			total.put(request.getUv()==1?"cumGthPer":"cumGthPer", gthPer);
			surdef=AppCalculationUtils.calculateSdf(columnTotal, targetTotal);
			total.put(request.getUv()==1?"cumSurSlashDef":"cumSurSlashDef", surdef);
			icrsls=columnTotal-lysTotal;
			icrsls=AppCalculationUtils.roundToDecimal2Places(icrsls);
			total.put(request.getUv()==1?"cumIncrSale":"cumIncrSale", icrsls);
			pmr=AppCalculationUtils.calculatePmr(columnTotal, gfs);
			total.put(request.getUv()==1?"cumPmr":"cumPmr", pmr);
		}
		
		
		
		response.setMonths(total);  // 🔥 Set the month-wise values
		response.setColor(2);       // Optional: special styling
		saleList.add(response);     // ✅ ADD to output

		return new ApiResponse<MktRepo6Response>(title.toString(),size,lupdate,saleList);


	}
}
