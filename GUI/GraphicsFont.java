/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import java.awt.Color;
import java.awt.Font;

/**
 *
 * @author James
 */
public class GraphicsFont {
    private Color color;
    private Font font;
    private int paddingW;
    private int paddingH;
    private float scale;

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public java.awt.Font getFont() {
        return font;
    }

    public void setFont(java.awt.Font font) {
        this.font = font;
    }

    public GraphicsFont() {
        this.scale = 0.5f;
        this.paddingH = 10;
        this.paddingW = 0;
        this.font = new Font("Arial", 0, 32);
        this.color = Color.WHITE;
    }

    public int getPaddingW() {
        return paddingW;
    }

    public void setPaddingW(int paddingW) {
        this.paddingW = paddingW;
    }

    public int getPaddingH() {
        return paddingH;
    }

    public void setPaddingH(int paddingH) {
        this.paddingH = paddingH;
    }

    public float getScale() {
        return scale;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }
    
}
