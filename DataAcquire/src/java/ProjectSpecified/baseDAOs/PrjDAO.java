/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ProjectSpecified.baseDAOs;

import ProjectSpecified.ConnectionPools.CPrjPool;
import General.DAOs.CDao;

/**
 * 用于连接主数据库的DAO对象，其中已经包含了对相应的数据库连接池的调用。
 * @author bahamut
 */
public abstract class PrjDAO extends CDao{
    @Override
    protected void getCnPool() {
        this.thisCnPool = CPrjPool.getInstance();
    }
}
