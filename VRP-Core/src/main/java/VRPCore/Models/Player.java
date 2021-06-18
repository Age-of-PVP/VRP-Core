package VRPCore.Models;

import org.bukkit.ChatColor;

public class Player {
    public String Username;

    public java.util.UUID UUID;

    public EditorStickMode StickMode = EditorStickMode.None;

    public Job Job;

    public boolean firstLogon = true;

    private float wantedLevel = 0;

    // <Stats>

    private final float ADRENALINE_MAX = 100f;
    private final float STRENGTH_MAX = 100f;

    public float Adrenaline = 0.0f;
    public float Strength = 0.0f;

    public void clipStats() {
        if(Adrenaline > ADRENALINE_MAX)
            Adrenaline = ADRENALINE_MAX;
        if(Strength > STRENGTH_MAX)
            Strength = STRENGTH_MAX;
    }

    // </Stats>

    public int getWantedLevel() {
        if(wantedLevel > 0.5)
            return (int)Math.ceil(wantedLevel);
        else return 0;
    }

    public String getWantedDisplay() {
        if(wantedLevel >= 0.5)
            if(wantedLevel >= 9.0)
                return ChatColor.RED + "MOST WANTED";
            else return ChatColor.GOLD + "" + ChatColor.ITALIC + "★ x" + ChatColor.BOLD + Integer.toString(getWantedLevel());
        else return "Good Citizen";
    }
    
    public void setWantedLevel(float x) {
    	if(x >= 10)
    		wantedLevel = 10;
    	else if(x <= 0)
    		wantedLevel = 0;
    	else
    		wantedLevel = x;
    }
}
