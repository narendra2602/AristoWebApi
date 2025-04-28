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

	private String wmonth;

	
	
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
		double monthlyTarget=0;
		double monthlySale=0;
		double monthlyLys=0;
		double lysTotal=0;

		double bmonthlyTarget=0;
		double bmonthlySale=0;
		double bmonthlyLys=0;

		double tmonthlyTarget=0;
		double tmonthlySale=0;
		double tmonthlyLys=0;
		
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
		ArrayList<String> decimalKeys = new ArrayList<>();
		
		boolean second =true;
		
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
					monthlyTarget=AppCalculationUtils.roundToDecimal2Places(monthlyTarget);
					months.put(wmonth+"_budget", monthlyTarget);
					months.put(wmonth+"_sale", monthlySale);
					months.put(wmonth+"_lys", monthlyLys);
					achPer=AppCalculationUtils.calculateAch(monthlySale, monthlyTarget);
					months.put(wmonth+"_ach_per", achPer);
					gthPer=AppCalculationUtils.calculateGth(monthlySale, monthlyLys);
					months.put(wmonth+"_gth_per", gthPer);
					surdef=AppCalculationUtils.calculateSdf(monthlySale, monthlyTarget);
					months.put(wmonth+"_sur_slash_def", surdef);
					icrsls=monthlySale-monthlyLys;
					icrsls=AppCalculationUtils.roundToDecimal2Places(icrsls);
					months.put(wmonth+"_incr_sale", icrsls);
					pmr=AppCalculationUtils.calculatePmr(monthlySale, fs);
					months.put(wmonth+"_pmr", pmr);
					
					targetTotal=Math.round(targetTotal*100.0)/100.00;
					months.put("cumBudget", targetTotal);
					columnTotal=Math.round(columnTotal*100.0)/100.00;
					months.put("cumSale", columnTotal);
					lysTotal=Math.round(lysTotal*100.0)/100.00;
					months.put("cumLys", lysTotal);
					achPer=AppCalculationUtils.calculateAch(columnTotal, targetTotal);
					months.put("cumAchPer", achPer);
					gthPer=AppCalculationUtils.calculateGth(columnTotal, lysTotal);
					months.put("cumGthPer", gthPer);
					surdef=AppCalculationUtils.calculateSdf(columnTotal, targetTotal);
					months.put("cumSurSlashDef", surdef);
					icrsls=columnTotal-lysTotal;
					icrsls=AppCalculationUtils.roundToDecimal2Places(icrsls);
					months.put("cumIncrSale", icrsls);
					pmr=AppCalculationUtils.calculatePmr(columnTotal, fs);
					months.put("cumPmr", pmr);
				}
				response.setMonths(months);
				saleList.add(response);
				depo_code=data.getDepo_code();
				name=data.getDepo_name();
				
				fs=0;
				columnTotal=0;
				targetTotal=0;
				lysTotal=0;
				bmonthlySale+=monthlySale;
				bmonthlyTarget+=monthlyTarget;
				bmonthlyLys+=monthlyLys;
				tmonthlySale+=monthlySale;
				tmonthlyTarget+=monthlyTarget;
				tmonthlyLys+=monthlyLys;
				monthlySale=0;
				monthlyTarget=0;
				monthlyLys=0;
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
					bmonthlyTarget=AppCalculationUtils.roundToDecimal2Places(bmonthlyTarget);
					branchtotal.put(wmonth+"_budget", bmonthlyTarget);
					bmonthlySale=AppCalculationUtils.roundToDecimal2Places(bmonthlySale);
					branchtotal.put(wmonth+"_sale", bmonthlySale);
					bmonthlyLys=AppCalculationUtils.roundToDecimal2Places(bmonthlyLys);
					branchtotal.put(wmonth+"_lys", bmonthlyLys);
					achPer=AppCalculationUtils.calculateAch(bmonthlySale, bmonthlyTarget);
					branchtotal.put(wmonth+"_ach_per", achPer);
					gthPer=AppCalculationUtils.calculateGth(bmonthlySale, bmonthlyLys);
					branchtotal.put(wmonth+"_gth_per", gthPer);
					surdef=AppCalculationUtils.calculateSdf(bmonthlySale, bmonthlyTarget);
					branchtotal.put(wmonth+"_sur_slash_def", surdef);
					icrsls=bmonthlySale-bmonthlyLys;
					icrsls=AppCalculationUtils.roundToDecimal2Places(icrsls);
					branchtotal.put(wmonth+"_incr_sale", icrsls);
					pmr=AppCalculationUtils.calculatePmr(bmonthlySale, fs);
					branchtotal.put(wmonth+"_pmr", pmr);

					
					targetTotal=Math.round(branchtargetTotal*100.0)/100.00;
					branchtotal.put("cumBudget", targetTotal);
					columnTotal=Math.round(branchcolumnTotal*100.0)/100.00;
					branchtotal.put("cumSale", columnTotal);
					lysTotal=Math.round(branchlysTotal*100.0)/100.00;
					branchtotal.put("cumLys", lysTotal);
					achPer=AppCalculationUtils.calculateAch(columnTotal, targetTotal);
					branchtotal.put("cumAchPer", achPer);
					gthPer=AppCalculationUtils.calculateGth(columnTotal, lysTotal);
					branchtotal.put("cumGthPer", gthPer);
					surdef=AppCalculationUtils.calculateSdf(columnTotal, targetTotal);
					branchtotal.put("cumSurSlashDef", surdef);
					icrsls=columnTotal-lysTotal;
					icrsls=AppCalculationUtils.roundToDecimal2Places(icrsls);
					branchtotal.put("cumIncrSale", icrsls);
					pmr=AppCalculationUtils.calculatePmr(columnTotal, bfs);
					branchtotal.put("cumPmr", pmr);
				}
				response.setMonths(branchtotal);
				response.setColor(1);
				saleList.add(response);
				
				sdepo_code=data.getSdepo_code();
				bfs=0;
				branchcolumnTotal=0;
				branchtargetTotal=0;
				branchlysTotal=0;
				bmonthlySale=0;
				bmonthlyTarget=0;
				bmonthlyLys=0;

				response=new MktRepo6Response();
				branchtotal=new LinkedHashMap();

				branch=data.getBranch_name();


			}

			
			// before put please check depo code in branch list if not found put 0 value in map otherwise actual zero
			for(int b=k;b<sz;b++)
			{
				MonthDto mn=monthData.get(b);
				if(mn.getMnth_code()>data.getMnth_code())
				{
					second=false;
					break;
				}
				wmonth=mn.getMnth_abbr();
				
				if(data.getSales_qty() == (double ) data.getSales_qty() && second)
				{
					// list.add(data.getMnth_abbr());
//					System.out.println(data.getMnth_abbr());
					decimalKeys.add(data.getMnth_abbr());
					if(b==sz-1) 
					{	
						
						decimalKeys.add(wmonth+"_budget");
						decimalKeys.add(wmonth+"_sale");
						decimalKeys.add(wmonth+"_lys");
						decimalKeys.add(wmonth+"_ach_per");
						decimalKeys.add(wmonth+"_gth_per");
						decimalKeys.add(wmonth+"_sur_slash_def");
						decimalKeys.add(wmonth+"_incr_sale");
						decimalKeys.add(wmonth+"_pmr");
						decimalKeys.add("cumBudget");
						decimalKeys.add("cumSale");
						decimalKeys.add("cumLys");
						decimalKeys.add("cumAchPer");
						decimalKeys.add("cumGthPer");
						decimalKeys.add("cumSurSlashDef");
						decimalKeys.add("cumIncrSale");
						decimalKeys.add("cumPmr");

						second=false;
					}
					
				}
				if(mn.getMnth_code()==data.getMnth_code())
				{
					if(request.getUv()==1)
					{
						months.put(data.getMnth_abbr(), data.getSales_qty());
						columnTotal = AppCalculationUtils.addDouble(columnTotal, data.getSales_qty());
						targetTotal = AppCalculationUtils.addDouble(targetTotal, data.getTarget_qty());
						lysTotal = AppCalculationUtils.addDouble(lysTotal, data.getLys_qty());

						branchtargetTotal= AppCalculationUtils.addDouble(branchtargetTotal, data.getTarget_qty());
						branchcolumnTotal= AppCalculationUtils.addDouble(branchcolumnTotal, data.getSales_qty());
						branchlysTotal = AppCalculationUtils.addDouble(branchlysTotal, data.getLys_qty());
						
						grandtargetTotal= AppCalculationUtils.addDouble(grandtargetTotal, data.getTarget_qty());
						grandColumnTotal= AppCalculationUtils.addDouble(grandColumnTotal, data.getSales_qty());
						grandlysTotal = AppCalculationUtils.addDouble(grandlysTotal, data.getLys_qty());
						
						monthlyTarget=data.getTarget_qty();
						monthlySale=data.getSales_qty();
						monthlyLys=data.getLys_qty();

					}
					else
					{
						months.put(data.getMnth_abbr(), data.getSales_val());
						columnTotal = AppCalculationUtils.addDouble(columnTotal, data.getSales_val());
						targetTotal = AppCalculationUtils.addDouble(targetTotal, data.getTarget_val());
						lysTotal = AppCalculationUtils.addDouble(lysTotal, data.getLys_val());

						branchtargetTotal= AppCalculationUtils.addDouble(branchtargetTotal, data.getTarget_val());
						branchcolumnTotal= AppCalculationUtils.addDouble(branchcolumnTotal, data.getSales_val());
						branchlysTotal = AppCalculationUtils.addDouble(branchlysTotal, data.getLys_val());
						
						grandtargetTotal= AppCalculationUtils.addDouble(grandtargetTotal, data.getTarget_val());
						grandColumnTotal= AppCalculationUtils.addDouble(grandColumnTotal, data.getSales_val());
						grandlysTotal = AppCalculationUtils.addDouble(grandlysTotal, data.getLys_val());
						
						monthlyTarget=data.getTarget_val();
						monthlySale=data.getSales_val();
						monthlyLys=data.getLys_val();


					}
					gfs+=data.getFs();
					fs+=data.getFs();
					bfs+=data.getFs();
					if(request.getOption()==2)
					{
						if(branchtotal.containsKey(data.getMnth_abbr()))
						{
							double ggval =0;
							if (request.getUv()==1) {
								ggval = AppCalculationUtils.addDouble(branchtotal.get(data.getMnth_abbr()), data.getSales_qty());
							}
							else
							{
								ggval = AppCalculationUtils.addDouble(branchtotal.get(data.getMnth_abbr()), data.getSales_val());
							}
							branchtotal.put(data.getMnth_abbr(), ggval);

							
						}
						else
						{
							if (request.getUv()==1) {
								branchtotal.put(data.getMnth_abbr(), data.getSales_qty());
							}
							else
							{
								branchtotal.put(data.getMnth_abbr(), data.getSales_val());
							}
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
			monthlyTarget=AppCalculationUtils.roundToDecimal2Places(monthlyTarget);
			months.put(wmonth+"_budget", monthlyTarget);
			months.put(wmonth+"_sale", monthlySale);
			months.put(wmonth+"_lys", monthlyLys);
			achPer=AppCalculationUtils.calculateAch(monthlySale, monthlyTarget);
			months.put(wmonth+"_ach_per", achPer);
			gthPer=AppCalculationUtils.calculateGth(monthlySale, monthlyLys);
			months.put(wmonth+"_gth_per", gthPer);
			surdef=AppCalculationUtils.calculateSdf(monthlySale, monthlyTarget);
			months.put(wmonth+"_sur_slash_def", surdef);
			icrsls=AppCalculationUtils.roundToDecimal2Places(monthlySale-monthlyLys);
			months.put(wmonth+"_incr_sale", icrsls);
			pmr=AppCalculationUtils.calculatePmr(monthlySale, fs);
			months.put(wmonth+"_pmr", pmr);
			
			targetTotal=Math.round(targetTotal*100.0)/100.00;
			months.put("cumBudget", targetTotal);
			columnTotal=Math.round(columnTotal*100.0)/100.00;
			months.put("cumSale", columnTotal);
			lysTotal=Math.round(lysTotal*100.0)/100.00;
			months.put("cumLys", lysTotal);
			achPer=AppCalculationUtils.calculateAch(columnTotal, targetTotal);
			months.put("cumAchPer", achPer);
			gthPer=AppCalculationUtils.calculateGth(columnTotal, lysTotal);
			months.put("cumGthPer", gthPer);
			surdef=AppCalculationUtils.calculateSdf(columnTotal, targetTotal);
			months.put("cumSurSlashDef", surdef);
			icrsls=AppCalculationUtils.roundToDecimal2Places(columnTotal-lysTotal);
			months.put("cumIncrSale", icrsls);
			pmr=AppCalculationUtils.calculatePmr(columnTotal, fs);
			months.put("cumPmr", pmr);
		}
		response.setMonths(months);
		saleList.add(response);
		bmonthlySale+=monthlySale;
		bmonthlyTarget=monthlyTarget;
		bmonthlyLys+=monthlyLys;
		tmonthlySale+=monthlySale;
		tmonthlyTarget+=monthlyTarget;
		tmonthlyLys+=monthlyLys;

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
				bmonthlyTarget=AppCalculationUtils.roundToDecimal2Places(bmonthlyTarget);
				branchtotal.put(wmonth+"_budget", bmonthlyTarget);
				bmonthlySale=AppCalculationUtils.roundToDecimal2Places(bmonthlySale);
				branchtotal.put(wmonth+"_sale", bmonthlySale);
				bmonthlyLys=AppCalculationUtils.roundToDecimal2Places(bmonthlyLys);
				branchtotal.put(wmonth+"_lys", bmonthlyLys);
				achPer=AppCalculationUtils.calculateAch(bmonthlySale, bmonthlyTarget);
				branchtotal.put(wmonth+"_ach_per", achPer);
				gthPer=AppCalculationUtils.calculateGth(bmonthlySale, bmonthlyLys);
				branchtotal.put(wmonth+"_gth_per", gthPer);
				surdef=AppCalculationUtils.calculateSdf(bmonthlySale, bmonthlyTarget);
				branchtotal.put(wmonth+"_sur_slash_def", surdef);
				icrsls=AppCalculationUtils.roundToDecimal2Places(bmonthlySale-bmonthlyLys);
				branchtotal.put(wmonth+"_incr_sale", icrsls);
				pmr=AppCalculationUtils.calculatePmr(bmonthlySale, fs);
				branchtotal.put(wmonth+"_pmr", pmr);

				
				targetTotal=Math.round(branchtargetTotal*100.0)/100.00;
				branchtotal.put("cumBudget", targetTotal);
				columnTotal=Math.round(branchcolumnTotal*100.0)/100.00;
				branchtotal.put("cumSale", columnTotal);
				lysTotal=Math.round(branchlysTotal*100.0)/100.00;
				branchtotal.put("cumLys", lysTotal);
				achPer=AppCalculationUtils.calculateAch(columnTotal, targetTotal);
				branchtotal.put("cumAchPer", achPer);
				gthPer=AppCalculationUtils.calculateGth(columnTotal, lysTotal);
				branchtotal.put("cumGthPer", gthPer);
				surdef=AppCalculationUtils.calculateSdf(columnTotal, targetTotal);
				branchtotal.put("cumSurSlashDef", surdef);
				icrsls=columnTotal-lysTotal;
				icrsls=AppCalculationUtils.roundToDecimal2Places(icrsls);
				branchtotal.put("cumIncrSale", icrsls);
				pmr=AppCalculationUtils.calculatePmr(columnTotal, bfs);
				branchtotal.put("cumPmr", pmr);
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
			tmonthlyTarget=AppCalculationUtils.roundToDecimal2Places(tmonthlyTarget);
			total.put(wmonth+"_budget", tmonthlyTarget);
			tmonthlySale=AppCalculationUtils.roundToDecimal2Places(tmonthlySale);
			total.put(wmonth+"_sale", tmonthlySale);
			tmonthlyLys=AppCalculationUtils.roundToDecimal2Places(tmonthlyLys);
			total.put(wmonth+"_lys", tmonthlyLys);
			achPer=AppCalculationUtils.calculateAch(tmonthlySale, tmonthlyTarget);
			total.put(wmonth+"_ach_per", achPer);
			gthPer=AppCalculationUtils.calculateGth(tmonthlySale, tmonthlyLys);
			total.put(wmonth+"_gth_per", gthPer);
			surdef=AppCalculationUtils.calculateSdf(tmonthlySale, tmonthlyTarget);
			total.put(wmonth+"_sur_slash_def", surdef);
			icrsls=AppCalculationUtils.roundToDecimal2Places(tmonthlySale-tmonthlyLys);
			total.put(wmonth+"_incr_sale", icrsls);
			pmr=AppCalculationUtils.calculatePmr(tmonthlySale, fs);
			total.put(wmonth+"_pmr", pmr);

			
			targetTotal=Math.round(grandtargetTotal*100.0)/100.00;
			total.put("cumBudget", targetTotal);
			columnTotal=Math.round(grandColumnTotal*100.0)/100.00;
			total.put("cumSale", columnTotal);
			lysTotal=Math.round(grandlysTotal*100.0)/100.00;
			total.put("cumLys", lysTotal);
			achPer=AppCalculationUtils.calculateAch(columnTotal, targetTotal);
			total.put("cumAchPer", achPer);
			gthPer=AppCalculationUtils.calculateGth(columnTotal, lysTotal);
			total.put("cumGthPer", gthPer);
			surdef=AppCalculationUtils.calculateSdf(columnTotal, targetTotal);
			total.put("cumSurSlashDef", surdef);
			icrsls=columnTotal-lysTotal;
			icrsls=AppCalculationUtils.roundToDecimal2Places(icrsls);
			total.put("cumIncrSale", icrsls);
			pmr=AppCalculationUtils.calculatePmr(columnTotal, bfs);
			total.put("cumPmr", pmr);
		}
		
		
		
		response.setMonths(total);  // 🔥 Set the month-wise values
		response.setColor(2);       // Optional: special styling
		saleList.add(response);     // ✅ ADD to output

		System.out.println(decimalKeys);
		return new ApiResponse<MktRepo6Response>(title.toString(),size,decimalKeys,saleList);


	}
}
