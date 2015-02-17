package com.infotop.system.area.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.Proxy;

import com.google.common.collect.Lists;

@Entity
@Table(name = "sys_area_province")
@Proxy(lazy = false)
public class Province implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long id;

	private String provinceId;
	private String province;
	private String provinceForShort;
	private String provinceForBusNo;
	private List<City> citylist = Lists.newArrayList();

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	// Variable with getter and setter
	public String getProvinceId() {
		return provinceId;
	}

	public void setProvinceId(String provinceId) {
		this.provinceId = provinceId;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getProvinceForShort() {
		return provinceForShort;
	}

	public void setProvinceForShort(String provinceForShort) {
		this.provinceForShort = provinceForShort;
	}

	public String getProvinceForBusNo() {
		return provinceForBusNo;
	}

	public void setProvinceForBusNo(String provinceForBusNo) {
		this.provinceForBusNo = provinceForBusNo;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "province")
	@Fetch(value = FetchMode.SUBSELECT)
	@Cascade(value = { CascadeType.ALL })
	public List<City> getCitylist() {
		return citylist;
	}

	public void setCitylist(List<City> citylist) {
		this.citylist = citylist;
	}
}
