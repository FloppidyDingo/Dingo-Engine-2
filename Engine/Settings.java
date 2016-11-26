/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Engine;

import java.awt.image.BufferedImage;

/**
 *
 * @author James
 */
public class Settings {
    private int resolutionX;
    private int resolutionY;
    private int internalResX;
    private int internalResY;
    private int frameRate;
    private int colorModel;

    public Settings() {
        this.colorModel = BufferedImage.TYPE_INT_ARGB;
        this.frameRate = 30;
        this.internalResY = 480;
        this.internalResX = 640;
        this.resolutionY = 480;
        this.resolutionX = 640;
    }

    public int getResolutionX() {
        return resolutionX;
    }

    public void setResolutionW(int resolutionW) {
        this.resolutionX = resolutionW;
    }

    public int getResolutionY() {
        return resolutionY;
    }

    public void setResolutionY(int resolutionY) {
        this.resolutionY = resolutionY;
    }

    public int getInternalResX() {
        return internalResX;
    }

    public void setInternalResX(int internalResX) {
        this.internalResX = internalResX;
    }

    public int getInternalResY() {
        return internalResY;
    }

    public void setInternalResY(int internalResY) {
        this.internalResY = internalResY;
    }

    public int getFrameRate() {
        return frameRate;
    }

    public void setFrameRate(int frameRate) {
        this.frameRate = frameRate;
    }

    public int getColorModel() {
        return colorModel;
    }

    public void setColorModel(int colorModel) {
        this.colorModel = colorModel;
    }
    
}
