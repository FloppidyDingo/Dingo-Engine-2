/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Engine;

import objects.Node;
import java.util.ArrayList;
import java.util.List;
import objects.Light;

/**
 *
 * @author James
 */
public class Scene {
    
    private final List<Node> items;
    private final List<Node> GUI;
    private final List<Light> lights;
    
    public Scene() {
        this.items = new ArrayList<>();
        this.lights = new ArrayList<>();
        this.GUI = new ArrayList<>();
    }
    
    public void attachChild(Node item){
        items.add(item);
    }
    
    public void removeChild(Node item){
        items.remove(item);
    }

    public List<Node> getItems() {
        return items;
    }
    
    public void attachLight(Light item){
        lights.add(item);
    }
    
    public void removeLight(Light item){
        lights.remove(item);
    }

    public List<Light> getLights() {
        return lights;
    }
    
    public void attachGUIChild(Node item){
        GUI.add(item);
    }
    
    public void removeGUIItem(Node item){
        GUI.remove(item);
    }

    public List<Node> getGUI() {
        return GUI;
    }
}
