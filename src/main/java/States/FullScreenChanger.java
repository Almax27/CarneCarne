/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package States;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

/**
 *
 * @author a203945
 */
public class FullScreenChanger implements Runnable{
    private boolean mFullScreen;
    StateBasedGame mSbg;
    public FullScreenChanger(StateBasedGame _sbg)
    {
        mSbg = _sbg;
        mFullScreen = false;
    }
    public void run() {
        mFullScreen = !mFullScreen;
        try {mSbg.getContainer().setFullscreen(mFullScreen);} 
        catch (SlickException ex) {Logger.getLogger(FullScreenChanger.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
