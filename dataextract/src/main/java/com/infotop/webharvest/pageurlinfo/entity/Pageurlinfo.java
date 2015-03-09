package com.infotop.webharvest.pageurlinfo.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.infotop.common.IdEntity;
import com.infotop.webharvest.pagedatainfo.entity.Pagedatainfo;

@Entity
@Table(name = "page_url_info")
public class Pageurlinfo extends IdEntity{
	
	private String url;
	private String element;
	private String attribute;
	private String value;
	private String extractedDate;
	private String jobon;
	private String nextScheduleOn;
	private String startTag;
	private String endTag;
	private String pageFormat;
	private int startPage;
	private int endPage;
	 private List<Pagedatainfo> pagedatainfos;
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getElement() {
		return element;
	}
	public void setElement(String element) {
		this.element = element;
	}
	public String getAttribute() {
		return attribute;
	}
	public void setAttribute(String attribute) {
		this.attribute = attribute;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getExtractedDate() {
		return extractedDate;
	}
	public void setExtractedDate(String extractedDate) {
		this.extractedDate = extractedDate;
	}
	public String getJobon() {
		return jobon;
	}
	public void setJobon(String jobon) {
		this.jobon = jobon;
	}
	public String getNextScheduleOn() {
		return nextScheduleOn;
	}
	public void setNextScheduleOn(String nextScheduleOn) {
		this.nextScheduleOn = nextScheduleOn;
	}
	public String getStartTag() {
		return startTag;
	}
	public void setStartTag(String startTag) {
		this.startTag = startTag;
	}
	public String getEndTag() {
		return endTag;
	}
	public void setEndTag(String endTag) {
		this.endTag = endTag;
	}
	public String getPageFormat() {
		return pageFormat;
	}
	public void setPageFormat(String pageFormat) {
		this.pageFormat = pageFormat;
	}
	public int getStartPage() {
		return startPage;
	}
	public void setStartPage(int startPage) {
		this.startPage = startPage;
	}
	public int getEndPage() {
		return endPage;
	}
	public void setEndPage(int endPage) {
		this.endPage = endPage;
	}
	@OneToMany(mappedBy="pageurlinfo",orphanRemoval=true, cascade={CascadeType.ALL})
	public List<Pagedatainfo> getPagedatainfos() {
		return pagedatainfos;
	}
	public void setPagedatainfos(List<Pagedatainfo> pagedatainfos) {
		this.pagedatainfos = pagedatainfos;
	}
	
	 

}
