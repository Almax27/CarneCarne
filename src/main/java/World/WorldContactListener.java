/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package World;

import World.ContactListeners.NullListener;
import World.ContactListeners.WaterListener;
import World.ContactListeners.iListener;
import World.ContactListeners.HighImpulseListener;
import World.ContactListeners.GumListener;
import World.ContactListeners.FlipListener;
import World.ContactListeners.DeathListener;
import World.ContactListeners.TileBreakListener;
import Entities.Entity.CauseOfDeath;
import World.ContactListeners.SelfSensorListener;
import World.ContactListeners.SpikeListener;
import World.ContactListeners.StaticCarrotListener;
import World.ContactListeners.ZoomzoomListener;
import World.sWorld.BodyCategories;
import org.jbox2d.callbacks.ContactImpulse;
import org.jbox2d.callbacks.ContactListener;
import org.jbox2d.collision.Manifold;
import org.jbox2d.dynamics.contacts.Contact;

/**
 *
 * @author alasdair
 */
public class WorldContactListener implements ContactListener{

    iListener reactions[][];
    public WorldContactListener()
    {
        reactions = new iListener[BodyCategories.eBodyCategoriesMax.ordinal()][BodyCategories.eBodyCategoriesMax.ordinal()];
        iListener defaultListener = new NullListener();
        for (int i = 0; i < BodyCategories.eBodyCategoriesMax.ordinal(); i++)
        {
            for (int ii = 0; ii < BodyCategories.eBodyCategoriesMax.ordinal(); ii++)
            {
                reactions[i][ii] = defaultListener;
            }
        }
        iListener waterListener = new WaterListener();
        iListener highImpulse = new HighImpulseListener();
        iListener selfSensorListener = new SelfSensorListener();
        iListener zoomzoomListener = new ZoomzoomListener();
        for (int i = 0; i < BodyCategories.eBodyCategoriesMax.ordinal(); i++)
        {
            set(BodyCategories.eWater.ordinal(),i,waterListener);
            set(BodyCategories.eCarcass.ordinal(),i,highImpulse);
            set(BodyCategories.eSelfSensor.ordinal(),i,selfSensorListener);
            set(BodyCategories.eZoomzoom.ordinal(),i,zoomzoomListener);
        }
        iListener gumListener = new GumListener();
        iListener fireListener = new DeathListener(CauseOfDeath.eFire);
        iListener spikeListener = new SpikeListener();
        iListener acidListener = new DeathListener(CauseOfDeath.eAcid);
        iListener spikeTileListener = new TileBreakListener(1 << BodyCategories.eSpikes.ordinal());
        iListener acidTileListener = new TileBreakListener(1 << BodyCategories.eAcid.ordinal());
        
        set(BodyCategories.eGum.ordinal(),BodyCategories.eEdibleTiles.ordinal(),gumListener);
        set(BodyCategories.eGum.ordinal(),BodyCategories.eNonEdibleTiles.ordinal(),gumListener);
        set(BodyCategories.ePlayer.ordinal(),BodyCategories.eSpikes.ordinal(),spikeListener);
        set(BodyCategories.ePlayer.ordinal(),BodyCategories.eFire.ordinal(),fireListener);
        set(BodyCategories.eWater.ordinal(),BodyCategories.ePlayer.ordinal(),waterListener);
        set(BodyCategories.eAcid.ordinal(),BodyCategories.ePlayer.ordinal(),acidListener);
        set(BodyCategories.eEnemy.ordinal(),BodyCategories.eSpikes.ordinal(),spikeListener);
        //set(BodyCategories.eCarcass.ordinal(),BodyCategories.eSpikes.ordinal(),spikeListener);
        set(BodyCategories.eEnemy.ordinal(),BodyCategories.eFire.ordinal(),fireListener);
        set(BodyCategories.eAcid.ordinal(),BodyCategories.eEnemy.ordinal(),acidListener);
        set(BodyCategories.ePlayer.ordinal(),BodyCategories.eEnemy.ordinal(),new StaticCarrotListener());
        set(BodyCategories.eAcid.ordinal(),BodyCategories.eCarcass.ordinal(),waterListener);
        set(BodyCategories.eCarcass.ordinal(),BodyCategories.eFire.ordinal(),fireListener);
        //set(BodyCategories.eCheckPoint.ordinal(), BodyCategories.ePlayer.ordinal(), new CheckPointListener());
        
        set(BodyCategories.eEdibleTiles.ordinal(),BodyCategories.eSpikes.ordinal(),spikeTileListener);
        set(BodyCategories.eEdibleTiles.ordinal(),BodyCategories.eAcid.ordinal(),acidTileListener);
    }
    private void set(int _x, int _y, iListener _reaction)
    {
        reactions[_x][_y] = _reaction;
        reactions[_y][_x] = new FlipListener(_reaction);
        reactions[_y][_x] = _reaction;
    }
    private int getIndex(int bitMask)
    {
        int count = 0;
        while ((bitMask >> count) != 1)
        {
            count++;
        }
        return count;
    }
    public void beginContact(Contact _contact)
    {
        int categories[] = new int[2];
        categories[0] = getIndex(_contact.m_fixtureA.m_filter.categoryBits);
        categories[1] = getIndex(_contact.m_fixtureB.m_filter.categoryBits);
        reactions[categories[0]][categories[1]].beginContact(_contact);
    }

    public void endContact(Contact _contact)
    {
        int categories[] = new int[2];
        categories[0] = getIndex(_contact.m_fixtureA.m_filter.categoryBits);
        categories[1] = getIndex(_contact.m_fixtureB.m_filter.categoryBits);
        reactions[categories[0]][categories[1]].endContact(_contact);
    }

    public void preSolve(Contact _contact, Manifold _manifold)
    {
        int categories[] = new int[2];
        categories[0] = getIndex(_contact.m_fixtureA.m_filter.categoryBits);
        categories[1] = getIndex(_contact.m_fixtureB.m_filter.categoryBits);
        reactions[categories[0]][categories[1]].preSolve(_contact, _manifold);
    }

    public void postSolve(Contact _contact, ContactImpulse _impulse)
    {
        int categories[] = new int[2];
        categories[0] = getIndex(_contact.m_fixtureA.m_filter.categoryBits);
        categories[1] = getIndex(_contact.m_fixtureB.m_filter.categoryBits);
        reactions[categories[0]][categories[1]].postSolve(_contact, _impulse);
    }
    
}
