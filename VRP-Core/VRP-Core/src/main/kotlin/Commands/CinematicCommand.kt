package VRPCore.Commands

import VRPCore.Cinematics.Cinematic
import VRPCore.Models.Vec5
import VRPCore.VRPCore
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.owlike.genson.Genson
import com.owlike.genson.GensonBuilder
import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import java.util.ArrayList

class CinematicCommand(var core: VRPCore) : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        if (sender !is Player) {
            sender.sendMessage(ChatColor.RED.toString() + "Only Players can execute this command")
            return true
        }
        if (args.size == 0) {
            sender.sendMessage(ChatColor.RED.toString() + "Error: Invalid Syntax.")
            return true
        }
        if (args[0].equals("play", ignoreCase = true)) {
            if (args.size == 2) {
                core.CinematicManager.StartPlayerCinematic(sender, args[1])
            } else {
                sender.sendMessage(ChatColor.RED.toString() + "Error: Invalid Syntax. Usage: /cinematic play [name]")
                return true
            }
        } else if (args[0].equals("create", ignoreCase = true)) {
            if (args.size >= 3) {
                sender.sendMessage(ChatColor.GRAY.toString() + "Starting Creation of " + ChatColor.GREEN + args[1])
                val tmp = Cinematic()
                tmp.Name = args[1]
                tmp.KeyPoints = ArrayList()
                tmp.Duration = args[2].toInt()
                tmp.LinkedCinematic = ""
                tmp.Titles = arrayOfNulls(0)
                tmp.Subtitles = arrayOfNulls(0)
                core.CinematicManager.Cinematics!!.add(tmp)
            }
        } else if (args[0].equals("points", ignoreCase = true)) {
            if (args.size >= 3) {
                if (args[1].equals("add", ignoreCase = true)) {
                    val cinematic = core.CinematicManager[args[2]]
                    cinematic!!.KeyPoints!!.add(Vec5(sender.location))
                    sender.sendMessage(ChatColor.GREEN.toString() + "Added Location to " + ChatColor.YELLOW + cinematic.Name)
                } else if (args[1].equals("remove", ignoreCase = true)) {
                    if (args.size >= 4) {
                        val cinematic = core.CinematicManager[args[2]]
                        cinematic!!.KeyPoints!!.removeAt(args[3].toInt())
                    } else {
                        sender.sendMessage(ChatColor.RED.toString() + "Error: Too few arguments")
                    }
                } else {
                    sender.sendMessage(ChatColor.RED.toString() + "Unknown Sub-Command")
                }
            } else {
                sender.sendMessage(ChatColor.RED.toString() + "Error. Usuage: /cinematic points add/remove [name] [index (only if removing)]")
            }
        } else if (args[0].equals("json", ignoreCase = true)) {
            val gson = GsonBuilder()
                .setPrettyPrinting()
                .create()
            sender.sendMessage(gson.toJson(core.CinematicManager.Cinematics!!.toTypedArray()))
        } else if (args[0].equals("delete", ignoreCase = true)) {
            if (args.size >= 2) {
                val cinematic = core.CinematicManager[args[1]]
                if (cinematic != null) {
                    core.CinematicManager.Cinematics!!.remove(cinematic)
                    sender.sendMessage(ChatColor.GREEN.toString() + "Deleted cinematic " + ChatColor.YELLOW + cinematic.Name)
                } else {
                    sender.sendMessage(ChatColor.RED.toString() + "Failed to find cinematic named " + ChatColor.YELLOW + args[1])
                }
            } else {
                sender.sendMessage(ChatColor.RED.toString() + "Error: Too few arguments")
            }
        } else if (args[0].equals("modify", ignoreCase = true)) {
            if (args.size >= 4) {
                if (args[1].equals("link", ignoreCase = true)) {
                    core.CinematicManager[args[2]]!!.LinkedCinematic = args[3]
                    sender.sendMessage(ChatColor.GREEN.toString() + "Changed linked Cinematic to " + ChatColor.YELLOW + args[3])
                } else if (args[1].equals("duration", ignoreCase = true)) {
                    core.CinematicManager[args[2]]!!.Duration = args[3].toInt()
                    sender.sendMessage(ChatColor.GREEN.toString() + "Set duration to " + args[3] + " seconds")
                } else {
                    sender.sendMessage(ChatColor.RED.toString() + "Unknown Sub-Command")
                }
            } else {
                sender.sendMessage(ChatColor.RED.toString() + "Error: Too few arguments")
            }
        } else if (args[0].equals("list", ignoreCase = true)) {
            sender.sendMessage(ChatColor.GREEN.toString() + "Available Cinematics:")
            for (i in core.CinematicManager.Cinematics!!.indices) {
                sender.sendMessage(ChatColor.YELLOW.toString() + " - " + core.CinematicManager.Cinematics!![i].Name)
            }
        } else {
            sender.sendMessage(ChatColor.RED.toString() + "Unknown Sub-Command")
        }
        return true
    }
}