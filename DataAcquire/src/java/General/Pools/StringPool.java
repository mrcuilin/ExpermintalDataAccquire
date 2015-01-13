/*
 * StringPool.java
 *
 * Created on 2006年6月22日, 下午3:12
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package General.Pools;

import java.util.Vector;

/**
 *
 * @author Cuilin
 */
public class StringPool extends CGeneralPool{
    
    /** Creates a new instance of StringPool */
    public StringPool(int i) throws Exception {
        super(i);
    }
    
    public boolean isOpen(Object Obj) {
        return true;
    }
    
    public boolean closePoolItem(Object Obj) {
        return true;
    }
    
    public Object createPoolItem() {
        return new String("StringX");
    }
    public static void main(String[] args) {
        try {
            StringPool myStringPool = new StringPool(10);
            myStringPool.setPoolCapacity(30);
            Vector CP = new Vector();
            for( int i = 0; i < 6 ; i++) {
                String temp = (String)(myStringPool.getPoolItem());
                temp += i;
                CP.addElement(temp);
                System.out.print("PoolIdleSize: ");
                System.out.println(myStringPool.getIdlePoolItemNumber());

            }

            int Y = 0;
            for(int X = 0; X <1000000; X ++) {
                Y += X;
            }
            
            System.out.print("PoolSize: ");
            System.out.println(myStringPool.getPoolSize());
            
            for( int i = 0 ; i < 6 ;i++) {
                System.out.println(CP.get(i));
            }
            System.out.println(myStringPool.getPoolSize());
            for( int i = 0 ; i < 6 ;i++) {
                try { myStringPool.freePoolItem(CP.get(i)); } catch(Exception e) {}
            }
            CP.clear();
            
            for( int i = 0; i < 6 ; i++) {
                String temp = (String)(myStringPool.getPoolItem());
                temp += i;
                CP.addElement(temp);
                System.out.print("PoolIdleSize: ");
                System.out.println(myStringPool.getIdlePoolItemNumber());

            }            
            System.out.print("PoolIdleSize: ");
            System.out.println(myStringPool.getIdlePoolItemNumber());
            System.out.print("PoolSize: ");
            System.out.println(myStringPool.getPoolSize());            
        }
        catch(Exception e) {
            System.out.println(e);
        }
        
        
        
    }
    
}
