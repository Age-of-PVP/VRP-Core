package VRPCore;

import VRPCore.Cinematics.Cinematic;
import VRPCore.Cinematics.CinematicManager;
import VRPCore.Commands.CinematicCommand;
import VRPCore.Commands.EditorStick;
import VRPCore.Commands.ForecastCommand;
import VRPCore.Commands.WantedCommand;
import VRPCore.Economy.JobPaymentHandler;
import VRPCore.Events.EditorStickEvents;
import VRPCore.Interfaces.IStorable;
import VRPCore.Models.Player;
import VRPCore.Placeholders.WantedLevelPlaceholder;
import VRPCore.Runnables.StartupRunnable;
import VRPCore.Runnables.TimeCheckRunnable;
import VRPCore.TabCompleters.CinematicTabCompleter;
import VRPCore.Utils.DateManager;
import VRPCore.Utils.WeatherManager;
import be.maximvdw.placeholderapi.PlaceholderAPI;
import be.maximvdw.placeholderapi.PlaceholderReplaceEvent;
import be.maximvdw.placeholderapi.PlaceholderReplacer;
import me.filoghost.chestcommands.ChestCommands;
import me.filoghost.chestcommands.Permissions;
import me.filoghost.chestcommands.api.ChestCommandsAPI;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.event.Listener;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;
import org.velocity.CommandHandler.CommandHandler;

import java.util.ArrayList;
import java.util.Date;

public class VRPCore extends JavaPlugin {
    public DateManager DateManager = new DateManager(this);
    public PlayerManager playerManager = new PlayerManager(this);
    public LightManager lightManager = new LightManager();
    public CinematicManager CinematicManager = new CinematicManager(this);
    public WeatherManager WeatherManager;
    public JobPaymentHandler JobPaymentHandler = new JobPaymentHandler(this);
    
    public CommandHandler commandHandler;

    public World world;

    public Economy economy;
    public Permission permissions;
    public Chat chat;

    public String TestString = "Hello World";
    
    public int DateManagerHandle;
    public int WeatherChangeHandle = -1;

    private ArrayList<IStorable> storables = new ArrayList<>();

    public static void main(String[] args) {
    	System.out.println("This is a minecraft plugin. Please put the jar file in /plugins/ in your server directory");
    }
    
    @Override
    public void onEnable(){
        if (!setupEconomy()) {
            this.getLogger().severe("Disabled due to no Vault dependency found!");
            this.getLogger().severe("[VRP] BYPASSING VAULT! IGNORE ABOVE MESSAGE. FIX THIS!");
            //this.getPluginLoader().disablePlugin(this);
            //return;
        }
        
        // Featherboard Placeholders
        if(Bukkit.getPluginManager().isPluginEnabled("MVdWPlaceholderAPI") && false){ // @TODO: <- REMOVE THE && FALSE
            PlaceholderAPI.registerPlaceholder(this, "wantedString", new PlaceholderReplacer() {
                @Override
                public String onPlaceholderReplace(PlaceholderReplaceEvent e) {
                    Player player = playerManager.GetPlayer(e.getPlayer().getUniqueId());

                    return player.getWantedDisplay();
                }
            });

            PlaceholderAPI.registerPlaceholder(this, "wantedLevel", new PlaceholderReplacer() {
                @Override
                public String onPlaceholderReplace(PlaceholderReplaceEvent e) {
                    Player player = playerManager.GetPlayer(e.getPlayer().getUniqueId());

                    return Integer.toString(player.getWantedLevel());
                }
            });
            
            PlaceholderAPI.registerPlaceholder(this, "job", new PlaceholderReplacer() {
				
				@Override
				public String onPlaceholderReplace(PlaceholderReplaceEvent e) {
					Player player = playerManager.GetPlayer(e.getPlayer().getUniqueId());
					return player.Job.jobName;
				}
			}); 
            
            PlaceholderAPI.registerPlaceholder(this, "salary", new PlaceholderReplacer() {
				
				@Override
				public String onPlaceholderReplace(PlaceholderReplaceEvent e) {
					Player player = playerManager.GetPlayer(e.getPlayer().getUniqueId());
					return Double.toString(player.Job.weeklySalary);
				}
			});
        }
        
        // TAB Placeholders
        if(Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
        	new WantedLevelPlaceholder(this).register();
        }
        
        setupChat();
        setupPermissions();

        commandHandler = new CommandHandler<VRPCore>(this, "VRPCore.ScaffoldedCommands");
        registerCmds();
        registerStorables();

        BukkitScheduler scheduler = getServer().getScheduler();

        scheduler.scheduleSyncDelayedTask(this, new StartupRunnable(this), 0);
    }

    private void registerStorables() {
        storables.add(CinematicManager);

        for(IStorable storable : storables) {
            getLogger().warning("Attempting to load state of " + storable.getClass().getSimpleName());
            storable.load();
        }
    }

    @Override
    public void onDisable(){
        //Fired when the server stops and disables all plugins
        for(IStorable storable : storables) {
            getLogger().warning("Attempting to save state of " + storable.getClass().getSimpleName());
            storable.save();
        }
    }

    public boolean setupEconomy() {
        if (Bukkit.getPluginManager().getPlugin("Vault") == null) {
        	this.getLogger().severe("Vault Plugin Not Found!");
            return false;
        }

        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
        	this.getLogger().severe("Failed to obtain instance of RegisteredServiceProvider<Economy> from Vault!");
            return false;
        }
        
        economy = rsp.getProvider();
        return economy != null;
    }

    private boolean setupChat() {
        RegisteredServiceProvider<Chat> rsp = getServer().getServicesManager().getRegistration(Chat.class);
        chat = rsp.getProvider();
        return chat != null;
    }

    private boolean setupPermissions() {
        RegisteredServiceProvider<Permission> rsp = getServer().getServicesManager().getRegistration(Permission.class);
        permissions = rsp.getProvider();
        return permissions != null;
    }

    public void registerCmds() {
        this.getCommand("/es").setExecutor(new EditorStick(this));
        getServer().getPluginManager().registerEvents(new EditorStickEvents(this), this);
        this.getCommand("cinematic").setTabCompleter(new CinematicTabCompleter(this));
        this.getCommand("cinematic").setExecutor(new CinematicCommand(this));
        //this.getCommand("forecast").setExecutor(new ForecastCommand(this));
        this.getCommand("wanted").setExecutor(new WantedCommand(this));
    }

    public void registerTasks() {
        DateManager.RegisterDailyTask(playerManager);
        DateManager.RegisterDailyTask(WeatherManager);
        DateManager.RegisterDailyTask(JobPaymentHandler);
    }
}