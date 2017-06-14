/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import Graphics.GPU;
import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import objects.Node;

/**
 *
 * @author James
 */
public class Label extends Node{
    private String text;
    private GraphicsFont font;
    private BufferedImage img;
    public int offsetX;
    public int offsetY;
    public int padding;

    public Label() {
        this.img = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        font = new GraphicsFont();
        padding = 1;
        text = "sampletext";
    }

    @Override
    public BufferedImage render(GPU gpu) {
        Graphics2D g2 = (Graphics2D)img.getGraphics();
        g2.clearRect(0, 0, img.getWidth(), img.getHeight());
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.CLEAR, 1));
        g2.fillRect(0, 0, img.getWidth(), img.getHeight());
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1));
        g2.setFont(font.getFont());
        g2.setColor(font.getColor());
        g2.drawString(text, offsetX, font.getFont().getSize() + offsetY);
        g2.dispose();
        return img;
    }

    @Override
    public int getWidth() {
        return img.getWidth();
    }

    @Override
    public int getHeight() {
        return img.getHeight();
    }

    public void setFont(GraphicsFont font) {
        this.font = font;
    }

    public GraphicsFont getFont() {
        return font;
    }
    
    public void setWidth(int width){
        this.img = new BufferedImage(width, img.getHeight(), BufferedImage.TYPE_INT_ARGB);
    }
    
    public void setHeight(int height){
        this.img = new BufferedImage(img.getWidth(), height, BufferedImage.TYPE_INT_ARGB);
    }

    public String getText() {
        return text;
    }

    public void setText(String label) {
        this.text = label;
        int lines = label.split("\\r?\\n").length;
        this.img = new BufferedImage(label.length() * (int)((font.getFont().getSize() + padding) * font.getScale()) + font.getPaddingW(),
               lines * font.getFont().getSize() + font.getPaddingH(), BufferedImage.TYPE_INT_ARGB);
    }

    public int getOffsetX() {
        return offsetX;
    }

    public void setOffsetX(int offsetX) {
        this.offsetX = offsetX;
    }

    public int getOffsetY() {
        return offsetY;
    }

    public void setOffsetY(int offsetY) {
        this.offsetY = offsetY;
    }

    public void setPadding(int padding) {
        this.padding = padding;
    }

    public int getPadding() {
        return padding;
    }
    
}
