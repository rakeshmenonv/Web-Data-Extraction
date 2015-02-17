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

import com.infotop.system.area.entity.City;
import com.infotop.system.area.repository.CityDao;

/**
 * SpmissysareacityManager
 * $Id: SpmissysareacityManager.java,v 0.0.1   $
 */
@Component
@Transactional(readOnly = true)
public class CityService {
	
	
	@Autowired
	private CityDao cityDao;
	/**
	 * 保存一个Spmissysareacity，如果保存成功返回该对象的id，否则返回null
	 * @param entity
	 * @return 保存成功的对象的Id
	 */
	@Transactional(readOnly = false)
	public void save(City entity){
		cityDao.save(entity);
	}
	
	/**
	 * 根据一个ID得到Spmissysareacity
	 * 
	 * @param id
	 * @return
	 */
	public City get(Long id){
		return cityDao.findOne(id);
	}
	
	public City get(String cityId){
		return this.cityDao.findCityByCityId(cityId);
	}
	
	/**
	 * 删除一个Spmissysareacity
	 * @param id
	 * @return
	 */
	@Transactional(readOnly = false)
    public void delete(Long id) {
        this.cityDao.delete(id);
    }
	
	/**
	 * 批量删除Spmissysareacity
	 * @param ids
	 * @return
	 */
	@Transactional(readOnly = false)
	public void delete(List<Long> ids){
		List<City> test = (List<City>) this.cityDao.findAll(ids);
		this.cityDao.delete(test);
	}
	


    public Page<City> getAllSpmissysareacity(Map<String, Object> filterParams, int pageNumber, int pageSize,
            String sortType) {
        PageRequest pageRequest = buildPageRequest(pageNumber, pageSize, sortType);
        Specification<City> spec = buildSpecification(filterParams);
        return cityDao.findAll(spec, pageRequest);
    }

    /**
     * 创建分页请求.
     */
    private PageRequest buildPageRequest(int pageNumber, int pagzSize, String sortType) {
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
    private Specification<City> buildSpecification(Map<String, Object> filterParams) {
        Map<String, SearchFilter> filters = SearchFilter.parse(filterParams);
        Specification<City> spec = DynamicSpecifications.bySearchFilter(filters.values(), City.class);
        return spec;
    }
    public List<City> getAll() {
		List<City> searchdatalist = (List<City>) cityDao.findAll();
		return searchdatalist;
	}
    	
    public List<City> getCityListByProvinceId(String provinceId){
    	
    	return this.cityDao.getProvinceId(provinceId);
    }
    
    

	public City getCityByCityId(String entCity) {
		// TODO Auto-generated method stub
		return this.cityDao.findByCityId(entCity);
	}
		
		

}