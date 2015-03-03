package com.infotop.webharvest.pageurlinfo.repository;

import java.util.List;

import com.infotop.webharvest.pageurlinfo.entity.Pageurlinfo;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface PageurlinfoDao extends PagingAndSortingRepository<Pageurlinfo, Long>, JpaSpecificationExecutor<Pageurlinfo>  {
	
	List<Pageurlinfo> getPageurlinfoByjobon(String jobon);
	
	List<Pageurlinfo> findByJobonNotNull();
	
	List<Pageurlinfo> findByUrl(String url);

}