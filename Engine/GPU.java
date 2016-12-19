/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Engine;

import GUI.GUI;
import objects.Node;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.DisplayMode;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.RenderingHints;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.Comparator;
import javax.swing.JFrame;
import javax.swing.JPanel;
import objects.Light;

/**
 *
 * @author James
 */
public class GPU extends JPanel{
    private Scene scene;
    private GUI gui;
    private BufferedImage lightMap;
    private BufferedImage frame;
    private BufferedImage overlay;
    private BufferedImage buffer;
    private final int color;
    private boolean fullScreen;
    private BufferStrategy FB;

    protected void setScene(Scene s) {
        scene = s;
    }

    public Scene getScene() {
        return scene;
    }

    public GUI getGui() {
        return gui;
    }

    public void setGui(GUI gui) {
        this.gui = gui;
    }
    
    //<editor-fold defaultstate="collapsed" desc="Render Methods">
    public void draw(){
        Graphics2D frameBuffer = (Graphics2D) frame.getGraphics();
        Graphics2D lightGraphics = (Graphics2D) lightMap.getGraphics();
        Graphics2D overlayGraphics = (Graphics2D) overlay.getGraphics();
        frameBuffer.setRenderingHint(RenderingHints.KEY_ANTIALIASING,    RenderingHints.VALUE_ANTIALIAS_ON);
        lightGraphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,    RenderingHints.VALUE_ANTIALIAS_ON);
        lightGraphics.clearRect(0, 0, lightMap.getWidth(), lightMap.getHeight());
        frameBuffer.clearRect(0, 0, frame.getWidth(), frame.getHeight());
        overlayGraphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,    RenderingHints.VALUE_ANTIALIAS_ON);
        overlayGraphics.clearRect(0, 0, overlay.getWidth(), overlay.getHeight());
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
        for(Node item : gui.getGuiList()){
            BufferedImage temp = item.render(this);
            if (temp != null) {
                overlayGraphics.drawImage(temp, item.getX() - (temp.getWidth() / 2) + (frame.getWidth() / 2),
                        item.getY() - (temp.getHeight() / 2) + (frame.getHeight() / 2), null);
            }
        }
        frameBuffer.dispose();
        lightGraphics.dispose();
        overlayGraphics.dispose();
        if(fullScreen){
            Graphics2D g2 = (Graphics2D)buffer.getGraphics();
            g2.setColor(Color.BLACK);
            g2.clearRect(0, 0, frame.getWidth(), frame.getHeight());
            g2.drawImage(frame, 0, 0, this);
            g2.drawImage(lightMap, 0, 0, this);
            g2.drawImage(overlay, 0, 0, this);
            Graphics g = FB.getDrawGraphics();
            g.clearRect(0, 0, frame.getWidth(), frame.getHeight());
            g.drawImage(buffer, 0, 0, this.getSize().width, this.getSize().height, this);
            if(!FB.contentsLost()){
                FB.show();
            }
            g2.dispose();
            g.dispose();
        }else{
            Graphics2D g2 = (Graphics2D)buffer.getGraphics();
            g2.setColor(Color.BLACK);
            g2.clearRect(0, 0, frame.getWidth(), frame.getHeight());
            g2.drawImage(frame, 0, 0, this);
            g2.drawImage(lightMap, 0, 0, this);
            g2.drawImage(overlay, 0, 0, this);
            this.repaint();
        }
    }
    
    @Override
    public void paintComponent(Graphics g){
        if (!fullScreen) {
            super.paintComponent(g);
            g.drawImage(buffer, 0, 0, this.getSize().width, this.getSize().height, this);
            g.dispose();
        }
    }
//</editor-fold>
    
    public GPU(Settings s) {
        frame = new BufferedImage(s.getInternalResX(), s.getInternalResY(), s.getColorModel());
        lightMap = new BufferedImage(s.getInternalResX(), s.getInternalResY(), s.getColorModel());
        overlay = new BufferedImage(s.getInternalResX(), s.getInternalResY(), s.getColorModel());
        buffer = new BufferedImage(s.getInternalResX(), s.getInternalResY(), s.getColorModel());
        color = s.getColorModel();
    }
    
    public void setInternalResolution(int resx, int resy){
        frame = new BufferedImage(resx, resy, color);
        lightMap = new BufferedImage(resx, resy, color);
        overlay = new BufferedImage(resx, resy, color);
        buffer = new BufferedImage(resx, resy, color);
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
    
    public void enterFullScreen(JFrame frame, int display, Settings set){
        GraphicsDevice device = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()[display];
        device.setFullScreenWindow(frame);
        device.setDisplayMode(new DisplayMode(set.getResolutionX(), set.getResolutionY(), 32, DisplayMode.REFRESH_RATE_UNKNOWN));
        frame.revalidate();
        frame.setIgnoreRepaint(true);
        frame.createBufferStrategy(2);
        FB = frame.getBufferStrategy();
        fullScreen = true;
    }
    
    public static int getNumScreens(){
        return GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices().length;
    }

    public boolean isFullScreen() {
        return fullScreen;
    }
}
