/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Media;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author James
 */
public class MediaStream extends Codec{
    private BufferedReader br;
    private boolean open;
    
    /**
     * Opens the media stream and sets buffer size
     * @param f The file to read data from
     * @param blockSize The size of the buffer
     * @throws FileNotFoundException
     */
    public void openStream(File f, int blockSize) throws FileNotFoundException{
        br = new BufferedReader(new FileReader(f));
        data = new int[blockSize];
        open = true;
    }
    
    /**
     * Closes the stream
     * @throws IOException
     */
    public void close() throws IOException{
        br.close();
    }

    @Override
    public int[] process() {
        int i2;
        for (int i = 0; i < data.length; i++) {
            data[i] = 0;
        }
        if (open) {
            for (int i = 0; i < data.length; i++) {
                try {
                    i2 = br.read();
                    if (i2 == -1) {
                        br.close();
                        open = false;
                        break;
                    } else {
                        data[i] = i2;
                    }
                } catch (IOException ex) {
                    Logger.getLogger(MediaStream.class.getName()).log(Level.SEVERE, null, ex);
                    open = false;
                }
            }
        }
        return data;
    }
    
}
