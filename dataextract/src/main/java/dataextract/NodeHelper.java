package dataextract;  
  
import java.io.BufferedReader;  
import java.io.File;  
import java.io.FileInputStream;  
import java.io.FileReader;
import java.io.InputStreamReader;  
  

import javax.xml.transform.TransformerException;  
  

import org.apache.xpath.XPathAPI;  
import org.cyberneko.html.parsers.DOMParser;  
import org.w3c.dom.Document;  
import org.w3c.dom.NamedNodeMap;  
import org.w3c.dom.Node;  
import org.w3c.dom.NodeList;  
import org.xml.sax.InputSource;  
  
/*import com.isa.bbs.parser.common.Block;  
import com.isa.bbs.spider.common.SpiderGlobal;  */
  
  
public class NodeHelper {  
  
    public static final int FIND_SUB = 0; // 找子节点  
    public static final int FIND_SIB = 1; // 找同级节点  
    public static final int FIND_END = 2; // 结束  
      
    public static final String SEPARTOR = System.getProperty ("line.separator");  
      
    /** 
     * 得 到该nodelist下的所有文章内容。 
     * @param list 
     * @param sb 
     */  
    public static String getNodeListPlainText(NodeList list){  
        StringBuffer sb = new StringBuffer("");  
        printNodeList(list, sb, 1);  
        return sb.toString();  
    }  
      
    /** 
     * 获 得该节点下指定的xpath匹配到的node节点 
     * @param node 
     * @param sequence 
     * @return 
     */  
    public static NodeList getAllTargetNodeList(Node node, String sequence){  
        if(node == null){  
            return null;  
        }         
        NodeList nodelist = null;  
        try {  
            nodelist = XPathAPI.selectNodeList(node, sequence);  
        } catch (TransformerException e) {  
            // TODO Auto-generated catch block  
            e.printStackTrace();  
        }  
        return nodelist;  
    }  
      
    /** 
     * 获 得该nodelist下所有的文字内容，去掉A 和 style标签下的内容 
     * @param list 
     * @return 
     */  
    public static String getNodeListPlainTextExceptTags(NodeList list){  
        StringBuffer sb = new StringBuffer("");  
        printNodeList(list, sb, 0);  
        return sb.toString();  
    }  
      
    /** 
     * 得 到该nodelist下的html代码。 
     * @param list 
     * @return 
     */  
    public static String getNodeListHTML(NodeList list){  
        StringBuffer sb = new StringBuffer("");  
        printNodeListHTML(list, sb);  
        return sb.toString();  
    }  
      
    /** 
     * 得 到该node下的html内容 
     * @param node 
     * @param sb 
     */  
    public static String getNodeHTML(Node node){  
        StringBuffer sb = new StringBuffer("");  
        printNodeHTML(node, sb);  
        return sb.toString();  
    }  
      
    /** 
     * 获 得该node下的文字内容，包括A 或者 style标签下的内容 
     * @param node 
     * @return 
     */   
    public static String getNodePlainText(Node node){  
        StringBuffer sb = new StringBuffer("");  
        printNodeValue(node, sb, 1);  
        return sb.toString();  
    }  
      
    /** 
     * 获 得该node下的文字内容，不包括A 或者 style标签下的内容 
     * @param node 
     * @return 
     */  
    public static String getNodePlainTextExceptTags(Node node){  
        StringBuffer sb = new StringBuffer("");  
        printNodeValue(node, sb, 0);  
        return sb.toString();  
    }  
      
    /** 
     * 得 到nodelist中的文本 
     * @param list 
     * @param sb 
     * @param flag 0 除去A 或者 style 标签里面的字符串 1 保持原样 
     */  
    private static void printNodeList(NodeList list, StringBuffer sb, int flag){  
        if(list == null){  
            return;  
        }  
        for (int i = 0; i < list.getLength(); i++) {  
            Node node = list.item(i);  
            if(flag == 0){  
                if(node.getNodeName().equals("A") || node.getNodeName().equals("STYLE") ){  
                    continue;  
                }  
            }  
            if (node.hasChildNodes()) { // 如果该节点含有子节点，表明它是一个Element。  
                printNodeList(node.getChildNodes(), sb, flag); // 递归调用，操作该Element的子节点。  
            } else if (node.getNodeType() == Node.TEXT_NODE) { // 检查这个非Element节点是否是文本  
                String text =  node.getNodeValue().trim();  
                if(!text.startsWith("<!--")){  
                    sb.append(text);  
                }  
            } else {  
                continue;  
            }  
        }  
    }  
  
