package com.infotop.system.account.repository;

import java.util.List;





import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.infotop.system.account.entity.Role;

public interface RoleDao extends PagingAndSortingRepository<Role, Long>, RoleDaoCustom, JpaSpecificationExecutor<Role> {
	Role findByName(String name);

	List<Role> findRoleByRoleType(String value);
	
}
