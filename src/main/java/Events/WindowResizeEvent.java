/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Events;

import org.jbox2d.common.Vec2;

/**
 *
 * @author aaron
 */
public class WindowResizeEvent extends iEvent {
    
    private Vec2 mNewDimensions;

    public WindowResizeEvent(Vec2 _newDimentions) {
        mNewDimensions = _newDimentions;
    }

    public Vec2 getDimensions()
    {
        return mNewDimensions;
    }
    
    @Override
    public String getName() {
        return "WindowResizeEvent";
    }

    @Override
    public String getType() {
        return "WindowResizeEvent";
    }
    
    
}
