package Input;

import Events.AnalogueStickEvent;
import Events.KeyDownEvent;
import Events.KeyUpEvent;
import Events.MapClickEvent;
import Events.MapClickReleaseEvent;
import Events.RightStickEvent;
import Events.sEvents;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.jbox2d.common.Vec2;
import org.newdawn.slick.Input;

/**
 *
 * @author alasdair
 */
/* XBox Controls
 * 0 - A
 * 1 - B
 * 2 - X
 * 3 - Y
 * 4 - L1
 * 5 - R1
 * 6 - Back
 * 7 - Start
 * 8 - L3
 * 9 - R3
 */
//// Clean up this class with arrays and shit, so the input can be pluggable FIXME
class XBoxController extends Controller
{
    private enum TriggerState
    {
        eStart, // This is the value for the controller sending out -1 continously because its stupid
        eNotPressed,
        eRightPressed,
        eLeftPressed,
        eTriggerStateMax,
    }
    private class ButtonBinding
    {
        public ButtonBinding(int _buttonId, char _key)
        {
            buttonId = _buttonId;
            key = _key;
        }
        public int buttonId;
        public char key;
        public boolean isPressed = false;
    }

    static private float shoulderButtonEpsilon = 0.3f;
    private List<ButtonBinding> mButtonBindings = new ArrayList<ButtonBinding>();
    
    private TriggerState mTriggerState = TriggerState.eStart;
    
    public XBoxController(int _inputId, int _player)
    {
        super(_inputId, _player);

        mButtonBindings.add(new ButtonBinding(0, 'w'));
        mButtonBindings.add(new ButtonBinding(6, 'r'));
        mButtonBindings.add(new ButtonBinding(7, 'Q'));
    }
    
    public void update(Input _input, int _delta)
    {
        assert(_input.getAxisCount(mInputId) >= 4);

        Vec2 rightStick = new Vec2(_input.getAxisValue(mInputId, 3),_input.getAxisValue(mInputId, 2));
        Vec2 leftStick = new Vec2(_input.getAxisValue(mInputId, 1),_input.getAxisValue(mInputId, 0));

        //Slick input is -1,1 until first input is recieved...
        if(leftStick.x == -1.0 && leftStick.y == -1.0)
        {
            return;
        }

        //override with D-Pad
        if(_input.isControllerRight(mInputId))
        {
            leftStick.x = 1;
        }
        else if(_input.isControllerLeft(mInputId))
        {
            leftStick.x = -1;
        }
        if(_input.isControllerUp(mInputId))
        {
            leftStick.y = -1;
        }
        else if(_input.isControllerDown(mInputId))
        {
            leftStick.y = 1;
        }

        if (leftStick.length() > 0.7f)
        {
            sEvents.triggerEvent(new AnalogueStickEvent(leftStick.x,-leftStick.y, mPlayer)); //move
        }
        
        //fallback right to left
        if (rightStick.length() < 0.2f)
        {
            rightStick = leftStick;
        }
        if (rightStick.length() > 0.2f)
        {
            sEvents.triggerEvent(new RightStickEvent(rightStick, mPlayer));
        }
        
        //Handle button event
        mButtonBindings.forEach((v) -> 
        {
            boolean isPressed = _input.isButtonPressed(v.buttonId, mInputId);
            if(v.isPressed != isPressed)
            {
                v.isPressed = isPressed;
                if(isPressed)
                {
                    sEvents.triggerEvent(new KeyDownEvent(v.key, mPlayer)); //menu
                }
                else
                {
                    sEvents.triggerEvent(new KeyUpEvent(v.key, mPlayer)); //menu
                }
            }            
        });

        //handle shoulder triggers
        float shoulderButtons =_input.getAxisValue(mInputId,4);
        if (mTriggerState != TriggerState.eStart || shoulderButtons != -1.0f)
        {
            if (shoulderButtons > shoulderButtonEpsilon) //left trigger
            {
                changeState(TriggerState.eLeftPressed, rightStick);
            }
            else if (shoulderButtons < -shoulderButtonEpsilon) //right trigger
            {
                changeState(TriggerState.eRightPressed, rightStick);
            }
            else
            {
                changeState(TriggerState.eNotPressed, rightStick);
            }
        }   
    }
    //statemachine required to determine releases for triggers
    private void changeState(TriggerState _newState, Vec2 _rightStick)
    {
        switch (mTriggerState) /// Old
        {
            case eStart:
            case eNotPressed:
            {
                break;
            }
            case eRightPressed:
            {
                if (_newState != TriggerState.eRightPressed)
                {
                    sEvents.triggerEvent(new MapClickReleaseEvent(_rightStick,"TongueHammer", mPlayer));
                }
                break;
            }
            case eLeftPressed:
            {
                if (_newState != TriggerState.eLeftPressed)
                {
                    sEvents.triggerEvent(new MapClickReleaseEvent(_rightStick,"Spit", mPlayer));
                }
                break;
            }
            
        }
        switch (_newState) /// New
        {
            case eNotPressed:
            {
                break;
            }
            case eRightPressed:
            {
                sEvents.triggerEvent(new MapClickEvent(_rightStick,"TongueHammer", mPlayer));
                break;
            }
            case eLeftPressed:
            {
                sEvents.triggerEvent(new MapClickEvent(_rightStick,"Spit", mPlayer));
                break;
            }
            
        }
        mTriggerState = _newState;
    }
}
