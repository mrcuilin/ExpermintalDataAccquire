/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DdatAcquire.View;

import DdatAcquire.DAO.CDao_SampleData;
import DdatAcquire.DAO.CValue_SampleData;
import General.DAOs.CValue;
import General.HashMap;
import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.input.DataFormat;
import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

/**
 *
 * @author cuilin
 */
public class CRenderGraph extends CAbstractController {
    double SP_maxV, SP_minV,  SP_Range;     // SP, PV is in same bound
    double OP_maxV, OP_minV,  OP_Range;     // 100 in default
    long beginTime, endTime;
    
    int picWidth = 1024 , picHeight = 480, picMargin = 5, textWidth = 80, textHeight = 20;
    
    int offsetX = picMargin + textWidth;
    int offsetY = picMargin + textHeight;
    
    public class POS {
        public int X;
        public int Y;
    }
    
    public class POSies {
        public POS opc; 
        public POS sp;
        public POS pv;
    }
    
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
        ServletOutputStream os = res.getOutputStream();
        
        String nodeName = req.getParameter("NodeName");
        String fromTime = req.getParameter("FromTime");
        String toTime = req.getParameter("ToTime");
        
        String W = req.getParameter("W");
        if(W != null && !W.equals("")) this.picWidth = Integer.parseInt(W) - 2 * this.picMargin - textWidth;
        String H = req.getParameter("H");        
        if(W != null && !W.equals("")) this.picHeight = Integer.parseInt(H) - 2 * this.picMargin - textHeight;
        
        String SQL = "select * from sampledata where DataNode = '" + nodeName + "' ";
        if( fromTime != null && (!fromTime.trim().equals(""))) SQL += " and SampleTime >= '" + fromTime + "' ";
        if( toTime != null && (!toTime.trim().equals(""))) SQL += " and SampleTime <= '" + toTime + "' ";
        
        CDao_SampleData dao = new CDao_SampleData();
        Vector<HashMap> raw = dao.doSpecialThing(SQL);
        CValue_SampleData[] r = new CValue_SampleData[ raw.size() ];
        r = dao.getCValuesFromSpecifiedOpt(raw, r);
        
        this.getBound(r);
        BufferedImage graph = this.drawGraph(r);
        this.publshChart(graph);
        
