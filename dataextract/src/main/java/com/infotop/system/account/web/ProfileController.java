package com.infotop.system.account.web;

import javax.validation.Valid;

import net.infotop.web.easyui.Message;

import org.apache.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.infotop.common.BasicController;
import com.infotop.system.account.entity.User;
import com.infotop.system.account.service.ShiroDbRealm.ShiroUser;

@Controller
@RequestMapping(value = "/profile")
public class ProfileController extends BasicController {
	private static final Logger logger = Logger
			.getLogger(ProfileController.class);

	@RequestMapping(method = RequestMethod.GET)
	public String updateForm(Model model) {
		Long id = getCurrentUserId();
		model.addAttribute("user", accountService.getUser(id));
		logger.warn("operated by " + SecurityUtils.getSubject().getPrincipal());
		return "account/profile";
	}

	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	public Message update(@Valid @ModelAttribute("preloadUser") User user) {
		accountService.updateUser(user);
		updateCurrentUserName(user.getName());
		msg.setSuccess(true);
		msg.setMessage("密码修改成功！");
		msg.setData(user);
		return msg;
	}

	@ModelAttribute("preloadUser")
	public User getUser(@RequestParam(value = "id", required = false) Long id) {
		logger.warn("operated by " + SecurityUtils.getSubject().getPrincipal());
		if (id != null) {
			return accountService.getUser(id);
		}

		return null;
	}

	/**
	 * 取出Shiro中的当前用户Id.
	 */
	private Long getCurrentUserId() {
		logger.warn("operated by " + SecurityUtils.getSubject().getPrincipal());
		ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		return user.id;
	}

	/**
	 * 更新Shiro中当前用户的用户名.
	 */
	private void updateCurrentUserName(String userName) {
		logger.warn("operated by " + SecurityUtils.getSubject().getPrincipal());
		ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		user.name = userName;
	}
}
