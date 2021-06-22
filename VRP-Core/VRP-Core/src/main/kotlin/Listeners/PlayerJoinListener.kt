package VRPCore.Listeners

import VRPCore.Models.Job
import VRPCore.Models.VRPlayer
import VRPCore.VRPCore
import org.bukkit.ChatColor
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent

class PlayerJoinListener(private val core: VRPCore) : Listener {
    @EventHandler
    fun OnPlayerJoin(event: PlayerJoinEvent) {
        val player: VRPlayer = core.playerManager.GetPlayer(event.player.uniqueId)
        player.Job = object : Job() {
            init {
                weeklySalary = 1480.0
                jobName = "Cop"
            }
        }
        if (player.firstLogon) {
            // Play intro cinematic
        }
        player.firstLogon = false
        event.player.sendMessage(ChatColor.RED.toString() + "Today's Date: " + core.DateManager.GetMonthOfYear() + " " + core.DateManager.GetDayOfMonth() + ", " + core.DateManager.GetYear() + " (A " + core.DateManager.GetDayOfWeek() + ")")
    }
}