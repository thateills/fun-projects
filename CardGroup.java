/**
 * CardGroup -- encapsulates shared features of CardStack and CardList.
 *                          facilitates display.
 * @Thatcher Eills
 */
public interface CardGroup
{
    /** 
     * Return i-th card in group. 
     * @param i int
     * @return Card
     */
    public Card get( int i );     // get the i-th card
    
    /**
     * Return number cards in the group.
     * @return int
     */
    public int  size();           // number Cards in the group
    
    /**
     * Return x location of the group display representation.
     * @return int
     */
    public int  getXLocation();   // return x location
    
    /**
     * Return y location of the group display representation.
     * @return int
     */
    public int  getYLocation();   // return y location
} 
