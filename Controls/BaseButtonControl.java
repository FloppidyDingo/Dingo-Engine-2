/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controls;

import GUI.Button;
import java.awt.event.MouseEvent;
import objects.Node;

/**
 *
 * @author James
 */
public class BaseButtonControl extends ButtonControl{
    private final Button b;

    public BaseButtonControl(Button b) {
        this.b = b;
    }
    
    @Override
    public void onPress() {
        b.setActiveImage(b.getSelect());
    }

    @Override
    public void onRelease() {
        b.setActiveImage(b.getHover());
    }

    @Override
    public void onHover() {
        b.setActiveImage(b.getHover());
    }
    
    @Override
    public void onIdle() {
        b.setActiveImage(b.getIdle());
    }

    @Override
    public void perform(Node e) {
        
    }
    
    @Override
    public void buttonPerform(MouseEvent e){
        if(b.getOnAction() != null){
            b.getOnAction().event(e);
        }
    }

}
