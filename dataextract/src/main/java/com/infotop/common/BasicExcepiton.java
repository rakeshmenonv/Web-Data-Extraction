package com.infotop.common;

public class BasicExcepiton extends Exception{

	private static final long serialVersionUID = 1L;

	public BasicExcepiton() {
		super();
	}

	public BasicExcepiton(String message, Throwable cause) {
		super(message, cause);
	}

	public BasicExcepiton(String message) {
		super(message);
	}

	public BasicExcepiton(Throwable cause) {
		super(cause);
	}

}
