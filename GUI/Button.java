/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import Controls.BaseButtonControl;
import Engine.GPU;
import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import objects.Node;


/**
 *
 * @author James
 */
public class Button extends Node{
    private BufferedImage idle;
    private BufferedImage hover;
    private BufferedImage select;
    private ButtonAction action;
    private BufferedImage active;
    private BufferedImage img;
    private Label label;

    public Button(int w, int h, BufferedImage idle, BufferedImage hover, BufferedImage select) {
        this.idle = idle;
        this.hover = hover;
        this.select = select;
        this.setControl(new BaseButtonControl(this));
        active = idle;
        label = new Label();
        img = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
    }
    
    public Button(int w, int h) {
        this.setControl(new BaseButtonControl(this));
        label = new Label();
        img = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
    }

    public BufferedImage getIdle() {
        return idle;
    }

    public void setIdle(BufferedImage idle) {
        this.idle = idle;
        active = idle;
    }

    public BufferedImage getHover() {
        return hover;
    }

    public void setHover(BufferedImage hover) {
        this.hover = hover;
    }

    public BufferedImage getSelect() {
        return select;
    }

    public void setSelect(BufferedImage select) {
        this.select = select;
    }
    
    public void setActiveImage(BufferedImage i){
        active = i;
    }
    
    public void setOnAction(ButtonAction event){
        this.action = event;
    }

    public ButtonAction getOnAction() {
        return action;
    }

    @Override
    public BufferedImage render(GPU gpu) {
        Graphics2D fg2 = (Graphics2D)img.getGraphics();
        fg2.clearRect(0, 0, img.getWidth(), img.getHeight());
        fg2.setComposite(AlphaComposite.getInstance(AlphaComposite.CLEAR, 1));
        fg2.fillRect(0, 0, img.getWidth(), img.getHeight());
        fg2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1));
        fg2.drawImage(active, 0, 0, img.getWidth(), img.getHeight(), null);
        fg2.drawImage(label.render(gpu), 0, 0, img.getWidth(), img.getHeight(), null);
        fg2.dispose();
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

    public Label getLabel() {
        return label;
    }

    public void setLabel(Label Text) {
        this.label = Text;
    }
    
    public void setText(String text){
        label.setText(text);
    }
    
    public void setWidth(int w){
        img = new BufferedImage(w, img.getHeight(), BufferedImage.TYPE_INT_ARGB);
    }
    
    public void setHeight(int h){
        img = new BufferedImage(img.getWidth(), h, BufferedImage.TYPE_INT_ARGB);
    }
}
