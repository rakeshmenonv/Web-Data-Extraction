/**
 * @(#)MakeCode.java	  2011-10-22 下午4:20:35
 * @since JDK1.6
 * 
 * 版权所有 © 1999-2011  临沂市拓普网络有限公司
 */
package com.infotop.webharvest;

import net.infotop.generate.Generate;

/**
 * <p>
 * Title:构建代码
 * </p>
 * <p>
 * Description:构建java和jsp
 * </p>
 * <p>
 * Copyright:版权所有 © 1999-2011
 * </p>
 * <p>
 * Company:临沂市拓普网络有限公司
 * </p>
 * 
 * @author lxr
 * @version v1.0
 */
public class MakeCode {

	public static void main(String[] args) {
		//需要自动构建代码的实体路径，可多个
		String str[] = { "com.infotop.webharvest.textTagInfo.entity.textTagInfo" };
		Generate obj = Generate.getInstance();
		obj.javaPath = "src/main/java";//java源码目标路径
		obj.webAppPath = "src/main/webapp";//jsp目标路径
		obj.propertiesPath = "src/main/resources";//配置文件目标路径

		obj.start(str);
	}

}
