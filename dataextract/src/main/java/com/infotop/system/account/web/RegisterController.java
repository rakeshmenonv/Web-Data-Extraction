package com.infotop.system.account.web;

import java.security.NoSuchAlgorithmException;
import java.util.Map;

import javax.validation.Valid;

import net.infotop.web.easyui.Message;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.common.collect.Maps;
import com.infotop.common.BasicController;
import com.infotop.system.account.entity.User;

@Controller
@RequestMapping(value = "/register")
public class RegisterController extends BasicController {
	private static final Logger logger = Logger
			.getLogger(RegisterController.class);
	private static Map<String, String> allStatus = Maps.newHashMap();

	static {
		allStatus.put("enabled", "有效");
		allStatus.put("disabled", "无效");
	}

	@RequestMapping(value = "/{userType}", method = RequestMethod.GET)
	public String registerForm(@PathVariable("userType") String userType,
			Model model) {
		try {
			User user = new User();
			if (StringUtils.isNotBlank(userType)) {
				int usertype = Integer.parseInt(userType);
				user.setUserType(usertype);
				if (usertype > 0) {
					model.addAttribute("area", parameterService
							.getParameterByCategoryAndSubcategory("county",
									"county_second"));
					model.addAttribute("group", parameterService
							.getParameterByCategoryAndSubcategory("ent_group",
									"ent_group_list"));
				}
			}
			model.addAttribute("user", user);
			logger.warn("operated by "
					+ SecurityUtils.getSubject().getPrincipal());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return "account/user/register";
	}

	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	public Message register(@Valid User user,
			RedirectAttributes redirectAttributes)
			throws NoSuchAlgorithmException {
		accountService.registerUser(user);

		redirectAttributes.addFlashAttribute("username", user.getLoginName());
		logger.warn("operated by " + SecurityUtils.getSubject().getPrincipal());
		msg.setSuccess(true);
		msg.setMessage("添加用户" + user.getLoginName() + "成功");
		msg.setData(user);
		return msg;
	}

	/**
	 * Ajax请求校验loginName是否唯一。
	 */
	@RequestMapping(value = "checkLoginName")
	@ResponseBody
	public String checkLoginName(@RequestParam("loginName") String loginName) {
		logger.warn("operated by " + SecurityUtils.getSubject().getPrincipal());
		if (accountService.findUserByLoginName(loginName) == null) {
			return "true";
		} else {
			return "false";
		}
	}

	/**
	 * 不自动绑定对象中的roleList属性，另行处理。
	 */
	@InitBinder
	protected void initBinder(WebDataBinder binder) {
		logger.warn("operated by " + SecurityUtils.getSubject().getPrincipal());
		binder.setDisallowedFields("roleList");
	}

}
