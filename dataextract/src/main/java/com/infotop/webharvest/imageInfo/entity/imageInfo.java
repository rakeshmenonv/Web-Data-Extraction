package com.infotop.webharvest.imageInfo.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.infotop.common.IdEntity;

@Entity
@Table(name = "image_info")
public class imageInfo extends IdEntity  {
	
	private String SourceImageUrl;
	private String localImageUrl;
	private Long PageInfoId;
	
	
	public String getSourceImageUrl() {
		return SourceImageUrl;
	}
	public void setSourceImageUrl(String sourceImageUrl) {
		SourceImageUrl = sourceImageUrl;
	}
	public String getLocalImageUrl() {
		return localImageUrl;
	}
	public void setLocalImageUrl(String localImageUrl) {
		this.localImageUrl = localImageUrl;
	}
	public Long getPageInfoId() {
		return PageInfoId;
	}
	public void setPageInfoId(Long pageInfoId) {
		PageInfoId = pageInfoId;
	}
	
	
	

}
