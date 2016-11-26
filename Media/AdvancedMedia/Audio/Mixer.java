/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Media.AdvancedMedia.Audio;

import Media.Codec;
import Media.Connector;

/**
 *
 * @author James
 */
public class Mixer extends Codec{
    private final int block;
    private int div;
    
    public Mixer(int blockSize){
        block = blockSize;
    }

    @Override
    public void process() {
        div = 0;
        int data[] = new int[block];
        for (Connector con : in) {
            if (con.isUpdated()) {
                div++;
                for (int i = 0; i < con.getData().length; i++) {
                    data[i] = con.getData()[i] + data[i];
                }
            }
        }
        if (div != 0) {
            for (int i = 0; i < data.length; i++) {
                data[i] = data[i] / div;
            }
        }
        out.setData(data);
    }
    
}
