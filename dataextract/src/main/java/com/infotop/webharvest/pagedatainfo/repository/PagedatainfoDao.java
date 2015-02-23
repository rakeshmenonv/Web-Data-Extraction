package com.infotop.webharvest.pagedatainfo.repository;

import java.util.List;

import com.infotop.webharvest.pagedatainfo.entity.Pagedatainfo;
import com.infotop.webharvest.pageurlinfo.entity.Pageurlinfo;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface PagedatainfoDao extends PagingAndSortingRepository<Pagedatainfo, Long>, JpaSpecificationExecutor<Pagedatainfo>  {

	List<Pagedatainfo> getPagedatainfoBypageurlinfo(Pageurlinfo pageurlinfo);
}