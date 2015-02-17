package com.infotop.system.account.repository;

import javax.annotation.Resource;

import org.springframework.jdbc.core.JdbcTemplate;

public class PermissionDaoImpl implements PermissionCustom{


    @Resource
    private JdbcTemplate jdbcTemplate;
	 
    public void deleteRolePermissionByPvalue(String value){
    	String sql = "delete from sys_role_permission where permission = '"+value+"'";
    	jdbcTemplate.execute(sql);
    }

}
