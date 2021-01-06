package VRPCore.Commands;

import VRPCore.Models.EditorStickMode;
import VRPCore.VRPCore;
import de.tr7zw.nbtapi.NBTItem;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class EditorStick implements CommandExecutor {
    VRPCore plugin;

    public EditorStick(VRPCore main){
        this.plugin = main;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
        if(sender.hasPermission("vrpcore.es")){
            if(sender instanceof Player){
                Player player = (Player)sender;

                if(args.length == 0){ // No args, just give the stick
                    ItemStack IS = new ItemStack(Material.STICK, 1);
                    ItemMeta IM = IS.getItemMeta();

                    // Set Item Metadata
                    IM.setDisplayName(ChatColor.RED + "Editor Stick");
                    ArrayList<String> lore = new ArrayList<String>();
                    lore.add(ChatColor.ITALIC + "You posses a great power...");
                    IM.setLore(lore);
                    IS.setItemMeta(IM);

                    // Set EditorStick NBT
                    NBTItem nbti = new NBTItem(IS);
                    nbti.setBoolean("isEditorStick", true);
                    nbti.applyNBT(IS);

                    player.getInventory().addItem(IS);
                    player.sendMessage(ChatColor.GREEN + "Editor Stick" + ChatColor.YELLOW + " has been added to your inventory!");
                }else {
                    if(args.length >= 2){
                        if(args[0].equalsIgnoreCase("mode")){
                            switch(args[1].toLowerCase()){
                                case "light":
                                    if(player.hasPermission("vrpcore.es.light")){
                                        player.sendMessage(ChatColor.GREEN + "Switched to Light Editor");
                                        plugin.playerManager.GetPlayer(player.getUniqueId()).StickMode = EditorStickMode.LightEditor;
                                    }else{
                                        player.sendMessage(ChatColor.RED + "Missing Permission");
                                    }
                                    break;
                                case "waypoint":
                                    if(player.hasPermission("vrpcore.es.waypoint")){
                                        player.sendMessage(ChatColor.GREEN + "Switched to Waypoint Editor");
                                        plugin.playerManager.GetPlayer(player.getUniqueId()).StickMode = EditorStickMode.WaypointEditor;
                                    }else{
                                        player.sendMessage(ChatColor.RED + "Missing Permission");
                                    }
                                    break;
                                case "intersection":
                                    if(player.hasPermission("vrpcore.es.intersection")){
                                        player.sendMessage(ChatColor.GREEN + "Switched to Intersection Editor");
                                        plugin.playerManager.GetPlayer(player.getUniqueId()).StickMode = EditorStickMode.Intersection;
                                    }else{
                                        player.sendMessage(ChatColor.RED + "Missing Permission");
                                    }
                                    break;
                            }
                        }else if(args[0].equalsIgnoreCase("lights")){
                            if(player.hasPermission(("vrpcore.es.light"))) {
                                switch (args[1].toLowerCase()) {
                                    case "on":
                                        player.sendMessage("3");
                                        player.sendMessage("" + plugin.lightManager.DayNightLamps.size());
                                        plugin.lightManager.TurnOnLights();
                                        break;
                                    case "off":
                                        plugin.lightManager.TurnOffLights();
                                        break;
                                }
                            }else
                                player.sendMessage(ChatColor.RED + "Missing Permission");
                        }
                        System.out.println(args[0]);
                    }
                }
            }else{
                sender.sendMessage(ChatColor.RED + "This command is only executable by players.");
            }
        }else{
            sender.sendMessage(ChatColor.RED + " Missing permission!");
        }
        return true;
    }
}
