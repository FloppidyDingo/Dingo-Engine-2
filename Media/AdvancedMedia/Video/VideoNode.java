/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Media.AdvancedMedia.Video;

import objects.Node;

/**
 *
 * @author James
 */
public abstract class VideoNode extends Node{
    public int resX;
    public int resY;
    
    public abstract void update();
    
    public abstract void stop();
}
