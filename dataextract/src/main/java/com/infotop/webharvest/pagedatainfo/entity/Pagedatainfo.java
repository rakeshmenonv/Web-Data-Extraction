package com.infotop.webharvest.pagedatainfo.entity;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.infotop.common.IdEntity;
import com.infotop.webharvest.pageurlinfo.entity.Pageurlinfo;

@Entity
@Table(name = "page_data_info")
public class Pagedatainfo extends IdEntity{
	
	private Pageurlinfo pageurlinfo;
	private String tableGroupKey;
	private String rowGroupKey;
	private String content;
	private String type;
	private String extractedDate;
	
	
	@ManyToOne
	@JoinColumn(name="page_url_info_id",referencedColumnName="id")
	public Pageurlinfo getPageurlinfo() {
		return pageurlinfo;
	}
	public void setPageurlinfo(Pageurlinfo pageurlinfo) {
		this.pageurlinfo = pageurlinfo;
	}
	public String getTableGroupKey() {
		return tableGroupKey;
	}
	public void setTableGroupKey(String tableGroupKey) {
		this.tableGroupKey = tableGroupKey;
	}
	public String getRowGroupKey() {
		return rowGroupKey;
	}
	public void setRowGroupKey(String rowGroupKey) {
		this.rowGroupKey = rowGroupKey;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getExtractedDate() {
		return extractedDate;
	}
	public void setExtractedDate(String extractedDate) {
		this.extractedDate = extractedDate;
	}
	
}
