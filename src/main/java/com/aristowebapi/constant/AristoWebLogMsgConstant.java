package com.aristowebapi.constant;

public class AristoWebLogMsgConstant {
	
	private AristoWebLogMsgConstant()
	{
		
	}

	public static final String MKT_REPORT_CONTROLLER_01="MktRepo1Controller:: {} method execution started, myear : {} , div_code: {}";
	public static final String MKT_REPORT_SERVICE_01="MktRepo1ServiceImpl:: {} method execution started";

	public static final String MKT_REPORT_CONTROLLER_02="MktRepo2Controller:: {} method execution started, myear : {} , div_code: {}";
	public static final String MKT_REPORT_SERVICE_02="MktRepo2ServiceImpl:: {} method execution started";
	public static final String MKT_REPORT_SERVICE_021="MktRepo2ServiceImpl:: {} method occurred exception while fetching data from the database, kindly check store procedure ::web_mkt_2all";
	public static final String MKT_REPORT_SERVICE_022="MktRepo2ServiceImpl:: {} No data available. Kindly check parameters of the stored procedure ::web_mkt_2all";

	
	public static final String MKT_REPORT_CONTROLLER_05="MktRepo5Controller:: {} method execution started, myear : {} , div_code: {}";
	public static final String MKT_REPORT_SERVICE_05="MktRepo5ServiceImpl:: {} method execution started";

}
