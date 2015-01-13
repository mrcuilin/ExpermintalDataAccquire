/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DdatAcquire.View;

import DdatAcquire.DAO.CDao_SampleData;
import DdatAcquire.DAO.CValue_SampleData;
import General.HashMap;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Vector;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author cuilin
 */
public class CGetGraphData extends CAbstractController {

    double SP_maxV, SP_minV,  SP_Range;     // SP, PV is in same bound
    double OP_maxV, OP_minV,  OP_Range;     // 100 in default
    long beginTime, endTime;
 
    
    /**
     *
     * @param hsr
     * @param hsr1
     * @return
     * @throws Exception
     */
    @Override
    public ModelAndView handleRequest(HttpServletRequest hsr, HttpServletResponse hsr1) throws Exception {
        super.handleRequest(hsr, hsr1);
        
        String nodeName = req.getParameter("NodeName");
        String fromTime = req.getParameter("FromTime");
        String toTime = req.getParameter("ToTime");

        String SQL = "select * from sampledata where DataNode = '" + nodeName + "' ";
        if( fromTime != null && (!fromTime.trim().equals(""))) SQL += " and SampleTime >= '" + fromTime + "' ";
        if( toTime != null && (!toTime.trim().equals(""))) SQL += " and SampleTime <= '" + toTime + "' ";
        
        CDao_SampleData dao = new CDao_SampleData();
        Vector<HashMap> raw = dao.doSpecialThing(SQL);
        CValue_SampleData[] r = new CValue_SampleData[ raw.size() ];
        r = dao.getCValuesFromSpecifiedOpt(raw, r);
        
        this.getBound(r);
        
        res.setContentType("text/html");
        OutputStream ost = res.getOutputStream();
        for( int i = 0; i < r.length; i++ ) {
            ost.write( ( r[i].SampleTime + "  SP :" + r[i].SP_DataValue + "  OP:" + r[i].OP_DataValue + "  PV:" + r[i].PV_DataValue + "\r\n").getBytes("UTF-8"));
            ost.flush();
        }
        ost.flush();
        ost.close();
        
        return null;
    }
    
    private void getBound( CValue_SampleData[] d ) throws ParseException {
        //double SP_maxV, SP_minV, SP_ratio, OP_maxV, OP_minV, OP_ratio;
        SP_maxV = Double.NEGATIVE_INFINITY;
        SP_minV = Double.POSITIVE_INFINITY;
        OP_maxV = Double.NEGATIVE_INFINITY;
        OP_minV = Double.POSITIVE_INFINITY;

        beginTime = Long.MAX_VALUE;
        endTime = 0;

        double SP = Double.POSITIVE_INFINITY, OP=Double.POSITIVE_INFINITY , PV=Double.POSITIVE_INFINITY ;
        for( int i = 0; i < d.length; i++ ) {
            try {   SP = Double.parseDouble( d[i].SP_DataValue );  } catch(Exception e){}
            try {   OP = Double.parseDouble( d[i].OP_DataValue );  } catch(Exception e){}
            try {   PV = Double.parseDouble( d[i].PV_DataValue );  } catch(Exception e){}            
            
            if( SP_maxV < SP ) SP_maxV = SP;
            if( SP_minV > SP ) SP_minV = SP;
            if( SP_maxV < PV ) SP_maxV = PV;
            if( SP_minV > PV ) SP_minV = PV;
            
            /*
            if( OP_maxV < OP ) OP_maxV = OP;
            if( OP_minV > OP ) OP_minV = OP;
            */
            OP_maxV = 100;
            OP_minV = 0;
                    
            long t = getTimeInMS(d[i].SampleTime );
            if( beginTime > t ) beginTime = t;
            if( endTime < t ) endTime = t;
        }
        
        SP_Range = SP_maxV - SP_minV;
        OP_Range = OP_maxV - OP_minV;
                
    }

    public long getTimeInMS( String date ) throws ParseException {
        String[] sd = date.split("\\.");
        SimpleDateFormat df = (SimpleDateFormat) SimpleDateFormat.getDateTimeInstance();
        long time = df.parse( sd[0].replaceAll("/", "-") ).getTime();
        if( sd.length > 1 ) {
            time += Integer.parseInt( sd[1] );
        }
        return time;
    }    
}
