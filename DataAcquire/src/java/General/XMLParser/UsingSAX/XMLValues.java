/*
 * XMLValues.java
 *
 * Created on 2006年6月23日, 上午10:26
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package General.XMLParser.UsingSAX;

import java.lang.String;
import java.util.Map;
import java.util.Vector;
import General.HashMap;
import java.util.Set;
import java.util.Iterator;
/**
 * It is a data container to store the data retrived from a XML file by SimpleHandler.
 * This object must be initialzed with a specified SECTION name. And in parsing progress
 * Only the tags and value within the section will be retrived form example:
 *      <SECTION_PEO>
 *              <NAME>Mikkey</NAME>
 *              <AGE>22</AGE>
 *      </SECTION_PEO>
 *      <SECTION_CCD>
 *              <PRO>middle age</PRO>
 *      </SECTION_CCD>
 * if the Object is initialized as XMLValues("SECTION_PEO"),
 * Only the NAME-Mikkey and AGE-22 will be retrived.
 *          
 * @author Cuilin
 */

public class XMLValues {
    private String strSection;
    private HashMap hsmap_Keys = new HashMap();
    /** Creates a new instance of XMLValues */
    public XMLValues(String sectionName) {
        this.strSection = sectionName;
    }
    public String getSection() {
        return this.strSection;
    }
    public void insert(String Key, String Value) {
        hsmap_Keys.put( Key, Value);
    }
    public void remove(String Key) {
        hsmap_Keys.remove(Key);
    }
    
    public Object get(String Key) {
        return hsmap_Keys.get(Key);
    }
    
    public Set getAllKey() {
        return hsmap_Keys.keySet();
    }
    
    public Vector getAllKeyVector() {
        Vector tempVector = new Vector();
        Iterator tempItr = this.getKeysIt();
        while( tempItr.hasNext() ){
            tempVector.addElement(((Map.Entry)tempItr.next()).getKey());
        }
        return tempVector;
    }
    
    private Iterator getKeysIt() {
        return this.hsmap_Keys.entrySet().iterator();
    }
}
