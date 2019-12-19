/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Events.AreaEvents;

import Entities.AIEntity;
import Entities.Football;
import Sound.SoundScape;
import Sound.sSound;
import States.Game.FootballMode.FootballMode;

/**
 *
 * @author alasdair
 */
public class GoalZone extends AreaEvent
{
    int mGoalNumber;
    FootballMode mGameMode;
    public GoalZone(int _x, int _y, int _x2, int _y2, int _goalNumber)
    {
        super(_x, _y, _x2, _y2);
        mGoalNumber = _goalNumber;
        mGameMode = null;
    }
    
    public void setGameMode(FootballMode _gameMode)
    {
        mGameMode = _gameMode;
    }
    @Override
    public void enter(AIEntity _entity)
    {
        //try
        {
            Football football = (Football)_entity;
            sSound.play(SoundScape.Sound.eGoalScore, 0);
            mGameMode.score(mGoalNumber, football);
        }
        //catch (ClassCastException e)
        {
            
        }
    }

    @Override
    public void leave(AIEntity _entity)
    {
    }
    
    public int getNumber()
    {
        return mGoalNumber;
    }
    
}
