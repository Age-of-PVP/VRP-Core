package VRPCore;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.material.MaterialData;

import java.util.ArrayList;

public class LightManager {

    public ArrayList<Block> DayNightLamps = new ArrayList<Block>();

    public void TurnOnLights(){
        ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();

        for(int i = 0; i < DayNightLamps.size(); i++){
            Block b = DayNightLamps.get(i);
            String cmd = "/lamp loc " + b.getLocation().getBlockX() + " " + b.getLocation().getBlockY() + " " + b.getLocation().getBlockZ() + " on " + b.getWorld().getName();
            Bukkit.dispatchCommand(console, cmd);
        }
    }

    public void TurnOffLights() {
        ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();

        for(int i = 0; i < DayNightLamps.size(); i++){
            Block b = DayNightLamps.get(i);
            Bukkit.dispatchCommand(console,"/lamp loc " + b.getLocation().getBlockX() + " " + b.getLocation().getBlockY() + " " + b.getLocation().getBlockZ() + " off " + b.getWorld().getName());
        }
    }

}
