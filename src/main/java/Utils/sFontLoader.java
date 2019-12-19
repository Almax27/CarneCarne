/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Utils;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.newdawn.slick.Font;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;
import org.newdawn.slick.util.ResourceLoader;

/**
 *
 * @author A203945
 */
public class sFontLoader {
    private sFontLoader(){}
    private static Map<String, UnicodeFont> mFontList = new HashMap<String, UnicodeFont>();
    private static UnicodeFont mDefaultFont = null;
    
    public static UnicodeFont getDefaultFont(){return mDefaultFont;}
    public static void setDefaultFont(String _ref)
    {
        mDefaultFont = createFont(_ref, 100);
    }
    /*
     * loads truetype font from file, prepends dir, appends ".ttf"
     */
    public static UnicodeFont createFont(String _ref)
    {
        return createFont(_ref, 32, false, false);
    }
    public static UnicodeFont createFont(String _ref, int _size)
    {
        return createFont(_ref, _size, false, false);
    }
    public static UnicodeFont createFont(String _ref, int _size, boolean _bold, boolean _italic)
    {
        if(mFontList.containsKey(_ref))
        {
            return mFontList.get(_ref);
        }
        else
        {
            String dir = "ui/fonts/";
            UnicodeFont font = null;
            try
            {
                java.awt.Font jfont = java.awt.Font.createFont(java.awt.Font.TRUETYPE_FONT, ResourceLoader.getResourceAsStream(dir+_ref+".ttf"));
                font = new UnicodeFont(jfont, _size, _bold, _italic);
                
                //initialise font
                font.addAsciiGlyphs();   //Add Glyphs
                font.addGlyphs(400, 600); //Add Glyphs
                font.addNeheGlyphs();
                font.addGlyphs("_");
                font.getEffects().add(new ColorEffect(java.awt.Color.WHITE)); //Add Effects
                font.loadGlyphs();  //Load Glyphs
                        
                if(font != null)
                    mFontList.put(_ref, font);
            } 
            catch (Exception ex) {Logger.getLogger(sFontLoader.class.getName()).log(Level.SEVERE, null, ex);}
            
            return font;
        }
    }
    public static UnicodeFont scaleFont(UnicodeFont _font, float _scalar)
    {
        try {
            UnicodeFont out = new UnicodeFont(_font.getFont().deriveFont(_font.getFont().getSize2D() *_scalar));
            //initialise font
            out.addAsciiGlyphs();   //Add Glyphs
            out.addGlyphs(400, 600); //Add Glyphs
            out.addNeheGlyphs();
            out.addGlyphs("_");
            out.getEffects().add(new ColorEffect(java.awt.Color.WHITE)); //Add Effects
            out.loadGlyphs();  //Load Glyphs
            return out;
        } catch (SlickException ex) {
            Logger.getLogger(sFontLoader.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
}
