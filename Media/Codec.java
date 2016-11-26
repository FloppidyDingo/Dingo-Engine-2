/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Media;

import java.util.ArrayList;
import java.util.List;


/**
 *
 * @author James
 */
public abstract class Codec {
    
    public Connector out;
    public List<Connector> in;
    protected int order;

    public Codec() {
        this.out = new Connector();
        this.in = new ArrayList<>();
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public Connector getOut() {
        return out;
    }
    
    public void link(Codec source){
        in.add(source.out);
        source.setOrder(order - 1);
    }
    
    public void unlink(Codec source){
        in.remove(source.getOut());
    }
    
    public abstract void process();
    
}
