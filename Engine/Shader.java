/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Engine;

import java.awt.image.BufferedImage;
import java.io.File;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

/**
 *
 * @author James
 */
public class Shader {
    private String shader;
    
    public void draw(BufferedImage input, GPU gpu){
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("JavaScript");
    }
    
    public void loadShader(File f){
        
    }
}
