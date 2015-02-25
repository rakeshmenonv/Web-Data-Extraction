package com.infotop.webharvest.dataharvest.service;

import java.io.FileNotFoundException;
import java.io.IOException;
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
	public boolean selectedsave(Pageurlinfo pageurlinfo) {
		Document doc;
		try {
			logmsg.setMessage(null);
			doc = Jsoup.connect(pageurlinfo.getUrl()).ignoreContentType(true)
					.parser(Parser.xmlParser()).get();
			String selectedelement = pageurlinfo.getElement() + "["
					+ pageurlinfo.getAttribute() + "=" + pageurlinfo.getValue()
					+ "]";
			Elements elements = doc.select(selectedelement);
			for (Element element2 : elements) {
				String tableGroupKey = OperationNoUtil.getUUID();
				element2.getAllElements();
				for (Element element : element2.getAllElements()) {
					String rowGroupKey = OperationNoUtil.getUUID();
					if (element.nodeName().equals("img")) {
						for (Attribute attribute : element.attributes()) {

							if (("src".equals(attribute.getKey()))) {
								Pagedatainfo pagedatainfo = new Pagedatainfo();
								if (!element.ownText().isEmpty()) {
									pagedatainfo.setContent(element.ownText()
											+ "|" + element.absUrl("src"));
								} else {
									pagedatainfo.setContent(element
											.absUrl("src"));
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
											+ "|" + element.attr("abs:src"));
								} else {
									pagedatainfo.setContent(element
											.attr("abs:src"));
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
								Pagedatainfo pagedatainfo = new Pagedatainfo();
								if (!element.ownText().isEmpty()) {
									pagedatainfo.setContent(element.ownText()
											+ "|"
											+ element.attr("data-lazyload"));
								} else {
									pagedatainfo.setContent(element
											.attr("data-lazyload"));
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
					} else if (element.nodeName().equals("a")) {
						Pagedatainfo pagedatainfo = new Pagedatainfo();
						if (!element.ownText().isEmpty()) {
							pagedatainfo.setContent(element.ownText() + "|"
									+ element.attr("abs:href"));
						} else {
							pagedatainfo.setContent(element.attr("abs:href"));
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
						if (!element.ownText().isEmpty()) {

							pagedatainfo.setContent(element.ownText()
									.replaceAll("  ", " ").trim());
						} else {
							pagedatainfo.setContent(element.attr("abs:href"));
						}
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
			
		} catch (IOException e) {
			logmsg.setMessage("connection Timeout : "+pageurlinfo.getUrl()+" can not access.");
			e.printStackTrace();
			return false;
		}
		return true;
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
		/*try {
              // Thread.sleep(500);
		} catch (InterruptedException e) {
               e.printStackTrace();
		} */
		ObjectMapper mapper = new ObjectMapper();
		/*if(listPagedatainfo!=null){
			*/logmsg.setData(listPagedatainfo);
	//	}
	return "retry: 5\ndata:"+mapper.writeValueAsString(logmsg)+"\n\n";
		
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
