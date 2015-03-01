package com.infotop.webharvest.pagedatainfo.repository;

import java.util.List;

import com.infotop.webharvest.pagedatainfo.entity.Pagedatainfo;
import com.infotop.webharvest.pageurlinfo.entity.Pageurlinfo;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

public interface PagedatainfoDao extends PagingAndSortingRepository<Pagedatainfo, Long>, JpaSpecificationExecutor<Pagedatainfo>  {

	List<Pagedatainfo> getPagedatainfoBypageurlinfo(Pageurlinfo pageurlinfo);
	
	@Modifying
	@Query("delete from Pagedatainfo pagedatainfo where pagedatainfo.tableGroupKey = :tableGroupKey")
	void deleteByTableGroupKey(@Param("tableGroupKey") String tableGroupKey);
}