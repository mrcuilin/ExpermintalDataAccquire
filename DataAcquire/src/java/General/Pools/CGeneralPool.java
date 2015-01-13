/*
 * generalPool.java
 *
 * Created on 2006年6月22日, 上午11:18
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package General.Pools;

import java.util.Vector;
import java.util.logging.Logger;
import java.util.logging.Level;

/**
 * CGeneralPool is an general Pool Class, which contains a set of pooled objects
 * To implement a useful pool, the 
 *      CGeneralPool();
 *      initPool(N);
 * are needed in consequence;
 * @author Cuilin
 */
public abstract class CGeneralPool {
    
    private Vector vector_IdleItems, vector_BusyItems;
    private int int_PoolCapacity;
    protected Logger myLoger = null;
    
    
    /** Creates a new instance of generalPool */
    public CGeneralPool() {
        vector_IdleItems = new Vector();
        vector_BusyItems = new Vector();
        reloadConfig();
    }
    public CGeneralPool(int PoolSize) throws CPoolException {
        vector_IdleItems = new Vector();
        vector_BusyItems = new Vector();
        reloadConfig();        
        this.initPool(PoolSize);
    }
    public void reloadConfig() {
        clearPool();
        setPoolCapacity(10);    // should be overrided by its subclass
    }  

    public final void setLogger(Logger theLogger) {
        this.myLoger = theLogger;
    }

    /**
     * Set the MAX number of the pool items that the pool can contain
     */
    public final void setPoolCapacity(int capacity) {
        this.int_PoolCapacity = capacity;
    }
    
    /**
     * Return the MAX number of pool items which the pool can contain
     */
    public final int getPoolCapacity() {
        return this.int_PoolCapacity;
    }
    
    /** 
     * Return the poolitems' number within the pool
     * Notice that the number is NOT the max capacity of the pool.
     */
    public final synchronized int getPoolSize() {
        return vector_IdleItems.size() + vector_BusyItems.size();
    }

    public final synchronized int getIdlePoolItemNumber() {
        return vector_IdleItems.size();
    }
    /**
     * An simple loop which can generate a pool with specified amount poolitems.
     */
    public final synchronized void initPool(int PoolSize) 
                    throws CPoolException{
        this.clearPool();
        for(int i = 0; i < PoolSize; i++) {
            generateNewPoolItem();                        
        }
    }
    
    /**
     *  Clear the pool
     * before remove each item, close it firstly if it is opened
     * The close item method is an abstract method, which should be specified in
     *   the subclass corresponding to the subclass' pool item type.
     */
    public final synchronized void clearPool() {
        for( int i = 0; i < vector_BusyItems.size(); i++) {
            if( isOpen(vector_BusyItems.get(i)))            
                closePoolItem(vector_BusyItems.get(i));
        }
        vector_BusyItems.clear();
        for( int i = 0; i < vector_IdleItems.size(); i++) {
            if( isOpen(vector_IdleItems.get(i)))
                closePoolItem(vector_IdleItems.get(i));
        }
        vector_IdleItems.clear();
    }
    
    /**
     * Get a pool item from the idle item collections
     * If all the pool item are busy, try create a new one.
     * If the max capacity has reached, throw an Exception
     */
    public final synchronized Object getPoolItem() 
                                 throws CPoolException{
        if( !vector_IdleItems.isEmpty()) {
            // If there are idle items in the Pool, return it, and put in into the Busy Collections
            Object tempObj = vector_IdleItems.lastElement();
            if ( isOpen(tempObj) ) {
                vector_IdleItems.removeElement(tempObj);
                vector_BusyItems.addElement(tempObj);
                return tempObj;
            }
            else {
                // If the got Obj had been closed, which means it is a dead obj, remove it and generate a new one.
                // then rerun getPoolItem(),
                // if the client is a single thread app, the new created obj will return when the method rerun.
                vector_IdleItems.removeElement(tempObj);
                if( (vector_BusyItems.size() + vector_IdleItems.size()) <= this.int_PoolCapacity )  {
                    generateNewPoolItem();
                    return getPoolItem();
                }
                else {
                    throw new CPoolException("Pool has reached its max capacity.");
                }
            }
        }
        else {
            // If there is not any idle items, try create a new one or throw a exception
            if( (vector_BusyItems.size() + vector_IdleItems.size()) <= this.int_PoolCapacity )  {
                // Create a new Item , return it and put it into the BusyItems
                generateNewPoolItem();
                return getPoolItem();
            }
            else {
                throw new CPoolException("Pool has reached its max capacity.");
            }
        }
    }

    /**
     * recycle a pool item, put it into the idle collection
     * If it is open when it be recycling, close it firstly
     * If the item specified does not belong to this pool, throw a Exception
     */
    public final synchronized void freePoolItem(Object poolItem){
        if( vector_BusyItems.contains(poolItem) ) {
            // If the obj is opening when return,  keep it in the idle Collection,
            // otherwise, remove it and generate a new one.
            if( isOpen(poolItem) ) {
                vector_BusyItems.removeElement(poolItem);
                vector_IdleItems.addElement(poolItem);
            }
            else {
                vector_BusyItems.removeElement(poolItem);
                try {
                    generateNewPoolItem();
                }
                catch (CPoolException ex) {
                    if( this.myLoger != null) {
                        try {
                            myLoger.log(Level.SEVERE,"The freed item is closed, but error occur when recreate it");
                        }
                        catch ( Exception ex2){}
                    }
                    //Log Needed
                }
            }
            //notifyAll();             
        }
        else {
            if( this.myLoger != null) {
                try {
                    myLoger.log(Level.WARNING,"The specified item don't belong to this Pool.");
                }
                catch ( Exception ex){}
            }
        }
    }
    
    /**
     *  create a new item and place it into the idle items collection: vector_IdleItems
     */
    public final synchronized void generateNewPoolItem() throws CPoolException{
        /*CGeneralPoolItemThread newThread = new CGeneralPoolItemThread(this);
        Thread createNew = new Thread(newThread);
        createNew.start();*/
        Object newItem = createPoolItem();
        if( newItem != null ) {
            vector_IdleItems.addElement(newItem);
        }
        else {
            if( this.myLoger != null) {
                try {
                    myLoger.log(Level.SEVERE,"Cannot Create New Pool Item.");
                }
                catch ( Exception ex){}
            }
            // Make a Log here            
            throw new CPoolException("New Pool Item creatation failed!");

        }
    }
    
    /**
     * Accessed only be the CGeneralPoolItemThread
     *  After create a new pool item, use it to insert into the idle collection,
     */
    public final synchronized void addPoolItem(Object Obj) {
        vector_IdleItems.addElement(Obj);
    }
    
    /**
     * Specify a creatation of a new pool item.
     * Must be implement by its subclass.
     * If failed, return a null
     */
    protected abstract Object createPoolItem();
    
    /**
     * Specify the operation of closing a pool item depending its type
     *
     * if failed ,return false and make log
     */
    protected abstract boolean closePoolItem(Object Obj);
    
    /**
     * Specify the operation of determinding a pool's status depending its type
     * if failed , return false and make log
     */
    protected abstract boolean isOpen(Object Obj);
    
}
