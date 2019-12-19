/*
 * This class is for managing ParticleSys instances
 * It abstractsthe creation process and handles all updating, rending and culling
 */
package Graphics.Particles;

import Graphics.sGraphicsManager;
import World.sWorld;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.particles.ParticleIO;
import org.newdawn.slick.particles.ParticleSystem;

/**
 *
 * @author aaron
 */
public class sParticleManager {
    
    static HashMap<String, ParticleSystem> mLoadedSystems = new HashMap<String, ParticleSystem>();
    static ArrayList<ParticleSysBase> mInstancedSystems = new ArrayList<ParticleSysBase>();
    static HashMap<String, ArrayList<ParticleSystem>> mSystemPool = new HashMap<String, ArrayList<ParticleSystem>>();
    static int mVisibleSystemCount = 0;
    
    private sParticleManager()
    {
        
    }
    
    public static int getLoadedSystemCount() {return mLoadedSystems.size();}
    public static int getInstancedSystemCount() {return mInstancedSystems.size();}
    public static int getPoolCount() {return mSystemPool.size();}
    public static int getVisibleSystemCount() {return mVisibleSystemCount;}
    
    public static void warmUp()
    {
        //warm the pool here
        //FIXME: these are warmed due to icefirehit spawning too many
//        for(int i = 0; i < 20; i++)
//            createSystem("IceFireHit1", new Vec2(-1000,-1000),0);
//        for(int i = 0; i < 20; i++)
//            createSystem("IceFireHit2", new Vec2(-1000,-1000),0);
//        
//        for(int i = 0; i < 4; i++)
//            createSystem("Fire1", new Vec2(-1000,-1000),0);
//        for(int i = 0; i < 4; i++)
//            createSystem("Fire2", new Vec2(-1000,-1000),0);
    }
    /*
     * _ref:        classpath dir of system's .xml
     * _x           position in X (pixels)
     * _y           position in Y (pixels)
     * _lifeTime:   time until death (seconds)
     */
    public static ParticleSysBase createMovingSystem(String _ref, float _lifeTime, Body _body, Vec2 _localPosition, Vec2 _offset)
    {
        ParticleSysBase p = new BodyMovingParticleSys(createSystemImplementation(_ref, _body.getWorldPoint(_localPosition).add(_offset).mul(64), _lifeTime, true, false), _body, _localPosition, _offset);
        mInstancedSystems.add(p);
        return p;
    }
    public static ParticleSysBase createFirework(String _colour, Vec2 _position, Vec2 _velocity)
    {
        ParticleSysBase p = new Firework(createSystemImplementation("Firework" + _colour + "Rocket", _position, 1.0f, true, false), _position, _velocity, _colour);
        mInstancedSystems.add(p);
        return p;
    }
    public static ParticleSysBase createSystem(String _ref, Vec2 _position, float _lifeTime)
    {
        return createSystemImplementation(_ref, _position, _lifeTime, true, true);
    }
    private static ParticleSysBase createSystemImplementation(String _ref, Vec2 _position, float _lifeTime, boolean _dive, boolean _store)
    {     
        if(false == sGraphicsManager.getAllowParticles())
            return new NullParticleSys();
            //return null; /// FIXME put this back in for protoplay
        
        //if pooled object of same type exists use that
        ParticleSystem system = grabPooledSystem(_ref);
        if(system != null)
        {          
            system.reset();
        }
        //else if system already loaded but not pooled use that
        else if(mLoadedSystems.containsKey(_ref))
        {   
            system = getLoadedSystem(_ref);
        }
        else
        {//create new
            try
            {
                system = ParticleIO.loadConfiguredSystem("particleSystems/" + _ref + ".xml");
            } 
            catch (Throwable ex)
            {
                if (_dive)
                {
                    //load first system
                    ParticleSysBase sys = createSystemImplementation(_ref + "1", _position, _lifeTime, false, _store);
                    if(sys == null) return new NullParticleSys();
                    system = sys.getSystem();
                    
                    //load second
                    sys = createSystemImplementation(_ref + "2", _position, _lifeTime, false, _store);
                    if(sys == null) return new NullParticleSys();
                    ParticleSystem system2 = sys.getSystem();
                    
                    //combine them
                    ParticleSys p = new DoubleParticleSys(system,_ref + "1", system2, _ref + "2", _lifeTime);
                    if (_store)
                        mInstancedSystems.add(p);
                    return p;
                }
                else
                {
                    System.err.println(ex.getMessage());
                    //return new NullParticleSys();
                    return null; /// FIXME put this back in for protoplay
                }
            }
            putLoadedSystem(_ref, system);
        }
        system.setPosition(_position.x, _position.y);
        
        //Create wrapper for system and instance it
        ParticleSys p = new ParticleSys(system, _lifeTime, _ref);
        
        if (_dive && _store) //only instance systems when diving so we don't duplicate
        {
            mInstancedSystems.add(p);
        }
        
        return p;        
    }
    
