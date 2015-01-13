/*
 * CUtils.java
 *
 * Created on 2006年7月16日, 下午5:51
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package General;

import java.lang.String;
import java.util.Iterator;

import java.io.*;
import java.security.*;
import java.text.Collator;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Cuilin
 */
public class CUtils {
    
    /** Creates a new instance of CUtils */
    public CUtils() {
    }

    public static String getGBStrWithoutFilter(String htmlStr) {
        String Temp = "";
        try {
            byte[] SourceCode = htmlStr.getBytes("ISO8859-1");
            Temp = new String(SourceCode,"utf-8");
        }
        catch (Exception ex) {
        }
        return Temp;
    }
    
    
    /**
     * Get a utf-8 string from a string passed via HTTP
     * The string passed is generally encoded in ISO8859-1
     * This function transformed it to a string in utf-8 code.
     * 把HTTP传入的字符串转换成utf-8格式的。
     * 并且把 , @ 换成 - 
     */
    public static String getGBStr(String htmlStr) {
        String Temp = "";
        try {
            byte[] SourceCode = htmlStr.getBytes("ISO8859-1");
            Temp = new String(SourceCode,"utf-8");
            //Temp = Temp.replace("#","＃"); MayBe can removed, only the ProjectUser uses it feature
            //Filter of the illigeal characters
            Temp = Temp.replace(",",", ");
            Temp = Temp.replace("'","’");
            Temp = Temp.replace("@","-");
            Temp = Temp.replace( "\r", "");
            Temp = Temp.replace( "\n", " " );
            
        }
        catch (Exception ex) {
        }
        return Temp;
    }
    
        public static String getGBStr3(String htmlStr) {
        String Temp = "";
        try {
            byte[] SourceCode = htmlStr.getBytes("ISO8859-1");
            Temp = new String(SourceCode,"utf-8");
            //Temp = Temp.replace("#","＃"); MayBe can removed, only the ProjectUser uses it feature
            //Filter of the illigeal characters
            Temp = Temp.replace(",",", ");
            Temp = Temp.replace("@","-");
            Temp = Temp.replace( "\r", "");
            Temp = Temp.replace( "\n", " " );
        }
        catch (Exception ex) {
        }
        return Temp;
    }
    
    /** to GB Str , no more operation
     * 用于把 QueryString 中的中文字符转换掉（用GET方法传递的或是直接附在URL后边的）
     * # 换成 中文#
     */
   public static String getGBStr2(String htmlStr) {
        String Temp = "";
        try {
            byte[] SourceCode = htmlStr.getBytes("UTF-8");
            //byte[] SourceCode = htmlStr.getBytes("utf-8");
            Temp = new String(SourceCode,"UTF-8");
            //Temp = Temp.replace("#","＃"); MayBe can removed, only the ProjectUser uses it feature
            Temp = Temp.replace("'","’");
            Temp = Temp.replace( "\r", "");
            Temp = Temp.replace( "\n", " " );
            //Filter of the illigeal characters
            //Temp = Temp.replace(",","-");
            //Temp = Temp.replace("@","-");
            //注释掉的原因是在物料、反应等页面中以@作为分隔符
        }
        catch (Exception ex) {
        }
        return Temp;
    }
   /**
    *  把HTTP传入的字符串转换成utf-8格式的。
     * 并且把 ' 换成 中文‘
    * @param src
    * @return
    */
   public static String getGBStrClear( String src ) {
        String Temp = "";
        try {
            byte[] SourceCode = src.getBytes("ISO8859-1");
            Temp = new String(SourceCode,"utf-8");
            //Filter of the illigeal characters
            Temp = Temp.replace("'","’");
            Temp = Temp.replace("\n"," ");
            Temp = Temp.replace("\r", "");
        }
        catch (Exception ex) {
        }
        return Temp;       
   }
   
