/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package AI;

import org.jbox2d.common.Vec2;

/**
 *
 * @author alasdair
 */
public class StupidPlatformController extends iPlatformController
{

    @Override
    public void update()
    {
        mEntity.getBody().setLinearVelocity(new Vec2(0.1f,0));
    }
    
}
