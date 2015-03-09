package com.infotop.webharvest.dataharvest.service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletResponse;

import net.infotop.util.DateTimeUtil;
import net.infotop.util.OperationNoUtil;
import net.infotop.util.StringUtils;
import net.infotop.web.easyui.Message;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.safety.Whitelist;
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
import org.seagatesoft.sde.tagtreebuilder.JsoupUtil;
import org.seagatesoft.sde.tagtreebuilder.TagTreeBuilder;
import org.seagatesoft.sde.treematcher.EnhancedSimpleTreeMatching;
import org.seagatesoft.sde.treematcher.SimpleTreeMatching;
import org.seagatesoft.sde.treematcher.TreeMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.xml.sax.SAXException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.CookieManager;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.infotop.webharvest.pagedatainfo.entity.Pagedatainfo;
import com.infotop.webharvest.pagedatainfo.service.PagedatainfoService;
import com.infotop.webharvest.pageurlinfo.entity.Pageurlinfo;
import com.infotop.webharvest.pageurlinfo.service.PageurlinfoService;

@Component
@Transactional(readOnly = true)
public class DataHarvestService {
	@Autowired
	private PageurlinfoService pageurlinfoService;

	@Autowired
	private PagedatainfoService pagedatainfoService;
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	Pagedatainfo listPagedatainfo; 
	Message logmsg  = new Message();
	
	public List getPiechartData()
	{
		String sqlStr = "select count(url)  as value, url as name from page_url_info group by url order by  value desc limit 10";
		return jdbcTemplate.queryForList(sqlStr);
	}
	
	public List getPiechartDate(String url)
	{
		
		String sqlStr = "select  count(url)as value, date_format(extracted_date,'%d-%m-%Y') as name  from webharvest.page_url_info where url='"+url+"' group by name";
		return jdbcTemplate.queryForList(sqlStr);
	}
	public boolean selectedsave(Pageurlinfo pageurlinfo) throws MalformedURLException {
		dataExtract(pageurlinfo,pageurlinfo.getUrl());
		String pageFormate=pageurlinfo.getPageFormat();
		if(!pageFormate.isEmpty()){
			int startPage=pageurlinfo.getStartPage();
			int endPage=pageurlinfo.getEndPage();
			for(int i=startPage;i<=endPage;i++){
				dataExtract(pageurlinfo,pageFormate.replace("*",String.valueOf(i)));
			}
		}
		return true;
	}
	
	private void dataExtract(Pageurlinfo pageurlinfo,String url) throws MalformedURLException{
		Document doc;
		listPagedatainfo = null;
		logmsg.setMessage(null);		
		HtmlPage page = HarvestUtil.getPage(url);	
		doc = Jsoup.parse(page.asXml());
		selectedLogic(pageurlinfo, doc);
	}

	public void selectedLogic(Pageurlinfo pageurlinfo, Document doc)
			throws MalformedURLException {
		Elements elements = HarvestUtil.selectedElement(pageurlinfo, doc);
		if ((pageurlinfo.getStartTag() != null && pageurlinfo.getEndTag() != null)
				&& (!pageurlinfo.getStartTag().isEmpty() && !pageurlinfo
						.getEndTag().isEmpty())) {
			final Matcher matcher = HarvestUtil.patternAndMatcher(pageurlinfo,
					elements);
			while (matcher.find()) {
				if (matcher.group(1) != null && matcher.group(1) != "") {
					doc = Jsoup.parse(pageurlinfo.getStartTag()
							+ matcher.group(1) + pageurlinfo.getEndTag());
					elements = doc.select("body");
					tableLogic(pageurlinfo, elements);
					parseElements(elements, pageurlinfo);
				}
			}

		} else {
			if (!pageurlinfo.getElement().equals("table")) {
				tableLogic(pageurlinfo, elements);
			}
			parseElements(elements, pageurlinfo);
		}
	}

