/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Media.AdvancedMedia.Audio;

import Media.Codec;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 *
 * @author James
 */
public class Sound2 extends Codec{
    private int cursor;
    private int bufferSize;
    private List<Integer> instances;
    private boolean loop;
    private boolean overflow;
    private boolean playing;
    private boolean instanced;
    private boolean streamed;
    private int[] buffer;
    private float volume;
    private float balance;

    public Sound2() {
        volume = 1.0f;
        balance = 1.0f;
    }

    @Override
    public int[] process() {
        int c1;
        if(instanced){
            for (Integer c2 : instances) {
                
            }
        }else{
            
        }
        return data;
    }
    
    public void play(){
        if(instanced){
            instances.add(0);
        }
    }
    
    public void openSound(String file, int blockSize, boolean mono, boolean stream, boolean instanced) throws IOException{
        if(instanced & stream){
            throw new IllegalArgumentException("Cannot have instanced streams");
        }
        this.instanced = instanced;
        this.streamed = stream;
        if(stream){
            
        }else{
            this.bufferSize = blockSize * 2;
            Path path = Paths.get(file);
            byte[] temp = Files.readAllBytes(path);
            buffer = new int[(int) Files.size(path)];
            for (int i = 0; i < temp.length; i++) {
                buffer[i] = temp[i];
            }
        }
    }

    public boolean isLoop() {
        return loop;
    }

    public void setLoop(boolean loop) {
        this.loop = loop;
    }

    public boolean isPlaying() {
        return playing;
    }

    public void setPlaying(boolean playing) {
        this.playing = playing;
    }

    public float getVolume() {
        return volume;
    }

    public void setVolume(float volume) {
        this.volume = volume;
    }

    public float getBalance() {
        return balance;
    }

    public void setBalance(float balance) {
        this.balance = balance;
    }

}
