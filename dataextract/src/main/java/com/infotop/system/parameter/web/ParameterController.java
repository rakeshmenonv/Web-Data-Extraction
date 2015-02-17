package com.infotop.system.parameter.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.validation.Valid;

import net.infotop.util.OperationNoUtil;
import net.infotop.web.easyui.DataGrid;
import net.infotop.web.easyui.Message;
import net.infotop.web.easyui.Tree;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springside.modules.mapper.JsonMapper;
import org.springside.modules.web.Servlets;

import ch.qos.logback.classic.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.infotop.common.BasicController;
import com.infotop.system.account.entity.User;
import com.infotop.system.account.service.ShiroDbRealm.ShiroUser;
import com.infotop.system.parameter.entity.Parameter;

@Controller
@RequestMapping(value = "/parameter")
public class ParameterController extends BasicController {

	@RequestMapping(value = "")
	public String list() {
		return "parameter/parameterList";
	}

	@RequestMapping(value = "findList", method = RequestMethod.POST)
	@ResponseBody
	public DataGrid findList(
			@RequestParam(value = "sort", defaultValue = "auto") String sortType,
			@RequestParam(value = "order", defaultValue = "desc") String order,
			@RequestParam(value = "id", defaultValue = "0") String parentId,
			@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "rows", defaultValue = "2000") int rows,
			Model model, ServletRequest request) {
		DataGrid dataGrid = new DataGrid();
		try {
			Map<String, Object> searchParams = Servlets
					.getParametersStartingWith(request, "search_");
			model.addAttribute("searchParams", Servlets
					.encodeParameterStringWithPrefix(searchParams, "search_"));
			dataGrid = parameterService.dataGrid(searchParams, pageNumber,
					rows, sortType, order);

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return dataGrid;
	}

	@RequestMapping(value = "findList/{category}/{second}", method = RequestMethod.POST)
	@ResponseBody
	public String findList(@PathVariable("category") String category,
			@PathVariable("second") String second, Model model,
			ServletRequest request) {
		String jsonString = "";
		try {
			ObjectMapper mapper = new ObjectMapper();
			jsonString = mapper.writeValueAsString(parameterService
					.getParameterByCategoryAndSubcategory(category, second));

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return jsonString;
	}

	@RequestMapping(value = "findGroupList/{category1}/{subcategory1}/{category2}/{subcategory2}", method = RequestMethod.POST)
	@ResponseBody
	public String findGroupList(@PathVariable("category1") String category1,
			@PathVariable("category2") String category2,
			@PathVariable("subcategory1") String subcategory1,
			@PathVariable("subcategory2") String subcategory2, Model model,
			ServletRequest request) {
		String jsonString = "";
		try {
			ObjectMapper mapper = new ObjectMapper();
			List<Parameter> categoryList1 = parameterService
					.getParameterByCategoryAndSubcategory(category1,
							subcategory1);
			List<Parameter> categoryList2 = parameterService
					.getParameterByCategoryAndSubcategory(category2,
							subcategory2);
			List<Parameter> joinedList = new ArrayList<Parameter>();
			Parameter p = new Parameter();
			p.setName("请选择");
			p.setValue("");
			joinedList.add(p);
			joinedList.addAll(categoryList1);
			joinedList.addAll(categoryList2);
			jsonString = mapper.writeValueAsString(joinedList);
			jsonString = jsonString.replaceAll("county", "县区");
			jsonString = jsonString.replaceAll("manage_department", "市直部门");
			System.out.println(jsonString);

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return jsonString;
	}

	@RequestMapping(value = "treeList", method = RequestMethod.POST)
	@ResponseBody
	public List<Tree> treeList(
			@RequestParam(value = "id", defaultValue = "0") String parentId,
			ServletRequest request) {
		Map<String, Object> filterParams = new HashMap<String, Object>();
		filterParams.put("EQ_parentId", parentId);
		return parameterService.parameterTree(filterParams);
	}

	/**
	 * 根据参数类别查询参数数据
	 * 
	 * @param category
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "findListByCategory/{category}")
	@ResponseBody
	public List<Tree> findListByCategory(
			@PathVariable("category") String category, ServletRequest request) {
		return parameterService.categoryParameter(category);
	}

	/**
	 * 根据参数类别查询参数数据
	 * 
	 * @param category
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "findListByCategorysecond/{second}")
	@ResponseBody
	public List<Tree> findListByCategorysecond(
			@PathVariable("second") String second, ServletRequest request) {
		return parameterService.categoryParametersecond(second);
	}

	/**
	 * 创建参数表单
	 * 
	 * @param model
	 * @param pid
	 * @return
	 */
	@RequestMapping(value = "add", method = RequestMethod.GET)
	public String createForm(Model model,
			@RequestParam(value = "pid", defaultValue = "0") Long pid) {
		Parameter parameter = new Parameter();
		parameter.setParentId(pid);
		if (0 != pid) {
			Parameter parent = parameterService.get(pid);
			parameter.setCategory(parent.getCategory());
		}
		model.addAttribute("parameter", parameter);
		model.addAttribute("action", "add");
		return "parameter/parameterForm";
	}

	/**
	 * 保存参数
	 * 
	 * @param parameter
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value = "add", method = RequestMethod.POST)
	@ResponseBody
	public Message create(@Valid Parameter parameter,
			RedirectAttributes redirectAttributes) {
		parameter.setUuid(OperationNoUtil.getUUID());
		parameterService.save(parameter);
		msg.setSuccess(true);
		msg.setMessage("参数添加成功");
		msg.setData(parameter);
		return msg;
	}

	/**
	 * 创建更新表单
	 * 
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "update/{id}", method = RequestMethod.GET)
	public String updateForm(@PathVariable("id") Long id, Model model) {
		model.addAttribute("parameter", parameterService.get(id));
		model.addAttribute("action", "update");
		return "parameter/parameterForm";
	}

	/**
	 * 更新参数
	 * 
	 * @param parameter
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value = "update", method = RequestMethod.POST)
	@ResponseBody
	public Message update(
			@Valid @ModelAttribute("preloadParameter") Parameter parameter) {
		parameterService.save(parameter);
		msg.setSuccess(true);
		msg.setMessage("参数更新成功");
		msg.setData(parameter);
		return msg;
	}

	/**
	 * 使用@ModelAttribute, 实现Struts2
	 * Preparable二次部分绑定的效果,先根据form的id从数据库查出User对象,再把Form提交的内容绑定到该对象上。
	 * 因为仅update()方法的form中有id属性，因此本方法在该方法中执行.
	 */
	@ModelAttribute("preloadParameter")
	public Parameter getParameter(
			@RequestParam(value = "id", required = false) Long id) {
		if (id != null) {
			return parameterService.get(id);
		}
		return null;
	}

	@RequestMapping(value = "parameterData/{id}")
	@ResponseBody
	public String parameterData(@PathVariable("id") Long id) {
		JsonMapper mapper = JsonMapper.nonDefaultMapper();
		Parameter pt = parameterService.get(id);
		return mapper.toJson(pt);
	}

	@RequestMapping(value = "getParameterByPID/{pid}", method = RequestMethod.GET)
	@ResponseBody
	public List<Parameter> getParameterByPID(@PathVariable("pid") String pid) {
		return parameterService.getParameterByPID(pid);
	}

	@RequestMapping(value = "parameterJson/{uuid}", method = RequestMethod.GET)
	@ResponseBody
	public String getParameterJson(@PathVariable("uuid") String uuid) {
		Parameter parameter = this.parameterService.getParameterByUuid(uuid);
		List<Parameter> parameterList = null;
		if (parameter != null) {
			parameterList = parameterService.getParameterByCategoryAndParentId(
					"industry_category", parameter.getId());
		}
		String beanListString = json.toJson(parameterList);
		return beanListString;
	}

	@RequestMapping(value = "getparameterJson/{id}", method = RequestMethod.GET)
	@ResponseBody
	public String getParameterJson(@PathVariable("id") Long id) {
		Parameter parameter = this.parameterService.get(id);
		List<Parameter> parameterList = null;
		if (parameter != null) {
			parameterList = parameterService.getParameterByCategoryAndParentId(
					"industry_category", parameter.getId());
		}
		String beanListString = json.toJson(parameterList);
		return beanListString;
	}

	@RequestMapping(value = "uuid")
	@ResponseBody
	public String uuidData() {
		return OperationNoUtil.getUUID();
	}

	/**
	 * 删除操作
	 * 
	 * @param ids
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "delete", method = RequestMethod.POST)
	@ResponseBody
	public Message delete(@RequestParam(value = "id") Long id,
			ServletRequest request) throws Exception {
		try {
			Map<String, Object> filterParams = new HashMap<String, Object>();
			filterParams.put("EQ_parentId", id + "");
			Long count = parameterService.getCount(filterParams);
			if (count > 0) {
				msg.setSuccess(false);
				msg.setMessage("信息删除失败，不能直接删除包含子节点的节点");
				msg.setData("");
			} else {
				Parameter parameter = parameterService.get(id);
				Long parentId = parameter.getParentId();
				parameterService.delete(id);
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

	/**
	 * 删除操作
	 * 
	 * @param ids
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "deleteBatch", method = RequestMethod.POST)
	@ResponseBody
	public Message deleteBatch(@RequestParam(value = "ids") List<Long> ids,
			ServletRequest request) throws Exception {
		try {
			System.out.println("entered");
			parameterService.delete(ids);
			msg.setSuccess(true);
			msg.setMessage("信息删除成功");
			msg.setData("");
		} catch (Exception ex) {
			ex.printStackTrace();
			msg.setSuccess(false);
			msg.setMessage(ex.getMessage());
			msg.setData("");
		}
		return msg;
	}

	@RequestMapping(value = "del/{id}")
	@ResponseBody
	public String del(@PathVariable("id") Long id) {
		parameterService.delete(id);
		return "true";
	}

	@RequestMapping(value = "getmonitorlist")
	@ResponseBody
	public List<Parameter> getmonitorlist() {
		List<Parameter> monitorList = new ArrayList();
		try {
			ShiroUser su = super.getLoginUser();

			User user = accountService.findUserByLoginName(su.getLoginName());
			if (user != null) {

				monitorList = parameterService
						.getParameterByCategoryAndSubcategory("monitor_unit",
								"monitor_unit_list");

			} else {
				logger.log(this.getClass(), Logger.ERROR_INT, "登陆帐号无效!", "",
						null);
			}
		} catch (Exception ex) {
			logger.log(this.getClass(), Logger.ERROR_INT, ex.getMessage(),
					super.getLoginUser().getLoginName(), null);
		}
		return monitorList;
	}

}
