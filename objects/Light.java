/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package objects;

import java.awt.Color;

/**
 *
 * @author James
 */
public class Light {
    private float brightness;
    private int x;
    private int y;
    private int radius;
    private int width;
    private int height;
    private int color;
    private int type;
    private String ID;
    
    public static final int CIRCLE = 0;
    public static final int RECTANGLE = 1;
    public static final int SQUARE = 2;

    public Light() {
        this.type = 0;
        this.color = Color.white.getRGB();
        this.height = 10;
        this.width = 10;
        this.radius = 10;
        this.y = 0;
        this.x = 0;
        this.brightness = 0.5f;
        
    }

    public float getBrightness() {
        return brightness;
    }

    public void setBrightness(float brightness) {
        this.brightness = brightness;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }
}
