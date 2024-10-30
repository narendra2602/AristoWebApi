package com.aristowebapi.serviceimpl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aristowebapi.constant.AristoWebLogMsgConstant;
import com.aristowebapi.constant.AristoWebMessageConstant;
import com.aristowebapi.dao.IqviaDao;
import com.aristowebapi.dto.Iqvia;
import com.aristowebapi.dto.MktRepo8;
import com.aristowebapi.request.IqviaRequest;
import com.aristowebapi.response.ApiResponse;
import com.aristowebapi.response.IqviaResponse;
import com.aristowebapi.response.MktRepo8Response;
import com.aristowebapi.service.IqviaService;

@Service
public class IqviaServiceImpl implements IqviaService{

	Logger logger = LoggerFactory.getLogger(IqviaServiceImpl.class);
	private final String monthArray[] = new String[] {"","OCT","NOV","DEC","JAN","FEB","MAR","APR","MAY","JUN","JUL","AUG","SEP"};
	
	@Autowired
	private IqviaDao iqviaDao;

	@Autowired
	private AristoWebMessageConstant  aristoWebMessageConstant;

	private String getTitle(IqviaRequest request,Iqvia data)
	{

		
		StringBuilder title=new StringBuilder();
		title.append(aristoWebMessageConstant.divisionMap.get(String.valueOf(request.getDivCode())));
		title.append(" PRODUCT MATRIX  ");
		title.append(request.getDepoCode()==0?"All India":iqviaDao.getBranch(request.getDepoCode())+" Branch: -");
		title.append(" - TOP 10 ");
		return title.toString();

	}

	
	
