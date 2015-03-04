package com.infotop.webharvest.dataharvest.service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Element;
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
import org.xml.sax.SAXException;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.CookieManager;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.infotop.webharvest.pageurlinfo.entity.Pageurlinfo;

public class HarvestUtil {
	static double similarityTreshold = 0.80;
	static boolean ignoreFormattingTags = false;
	static boolean useContentSimilarity = false;
	static int maxNodeInGeneralizedNodes = 9;
    private static Pattern absoluteURIPattern = Pattern.compile("^.*:.*$");
	private static Pattern PartialURIPattern = Pattern.compile("//.*$");

	public static String getContent(Element element, String url)
			throws MalformedURLException {
		String content = "";
		if (element.nodeName().equals("img")) {
			String absUrl = getabsUrl(url, element.attr("src"));
			for (Attribute attribute : element.attributes()) {
				if (("src".equals(attribute.getKey()))) {
					if (!element.ownText().isEmpty()) {
						content = element.ownText() + "|" + "<img src='"
								+ absUrl + "' alt=\"+" + element.ownText()
								+ "\"  >";
					} else {
						content = "<img src='" + absUrl + "' alt=\"+"
								+ element.ownText() + "\"  >";
					}
				}
				if ("abs:src".equals(attribute.getKey())) {
					if (!element.ownText().isEmpty()) {
						content = element.ownText() + "|" + "<img src='"
								+ absUrl + "' alt=\"+" + element.ownText()
								+ "\"  >";
					} else {
						content = "<img src='" + absUrl + "' alt=\"+"
								+ element.ownText() + "\"  >";
					}
				}
				if ("data-lazyload".equals(attribute.getKey())) {
					absUrl = getabsUrl(url,	element.attr("data-lazyload"));
					if (!element.ownText().isEmpty()) {
						content = element.ownText() + "|" + "<img src=\""
								+ absUrl + "\" alt=\"+" + element.ownText()
								+ "\"  >";
					} else {
						content = "<img src='" + absUrl + "' alt=\"+"
								+ element.ownText() + "\"  >";
					}
				}
			}
		} else if (element.nodeName().equals("a")
				&& !element.attr("href").isEmpty()) {
			String absUrl = getabsUrl(url, element.attr("href"));
			if (!element.ownText().isEmpty()) {
				content = element.ownText() + "|" + "<a href='" + absUrl + "'>"
						+ element.ownText() + "</a>";
			} else {
				content = "<a href='" + absUrl + "'>Link</a>";

			}
		} else if (element.nodeName().equals("script")) {
		} else if (element.nodeName().equals("Imports")) {
		} else {
			content = element.ownText();
		}
		return content;
	}

	public static HtmlPage getPage(String url) {
		WebClient webClient = new WebClient(BrowserVersion.FIREFOX_24);
		HtmlPage page = null;
		try {
			webClient.getOptions().setJavaScriptEnabled(true);
			CookieManager cookieMan = new CookieManager();
			cookieMan = webClient.getCookieManager();
			cookieMan.setCookiesEnabled(true);
			webClient.getOptions().setRedirectEnabled(true);
			webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
			webClient.getOptions().setTimeout(60000);
			webClient.getOptions().setPrintContentOnFailingStatusCode(true);
			webClient.getOptions().setThrowExceptionOnScriptError(false);
			page = webClient.getPage(url);
		} catch (FailingHttpStatusCodeException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (MalformedURLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return page;
	}

	public static List<String[][]> getDataTables(String url) {
		TagTreeBuilder builder = new DOMParserTagTreeBuilder();
		TagTree tagTree;
		List<String[][]> dataTables = new ArrayList<String[][]>();
		try {
			tagTree = builder.buildTagTree(url, ignoreFormattingTags);

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

			for (int tableCounter = 0; tableCounter < dataRecords.length; tableCounter++) {
				String[][] dataTable = aligner
						.alignDataRecords(dataRecords[tableCounter]);

				if (dataTable != null) {
					dataTables.add(dataTable);
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return dataTables;
	}

	public static String getTagType(String data) {
		if (data.contains("<a href")) {
			return "Link";
		} else if (data.contains("<img src")) {
			return "Image";
		}
		return "Text";
	}
	
	public static String getabsUrl(String baseURL,String relativeUrl) throws MalformedURLException{
    	URL url = new URL(baseURL);
    	baseURL = url.getProtocol() + "://" + url.getHost();
    	Matcher absoluteURIMatcher = absoluteURIPattern.matcher( relativeUrl );		
		if ( ! absoluteURIMatcher.matches() )
		{
			Matcher partialURIMatcher = PartialURIPattern.matcher( relativeUrl );
			if(! partialURIMatcher.matches() ){
				String[] splitimgURI=relativeUrl.split("/",2);	
				if(splitimgURI[0].contains(".") || splitimgURI[0].contains("/")){									
					if(splitimgURI.length==2){
						relativeUrl = baseURL+"/" + splitimgURI[1];	
					}else{
						relativeUrl = baseURL+"/" + splitimgURI[0];
					}
				}else{
					relativeUrl = baseURL+"/" + relativeUrl;	
				}														
			}else{
				relativeUrl = "http:" + relativeUrl;
			}
		}
		return relativeUrl;
    }
}
