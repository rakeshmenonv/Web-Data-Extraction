package com.infotop.webharvest.pageurlinfo.service;

import java.util.List;
import java.util.Map;

import com.infotop.common.log.BusinessLogger;
import com.infotop.system.account.service.ShiroDbRealm.ShiroUser;
import com.infotop.webharvest.pageurlinfo.entity.Pageurlinfo;
import com.infotop.webharvest.pageurlinfo.repository.PageurlinfoDao;
import com.google.common.collect.Maps;






import net.infotop.web.easyui.DataGrid;

import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springside.modules.persistence.DynamicSpecifications;
import org.springside.modules.persistence.SearchFilter;

/**
 * PageurlinfoManager
 * $Id: PageurlinfoManager.java,v 0.0.1 2015-02-17 11:05:00  $
 */
@Component
@Transactional(readOnly = true)
public class PageurlinfoService {
	
	
	@Autowired
	private PageurlinfoDao pageurlinfoDao;
	
	@Autowired
	private BusinessLogger businessLogger;
	/**
	 * 保存一个Pageurlinfo，如果保存成功返回该对象的id，否则返回null
	 * @param entity
	 * @return 保存成功的对象的Id
	 */
	@Transactional(readOnly = false)
	public void save(Pageurlinfo entity){
		pageurlinfoDao.save(entity);
		Map logData = Maps.newHashMap();
		logData.put("ID", entity.getId());
//		businessLogger.log("pageurlinfo", "SAVE", getCurrentUserName(), logData);
		try{
			businessLogger.log("pageurlinfo", "SAVE", getCurrentUserName(), logData);
		}catch(Exception e){
			businessLogger.log("pageurlinfo", "SAVE", "cronjob", logData);	
		}
	}
	
	/**
	 * 根据一个ID得到Pageurlinfo
	 * 
	 * @param id
	 * @return
	 */
	public Pageurlinfo get(Long id){
		Map logData = Maps.newHashMap();
		logData.put("ID", id);
		businessLogger.log("pageurlinfo", "GET", getCurrentUserName(), logData);
		return pageurlinfoDao.findOne(id);
	}
	
	/**
	 * 删除一个Pageurlinfo
	 * @param id
	 * @return
	 */
	@Transactional(readOnly = false)
    public void delete(Long id) {
        this.pageurlinfoDao.delete(id);
		Map logData = Maps.newHashMap();
		logData.put("ID", id);
		businessLogger.log("pageurlinfo", "DELETE", getCurrentUserName(), logData);
    }
	
	/**
	 * 批量删除Pageurlinfo
	 * @param ids
	 * @return
	 */
	@Transactional(readOnly = false)
	public void delete(List<Long> ids){
		List<Pageurlinfo> test = (List<Pageurlinfo>) this.pageurlinfoDao.findAll(ids);
		this.pageurlinfoDao.delete(test);
		Map logData = Maps.newHashMap();
		logData.put("IDS", ids);
		businessLogger.log("pageurlinfo", "DELETE", getCurrentUserName(), logData);
	}
	
	@Transactional(readOnly = false)
	public void deleteByUrl(List<String> url) {
		for(String urlName:url){
			List<Pageurlinfo> pageurlinfo =  pageurlinfoDao.findByUrl(urlName);
			for(Pageurlinfo pageurlinfoDelete:pageurlinfo){
				delete(pageurlinfoDelete.getId());
			}
			
		}
		// TODO Auto-generated method stub
		
	}

	/**
	 * 删除操作，支持单个删除和批量删除
	 * @param ids 要删除的记录ID
	 * @param bool true:物理删除 false:逻辑删除
	 */
	/*@Transactional(readOnly = false)
	public void delete(List<Long> ids, boolean bool) {
		List<Pageurlinfo> temp = (List<Pageurlinfo>) this.pageurlinfoDao.findAll(ids);
		if (bool) {
			this.pageurlinfoDao.delete(temp);
		} else {
			if (temp != null && temp.size() > 0) {
				for (Pageurlinfo obj : temp) {
					obj.setFlag(1);
					this.pageurlinfoDao.save(obj);
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
    private Specification<Pageurlinfo> buildSpecification(Map<String, Object> filterParams) {
        Map<String, SearchFilter> filters = SearchFilter.parse(filterParams);
        Specification<Pageurlinfo> spec = DynamicSpecifications.bySearchFilter(filters.values(), Pageurlinfo.class);
        return spec;
    }
	
    
    public DataGrid dataGrid(Map<String, Object> searchParams, int pageNumber,
			int rows, String sortType, String order) {
		Page<Pageurlinfo> page = getAllPageurlinfo(searchParams,
				pageNumber, rows, sortType, order);
		DataGrid dataGrid = new DataGrid();
		dataGrid.setTotal(page.getTotalElements());
		dataGrid.setRows(page.getContent());
		return dataGrid;
	}
    
    public Page<Pageurlinfo> getAllPageurlinfo(
			Map<String, Object> filterParams, int pageNumber, int pageSize,
			String sortType, String order) {
		PageRequest pageRequest = buildPageRequest(pageNumber, pageSize,
				sortType, order);
		Specification<Pageurlinfo> spec = buildSpecification(filterParams);
		return pageurlinfoDao.findAll(spec, pageRequest);
	}
    
    /**
	 * 取出Shiro中的当前用户LoginName.
	 */
	public String getCurrentUserName() {
		ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		return user.loginName;
	}
	
	public List<Pageurlinfo> getPageurlinfoByjobon(String jobon){
		return pageurlinfoDao.getPageurlinfoByjobon(jobon);
	}
	
	public List<Pageurlinfo> findByJobonNotNull(){
		return pageurlinfoDao.findByJobonNotNull();
	}

	
}
