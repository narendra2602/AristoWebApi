package com.aristowebapi.dao;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;

	@Repository
	public class StkRepo5Repository {

	    @Autowired
	    private JdbcTemplate jdbcTemplate;

	    private SimpleJdbcCall simpleJdbcCall;

	    @PostConstruct
	    public void init() {
	        simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate)
	                .withProcedureName("web_stockiest_repo5New")
	                .returningResultSet("stockiestData", new RowMapper<Map<String, Object>>() {
	                    @Override
	                    public Map<String, Object> mapRow(ResultSet rs, int rowNum) throws SQLException {
	                        Map<String, Object> row = new LinkedHashMap<>();
	                        ResultSetMetaData metaData = rs.getMetaData();
	                        int columnCount = metaData.getColumnCount();

	                        for (int i = 1; i <= columnCount; i++) {
	                            String columnName = metaData.getColumnLabel(i); // use alias if present
	                            Object columnValue = rs.getObject(i);
	                            
	                            
	                            if (columnValue instanceof BigDecimal) {
	                                row.put(columnName, ((BigDecimal) columnValue).toPlainString());
	                            }   
	                            else {
	                                row.put(columnName, columnValue);
	                            }

	                          //  row.put(columnName, columnValue);
	                        }
	                        return row;
	                    }
	                });
	    }
	    

	    @SuppressWarnings("unchecked")
		public List<HashMap<String, Object>> getStockiestRepo5(int myear,int divCode,int depoCode,int smon,int emon,int code,int loginId) {
	        Map<String, Object> inParams = new HashMap<>();
	        inParams.put("in_myear", myear);
	        inParams.put("in_div", divCode);
	        inParams.put("in_depo", depoCode);
	        inParams.put("in_fmnth", smon);
	        inParams.put("in_tmnth", emon);
	        inParams.put("in_code", code);
	        inParams.put("userid", loginId);

	        Map<String, Object> out = simpleJdbcCall.execute(inParams);

	        // "stockiestData" key contains the result list
	        return (List<HashMap<String, Object>>) out.get("stockiestData");
	    }

	    
	   	}



