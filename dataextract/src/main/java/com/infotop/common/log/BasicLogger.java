package com.infotop.common.log;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springside.modules.mapper.JsonMapper;

@Component
public class BasicLogger {

	private Logger basicLogger;
	private JsonMapper jsonMapper = new JsonMapper();

	/**
	 * 输出日志
	 * @param cls 所在类
	 * @param logLevel 日志级别
	 * @param action 操作类型
	 * @param user 操作人
	 * @param data 相关信息
	 */
	public void log(Class cls, int logLevel, String action, String user,
			Map data) {
		basicLogger = LoggerFactory.getLogger(cls);
		String json = (data != null ? jsonMapper.toJson(data) : "{}");
		switch (logLevel) {
		case 00:
			basicLogger.trace("{},{},{},{}", cls.toString(), action, user, json);
			break;
		case 10:
			basicLogger.debug("{},{},{},{}", cls.toString(), action, user, json);
			break;
		case 30:
			basicLogger.warn("{},{},{},{}", cls.toString(), action, user, json);
			break;
		case 40:
			basicLogger.error("{},{},{},{}", cls.toString(), action, user, json);
			break;
		default:
			basicLogger.info("{},{},{},{}", cls.toString(), action, user, json);
			break;
		}
	}
}
