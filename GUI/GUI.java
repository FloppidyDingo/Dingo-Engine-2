/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import java.util.ArrayList;
import java.util.List;
import objects.Node;

/**
 *
 * @author James
 */
public class GUI{
    private final List<Node> guiList;
    
    public GUI(){
        guiList = new ArrayList<>();
    }
    
    public void addNode(Node n){
        guiList.add(n);
    }
    
    public void removeNode(Node n){
        guiList.remove(n);
    }
    
    public void clear(){
        guiList.clear();
    }
    
    public List<Node> getGuiList(){
        return guiList;
    }
}
