import java.awt.Point;
/**  
 * @Thatcher Eills
 * @param <T> extends Geometry
 */
class PyramidNode<T extends Geometry >  implements Geometry
{
    //--------------------- instance variables --------------------
    private PyramidNode<T>           _left;
    private PyramidNode<T>           _right;
    private int                      _xLoc;
    private int                      _yLoc;
    private int                      _width;
    private int                      _height; 
    private T                        _data;
    private boolean                  _hasData;
    //------------------ additinal ----------------
    private int                      lev; 
    private int                      pos; 
    private boolean                  _covered = false;
    private boolean                  inGameState = true;
    //----------------- constructors -----------------------------
    /**
     * Construct a PyramidNode with position and size information.
     * @param x int
     * @param y int
     * @param w int
     * @param h int
     */
    public PyramidNode( int x, int y, int w, int h )
    {
        _left = null;
        _right = null;
        _data = null;
        _hasData = false;
        _xLoc = x;
        _yLoc = y;
        _width = w;
        _height = w;
    }
    //------------------ setData( T content ) -------------------------
    /**
     * Define the data content to be saved in this node.
     * 
     * @param content T
     */
    public void setData( T content )
    {
        _data = content;
        _hasData = true;
    }
    //------------------ getData() -------------------------
    /**
     * Get the data content saved in this node.
     * @return T
     */
    public T getData()
    {
        return _data;
    }
    //------------------ toString() ------------------------------
    /**
     * Return a string representation for the node.
     * @return String
     */
    public String toString()
    {
        return "{." + _left + "." + _right + ":" + _data + "}";
    }
    //================ Geometry interface ====================
    
    //------------------ getX() ---------------
    /**
     * Return the x value of the object's location.
     * @return int
     */
    public int getX()
    {
        return _xLoc;
    }
    //------------------ getY() ---------------
    /**
     * Return the y value of the object's location.
     * @return int
     */
    public int getY()
    {
        return _yLoc;
    }
    //------------------ getLocation() ---------------
    /**
     * Return the object's location as a Point.
     * @return Point
     */    
    public Point getLocation()
    {
        return new Point( _xLoc, _yLoc );
    }
    //------------------ getHeight() ---------------
    /**
     * Return the height of the object's bounding box.
     * @return int
     */    
    public int getHeight()
    {
        return _height;
    }
    //------------------ getWidth() ---------------
    /**
     * Return the height of the object's bounding box.
     * @return int
     */    
    public int getWidth()
    {
        return _width;
    }
    //----------------- setPosition of node -------------
    /**
     * this sets the position of the node. 
     * @param  llev int is the level.
     * @param  ppos  int is the level.
     */
    public void setLevPos( int llev, int ppos )
    {
        lev = llev;
        pos = ppos;

    }
    //------------------getLevelLocation---------
    /**
     * this returns the level.
     * @return lev int is the level.
     */
    public int getLevelLocation()
    {
        return lev;
    }
    //-----------------getPosLocation-------------
    /**.
     * this returns the position.
     * @return pos int is the position.
     */
    public int getPosLocation()
    {
        return pos; 
    }
    //-----------------setUncov-------------
    /**
     * this sets the _covered variable 
     * to what is passed in. 
     * @param b boolean is the value to set to. 
     */
    public void setUncov( boolean b )
    {
        _covered = b;
    }
    //------------coverState---------
    /**
     * this returns the flipped state.
     * @return _covered boolean is the flip.
     */
    public boolean coverState ()
    {
        return _covered;
    }
    //-------------inGame()---------
    /**
     * this checks to see if the node is still in the game Pyramid.
     */
    public boolean inGame( )
    {
        return inGameState;
    }
    //-----------setInGame----------
    /*
     * this sets the inGameState boolean.
     */
    public void setInGame( boolean b )
    {
        inGameState = b;
    }
     //-----------isFilled----------
    /*
     * this sets the inGameState boolean.
     */
    public boolean isFilled(  )
    {
        return _hasData;
    }
}
