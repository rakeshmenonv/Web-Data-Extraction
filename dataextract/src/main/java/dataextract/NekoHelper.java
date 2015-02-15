package dataextract;  
  
import java.io.BufferedReader;  
import java.io.File;  
import java.io.FileInputStream;  
import java.io.IOException;  
import java.io.InputStream;  
import java.io.InputStreamReader;  
import java.io.StringReader;  
import java.net.HttpURLConnection;  
import java.net.URL;  
  


import javax.xml.transform.TransformerException;  
  


import org.apache.xpath.XPathAPI;  
import org.cyberneko.html.parsers.DOMParser;  
import org.w3c.dom.Document;  
import org.w3c.dom.NodeList;  
import org.xml.sax.InputSource;  
  
/*import com.isa.bbs.parser.common.Block;  
import com.isa.bbs.parser.common.Queue;  
import com.isa.bbs.parser.common.SiteContext;  
import com.isa.bbs.spider.common.SpiderGlobal;  
 */ 
public class NekoHelper {  
          
    /** 
     * 通 过给定的xpath、文件路径、是否是网络文件来获得符合该xpath路径下的节点集合 
     * @param block 
     * @param url 
     * @param isurl 
     * @return 
     */  
	static String productsXpath = "/HTML/BODY/DIV[2]/DIV[4]/DIV[2]/DIV/DIV[3]/UL[@class]/LI[9]";
    public static NodeList getTargetNodeList(/*Block block,*/ String url , boolean isurl){  
        /*if(block == null){  
            return null;  
        } */ 
        DOMParser parser = getParser(url, isurl);  
        Document doc = parser.getDocument();  
        if(doc == null){  
            return null;  
        }  
        NodeList list = null;  
        try {             
            list = XPathAPI.selectNodeList(doc, productsXpath);  
        } catch (TransformerException e) {  
            // TODO Auto-generated catch block  
            e.printStackTrace();  
        }  
        return list;  
    }  
      
    /*public static Block getBlock(String blockname){  
        Block block = null;  
        if(Queue.siteQueue!=null) {  
            SiteContext context = Queue.siteQueue.get(0);  
            block = (Block)context.getAttribute(blockname);  
        }         
        return block;  
    }  */
      
    /** 
     * 给 定xpath路径，在给定的content下获得指定的路径下的节点集合 
     * @param content 
     * @param xpath 
     * @return 
     */  
    public static NodeList getContentByXpath(String content, String xpath){  
        DOMParser parser = getStrParser(content);  
          
        Document doc = parser.getDocument();  
        if(doc == null){  
            return null;  
        }  
        NodeList list = null;  
        try{  
            list = XPathAPI.selectNodeList(doc, xpath);  
        }catch(TransformerException e){  
            e.printStackTrace();  
        }  
        return list;  
    }  
      
    /** 
     * 获 得指定的本地文件的解析器 DOMParser 
     * @param str 
     * @return 
     */  
    static String charset = "gb2312";
    private static DOMParser getStrParser(String str){  
        DOMParser parser = null;  
        try{  
            parser = new DOMParser();  
            parser.setProperty("http://cyberneko.org/html/properties/default-encoding",  
            		charset);  
            parser.setFeature("http://xml.org/sax/features/namespaces", false);           
            parser.parse(new InputSource(new StringReader(str)));  
        }catch(Exception e){  
            System.out.println("no filematch !" + e.getMessage());  
        }  
        return parser;  
    }  
      
    /** 
     * 获 得指定的本地文件或者网络上指定的url下的解析器 DOMParser 
     * @param url 
     * @param isurl true 本地文件  false 网络文件 
     * @return 
     */  
    public static DOMParser getParser(String url, boolean isurl){  
        DOMParser parser = null;  
        try{  
            parser = new DOMParser();  
            parser.setProperty("http://cyberneko.org/html/properties/default-encoding",  
            		charset);  
            parser.setFeature("http://xml.org/sax/features/namespaces", false);  
            BufferedReader in = null;  
            if(isurl){  
                File file = new File(url);            
                in = new BufferedReader(new InputStreamReader(  
                        new FileInputStream(file),charset));  
            }else{  
                HttpURLConnection conn = (HttpURLConnection)new URL(url).openConnection();  
                conn.setRequestProperty("user-agent","mozilla/4.0 (compatible; msie 6.0; windows 2000)");  
                conn.setConnectTimeout(30000);   
                conn.setReadTimeout(30000);   
                conn.connect();  
                in = new BufferedReader(new InputStreamReader(  
                        conn.getInputStream(),charset));  
            }  
            parser.parse(new InputSource(in));  
            in.close();  
        }catch(Exception e){  
            System.out.println("no filematch !" + e.getMessage());  
        }  
        return parser;  
    }   
              
    /** 
     * 得 到该url下的文章内容 
     * @param url 
     * @return 
     */  
    public static String getWebContent(String url) {  
        String s = null;  
        String src = url;  
        String pageEncoding = charset;  
        try {  
            // 从url打开stream  
            InputStream in = null;  
            HttpURLConnection conn = (HttpURLConnection)new URL(src).openConnection();  
            conn.setRequestProperty("user-agent","mozilla/4.0 (compatible; msie 6.0; windows 2000)");  
            conn.setConnectTimeout(30000);   
            conn.setReadTimeout(30000);   
            conn.connect();  
            in = conn.getInputStream();  
            // 开始读取内容正文。  
            BufferedReader br = new BufferedReader(new InputStreamReader(in, pageEncoding));  
            StringBuffer sb = new StringBuffer();  
            char[] charBuf = new char[2048];  
            int len = br.read(charBuf);  
            while(len != -1) {  
                sb.append(charBuf, 0, len);  
                len = br.read(charBuf);  
            }  
            br.close();  
            s = sb.toString();  
        }catch(IOException ex) {  
            System.err.println(src);  
            System.out.println("Web页面读取失败！==IO异常");  
            return null;  
        }catch (Exception e) {  
            // TODO: handle exception  
            System.out.println("Web页面读取失败！==普通异常");  
            return null;  
        }  
        return s;  
    }  
      
}  
