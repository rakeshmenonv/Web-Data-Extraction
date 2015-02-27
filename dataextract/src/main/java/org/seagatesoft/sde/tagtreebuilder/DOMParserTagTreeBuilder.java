/*
 *  TagTreeBuilder.java  - Kelas yang digunakan untuk membangun TagTree dari InputSource menggunakan 
 *  parser DOM. Parser DOM yang digunakan adalah NekoHTML.
 *  Copyright (C) 2009 Sigit Dewanto
 *  sigitdewanto11@yahoo.co.uk
 *  http://sigit.web.ugm.ac.id
 *
 *  This program is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU Lesser General Public License
 *  as published by the Free Software Foundation; either version 2
 *  of the License, or (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 */
package org.seagatesoft.sde.tagtreebuilder;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

import org.seagatesoft.sde.TagNode;
import org.seagatesoft.sde.TagTree;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.cyberneko.html.HTMLElements;
import org.cyberneko.html.parsers.DOMParser;
import org.jsoup.Jsoup;
import org.jsoup.parser.Parser;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.javascript.background.JavaScriptJobManager;

/**
 * Kelas yang dapat membangun TagTree dari InputSource menggunakan parser DOM. Parser DOM yang digunakan adalah parser DOM dari NekoHTML.
 * 
 * @author seagate
 *
 */
public class DOMParserTagTreeBuilder implements TagTreeBuilder
{
	/**
	 * array yang menyimpan tag2 yang diabaikan dalam membangun pohon tag
	 */
	private short[] ignoredTags = { HTMLElements.STYLE, HTMLElements.SCRIPT, HTMLElements.APPLET, HTMLElements.OBJECT};
	/**
	 * 
	 */
	private Pattern filterPattern = Pattern.compile("^[\\s\\W]*$");
	/**
	 * 
	 */
	private Pattern absoluteURIPattern = Pattern.compile("^.*:.*$");
	private Pattern PartialURIPattern = Pattern.compile("//.*$");
	//g.tbcdn.cn/s.gif
	/**
	 * 
	 */
	private String baseURI;
	/**
	 * 
	 */
	private TagNodeCreator tagNodeCreator;
	
	
	private int connectionTimeout = 5000;
    private int soTimeout = 12000;
    
	/**
	 * Membangun TagTree dari system identifier yang diberikan. Method ini equivalent dengan 
	 * <code>parse(new InputSource(htmlDocument));</code>
	 * 
	 * @param htmlDocument
	 * @return
	 */
	public TagTree buildTagTree(String htmlDocument)  throws IOException, SAXException
	{
		return buildTagTree(htmlDocument, false);
	}
	
	public TagTree buildTagTree(String htmlDocument, boolean ignoreFormattingTags)  throws IOException, SAXException
	{
		URL url = new URL(htmlDocument);
		HttpURLConnection httpcon = (HttpURLConnection) url.openConnection();
	    httpcon.addRequestProperty("User-Agent", "Mozilla/4.0");
	    BufferedReader in = new BufferedReader(  
                new InputStreamReader(httpcon.getInputStream(),"UTF-8"));
	    baseURI = url.getProtocol() + "://" + url.getHost();
		return buildTagTree(new InputSource(in), ignoreFormattingTags,htmlDocument);
	}
	
	/**
	 * Membangun TagTree dari InputSource. Proses pembangunan TagTree dilakukan menggunakan parser 
	 * DOM. Root dari TagTree adalah elemen BODY.
	 * 
	 * @param inputSource
	 * @return TagTree dari InputSource
	 */
	public TagTree buildTagTree(InputSource inputSource) throws IOException, SAXException
	{
		return buildTagTree(inputSource, false);
	}

