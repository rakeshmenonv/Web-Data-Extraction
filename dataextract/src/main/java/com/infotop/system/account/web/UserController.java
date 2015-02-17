package com.infotop.system.account.web;

import java.util.List;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.validation.Valid;

import net.infotop.web.easyui.DataGrid;
import net.infotop.web.easyui.Message;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springside.modules.web.MediaTypes;
import org.springside.modules.web.Servlets;

import ch.qos.logback.classic.Logger;

import com.google.common.collect.Maps;
import com.infotop.common.BasicController;
import com.infotop.system.account.entity.Role;
import com.infotop.system.account.entity.User;
import com.infotop.system.account.service.ShiroDbRealm.ShiroUser;

@Controller
@RequestMapping(value = "/account/user")
public class UserController extends BasicController {
	/**
	 * 账号状态
	 */
	private static Map<String, String> allStatus = Maps.newHashMap();

	static {
		allStatus.put("enabled", "有效");
		allStatus.put("disabled", "无效");
	}

	/**
	 * 跳转列表页面
	 * @return
	 */
	@RequestMapping(value = "")
	public String list() {
		return "account/user/userList";
	}

	/**
	 * 获取列表数据
	 * @param sortType
	 * @param order
	 * @param pageNumber
	 * @param rows
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "findList")
	@ResponseBody
	public DataGrid findList(
			@RequestParam(value = "sort", defaultValue = "auto") String sortType,
			@RequestParam(value = "order", defaultValue = "desc") String order,
			@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "rows", defaultValue = "10") int rows,
			Model model, ServletRequest request) {
		DataGrid dataGrid = new DataGrid();
		try {
			Map<String, Object> searchParams = Servlets
					.getParametersStartingWith(request, "search_");
			model.addAttribute("searchParams", Servlets
					.encodeParameterStringWithPrefix(searchParams, "search_"));
			dataGrid = accountService.dataGridUser(searchParams, pageNumber,
					rows, sortType, order);

		} catch (Exception ex) {
			logger.log(this.getClass(),Logger.ERROR_INT,ex.getMessage(),"",null);
		}
		return dataGrid;
	}

	/**
	 * 跳转授权表单
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "authorize/{id}", method = RequestMethod.GET)
	public String authorizeForm(@PathVariable("id") Long id, Model model) {
		User user = accountService.getUser(id);
		model.addAttribute("user", user);
		model.addAttribute("action", "authorize");
		return "account/user/userAuthorizeForm";
	}

	/**
	 * 保存授权数据
	 * @param user
	 * @return
	 */
	@RequestMapping(value = "authorize", method = RequestMethod.POST)
	@ResponseBody
	public Message authorize(@Valid @ModelAttribute("preloadUser") User user) {
		user.getRoleList().clear();
		String checkedRoleList = user.getRoleIds();
		if (StringUtils.isNotBlank(checkedRoleList)) {
			for (String id : checkedRoleList.split(",")) {
				if (StringUtils.isNotBlank(id)) {
					Role role = new Role(Long.parseLong(id));
					user.getRoleList().add(role);
				}
			}
		}
		accountService.updateUser(user);
		msg.setSuccess(true);
		msg.setMessage("更新用户" + user.getLoginName() + "成功");
		msg.setData(user);
		return msg;
	}

	/**
	 * 跳转更新表单
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "update/{id}", method = RequestMethod.GET)
	public String updateForm(@PathVariable("id") Long id, Model model) {
		User entity = accountService.getUser(id);
		if (entity.getUserType() > 0) {
			model.addAttribute("area", parameterService
					.getParameterByCategoryAndSubcategory("county",
							"county_second"));
			model.addAttribute("group", parameterService
					.getParameterByCategoryAndSubcategory("ent_group",
							"ent_group_list"));
		}
		model.addAttribute("user", entity);
		model.addAttribute("action", "update");
		return "account/user/userForm";
	}

	/**
	 * 更新表单数据
	 * @param user
	 * @return
	 */
	@RequestMapping(value = "update", method = RequestMethod.POST )
	@ResponseBody
	public Message update(@Valid @ModelAttribute("preloadUser") User user) {
		accountService.updateUser(user);
		updateCurrentUserName(user.getName());
		msg.setSuccess(true);
		msg.setMessage("更新用户" + user.getLoginName() + "成功");
		msg.setData(user);
		return msg;
	}

	/**
	 * 初始化密码
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "resetpassword")
	@ResponseBody
	public Message resetpassword(@RequestParam("id") Long id) {
		User user = accountService.getUser(id);
		user.setPlainPassword("123456");
		accountService.updateUser(user);
		msg.setSuccess(true);
		msg.setMessage("密码初始化成功");
		msg.setData(user);
		return msg;
	}

	/**
	 * 详细信息页面
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "view/{id}", method = RequestMethod.GET)
	public String view(@PathVariable("id") Long id, Model model) {
		User entity = accountService.getUser(id);
		model.addAttribute("user", entity);
		if (entity.getUserType() > 0) {
			model.addAttribute("area", parameterService
					.getParameterByCategoryAndSubcategory("county",
							"county_second"));
			model.addAttribute("group", parameterService
					.getParameterByCategoryAndSubcategory("ent_group",
							"ent_group_list"));
		}
		return "account/user/userView";
	}

	/**
	 * 批量删除
	 * @param ids
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "delete", method = RequestMethod.POST)
	@ResponseBody
	public Message delete(@RequestParam(value = "id") List<Long> ids,
			ServletRequest request) {
		try {
			accountService.deleteUser(ids);
			msg.setSuccess(true);
			msg.setMessage("信息删除成功");
			msg.setData(null);
		} catch (Exception ex) {
			ex.printStackTrace();
			msg.setSuccess(false);
			msg.setMessage(ex.getMessage());
			msg.setData(null);
		}
		return msg;
	}

	/**
	 * 检查登录名称是否已存在
	 * @param loginName
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "checkLoginName")
	@ResponseBody
	public String checkLoginName(@RequestParam("loginName") String loginName,
			@RequestParam("id") String id) {
		User user = accountService.findUserByLoginName(loginName);
		if (user == null) {
			return "true";
		} else if (user.id.toString().equalsIgnoreCase(id)) {
			return "true";
		} else {
			return "false";
		}
	}

	/**
	 * 使用@ModelAttribute, 实现Struts2
	 * Preparable二次部分绑定的效果,先根据form的id从数据库查出User对象,再把Form提交的内容绑定到该对象上。
	 * 因为仅update()方法的form中有id属性，因此本方法在该方法中执行.
	 */
	@ModelAttribute("preloadUser")
	public User getUser(@RequestParam(value = "id", required = false) Long id) {
		if (id != null) {
			return accountService.getUser(id);
		}
		return null;
	}

	/**
	 * 不自动绑定对象中的roleList属性，另行处理。
	 */
	@InitBinder
	protected void initBinder(WebDataBinder binder) {
		binder.setDisallowedFields("roleList");
	}

	/**
	 * 更新Shiro中当前用户的用户名.
	 */
	private void updateCurrentUserName(String userName) {
		ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		user.name = userName;
	}

}
