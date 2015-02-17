/**
 * @(#)IncoreectCaptchaException.java	  2013-5-23 上午9:32:16
 * @since JDK1.6
 * 
 * 版权所有 © 1999-2011  临沂市拓普网络有限公司
 */
package com.infotop.system.account.service;

import org.apache.shiro.authc.AuthenticationException;

/**
 *<p>Title:TODO (描述这个类属于哪一系统哪一模块)</p>
 *<p>Description: TODO (描述该类实现了什么功能，尽可能详尽)</p>
 *<p>Copyright:版权所有 © 1999-2011</p>
 *<p>Company:临沂市拓普网络有限公司</p>
 *@author TODO (你的姓名Administrator)
 *@version v1.0
 */
public class IncoreectCaptchaException extends AuthenticationException {

	private static final long serialVersionUID = 1L;

	public IncoreectCaptchaException() {
        super();
    }

    public IncoreectCaptchaException(String message, Throwable cause) {
        super(message, cause);
    }

    public IncoreectCaptchaException(String message) {
        super(message);
    }

    public IncoreectCaptchaException(Throwable cause) {
        super(cause);
    }

}
