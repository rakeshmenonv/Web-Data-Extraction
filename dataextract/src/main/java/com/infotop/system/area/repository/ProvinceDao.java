package com.infotop.system.area.repository;


import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.infotop.system.area.entity.Province;
public interface ProvinceDao extends PagingAndSortingRepository<Province, Long>, JpaSpecificationExecutor<Province>  {
      Province findByProvinceId(String provinceId);
}