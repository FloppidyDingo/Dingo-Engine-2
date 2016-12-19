/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Engine;

import Controls.ButtonControl;
import GUI.Button;
import GUI.GUI;
import objects.Node;
import Media.AdvancedMedia.Audio.DingoSoundDriver;
import Media.MediaPipeline;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;
import motion.Vector;
import objects.Entity;
import objects.Spawn;
import objects.Trigger;

/**
 * The main engine. Have your main class extend this class, then override all of
 * the abstract methods.
 *
 * @author James
 */
public abstract class Engine {

    //<editor-fold defaultstate="collapsed" desc="Engine Variables">
    private static final int MapVersion = 2;
    private static final String version = "2.0.1";
    private int fps;
    private List<Extension> extensions;
    private List<KeyMap> keys;
    private Timer animtimer;
    private int preComputedRate;
    public GPU gpu;
    public JFrame stage;
    public MediaPipeline media;
    public GUI guiNode;
//</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Physics Variables">
    private List<Entity> entityList;//solid entities
    private List<Trigger> triggerList;//activate when intersecting target
    private List<Spawn> spawnList;//spawn point for entities
    private int mode;
    private int gravity;
    private int maxGVelocity;

    /**
     * the physics engine will run in platformer mode.
     */
    public static final int PLATFORMER = 0;

    /**
     * the physics engine will run in top-down mode.
     */
    public static final int TOP_DOWN = 1;

    private Vector camDir;
    private Vector camPos;
    private boolean trackcam;
    private Entity camTarget;
    private int collisionMode = 3;
//</editor-fold>

    /**
     * call this to start the engine
     *
     * @param settings
     */
    public void start(Settings settings) {
        //<editor-fold defaultstate="collapsed" desc="init Engine">
        System.setProperty("sun.java2d.opengl", "true");
        fps = settings.getFrameRate();
        this.animtimer = new Timer(1000 / fps) {
            @Override
            public void action() {
                frame2();
            }
        };
        animtimer.stop();
        extensions = new ArrayList<>();
        keys = new ArrayList<>();
        stage = new JFrame();
        stage.setMinimumSize(new Dimension(settings.getResolutionX(), settings.getResolutionY()));
        gpu = new GPU(settings);
        gpu.addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {
                
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                for (Node node : guiNode.getGuiList()) {
                    Rectangle r = new Rectangle(node.getX() - (node.getWidth() / 2), node.getY() - (node.getHeight() / 2),
                            node.getWidth(), node.getHeight());
                    Point point = new Point((int)(e.getX() * ((float)gpu.getResX() / gpu.getWidth())) - (gpu.getResX() / 2), 
                        (int)(e.getY() * ((float)gpu.getResY() / gpu.getHeight())) - (gpu.getResY() / 2));
                    if(node instanceof Button){
                        if (r.contains(point)) {
                            ((ButtonControl) node.getControl()).onHover();
                        }else{
                            ((ButtonControl) node.getControl()).onIdle();
                        }
                    }
                }
            }
        });
        gpu.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                for (Node node : guiNode.getGuiList()) {
                    Rectangle r = new Rectangle(node.getX() - (node.getWidth() / 2), node.getY() - (node.getHeight() / 2),
                            node.getWidth(), node.getHeight());
                    Point point = new Point((int)(e.getX() * ((float)gpu.getResX() / gpu.getWidth())) - (gpu.getResX() / 2), 
                        (int)(e.getY() * ((float)gpu.getResY() / gpu.getHeight())) - (gpu.getResY() / 2));
                    if(r.contains(point)){
                        if (node instanceof Button) {
                            ((ButtonControl) node.getControl()).buttonPerform(e);
                        }
                    }
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
                for (Node node : guiNode.getGuiList()) {
                    Rectangle r = new Rectangle(node.getX() - (node.getWidth() / 2), node.getY() - (node.getHeight() / 2),
                            node.getWidth(), node.getHeight());
                    Point point = new Point((int)(e.getX() * ((float)gpu.getResX() / gpu.getWidth())) - (gpu.getResX() / 2), 
                        (int)(e.getY() * ((float)gpu.getResY() / gpu.getHeight())) - (gpu.getResY() / 2));
                    if(r.contains(point)){
                        if (node instanceof Button) {
                            ((ButtonControl) node.getControl()).onPress();
                        }
                    }
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                for (Node node : guiNode.getGuiList()) {
                    Rectangle r = new Rectangle(node.getX() - (node.getWidth() / 2), node.getY() - (node.getHeight() / 2),
                            node.getWidth(), node.getHeight());
                    Point point = new Point((int)(e.getX() * ((float)gpu.getResX() / gpu.getWidth())) - (gpu.getResX() / 2), 
                        (int)(e.getY() * ((float)gpu.getResY() / gpu.getHeight())) - (gpu.getResY() / 2));
                    if(r.contains(point)){
                        if (node instanceof Button) {
                            ((ButtonControl) node.getControl()).onRelease();
                        }
                    }
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                
            }

            @Override
            public void mouseExited(MouseEvent e) {
                
            }
        });
        gpu.addKeyListener(new KeyListener() {

            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                for (KeyMap key : keys) {
                    if (e.getKeyCode() == key.getCode()) {
                        KeyPressed(key.getKey());
                        extensions.stream().filter((ext) -> (ext.isEnabled())).forEach((ext) -> {
                            ext.KeyPressed(key.getKey());
                        });
                    }
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                for (KeyMap key : keys) {
                    if (e.getKeyCode() == key.getCode()) {
                        KeyReleased(key.getKey());
                        extensions.stream().filter((ext) -> (ext.isEnabled())).forEach((ext) -> {
                            ext.KeyReleased(key.getKey());
                        });
                    }
                }
            }
        });
        guiNode = new GUI();
        gpu.setGui(guiNode);
        stage.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                if (media != null) {
                    media.halt();
                }
                System.exit(0);
            }
        });
