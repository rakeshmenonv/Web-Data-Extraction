package com.infotop.webharvest.dataharvest.web;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import net.infotop.util.DateTimeUtil;
import net.infotop.util.OperationNoUtil;
import net.infotop.web.easyui.Message;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.xml.sax.SAXException;

import ch.qos.logback.classic.Logger;

import com.infotop.common.BasicController;
import com.infotop.system.account.entity.User;
import com.infotop.system.account.service.ShiroDbRealm.ShiroUser;
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
    
	String input = "";
	String resultOutput = "MDR.html";
	double similarityTreshold = 0.80;
	boolean ignoreFormattingTags = false;
	boolean useContentSimilarity = false;
	int maxNodeInGeneralizedNodes = 9;
	
	
	 @RequestMapping(value = "extract", method = RequestMethod.POST)
	 @ResponseBody
	 public Message extract(@Valid Pageurlinfo pageurlinfo, RedirectAttributes redirectAttributes) {
		try {
			ShiroUser su = super.getLoginUser();
			User user = accountService.findUserByLoginName(su.getLoginName());
			if (user != null) {
		    	pageurlinfoService.save(pageurlinfo);
		    	Basicsave(pageurlinfo);
				msg.setSuccess(true);
				msg.setMessage("信息添加成功");
				msg.setData(pageurlinfo);
			} else {
				logger.log(this.getClass(),Logger.ERROR_INT,"登陆帐号无效!","",null);
				msg.setSuccess(false);
				msg.setMessage("登陆帐号无效!");
				msg.setData("");
			}
		} catch (Exception ex) {
			logger.log(this.getClass(),Logger.ERROR_INT,ex.getMessage(),super.getLoginUser().getLoginName(),null);
			msg.setSuccess(false);
			msg.setMessage(ex.getMessage());
			msg.setData("");
		}
		return msg;
	}

	@RequestMapping(value = "")
	public String list() {
		return "dataharvest/basicsearch";
	}

	
	void Basicsave(Pageurlinfo pageurlinfo){
	
	try
	{
		
		TagTreeBuilder builder = new DOMParserTagTreeBuilder();	
		TagTree tagTree = builder.buildTagTree(input, ignoreFormattingTags);		
		TreeMatcher matcher = new SimpleTreeMatching();
		DataRegionsFinder dataRegionsFinder = new MiningDataRegions( matcher );	
		List<DataRegion> dataRegions = dataRegionsFinder.findDataRegions(tagTree.getRoot(), maxNodeInGeneralizedNodes, similarityTreshold);		
		DataRecordsFinder dataRecordsFinder = new MiningDataRecords( matcher );		
		DataRecord[][] dataRecords = new DataRecord[ dataRegions.size() ][];			
		for( int dataRecordArrayCounter = 0; dataRecordArrayCounter < dataRegions.size(); dataRecordArrayCounter++)
		{
			DataRegion dataRegion = dataRegions.get( dataRecordArrayCounter );
			dataRecords[ dataRecordArrayCounter ] = dataRecordsFinder.findDataRecords(dataRegion, similarityTreshold);
		}	
		ColumnAligner aligner = null;
		if ( useContentSimilarity )
		{
			aligner = new PartialTreeAligner( new EnhancedSimpleTreeMatching() );
		}
		else
		{
			aligner = new PartialTreeAligner( matcher );
		}
		List<String[][]> dataTables = new ArrayList<String[][]>();		
		for(int tableCounter=0; tableCounter< dataRecords.length; tableCounter++)
		{
			String[][] dataTable = aligner.alignDataRecords( dataRecords[tableCounter] );

			if ( dataTable != null )
			{
				dataTables.add( dataTable );
			}
		}		
		int recordsFound = 0;					
		for ( String[][] dataTable: dataTables )
		{
			recordsFound += dataTable.length;
		}
		//int tableCounter = 1;
		for ( String[][] table: dataTables)
		{
			String tableGroupKey=OperationNoUtil.getUUID();
			//output.format("<h2>Table %s</h2>\n<table>\n<thead>\n<tr>\n<th>Row Number</th>\n", tableCounter);
			//for(int columnCounter=1; columnCounter<=table[0].length; columnCounter++)
			//{
				//output.format("<th></th>\n");
			//}
			//output.format("</tr>\n</thead>\n<tbody>\n");
			//int rowCounter = 1;
			for (String[] row: table)
			{
				String rowGroupKey=OperationNoUtil.getUUID();
				//output.format("<tr>\n<td>%s</td>", rowCounter);
				//int columnCounter = 1;
				for(String item: row)
				{
					Pagedatainfo pagedatainfo=new Pagedatainfo();
					pagedatainfo.setContent(item);
					pagedatainfo.setPageurlinfo(pageurlinfo);
					pagedatainfo.setTableGroupKey(tableGroupKey);
					pagedatainfo.setRowGroupKey(rowGroupKey);
					pagedatainfo.setExtractedDate(DateTimeUtil.nowTimeStr());
					pagedatainfo.setType("");
					pagedatainfoService.save(pagedatainfo);
					String itemText = item;
					if (itemText == null)
					{
						itemText = "";
					}
					System.out.println(itemText);
					//output.format("<td>%s</td>\n", itemText);
					//columnCounter++;
				}
				//output.format("</tr>\n");
				//rowCounter++;
			}
			//output.format("</tbody>\n</table>\n");
			//tableCounter++;
		}
		
		//output.format("</body></html>");
	}
	catch (SecurityException exception)
	{
		exception.printStackTrace();
		System.exit(1);
	}
	catch(FileNotFoundException exception)
	{
		exception.printStackTrace();
		System.exit(2);
	}
	catch(IOException exception)
	{
		exception.printStackTrace();
		System.exit(3);
	}
	catch(SAXException exception)
	{
		exception.printStackTrace();
		System.exit(4);
	}
	catch(Exception exception)
	{
		exception.printStackTrace();
		System.exit(5);
	}
	
}
	
	
	
	@RequestMapping(value="/log", method=RequestMethod.GET)
	@ResponseBody  
	public String sendMessage(Locale locale, HttpServletResponse response) {
		 Random r = new Random();
		 System.out.println("inside");
         response.setContentType("text/event-stream");
         try {
                 Thread.sleep(10000);
         } catch (InterruptedException e) {
                 e.printStackTrace();
         } 
         for(int i=0;i<=100;i++)
         {
        	 return "data:arun" + i +"\n\n";
         }
         //return "data:Testing 1,2,3" + r.nextInt() +"\n\n";
		return null;
	
      }
}
