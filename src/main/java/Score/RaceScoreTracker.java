/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Score;

import Entities.PlayerEntity;

/**
 *
 * @author alasdair
 */

/// I say race score tracker, I'm just using this for everything now since its persistant
public class RaceScoreTracker extends ScoreTracker
{
    public RaceScoreTracker(int _GUIManager, PlayerEntity _entity)
    {
        super(_GUIManager, _entity);
    }
    
    private static Integer[] getScoreValues()
    {
        Integer[] scoreValues = new Integer[ScoreEvent.eScoreEventsMax.ordinal()];
        /*scoreValues[ScoreEvent.eKilledOpponent.ordinal()] = 100;
        scoreValues[ScoreEvent.eDied.ordinal()] = -100;
        scoreValues[ScoreEvent.eWonRace.ordinal()] = 500;
        scoreValues[ScoreEvent.eLostRace.ordinal()] = -200;
        scoreValues[ScoreEvent.eScoredGoal.ordinal()] = 500;
        scoreValues[ScoreEvent.eConceededGoal.ordinal()] = -500;*/
        scoreValues[ScoreEvent.eKilledOpponent.ordinal()] = 0;
        scoreValues[ScoreEvent.eDied.ordinal()] = -0;
        scoreValues[ScoreEvent.eWonRace.ordinal()] = 0;
        scoreValues[ScoreEvent.eLostRace.ordinal()] = -0;
        scoreValues[ScoreEvent.eScoredGoal.ordinal()] = 1;
        scoreValues[ScoreEvent.eConceededGoal.ordinal()] = -0;
        return scoreValues;
    }
    private static Integer[] mScoreValues = getScoreValues(); /// These are Integers instead of ints just so they can be null and therefore an error

    @Override
    public void scoreimpl(ScoreEvent _event)
    {
        mScore += mScoreValues[_event.ordinal()];
    }
    
}
