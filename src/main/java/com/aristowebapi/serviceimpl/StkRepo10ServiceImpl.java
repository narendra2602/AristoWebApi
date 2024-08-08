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
import com.aristowebapi.dao.StkRepo10Dao;
import com.aristowebapi.dto.MonthDto;
import com.aristowebapi.dto.StkRepo10;
import com.aristowebapi.request.StkRepo10Request;
import com.aristowebapi.response.ApiResponse;
import com.aristowebapi.response.StkRepo10Response;
import com.aristowebapi.response.StkRepo2Response;
import com.aristowebapi.service.StkRepo10Service;
@Service
public class StkRepo10ServiceImpl implements StkRepo10Service {

	Logger logger = LoggerFactory.getLogger(StkRepo10ServiceImpl.class);
	@Autowired
	private StkRepo10Dao stkRepo10Dao;

	@Autowired
	private AristoWebMessageConstant  aristoWebMessageConstant;
	
	private String lupdate="";

	Map<String, Long> grandTotalDoubleMap=null;
	double grandColumnTotalDouble=0.00;

	
	private String getTitle(StkRepo10Request request,StkRepo10 data)
	{

		
		StringBuilder title=new StringBuilder();
		title.append(aristoWebMessageConstant.divisionMap.get(String.valueOf(request.getDivCode())));
		title.append(request.getRepTypePgwise()==1?" PRODUCT -> : ":"Group ->");
		title.append(data.getPname());
		title.append(" ");
		title.append(" - STOCKIEST WISE ");
		title.append(request.getUv()==1?" UNIT TREND ":request.getUv()==2?" VALUE TREND ":" UNIT/VALUE TREND ");
		String crtype="";
		if (request.getCreditNoteType()==1)
				crtype="Saleable";
		else if(request.getCreditNoteType()==2)
			crtype="Spoiled";
		else if(request.getCreditNoteType()==3)
			crtype="Breakage";
		else if(request.getCreditNoteType()==4)
			crtype="Expired";
		else if(request.getCreditNoteType()==6)
			crtype="Price Diff";
		else if(request.getCreditNoteType()==7)
			crtype="Short Received";
		else if(request.getCreditNoteType()==9)
			crtype="Loss in Transit";

		title.append(request.getRepType()==1?" Gross ":((request.getRepType()==22 || request.getRepType()==2)?" Credit -":" Net "));
		title.append(request.getCreditNoteType()>0?crtype:"");
		title.append(" From ");
		title.append(data.getSmname());
		title.append(" To ");
		title.append(data.getEmname());

		return title.toString();

	}

