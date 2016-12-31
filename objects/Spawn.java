/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package objects;

import Engine.GPU;
import java.awt.Color;
import java.awt.image.BufferedImage;

/**
 *
 * @author James
 */
public class Spawn extends Node{
    private String ID;
    private int UD;
    private int time = 1;
    private int count = 0;
    private boolean visible;

    public Spawn() {
        type = "spawn";
    }
    
    /**
     *
     * @return The spawn's ID.
     */
    public String getID() {
        return ID;
    }
    
    /**
     *Sets the spawn's ID (may break things).
     * @param ID
     */
    public void setID(String ID) {
        this.ID = ID;
    }
    
    /**
     *
     * @return The User data
     */
    public int getUD() {
        return UD;
    }
    
    /**
     *Sets the User data
     * @param UD
     */
    public void setUD(int UD) {
        this.UD = UD;
    }

    /**
     *Returns the time to wait in frames between spawns.
     * @return
     */
    public int getTime() {
        return time;
    }

    /**
     *Sets the time to wait in frames between spawns.
     * @param time
     */
    public void setTime(int time) {
        this.time = time;
    }

    /**
     *Gets the timer's current time.
     * @return
     */
    public int getCount() {
        return count;
    }

    /**
     *Sets the timers time.
     * @param count
     */
    public void setCount(int count) {
        this.count = count;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    @Override
    public BufferedImage render(GPU gpu) {
        if(!visible){
            return null;
        }
        BufferedImage img = new BufferedImage(5, 5, gpu.getColor());
        img.getGraphics().setColor(Color.blue);
        img.getGraphics().fillRect(0, 0, 5, 5);
        return img;
    }

    @Override
    public int getWidth() {
        return 0;
    }

    @Override
    public int getHeight() {
        return 0;
    }
    
}
