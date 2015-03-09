package com.infotop.webharvest.dataharvest.web;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import net.infotop.util.DateTimeUtil;
import net.infotop.util.OperationNoUtil;
import net.infotop.web.easyui.DataGrid;
import net.infotop.web.easyui.Message;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;
import org.seagatesoft.sde.DataRecord;
import org.seagatesoft.sde.DataRegion;
import org.seagatesoft.sde.TagTree;
import org.seagatesoft.sde.columnaligner.ColumnAligner;
import org.seagatesoft.sde.columnaligner.PartialTreeAligner;
import org.seagatesoft.sde.datarecordsfinder.DataRecordsFinder;
import org.seagatesoft.sde.datarecordsfinder.MiningDataRecords;
import org.seagatesoft.sde.dataregionsfinder.DataRegionsFinder;
import org.seagatesoft.sde.dataregionsfinder.MiningDataRegions;
import org.seagatesoft.sde.tagtreebuilder.DOMParserTagTreeBuilder;
import org.seagatesoft.sde.tagtreebuilder.TagTreeBuilder;
import org.seagatesoft.sde.treematcher.EnhancedSimpleTreeMatching;
import org.seagatesoft.sde.treematcher.SimpleTreeMatching;
import org.seagatesoft.sde.treematcher.TreeMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.xml.sax.SAXException;

import ch.qos.logback.classic.Logger;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.infotop.common.BasicController;
import com.infotop.system.account.entity.User;
import com.infotop.system.account.service.ShiroDbRealm.ShiroUser;
import com.infotop.system.parameter.entity.Parameter;
import com.infotop.webharvest.dataharvest.service.DataHarvestService;
import com.infotop.webharvest.pagedatainfo.entity.Pagedatainfo;
import com.infotop.webharvest.pagedatainfo.service.PagedatainfoService;
import com.infotop.webharvest.pageurlinfo.entity.Pageurlinfo;
import com.infotop.webharvest.pageurlinfo.service.PageurlinfoService;

@Controller
@RequestMapping(value = "/dataharvest")
public class DataHarvestController extends BasicController {

	@Autowired
	private PageurlinfoService pageurlinfoService;

	@Autowired
	private PagedatainfoService pagedatainfoService;

	@Autowired
	private DataHarvestService dataHarvestService;

	double similarityTreshold = 0.80;
	boolean ignoreFormattingTags = false;
	boolean useContentSimilarity = false;
	int maxNodeInGeneralizedNodes = 9;

	@RequestMapping(value = "extract", method = RequestMethod.GET)
	public String extract(Model model) {
		ShiroUser su = super.getLoginUser();
		User user = accountService.findUserByLoginName(su.getLoginName());
		if (user != null) {
			Pageurlinfo entity = new Pageurlinfo();
			// List<Parameter> schedulerList =
			// parameterService.getParameterByCategory("scheduler");
			List<Parameter> schedulerList = parameterService
					.getParameterByCategoryAndSubcategory("scheduler",
							"schedulerType");
			model.addAttribute("schedulerList", schedulerList);
			model.addAttribute("pageurlinfo", entity);
			model.addAttribute("action", "extract");
		} else {
			logger.log(this.getClass(), Logger.ERROR_INT, "登陆帐号无效!", "", null);
			return "redirect:/login";
		}
		return "dataharvest/basicsearch";
	}

	@RequestMapping(value = "extract", method = RequestMethod.POST)
	@ResponseBody
	public Message extract(@Valid Pageurlinfo pageurlinfo,
			RedirectAttributes redirectAttributes) {
		try {

			ShiroUser su = super.getLoginUser();
			boolean result = false;
			User user = accountService.findUserByLoginName(su.getLoginName());
			if (user != null) {

				try{
					  Integer.parseInt(pageurlinfo.getJobon());
				}catch(Exception e){
					pageurlinfo.setJobon(null);
				}
				pageurlinfo.setExtractedDate(DateTimeUtil.nowTimeStr());
				pageurlinfoService.save(pageurlinfo);
				if (pageurlinfo.getElement().isEmpty()) {
					result = dataHarvestService.basicsave(pageurlinfo);
				} else {
					result = dataHarvestService.selectedsave(pageurlinfo);
				}
				msg.setSuccess(result);
				msg.setMessage("信息添加成功");
				msg.setData(pageurlinfo);
			} else {
				logger.log(this.getClass(), Logger.ERROR_INT, "登陆帐号无效!", "",
						null);
				msg.setSuccess(false);
				msg.setMessage("登陆帐号无效!");
				msg.setData("");
			}
		} catch (Exception ex) {
			logger.log(this.getClass(), Logger.ERROR_INT, ex.getMessage(),
					super.getLoginUser().getLoginName(), null);
			msg.setSuccess(false);
			msg.setMessage(ex.getMessage());
			msg.setData("");
		}
		return msg;
	}