	@Override
	public ApiResponse<StkRepo10Response> getStkRepo10(StkRepo10Request request) {
		logger.info(AristoWebLogMsgConstant.STK_REPO10_SERVICE,"getStkRepo10");


		List<MonthDto> monthData = stkRepo10Dao.getAllMonth(request.getMyear());
		int sz=monthData.size();
		int k=0;
		int z=0;
		sz=request.getEmon();

		Map<String, Object> grandTotalMap=null;
		Map<String, Integer> grandTotalIntMap=null;
		long grandColumnTotal=0;
		long grandColumnTotalVal=0;
		long vgrandColumnTotal=0;

		String title=null;
		
		if(request.getRepTypePgwise()==2 && request.getRepType()==1)
				request.setRepType(12);
		if(request.getRepTypePgwise()==2 && request.getRepType()==2)
			request.setRepType(22);
		if(request.getRepTypePgwise()==2 && request.getRepType()==3)
			request.setRepType(32);
		
		if(request.getRepTypePgwise()==2)
			request.setUv(2);
		
		System.out.println("rep type "+request.getRepType()+" uv "+request.getUv());
		List<StkRepo10> stkRepo10SaleList=stkRepo10Dao.getStockiestRepo10(request.getMyear(),request.getDivCode(),request.getDepoCode()
				,request.getSmon(),request.getEmon(),request.getRepType(),request.getPcode(),request.getLoginId());
	
		
		StkRepo10Response response=null;

		Map<String, Long> months=null;
		Map<String, Long> total=null;
		Map<String, Long> group=null;
		
		List<StkRepo10Response> saleList = new ArrayList();

		int size = stkRepo10SaleList.size();
		
		System.out.println("size is "+size);
		long columnTotal=0;
		long columnTotalVal=0;
		long groupColumnTotalVal=0;
		long groupColumnTotal=0;
		boolean first=true;
		int depo_code=0;
		int ter_code=0;
		String name="";
		String ter_name="";
		int fs=0;
		int gfs=0;
		String pcode="";
		String depo_name="";
		for (int i=0;i<size;i++)
		{
			StkRepo10 data = stkRepo10SaleList.get(i);
			if(first)
			{
				response=new StkRepo10Response();
				pcode=data.getSprt_cd();
				name=data.getMname();
				ter_code=data.getTerr_cd();
				ter_name=data.getTer_name();
				depo_name=data.getDepo_name();
				months=new LinkedHashMap();
				group=new LinkedHashMap();
				total=new LinkedHashMap();
				first=false;
				
				title = getTitle(request, data); 
			}

			if(!pcode.equalsIgnoreCase(data.getSprt_cd()))
			{
				
				response.setBranch(depo_name);
				response.setName(name);
				
/*			z=k;
				for(int b=k;b<sz;b++)
				{
					MonthDto mn=monthData.get(b);
					if(request.getUv()==1)
						months.put((mn.getMnth_abbr()+" UNITS"), 0L);
					else if(request.getUv()==2)
						months.put((mn.getMnth_abbr()+" VALUE"), 0L);
					else
					{
						months.put((mn.getMnth_abbr()+" UNITS"), 0L);
						months.put((mn.getMnth_abbr()+" VALUE"), 0L);
					}
					k++;
				}
*/
				if(request.getUv()==1)
					months.put("TOTAL UNITS", columnTotal);
				else if(request.getUv()==2)
					months.put("TOTAL VALUE", columnTotalVal);
				else
				{
					months.put("TOTAL UNITS", columnTotal);
					months.put("TOTAL VALUE", columnTotalVal);
					
				}
				response.setMonths(months);
				
				saleList.add(response);
				pcode=data.getSprt_cd();
				name=data.getMname();
				
				columnTotal=0;
				columnTotalVal=0;
				
				k=0;
				response=new StkRepo10Response();
				months=new LinkedHashMap();

			}

			
			if(ter_code!=data.getTerr_cd())
			{
				response.setBranch(depo_name);
				response.setName(ter_name+"- Total");
				
/*				for(int b=z;b<sz;b++)
				{
					MonthDto mn=monthData.get(b);
					if(request.getUv()==1)
						group.put((mn.getMnth_abbr()+" UNITS"), 0L);
					else if(request.getUv()==2)
						group.put((mn.getMnth_abbr()+" VALUE"), 0L);
					else
					{
						group.put((mn.getMnth_abbr()+" UNITS"), 0L);
						group.put((mn.getMnth_abbr()+" VALUE"), 0L);
					}
				
					z++;
				}
*/
//				groupColumnTotal = group.values().stream().mapToLong(d -> d).sum();
				if(request.getUv()==1)
					group.put("TOTAL UNITS", groupColumnTotal);
				else if(request.getUv()==2)
					group.put("TOTAL VALUE", groupColumnTotalVal);
				else
				{
					group.put("TOTAL UNITS", groupColumnTotal);
					group.put("TOTAL VALUE", groupColumnTotalVal);
					
				}
				
				months.putAll(group);

				
				response.setMonths(months);
				response.setColor(1);
				saleList.add(response);

				
				ter_code=data.getTerr_cd();
				ter_name=data.getTer_name();
				depo_name=data.getDepo_name();
				z=0;
				groupColumnTotal=0;
				groupColumnTotalVal=0;
				response=new StkRepo10Response();
				months=new LinkedHashMap();
				group=new LinkedHashMap();
				
			}

			
			// before put please check depo code in branch list if not found put 0 value in map otherwise actual zero
			for(int b=0;b<sz;b++)
			{
				MonthDto mn=monthData.get(b);
				
				if(mn.getMnth_code()==data.getMnth_code())
				{
					
					grandColumnTotal+=data.getSales();
					grandColumnTotalVal+=data.getSales_val();
					
					if(request.getUv()==1)
					{
						months.put((data.getMnth_abbr()+" UNITS"),data.getSales());
						columnTotal+=data.getSales();
						groupColumnTotal+=data.getSales();
						
					}
					else if(request.getUv()==2)
					{
						months.put((data.getMnth_abbr()+" VALUE"),data.getSales_val());
						columnTotalVal+=data.getSales_val();
						groupColumnTotalVal+=data.getSales_val();
						
					}
					else if(request.getUv()==3)
					{
						months.put((data.getMnth_abbr()+" UNITS"),data.getSales());
						months.put((data.getMnth_abbr()+" VALUE"),data.getSales_val());
						columnTotal+=data.getSales();
						columnTotalVal+=data.getSales_val();
						groupColumnTotal+=data.getSales();
						groupColumnTotalVal+=data.getSales_val();
					}
					
					
					if(group.containsKey(data.getMnth_abbr()+" UNITS"))
					{
						long gval = 0;

						if(request.getUv()==1)
						{
							gval = group.get(data.getMnth_abbr()+" UNITS")+data.getSales();
							group.put(data.getMnth_abbr()+" UNITS", gval);
						}
						if(request.getUv()==2)
						{
							gval = group.get(data.getMnth_abbr()+" VALUE")+data.getSales_val();
							group.put(data.getMnth_abbr()+" VALUE", gval);
						}
						if(request.getUv()==3)
						{
							gval = group.get(data.getMnth_abbr()+" UNITS")+data.getSales();
							group.put(data.getMnth_abbr()+" UNITS", gval);
							gval = group.get(data.getMnth_abbr()+" VALUE")+data.getSales_val();
							group.put(data.getMnth_abbr()+" VALUE", gval);
						}
						
					}
					
					else if(group.containsKey(data.getMnth_abbr()+" VALUE"))
					{
						long gval = 0;

						if(request.getUv()==1)
						{
							gval = group.get(data.getMnth_abbr()+" UNITS")+data.getSales();
							group.put(data.getMnth_abbr()+" UNITS", gval);
						}
						if(request.getUv()==2)
						{
							gval = group.get(data.getMnth_abbr()+" VALUE")+data.getSales_val();
							group.put(data.getMnth_abbr()+" VALUE", gval);
						}
						if(request.getUv()==3)
						{
							gval = group.get(data.getMnth_abbr()+" UNITS")+data.getSales();
							group.put(data.getMnth_abbr()+" UNITS", gval);
							gval = group.get(data.getMnth_abbr()+" VALUE")+data.getSales_val();
							group.put(data.getMnth_abbr()+" VALUE", gval);
						}
						
					}

					else
					{
						if(request.getUv()==1)
						{
							group.put(data.getMnth_abbr()+" UNITS", data.getSales());
						}
						if(request.getUv()==2)
						{
							group.put(data.getMnth_abbr()+" VALUE", data.getSales_val());
						}
						if(request.getUv()==3)
						{
							group.put(data.getMnth_abbr()+" UNITS", data.getSales());
							group.put(data.getMnth_abbr()+" VALUE", data.getSales_val());
						}
					}
					
					if(total.containsKey(data.getMnth_abbr()+" UNITS"))
					{
						long gval = 0;

						if(request.getUv()==1)
						{
							gval = total.get(data.getMnth_abbr()+" UNITS")+data.getSales();
							total.put(data.getMnth_abbr()+" UNITS", gval);
						}
						if(request.getUv()==2)
						{
							gval = total.get(data.getMnth_abbr()+" VALUE")+data.getSales_val();
							total.put(data.getMnth_abbr()+" VALUE", gval);
						}
						if(request.getUv()==3)
						{
							gval = total.get(data.getMnth_abbr()+" UNITS")+data.getSales();
							total.put(data.getMnth_abbr()+" UNITS", gval);
							gval = total.get(data.getMnth_abbr()+" VALUE")+data.getSales_val();
							total.put(data.getMnth_abbr()+" VALUE", gval);
						}
						
					}
					
					else if(total.containsKey(data.getMnth_abbr()+" VALUE"))
					{
						long gval = 0;

						if(request.getUv()==1)
						{
							gval = total.get(data.getMnth_abbr()+" UNITS")+data.getSales();
							total.put(data.getMnth_abbr()+" UNITS", gval);
						}
						if(request.getUv()==2)
						{
							gval = total.get(data.getMnth_abbr()+" VALUE")+data.getSales_val();
							total.put(data.getMnth_abbr()+" VALUE", gval);
						}
						if(request.getUv()==3)
						{
							gval = total.get(data.getMnth_abbr()+" UNITS")+data.getSales();
							total.put(data.getMnth_abbr()+" UNITS", gval);
							gval = total.get(data.getMnth_abbr()+" VALUE")+data.getSales_val();
							total.put(data.getMnth_abbr()+" VALUE", gval);
						}
						
					}

					else
					{
						if(request.getUv()==1)
						{
							total.put(data.getMnth_abbr()+" UNITS", data.getSales());
						}
						if(request.getUv()==2)
						{
							total.put(data.getMnth_abbr()+" VALUE", data.getSales_val());
						}
						if(request.getUv()==3)
						{
							total.put(data.getMnth_abbr()+" UNITS", data.getSales());
							total.put(data.getMnth_abbr()+" VALUE", data.getSales_val());
						}
					}

/*					if(request.getUv()==1)
					{
						if(total.containsKey((data.getMnth_abbr()+" UNITS")))
						{
							long ggval = total.get(data.getMnth_abbr()+" UNITS")+data.getSales();
							total.put((data.getMnth_abbr()+" UNITS"), ggval);
						}
						else
						{
							total.put((data.getMnth_abbr()+" UNITS"), data.getSales());
						}
					}
					if(request.getUv()==2)
					{
						if(total.containsKey((data.getMnth_abbr()+" VALUE")))
						{
							long ggval = total.get((data.getMnth_abbr()+" VALUE"))+data.getSales_val();
							total.put((data.getMnth_abbr()+" VALUE"), ggval);
						}
						else
						{
							total.put((data.getMnth_abbr()+" VALUE"), data.getSales_val());
						}
					}
					if(request.getUv()==3)
					{
						if(total.containsKey((data.getMnth_abbr()+" UNITS")))
						{
							long ggval = total.get(data.getMnth_abbr()+" UNITS")+data.getSales();
							total.put((data.getMnth_abbr()+" UNITS"), ggval);
						}
						else
						{
							total.put((data.getMnth_abbr()+" UNITS"), data.getSales());
						}

						if(total.containsKey((data.getMnth_abbr()+" VALUE")))
						{
							long ggval = total.get((data.getMnth_abbr()+" VALUE"))+data.getSales_val();
							total.put((data.getMnth_abbr()+" VALUE"), ggval);
						}
						else
						{
							total.put((data.getMnth_abbr()+" VALUE"), data.getSales_val());
						}
					}

*/					
					
					k++;
//					break;
				}
				else
				{
					
					k++;
					
					if(months.containsKey(mn.getMnth_abbr()+" UNITS") || months.containsKey(mn.getMnth_abbr()+" VALUE"))
					{
						// do nothing
					}
					else
					{
						if(request.getUv()==1)
							months.put(mn.getMnth_abbr()+" UNITS", 0L);
						if(request.getUv()==2)
							months.put(mn.getMnth_abbr()+" VALUE", 0L);
						if(request.getUv()==3)
						{
							months.put(mn.getMnth_abbr()+" UNITS", 0L);
							months.put(mn.getMnth_abbr()+" VALUE", 0L);
						}
					}

					if(group.containsKey(mn.getMnth_abbr()+" UNITS") || group.containsKey(mn.getMnth_abbr()+" VALUE"))
					{
						// do nothing
					}
					else
					{
						if(request.getUv()==1)
							group.put(mn.getMnth_abbr()+" UNITS", 0L);
						if(request.getUv()==2)
							group.put(mn.getMnth_abbr()+" VALUE", 0L);
						if(request.getUv()==3)
						{
							group.put(mn.getMnth_abbr()+" UNITS", 0L);
							group.put(mn.getMnth_abbr()+" VALUE", 0L);
						}
					}

					if(total.containsKey(mn.getMnth_abbr()+" UNITS") || total.containsKey(mn.getMnth_abbr()+" VALUE"))
					{
						// do nothing
					}
					else
					{
						if(request.getUv()==1)
							total.put(mn.getMnth_abbr()+" UNITS", 0L);
						if(request.getUv()==2)
							total.put(mn.getMnth_abbr()+" VALUE", 0L);
						if(request.getUv()==3)
						{
							total.put(mn.getMnth_abbr()+" UNITS", 0L);
							total.put(mn.getMnth_abbr()+" VALUE", 0L);
						}

					}

//					k++;
				}
			}
			
		}		
		
			if(!first)
			{
			response=new StkRepo10Response();
			response.setBranch(depo_name);
			response.setName(name);
/*			z=k;
			for(int b=k;b<sz;b++)
			{
				MonthDto mn=monthData.get(b);
				if(request.getUv()==1)
				months.put((mn.getMnth_abbr()+" UNITS"), 0L);
				else if(request.getUv()==2)
				months.put((mn.getMnth_abbr()+" VALUE"), 0L);
				else
				{
					months.put((mn.getMnth_abbr()+" UNITS"), 0L);
					months.put((mn.getMnth_abbr()+" VALUE"), 0L);
				}
				k++;
			}
*/			if(request.getUv()==1)
				months.put("TOTAL UNITS", columnTotal);
			else if(request.getUv()==2)
				months.put("TOTAL VALUE", columnTotalVal);
			else
			{
				months.put("TOTAL UNITS", columnTotal);
				months.put("TOTAL VALUE", columnTotalVal);
				
			}

			response.setMonths(months);
			saleList.add(response);


			
			months=new LinkedHashMap();
			response=new StkRepo10Response();
			response.setBranch(depo_name);
			response.setName(ter_name+"- Total");
/*			z=sz;
			for(int b=z;b<sz;b++)
			{
				MonthDto mn=monthData.get(b);
				if(request.getUv()==1)
					group.put((mn.getMnth_abbr()+" UNITS"), 0L);
				else if(request.getUv()==2)
					group.put((mn.getMnth_abbr()+" VALUE"), 0L);
				else
				{
					group.put((mn.getMnth_abbr()+" UNITS"), 0L);
					group.put((mn.getMnth_abbr()+" VALUE"), 0L);
				}
			
				z++;
			}
*/
//			groupColumnTotal = group.values().stream().mapToLong(d -> d).sum();
			if(request.getUv()==1)
				group.put("TOTAL UNITS", groupColumnTotal);
			else if(request.getUv()==2)
				group.put("TOTAL VALUE", groupColumnTotalVal);
			else
			{
				group.put("TOTAL UNITS", groupColumnTotal);
				group.put("TOTAL VALUE", groupColumnTotalVal);
				
			}

			months.putAll(group);
			response.setMonths(months);
			response.setColor(1);
			saleList.add(response);
		
			
//			grandColumnTotal = total.units().stream().mapToLong(d -> d).sum();
//			grandColumnTotalVal = total.values().stream().mapToLong(d -> d).sum();
			
			months=new LinkedHashMap();
			response=new StkRepo10Response();
			z=sz;
			for(int b=z;b<sz;b++)
			{
				MonthDto mn=monthData.get(b);
				if(request.getUv()==1)
					total.put((mn.getMnth_abbr()+" UNITS"), 0L);
				else if(request.getUv()==2)
					total.put((mn.getMnth_abbr()+" VALUE"), 0L);
				else
				{
					total.put((mn.getMnth_abbr()+" UNITS"), 0L);
					total.put((mn.getMnth_abbr()+" VALUE"), 0L);
				}
			
				z++;
			}

			
			if(request.getUv()==1)
				total.put("TOTAL UNITS", grandColumnTotal);
			else if(request.getUv()==2)
				total.put("TOTAL VALUE", grandColumnTotalVal);
			else
			{
				total.put("TOTAL UNITS", grandColumnTotal);
				total.put("TOTAL VALUE", grandColumnTotalVal);
				
			}



			months.putAll(total);
			
			response.setBranch("Total");
			response.setName("");
			

			response.setMonths(months);
			response.setColor(2);
			saleList.add(response);		
			return new ApiResponse<StkRepo10Response>(title.toString(),size,lupdate,saleList);
			}
			else 
			return null;
	}

}
