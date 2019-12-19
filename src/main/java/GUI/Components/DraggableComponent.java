/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI.Components;

import org.newdawn.slick.Input;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.gui.GUIContext;

/**
 *
 * @author a203945
 */
public class DraggableComponent extends GraphicalComponent {
    public DraggableComponent(GUIContext _context, Vector2f _position, Vector2f _dimensions) {
        super(_context, _position, _dimensions);
        init(_context);
    }
    public DraggableComponent(GUIContext _context) {
        super(_context);
        init(_context);
    }
    
    private boolean mIsSelected = false;
    private boolean mMoveInX = true , mMoveInY = true;
    private Vector2f mRangeX = new Vector2f(0,0);
    private Vector2f mRangeY = new Vector2f(0,0);
    
    public void setRangeX(Vector2f _range){mRangeX = _range.copy();}
    public void setRangeY(Vector2f _range){mRangeY = _range.copy();}
    public void setMoveInX(boolean _move){mMoveInX = _move;}
    public void setMoveInY(boolean _move){mMoveInY = _move;}
    public boolean getMoveInX(){return mMoveInX;}
    public boolean getMoveInY(){return mMoveInY;}
   
    private void init(GUIContext _context)
    {
        _context.getInput().addListener(this);
    }
    
    @Override
    public void mousePressed(int button, int x, int y) {
        if(button == Input.MOUSE_LEFT_BUTTON)
        {
            if(getShape().contains(x, y))
            {
                consumeEvent();
                mIsSelected = true;
            }
        }
    }

    @Override
    public void mouseReleased(int button, int x, int y) {
        if(mIsSelected)
        {
            consumeEvent();
            mIsSelected = false;
        }
    }

    @Override
    public void mouseDragged(int oldx, int oldy, int newx, int newy) {
        if(mIsSelected)
        {
            int dx = 0, dy = 0;
            //limit movement
            if(mMoveInX)
                dx = newx - oldx;
            if(mMoveInY)
                dy = newy - oldy;
            //ensure in given range
            Vector2f newPos = getLocalTranslation().add(new Vector2f(dx,dy));
            if( newPos.x >= mRangeX.x && newPos.x <= mRangeX.y &&
                newPos.y >= mRangeY.x && newPos.y <= mRangeY.y)
            {
                setLocalTranslation(getLocalTranslation().add(new Vector2f(dx,dy)));
            }
        }
    }
    
    
    

    
    
    
    
}
