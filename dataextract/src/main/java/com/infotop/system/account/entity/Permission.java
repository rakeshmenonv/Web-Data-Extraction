package com.infotop.system.account.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.infotop.common.IdEntity;

/**
 * Resource Base Access Control中的资源定义.
 * 
 * @author calvin
 */

@Entity
@Table(name = "sys_permission")
public class Permission extends IdEntity {

	private static final long serialVersionUID = 19809509035384524L;
	private int pid;
	private String ckey;
	private String pkey;
	private String name;
	private String value;
	private String permissionType;
	private int sort;
	

	public Permission() {
	}

	public Permission(long id) {
		this.id = id;
	}

	public String getPermissionType() {
		return permissionType;
	}

	public void setPermissionType(String permissionType) {
		this.permissionType = permissionType;
	}

	public String getCkey() {
		return ckey;
	}

	public void setCkey(String ckey) {
		this.ckey = ckey;
	}

	public String getPkey() {
		return pkey;
	}

	public void setPkey(String pkey) {
		this.pkey = pkey;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public int getPid() {
		return pid;
	}

	public void setPid(int pid) {
		this.pid = pid;
	}
	public int getSort() {
		return sort;
	}

	public void setSort(int sort) {
		this.sort = sort;
	}
	
}
