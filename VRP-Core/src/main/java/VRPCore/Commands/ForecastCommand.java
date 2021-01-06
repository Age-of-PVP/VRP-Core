package VRPCore.Commands;

import VRPCore.Enums.WeatherType;
import VRPCore.VRPCore;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class ForecastCommand implements CommandExecutor {

    VRPCore core;
    public ForecastCommand(VRPCore _core){
        core = _core;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        StringBuilder SB = new StringBuilder();

        for(WeatherType type : core.WeatherManager.futureForecast){
            switch(type){
                case Rainy:
                    SB.append(ChatColor.YELLOW + "R ");
                    break;
                case Stormy:
                    SB.append(ChatColor.RED + "T ");
                    break;
                case Sunny:
                    SB.append(ChatColor.GREEN + "S ");
                    break;
            }
        }

        commandSender.sendMessage(ChatColor.GOLD + "Here is the 7 day forecast!");
        commandSender.sendMessage(SB.toString());

        return true;
    }
}