	public TagTree buildTagTree(InputSource inputSource, boolean ignoreFormattingTags,String htmlDocument) throws IOException, SAXException 
	{
		WebClient webClient = new WebClient(BrowserVersion.FIREFOX_24); // Chrome not working
		HtmlPage page = null;
		try {
	        page = processWebPage(htmlDocument,webClient);
	    } catch (Exception e) {
	        System.out.println("Get page error");
	    }
        TagTree tree = null;
		if ( ignoreFormattingTags )
		{
			tagNodeCreator = new IgnoreFormattingTagsTagNodeCreator();
		}
		else
		{
			tagNodeCreator = new DefaultTagNodeCreator();
		}
		DOMParser parser = new DOMParser();
		try {  
	           //设置网页的默认编码  
			parser.setFeature("http://xml.org/sax/features/validation", true);
			parser.setFeature("http://apache.org/xml/features/dom/include-ignorable-whitespace", false);
	        parser.setProperty("http://cyberneko.org/html/properties/default-encoding","UTF-8");  
	        parser.setFeature("http://xml.org/sax/features/namespaces", false);  
	        inputSource.setEncoding("UTF-8");
	        } catch (Exception e) {  
	           e.printStackTrace();  
	        }  
		org.jsoup.nodes.Document doc=Jsoup.parse(page.asXml());
		File temprFile = File.createTempFile("parsedHtml", ".html");
		temprFile.deleteOnExit();
        FileOutputStream fos = new FileOutputStream(temprFile);
        fos.write(doc.html().getBytes("UTF-8"));
        fos.close();
        BufferedReader in = new BufferedReader(
                new InputStreamReader(new FileInputStream(temprFile.getAbsolutePath()), "UTF8"));
        parser.parse(new InputSource(in));
		org.w3c.dom.Document documentNode =parser.getDocument();
		//baseURI = documentNode.getBaseURI();
		//Pattern baseDirectoryPattern = Pattern.compile("^(.*/)[^/]*$");
		//Matcher matcher = baseDirectoryPattern.matcher( baseURI );
			
		/*// dapatkan BaseURI dari dokumen HTML ini
		if ( matcher.lookingAt() )
		{
			baseURI = matcher.group(1);
		}*/
			
		Node bodyNode = documentNode.getElementsByTagName("BODY").item(0);
		TagNode rootTagNode = new TagNode();
		tree = new TagTree();
		tree.setRoot(rootTagNode);
		rootTagNode.setTagElement(HTMLElements.getElement(bodyNode.getNodeName()).code);
		tree.addTagNodeAtLevel(rootTagNode);
		Node child = bodyNode.getFirstChild();

		while(child != null)
		{
			tagNodeCreator.createTagNodes(child, rootTagNode, tree);
			child = child.getNextSibling();
		}
		tree.assignNodeNumber();
		return tree;
	}
	
	private static HtmlPage processWebPage(String url, WebClient webClient) throws InterruptedException {
		webClient.waitForBackgroundJavaScript(5000);
		webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
		webClient.getOptions().setThrowExceptionOnScriptError(false);
		HtmlPage page = null;
		try {
		    page = webClient.getPage(url);
		    /*int jsPending = 1;
            int msecsToWait = 2048;
            int msecsWaited = 0;
            jsPending = webClient.waitForBackgroundJavaScript(msecsToWait);
            msecsWaited += msecsToWait;
            while (jsPending > 0 && msecsWaited < 10000){
           	 jsPending = webClient.waitForBackgroundJavaScript(msecsToWait);
           	 msecsWaited += msecsToWait;
           	 msecsToWait = msecsToWait/2;
            }*/
		} catch (Exception e) {
		    System.out.println(e+"Get page error");
		}
		JavaScriptJobManager manager = page.getEnclosingWindow().getJobManager();
		// Thread.sleep(10000);
		/*while (manager.getJobCount() > 0) {
		    Thread.sleep(1000);
		}*/
		//System.out.println(page.asXml());
		return page;
	}
	/**
	 * Method untuk mengecek apakah array mengandung element
	 * 
	 * @param array array element HTML
	 * @param element element HTML yang akan dicek apakah berada dalam array 
	 * @return hasil pengecekan, true jika element terdapat pada array dan false jika sebaliknya
	 */
	private boolean arrayContains(short[] array, short element)
	{
		for (short e: array)
		{
			if (e == element)
			{
				return true;
			}
		}
		
		return false;
	}
	
	private interface TagNodeCreator
	{
		public void createTagNodes(Node node, TagNode parent, TagTree tagTree);
	}
	
