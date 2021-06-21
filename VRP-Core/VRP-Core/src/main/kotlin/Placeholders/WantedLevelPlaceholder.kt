package VRPCore.Placeholders

import VRPCore.Models.Player
import VRPCore.VRPCore
import me.clip.placeholderapi.expansion.PlaceholderExpansion
import org.bukkit.entity.Player

class WantedLevelPlaceholder(var core: VRPCore) : PlaceholderExpansion() {
    // TODO Auto-generated method stub
    val author: String
        get() =// TODO Auto-generated method stub
            "wickedlizerd"

    fun persist(): Boolean {
        return true
    }

    fun canRegister(): Boolean {
        return true
    }

    // TODO Auto-generated method stub
    val identifier: String
        get() =// TODO Auto-generated method stub
            "vrpcore"

    // TODO Auto-generated method stub
    val version: String
        get() =// TODO Auto-generated method stub
            "1.0.0"

    /**
     * This is the method called when a placeholder with our identifier
     * is found and needs a value.
     * <br></br>We specify the value identifier in this method.
     * <br></br>Since version 2.9.1 can you use OfflinePlayers in your requests.
     *
     * @param  player
     * A [Player][org.bukkit.Player].
     * @param  identifier
     * A String containing the identifier/value.
     *
     * @return possibly-null String of the requested identifier.
     */
    fun onPlaceholderRequest(player: Player?, identifier: String): String? {
        if (player == null) {
            return ""
        }
        val _player: Player = core.playerManager.GetPlayer(player.uniqueId)

        // %someplugin_placeholder1%
        if (identifier == "wantedLevel") {
            return Integer.toString(_player.getWantedLevel())
        }

        // %someplugin_placeholder2%
        if (identifier == "wantedString") {
            return _player.getWantedDisplay()
        }
        if (identifier == "job") {
            return _player.Job.jobName
        }
        return if (identifier == "salary") {
            java.lang.Double.toString(_player.Job.weeklySalary)
        } else null

        // We return null if an invalid placeholder (f.e. %someplugin_placeholder3%)
        // was provided
    }
}