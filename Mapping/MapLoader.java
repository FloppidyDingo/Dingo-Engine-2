/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Mapping;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import Engine.Engine;
import Engine.InvalidVersionException;
import Engine.Utils;
import motion.Animation;
import objects.Entity;
import objects.Skin;
import objects.Spawn;
import objects.Trigger;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 *
 * @author James
 */
public class MapLoader {

    /**
     *
     * Creates a Map object using the specified map data file.
     *
     * @param url The filename of the map
     * @param referenceURL Specifies the map folder
     * @param imageURL Specifies the images folder
     * @return The generated Map object
     * @throws Engine.InvalidVersionException
     */
    public Map generateMap(String url, String referenceURL, String imageURL) throws InvalidVersionException {
        Map map = new Map();
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();
            SAXHandler handler = new SAXHandler();
            saxParser.parse(url, handler);
            if(handler.getVersion() > Engine.getMapVersion()){
                throw new InvalidVersionException("Map designed for newer version of the Dingo Engine.");
            }
            map = handler.getMap();
        } catch (ParserConfigurationException | SAXException | IOException ex) {
            Logger.getLogger(MapLoader.class.getName()).log(Level.SEVERE, null, ex);
        }
        map.compileScene();
        return map;
    }

    public class SAXHandler extends DefaultHandler {

        private Map map;
        private String element;
        private String imageURL;
        private Skin skinField;
        private Animation ani;
        private int version;
        private Trigger tb;
        private Entity ent;
        
        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
            switch (qName) {
                case "map":{
                    version = Integer.parseInt(attributes.getValue("version"));
                    break;
                }
                case "entity": {
                    int x;
                    int y;
                    x = Integer.parseInt(attributes.getValue("xpos"));
                    y = Integer.parseInt(attributes.getValue("ypos"));
                    String skin = attributes.getValue("skin");
                    Skin sk = new Skin();
                    for (Skin s : map.getSkinList()) {
                        if (s.getID().equals(skin)) {
                            sk = s;
                        }
                    }
                    ent = new Entity(sk, x, y, attributes.getValue("id"));
                    ent.setUserData(Integer.parseInt(attributes.getValue("ud")));
                    ent.setMass(Integer.parseInt(attributes.getValue("mass")));
                    ent.setActive(Boolean.parseBoolean(attributes.getValue("active")));
                    ent.setSolid(Boolean.parseBoolean(attributes.getValue("solid")));
                    ent.useSkin(0);
                    break;
                }
                case "light": {
                    
                    break;
                }
                case "trigger": {
                    int x;
                    int y;
                    int w;
                    int h;
                    x = Integer.parseInt(attributes.getValue("xpos"));
                    y = Integer.parseInt(attributes.getValue("ypos"));
                    w = Integer.parseInt(attributes.getValue("width"));
                    h = Integer.parseInt(attributes.getValue("height"));
                    tb = new Trigger(x, y, w, h);
                    element = "trigger";
                    tb.setID(attributes.getValue("ID"));
                    tb.setEnabled(Boolean.parseBoolean(attributes.getValue("enabled")));
                    tb.setOneshot(Boolean.parseBoolean(attributes.getValue("one-shot")));
                    tb.setUD(Integer.parseInt(attributes.getValue("ud")));
                    break;
                }
                case "spawn": {
                    int x;
                    int y;
                    int time;
                    x = Integer.parseInt(attributes.getValue("xpos"));
                    y = Integer.parseInt(attributes.getValue("ypos"));
                    time = Integer.parseInt(attributes.getValue("time"));
                    Spawn sp = new Spawn();
                    sp.setUD(Integer.parseInt(attributes.getValue("ud")));
                    sp.setX(x);
                    sp.setY(y);
                    sp.setTime(time);
                    sp.setID(attributes.getValue("id"));
                    sp.setVisible(false);
                    map.getSpawnList().add(sp);
                    break;
                }
                case "skin": {
                    try {
                        skinField = new Skin();
                        skinField.setID(attributes.getValue("id"));
                        skinField.setBaseImage(Utils.generateImage(imageURL + attributes.getValue("url")));
                        String def = attributes.getValue("definition");
                        if(def != null){
                            skinField.bufferSkinDef(imageURL + attributes.getValue("definition"));
                        }
                        element = "skin";
                        break;
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
                case "animation": {
                    ani = new Animation();
                    ani.setID(attributes.getValue("id"));
                    element = "animation";
                    ani.setFrameSkipping(Integer.parseInt(attributes.getValue("skip")));
                    ani.setRunning(false);
                    ent.addAni(ani);
                    break;
                }
                case "viewport":{
                    if ("skin".equals(element)) {
                        int x1 = Integer.parseInt(attributes.getValue("x1"));
                        int y1 = Integer.parseInt(attributes.getValue("y1"));
                        int x2 = Integer.parseInt(attributes.getValue("x2"));
                        int y2 = Integer.parseInt(attributes.getValue("y2"));
                        skinField.addFrame(x1, y1, x2, y2);
                    }
                    break;
                }
            }
        }

        @Override
        public void endElement(String uri, String localName, String qName) throws SAXException {
            switch(qName){
                case "trigger":{
                    map.getTriggerList().add(tb);
                    tb = null;
                    break;
                }
                case "skin":{
                    map.getSkinList().add(skinField);
                    skinField = null;
                    break;
                }
                case "animation":{
                    map.getAnimationList().add(ani);
                    ani = null;
                    break;
                }
                case "entity":{
                    map.getEntityList().add(ent);
                    ent = null;
                    break;
                }
            }
        }

        @Override
        public void characters(char ch[], int start, int length) throws SAXException {
            String input = String.copyValueOf(ch);
            List<String> items = Arrays.asList(input.split("\\s*,\\s*"));
            switch(element){
                case "trigger":{
                    for (String item : items) {
                        tb.getTargets().add(item); 
                    }
                    element = "";
                    break;
                }
                case "animation":{
                    for (String item : items) {
                        ani.addFrame(Integer.parseInt(item));
                    }
                    element = "";
                    break;
                }
            }
            
        }

        public Map getMap() {
            return map;
        }

        public void setImageURL(String imageURL) {
            this.imageURL = imageURL;
        }

        public int getVersion() {
            return version;
        }
        
    }
}
