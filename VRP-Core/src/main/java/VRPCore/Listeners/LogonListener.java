package VRPCore.Listeners;

import VRPCore.Models.Job;
import VRPCore.Models.Player;
import VRPCore.VRPCore;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class LogonListener implements Listener {

    private VRPCore core;
    public LogonListener(VRPCore _core){
        this.core = _core;
    }

    @EventHandler
    public void OnPlayerJoin(PlayerJoinEvent event){
        Player player = core.playerManager.GetPlayer(event.getPlayer().getUniqueId());

        player.Job = new Job() {{
           weeklySalary = 1480;
           jobName = "Cop";
        }};

        if(player.firstLogon){
            // Play intro cinematic
        }

        player.firstLogon = false;

        event.getPlayer().sendMessage(ChatColor.RED + "Today's Date: " + core.DateManager.GetMonthOfYear() + " " + core.DateManager.GetDayOfMonth() + ", " + core.DateManager.GetYear() + " (A " + core.DateManager.GetDayOfWeek() + ")");
    }

}
