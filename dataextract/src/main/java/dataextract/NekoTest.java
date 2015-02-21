package dataextract;

import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;

public class NekoTest {
	public static void printNode(NodeList nodelist) {
	    for (int i = 0; nodelist != null && i < nodelist.size(); i++) {
	        Node node = nodelist.elementAt(i);
	        System.out.print(node.getText());

	        printNode(node.getChildren());
	    }
	}

	public static void main(String[] args) {
	    try {
	        Parser parser = new Parser("http://en.wikipedia.org/wiki/List_of_blogs");
	        NodeList nodelist = parser.parse(null);
	        //printNode(nodelist);

	        NodeFilter filter = new TagNameFilter("tr");
	        NodeList list = nodelist.extractAllNodesThatMatch(filter, true);
	        printNode(list);

	    } catch (ParserException e) {
	        e.printStackTrace();
	    }

	}

}
