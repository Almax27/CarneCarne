/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Entities;

import Events.AreaEvents.AreaEvent;
import Events.AreaEvents.GoalZone;
import Events.AreaEvents.sAreaEvents;
import Graphics.Skins.iSkin;
import Level.Tile;
import Level.sLevel.TileType;
import Sound.SoundScape;
import Sound.sSound;
import States.Game.FootballMode.FootballMode;
import java.util.HashMap;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Fixture;
import org.jbox2d.dynamics.contacts.ContactEdge;

/**
 *
 * @author alasdair
 */
public class Football extends Pea
{
    FootballMode mGameMode;
    int mDoomTimer;
    Football(iSkin _skin)
    {
        super(_skin);
        mGameMode = null;
        mDoomTimer = 0;
    }
    
    @Override
    public void update()
    {
        if (mWaterHeight != 0)
        {
            buoyancy();
        }
        if (mDoomTimer != 0)
        {
            mDoomTimer--;
            if (mDoomTimer == 0)
            {
                kill(CauseOfDeath.eMundane, this);
                mBody = null;
                return;
            }
        }
        else
        {
            AreaEvent event = sAreaEvents.collidePoint(mBody.getPosition()); 
            try
            {
                GoalZone zone = (GoalZone)event;
                zone.enter(this);
            }
            catch (Throwable e) /// Null pointer and class cass
            {

            }
        }
        ContactEdge edge = getBody().m_contactList;
        while (edge != null)
        {
            Fixture other = edge.contact.m_fixtureA;
            boolean AtoB = true;
            if (other.m_body == getBody())
            {
                AtoB = false;
                other = edge.contact.m_fixtureB;
            }
            if (other.getUserData() != null && ((Tile)other.getUserData()).getTileType().equals(TileType.eSpikes) && edge.contact.isTouching())
            {
                Vec2 collisionNorm = edge.contact.m_manifold.localNormal;
                float rot = other.getBody().getTransform().getAngle();
                collisionNorm.x = (float) (collisionNorm.x*Math.cos(rot) - collisionNorm.y*Math.sin(rot));
                collisionNorm.y = (float) (collisionNorm.x*Math.sin(rot) + collisionNorm.y*Math.cos(rot));
                if(AtoB == false)
                {
                    collisionNorm.negateLocal();
                }
                collisionNorm.normalize();
                if (Vec2.dot(collisionNorm, ((Tile)other.getUserData()).getRootTile().getSpikeNormal()) > 0.8f)
                {
                    kill(CauseOfDeath.eSpikes, other); 
                }
            }
            edge = edge.next;
        }
        if (mDoomTimer != 0)
        {
            mDoomTimer--;
            if (mDoomTimer == 0)
            {
                kill(CauseOfDeath.eMundane, this);
                mBody = null;
            }
        }
        else
        {
            try
            {
                AreaEvent event = sAreaEvents.collidePoint(mBody.getPosition()); 
                try
                {
                    GoalZone zone = (GoalZone)event;
                    zone.enter(this);
                }
                catch (Throwable e) /// Null pointer and class cass
                {

                }
            }
            catch(Throwable e)
            {
                
            }

        }
    }

    public void setGameMode(FootballMode _gameMode)
    {
        mGameMode = _gameMode;
    }
    
    @Override
    public void kill(CauseOfDeath _cause, Object _killer)
    {
        if (mBody != null)
        {
            sSound.playPositional(SoundScape.Sound.eFootballDeath, mBody.getPosition());
            HashMap params = new HashMap();
            switch (_cause)
            {
                case eSpikes:
                //{
                    Fixture killer = (Fixture)_killer;
                    params.put("attachment", killer);
                    /// Purposefully not breaking
                    case eFire:
                    {
                        params.put("characterType", "Pea");
                        try
                        {
                            params.put("killer", ((Fixture)_killer).getBody().getUserData());
                        }
                        catch (ClassCastException e)
                        {
                            params.put("killer", _killer);
                        }
                        params.put("causeOfDeath", _cause);
                        params.put("position", mBody.getPosition());
                        params.put("rotation", mBody.getAngle());
                        params.put("linearVelocity", mBody.getLinearVelocity());
                        params.put("angularVelocity", mBody.getAngularVelocity());
                        sEntityFactory.create("Carcass", params);
                        break;
                    }
                //}
                default:
                {
                    break;
                }
            }
            mGameMode.footballDied(this);
            super.kill(_cause, _killer);
        }
    }

    public void doom()
    {
        mDoomTimer = 120;
    }
}
