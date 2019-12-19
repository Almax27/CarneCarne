/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package World.PhysicsFactories;

import java.util.HashMap;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.World;

/**
 *
 * @author A203946
 */
public interface iPhysicsFactory {
    
    Body useFactory(HashMap _parameters, World _world);
}
