import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;
import java.util.*;
import java.awt.event.*;
import javax.swing.event.*;
import java.io.*;
/**
 * Game.java - implementation of a solitaire card game.
 * @Thatcher Eills
 */
public class Game extends JPanel
{
    //----------------------- class variables ---------------------------
    private static int   seed = 0;
    static         Game  theGame;
    static         int   numRows = 4;
    //----------------------- instance variables ------------------------
    private CardStack                   _drawPile = null; 
    private CardStack                   _discards = null;
    private ArrayList<Card>             _baseDeck = null;
    private int                         _parentWidth;
    private Pyramid<Card>               _pyramid;
    private Random                      _rng;
    private ArrayList<Card>             _pyramidCards;
    private boolean                     fin;
    private String                      endCase;
    //----- positioning variables
    private   int discardX  = 60,  discardY  = 140;
    private   int drawPileX = 60,  drawPileY = 40;
    private   int pyramidX  = 400,   pyramidY  = 40;
    //---------------------- constructor -----------------------------
    /**
     * Game is where most of the game-based code is found.
     * 
     * @param pWidth int     width of the panel (apprx)
     */
    public Game( int pWidth )
    {
        fin = true;
        this.setLayout( null );
        theGame = this;
        _pyramid = new Pyramid<Card>( numRows, 0, 0, 0, 0 );
        _parentWidth = pWidth;
        _baseDeck = new ArrayList<Card>();
        _pyramidCards = new ArrayList<Card>();
        createDeck();
        _discards = new CardStack( this, discardX, discardY );
        _discards.setYOffset( 2 );
        _drawPile = new CardStack( this, drawPileX, drawPileY );
        _drawPile.setXOffset( 0 );
        _rng = new Random( seed );
        makeNewDeck();
    }
    //------------------------- drawCard() ----------------------------
    /**
     * Draw a card from the display pile.
     * 
     * @return String
     */
    public String drawCard()
    {
        String msg = null;
       
        //game over final
        if ( _drawPile.size() != 0 )
        {
            Card nextCard = _drawPile.pop();
            _discards.push( nextCard );
        }
        else
        {
            msg = "There are no more cards to draw." ;
        }
        //Game over first run code
        //if( _drawPile.size() == 0 )
        //{
        //   gameOver( "You Lost.. Game Over" ); 
        //}
        update();
        return msg;
    }
    //------------------------ makeNewDeck() --------------------------
    /**
     * Creates new deck.
     */
    public void makeNewDeck()
    {
        Collections.shuffle( _baseDeck, _rng );
        replay();
    }
    //---------------------- replay( ) -----------------------------
    /**
     * Replay the game.
     */
    public void replay()
    {
        _pyramid = new Pyramid<Card>( numRows, 0, 0, 0, 0 );
        _discards.clear();
        deckToDrawPile( _baseDeck );          
        dealCards( _drawPile );
        update();
    }
    //---------------------- draw( ) -----------------------------
    /**
     * Draw from draw pile. Simulate click on draw pile.
     */
    public void draw()
    {
        play( _drawPile.top() );
    }
    //---------------------- undoPlay( ) -----------------------------
    /**
     * Undo the previous play.
     */
    public void undoPlay()
    {
        if ( _discards.top().getNode() == null )
        {
            Card prev = _discards.pop();
            _drawPile.push( prev );
        }
        else
        {
            Card prev =  _discards.pop();
            PyramidNode<Card> after = prev.getNode();
            _pyramid.getDataStructure().get( after.getLevelLocation() ).get( 
                                    after.getPosLocation() ).setUncov( false );
            prev.setLocation( after.getX(), after.getY() );
        }
        drawPyramid();
        update();
    }
    //---------------------- autoPlay( ) ----------------------------
    /**
     * Go into auto play mode.
     */
    public void autoPlay()
    {
        if( _discards.size() == 0 )
        {
            drawCard(); 
            return;
        }
        else
        {
            for( int row = _pyramid.size() - 1; row >= 0; row-- )
            {
                for( int col = 0; col < row + 1; col++ )
                {
                    if( _pyramid.getNode( row, col ).inGame() && play(
                                  _pyramid.getNode( row, col ).getData() ) )
                    {
                        return; 
                    }

                    if( _drawPile.size() == 0 )
                    {
                        fin = false;
                        PyramidSolitaire.log( "Draw pile", _drawPile );
                        PyramidSolitaire.log( "Play pile", _discards );
                        System.out.println( "You Lose" );
                        endCase = "You Lose! You still had: " + (52 - 
                                    (_drawPile.size()+ _discards.size())) +
                                             " cards left in your pyramid." ;
                        return;
                    }
                    if( _pyramid.getRoot().coverState() )
                    {
                        PyramidSolitaire.log( "Draw pile", _drawPile );
                        PyramidSolitaire.log( "Play pile", _discards );
                        System.out.println( "You WIN!" );
                        fin = false;
                        endCase = "You Win! You still had: " + 
                            _drawPile.size() + " cards left in your draw pile.";
                        //fin = false;
                        return;
                    }
                }
            }
            drawCard();
            update();
        }
    }
    //---------------------- autoPlayAll( ) ---------------------------
    /**
     * Make autoPlays until end of game.
     */
    public void autoPlayAll()
    {
        fin = true;
        while( fin )
        {
            autoPlay();
        }
        gameOver( endCase );
        update();        
    }
    //------------------------ play( Card ) --------------------------
    /**
     * Play a card.
     * 
     * @param picked Card     a card that is to be played.
     * @return boolean        true means a valid play occurred.
     */
    public boolean play( Card picked )
    {
        boolean playedFromPyramid = false;
        
        if ( picked == _drawPile.top() )
        {
            drawCard();
        }
        else if ( playPyramidCard( picked ) )
        {
            playedFromPyramid = true;
        }
        return playedFromPyramid;       
    }
    //------------------------ playPyramidCard ------------------------
    /** 
     * Play a card from the pyramid.
     *
     * @param picked Card
     * @return boolean 
     */
    private boolean playPyramidCard( Card picked )
    {
        boolean success = false;
        PyramidNode<Card> cardNode = picked.getNode();
        if( _pyramid.checking( cardNode ) )
        {
            Card.Rank pickedRank = picked.getRank();
            Card top = _discards.top();
            int diff = Math.abs( pickedRank.ordinal() 
                                    - top.getRank().ordinal() );
            if ( diff == 1 || diff == 12 )
            {
                cardNode.setInGame( false );
                _discards.push( picked );                
                update();
                success = true;
                _pyramid.getDataStructure().get( 
                      cardNode.getLevelLocation( ) ).get( 
                      cardNode.getPosLocation() ).setUncov( true );
            }
        }
        return success;
    }
    //----------------------------- update() ----------------------
    /**
     * Update the display components as needed.
     */
    public void update()
    {   
        // show all cards on the _discards stack 
        _discards.showCards( -1 );
        // show no cards on the draw pile
        _drawPile.showCards( 0 );
        this.repaint();    
    }
    //-------------------- dealCards() ----------------------------
    /**
     * Deal the cards from the drawPile to the pyramid. 
     *    This version just stores all the dealt cards in an ArrayList.
     * @param deck CardStack    deck to deal from
     */
    public void dealCards( CardStack deck )
    {
        int xGap = 2;
        int yDelta = 30;
        for ( int level = 0; level < numRows; level++ )
        {
            int span = Card.width * ( level + 1 ) + xGap * level;
            
            int xPos = pyramidX - span / 2;
            int yPos = pyramidY + level * yDelta;
            for ( int n = 0; n < level + 1; n++ )
            {
                PyramidNode<Card> node = new PyramidNode<Card>( xPos, yPos, 
                                    Card.width, Card.height );
                Card card = deck.pop();
                _pyramidCards.add( card );
                card.setNode( node );
                node.setData( card );
                card.setFaceUp( true );
                card.setLocation( xPos, yPos );
                this.setComponentZOrder( card, 0 );
                xPos += Card.width + xGap;
                _pyramid.add( node, level, n );
            }
        }
        drawPyramid();
    }

