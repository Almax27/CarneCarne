/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package States.Game.Tutorial;

import Events.sEvents;
import org.jbox2d.common.Vec2;

/**
 *
 * @author alasdair
 */
class IntroStartSection extends IntroSection
{
    public IntroStartSection(Vec2 _position, int _playerNumber)
    {
        super(_position, _playerNumber);
        sEvents.blockEvent("MapClickEventL"+mPlayerNumber);
        sEvents.blockEvent("MapClickEventR"+mPlayerNumber);
        sEvents.blockEvent("KeyDownEvent"+'w'+mPlayerNumber);
    }

    @Override
    public IntroSection updateImpl()
    {
        if (mTimer > 60)
        {
            return new IntroJumpSection(mPosition,mPlayerNumber);
        }
        return this;
    }

    @Override
    public void render()
    {
    }

}