import java.awt.geom.*;
import java.awt.*;
import java.awt.image.*;
import javax.swing.*;
import java.io.*;
import javax.imageio.*;
import java.awt.event.*;
import java.util.*;
import java.util.jar.*;
import java.util.zip.*;
/** 
 * Card Class: represents a playing card.
 * @Thatcher Eills
 * 03/10/14 -Change image access to jar file
 * 03/28/14 -added Geometry interface spec 
 */

public class Card extends JLabel 
                  implements Comparable<Card>, MouseListener, Geometry
{
    //-------------------------- class variables --------------------
    //--- package access 
    static int width = 71, height = 96;   // card size; package access
    static String  imageSource = "cards_gif.jar";  
    //static String  imageSource = "cards_gif/";
    
    //--- private access
    private static BufferedImage backImage = null;
    private static JarFile  jarFile = null;  // rep jar file if used
    
    //-------------------------- instance variables -------------------
    private Rank rank = null;
    private Suit suit = null;
    private BufferedImage suitImage;
    private BufferedImage faceImage;
    private CardStack sRef;
    private boolean _faceUp = false;
    private Point lastPoint;
    private JPanel _parent;
    private PyramidNode<Card> _pyramidNode;
    
    /**
     * Enum for representing the rank of a card with symbolic names.
     * In this case Ace is low.
     */
    public static enum Rank 
    { 
        ACE, TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE, 
            TEN, JACK, QUEEN, KING 
    }
    
    /**
     * Enum for representing the suit of a card.
     */
    public static enum Suit 
    { 
        CLUBS, DIAMONDS, HEARTS, SPADES 
    }
    
    //------------------------ Constructor -----------------------------
    /**
     * Construct a Card of the specified rank and suit.
     * 
     * @param r Rank
     * @param s Suit
     */
    public Card( Rank r, Suit s )
    {
        rank = r;
        suit = s;
        this.setSize( width, height );
        _pyramidNode = null;
        
        addMouseListener( this );
        if ( backImage == null ) // only read the backImage once
            readBackImage( imageSource );
        
        //---- Ace high
        //String cardFileName = getAceHiFileName( s, r );
        //---- Ace low 
        String cardFileName = getAceLoFileName( s, r );
        faceImage = readImageFromJar( imageSource, cardFileName );             
    }
    //---------------------- getAceHiFileName ------------------------
    /**
     * Get filename for the face image from the Rank when Ace is high.
     * The file names for clubs are:
     *     c1.gif == ace
     *     c2.gif == 2
     *      ...
     *     c10.gif == 10
     *     c11.gif == jack
     *     c12.gif == queen
     *     c13.gif == king
     * Other suits are the same except the first letter is d or h or s.
     * @param s Suit   the suit for the card
     * @param r Rank   the rank for the card
     * @return String    file name for the card
     */ 
    public String getAceHiFileName( Suit s, Rank r )
    {
        String[] suitPrefix = { "c", "d", "h", "s" };        
        String[] rankSuffix = { "2", "3", "4", "5", "6", "7", "8",  
            "9", "10", "11", "12", "13", "1" };
        
        return suitPrefix[ s.ordinal() ] + rankSuffix[ r.ordinal() ]
            + ".gif";        
    } 
    //---------------------- getAceLoFileName ------------------------
    /**
     * Get filename for the face image from the Rank when Ace is high
     * The file names for clubs are:
     *     c1.gif == ace
     *     c2.gif == 2
     *      ...
     *     c10.gif == 10
     *     c11.gif == jack
     *     c12.gif == queen
     *     c13.gif == king
     * Other suits are the same except the first letter is d or h or s.
     * 
     * @param s Suit      the suit for the card
     * @param r Rank      the rank for the card
     * @return String     file name for the card
     */ 
    public String getAceLoFileName( Suit s, Rank r )
    {
        String[] suitPrefix = { "c", "d", "h", "s" };        
        String[] rankSuffix = { "1", "2", "3", "4", "5", 
            "6", "7", "8", "9", "10", "11", "12", "13" };
        
        return suitPrefix[ s.ordinal() ] + rankSuffix[ r.ordinal() ]
            + ".gif";        
    } 
    //---------------------- readImageFromJar -------------------------
    /**
     * Read an image of a card from a jar file.
     * 
     * @param source String      the data source
     * @param cardName String    the file name of desired image
     * @return BufferedImage
     */
    private BufferedImage readImageFromJar( String source, String cardName )
    {
        BufferedImage cardImage = null;  // initialize return to null
        try 
        {
            // 1. create JarFile if not already done
            jarFile = new JarFile( source );
            
            // 2. get ZipEntry for "cardName" 
            ZipEntry cardEntry = jarFile.getEntry( cardName );
            
            // 3. get an InputStream object from the jarFile for the
            //    ZipEntry in step 2.
            InputStream jarIn = jarFile.getInputStream( cardEntry );
            
            // 4. Use the static read method of ImageIO to read the 
            //    input stream as a BufferedImage object:
            cardImage =  ImageIO.read( jarIn );
        }
        catch ( IOException ex )
        {
            System.err.println( "Card image error: " + cardName + 
                               "  " + source + "   "
                                   + ex.getMessage() );
        }
        return cardImage;
    }
    //---------------------- readBackImage ----------------------------
    /**
     * Get back image for all cards; you can choose blue or red backs!
     * @param source String         the data source
     */ 
    public void readBackImage( String source )
    {
        String backImageName = "b1fv.gif";  // blue back
        //String backImageName = "b2fv.gif";  // red back
        
        backImage = readImageFromJar( source, backImageName );
    }
    //--------------------- setNode( PyramidNode ) --------------------
    /**
     * Identify the PyramidNode that this card is in.
     * 
     * @param n PyramidNode<Card>
     */ 
    public void setNode( PyramidNode<Card> n )
    {
        _pyramidNode = n;   
    }   
    //--------------------- getNode() --------------------
    /**
     * Return the PyramidNode that this card is in.
     * 
     * @return PyramidNode<Card>
     */ 
    public PyramidNode<Card> getNode()
    {
        return _pyramidNode;   
    }   
    //--------------------- setFaceUp( boolean ) --------------------
    /**
     * Set the face up status of the card.
     * 
     * @param up boolean   true implies will be face up.
     */ 
    public void setFaceUp( boolean up )
    {
        _faceUp = up;   
    }   
    //----------------------- getSuit() -------------------------
    /**
     * Return the suit of the card.
     * 
     * @return Suit
     */ 
    public Suit getSuit() 
    {
        return this.suit;
    }  
    //--------------------- getRank ------------------------------
    /**
     * Return the rank of the card.
     * 
     * @return Rank
     */ 
    public Rank getRank() 
    {
        return rank;
    }   
       
    //--------------------- mousePressed --------------------------
    /**
     * mousePressed -- will attempt to play the card.
     * 
     * @param e MouseEvent
     */ 
    public void mousePressed( MouseEvent e ) 
    {
        Game.theGame().play( this );
    }
    
    //----------------------------------------------------------
    /**
     * mouse methods.
     */ 
    //----------------------------------------------------------
    /** Unimplemented. @param e MouseEvent */
    public void mouseClicked( MouseEvent e )  
    { }
    /** Unimplemented. @param e MouseEvent */
    public void mouseReleased( MouseEvent e ) 
    { }
    /** Unimplemented. @param e MouseEvent */
    public void mouseEntered( MouseEvent e ) 
    { }
    /** Unimplemented. @param e MouseEvent */
    public void mouseExited( MouseEvent e ) 
    { }
    
    //--------------------- compareTo ------------------------------
    /**
     * CompareTo only uses the Rank component in this version.
     * 
     * @param o Card
     * @return int
     */ 
    public int compareTo( Card o ) 
    {
        return this.rank.ordinal() - o.getRank().ordinal();
    }
    
    //----------------------- equals ---------------------------
    /**
     * equals only uses the Rank component.
     * 
     * @param o Card
     * @return boolean
     */ 
    public boolean equals( Card o )
    {
        return o.getRank() == this.rank;
    }
    
    //-------------------------- toString -------------------------
    /**
     * Generates a String representing this Card.
     * 
     * @return String
     */ 
    public String toString() 
    {
        return this.rank + " of " + this.suit;
    }
    
    //-------------------- paintComponent ------------------------
    /**
     * Draws the card as either face up or face down.
     * 
     * @param brush Graphics
     */ 
    public void paintComponent( java.awt.Graphics brush )
    {
        //System.out.println( "Pc: " + getLocation() );
        super.paintComponent( brush ); 
        Graphics2D brush2 = (Graphics2D) brush;
        if( _faceUp )
            brush2.drawImage( faceImage, 0, 0, null );
        else
            brush2.drawImage( backImage, 0, 0, null );
    }
    //------------------- main unit test ------------------------------
    /**
     * basic unit test for the Card
     * 
     * @param args String[]    command line args. Not used.
     */
    public static void main( String[] args )
    {
        Rank rank;
        Suit suit;
        // Create Heart Card with rank, 8:
        //     8 is a 10 if Ace is High
        //     8 is a 9 if Ace is Low
        //rank = 8;
        rank = Rank.TEN;
        suit = Suit.HEARTS;
        
        Card c1 = new Card( rank, suit );
        System.out.println( rank + ", " + suit +  " --- " + c1 );
        c1.setFaceUp( true );
        
        // Create a Space Card with rank 12,
        //     11 is either a Queen or King
        //rank = 11;
        rank = Rank.KING;
        suit = Suit.SPADES;
        
        Card c2 = new Card( rank, suit ); 
        System.out.println( rank + ", " + suit +  " --- " + c2 );
        c2.setFaceUp( true );
        /****/
        
        //------ graphical test -------------------
        JFrame f = new JFrame();
        f.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        f.setLayout( null );
        
        c1.setLocation( 50, 50 );
        f.add( c1 );
        c2.setLocation( 200, 50 );
        f.add( c2 );
        
        f.setSize( 600, 200 );
        f.setVisible( true );      
    }   
} 
