/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package AI;

import Entities.AIEntity;

/**
 *
 * @author alasdair
 */
public class PathShowController extends CarrotController
{
    
    public PathShowController(AIEntity _entity)
    {
        super(_entity);
        mTarget = null;
    }
    @Override
    protected void updateTarget()
    {
        mTargetX = 26;
        mTargetY = 6;
    }
}
