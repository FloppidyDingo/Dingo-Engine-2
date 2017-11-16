/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Engine;

import Controls.Listeners.GameEventListener;
import java.util.ArrayList;
import java.util.List;
import objects.Entity;
import objects.Spawn;

/**
 *
 * @author James
 */
public abstract class Extension {
    public Engine engine;
    private boolean enabled;
    private List<GameEventListener> listeners;

    public Extension() {
        this.listeners = new ArrayList<>();
    }
    
    public abstract void frame();
    
    /**
     *called every time a spawner wants to spawn.
     * @param spawn the corresponding spawner
     * @param e the entity object generated by the spawner
     */
    public void onSpawning(Spawn spawn, Entity e){}
    
    /**
     *called every time the physics engine finishes processing.
     */
    public void postPhysicsTick(){}
    
    /**
     *called whenever a collision is detected.
     * @param e1
     * @param e2
     */
    public void onCollision(Entity e1, Entity e2){}
    
    /**
     *Called when added to the Engine. Put all of your startup code in here
     */
    public abstract void init();
    
    /**
     *Called when removed from the Engine. Put cleanup code here
     */
    public abstract void detach();
    
    public void KeyReleased(String key){}
    
    public void KeyPressed(String key){}
    
    protected void setEngine(Engine e){
        this.engine = e;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public List<GameEventListener> getListeners() {
        return listeners;
    }
    
    public void addListener(GameEventListener e){
        listeners.add(e);
    }
    
    public void removeListener(GameEventListener e){
        listeners.remove(e);
    }
    
    public void clearListeners(){
        listeners.clear();
    }
}
