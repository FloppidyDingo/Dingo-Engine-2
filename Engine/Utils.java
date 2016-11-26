

package Engine;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Random;
import javax.imageio.ImageIO;
import objects.Entity;
import objects.Skin;

/**
 *A set of utilities.
 * @author Jaca
 */
public class Utils {
    private static final Random r = new Random();

    /**
     *Used as a reference that an object is above or to the left of another.
     */
    public static final int ABOVE_LEFT = 1;

    /**
     *Used as a reference that an object is below or to the right of another.
     */
    public static final int BELOW_RIGHT = 2;

    /**
     *Used as a reference that an object is lined up with another.
     */
    public static final int CENTERED = 3;
    
    /**
     *Calculates the distance between two Nodes
     * @param i1
     * @param i2
     * @return
     */
    public static double getRange(Entity i1, Entity i2){
        double x1 = i1.getX();
        double x2 = i2.getX();
        double y1 = i1.getY();
        double y2 = i2.getY();
        return (Math.sqrt(Math.pow((x1 - x2), 2) + Math.pow((y1 - y2), 2)));
    }
    
    /**
     *Gets the closest node from a list in reference to a target.
     * @param ol The list of nodes.
     * @param target The target.
     * @return The closest node to the target out of the nodes in ol.
     */
    public static Entity getClosest(List<Entity> ol, Entity target){
        Entity n = ol.get(0);
        for (Entity node : ol) {
            if(Utils.getRange(node, target) < Utils.getRange(n, target)){
                n = node;
            }
        }
        return n;
    }
    
    /**
     *Returns a random integer between min and max.
     * @param min
     * @param max
     * @return
     */
    public static int randInt(int min, int max) {
        int randomNum = r.nextInt((max - min) + 1) + min;
        return randomNum;
    }
    
    /**
     *Places one node on top of another.
     * @param n
     * @param destination
     */
    public static void goTo(Entity n, Entity destination){
        n.setX(destination.getX());
        n.setY(destination.getY());
    }
    
    /**
     *Creates an Entity object from the specified ImageView
     * @param iv
     * @param ID
     * @return
     */
    public static Entity generateEntity(BufferedImage iv, String ID){
        Skin s = new Skin();
        s.setBaseImage(iv);
        return new Entity(s, ID);
    }
    
    /**
     * Returns the relative position between two Entities. Returned as an array of two
     * values, the first one being position along the X axis, the second along the Y axis.
     * @param e1
     * @param e2
     * @return The relative position of e1 in relation to e2.
     */
    public static int[] getRelative(Entity e1, Entity e2){
        int[] result = new int[2];
        if(e1.getX() > e2.getX()){
            result[0] = 2;
        }else if (e1.getX() < e2.getX()){
            result[0] = 1;
        }else {
            result[0] = 3;
        }
        if(e1.getY() > e2.getY()){
            result[1] = 2;
        }else if (e1.getY() < e2.getY()){
            result[1] = 1;
        }else {
            result[1] = 3;
        }
        return result;
    }
    
    public static BufferedImage generateImage(String path) throws IOException{
        return ImageIO.read(new File(path));
    }
    
    public static int colorToInt(Color c) {
        int R = Math.round(255 * c.getRed());
        int G = Math.round(255 * c.getGreen());
        int B = Math.round(255 * c.getBlue());

        R = (R << 16) & 0x00FF0000;
        G = (G << 8) & 0x0000FF00;
        B = B & 0x000000FF;

        return 0xFF000000 | R | G | B;
    }
    
    public void reseedRNG(long seed){
        r.setSeed(seed);
    }
}
