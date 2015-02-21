package com.infotop.webharvest.pageInfo.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.infotop.webharvest.pageInfo.entity.pageInfo;
import com.infotop.webharvest.pageInfo.repository.pageInfoDao;
import com.google.common.collect.Maps;
import com.infotop.common.log.BusinessLogger;
import com.infotop.system.account.service.ShiroDbRealm.ShiroUser;

import net.infotop.web.easyui.DataGrid;

import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springside.modules.persistence.DynamicSpecifications;
import org.springside.modules.persistence.SearchFilter;

/**
 * pageInfoManager
 * $Id: pageInfoManager.java,v 0.0.1 2015-02-12 15:41:12  $
 */
@Component
@Transactional(readOnly = true)
public class pageInfoService {
	
	
	@Autowired
	private pageInfoDao pageInfoDao;
	
	@Autowired
	private BusinessLogger businessLogger;
	
	@Autowired
	JdbcTemplate jdbcTemplate;
	/**
	 * 保存一个pageInfo，如果保存成功返回该对象的id，否则返回null
	 * @param entity
	 * @return 保存成功的对象的Id
	 */
	@Transactional(readOnly = false)
	public void save(pageInfo entity){
		pageInfoDao.save(entity);
		Map logData = Maps.newHashMap();
		logData.put("ID", entity.getId());
		businessLogger.log("pageInfo", "SAVE", getCurrentUserName(), logData);
	}
	
	/**
	 * 根据一个ID得到pageInfo
	 * 
	 * @param id
	 * @return
	 */
	public pageInfo get(Long id){
		Map logData = Maps.newHashMap();
		logData.put("ID", id);
		businessLogger.log("pageInfo", "GET", getCurrentUserName(), logData);
		return pageInfoDao.findOne(id);
	}
	
	/**
	 * 删除一个pageInfo
	 * @param id
	 * @return
	 */
	@Transactional(readOnly = false)
    public void delete(Long id) {
        this.pageInfoDao.delete(id);
		Map logData = Maps.newHashMap();
		logData.put("ID", id);
		businessLogger.log("pageInfo", "DELETE", getCurrentUserName(), logData);
    }
	
	/**
	 * 批量删除pageInfo
	 * @param ids
	 * @return
	 */
	@Transactional(readOnly = false)
	public void delete(List<Long> ids){
		List<pageInfo> test = (List<pageInfo>) this.pageInfoDao.findAll(ids);
		this.pageInfoDao.delete(test);
		Map logData = Maps.newHashMap();
		logData.put("IDS", ids);
		businessLogger.log("pageInfo", "DELETE", getCurrentUserName(), logData);
	}
	

	/**
	 * 删除操作，支持单个删除和批量删除
	 * @param ids 要删除的记录ID
	 * @param bool true:物理删除 false:逻辑删除
	 */
	/*@Transactional(readOnly = false)
	public void delete(List<Long> ids, boolean bool) {
		List<pageInfo> temp = (List<pageInfo>) this.pageInfoDao.findAll(ids);
		if (bool) {
			this.pageInfoDao.delete(temp);
		} else {
			if (temp != null && temp.size() > 0) {
				for (pageInfo obj : temp) {
					obj.setFlag(1);
					this.pageInfoDao.save(obj);
				}
			}
		}
	}*/
   
   
    /**
	 * 创建分页请求.
	 */
	private PageRequest buildPageRequest(int pageNumber, int pagzSize,
			String sortType, String order) {
		Sort sort = null;
		if ("auto".equals(sortType)) {
			if ("asc".equalsIgnoreCase(order)) {
				sort = new Sort(Direction.ASC, "id");
			} else {
				sort = new Sort(Direction.DESC, "id");
			}
		} else {
			if ("asc".equalsIgnoreCase(order)) {
				sort = new Sort(Direction.ASC, sortType);
			} else {
				sort = new Sort(Direction.DESC, sortType);
			}
		}
		return new PageRequest(pageNumber - 1, pagzSize, sort);
	}

