package tree;

import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import org.xml.sax.SAXException;
import tree.MyNode;
import tree.MyTree;
import xml.TreeXML;

public class Main {
    public static void main(String[] args) throws ParserConfigurationException, IOException, SAXException, TransformerException {

        TreeXML tree = new TreeXML();
        tree.parseXML("D:\\TestXML2\\src\\main\\resources\\books.xml");
        MyNode root = tree.getRoot();
        tree.saveTreeToXML(root, "D:\\TestXML2\\src\\main\\resources\\books_edit.xml");
    }

}
