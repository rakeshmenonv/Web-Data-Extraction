package com.infotop.webharvest.dataharvest.service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import net.infotop.util.DateTimeUtil;
import net.infotop.util.OperationNoUtil;
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
	double similarityTreshold = 0.80;
	boolean ignoreFormattingTags = false;
	boolean useContentSimilarity = false;
	int maxNodeInGeneralizedNodes = 9;
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	Pagedatainfo listPagedatainfo=null; 
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
		Document doc;
		listPagedatainfo = null;
		logmsg.setMessage(null);
		WebClient webClient = new WebClient(BrowserVersion.FIREFOX_24); // Chrome not working
		HtmlPage page = null;
		try 
		{ webClient.getOptions().setJavaScriptEnabled(true);

	    CookieManager cookieMan = new CookieManager();
	    cookieMan = webClient.getCookieManager();
	    cookieMan.setCookiesEnabled(true);

	    webClient.getOptions().setRedirectEnabled(true);
	    webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
	    webClient.getOptions().setTimeout(60000);
        
	    webClient.getOptions().setPrintContentOnFailingStatusCode(true);
	    webClient.getOptions().setThrowExceptionOnScriptError(false);
		page = webClient.getPage(pageurlinfo.getUrl());
		} catch (FailingHttpStatusCodeException e1) 
		{
		    // TODO Auto-generated catch block
		    e1.printStackTrace();
		}
		catch (MalformedURLException e1) 
		{
		    // TODO Auto-generated catch block
		    e1.printStackTrace();
		}
		catch (IOException e1) 
		{
		    // TODO Auto-generated catch block
		    e1.printStackTrace();
		} 
		
		String selectedelement = pageurlinfo.getElement() + "["
				+ pageurlinfo.getAttribute() + "=" + pageurlinfo.getValue()
				+ "]";
		
		doc = Jsoup.parse(page.asXml());
		if(pageurlinfo.getAttribute().equals("id")){
			   selectedelement = pageurlinfo.getElement()+"#"+pageurlinfo.getValue();
		}else if(pageurlinfo.getAttribute().equals("class")){
			
			   selectedelement = pageurlinfo.getElement()+"."+pageurlinfo.getValue();
		}
		
		Elements elements = doc.select(selectedelement);
		if(!pageurlinfo.getElement().equals("table")){
			Elements tableElements=elements.select("table");
			if(!tableElements.isEmpty()){
				parseElements(tableElements,pageurlinfo);
				elements.select("table").remove();
			}
		}
		parseElements(elements,pageurlinfo);
		return true;
	}
	private void parseElements(Elements elements,Pageurlinfo pageurlinfo) throws MalformedURLException{
		for (Element element2 : elements) {
			String tableGroupKey = OperationNoUtil.getUUID();
			if (!element2.nodeName().equals("table")){
			for (Element element : element2.getAllElements()) {
				String rowGroupKey = OperationNoUtil.getUUID();
				if (element.nodeName().equals("img")) {
					
					String absUrl=JsoupUtil.getabsUrl(pageurlinfo.getUrl(), element.attr("src"));
					for (Attribute attribute : element.attributes()) {

						if (("src".equals(attribute.getKey()))) {
							Pagedatainfo pagedatainfo = new Pagedatainfo();
							if (!element.ownText().isEmpty()) {
								pagedatainfo.setContent(element.ownText()
										+ "|" +  "<img src='"+absUrl+"' alt=\"+"+element.ownText()+"\"  >");
							} else {
								pagedatainfo.setContent( "<img src='"+absUrl+"' alt=\"+"+element.ownText()+"\"  >");
							}
							pagedatainfo.setType(element.nodeName());
							pagedatainfo.setExtractedDate(DateTimeUtil
									.nowTimeStr());
							pagedatainfo.setRowGroupKey(rowGroupKey);
							pagedatainfo.setTableGroupKey(tableGroupKey);
							pagedatainfo.setPageurlinfo(pageurlinfo);
							pageDataInfoSave(pagedatainfo);
							
							listPagedatainfo = pagedatainfo;
							listPagedatainfo.setContent(pagedatainfo.getContent());
							listPagedatainfo.setPageurlinfo(pageurlinfo);
							listPagedatainfo.setExtractedDate(DateTimeUtil.nowTimeStr());
							logmsg.setSuccess(true);
							
						}
						if ("abs:src".equals(attribute.getKey())) {
							Pagedatainfo pagedatainfo = new Pagedatainfo();
							if (!element.ownText().isEmpty()) {
								pagedatainfo.setContent(element.ownText()
										+ "|" + "<img src='"+absUrl+"' alt=\"+"+element.ownText()+"\"  >");
								
								
							} else {
								pagedatainfo.setContent("<img src='"+absUrl+"' alt=\"+"+element.ownText()+"\"  >");
							}
							pagedatainfo.setType(element.nodeName());
							pagedatainfo.setExtractedDate(DateTimeUtil
									.nowTimeStr());
							pagedatainfo.setRowGroupKey(rowGroupKey);
							pagedatainfo.setTableGroupKey(tableGroupKey);
							pagedatainfo.setPageurlinfo(pageurlinfo);
							pageDataInfoSave(pagedatainfo);
							
							listPagedatainfo = pagedatainfo;
							listPagedatainfo.setContent(pagedatainfo.getContent());
							listPagedatainfo.setPageurlinfo(pageurlinfo);
							listPagedatainfo.setExtractedDate(DateTimeUtil.nowTimeStr());
							logmsg.setSuccess(true);
						}
						if ("data-lazyload".equals(attribute.getKey())) {
							absUrl=JsoupUtil.getabsUrl(pageurlinfo.getUrl(), element.attr("data-lazyload"));
							Pagedatainfo pagedatainfo = new Pagedatainfo();
							if (!element.ownText().isEmpty()) {
								pagedatainfo.setContent(element.ownText()
										+ "|"
										+ "<img src=\""+absUrl+"\" alt=\"+"+element.ownText()+"\"  >" );
							} else {
								pagedatainfo.setContent("<img src='"+absUrl+"' alt=\"+"+element.ownText()+"\"  >" );
							}
							pagedatainfo.setPageurlinfo(pageurlinfo);
							pagedatainfo.setExtractedDate(DateTimeUtil
									.nowTimeStr());
							pagedatainfo.setType(element.nodeName());
							pagedatainfo.setRowGroupKey(rowGroupKey);
							pagedatainfo.setTableGroupKey(tableGroupKey);
							pageDataInfoSave(pagedatainfo);
							
							listPagedatainfo = pagedatainfo;
							listPagedatainfo.setContent(pagedatainfo.getContent());
							listPagedatainfo.setPageurlinfo(pageurlinfo);
							listPagedatainfo.setExtractedDate(DateTimeUtil.nowTimeStr());
							logmsg.setSuccess(true);
						}
					}
				} else if (element.nodeName().equals("a")&&!element.attr("href").isEmpty()) {
					String absUrl=JsoupUtil.getabsUrl(pageurlinfo.getUrl(), element.attr("href"));
					Pagedatainfo pagedatainfo = new Pagedatainfo();
					if (!element.ownText().isEmpty()) {
						pagedatainfo.setContent(element.ownText() + "|"
								+"<a href='"+absUrl+"'>"+element.ownText()+"</a>");
					} else {
						pagedatainfo.setContent("<a href='"+absUrl+"'>Link</a>");
						
					}
					pagedatainfo.setType(element.nodeName());
					pagedatainfo
							.setExtractedDate(DateTimeUtil.nowTimeStr());
					pagedatainfo.setRowGroupKey(rowGroupKey);
					pagedatainfo.setPageurlinfo(pageurlinfo);
					pagedatainfo.setTableGroupKey(tableGroupKey);
					pageDataInfoSave(pagedatainfo);
					
					listPagedatainfo = pagedatainfo;
					listPagedatainfo.setContent(pagedatainfo.getContent());
					listPagedatainfo.setPageurlinfo(pageurlinfo);
					listPagedatainfo.setExtractedDate(DateTimeUtil.nowTimeStr());
					logmsg.setSuccess(true);
				} else if (element.nodeName().equals("script")) {
				} else if (element.nodeName().equals("Imports")) {
				} else {

					Pagedatainfo pagedatainfo = new Pagedatainfo();
					pagedatainfo.setContent(element.ownText());
					pagedatainfo.setType(element.nodeName());
					pagedatainfo
							.setExtractedDate(DateTimeUtil.nowTimeStr());
					pagedatainfo.setRowGroupKey(rowGroupKey);
					pagedatainfo.setTableGroupKey(tableGroupKey);
					pagedatainfo.setPageurlinfo(pageurlinfo);
					if (!element.ownText().isEmpty()) {
						if (!element.ownText().equals("  ")) {
							pageDataInfoSave(pagedatainfo);
							listPagedatainfo = pagedatainfo;
							listPagedatainfo.setContent(pagedatainfo.getContent());
							listPagedatainfo.setPageurlinfo(pageurlinfo);
							listPagedatainfo.setExtractedDate(DateTimeUtil.nowTimeStr());
							logmsg.setSuccess(true);
						}

					} else {
					}

				}
			}
			
			
		}else{
			String rowGroupKey = OperationNoUtil.getUUID();
				for (Element element : element2.getAllElements()) {
				if (element.nodeName().equals("tr")){
					  rowGroupKey = OperationNoUtil.getUUID();
				}else{
				}
				
				if (element.nodeName().equals("img")) {
					String absUrl=JsoupUtil.getabsUrl(pageurlinfo.getUrl(), element.attr("src"));
					
					for (Attribute attribute : element.attributes()) {

						if (("src".equals(attribute.getKey()))) {
							Pagedatainfo pagedatainfo = new Pagedatainfo();
							if (!element.ownText().isEmpty()) {
								pagedatainfo.setContent(element.ownText()
										+ "|" +  "<img src='"+absUrl+"' alt=\"+"+element.ownText()+"\"  >");
							} else {
								pagedatainfo.setContent( "<img src='"+absUrl+"' alt=\""+element.ownText()+"\"  >");
							}
							pagedatainfo.setType(element.nodeName());
							pagedatainfo.setExtractedDate(DateTimeUtil
									.nowTimeStr());
							pagedatainfo.setRowGroupKey(rowGroupKey);
							pagedatainfo.setTableGroupKey(tableGroupKey);
							pagedatainfo.setPageurlinfo(pageurlinfo);
							pageDataInfoSave(pagedatainfo);
							
							listPagedatainfo = pagedatainfo;
							listPagedatainfo.setContent(pagedatainfo.getContent());
							listPagedatainfo.setPageurlinfo(pageurlinfo);
							listPagedatainfo.setExtractedDate(DateTimeUtil.nowTimeStr());
							logmsg.setSuccess(true);
							
						}
						if ("abs:src".equals(attribute.getKey())) {
							Pagedatainfo pagedatainfo = new Pagedatainfo();
							if (!element.ownText().isEmpty()) {
								pagedatainfo.setContent(element.ownText()
										+ "|" + "<img src='"+absUrl+"' alt=\"+"+element.ownText()+"\"  >");
								
								
							} else {
								pagedatainfo.setContent("<img src='"+absUrl+"' alt=\"+"+element.ownText()+"\"  >");
							}
							pagedatainfo.setType(element.nodeName());
							pagedatainfo.setExtractedDate(DateTimeUtil
									.nowTimeStr());
							pagedatainfo.setRowGroupKey(rowGroupKey);
							pagedatainfo.setTableGroupKey(tableGroupKey);
							pagedatainfo.setPageurlinfo(pageurlinfo);
							pageDataInfoSave(pagedatainfo);
							
							listPagedatainfo = pagedatainfo;
							listPagedatainfo.setContent(pagedatainfo.getContent());
							listPagedatainfo.setPageurlinfo(pageurlinfo);
							listPagedatainfo.setExtractedDate(DateTimeUtil.nowTimeStr());
							logmsg.setSuccess(true);
						}
						if ("data-lazyload".equals(attribute.getKey())) {
							absUrl=JsoupUtil.getabsUrl(pageurlinfo.getUrl(), element.attr("data-lazyload"));
							Pagedatainfo pagedatainfo = new Pagedatainfo();
							if (!element.ownText().isEmpty()) {
								pagedatainfo.setContent(element.ownText()
										+ "|"
										+ "<img src='"+absUrl+"' alt=\"+"+element.ownText()+"\"  >" );
							} else {
								pagedatainfo.setContent("<img src='"+absUrl+"' alt=\"+"+element.ownText()+"\"  >" );
							}
							pagedatainfo.setPageurlinfo(pageurlinfo);
							pagedatainfo.setExtractedDate(DateTimeUtil
									.nowTimeStr());
							pagedatainfo.setType(element.nodeName());
							pagedatainfo.setRowGroupKey(rowGroupKey);
							pagedatainfo.setTableGroupKey(tableGroupKey);
							pageDataInfoSave(pagedatainfo);
							
							listPagedatainfo = pagedatainfo;
							listPagedatainfo.setContent(pagedatainfo.getContent());
							listPagedatainfo.setPageurlinfo(pageurlinfo);
							listPagedatainfo.setExtractedDate(DateTimeUtil.nowTimeStr());
							logmsg.setSuccess(true);
						}
					}
				} else if (element.nodeName().equals("a")&&!element.attr("href").isEmpty()) {
					Pagedatainfo pagedatainfo = new Pagedatainfo();
					String absUrl=JsoupUtil.getabsUrl(pageurlinfo.getUrl(), element.attr("href"));
					if (!element.ownText().isEmpty()) {
						pagedatainfo.setContent("<a href='"+absUrl+"'>"+element.ownText()+"</a>");
					} else {
						pagedatainfo.setContent("<a href='"+absUrl+"'>Link</a>");
						
					}
					pagedatainfo.setType(element.nodeName());
					pagedatainfo
							.setExtractedDate(DateTimeUtil.nowTimeStr());
					pagedatainfo.setRowGroupKey(rowGroupKey);
					pagedatainfo.setPageurlinfo(pageurlinfo);
					pagedatainfo.setTableGroupKey(tableGroupKey);
					pageDataInfoSave(pagedatainfo);
					
					listPagedatainfo = pagedatainfo;
					listPagedatainfo.setContent(pagedatainfo.getContent());
					listPagedatainfo.setPageurlinfo(pageurlinfo);
					listPagedatainfo.setExtractedDate(DateTimeUtil.nowTimeStr());
					logmsg.setSuccess(true);
				} else if (element.nodeName().equals("script")) {
				} else if (element.nodeName().equals("Imports")) {
				} else {

					Pagedatainfo pagedatainfo = new Pagedatainfo();
					pagedatainfo.setContent(element.ownText());
					pagedatainfo.setType(element.nodeName());
					pagedatainfo
							.setExtractedDate(DateTimeUtil.nowTimeStr());
					pagedatainfo.setRowGroupKey(rowGroupKey);
					pagedatainfo.setTableGroupKey(tableGroupKey);
					pagedatainfo.setPageurlinfo(pageurlinfo);
					if (!element.ownText().isEmpty()) {
						if (!element.ownText().equals("  ")) {
							pageDataInfoSave(pagedatainfo);
							listPagedatainfo = pagedatainfo;
							listPagedatainfo.setContent(pagedatainfo.getContent());
							listPagedatainfo.setPageurlinfo(pageurlinfo);
							listPagedatainfo.setExtractedDate(DateTimeUtil.nowTimeStr());
							logmsg.setSuccess(true);
						}

					} else {
					}

				}
			
			}
			}
		}
	}
	private void pageDataInfoSave(Pagedatainfo pagedatainfo) {
		try{
		pagedatainfoService.save(pagedatainfo);
		}catch(Exception e){
			e.printStackTrace();	
		}
	}
	public boolean basicsave(Pageurlinfo pageurlinfo) {
		    
		try {
			listPagedatainfo = null;
			logmsg.setMessage(null);
			TagTreeBuilder builder = new DOMParserTagTreeBuilder();
			TagTree tagTree = builder.buildTagTree(pageurlinfo.getUrl(),
					ignoreFormattingTags);
			TreeMatcher matcher = new SimpleTreeMatching();
			DataRegionsFinder dataRegionsFinder = new MiningDataRegions(matcher);
			List<DataRegion> dataRegions = dataRegionsFinder.findDataRegions(
					tagTree.getRoot(), maxNodeInGeneralizedNodes,
					similarityTreshold);
			DataRecordsFinder dataRecordsFinder = new MiningDataRecords(matcher);
			DataRecord[][] dataRecords = new DataRecord[dataRegions.size()][];
			for (int dataRecordArrayCounter = 0; dataRecordArrayCounter < dataRegions
					.size(); dataRecordArrayCounter++) {
				DataRegion dataRegion = dataRegions.get(dataRecordArrayCounter);
				dataRecords[dataRecordArrayCounter] = dataRecordsFinder
						.findDataRecords(dataRegion, similarityTreshold);
			}
			ColumnAligner aligner = null;
			if (useContentSimilarity) {
				aligner = new PartialTreeAligner(
						new EnhancedSimpleTreeMatching());
			} else {
				aligner = new PartialTreeAligner(matcher);
			}
			List<String[][]> dataTables = new ArrayList<String[][]>();
			for (int tableCounter = 0; tableCounter < dataRecords.length; tableCounter++) {
				String[][] dataTable = aligner
						.alignDataRecords(dataRecords[tableCounter]);

				if (dataTable != null) {
					dataTables.add(dataTable);
				}
			}
			int recordsFound = 0;
			for (String[][] dataTable : dataTables) {
				recordsFound += dataTable.length;
			}
			// int tableCounter = 1;
			for (String[][] table : dataTables) {
				String tableGroupKey = OperationNoUtil.getUUID();
				// output.format("<h2>Table %s</h2>\n<table>\n<thead>\n<tr>\n<th>Row Number</th>\n",
				// tableCounter);
				// for(int columnCounter=1; columnCounter<=table[0].length;
				// columnCounter++)
				// {
				// output.format("<th></th>\n");
				// }
				// output.format("</tr>\n</thead>\n<tbody>\n");
				// int rowCounter = 1;
				for (String[] row : table) {
					String rowGroupKey = OperationNoUtil.getUUID();
					// output.format("<tr>\n<td>%s</td>", rowCounter);
					// int columnCounter = 1;
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

						pagedatainfo.setType(getTagType(itemText.trim()));
						pagedatainfoService.save(pagedatainfo);		
						
						listPagedatainfo = pagedatainfo;
						listPagedatainfo.setContent(item);
						listPagedatainfo.setPageurlinfo(pageurlinfo);
						listPagedatainfo.setExtractedDate(DateTimeUtil.nowTimeStr());
						logmsg.setSuccess(true);

						pagedatainfo.setType(getTagType(itemText.trim()));
						pagedatainfoService.save(pagedatainfo);						
                        //github.com/rakeshmenonv/Web-Data-Extraction.git
						//System.out.println(itemText);
						// output.format("<td>%s</td>\n", itemText);
						// columnCounter++;
					}
					// output.format("</tr>\n");
					// rowCounter++;
				}
				
				// output.format("</tbody>\n</table>\n");
				// tableCounter++;
			}
			
			
			// output.format("</body></html>");
		} catch (SecurityException exception) {
			logmsg.setMessage(exception.toString());
			exception.printStackTrace();
			return false;
		} catch (FileNotFoundException exception) {
			logmsg.setMessage(exception.toString());
			exception.printStackTrace();
			return false;
		} catch (IOException exception) {
			logmsg.setMessage("connection Timeout : "+pageurlinfo.getUrl()+" can not access.");
			exception.printStackTrace();			
			return false;
		} catch (SAXException exception) {
			exception.printStackTrace();
			return false;
		} catch (Exception exception) {
			logmsg.setMessage(exception.toString());
			exception.printStackTrace();
			return false;
		}
		return true;
	}

	public String logProgress(HttpServletResponse response) throws JsonProcessingException{

		/*ObjectMapper mapper = new ObjectMapper();
	    logmsg.setData(listPagedatainfo);
	    listPagedatainfo = null;
        return "retry: 5\ndata:"+mapper.writeValueAsString(logmsg)+"\n\n";*/
		
		ObjectMapper mapper = new ObjectMapper();
	    logmsg.setData(listPagedatainfo);
	    String event =  "retry: 5\ndata:"+mapper.writeValueAsString(logmsg)+"\n\n";
        logmsg  = new Message();
        listPagedatainfo = null;
        return event;
		
	}

	public String getTagType(String data){
		if(data.contains("<a href")){
			return "Link";
		}else if(data.contains("<img src")){
			return "Image";
		}
		return "Text";		
	}
	
}
