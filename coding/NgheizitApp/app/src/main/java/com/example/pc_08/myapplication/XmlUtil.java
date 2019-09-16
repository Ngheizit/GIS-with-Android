package com.example.pc_08.myapplication;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

import java.io.ByteArrayInputStream;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Created by Administrator on 2017/11/22.
 */

public class XmlUtil {
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static HashMap UnPackageXml(String xmlStr) {
        HashMap rst = new HashMap();
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            ByteArrayInputStream stream = new ByteArrayInputStream(xmlStr.getBytes("UTF-8"));
            Document doc = builder.parse(stream);
            Element root = doc.getDocumentElement();
            NodeList children = root.getChildNodes();
            getAllLeafNode(null, children, rst);
        } catch (Exception e) {
            e.printStackTrace();
            rst = null;
        }
// if (rst.isEmpty())
// rst = null;
        return rst;


    }


    private static void getAllLeafNode(String parentNodeName, NodeList nodeList, HashMap<String, String> rst) {


        for (int i = 0; i < nodeList.getLength(); i++) {
            Node child = nodeList.item(i);


            if (child.getNodeType() == Node.ELEMENT_NODE) {
                Element childElement = (Element) child;
                String name = childElement.getTagName().trim();
                String pathName = name;
                if (parentNodeName != null && !parentNodeName.equals("")) {
                    pathName = parentNodeName + "." + name;
                }
                pathName = getPathNodeNameFromList(rst, pathName, 0);
                if (isLeaf(childElement)) {
                    Text textnode = (Text) childElement.getFirstChild();
                    if (textnode != null) {
                        String text = textnode.getData().trim();
                        if (text != null && !text.equals("")) {
                            rst.put(pathName, text);
                        }
                    }
                } else {
                    NodeList childList = childElement.getChildNodes();
                    getAllLeafNode(pathName, childList, rst);
                }
            }
        }
    }


    private static String getPathNodeNameFromList(HashMap<String, String> rst, String name, int count) {
        if (rst.containsKey(name)) {
            String nn = name;
            if (nn.indexOf("[") > 0) {
                nn = nn.substring(0, nn.indexOf("["));
            }
            nn = nn + "[" + count + "]";
            return getPathNodeNameFromList(rst, nn, ++count);
        } else {
            return name;
        }
    }


    private static boolean isLeaf(Element ele) {
        if (ele.getChildNodes().getLength() == 0) {
            return true;
        }
        if (ele.getChildNodes().getLength() == 1) {
            Node child = ele.getChildNodes().item(0);
            if (child.getNodeType() != Node.ELEMENT_NODE) {
                return true;
            }
        }
        return false;
    }

}