    //returns null if not pooled
    private static ParticleSystem grabPooledSystem(String _ref)
    {
        if(false == mSystemPool.containsKey(_ref))
            return null;
        
        ArrayList<ParticleSystem> pool = mSystemPool.get(_ref);
        if(pool.isEmpty())
            return null;
        
        ParticleSystem sys = pool.get(0);
        pool.remove(0);
        return sys;
    }
    
    private static ParticleSystem getLoadedSystem(String _ref)
    {
        try 
        {
            return mLoadedSystems.get(_ref).duplicate();
        } 
        catch (SlickException ex) 
        {
            Logger.getLogger(sParticleManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    private static void putLoadedSystem(String _ref, ParticleSystem _system)
    {
        try 
        {
            mLoadedSystems.put(_ref, _system.duplicate());
        } 
        catch (SlickException ex) 
        {
            Logger.getLogger(sParticleManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    /*
     * _delta: time step in milliseconds
     */
    public static void update(int _delta)
    {
        ParticleSysBase sys = null;
        ArrayList<ParticleSysBase> instancedSystems = mInstancedSystems;
        mInstancedSystems = new ArrayList<ParticleSysBase>();
        for(Iterator<ParticleSysBase> i = instancedSystems.iterator(); i.hasNext();)
        {
            sys = i.next();
            if(false == sys.update(_delta))
            {
                sys.recycle();
                i.remove();
            }
        }
        for (ParticleSysBase particle: mInstancedSystems)
        {
            instancedSystems.add(particle);
        }
        mInstancedSystems = instancedSystems;
    }
    
    protected static void recycle(ParticleSystem _system, String _ref)
    {
        if(mSystemPool.containsKey(_ref))
        {
            mSystemPool.get(_ref).add(_system);
        }
        else
        {
            mSystemPool.put(_ref, new ArrayList<ParticleSystem>());
            mSystemPool.get(_ref).add(_system);
        }
    }
    
    /*
     * _x:      world translation in X (pixels)
     * _y:      world translation in Y (pixels)
     * _width:  width of screen (pixels)
     * _height: height of screen (pixels)
     * _border: culling border size to stop popping (pixels)
     */
    public static void render(int _x, int _y, int _width, int _height, int _border)
    {
        mVisibleSystemCount = 0;
        //assumes equal length arrays: mInstancedSystems & mInstancedSysPos
        for(ParticleSysBase particle : mInstancedSystems)
        {
            if(cull(particle))
            {
                mVisibleSystemCount++;
                //calc positions in screenspace
                Vec2 position = particle.getPosition();
                particle.render(position.x + _x, position.y + _y);
            }
        }
    }
    private static boolean cull(ParticleSysBase _p)
    {
        Rectangle clip = sGraphicsManager.getClip();
        Vec2 offset = sWorld.getPixelTranslation();
        Rectangle particle = new Rectangle(_p.getPosition().x + offset.x, _p.getPosition().y + offset.y, clip.getWidth(), clip.getHeight());
        if(clip.intersects(particle))
        {
            return true;
        }
        return false;
    }
    
    public static void cleanupInstancedSystems()
    {
        mInstancedSystems.clear();
    }
}
