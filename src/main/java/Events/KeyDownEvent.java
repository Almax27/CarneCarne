/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Events;

/**
 *
 * @author alasdair
 */
public class KeyDownEvent extends iEvent{
    
    private char mKey;
    private int mPlayer;
    public KeyDownEvent(char _key, int _player)
    {
        mKey = _key;
        mPlayer = _player;
    }
    public String getName()
    {
        return getType() + mKey + mPlayer;
    }
    public String getType()
    {
        return "KeyDownEvent";
    }
    public char getKey()
    {
        return mKey;
    }
    public int getPlayer()
    {
        return mPlayer;
    }
}
