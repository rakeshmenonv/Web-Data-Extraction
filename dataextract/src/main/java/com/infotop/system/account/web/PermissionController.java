package com.infotop.system.account.web;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.validation.Valid;

import net.infotop.util.OperationNoUtil;
import net.infotop.web.easyui.Message;
import net.infotop.web.easyui.Tree;

import org.apache.log4j.Logger;
import org.apache.shiro.SecurityUtils;
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

import com.infotop.common.BasicController;
import com.infotop.system.account.entity.Permission;
import com.infotop.system.account.service.PermissionService;

@Controller
@RequestMapping(value = "/account/permission")
public class PermissionController extends BasicController {
	private static final Logger logger = Logger
			.getLogger(PermissionController.class);

	@Autowired
	private PermissionService permissionService;

	@RequestMapping(value = "")
	public String list() {
		return "account/permission/permissionList";
	}

	@RequestMapping(value = "treeList", method = RequestMethod.POST)
	@ResponseBody
	public List<Tree> treeList(
			@RequestParam(value = "id", defaultValue = "0") String parentId,
			ServletRequest request) {
		Map<String, Object> filterParams = new HashMap<String, Object>();
		filterParams.put("EQ_pid", parentId);
		return permissionService.permissionTree(filterParams);
	}

	@RequestMapping(value = "create", method = RequestMethod.GET)
	public String createForm(Model model,
			@RequestParam(value = "pid", defaultValue = "0") int pid) {
		Permission entity = new Permission();
		entity.setPid(pid);
		Permission temp = permissionService.getPermission((long) pid);
		if (temp != null)
			model.addAttribute("pName", temp.getName());
		entity.setCkey(OperationNoUtil.getUUID());
		model.addAttribute("permission", entity);
		model.addAttribute("action", "create");
		return "account/permission/permissionForm";
	}

	@RequestMapping(value = "create", method = RequestMethod.POST)
	@ResponseBody
	public Message create(@Valid Permission permission,
			RedirectAttributes redirectAttributes, ServletRequest request) {
		if (permission.getPid() == 0) {
			permission.setPkey("0");
		} else {
			Permission temp = permissionService.getPermission((long) permission
					.getPid());
			permission.setPkey(temp.getCkey());
		}
		permissionService.savePermission(permission);
		logger.warn("operated by " + SecurityUtils.getSubject().getPrincipal());
		msg.setSuccess(true);
		msg.setMessage("权限增加成功");
		msg.setData("");
		return msg;
	}

	@RequestMapping(value = "update/{id}", method = RequestMethod.GET)
	public String updateForm(@PathVariable("id") Long id, Model model) {
		Permission permission = permissionService.getPermission(id);
		Permission temp = permissionService.getPermission((long) permission.getPid());
		if (temp != null)
			model.addAttribute("pName", temp.getName());
		model.addAttribute("permission", permission);
		model.addAttribute("action", "update");
		logger.warn("operated by " + SecurityUtils.getSubject().getPrincipal());
		return "account/permission/permissionForm";
	}

	@RequestMapping(value = "update", method = RequestMethod.POST)
	@ResponseBody
	public Message update(
			@Valid @ModelAttribute("preloadPermission") Permission permission,
			RedirectAttributes redirectAttributes, ServletRequest request) {
		permissionService.savePermission(permission);
		logger.warn("operated by " + SecurityUtils.getSubject().getPrincipal());
		msg.setSuccess(true);
		msg.setMessage("权限更新成功");
		msg.setData(permission);
		return msg;
	}

	/**
	 * 逻辑删除
	 * 
	 * @param ids
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "delete", method = RequestMethod.POST)
	@ResponseBody
	public Message delete(@RequestParam(value = "id") Long id,
			ServletRequest request) {

		try {
			Map<String, Object> filterParams = new HashMap<String, Object>();
			filterParams.put("EQ_pid", id + "");
			Long count = permissionService.getCount(filterParams);
			if (count > 0) {
				msg.setSuccess(false);
				msg.setMessage("信息删除失败，不能直接删除包含子节点的节点");
				msg.setData("");
			} else {
				Permission permission = permissionService.getPermission(id);
				int parentId = permission.getPid();
				permissionService.deletePermission(id);
				msg.setSuccess(true);
				msg.setMessage("信息删除成功");
				msg.setData(parentId);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			msg.setSuccess(false);
			msg.setMessage(ex.getMessage());
			msg.setData("");
		}
		return msg;
	}

	@ModelAttribute("preloadPermission")
	public Permission getPermission(
			@RequestParam(value = "id", required = false) Long id) {
		if (id != null) {
			return permissionService.getPermission(id);
		}
		logger.warn("operated by " + SecurityUtils.getSubject().getPrincipal());
		return null;
	}

	@RequestMapping(value = "checkCkey")
	@ResponseBody
	public String checkRoleName(@RequestParam("ckey") String ckey,
			@RequestParam("id") String id, ServletRequest request)
			throws UnsupportedEncodingException {
		if (id == null) {
			id = "";
		}
		logger.warn("operated by " + SecurityUtils.getSubject().getPrincipal());
		Permission permission = permissionService
				.findPermissionByCkey(new String(ckey.getBytes("ISO8859-1"),
						"UTF-8"));
		if (permission == null) {
			return "true";
		} else if (permission.id.toString().equalsIgnoreCase(id)) {
			return "true";
		} else {
			return "false";
		}
	}

	@RequestMapping(value = "tree")
	@ResponseBody
	public List<Tree> tree() {
		return permissionService.permissionTree();
	}
}
