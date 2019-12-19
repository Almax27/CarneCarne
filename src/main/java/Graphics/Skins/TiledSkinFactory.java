/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Graphics.Skins;

import Level.CaveInSearcher;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author alasdair
 */
class TiledSkinFactory implements iSkinFactory
{

    public TiledSkinFactory()
    {
    }

    public iSkin useFactory(HashMap _params)
    {
        ArrayList<CaveInSearcher.TempTile> tiles = (ArrayList<CaveInSearcher.TempTile>)_params.get("tiles");
        int width = (Integer)_params.get("width");
        int height = (Integer)_params.get("height");
        return new TiledSkin(tiles, width, height);
    }
    
}
