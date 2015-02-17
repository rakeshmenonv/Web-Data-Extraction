/**
 * @(#)Lllegal.java	  2013-5-24 上午9:14:30
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
public class LllegalLoginException extends AuthenticationException {

    /**
     * TODO（用一句话描述这个变量表示什么）
     */
    private static final long serialVersionUID = 1L;

    LllegalLoginException() {
        super();
        // TODO Auto-generated constructor stub
    }

    LllegalLoginException(String message, Throwable cause) {
        super(message, cause);
        // TODO Auto-generated constructor stub
    }

    LllegalLoginException(String message) {
        super(message);
        // TODO Auto-generated constructor stub
    }

    LllegalLoginException(Throwable cause) {
        super(cause);
        // TODO Auto-generated constructor stub
    }

}
