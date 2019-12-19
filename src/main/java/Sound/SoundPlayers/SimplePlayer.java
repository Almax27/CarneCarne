/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Sound.SoundPlayers;

import Sound.iSoundAnchor;
import Sound.iSoundPlayer;
import Sound.sSound;
import org.jbox2d.common.Vec2;

/**
 *
 * @author alasdair
 */
public class SimplePlayer implements iSoundPlayer
{
    String mName;
    public SimplePlayer(String _name)
    {
        mName = _name;
    }
    public void play(Object _parameter)
    {
        sSound.play(mName, false);
    }
    public void playPositional(iSoundAnchor _position, Object _parameter)
    {
        Vec2 position = _position.getPosition();
        sSound.play3D(mName, position.x, position.y, 1);
    }
    public void stop(Object _parameter)
    {
        sSound.stop(mName);
    }

    public boolean isPlaying()
    {
        return sSound.isPlaying(mName);
    }
    
}
