package VRPCore;

import VRPCore.Cinematics.Cinematic;
import VRPCore.Cinematics.CinematicManager;
import VRPCore.Commands.CinematicCommand;
import VRPCore.Commands.EditorStick;
import VRPCore.Commands.ForecastCommand;
import VRPCore.Economy.JobPaymentHandler;
import VRPCore.Events.EditorStickEvents;
import VRPCore.Runnables.StartupRunnable;
import VRPCore.Runnables.TimeCheckRunnable;
import VRPCore.TabCompleters.CinematicTabCompleter;
import VRPCore.Utils.DateManager;
import VRPCore.Utils.WeatherManager;
import me.filoghost.chestcommands.ChestCommands;
import me.filoghost.chestcommands.Permissions;
import me.filoghost.chestcommands.api.ChestCommandsAPI;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;

import java.util.Date;

public class VRPCore extends JavaPlugin {
    public PlayerManager playerManager = new PlayerManager();
    public LightManager lightManager = new LightManager();
    public CinematicManager CinematicManager = new CinematicManager(this);
    public DateManager DateManager = new DateManager(this);
    public WeatherManager WeatherManager;
    public JobPaymentHandler JobPaymentHandler = new JobPaymentHandler(this);

    public Economy economy;
    public Permission permissions;
    public Chat chat;

    public int DateManagerHandle;

    @Override
    public void onEnable(){
        if (!setupEconomy()) {
            this.getLogger().severe("Disabled due to no Vault dependency found!");
            this.getPluginLoader().disablePlugin(this);
            return;
        }
        setupChat();
        setupPermissions();

        registerCmds();

        BukkitScheduler scheduler = getServer().getScheduler();

        scheduler.scheduleSyncDelayedTask(this, new StartupRunnable(this), 40);
    }

    @Override
    public void onDisable(){
        //Fired when the server stops and disables all plugins
    }

    public boolean setupEconomy() {
        if (Bukkit.getPluginManager().getPlugin("Vault") == null)
            return false;

        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
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
        this.getCommand("forecast").setExecutor(new ForecastCommand(this));
    }

}