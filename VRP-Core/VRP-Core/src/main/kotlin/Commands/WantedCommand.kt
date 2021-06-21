package VRPCore.Commands

import VRPCore.Models.Player
import VRPCore.Models.VRPlayer
import VRPCore.VRPCore
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class WantedCommand(private val core: VRPCore) : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        val player = sender as VRPlayer
        if (args.size >= 1) {
            if (args[0].equals("set", ignoreCase = true)) {
                if (args.size >= 2) {
                    val _player: Player = core.playerManager.GetPlayer(player.uniqueId)
                    _player.setWantedLevel(args[1].toFloat())
                }
            }
        }
        return true
    }
}