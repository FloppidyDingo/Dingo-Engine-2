/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;


/**
 *
 * @author James
 */
public class Button extends JPanel implements MouseListener{
    private BufferedImage idle;
    private BufferedImage hover;
    private BufferedImage select;
    private ButtonAction action;
    private BufferedImage active;

    public Button(BufferedImage idle, BufferedImage hover, BufferedImage select) {
        this.idle = idle;
        this.hover = hover;
        this.select = select;
        this.addMouseListener(this);
        active = idle;
    }
    
    public Button() {
        this.addMouseListener(this);
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

    @Override
    public void mouseClicked(MouseEvent e) {
        action.event(e);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        active = select;
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        active = hover;
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        active = hover;
    }

    @Override
    public void mouseExited(MouseEvent e) {
        active = idle;
    }
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(active, 0, 0, this);
    }

}