//</editor-fold>
        //<editor-fold defaultstate="collapsed" desc="init Physics">
        this.camDir = new Vector();
        this.camPos = new Vector();
        gravity = 1;
        mode = 0;
        entityList = new ArrayList<>();
        triggerList = new ArrayList<>();
        spawnList = new ArrayList<>();
//</editor-fold>
        
        if(settings.isFullScreen()){
            stage.setUndecorated(true);
            gpu.enterFullScreen(stage, settings.getDisplay(), settings);
        }
        stage.add(gpu);
        stage.setVisible(true);
        gpu.setFocusable(true);
        gpu.requestFocus();
        this.init();
    }

    private void frame2() {
        frame();
        for (Node n : entityList) {
            if (n.getControl() != null) {
                n.getControl().perform(n);
            }
        }
        for (Node n : guiNode.getGuiList()) {
            if (n.getControl() != null) {
                n.getControl().perform(n);
            }
        }
        extensions.stream().forEach((extension) -> {
            if (extension.isEnabled()) {
                extension.frame();
            }
        });
        TimeQueue.masterCheck();
        //<editor-fold defaultstate="collapsed" desc="Physics">
        //<editor-fold defaultstate="collapsed" desc="spawner">
        for (Spawn s : spawnList) {
            if (s.getTime() > s.getCount()) {
                onSpawning(s, new Entity(null, ""));
                s.setTime(0);
            }
            s.setTime(s.getTime() + 1);
        }
//</editor-fold>
        //<editor-fold defaultstate="collapsed" desc="gravity">
        if (mode == PLATFORMER) {
            for (Entity e : entityList) {
                if (!(e.getDir().getY() > maxGVelocity)) {
                    e.getDir().applyForce(0, gravity * e.getMass());
                }
            }
        }
//</editor-fold>
        //<editor-fold defaultstate="collapsed" desc="Collisions (triggers, doors, solid entities">
        for (Entity e : entityList) {
            for (Entity e2 : entityList) {
                if ((!e.getID().equals(e2.getID()))) {
                    //collision between e and e2
                    if ((!e.getID().equals(e2.getID()))) {
                        CollisionBlock cb = predictCollision(e, e2);
                        switch (cb.getSide()) {
                            case 1: {
                                e.getDir().setY(cb.getyDist());
                                onCollision(e, e2);
                                break;
                            }
                            case 2: {
                                e.getDir().setY(-cb.getyDist());
                                onCollision(e, e2);
                                break;
                            }
                            case 3: {
                                e.getDir().setX(cb.getxDist());
                                onCollision(e, e2);
                                break;
                            }
                            case 4: {
                                e.getDir().setX(-cb.getxDist());
                                onCollision(e, e2);
                                break;
                            }
                        }
                    }
                }
            }
            for (Trigger t : triggerList) {
                if ((!e.intersects((Node) t)) & t.isEnabled()) {
                    t.fire();
                }
            }
        }

//</editor-fold>
        //<editor-fold defaultstate="collapsed" desc="camera movement">
        //camPos.setDirection(camPos.getX() + camDir.getX(), camPos.getY() + camDir.getY());
        camPos.applyForce(camDir);
        for (Entity e : entityList) {
            e.setX(e.getX() - camDir.getX());
            e.setY(e.getY() + camDir.getY());
        }
        for (Trigger e : triggerList) {
            e.setX(e.getX() - camDir.getX());
            e.setY(e.getY() + camDir.getY());
        }
        for (Spawn e : spawnList) {
            e.setX(e.getX() - camDir.getX());
            e.setY(e.getY() + camDir.getY());
        }
//</editor-fold>
        //<editor-fold defaultstate="collapsed" desc="movement">
        for (Entity e : entityList) {
            if (e.getMass() != 0) {
                if (e.getDir().getX() != 0) {
                    e.setX((e.getX() + e.getDir().getX()));
                }
                if (e.getDir().getY() != 0) {
                    e.setY((e.getY() + e.getDir().getY()));
                }
            }
        }
//</editor-fold>
        postPhysicsTick();
//</editor-fold>
        if (media != null) {
            media.process();
        }
        for (Node n : gpu.getScene().getItems()) {
            if ("entity".equals(n.getType())) {
                ((Entity) n).updateAnimation();
            }
        }
        gpu.draw();
    }

    //<editor-fold defaultstate="collapsed" desc="Engine Methods">
    public void beginMediaSystem(int sampleRate, int bitDepth) {
        media = new MediaPipeline();
        DingoSoundDriver DSD = new DingoSoundDriver();
        DSD.init(sampleRate, bitDepth, (sampleRate / fps) * (bitDepth / 8));
        media.setAudio(DSD);
        media.start();
        preComputedRate = sampleRate * (bitDepth / 8);
    }

    public int getComputedMediaBufferSize() {
        return preComputedRate / fps * 2;
    }

    /**
     * returns the compatible map structure version
     *
     * @return
     */
    public static int getMapVersion() {
        return MapVersion;
    }

    /**
     *
     * @return the set FPS (does NOT return the actual FPS)
     */
    public int getFps() {
        return fps;
    }

    /**
     * Sets the engine's timing. Changing this changes the speed of the game
     * itself.
     *
     * @param fps
     */
    public void setFps(int fps) {
        this.fps = fps;
        animtimer.setInterval(1000 / fps);
    }

    /**
     * starts/stops the animation timer.
     *
     * @param run
     */
    public void setRunning(boolean run) {
        if (run) {
            animtimer.start();
        } else {
            animtimer.stop();
        }
    }

    /**
     * returns the engine's version
     *
     * @return
     */
    public static String getVersion() {
        return version;
    }

    /**
     * Adds an extension class to the Engine
     *
     * @param ext The extension class to add
     */
    public void addExtension(Extension ext) {
        extensions.add(ext);
        ext.setEngine(this);
        ext.init();
    }

    /**
     * Removes an extension class from the Engine
     *
     * @param ext The extension class to remove
     */
    public void removeExtension(Extension ext) {
        extensions.remove(ext);
        ext.detach();
    }

    /**
     * Maps a new key to the internal Key Mapper
     *
     * @param code
     * @param key
     */
    public void addKey(int code, String key) {
        keys.add(new KeyMap(key, code));
    }

    /**
     * Removes a key mapping based by KeyCode
     *
     * @param code
     */
    public void removeKey(int code) {
        int size = keys.size();
        for (int i = 0; i < size; i++) {
            if (keys.get(i).getCode() == code) {
                keys.remove(i);
                size = keys.size();
            }
        }
    }

    /**
     * Removes a key mapping based by Key
     *
     * @param key
     */
    public void removeKey(String key) {
        int size = keys.size();
        for (int i = 0; i < size; i++) {
            if (keys.get(i).getKey() == null ? key == null : keys.get(i).getKey().equals(key)) {
                keys.remove(i);
                size = keys.size();
            }
        }
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Physics Methods">
    /**
     * sets the location of the camera
     *
     * @param camPos
     */
    public void setCamPos(Vector camPos) {
        this.camPos = camPos;
    }

    /**
     * gets the vector storing the camera's location
     *
     * @return the camera's position vector
     */
    public Vector getCamPos() {
        return camPos;
    }

    /**
     *
     * @return
     */
    public Vector getCamDir() {
        return camDir;
    }

    /**
     *
     * @param camDir
     */
    public void setCamDir(Vector camDir) {
        this.camDir = camDir;
    }

    /**
     *
     * @return
     */
    public int getMode() {
        return mode;
    }

    /**
     *
     * @param mode
     */
    public void setMode(int mode) {
        this.mode = mode;
    }

    /**
     *
     * @return
     */
    public List<Entity> getEntityList() {
        return entityList;

    }

    /**
     *
     * @param entityList
     */
    public void setEntityList(List<Entity> entityList) {
        this.entityList = entityList;
    }

    /**
     *
     * @return
     */
    public List<Trigger> getTriggerList() {
        return triggerList;
    }

    /**
     *
     * @param triggerList
     */
    public void setTriggerList(List<Trigger> triggerList) {
        this.triggerList = triggerList;
    }

    /**
     *
     * @return
     */
    public List<Spawn> getSpawnList() {
        return spawnList;
    }

    /**
     *
     * @param spawnList
     */
    public void setSpawnList(List<Spawn> spawnList) {
        this.spawnList = spawnList;
    }

    /**
     *
     * @return
     */
    public boolean isTrackcam() {
        return trackcam;
    }

    /**
     *
     * @param trackcam
     */
    public void setTrackcam(boolean trackcam) {
        this.trackcam = trackcam;
    }

    /**
     *
     * @return
     */
    public Entity getCamTarget() {
        return camTarget;
    }

    /**
     *
     * @param camTarget
     */
    public void setCamTarget(Entity camTarget) {
        this.camTarget = camTarget;
    }

    /**
     *
     * @return
     */
    public int getGravity() {
        return gravity;
    }

    /**
     *
     * @param gravity
     */
    public void setGravity(int gravity) {
        this.gravity = gravity;
    }

    public int getMaxGVelocity() {
        return maxGVelocity;
    }

    public void setTerminalVelocity(int maxGVelocity) {
        this.maxGVelocity = maxGVelocity;
    }

    public int getCollisionMode() {
        return collisionMode;
    }

    public void setCollisionMode(int collisionMode) {
        this.collisionMode = collisionMode;
    }

    private CollisionBlock predictCollision(Entity source, Entity target) {
        CollisionBlock cb = new CollisionBlock();
        cb.setSide(0);
        Vector v = source.getDir();
        int YAxis;// 0 none, 1 up, 2 down
        int XAxis;// 0 none, 1 left, 2 right
        int x;
        int y;
        Rectangle r;
        
        if (v.getX() == 0) {
            XAxis = 0;
        } else if (v.getX() < 0) {
            XAxis = 1;
        } else {
            XAxis = 2;
        }
        if (v.getY() == 0) {
            YAxis = 0;
        } else if (v.getY() < 0) {
            YAxis = 1;
        } else {
            YAxis = 2;
        }

        if ((XAxis == 0) & (YAxis == 0)) {//no movement
            return cb;
        }
        if ((XAxis == 1) & (YAxis == 0)) {//straight left
            r = target.getBound();
            x = source.getX() - (source.getWidth() / 2);
            y = source.getY() - (source.getHeight() / 2);
            for (int px = 0; px < v.getX(); px--) {
                for (int py = 0; py < source.getHeight(); py++) {
                    if (r.contains(x + px, y + py)) {
                        cb.setSide(4);
                        break;
                    }
                }
            }
        }
        if ((XAxis == 2) & (YAxis == 0)) {//straight right
            r = target.getBound();
            x = source.getX() + (source.getWidth() / 2);
            y = source.getY() - (source.getHeight() / 2);
            for (int px = 0; px < v.getX(); px++) {
                for (int py = 0; py < source.getHeight(); py++) {
                    if (r.contains(x + px, y + py)) {
                        cb.setSide(3);
                        break;
                    }
                }
            }
        }
        if ((XAxis == 0) & (YAxis == 1)) {//straight up
            r = target.getBound();
            x = source.getX() - (source.getWidth() / 2);
            y = source.getY() - (source.getHeight() / 2);
            for (int py = 0; py < v.getY(); py--) {
                for (int px = 0; px < source.getWidth(); px++) {
                    if (r.contains(x + px, y + py)) {
                        cb.setSide(2);
                        break;
                    }
                }
            }
        }
        if ((XAxis == 0) & (YAxis == 2)) {//straight down
            r = target.getBound();
            x = source.getX() - (source.getWidth() / 2);
            y = source.getY() + (source.getHeight() / 2);
            for (int py = 0; py < v.getY(); py++) {
                for (int px = 0; px < source.getWidth(); px++) {
                    if (r.contains(x + px, y + py)) {
                        cb.setSide(1);
                        break;
                    }
                }
            }
        }
        if ((XAxis == 1) & (YAxis == 1)) {//up left
            r = target.getBound();
            x = source.getX() - (source.getWidth() / 2);
            y = source.getY() - (source.getHeight() / 2);
            for (int py = 0; py < v.getY(); py--) {
                for (int px = 0; px < source.getWidth(); px++) {
                    if (r.contains(x + px, y + py)) {
                        cb.setSide(2);
                        break;
                    }
                }
            }
            r = target.getBound();
            x = source.getX() - (source.getWidth() / 2);
            y = source.getY() - (source.getHeight() / 2);
            for (int px = 0; px < v.getX(); px--) {
                for (int py = 0; py < source.getHeight(); py++) {
                    if (r.contains(x + px, y + py)) {
                        cb.setSide(4);
                        break;
                    }
                }
            }
        }
        if ((XAxis == 2) & (YAxis == 1)) {//up right
            r = target.getBound();
            x = source.getX() - (source.getWidth() / 2);
            y = source.getY() - (source.getHeight() / 2);
            for (int py = 0; py < v.getY(); py--) {
                for (int px = 0; px < source.getWidth(); px++) {
                    if (r.contains(x + px, y + py)) {
                        cb.setSide(2);
                        break;
                    }
                }
            }
            r = target.getBound();
            x = source.getX() + (source.getWidth() / 2);
            y = source.getY() - (source.getHeight() / 2);
            for (int px = 0; px < v.getX(); px++) {
                for (int py = 0; py < source.getHeight(); py++) {
                    if (r.contains(x + px, y + py)) {
                        cb.setSide(3);
                        break;
                    }
                }
            }
        }
        if ((XAxis == 1) & (YAxis == 2)) {//down left
            r = target.getBound();
            x = source.getX() - (source.getWidth() / 2);
            y = source.getY() + (source.getHeight() / 2);
            for (int py = 0; py < v.getY(); py++) {
                for (int px = 0; px < source.getWidth(); px++) {
                    if (r.contains(x + px, y + py)) {
                        cb.setSide(1);
                        break;
                    }
                }
            }
            r = target.getBound();
            x = source.getX() - (source.getWidth() / 2);
            y = source.getY() - (source.getHeight() / 2);
            for (int px = 0; px < v.getX(); px--) {
                for (int py = 0; py < source.getHeight(); py++) {
                    if (r.contains(x + px, y + py)) {
                        cb.setSide(4);
                        break;
                    }
                }
            }
        }
        if ((XAxis == 2) & (YAxis == 2)) {//down right
            r = target.getBound();
            x = source.getX() - (source.getWidth() / 2);
            y = source.getY() + (source.getHeight() / 2);
            for (int py = 0; py < v.getY(); py++) {
                for (int px = 0; px < source.getWidth(); px++) {
                    if (r.contains(x + px, y + py)) {
                        cb.setSide(1);
                        break;
                    }
                }
            }
            r = target.getBound();
            x = source.getX() + (source.getWidth() / 2);
            y = source.getY() - (source.getHeight() / 2);
            for (int px = 0; px < v.getX(); px++) {
                for (int py = 0; py < source.getHeight(); py++) {
                    if (r.contains(x + px, y + py)) {
                        cb.setSide(3);
                        break;
                    }
                }
            }
        }
        return cb;
    }
//</editor-fold>

    public void setScene(Scene s) {
        gpu.setScene(s);
        for(Node n : s.getItems()){
            switch(n.getType()){
                case "entity":{
                    entityList.add((Entity)n);
                    break;
                }
                case "trigger":{
                    triggerList.add((Trigger)n);
                    break;
                }
                case "spawn":{
                    spawnList.add((Spawn)n);
                    break;
                }
            }
        }
    }
    
    /**
     * called every time a new frame is drawn.
     */
    public abstract void frame();

    /**
     * called every time a spawner wants to spawn.
     *
     * @param spawn the corresponding spawner
     * @param e the entity object generated by the spawner
     */
    public abstract void onSpawning(Spawn spawn, Entity e);

    /**
     * called every time the physics engine finishes processing.
     */
    public abstract void postPhysicsTick();

    /**
     * called whenever a collision is detected.
     *
     * @param e1
     * @param e2
     */
    public abstract void onCollision(Entity e1, Entity e2);

    /**
     * called in your main class. put all of your startup code in here
     */
    public abstract void init();

    /**
     * Called when the internal KeyMapper detects a key press
     *
     * @param key The key that was pressed
     */
    public abstract void KeyPressed(String key);

    /**
     * Called when the internal KeyMapper detects a key release
     *
     * @param key The key that was released
     */
    public abstract void KeyReleased(String key);

    private class KeyMap {

        private final int code;
        private final String key;

        public KeyMap(String key, int code) {
            this.code = code;
            this.key = key;
        }

        public int getCode() {
            return code;
        }

        public String getKey() {
            return key;
        }
    }

    class CollisionBlock {

        private int xDist;
        private int yDist;
        private int axis;//0 = none, 1 = top, 2 = bottom, 3 = left, 4 = right

        public int getxDist() {
            return xDist;
        }

        public void setxDist(int xDist) {
            this.xDist = xDist;
        }

        public int getyDist() {
            return yDist;
        }

        public void setyDist(int yDist) {
            this.yDist = yDist;
        }

        public int getSide() {
            return axis;
        }

        public void setSide(int side) {
            this.axis = side;
        }

    }
}
