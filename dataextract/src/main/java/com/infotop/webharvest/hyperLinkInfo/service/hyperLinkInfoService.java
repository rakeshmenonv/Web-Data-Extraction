package com.infotop.webharvest.hyperLinkInfo.service;

import java.util.List;
import java.util.Map;

import com.infotop.webharvest.hyperLinkInfo.entity.hyperLinkInfo;
import com.infotop.webharvest.hyperLinkInfo.repository.hyperLinkInfoDao;

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
 * hyperLinkInfoManager
 * $Id: hyperLinkInfoManager.java,v 0.0.1 2015-02-12 15:43:11  $
 */
@Component
@Transactional(readOnly = true)
public class hyperLinkInfoService {
	
	
	@Autowired
	private hyperLinkInfoDao hyperLinkInfoDao;
	
	@Autowired
	private BusinessLogger businessLogger;
	/**
	 * 保存一个hyperLinkInfo，如果保存成功返回该对象的id，否则返回null
	 * @param entity
	 * @return 保存成功的对象的Id
	 */
	@Transactional(readOnly = false)
	public void save(hyperLinkInfo entity){
		hyperLinkInfoDao.save(entity);
		Map logData = Maps.newHashMap();
		logData.put("ID", entity.getId());
		businessLogger.log("hyperLinkInfo", "SAVE", getCurrentUserName(), logData);
	}
	
	/**
	 * 根据一个ID得到hyperLinkInfo
	 * 
	 * @param id
	 * @return
	 */
	public hyperLinkInfo get(Long id){
		Map logData = Maps.newHashMap();
		logData.put("ID", id);
		businessLogger.log("hyperLinkInfo", "GET", getCurrentUserName(), logData);
		return hyperLinkInfoDao.findOne(id);
	}
	
	/**
	 * 删除一个hyperLinkInfo
	 * @param id
	 * @return
	 */
	@Transactional(readOnly = false)
    public void delete(Long id) {
        this.hyperLinkInfoDao.delete(id);
		Map logData = Maps.newHashMap();
		logData.put("ID", id);
		businessLogger.log("hyperLinkInfo", "DELETE", getCurrentUserName(), logData);
    }
	
	/**
	 * 批量删除hyperLinkInfo
	 * @param ids
	 * @return
	 */
	@Transactional(readOnly = false)
	public void delete(List<Long> ids){
		List<hyperLinkInfo> test = (List<hyperLinkInfo>) this.hyperLinkInfoDao.findAll(ids);
		this.hyperLinkInfoDao.delete(test);
		Map logData = Maps.newHashMap();
		logData.put("IDS", ids);
		businessLogger.log("hyperLinkInfo", "DELETE", getCurrentUserName(), logData);
	}
	

	/**
	 * 删除操作，支持单个删除和批量删除
	 * @param ids 要删除的记录ID
	 * @param bool true:物理删除 false:逻辑删除
	 */
	/*@Transactional(readOnly = false)
	public void delete(List<Long> ids, boolean bool) {
		List<hyperLinkInfo> temp = (List<hyperLinkInfo>) this.hyperLinkInfoDao.findAll(ids);
		if (bool) {
			this.hyperLinkInfoDao.delete(temp);
		} else {
			if (temp != null && temp.size() > 0) {
				for (hyperLinkInfo obj : temp) {
					obj.setFlag(1);
					this.hyperLinkInfoDao.save(obj);
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
    private Specification<hyperLinkInfo> buildSpecification(Map<String, Object> filterParams) {
        Map<String, SearchFilter> filters = SearchFilter.parse(filterParams);
        Specification<hyperLinkInfo> spec = DynamicSpecifications.bySearchFilter(filters.values(), hyperLinkInfo.class);
        return spec;
    }
	
    
    public DataGrid dataGrid(Map<String, Object> searchParams, int pageNumber,
			int rows, String sortType, String order) {
		Page<hyperLinkInfo> page = getAllhyperLinkInfo(searchParams,
				pageNumber, rows, sortType, order);
		DataGrid dataGrid = new DataGrid();
		dataGrid.setTotal(page.getTotalElements());
		dataGrid.setRows(page.getContent());
		return dataGrid;
	}
    
    public Page<hyperLinkInfo> getAllhyperLinkInfo(
			Map<String, Object> filterParams, int pageNumber, int pageSize,
			String sortType, String order) {
		PageRequest pageRequest = buildPageRequest(pageNumber, pageSize,
				sortType, order);
		Specification<hyperLinkInfo> spec = buildSpecification(filterParams);
		return hyperLinkInfoDao.findAll(spec, pageRequest);
	}
    
    /**
	 * 取出Shiro中的当前用户LoginName.
	 */
	public String getCurrentUserName() {
		ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		return user.loginName;
	}
}
