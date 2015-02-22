package com.infotop.webharvest.dataharvest.web;

import java.util.Map;

import javax.servlet.ServletRequest;

import net.infotop.web.easyui.DataGrid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springside.modules.web.Servlets;

import ch.qos.logback.classic.Logger;

import com.infotop.common.BasicController;
import com.infotop.system.account.entity.User;
import com.infotop.system.account.service.ShiroDbRealm.ShiroUser;
import com.infotop.webharvest.dataharvest.service.AuditService;
import com.infotop.webharvest.pagedatainfo.service.PagedatainfoService;
import com.infotop.webharvest.pageurlinfo.service.PageurlinfoService;

@Controller
@RequestMapping(value = "/audit")
public class AuditController extends  BasicController{
	@Autowired
	private PageurlinfoService pageurlinfoService;

	@Autowired
	private PagedatainfoService pagedatainfoService;
	
	@Autowired
	private AuditService auditService;
	
	@RequestMapping(value = "urlCount", method = RequestMethod.GET)
	public String urlCount(Model model, ServletRequest request) {
    	ShiroUser su = super.getLoginUser();
		User user = accountService.findUserByLoginName(su.getLoginName());
		if (user != null) {
			//TODO add some code.
		} else {
			logger.log(this.getClass(),Logger.ERROR_INT,"登陆帐号无效!","",null);
			return "redirect:/login";
		}
       return "dataharvest/audit/urlinfoLogList";
    }
 	@RequestMapping(value = "urlInfo/{url}", method = RequestMethod.GET)
	public String urlInfo(Model model, ServletRequest request,@PathVariable("url") String url) {
    	ShiroUser su = super.getLoginUser();
		User user = accountService.findUserByLoginName(su.getLoginName());
		if (user != null) {
			//TODO add some code.
			model.addAttribute("urlName", url);
		} else {
			logger.log(this.getClass(),Logger.ERROR_INT,"登陆帐号无效!","",null);
			return "redirect:/login";
		}
       return "dataharvest/audit/urlinfoView";
    }
 	 @RequestMapping(value = "findLogList", method = RequestMethod.POST )
 	@ResponseBody
 	public DataGrid findLogList(
 			@RequestParam(value = "sort", defaultValue = "auto") String sortType,
 			@RequestParam(value = "order", defaultValue = "desc") String order,
 			@RequestParam(value = "page", defaultValue = "1") int pageNumber,
 			@RequestParam(value = "rows", defaultValue = ROWS) int rows,
 			Model model, ServletRequest request) {
 		DataGrid dataGrid = new DataGrid();
 		try {
 			ShiroUser su = super.getLoginUser();
 			User user = accountService.findUserByLoginName(su.getLoginName());
 			if (user != null) {
 				Map<String, Object> searchParams = Servlets
 						.getParametersStartingWith(request, "search_");
 				model.addAttribute("searchParams", Servlets
 						.encodeParameterStringWithPrefix(searchParams, "search_"));
 				dataGrid = auditService.dataGridForLog(searchParams, pageNumber,
 						rows, sortType, order);
 			} else {
 				logger.log(this.getClass(),Logger.ERROR_INT,"登陆帐号无效!","",null);
 			}
 		} catch (Exception ex) {
 			logger.log(this.getClass(),Logger.ERROR_INT,ex.getMessage(),super.getLoginUser().getLoginName(),null);
 		}
 		return dataGrid;
 	}

 	    @RequestMapping(value = "getUrlInfo", method = RequestMethod.GET)
	 	@ResponseBody
	 	public DataGrid getUrlInfo(
	 			@RequestParam(value = "sort", defaultValue = "auto") String sortType,
	 			@RequestParam(value = "order", defaultValue = "desc") String order,
	 			@RequestParam(value = "page", defaultValue = "1") int pageNumber,
	 			@RequestParam(value = "rows", defaultValue = ROWS) int rows,
	 			@RequestParam(value = "urlName") String url,
	 			Model model, ServletRequest request) {
	 		DataGrid dataGrid = new DataGrid();
	 		try {
	 			ShiroUser su = super.getLoginUser();
	 			User user = accountService.findUserByLoginName(su.getLoginName());
	 			if (user != null) {
	 				Map<String, Object> searchParams = Servlets
	 						.getParametersStartingWith(request, "search_");
	 				model.addAttribute("searchParams", Servlets
	 						.encodeParameterStringWithPrefix(searchParams, "search_"));
	 				dataGrid = auditService.dataGridForUrlInfo(searchParams, pageNumber,
	 						rows, sortType, order,url);
	 			} else {
	 				logger.log(this.getClass(),Logger.ERROR_INT,"登陆帐号无效!","",null);
	 			}
	 		} catch (Exception ex) {
	 			logger.log(this.getClass(),Logger.ERROR_INT,ex.getMessage(),super.getLoginUser().getLoginName(),null);
	 		}
	 		return dataGrid;
	 	}
}



