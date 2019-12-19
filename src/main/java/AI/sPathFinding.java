/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package AI;

import Entities.Entity;
import java.util.LinkedList;

/**
 *
 * @author G203947
 */
public class sPathFinding 
{
    private static LinkedList<Entity> mPlayers = new LinkedList();
    
    public static void addPlayer(Entity _player)
    {
        if(!mPlayers.contains(_player))
        {
            mPlayers.add(_player);
        }
    }
    public static LinkedList<Entity> getPlayerList()
    {
        return new LinkedList<Entity>(mPlayers);
    }
    public static Entity getPlayer()
    {
        if(!mPlayers.isEmpty())
            return mPlayers.peek();
        return null;
    }

    public static void cleanup()
    {
        mPlayers = new LinkedList();
    }
    
}
