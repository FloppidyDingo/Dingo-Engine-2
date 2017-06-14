/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Engine;

import objects.Node;
import java.util.ArrayList;
import java.util.List;
import Graphics.Light;

/**
 *
 * @author James
 */
public class Scene {
    
    private final List<Node> items;
    private final List<Node> GUI;
    private final List<Light> lights;
    private final List<Node> removedItems;
    private final List<Node> removedGUI;
    private final List<Light> removedLights;
    
    public Scene() {
        this.items = new ArrayList<>();
        this.lights = new ArrayList<>();
        this.GUI = new ArrayList<>();
        this.removedGUI = new ArrayList<>();
        this.removedLights = new ArrayList<>();
        this.removedItems = new ArrayList<>();
    }
    
    public void attachChild(Node item){
        items.add(item);
    }
    
    public void removeChild(Node item){
        removedItems.add(item);
    }

    public List<Node> getItems() {
        return items;
    }
    
    public void attachLight(Light item){
        lights.add(item);
    }
    
    public void removeLight(Light item){
        removedLights.add(item);
    }

    public List<Light> getLights() {
        return lights;
    }
    
    public void attachGUIChild(Node item){
        GUI.add(item);
    }
    
    public void removeGUIItem(Node item){
        removedGUI.add(item);
    }

    public List<Node> getGUI() {
        return GUI;
    }

    void updateLists() {
        GUI.removeAll(removedGUI);
        items.removeAll(removedItems);
        lights.removeAll(removedLights);
    }
}
