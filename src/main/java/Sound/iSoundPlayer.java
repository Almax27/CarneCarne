/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Sound;

/**
 *
 * @author alasdair
 */
public interface iSoundPlayer
{
    void play(Object _parameter);
    boolean isPlaying();

    public void stop(Object _parameter);

    public void playPositional(iSoundAnchor _position, Object _parameter);
}
