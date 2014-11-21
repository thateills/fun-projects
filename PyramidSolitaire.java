import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.*;
/**
 * PyramidSolitaire.
 * @Thatcher Eills
 * Last modified 03/28/14
 */
public class PyramidSolitaire extends JFrame
{
    //---------------------- class variables -------------------------
    static String      deckFile = null;
    
    //---------------------- instance variables ----------------------
    private GUI _appPanel;      // the app's JPanel
    
    //--------------------------- constructor ------------------------
    /**
     * Constructor for playable solitaire game. 
     * The first argument = file name for the initial deck.
     * 
     * @param title String     window title
     * @param args String[]    command line args; only arg is initial deck
     */
    public PyramidSolitaire( String title, String[] args )     
    {
        super( title );
        this.setBackground( Color.LIGHT_GRAY );
        this.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );

        _appPanel = new GUI( );
        this.add( _appPanel );
        this.setSize( _appPanel.getWidth(), _appPanel.getHeight() + 100 );
        
        this.setVisible( true );
    }
    //--------------------------- main --------------------------------
    /**
     * This starts the application.
     * 
     * @param args String[]            command line arguments.
     */
    public static void main( String [ ] args ) 
    {
        if ( args.length > 0 )  
            batchRun( args );
        else
            new PyramidSolitaire( "PyramidSolitaire", args );
    }
}
