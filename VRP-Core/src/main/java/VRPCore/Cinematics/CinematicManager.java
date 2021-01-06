package VRPCore.Cinematics;

import VRPCore.Utils.IO;
import VRPCore.VRPCore;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.stream.Stream;

public class CinematicManager {

    private VRPCore core;
    public ArrayList<Cinematic> Cinematics;
    public Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private String CinematicsDirectory = "./plugins/VRPCore/Cinematics/";

    public CinematicManager(VRPCore _core){
        core = _core;
        LoadCinematics();
    }

    public boolean StartPlayerCinematic(Player player, String CinematicName){
        Cinematic cinematic = get(CinematicName);
        if(cinematic != null) {
            BukkitTask runnable = new MovementHandler(player, core.CinematicManager.get(CinematicName), core).runTaskTimer(core, 0, 1);
            return true;
        }
        player.sendMessage(ChatColor.RED +  "Failed to find Cinematic called " + ChatColor.YELLOW + CinematicName);
        return false;
    }

    public Cinematic get(String CinematicName){
        for(int i = 0; i < Cinematics.size(); i++){
            if(Cinematics.get(i).Name.equalsIgnoreCase(CinematicName)){
                return Cinematics.get(i);
            }
        }
        return null;
    }


    public void LoadCinematics(){
        File directory = new File(CinematicsDirectory);
        if (!directory.exists()){
            directory.mkdirs();
            Cinematics = new ArrayList<Cinematic>();
        }else{
            Cinematics = new ArrayList<>(Arrays.asList(
                    gson.fromJson(IO.ReadAllLines(CinematicsDirectory + "cinematics.json"), Cinematic[].class)
            ));
        }
    }
}