   public static String getGBStr_OnlyFilterCR( String src ) {
        String Temp = "";
        try {
            byte[] SourceCode = src.getBytes("ISO8859-1");
            Temp = new String(SourceCode,"utf-8");
            //Filter of the illigeal characters
            Temp = Temp.replace("\n"," ");
            Temp = Temp.replace("\r", "");
        }
        catch (Exception ex) {
        }
        return Temp;       
   }
   
   
    public static String getDateStr( Calendar Da ) {
        String Y = String.valueOf(Da.get(Calendar.YEAR));
        while (Y.length() < 4 ) Y = "0" + Y;
        String M = String.valueOf(Da.get(Calendar.MONTH) + 1);
        while (M.length() < 2 ) M = "0" + M;
        String D = String.valueOf(Da.get(Calendar.DAY_OF_MONTH));
        while (D.length() < 2 ) D = "0" + D;
        String H = String.valueOf(Da.get(Calendar.HOUR_OF_DAY));
        while (H.length() < 2 ) H = "0" + H;
        String Min = String.valueOf(Da.get(Calendar.MINUTE));
        while (Min.length() < 2 ) Min = "0" + Min;
        String S = String.valueOf(Da.get(Calendar.SECOND));
        while (S.length() < 2 ) S = "0" + S;
        String MS = String.valueOf(Da.get(Calendar.MILLISECOND));
        while (MS.length() < 3 ) MS = "0" + MS;
        return Y + "-" + M + "-" + D + " " + H + ":" + Min + ":" + S + "." + MS;
    }
    
    public static String getDateStrWithoutSpliter( Calendar Da ) {
        String Y = String.valueOf(Da.get(Calendar.YEAR));
        while (Y.length() < 4 ) Y = "0" + Y;
        String M = String.valueOf(Da.get(Calendar.MONTH) + 1);
        while (M.length() < 2 ) M = "0" + M;
        String D = String.valueOf(Da.get(Calendar.DAY_OF_MONTH));
        while (D.length() < 2 ) D = "0" + D;
        String H = String.valueOf(Da.get(Calendar.HOUR_OF_DAY));
        while (H.length() < 2 ) H = "0" + H;
        String Min = String.valueOf(Da.get(Calendar.MINUTE));
        while (Min.length() < 2 ) Min = "0" + Min;
        String S = String.valueOf(Da.get(Calendar.SECOND));
        while (S.length() < 2 ) S = "0" + S;
        String MS = String.valueOf(Da.get(Calendar.MILLISECOND));
        while (MS.length() < 3 ) MS = "0" + MS;
        return Y + M + D +  H + Min + S + "." + MS;
    }
    
    public static String getNow() {
        Calendar Now = Calendar.getInstance();
        return CUtils.getDateStr( Now );
    }

    public static Calendar getCalendarByString( String timeStr ) {
        Calendar myD = Calendar.getInstance();
        int Y = Integer.parseInt( timeStr.substring(0,4) );
        int M = Integer.parseInt( timeStr.substring(5,7) );
        int D = Integer.parseInt( timeStr.substring(8,10) );
        int H = Integer.parseInt( timeStr.substring(11,13) );
        int Min = Integer.parseInt( timeStr.substring(14,16) );
        int S = Integer.parseInt( timeStr.substring(17,19) );   
        myD.set(Y,M,D,H,Min,S);
        return myD;
    }
    
    public static long getDiffMinute( Calendar D1, Calendar D2 ) {
        return ( D1.getTimeInMillis() - D2.getTimeInMillis() ) / 60000;
    }
    
    public static String getDate() {
        return CUtils.getNow().substring(0,10);
    }
    
    public static String getTime() {
        return CUtils.getNow().substring(11);
    }
    
    public static String getShortTime() {
        return CUtils.getNow().substring(11,19);
    }
    public static String getUniqueId() {
        String Result = CUtils.getNow().replace("-","").replace(":","").replace(" ","").replace(".","");
        Random rnd = new Random();
        String rndStr = String.valueOf(Math.round(rnd.nextDouble() * 1000000));
        while( rndStr.length() < 6 ) rndStr = "0" + rndStr ;
        return Result + rndStr;
    }
    
