package com.infotop.webharvest.dataharvest.web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletRequest;

import net.infotop.web.easyui.DataGrid;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springside.modules.web.Servlets;

import ch.qos.logback.classic.Logger;

import com.infotop.common.BasicController;
import com.infotop.system.account.entity.User;
import com.infotop.system.account.service.ShiroDbRealm.ShiroUser;
import com.infotop.webharvest.pagedatainfo.entity.Pagedatainfo;
import com.infotop.webharvest.pagedatainfo.service.PagedatainfoService;

@Controller
@RequestMapping(value = "/dataharvest")
public class DataHarvestController extends BasicController {
	 @Autowired
	    private PagedatainfoService pagedatainfoService;
	
	void saveData(){
		
	}
	  @RequestMapping(value = "saveselected")
		@ResponseBody
		public DataGrid findList(
				Model model, ServletRequest request) {
			DataGrid dataGrid = new DataGrid();
			try {
				ShiroUser su = super.getLoginUser();
				User user = accountService.findUserByLoginName(su.getLoginName());
				if (user != null) {
					selectedsave();
				} else {
					logger.log(this.getClass(),Logger.ERROR_INT,"登陆帐号无效!","",null);
				}
			} catch (Exception ex) {
				logger.log(this.getClass(),Logger.ERROR_INT,ex.getMessage(),super.getLoginUser().getLoginName(),null);
			}
			return dataGrid;
		}
	public void selectedsave(){

		Document doc;
		try {
	 
			// need http protocol
			doc = Jsoup.connect("http://jd.com/").ignoreContentType(true)
				      .parser(Parser.xmlParser()).get();
//			Elements elements = doc.select("div[class=sc-list clearfix]");
			String tableGroupKey ="div[class=item w-bg]";
			Elements elements = doc.select(tableGroupKey);
			for (Element element2 : elements) {
				 element2.getAllElements();
				 for (Element element:element2.getAllElements()){
					 if(element.nodeName().equals("img")){
							for (Attribute attribute:element.attributes() ){
								
								if (("src".equals(attribute.getKey()))){
									Pagedatainfo  pagedatainfo =new Pagedatainfo();
									if (!element.ownText().isEmpty()){
										pagedatainfo.setContent(element.ownText()+"|"+element.absUrl("src"));
									}else{
										pagedatainfo.setContent(element.absUrl("src"));
									}
								
									pagedatainfo.setType(element.nodeName());
									pagedatainfo.setRowGroupKey(element2.nodeName()+"[class=+"+element2.className()+"]");
									pagedatainfo.setTableGroupKey(tableGroupKey);
									pagedatainfoService.save(pagedatainfo);
									System.out.println(element.nodeName()+"text"+element.ownText()+"%%%%%%%%"+ element.absUrl("src"));
								}
								if("abs:src".equals(attribute.getKey())){
									Pagedatainfo  pagedatainfo =new Pagedatainfo();
									if (!element.ownText().isEmpty()){
										pagedatainfo.setContent(element.ownText()+"|"+element.attr("abs:src"));
									}else{
										pagedatainfo.setContent(element.attr("abs:src"));
									}
									pagedatainfo.setType(element.nodeName());
									pagedatainfo.setRowGroupKey(element2.nodeName()+"[class=+"+element2.className()+"]");
									pagedatainfo.setTableGroupKey(tableGroupKey);
									pagedatainfoService.save(pagedatainfo);
									System.out.println(element.nodeName()+"text"+element.ownText()+"%%%%%%%%"+ element.attr("abs:src"));
								}
								if ("data-lazyload".equals(attribute.getKey())){
									Pagedatainfo  pagedatainfo =new Pagedatainfo();
									if (!element.ownText().isEmpty()){
										pagedatainfo.setContent(element.ownText()+"|"+element.attr("data-lazyload"));
									}else{
										pagedatainfo.setContent(element.attr("data-lazyload"));
									}
									pagedatainfo.setType(element.nodeName());
									pagedatainfo.setRowGroupKey(element2.nodeName()+"[class=+"+element2.className()+"]");
									pagedatainfo.setTableGroupKey(tableGroupKey);
									pagedatainfoService.save(pagedatainfo);
									System.out.println(element.nodeName()+"text"+element.ownText()+"%%%%%%%%"+ element.attr("data-lazyload"));
								}
							}
						}else if (element.nodeName().equals("a")){
							Pagedatainfo  pagedatainfo =new Pagedatainfo();
							if (!element.ownText().isEmpty()){
								pagedatainfo.setContent(element.ownText()+"|"+element.attr("abs:href"));
							}else{
								pagedatainfo.setContent(element.attr("abs:href"));
							}
							pagedatainfo.setType(element.nodeName());
							pagedatainfo.setRowGroupKey(element2.nodeName()+"[class=+"+element2.className()+"]");
							pagedatainfo.setTableGroupKey(tableGroupKey);
							pagedatainfoService.save(pagedatainfo);
							System.out.println(element.nodeName()+"text"+element.ownText()+"$$$$$$$$"+ element.attr("abs:href"));
						}else if (element.nodeName().equals("script")){
							Pagedatainfo  pagedatainfo =new Pagedatainfo();
							if (!element.ownText().isEmpty()){
								pagedatainfo.setContent(element.ownText()+"|"+element.absUrl("src"));
							}else{
								pagedatainfo.setContent(element.absUrl("src"));
							}
							pagedatainfo.setType(element.nodeName());
							pagedatainfo.setRowGroupKey(element2.nodeName()+"[class=+"+element2.className()+"]");
							pagedatainfo.setTableGroupKey(tableGroupKey);
							pagedatainfoService.save(pagedatainfo);
							System.out.println(element.nodeName()+"text"+element.ownText()+"&&&&&&&"+ element.absUrl("src"));
						} else	if (element.nodeName().equals("Imports")){
							Pagedatainfo  pagedatainfo =new Pagedatainfo();
							if (!element.ownText().isEmpty()){
								pagedatainfo.setContent(element.ownText()+"|"+element.attr("abs:href"));
							}else{
								pagedatainfo.setContent(element.attr("abs:href"));
							}
							pagedatainfo.setType(element.nodeName());
							pagedatainfo.setRowGroupKey(element2.nodeName()+"[class=+"+element2.className()+"]");
							pagedatainfo.setTableGroupKey(tableGroupKey);
							pagedatainfoService.save(pagedatainfo);
							System.out.print(element.nodeName()+"text"+element.ownText()+"*********"+element.attr("abs:href"));
						} else{
							
							Pagedatainfo  pagedatainfo =new Pagedatainfo();
							if (!element.ownText().isEmpty()){
								pagedatainfo.setContent(element.ownText());
							}else{
								pagedatainfo.setContent(element.attr("abs:href"));
							}
							pagedatainfo.setType(element.nodeName());
							pagedatainfo.setRowGroupKey(element2.nodeName()+"[class=+"+element2.className()+"]");
							pagedatainfo.setTableGroupKey(tableGroupKey);
//							pagedatainfoService.save(pagedatainfo);
							if (!element.ownText().isEmpty()){
								pagedatainfoService.save(pagedatainfo);
//								System.out.println("coming"+element.ownText());
							}else{
//								System.out.println("not coming"+ element.ownText());
							}
							
						}
				 }
				 System.out.println("###############################");
			}
			
//			}
	 
		} catch (IOException e) {
			e.printStackTrace();
		}
	
	}
	
	
}
