package com.infotop.webharvest.pageInfo.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.infotop.common.IdEntity;

@Entity
@Table(name = "page_info")
public class pageInfo extends IdEntity {
	
	private String title;
	private String url;
	
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	

}
