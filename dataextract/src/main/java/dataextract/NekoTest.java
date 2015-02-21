package dataextract;

import java.util.ArrayList;
import java.util.List;

import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.nodes.RemarkNode;
import org.htmlparser.nodes.TextNode;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.tags.Div;
import org.htmlparser.tags.ImageTag;
import org.htmlparser.tags.MetaTag;
import org.htmlparser.tags.ScriptTag;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;

public class NekoTest {
	
	public static void printNode(NodeList nodelist,List<Keyvalue> listKeyvalue) {  
		
	    for (int i = 0; nodelist != null && i < nodelist.size(); i++) {  
	        Node node = nodelist.elementAt(i);  
	   
                if (node instanceof ImageTag) {
    	        	ImageTag ImageTag = (ImageTag) node;
    	        	Keyvalue keyvalue =new Keyvalue();
    	        	keyvalue.setKey("image");
    	        	keyvalue.setValue(ImageTag.getImageURL());
    	        	listKeyvalue.add(keyvalue);
//    	        	System.out.println("________________");
//    	        	System.out.println(ImageTag.getImageURL());
//                    System.out.println("________________");          
                    
            }  else  if (node instanceof ScriptTag){
            	ScriptTag scriptTag= (ScriptTag) node;
	        	Keyvalue keyvalue =new Keyvalue();
	        	keyvalue.setKey("scriptTag");
	        	keyvalue.setValue(scriptTag.getScriptCode());
	        	listKeyvalue.add(keyvalue);
//	            System.out.println("1________________");   
//	         System.out.println("@@"+linkTag.getLink());
//	         System.out.println("2________________"); 
	        }else  if (node instanceof LinkTag){
	        	LinkTag linkTag= (LinkTag) node;
	        	Keyvalue keyvalue =new Keyvalue();
	        	keyvalue.setKey("link");
	        	keyvalue.setValue(linkTag.getLink());
	        	listKeyvalue.add(keyvalue);
//	            System.out.println("1________________");   
//	         System.out.println("@@"+linkTag.getLink()); //Remark 
//	         System.out.println("2________________"); 
	        }else  if (node instanceof RemarkNode){
//	        	LinkTag linkTag= (LinkTag) node;
//	        	Keyvalue keyvalue =new Keyvalue();
//	        	keyvalue.setKey("link");
//	        	keyvalue.setValue(linkTag.getLink());
//	        	listKeyvalue.add(keyvalue);
//	            System.out.println("1________________");   
//	         System.out.println("@@"+linkTag.getLink()); //Remark 
//	         System.out.println("2________________"); 
	        }else{
//	        	Keyvalue keyvalue =new Keyvalue();
//	        	keyvalue.setKey(node.getClass().getSimpleName());
//	        	keyvalue.setValue(node.toPlainTextString());
//	        	listKeyvalue.add(keyvalue);
	        	if (node instanceof Div){
	        	
	        	}else{
	        		if (node instanceof TextNode){
//	        			System.out.print(node.getClass().getSimpleName()+"@@@@@");
//	        			if (!(node.toPlainTextString().trim().equals(""))){
//	        				  System.out.print("\n"+node.toPlainTextString());
//	        			}
	    	   	      
	        		}
	        	   
	        }
	                   
	        }
	        
	        
	        printNode(node.getChildren(),listKeyvalue);  
	    }  
	}  
	  
	public static void main(String[] args) {  
		
//		String html ="<div class='item w-bg'><div class='aside'><a class='s-link' target='_blank' href=\"http://pinpaijie.jd.com/quan.html\"></a>+" +
//				"<h3>优惠券</h3>"+
//				"<div class='s-name'><a target='_blank' href=\"http://pinpaijie.jd.com/quan.html\" clstag=\"homepage|keycount|home2013|40f1\">特美刻送豪礼</a>"+
//                        "</div><div class='s-ext'><b>先领券再满减</b></div><ul class='s-hotword'></ul></div><a class='s-img' target='_blank' href=\"http://pinpaijie.jd.com/quan.html\">"+
//                        "<img class='err-product' width='305px' height='190px' data-img='2' alt=\"\" clstag=\"homepage|keycount|home2013|40f2\" src=\"http://img10.360buyimg.com/vclist/jfs/t730/353/752299493/47879/f7e6e79a/54dc8365N63bc4616.jpg\">"+"</a></div>";
	    try {  
//	    	 Parser parser = new Parser(html);
//	        Parser parser = new Parser("http://en.wikipedia.org/wiki/List_of_blogs");  
	    	List<Keyvalue> listKeyvalue = new ArrayList<Keyvalue>();
	    	   Parser parser = new Parser("http://www.jd.com"); 
	        NodeList nodelist = parser.parse(null);  

           

	        printNode(nodelist,listKeyvalue);  
	        for (Keyvalue keyvalue:listKeyvalue){
//	        	System.out.println("key:"+keyvalue.getKey()+"::  value:"+keyvalue.getValue());
	        }
	          
//	        NodeFilter filter = new TagNameFilter("tr");  
//	        NodeList list = nodelist.extractAllNodesThatMatch(filter, true);  
//	        printNode(list);  
	  
	    } catch (ParserException e) {  
	        e.printStackTrace();  
	    }  
	  
	}  

}
