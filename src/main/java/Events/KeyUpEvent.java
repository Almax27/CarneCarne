/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Events;

/**
 *
 * @author A203946
 */
public class KeyUpEvent extends iEvent {
    
    private char mKey;
    private int mPlayer;
    public KeyUpEvent(char _key, int _player)
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
        return "KeyUpEvent";
    }
    public char getKey()
    {
        return mKey;
    }
}
