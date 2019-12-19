/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package AI;

import Entities.AIEntity;
import Entities.Broccoli;
import Entities.Entity;
import java.util.LinkedList;
import org.jbox2d.common.Vec2;

/**
 *
 * @author Graham
 */
public class BroccoliController extends iAIController
{
     private static int newTargetDelay = 60; //FIXME: assumes 60fps
     private Entity mTarget;
     private float mFollowRadius;
     private float mAttackRadius;
     private boolean mAttacking;
     private int mNewTargetTimer = 0;
     
    public BroccoliController(AIEntity _entity)
    {
        super(_entity);
        mFollowRadius = 7.5f;
        mAttackRadius = 1.5f;
        mAttacking = false;
    }
    
    public void update()
    {
        mTarget = pickTarget(sPathFinding.getPlayerList());
        if(mTarget != null)
            ((Broccoli)mEntity).updateTargetPos(mTarget.getBody().getPosition());
        
        if(mTarget != null)
        {
            Vec2 myPos = mEntity.getBody().getPosition();
            Vec2 targetPos = mTarget.getBody().getPosition();
            
            if(mAttacking == false)            
            {
                if ((targetPos.x < myPos.x+mAttackRadius) && targetPos.x > myPos.x-mAttackRadius)
                {
                    if((targetPos.y < myPos.y+mAttackRadius) && targetPos.y > myPos.y-mAttackRadius)
                    {
                        mAttacking = true;
                    }
                }
                else if((targetPos.x < myPos.x+mFollowRadius) && targetPos.x > myPos.x-mFollowRadius)
                {
                    if((targetPos.y < myPos.y+mFollowRadius) && targetPos.y > myPos.y-mFollowRadius)
                    {
                        if(targetPos.x > myPos.x)
                        {
                            ((Broccoli)mEntity).move(1.5f);
                        }
                        else
                        {
                            ((Broccoli)mEntity).move(-1.5f);
                        }
                    }
                }
            }
            else
            {
                 ((Broccoli)mEntity).attack();
            }
        }
    }
    private Entity pickTarget(LinkedList<Entity> _players)
    {
        Entity newTarget = null;
        float minDist = Float.MAX_VALUE;
        //calc closest player
        for(Entity player : _players)
        {
            float dist = player.getBody().getPosition().sub(mEntity.getBody().getPosition()).lengthSquared();
            if(dist < minDist)
            {
                minDist = dist;
                newTarget = player;
            }
        }
        if(!newTarget.equals(mTarget)) //if new target 
        {
            if(mNewTargetTimer > newTargetDelay)//wait for timer before swtiching
            {
                mNewTargetTimer = 0;
            }
            else
            {
                mNewTargetTimer++;
                newTarget = mTarget;
            }
        }
        else //if same target: reset delay
            mNewTargetTimer = 0;
        return newTarget;
    }
}