	private class DefaultTagNodeCreator implements TagNodeCreator
	{
		/**
		 * Membuat TagNode yang merupakan node pada TagTree dengan menggunakan informasi dari Node yang 
		 * dihasilkan oleh parser DOM. Method ini dipanggil secara rekursif.
		 * 
		 * @param node node yang akan dijadikan sumber informasi untuk membuat TagNode
		 * @param parent Parent dari TagNode yang akan dibuat
		 * @param tagTree TagTree yang sedang dibuat
		 */
		public void createTagNodes(Node node, TagNode parent, TagTree tagTree)
		{
			// jika node DOM bertipe ELEMENT
			if (node.getNodeType() == Node.ELEMENT_NODE)
			{
				// dapatkan tagCode-nya (representasi tag dalam short)
				short tagCode = HTMLElements.getElement(node.getNodeName()).code;

				// jika tagCode tidak termasuk dalam daftar tag yang diabaikan
				if ( !arrayContains(ignoredTags, tagCode) )
				{
					// salin node DOM menjadi TagNode (tagCode-nya)
					TagNode tagNode = new TagNode();
					tagNode.setTagElement(tagCode);
					// set parent dari TagNode yang baru dibuat
					tagNode.setParent(parent);
					// tambahkan ke dalam TagTree
					tagTree.addTagNodeAtLevel(tagNode);
					
					// jika tagCode merupakan tag IMG
					if ( tagCode == HTMLElements.IMG )
					{
						// dapatkan nilai atribut src-nya
						NamedNodeMap attributesMap = node.getAttributes();
						String imgURI = "";
						try{
							imgURI = attributesMap.getNamedItem("src").getNodeValue();
						}catch(Exception e){
							
						}
						Matcher absoluteURIMatcher = absoluteURIPattern.matcher( imgURI );
						
						// jika URI pada atribut src bukan merupakan URI absolut (URI relatif)
						if ( ! absoluteURIMatcher.matches() )
						{
							Matcher partialURIMatcher = PartialURIPattern.matcher( imgURI );
							if(! partialURIMatcher.matches() ){
								String[] splitimgURI=imgURI.split("/",2);	
								if(splitimgURI[0].contains(".") || splitimgURI[0].contains("/")){									
									if(splitimgURI.length==2){
										imgURI = baseURI+"/" + splitimgURI[1];	
									}else{
										imgURI = baseURI+"/" + splitimgURI[0];
									}
								}else{
									imgURI = baseURI+"/" + imgURI;	
								}														
							}else{
								imgURI = "http:" + imgURI;
							}
							// tambahkan baseURI sehingga menjadi URI absolut
						}
						
						// tambahkan tag IMG dengan src-nya sebagai teks HTML pada TagNode parent-nya
						String imgText = String.format("<img src=\"%s\" />", imgURI);
						tagNode.appendInnerText( imgText );
					}
					// jika tagCode merupakan tag A
					else if( tagCode == HTMLElements.A )
					{
						// append teks di dalam tag A ke innerText milik parent
						parent.appendInnerText( node.getTextContent() );
						// dapatkan map atribut dari tag A ini
						NamedNodeMap attributesMap = node.getAttributes();
						
						// jika tag A ini memiliki atribut href
						if ( attributesMap.getNamedItem("href") != null)
						{
							// dapatkan nilai atribut href-nya
							String linkURI = attributesMap.getNamedItem("href").getNodeValue();
							Matcher absoluteURIMatcher = absoluteURIPattern.matcher( linkURI );
						
							// jika nilai atribut href bukan merupakan URI absolut
							if ( ! absoluteURIMatcher.matches() )
							{
								Matcher partialURIMatcher = PartialURIPattern.matcher( linkURI );
								if(! partialURIMatcher.matches() ){											
									String[] splitimgURI=linkURI.split("/",2);	
									if(splitimgURI[0].contains(".") || splitimgURI[0].contains("/")){
										if(splitimgURI.length==2){
											linkURI = baseURI+"/" + splitimgURI[1];	
										}else{
											linkURI = baseURI+"/" + splitimgURI[0];
										}
									}else{
										linkURI = baseURI+"/" + linkURI;	
									}	
								}else{
									linkURI = "http:" + linkURI;
								}
								// tambahkan baseURI sehingga menjadi URI absolut								
							}
						
							// tambahkan tag A dengan href-nya dan teks Link sebagai teks HTML pada TagNode parent-nya
							String linkText = String.format("<a href=\"%s\">Link&lt;&lt;</a>", linkURI);
							tagNode.appendInnerText( linkText );
						}
					}
					
					// lakukan secara rekursif penyalinan Node DOM menjadi DOM pada anak2 dari node ini (jika memiliki anak)
					Node child = node.getFirstChild();
					
					while(child != null)
					{
						createTagNodes(child, tagNode, tagTree);
						child = child.getNextSibling();
					}
				}
			}
			// jika node DOM bertipe TEXT
			else if (node.getNodeType() == Node.TEXT_NODE)
			{
				Matcher filterMatcher = filterPattern.matcher( node.getNodeValue() );
				
				// kalau Text node ini hanya berisi string yang tidak terbaca, maka tidak usah 
				// disimpan sebagai innerText pada node parent-nya
				if ( ! filterMatcher.matches() )
				{
					// jika mengandung teks yang bisa terbaca, maka di-append pada innerText node parent-nya
					parent.appendInnerText(node.getNodeValue());
				}
			}
			// selain bertipe ELEMENT dan TEXT diabaikan
		}
	}
	
	private class IgnoreFormattingTagsTagNodeCreator implements TagNodeCreator
	{
		/**
		 * array yang menyimpan tag2 formatting
		 */
		private short[] formattingTags = { HTMLElements.B, HTMLElements.I, HTMLElements.U, HTMLElements.STRONG, HTMLElements.STRIKE, HTMLElements.EM, HTMLElements.BIG, HTMLElements.SMALL, HTMLElements.SUP, HTMLElements.SUP, HTMLElements.BDO, HTMLElements.BR};
		
