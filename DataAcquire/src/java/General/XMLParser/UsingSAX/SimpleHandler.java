/*
 * SimpleHandler.java
 *
 * Created on 2006年6月23日, 上午10:09
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package General.XMLParser.UsingSAX;

import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import java.lang.StringBuffer;
/**
 * My Handler used by SimpleXMLReader
 * XML Reader needs a Handler extending from org.xml.sax.helpers.DefaultHandler
 *       to manipulate the data within the XML. The handler responses to each Tag
 *       DTD mark, characters and some other things. And save them in somewhere.
 * In this design the DATA gotten when then event handler is invoked is save in a 
 *       external Object (Class XMLValues) passed in this class in initializing.
 *
 *        
 * @author Cuilin
 */
public class SimpleHandler extends DefaultHandler{
    
    private XMLValues ValueSet;
    private StringBuffer sb_Value;
    
    private boolean isReading;
    /** Creates a new instance of SimpleHandler */
    public SimpleHandler(XMLValues valueSet) {
        this.ValueSet = valueSet;
        this.isReading = false;
        sb_Value = new StringBuffer();
    }
    
    /**
     * Deal with the Tag's Start
     * If the Tag is the specified section mark, begin to save the inner tag's key and value
     * or clear the buffer
     */
    public void startElement (String uri, String localName,
			      String qName, Attributes attributes)
        	throws SAXException {
        sb_Value.setLength(0);          
        //Every time it found a new tag start, clear the buffer, ready to receive new value; 
        if( ! this.isReading ) {
            if ( this.ValueSet.getSection().equalsIgnoreCase(localName)) {
                isReading = true;
            }
        }
    }
    
    
    /**
     * Deal with the Tag ending:
     * Save the Tag value if it is within the specified SECTION.
     */
    public void endElement (String uri, String localName, String qName)
                throws SAXException
    {
        if( this.isReading ) {
            if( this.ValueSet.getSection().equalsIgnoreCase(localName)) {
                isReading = false;
            }
            else {
                // Every time it found a tag end, put the value into the resultset
                this.ValueSet.insert(localName, sb_Value.toString().trim());
            }
        }
    }
    
    /**
     * The Event that the parser received a segment of characters.
     * Notice: It seems taht the ch[] contains all the characters of the XML file actually.
     *         And the start and length specified the current segment's position. 
     *         So that only save the segment is OK.
     */
    public void characters (char ch[], int start, int length)
	throws SAXException
    {
        if( this.isReading) {
            sb_Value.append(ch,start,length);
        }
    }
}
