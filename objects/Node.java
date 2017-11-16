/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package objects;

import Controls.Control;
import Graphics.GPU;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

/**
 *
 * @author James
 */
public abstract class Node {
    protected String type;
    protected int x;
    protected int y;
    private Control control;
    protected int collisionLayer;
    private boolean visible = true;
    private String ID;
    private boolean invertX;
    private boolean invertY;
    private float opacity = 1;
    
    public String getType() {
        return type;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }
    
    public boolean intersects(Node n){
        Rectangle r1 = new Rectangle(this.getX() - (this.getWidth() / 2), this.getY() - (this.getHeight() / 2), 
                this.getWidth(), this.getHeight());
        Rectangle r2 = new Rectangle(n.getX() - (n.getWidth() / 2), n.getY() - (n.getHeight() / 2), 
                n.getWidth(), n.getHeight());
        return r1.intersects(r2);
    }
    
    public Control getControl() {
        return control;
    }

    public void setControl(Control control) {
        this.control = control;
    }

    public int getCollisionLayer() {
        return collisionLayer;
    }

    public void setCollisionLayer(int collisionLayer) {
        this.collisionLayer = collisionLayer;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public boolean isInvertX() {
        return invertX;
    }

    public void setInvertX(boolean invertX) {
        this.invertX = invertX;
    }

    public boolean isInvertY() {
        return invertY;
    }

    public void setInvertY(boolean invertY) {
        this.invertY = invertY;
    }
    
    public abstract BufferedImage render(GPU gpu);
    
    public abstract int getWidth();
    
    public abstract int getHeight();

    public float getOpacity() {
        return opacity;
    }

    public void setOpacity(float opacity) {
        this.opacity = opacity;
    }
    
    public void setPosition(Point get) {
        this.setX((int)get.getX());
        this.setY((int)get.getY());
    }
}
