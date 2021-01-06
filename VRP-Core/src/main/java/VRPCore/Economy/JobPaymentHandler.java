package VRPCore.Economy;

import VRPCore.Models.Player;
import VRPCore.VRPCore;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Level;

public class JobPaymentHandler {
    private final VRPCore core;

    public JobPaymentHandler(VRPCore _core){

        this.core = _core;
        this.core.getLogger().warning("New Instance of JobPaymentHandler Created");
    }

    public void Daily() {
        // If the current day is thursday
        this.core.getLogger().warning("JobPaymentHandler Ran");

        if(core.DateManager.GetDayOfWeekAsInt() == 4) {
            ArrayList<Player> Players = core.playerManager.Players;

            for(int i = 0; i < Players.size(); i++){
                Player player = Players.get(i);

                OfflinePlayer bukkitPlayer = core.getServer().getOfflinePlayer(player.UUID);

                core.economy.depositPlayer(bukkitPlayer, player.Job.weeklySalary);
                org.bukkit.entity.Player onlinePlayer = core.getServer().getPlayer(player.UUID);
                if(onlinePlayer != null) {
                    onlinePlayer.sendMessage(ChatColor.GREEN + "Your weekly salary of " + ChatColor.YELLOW + "$" + player.Job.weeklySalary + " " + ChatColor.GREEN + " has been deposited!");
                    onlinePlayer.playSound(onlinePlayer.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 0.8F, 1.0F);
                }

                core.getLogger().log(Level.INFO, "Updated Balance of " + bukkitPlayer.getName());
            }
        }
        else {
            Collection<? extends org.bukkit.entity.Player> players = core.getServer().getOnlinePlayers();

            for(org.bukkit.entity.Player player : players){
                player.sendMessage(ChatColor.RED + "It is now " + core.DateManager.GetDayOfWeek() + ChatColor.YELLOW + " (" + player.getWorld().getTime() + " : " + player.getWorld().getFullTime() + ")");
            }
        }
    }
}
