/*
 * ICanExport.java
 *
 * Created on 2007年10月13日, 下午4:40
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package General.DAOs;

import General.DAOs.CValue;
import java.lang.reflect.*;
import java.util.Vector;
//import org.eclipse.jdt.internal.compiler.ast.ThisReference;
/**
 *
 * @author Cuilin
 */
public class ICanExport implements IAbstractTO {
    
    /** Creates a new instance of ICanExport */
    
    public static String filterKeyChar( Object o ) {
        if( o == null ) return "null";
        else 
            return o.toString().replaceAll("%","%00").replaceAll("#","%01").replaceAll(",","%02").replaceAll("/","%03").replaceAll("\\n","%04").replaceAll("\\r", "%05" );
    }
    public static String unFilterKeyChar( String o ) {
        return o.replaceAll("%05","\r").replaceAll("%04","\n").replaceAll("%03","/").replaceAll("%02",",").replaceAll("%01","#").replaceAll("%00","%");
    }
    public static Field[] getFields( Class cls ) {
        Class rootcls = ICanExport.class;
        Class temp = cls;
        Vector result = new Vector();
        while( ! temp.equals( rootcls ) && ( !temp.equals( Object.class ))) {
            Field[] fs = temp.getDeclaredFields();
            for( int i = 0; i < fs.length; i++ ) {
                result.add( fs[i] );
            }
            temp = temp.getSuperclass();
        }
        Field[] r = new Field[ result.size() ];
        r = (Field[])result.toArray( r );
        return r;
    }

    /**
     * 产生一个可以用于导出的字符串，包含该对象的全部属性数据
     * 数据中的分隔符等关键字符会被 filterKeyChar 方法过滤掉，与之相对应的有unFilterKeyChar来恢复
     * @return 生成的字符串
     */
    public String exportData() {
        StringBuffer r = new StringBuffer();
        //r.append("{");
        Class self = this.getClass();
        Field[] fs = this.getFields( self );
        try {
            String[] className = self.getName().split("\\.");
            r.append( className[className.length-1] );
            for( int i = 0; i < fs.length; i++ ) {
                r.append(",");
                fs[i].setAccessible( true );
                r.append( fs[i].getName()  );
                r.append("#");
                r.append( filterKeyChar( fs[i].get( this ) ) );
            }
        }
        catch( Exception ee ){
            ee.printStackTrace();
        }
        /*
        r.append("projectId#" + this.projectID + ",");
        r.append("companyLocation#" + this.companyLocation + ",");
        r.append("companyName#" + this.companyName + ",");
        r.append("meetingLocation#" + this.meetingLocation + ",");
        r.append("projectDetail#" + this.projectDetail + ",");
        r.append("projectEndDate#" + this.projectEndDate + ",");
        r.append("projectName#" + this.projectName + ",");
        r.append("projectNumber#" + this.projectNumber + ",");
        r.append("projectStartDate#" + this.projectStartDate + ",");*/
        //r.append("}");
        return r.toString();
    }
    

    public void importData( String dataStr ) {
        //String data = dataStr.substring( 1, dataStr.length() - 2 );
        String[] dataAry = dataStr.split(",");
        Class self = this.getClass();
        try {
            for( int i = 1; i < dataAry.length; i++ ) {
                String[] singleData = dataAry[i].split("#");
                if( singleData != null && ! singleData[0].equalsIgnoreCase("null") ) {
                    try {
                        Field p = self.getDeclaredField( singleData[0] );
                        Class type = p.getType();
                        p.setAccessible( true );
                        if( type.getName().equalsIgnoreCase("java.lang.String")) {
                            if( singleData.length > 1 )
                                p.set( this, unFilterKeyChar( singleData[1]) );
                            else 
                                p.set( this, "" );
                        }
                        else if( type.getName().equalsIgnoreCase("int") || type.getName().equalsIgnoreCase("integer") ){
                                if( singleData.length > 1 )
                                    p.setInt( this, Integer.valueOf( unFilterKeyChar( singleData[1])).intValue() );
                                else 
                                    p.setInt( this, 0 );

                        }
                        else if( type.getName().equalsIgnoreCase("boolean") ) {
                            if( singleData.length > 1 ) {
                                p.setBoolean( this, Boolean.valueOf( unFilterKeyChar( singleData[1])).booleanValue() );
                            }
                            else {
                                p.setBoolean( this, false );
                            }
                        }
                        else if( type.getName().equalsIgnoreCase("long") ) {
                            if( singleData.length > 1 ) {
                                p.setLong( this, Long.valueOf( unFilterKeyChar( singleData[1])).longValue() );
                            }
                            else {
                                p.setLong( this, 0 );
                            }
                        }
                        else if( type.getName().equalsIgnoreCase("float") ) {
                            if( singleData.length > 1 ) {
                                p.setFloat( this, Float.valueOf( unFilterKeyChar( singleData[1])).floatValue() );
                            }
                            else {
                                p.setFloat( this, 0.0f );
                            }
                        }
                        else if( type.getName().equalsIgnoreCase("double") ) {
                            if( singleData.length > 1 ) {
                                p.setDouble( this, Double.valueOf( unFilterKeyChar( singleData[1])).doubleValue() );
                            }
                            else {
                                p.setDouble( this, 0.0f );
                            }
                        }
                    }
                    catch( Exception ee ){
                        
                    }
                }
            }
        }
        catch( Exception ee ){
            ee.printStackTrace();
        }
    }
    
