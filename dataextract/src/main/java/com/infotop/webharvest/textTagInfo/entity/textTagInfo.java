package com.infotop.webharvest.textTagInfo.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.infotop.common.IdEntity;

@Entity
@Table(name = "text_tag_info")
public class textTagInfo extends IdEntity {
	
	private String tag;
	private String value;
	private Long PageInfoId;
	
	
	public String getTag() {
		return tag;
	}
	public void setTag(String tag) {
		this.tag = tag;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public Long getPageInfoId() {
		return PageInfoId;
	}
	public void setPageInfoId(Long pageInfoId) {
		PageInfoId = pageInfoId;
	}
	
	

}