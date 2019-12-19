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
abstract public class iAIController {
    
    protected AIEntity mEntity;
    public iAIController(AIEntity _entity)
    {
        mEntity = _entity;
    }
    abstract public void update();
    
    public void destroy()
    {
        mEntity = null;
    }

}
