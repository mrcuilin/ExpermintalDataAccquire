/*
 * systemConstants.java
 *
 * Created on 2006年8月10日, 下午2:44
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package ProjectSpecified;

/**
 * 系统常量定义
 * @author Cuilin
 */
public class systemConstants {
    
    /** Creates a new instance of systemConstants */
    
    public static String dbConfigFile = "/WEB-INF/CustomConfigs/SystemConf.xml";
    public static String basicDataType = "STR|INT|DATE|NUMBER|STING";
    public static String licenseServerIPAddr = "192.168.0.188";
    public static int pageSize = 20;

    public static String iconImgPath = "/pub/img/icons/";
    
    /**
     * 用户互斥锁的保持时间，单位是分钟
     */
    public static int LockLife = 120;  // The minute number before a Edit Lock auto release

    
    public static String appName = "PHSSERKM";

    /**
     * 用户系统的缺省语言
     */
    public static String defaultLang = "CHS";
    
    /**
     * 本系统开发所用的原生语言，用于自动翻译
     */
    public static String nativeLang = "CHS";

    /**
     * 多语言资源文件保存的位置，注意是相对路径
     */
    public static String multiLangPath = "/WEB-INF/CustomConfigs/MultiLanguageRes";
    
    public static String attachmentExtName = "JPG,JPEG,BMP,GIF,PNG,PPT,PPTX,PPTM,PDF,DOC,DOCX,RTF,TXT";
    public static String dullImgFileNames = "blank,empty,null";
    
    public static String uncategorizedTagRoot = "Uncategorized";
    public static String tempDirectory;
    
    public systemConstants() {
    }
    
}
