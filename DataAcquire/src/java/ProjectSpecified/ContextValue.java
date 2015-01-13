/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ProjectSpecified;

import General.HashMap;

import javax.servlet.ServletContextEvent;

/**
 *
 * @author cuilin
 */
public class ContextValue {
    private static ContextValue instance;

    public ServletContextEvent sce;
    
    /** 多语言的翻译语言资源 */
    public static HashMap LangDictionarys;
        
    public static String contextPath = "";
    public static String uploadDirectory;
    public static String tempDirectory;
    
    
    private ContextValue() {
        
    }

    public static ContextValue getInstance() {
        if( ContextValue.instance == null ) {
            ContextValue.instance = new ContextValue();
        }
        return ContextValue.instance;        
    }
}
