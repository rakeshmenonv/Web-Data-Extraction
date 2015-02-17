package com.infotop.system.account.web;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.validation.Valid;

import net.infotop.web.easyui.DataGrid;
import net.infotop.web.easyui.Message;
import net.infotop.web.easyui.Tree;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springside.modules.web.MediaTypes;
import org.springside.modules.web.Servlets;

import ch.qos.logback.classic.Logger;

import com.infotop.common.BasicController;
import com.infotop.system.account.entity.Role;
import com.infotop.system.account.service.PermissionService;

/**
 * 管理员管理用户的Controller.
 * 
 * @author calvin
 */
@Controller
@RequestMapping(value = "/account/role")
public class RoleController extends BasicController {
	@Autowired
	private PermissionService permissionService;

	@RequestMapping(value = "")
	public String list() {
		return "account/role/roleList";
	}

	@RequestMapping(value = "findList", method = RequestMethod.POST)
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
			dataGrid = accountService.roleDataGrid(searchParams, pageNumber,
					rows, sortType, order);

		} catch (Exception ex) {
			logger.log(this.getClass(), Logger.ERROR_INT, ex.getMessage(),
					super.getLoginUser().getLoginName(), null);
		}
		return dataGrid;
	}

	@RequestMapping(value = "create", method = RequestMethod.GET)
	public String createForm(Model model) {
		Role entity = new Role();
		model.addAttribute("role", entity);
		model.addAttribute("action", "create");
		return "account/role/roleForm";
	}

	@RequestMapping(value = "create", method = RequestMethod.POST)
	@ResponseBody
	public Message create(@Valid Role newRole,
			RedirectAttributes redirectAttributes, ServletRequest request) {
		accountService.saveRole(newRole);
		msg.setSuccess(true);
		msg.setMessage("角色创建成功");
		msg.setData("");
		return msg;
	}

	@RequestMapping(value = "update/{id}")
	public String updateForm(@PathVariable("id") Long id, Model model) {
		Role role = accountService.getRole(id);
		model.addAttribute("role", role);
		model.addAttribute("action", "update");
		return "account/role/roleForm";
	}

	@RequestMapping(value = "update", method = RequestMethod.POST)
	@ResponseBody
	public Message update(@Valid @ModelAttribute("preloadRole") Role role,
			RedirectAttributes redirectAttributes, ServletRequest request) {
		accountService.saveRole(role);
		redirectAttributes.addFlashAttribute("message", "角色更新成功");
		msg.setSuccess(true);
		msg.setMessage("角色更新成功");
		msg.setData("");
		return msg;
	}

	@RequestMapping(value = "authorize/{id}", method = RequestMethod.GET)
	public String authorizeForm(@PathVariable("id") Long id, Model model) {
		Role role = accountService.getRole(id);
		model.addAttribute("role", role);

		model.addAttribute("pids",
				permissionService.getPids(role.getPermissionList()));
		model.addAttribute("action", "authorize");
		return "account/role/roleAuthorizeForm";
	}

	@RequestMapping(value = "authorize", method = RequestMethod.POST)
	@ResponseBody
	public Message authorize(@Valid @ModelAttribute("preloadRole") Role role,
			RedirectAttributes redirectAttributes, ServletRequest request) {
		role.getPermissionList().clear();
		String pidstemp = role.getPermissionIds();
		String[] pids = null;
		if (StringUtils.isNotBlank(pidstemp)) {
			pids = pidstemp.split(",");
			if (pids != null && pids.length > 0) {
				role.setPermissionList(permissionService
						.getPermissionList(pids));
			} else {
				role.getPermissionList().clear();
			}
		} else {
			role.getPermissionList().clear();
		}

		accountService.saveRole(role);
		msg.setSuccess(true);
		msg.setMessage("角色更新成功");
		msg.setData(role);
		return msg;
	}

	/**
	 * 删除
	 * 
	 * @param ids
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "delete", method = RequestMethod.POST)
	@ResponseBody
	public Message delete(@RequestParam("id") List<Long> id,
			ServletRequest request) {
		try {
			accountService.deleteRole(id);
			msg.setSuccess(true);
			msg.setMessage("删除成功");
			msg.setData("");
		} catch (Exception ex) {
			ex.printStackTrace();
			msg.setSuccess(false);
			msg.setMessage(ex.getMessage());
			msg.setData("");
		}
		return msg;
	}

	@RequestMapping(value = "view/{id}", method = RequestMethod.GET)
	public String viewForm(@PathVariable("id") Long id, Model model) {
		Role role = accountService.getRole(id);
		model.addAttribute("role", role);
		model.addAttribute(
				"jsonData",
				permissionService.getJosnData(role.getPermissionList(),
						role.getRoleType()));
		return "account/role/roleView";
	}

	/**
	 * 使用@ModelAttribute, 实现Struts2
	 * Preparable二次部分绑定的效果,先根据form的id从数据库查出User对象,再把Form提交的内容绑定到该对象上。
	 * 因为仅update()方法的form中有id属性，因此本方法在该方法中执行.
	 */
	@ModelAttribute("preloadRole")
	public Role getRole(@RequestParam(value = "id", required = false) Long id) {
		if (id != null) {
			return accountService.getRole(id);
		}
		return null;
	}

	@RequestMapping(value = "checkRoleName")
	@ResponseBody
	public String checkRoleName(@RequestParam("name") String name,
			@RequestParam(value = "id", defaultValue = "") String id,
			ServletRequest request) throws UnsupportedEncodingException {
		Role role = accountService.findRoleByName(name);
		if (role == null) {
			return "true";
		} else if (role.id.toString().equalsIgnoreCase(id)) {
			return "true";
		} else {
			return "false";
		}
	}

	@RequestMapping(value = "tree")
	@ResponseBody
	public List<Tree> tree(Model model, ServletRequest request) {
		return accountService.roleTree();
	}

}
