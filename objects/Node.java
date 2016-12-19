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

/**
 *
 * @author James
 */
public abstract class Node {
    public String type;
    public int x;
    public int y;
    private Control control;
    
    public String getType() {
        return type;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
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
    
    public abstract BufferedImage render(GPU gpu);
    
    public abstract int getWidth();
    
    public abstract int getHeight();
}
