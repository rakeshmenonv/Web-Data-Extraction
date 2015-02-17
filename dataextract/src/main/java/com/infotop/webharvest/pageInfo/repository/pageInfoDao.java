package com.infotop.webharvest.pageInfo.repository;

import com.infotop.webharvest.pageInfo.entity.pageInfo;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface pageInfoDao extends PagingAndSortingRepository<pageInfo, Long>, JpaSpecificationExecutor<pageInfo>  {

}