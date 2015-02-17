package com.infotop.system.area.service;

import java.util.List;
import java.util.Map;

import net.infotop.web.easyui.DataGrid;
import net.infotop.web.easyui.Tree;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.infotop.system.area.entity.City;
import com.infotop.system.area.entity.County;
import com.infotop.system.area.repository.CityDao;
import com.infotop.system.area.repository.CountyDao;
import com.infotop.system.area.repository.ProvinceDao;

@Component
@Transactional(readOnly = true)
public class AreaService {

	@Autowired
	private CityDao cityDao;
	@Autowired
	private CountyDao countyDao;
	@Autowired
	private ProvinceDao provinceDao;
	public DataGrid dataGrid(String root) {
		List<Tree> lt = Lists.newArrayList();
		City tempCity = getCity(root);
		if(tempCity!=null){
			Tree treeCity = new Tree();
			Map<String, String> otherCity = Maps.newHashMap();
			treeCity.setId(tempCity.getId() + "");
			treeCity.setText(tempCity.getCity());
			treeCity.setState("open");
			otherCity.put("code", tempCity.getCityId());
			treeCity.setAttributes(otherCity);
			lt.add(treeCity);
			List<County> tempCountyList = tempCity.getCountylist();
			if(tempCountyList!=null&&tempCountyList.size()>0){
				for (County county : tempCountyList) {
					if(county!=null){
						Tree treeCounty = new Tree();
						Map<String, String> otherCounty = Maps.newHashMap();
						treeCounty.setId(county.getId() + "");
						treeCounty.setText(county.getCounty());
						treeCounty.setState("open");
						treeCounty.set_parentId(tempCity.getId()+"");//所属市ID
						otherCounty.put("code", county.getCountyId());
						treeCounty.setAttributes(otherCounty);
						lt.add(treeCounty);
					}
				}
			}
			
		}
		DataGrid dataGrid = new DataGrid();
		dataGrid.setTotal(0l);
		dataGrid.setRows(lt);
		return dataGrid;
	}
	
	
	
	public City getCity(String cityId){
		return this.cityDao.findCityByCityId(cityId);
	}



	public List<County> getAllCountyByCityId(String cityId) {
    	return this.countyDao.findByCityCityId(cityId);
	}



	public County getCounty(String countyId) {
		return countyDao.findCountyByCountyId(countyId);
	}


}
