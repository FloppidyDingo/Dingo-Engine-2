/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Engine;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 *A timer that fires an event once every specified interval.
 * Not in sync with the animation timer.
 * @author Jaca
 */
public abstract class Timer {
    private final javax.swing.Timer t;
    private final javax.swing.Timer fpscounter;
    private int FPS;
    private int count;
    
    /**
     *
     * @param interval The time in between event calls (in microseconds)
     */
    public Timer(int interval){
        t = new javax.swing.Timer(interval, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                action();
                count ++;
            }
        });
        t.stop();
        t.setCoalesce(false);
        fpscounter = new javax.swing.Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                FPS = count;
                count = 0;
            }
        });
        fpscounter.start();
        fpscounter.setCoalesce(false);
    }
    
    /**
     *
     * @param i The time in between event calls (in microseconds)
     */
    public void setInterval(int i){
        t.setDelay(i);
    }
    
    /**
     *Starts the timer.
     */
    public void start(){
        t.start();
    }
    
    /**
     *Stops the timer.
     */
    public void stop(){
        t.stop();
    }

    public int getFPS() {
        return FPS;
    }
    
    /**
     *Called everytime the specified interval has passed.
     */
    public abstract void action();
}
