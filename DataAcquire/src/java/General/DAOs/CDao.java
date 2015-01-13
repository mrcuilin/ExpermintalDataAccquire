/*
 * CDao.java
 *
 * Created on 2006年7月22日, 下午9:41
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package General.DAOs;

import java.sql.Connection;
import java.util.logging.Logger;
import java.util.logging.Level;
import General.HashMap;
import java.util.Vector;
import General.Pools.CPoolException;

import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import General.DAOs.CValue;
import General.Pools.CAbsConnectionPool;
import ProjectSpecified.ConnectionPools.CPrjPool;

import ProjectSpecified.systemConstants;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * 所有数据库访问的DAO的基类，
 *
 * @author Cuilin
 */
public abstract class CDao {

    protected Class correspondValueObj;
    public CAbsConnectionPool thisCnPool = null;
    protected Logger myLogger = null;
    public String idColumnName;
    /** Creates a new instance of CDao */
    public String tableName;

    public CDao() {
        this.getCnPool();
        this.setKeyColumns();
        this.setTableName();
        this.setValueObjClass();
    }

    abstract protected void setValueObjClass();

    abstract protected void setTableName();

    abstract protected void setKeyColumns();

    /**
     * 获取数据连接池，由于该方法需要知道具体的数据库，所以不能在这里定义，必须由其子类说明
     */
    abstract protected void getCnPool();

    public final void setLogger(Logger theLogger) {
        this.myLogger = theLogger;
    }

    protected final void makeLog(Level lv, String descp) {
        try {
            if (this.myLogger != null) {
                this.myLogger.log(lv, descp);
            }
        } catch (Exception ex) {
        }
    }

