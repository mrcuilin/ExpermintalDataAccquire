/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DdatAcquire.Service;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 *
 * @author cuilin
 */
public class CContextListener implements ServletContextListener {

    private static CTimerService tim;
    public void contextInitialized(ServletContextEvent sce) {
        this.tim = new CTimerService();
        this.tim.initSelf();
        System.out.println("My Severlet Listener");
        this.tim.start();
    }

    public void contextDestroyed(ServletContextEvent sce) {
        //
        this.tim.interupt();
    }
    
}
