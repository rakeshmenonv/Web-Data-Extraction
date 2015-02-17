/**
 * @(#)ParameterDao.java 
 * @since JDK1.6
 * 
 * 版权所有 © 1999-2014  临沂市拓普网络有限公司
 */
package com.infotop.system.parameter.repository;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.infotop.system.parameter.entity.Parameter;

public interface ParameterDao extends
		PagingAndSortingRepository<Parameter, Long>,
		JpaSpecificationExecutor<Parameter> {

	Parameter findByUuid(String uuid);

	@Query("select name from Parameter where uuid=?1")
	String findNameByUuid(String uuid);

	List<Parameter> findByCategory(String category, Sort sort);

	List<Parameter> findByParentId(String pid, Sort sort);

	List<Parameter> findByCategoryAndSubcategory(String category,
			String subcategory, Sort sort);

	List<Parameter> findByCategoryAndParentId(String category, Long parentid,
			Sort sort);

	public Parameter findParameterByValue(String value);

	public Parameter findParameterByCategoryAndId(String category, Long id);

	public Parameter findParameterByCategoryAndSubcategoryAndId(
			String category, String subcategory, Long id);

	@Modifying
	@Query("delete from Parameter a where a.id = ?1")
	public int remove(Long id);

	Parameter findByValueAndCategory(String reportUnit, String category);

	/* List<Parameter> findBySubCategory(String subcategory, Sort sort); */

}