    /** 
     * 得 到该节点的完整html表现形式 
     * @param node 
     * @param sb 
     */  
    private static void printNodeHTML(Node node, StringBuffer sb){  
        if(node == null){  
            return ;  
        }  
        if(node.hasChildNodes()){  
            sb.append("<"+node.getNodeName());  
            NamedNodeMap attrs = node.getAttributes();  
            for(int j=0;j<attrs.getLength();j++){  
                sb.append(" "+attrs.item(j).getNodeName()+"=\""+attrs.item(j).getNodeValue()+"\"");  
            }  
            sb.append(">");  
            for(int i=0;i<node.getChildNodes().getLength();i++){  
                printNodeHTML(node.getChildNodes().item(i), sb);  
            }                             
            sb.append("</"+node.getNodeName()+">");  
        }else if(node.getNodeType() == Node.TEXT_NODE){  
            sb.append(node.getNodeValue());  
        }else if(node.getNodeType() == Node.ELEMENT_NODE){  
            sb.append("<"+node.getNodeName());  
            NamedNodeMap attrs = node.getAttributes();  
            for(int j=0;j<attrs.getLength();j++){  
                sb.append(" "+attrs.item(j).getNodeName()+"=\""+attrs.item(j).getNodeValue()+"\"");  
            }  
            sb.append(">");                
            sb.append("</"+node.getNodeName()+">");  
        }  
    }  
              
    /** 
     *  
     * @param node 
     * @param sb 
     * @param flag 0 表示除去A、STYLE标签 1 表示留下A、STYLE标签 
     */  
    private static void printNodeValue(Node node, StringBuffer sb, int flag){  
        if(node == null){  
            return;  
        }  
        if(node.hasChildNodes()){  
            if(flag == 0){  
                for(int i=0;i<node.getChildNodes().getLength();i++){  
                    if(node.getNodeName().equals("A") || node.getNodeName().equals("STYLE")){  
                        continue;  
                    }  
                    printNodeValue(node.getChildNodes().item(i), sb, flag);  
                }  
            }else{  
                for(int i=0;i<node.getChildNodes().getLength();i++){  
                    printNodeValue(node.getChildNodes().item(i), sb, flag);  
                }  
            }  
        }else if(node.getNodeType() == Node.TEXT_NODE){  
            String text =  node.getNodeValue().trim();  
            if(!text.startsWith("<!--")){  
                sb.append(text);  
            }             
        }  
    }  
          
    /** 
     * 得 到该nodelist的完整的html内容 
     * @param list 
     * @param sb 
     */  
    private static void printNodeListHTML(NodeList list, StringBuffer sb){  
        if(list == null){  
            return;  
        }  
        for (int i = 0; i < list.getLength(); i++) {  
            Node node = list.item(i);  
            if (node.hasChildNodes()) { // 如果该节点含有子节点，表明它是一个Element。  
                sb.append("<"+node.getNodeName());  
                NamedNodeMap attrs = node.getAttributes();  
                for(int j=0;j<attrs.getLength();j++){  
                    sb.append(" "+attrs.item(j).getNodeName()+"=\""+attrs.item(j).getNodeValue()+"\"");  
                }  
                sb.append(">");  
                printNodeListHTML(node.getChildNodes(), sb);    // 递归调用，操作该Element的子节点。           
                sb.append("</"+node.getNodeName()+">");  
            }  else if(node.getNodeType() == Node.TEXT_NODE){   //如果该节点类型是一个文本类型的节点.  
                sb.append(node.getNodeValue());  
            } else if(node.getNodeType() == Node.ELEMENT_NODE){  
                sb.append("<"+node.getNodeName());  
                NamedNodeMap attrs = node.getAttributes();  
                for(int j=0;j<attrs.getLength();j++){  
                    sb.append(" "+attrs.item(j).getNodeName()+"=\""+attrs.item(j).getNodeValue()+"\"");  
                }  
                sb.append(">");                
                sb.append("</"+node.getNodeName()+">");  
            }  
        }  
    }     
      
    /** 
     * 查 找给定的文件中是否含有特定的唯一性标志块。如果有，返回一个不为null的节点集合。 
     * @param block 
     * @param fileName 
     * @return 
     */
    public static void main(String[] args) throws Exception{  
    	NodeList l=getTargetNodeList("E:\\doc\\jsoup 和nekohtml，htmlparser解析html - yysmid的博客 - ITeye技术网站.html");
		  
    	System.out.println(l.getLength());  
		   
		}  
    static String productsXpath = "/HTML/BODY/DIV[2]/DIV[4]/DIV[2]/DIV/DIV[3]/UL[@class]/LI[9]";
    public static NodeList getTargetNodeList(/*Block block,*/ String fileName){  
        DOMParser parser = null;  
        NodeList list = null;  
        try {  
            String charset = "gb2312";  
            parser = new DOMParser();  
            parser.setProperty(  
                    "http://cyberneko.org/html/properties/default-encoding",  
                    charset);  
            parser.setFeature("http://xml.org/sax/features/namespaces", false);  
            File file = new File(fileName);  
            BufferedReader in = new BufferedReader(new InputStreamReader(  
                    new FileInputStream(file)  
                    ));  
            parser.parse(new InputSource(in));  
            in.close();  
            Document doc = parser.getDocument();  
            if(doc == null){  
                return null;  
            }  
            list = XPathAPI.selectNodeList(doc, productsXpath);  
        }  catch (Exception e){  
            System.out.println("no filematch !" +e.getMessage());  
            return null;  
        }  
        return list;  
    }  
}  