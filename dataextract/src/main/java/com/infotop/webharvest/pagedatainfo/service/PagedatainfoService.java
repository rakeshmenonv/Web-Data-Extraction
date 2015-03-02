package com.infotop.webharvest.pagedatainfo.service;

import java.util.List;
import java.util.Map;

import com.infotop.common.log.BusinessLogger;
import com.infotop.system.account.service.ShiroDbRealm.ShiroUser;
import com.infotop.webharvest.pagedatainfo.entity.Pagedatainfo;
import com.infotop.webharvest.pagedatainfo.repository.PagedatainfoDao;
import com.infotop.webharvest.pageurlinfo.entity.Pageurlinfo;
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
 * PagedatainfoManager
 * $Id: PagedatainfoManager.java,v 0.0.1 2015-02-17 11:06:13  $
 */
@Component
@Transactional(readOnly = true)
public class PagedatainfoService {
	
	
	@Autowired
	private PagedatainfoDao pagedatainfoDao;
	
	@Autowired
	private BusinessLogger businessLogger;
	/**
	 * 保存一个Pagedatainfo，如果保存成功返回该对象的id，否则返回null
	 * @param entity
	 * @return 保存成功的对象的Id
	 */
	@Transactional(readOnly = false)
	public void save(Pagedatainfo entity){
		pagedatainfoDao.save(entity);
		Map logData = Maps.newHashMap();
		logData.put("ID", entity.getId());
		try{
			businessLogger.log("pageurlinfo", "SAVE", getCurrentUserName(), logData);
		}catch(Exception e){
			businessLogger.log("pageurlinfo", "SAVE", "cronjob", logData);	
		}
	}
	
	/**
	 * 根据一个ID得到Pagedatainfo
	 * 
	 * @param id
	 * @return
	 */
	public Pagedatainfo get(Long id){
		Map logData = Maps.newHashMap();
		logData.put("ID", id);
		businessLogger.log("pagedatainfo", "GET", getCurrentUserName(), logData);
		return pagedatainfoDao.findOne(id);
	}
	
	/**
	 * 删除一个Pagedatainfo
	 * @param id
	 * @return
	 */
	@Transactional(readOnly = false)
    public void delete(Long id) {
        this.pagedatainfoDao.delete(id);
		Map logData = Maps.newHashMap();
		logData.put("ID", id);
		businessLogger.log("pagedatainfo", "DELETE", getCurrentUserName(), logData);
    }
	
	/**
	 * 批量删除Pagedatainfo
	 * @param ids
	 * @return
	 */
	@Transactional(readOnly = false)
	public void delete(List<Long> ids){
		List<Pagedatainfo> test = (List<Pagedatainfo>) this.pagedatainfoDao.findAll(ids);
		this.pagedatainfoDao.delete(test);
		Map logData = Maps.newHashMap();
		logData.put("IDS", ids);
		businessLogger.log("pagedatainfo", "DELETE", getCurrentUserName(), logData);
	}
	

	/**
	 * 删除操作，支持单个删除和批量删除
	 * @param ids 要删除的记录ID
	 * @param bool true:物理删除 false:逻辑删除
	 */
	/*@Transactional(readOnly = false)
	public void delete(List<Long> ids, boolean bool) {
		List<Pagedatainfo> temp = (List<Pagedatainfo>) this.pagedatainfoDao.findAll(ids);
		if (bool) {
			this.pagedatainfoDao.delete(temp);
		} else {
			if (temp != null && temp.size() > 0) {
				for (Pagedatainfo obj : temp) {
					obj.setFlag(1);
					this.pagedatainfoDao.save(obj);
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
    private Specification<Pagedatainfo> buildSpecification(Map<String, Object> filterParams) {
        Map<String, SearchFilter> filters = SearchFilter.parse(filterParams);
        Specification<Pagedatainfo> spec = DynamicSpecifications.bySearchFilter(filters.values(), Pagedatainfo.class);
        return spec;
    }
	
    
    public DataGrid dataGrid(Map<String, Object> searchParams, int pageNumber,
			int rows, String sortType, String order) {
		Page<Pagedatainfo> page = getAllPagedatainfo(searchParams,
				pageNumber, rows, sortType, order);
		DataGrid dataGrid = new DataGrid();
		dataGrid.setTotal(page.getTotalElements());
		dataGrid.setRows(page.getContent());
		return dataGrid;
	}
    
    public Page<Pagedatainfo> getAllPagedatainfo(
			Map<String, Object> filterParams, int pageNumber, int pageSize,
			String sortType, String order) {
		PageRequest pageRequest = buildPageRequest(pageNumber, pageSize,
				sortType, order);
		Specification<Pagedatainfo> spec = buildSpecification(filterParams);
		return pagedatainfoDao.findAll(spec, pageRequest);
	}
    
    public List<Pagedatainfo> getAlldatainfo(Pageurlinfo pageurlinfo) {
		return pagedatainfoDao.getPagedatainfoBypageurlinfo(pageurlinfo);
	}
    /**
	 * 取出Shiro中的当前用户LoginName.
	 */
	public String getCurrentUserName() {
		ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		return user.loginName;
	}
	
	public void deleteByTableGroupKey(List<String> tableGroupKeyList) {
		pagedatainfoDao.deleteByTableGroupKey(tableGroupKeyList);		
	}
}
