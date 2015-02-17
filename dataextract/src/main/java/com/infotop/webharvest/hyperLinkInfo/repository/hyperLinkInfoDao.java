package com.infotop.webharvest.hyperLinkInfo.repository;

import com.infotop.webharvest.hyperLinkInfo.entity.hyperLinkInfo;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface hyperLinkInfoDao extends PagingAndSortingRepository<hyperLinkInfo, Long>, JpaSpecificationExecutor<hyperLinkInfo>  {

}