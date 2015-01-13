/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DdatAcquire.DAO;

import General.CUtils;
import General.DAOs.CDao;
import General.DAOs.CValue;
import ProjectSpecified.baseDAOs.PrjDAO;
import ProjectSpecified.systemConstants;
import java.util.Calendar;

/**
 *
 * @author cuilin
 */
public class CDao_SampleData extends PrjDAO {

    @Override
    protected void setValueObjClass() {
        this.correspondValueObj = CValue_SampleData.class;
    }

    @Override
    protected void setTableName() {
        this.tableName = "sampledata";
    }

    @Override
    protected void setKeyColumns() {
        this.idColumnName = "DataNode,SampleTime";
    }
    
    public static void main(String[] args) {
        CDao_SampleData dao = new CDao_SampleData();
        systemConstants.dbConfigFile = "D:\\微云网盘\\10143197\\Java\\DataAcquire\\web\\WEB-INF\\CustomConfigs\\SystemConf.xml";
        CDao.prepareConnectionPool();
        
        
        
        for( int i = 0; i < 500; i++ ) {
            String IX = String.valueOf(i);
            while( IX.length() < 5 ) IX = "0" + IX;
            IX = IX.substring(1,4);
            String ID = CUtils.getDateStr( Calendar.getInstance() );
            CValue_SampleData d = new CValue_SampleData();
            d.DataNode = "node1";
            d.SampleTime = ID;

            double pv = Math.sin( 2 * Math.PI / 1000 * 4 * i ) * 100;
            double sp = Math.cos( 2 * Math.PI / 1000 * 6 * i ) * 80;
            double op = Math.min(pv, sp) / 2;
            d.OP_DataValue = String.valueOf(op);
            d.PV_DataValue = String.valueOf(pv);
            d.SP_DataValue = String.valueOf( sp );
            
            d.OP_App = "";
            d.OP_RawValue = "";
            
            d.SP_App = "";
            d.SP_RawValue = "";
            d.PV_App = "";
            d.PV_RawValue = "";
            
            dao.insert(d);
        }
        
        CDao.terminalConnectionPool();
    }
    
}
