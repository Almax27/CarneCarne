/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Entities;

import org.jbox2d.dynamics.Body;
import Graphics.Skins.iSkin;
import World.sWorld;
import org.jbox2d.collision.shapes.MassData;
import org.jbox2d.collision.shapes.Shape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Fixture;
/**
 *
 * @author alasdair
 */
abstract public class Entity {
    
    protected Body mBody;
    public iSkin mSkin;
    protected int mWaterHeight;
    protected int mWaterTiles;
    private float mArea;
    
    public Entity(iSkin _skin)
    {
        mSkin = _skin;
        mWaterHeight = 0;
        mWaterTiles = 0;
    }
    
    abstract public void update();
    
    public void render()
    {
        Vec2 pixelPosition = sWorld.translateToWorld(getBody().getPosition());
        mSkin.render(pixelPosition.x,pixelPosition.y);
    }
    
    public void submerge(int _height)
    {
        mWaterHeight = _height;
        mWaterTiles++;
    }
    public void unsubmerge()
    {
        mWaterTiles--;
        if (mWaterTiles == 0)
        {
            mWaterHeight = 0;
        }
    }
    private static final Vec2 waterNormal = new Vec2(0,-1);
    
    private void computeSubmergeArea(Shape _shape, Vec2 _sc)
    {
        switch (_shape.getType())
        {
            case CIRCLE:
            {
                break;
            }
        }
    }
    public boolean killedOpponent(Entity _oppononent)
    {
        return true;
    }
    public boolean isGrabbable()
    {
        return true;
    }
    /*protected void buoyancy()
    {
        Vec2 areac = new Vec2(0, 0);
        Vec2 massc = new Vec2(0, 0);
        float area = 0;
        float mass = 0;
        for (Fixture fixture = mBody.getFixtureList(); fixture != null; fixture = fixture.getNext())
        {
            Shape shape = fixture.getShape();
            Vec2 sc = new Vec2(0, 0);
            float sarea = shape.computeSubmergedArea(waterNormal, mWaterHeight, sc);
            area += sarea;
            areac.x += sarea * sc.x;
            areac.y += sarea * sc.y;
            float shapeDensity = 0;
            if (useDensity) {
                //TODO: Expose density publicly
                shapeDensity = shape.getDensity();
            } else {
                shapeDensity = 1;
            }
            mass += sarea * shapeDensity;
            massc.x += sarea * sc.x * shapeDensity;
            massc.y += sarea * sc.y * shapeDensity;
        }
        areac.x /= area;
        areac.y /= area;
        //Vec2 localCentroid = XForm.mulTrans(body.getXForm(),areac);
        massc.x /= mass;
        massc.y /= mass;
        if (area < Settings.EPSILON)
            continue;
        //Buoyancy
        Vec2 buoyancyForce = gravity.mul(-density * area);
        body.applyForce(buoyancyForce, massc);
        //Linear drag
        Vec2 dragForce = body
                .getLinearVelocityFromWorldPoint(areac).sub(
                        velocity);
        dragForce.mulLocal(-linearDrag * area);
        body.applyForce(dragForce, areac);
        //Angular drag
        //TODO: Something that makes more physical sense?
        body.applyTorque(-body.getInertia() / body.getMass() * area
                * body.getAngularVelocity() * angularDrag);

        /*Fixture fixture = mBody.getFixtureList();
        while (fixture != null)
        {
            switch (fixture.getShape().getType())
            {
                case CIRCLE:
                {
                    
                }
            }
        }*/
    //}
    public enum CauseOfDeath
    {
        eFire,
        eSpikes,
        eAcid,
        eImpact,
        eMundane,
        eEnemy,
        eCauseOfDeathMax
    }
    public void kill(CauseOfDeath _causeOfDeath, Object _killer)
    {
        if (mBody != null)
        {
            mBody.getWorld().destroyBody(mBody);
            mBody = null;
        }
    }
    protected float calculateArea()
    {
        float area = 0.0f;
        if (mBody == null)
        {
            return 0.0f;
        }
        for (Fixture fixture = getBody().getFixtureList(); fixture != null; fixture = fixture.getNext())
        {
            MassData massData = new MassData();
            fixture.getMassData(massData);
            
            area += massData.mass/fixture.getDensity();
        }
        return area;
    }
    protected void buoyancy()
    {
        float waterHeight = mWaterHeight;
        waterHeight = getBody().getPosition().y + 1 - waterHeight;
        getBody().applyLinearImpulse(new Vec2(0, -waterHeight*0.3f*mArea), getBody().getWorldCenter());
        if (waterHeight > 1.0f)
        {
            waterHeight = 1.0f;
        }
        mBody.applyLinearImpulse(mBody.getLinearVelocity().mul(-0.1f*waterHeight*mBody.getMass()), mBody.getWorldCenter());
        mBody.applyAngularImpulse(mBody.getAngularVelocity() * (-0.001f*waterHeight*mBody.getMass()));
    }

    public Body getBody()
    {
        return mBody;
    }

    public void setBody(Body mBody)
    {
        this.mBody = mBody;
        mArea = calculateArea();
    }
}
