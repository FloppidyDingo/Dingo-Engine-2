/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import Engine.GPU;
import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import objects.Node;

/**
 *
 * @author James
 * @param <E>
 */
public class ListBox<E> extends Node{
    
    private final List<E> list;
    private BufferedImage image;
    private GraphicsFont labelFont;
    private int w;
    private int h;
    private int cellHeight;
    private int selectedLine;
    private int offsetX;
    private int offsetY;
    private int padding;
    private BufferedImage selected;
    private BufferedImage deselected;
    private int scale;
    private int scroll;

    public ListBox(int width, int height) {
        labelFont = new GraphicsFont();
        this.list = new ArrayList<>();
        scale = 1;
        cellHeight = 20;
        h = height;
        w = width;
        selected = GUIStore.getDefaultSelected();
        deselected = GUIStore.getDefaultDeselected();
        buildImage();
    }
    
    @Override
    public BufferedImage render(GPU gpu) {
        Graphics2D g = (Graphics2D)image.getGraphics();
        g.clearRect(0, 0, image.getWidth(), image.getHeight());
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.CLEAR, 1));
        g.fillRect(0, 0, image.getWidth(), image.getHeight());
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1));
        for (E e : list) {
            int line = list.indexOf(e);
            Label l = new Label();
            l.setFont(labelFont);
            l.setOffsetX(offsetX);
            l.setOffsetY(offsetY);
            l.setPadding(padding);
            l.setText(e.toString());
            if(selectedLine == line){
                g.drawImage(selected, 0, line * cellHeight, image.getWidth(), cellHeight, null);
            }else{
                g.drawImage(deselected, 0, line * cellHeight, image.getWidth(), cellHeight, null);
            }
            g.drawImage(l.render(gpu), 0, line * cellHeight, l.getWidth(), cellHeight, null);
        }
        BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        img.getGraphics().drawImage(image, 0, x * scroll * cellHeight, w, list.size() * cellHeight, null);
        g.dispose();
        return img;
    }

    @Override
    public int getWidth() {
        return w;
    }

    @Override
    public int getHeight() {
        return h;
    }

    public void setLabelFont(GraphicsFont labelFont) {
        this.labelFont = labelFont;
    }

    public GraphicsFont getLabelFont() {
        return labelFont;
    }
    
    public void setInternalScale(int scale){
        this.scale = scale;
        buildImage();
    }

    public int getScale() {
        return scale;
    }
    
    public void addElement(E e){
        list.add(e);
        h = list.size() * cellHeight;
        buildImage();
    }

    public void removeElement(E e){
        list.remove(e);
        h = list.size() * cellHeight;
        buildImage();
    }
    
    private void buildImage() {
        image = new BufferedImage(w * scale, (list.size() * cellHeight * scale) + 1, BufferedImage.TYPE_INT_ARGB);
    }

    public BufferedImage getSelected() {
        return selected;
    }

    public void setSelected(BufferedImage selected) {
        this.selected = selected;
    }

    public BufferedImage getDeselected() {
        return deselected;
    }

    public void setDeselected(BufferedImage deselected) {
        this.deselected = deselected;
    }

    public int getCellHeight() {
        return cellHeight;
    }

    public void setCellHeight(int cellHeight) {
        this.cellHeight = cellHeight;
    }

    public void setWidth(int w) {
        this.w = w;
    }

    public void setHeight(int h) {
        this.h = h;
    }

    public int getOffsetX() {
        return offsetX;
    }

    public void setOffsetX(int offsetX) {
        this.offsetX = offsetX;
    }

    public int getOffsetY() {
        return offsetY;
    }

    public void setOffsetY(int offsetY) {
        this.offsetY = offsetY;
    }

    public int getSelectedLine() {
        return selectedLine;
    }
     public E getItemAt(int index){
         return list.get(index);
     }

    public int getScroll() {
        return scroll;
    }

    public void setScroll(int scroll) {
        this.scroll = scroll;
    }

    public void setScale(int scale) {
        this.scale = scale;
        buildImage();
    }

    public int getPadding() {
        return padding;
    }

    public void setPadding(int padding) {
        this.padding = padding;
    }
    
}
