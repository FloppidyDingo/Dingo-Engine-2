/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package objects;

import Controls.Control;
import Engine.GPU;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import motion.Animation;
import motion.Vector;

/**
 *
 * @author Jaca
 */
public class Entity extends Node{
    
    protected Skin skin;
    private Object userData;
    private String ID;
    private Vector dir = new Vector();
    private int mass;
    private boolean solid;
    private boolean active;
    private BufferedImage image;
    private Control control;
    private final List<Animation> ani;
    private boolean drawOffScreen;
    private final Rectangle bound;

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
    
    /**
     *
     * @return true if the Entity is solid
     */
    public boolean isSolid() {
        return solid;
    }

    /**
     *Set's the Entity's Solid property. Collision will not apply if not solid.
     * @param active
     */
    public void setSolid(boolean active) {
        this.solid = active;
    }
    
    /**
     *
     * @return The mass of the Entity.
     */
    public int getMass() {
        return mass;
    }

    /**
     *Sets the mass of the Entity.
     * @param mass
     */
    public void setMass(int mass) {
        this.mass = mass;
    }
    
    /**
     *Sets the currently visible frame of the Entity's skin.
     * @param frame
     */
    public void useSkin(int frame){
        image = skin.getFrame(frame);
    }

    /**
     *
     * @return The assigned Skin object of the Entity.
     */
    public Skin getSkin() {
        return skin;
    }

    /**
     *Assigns a Skin object to the Entity.
     * @param skin
     */
    public void setSkin(Skin skin) {
        this.skin = skin;
        image = skin.getFrame(0);
    }
    
    /**
     *
     * @return The Entity's motion vector.
     */
    public Vector getDir() {
        return dir;
    }

    /**
     *Set the Entity's motion vector.
     * @param dir
     */
    public void setDir(Vector dir) {
        this.dir = dir;
    }

    /**
     *
     * @return The Entity's ID.
     */
    public String getID() {
        return ID;
    }

    /**
     *Sets the Entity's ID (may break things)
     * @param ID
     */
    public void setID(String ID) {
        this.ID = ID;
    }
    
    /**
     *
     * @param s The Skin object.
     * @param ID
     */
    public Entity(Skin s, String ID){
        this.bound = new Rectangle();
        this.drawOffScreen = true;
        ani = new ArrayList<>();
        type = "entity";
        skin = s;
        userData = 0;
        this.ID = ID;
        image = skin.getFrame(0);
    }

    /**
     *
     * @param s The Skin object.
     * @param x The x coordinate.
     * @param y The y coordinate.
     * @param ID
     */
    public Entity(Skin s, int x, int y, String ID){
        this.bound = new Rectangle();
        this.drawOffScreen = true;
        ani = new ArrayList<>();
        type = "entity";
        skin = s;
        image = skin.getFrame(0);
        userData = null;
        this.x = x;
        this.y = y;
        this.ID = ID;
    }
    
    /**
     *
     * @param s The Skin object.
     * @param x The x coordinate.
     * @param y The y coordinate.
     * @param UD The User Data.
     * @param ID
     */
    public Entity(Skin s, int x, int y, Object UD, String ID){
        this.bound = new Rectangle();
        this.drawOffScreen = true;
        ani = new ArrayList<>();
        type = "entity";
        skin = s;
        image = skin.getFrame(0);
        userData = UD;
        this.x = x;
        this.y = y;
        this.ID = ID;
    }
    
    /**
     *Sets the x coordinate of the Entity.
     * @param d
     */
    public void setX(int d){
        x = d;
    }
    
    /**
     *Sets the y coordinate of the Entity.
     * @param d
     */
    public void setY(int d){
        y = d;
    }

    /**
     *
     * @return The Entity's User Data.
     */
    public Object getUserData() {
        return userData;
    }

    /**
     *Sets the Entity's User Data
     * @param userData
     */
    public void setUserData(Object userData) {
        this.userData = userData;
    }

    public boolean isDrawOffScreen() {
        return drawOffScreen;
    }

    public void setDrawOffScreen(boolean drawOffScreen) {
        this.drawOffScreen = drawOffScreen;
    }

    public void addAni(Animation e) {
        ani.add(e);
    }

    public void removeAni(Animation e) {
        ani.remove(e);
    }

    public void updateAnimation(){
        for (Animation animation : ani) {
            int a = animation.nextFrame();
            if(a != -1){
                useSkin(a);
            }
        }
    }
    
    @Override
    public BufferedImage render(GPU gpu) {
        if(drawOffScreen){
            return image;
        }else{
            Rectangle r = new Rectangle(0, 0, gpu.getResX(), gpu.getResY());
            if(this.getBound().intersects(r)){
                return image;
            }
        }
        return null;
    }

    public Control getControl() {
        return control;
    }

    public void setControl(Control control) {
        this.control = control;
    }

    @Override
    public int getWidth() {
        return image.getWidth();
    }

    @Override
    public int getHeight() {
        return image.getHeight();
    }

    public Rectangle getBound() {
        bound.setLocation(x - (image.getWidth() / 2), y  - (image.getHeight() / 2));
        bound.setSize(image.getWidth(), image.getHeight());
        return bound;
    }
    
}
