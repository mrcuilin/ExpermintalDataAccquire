/*
 * CXML2DOMReader.java
 *
 * Created on 2007骞���, 涓嬪崍8:51
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package General.XMLParser.UsingDOM;
import org.w3c.dom.*;
import javax.xml.parsers.*;
import org.xml.sax.*;
import java.io.*;
/**
 *
 * @author Cuilin
 */
public class CXML2DOMReader {
    //
    public CXML2DOMReader() {
    
    }
    
    public static Document parseFile( String filePath ) throws Exception{
        DocumentBuilder dm = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document domt = dm.parse( new File( filePath) );
        return domt;
    }
    public static Document parseXML( String xmlStr ) throws Exception {
        DocumentBuilder dm = DocumentBuilderFactory.newInstance().newDocumentBuilder();

        StringReader strRdr = new StringReader( xmlStr );
        InputSource is = new InputSource( strRdr );
        Document domt = dm.parse( is );
        
        return domt;
    }
    
    public static void main( String[] args ) throws Exception {
        String xml = "<?xml version='1.0' encoding='utf-8'?>" +
                "<DataNodes>" +
                "<DataNode >" +
                "<DataNodes>" +
                "<DataNode Name='cause' Value='C1' ></DataNode>" +
                "<DataNode Name='cause' Value='C2' ></DataNode>" +
                "<DataNode Name='cause' Value='C3' ></DataNode>" +
                "</DataNodes>" +
                "<DataNodes>" +
                "<DataNode Name='consequence' Value='Con1' >" +
                "<DataNodes>" +
                "<DataNode Name='safeguard' Value='SG11' ></DataNode>" +
                "<DataNode Name='safeguard' Value='SG12' ></DataNode>" +
                "<DataNode Name='safeguard' Value='SG13' ></DataNode>" +
                "</DataNodes>" +
                "<DataNodes>" +
                "<DataNode Name='recommendation' Value='R11' ></DataNode>" +
                "<DataNode Name='recommendation' Value='R12' ></DataNode>" +
                "</DataNodes>" +
                "</DataNode>" +
                "<DataNode Name='consequence' Value='Con2' >" +
                "<DataNodes>" +
                "<DataNode Name='safeguard' Value='SG21' ></DataNode>" +
                "</DataNodes>" +
                "<DataNodes>" +
                "<DataNode  Name='recommendation' Value='R21' ></DataNode>" +
                "<DataNode  Name='recommendation' Value='R22' ></DataNode>" +
                "</DataNodes>" +
                "</DataNode>" +
                "</DataNodes>" +
                "</DataNode>" +
                "</DataNodes>"                ;
        Document dmx = parseXML( xml );
        //Document dmx = parseFile( "C:/WORKING/Java/HAZOPPrj/web/Deviation/DeviationEditorSubmitData.xml" );
        System.out.print( dmx.getFirstChild().getFirstChild().getFirstChild().getFirstChild().getAttributes().getNamedItem("Value").getNodeValue() );
      
    }
}
