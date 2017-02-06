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

/**
 *
 * @author James
 */
public class Sound extends Codec{
    private int block;
    private int cursor;
    private boolean loop;
    private boolean overflow;
    private boolean playing;
    private int[] buffer;
    private float volume;

    @Override
    public int[] process() {
        data = new int[block];
        if (playing) {
            for (int i = 0; i < block; i++) {
                if (cursor > buffer.length - 1) {
                    cursor = 0;
                    if (!loop) {
                        overflow = true;
                    }
                }
                if (overflow) {
                    data[i] = 0;
                } else {
                    data[i] = buffer[cursor];
                }
                cursor ++;
            }
        }else{
            for (int i = 0; i < data.length; i++) {
                data[i] = 0;
            }
        }
        if(overflow){
            playing = false;
        }
        return data;
    }
    
    public void openFile(String file, int blockSize) throws IOException{
        block = blockSize * 2;
        Path path = Paths.get(file);
        byte[] temp = Files.readAllBytes(path);
        buffer = new int[(int)Files.size(path)];
        for (int i = 0; i < temp.length; i++) {
            buffer[i] = temp[i];
        }
    }
    
    public void reset(){
        overflow = false;
    }

    public int getBlock() {
        return block;
    }

    public void setBlock(int block) {
        this.block = block;
    }

    public int getCursor() {
        return cursor;
    }

    public void setCursor(int cursor) {
        this.cursor = cursor;
    }

    public boolean isLoop() {
        return loop;
    }

    public void setLoop(boolean loop) {
        this.loop = loop;
    }

    public boolean isOverflow() {
        return overflow;
    }

    public void setOverflow(boolean overflow) {
        this.overflow = overflow;
    }
    
    public void play(){
        playing = true;
    }
    
    public void pause(){
        playing = false;
    }
    
    public void stop(){
        playing = false;
        cursor = 0;
    }

    public boolean isPlaying() {
        return playing;
    }

    public float getVolume() {
        return volume;
    }

    public void setVolume(float volume) {
        this.volume = volume;
    }
    
}
