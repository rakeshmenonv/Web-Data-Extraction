package dataextract;

import java.io.BufferedReader;
import java.io.FileReader;

import org.cyberneko.html.parsers.DOMParser;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;



public class Test1 {

	public static String TextExtractor(Node root){  
		   //若是文本节点的话，直接返回  
		   if (root.getNodeType() == Node.TEXT_NODE) {  
		    return root.getNodeValue().trim();  
		   }  
		   if(root.getNodeType() == Node.ELEMENT_NODE) {  
		    Element elmt = (Element) root;  
		    //抛弃脚本  
		    if (elmt.getTagName().equals("STYLE")  
		      || elmt.getTagName().equals("SCRIPT"))  
		     return "";  
		     
		    NodeList children = elmt.getChildNodes();  
		    StringBuilder text = new StringBuilder();  
		    for (int i = 0; i < children.getLength(); i++) {  
		     text.append(TextExtractor(children.item(i)));  
		    }  
		    return text.toString();  
		   }  
		   //对其它类型的节点，返回空值  
		   return "";  
		}  
		public static void main(String[] args) throws Exception{  
		   //生成html parser  
		   DOMParser parser = new DOMParser();  
		   //设置网页的默认编码  
		   parser.setProperty(  
		     "http://cyberneko.org/html/properties/default-encoding",  
		     "gb18030");  
		   //input file  
		   BufferedReader in = new BufferedReader(new FileReader("E:\\doc\\jsoup 和nekohtml，htmlparser解析html - yysmid的博客 - ITeye技术网站.html"));  
		   parser.parse(new InputSource(in));  
		   Document doc = parser.getDocument();  
		   //获得body节点，以此为根，计算其文本内容  
		   Node body = doc.getElementsByTagName("BODY").item(0);  
		   System.out.println(TextExtractor(body));  
		}  

}
