import javax.swing.border.*;
import javax.swing.*;
import java.awt.*;
import java.util.*;

/**
 * Provided as starter code
 * CardStack -- a stack of cards built on Stack.
 * @Thatcher Eills
 */

public class CardStack extends Stack<Card> implements CardGroup
//public class CardStack extends java.util.Stack<Card> implements CardGroup
{
    //--------------- instance variables ------------------------ 
    private int    _xLoc;
    private int    _yLoc;
    private int    _xOffset = 0;
    private int    _yOffset = 0;
    private JLabel _base;
    private JPanel _parent;
    
    //--------------------- constructors -------------------------
    /**
     * Constructs a CardStack at 0,0.
     * @param parent JPanel
     */
    public CardStack( JPanel parent )
    {
        this( parent, 0, 0 );
    }
    /**
     * Construct a CardStack at x, y. 
     * 
     * @param parent JPanel
     * @param x int
     * @param y int
     */ 
    public CardStack( JPanel parent,  int x, int y )
    {
        super(); 
        
        _parent = parent;
        _xLoc = x;
        _yLoc = y;

        _base = new JLabel();
        _base.setSize( Card.width, Card.height );
        _base.setBorder( new LineBorder( Color.BLACK, 1 ) );
        _base.setText( "  Empty" );
        _base.setOpaque( true );
        //_base.setBackground( Color.LIGHT_GRAY );
        setLocation( _xLoc, _yLoc );
        parent.add( _base );   
    }
    
    //------------------ push ---------------------------------
    /**
     * Pushes a card onto the stack and return it.
     * 
     * @param c Card
     * @return Card
     */ 
    public Card push( Card c )
    {
        super.push( c );
        _parent.setComponentZOrder( c , 0 );
        return c;
    }

   //--------------------- setXOffset( int )--------------------------
    /**
     * Sets the offset in x for showing edges of cards in stack.
     * @param m int          offset
     */ 
    public void setXOffset( int m )
    {
        _xOffset = m;  
    }    
    
    //---------------------setYOffset( int )-----------------------
    /**
     * Sets offset in y for showing edges of cards in stack.
     * 
     * @param m int
     */ 
    public void setYOffset( int m )
    {
        _yOffset = m;  
    }
        
    //--------------------- showCards( int ) --------------------------------
    /**
     * Show the faces of the top number cards in the stack.
     * 
     * @param num int     number cards to show at top
     */ 
    public void showCards( int num )
    {
        int size = size();
        if (  size == 0 )
            return;
        int show = Math.min( num, size );  // number to show
        
        if ( num < 0 )
            show = size;

        int count = 0;
        
        // place from bottom of stack
        for ( int c = size( ) - 1; c >= 0; c-- )
        {
            Card card = get( c );  
            if ( c > show - 1 )
                card.setFaceUp( false );
            else
                card.setFaceUp( true );     
            card.setLocation( _xLoc + _xOffset * count,
                              _yLoc + _yOffset * count++ ); 
            _parent.setComponentZOrder( card, 0 );
        }
    }
    
    //------------------- get( int ) -------------------------
    /**
     * Returns the i-th entry for display purposes.
     * 
     * @param i int       item to return
     * @return Card
     */
    public Card get( int i )
    {
        if ( i < 0 || i >= size() ) 
            return null;
        else
            return super.get( size() - 1 - i );
    }
    //------------------- top( ) -------------------------
    /**
     * Returns top of stack.
     * 
     * @return Card
     */
    public Card top()
    {
        if ( size() == 0 )
            return null;
        else
            return get( 0 );
    }
    
    //------------------- setLocation( int, int ) -----------------
    /**
     * Sets a new Location for the stack.
     * 
     * @param x int
     * @param y int
     */
    public void setLocation( int x, int y )
    {
        _xLoc = x;
        _yLoc = y;
        if ( _base != null )
            _base.setLocation( x, y );
        for ( int i = 0; i < this.size(); i++ )
            this.get( i ).setLocation( x + _xOffset * i, y + i  * _yOffset );
    }
    
    //------------------- getXLocation() --------------------------
    /**
     * Returns the x location for the display of this card stack.
     * 
     * @return int
     */   
    public int getXLocation()
    {
        return _xLoc;
    }
    
    //------------------- getYLocation() --------------------------
    /**
     * Returns the y location for the display of this card stack.
     * 
     * @return int 
     */
    public int getYLocation()
    {
        return _yLoc;
    }
}
