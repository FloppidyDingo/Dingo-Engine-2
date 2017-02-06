/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 *
 * @author James
 */
public class GUIStore {
    
    public static BufferedImage getDefaultDeselected(){
        try {
            return ImageIO.read(GUIStore.class.getResource("deselected.png"));
        } catch (IOException ex) {
            Logger.getLogger(GUIStore.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(0);
        }
        return null;
    }
    
    public static BufferedImage getDefaultSelected(){
        try {
            return ImageIO.read(GUIStore.class.getResource("selected.png"));
        } catch (IOException ex) {
            Logger.getLogger(GUIStore.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(0);
        }
        return null;
    }
    
    public static BufferedImage getDefaultHover(){
        try {
            return ImageIO.read(GUIStore.class.getResource("hover.png"));
        } catch (IOException ex) {
            Logger.getLogger(GUIStore.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(0);
        }
        return null;
    }
    
}
