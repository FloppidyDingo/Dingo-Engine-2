/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Mapping;

import Engine.Scene;
import java.util.ArrayList;
import java.util.List;
import motion.Animation;
import objects.Entity;
import objects.Light;
import objects.Skin;
import objects.Spawn;
import objects.Trigger;

/**
 *
 * @author James
 */
public class Map {
    private List<Entity> entityList;//entities
    private List<Trigger> triggerList;//activate when intersecting targets
    private List<Spawn> spawnList;//spawn point for entities
    private List<Skin> skinList;//skins for entities
    private List<Animation> animationList;//list of animations
    private Scene scene;
    private List<Light> lightList;
    
    public List<Skin> getSkinList() {
        return skinList;
    }

    public void setSkinList(List<Skin> skinList) {
        this.skinList = skinList;
    }

    public List<Animation> getAnimationList() {
        return animationList;
    }

    public void setAnimationList(List<Animation> animationList) {
        this.animationList = animationList;
    }

    public Map() {
        this.entityList = new ArrayList<>();
        this.triggerList = new ArrayList<>();
        this.spawnList = new ArrayList<>();
        this.skinList = new ArrayList<>();
        this.animationList = new ArrayList<>();
    }
    
    public Entity getEntity(String ID){
        for (Entity col : entityList) {
            if (col.getID().equals(ID)) {
                return col;
            }
        }
        return null;
    }
    
    public Trigger getTrigger(String ID){
        for (Trigger col : triggerList) {
            if (col.getID().equals(ID)) {
                return col;
            }
        }
        return null;
    }
    
    public Spawn getSpawn(String ID){
        for (Spawn col : spawnList) {
            if (col.getID().equals(ID)) {
                return col;
            }
        }
        return null;
    }
    
    public Skin getSkin(String ID){
        for (Skin col : skinList) {
            if (col.getID().equals(ID)) {
                return col;
            }
        }
        return null;
    }
    
    public Animation getAnimation(String ID){
        for (Animation col : animationList) {
            if (col.getID().equals(ID)) {
                return col;
            }
        }
        return null;
    }
    
    public Light getLight(String ID){
        for (Light col : lightList) {
            if (col.getID().equals(ID)) {
                return col;
            }
        }
        return null;
    }
    
    /**
     * returns the map's scene (must call compileScene() first, or you will get null)
     * @return the scene generated from a map
     */
    public Scene getScene() {
        return scene;
    }
    
    public List<Entity> getEntityList() {
        return entityList;
    }

    public void setEntityList(List<Entity> entityList) {
        this.entityList = entityList;
    }

    public List<Trigger> getTriggerList() {
        return triggerList;
    }

    public void setTriggerList(List<Trigger> triggerList) {
        this.triggerList = triggerList;
    }

    public List<Spawn> getSpawnList() {
        return spawnList;
    }

    public void setSpawnList(List<Spawn> spawnList) {
        this.spawnList = spawnList;
    }

    public List<Light> getLightList() {
        return lightList;
    }

    public void setLightList(List<Light> lightList) {
        this.lightList = lightList;
    }
    
    /**
     * generates a scene with the map data
     * @return The compiled scene
     */
    public Scene compileScene(){
        scene = new Scene();
        for (Entity e : entityList) {
            scene.attachChild(e);
        }
        for (Spawn e : spawnList) {
            scene.attachChild(e);
        }
        for (Trigger e : triggerList) {
            scene.attachChild(e);
        }
        for (Light l : lightList) {
            scene.attachLight(l);
        }
        return scene;
    }
}