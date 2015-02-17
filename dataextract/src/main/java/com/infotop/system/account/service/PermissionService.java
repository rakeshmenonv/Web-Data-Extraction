package com.infotop.system.account.service;

import java.util.List;
import java.util.Map;

import net.infotop.web.easyui.Tree;

import org.apache.commons.lang3.StringUtils;
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

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.infotop.system.account.entity.Permission;
import com.infotop.system.account.repository.PermissionDao;

@Component
@Transactional(readOnly = true)
public class PermissionService {

	@Autowired
	private PermissionDao permissionDao;

	/**
	 * 获取所有授权列表
	 * 
	 * @return
	 */
	public List<Permission> getAll() {
		return (List<Permission>) permissionDao.findAll();
	}

	/**
	 * 获得所有授权的JSON
	 * 
	 * @return
	 */
	public String getJosnData() {
		// 获得所有权限列表
		List<Permission> test = getAll();
		StringBuilder PERMISSION_JSON = new StringBuilder();
		int size = test.size();
		PERMISSION_JSON.append("[");
		for (Permission m : test) {
			size--;
			PERMISSION_JSON.append("{id:'");
			PERMISSION_JSON.append(m.getCkey());
			PERMISSION_JSON.append("',pId:'");
			PERMISSION_JSON.append(m.getPkey());
			PERMISSION_JSON.append("',name:'");
			PERMISSION_JSON.append(m.getName());
			PERMISSION_JSON.append("',value:'");
			PERMISSION_JSON.append(m.getValue());
			PERMISSION_JSON.append("',no:'");
			PERMISSION_JSON.append(m.getId());
			PERMISSION_JSON.append("',open:'false'}");
			if (size > 0) {
				PERMISSION_JSON.append(",");
			}
		}
		PERMISSION_JSON.append("]");

		return PERMISSION_JSON.toString();
	}

	public String getJosnData(List<String> permissionList, String type) {
		List<Permission> test = permissionDao
				.findPermissionByPermissionType(type);
		StringBuilder PERMISSION_JSON = new StringBuilder();
		int size = test.size();
		PERMISSION_JSON.append("[");
		for (Permission m : test) {
			size--;
			PERMISSION_JSON.append("{id:\"");
			PERMISSION_JSON.append(m.getCkey());
			PERMISSION_JSON.append("\",pId:\"");
			PERMISSION_JSON.append(m.getPkey());
			PERMISSION_JSON.append("\",name:'");
			PERMISSION_JSON.append(m.getName());
			for (String permission : permissionList) {
				if (permission.equalsIgnoreCase(m.getValue())) {
					PERMISSION_JSON.append("',open:'true',checked:'true");
					break;
				}
			}
			PERMISSION_JSON.append("'}");

			if (size > 0) {
				PERMISSION_JSON.append(",");
			}
		}
		PERMISSION_JSON.append("]");

		return PERMISSION_JSON.toString();
	}

	@Transactional(readOnly = false)
	public void deletePermission(Long id) {
		permissionDao.delete(id);
	}

	@Transactional(readOnly = false)
	public void deletePermission(List<Long> ids) {
		List<Permission> temp = (List<Permission>) this.permissionDao
				.findAll(ids);
		if (temp != null && temp.size() > 0) {
			for (Permission obj : temp) {
				permissionDao.deleteRolePermissionByPvalue(obj.getValue());
				permissionDao.delete(obj.getId());
			}
		}
	}

	@Transactional(readOnly = false)
	public void savePermission(Permission permission) {
		permissionDao.save(permission);
	}

	public Permission getPermission(Long id) {
		return permissionDao.findOne(id);
	}

	/**
	 * 通过key获取授权Value
	 * 
	 * @param key
	 * @return
	 */
	public String getValueByKey(String key) {
		return this.permissionDao.findPermissionByCkey(key).getValue();
	}

	public String getValueById(String id) {
		return getPermission(Long.parseLong(id)).getValue();
	}

	public Permission findPermissionByCkey(String key) {
		return this.permissionDao.findPermissionByCkey(key);
	}

	public String getNameByValue(String value) {
		return this.permissionDao.findPermissionByValue(value).getName();
	}

	/**
	 * 通过pKey获取授权列表
	 * 
	 * @param pids
	 * @return
	 */
	public List<String> getPermissionList(String[] pids) {
		List<String> permissionList = Lists.newArrayList();
		if (pids != null && pids.length > 0) {
			for (String i : pids) {
				permissionList.add(getValueById(i));
			}
		}
		return permissionList;
	}

