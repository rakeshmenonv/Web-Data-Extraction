package com.infotop.webharvest.dataharvest.service;

import java.util.Map;
import java.util.Set;

import net.infotop.web.easyui.DataGrid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional(readOnly = true)
public class AuditService {

	@Autowired
	JdbcTemplate jdbcTemplate;
	
	 public DataGrid dataGridForLog(Map<String, Object> searchParams, int pageNumber,
				int rows, String sortType, String order) {			
			return getUrlCountList(searchParams,rows,(pageNumber-1)*rows);
		}
	    
	    public DataGrid getUrlCountList(Map<String, Object> filterParams,int limitvalue,int offsetvalue) {		
			
	    	DataGrid dataGrid = new DataGrid();		
			String sql="select id,url, COUNT(*) AS CountOf FROM page_url_info GROUP BY url";		
			String countSql="SELECT COUNT(DISTINCT url) FROM page_url_info ";		
			String	whereSql = searchQuery(filterParams);
			dataGrid.setTotal(jdbcTemplate.queryForLong(countSql+whereSql));	
			whereSql+=" limit "+offsetvalue+","+limitvalue;		
			dataGrid.setRows(jdbcTemplate.queryForList(sql+whereSql));	
			return dataGrid;
	}

	    public DataGrid dataGridForUrlInfo(Map<String, Object> searchParams, int pageNumber,
				int rows, String sortType, String order,String Url) {			
			return getUrlInfoList(searchParams,rows,(pageNumber-1)*rows,Url);
		} 
	    
	    public DataGrid getUrlInfoList(Map<String, Object> filterParams,int limitvalue,int offsetvalue,String Url) {		
			
	    	DataGrid dataGrid = new DataGrid();		
			String sql="select * FROM page_url_info where url='"+Url+"'";		
			String countSql="SELECT COUNT(*) FROM page_url_info where url='"+Url+"'";		
			String	whereSql = searchQuery(filterParams);
			dataGrid.setTotal(jdbcTemplate.queryForLong(countSql+whereSql));	
			whereSql+=" limit "+offsetvalue+","+limitvalue;		
			dataGrid.setRows(jdbcTemplate.queryForList(sql+whereSql));	
			return dataGrid;
	}
	    
	String searchQuery(Map<String, Object> filterParams) {
		String Query = "";
		Set<String> keyset = filterParams.keySet();
		for (String key : keyset) {
			String[] filter = key.split("_");
			String condition = "";
			if (filter[0].equals("EQ")) {
				condition = "='" + filterParams.get(key) + "' ";
			} else if (filter[0].equals("NE")) {
				condition = "<>'" + filterParams.get(key) + "' ";
			} else if (filter[0].equals("LIKE")) {
				condition = " LIKE '%" + filterParams.get(key) + "%'";
			} else if (filter[0].equals("GTE")) {
				condition = ">='" + filterParams.get(key) + "'";
			} else if (filter[0].equals("LTE")) {
				condition = "<='" + filterParams.get(key) + "'";
			} else if (filter[0].equals("GT")) {
				condition = ">'" + filterParams.get(key) + "'";
			} else if (filter[0].equals("LT")) {
				condition = "<'" + filterParams.get(key) + "'";
			}
			Query += " and " + filter[1].replaceAll("(.)([A-Z])", "$1_$2")
					+ condition;
		}
		return Query;
	}
}
