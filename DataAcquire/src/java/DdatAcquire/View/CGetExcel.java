/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DdatAcquire.View;

import DdatAcquire.DAO.CDao_SampleData;
import DdatAcquire.DAO.CValue_SampleData;
import General.CUtils;
import General.HashMap;
import java.text.DateFormat;
import java.util.Date;
import java.util.Vector;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import jxl.CellFeatures;
import jxl.CellType;
import jxl.Workbook;
import jxl.format.CellFormat;
import jxl.write.DateTime;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author cuilin
 */
public class CGetExcel extends CAbstractController {
    public ModelAndView handleRequest(HttpServletRequest hsr, HttpServletResponse hsr1) throws Exception {
        super.handleRequest(hsr, hsr1);
            
        String fromTime = req.getParameter("FromTime");
        String toTime = req.getParameter("ToTime");
        
        String SQL = "select * from sampledata where 1=1 ";
        if( fromTime != null && (!fromTime.trim().equals(""))) SQL += " and SampleTime >= '" + fromTime + "' ";
        if( toTime != null && (!toTime.trim().equals(""))) SQL += " and SampleTime <= '" + toTime + "' ";
        SQL += " order by DataNode, SampleTime";
        
        CDao_SampleData dao = new CDao_SampleData();
        Vector<HashMap> raw = dao.doSpecialThing(SQL);
        CValue_SampleData[] r = new CValue_SampleData[ raw.size() ];
        r = dao.getCValuesFromSpecifiedOpt(raw, r);
        res.setContentType("application/vnd.ms-excel");
        res.setHeader("Content-disposition", "attachment; filename=\"DataSummary" +
                fromTime.replaceAll(":","-").replaceAll("/","-") + "----" +
                toTime.replaceAll(":","-").replaceAll("/","-") + ".xls\"");
        
        WritableWorkbook myBook;     
        myBook = Workbook.createWorkbook( res.getOutputStream() );
        
        String currentSheetName = "";
        int currentSheetNum = 0;
        int currentRow = 0;
        WritableSheet currentSheet = null;
        for( int i = 0; i < r.length; i++  ) {
            if( ! currentSheetName.equals( r[i].DataNode )) {
                currentSheet = myBook.createSheet( r[i].DataNode, currentSheetNum );
                currentSheetName = r[i].DataNode;
                currentSheetNum++;
                
                Label cell;
                cell = new Label(0,0,"Sample Time");
                currentSheet.addCell(cell);
                cell = new Label(1,0,"PV");
                currentSheet.addCell( cell );
                cell = new Label(2,0,"PV_RAW");
                currentSheet.addCell( cell );
                cell = new Label(3,0,"SP");
                currentSheet.addCell( cell );
                cell = new Label(4,0,"SP_RAW");
                currentSheet.addCell( cell );
                cell = new Label(5,0,"OP");
                currentSheet.addCell( cell );
                cell = new Label(6,0,"OP_Raw");
                currentSheet.addCell( cell );
                cell = new Label(7,0,"Time Serial");
                currentSheet.addCell( cell );
                

                currentRow = 0;
            }

            currentRow++;
            Label dCell = new Label( 0, currentRow, r[i].SampleTime.replaceAll("/", "-"));
            currentSheet.addCell(dCell);
            
            Date da = DateFormat.getDateTimeInstance().parse(r[i].SampleTime.replaceAll("/", "-") );
            String[] split = r[i].SampleTime.split("\\.");
            if( split.length > 1 ) {
                da.setTime( da.getTime() + Long.parseLong( split[1]) );
            }
            
            long time = da.getTime();
            
            jxl.write.Number nCell;
            nCell = new jxl.write.Number( 1, currentRow, Double.parseDouble( r[i].PV_DataValue )  );
            currentSheet.addCell( nCell );

            nCell = new jxl.write.Number( 2, currentRow, Double.parseDouble( r[i].PV_RawValue )  );
            currentSheet.addCell( nCell );

            nCell = new jxl.write.Number( 3, currentRow, Double.parseDouble( r[i].SP_DataValue )  );
            currentSheet.addCell( nCell );

            nCell = new jxl.write.Number( 4, currentRow, Double.parseDouble( r[i].SP_RawValue )  );
            currentSheet.addCell( nCell );

            nCell = new jxl.write.Number( 5, currentRow, Double.parseDouble( r[i].OP_DataValue )  );
            currentSheet.addCell( nCell );

            nCell = new jxl.write.Number( 6, currentRow, Double.parseDouble( r[i].OP_RawValue )  );
            currentSheet.addCell( nCell );

            nCell = new jxl.write.Number( 7, currentRow, (double)time );
            currentSheet.addCell( nCell );
        }
        myBook.write();
        myBook.close();
        
        res.getOutputStream().flush();
        //res.getOutputStream().close();
        
        return null;
    }
}
