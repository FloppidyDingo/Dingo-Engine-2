/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Media.AdvancedMedia.Video;

import Engine.GPU;
import java.awt.image.BufferedImage;


/**
 *
 * @author James
 */
public class BufferedVideo extends VideoNode{
    private final BufferedImage image;
    
    @Override
    public void update(){
        for (int iy = 0; iy < image.getWidth(); iy++) {
            for (int ix = 0; ix < image.getHeight(); ix++) {
                image.setRGB(ix, iy, input.getData()[(iy * resX) + ix]);
            }
        }
    }

    public BufferedVideo(GPU gpu, int w, int h) {
        resX = w;
        resY = h;
        image = new BufferedImage(resX, resY, gpu.getColor());
    }

    @Override
    public void stop() {
        
    }

    @Override
    public BufferedImage render(GPU gpu) {
        return image;
    }

    @Override
    public int getWidth() {
        return resX;
    }

    @Override
    public int getHeight() {
        return resY;
    }
}
