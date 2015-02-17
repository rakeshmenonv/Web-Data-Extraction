package com.infotop.webharvest.textTagInfo.service;

import java.util.List;
import java.util.Map;

import com.infotop.webharvest.textTagInfo.entity.textTagInfo;
import com.infotop.webharvest.textTagInfo.repository.textTagInfoDao;

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
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springside.modules.persistence.DynamicSpecifications;
import org.springside.modules.persistence.SearchFilter;

/**
 * textTagInfoManager
 * $Id: textTagInfoManager.java,v 0.0.1 2015-02-12 15:43:55  $
 */
@Component
@Transactional(readOnly = true)
public class textTagInfoService {
	
	
	@Autowired
	private textTagInfoDao textTagInfoDao;
	
	@Autowired
	private BusinessLogger businessLogger;
	/**
	 * 保存一个textTagInfo，如果保存成功返回该对象的id，否则返回null
	 * @param entity
	 * @return 保存成功的对象的Id
	 */
	@Transactional(readOnly = false)
	public void save(textTagInfo entity){
		textTagInfoDao.save(entity);
		Map logData = Maps.newHashMap();
		logData.put("ID", entity.getId());
		businessLogger.log("textTagInfo", "SAVE", getCurrentUserName(), logData);
	}
	
	/**
	 * 根据一个ID得到textTagInfo
	 * 
	 * @param id
	 * @return
	 */
	public textTagInfo get(Long id){
		Map logData = Maps.newHashMap();
		logData.put("ID", id);
		businessLogger.log("textTagInfo", "GET", getCurrentUserName(), logData);
		return textTagInfoDao.findOne(id);
	}
	
	/**
	 * 删除一个textTagInfo
	 * @param id
	 * @return
	 */
	@Transactional(readOnly = false)
    public void delete(Long id) {
        this.textTagInfoDao.delete(id);
		Map logData = Maps.newHashMap();
		logData.put("ID", id);
		businessLogger.log("textTagInfo", "DELETE", getCurrentUserName(), logData);
    }
	
	/**
	 * 批量删除textTagInfo
	 * @param ids
	 * @return
	 */
	@Transactional(readOnly = false)
	public void delete(List<Long> ids){
		List<textTagInfo> test = (List<textTagInfo>) this.textTagInfoDao.findAll(ids);
		this.textTagInfoDao.delete(test);
		Map logData = Maps.newHashMap();
		logData.put("IDS", ids);
		businessLogger.log("textTagInfo", "DELETE", getCurrentUserName(), logData);
	}
	

	/**
	 * 删除操作，支持单个删除和批量删除
	 * @param ids 要删除的记录ID
	 * @param bool true:物理删除 false:逻辑删除
	 */
	/*@Transactional(readOnly = false)
	public void delete(List<Long> ids, boolean bool) {
		List<textTagInfo> temp = (List<textTagInfo>) this.textTagInfoDao.findAll(ids);
		if (bool) {
			this.textTagInfoDao.delete(temp);
		} else {
			if (temp != null && temp.size() > 0) {
				for (textTagInfo obj : temp) {
					obj.setFlag(1);
					this.textTagInfoDao.save(obj);
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
    private Specification<textTagInfo> buildSpecification(Map<String, Object> filterParams) {
        Map<String, SearchFilter> filters = SearchFilter.parse(filterParams);
        Specification<textTagInfo> spec = DynamicSpecifications.bySearchFilter(filters.values(), textTagInfo.class);
        return spec;
    }
	
    
    public DataGrid dataGrid(Map<String, Object> searchParams, int pageNumber,
			int rows, String sortType, String order) {
		Page<textTagInfo> page = getAlltextTagInfo(searchParams,
				pageNumber, rows, sortType, order);
		DataGrid dataGrid = new DataGrid();
		dataGrid.setTotal(page.getTotalElements());
		dataGrid.setRows(page.getContent());
		return dataGrid;
	}
    
    public Page<textTagInfo> getAlltextTagInfo(
			Map<String, Object> filterParams, int pageNumber, int pageSize,
			String sortType, String order) {
		PageRequest pageRequest = buildPageRequest(pageNumber, pageSize,
				sortType, order);
		Specification<textTagInfo> spec = buildSpecification(filterParams);
		return textTagInfoDao.findAll(spec, pageRequest);
	}
    
    /**
	 * 取出Shiro中的当前用户LoginName.
	 */
	public String getCurrentUserName() {
		ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		return user.loginName;
	}
}