    //获取一个随机ID, 其中包含用户定义的一个序列号，用来生成随机但是有顺序的一系列UniqueId
    public static String getUniqueSerialId( long serial ) {
        String Result = CUtils.getNow().replace("-","").replace(":","").replace(" ","").replace(".","");
        Result = Result.substring(0,14);
        Random rnd = new Random();
        String rndStr = String.valueOf(Math.round(rnd.nextDouble() * 10000));
        String serialNo = String.valueOf( serial );
        while ( serialNo.length() < 6 ) serialNo = "0" + serialNo;
        while( rndStr.length() < 4 ) rndStr = "0" + rndStr ;       
        return Result + serialNo + rndStr;
    }    
    public static String hex(byte[] array) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < array.length; ++i) {
            sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).toUpperCase().substring(1,3));
        }
        return sb.toString();
    }
    public static String md5 (String message) { 
        try { 
            MessageDigest md = MessageDigest.getInstance("MD5"); 
            return hex (md.digest(message.getBytes("UTF-8"))); 
        } catch (NoSuchAlgorithmException e) { 
        } catch (UnsupportedEncodingException e) { 
        } 
        return null;
    }
    public static String encrypt (String message) { 
        try { 
            MessageDigest md = MessageDigest.getInstance("MD5"); 
            return hex (md.digest(message.getBytes("UTF-8"))); 
        } catch (NoSuchAlgorithmException e) { 
        } catch (UnsupportedEncodingException e) { 
        } 
        return null;
    }

    public static String encrypt (String message, String alg) { 
        try { 
            MessageDigest md = MessageDigest.getInstance(alg); 
            return hex (md.digest(message.getBytes("UTF-8"))); 
        } catch (NoSuchAlgorithmException e) { 
        } catch (UnsupportedEncodingException e) { 
        } 
        return null;
    }    
    public static String filterChar( String src ) {
        String str = src;
        if( src == null ) return "";
       	str = str.replace("'","‘");
	str = str.replace("\"", "“");
        str = str.replace("@", "◎");
        //str = str.replace("-", "－");
        str = str.replace(",", "，");
        str = str.replace(";", "；");
        str = str.replace("&", "＆");
        str = str.replace("<", "＜");
        str = str.replace(">", "＞");
        return str;
    }
    
    public static String filterForHTML( String src ) {
        if( src == null ) return "";
        String str = src;
        str = str.replace("&","&amp;");
        str = str.replace(" ","&nbsp;");
        str = str.replace("<","&lt;");
        str = str.replace(">","&gt;");
        str = str.replaceAll("\\r\\n","<BR>");
        str = str.replaceAll("\\n","<BR>");

        return str;
    }
    
    public static String filterForJS( String src ) {
        if( src == null ) return "";
        String str = src;
        str = str.replaceAll("\\r\\n"," ");
        str = str.replaceAll("\\n"," ");
        str = str.replaceAll("\"","“");
        str = str.replaceAll("\'","‘");
        return str;
    }

    
    /** Remove elements from an Array
     * begin from [index], remove [length] elements
     */
    public synchronized static<T> T[] splice( T[] ary, int index, int length ) {
        T[] result = Arrays.copyOf(ary,ary.length -  length); //new Object[ ary.length - length ];
        if( index > 0 ) {
            System.arraycopy( ary, 0, result, 0, index );
        }
        if( index < (ary.length - 1) ) {
            System.arraycopy( ary, index + length, result, index, result.length - index );
        }
        return result;
    } 

    /**
     * Push an Element to a array
     * @param ary
     * @param obj
     * @return
     */
    public synchronized static<T>   T[] push( T[] ary, T obj ) {
        T[] r = Arrays.copyOf( ary,  ary.length + 1 );
        System.arraycopy(ary, 0, r, 0, ary.length );
        r[ ary.length ] = obj;
        return r;
    }

    public static String getDateStr() {
        return null;
    }


    public static String TVC( String capChar ) {
        if( capChar.equalsIgnoreCase("A") ) {
            return "吖";
        }
        else if( capChar.equalsIgnoreCase("A") ) {
            return "";
        }
        return "";
    }

    //吖八嚓咑妸发旮铪讥咔垃嘸拏噢妑七亽仨他哇夕丫帀咗

    public static String CVT(String str) {
        if (str.compareTo("吖") < 0) {
            String s = str.substring(0, 1).toUpperCase();
            if (Character.isDigit(s.charAt(0))) {
                return "0";
            } else {
                return s;
            }
        } else if (str.compareTo("八") < 0) {
            return "A";
        } else if (str.compareTo("嚓") < 0) {
            return "B";
        } else if (str.compareTo("咑") < 0) {
            return "C";
        } else if (str.compareTo("妸") < 0) {
            return "D";
        } else if (str.compareTo("发") < 0) {
            return "E";
        } else if (str.compareTo("旮") < 0) {
            return "F";
        } else if (str.compareTo("铪") < 0) {
            return "G";
        } else if (str.compareTo("讥") < 0) {
            return "H";
        } else if (str.compareTo("咔") < 0) {
            return "J";
        } else if (str.compareTo("垃") < 0) {
            return "K";
        } else if (str.compareTo("嘸") < 0) {
            return "L";
        } else if (str.compareTo("拏") < 0) {
            return "M";
        } else if (str.compareTo("噢") < 0) {
            return "N";
        } else if (str.compareTo("妑") < 0) {
            return "O";
        } else if (str.compareTo("七") < 0) {
            return "P";
        } else if (str.compareTo("亽") < 0) {
            return "Q";
        } else if (str.compareTo("仨") < 0) {
            return "R";
        } else if (str.compareTo("他") < 0) {
            return "S";
        } else if (str.compareTo("哇") < 0) {
            return "T";
        } else if (str.compareTo("夕") < 0) {
            return "W";
        } else if (str.compareTo("丫") < 0) {
            return "X";
        } else if (str.compareTo("帀") < 0) {
            return "Y";
        } else if (str.compareTo("咗") < 0) {
            return "Z";
        } else {
            return "0";
        }


    }

    /**
     * 求一个小数对应的字符串，可以指定小数点后的位数
     * @param d
     * @param num
     * @return
     */
    public static String round( double d, int num ) {

        String p = String.valueOf( Math.round( Math.floor(d)) );
        int power = 1;
        for( int i = 0; i < num ;i ++ ) {
            power = power * 10;
        }
        long Dig = Math.round(Math.round(d * power) - Math.floor(d) * power) ;
        if( num > 0 ) {
            String dig = String.valueOf(Dig);
            while( dig.length() < num ) dig = "0" + dig;
            return p + "." + dig;
        }
        else {
            return p;
        }
    }

    /**
     * 计算两个日期相减的天数，
     * @param date1
     * @param date2
     * @return
     */
    public static int dateStrMinus( String date1, String date2 ) throws ParseException {
        Date d1 = DateFormat.getDateInstance().parse(date1);
        Date d2 = DateFormat.getDateInstance().parse(date2);
        long timeDis = d2.getTime() - d1.getTime();
        int days = (int) (timeDis / (24 * 60 * 60 * 1000));
        if( days < 0 ) days = -1 * days;
        return days;
    }

    /**
     * 判断是否在日期范围内
     * @param date
     * @param startDate
     * @param endDate
     * @return
     */
    public static boolean inDataRange( String date, String startDate, String endDate ) throws ParseException {
        Date d = DateFormat.getDateInstance().parse(date);
        Date d1 = DateFormat.getDateInstance().parse( startDate );
        Date d2 = DateFormat.getDateInstance().parse( endDate );
        if( d1.compareTo( d2 ) > 0 ) {
            Date dswap = d1;
            d1 = d2;
            d2 = dswap;
        }
        if( d.compareTo( d1 ) >= 0 && d.compareTo(d2) <= 0 ) {
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * 获取合适的文本比较器
     * @param lang
     * @return
     */
    public static Collator getProperCollator( String lang ) {
        if( lang == null ) return Collator.getInstance();
        if( lang.equalsIgnoreCase("CHS") ) return Collator.getInstance( Locale.SIMPLIFIED_CHINESE );
        else if ( lang.equalsIgnoreCase("ENG") ) return Collator.getInstance( Locale.ENGLISH );
        else if ( lang.equalsIgnoreCase("CHT") ) return Collator.getInstance( Locale.TRADITIONAL_CHINESE );
        else return Collator.getInstance();

    }


    public static String removeReturn( String str ) {
        return str.replaceAll("\\r\\n", " ").replaceAll("\\n", " ");
    }

    /**
     * 合并字符串数组
     * @param s
     * @param delimmiter
     * @return
     */
    public static String join( Collection s, String delimmiter ) {
        StringBuilder stb = new StringBuilder();
        Iterator it = s.iterator();
        boolean alreadySet = false;
        while( it.hasNext() ) {
            if( alreadySet ) stb.append( delimmiter );
            Object v = it.next();
            v = ( v!= null )?v:"null";
            stb.append( v );
            alreadySet = true;
        }
        return stb.toString();
    }

    /**
     * 合并字符串数组
     * @param s
     * @param delimmiter
     * @return
     */
    public static String join( Object[] s, String delimmiter ) {
        StringBuilder stb = new StringBuilder();
        for( int i = 0; i < s.length; i++ ) {
            if( i > 0 ) stb.append( delimmiter );
            stb.append( ( s[i] != null )?s[ i ]:"null" );
        }
        return stb.toString();
    }

    public static String getCollatorCharSet( String lang ) {
        if( lang.equalsIgnoreCase("CHS") ) return "GBK";
        else if ( lang.equalsIgnoreCase("ENG") ) return "UTF8";
        else if ( lang.equalsIgnoreCase("CHT") ) return "BIG5";
        else return "UTF8";
    }

    public static String getDefaultFont( String lang ) {
        if( lang.equalsIgnoreCase("CHS") ) return "宋体";
        else if ( lang.equalsIgnoreCase("ENG") ) return "SansSerif";
        else if ( lang.equalsIgnoreCase("CHT") ) return "宋体";
        else return "SansSerif";
    }

    /**
     * 把一个数组中全部元素加入到一个集合对象中
     * @param col
     * @param obj 
     */
    public static void addAll( Collection col, Object[] obj ) {
        for( int i = 0; i < obj.length; i++ ) {
            col.add( obj[i] );
        }
    }

    public static String repeat( String S, int N ) {
        StringBuilder b = new StringBuilder();
        for( int i = 0; i < N ; i++ ) {
            b.append( S );
        }
        return b.toString();
    }
    
    /**
     * 把形如 1.5.7的数字对齐，以避免出现 2.1 > 10.5这样的错误
     * @param N
     * @param length
     * @return 
     */
    public static String getAlignedNumber( String N, int length ) {
        if( N == null ) return null;
        if( length <= 0 ) return N;
        
        Pattern P = Pattern.compile("(\\d+)([\\.、．-]\\d+)*");        //
        Matcher m = P.matcher( N );
        String NExact = "";
        if( m.find() ) {
            NExact = m.group();
        }
        else {
            return "";
        }
        String[] Ns = NExact.split("[\\.、．-]");
        for( int i = 0; i < Ns.length; i++ ) {
            if( Ns[i].length() < length ) {
                Ns[i] = repeat( "0", length - Ns[i].length() ) + Ns[i];
            }
        }
        return join( Ns, "." );
    }
    
    public static String getAlignedNumberString( String N, int length ) {
        if( N == null ) return null;
        if( length <= 0 ) return N;
        String NX = CUtils.getAlignedNumber(N, length);
        return N.replaceFirst( "(\\d+)([\\.、．-]\\d+)*", NX );
    }
    
    /**
     * 替换字符串中的 & < > ' " \r \n
     * 换成 &amp; &lt; &gt; &#39; &quot; &nextline; &enter;
     * @param v
     * @return 
     */
    public static String encodingStr( String v ) {
        if( v == null ) return null;
        return v.replaceAll("&", "&amp;").replaceAll("\r", "&nextline;").replaceAll("\n", "&enter;").replaceAll("'", "&#39;").replace("\"", "&quot;").replaceAll(">","&gt;").replaceAll("<", "&lt;");
    }
    
    /**
     * 替换字符串中的 &amp; &lt; &gt; &#39; &quot; &nextline; &enter;
     * 换成          &     <    >    '     "      \r         \n
     * 换成 
     * @param v
     * @return 
     */
    public static String decodingStr( String v ) {
        if( v == null ) return null;
        return v.replaceAll("&gt;",">").replaceAll("&lt;","<").replaceAll("&quot;", "\"").replaceAll("&#39;", "'").replaceAll("&enter;", "\n").replaceAll("&nextline;", "\r").replaceAll("&amp;", "&");
    }
    
    public static String decodingStrToHTML( String v ) {
        if( v == null ) return null;
        return v.replaceAll("&enter;", "<BR>").replaceAll("&nextline;", "");
    }
    
    public static void main(String[] args) {
        System.out.println( CUtils.getAlignedNumberString("S ISJXIJKJ", 4) );
    }
    
}
