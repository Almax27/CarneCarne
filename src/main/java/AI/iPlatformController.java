/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package AI;

import Entities.CaveIn;

/**
 *
 * @author alasdair
 */
public abstract class iPlatformController
{
    CaveIn mEntity;
    public void setTileGrid(CaveIn _tileGrid)
    {
        mEntity = _tileGrid;
    }
    public abstract void update();
}
