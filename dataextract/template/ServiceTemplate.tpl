package $!{service_package};

import java.util.List;
import java.util.Map;

import $!{entity_classpath};
import $!{dao_classpath};

import com.google.common.collect.Maps;
import com.infotop.webharvest.common.log.BusinessLogger;
import com.infotop.webharvest.system.account.service.ShiroDbRealm.ShiroUser;

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
 * $!{className}Manager
 * $Id: $!{className}Manager.java,v 0.0.1 $!{nowTime}  $
 */
@Component
@Transactional(readOnly = true)
public class $!{className}Service {
	
	
	@Autowired
	private $!{className}Dao $!{beanName}Dao;
	
	@Autowired
	private BusinessLogger businessLogger;
	/**
	 * 保存一个$!{className}，如果保存成功返回该对象的id，否则返回null
	 * @param entity
	 * @return 保存成功的对象的Id
	 */
	@Transactional(readOnly = false)
	public void save($!{className} entity){
		$!{beanName}Dao.save(entity);
		Map logData = Maps.newHashMap();
		logData.put("ID", entity.getId());
		businessLogger.log("$!{beanName}", "SAVE", getCurrentUserName(), logData);
	}
	
	/**
	 * 根据一个ID得到$!{className}
	 * 
	 * @param id
	 * @return
	 */
	public $!{className} get(Long id){
		Map logData = Maps.newHashMap();
		logData.put("ID", id);
		businessLogger.log("$!{beanName}", "GET", getCurrentUserName(), logData);
		return $!{beanName}Dao.findOne(id);
	}
	
	/**
	 * 删除一个$!{className}
	 * @param id
	 * @return
	 */
	@Transactional(readOnly = false)
    public void delete(Long id) {
        this.$!{beanName}Dao.delete(id);
		Map logData = Maps.newHashMap();
		logData.put("ID", id);
		businessLogger.log("$!{beanName}", "DELETE", getCurrentUserName(), logData);
    }
	
	/**
	 * 批量删除$!{className}
	 * @param ids
	 * @return
	 */
	@Transactional(readOnly = false)
	public void delete(List<Long> ids){
		List<$!{className}> test = (List<$!{className}>) this.$!{beanName}Dao.findAll(ids);
		this.$!{beanName}Dao.delete(test);
		Map logData = Maps.newHashMap();
		logData.put("IDS", ids);
		businessLogger.log("$!{beanName}", "DELETE", getCurrentUserName(), logData);
	}
	

	/**
	 * 删除操作，支持单个删除和批量删除
	 * @param ids 要删除的记录ID
	 * @param bool true:物理删除 false:逻辑删除
	 */
	/*@Transactional(readOnly = false)
	public void delete(List<Long> ids, boolean bool) {
		List<$!{className}> temp = (List<$!{className}>) this.$!{beanName}Dao.findAll(ids);
		if (bool) {
			this.$!{beanName}Dao.delete(temp);
		} else {
			if (temp != null && temp.size() > 0) {
				for ($!{className} obj : temp) {
					obj.setFlag(1);
					this.$!{beanName}Dao.save(obj);
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
    private Specification<$!{className}> buildSpecification(Map<String, Object> filterParams) {
        Map<String, SearchFilter> filters = SearchFilter.parse(filterParams);
        Specification<$!{className}> spec = DynamicSpecifications.bySearchFilter(filters.values(), $!{className}.class);
        return spec;
    }
	
    
    public DataGrid dataGrid(Map<String, Object> searchParams, int pageNumber,
			int rows, String sortType, String order) {
		Page<$!{className}> page = getAll$!{className}(searchParams,
				pageNumber, rows, sortType, order);
		DataGrid dataGrid = new DataGrid();
		dataGrid.setTotal(page.getTotalElements());
		dataGrid.setRows(page.getContent());
		return dataGrid;
	}
    
    public Page<$!{className}> getAll$!{className}(
			Map<String, Object> filterParams, int pageNumber, int pageSize,
			String sortType, String order) {
		PageRequest pageRequest = buildPageRequest(pageNumber, pageSize,
				sortType, order);
		Specification<$!{className}> spec = buildSpecification(filterParams);
		return $!{beanName}Dao.findAll(spec, pageRequest);
	}
    
    /**
	 * 取出Shiro中的当前用户LoginName.
	 */
	public String getCurrentUserName() {
		ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		return user.loginName;
	}
}
