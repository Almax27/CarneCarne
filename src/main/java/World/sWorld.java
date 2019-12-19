/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package World;

import Entities.AIEntity;
import Entities.Carrot;
import World.PhysicsFactories.SeeSawBodyFactory;
import World.PhysicsFactories.SpatBlockBodyFactory;
import World.PhysicsFactories.FireParticleBody;
import World.PhysicsFactories.MovingPlatformBodyFactory;
import World.PhysicsFactories.CircleCharFactory;
import World.PhysicsFactories.TileArrayFactory;
import World.PhysicsFactories.NonEdibleTileFactory;
import World.PhysicsFactories.TileFactory;
import World.PhysicsFactories.iPhysicsFactory;
import World.PhysicsFactories.BoxCharFactory;
import Entities.Entity;
import Entities.PlayerEntity;
import Entities.SpatBlock;
import Events.PlayerSwingEvent;
import Events.TileDestroyedEvent;
import Events.iEvent;
import Events.iEventListener;
import Events.sEvents;
import Graphics.Camera.FreeCamera;
import Graphics.Camera.iCamera;
import Graphics.Particles.sParticleManager;
import Graphics.sGraphicsManager;
import Level.FakeTile;
import Level.RootTile;
import Level.Tile;
import Level.sLevel;
import Level.sLevel.TileType;
import Sound.MovingSoundAnchor;
import Sound.SoundScape;
import Sound.sSound;
import World.PhysicsFactories.BroccoliExplosionBody;
import World.PhysicsFactories.CarcassBodyFactory;
import World.PhysicsFactories.ExplosionBody;
import World.PhysicsFactories.GroundBodyFactory;
import World.PhysicsFactories.PlayerFactory;
import java.util.HashMap;
import org.jbox2d.callbacks.QueryCallback;
import org.jbox2d.callbacks.RayCastCallback;
import org.jbox2d.collision.AABB;
import org.jbox2d.collision.RayCastInput;
import org.jbox2d.collision.RayCastOutput;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.Fixture;
import org.jbox2d.dynamics.World;
import org.jbox2d.dynamics.joints.DistanceJoint;
import org.jbox2d.dynamics.joints.DistanceJointDef;
import org.jbox2d.dynamics.joints.Joint;
import org.jbox2d.dynamics.joints.PrismaticJointDef;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;
/**
 *
 * @author alasdair
 */
public class sWorld
{
    private static World mWorld;
    private static iCamera mCamera;
    private static HashMap<String,iPhysicsFactory> factories;
    private static ResizeListener mResizeListener;

    public static void destroy()
    {
        try
        {
            while (true)
            {
                mWorld.destroyBody(mWorld.getBodyList());
            }
        }
        catch (NullPointerException e)
        {
            
        }
        mWorld = new World(new Vec2(0,9.8f),true);   
        mWorld.setContactListener(new WorldContactListener());
        Vec2 s = sGraphicsManager.getTrueScreenDimensions();
        mCamera = new FreeCamera(new Rectangle(0,0,s.x, s.y));
    }

    public static boolean isLocked()
    {
        return mWorld.isLocked();
    }
    static class ResizeListener implements iEventListener
    {
        ResizeListener()
        {
            sEvents.subscribeToEvent("WindowResizeEvent", this);
        }
        public boolean trigger(iEvent _event)
        {
            if(_event.getType().equals("WindowResizeEvent"))
            {
                Vec2 s = sGraphicsManager.getTrueScreenDimensions();
                sWorld.resizeViewport(new Rectangle(0,0,s.x, s.y));
            }
            return true;
        }
    }


