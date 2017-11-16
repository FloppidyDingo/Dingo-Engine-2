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
    private BufferedImage fb2;
    String strings[];
    public int offsetX;
    public int offsetY;
    public int padding;
    private float scale;

    public Label() {
        this.img = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        font = new GraphicsFont();
        padding = 1;
        scale = 1;
        this.setText("sample text");
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
        int i = 0;
        for (String string : strings) {
            g2.drawString(string, offsetX, font.getFont().getSize() + offsetY + (font.getFont().getSize() * i));
            i++;
        }
        g2.dispose();
        g2 = (Graphics2D)fb2.getGraphics();
        g2.drawImage(img, 0, 0, fb2.getWidth(), fb2.getHeight(), null);
        g2.dispose();
        return fb2;
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
        strings = label.split("\\r?\\n");
        int len = 0;
        for (String string : strings) {
            if(string.length() > len){
                len = string.length();
            }
        }
        int lines = strings.length;
        this.img = new BufferedImage((len + padding) * (int)((font.getFont().getSize()) * font.getScale()) + font.getPaddingW(),
               lines * font.getFont().getSize() + font.getPaddingH(), BufferedImage.TYPE_INT_ARGB);
        fb2 = new BufferedImage((int)(img.getWidth() * scale) + 1, (int)(img.getHeight() * scale) + 1, BufferedImage.TYPE_INT_ARGB);
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
    
    public void setScale(float scale){
        this.scale = scale;
        fb2 = new BufferedImage((int)(img.getWidth() * scale) + 1, (int)(img.getHeight() * scale) + 1, BufferedImage.TYPE_INT_ARGB);
    }
}
