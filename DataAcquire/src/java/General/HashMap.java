/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package General;

/**
 *
 * @author bahamut
 */
public class HashMap<K,V> extends java.util.HashMap<K,V> {
    @Override
    public V put( K Key, V Value ) {
        if( Key instanceof String ) {
            Key = (K) ((String)Key).toLowerCase();
        }
        return super.put(Key, Value);
    }
    
    public V get( Object Key ) {
        if( Key instanceof String ) {
            Key = (K) ((String)Key).toLowerCase();
        }
        return super.get(Key);
    }
    
    public boolean containsKey(Object Key) {
        if( Key instanceof String ) {
            Key = (K) ((String)Key).toLowerCase();
        }
        return super.containsKey(Key);
    }
    
    public V remove(Object Key) {
        if( Key instanceof String ) {
            Key = (K) ((String)Key).toLowerCase();
        }
        return super.remove(Key);
    }    
    
    public static void  main( String[] arguments ) {
        HashMap hm = new HashMap();
        hm.put("Kof","helloSS");
        hm.put("OOP", 3 );
        System.out.println( hm.get("KOF") );
        System.out.println( hm.get("kof") );
        System.out.println( hm.get("ooP") );
    }
}