	@Override
	public ApiResponse<IqviaResponse> getIqvia(IqviaRequest request) {
		logger.info(AristoWebLogMsgConstant.IQVIA_SERVICE,"getIqvia");
		
		List<Iqvia> IqviaList=null;
		int size = 0;
		IqviaList=iqviaDao.getIqvia(request.getDivCode(),request.getDepoCode(),request.getUserType(),request.getLoginId(),request.getGpCode());
		size = IqviaList.size();
		
		logger.info("size of the data is {}",size);
	
		IqviaResponse response=null;
	List<IqviaResponse> saleList = new ArrayList();

	boolean first=true;
	String title=null;
	long  sval=0;
	long  eval=0;
	long  bval=0;
	long  tval=0;

	for (int i=0;i<size;i++)
	{
		Iqvia data = IqviaList.get(i);
		
/*		if(data.getDepo_code()==0)
			continue;
*/		
		if(first)
		{
			title = getTitle(request, data);
			first=false;
		}
		
		response=new IqviaResponse();


		response.setBranch(data.getBranch());
		response.setMolecule(data.getMolecule());
		response.setRank_mat1(data.getRank_mat1());
		response.setRank_mat2(data.getRank_mat2());
		response.setRank_mat3(data.getRank_mat3());
		response.setRank_mat4(data.getRank_mat4());
		response.setRank_mat5(data.getRank_mat5());
		response.setProduct(data.getProduct());
		response.setCompay(data.getCompay());
		response.setDivision(data.getDivision());
		response.setLaunch(data.getLaunch());
		response.setMat_val_1(data.getMat_val_1());
		response.setMat_val_2(data.getMat_val_2());
		response.setMat_val_3(data.getMat_val_3());
		response.setMat_val_4(data.getMat_val_4());
		response.setMat_val_5(data.getMat_val_5());
		response.setMat_msper_1(data.getMat_msper_1());
		response.setMat_msper_2(data.getMat_msper_2());
		response.setMat_msper_3(data.getMat_msper_3());
		response.setMat_msper_4(data.getMat_msper_4());
		response.setMat_msper_5(data.getMat_msper_5());
		response.setMat_msglper_1(data.getMat_msglper_1());
		response.setMat_msglper_2(data.getMat_msglper_2());
		response.setMat_msglper_3(data.getMat_msglper_3());
		response.setMat_msglper_4(data.getMat_msglper_4());
		response.setMat_grtper_1(data.getMat_grtper_1());
		response.setMat_grtper_2(data.getMat_grtper_2());
		response.setMat_grtper_3(data.getMat_grtper_3());
		response.setMat_grtper_4(data.getMat_grtper_4());
		
		response.setCy_cumm_val(data.getCy_cumm_val());
		response.setCumm_grt_per(data.getCumm_grt_per());
		response.setMat_unit_1(data.getMat_unit_1());
		response.setMat_unit_2(data.getMat_unit_2());
		response.setMat_unit_3(data.getMat_unit_3());
		response.setMat_unit_4(data.getMat_unit_4());
		response.setMat_unit_5(data.getMat_unit_5());
		response.setUmat_msper_1(data.getUmat_msper_1());
		response.setUmat_msper_2(data.getUmat_msper_2());
		response.setUmat_msper_3(data.getUmat_msper_3());
		response.setUmat_msper_4(data.getUmat_msper_4());
		response.setUmat_msper_5(data.getUmat_msper_5());
		response.setUmat_msglper_1(data.getUmat_msglper_1());
		response.setUmat_msglper_2(data.getUmat_msglper_2());
		response.setUmat_msglper_3(data.getUmat_msglper_3());
		response.setUmat_msglper_4(data.getUmat_msglper_4());
		response.setUmat_grtper_1(data.getUmat_grtper_1());
		response.setUmat_grtper_2(data.getUmat_grtper_2());
		response.setUmat_grtper_3(data.getUmat_grtper_3());
		response.setUmat_grtper_4(data.getUmat_grtper_4());
		
		response.setUcy_cumm_val(data.getUcy_cumm_val());;
		response.setUcumm_grt_per(data.getUcumm_grt_per());
		
		response.setQtrval_5(data.getQtrval_5());
		response.setQtrval_6(data.getQtrval_6());
		response.setQtrval_7(data.getQtrval_7());
		response.setQtrval_8(data.getQtrval_8());
		
		response.setQtr_msper_5(data.getQtr_msper_5());
		response.setQtr_msper_6(data.getQtr_msper_6());
		response.setQtr_msper_7(data.getQtr_msper_7());
		response.setQtr_msper_8(data.getQtr_msper_8());
		response.setQtr_msgl_1(data.getQtr_msgl_1());
		response.setQtr_msgl_2(data.getQtr_msgl_2());
		response.setQtr_msgl_3(data.getQtr_msgl_3());
		response.setQtr_msgl_4(data.getQtr_msgl_4());

		response.setQtr_grt_5(data.getQtr_grt_5());;
		response.setQtr_grt_6(data.getQtr_grt_6());;
		response.setQtr_grt_7(data.getQtr_grt_7());;
		response.setQtr_grt_8(data.getQtr_grt_8());;

		response.setQtr_unit_5(data.getQtr_unit_5());;
		response.setQtr_unit_6(data.getQtr_unit_6());;
		response.setQtr_unit_7(data.getQtr_unit_7());;
		response.setQtr_unit_8(data.getQtr_unit_8());;

		response.setQtr_unitms_5(data.getQtr_unitms_5());;;
		response.setQtr_unitms_6(data.getQtr_unitms_6());;;
		response.setQtr_unitms_7(data.getQtr_unitms_7());;;
		response.setQtr_unitms_8(data.getQtr_unitms_8());;;
		response.setQtr_unitmsgl_1(data.getQtr_unitmsgl_1());
		response.setQtr_unitmsgl_2(data.getQtr_unitmsgl_2());
		response.setQtr_unitmsgl_3(data.getQtr_unitmsgl_3());
		response.setQtr_unitmsgl_4(data.getQtr_unitmsgl_4());

		response.setQtr_unitmsgrt_5(data.getQtr_unitmsgrt_5());
		response.setQtr_unitmsgrt_6(data.getQtr_unitmsgrt_6());
		response.setQtr_unitmsgrt_7(data.getQtr_unitmsgrt_7());
		response.setQtr_unitmsgrt_8(data.getQtr_unitmsgrt_8());

		response.setMnth_val13(data.getMnth_val13());
		response.setMnth_val14(data.getMnth_val14());
		response.setMnth_val15(data.getMnth_val15());
		response.setMnth_val16(data.getMnth_val16());
		response.setMnth_val17(data.getMnth_val17());
		response.setMnth_val18(data.getMnth_val18());
		response.setMnth_val19(data.getMnth_val19());
		response.setMnth_val20(data.getMnth_val20());
		response.setMnth_val21(data.getMnth_val21());
		response.setMnth_val22(data.getMnth_val22());
		response.setMnth_val23(data.getMnth_val23());
		response.setMnth_val24(data.getMnth_val24());

		response.setMnth_valms_13(data.getMnth_valms_13());
		response.setMnth_valms_14(data.getMnth_valms_14());
		response.setMnth_valms_15(data.getMnth_valms_15());
		response.setMnth_valms_16(data.getMnth_valms_16());
		response.setMnth_valms_17(data.getMnth_valms_17());
		response.setMnth_valms_18(data.getMnth_valms_18());
		response.setMnth_valms_19(data.getMnth_valms_19());
		response.setMnth_valms_20(data.getMnth_valms_20());
		response.setMnth_valms_21(data.getMnth_valms_21());
		response.setMnth_valms_22(data.getMnth_valms_22());
		response.setMnth_valms_23(data.getMnth_valms_23());
		response.setMnth_valms_24(data.getMnth_valms_24());
		response.setMnth_valgl_1(data.getMnth_valgl_1());
		response.setMnth_valgl_2(data.getMnth_valgl_2());
		response.setMnth_valgl_3(data.getMnth_valgl_3());
		response.setMnth_valgl_4(data.getMnth_valgl_4());
		response.setMnth_valgl_5(data.getMnth_valgl_5());
		response.setMnth_valgl_6(data.getMnth_valgl_6());
		response.setMnth_valgl_7(data.getMnth_valgl_7());
		response.setMnth_valgl_8(data.getMnth_valgl_8());
		response.setMnth_valgl_9(data.getMnth_valgl_9());
		response.setMnth_valgl_10(data.getMnth_valgl_10());
		response.setMnth_valgl_11(data.getMnth_valgl_11());
		response.setMnth_valgl_12(data.getMnth_valgl_12());
		response.setMnth_valgrt_1(data.getMnth_valgrt_1());
		response.setMnth_valgrt_2(data.getMnth_valgrt_2());
		response.setMnth_valgrt_3(data.getMnth_valgrt_3());
		response.setMnth_valgrt_4(data.getMnth_valgrt_4());
		response.setMnth_valgrt_5(data.getMnth_valgrt_5());
		response.setMnth_valgrt_6(data.getMnth_valgrt_6());
		response.setMnth_valgrt_7(data.getMnth_valgrt_7());
		response.setMnth_valgrt_8(data.getMnth_valgrt_8());
		response.setMnth_valgrt_9(data.getMnth_valgrt_9());
		response.setMnth_valgrt_10(data.getMnth_valgrt_10());
		response.setMnth_valgrt_11(data.getMnth_valgrt_11());
		response.setMnth_valgrt_12(data.getMnth_valgrt_12());

		response.setMnth_unit13(data.getMnth_unit13());
		response.setMnth_unit14(data.getMnth_unit14());
		response.setMnth_unit15(data.getMnth_unit15());
		response.setMnth_unit16(data.getMnth_unit16());
		response.setMnth_unit17(data.getMnth_unit17());
		response.setMnth_unit18(data.getMnth_unit18());
		response.setMnth_unit19(data.getMnth_unit19());
		response.setMnth_unit20(data.getMnth_unit20());
		response.setMnth_unit21(data.getMnth_unit21());
		response.setMnth_unit22(data.getMnth_unit22());
		response.setMnth_unit23(data.getMnth_unit23());
		response.setMnth_unit24(data.getMnth_unit24());

		response.setMnth_unitms_13(data.getMnth_unitms_13());
		response.setMnth_unitms_14(data.getMnth_unitms_14());
		response.setMnth_unitms_15(data.getMnth_unitms_15());
		response.setMnth_unitms_16(data.getMnth_unitms_16());
		response.setMnth_unitms_17(data.getMnth_unitms_17());
		response.setMnth_unitms_18(data.getMnth_unitms_18());
		response.setMnth_unitms_19(data.getMnth_unitms_19());
		response.setMnth_unitms_20(data.getMnth_unitms_20());
		response.setMnth_unitms_21(data.getMnth_unitms_21());
		response.setMnth_unitms_22(data.getMnth_unitms_22());
		response.setMnth_unitms_23(data.getMnth_unitms_23());
		response.setMnth_unitms_24(data.getMnth_unitms_24());
		response.setMnth_unitgl_1(data.getMnth_unitgl_1());
		response.setMnth_unitgl_2(data.getMnth_unitgl_2());
		response.setMnth_unitgl_3(data.getMnth_unitgl_3());
		response.setMnth_unitgl_4(data.getMnth_unitgl_4());
		response.setMnth_unitgl_5(data.getMnth_unitgl_5());
		response.setMnth_unitgl_6(data.getMnth_unitgl_6());
		response.setMnth_unitgl_7(data.getMnth_unitgl_7());
		response.setMnth_unitgl_8(data.getMnth_unitgl_8());
		response.setMnth_unitgl_9(data.getMnth_unitgl_9());
		response.setMnth_unitgl_10(data.getMnth_unitgl_10());
		response.setMnth_unitgl_11(data.getMnth_unitgl_11());
		response.setMnth_unitgl_12(data.getMnth_unitgl_12());
		response.setMnth_unitgrt_1(data.getMnth_unitgrt_1());
		response.setMnth_unitgrt_2(data.getMnth_unitgrt_2());
		response.setMnth_unitgrt_3(data.getMnth_unitgrt_3());
		response.setMnth_unitgrt_4(data.getMnth_unitgrt_4());
		response.setMnth_unitgrt_5(data.getMnth_unitgrt_5());
		response.setMnth_unitgrt_6(data.getMnth_unitgrt_6());
		response.setMnth_unitgrt_7(data.getMnth_unitgrt_7());
		response.setMnth_unitgrt_8(data.getMnth_unitgrt_8());
		response.setMnth_unitgrt_9(data.getMnth_unitgrt_9());
		response.setMnth_unitgrt_10(data.getMnth_unitgrt_10());
		response.setMnth_unitgrt_11(data.getMnth_unitgrt_11());
		response.setMnth_unitgrt_12(data.getMnth_unitgrt_12());
		if(data.getCompay().contains("ARISTO PHARMA"))
			response.setColor(1);
		else
			response.setColor(0);

		
		
/*		response.setRankLy(data.getRl());
		response.setRankCy(data.getRc());
		response.setProduct(data.getProduct());
		response.setCompany(data.getCompany());
		response.setLaunch(data.getLaunch());
		response.setValInLacLy(data.getVall());
		response.setValInLacCy(data.getValc());
		response.setMsLy(data.getMsl());
		response.setMsCy(data.getMsc());
		response.setGthLy(data.getGthl());
		response.setGthCy(data.getGthc());
		response.setValInLacsQ1(data.getValq1());
		response.setValInLacsQ2(data.getValq2());
		response.setValInLacsQ3(data.getValq3());
		response.setValInLacsQ4(data.getValq4());
		response.setMsQ1(data.getMsq1());
		response.setMsQ2(data.getMsq2());
		response.setMsQ3(data.getMsq3());
		response.setMsQ4(data.getMsq4());
		response.setGthQ1(data.getGthq1());
		response.setGthQ2(data.getGthq2());
		response.setGthQ3(data.getGthq3());
		response.setGthQ4(data.getGthq4());
		if(data.getCompany().contains("ARISTO PHARMA"))
			response.setColor(1);
		else
			response.setColor(0);
			
		
*/		
    	saleList.add(response);

    	

	} //end of for loop

		

	ApiResponse<IqviaResponse> apiResponse = new ApiResponse<>(title!=null?title.toString():"", size,saleList);
	return apiResponse;

	}

}
