package com.aristowebapi.serviceimpl;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aristowebapi.constant.AristoWebMessageConstant;
import com.aristowebapi.dao.BranchMktDao;
import com.aristowebapi.dto.BranchMktRepo5;
import com.aristowebapi.dto.MonthDto;
import com.aristowebapi.request.BranchMktRepo5Request;
import com.aristowebapi.response.ApiResponse;
import com.aristowebapi.response.BranchMktRepo5Response;
import com.aristowebapi.service.BranchMktService;

@Service
public class BranchMktRepo5ServiceImpl implements BranchMktService {
	
	@Autowired
	private BranchMktDao branchMktDao;
	
	@Autowired
	private AristoWebMessageConstant  aristoWebMessageConstant;

	private String getTitle(BranchMktRepo5Request request,BranchMktRepo5 data)
	{
		
		StringBuilder title=new StringBuilder();

		title.append(aristoWebMessageConstant.divisionMap.get(String.valueOf(data.getDiv_code())));
//		title.append(request.getDataType()==1?" H.Q. Wise -> ":request.getDataType()==2?" REGION WISE -> ":request.getDataType()==3?" AREA WISE ->":request.getDepoCode()==0?"":" Branch Wise -> ");
		title.append(request.getDepoCode()==0?"All India ":data.getBrname());
		title.append(request.getRepType()==1?" Product/ ":" Group/ ");
		title.append(request.getUv()==1?"Unit Wise ":"Value Wise ");
		title.append(request.getOption()==1?"This Year Target/Sales Trend FOR THE MKT YEAR ":"Last Year/This Year Sales Trend  FOR MKT THE YEAR ");
		title.append(request.getMyear());
		return title.toString();

	}
	
	
	@Override
	public ApiResponse<BranchMktRepo5Response> getBranchMktRepo5(BranchMktRepo5Request request) {
		List<MonthDto> monthData = branchMktDao.getAllMonth(request.getMyear());
		int sz=monthData.size();
		int k=0;
		int z=0;

		
		String title=null;
		List<BranchMktRepo5> brnachRepo5List=null;
		
			brnachRepo5List=branchMktDao.getBranchMktRepo5(request.getMyear(),request.getDivCode(),request.getDepoCode()
				,request.getSmon(),request.getEmon(),request.getUtype(),request.getLoginId(),request.getRepType(),request.getHqCode(),request.getDataType());

			BranchMktRepo5Response response=null;
			List<BranchMktRepo5Response> saleList = new ArrayList();
			Map<String, Long> months=null;
			Map<String, Long> group=null;
			Map<String, Long> total=null;

			boolean first=true;

			int size = brnachRepo5List.size();
			int pcode=0;
			int mgrp=0;
			long columnTotal=0;
			long groupColumnTotal=0;
			long grandColumnTotal=0;

			String pname=null;
			String pack=null;
			String gname=null;

			for (int i=0;i<size;i++)
			{
				BranchMktRepo5 data = brnachRepo5List.get(i);
				
				if(first)
				{
					pcode=data.getMcode();
					pname=data.getMname();
					pack=data.getPack();
					mgrp=data.getMgrp();
					gname=data.getGp_name();
					response=new BranchMktRepo5Response();
					months=new LinkedHashMap();
					group=new LinkedHashMap();
					total=new LinkedHashMap();
					first=false;
					
					title = getTitle(request, data); 
				}

				if(pcode!=data.getMcode())
				{
					
					response.setCode(pcode);
					response.setName(pname);
					response.setPack(pack);
					z=k;
					for(int b=k;b<sz;b++)
					{
						MonthDto mn=monthData.get(b);
						months.put(mn.getMnth_abbr(), 0L);
						k++;
					}

//					months.put("TOTAL", columnTotal);
					response.setMonths(months);
					
					saleList.add(response);
					pcode=data.getMcode();
					pname=data.getMname();
					pack=data.getPack();
					columnTotal=0;
					
					k=0;
					response=new BranchMktRepo5Response();
					months=new LinkedHashMap();

				}
				if(mgrp!=data.getMgrp())
				{
					response.setCode(mgrp);
					response.setName(gname);
					response.setPack("");
					for(int b=z;b<sz;b++)
					{
						MonthDto mn=monthData.get(b);
						group.put(mn.getMnth_abbr(), 0L);
						z++;
					}



					months.putAll(group);

					
					response.setMonths(months);
					response.setColor(1);
					saleList.add(response);

					
					mgrp=data.getMgrp();
					gname=data.getGp_name();
					
					z=0;
					groupColumnTotal=0;
					response=new BranchMktRepo5Response();
					months=new LinkedHashMap();
					group=new LinkedHashMap();
					
				}

				
				// before put please check depo code in branch list if not found put 0 value in map otherwise actual zero
				for(int b=k;b<sz;b++)
				{
					MonthDto mn=monthData.get(b);
					if(mn.getMnth_code()==data.getMnth_code())
					{
						if(request.getOption()==1)
							months.put((data.getMnth_abbr()+" TGT"), request.getUv()==2?data.getTarget_val():data.getTarget_unit());
						else
							months.put((data.getMnth_abbr()+" LYS"), request.getUv()==2?data.getLys_val():data.getLys_unit());

						months.put((data.getMnth_abbr()+" SALE"), request.getUv()==2?data.getSales_val():data.getSales_unit());

						if(request.getOption()==1)
						{
							if(group.containsKey((data.getMnth_abbr()+" TGT")))
							{
								long gval = group.get((data.getMnth_abbr()+" TGT"))+data.getTarget_val();
								group.put((data.getMnth_abbr()+" TGT"), gval);
							}
							else
							{
								group.put((data.getMnth_abbr()+" TGT"), data.getTarget_val());
							}
						}
						else
						{
							if(group.containsKey((data.getMnth_abbr()+" LYS")))
							{
								long gval = group.get((data.getMnth_abbr()+" LYS"))+data.getLys_val();
								group.put((data.getMnth_abbr()+" LYS"), gval);
							}
							else
							{
								group.put((data.getMnth_abbr()+" LYS"), data.getLys_val());

							}
						}
						if(group.containsKey((data.getMnth_abbr()+" SALE")))
						{
							long gval = group.get((data.getMnth_abbr()+" SALE"))+data.getSales_val();
							group.put((data.getMnth_abbr()+" SALE"), gval);
						}
						else
						{
							group.put((data.getMnth_abbr()+" SALE"), data.getSales_val());
						}

						if(request.getOption()==1)
						{

							if(total.containsKey((data.getMnth_abbr()+" TGT")))
							{
								long ggval = total.get((data.getMnth_abbr()+" TGT"))+data.getTarget_val();
								total.put((data.getMnth_abbr()+" TGT"), ggval);

							}
							else
							{
								total.put((data.getMnth_abbr()+" TGT"), data.getTarget_val());
							}

						}
						else
						{
							if(total.containsKey((data.getMnth_abbr()+" LYS")))
							{
								long ggval = total.get((data.getMnth_abbr()+" LYS"))+data.getLys_val();
								total.put((data.getMnth_abbr()+" LYS"), ggval);

							}
							else
							{
								total.put((data.getMnth_abbr()+" LYS"), data.getLys_val());
							}
						}
						if(total.containsKey((data.getMnth_abbr()+" SALE")))
						{
							long ggval = total.get((data.getMnth_abbr()+" SALE"))+data.getSales_val();
							total.put((data.getMnth_abbr()+" SALE"), ggval);
						}
						else
						{
							total.put((data.getMnth_abbr()+" SALE"), data.getSales_val());
						}
						
						k++;
						break;
					}
					else
					{
						if(request.getOption()==1)
							months.put((data.getMnth_abbr()+" TGT"), 0L);
						else
							months.put((data.getMnth_abbr()+" LYS"), 0L);
						months.put((data.getMnth_abbr()+" SALE"), 0L);

						if(request.getOption()==1)
						{
							if(group.containsKey((data.getMnth_abbr()+" TGT")))
							{
								// do nothing
							}
							else
							{
								group.put((data.getMnth_abbr()+" TGT"), 0L);
							}
						}
						else
						{
							if(group.containsKey((data.getMnth_abbr()+" LYS")))
							{
								// do nothing
							}
							else
							{
								group.put((data.getMnth_abbr()+" LYS"), 0L);

							}
						}
						if(group.containsKey((data.getMnth_abbr()+" SALE")))
						{
							// do nothing
						}
						else
						{
							group.put((data.getMnth_abbr()+" SALE"), 0L);

						}
						
						if(request.getOption()==1)
						{
							if(total.containsKey((data.getMnth_abbr()+" TGT")))
							{
								// do nothing
							}
							else
							{
								total.put((data.getMnth_abbr()+" TGT"), 0L);
							}
						}
						else
						{
							if(total.containsKey((data.getMnth_abbr()+" LYS")))
							{
								// do nothing
							}
							else
							{
								total.put((data.getMnth_abbr()+" LYS"), 0L);

							}
						}
						
						if(total.containsKey((data.getMnth_abbr()+" SALE")))
						{
							// do nothing
						}
						else
						{
							total.put((data.getMnth_abbr()+" SALE"), 0L);

						}

						
						k++;
					}
				}
				
			}			
				response=new BranchMktRepo5Response();
				response.setCode(pcode);
				response.setName(pname);
				response.setPack(pack);
				z=k;
				for(int b=k;b<sz;b++)
				{
					MonthDto mn=monthData.get(b);
					months.put(mn.getMnth_abbr(), 0L);
					k++;
				}


				response.setMonths(months);
				saleList.add(response);

				
				months=new LinkedHashMap();
				response=new BranchMktRepo5Response();
				response.setCode(mgrp);
				response.setName(gname);
				response.setPack("");
				for(int b=z;b<sz;b++)
				{
					MonthDto mn=monthData.get(b);
					group.put(mn.getMnth_abbr(), 0L);
					z++;
				}


				months.putAll(group);
				response.setMonths(months);
				response.setColor(1);
				saleList.add(response);
				
				
				grandColumnTotal = total.values().stream().mapToLong(d -> d).sum();
				
				months=new LinkedHashMap();
//				total.put("TOTAL", grandColumnTotal);
//				total.keySet().stream().forEach(d->System.out.print(d));

				months.putAll(total);
				response=new BranchMktRepo5Response();
				response.setCode(0);
				response.setName("Grand Total");
				response.setPack("");
				response.setMonths(months);
				response.setColor(2);
				saleList.add(response);

				return new ApiResponse<BranchMktRepo5Response>(title.toString(),size,saleList);

	}

}
