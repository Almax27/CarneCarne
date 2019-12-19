/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Entities;

import Graphics.Particles.ParticleSysBase;
import Graphics.Particles.sParticleManager;
import Graphics.Skins.iSkin;
import Level.RootTile.AnimationType;
import Level.Tile;
import Level.sLevel.TileType;
import Score.ScoreTracker.ScoreEvent;
import Sound.MovingSoundAnchor;
import Sound.SoundScape;
import Sound.sSound;
import World.sWorld;
import World.sWorld.BodyCategories;
import java.util.HashMap;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.Fixture;
import org.jbox2d.dynamics.contacts.ContactEdge;

/**
 *
 * @author alasdair
 */
public class FireParticle extends Entity
{
    Vec2 mVelocity;
    ParticleSysBase mParticles;
    int mTimer;
    PlayerEntity mOwner;
    public FireParticle(iSkin _skin, Vec2 _velocity, PlayerEntity _owner)
    {
        super(_skin);
        mVelocity = _velocity;
        mOwner = _owner;
        mTimer = 10000;
        mParticles = sParticleManager.createSystem("Fire", new Vec2(0, 0),-1);
        mTimer = 100;
    }
    @Override
    public void setBody(Body _body)
    {
        super.setBody(_body);
        sSound.playPositional(SoundScape.Sound.eFireParticleBurn, new MovingSoundAnchor(_body, new Vec2(0,0)));
    }
    @Override
    public void update()
    {
        mTimer--;
        if (mTimer <= 0)
        {
            kill(CauseOfDeath.eMundane, null);
        }
        else
        {
            //mVelocity.y += 0.2f;
            getBody().setLinearVelocity(mVelocity);
            int tileMask = (1 << BodyCategories.eEdibleTiles.ordinal()) |
                (1 << BodyCategories.eNonEdibleTiles.ordinal()) |
                (1 << BodyCategories.eWater.ordinal()) |
                (1 << BodyCategories.eGum.ordinal()) |
                (1 << BodyCategories.eIce.ordinal()) |
                (1 << BodyCategories.eTar.ordinal());
            for (ContactEdge edge = getBody().getContactList(); edge != null; edge = edge.next)
            {
                Fixture other = edge.contact.m_fixtureA;
                Tile tile = (Tile)edge.contact.m_fixtureA.getUserData();
                if (tile == null)
                {
                    tile = (Tile)edge.contact.m_fixtureB.getUserData();
                    other = edge.contact.m_fixtureB;
                }
                if (edge.contact.isTouching() && (other.m_filter.categoryBits & tileMask) != 0)
                {
                    Vec2 position = this.getBody().getPosition();
                    Vec2 direction = this.getBody().getLinearVelocity();
                    String animationName = tile.getAnimationsName(AnimationType.eFireHit);
                    if (!tile.getTileType().equals(TileType.eIce))
                    {
                        if (tile.getTileType().equals(TileType.eWater))
                        {
                            sSound.playPositional(SoundScape.Sound.eFireHitWater, mBody.getPosition());
                        }
                        else
                        {
                            sSound.playPositional(SoundScape.Sound.eFireHitObject, mBody.getPosition());
                        }
                        kill(CauseOfDeath.eMundane, null);
                    }
                    else
                    {
                        mTimer-= 15;
                    }
                    tile.setOnFire();
                    ParticleSysBase system = sParticleManager.createSystem(animationName + "FireHit", position.add(new Vec2(0.5f,0.5f)).mul(64.0f), 2);
                    if(system != null) //catch if system doesn't exist
                    {
                        direction.normalize();
                        float offset = (float)Math.atan2(direction.y, direction.x) * 180.0f/(float)Math.PI;
                        system.setAngularOffset(offset-90.0f);
                    }
                    break;
                }
            }
        }
    }
    @Override
    public void render()
    {
        Vec2 pixelPosition = sWorld.translateToWorld(getBody().getPosition().add(new Vec2(0.0f,0.0f))); /// FIXME
        Vec2 particlePosition = getBody().getPosition().add(new Vec2(0.5f,0.5f)).mul(64f);
        mParticles.moveEmittersTo(particlePosition.x, particlePosition.y);
        /*Vec2 direction = mVelocity.clone(); Y U NO WORK?
        direction.normalize();
        mSkin.setRotation((float)Math.atan2(direction.y, direction.x));*/
        mSkin.render(pixelPosition.x,pixelPosition.y);
    }
    @Override
    public void kill(CauseOfDeath _causeOfDeath, Object _killer)
    {
        if (!_causeOfDeath.equals(CauseOfDeath.eFire))
        {
            HashMap params = new HashMap();
            params.put("position", mBody.getPosition());
            params.put("player", mOwner);
            sEntityFactory.create("Explosion", params);
            sWorld.destroyBody(getBody());
            mParticles.kill();
        }
    }

    @Override
    public boolean killedOpponent(Entity _oppononent)
    {
        if (mOwner == _oppononent)
        {
            return false;
        }
        else
        {
            mOwner.mScoreTracker.score(ScoreEvent.eKilledOpponent);
            return true;
        }
    }
}
