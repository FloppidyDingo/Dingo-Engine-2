/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controls;

import java.awt.event.MouseEvent;

/**
 *
 * @author James
 */
public abstract class ButtonControl extends Control{
    
    public abstract void onPress();
    
    public abstract void onRelease();
    
    public abstract void onHover();
    
    public abstract void onIdle();
    
    public abstract void buttonPerform(MouseEvent e);
}
