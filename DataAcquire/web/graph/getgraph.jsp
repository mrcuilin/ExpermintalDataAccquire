<%-- 
    Document   : getgraph
    Created on : 2014-10-27, 23:20:37
    Author     : cuilin
--%>

<%@page import="java.util.Calendar"%>
<%@page import="General.CUtils"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    <link href="../modulePublic/tableAndButtons.css" rel="stylesheet" type="text/css">
    <link href="../modulePublic/TabAndBorder.css" rel="stylesheet" type="text/css">
    </head>
    <script language="javascript" src="../pub/scripts/jquery-1.4.2.js" ></script>
    <script language="javascript" src="../pub/scripts/jquery.corner.js"></script>
    <script language="javascript" src="../pub/scripts/myUtilities.js"></script>
    <script language="javascript" >
        $(document).ready( function() {
            $("#showButton").corner({radix:"5px"});
            $("#downloadButton").corner({radix:"5px"});
            $("#autoReloadButton").corner({radix:"5px"});
            
        })
    </script>
    <%
        Calendar d = Calendar.getInstance();
        Calendar dold = Calendar.getInstance();
        dold.setTimeInMillis( d.getTimeInMillis() - 3600000 );
        String now = CUtils.getDateStr( d );
        String prev = CUtils.getDateStr( dold );
        %>
    <body>
    	<form action="showGraph.ko" method="post" target="result">
        <div style="float:none; clear:both;">
            <div style="float:left;">From Time (YYYY-MM-DD HH:MM:SS )<input type="text" name="FromTime" id="FromTime" value="<%= prev %>">&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;</div>
            <div style="float:left;">To Time (YYYY-MM-DD HH:MM:SS ) <input type="text" name="ToTime" id="ToTime" value="<%= now %>">&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;</div>
            <div style="float:left;">Nodes
                    <select name="NodeName" id="NodeName">
                        <option value="NODE1">NODE 1</option>
                        <option value="NODE2">NODE 2</option>
                        <option value="NODE3">NODE 3</option>
                    </select></div>
            <div onclick="showGRAPH()" class="BlueTab" style="float:left;padding:3px;margin-left:8px; cursor: pointer;" id="showButton">&nbsp;&nbsp;&nbsp;SHOW&nbsp;&nbsp;&nbsp;</div>
            <div onclick="showGRAPHData()" class="BlueTab" style="float:left;padding:3px;margin-left:8px; cursor: pointer;" id="showDataButton">&nbsp;&nbsp;&nbsp;SHOW Data&nbsp;&nbsp;&nbsp;</div>            
            <div onclick="download()"  class="BlueTab" style="float:left;padding:3px;margin-left:8px; cursor: pointer;" id="downloadButton">&nbsp;&nbsp;&nbsp;下载数据（全部采集点）&nbsp;&nbsp;&nbsp;</div>
        </div>
            <hr style="float:none; clear:both;" />
        <div align="center" style="float:left;clear:both;">
                <div style="float:left;">最近的<input type="text" name="Interval" id="Interval" value="" size="4">秒&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;</div>
                <div style="float:left;">Nodes
                        <select name="NodeName" id="NodeName">
                            <option value="NODE1">NODE 1</option>
                            <option value="NODE2">NODE 2</option>
                            <option value="NODE3">NODE 3</option>
                        </select>&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;</div>
                <div style="float:left;">                
                    <div onclick="beginRefresh()" class="BlueTab" style="float:left; padding:3px;margin-left:8px; cursor: pointer;" id="autoReloadButton">&nbsp;&nbsp;&nbsp;  AUTO Reload  &nbsp;&nbsp;&nbsp;</div>
        </div>
            <hr style="float:none; clear:both;" />
        </form>
        <img id="XX" src="" align="center" />
            
    </body>
    <script language="javascript">
        var winWidth = 0;
        var winHeight = 0;
        
        var playing = false;
        
        function findDimensions() //函数：获取尺寸
        {
                // 获取窗口宽度
                if (window.innerWidth)
                        winWidth = window.innerWidth;
                else if ((document.body) && (document.body.clientWidth))
                        winWidth = document.body.clientWidth;
                // 获取窗口高度
                if (window.innerHeight)
                        winHeight = window.innerHeight;
                else if ((document.body) && (document.body.clientHeight))
                        winHeight = document.body.clientHeight;

                // 通过深入Document内部对body进行检测，获取窗口大小
                if (document.documentElement  && document.documentElement.clientHeight && document.documentElement.clientWidth)
                {
                    winHeight = document.documentElement.clientHeight;
                    winWidth = document.documentElement.clientWidth;
                }
        }        
        
        function showGRAPHData() {
            findDimensions();
            playing = false;
            $("#XX")[0].src = "../pub/img/spacer.gif";
            var URI = "showGraphData.ko?FromTime=" + $("#FromTime")[0].value + "&" +
                    "ToTime=" + $("#ToTime")[0].value + "&" + 
                    "NodeName=" + $("#NodeName").val() + "&W=" + ( winWidth - 10 ) +
                    "&H=" + (winHeight - 150) + "&R=" + getUniqueId();
            self.open( URI );
        }
        
        function showGRAPH() {
            findDimensions();
            playing = false;
            $("#XX")[0].src = "../pub/img/spacer.gif";
            var URI = "showGraph.ko?FromTime=" + $("#FromTime")[0].value + "&" +
                    "ToTime=" + $("#ToTime")[0].value + "&" + 
                    "NodeName=" + $("#NodeName").val() + "&W=" + ( winWidth - 10 ) +
                    "&H=" + (winHeight - 150) + "&R=" + getUniqueId();
            $("#XX")[0].src = URI;
        }
        
        function download() {
            playing = false;
            var URI = "downloadExcel.ko?FromTime=" + $("#FromTime")[0].value + "&" +
                    "ToTime=" + $("#ToTime")[0].value + "&" + 
                    "R=" + getUniqueId();
            self.open( URI,"Download Excel" + getUniqueId() );
        }        
        function beginRefresh() {
            if( ! isNaN( parseInt( $("#Interval").val()) ) ) {
                playing = true;

                window.setTimeout( reload, 2000 );
            }
        }
        
        function reload() {
            if( ! playing ) {
                showGRAPH();
                return;
            }
             var DD = new Date();
            DD.setTime( DD.getTime() - parseInt( $("#Interval").val()) * 1000 );
            var Y = DD.getFullYear() + "";
            var M = (DD.getMonth() + 1) + "";
            if( M.length < 2 ) M= "0" + M;
            var D = DD.getDate() + "";
            if( D.length < 2 ) D= "0" + D;
            var H = DD.getHours() + "";
            if( H.length < 2 ) H= "0" + H;
            var Min = DD.getMinutes() + "";
            if( Min.length < 2 ) Min= "0" + Min;
            var S = DD.getSeconds() + "";
            if( S.length < 2 ) S= "0" + S;
            
            var FromTime = Y + "/" + M + "/" + D + " " + H + ":" + Min + ":" + S;
            
            findDimensions();
            $("#XX")[0].src = "../pub/img/spacer.gif";
            var URI = "showGraph.ko?FromTime=" + FromTime + "&" +
                    "NodeName=" + $("#NodeName").val() + "&W=" + ( winWidth - 10 ) +
                    "&H=" + (winHeight - 150) + "&R=" + getUniqueId();
            $("#XX")[0].src = URI;            
            window.setTimeout( reload, 2000 );
        }
    </script>
</html>
