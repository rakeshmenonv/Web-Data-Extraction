/**
 * @(#)CaptchaUsernamePasswordToken.java	  2013-5-23 上午9:16:01
 * @since JDK1.6
 * 
 * 版权所有 © 1999-2011  临沂市拓普网络有限公司
 */
package com.infotop.system.account.service;

import org.apache.shiro.authc.UsernamePasswordToken;

/**
 * <p>
 * Title:TODO (描述这个类属于哪一系统哪一模块)
 * </p>
 * <p>
 * Description: TODO (描述该类实现了什么功能，尽可能详尽)
 * </p>
 * <p>
 * Copyright:版权所有 © 1999-2011
 * </p>
 * <p>
 * Company:临沂市拓普网络有限公司
 * </p>
 * 
 * @author TODO (你的姓名Administrator)
 * @version v1.0
 */
public class CaptchaUsernamePasswordToken extends UsernamePasswordToken {

	private static final long serialVersionUID = 1L;

	private String captcha;


	public CaptchaUsernamePasswordToken() {
		super();
	}

	public CaptchaUsernamePasswordToken(String username, char[] password,
			boolean rememberMe, String host, String captcha ) {
		super(username, password, rememberMe, host);
		this.captcha = captcha;
	}

	/**
	 * username（登录名），password（密码），captcha （验证码）
	 */
	public CaptchaUsernamePasswordToken(String username,char[] password, String captcha ){
		super(username, password);
		this.captcha = captcha;
	}
	
	public String getCaptcha() {
		return captcha;
	}

	public void setCaptcha(String captcha) {
		this.captcha = captcha;
	}


}
