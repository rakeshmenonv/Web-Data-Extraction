package com.infotop.webharvest.textTagInfo.repository;

import com.infotop.webharvest.textTagInfo.entity.textTagInfo;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface textTagInfoDao extends PagingAndSortingRepository<textTagInfo, Long>, JpaSpecificationExecutor<textTagInfo>  {

}