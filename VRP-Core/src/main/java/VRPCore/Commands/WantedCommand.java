package VRPCore.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import VRPCore.VRPCore;
import VRPCore.Models.Player;

public class WantedCommand implements CommandExecutor {
	
	private VRPCore core;
	
	public WantedCommand(VRPCore core) {
		this.core = core;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		org.bukkit.entity.Player player = (org.bukkit.entity.Player)sender;
		
		if(args.length >= 1) {
			if(args[0].equalsIgnoreCase("set")) {
				if(args.length >= 2){
					Player _player = core.playerManager.GetPlayer(player.getUniqueId());
					_player.setWantedLevel(Float.parseFloat(args[1]));
				}
			}
		}
		
		return true;
	}
}
