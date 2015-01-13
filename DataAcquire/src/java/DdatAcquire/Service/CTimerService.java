/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DdatAcquire.Service;

import java.time.Instant;
import java.util.Date;
import java.util.Timer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author cuilin
 */
public class CTimerService extends Thread {
    private Timer t = new Timer();
    
    private String status;

    private int STEP;
    
    private long prevTime;
    public void initSelf() {
        this.status = "IDLE";

    }
    
    public void run() {
        this.status = "RUN";
        STEP = 0;
        prevTime = ( Instant.now().toEpochMilli() );
        System.out.println("STARTED");
        while( this.status.equals("RUN") ) {
            System.out.println( STEP++ );
            while ( Instant.now().toEpochMilli() - prevTime < 2000 ) {
                try {
                    sleep(500);
                } catch (InterruptedException ex) {
                    Logger.getLogger(CTimerService.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            prevTime = ( Instant.now().toEpochMilli() );
        }
        System.out.println("STOPED");
    }
    
    public void interupt() {
        this.status = "IDLE";
    }
}
