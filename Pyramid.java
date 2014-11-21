import javax.swing.JPanel;
import java.awt.*;
import java.util.*;
/**
 * @Thatcher Eills
 * @param <T>    data field of type T
 */
public class Pyramid<T extends Geometry> 
{
    //-------------------- instance variables --------------------
    private int  _xCenter;    // x is CENTER of top card of pyramid
    private int  _yCenter;    // y center of top card
    private ArrayList<ArrayList<PyramidNode<T>>>             dataStructure;
    private PyramidNode                                      ridNode;
    private int                                              r;
    private int                                              position;
    private int                                              level;
    //--------------------- constructor --------------------------
    /**
     * Constructor.
     * @param nRows int        number rows in the pyramid
     * @param x int            x location of center of the pyramid
     * @param y int            y location of center of top card
     * @param nW int           width of nodes of pyramid
     * @param nH int           height of nodes of pyramid
     */
    public Pyramid( int nRows, int x, int y, int nW , int nH )
    {
        dataStructure = new ArrayList<ArrayList<PyramidNode<T>>>();
        ridNode = new PyramidNode<T>( x, y, nW, nH );
        r = nRows;
    }
    //----------------- clear( ) -------------------
    /**
     * Empty all nodes of content.
     */
    public void clear()
    {
        dataStructure = new ArrayList < ArrayList < PyramidNode < T > > >();
    }
    //------------------- getNode( int, int  ) -----------------------
    /**
     * Get the PyramidNode at level l, position p. 
     * @param l int     level of pyramid
     * @param p int     position in row ( 0 .. l-1 )
     * @throws IndexOutOfBoundsException
     * @return PyramidNode<T>
     */
    public PyramidNode<T> getNode( int l, int p )  // get l,n node
    { 
        return dataStructure.get( l ).get( p );
    }
    //------------------ add Node -------------
    /**
     * this adds the node.
     * @param nod PyramidNode<Card> is the node to add.
     * @param l int is the leve.
     * @param n int is the to position.
     */
    public void add( PyramidNode<T> nod, int l, int n )
    {
        if( n == 0 )
        {
            dataStructure.add( new ArrayList<PyramidNode<T>>() ); 
        }
        nod.setLevPos( l , n );
        dataStructure.get( l ) .add( n , nod );
    }
    //------------------- getRoot() -----------------------
    /**
     * Get the root PyramidNode. 
     * @return PyramidNode
     */
    public PyramidNode<T> getRoot()  // get root node
    { 
        return dataStructure.get( 0 ).get( 0 );
    }
    //-------------------------- isEmpty() ----------------------------
    /**
     * One way of making this test is to assume that the entire pyramid
     *    is "empty" if the top element does not contain valid data.
     * @return boolean    true if no valid data is in the pyramid.
     */
    public boolean isEmpty()
    {
        if( dataStructure == null )
            return true;
        else
            return false;
    }
    //--------------------------- getDataStructure -------------------
    /**
     * returns data structure.
     * @return dataStructure ArrayList<ArrayList<PyramidNode<Card>>>.
     */
    public ArrayList<ArrayList<PyramidNode<T>>> getDataStructure()
    {
        return dataStructure;
    }
    //--------------------------- checking ----------------------------
    /**
     * this does a check to see card has kids.
     * @param node PyramidNode<Card> is the node your checking.
     * @return boolean. 
     */
    public boolean checking( PyramidNode<T> node )
    {
        level = node.getLevelLocation();
        position = node.getPosLocation();
        if( level >= 0 && position >= 0 || level >=  0 && position >= 0 )
        {
            if(   level == r - 1 )
            {
                return true;
            }
            if( dataStructure.get( level + 1 ).get( position ).coverState() 
                   && dataStructure.get( level + 1 ).get( position + 
                                                         1 ).coverState() )
            {
                return true; 
            }
        }
        return false;
    }
    //-------------------------- size() -----------------------------
    /**
     * this returns the size of the data structure.
     * 
     */
    public int size()
    {
        return dataStructure.size();
    }
    //--------------------------- main --------------------------------
    /**
     * This main is a convenience call to the application.
     * 
     * @param args String[]            command line arguments.
     */
    public static void main( String[] args )
    {
        // Invoke main class's main
        PyramidSolitaire.main( args );
    }
} 
