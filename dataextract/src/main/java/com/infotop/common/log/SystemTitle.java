package com.infotop.common.log;

/**
 * 系统标题
 */
public enum SystemTitle {
	INFO("系统信息");

	private String value;

	SystemTitle(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	@Override
	public String toString() {
		return this.value;
	}

}
