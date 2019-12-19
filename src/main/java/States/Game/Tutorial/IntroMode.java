/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package States.Game.Tutorial;

import Entities.PlayerEntity;
import Entities.sEntityFactory;
import Events.AreaEvents.PlayerSpawnZone;
import Events.GenericEvent;
import Events.PlayerEndedTutorialEvent;
import Events.TutorialSpawnEvent;
import Events.iEvent;
import Events.iEventListener;
import Events.sEvents;
import Graphics.Camera.FreeCamera;
import Graphics.Particles.sParticleManager;
import Graphics.sGraphicsManager;
import Level.sLevel;
import States.Game.RaceMode.RaceMode;
import States.Game.iGameMode;
import World.sWorld;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Queue;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;

/**
 *
 * @author alasdair
 */
public class IntroMode implements iGameMode, iEventListener
{
    ArrayList<PlayerEntity> mPlayers;
    ArrayList<IntroSection> mSections;
    Body mGroundBody;
    int mEndedPlayers = 0;
    Queue<String> mNextLevelProgression;
    ArrayList<Boolean> mPlayersReallyEnded;
    public IntroMode(Queue<String> _nextLevelProgression)
    {
        mPlayersReallyEnded = new ArrayList<Boolean>();
        mPlayers = new ArrayList<PlayerEntity>();
        mSections = new ArrayList<IntroSection>();
        sEvents.subscribeToEvent("TutorialSpawnEvent", this);
        sEvents.subscribeToEvent("PlayerEndedTutorialEvent", this);
        mNextLevelProgression = new ArrayDeque<String>(_nextLevelProgression);
        sLevel.newLevel(mNextLevelProgression.remove());
    }
    public iGameMode update(Graphics _graphics, float _time)
    {
        boolean sectionsLeft = false;
        for (int i = 0; i < mSections.size(); i++)
        {
            if (mSections.get(i) != null)
            {
                mSections.set(i, mSections.get(i).update());
                if (mSections.get(i) != null)
                    sectionsLeft = true;
                mPlayers.get(i).mIntroSection = mSections.get(i);
            }
        }
        if (!sectionsLeft)
        {
            Vec2 s = sGraphicsManager.getTrueScreenDimensions();
            sWorld.switchCamera(new FreeCamera(new Rectangle(0,0,s.x, 0 + s.y)));
            cleanup();
            iGameMode raceMode = new RaceMode(mNextLevelProgression);
            sEvents.unsubscribeToEvent("TutorialSpawnEvent", this);
            return raceMode;
        }
        //sLevel.update();
        sWorld.update(_graphics, _time);
        sEvents.processEvents();
        return this;
    }
    public void render(Graphics _graphics)
    {
        sWorld.getCamera().render(_graphics);
        /*for (IntroSection section: mSections)
        {
            if (section != null)
                section.render();
        }*/
    }

    public boolean trigger(iEvent _event)
    {
        if (_event.getName().equals("TutorialSpawnEvent"))
        {
            TutorialSpawnEvent event = (TutorialSpawnEvent)_event;
            IntroSection introSection = new IntroStartSection(event.getPosition(), event.getPlayerNumber());
            mSections.add(introSection);
            HashMap parameters = new HashMap();
            parameters.put("position", event.getPosition());
            parameters.put("playerNumber", event.getPlayerNumber());
            parameters.put("checkPoint", new PlayerSpawnZone((int)event.getPosition().x, (int)event.getPosition().y, (int)event.getPosition().x+1, (int)event.getPosition().y+1, null));
            PlayerEntity player = (PlayerEntity)sEntityFactory.create("Player",parameters);
            player.mIntroSection = introSection;
            mPlayers.add(player);
            mPlayersReallyEnded.add(false);
        }
        else if (_event.getType().equals("PlayerEndedTutorialEvent"))
        {
            PlayerEndedTutorialEvent event = (PlayerEndedTutorialEvent)_event;
            int player = event.getPlayer();
            if (!mPlayersReallyEnded.get(player))
            {
                mPlayersReallyEnded.set(player, Boolean.TRUE);
                mEndedPlayers++;
                if (mEndedPlayers == mPlayers.size())
                {
                    sEvents.triggerEvent(new GenericEvent("AllPlayersTutorialEndedEvent"));
                    return false;
                }
            }
        }
        return true;
    }
    
    public void cleanup()
    {
        for(PlayerEntity player : mPlayers)
        {
            player.destroy();
            sWorld.destroyBody(player.getBody());
        }
        for (IntroSection section: mSections)
        {
            if(section != null)
                section.cleanup();
        }
        sEvents.unblockAllEvents();
        sEvents.unsubscribeToEvent("TutorialSpawnEvent", this);
        sEvents.unsubscribeToEvent("PlayerEndedTutorialEvent", this);
    }
}