    //--------------------drawPyramid--------------
    /**
     * this makes the pyramid.
     * 
     */
    public void drawPyramid()
    {
        for( int lev = 0; lev < numRows; lev++ )
        {
            for( int n = 0; n < lev + 1; n++ )
            {
                PyramidNode<Card> node =  _pyramid.getNode( lev, n );
                Card card = node.getData();
                if( card != null && node.isFilled() )
                {
                    card.setLocation( node.getX(), node.getY() );
                    card.setFaceUp( true );
                    this.setComponentZOrder( card, 0 );
                }
            }
        }
    }
    //------------------------ newSeed( ) -----------------------------
    /**
     * Set new random number generator seed.
     * 
     * @param newSeed int
     */
    public void newSeed( int newSeed )
    {
        _rng = new Random( newSeed );
        seed = newSeed;
    }
    //------------------------ setRows( ) -----------------------------
    /**
     * Change the number of rows in the game.
     *     Won't have effect until next game is played.
     * @param rows int
     */
    public void setRows( int rows )
    {
        numRows = rows;
    }
    //------------------- theGame() --------------------------------
    /**
     *    
     * @return Game
     */
    public static Game theGame()
    {
        return theGame;
    }
    //------------------ showDeck() ----------------------------------
    /**
     * Turns all cards in deck face up and spread them out.
     */
    public void showDeck()
    {
        _drawPile.setXOffset( 11 );
        _drawPile.showCards( -1 );
        this.repaint();
    }
    //------------------ hideDeck() ----------------------------------
    /**
     * Turns all cards in deck face down and stack them up again.
     */
    public void hideDeck()
    {
        _drawPile.setXOffset( 0 );
        _drawPile.showCards( 0 );
        this.repaint();
    }
    //------------------------ createDeck() ---------------------------
    /**
     * Creates a deck of cards in the _base variable.
     */ 
    private void createDeck()
    {
        int  cardIndex = 0;
        
        for ( Card.Suit suit: Card.Suit.values() )
        {
            for ( Card.Rank rank: Card.Rank.values() )
            {
                Card card = new Card( rank, suit );
                _baseDeck.add( 0, card );
                this.add( card );
            }
        }
    }
    //------------------------ deckToDrawPile( Card[] ) ---------------
    /**
     * Copys an array of cards into CardStack representing draw pile.
     * 
     * @param deck ArrayList<Card>
     */
    private void deckToDrawPile( ArrayList<Card> deck )
    {
        _drawPile.clear();
        for ( int c = 0; c < 52; c++ )
            _drawPile.push( deck.get( c ) );
    }
    //-----------------------gameOver( String )-----------------------
    /**
     * this is the game over method.
     * @param msg String is the end game message.  
     * 
     */
    public void gameOver( String msg ) 
    {
        msg = msg + "\nWant a new deck?";
        int choice = JOptionPane.showConfirmDialog( null, msg );
        if ( choice == 0 )
            makeNewDeck();
        else if ( choice == 1 )
            System.exit( 0 );
    }
    //------------------------ setBaseDeck() --------------------------
    /**
     * Creates new base deck.
     * @param newDeck ArrayList<Card>    the new deck.
     */
    public void setBaseDeck( ArrayList<Card> newDeck )
    {    
        if ( _baseDeck != null )
        {
            for ( Card card: _baseDeck )
                this.remove( card );   // remove from JPanel
        }
        _baseDeck.clear();
        
        for ( Card card: newDeck )
        {
            _baseDeck.add( card );
            this.add( card );
        }
        PyramidSolitaire.log( "Starting deck", _baseDeck );        
        replay();
    }
    //---------------------- writeDeck( ) -----------------------------
    /**
     * Writes the current _baseDeck to a file.
     */
    public void writeDeck()
    {
        String msg = "Enter file name of desired deck file";
        String outName = JOptionPane.showInputDialog( null, msg );
        if ( outName != null && outName.length() > 0 )
            writeDeck( outName );
    }
    //---------------------- writeDeck( String ) ----------------------
    /**
     * Writes the current _baseDeck to the named file.
     * @param fileName String     name of output file
     */
    public void writeDeck( String fileName )
    {
        PrintStream out;
        try
        {
            out = new PrintStream( new File( fileName ) );
        }
        catch ( FileNotFoundException fnf )
        {
            System.err.println( "*** Error: unable to open " + fileName );
            return;
        }
        out.println( cardsToString( _baseDeck ) );
        out.close();
    }
    //---------------------- readDeck( ) -----------------------------
    /**
     * Read a file to replace _baseDeck.
     */
    public void readDeck()
    {
        String inName = Utilities.getFileName( "Choose a card file" );
        if ( inName != null && inName.length() > 0 )
        {
            ArrayList<Card> newDeck = readDeck( inName );
            if ( newDeck.size() != 52 )
                System.err.println( "*** ERROR. Game.readDeck: file has "
                                       + newDeck.size() + " cards!" );
            else                          
                setBaseDeck( newDeck );
        }
    }
    //------------------------ readCardFile -------------------------
    /**
     * Read card representations from a file. Input file should consist of
     *    card representations of the form rs separated by spaces, where
     *        r is one of  23456789XJQKA
     *        s is one of  CDHS
     *    Case and Lines are not relevant.
     * @param filename String  
     * @return ArrayList<Card>
     */
    public ArrayList<Card> readDeck( String filename )
    { 
        Card.Rank[] rankValues = Card.Rank.values();
        Card.Suit[] suitValues = Card.Suit.values();
        //-------- This assumes Ace low! ----------------------
        String rankCodes = "A23456789XJQK";
        String suitCodes = "CDHS";
        Scanner scan = null;
        
        ArrayList<Card> cards = new ArrayList<Card>();
        try
        {
            scan = new Scanner( new File( filename ) );
        } 
        catch ( FileNotFoundException fnf )
        {
            System.out.println( "Can't find: " + filename );
            return null;
        }
        int count = 0;
        while ( scan.hasNextLine() )
        {
            String line = scan.nextLine();
            line = line.trim();
            if ( line.length() > 0 && line.charAt( 0 ) != '#' ) 
            {
                String[] words = line.split( " " );
                for ( int w = 0; w < words.length; w++ )
                {
                    String word = words[ w ];
                    char rankch = words[ w ].charAt( 0 );
                    char suitch = words[ w ].charAt( 1 );
                    int rankIndex = rankCodes.indexOf( rankch );
                    int suitIndex = suitCodes.indexOf( suitch );
                    if ( rankIndex < 0 || suitIndex < 0 )
                    {
                        System.err.println( "Scan error?: " + rankIndex +
                                   " " +   suitIndex + " " + words[ w ] );
                    }
                    else
                    {
                        Card.Rank rank = rankValues[ rankIndex ];
                        Card.Suit suit = suitValues[ suitIndex ];
                        cards.add( new Card( rank, suit ) );
                        count++;
                    }
                }
            }
        }
        return cards;
    }
    //------------------- cardsToString( Iterable<Card> ) -------------
    /**
     * Creates a compact string representation of a collection of Cards.
     * 
     * @param cardSet Iterable<Card>   the card collection to process
     * @return String
     */
    public static String cardsToString( Iterable<Card> cardSet )
    {
        //-------- This assumes Ace low! ----------------------
        String rankChar = "A23456789XJQK";
        String suitChar = "CDHS";
        StringBuffer out = new StringBuffer();
        int lineLen = 0;
        int maxLine = 38;
        for ( Card card: cardSet )
        {
            char r = rankChar.charAt( card.getRank().ordinal() );
            char s = suitChar.charAt( card.getSuit().ordinal() );
            out.append( "" + r + s + " " );
            lineLen += 3;
            if ( lineLen > maxLine )
            {
                out.append( "\n" );
                lineLen = 0;
            }
        }
        if ( lineLen > 0 )
            out.append( "\n" );
        return out.toString();
    }

    //--------------------------- main ---------------------------------
    /**
     * A convenient tool for invoking main class.
     * @param args String[]    Command line arguments
     */
    public static void main( String[] args )
    {
        // Invoke main class's main
        PyramidSolitaire.main( args );
    }
} 