	@RequestMapping(value = "saveElement", method = RequestMethod.POST)
	@ResponseBody
	public Message saveElement(@Valid Pageurlinfo pageurlinfo,
			RedirectAttributes redirectAttributes) {
		try {

			ShiroUser su = super.getLoginUser();
			
			User user = accountService.findUserByLoginName(su.getLoginName());
			if (user != null) {

				try{
					  Integer.parseInt(pageurlinfo.getNextScheduleOn());
				}catch(Exception e){
					pageurlinfo.setJobon(null);
				}
				pageurlinfo.setExtractedDate(DateTimeUtil.nowTimeStr());
				pageurlinfoService.save(pageurlinfo);
				msg.setSuccess(true);
				msg.setMessage("信息添加成功");
				msg.setData(pageurlinfo);
			} else {
				logger.log(this.getClass(), Logger.ERROR_INT, "登陆帐号无效!", "",
						null);
				msg.setSuccess(false);
				msg.setMessage("登陆帐号无效!");
				msg.setData("");
			}
		} catch (Exception ex) {
			logger.log(this.getClass(), Logger.ERROR_INT, ex.getMessage(),
					super.getLoginUser().getLoginName(), null);
			msg.setSuccess(false);
			msg.setMessage(ex.getMessage());
			msg.setData("");
		}
		return msg;
	}
	@RequestMapping(value = "extracted")
	@ResponseBody
	public Message extracted(@Valid Pageurlinfo pageurlinfo,
			RedirectAttributes redirectAttributes, @RequestParam("id") Long id) {
		try {

			ShiroUser su = super.getLoginUser();
			boolean result = false;
			User user = accountService.findUserByLoginName(su.getLoginName());
			if (user != null) {
				Pageurlinfo pageurlinfonew = new Pageurlinfo();
				pageurlinfo = pageurlinfoService.get(id);
				pageurlinfonew.setUrl(pageurlinfo.getUrl());
				pageurlinfonew.setAttribute(pageurlinfo.getAttribute());
				pageurlinfonew.setElement(pageurlinfo.getElement());
				pageurlinfonew.setJobon(pageurlinfo.getJobon());
				pageurlinfonew.setValue(pageurlinfo.getValue());
				pageurlinfonew.setExtractedDate(DateTimeUtil.nowTimeStr());
				pageurlinfoService.save(pageurlinfonew);
				if (pageurlinfonew.getElement().isEmpty()) {
					result = dataHarvestService.basicsave(pageurlinfonew);
				} else {
					result = dataHarvestService.selectedsave(pageurlinfonew);
				}
				msg.setSuccess(result);
				msg.setMessage("信息添加成功");
				msg.setData(pageurlinfonew);
			} else {
				logger.log(this.getClass(), Logger.ERROR_INT, "登陆帐号无效!", "",
						null);
				msg.setSuccess(false);
				msg.setMessage("完成！单击“下一步”观");
				msg.setData("");
			}
		} catch (Exception ex) {
			logger.log(this.getClass(), Logger.ERROR_INT, ex.getMessage(),
					super.getLoginUser().getLoginName(), null);
			msg.setSuccess(false);
			msg.setMessage(ex.getMessage());
			msg.setData("");
		}
		return msg;
	}

	@RequestMapping(value = "showlog")
	public String showlog() {
		return "dataharvest/showlog";
	}

	@RequestMapping(value = "/log", method = RequestMethod.GET)

	public void sendMessage(Locale locale, HttpServletResponse response) throws IOException{
		dataHarvestService.logProgress(response);
	
	}

	@RequestMapping(value = "showdata/{id}", method = RequestMethod.GET)
	public String updateForm(@PathVariable("id") Long id, Model model) {
		ShiroUser su = super.getLoginUser();
		User user = accountService.findUserByLoginName(su.getLoginName());
		if (user != null) {
			Pageurlinfo entity = pageurlinfoService.get(id);
			List<Pagedatainfo> pagedatainfoList = pagedatainfoService
					.getAlldatainfo(entity);
			model.addAttribute("pagedatainfoList", pagedatainfoList);
			model.addAttribute("action", "update");
		} else {
			logger.log(this.getClass(), Logger.ERROR_INT, "登陆帐号无效!", "", null);
			return "redirect:/login";
		}
		return "dataharvest/showdata";
	}

	@RequestMapping(value = "delete", method = RequestMethod.POST)
	@ResponseBody
	public Message delete(@RequestParam(value = "tableGroupKeyList") List<String> tableGroupKeyList,
			ServletRequest request) throws Exception {
		try {
			ShiroUser su = super.getLoginUser();
			User user = accountService.findUserByLoginName(su.getLoginName());
			if (user != null) {
				pagedatainfoService.deleteByTableGroupKey(tableGroupKeyList);
				msg.setSuccess(true);
				msg.setMessage("信息删除成功");
				msg.setData("");
			} else {
				logger.log(this.getClass(), Logger.ERROR_INT, "登陆帐号无效!", "",
						null);
				msg.setSuccess(false);
				msg.setMessage("登陆帐号无效!");
				msg.setData("");
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			msg.setSuccess(false);
			msg.setMessage(ex.getMessage());
			msg.setData("");

		}
		return msg;
	}
}
