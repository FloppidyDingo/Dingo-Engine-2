/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package motion;

/**
 *
 * @author James
 */
public class Vector {
    private int x;
    private int y;
    
    /**
     *
     * @return The x value of this vector.
     */
    public int getX() {
        return x;
    }

    /**
     *Sets the x value of this vector.
     * @param x
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     *
     * @return The y value of this vector.
     */
    public int getY() {
        return y;
    }

    /**
     *Sets the y value of this vector.
     * @param y
     */
    public void setY(int y) {
        this.y = y;
    }
    
    /**
     *
     */
    public Vector(){
        x = 0;
        y = 0;
    }
    
    /**
     *
     * @param x
     * @param y
     */
    public Vector(int x, int y){
        this.x = x;
        this.y = y;
    }
    
    /**
     *Sets the x and y values of this vector.
     * @param x
     * @param y
     */
    public void setDirection(int x, int y){
        this.x = x;
        this.y = y;
    }
    
    /**
     *Adds the given x and y values to the ones of this vector.
     * @param x
     * @param y
     */
    public void applyForce(int x, int y){
        this.x += x;
        this.y += y;
    }
    
    /**
     *Adds the x and y values of the given vector to those of this one.
     * @param v
     */
    public void applyForce(Vector v){
        this.x += v.getX();
        this.y += v.getY();
    }
    
    /**
     *Multiplies the x and y values by the given scalar.
     * @param scalar
     */
    public void multlocal(int scalar){
        this.x = this.x * scalar;
        this.y = this.y * scalar;
    }
    
    /**
     *Returns a new Vector with x and y values the ones of this vector multiplied by the scalar.
     * @param scalar
     * @return
     */
    public Vector mult(int scalar){
        return new Vector(this.x * scalar, this.y * scalar);
    }
    
    /**
     *Returns a new Vector with x and y values the ones of this vector added to those of the given vector.
     * @param scalar
     * @return
     */
    public Vector add(int scalar){
        return new Vector(this.x + scalar, this.y + scalar);
    }
}
