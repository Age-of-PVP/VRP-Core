package VRPCore.Commands

import VRPCore.Enums.EditorStickMode
import VRPCore.VRPCore
import de.tr7zw.nbtapi.NBTItem
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta
import java.util.ArrayList

class EditorStick(var plugin: VRPCore) : CommandExecutor {
    override fun onCommand(sender: CommandSender, cmd: Command, label: String, args: Array<String>): Boolean {
        if (sender.hasPermission("vrpcore.es")) {
            if (sender is Player) {
                val player = sender
                if (args.size == 0) { // No args, just give the stick
                    val IS = ItemStack(Material.STICK, 1)
                    val IM = IS.itemMeta

                    // Set Item Metadata
                    IM!!.setDisplayName(ChatColor.RED.toString() + "Editor Stick")
                    val lore = ArrayList<String>()
                    lore.add(ChatColor.ITALIC.toString() + "You posses a great power...")
                    IM.lore = lore
                    IS.itemMeta = IM

                    // Set EditorStick NBT
                    val nbti = NBTItem(IS)
                    nbti.setBoolean("isEditorStick", true)
                    nbti.applyNBT(IS)
                    player.inventory.addItem(IS)
                    player.sendMessage(ChatColor.GREEN.toString() + "Editor Stick" + ChatColor.YELLOW + " has been added to your inventory!")
                } else {
                    if (args.size >= 2) {
                        if (args[0].equals("mode", ignoreCase = true)) {
                            when (args[1].toLowerCase()) {
                                "light" -> if (player.hasPermission("vrpcore.es.light")) {
                                    player.sendMessage(ChatColor.GREEN.toString() + "Switched to Light Editor")
                                    plugin.playerManager.GetPlayer(player.uniqueId).StickMode =
                                        EditorStickMode.LightEditor
                                } else {
                                    player.sendMessage(ChatColor.RED.toString() + "Missing Permission")
                                }
                                "waypoint" -> if (player.hasPermission("vrpcore.es.waypoint")) {
                                    player.sendMessage(ChatColor.GREEN.toString() + "Switched to Waypoint Editor")
                                    plugin.playerManager.GetPlayer(player.uniqueId).StickMode =
                                        EditorStickMode.WaypointEditor
                                } else {
                                    player.sendMessage(ChatColor.RED.toString() + "Missing Permission")
                                }
                                "intersection" -> if (player.hasPermission("vrpcore.es.intersection")) {
                                    player.sendMessage(ChatColor.GREEN.toString() + "Switched to Intersection Editor")
                                    plugin.playerManager.GetPlayer(player.uniqueId).StickMode =
                                        EditorStickMode.Intersection
                                } else {
                                    player.sendMessage(ChatColor.RED.toString() + "Missing Permission")
                                }
                            }
                        } else if (args[0].equals("lights", ignoreCase = true)) {
                            if (player.hasPermission("vrpcore.es.light")) {
                                player.sendMessage("This section has been disabled as LightManager is no longer present in VRPCore");
                                when (args[1].toLowerCase()) {
                                    "on" -> {
                                        player.sendMessage("3")
                                        //player.sendMessage("" + plugin.lightManager.DayNightLamps.size())
                                        //plugin.lightManager.TurnOnLights()
                                    }
                                    //"off" -> plugin.lightManager.TurnOffLights()
                                }
                            } else player.sendMessage(ChatColor.RED.toString() + "Missing Permission")
                        }
                        println(args[0])
                    }
                }
            } else {
                sender.sendMessage(ChatColor.RED.toString() + "This command is only executable by players.")
            }
        } else {
            sender.sendMessage(ChatColor.RED.toString() + " Missing permission!")
        }
        return true
    }
}