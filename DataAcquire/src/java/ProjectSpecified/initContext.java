/*
 * initContext.java
 *
 * Created on 2006年8月10日, 下午1:49
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package ProjectSpecified;

import java.io.File;

import javax.servlet.ServletContextListener;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContext;
import General.HashMap;
import General.XMLParser.UsingDOM.CXML2DOMReader;
import General.XMLParser.UsingSAX.SimpleXMLReader;
import General.XMLParser.UsingSAX.XMLValues;
import ProjectSpecified.ConnectionPools.CPrjPool;
import java.io.FileReader;
import java.net.MalformedURLException;
import java.net.NetworkInterface;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Vector;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
/**
 *
 * @author Cuilin
 */
public class initContext implements ServletContextListener {
	public ServletContextEvent contextEvent;
    public static String concurrentUserLimit = "200";
    /** Creates a new instance of initContext */
    public initContext() {
    }

    public void contextInitialized(ServletContextEvent sce) {
        ServletContext myContext = sce.getServletContext();
        this.resetConfigFile(sce);
        ContextValue myContextV = initContext.initialzieProcess( sce );
        //myContextV.Deviations = this.getDeviations();
        this.contextEvent = sce;
		
        String rootPath;
        try {
            rootPath = myContext.getResource("/").getPath();
            String[] rootPaths = rootPath.replace("/", " ").trim().split(" ");
            if( rootPaths.length > 0 ) systemConstants.appName = rootPaths[ rootPaths.length - 1 ];
        } catch (MalformedURLException ex) {
        }

    }

    public void contextDestroyed(ServletContextEvent sce) {
        ServletContext myContext = sce.getServletContext();

        destoryDBPools();
        
    }


	/**
	 * 读取其他的单另的数据字典的条目
	 * @param lang
	 * @return 
	 */
	/**
         * public static HashMap<String, DatadictionaryTO > getOtherDictItems( String lang ) {
		HashMap<String, DatadictionaryTO > r = new HashMap<String, DatadictionaryTO >();
		
		DatadictionaryDAO dao = new DatadictionaryDAO();
		
		String dictNames = CUtils.join( ContextValue.dictValueOfOneLang.otherDictItemNames , "," );
		ArrayList<DatadictionaryTO> rArray = dao.findByMultipleCondition(dictNames, null, null , lang );
		for( int i = 0; i < rArray.size(); i++ ) {
			r.put( rArray.get(i).getDictCategory() , rArray.get(i) );
		}
		return r;
	}*/
	
    
    public static void resetConfigFile( ServletContextEvent sce ) {
        ContextValue myContextV = ContextValue.getInstance();
        myContextV.contextPath = sce.getServletContext().getRealPath("/WEB-INF");
        systemConstants.dbConfigFile = sce.getServletContext().getRealPath(systemConstants.dbConfigFile );
    }
    
    public static ContextValue initialzieProcess( ServletContextEvent sce ) {
        ContextValue myContextV = ContextValue.getInstance();
        myContextV.sce = sce;

        initContext.initDBPools();     //新的数据库连接池
        
        return myContextV;
    }


    private static String hexByte(byte b) {
        return String.format("%02x", b);
    }
    private static String getMac() {
        try {
            Vector macs = new Vector();
            Enumeration<NetworkInterface> el = NetworkInterface.getNetworkInterfaces();
            while (el.hasMoreElements()) {
                NetworkInterface nf = el.nextElement();
                if( ( nf.getName().length() < 3 ) || ( !nf.getName().substring(0,3).equalsIgnoreCase("eth") ) || ( nf.getDisplayName().toUpperCase().indexOf("VIRT") >=0 )) {
                    continue;
                }
                byte[] mac = nf.getHardwareAddress();
                if (mac == null) {
                    continue;
                }
                StringBuilder builder = new StringBuilder();
                for (byte b : mac) {
                    builder.append(hexByte(b));
                    builder.append("-");
                }
                if( builder.length() > 0 ) {
                    builder.deleteCharAt(builder.length() - 1);
                    if( builder.length() < 18 ) {
                        macs.add( builder.toString() );
                    }
                }

            }
            Collections.sort(macs);
            StringBuilder macList = new StringBuilder();
            for( int i = 0; i < macs.size(); i++ ) {
                macList.append( macs.get(i).toString().replace("-", "") );
            }
            return macList.toString();
        } catch (Exception exception) {
            exception.printStackTrace();
            return null;
        }
    }
	
    public static void initDBPools() {
            CPrjPool dp = CPrjPool.getInstance();
            dp.initConnectionPool( systemConstants.dbConfigFile );
            dp.reloadConfig();
            try{
                dp.initPool();
            }
            catch( General.Pools.CPoolException ex ) {
                ex.printStackTrace();
            }

    }
	
    public static void destoryDBPools() {
        CPrjPool.getInstance().clearPool();
    }

	
	/**
	 * 获取多语言的用于界面翻译的数据字典，读取完后放在CMultiLangResDict.instance里
	 */
    public void loadMultiLangRes() {

    }

    public static void main( String[] args ) {
        //CConnectionPool cp = initContext.getDBPool( systemConstants.dbConfigFile );

    }
}
