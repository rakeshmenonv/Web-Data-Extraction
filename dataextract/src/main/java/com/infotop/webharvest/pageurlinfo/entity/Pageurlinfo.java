package com.infotop.webharvest.pageurlinfo.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.infotop.common.IdEntity;

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
	

}
