package com.infotop.system.account.repository;

import java.util.List;




import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.infotop.system.account.entity.Permission;

public interface PermissionDao  extends PagingAndSortingRepository<Permission, Long>,PermissionCustom, JpaSpecificationExecutor<Permission> {

	Permission findPermissionByCkey(String key);
	Permission findPermissionByValue(String value);
	List<Permission> findPermissionByPermissionType(String value);
	List<Permission> findByPid(int pid);
}
