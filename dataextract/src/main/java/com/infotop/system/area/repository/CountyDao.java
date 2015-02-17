package com.infotop.system.area.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.infotop.system.area.entity.County;

public interface CountyDao extends PagingAndSortingRepository<County, Long>,
		JpaSpecificationExecutor<County> {

	@Query("from County c where c.city.cityId =?1")
	List<County> findByCityCityId(String cityId);

	County findCountyByCountyId(String countyId);

	List<County> findByCityId(Long cityId);

	County findByCountyId(String entCounty);

	County findCountyByCounty(String county);

}