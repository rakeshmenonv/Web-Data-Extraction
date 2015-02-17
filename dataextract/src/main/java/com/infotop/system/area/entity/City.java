package com.infotop.system.area.entity;

import java.io.Serializable;

import java.util.List;

import javax.persistence.Entity;

import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import com.google.common.collect.Lists;

@Entity
@Table(name = "sys_area_city")
public class City implements Serializable {

	private static final long serialVersionUID = 1L;
	private Long id;
	private String cityId;
	private String city;
	private List<County> countylist = Lists.newArrayList();
	private Province province;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCityId() {
		return cityId;
	}

	public void setCityId(String cityId) {
		this.cityId = cityId;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "city")
	@Fetch(value = FetchMode.SUBSELECT)
	@Cascade(value = { CascadeType.ALL })
	public List<County> getCountylist() {
		return countylist;
	}

	public void setCountylist(List<County> countylist) {
		this.countylist = countylist;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "province_id", referencedColumnName = "provinceId")
	public Province getProvince() {
		return province;
	}

	public void setProvince(Province province) {
		this.province = province;
	}

}
