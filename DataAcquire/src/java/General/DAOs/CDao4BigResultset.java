/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package General.DAOs;

import General.Pools.CPoolException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import General.HashMap;
import java.util.Vector;
import java.util.logging.Level;

/**
 *
 * @author bahamut
 */
public abstract class CDao4BigResultset extends CDao{
    public abstract boolean dataOperation( HashMap dataObj );

    @Override
    public Vector doSpecialThing( String aboutSQL, Connection myCn ) {
        Vector myComment = new Vector();
            try {
                Statement mySt = null;
                ResultSet myRs = null;
                try {
                    mySt = myCn.createStatement();

                    String SQL = aboutSQL;

                    myRs = mySt.executeQuery(SQL);

                    if ( myRs.next() ) {
                    //myRs.first();
                    //if( myRs.isFirst()) {
                        int count = 0;
                        ResultSetMetaData theRsInfo= myRs.getMetaData();
                        do {
                            HashMap thisRs = new HashMap();
                            for ( int i = 1; i <= theRsInfo.getColumnCount(); i++) {
                                thisRs.put(theRsInfo.getColumnName(i).toLowerCase(), myRs.getObject(i));
                            }
                            count++;
                            this.dataOperation(thisRs);
                        }while( myRs.next()) ;
                    }
                    myRs.close();
                    mySt.close();
                }
                catch( Exception e2 ){
                    if( myRs != null ) {
                        try { myRs.close(); } catch( Exception ee ) { ee.printStackTrace();}
                    }
                    if( mySt != null ){
                        try { mySt.close(); } catch( Exception ee ) { ee.printStackTrace();}
                    }
                    e2.printStackTrace();
                }
            }
            catch( Exception sqlex) {
                this.makeLog( Level.SEVERE,"SQL Exception :" + sqlex.toString());
            }
        return myComment;
    }
    @Override
    public Vector doSpecialThing( String aboutSQL ) {
        Vector myComment;
        Connection myCn = null;
        try {
            myCn = (Connection)thisCnPool.getPoolItem();
            myComment = this.doSpecialThing( aboutSQL, myCn );
            this.thisCnPool.freePoolItem(myCn);
        }
        catch( CPoolException pex ) {
            this.makeLog (Level.SEVERE,"Connection Pool Exception");
            myComment = new Vector();
            if( myCn != null ) this.thisCnPool.freePoolItem(myCn);
        }
        return myComment;
    }

            @Override
            public String generateSQL(CValue theValueObj, String Operation) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public String generateSQL(CValue theOldValueObj, CValue theNewValueObj, String Operation) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public String generateSQL(String theKey, String theValue, String Operation) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public String generateSQL(String theKey, int theValue, String Operation) {
                throw new UnsupportedOperationException("Not supported yet.");
            }
}
