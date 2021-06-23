package VRPCore.Placeholders

import VRPCore.Models.VRPlayer
import VRPCore.VRPCore
import me.clip.placeholderapi.expansion.PlaceholderExpansion
import org.bukkit.entity.Player

class WantedLevelPlaceholder(var core: VRPCore) : PlaceholderExpansion() {

    override fun persist(): Boolean {
        return true
    }

    override fun canRegister(): Boolean {
        return true
    }

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
    override fun onPlaceholderRequest(player: Player?, identifier: String): String? {
        if (player == null) {
            return "";
        }
        val _player: VRPlayer = core.playerManager.GetPlayer(player.uniqueId);

        // %someplugin_placeholder1%
        if (identifier == "wantedLevel") {
            return Integer.toString(_player.getWantedLevel());
        }

        // %someplugin_placeholder2%
        if (identifier == "wantedString") {
            return _player.wantedDisplay;
        }
        if (identifier == "job") {
            return _player.Job!!.jobName;
        }
        return if (identifier == "salary") {
            _player.Job!!.weeklySalary.toString();
        } else null

        // We return null if an invalid placeholder (f.e. %someplugin_placeholder3%)
        // was provided
    }

    override fun getIdentifier(): String {
        return "VRPCore";
    }

    override fun getAuthor(): String {
        return "Wickedlizerd";
    }

    override fun getVersion(): String {
        return "1.0.0";
    }
}