    /**
     * 创建动态查询条件组合.
     */
    private Specification<pageInfo> buildSpecification(Map<String, Object> filterParams) {
        Map<String, SearchFilter> filters = SearchFilter.parse(filterParams);
        Specification<pageInfo> spec = DynamicSpecifications.bySearchFilter(filters.values(), pageInfo.class);
        return spec;
    }
	
    
    public DataGrid dataGrid(Map<String, Object> searchParams, int pageNumber,
			int rows, String sortType, String order) {
		Page<pageInfo> page = getAllpageInfo(searchParams,
				pageNumber, rows, sortType, order);
		DataGrid dataGrid = new DataGrid();
		dataGrid.setTotal(page.getTotalElements());
		dataGrid.setRows(page.getContent());
		return dataGrid;
	}
    
    public DataGrid dataGridForLog(Map<String, Object> searchParams, int pageNumber,
			int rows, String sortType, String order) {			
		return getUrlCountList(searchParams,rows,(pageNumber-1)*rows);
	}
    String searchQuery(Map<String, Object> filterParams){
    	String Query="";
    	Set<String> keyset=filterParams.keySet();
    	for(String key:keyset){    	    		
    		String[] filter=key.split("_"); 
    		String condition="";
    		if(filter[0].equals("EQ")){
    			condition="='"+filterParams.get(key)+"' ";
    		}else if(filter[0].equals("NE")){
    			condition="<>'"+filterParams.get(key)+"' ";
    		}else if(filter[0].equals("LIKE")){
    			condition=" LIKE '%"+filterParams.get(key)+"%'";
    		}else if(filter[0].equals("GTE")){
    			condition=">='"+filterParams.get(key)+"'";
    		}else if(filter[0].equals("LTE")){
    			condition="<='"+filterParams.get(key)+"'";
    		}else if(filter[0].equals("GT")){
    			condition=">'"+filterParams.get(key)+"'";    			
    		}else if(filter[0].equals("LT")){
    			condition="<'"+filterParams.get(key)+"'";
    		}		
    		Query+=" and "+filter[1].replaceAll("(.)([A-Z])","$1_$2")+condition;
    	}    	
		return Query;    	
    }
    public DataGrid getUrlCountList(Map<String, Object> filterParams,int limitvalue,int offsetvalue) {		
		DataGrid dataGrid = new DataGrid();		
		
		String sql="select id,url, COUNT(*) AS CountOf FROM page_url_info GROUP BY url";		
		
		String countSql="SELECT COUNT(DISTINCT url) FROM page_url_info ";		
		
		//String whereSql=" where 1=1 ";			
		String	whereSql = searchQuery(filterParams);
	//	whereSql+="order by B.name";
		//System.out.println("count==============="+jdbcTemplate.queryForLong(countSql+whereSql));
		dataGrid.setTotal(jdbcTemplate.queryForLong(countSql+whereSql));	
		whereSql+=" limit "+offsetvalue+","+limitvalue;		
		System.out.println("sql======================"+jdbcTemplate.queryForList(sql+whereSql));			
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
		
		//String whereSql=" where 1=1 ";			
		String	whereSql = searchQuery(filterParams);
	//	whereSql+="order by B.name";
		//System.out.println("count==============="+jdbcTemplate.queryForLong(countSql+whereSql));
		dataGrid.setTotal(jdbcTemplate.queryForLong(countSql+whereSql));	
		whereSql+=" limit "+offsetvalue+","+limitvalue;		
		System.out.println("sql======================"+jdbcTemplate.queryForList(sql+whereSql));			
		dataGrid.setRows(jdbcTemplate.queryForList(sql+whereSql));	
		return dataGrid;
}
    public Page<pageInfo> getAllpageInfo(
			Map<String, Object> filterParams, int pageNumber, int pageSize,
			String sortType, String order) {
		PageRequest pageRequest = buildPageRequest(pageNumber, pageSize,
				sortType, order);
		Specification<pageInfo> spec = buildSpecification(filterParams);
		return pageInfoDao.findAll(spec, pageRequest);
	}
    
    /**
	 * 取出Shiro中的当前用户LoginName.
	 */
	public String getCurrentUserName() {
		ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		return user.loginName;
	}
}
