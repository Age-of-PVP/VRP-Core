package VRPCore.Cinematics;

import VRPCore.Interfaces.IStorable;
import VRPCore.Utils.IO;
import VRPCore.VRPCore;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.stream.Stream;

public class CinematicManager implements IStorable {

    private VRPCore core;
    public ArrayList<Cinematic> Cinematics;
    public Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private String CinematicsDirectory = "./plugins/VRPCore/Cinematics/cinematics.json";
    private String CinematicsDir = "./plugins/VRPCore/Cinematics/";

    public CinematicManager(VRPCore _core){
        core = _core;
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

    @Override
    public void save() {
        try {
            File jsonFile = new File(CinematicsDirectory);
            if(!jsonFile.exists()) {
                jsonFile.createNewFile();
            }
            gson.toJson(Cinematics, new FileWriter(jsonFile));
        } catch (IOException e) {
            core.getLogger().severe("Failed to export cinematic data to json");
            e.printStackTrace();
        }
    }

    @Override
    public void load() {
        try {
            File directory = new File(CinematicsDir);
            if (!directory.exists()){
                directory.mkdirs();
                Cinematics = new ArrayList<Cinematic>();
            }else{
                gson.fromJson(new FileReader(new File(CinematicsDirectory)), new TypeToken<ArrayList<Cinematic>>(){}.getType());
                //Cinematics = new ArrayList<>(Arrays.asList(
                //        gson.fromJson(IO.ReadAllLines(CinematicsDirectory + "cinematics.json"), Cinematic[].class)
                //));
            }
        } catch (Exception e) {
            core.getLogger().severe("Failed to load cinematic data from json");
            e.printStackTrace();
            Cinematics = new ArrayList<Cinematic>();
        }
    }
}
