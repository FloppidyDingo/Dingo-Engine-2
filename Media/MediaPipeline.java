/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Media;

import Media.AdvancedMedia.Audio.AudioDriver;
import Media.AdvancedMedia.Audio.Sound;
import Media.AdvancedMedia.Video.VideoNode;

/**
 *
 * @author James
 */
public class MediaPipeline extends Thread{
    private AudioDriver audio;
    private VideoNode video;
    private volatile boolean running;
    private volatile int req;
    
    @Override
    public void run(){
        System.out.println("MME Started");
        running = true;
        while (running) {
            if (req > 0) {
                if (audio != null) {
                    audio.update();
                }
                if (video != null) {
                    video.update();
                }
                req --;
            }
        }
        System.out.println("MME Stopped");
    }
    
    public synchronized void process(){
        req ++;
    }

    public synchronized int getMediaQueue() {
        return req;
    }

    public AudioDriver getAudio() {
        return audio;
    }

    public void setAudio(AudioDriver audio) {
        this.audio = audio;
    }

    public VideoNode getVideo() {
        return video;
    }

    public void setVideo(VideoNode video) {
        this.video = video;
    }
    
    public void halt(){
        if(audio != null){
            audio.stop();
        }
        if(video != null){
            video.stop();
        }
        running = false;
    }
    
    public void addSound(Codec sound){
        audio.addCodec(sound);
    }

    public void removeSound(Sound s) {
        audio.removeCodec(s);
    }
}
