/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Events.AreaEvents;

import Graphics.sGraphicsManager;

/**
 *
 * @author alasdair
 */
public class PlayerSpawnZone extends CheckPointZone
{
    public PlayerSpawnZone(int _x, int _y, int _x2, int _y2, RaceStartZone _raceStart)
    {
        super(_x, _y, _x2, _y2, -1, _raceStart);
    }
    public void renderRaceState()
    {
        sGraphicsManager.drawString("Head to the race's starting point", 0f, 0);
    }
}
