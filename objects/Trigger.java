/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package objects;

import Engine.GPU;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;


/**
 *
 * @author Jaca
 */
public class Trigger extends Node{
    private Object userData;
    private boolean fired;
    private Event event;
    private String ID;
    private boolean enabled;
    private boolean oneshot;
    private int w;
    private int h;
    private boolean visible;
    private final List<String> targets;

    /**
     *
     * @param x The x coordinate.
     * @param y The y coordinate.
     * @param w The width.
     * @param h the height.
     */
    public Trigger(int x, int y, int w, int h) {
        type = "trigger";
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        this.enabled = true;
        fired = false;
        event = new Event() {
            @Override
            public void fire() {}
        };
        targets = new ArrayList<>();
    }
    
    /**
     *
     * @param x The x coordinate.
     * @param y The y coordinate.
     * @param w The width.
     * @param h the height.
     * @param UD The User Data
     */
    public Trigger(int x, int y, int w, int h, Object UD) {
        type = "trigger";
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        this.enabled = true;
        userData = UD;
        fired = false;
        event = new Event() {
            @Override
            public void fire() {}
        };
        targets = new ArrayList<>();
    }
    
    public void addTarget(String ID){
        targets.add(ID);
    }
    
    public void removeTarget(String ID){
        targets.remove(ID);
    }

    public List<String> getTargets() {
        return targets;
    }
    
    /**
     *
     * @return The User Data.
     */
    public Object getUD() {
        return userData;
    }

    /**
     *Sets the User Data (may break things).
     * @param userData
     */
    public void setUD(Object userData) {
        this.userData = userData;
    }
    
    /**
     *Assigns an Event object to fire when triggered.
     * @param e
     */
    public void setOnFire(Event e){
        event = e;
    }
    
    /**
     *Returns the associated Event object.
     * @return
     */
    public Event getOnFired(){
        return event;
    }
    
    /**
     *Resets a One-shot trigger to be fired again.
     */
    public void resetOneShot(){
        fired = false;
    }
    
    /**
     *Disables a one-shot trigger.
     */
    public void disableOneShot(){
        fired = true;
    }

    /**
     *
     * @return The trigger's ID.
     */
    public String getID() {
        return ID;
    }

    /**
     *Sets the trigger's ID.
     * @param ID
     */
    public void setID(String ID) {
        this.ID = ID;
    }

    /**
     *Returns true if the trigger is enabled.
     * @return
     */
    public boolean isEnabled() {
        return enabled;
    }

    /**
     *Enables or disables the trigger.
     * @param enabled
     */
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
    
    /**
     *Fires the trigger (should not be called manually, the physics engine takes care of this).
     */
    public void fire(){
        if (!oneshot) {
            event.fire();
        } else {
            if (!fired) {
                event.fire();
            }
            fired = true;
        }
    }

    /**
     *
     * @return True if trigger can only be fired once.
     */
    public boolean isOneshot() {
        return oneshot;
    }

    /**
     *Sets whether the trigger can only be fired once or multiple times.
     * @param oneshot
     */
    public void setOneshot(boolean oneshot) {
        this.oneshot = oneshot;
    }

    public int getBx() {
        return x;
    }

    public void setX(int bx) {
        this.x = bx;
    }

    public int getBy() {
        return y;
    }

    public void setY(int by) {
        this.y = by;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public void setWidth(int w) {
        this.w = w;
    }

    public void setHeight(int h) {
        this.h = h;
    }

    @Override
    public BufferedImage render(GPU gpu) {
        if(!visible){
            return null;
        }
        BufferedImage img = new BufferedImage(w, h, gpu.getColor());
        img.getGraphics().setColor(Color.blue);
        img.getGraphics().fillRect(0, 0, w, h);
        return img;
    }

    @Override
    public int getWidth() {
        return w;
    }

    @Override
    public int getHeight() {
        return h;
    }
    
}
