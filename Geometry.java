//-------------------- Geometry interface -----------------------------
import java.awt.Point;
/**
 * Geometry interface defines opjects that have a location, width and
 *     height.
 * @Thatcher Eills
 */
public interface Geometry 
{
    //------------------ getX() ---------------
    /**
     * Return the x value of the object's location.
     * @return int
     */
    public int getX();
    //------------------ getY() ---------------
    /**
     * Return the y value of the object's location.
     * @return int
     */
    public int getY();
    //------------------ getLocation() ---------------
    /**
     * Return the object's location as a Point.
     * @return Point
     */    
    public Point getLocation();
    //------------------ getHeight() ---------------
    /**
     * Return the height of the object's bounding box.
     * @return int
     */    
    public int getHeight();
    //------------------ getWidth() ---------------
    /**
     * Return the height of the object's bounding box.
     * @return int
     */    
    public int getWidth();
} 
