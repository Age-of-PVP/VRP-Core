package VRPCore.TabCompleters;

import VRPCore.VRPCore;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.List;

public class CinematicTabCompleter implements TabCompleter {
    VRPCore core;
    public CinematicTabCompleter(VRPCore _core){core = _core;}

    private ArrayList<String> CinematicNames(){
        ArrayList<String> cinematics = new ArrayList<>();
        for(int i = 0; i < core.CinematicManager.Cinematics.size(); i++){
            cinematics.add(core.CinematicManager.Cinematics.get(i).Name);
        }
        return cinematics;
    }


    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] args) {
        switch(args.length){
            case 0:{
                return new ArrayList<String>(){{add("play"); add("create"); add("points"); add("json");}};
            }
            case 1:{
                if(args[0].equalsIgnoreCase("play")){
                    return CinematicNames();
                }else if(args[0].equalsIgnoreCase("points")){
                    return new ArrayList<String>(){{add("add"); add("remove");}};
                }
            }
            case 2:{
                if(args[0].equalsIgnoreCase("points")){
                    return CinematicNames();
                }
            }
        }
        return null;
    }
}
