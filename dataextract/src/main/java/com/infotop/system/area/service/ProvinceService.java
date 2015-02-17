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
import com.infotop.system.area.entity.County;
import com.infotop.system.area.entity.Province;
import com.infotop.system.area.repository.ProvinceDao;

/**
 * SpmissysareaprovinceManager
 * $Id: SpmissysareaprovinceManager.java,v 0.0.1   $
 */
@Component
@Transactional(readOnly = true)
public class ProvinceService {
	
	
	@Autowired
	private ProvinceDao provinceDao;
	/**
	 * 保存一个Spmissysareaprovince，如果保存成功返回该对象的id，否则返回null
	 * @param entity
	 * @return 保存成功的对象的Id
	 */
	@Transactional(readOnly = false)
	public void save(Province entity){
		provinceDao.save(entity);
	}
	
	/**
	 * 根据一个ID得到Spmissysareaprovince
	 * 
	 * @param id
	 * @return
	 */
	public Province get(Long id){
		return provinceDao.findOne(id);
	}
	
	/**
	 * 删除一个Spmissysareaprovince
	 * @param id
	 * @return
	 */
	@Transactional(readOnly = false)
    public void delete(Long id) {
        this.provinceDao.delete(id);
    }
	
	/**
	 * 批量删除Spmissysareaprovince
	 * @param ids
	 * @return
	 */
	@Transactional(readOnly = false)
	public void delete(List<Long> ids){
		List<Province> test = (List<Province>) this.provinceDao.findAll(ids);
		this.provinceDao.delete(test);
	}
	


    public Page<Province> getAllSpmissysareaprovince(Map<String, Object> filterParams, int pageNumber, int pageSize,
            String sortType) {
        PageRequest pageRequest = buildPageRequest(pageNumber, pageSize, sortType);
        Specification<Province> spec = buildSpecification(filterParams);
        return provinceDao.findAll(spec, pageRequest);
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
    
    public String makeJsonData(){
        StringBuffer sb = new StringBuffer();
        List<Province> test = (List<Province>) this.provinceDao.findAll();
        if(test!=null&&test.size()>0){
        	sb.append("[");
            for (Province spmissysareaprovince : test) {
                sb.append("{id:'p"+spmissysareaprovince.getId()+"',pId:-1,name: '"+spmissysareaprovince.getProvince()+"'}, ");
                List<City> spmissysareacityList=spmissysareaprovince.getCitylist();
                for (City spmissysareacity :spmissysareacityList ) {
                sb.append("{id:'c"+spmissysareacity.getId()+"',pId:'p"+spmissysareacity.getProvince().getId()+"',name: '"+spmissysareacity.getCity()+"'},");
           
                
                List<County> spmissysareacountyList=spmissysareacity.getCountylist();
                for (County spmissysareacounty :spmissysareacountyList ) {
                sb.append("{id:'co"+spmissysareacounty.getId()+"',pId:'c"+spmissysareacounty.getCity().getId()+"',name: '"+spmissysareacounty.getCounty()+"'},");
            }
                }
                
            
            }
        }
        sb.append("]");
        return sb.toString();
    }

    /**
     * 创建动态查询条件组合.
     */
    private Specification<Province> buildSpecification(Map<String, Object> filterParams) {
        Map<String, SearchFilter> filters = SearchFilter.parse(filterParams);
        Specification<Province> spec = DynamicSpecifications.bySearchFilter(filters.values(), Province.class);
        return spec;
    }
    public List<Province> getAll() {
		List<Province> searchdatalist = (List<Province>) provinceDao.findAll();
		return searchdatalist;
	}
    	
	
    public Province getProvinceByProvinceId(String provinceId){
    	return provinceDao.findByProvinceId(provinceId);
    }		
			
		

}