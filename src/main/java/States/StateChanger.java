/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package States;

import States.Menu.StateMenu;
import org.newdawn.slick.Image;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.Transition;

/**
 *
 * @author a203945
 */
public class StateChanger implements Runnable{
    private StateBasedGame mSbg;
    private int mID = 0;
    private Transition mEnter,mLeave;
    public StateChanger(int _id, Transition _leave, Transition _enter, StateBasedGame _sbg)
    {
        mID = _id;
        mLeave = _leave;
        mEnter = _enter;
        mSbg = _sbg;
    }
    public void setTransitions(Transition _leave, Transition _enter)
    {
        mLeave = _leave;
        mEnter = _enter;
    }
    public void run() 
    {
        if(mID == 4) //menu
        {
            Image screenShot = ((StateMenu)mSbg.getState(mID)).getScreenShot();
            screenShot.flushPixelData();
            mSbg.getContainer().getGraphics().copyArea(screenShot, 0, 0);
            screenShot.setAlpha(0.5f);
        }
        mSbg.enterState(mID,mLeave,mEnter);
    }
}
