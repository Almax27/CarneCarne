/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package States.Game;

import org.newdawn.slick.Graphics;

/**
 *
 * @author alasdair
 */
public interface iGameMode
{
    public iGameMode update(Graphics _graphics, float _time);
    public void render(Graphics _graphics);
    public void cleanup();
}
