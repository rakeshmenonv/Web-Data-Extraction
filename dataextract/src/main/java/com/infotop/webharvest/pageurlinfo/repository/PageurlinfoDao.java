package com.infotop.webharvest.pageurlinfo.repository;

import com.infotop.webharvest.pageurlinfo.entity.Pageurlinfo;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface PageurlinfoDao extends PagingAndSortingRepository<Pageurlinfo, Long>, JpaSpecificationExecutor<Pageurlinfo>  {

}