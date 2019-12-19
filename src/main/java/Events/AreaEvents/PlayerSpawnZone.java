/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Events.AreaEvents;

/**
 *
 * @author alasdair
 */
public class PlayerSpawnZone extends CheckPointZone
{
    public PlayerSpawnZone(int _x, int _y, int _x2, int _y2, CheckPointZone _raceStart)
    {
        super(_x, _y, _x2, _y2, -1, _raceStart);
    }
    @Override
    public void renderRaceState(int _raceTimer)
    {
      //  sGraphicsManager.drawString("Head to the race's starting point", 0f, 0);
    }
    @Override
    public boolean incrementRaceTimer()
    {
        return false;
    }
}
