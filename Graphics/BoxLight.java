/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Graphics;

/**
 *
 * @author James
 */
public class BoxLight extends Light{
    private int width;

    public int getWidth() {
        return width;
    }

    public void setWidth(int length) {
        this.width = length;
    }
    
    public BoxLight(){
        currentType = type.Box;
    }
}
