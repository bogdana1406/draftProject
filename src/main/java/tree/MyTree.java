package tree;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.*;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

public class MyTree {

    private MyNode root;

    public void setRoot(MyNode root) {
        this.root = root;
    }

    public MyNode getRoot() {
        return root;
    }

//    // распарсить все дерево
//    public void parseXML(String path) throws ParserConfigurationException, IOException, SAXException {
//
//        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
//        DocumentBuilder db = dbf.newDocumentBuilder();
//
//        Document document = db.parse(path);
//        document.getDocumentElement().normalize();
//
//        // получили корневой элмент (books)
//        Element document_root = document.getDocumentElement();
////        System.out.println(document_root .getNodeName());
//
//        // root присваиваем значение выполнения функции parseNode (root = parseNode(books...))
//        setRoot(parseNodeXML(document_root));
////        root = parseNodeXML(document_root);
//    }
//
//    // распарсить узел
//    private MyNode parseNodeXML(Node element) {
//
//        //System.out.println(root.getNodeName() + " = " + root.getNodeValue());
//        if (element.getNodeType() != element.ELEMENT_NODE) {
//            return null;
//        }
//
//        NamedNodeMap attrArr = element.getAttributes();
//        MyNode parent = new MyNode();
//        parent.setName(element.getNodeName());
////        System.out.println("nodeName = " + parent.getName());
//        parent.setText(element.getTextContent().trim());
////        System.out.println("nodeText = " + parent.getText());
//        NodeList children = element.getChildNodes();
//
//        for (int j = 0; j < attrArr.getLength(); j++) {
//            Node attr = attrArr.item(j);
////            System.out.println("nodeName = " + parent.getName() + "nodeValue = " + parent.getText());
////            System.out.println("Attribute: " + attr.getNodeName() +
////                    " = " + attr.getNodeValue());
//            parent.addAttributes(attr.getNodeName(), attr.getNodeValue());
//        }
//        for (int i = 0; i < children.getLength(); i++) {
//            Node child = children.item(i);
//            MyNode node = parseNodeXML(child);
//            if (node != null) {
//                parent.addChild(node);
//            }
//        }
//        return parent;
//    }
//
//
//    public String saveTreeToXML(MyNode root, String outputURL) throws ParserConfigurationException, IOException, TransformerException {
//        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
//        dbf.setIgnoringElementContentWhitespace(true);
//        DocumentBuilder db = dbf.newDocumentBuilder();
//        Document document = db.newDocument();
//        Element startRoot = document.createElement(root.getName());
//        setAttributes (root, startRoot);
//
//        if (root.hasChildren()) {
//            List<MyNode> children = root.getChildren();
//            for (MyNode node: children) {
//                Element element = saveNodeToXML(node, document);
//                startRoot.appendChild(element);
//            }
//        }
//        document.appendChild(startRoot);
//        DOMSource source = new DOMSource(document);
//        StreamResult result = new StreamResult(new File(outputURL));
//        TransformerFactory tf = TransformerFactory.newInstance();
//        Transformer transformer = tf.newTransformer();
//        transformer.transform(source, result);
//        String fileName = new File(outputURL).getCanonicalPath();
//        System.out.println("FileName " + new File(outputURL).getCanonicalPath());
//        return fileName;
//    }
//
//    // SHORT VERSION
//    private Element saveNodeToXML(MyNode node, Document document) {
//
//        Element root = document.createElement(node.getName());
//        setAttributes (node, root);
//        if (!node.hasChildren()) {
//            root.setTextContent(node.getText());
//        }
//        if (node.hasChildren()) {
//
//            List<MyNode> children = node.getChildren();
//
//            for (MyNode child: children) {
//
//                Element newRoot = saveNodeToXML(child, document);
//                root.appendChild(newRoot);
//
//            }
//        }
//        return root;
//    }
//
//
//    private void setAttributes (MyNode node, Element element) {
//        if (node.hasAttributes()) {
//            Map<String, String> attributes = node.getAttributes();
//            for (String key: attributes.keySet()) {
//                element.setAttribute(key, attributes.get(key));
//            }
//        }
//    }



    public void printTree(MyNode root, String appender) {
        System.out.println(appender + root.getName());
        root.getChildren().forEach(each ->  printTree(each, appender + appender));
    }

    public void printTreeStructure(MyNode root) {
        System.out.println(root.getName());
//        System.out.println(root.getText());
        if (root.getAttributes() != null) {
            Map<String, String> attributes = root.getAttributes();
            for (String attr: attributes.keySet()) {
                System.out.print(attr + " = " + attributes.get(attr) + "  ");
            }
            System.out.println();

        }
        List<MyNode> children = root.getChildren();
        for (MyNode child: children) {
            printTreeStructure(child);
        }
    }


    public MyNode findNodeInTreeFirst(MyNode node, String searchNodeName) {
        MyNode searchNode = null;
        if (searchNodeName.equals(node.getName())) {
            return node;
        }
        List<MyNode> children = node.getChildren();
        for (MyNode eachNode: children) {
            MyNode nodeInTreeFirst = findNodeInTreeFirst(eachNode, searchNodeName);
            if (nodeInTreeFirst != null) {
                searchNode = nodeInTreeFirst;
            }
        }
        return searchNode;
    }


}