        return null;
    }
    
    /**
     
     * @param image
     * @param width
     * @param height 
     */
    public void publshChart( BufferedImage image ) {
            try {
                res.setContentType("image/png");
                res.setHeader("Content-Type", "application/octet-stream");
                res.setHeader("Content-Disposition","attachment;  filename=\"x.png\""  );
                OutputStream ost = res.getOutputStream();

                //JFreeChart chart = this.createChart( this.createDataset() );

                ImageIO.write(image, "png", ost );

                ost.close();
            } catch (IOException ex) {
                
            } 
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
    
    public int getPosY( double v , double maxV, double minV ) {
        double range = maxV - minV;
        int Y = (int) Math.round( ( (v - minV) /  range ) * ( this.picHeight / 1.2 ) + ( this.picHeight * 0.1 / 1.2 ) );
        return ( this.picHeight - Y ) + this.picMargin;
    }
    
    public int getPosX( long v , long maxV, long minV ) {
        long range = maxV - minV;
        int X = (int) Math.round ( (1.0f * ( v - minV )) / range  * this.picWidth );
        return X + offsetX ; 
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
    
    public POSies getSPPoss( CValue_SampleData d ) throws Exception {
        POSies r = new POSies();

        r.opc = new POS();
        r.opc.X = this.getPosX( getTimeInMS( d.SampleTime ) , endTime, beginTime );
        r.opc.Y = this.getPosY( Double.parseDouble( d.OP_DataValue ), this.OP_maxV, this.OP_minV );
        
        r.sp = new POS();
        r.sp.X = r.opc.X;
        r.sp.Y = this.getPosY( Double.parseDouble(  d.SP_DataValue ), SP_maxV, SP_minV );

        r.pv = new POS();
        r.pv.X = r.opc.X;
        r.pv.Y = this.getPosY( Double.parseDouble( d.PV_DataValue ), SP_maxV, SP_minV );
        
        return r;
    }
    
    public BufferedImage drawGraph( CValue_SampleData[] d ) {

        
            BufferedImage bi = new BufferedImage( this.picWidth + this.picMargin * 2 + this.textWidth, 
                            this.picHeight + this.picMargin * 2 + this.textHeight , 
                    BufferedImage.TYPE_INT_RGB);
            Graphics2D g2 = (Graphics2D) bi.getGraphics();
            g2.setRenderingHint( RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON );
            g2.setBackground(Color.WHITE);
            //g2.setRenderingHint(null, width);
            g2.clearRect(0, 0, this.picWidth + this.picMargin * 2 + this.textWidth, 
                    this.picHeight + this.picMargin * 2 + this.textHeight );
            
            if( d.length > 3 ) {
                drawCurve(d, g2);
                drawGrid(g2);
            }
            return bi;
    }
    public void drawCurve( CValue_SampleData[] d , Graphics2D g2 ) {
    
            Font titleFont = new Font("Arial", Font.BOLD, 18 );
            Font axisFont = new Font("Arial", Font.PLAIN, 16 );
            Font upperFont = new Font("Arial", Font.PLAIN, 10 );                        
            Font axisFont_EM = new Font("Arial", Font.BOLD , 18 );
            Font upperFont_EM = new Font("Arial", Font.BOLD, 12 );
            //如果是粗图，则显示大一些的SIL标记
            Font silFont = new Font("Arial", Font.BOLD, 24 );
                                
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, 1.0f));     
            
            g2.setStroke( new BasicStroke(2));

            int s = 0;
            POSies start = null;
            
            while( s < d.length && start == null ) {
                
                try {
                    start = this.getSPPoss( d[s] );
                }
                catch( Exception e ) {
                    
                }
                s++;
            }
            for( int i = s; i < d.length; i++ ) {
                POSies point = null;
                try {
                    point = this.getSPPoss( d[i] );
                }
                catch( Exception e ) {}
                if( point == null ) continue;

                g2.setPaint(  Color.BLUE );
                g2.drawLine( start.sp.X , 
                             start.sp.Y , 
                             point.sp.X, 
                             point.sp.Y );

                g2.setPaint(  Color.GREEN );
                g2.drawLine( start.opc.X , start.opc.Y , point.opc.X , point.opc.Y  );

                g2.setPaint(  Color.MAGENTA );
                try {
                    g2.drawLine( start.pv.X , start.pv.Y , point.pv.X , point.pv.Y);
                }
                catch( Exception e ) {
                    e.printStackTrace();
                    System.out.println( i );
                }
                
                start = point;
            }
            
    }
    
    public void drawGrid( Graphics2D g2 ) {
        double RoughGridSize, detailGridSize;
        Font axisFont = new Font("Arial", Font.PLAIN, 16 );
        g2.setFont(axisFont);
        g2.setPaint( Color.BLACK );
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, 0.5f));     
            
        g2.setStroke( new BasicStroke(2));
        g2.drawRect( offsetX ,this.picMargin, picWidth , picHeight );
        
        RoughGridSize = Math.pow(10, Math.round( Math.log10( this.SP_Range )) - 1 );
        while( this.SP_Range / RoughGridSize > 5 )
            RoughGridSize += RoughGridSize;
        detailGridSize = RoughGridSize / 10;
        
        double firstRoughGridValue = RoughGridSize * ( Math.floor( (this.SP_minV - this.SP_Range * 0.1/1.2) * ( 1.0f / RoughGridSize )) + 1.0f); 
        double firstDetailGridValue = detailGridSize * ( Math.floor( (this.SP_minV - this.SP_Range * 0.1/1.2) * ( 1.0f / detailGridSize )) + 1.0f); 
        
        g2.setStroke( new BasicStroke(1));
        g2.setPaint( Color.LIGHT_GRAY );
        while( firstDetailGridValue < ( this.SP_maxV + this.SP_Range * 1.1/1.2 )) {
            double Y = this.getPosY( firstDetailGridValue, SP_maxV, SP_minV);
            g2.drawLine(offsetX, (int)Math.round(Y), offsetX + this.picWidth, (int)Math.round(Y) );
            firstDetailGridValue += detailGridSize;
        }
        
        g2.setStroke( new BasicStroke(1));
        g2.setPaint( Color.BLACK );
        while( firstRoughGridValue < ( this.SP_maxV + this.SP_Range * 1.1/1.2 )) {
            double Y = this.getPosY( firstRoughGridValue, SP_maxV, SP_minV);
            g2.drawLine(offsetX, (int)Math.round(Y), offsetX + this.picWidth, (int)Math.round(Y) );
            String YAxis = String.valueOf(firstRoughGridValue);
            if( YAxis.length() > 8 ) YAxis.substring(0,7 );
            g2.drawString( YAxis, 0, (int)Math.round(Y + this.textHeight / 2 ) );
            firstRoughGridValue += RoughGridSize;
        }
        
        long timeRange = ( this.endTime - this.beginTime ) /1000;
        int roughTimeGridSize, detailTimeGridSize;
        if( timeRange < 60 ) {      // less than 1m, 1s, --
            roughTimeGridSize = 1000;
            detailTimeGridSize = 0;
        }
        else if( timeRange < 900 ) {   // less than 15min,  1min, 10s
            roughTimeGridSize = 60000;
            detailTimeGridSize = 10000;
        }
        else if( timeRange < 3600 ) {   // less than 1 hour,  15min, 1min
            roughTimeGridSize = 900000;
            detailTimeGridSize = 60000;
        }
        else if( timeRange < 8*3600 ) {   // less than 8 hour,  1h, 15min
            roughTimeGridSize = 3600000;
            detailTimeGridSize = 900000;
        }
        else if( timeRange < 24 * 3600 ) { // less than 24 hour,  3h, 30min
            roughTimeGridSize = 3 * 3600000;
            detailTimeGridSize = 1800000;
        }
        else {
            roughTimeGridSize = 24 * 3600000;
            detailTimeGridSize = 3600000;
        }
        //RoughGridSize = this.OP_Range
        
        if( detailTimeGridSize > 0 ) {
            g2.setStroke( new BasicStroke(1));
            g2.setPaint( Color.LIGHT_GRAY );
            firstDetailGridValue = this.beginTime + detailTimeGridSize;

            while( firstDetailGridValue <  this.endTime ) {
                double X = this.getPosX((long) firstDetailGridValue, this.endTime, this.beginTime );
                g2.drawLine((int)Math.round(X), picMargin , (int)Math.round(X), picMargin + picHeight );
                firstDetailGridValue += detailTimeGridSize;
            }
        }
        g2.setStroke( new BasicStroke(1));
        g2.setPaint( Color.BLACK );
        Calendar t = Calendar.getInstance();
        firstRoughGridValue = this.beginTime + roughTimeGridSize;

        while( firstRoughGridValue < this.endTime ) {
            double X = this.getPosX((long) firstRoughGridValue, this.endTime, this.beginTime );
            g2.drawLine((int)Math.round(X), picMargin , (int)Math.round(X), picMargin + picHeight );
            
            t.setTimeInMillis((long) (firstRoughGridValue - this.beginTime));
            String YAxis = String.valueOf( t.getTimeInMillis()/1000);
            
            g2.drawString( YAxis, Math.round(X), this.textHeight + picMargin + picHeight );
            firstRoughGridValue += roughTimeGridSize;

        }
        
    }
    
    public static void main( String[] X ) {
        String a = "0036";
        int b = Integer.parseInt( a );
        System.out.println( b );
    }
}