    public enum BodyCategories
    {
        ePlayer,
        eEnemy,
        eEdibleTiles,
        eNonEdibleTiles,
        eSpatTiles,
        eWater,
        eAcid,
        eIce,
        eGum,
        eTar,
        eSpikes,
        eFire,
        eEtherealEnemy,
        eCarcass,
        eSelfSensor,
        eZoomzoom,
        eBodyCategoriesMax;
    }
    private sWorld()
    {
           
    }
    public static void init()
    {
        Vec2 s = sGraphicsManager.getTrueScreenDimensions();
        mWorld = new World(new Vec2(0,9.8f),true);   
        mWorld.setContactListener(new WorldContactListener());
        factories = new HashMap<String, iPhysicsFactory>();
        factories.put("TileFactory", new TileFactory());
        factories.put("PlayerFactory", new PlayerFactory());
        factories.put("BoxCharFactory", new BoxCharFactory());
        factories.put("CircleCharFactory", new CircleCharFactory());
        factories.put("NonEdibleTileFactory", new NonEdibleTileFactory());
        factories.put("SpatBlockFactory", new SpatBlockBodyFactory());
        factories.put("TileArrayFactory", new TileArrayFactory());
        factories.put("CheckPointFactory", new CheckPointFactory());
        factories.put("SeeSawBodyFactory", new SeeSawBodyFactory());
        factories.put("MovingPlatformBodyFactory", new MovingPlatformBodyFactory());
        factories.put("FireParticleBody", new FireParticleBody());
        factories.put("ExplosionBody", new ExplosionBody());
        factories.put("BroccoliExplosionBody", new BroccoliExplosionBody());
        factories.put("GroundBody", new GroundBodyFactory());
        factories.put("CarcassBody", new CarcassBodyFactory());
        mCamera = new FreeCamera(new Rectangle(0,0,s.x, s.y));        
        resizeViewport(new Rectangle(0,0,s.x, s.y));    
        mResizeListener = new ResizeListener();
    }
    public static void queryForKicks()
    {
        AABBCallback callback = new AABBCallback();
        mWorld.queryAABB(callback, new AABB(new Vec2(25, 42), new Vec2(33, 48)));
    }
    
    public static Body useFactory(String _factory, HashMap _parameters)
    {
        if (mWorld.isLocked())
        {
            throw new UnsupportedOperationException("World is locked");
        }
        return factories.get(_factory).useFactory(_parameters, mWorld);
    }
    private static class AABBCallback implements QueryCallback
    {
        Body mBody;
        public AABBCallback()
        {
            mBody = null;
        }
        public boolean reportFixture(Fixture _fixture)
        {
            //if ((_fixture.m_filter.categoryBits & mCollisionMask) != 0 ||
            //        _fixture.getBody().getType().equals(BodyType.KINEMATIC))
            try
            {
                PlayerEntity entity = (PlayerEntity)_fixture.getBody().getUserData();
                entity.getCheckPoint();
                entity = null;
            }
            catch (Throwable e)
            {
                int a = 5 * 6;
            }
            if (!_fixture.getBody().getType().equals(BodyType.STATIC))
            {
                mBody = _fixture.getBody();
                return false;
            }
            else
            {
                try
                {
                    Carrot carrot = (Carrot)_fixture.getBody().getUserData();
                    carrot.mJoint.getJointAngle();
                    mBody = _fixture.getBody();
                    return false;
                }
                catch (Throwable e)
                {
                    return true;
                }
            }
        }
    }
    private static class TongueCallback implements RayCastCallback
    {
        Fixture fixture;
        Vec2 start, end;
        int collisionMask;
        public TongueCallback(Vec2 _start, Vec2 _end)
        {
            start = _start;
            end = _end;
            collisionMask = (1 << BodyCategories.eEdibleTiles.ordinal())|
                    (1 << BodyCategories.eNonEdibleTiles.ordinal())|
                    (1 << BodyCategories.eGum.ordinal())|
                    (1 << BodyCategories.eSpatTiles.ordinal())|
                    (1 << BodyCategories.ePlayer.ordinal())|
                    (1 << BodyCategories.eEnemy.ordinal())|
                    (1 << BodyCategories.eTar.ordinal())|
                    (1 << BodyCategories.eIce.ordinal());
        }
        public float reportFixture(Fixture _fixture, Vec2 _p1, Vec2 _p2, float _fraction)
        {
            if ((_fixture.m_filter.categoryBits & collisionMask) != 0)
            {
                RayCastInput input = new RayCastInput();
                input.p1.x = start.x;
                input.p1.y = start.y;
                input.p2.x = end.x;
                input.p2.y = end.y;
                RayCastOutput output = new RayCastOutput();
                //if (_fixture.raycast(output, input)) Is this neccessary?
                {
                    fixture = _fixture;
                    return _fraction;
                }
            }
            return 1.0f;
        }
        
