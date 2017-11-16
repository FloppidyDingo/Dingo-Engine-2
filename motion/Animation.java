/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package motion;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author James
 */
public class Animation {
    
    private final List<Integer> idList;
    private int frameSkipping;
    private int currentFrame;
    private int currentSkin;
    private boolean running;
    private String ID;
    
    /**
     *
     */
    public Animation(){
        frameSkipping = 0;
        currentFrame = 0;
        currentSkin = 0;
        running = false;
        idList = new ArrayList<>();
    }

    /**
     *Returns the animation's ID
     * @return
     */
    public String getID() {
        return ID;
    }

    /**
     *set's the animation's ID (may break functionality)
     * @param ID
     */
    public void setID(String ID) {
        this.ID = ID;
    }
    
    /**
     *adds a frame to the animation
     * @param frame the frame number
     */
    public void addFrame(int frame){
        idList.add(frame);
    }
    
    /**
     *removes a frame
     * @param frame the frame number
     */
    public void removeFrame(int frame){
        idList.remove(frame);
    }
    
    /**
     *Goes to the next frame in the animation. Is automatically called by the engine.
     * @return 
     */
    public int nextFrame(){
        if (running) {
            if (currentFrame == frameSkipping) {
                currentFrame = -1;
                currentSkin++;
                if (currentSkin > idList.size() - 1) {
                    currentSkin = 0;
                }
                return idList.get(currentSkin);
            }
            currentFrame++;
        }
        return -1;
    }

    /**
     *returns true if the animation is playing.
     * @return
     */
    public boolean isRunning() {
        return running;
    }

    /**
     *Plays the animation if true, stops if false.
     * @param running
     */
    public void setRunning(boolean running) {
        this.running = running;
    }
    
    /**
     *returns the number of frames to wait until going to the next animation frame.
     * @return
     */
    public int getFrameSkipping() {
        return frameSkipping;
    }

    /**
     *sets the number of frames to wait until going to the next animation frame.
     * @param frameSkipping
     */
    public void setFrameSkipping(int frameSkipping) {
        this.frameSkipping = frameSkipping;
    }

    public void setFrame(int i) {
        currentSkin = i;
    }
    
    public void reset(){
        currentFrame = 0;
        currentSkin = 0;
    }

    public int getCurrentSkin() {
        return currentSkin;
    }
        
}
