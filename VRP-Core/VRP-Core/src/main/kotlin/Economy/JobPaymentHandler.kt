package VRPCore.Economy

import VRPCore.Interfaces.IDailyRunnable
import VRPCore.Models.Player
import VRPCore.VRPCore
import org.bukkit.ChatColor
import org.bukkit.OfflinePlayer
import org.bukkit.Sound
import org.bukkit.entity.Player
import java.util.ArrayList
import java.util.logging.Level

class JobPaymentHandler(private val core: VRPCore) : IDailyRunnable {
    override fun daily() {
        // If the current day is thursday
        if (core.DateManager.GetDayOfWeekAsInt() == 4) {
            val Players: ArrayList<Player> = core.playerManager.Players
            for (i in Players.indices) {
                val player = Players[i]
                val bukkitPlayer: OfflinePlayer = core.server.getOfflinePlayer(player.UUID)
                core.economy.depositPlayer(bukkitPlayer, player.Job.weeklySalary)
                val onlinePlayer: Player = core.server.getPlayer(player.UUID)
                if (onlinePlayer != null) {
                    onlinePlayer.sendMessage(ChatColor.GREEN.toString() + "Your weekly salary of " + ChatColor.YELLOW + "$" + player.Job.weeklySalary + " " + ChatColor.GREEN + " has been deposited!")
                    onlinePlayer.playSound(onlinePlayer.location, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 0.8f, 1.0f)
                }
                core.logger.log(Level.INFO, "Updated Balance of " + bukkitPlayer.name)
            }
        } else {
            val players = core.server.onlinePlayers
            for (player in players) {
                player.sendMessage(ChatColor.RED.toString() + "It is now " + core.DateManager.GetDayOfWeek() + ChatColor.YELLOW + " (" + player.world.time + " : " + player.world.fullTime + ")")
            }
        }
    }

    override val taskName: String
        get() = "JobPaymentHandler"

}