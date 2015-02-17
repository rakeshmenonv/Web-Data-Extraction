package com.infotop.system.account.web;

import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.infotop.system.account.service.CaptchaFormAuthenticationFilter;

@Controller
@RequestMapping(value = "/login")
public class LoginController {

	private static Logger logger = LoggerFactory
			.getLogger(LoginController.class);
	@RequestMapping(method = RequestMethod.GET)
	public String login() {
		if (SecurityUtils.getSubject().getSession() != null) {
			SecurityUtils.getSubject().logout();
		}
		logger.info("login page ");
		return "login";
	}


	@RequestMapping(method = RequestMethod.POST)
	public String fail(
			@RequestParam(CaptchaFormAuthenticationFilter.DEFAULT_USERNAME_PARAM) String userName,
			Model model) {
		model.addAttribute(
				CaptchaFormAuthenticationFilter.DEFAULT_USERNAME_PARAM,
				userName);
		logger.info("login fail");
		return "login";
	}

}
