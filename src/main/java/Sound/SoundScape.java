/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Sound;

import Entities.Entity.CauseOfDeath;
import Level.sLevel.TileType;
import Sound.SoundPlayers.CauseOfDeathPlayer;
import Sound.SoundPlayers.NullPlayer;
import Sound.SoundPlayers.SimplePlayer;
import Sound.SoundPlayers.SinglePlayBlocker;
import Sound.SoundPlayers.TileTypePlayer;
import org.jbox2d.dynamics.Body;

/**
 *
 * @author alasdair
 */
public class SoundScape
{
    public enum Sound
    {
        ePlayerUnderwater,
        
        ePlayerJump, /// Parameter is sLevel.TileType (including eEmpty)
        ePlayerDeath, /// Parameter is Entity.CauseOfDeath
        eLaunchFireball,
        eFireHitWater,
        eFireHitObject, 
        eSpitBlock,
        eTileEat, /// Parameter is sLevel.TileType
        eTileSmash, /// Parameter is sLevel.TileType
        eTongueFire,
        eFireParticleBurn,
        eTarFireBurn,
        
        eFootballDeath, /// Parameter is Entity.CauseOfDeath
        eBroccoliExplode,
        
        eRaceWin,
        eGoalScore,
        eCheckPointHit,
        eSoundsMax
    }
    private static iSoundPlayer mSoundPlayers[] = initSoundPlayers();
    private static iSoundPlayer[] initSoundPlayers()
    {
        iSoundPlayer soundPlayers[] = new iSoundPlayer[Sound.eSoundsMax.ordinal()];
        iSoundPlayer nullPlayer = new NullPlayer();
        for (int i = 0; i < soundPlayers.length; i++)
        {
            soundPlayers[i] = nullPlayer;
        }
        soundPlayers[Sound.ePlayerUnderwater.ordinal()] = new SinglePlayBlocker(new SimplePlayer("underwater"));
        {
            TileTypePlayer playerJumpPlayer = new TileTypePlayer();
            soundPlayers[Sound.ePlayerJump.ordinal()] = playerJumpPlayer;
            
            playerJumpPlayer.createBlockingPlayer(TileType.eIce, new SimplePlayer("ice_jump")); /// Add things here
        }
        {
            CauseOfDeathPlayer playerDeathPlayer = new CauseOfDeathPlayer();
            soundPlayers[Sound.ePlayerDeath.ordinal()] = playerDeathPlayer;
            
            playerDeathPlayer.createBlockingPlayer(CauseOfDeath.eSpikes, new SimplePlayer("player_spike_death")); /// Add things here
        }
        soundPlayers[Sound.eLaunchFireball.ordinal()] = new SinglePlayBlocker(new SimplePlayer("launch_fireball"));
        soundPlayers[Sound.eFireHitWater.ordinal()] = new SinglePlayBlocker(new SimplePlayer("fire_hit_water"));
        soundPlayers[Sound.eFireHitObject.ordinal()] = new SinglePlayBlocker(new SimplePlayer("fire_hit_object"));
        {
            TileTypePlayer spitBlockPlayer = new TileTypePlayer();
            soundPlayers[Sound.eSpitBlock.ordinal()] = spitBlockPlayer;
            
            spitBlockPlayer.createBlockingPlayer(TileType.eEdible, new SimplePlayer("spit_meat")); /// Add things here
        }
        {
            TileTypePlayer eatBlockPlayer = new TileTypePlayer();
            soundPlayers[Sound.eTileEat.ordinal()] = eatBlockPlayer;
            
            eatBlockPlayer.createBlockingPlayer(TileType.eEdible, new SimplePlayer("eat_meat")); /// Add things here
        }
        {
            TileTypePlayer smashBlockPlayer = new TileTypePlayer();
            soundPlayers[Sound.eTileSmash.ordinal()] = smashBlockPlayer;
            
            smashBlockPlayer.createBlockingPlayer(TileType.eEdible, new SimplePlayer("smash_meat")); /// Add things here
        }
        soundPlayers[Sound.eTongueFire.ordinal()] = new SinglePlayBlocker(new SimplePlayer("tongue_fire"));
        soundPlayers[Sound.eFireParticleBurn.ordinal()] = new SinglePlayBlocker(new SimplePlayer("fire_particle"));
        soundPlayers[Sound.eTarFireBurn.ordinal()] = new SinglePlayBlocker(new SimplePlayer("burning_tar"));
        {
            CauseOfDeathPlayer footballDeathPlayer = new CauseOfDeathPlayer();
            soundPlayers[Sound.eFootballDeath.ordinal()] = footballDeathPlayer;
            
            footballDeathPlayer.createBlockingPlayer(CauseOfDeath.eSpikes, new SimplePlayer("football_spike_death")); /// Add things here
        }
        soundPlayers[Sound.eBroccoliExplode.ordinal()] = new SinglePlayBlocker(new SimplePlayer("broccoli_explode"));
        soundPlayers[Sound.eRaceWin.ordinal()] = new SinglePlayBlocker(new SimplePlayer("race_win"));
        soundPlayers[Sound.eGoalScore.ordinal()] = new SinglePlayBlocker(new SimplePlayer("goal_score"));
        soundPlayers[Sound.eCheckPointHit.ordinal()] = new SinglePlayBlocker(new SimplePlayer("checkpoint_hit"));
        
        sSound.loadSound("underwater", "assets/sfx/underwater_1.ogg");
        sSound.loadSound("eat_meat", "assets/sfx/meat_rip_1.ogg");
        sSound.loadSound("spit_meat", "assets/sfx/spit_1.ogg");
        //sSound.loadSound("underwater", "assets/sfx/underwater_1.ogg");
        
        //sSound.loadSound("ice_jump", "assets/sfx/ice_jump.ogg");
        return soundPlayers;
    }
    private Body mBody;
    
    SoundScape(Body _body)
    {
        mBody = _body;
    }
    public void play(Sound _sound)
    {
        play(_sound, null);
    }
    void playPositional(Sound _sound, iSoundAnchor _position, Object _parameter)
    {
        //mSoundPlayers[_sound.ordinal()].playPositional(_position, _parameter);
        play(_sound, _parameter);
    }
    public void stop(Sound _sound)
    {
        stop(_sound, null);
    }
    public void play(Sound _sound, Object _parameter)
    {
        mSoundPlayers[_sound.ordinal()].play(_parameter);
    }
    public void stop(Sound _sound, Object _parameter)
    {
        mSoundPlayers[_sound.ordinal()].stop(_parameter);
    }
}
