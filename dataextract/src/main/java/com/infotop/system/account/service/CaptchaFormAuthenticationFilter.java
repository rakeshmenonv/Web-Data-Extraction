/**
 * @(#)CaptchaFormAuthenticationFilter.java	  2013-5-23 上午9:19:54
 * @since JDK1.6
 * 
 * 版权所有 © 1999-2011  临沂市拓普网络有限公司
 */
package com.infotop.system.account.service;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;

import com.infotop.system.account.service.ShiroDbRealm.ShiroUser;


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
public class CaptchaFormAuthenticationFilter extends FormAuthenticationFilter {

	public static final String DEFAULT_CAPTCHA_PARAM = "captcha";
	private String captchaParam = DEFAULT_CAPTCHA_PARAM;


	public String getCaptchaParam() {
		return captchaParam;
	}

	public void setCaptchaParam(String captchaParam) {
		this.captchaParam = captchaParam;
	}

	protected String getCaptcha(ServletRequest request) {
		return WebUtils.getCleanParam(request, getCaptchaParam());
	}


	protected CaptchaUsernamePasswordToken createToken(ServletRequest request,
			ServletResponse response) {
		String username = getUsername(request);
		String password = getPassword(request);
		String captcha = getCaptcha(request);
		boolean rememberMe = isRememberMe(request);
		String host = getHost(request);
		return new CaptchaUsernamePasswordToken(username,
				password.toCharArray(), rememberMe, host, captcha);
	}

	/**
	 * 验证码校验
	 * 
	 * @param request
	 * @param token
	 *            void
	 * @exception
	 */
	protected void doCaptchaValidate(HttpServletRequest request,
			CaptchaUsernamePasswordToken token) {
		String captcha = (String) request.getSession().getAttribute(
				com.google.code.kaptcha.Constants.KAPTCHA_SESSION_KEY);
		if (StringUtils.isNotBlank(captcha)
				&& !StringUtils.equalsIgnoreCase(captcha, token.getCaptcha())) {
			throw new IncoreectCaptchaException("验证码错误！");
		}
	}
	
	protected void doUserTypeValidate(Subject subject,
			CaptchaUsernamePasswordToken token) {
		ShiroUser su = (ShiroUser) subject.getPrincipal();
		if (su==null) {
			subject.logout();
			throw new UnknownAccountException("登陆失败，帐号或密码错误！");
		}
	}

	/**
	 * 认证
	 */
	protected boolean executeLogin(ServletRequest request,
			ServletResponse response) throws Exception {
		CaptchaUsernamePasswordToken token = createToken(request, response);
		try {
			doCaptchaValidate((HttpServletRequest) request, token);
			Subject subject = getSubject(request, response);
			subject.login(token);
			doUserTypeValidate(subject,token);
			return onLoginSuccess(token, subject, request, response);
		} catch (AuthenticationException ex) {
			return onLoginFailure(token, ex, request, response);
		}
	}
	
	/**
	 * 覆盖默认实现，用sendRedirect直接跳出框架，以免造成js框架重复加载js出错。
	 * 
	 * @param token
	 * @param subject
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 * @see org.apache.shiro.web.filter.authc.FormAuthenticationFilter#onLoginSuccess(org.apache.shiro.authc.AuthenticationToken,
	 *      org.apache.shiro.subject.Subject, javax.servlet.ServletRequest,
	 *      javax.servlet.ServletResponse)
	 */
	@Override
	protected boolean onLoginSuccess(AuthenticationToken token,
			Subject subject, ServletRequest request, ServletResponse response)
			throws Exception {
		// issueSuccessRedirect(request, response);
		// we handled the success redirect directly, prevent the chain from
		// continuing:
		HttpServletRequest httpServletRequest = (HttpServletRequest) request;
		HttpServletResponse httpServletResponse = (HttpServletResponse) response;

		httpServletResponse.sendRedirect(httpServletRequest.getContextPath()
				+ this.getSuccessUrl());

		return false;
	}

}
