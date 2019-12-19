/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Input;

import Events.KeyDownEvent;
import Events.KeyUpEvent;
import Events.MapClickEvent;
import Events.MapClickReleaseEvent;
import Events.MouseMoveEvent;
import Events.sEvents;
import Graphics.sGraphicsManager;
import World.sWorld;
import java.util.ArrayList;
import org.jbox2d.common.Vec2;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Input;

/**
 *
 * @author Aaron
 */
public class sInput 
{
    private sInput() {}
    static private GameContainer mGameContainer = null; 
    static private ArrayList<Controller> controllers = new ArrayList<Controller>();
    static MouseStateMachine mMouseStateMachine;
    static boolean mJumpToggle = false;
    static boolean mPauseToggle = false;
    
    public static void init(GameContainer _gc)
    {
        mGameContainer = _gc;
        mMouseStateMachine = new MouseStateMachine(_gc.getInput());
    }
    public static void update(int _delta)
    {
        Input input = mGameContainer.getInput();
        
        //handle keyboard&mouse input (defaults to player 0)
        mMouseStateMachine.tick(input);
        if(input.isKeyDown(Input.KEY_W))
        {
            sEvents.triggerEvent(new KeyDownEvent('w', 0));
            mJumpToggle = true;
        }
        else if(mJumpToggle)
        {
            sEvents.triggerEvent(new KeyUpEvent('w', 0));
            mJumpToggle = false;
        } 
        if(input.isKeyDown(Input.KEY_A))
            sEvents.triggerEvent(new KeyDownEvent('a', 0));
        if(input.isKeyDown(Input.KEY_S))
            sEvents.triggerEvent(new KeyDownEvent('s', 0));
        if(input.isKeyDown(Input.KEY_D))
            sEvents.triggerEvent(new KeyDownEvent('d', 0));
        if(input.isKeyDown(Input.KEY_SPACE))
            sEvents.triggerEvent(new KeyDownEvent(' ', 0));
        if(input.isKeyPressed(Input.KEY_ESCAPE))
            sEvents.triggerEvent(new KeyDownEvent('Q', 0));
        if(input.isKeyDown(Input.KEY_R))
            sEvents.triggerEvent(new KeyDownEvent('r', 0));
        if(input.isKeyDown(Input.KEY_F11))
            sGraphicsManager.toggleFullscreen();
        if(input.isKeyPressed(Input.KEY_F12))
            sGraphicsManager.toggleRenderDebugInfo();
        
        if(controllers.size() != input.getControllerCount())
            initContollers(input);
        
        for(Controller controller : controllers)
        {
            if(controller != null)
            {
                controller.update(input, _delta);
            }
        }
    }
    
    private static void initContollers(Input input)
    {
        System.err.println("Initialising Controllers");

        controllers.clear();
        
        boolean useControllerForPlayer0 = true;
        int playerIndex = useControllerForPlayer0 ? 0 : 1;

        input.clearControlPressedRecord();
        
        for(int i = 0; i < input.getControllerCount(); i++)
        {
            Controller controller = Controller.create(input, i, playerIndex);
            if(controller != null)
            {
                System.err.printf("Controller {0} assigned to player {1}\n", i, playerIndex);
                playerIndex++;    
            }
            controllers.add(controller);
            
        }
    }
    public static void setAbsCursorPos(Vec2 _pos)
    {
        org.lwjgl.input.Mouse.setCursorPosition((int)_pos.x, (int)_pos.y);
    }
    public static Vec2 getAbsCursorPos()
    {
        return new Vec2(org.lwjgl.input.Mouse.getX(),org.lwjgl.input.Mouse.getY());
    }
    public static Vec2 getDeltaCursor()
    {
        return new Vec2(org.lwjgl.input.Mouse.getDX(),org.lwjgl.input.Mouse.getDY());
    }
}

final class MouseStateMachine
{
    enum States
    {
        eRightPressed,
        eRightReleased,
        eLeftPressed,
        eLeftReleased,
        eMouseStatesMax
    }
    
    States mState = States.eMouseStatesMax;
    int mMouseX = 0, mMouseY = 0;
    int mNewMouseX = 0, mNewMouseY = 0;
    MouseStateMachine(Input _input) {
        mMouseX = mNewMouseX = _input.getMouseX();
        mMouseY = mNewMouseY = _input.getMouseY();
    }

    void tick(Input _input)
    {
        mNewMouseX = _input.getAbsoluteMouseX();
        mNewMouseY = _input.getAbsoluteMouseY();
        if(mNewMouseX != mMouseX || mNewMouseY != mMouseY) //on mouse move
        {
            sEvents.triggerEvent(new MouseMoveEvent(new Vec2(mNewMouseX,mNewMouseY),new Vec2(mMouseX,mMouseY), 0));
            mMouseX = mNewMouseX;
            mMouseY = mNewMouseY;
        }
        if(_input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON))
        {
            changeState(States.eLeftPressed);
        }
        else if(_input.isMouseButtonDown(Input.MOUSE_RIGHT_BUTTON))
        {
            changeState(States.eRightPressed);
        }
        else
        {
            changeState(States.eMouseStatesMax);
        }
    }
    void changeState(States _state)
    {
        Vec2 physicsPos = sWorld.translateToPhysics(new Vec2(mNewMouseX, mNewMouseY));
        switch(mState) //old
        {
            case eRightPressed:
                if(_state != States.eRightPressed)
                {
                    sEvents.triggerEvent(new MapClickReleaseEvent(physicsPos,"Spit", 0));
                }
                break;
            case eLeftPressed:
                if(_state != States.eLeftPressed)
                {
                    sEvents.triggerEvent(new MapClickReleaseEvent(physicsPos,"TongueHammer", 0));
                }
                break;
        }
        switch(_state)
        {
            case eRightPressed:
                sEvents.triggerEvent(new MapClickEvent(physicsPos,"Spit",0));
                break;
            case eLeftPressed:
                sEvents.triggerEvent(new MapClickEvent(physicsPos,"TongueHammer",0));
                break;
        }
        mState = _state;
    }

}