		public void createTagNodes(Node node, TagNode parent, TagTree tagTree)
		{
			// jika node DOM bertipe ELEMENT
			if (node.getNodeType() == Node.ELEMENT_NODE)
			{
				// dapatkan tagCode-nya (representasi tag dalam short)
				short tagCode = HTMLElements.getElement(node.getNodeName()).code;

				// jika tagCode termasuk dalam daftar formatting tags
				if ( arrayContains(formattingTags, tagCode) )
				{
					// khusus untuk tag BR hanya menambahkan tag <BR /> ke innerText parent TagNode-nya
					if ( tagCode == HTMLElements.BR)
					{
						parent.appendInnerText( "<BR />" );
					}
					else
					{
						// append teks di dalam tagCode (beserta tagCode itu sendiri) ke dalam innerText parent-nya
						parent.appendInnerText( String.format("<%s>%s</%s>", HTMLElements.getElement(tagCode).name, node.getTextContent(), HTMLElements.getElement(tagCode).name) );
					}
				}
				// jika tagCode tidak termasuk dalam daftar tag yang diabaikan
				else if ( !arrayContains(ignoredTags, tagCode) )
				{
					// salin node DOM menjadi TagNode (tagCode-nya)
					TagNode tagNode = new TagNode();
					tagNode.setTagElement(tagCode);
					// set parent dari TagNode yang baru dibuat
					tagNode.setParent(parent);
					// tambahkan ke dalam TagTree
					tagTree.addTagNodeAtLevel(tagNode);
					
					// jika tagCode merupakan tag IMG
					if ( tagCode == HTMLElements.IMG )
					{
						// dapatkan nilai atribut src-nya
						NamedNodeMap attributesMap = node.getAttributes();
						String imgURI = attributesMap.getNamedItem("src").getNodeValue();
						Matcher absoluteURIMatcher = absoluteURIPattern.matcher( imgURI );
						
						// jika URI pada atribut src bukan merupakan URI absolut (URI relatif)
						if ( ! absoluteURIMatcher.matches() )
						{
							// tambahkan baseURI sehingga menjadi URI absolut
							imgURI = baseURI + imgURI;
						}
						
						// tambahkan tag IMG dengan src-nya sebagai teks HTML pada TagNode parent-nya
						String imgText = String.format("<img src=\"%s\" />", imgURI);
						tagNode.appendInnerText( imgText );
					}
					// jika tagCode merupakan tag A
					else if( tagCode == HTMLElements.A )
					{
						// append teks di dalam tag A ke innerText milik parent
						parent.appendInnerText( node.getTextContent() );
						// dapatkan map atribut dari tag A ini
						NamedNodeMap attributesMap = node.getAttributes();
						
						// jika tag A ini memiliki atrbiut href
						if ( attributesMap.getNamedItem("href") != null)
						{
							// dapatkan nilai atribut href-nya
							String linkURI = attributesMap.getNamedItem("href").getNodeValue();
							Matcher absoluteURIMatcher = absoluteURIPattern.matcher( linkURI );
						
							// jika nilai atribut href bukan merupakan URI absolut
							if ( ! absoluteURIMatcher.matches() )
							{
								// tambahkan baseURI sehingga menjadi URI absolut
								linkURI = baseURI + linkURI;
							}
						
							// tambahkan tag A dengan href-nya dan teks Link sebagai teks HTML pada TagNode parent-nya
							String linkText = String.format("<a href=\"%s\">Link&lt;&lt;</a>", linkURI);
							tagNode.appendInnerText( linkText );
						}
					}
					
					// lakukan secara rekursif penyalinan Node DOM menjadi DOM pada anak2 dari node ini (jika memiliki anak)
					Node child = node.getFirstChild();
					
					while(child != null)
					{
						createTagNodes(child, tagNode, tagTree);
						child = child.getNextSibling();
					}
				}
			}
			// jika node DOM bertipe TEXT
			else if (node.getNodeType() == Node.TEXT_NODE)
			{
				Matcher filterMatcher = filterPattern.matcher( node.getNodeValue() );
				
				// kalau Text node ini hanya berisi string yang tidak terbaca, maka tidak usah 
				// disimpan sebagai innerText pada node parent-nya
				if ( ! filterMatcher.matches() )
				{
					// jika mengandung teks yang bisa terbaca, maka di-append pada innerText node parent-nya
					parent.appendInnerText(node.getNodeValue());
				}
			}
		}
	}

	@Override
	public TagTree buildTagTree(InputSource inputSource,
			boolean ignoreFormattingTags) throws IOException, SAXException {
		// TODO Auto-generated method stub
		return null;
	}
}
