package com.infotop.system.area.service;

import java.util.List;
import java.util.Map;

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

import com.infotop.system.area.entity.County;
import com.infotop.system.area.repository.CountyDao;

/**
 * SpmissysareacountyManager $Id: SpmissysareacountyManager.java,v 0.0.1 $
 */
@Component
@Transactional(readOnly = true)
public class CountyService {

	@Autowired
	private CountyDao countyDao;

	/**
	 * 保存一个Spmissysareacounty，如果保存成功返回该对象的id，否则返回null
	 * 
	 * @param entity
	 * @return 保存成功的对象的Id
	 */
	@Transactional(readOnly = false)
	public void save(County entity) {
		countyDao.save(entity);
	}

	/**
	 * 根据一个ID得到Spmissysareacounty
	 * 
	 * @param id
	 * @return
	 */
	public County get(Long id) {
		return countyDao.findOne(id);
	}

	public County get(String countyId) {
		return countyDao.findCountyByCountyId(countyId);
	}

	/**
	 * 删除一个Spmissysareacounty
	 * 
	 * @param id
	 * @return
	 */
	@Transactional(readOnly = false)
	public void delete(Long id) {
		this.countyDao.delete(id);
	}

	/**
	 * 批量删除Spmissysareacounty
	 * 
	 * @param ids
	 * @return
	 */
	@Transactional(readOnly = false)
	public void delete(List<Long> ids) {
		List<County> test = (List<County>) this.countyDao.findAll(ids);
		this.countyDao.delete(test);
	}

	public Page<County> getAllSpmissysareacounty(
			Map<String, Object> filterParams, int pageNumber, int pageSize,
			String sortType) {
		PageRequest pageRequest = buildPageRequest(pageNumber, pageSize,
				sortType);
		Specification<County> spec = buildSpecification(filterParams);
		return countyDao.findAll(spec, pageRequest);
	}

	/**
	 * 创建分页请求.
	 */
	private PageRequest buildPageRequest(int pageNumber, int pagzSize,
			String sortType) {
		Sort sort = null;
		if ("auto".equals(sortType)) {
			sort = new Sort(Direction.DESC, "id");
		} else if ("name".equals(sortType)) {
			sort = new Sort(Direction.DESC, "name");
		}
		return new PageRequest(pageNumber - 1, pagzSize, sort);
	}

	/**
	 * 创建动态查询条件组合.
	 */
	private Specification<County> buildSpecification(
			Map<String, Object> filterParams) {
		Map<String, SearchFilter> filters = SearchFilter.parse(filterParams);
		Specification<County> spec = DynamicSpecifications.bySearchFilter(
				filters.values(), County.class);
		return spec;
	}

	public List<County> getAll() {
		List<County> searchdatalist = (List<County>) countyDao.findAll();
		return searchdatalist;
	}

	public List<County> getAllByCityId(String cityId) {
		return this.countyDao.findByCityCityId(cityId);
	}

	public List<County> getCountyListByCityId(String cityId) {
		// TODO Auto-generated method stub
		return this.countyDao.findByCityId(Long.parseLong(cityId));
	}

	public County getCountyByCountyId(String entCounty) {
		// TODO Auto-generated method stub
		return this.countyDao.findByCountyId(entCounty);
	}

	public County getCountyId(String county) {
		return countyDao.findCountyByCounty(county);
	}

}