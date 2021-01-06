package VRPCore;

import VRPCore.Models.Player;

import java.util.ArrayList;
import java.util.UUID;

public class PlayerManager {
    public ArrayList<Player> Players = new ArrayList<Player>();

    public Player GetPlayer(final UUID ID){
        for(int i = 0; i < Players.size(); i++){
            if(Players.get(i).UUID.equals(ID))
                return Players.get(i);
        }
        Player p = new Player() {{UUID = ID;}};
        Players.add(p);
        return p;
    }
}
