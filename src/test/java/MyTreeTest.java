import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import org.custommonkey.xmlunit.XMLTestCase;
import org.custommonkey.xmlunit.XMLUnit;
import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;
import tree.MyNode;
import tree.MyTree;
import xml.TreeXML;

class MyTreeTest extends XMLTestCase {

    @Test
    void saveTreeToXML() throws IOException, SAXException, ParserConfigurationException, TransformerException {
        XMLUnit.setIgnoreWhitespace(true);
        String startFileName = "D:\\TestXML2\\src\\main\\resources\\books.xml";
        String resultFileName = "D:\\TestXML2\\src\\main\\resources\\books.xml_edit.xml";
        TreeXML tree = new TreeXML();
        tree.parseXML(startFileName);
        MyNode root = tree.getRoot();
        FileInputStream fis1 = new FileInputStream(startFileName);
        FileInputStream fis2 = new FileInputStream(tree.saveTreeToXML(root, resultFileName));
        BufferedReader source = new BufferedReader(new InputStreamReader(fis1));
        BufferedReader target = new BufferedReader(new InputStreamReader(fis2));

        assertXMLEqual(source, target);

    }
}