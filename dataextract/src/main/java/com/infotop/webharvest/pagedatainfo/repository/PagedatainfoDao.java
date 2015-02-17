package com.infotop.webharvest.pagedatainfo.repository;

import com.infotop.webharvest.pagedatainfo.entity.Pagedatainfo;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface PagedatainfoDao extends PagingAndSortingRepository<Pagedatainfo, Long>, JpaSpecificationExecutor<Pagedatainfo>  {

}