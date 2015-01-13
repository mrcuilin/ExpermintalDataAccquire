/*
 * CConnectionPool.java
 *
 * Created on 2006年6月22日, 上午11:49
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package General.Pools;

import ProjectSpecified.systemConstants;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.DriverManager;

import java.lang.String;
import General.Pools.CGeneralPool;
import General.Pools.CPoolException;
import General.XMLParser.UsingSAX.*;
import java.lang.Integer;

import java.util.logging.Logger;
import java.util.logging.Level;

/**
 *
 * @author Cuilin
 */
public abstract class CAbsConnectionPool extends CGeneralPool {

    //protected static CConnectionPool instance;

    protected String configFile = null;
    protected int initialConnectionNumber = 0;
    /*
     *      <Driver>com.mysql.jdbc.Driver</Driver>
            <ConnectURL>jdbc:mysql://127.0.0.1:3306/</ConnectURL>
            <User>netuser</User>
            <Password></Password>
     */
    protected String Driver = null;
    protected String ConnectURL = null;
    protected String User  = null;
    protected String Password = null;
    
    protected CAbsConnectionPool(){
        super();
    }

    
    public void initConnectionPool(String configFileName) {
        this.configFile = configFileName;
    }
    
    public static CAbsConnectionPool getInstance() throws CPoolException{
        throw new CPoolException("Do not support. As it is an semi-abstract pool.");
    };
    
    /**
     * Creates a new instance of CConnectionPool
     * the configSetName is necessary, used to distinguish the different database`
     */
  
    protected void reloadConfig( String configSetName ){
        if( this.configFile != null ) {
            super.reloadConfig();
            SimpleXMLReader myReader = new SimpleXMLReader( configSetName, this.configFile );
            XMLValues myValues = myReader.read();
            this.setPoolCapacity( Integer.parseInt( (String)myValues.get("MaxConnNum")) );
            this.initialConnectionNumber = Integer.parseInt( (String)myValues.get("InitConnNum"));
            this.Driver = (String)myValues.get("Driver");
            this.ConnectURL = (String)myValues.get("ConnectURL");
            this.User = (String)myValues.get("User");
            this.Password = (String)myValues.get("Password");
        }
    }

    public void initPool() throws CPoolException{
        super.initPool(this.initialConnectionNumber);
    }
    
    public synchronized boolean isOpen(Object Obj){
        if( Obj instanceof Connection ) {
            try {
                return !((Connection)Obj).isClosed();
            }
            catch (SQLException ex) {
                if( this.myLoger != null) {
                    try {
                        myLoger.log(Level.SEVERE,"Error when query the DB COnnection isOpen().");
                    }
                    catch ( Exception ex2){}
                }
                return false;
            }
        }
        else {
            return false;
        }
    }

    public synchronized boolean closePoolItem(Object Obj){
        if(Obj instanceof Connection) {
            try {
                ((Connection)Obj).close();
                return true;
            }
            catch (SQLException ex) {
                if( this.myLoger != null) {
                    try {
                        myLoger.log(Level.SEVERE,"Error when close the DB COnnection.");
                    }
                    catch ( Exception ex2){}
                }
                return false;
            }
        }
        else {
            return true;
        }
    }

    public synchronized Object createPoolItem(){
        try {
            Class.forName(this.Driver);
            Connection OneCn = DriverManager.getConnection( this.ConnectURL, this.User, this.Password);
            return OneCn;
        }
        catch ( SQLException ex ) {
            if( this.myLoger != null) {
                try {
                    myLoger.log(Level.SEVERE,"Error when create the DB COnnection).");
                }
                catch ( Exception ex2){}
            }
            return null;
        }
        catch ( ClassNotFoundException ex) {
                if( this.myLoger != null) {
                    try {
                        myLoger.log(Level.SEVERE,"The DB Driver is Error.");
                    }
                    catch ( Exception ex2){}
                }
                return null;
        }
    }
   
    public static void main( String[] args ) throws Exception{
        //这个类现在被转换成一个抽象类，不实现任何具体的ConnectionPool,
        //因为实际的连接池是单件模式的比较好
    }    
}