        public Fixture getFixture()
        {
            return fixture;
        }
    }
    private static Fixture mLastHit;
    private static TongueAnchor mLastTongueAnchor;
    
    private static Vec2 mLastGumEat;
    public static Vec2 getLastGumEaten()
    {
        return mLastGumEat;
    }
    
    public static Tile eatTiles(Vec2 start, Vec2 end)
    {
        TongueCallback callback = new TongueCallback(start, end);
        mWorld.raycast(callback, start, end);
        
        if (callback.getFixture() == null)
        {
            return null;
        }
        
        mLastHit = callback.getFixture();
        if (callback.getFixture().m_filter.categoryBits == (1 << BodyCategories.eSpatTiles.ordinal()))
        {
            int id = ((SpatBlock)callback.getFixture().getBody().getUserData()).getRootId();
            RootTile tile = sLevel.getRootTile(id);
            mWorld.destroyBody(callback.getFixture().getBody());
            //return new Tile(id, tile, null, -1, -1);
        }
        if (callback.getFixture().m_filter.categoryBits == (1 << BodyCategories.ePlayer.ordinal()))
        {
            mLastHit = callback.getFixture();
            AIEntity entity = (AIEntity)mLastHit.getBody().getUserData();
            if (entity.isGrabbable())
            {
                mLastTongueAnchor = new BreakableTongueAnchor(entity);
                return new FakeTile(mLastHit.getBody(), TileType.eSwingable);
            }
            else
            {
                return new FakeTile(mLastHit.getBody(), TileType.eIndestructible);
            }
        }
        if (callback.getFixture().m_filter.categoryBits == (1 << BodyCategories.eEnemy.ordinal()))
        {
            mLastHit = callback.getFixture();
            mLastTongueAnchor = new BreakableTongueAnchor((AIEntity)mLastHit.getBody().getUserData());
            return new FakeTile(mLastHit.getBody(), TileType.eSwingable);
        }
        Tile tile = (Tile)callback.getFixture().getUserData();
        Tile ret = tile;
        if (tile != null)
        {
            TileType tileType = tile.getTileType();
            switch (tileType)
            {
                case eEdible:
                case eGum:
                {
                    mLastGumEat = tile.getWorldPosition();
                    /// continue;
                }
                case eMelonFlesh:
                case eChilli:
                {
                    sSound.playPositional(SoundScape.Sound.eTileEat, new MovingSoundAnchor(tile.getTileGrid().getBody(), tile.getLocalPosition()),tileType);
                    sParticleManager.createSystem(tile.getAnimationsName(RootTile.AnimationType.eNom) + "Nom", tile.getWorldPosition().mul(64.0f).add(new Vec2(32,32)), 1);
                    ret = tile.clone();
                    sEvents.triggerEvent(new TileDestroyedEvent((int)callback.getFixture().m_body.getPosition().x, (int)callback.getFixture().m_body.getPosition().y));
                    tile.destroyFixture();
                    //if (callback.getFixture().m_filter.categoryBits != (1 << BodyCategories.eSpatTiles.ordinal()))
                    //    tile.getTileGrid().destroyTile((int)callback.getFixture().m_body.getPosition().x, (int)callback.getFixture().m_body.getPosition().y);
                    break;
                }
                case eTar:
                case eIndestructible:
                case eIce:
                {
                    break;
                }
                case eSwingable:
                {
                    Tile hitTile = (Tile)mLastHit.getUserData();
                    mLastTongueAnchor = new MovingBodyTongueAnchor(hitTile.getFixture(), hitTile.getLocalPosition());
                    break;
                }
                default:
                    break;
            }
        }
        else
        {
        }
        return ret;
    }
    public static boolean smashTiles(Vec2 start, Vec2 end, int _playerNum)
    {
        TongueCallback callback = new TongueCallback(start, end);
        mWorld.raycast(callback, start, end);
        if (callback.getFixture() == null)
        {
            return false;
        }
        if (callback.getFixture().m_filter.categoryBits == (1 << BodyCategories.ePlayer.ordinal())||
                callback.getFixture().m_filter.categoryBits == (1 << BodyCategories.eEnemy.ordinal()))
        {
            Vec2 direction = end.sub(start);
            direction.normalize();
            ((AIEntity)callback.getFixture().getBody().getUserData()).stun(direction);
        }
        else
        {
            Tile tile = (Tile)callback.getFixture().getUserData();
            if (tile != null) /// FIXME might be able to remove this check
            {
                TileType tileType = tile.getTileType();
                switch (tileType)
                {
                    case eChilli:
                    case eGum:
                    case eMelonFlesh:
                    {
                        //sHud.addHudElement(_playerNum, "TooltipSpit", new Vec2(0,0), 120, true);
                        break;
                    }
                    case eSwingable:
                    {
                        //sHud.addHudElement(_playerNum, "TooltipSpit", new Vec2(0,0), 120, true);
                        /// Purposefully not breaking
                    }
                    case eEdible:
                    case eMelonSkin:
                    {
                        try
                        {
                            if (tile.damageTile(true))
                            {
                                sEvents.triggerEvent(new TileDestroyedEvent((int)callback.getFixture().m_body.getPosition().x, (int)callback.getFixture().m_body.getPosition().y));
                                tile.destroyFixture();
                            }
                        }
                        catch (Throwable e)
                        {
                        }
                        break;
                    }
                    case eIce:
                    case eEmpty:
                    case eIndestructible:
                    {
                        Vec2 direction = end.sub(start);
                        direction.normalize();
                        tile.getTileGrid().getBody().applyLinearImpulse(direction.mul(5.0f), tile.getWorldPosition());
                        break;
                    }
                }
            }
        }
        return true;
    }
    public static iCamera switchCamera(iCamera _newCamera)
    {
        iCamera camera = mCamera;
        mCamera = _newCamera;
        return camera;
    }
    public static Body searchAABB(AABB _aabb)
    {
        AABBCallback callback = new AABBCallback();
        mWorld.queryAABB(callback, _aabb);
        return callback.mBody;
    }
    static Body groundBody = null;
    public static Joint createMouseJoint(Vec2 _targetPosition, Body _body)
    { /// FIXME mouse joint doesn't work the same as in c++
        if (groundBody == null)
        {
            BodyDef bodyDef = new BodyDef();
            bodyDef.position = _targetPosition;
            groundBody = mWorld.createBody(bodyDef);
        }
        PrismaticJointDef def = new PrismaticJointDef();
        Vec2 axis = _body.getPosition().sub(_targetPosition);
        axis.normalize();
        def.initialize(_body, groundBody, _targetPosition, axis);
        def.enableMotor = true;
        def.motorSpeed = 10.0f;
        def.maxMotorForce = 5000.0f;
        Joint joint = mWorld.createJoint(def);
        return joint;
    }
    public static TongueAnchor getLastTongueHit(int _player)
    {
        sEvents.triggerEvent(new PlayerSwingEvent(_player));
        return mLastTongueAnchor;
    }
    public static DistanceJoint createTongueJoint(Body _body)
    {
        DistanceJointDef def = new DistanceJointDef();
        def.initialize(_body, mLastHit.m_body, _body.getPosition(), mLastTongueAnchor.getPosition());
        def.collideConnected = true;
        Vec2 direction = _body.getPosition().sub(mLastTongueAnchor.getPosition());
        def.length = direction.normalize();
        def.length = 0.0f;
        def.frequencyHz = 1.0f;
        def.dampingRatio = 1.0f;
        return (DistanceJoint)mWorld.createJoint(def);
    }
    /*public static Body createAreaEvent(int _x, int _y, int _x2, int _y2, AreaEvent _event)
    {
        BodyDef def = new BodyDef();
        Vec2 halfDims = new Vec2(((float)_x2-_x)*0.25f, ((float)_y2-_y)*0.25f);
        //def.position = new Vec2(((_x2-_x)*0.5f)+(_x+0.5f), ((_y2-_y)*0.5f)+(_y+0.5f));
        def.position = new Vec2(_x, _y).add(halfDims);
        def.userData = _event;
        FixtureDef fixture = new FixtureDef();
        fixture.filter.categoryBits = (1 << BodyCategories.eCheckPoint.ordinal());
        fixture.filter.maskBits = Integer.MAX_VALUE;
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(halfDims.x, halfDims.y);
        fixture.shape = shape;
        fixture.isSensor = true;
        Body body = mWorld.createBody(def);
        body.createFixture(fixture);
        return body;
    }*/
    public static void destroyBody(Body _body)
    {
        if (mWorld.isLocked())
        {
            throw new UnsupportedOperationException("World is locked");
        }
        mWorld.destroyBody(_body);
    }
    public static void destroyJoint(Joint _joint)
    {
        if (mWorld.isLocked())
        {
            throw new UnsupportedOperationException("World is locked");
        }
        mWorld.destroyJoint(_joint);
    }
    public static void destroyMouseJoint(Joint _joint)
    {
        if (mWorld.isLocked())
        {
            throw new UnsupportedOperationException("World is locked");
        }
        mWorld.destroyJoint(_joint);
        //mWorld.destroyBody(groundBody);
    }
    public static void addPlayer(Body _body)
    {
        mCamera = mCamera.addPlayer(_body);
    }
    public static void resizeViewport(Rectangle _viewPort)
    {
        mCamera.resize(_viewPort);
    }
    public static Vec2 translateToWorld(Vec2 _position)
    {
        return mCamera.translateToWorld(_position);
    }
    public static Vec2 translateToPhysics(Vec2 _position)
    {
        return mCamera.translateToPhysics(_position);
    }
    public static Vec2 getPixelTranslation()
    {
        return mCamera.getPixelTranslation();
    }
    /*public static Body createPlayerBody(AIEntity _entity, Vec2 _position)
    {
        Hashtable parameters = new Hashtable();
        parameters.put("position", _position);
        parameters.put("aIEntity", _entity);
        return useFactory("CharacterFactory",parameters);
    }*/
    private static int mBodyCount;
    public static void update(Graphics _graphics, float _dt)
    {   
        //try
        {            
            mWorld.step(_dt/1000.0f, 6, 4);

            mBodyCount = 0;
            Body body = mWorld.getBodyList();
            while (body != null)
            {
                mBodyCount++;
                Entity entity = (Entity)body.getUserData();
                if (entity != null)
                    entity.update();
                body = body.getNext();
            }
            mCamera.update(_graphics);
        }
    }

    public static iCamera getCamera()
    {
        return mCamera;
    }
    private static class RenderCallback implements QueryCallback
    {
        public boolean reportFixture(Fixture _fixture)
        {
            Entity entity = (Entity)_fixture.getBody().getUserData();
            if (entity != null)
            {
                mVisibleEntityCount++;
                entity.render();
            }
            return true;
        }
    }
    static int mVisibleEntityCount = 0;
    public static void render()
    {
        mVisibleEntityCount = 0;
        AABB aabb = new AABB(sWorld.translateToPhysics(new Vec2(0,0)), sWorld.translateToPhysics(sGraphicsManager.getScreenDimensions()));
        mWorld.queryAABB(new RenderCallback(), aabb);
        mWorld.drawDebugData();
        
    }
    public static int getBodyCount()
    {
        return mBodyCount;
    }
    public static int getVisibleEntityCount()
    {
        return mVisibleEntityCount;
    }
}






