    /**
     * 产生可以用于Javascript的数据对象
     * Note: 所有属性都是小写的
     * @return
     */
    public String exportToJavascriptData() {
        StringBuffer r = new StringBuffer();
        //r.append("{");
        Class self = this.getClass();
        Field[] fs = this.getFields( self );
        r.append("{");
        try {
            for( int i = 0; i < fs.length; i++ ) {
                
                if( (( ! fs[i].getType().isPrimitive()) &&
                        ( ! ( fs[i].getType().getName().toLowerCase().indexOf("string") >=0 ))) ||
                        ( ( fs[i].getType().getName().toLowerCase().indexOf("[") >=0 ))
                ){
                    continue;
                }
                if( r.length() > 5 ) r.append(",\r\n");
                fs[i].setAccessible( true );
                r.append( fs[i].getName().toLowerCase() );
                r.append(":");
                Object v = fs[i].get( this );

                String type = fs[i].getType().getName().toLowerCase();
                if( type.indexOf("string")>=0 || type.indexOf("char")>=0 )  r.append("\"");
                r.append(  (v==null)?"null":v.toString()  );
                if( type.indexOf("string")>=0 || type.indexOf("char")>=0 )  r.append("\"");

            }
        }
        catch( Exception ee ){
            ee.printStackTrace();
        }
        r.append("}");
        return r.toString();
    }


    public ICanExport cloneSelf() {
        try {

            Object R = this.getClass().newInstance();
            if( ! ( R instanceof CValue ) ) {
                throw new Exception("Type Error");
            }
            
            Field[] fields = this.getClass().getDeclaredFields();
            for( int i = 0; i < fields.length; i++ ) {
                Field oneField = fields[i];
                oneField.setAccessible(true);
                Object v = oneField.get( this );
                if( v != null ) oneField.set( R , v);
            }
            return (ICanExport )R;
        }
        catch( Exception excp ) {
            return null;
        }
    }

    /**
     * 复制自身到其他对象，其他对象中的同名，同类型数据将被复制得到
     * @param targetClass
     * @return
     */
    public Object copySelf( Class targetClass) {
        try {

            Object R = targetClass.newInstance();

            Field[] fields = this.getClass().getDeclaredFields();
            for( int i = 0; i < fields.length; i++ ) {
                Field oneField = fields[i];
                oneField.setAccessible(true);
                Object v = oneField.get( this );
                try {
                    if( v != null ) oneField.set( R , v);
                }
                catch( Exception ee) {}
            }
            return (ICanExport )R;
        }
        catch( Exception excp ) {
            return null;
        }
    }

    /**
     * 复制自身到一个指定的对象中，其他对象中的同名，同类型数据将被复制得到
     * @param targetClass
     * @return
     */
    public void copySelfTo( Object targetObj ) {
        this.copySelfTo(targetObj, null);
    }

    /**
     * 复制自身到一个指定的对象中，不一定复制全部属性，只有在targetClass中存在的属性才会被复制过去
     * 例如：Rat和Habbit都是Anaimal的子类，而执行Habbit.copySelfTo( Rat, Anaimal.class )则只复制Habbit中
     * 属于Anaimal中的属性到Rat中去
     *
     * @param target
     * @param targetClass
     */
    public void copySelfTo( Object targetObj, Class targetClass ) {
        this.copySelfTo( targetObj, this.getClass(), targetClass );
    }

    public void copySelfTo( Object targetObj, Class sourceClass, Class targetClass ) {
        try {
            if( targetClass == null )  targetClass = targetObj.getClass();
            //Field[] fields = this..getFields();
            Field[] fields = sourceClass.getFields();
            Field[] fields2 = sourceClass.getDeclaredFields();
            Field[] allField = new Field[ fields.length + fields2.length ];
            System.arraycopy( fields, 0 , allField, 0, fields.length );
            System.arraycopy( fields2, 0 , allField, fields.length, fields2.length );
            for( int i = 0; i < allField.length; i++ ) {
                Field oneField = allField[i];
                oneField.setAccessible(true);
                Object v = oneField.get( this );
                try {
                    //测试是否指定类型的Class中存在指定的Field，如果不存在就会抛出异常
                    boolean existField = true;
                    boolean existDecalreField = true;
                    try { Field inTargetFiled = targetClass.getField( oneField.getName() ); } catch( Exception e ){ existField = false; }
                    try { Field inTargetFiled = targetClass.getDeclaredField( oneField.getName() ); } catch( Exception e ) { existDecalreField = false; }
                    //赋值，如果targetObj中没有对应的Field，则也会抛出异常
                    if( !existField && !existDecalreField ) {
                        throw new Exception("");
                    }
                    if( v != null ) oneField.set( targetObj , v);
                }
                catch( Exception ee) {
                    //ee.printStackTrace();
                }
            }

        }
        catch( Exception excp ) {
            excp.printStackTrace();
        }
    }

    public static void main( String[] args ){
        System.out.println( new Boolean(false).toString() );
        System.out.println( filterKeyChar("%01,GH%3,5##%02") );
        System.out.print( unFilterKeyChar("%0001%02GH%003%025%01%01%0002") );
    }
}
