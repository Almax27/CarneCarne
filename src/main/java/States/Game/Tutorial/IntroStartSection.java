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
        super(_position, _playerNumber, null, null, 0.0f);
        sEvents.blockEvent("MapClickEvent"+"Spit"+mPlayerNumber);
        sEvents.blockEvent("MapClickEvent"+"TongueHammer"+mPlayerNumber);
        sEvents.blockEvent("MapClickEvent"+"Tongue"+mPlayerNumber);
        sEvents.blockEvent("MapClickEvent"+"Hammer"+mPlayerNumber);
        sEvents.blockEvent("MapClickReleaseEvent"+"Spit"+mPlayerNumber);
        sEvents.blockEvent("MapClickReleaseEvent"+"TongueHammer"+mPlayerNumber);
        sEvents.blockEvent("MapClickReleaseEvent"+"Tongue"+mPlayerNumber);
        sEvents.blockEvent("MapClickReleaseEvent"+"Hammer"+mPlayerNumber);
        sEvents.blockEvent("KeyDownEvent"+'w'+mPlayerNumber);
    }

    @Override
    void cleanup()
    {
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
    protected void renderInternal(float scale)
    {
        //nothing this is the start
    }

}
