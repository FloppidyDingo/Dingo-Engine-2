/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package objects;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;

/**
 *
 * @author James
 */
public class Skin {
    
    private final List<ViewPort> skins;
    private String ID;
    private BufferedImage image;
    
    public Skin(){
        skins = new ArrayList<>();
    }

    /**
     *
     * @return The skin's ID.
     */
    public String getID() {
        return ID;
    }

    /**
     *Sets the skin's ID (may break things).
     * @param ID
     */
    public void setID(String ID) {
        this.ID = ID;
    }
    
    /**
     *Returns the skin's base image.
     * @return
     */
    public BufferedImage getSkin(){
        return image;
    }
    
    /**
     *
     * @param x1 The x coordinate of the top left corner
     * @param y1 The y coordinate of the top left corner
     * @param x2 The x coordinate of the bottom right corner
     * @param y2 The y coordinate of the bottom right corner
     */
    public void addFrame(int x1, int y1, int x2, int y2){
        skins.add(new ViewPort(x1, y1, x2 - x1 + 1, y2 - y1 + 1));
    }
    
    /**
     *Removes the specified frame.
     * @param frame
     */
    public void removeFrame(int frame){
        skins.remove(frame);
    }
    
    /**
     *Removes all frames.
     */
    public void clear(){
        skins.clear();
    }
    
    public ViewPort getViewPort(int frame){
        return skins.get(frame);
    }

    public List<ViewPort> getSkins() {
        return skins;
    }
    
    /**
     *Generates skin rectangles from a definition image
     * @param url of the Skin Definitions Image
     * @throws IOException
     */
    public void bufferSkinDef(String url) throws IOException{
        try {
            BufferedImage img = ImageIO.read(new File(url));
            int fx = 0;
            int fy = 0;
            int state = 0;
            for(int y = 0; y < img.getHeight(); y++){
                for(int x = 0; x < img.getWidth(); x++){
                    switch(state){
                        case 0:{
                            if(img.getRGB(x, y) == Color.BLACK.getRGB()){
                                img.setRGB(x, y, Color.RED.getRGB());
                                fx = x;
                                fy = y;
                                state = 1;
                            }
                            break;
                        }
                        case 1:{
                            if(img.getRGB(x, y) == Color.BLACK.getRGB()){
                                img.setRGB(x, y, Color.RED.getRGB());
                                for(int y2 = y; y2 < img.getHeight(); y2++){
                                    if(img.getRGB(x, y2) == Color.BLACK.getRGB()){
                                        img.setRGB(x, y2, Color.RED.getRGB());
                                        this.addFrame(fx, fy, x, y2);
                                        state = 0;
                                        break;
                                    }
                                }
                            }
                            break;
                        }
                    }
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     *Sets the skin's base image.
     * @param img
     */
    public void setBaseImage(BufferedImage img) {
        image = img;
    }
    
    /**
     * Returns the image associated with the specified frame
     * @param frame
     * @return 
     */
    public BufferedImage getFrame(int frame){
        ViewPort r = skins.get(frame);
        return image.getSubimage(r.getX(), r.getY(), r.getW(), r.getH());
    }
    
}
