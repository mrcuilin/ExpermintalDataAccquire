/*
 * SimpleXMLReader.java
 *
 * Created on 2006年6月23日, 下午12:25
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package General.XMLParser.UsingSAX;

import javax.xml.parsers.SAXParserFactory;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.util.Vector;

import org.xml.sax.XMLReader;

/**
 * This is a simple XML reader which is used to read config from a external 
 * XML config file.
 * To use it, 2 code line are needed
 * 
 *      SimpleXMLReader myXML = new SimpleXMLReader("[SECTION NAME]","[File Name]");
 *      // Initialize the reader, specify the SECTION and FILENAME
 *
 *      XMLValues result = myXML.read();
 *      // Perform parser operation and retrun result.
 *
 * The return value is a XMLValues object contains all the data from the XML
 * This object can only retrive a "plain" XML, that means, the data structure within
 * the section you specified must a list without any inner stricture such as:
 * <DB>
 *      <driver>SQL</driver>
 *      <usernaem>UN</username>
 *      <pwd>1234</pwd>
 * </DB>
 * Every tag within the section must have NOT properties, which will be neglected.
 * And eveny tag must a final sub tag without any inner tags.
 *
 * See the example in main()
 *
 * @author Cuilin
 */
public class SimpleXMLReader {
    
    private String Section;
    private String fileName;
    
    /** Creates a new instance of SimpleXMLReader */
    public SimpleXMLReader(String section, String fn) {
        this.Section = section;
        this.fileName = fn;
    }
    
    /**
     * Do parser operation
     */
    public XMLValues read(){
        XMLValues tempValues = new XMLValues(this.Section);
        
        SimpleHandler myHandler = new SimpleHandler(tempValues);
        try {
            
            SAXParserFactory SAXFactory = SAXParserFactory.newInstance();
            SAXFactory.setNamespaceAware(true);
            SAXFactory.setValidating(true);
            SAXParser myParser = SAXFactory.newSAXParser();
            
            /*myParser.setContentHandler(myHandler);
            myParser.setErrorHandler(myHandler);
            org.xml.sax.InputSource input = new InputSource();
            myParser.parse(input);*/
            myParser.parse(new File(this.fileName), myHandler);
            
        }
        catch ( Exception ex) {
            // ParserConfigurationException or SAXException
            ex.printStackTrace();
            //System.out.println(ex);
        }
        finally {
            return tempValues;
        }
    }
    
    public static void main(String[] args) {
        SimpleXMLReader myXML = new SimpleXMLReader("Database","C:/HAZOP/SystemConf.xml");
        XMLValues result = myXML.read();
        Vector C = result.getAllKeyVector();
        System.out.println("Total " + C.size() + " items allocated!");
        for( int i = 0; i < C.size(); i ++) {
            System.out.print(C.get(i) + " = ");
            System.out.println(result.get((String)C.get(i)));
        }
        
    }
}