	/**
	 * 通过pKey获取授权列表
	 * 
	 * @param pids
	 * @return
	 */
	public List<Permission> getPermissionListByValue(List<String> values) {
		List<Permission> permissionList = Lists.newArrayList();
		if (values != null && values.size() > 0) {
			for (String i : values) {
				permissionList.add(this.permissionDao.findPermissionByValue(i));
			}
		}
		return permissionList;
	}

	public String getPids(List<String> values) {
		List<Permission> permissionList = getPermissionListByValue(values);
		List<String> ids = Lists.newArrayList();
		if (permissionList != null && permissionList.size() > 0) {
			for (Permission permission : permissionList) {
				if (permission != null)
					ids.add(permission.getId() + "");
			}
		}
		return StringUtils.join(ids, ",");
	}

	/**
	 * 获取所有授权名称列表
	 * 
	 * @param permissionList
	 * @return
	 */
	public List<String> getPermissionNameList(List<String> permissionList) {
		List<String> permissionNameList = Lists.newArrayList();
		if (permissionList != null && permissionList.size() > 0) {
			for (String i : permissionList) {
				permissionNameList.add(getNameByValue(i));
			}
		}
		return permissionNameList;
	}

	public List<Permission> getPermissionByPermissionType(String value) {
		List<Permission> permissionList = permissionDao
				.findPermissionByPermissionType(value);
		return permissionList;
	}

	public Page<Permission> getAllPermission(Map<String, Object> filterParams,
			int pageNumber, int pageSize, String sortType, String order) {
		PageRequest pageRequest = buildPageRequest(pageNumber, pageSize,
				sortType, order);
		Specification<Permission> spec = buildSpecification(filterParams);
		return permissionDao.findAll(spec, pageRequest);
	}

	/**
	 * 创建动态查询条件组合.
	 */
	private Specification<Permission> buildSpecification(
			Map<String, Object> filterParams) {
		Map<String, SearchFilter> filters = SearchFilter.parse(filterParams);
		Specification<Permission> spec = DynamicSpecifications.bySearchFilter(
				filters.values(), Permission.class);
		return spec;
	}

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

	
	
	public List<Tree> permissionTree(Map<String, Object> filterParams) {
    	List<Tree> treeList = Lists.newArrayList();
		Specification<Permission> spec = buildSpecification(filterParams); 
		List<Permission> pl = permissionDao.findAll(spec,new Sort(Direction.ASC, "sort"));
		if(pl!=null && pl.size()>0){			
			for(Permission p:pl){	
				Map<String, String> other = Maps.newHashMap();
				Tree tree = new Tree();
				tree.setId(p.getId() + "");
				tree.setPid(p.getPid() + "");
				if(p.getPid()>0){
					tree.set_parentId(p.getPid() + "");
				}	
				tree.setText(p.getName());
				tree.setIconCls("status_online");
				other.put("permissionType", p.getPermissionType());
				other.put("value", p.getValue());
				tree.setAttributes(other);
				treeList.add(tree);
			}			
		}
        return treeList;
    }
	
	

	public List<Tree> permissionTree() {
		List<Permission> l = (List<Permission>) permissionDao.findAll();
		List<Tree> lt = Lists.newArrayList();
		if (l != null && l.size() > 0) {
			for (Permission t : l) {
				Tree tree = new Tree();
				tree.setId(t.getId() + "");
				if (StringUtils.equals(t.getPermissionType(), "1")) {
					tree.setText(t.getName() + "[系统]");
				} else if (StringUtils.equals(t.getPermissionType(), "2")) {
					tree.setText(t.getName() + "[菜单]");
				} else if (StringUtils.equals(t.getPermissionType(), "3")) {
					tree.setText(t.getName() + "[功能]");
				} else {

					tree.setText(t.getName());
				}
				tree.setState("open");
				tree.setIconCls("status_online");
				tree.setPid(t.getPid() + "");
				lt.add(tree);
			}
		}
		return lt;
	}

	 /**
     * 根据条件查询数据记录
     * @param value
     * @return
     */
    public Long getCount(Map<String, Object> filterParams){
    	Specification<Permission> spec = buildSpecification(filterParams); 
    	return permissionDao.count(spec);
    }



}
