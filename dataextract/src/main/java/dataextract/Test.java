package dataextract;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;



public class Test {
	/*public static void main(String[] args) throws Exception{
		HtmlFetcher fetcher = new HtmlFetcher();
		 // set cache. e.g. take the map implementation from google collections:
		 // fetcher.setCache(new MapMaker().concurrencyLevel(20).maximumSize(count).
		 //    expireAfterWrite(minutes, TimeUnit.MINUTES).makeMap();
		String articleUrl="https://github.com/karussell/snacktory/blob/master/test_data/1.html";
		 JResult res = fetcher.fetchAndExtract(articleUrl, 500000, true);
		 String text = res.getText(); 
		 String title = res.getTitle(); 
		 String imageUrl = res.getImageUrl();
		 System.out.println(text);
		 System.out.println(title);
		 System.out.println(imageUrl);
	}*/
	public static void main(String[] args) {
	    try {
	    	
	    	Document doc = Jsoup.connect("http://en.wikipedia.org/wiki/List_of_blogs").get();
	        Elements trs = doc.select("table.wikitable tr");

	        //remove header row
	        trs.remove(0);

	        for (Element tr : trs) {
	            Elements tds = tr.getElementsByTag("td");
	            Element td = tds.first();
	            System.out.println("Blog: " + td.text());
	        }
	        
	        
	        Document doc1 = Jsoup.connect("http://en.wikipedia.org/wiki/List_of_blogs").get();
	        for (Element table : doc1.select("table")) {
	        	System.out.println("table\n\n\n\n");
	        	/*for (Element row : table.select("tr")) {
	        		System.out.print(row.text()+"              ");
	        	}*/
	        	
	        	System.out.println();
	            for (Element row : table.select("tr")) {
	                Elements tds = row.select("td");
	                try{
	                	 System.out.println();
	                	 for (int i =0;i<tds.size();i++) {
	                		 System.out.print(" || "+tds.get(i).text());
	                	 }
	                	   
	                }
	                catch (Exception e) {
	        	     //   e.printStackTrace();
	        	    }
	            }
	        }
	    
	        URL url = new URL("http://en.wikipedia.org/wiki/List_of_blogs");
	        Document doc2 = Jsoup.parse(url,3*1000);

	        String text = doc2.body().text();
	        System.out.println(text);
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}
}
