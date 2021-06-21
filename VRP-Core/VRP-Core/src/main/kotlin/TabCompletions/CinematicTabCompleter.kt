package VRPCore.TabCompleters

import VRPCore.VRPCore
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import java.util.ArrayList

class CinematicTabCompleter(var core: VRPCore) : TabCompleter {
    private fun CinematicNames(): ArrayList<String> {
        val cinematics = ArrayList<String>()
        for (i in core.CinematicManager.Cinematics!!.indices) {
            cinematics.add(core.CinematicManager.Cinematics!![i].Name!!)
        }
        return cinematics
    }

    override fun onTabComplete(
        commandSender: CommandSender,
        command: Command,
        s: String,
        args: Array<String>
    ): List<String>? {
        when (args.size) {
            0 -> {
                return listOf<String>("play", "create", "points", "json")
            }
            1 -> {
                run {
                    if (args[0].equals("play", ignoreCase = true)) {
                        return CinematicNames()
                    } else if (args[0].equals("points", ignoreCase = true)) {
                        return listOf("add", "remove");
                    }
                }
                run {
                    if (args[0].equals("points", ignoreCase = true)) {
                        return CinematicNames()
                    }
                }
            }
            2 -> {
                if (args[0].equals("points", ignoreCase = true)) {
                    return CinematicNames()
                }
            }
        }
        return null
    }
}