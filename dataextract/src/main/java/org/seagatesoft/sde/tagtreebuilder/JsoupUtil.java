/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.seagatesoft.sde.tagtreebuilder;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.*;

/**
 *
 * @author ray
 */
public class JsoupUtil {

    Logger l = Logger.getLogger(JsoupUtil.class);
    private static JsoupUtil ins = new JsoupUtil();
    public static JsoupUtil getInstance() {
        return ins;
    }

    public Document parse(String html) {
        return Jsoup.parse(html);
    }

    public org.w3c.dom.Document convert(Document doc) {
        W3CDocumentConvertor convertor = new W3CDocumentConvertor();
        convertor.convert(doc);
        return convertor.doc;
    }
    
    private class W3CDocumentConvertor {

        String doctype = "html";
        org.w3c.dom.Document doc = null;

        void convert(Document document) {
            if (null != document) {
                doc = createDocument();
                convert(document, doc);
            }
        }

        void convert(Node node, org.w3c.dom.Node n) {
            List<Node> children = node.childNodes();
            if (null != children) {
                for (int i = 0; i < children.size(); i++) {
                    Node child = children.get(i);
                    if (child instanceof DocumentType) {
                        this.doctype = ((DocumentType) child).toString();
//                        doc.getDoctype().setNodeValue(doctype);
                    } else if (child instanceof Element) {
                        try {
                            String tagName = child.nodeName();
                            tagName = tagName.replaceAll("\"", "");//htmlæ ‡ç­¾ä¸­ä¸èƒ½åŒ…å«å¼•å·ã€‚
                            org.w3c.dom.Element ele = doc.createElement(tagName);
                            //æ·»åŠ å±žæ€§
                            Attributes attrs = child.attributes();
                            if (null != attrs) {
                                List<Attribute> list = attrs.asList();
                                for (Attribute attr : list) {
                                    String key = attr.getKey();
                                    String value = attr.getValue();
                                    try {
                                        org.w3c.dom.Attr a = doc.createAttribute(key);
                                        a.setValue(value);
                                        ele.setAttributeNode(a);
                                    } catch (Exception ex) {
                                        l.error("bad key-value!!! ----> the key[" + key + "]   &   the value[" + value + "]");
                                    }
                                }
                            }
                            n.appendChild(ele);
                            convert(child, ele);
                        } catch (org.w3c.dom.DOMException ex) {
                            l.error(child.nodeName());
                            l.error(ex.getMessage(), ex);
                        }
                    } else if (child instanceof TextNode) {
                        TextNode t = (TextNode) child;
                        org.w3c.dom.Text text = doc.createTextNode(t.text());
                        n.appendChild(text);
                    }
                }
            }
        }

        org.w3c.dom.Document createDocument() {
            DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
            domFactory.setNamespaceAware(true);
            DocumentBuilder builder = null;
            try {
                builder = domFactory.newDocumentBuilder();
            } catch (ParserConfigurationException ex) {
                ex.printStackTrace();
            }
            org.w3c.dom.Document doc = builder.newDocument();
            return doc;
        }
    }
}