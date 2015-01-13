/*
 * CValue.java
 *
 * Created on 2006年7月23日, 下午6:38
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package General.DAOs;

import General.DAOs.ICanExport;
import java.lang.String;
/**
 *
 * @author Cuilin
 */
public class CValue extends ICanExport {
    /** Creates a new instance of CValue */
    public static String getJavascriptArray( CValue[] d ){
        StringBuffer r = new StringBuffer();
        r.append("[");
        for( int i = 0; i < d.length; i++ ) {
            if( i > 0 ) r.append(",");
            r.append( d[i].exportToJavascriptData() );
        }
        r.append("]");
        return r.toString();
    }
}
