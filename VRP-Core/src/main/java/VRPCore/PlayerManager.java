package VRPCore;

import VRPCore.Interfaces.IDailyRunnable;
import VRPCore.Models.Player;

import java.util.ArrayList;
import java.util.UUID;

public class PlayerManager implements IDailyRunnable {
    public ArrayList<Player> Players = new ArrayList<Player>();

    private VRPCore core;

    private final boolean percentageDecay = true; // True = player loses % {STAT} every day, false = player loses flat {STAT} amount every day
    private final float ADRENALINE_DECAY_PER_DAY = 0.03f; // Player will lose 0.03% of their strength every day
    private final float STRENGTH_DECAY_PER_DAY = 0.04f;

    public PlayerManager(VRPCore _core) {
        this.core = _core;
    }

    public Player GetPlayer(final UUID ID){
        for(int i = 0; i < Players.size(); i++){
            if(Players.get(i).UUID.equals(ID))
                return Players.get(i);
        }
        Player p = new Player() {{UUID = ID;}};
        Players.add(p);
        return p;
    }

    public void SkillDecay(Player player){
        if(percentageDecay){
            player.Adrenaline *= 1 - ADRENALINE_DECAY_PER_DAY;
            player.Strength *= 1 - STRENGTH_DECAY_PER_DAY;
        }else{
            player.Adrenaline -= ADRENALINE_DECAY_PER_DAY;
            player.Strength -= STRENGTH_DECAY_PER_DAY;
        }
        player.clipStats();
    }

    @Override
    public void daily() {
        for(Player player : Players){
            SkillDecay(player);
        }
    }

    @Override
    public String getTaskName() {
        return "PlayerManager";
    }
}
