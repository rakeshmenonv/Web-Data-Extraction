package com.infotop.webharvest.imageInfo.repository;

import com.infotop.webharvest.imageInfo.entity.imageInfo;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface imageInfoDao extends PagingAndSortingRepository<imageInfo, Long>, JpaSpecificationExecutor<imageInfo>  {

}