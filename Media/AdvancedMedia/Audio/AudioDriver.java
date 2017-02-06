/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Media.AdvancedMedia.Audio;

import Media.Codec;
import java.util.ArrayList;
import java.util.List;



/**
 *
 * @author James
 */
public abstract class AudioDriver {
    protected float volume;
    protected String name;
    protected String version;
    protected final List<Codec> inputs;

    public AudioDriver() {
        this.inputs = new ArrayList<>();
    }
    
    public abstract void update();
    
    public abstract void stop();
    
    public abstract void init(int SampleRate, int bitDepth, int bufferSize, int overhead);
    
    public float getVolume() {
        return volume;
    }

    public void setVolume(float volume) {
        this.volume = volume;
    }
    
    public void addCodec(Codec c){
        inputs.add(c);
    }
    
    public void removeCodec(Codec c){
        inputs.remove(c);
    }
    
    public void reset(){
        inputs.clear();
    }
}
