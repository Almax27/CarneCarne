/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package AI;

import org.jbox2d.common.Vec2;

/**
 *
 * @author Graham
 */
public interface iPathFinding 
{ 
    
    enum Command
    {
        eMoveLeft,
        eMoveRight,
        eMoveUp,
        eMoveDown,
        eMoveTopLeft,
        eMoveTopRight,
        eMoveBottomLeft,
        eMoveBottomRight,
        eStandStill,
        eCommandMax
    }
    public void updatePath(int xStart, int _yStart, int _xTarget, int _yTarget);
    
    public Vec2 follow();
}