    /**
     * doQuery is a univerisal Query Function which can be used to do such a insert, delete ,uodate operation
     * The insert(), update() and delete() are actually call the doQuery();
     * The SQL used to preform the operation is defined by a abstract function generateSQL, which is implemented by 
     *     the reality DAO objects.
     * This function can be directly called to perform some special operation beside the 3 operation above.
     */
    public boolean doQuery(CValue theValueObj, String Operation, Connection con) {
        boolean TempResult = false;
        Statement mySt = null;
        try {
            mySt = con.createStatement();

            String SQL = this.generateSQL(theValueObj, Operation);

            TempResult = mySt.execute(SQL);
            mySt.close();
            TempResult = true;
        } catch (SQLException sqlex) {
            sqlex.printStackTrace();;
            if (mySt != null) {
                try {
                    mySt.close();
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
            this.makeLog(Level.SEVERE, "SQL Exception :" + sqlex.toString());
            TempResult = false;
        }
        return TempResult;
    }

    public boolean doQuery(CValue theValueObj, String Operation) {
        boolean TempResult = false;
        Connection myCn = null;
        try {
            myCn = (Connection) this.thisCnPool.getPoolItem();
            TempResult = this.doQuery(theValueObj, Operation, myCn);
            this.thisCnPool.freePoolItem(myCn);
        } catch (CPoolException pex) {
            this.makeLog(Level.SEVERE, "Connection Pool Exception");
            TempResult = false;
            if (myCn != null) {
                thisCnPool.freePoolItem(myCn);
            }
        }
        return TempResult;
    }

    public boolean doQuery(CValue[] theValueObj, String Operation, Connection myCn) {
        boolean TempResult = false;
        try {
            Statement mySt = null;
            try {
                mySt = myCn.createStatement();

                String SQL = "";
                for (int i = 0; i < theValueObj.length; i++) {
                    SQL = this.generateSQL(theValueObj[i], Operation);
                    TempResult = mySt.execute(SQL);
                }
                mySt.close();
                TempResult = true;
            } catch (Exception e2) {
                if (mySt != null) {
                    try {
                        mySt.close();
                    } catch (Exception ee) {
                        ee.printStackTrace();
                    }
                }
                e2.printStackTrace();
                TempResult = false;
            }
        } catch (Exception ex) {
            this.makeLog(Level.SEVERE, "SQL Exception :" + ex.toString());
            TempResult = false;
            if (myCn != null) {
                thisCnPool.freePoolItem(myCn);
            }
        }
        return TempResult;
    }

    public boolean doQuery(CValue[] theValueObj, String Operation) {
        boolean TempResult = true;
        Connection myCn = null;
        try {
            myCn = (Connection) this.thisCnPool.getPoolItem();
            TempResult = this.doQuery(theValueObj, Operation, myCn);
            this.thisCnPool.freePoolItem(myCn);
        } catch (CPoolException pex) {
            this.makeLog(Level.SEVERE, "Connection Pool Exception");
            TempResult = false;
        }
        return TempResult;
    }

    public boolean doQuery(CValue theOldValueObj, CValue theNewValueObj, String Operation, Connection myCn) {
        boolean TempResult = false;
        try {
            Statement mySt = null;
            try {
                mySt = myCn.createStatement();

                String SQL = this.generateSQL(theOldValueObj, theNewValueObj, Operation);

                mySt.execute(SQL);
                TempResult = true;
                mySt.close();
            } catch (Exception e2) {
                if (mySt != null) {
                    try {
                        mySt.close();
                    } catch (Exception ee) {
                        ee.printStackTrace();
                    }
                }
                e2.printStackTrace();
                TempResult = false;
            }
        } catch (Exception sqlex) {
            this.makeLog(Level.SEVERE, "SQL Exception :" + sqlex.toString());
            TempResult = false;
        }
        return TempResult;
    }

    public boolean doQuery(CValue theOldValueObj, CValue theNewValueObj, String Operation) {
        boolean TempResult = false;
        Connection myCn = null;
        try {
            myCn = (Connection) this.thisCnPool.getPoolItem();
            TempResult = this.doQuery(theOldValueObj, theNewValueObj, Operation, myCn);
            this.thisCnPool.freePoolItem(myCn);
        } catch (CPoolException pex) {
            this.makeLog(Level.SEVERE, "Connection Pool Exception");
            TempResult = false;
            if (myCn != null) {
                this.thisCnPool.freePoolItem(myCn);
            }
        }
        return TempResult;
    }

    public Vector<CValue> doFind(String theKey, String theValue, String Operation, Connection myCn) {
        Vector myComment = new Vector();
        try {
            Statement mySt = null;
            ResultSet myRs = null;
            try {
                mySt = myCn.createStatement();

                String SQL = this.generateSQL(theKey, theValue, Operation);

                myRs = mySt.executeQuery(SQL);
                //myRs.first();
                if (myRs.next()) {
                    int count = 0;
                    do {
                        myComment.add(getCValueByRs(myRs));
                        count++;
                    } while (myRs.next());
                }
                myRs.close();
                mySt.close();
            } catch (Exception e2) {
                if (myRs != null) {
                    try {
                        myRs.close();
                    } catch (Exception ee) {
                        ee.printStackTrace();
                    }
                }
                if (mySt != null) {
                    try {
                        mySt.close();
                    } catch (Exception ee) {
                        ee.printStackTrace();
                    }
                }
                e2.printStackTrace();
            }
        } catch (Exception sqlex) {
            this.makeLog(Level.SEVERE, "SQL Exception :" + sqlex.toString());
        }

        
        return myComment;
    }

    
    public CValue[] getAllItems() {
        String SQL = "select * from " + this.tableName;
        Vector<HashMap> cats = this.doSpecialThing(SQL);
        CValue[] t = new CValue[ cats.size() ];
        t = this.getCValuesFromSpecifiedOpt(cats, t );
        return t;
    }
    
    public Vector<CValue> doFind(String theKey, String theValue, String Operation) {
        Vector myComment;
        Connection myCn = null;
        try {
            myCn = (Connection) thisCnPool.getPoolItem();
            myComment = this.doFind(theKey, theValue, Operation, myCn);
            this.thisCnPool.freePoolItem(myCn);
        } catch (CPoolException pex) {
            this.makeLog(Level.SEVERE, "Connection Pool Exception");
            myComment = new Vector();
            if (myCn != null) {
                this.thisCnPool.freePoolItem(myCn);
            }
        }
        return myComment;
    }

    public Vector<CValue> doFind(String theKey, int theValue, String Operation, Connection myCn) {
        Vector myComment = new Vector();
        try {
            Statement mySt = null;
            ResultSet myRs = null;
            try {
                mySt = myCn.createStatement();

                String SQL = this.generateSQL(theKey, theValue, Operation);

                myRs = mySt.executeQuery(SQL);
                //myRs.first();                
                if (myRs.next()) {
                    int count = 0;
                    do {
                        myComment.add(getCValueByRs(myRs));
                        count++;
                    } while (myRs.next());
                }
                myRs.close();
                mySt.close();
            } catch (Exception e2) {
                if (myRs != null) {
                    try {
                        myRs.close();
                    } catch (Exception ee) {
                        ee.printStackTrace();
                    }
                }
                if (mySt != null) {
                    try {
                        mySt.close();
                    } catch (Exception ee) {
                        ee.printStackTrace();
                    }
                }
                e2.printStackTrace();
            }
        } catch (Exception sqlex) {
            this.makeLog(Level.SEVERE, "SQL Exception :" + sqlex.toString());
        }

        return myComment;
    }

    public Vector<CValue> doFind(String theKey, int theValue, String Operation) {
        Vector myComment;
        Connection myCn = null;
        try {
            myCn = (Connection) thisCnPool.getPoolItem();
            myComment = this.doFind(theKey, theValue, Operation, myCn);
            this.thisCnPool.freePoolItem(myCn);
        } catch (CPoolException pex) {
            this.makeLog(Level.SEVERE, "Connection Pool Exception");
            myComment = new Vector();
            if (myCn != null) {
                this.thisCnPool.freePoolItem(myCn);
            }
        }
        return myComment;
    }

    public boolean doSpecialThingWithoutResult(String aboutSQL, Connection myCn) {
        boolean result = false;
        try {
            Statement mySt = null;
            try {
                mySt = myCn.createStatement();

                String SQL = aboutSQL;

                mySt.execute(SQL);
                result = true;      //如果能运行到这里，证明操作成功
            } catch (Exception e2) {
                if (mySt != null) {
                    try {
                        mySt.close();
                    } catch (Exception ee) {
                        ee.printStackTrace();
                    }
                }
                e2.printStackTrace();
                result = false;
            }
        } catch (Exception sqlex) {
            this.makeLog(Level.SEVERE, "SQL Exception :" + sqlex.toString());
            result = false;
        }

        return result;
    }

    public boolean doSpecialThingWithoutResult(String aboutSQL) {
        boolean result = false;
        Connection myCn = null;
        try {
            myCn = (Connection) thisCnPool.getPoolItem();
            result = this.doSpecialThingWithoutResult(aboutSQL, myCn);
            this.thisCnPool.freePoolItem(myCn);
        } catch (CPoolException pex) {
            this.makeLog(Level.SEVERE, "Connection Pool Exception");
            result = false;
            if (myCn != null) {
                this.thisCnPool.freePoolItem(myCn);
            }
        }
        return result;
    }

    public Vector<HashMap> doSpecialThing(String aboutSQL, Connection myCn) {
        Vector myComment = new Vector();
        try {
            Statement mySt = null;
            ResultSet myRs = null;
            try {
                mySt = myCn.createStatement();

                String SQL = aboutSQL;

                myRs = mySt.executeQuery(SQL);

                if (myRs.next()) {
                    //myRs.first();
                    //if( myRs.isFirst()) {
                    int count = 0;
                    ResultSetMetaData theRsInfo = myRs.getMetaData();
                    do {
                        HashMap thisRs = new HashMap();
                        for (int i = 1; i <= theRsInfo.getColumnCount(); i++) {
                            thisRs.put(theRsInfo.getColumnName(i).toLowerCase(), myRs.getObject(i));
                        }
                        count++;
                        myComment.add(thisRs);
                    } while (myRs.next());
                }
                myRs.close();
                mySt.close();
            } catch (Exception e2) {
                if (myRs != null) {
                    try {
                        myRs.close();
                    } catch (Exception ee) {
                        ee.printStackTrace();
                    }
                }
                if (mySt != null) {
                    try {
                        mySt.close();
                    } catch (Exception ee) {
                        ee.printStackTrace();
                    }
                }
                e2.printStackTrace();
            }
        } catch (Exception sqlex) {
            this.makeLog(Level.SEVERE, "SQL Exception :" + sqlex.toString());
        }

        return myComment;
    }

    public Vector<HashMap> doSpecialThing(String aboutSQL) {
        Vector myComment;
        Connection myCn = null;
        try {
            myCn = (Connection) thisCnPool.getPoolItem();
            myComment = this.doSpecialThing(aboutSQL, myCn);
            this.thisCnPool.freePoolItem(myCn);
        } catch (CPoolException pex) {
            this.makeLog(Level.SEVERE, "Connection Pool Exception");
            myComment = new Vector();
            if (myCn != null) {
                this.thisCnPool.freePoolItem(myCn);
            }
        }
        return myComment;
    }

    /**
     * 获取指定的数据对象CValue中的对应的Field的值对应的字符串，如果是字符串型的，要带'
     * @param vObj
     * @param fld
     * @return
     */
    private String getFieldValue(CValue theValueObj, Field fld) {
        String SQL = null;
        String type = fld.getType().getName().toLowerCase();
        if ((type.indexOf("integer") >= 0) || (type.indexOf("int") >= 0)) {
            try {
                SQL = String.valueOf(fld.getInt(theValueObj));
            } catch (Exception ex) {
            }
        } else if (type.indexOf("long") >= 0) {
            try {
                SQL = String.valueOf(fld.getLong(theValueObj));
            } catch (Exception ne) {
            }
        } else if (type.indexOf("short") >= 0) {
            try {
                SQL = String.valueOf(fld.getShort(theValueObj));
            } catch (Exception ne) {
            }
        } else if (type.indexOf("string") >= 0) {
            try {
                Object temp = fld.get(theValueObj);
                SQL = "'" + (String) ((temp == null) ? "" : temp) + "'";
            } catch (Exception ne) {
            }
        } else if (type.indexOf("float") >= 0) {
            try {
                SQL = String.valueOf(fld.getFloat(theValueObj));
            } catch (Exception ne) {
            }
        } else if (type.indexOf("double") >= 0) {
            try {
                SQL = String.valueOf(fld.getDouble(theValueObj));
            } catch (Exception ne) {
            }
        }
        return SQL;
    }

    public String generateInsertSQL(CValue theValueObj, String tableName) {
        Field[] flds = this.correspondValueObj.getFields();
        StringBuilder SQL = new StringBuilder();
        SQL.append("insert into " + tableName + "(");
        for (int i = 0; i < flds.length; i++) {
            if (i > 0) {
                SQL.append(",");
            }
            SQL.append(flds[i].getName());
        }
        SQL.append(") values(");
        for (int i = 0; i < flds.length; i++) {
            if (i > 0) {
                SQL.append(",");
            }
            SQL.append(this.getFieldValue(theValueObj, flds[i]));
        }
        SQL.append(")");
        return SQL.toString();
    }
    
    /**
     * 产生一条包含多个数据的insert语句，
     * @param theValueObjs  数据对象数组
     * @param ignoreDuplicatedKeyError 是否忽略重复的主键错误
     * @return 
     */
    public String generateInsertMultiSQL( CValue[] theValueObjs, boolean ignoreDuplicatedKeyError ) {
        Field[] flds = this.correspondValueObj.getFields();
        StringBuilder SQL = new StringBuilder();
        SQL.append("insert " + ( ignoreDuplicatedKeyError?"IGNORE":"" ) + " into " + this.tableName + "(");
        for (int i = 0; i < flds.length; i++) {
            if (i > 0) {
                SQL.append(",");
            }
            SQL.append(flds[i].getName());
        }
        SQL.append(") values");
        for( int V = 0; V < theValueObjs.length; V++ ) {
            if( V> 0 ) SQL.append(",");
            SQL.append("(");
            for (int i = 0; i < flds.length; i++) {
                if (i > 0) {
                    SQL.append(",");
                }
                SQL.append(this.getFieldValue(theValueObjs[V], flds[i]));
            }
            SQL.append(")");

        }
        return SQL.toString();        
    }

    /**
     * 根据实体类配置条件
     * @param theValueObj
     * @return  and A in ( 'a1' ) and B in ( b1 ) ... and N in ( 'n1' )
     */
    public String generateWhereSQL(CValue theValueObj) {
        return generateWhereSQL(new CValue[]{theValueObj});
//        Field[] flds = this.correspondValueObj.getFields();
//        StringBuilder SQL = new StringBuilder();
//        String _temp = null;
//        // SQL :  and AAA='aaa' and BBB='bbb' ... and NNN='nnn'
//        for (int i = 0; i < flds.length; i++) {
//            _temp = this.getFieldValue(theValueObj, flds[i]);
//            // int 默认值 0 去掉 --- 将 int 转为 Integer 在ICanExport里面 long转Integet会出问题
//            if (_temp != null && !("0".equals(_temp) || "".equals(_temp) || "''".equals(_temp))) {
//                //SQL += " and xxx=ooo "
//                SQL.append(" and ");
//                SQL.append(flds[i].getName());
//                SQL.append(" = ");
//                SQL.append(_temp);
//            }
//        }
//        return SQL.toString();
    }

    /**
     * 根据实体类配置条件
     * @param theValueObjs
     * @return  and A in ( 'a1', 'a2' ) and B in ( b1, b2 ) ... and N in ( 'n1', 'n2' )
     */
    public String generateWhereSQL(CValue[] theValueObjs) {
        Field[] flds = this.correspondValueObj.getFields();
        StringBuilder SQL = new StringBuilder();
        String _temp = null;
        // SQL :  and A in ( 'a1', 'a2' ) and B in ( b1, b2 ) ... and N in ( 'n1', 'n2' )
        for (int i = 0; i < flds.length; i++) {
            boolean addColumn = true;
            //SQL += and A in ( 'a1', 'a2' ) or and B in ( b1, b2 )
            for (int j = 0; j < theValueObjs.length; j++) {
                if (null == theValueObjs[j]) {
                    continue;
                }
                _temp = this.getFieldValue(theValueObjs[j], flds[i]);
                // int 默认值 0 去掉 --- 将 int 转为 Integer 在ICanExport里面 long转Integet会出问题
                if (null != _temp && !("0".equals(_temp) || "".equals(_temp) || "''".equals(_temp))) {
                    if (addColumn) {
                        SQL.append(" and ");
                        SQL.append(flds[i].getName());
                        SQL.append(" in ( ");
                        addColumn = false;
                    }else{
                        SQL.append(" , ");
                    }
                    SQL.append(_temp);
                }
            }
            if (!addColumn) {
                SQL.append(" ) ");
            }
        }
        return SQL.toString();
    }

    /**
     * 根据对象里面的值多条件查询
     * @param obj
     * @return 
     */
    public Vector<HashMap> findByCValue(CValue theValueObj) {
        StringBuilder SQL = new StringBuilder();
        SQL.append("select * from ").append(this.tableName).append(" where 1=1 ");
        SQL.append(this.generateWhereSQL(theValueObj));
        return this.doSpecialThing(SQL.toString());
    }

    /**
     * 根据对象里面的值多条件删除
     * @param obj
     * @return 
     */
    public boolean deleteByCValue(CValue obj) {
        StringBuilder SQL = new StringBuilder();
        SQL.append("delete from ").append(this.tableName).append(" where 1=1 ");
        SQL.append(this.generateWhereSQL(obj));
        return this.doSpecialThingWithoutResult(SQL.toString());
    }

    public String generateDeleteSQL(CValue theValueObj, String tableName) {
        StringBuilder SQL = new StringBuilder();
        SQL.append("delete from " + tableName + " where ");
        String[] keyColumns = this.idColumnName.split(",");
        Field[] flds = this.correspondValueObj.getFields();
        for (int i = 0; i < keyColumns.length; i++) {
            if (i > 0) {
                SQL.append(" and ");
            }
            for (int k = 0; k < flds.length; k++) {
                if (flds[k].getName().equalsIgnoreCase(keyColumns[i])) {
                    SQL.append(flds[k].getName());
                    SQL.append(" = ");
                    SQL.append(this.getFieldValue(theValueObj, flds[k]));
                }
            }
        }
        return SQL.toString();
    }

    public String generateUpdateSQL(CValue oldValueObj, CValue newValueObj, String tableName) {
        StringBuilder SQL = new StringBuilder();
        String[] keyColumns = this.idColumnName.split(",");
        SQL.append("update " + tableName + " set ");
        Field[] flds = this.correspondValueObj.getFields();
        boolean alreadySet = false;
        for (int i = 0; i < flds.length; i++) {
            if (!(this.idColumnName.toLowerCase().indexOf(flds[i].getName().toLowerCase()) >= 0)) {
                if (alreadySet) {
                    SQL.append(",");
                }
                alreadySet = true;
                SQL.append(flds[i].getName() + "=");
                SQL.append(this.getFieldValue(newValueObj, flds[i]));
            }
        }

        SQL.append(" where ");
        for (int i = 0; i < keyColumns.length; i++) {
            if (i > 0) {
                SQL.append(" and ");
            }
            for (int k = 0; k < flds.length; k++) {
                if (flds[k].getName().equalsIgnoreCase(keyColumns[i])) {
                    SQL.append(flds[k].getName());
                    SQL.append(" = ");
                    SQL.append(this.getFieldValue(oldValueObj, flds[k]));
                }
            }
        }
        return SQL.toString();

    }
    // this function is used to perform INSERT and DELETE operation

    public String generateSQL(CValue theValueObj, String Operation) {
        String SQL = null;
        if (Operation.equalsIgnoreCase("insert")) {
            SQL = this.generateInsertSQL(theValueObj, this.tableName);
        } else if (Operation.equalsIgnoreCase("delete")) {
            SQL = this.generateDeleteSQL(theValueObj, this.tableName);
        }
        return SQL;
    }

    // this function is used to perform UPDATE operation
    public String generateSQL(CValue theOldValueObj, CValue theNewValueObj, String Operation) {
        String SQL = null;
        if (Operation.equalsIgnoreCase("update")) {
            SQL = this.generateUpdateSQL(theOldValueObj, theNewValueObj, this.tableName);
        }
        return SQL;
    }

    // this function is used to perform simple conditional FIND operation
    public String generateSQL(String theKey, String theValue, String Operation) {
        String SQL = null;
        if (Operation.equalsIgnoreCase("findbyid")) {
            SQL = "select * from " + this.tableName + " where " + theKey + " = '" + theValue + "'  order by " + this.idColumnName;
        }
        return SQL;
    }

    // this function is used to perform simple conditional FIND operation
    public String generateSQL(String theKey, int theValue, String Operation) {
        String SQL = null;
        if (Operation.equalsIgnoreCase("findbyid")) {
            SQL = "select * from " + this.tableName + " where " + theKey + " = " + String.valueOf(theValue) + "  order by " + this.idColumnName;
        }
        return SQL;

    }

    // this function is used to perform getting CValue operation
    //public abstract CValue getCValueByRs(ResultSet myRs) throws SQLException;
    public Vector<CValue> findById(int i) {
        return this.doFind(this.idColumnName, i, "findbyid");
    }
    public CValue findById_Single(int i) {
        Vector<CValue> doFind = this.doFind(this.idColumnName, i, "findbyid");
        if( doFind.size() > 0 )
            return doFind.get(0);
        else 
            return null;
    } 
    public Vector<CValue> findById(String i) {
        return this.doFind(this.idColumnName, i, "findbyid");
    }
    public CValue findById_Single(String i) {
        Vector<CValue> doFind = this.doFind(this.idColumnName, i, "findbyid");
        if( doFind.size() > 0 )
            return doFind.get(0);
        else 
            return null;
    }    

    public boolean insert(CValue theValueObj) {
        return this.doQuery(theValueObj, "insert");
    }

    public boolean insert(CValue[] theValueObj) {
        return this.doQuery(theValueObj, "insert");
    }

    public boolean update(CValue theOldValueObj, CValue theNewValueObj) {
        return this.doQuery(theOldValueObj, theNewValueObj, "update");
    }

    public boolean delete(CValue theValueObj) {
        return this.doQuery(theValueObj, "delete");
    }

    public boolean delete(CValue[] theValueObj) {
        return this.doQuery(theValueObj, "delete");
    }

    /**********************************WIth Cn ********************************/
    public Vector<CValue> findById(int i, Connection myCn) {
        return this.doFind(this.idColumnName, i, "findbyid", myCn);
    }
    
    public CValue findById_Single(int i, Connection myCn) {
        Vector<CValue> doFind = this.doFind(this.idColumnName, i, "findbyid", myCn);
        if( doFind.size() > 0 ) return doFind.get(0);
        return null;
    }

    
    public Vector<CValue> findById(String i, Connection myCn) {
        return this.doFind(this.idColumnName, i, "findbyid", myCn);
    }
    public CValue findById_Single(String i, Connection myCn) {
        Vector<CValue> doFind = this.doFind(this.idColumnName, i, "findbyid", myCn);
        if( doFind.size() > 0 ) return doFind.get(0);
        return null;
    }
    public boolean insert(CValue theValueObj, Connection myCn) {
        return this.doQuery(theValueObj, "insert", myCn);
    }

    public boolean insert(CValue[] theValueObj, Connection myCn) {
        return this.doQuery(theValueObj, "insert", myCn);
    }

    public boolean update(CValue theOldValueObj, CValue theNewValueObj, Connection myCn) {
        return this.doQuery(theOldValueObj, theNewValueObj, "update", myCn);
    }

    public boolean delete(CValue theValueObj, Connection myCn) {
        return this.doQuery(theValueObj, "delete", myCn);
    }

    public boolean delete(CValue[] theValueObj, Connection myCn) {
        return this.doQuery(theValueObj, "delete", myCn);
    }

    /**
     * 使用doSepcialThing获得的是一个Hashmap的Vector，
     * 使用本方法把这样的一个数组中的指定的字段的值提取出来，做成一个数组
     * @param S
     * @param Key
     * @return
     */
    protected static String[] getStrArrayFromHM(Vector S, String Key) {
        String[] R = new String[S.size()];
        for (int i = 0; i < R.length; i++) {
            R[i] = (String) ((HashMap) S.get(i)).get(Key.toLowerCase());
        }
        return R;
    }

    /**
     * 使用doSepcialThing获得的是一个Hashmap的Vector，
     * 使用本方法把这样的一个数组中的指定的字段的值提取出来，做成一个数组
     * @param S
     * @param Key
     * @return
     */
    protected static Vector<String> getStrVectorFromHM(Vector S, String Key) {
        Vector R = new Vector();
        for (int i = 0; i < S.size(); i++) {
            R.add(i, (String) ((HashMap) S.get(i)).get(Key.toLowerCase()));
        }
        return R;
    }

    private void NOP() {
    }

    /**
     * 使用doSepcialThing获得的是一个Hashmap的Vector，使用本方法把其中一个HashMap转换成一个CValue对象
     * @param hm
     * @return
     */
    public CValue getValueObjByHashMap(HashMap hm) {
        try {
            Object R = this.correspondValueObj.newInstance();
            if (!(R instanceof CValue)) {
                throw new Exception("Type Error");
            }
            Field[] fields = ICanExport.getFields(correspondValueObj);
            for (int i = 0; i < fields.length; i++) {
                Field oneField = fields[i];
                oneField.setAccessible(true);

                String type = oneField.getType().getName().toLowerCase();
                if (oneField.getType().isPrimitive()) {
                    Object v = hm.get(oneField.getName().toLowerCase());
                    if ((type.indexOf("integer") >= 0) || (type.indexOf("int") >= 0)) {
                        try {
                            oneField.setInt(R, ((Long) v).intValue());
                        } catch (Exception ne) {
                            NOP();
                        }
                    } else if (type.indexOf("long") >= 0) {
                        try {
                            oneField.setLong(R, ((Long) v).longValue());
                        } catch (Exception ne) {
                            NOP();
                        }
                    } else if (type.indexOf("short") >= 0) {
                        try {
                            oneField.setShort(R, ((Long) v).shortValue());
                        } catch (Exception ne) {
                            NOP();
                        }
                    } else if (type.indexOf("float") >= 0) {
                        try {
                            oneField.setFloat(R, ((BigDecimal) v).shortValue());
                        } catch (Exception ne) {
                            NOP();
                        }
                    } else if (type.indexOf("double") >= 0) {
                        try {
                            oneField.setDouble(R, ((BigDecimal) v).doubleValue());
                        } catch (Exception ne) {
                            NOP();
                        }
                    }
                } else {
                    Object v = hm.get(oneField.getName().toLowerCase());
                    if (v != null) {
                        oneField.set(R, v);
                    }
                }


            }
            return (CValue) R;
        } catch (Exception excp) {
            excp.printStackTrace();
            return null;
        }
    }

    public CValue getCValueByRs(ResultSet myRs) throws SQLException {
        try {
            Object R = this.correspondValueObj.newInstance();
            if (!(R instanceof CValue)) {
                throw new Exception("Type Error");
            }
            Field[] fields = ICanExport.getFields(correspondValueObj);
            for (int i = 0; i < fields.length; i++) {
                Field oneField = fields[i];
                oneField.setAccessible(true);
                String type = oneField.getType().getName().toLowerCase();
                String columnName = oneField.getName().toLowerCase();
                if ((type.indexOf("integer") >= 0) || (type.indexOf("int") >= 0)) {
                    try {
                        oneField.setInt(R, myRs.getInt(columnName));
                    } catch (Exception ne) {
                    }
                } else if (type.indexOf("long") >= 0) {
                    try {
                        oneField.setLong(R, myRs.getLong(columnName));
                    } catch (Exception ne) {
                    }
                } else if (type.indexOf("short") >= 0) {
                    try {
                        oneField.setShort(R, myRs.getShort(columnName));
                    } catch (Exception ne) {
                    }
                } else if (type.indexOf("string") >= 0) {
                    try {
                        oneField.set(R, myRs.getString(columnName));
                    } catch (Exception ne) {
                    }
                } else if (type.indexOf("float") >= 0) {
                    try {
                        oneField.setFloat(R, myRs.getFloat(columnName));
                    } catch (Exception ne) {
                    }
                } else if (type.indexOf("double") >= 0) {
                    try {
                        oneField.setDouble(R, myRs.getDouble(columnName));
                    } catch (Exception ne) {
                    }
                }

            }
            return (CValue) R;
        } catch (Exception excp) {
            return null;
        }
    }

    /**
     * 使用doSpecifalThing获得的是一个HashMap的Vector,使用本方法可以将其转换为一个CValue的数组
     * @param hms
     * @return
     */
    public <T> T[] getCValuesFromSpecifiedOpt(Vector hms, T[] template) {
        T[] r = Arrays.copyOf(template, hms.size());
        for (int i = 0; i < r.length; i++) {
            r[i] = (T) this.getValueObjByHashMap((HashMap) hms.get(i));
        }
        return r;
    }

    /** 用于调试时使用的 连接池初始化
     * 
     */
    public static void prepareConnectionPool() {
        // Init Pool Progress;
        CPrjPool CP = CPrjPool.getInstance();
        CP.initConnectionPool(systemConstants.dbConfigFile);
        CP.reloadConfig();
        try {
            CP.initPool();
        } catch (CPoolException ex) {
        }
    }

    /** 用于调试时使用的 连接池关闭
     *
     */
    public static void terminalConnectionPool() {
        CPrjPool.getInstance().clearPool();

    }

    /**
     * 执行一个事务处理，需要使用一个opt对象预先定义好事务中的各步操作
     * @param cnpool
     * @param opt
     * @return
     */
    public static boolean transactionOperate(CAbsConnectionPool cnpool, ITransactionOperate opt) {
        Connection cn = null;
        Statement st = null;
        try {

            cn = (Connection) cnpool.getPoolItem();
            cn.setAutoCommit(false);

            boolean success = opt.operate(cn);
            if (!success) {
                throw new Exception("Error when operate " + opt.getMessage());
            }

            //没有出错的话就提交事务，而后返回
            cn.commit();
            cn.setAutoCommit(true);

            cnpool.freePoolItem(cn);
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            if (st != null) {
                try {
                    st.close();
                } catch (Exception ee) {
                    ee.printStackTrace();
                }
            }
            if (cn != null) {
                try {
                    cn.rollback();
                } catch (Exception ee) {
                    ee.printStackTrace();
                }
                try {
                    cn.setAutoCommit(true);
                } catch (Exception ee) {
                    ee.printStackTrace();
                }
                cnpool.freePoolItem(cn);
            }
            return false;
        }
    }
}
/********************************Sample******************************************
@Override
public String generateSQL(CValue theValueObj, String Operation) {
CValue_Priv_Define v = (CValue_Priv_Define) theValueObj;
if( Operation.equalsIgnoreCase("insert") ) {
String SQL = "insert into Priv_Define(priv_Category, priv_id, priv_name, priv_value_type ) values(" +
"'" + v.Priv_Category + "'," +
"'" + v.Priv_Id + "'," +
"'" + v.Priv_Name + "'," +
"'" + v.Priv_Value_Type + "')";
return SQL;
}
else if( Operation.equalsIgnoreCase("delete")) {
String SQL = "delete from priv_define where priv_id = '" + v.Priv_Id + "'";
return SQL;
}
else
return null;
}

// this function is used to perform UPDATE operation
@Override
public String generateSQL(CValue theOldValueObj, CValue theNewValueObj, String Operation) {
CValue_Priv_Define oldV = (CValue_Priv_Define) theOldValueObj;
CValue_Priv_Define newV = (CValue_Priv_Define) theNewValueObj;
if(Operation.equalsIgnoreCase("update")) {

}
else {
return null;
}
}

// this function is used to perform simple conditional FIND operation
@Override
public String generateSQL(String theKey, String theValue, String Operation) {
String SQL = null;
if( Operation.equalsIgnoreCase("findbyid") ) {
SQL = "select * from Priv_define where " + theKey + " = '" + theValue + "'  order by Priv_Category , Priv_Id asc";
}
return SQL;

}

// this function is used to perform simple conditional FIND operation
@Override
public String generateSQL(String theKey, int theValue, String Operation) {
String SQL = null;
if( Operation.equalsIgnoreCase("findbyid") ) {

}
return null;
}


//典型的事务处理的程序段
Connection cn = null;
Statement st = null;
try {

cn = (Connection) dao.thisCnPool.getPoolItem();
cn.setAutoCommit( false );

//删除掉旧有的权限
boolean success = dao.deleteByEntity( entityId , cn);
if( ! success ) throw new Exception("Delete old role privs error." );

//增加入新的权限数据
success = dao.insert(privs, cn);
if( ! success ) throw new Exception("Delete old role privs error." );

//没有出错的话就提交事务，而后返回
cn.commit();
cn.setAutoCommit( true );

dao.thisCnPool.freePoolItem( cn );
return true;
}
catch( Exception ex ) {
ex.printStackTrace();
if( st != null ) try{ st.close(); } catch( Exception ee ) { ee.printStackTrace(); }
if( cn != null ) {
try {  cn.rollback(); }  catch( Exception ee ) { ee.printStackTrace(); }
try {  cn.setAutoCommit(true); }  catch( Exception ee ) { ee.printStackTrace(); }
dao.thisCnPool.freePoolItem( cn );
}
return false;
}
 */
