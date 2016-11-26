/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Engine;

import objects.Node;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.Comparator;
import javax.swing.JPanel;
import objects.Light;

/**
 *
 * @author James
 */
public class GPU extends JPanel{
    private Scene scene;
    private BufferedImage lightMap;
    private BufferedImage frame;
    private final int color;

    protected void setScene(Scene s) {
        scene = s;
    }

    public Scene getScene() {
        return scene;
    }
    
    public void draw(){
        Graphics2D frameBuffer = (Graphics2D) frame.getGraphics();
        Graphics2D lightGraphics = (Graphics2D) lightMap.getGraphics();
        frameBuffer.setRenderingHint(RenderingHints.KEY_ANTIALIASING,    RenderingHints.VALUE_ANTIALIAS_ON);
        lightGraphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,    RenderingHints.VALUE_ANTIALIAS_ON);
        lightGraphics.clearRect(0, 0, lightMap.getWidth(), lightMap.getHeight());
        frameBuffer.clearRect(0, 0, frame.getWidth(), frame.getHeight());
        for(Node item : scene.getItems()){
            BufferedImage temp = item.render(this);
            if (temp != null) {
                frameBuffer.drawImage(temp, item.getX() - (temp.getWidth() / 2) + (frame.getWidth() / 2),
                        item.getY() - (temp.getHeight() / 2) + (frame.getHeight() / 2), null);
            }
        }
        scene.getLights().sort(Comparator.comparing(Light::getBrightness));
        for(Light light : scene.getLights()){
            switch (light.getType()) {
                case 0: {
                    lightGraphics.setComposite(AlphaComposite.getInstance(AlphaComposite.CLEAR, 1));
                    lightGraphics.fillOval(light.getX() - (light.getRadius()) + (frame.getWidth() / 2), 
                            light.getY() - (light.getRadius()) + (frame.getHeight() / 2), light.getRadius() * 2, light.getRadius() * 2);
                    lightGraphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1 - light.getBrightness()));
                    lightGraphics.setColor(new Color(light.getColor()));
                    lightGraphics.fillOval(light.getX() - (light.getRadius()) + (frame.getWidth() / 2), 
                            light.getY() - (light.getRadius()) + (frame.getHeight() / 2), light.getRadius() * 2, light.getRadius() * 2);
                    break;
                }
                case 1:{
                    lightGraphics.setComposite(AlphaComposite.getInstance(AlphaComposite.CLEAR, 1));
                    lightGraphics.fillRect(light.getX() - (light.getWidth() / 2) + (frame.getWidth() / 2),
                        light.getY() - (light.getHeight() / 2) + (frame.getHeight() / 2), light.getWidth(), light.getHeight());
                    lightGraphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1 - light.getBrightness()));
                    lightGraphics.setColor(new Color(light.getColor()));
                    lightGraphics.fillRect(light.getX() - (light.getWidth() / 2) + (frame.getWidth() / 2),
                        light.getY() - (light.getHeight() / 2) + (frame.getHeight() / 2), light.getWidth(), light.getHeight());
                    break;
                }
                case 2:{
                    lightGraphics.setComposite(AlphaComposite.getInstance(AlphaComposite.CLEAR, 1));
                    lightGraphics.fillRect(light.getX() - (light.getWidth() / 2) + (frame.getWidth() / 2),
                        light.getY() - (light.getWidth() / 2) + (frame.getHeight() / 2), light.getWidth(), light.getWidth());
                    lightGraphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1 - light.getBrightness()));
                    lightGraphics.setColor(new Color(light.getColor()));
                    lightGraphics.fillRect(light.getX() - (light.getWidth() / 2) + (frame.getWidth() / 2),
                        light.getY() - (light.getWidth() / 2) + (frame.getHeight() / 2), light.getWidth(), light.getWidth());
                    break;
                }
            }
        }
        frameBuffer.dispose();
        lightGraphics.dispose();
        this.repaint();
    }
    
    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        g.drawImage(frame, 0, 0, this.getSize().width, this.getSize().height, this);
        g.drawImage(lightMap, 0, 0, this.getSize().width, this.getSize().height, this);
    }

    public GPU(Settings s) {
        frame = new BufferedImage(s.getInternalResX(), s.getInternalResY(), s.getColorModel());
        lightMap = new BufferedImage(s.getInternalResX(), s.getInternalResY(), s.getColorModel());
        color = s.getColorModel();
    }
    
    public void setInternalResolution(int resx, int resy){
        frame = new BufferedImage(resx, resy, color);
        lightMap = new BufferedImage(resx, resy, color);
    }

    public BufferedImage getFrame() {
        return frame;
    }

    public int getColor() {
        return color;
    }
    
    public int getResX(){
        return frame.getWidth();
    }
    
    public int getResY(){
        return frame.getHeight();
    }
}
