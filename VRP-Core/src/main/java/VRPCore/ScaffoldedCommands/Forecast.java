package VRPCore.ScaffoldedCommands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.velocity.CommandHandler.models.CommandArg;
import org.velocity.CommandHandler.models.CommandBase;

import VRPCore.VRPCore;
import VRPCore.Enums.WeatherType;

import org.velocity.CommandHandler.annotations.*;

@org.velocity.CommandHandler.annotations.Command(Name="forecast")
public class Forecast implements CommandBase<VRPCore> {

	VRPCore core;
	
	//forecast ..
	@Override
	public void execute(CommandSender arg0, Command arg1, String label, String[] args) {
		
	}

	//forecast
	@Override
	public boolean executeFinal(CommandSender sender, Command cmd, String label, String[] args) {
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

        sender.sendMessage(ChatColor.GOLD + "Here is the 7 day forecast!");
        sender.sendMessage(SB.toString());
		return true;
	}
	
	@Argument(Name="set", Index=0, Permission = "vrpcore.forecast.set")
	public class ForecastOverride implements CommandArg {

		@Override
		public void execute(CommandSender sender, Command command, String label, String[] args, String arg) {
			if(args.length >= 3) {
				int day = Integer.parseInt(args[1]);
				String type = args[2];
				WeatherType wt = WeatherType.Sunny;
				switch(type.toLowerCase()) {
				case "sunny":
				case "sun":
				case "clear":
					wt = WeatherType.Sunny;
					break;
				case "rainy":
				case "rain":
					wt = WeatherType.Rainy;
					break;
				case "storm":
				case "stormy":
					wt = WeatherType.Stormy;
					break;
				}
				if(wt == WeatherType.Stormy) {
					sender.sendMessage(ChatColor.YELLOW + "WeatherType: " + ChatColor.ITALIC + "Stormy" + ChatColor.YELLOW + " is not implemented yet. Defaulting to: Rainy.");
					wt = WeatherType.Rainy;
				}
				core.WeatherManager.futureForecast.set(day, wt);
				sender.sendMessage(ChatColor.YELLOW + "Updated Forecast");
			}
			else {
				sender.sendMessage(ChatColor.RED + "Syntax Error: " + ChatColor.YELLOW + "/forecast set [day] [type]");
				sender.sendMessage(ChatColor.GRAY + "Possible Types: SUNNY, RAIN, STORM");
			}
		}

		@Override
		public boolean executeFinal(CommandSender sender, Command command, String label, String[] args, String arg) {
			return true;
		}
	}

	@Override
	public void init(VRPCore arg0) {
		core = arg0;
	}
	
}

// End of class