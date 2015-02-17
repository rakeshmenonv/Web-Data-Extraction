package com.infotop.system.account.entity;

import java.util.List;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.google.common.collect.Lists;
import com.infotop.common.IdEntity;

/**
 * 角色.
 * 
 * @author calvin
 */
@Entity
@Table(name = "sys_role")
// 默认的缓存策略.
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Role extends IdEntity {
	private static final long serialVersionUID = 7368237298912360431L;
	private String name;
	private String roleType;
	private int pid;
	private List<String> permissionList = Lists.newArrayList();
	private List<String> permissionNameList = Lists.newArrayList();
	private String permissionIds;
	private String remark;
	
	public Role() {
	}

	public Role(Long id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	public String getRoleType() {
		return roleType;
	}

	public void setRoleType(String roleType) {
		this.roleType = roleType;
	}

	@ElementCollection
	@CollectionTable(name = " sys_role_permission", joinColumns = { @JoinColumn(name = "role_id") })
	@Column(name = "permission")
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	public List<String> getPermissionList() {
		return permissionList;
	}

	public void setPermissionList(List<String> permissionList) {
		this.permissionList = permissionList;
	}


	@Transient
	public List<String> getPermissionNameList() {
		return permissionNameList;
	}
	
	public void setPermissionNameList(List<String> permissionNameList) {  
		this.permissionNameList = permissionNameList;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

	public int getPid() {
		return pid;
	}

	public void setPid(int pid) {
		this.pid = pid;
	}
	

	@Transient
	public String getPermissionIds() {
		return permissionIds;
	}

	public void setPermissionIds(String permissionIds) {
		this.permissionIds = permissionIds;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	
}