	public void tableLogic(Pageurlinfo pageurlinfo, Elements elements)
			throws MalformedURLException {
		Elements tableElements=elements.select("table");
		if(!tableElements.isEmpty()){
			parseElements(tableElements,pageurlinfo);
			elements.select("table").remove();
		}
	}

	

	
	
	
	private void parseElements(Elements elements,Pageurlinfo pageurlinfo) throws MalformedURLException{
		for (Element element2 : elements) {
			String tableGroupKey = OperationNoUtil.getUUID();
			if (!element2.nodeName().equals("table")) {
				for (Element element : element2.getAllElements()) {
					String rowGroupKey = OperationNoUtil.getUUID();
					saveData(element, pageurlinfo, rowGroupKey, tableGroupKey);
				}
			} else {
				String rowGroupKey = OperationNoUtil.getUUID();
				for (Element element : element2.getAllElements()) {
					if (element.nodeName().equals("tr")) {
						rowGroupKey = OperationNoUtil.getUUID();
					} 
					saveData(element, pageurlinfo, rowGroupKey, tableGroupKey);
				}
			}
		}
	}
	
	private void saveData(Element element, Pageurlinfo pageurlinfo, String rowGroupKey, String tableGroupKey) throws MalformedURLException{
		Pagedatainfo pagedatainfo=new Pagedatainfo();
		String content=HarvestUtil.getContent(element,pageurlinfo.getUrl());
		if (!content.isEmpty()) {
			if (!content.equals("  ")) {
				pagedatainfo.setContent(content);
				pagedatainfo.setType(element.nodeName());
				pagedatainfo.setExtractedDate(DateTimeUtil.nowTimeStr());
				pagedatainfo.setRowGroupKey(rowGroupKey);
				pagedatainfo.setTableGroupKey(tableGroupKey);
				pagedatainfo.setPageurlinfo(pageurlinfo);
				pagedatainfoService.save(pagedatainfo);		
				listPagedatainfo = pagedatainfo;		
				logmsg.setSuccess(true);
			}
		}
	}	
	
	public boolean basicsave(Pageurlinfo pageurlinfo) {		    
		try {
			listPagedatainfo = null;
			logmsg.setMessage(null);			
			List<String[][]> dataTables = HarvestUtil.getDataTables(pageurlinfo.getUrl());			
			for (String[][] table : dataTables) {
				String tableGroupKey = OperationNoUtil.getUUID();			
				for (String[] row : table) {
					String rowGroupKey = OperationNoUtil.getUUID();					
					for (String item : row) {
						String itemText = item;
						if (itemText == null) {
							itemText = "";
						}
						Pagedatainfo pagedatainfo = new Pagedatainfo();
						pagedatainfo.setContent(itemText.trim().replace("\"", "'"));
						pagedatainfo.setPageurlinfo(pageurlinfo);
						pagedatainfo.setTableGroupKey(tableGroupKey);
						pagedatainfo.setRowGroupKey(rowGroupKey);
						pagedatainfo
								.setExtractedDate(DateTimeUtil.nowTimeStr());
						pagedatainfo.setType(HarvestUtil.getTagType(itemText.trim()));
						pagedatainfoService.save(pagedatainfo);	
						listPagedatainfo = pagedatainfo;				
						logmsg.setSuccess(true);
					}					
				}
			}
		} catch (SecurityException exception) {
			logmsg.setMessage(exception.toString());
			exception.printStackTrace();
			return false;
		} catch (Exception exception) {
			logmsg.setMessage(exception.toString());
			exception.printStackTrace();
			return false;
		}
		return true;
	}

	public void logProgress(HttpServletResponse response) throws IOException{
		ObjectMapper mapper = new ObjectMapper();
		Pagedatainfo sentPagedatainfo = new Pagedatainfo();
		sentPagedatainfo = listPagedatainfo;
		listPagedatainfo = null;
	    logmsg.setData(sentPagedatainfo);
	    sentPagedatainfo = null;
        response.setContentType("text/event-stream");
        response.setCharacterEncoding("UTF-8");
        PrintWriter writer = response.getWriter();
        String str = " ";
        String repeated = StringUtils.repeat(str, 2048);
        writer.write(":"+repeated+"\n"); // 2 kB padding for IE
        writer.write("retry: 5\n");
        writer.write("data: " + mapper.writeValueAsString(logmsg) + "\n\n");
        logmsg  = new Message();
        listPagedatainfo = null;
        writer.flush();
        writer.close();		
	}	
}
