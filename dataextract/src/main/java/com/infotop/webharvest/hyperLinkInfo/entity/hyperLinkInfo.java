package com.infotop.webharvest.hyperLinkInfo.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.infotop.common.IdEntity;

@Entity
@Table(name = "hyper_link_info")
public class hyperLinkInfo extends IdEntity {
	
	private String linkName;
	private String linkUrl;
	private Long PageInfoId;
	
	
	public String getLinkName() {
		return linkName;
	}
	public void setLinkName(String linkName) {
		this.linkName = linkName;
	}
	public String getLinkUrl() {
		return linkUrl;
	}
	public void setLinkUrl(String linkUrl) {
		this.linkUrl = linkUrl;
	}
	public Long getPageInfoId() {
		return PageInfoId;
	}
	public void setPageInfoId(Long pageInfoId) {
		PageInfoId = pageInfoId;
	}
	
	
	

}
