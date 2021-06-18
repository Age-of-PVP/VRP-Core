package VRPCore.Placeholders;

import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

import VRPCore.VRPCore;
import VRPCore.Models.Player;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;

public class WantedLevelPlaceholder extends PlaceholderExpansion {

	VRPCore core;

	public WantedLevelPlaceholder(VRPCore core) {
		this.core = core;
	}
	
	@Override
	public @NotNull String getAuthor() {
		// TODO Auto-generated method stub
		return "wickedlizerd";
	}

	@Override
	public boolean persist() {
		return true;
	}
	
	@Override
	public boolean canRegister() {
		return true;
	}
	
	@Override
	public @NotNull String getIdentifier() {
		// TODO Auto-generated method stub
		return "vrpcore";
	}

	@Override
	public @NotNull String getVersion() {
		// TODO Auto-generated method stub
		return "1.0.0";
	}

	/**
     * This is the method called when a placeholder with our identifier 
     * is found and needs a value.
     * <br>We specify the value identifier in this method.
     * <br>Since version 2.9.1 can you use OfflinePlayers in your requests.
     *
     * @param  player
     *         A {@link org.bukkit.Player Player}.
     * @param  identifier
     *         A String containing the identifier/value.
     *
     * @return possibly-null String of the requested identifier.
     */
    @Override
    public String onPlaceholderRequest(org.bukkit.entity.Player player, String identifier){

        if(player == null){
            return "";
        }

         Player _player = core.playerManager.GetPlayer(player.getUniqueId());
        
        // %someplugin_placeholder1%
        if(identifier.equals("wantedLevel")){
            return Integer.toString(_player.getWantedLevel());
        }

        // %someplugin_placeholder2%
        if(identifier.equals("wantedString")){
            return _player.getWantedDisplay();
        }
        
        if(identifier.equals("job")) {
        	return _player.Job.jobName;
        }
        
        if(identifier.equals("salary")) {
        	return Double.toString(_player.Job.weeklySalary);
        }
 
        // We return null if an invalid placeholder (f.e. %someplugin_placeholder3%) 
        // was provided
        return null;
    }
}
