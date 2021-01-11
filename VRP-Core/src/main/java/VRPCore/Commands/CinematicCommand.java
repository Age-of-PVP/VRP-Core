package VRPCore.Commands;

import VRPCore.Models.Vec5;
import VRPCore.VRPCore;
import VRPCore.Cinematics.Cinematic;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.owlike.genson.Genson;
import com.owlike.genson.GensonBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class CinematicCommand implements CommandExecutor {
    VRPCore core;
    public CinematicCommand(VRPCore _core){
        core = _core;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
        if(!(sender instanceof Player)){
            sender.sendMessage(ChatColor.RED + "Only Players can execute this command");
            return true;
        }
        if (args.length == 0){
            sender.sendMessage(ChatColor.RED + "Error: Invalid Syntax.");
            return true;
        }
        if(args[0].equalsIgnoreCase("play")){
            if(args.length == 2){
                core.CinematicManager.StartPlayerCinematic((Player)sender, args[1]);
            }else{
                sender.sendMessage(ChatColor.RED + "Error: Invalid Syntax. Usage: /cinematic play [name]");
                return true;
            }
        }else if(args[0].equalsIgnoreCase("create")) {
            if (args.length >= 3) {
                sender.sendMessage(ChatColor.GRAY + "Starting Creation of " + ChatColor.GREEN + args[1]);
                Cinematic tmp = new Cinematic();
                tmp.Name = args[1];
                tmp.KeyPoints = new ArrayList<Vec5>();
                tmp.Duration = Integer.parseInt(args[2]);
                tmp.LinkedCinematic = "";
                tmp.Titles = new String[0];
                tmp.Subtitles = new String[0];
                core.CinematicManager.Cinematics.add(tmp);
            }
        }else if(args[0].equalsIgnoreCase("points")) {
            if(args.length >= 3){
                if(args[1].equalsIgnoreCase("add")){
                    Cinematic cinematic = core.CinematicManager.get(args[2]);
                    cinematic.KeyPoints.add(new Vec5(((Player)sender).getLocation()));
                    sender.sendMessage(ChatColor.GREEN + "Added Location to " + ChatColor.YELLOW + cinematic.Name);
                }else if(args[1].equalsIgnoreCase("remove")){
                    if(args.length >= 4){
                        Cinematic cinematic = core.CinematicManager.get(args[2]);
                        cinematic.KeyPoints.remove(Integer.parseInt(args[3]));
                    }else {
                        sender.sendMessage(ChatColor.RED + "Error: Too few arguments");
                    }
                }else {
                    sender.sendMessage(ChatColor.RED + "Unknown Sub-Command");
                }
            }else{
                sender.sendMessage(ChatColor.RED + "Error. Usuage: /cinematic points add/remove [name] [index (only if removing)]");
            }
        }else if(args[0].equalsIgnoreCase("json")){
            Gson gson = new GsonBuilder()
                    .setPrettyPrinting()
                    .create();

            sender.sendMessage(gson.toJson(core.CinematicManager.Cinematics.toArray()));
        }else if(args[0].equalsIgnoreCase("delete")){
            if(args.length >= 2){
                Cinematic cinematic = core.CinematicManager.get(args[1]);
                if(cinematic != null) {
                    core.CinematicManager.Cinematics.remove(cinematic);
                    sender.sendMessage(ChatColor.GREEN + "Deleted cinematic " + ChatColor.YELLOW + cinematic.Name);
                }else{
                    sender.sendMessage(ChatColor.RED + "Failed to find cinematic named " + ChatColor.YELLOW + args[1]);
                }
            }else {
                sender.sendMessage(ChatColor.RED + "Error: Too few arguments");
            }
        }else if(args[0].equalsIgnoreCase("modify")){
            if(args.length >= 4){
                if(args[1].equalsIgnoreCase("link")){
                    core.CinematicManager.get(args[2]).LinkedCinematic = args[3];
                    sender.sendMessage(ChatColor.GREEN + "Changed linked Cinematic to " + ChatColor.YELLOW + args[3]);
                } else if(args[1].equalsIgnoreCase("duration")){
                    core.CinematicManager.get(args[2]).Duration = Integer.parseInt(args[3]);
                    sender.sendMessage(ChatColor.GREEN + "Set duration to " + args[3] + " seconds");
                }
                else {
                    sender.sendMessage(ChatColor.RED + "Unknown Sub-Command");
                }
            }else {
                sender.sendMessage(ChatColor.RED + "Error: Too few arguments");
            }
        }else if(args[0].equalsIgnoreCase("list")){
            sender.sendMessage(ChatColor.GREEN + "Available Cinematics:");
            for(int i = 0; i < core.CinematicManager.Cinematics.size(); i++){
                sender.sendMessage(ChatColor.YELLOW + " - " + core.CinematicManager.Cinematics.get(i).Name);
            }
        }
        else {
            sender.sendMessage(ChatColor.RED + "Unknown Sub-Command");
        }

        return true;
    }
}
