/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Engine;

import Cinematics.TimeQueue;
import Graphics.GPU;
import Controls.ButtonControl;
import Controls.Listeners.GameEventListener;
import Controls.Listeners.GameKeyListener;
import Controls.Listeners.GameMouseListener;
import GUI.Button;
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

    public static final int MAPVERSION = 3;
    public static final String VERSION = "2.3.8";
    //<editor-fold defaultstate="collapsed" desc="Engine Variables">

    private int fps;
    private List<Extension> extensions;
    private List<Extension> extensionRemoval;
    private List<Extension> extensionAdd;
    private List<KeyMap> keys;
    private List<GameEventListener> listeners;
    private Timer animtimer;
    private int preComputedRate;
    public GPU gpu;
    public JFrame stage;
    public MediaPipeline media;
    public Scene scene;
    private boolean tractionControl;
    private int fpsAdj;
    private int TCThreshold;
    private String gameTitle;
    private Point mousePoint;
    private Point prevMousePoint;
    private Vector mouseVelocity;
    public String[] input;
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
     * @param args
     */
    public void start(Settings settings, String[] args) {
        //<editor-fold defaultstate="collapsed" desc="init Engine">
        for (String arg : args) {
            System.out.println(arg);
            if ("-debug".equals(arg)) {
                Debugger debug = new Debugger();
                debug.setVisible(true);
                System.out.println("Debug Console ready");
                settings.setGPUAcceleration(false);
            }
        }
        if(settings.isDebugMode()){
            Debugger debug = new Debugger();
            debug.setVisible(true);
            System.out.println("Debug Console ready");
            settings.setGPUAcceleration(false);
        }
        if (settings.isGPUAcceleration()) {
            System.setProperty("sun.java2d.opengl", "true");
        }
        input = args;
        fps = settings.getFrameRate();
        fpsAdj = fps;
        this.animtimer = new Timer(1000 / fps) {
            @Override
            public void action() {
                frame2();
            }
        };
        animtimer.stop();
        extensions = new ArrayList<>();
        extensionRemoval = new ArrayList<>();
        extensionAdd = new ArrayList<>();
        listeners = new ArrayList<>();
        keys = new ArrayList<>();
        gpu = new GPU(settings, this);
        stage = new JFrame();
        stage.setMinimumSize(new Dimension(settings.getResolutionX(), settings.getResolutionY()));
        mousePoint = new Point(0, 0);
        prevMousePoint = new Point(0, 0);
        mouseVelocity = new Vector();
        gpu.addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                if (scene != null) {
                    for (GameEventListener el : listeners) {
                        if (el instanceof GameMouseListener) {
                            ((GameMouseListener) el).mouseAnalogEvent(e, mouseVelocity, new Point((int) (e.getX()
                                    * ((float) gpu.getResX() / gpu.getWidth())) - (gpu.getResX() / 2), (int) (e.getY()
                                    * ((float) gpu.getResY() / gpu.getHeight())) - (gpu.getResY() / 2)));
                        }
                    }
                    for (Extension extension : extensions) {
                        for (GameEventListener el : extension.getListeners()) {
                            if (el instanceof GameMouseListener) {
                                ((GameMouseListener) el).mouseAnalogEvent(e, mouseVelocity, new Point((int) (e.getX()
                                        * ((float) gpu.getResX() / gpu.getWidth())) - (gpu.getResX() / 2), (int) (e.getY()
                                        * ((float) gpu.getResY() / gpu.getHeight())) - (gpu.getResY() / 2)));
                            }
                        }
                    }
                    for (Node node : scene.getGUI()) {
                        Rectangle r = new Rectangle(node.getX() - (node.getWidth() / 2), node.getY() - (node.getHeight() / 2),
                                node.getWidth(), node.getHeight());
                        mousePoint.setLocation((int) (e.getX() * ((float) gpu.getResX() / gpu.getWidth())) - (gpu.getResX() / 2),
                                (int) (e.getY() * ((float) gpu.getResY() / gpu.getHeight())) - (gpu.getResY() / 2));
                        if (node instanceof Button) {
                            if (r.contains(mousePoint)) {
                                ((ButtonControl) node.getControl()).onHover();
                            } else {
                                ((ButtonControl) node.getControl()).onIdle();
                            }
                        }
                    }
                }
            }
        });
        gpu.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                boolean cons = false;
                if (scene != null) {
                    for (Node node : scene.getGUI()) {
                        Rectangle r = new Rectangle(node.getX() - (node.getWidth() / 2), node.getY() - (node.getHeight() / 2),
                                node.getWidth(), node.getHeight());
                        mousePoint.setLocation((int) (e.getX() * ((float) gpu.getResX() / gpu.getWidth())) - (gpu.getResX() / 2),
                                (int) (e.getY() * ((float) gpu.getResY() / gpu.getHeight())) - (gpu.getResY() / 2));
                        if (r.contains(mousePoint)) {
                            if (node instanceof Button & node.isVisible()) {
                                ((ButtonControl) node.getControl()).buttonPerform(e);
                                cons = true;
                            }
                        }
                    }
                    if (!cons) {
                        for (GameEventListener el : listeners) {
                            if (el instanceof GameMouseListener) {
                                ((GameMouseListener) el).mouseClick(e, new Point((int) (e.getX()
                                        * ((float) gpu.getResX() / gpu.getWidth())) - (gpu.getResX() / 2), (int) (e.getY()
                                        * ((float) gpu.getResY() / gpu.getHeight())) - (gpu.getResY() / 2)));
                            }
                        }
                        for (Extension extension : extensions) {
                            for (GameEventListener el : extension.getListeners()) {
                                if (el instanceof GameMouseListener) {
                                    ((GameMouseListener) el).mouseClick(e, new Point((int) (e.getX()
                                            * ((float) gpu.getResX() / gpu.getWidth())) - (gpu.getResX() / 2), (int) (e.getY()
                                            * ((float) gpu.getResY() / gpu.getHeight())) - (gpu.getResY() / 2)));
                                }
                            }
                        }
                    }
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
                boolean cons = false;
                if (scene != null) {
                    for (Node node : scene.getGUI()) {
                        Rectangle r = new Rectangle(node.getX() - (node.getWidth() / 2), node.getY() - (node.getHeight() / 2),
                                node.getWidth(), node.getHeight());
                        mousePoint.setLocation((int) (e.getX() * ((float) gpu.getResX() / gpu.getWidth())) - (gpu.getResX() / 2),
                                (int) (e.getY() * ((float) gpu.getResY() / gpu.getHeight())) - (gpu.getResY() / 2));
                        if (r.contains(mousePoint)) {
                            if (node instanceof Button) {
                                ((ButtonControl) node.getControl()).onPress();
                                cons = true;
                            }
                        }
                    }
                    if (!cons) {
                        for (GameEventListener el : listeners) {
                            if (el instanceof GameMouseListener) {
                                ((GameMouseListener) el).mousePressed(e, new Point((int) (e.getX()
                                        * ((float) gpu.getResX() / gpu.getWidth())) - (gpu.getResX() / 2), (int) (e.getY()
                                        * ((float) gpu.getResY() / gpu.getHeight())) - (gpu.getResY() / 2)));
                            }
                        }
                        for (Extension extension : extensions) {
                            for (GameEventListener el : extension.getListeners()) {
                                if (el instanceof GameMouseListener) {
                                    ((GameMouseListener) el).mousePressed(e, new Point((int) (e.getX()
                                            * ((float) gpu.getResX() / gpu.getWidth())) - (gpu.getResX() / 2), (int) (e.getY()
                                            * ((float) gpu.getResY() / gpu.getHeight())) - (gpu.getResY() / 2)));
                                }
                            }
                        }
                    }
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                boolean cons = false;
                if (scene != null) {
                    for (Node node : scene.getGUI()) {
                        Rectangle r = new Rectangle(node.getX() - (node.getWidth() / 2), node.getY() - (node.getHeight() / 2),
                                node.getWidth(), node.getHeight());
                        mousePoint.setLocation((int) (e.getX() * ((float) gpu.getResX() / gpu.getWidth())) - (gpu.getResX() / 2),
                                (int) (e.getY() * ((float) gpu.getResY() / gpu.getHeight())) - (gpu.getResY() / 2));
                        if (r.contains(mousePoint)) {
                            if (node instanceof Button) {
                                ((ButtonControl) node.getControl()).onRelease();
                                cons = true;
                            }
                        }
                    }
                    if (!cons) {
                        for (GameEventListener el : listeners) {
                            if (el instanceof GameMouseListener) {
                                ((GameMouseListener) el).mouseReleased(e, new Point((int) (e.getX()
                                        * ((float) gpu.getResX() / gpu.getWidth())) - (gpu.getResX() / 2), (int) (e.getY()
                                        * ((float) gpu.getResY() / gpu.getHeight())) - (gpu.getResY() / 2)));
                            }
                        }
                        for (Extension extension : extensions) {
                            for (GameEventListener el : extension.getListeners()) {
                                if (el instanceof GameMouseListener) {
                                    ((GameMouseListener) el).mouseReleased(e, new Point((int) (e.getX()
                                            * ((float) gpu.getResX() / gpu.getWidth())) - (gpu.getResX() / 2), (int) (e.getY()
                                            * ((float) gpu.getResY() / gpu.getHeight())) - (gpu.getResY() / 2)));
                                }
                            }
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
                for (GameEventListener el : listeners) {
                    for (KeyMap key : keys) {
                        if (key.getCode() == e.getKeyCode()) {
                            if (el instanceof GameKeyListener) {
                                ((GameKeyListener) el).keyPressed(e, key.getKey());
                            }
                        }
                    }

                }
                for (Extension extension : extensions) {
                    for (GameEventListener el : extension.getListeners()) {
                        for (KeyMap key : keys) {
                            if (key.getCode() == e.getKeyCode()) {
                                if (el instanceof GameKeyListener) {
                                    ((GameKeyListener) el).keyPressed(e, key.getKey());
                                }
                            }
                        }

                    }
                }
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
                for (GameEventListener el : listeners) {
                    for (KeyMap key : keys) {
                        if (key.getCode() == e.getKeyCode()) {
                            if (el instanceof GameKeyListener) {
                                ((GameKeyListener) el).keyReleased(e, key.getKey());
                            }
                        }
                    }

                }
                for (Extension extension : extensions) {
                    for (GameEventListener el : extension.getListeners()) {
                        for (KeyMap key : keys) {
                            if (key.getCode() == e.getKeyCode()) {
                                if (el instanceof GameKeyListener) {
                                    ((GameKeyListener) el).keyReleased(e, key.getKey());
                                }
                            }
                        }

                    }
                }
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

        if (settings.isFullScreen()) {
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
        mouseVelocity.setDirection(mousePoint.x - prevMousePoint.x, mousePoint.y - prevMousePoint.y);
        prevMousePoint.setLocation(mousePoint);
        for (Node n : entityList) {
            if (n.getControl() != null) {
                n.getControl().perform(n);
            }
        }
        if (scene != null) {
            for (Node n : scene.getGUI()) {
                if (n.getControl() != null) {
                    n.getControl().perform(n);
                }
                if ("entity".equals(n.getType())) {
                    ((Entity) n).update();
                }
            }
        }
        for (Extension extension : extensionRemoval) {
            extensions.remove(extension);
            extension.detach();
        }
        extensionRemoval.clear();
        for (Extension ext : extensionAdd) {
            extensions.add(ext);
            ext.setEngine(this);
            ext.init();
        }
        extensionAdd.clear();
        extensions.stream().forEach((extension) -> {
            if (extension.isEnabled()) {
                extension.frame();
            }
        });
        TimeQueue.masterCheck();
        frame();
        //<editor-fold defaultstate="collapsed" desc="Physics">
        entityList.clear();
        triggerList.clear();
        spawnList.clear();
        for (Node n : scene.getItems()) {
            switch (n.getType()) {
                case "entity": {
                    entityList.add((Entity) n);
                    break;
                }
                case "trigger": {
                    triggerList.add((Trigger) n);
                    break;
                }
                case "spawn": {
                    spawnList.add((Spawn) n);
                    break;
                }
            }
        }
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
                if ((!e.getID().equals(e2.getID())) & (e.getCollisionLayer() == e2.getCollisionLayer()) & e.isSolid() & e2.isSolid()) {
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
                ((Entity) n).update();
            }
        }
        gpu.draw();
        if (media != null) {
            stage.setTitle(gameTitle + " | FPS: " + Integer.toString(animtimer.getFPS()) + " | MMEQ: "
                    + media.getMediaQueue());
        } else {
            stage.setTitle(gameTitle + " | FPS: " + Integer.toString(animtimer.getFPS()));
        }
        if (tractionControl & (media != null)) {
            if (media.getMediaQueue() > TCThreshold) {
                fpsAdj--;
                animtimer.setInterval(1000 / fpsAdj);
            } else if (fpsAdj != fps) {
                fpsAdj++;
                animtimer.setInterval(1000 / fpsAdj);
            }
        }
        scene.updateLists();
    }

    public int getTCThreshold() {
        return TCThreshold;
    }

    public void setTCThreshold(int TCThreshold) {
        this.TCThreshold = TCThreshold;
    }

    //<editor-fold defaultstate="collapsed" desc="Engine Methods">
    public void clearKeys(){
        keys.clear();
    }
    
    public Point getMousePoint() {
        return mousePoint;
    }

    public void beginMediaSystem(int sampleRate, int bitDepth, int overhead, int latency) {
        media = new MediaPipeline();
        DingoSoundDriver DSD = new DingoSoundDriver();
        preComputedRate = (sampleRate / fps) * (bitDepth / 8);
        DSD.init(sampleRate, bitDepth, preComputedRate, overhead);
        System.out.println("MME Set to a buffer size of " + sampleRate / fps + " samples per frame");
        System.out.println("Sample Rate: " + sampleRate);
        System.out.println("Bit Depth: " + bitDepth);
        media.setAudio(DSD);
        media.setLatency(latency);
        media.start();
    }

    public int getComputedMediaBufferSize() {
        return preComputedRate;
    }

    public boolean isTractionControl() {
        return tractionControl;
    }

    public void setTractionControl(boolean tractionControl) {
        this.tractionControl = tractionControl;
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
        fpsAdj = fps;
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
     * Adds an extension class to the Engine
     *
     * @param ext The extension class to add
     */
    public void addExtension(Extension ext) {
        extensionAdd.add(ext);
    }

    /**
     * Removes an extension class from the Engine
     *
     * @param ext The extension class to remove
     */
    public void removeExtension(Extension ext) {
        extensionRemoval.add(ext);
    }

    public void clearExtensions() {
        extensionRemoval.addAll(extensions);
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

    public void addListener(GameEventListener e) {
        listeners.add(e);
    }

    public void removeListener(GameEventListener e) {
        listeners.remove(e);
    }

    public void clearListeners() {
        listeners.clear();
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
        scene = s;
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
    public void onSpawning(Spawn spawn, Entity e) {
    }

    /**
     * called every time the physics engine finishes processing.
     */
    public void postPhysicsTick() {
    }

    /**
     * called whenever a collision is detected.
     *
     * @param e1
     * @param e2
     */
    public void onCollision(Entity e1, Entity e2) {
    }

    /**
     * called in your main class. put all of your startup code in here
     */
    public abstract void init();

    /**
     * Called when the internal KeyMapper detects a key press
     *
     * @param key The key that was pressed
     */
    public void KeyPressed(String key) {
    }

    /**
     * Called when the internal KeyMapper detects a key release
     *
     * @param key The key that was released
     */
    public void KeyReleased(String key) {
    }

    public void shutdown() {
        if (media != null) {
            media.halt();
        }
        System.exit(0);
    }

    public String getGameTitle() {
        return gameTitle;
    }

    public void setGameTitle(String gameTitle) {
        this.gameTitle = gameTitle;
    }

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
