package ProjectSpecified.ConnectionPools;

import General.Pools.CAbsConnectionPool;

/**
 * Project               :PSSR检查表开发
 * Create DateTime       :2011-12-12 10:38:03
 * Author                :Wangjx(wangjinxu@macrosafety.com)
 * Description           :
 * Version               :1.0
 * 
 */
public class CPrjPool extends CAbsConnectionPool {
    private static CPrjPool pssrInstance = new  CPrjPool();
    
    private CPrjPool(){super();}
    
    public static CPrjPool getInstance(){
        if( CPrjPool.pssrInstance == null ){
            CPrjPool.pssrInstance = new CPrjPool();
        }
        return CPrjPool.pssrInstance;
    }
    
    public void reloadConfig(){
        this.reloadConfig("DA485");
    }
    
    
    
}
