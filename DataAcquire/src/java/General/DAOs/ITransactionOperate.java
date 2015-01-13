/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package General.DAOs;

import java.sql.Connection;

/**
 * 用于执行某些事务处理的处理部分，应该与CDao.transactionOperate配合使用
 * @author bahamut
 */
public interface ITransactionOperate {
    /**
     * 定义具体的操作，如果出错就抛出异常
     * @param cn
     * @return
     * @throws java.lang.Exception
     */
    public abstract boolean operate( Connection cn ) throws Exception;
    
    /**
     * 返回一个字符串，描述本对象进行的操作
     * @return
     */
    public abstract String getMessage();
}
