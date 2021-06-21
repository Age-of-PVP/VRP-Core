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
import VRPCore.Models.VRPlayer
import VRPCore.Placeholders.WantedLevelPlaceholder;
import VRPCore.Runnables.StartupRunnable;
import VRPCore.Runnables.TimeCheckRunnable;
import VRPCore.TabCompleters.CinematicTabCompleter;
import VRPCore.Utils.DateManager;
import VRPCore.Utils.WeatherManager;
import be.maximvdw.placeholderapi.PlaceholderAPI;
import be.maximvdw.placeholderapi.PlaceholderReplaceEvent;
import be.maximvdw.placeholderapi.PlaceholderReplacer;
import me.clip.placeholderapi.PlaceholderAPI
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

class VRPCore : JavaPlugin() {
    var DateManager: DateManager = DateManager(this)
    var playerManager: PlayerManager = PlayerManager(this)
    var CinematicManager: CinematicManager = CinematicManager(this)
    var WeatherManager: WeatherManager? = null
    var JobPaymentHandler: JobPaymentHandler = JobPaymentHandler(this)
    var commandHandler: CommandHandler? = null
    var world: World? = null
    var economy: Economy? = null
    var permissions: Permission? = null
    var chat: Chat? = null
    var TestString = "Hello World"
    var DateManagerHandle = 0
    var WeatherChangeHandle = -1
    private val storables: ArrayList<IStorable> = ArrayList<IStorable>()

    override fun onEnable() {
        if (!setupEconomy()) {
            this.getLogger().severe("Disabled due to no Vault dependency found!")
            this.getLogger().severe("[VRP] BYPASSING VAULT! IGNORE ABOVE MESSAGE. FIX THIS!")
            //this.getPluginLoader().disablePlugin(this);
            //return;
        }

        // Featherboard Placeholders
        if (Bukkit.getPluginManager().isPluginEnabled("MVdWPlaceholderAPI") && false) { // @TODO: <- REMOVE THE && FALSE
            PlaceholderAPI.registerPlaceholder(this, "wantedString", object : PlaceholderReplacer() {
                fun onPlaceholderReplace(e: PlaceholderReplaceEvent): String {
                    val player: VRPlayer = playerManager.GetPlayer(e.getPlayer().getUniqueId())
                    return player.getWantedDisplay()
                }
            })
            PlaceholderAPI.registerPlaceholder(this, "wantedLevel", object : PlaceholderReplacer() {
                fun onPlaceholderReplace(e: PlaceholderReplaceEvent): String {
                    val player: VRPlayer = playerManager.GetPlayer(e.getPlayer().getUniqueId())
                    return Integer.toString(player.getWantedLevel())
                }
            })
            PlaceholderAPI.registerPlaceholder(this, "job", object : PlaceholderReplacer() {
                fun onPlaceholderReplace(e: PlaceholderReplaceEvent): String {
                    val player: VRPlayer = playerManager.GetPlayer(e.getPlayer().getUniqueId())
                    return player.Job.jobName
                }
            })
            PlaceholderAPI.registerPlaceholder(this, "salary", object : PlaceholderReplacer() {
                fun onPlaceholderReplace(e: PlaceholderReplaceEvent): String {
                    val player: VRPlayer = playerManager.GetPlayer(e.getPlayer().getUniqueId())
                    return java.lang.Double.toString(player.Job.weeklySalary)
                }
            })
        }

        // @TODO: REMOVE COMMENTS
        // TAB Placeholders
        //if(Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
        //	new WantedLevelPlaceholder(this).register();
        //}
        if (!setupChat()) getLogger().severe("Failed to setup chat")
        if (!setupPermissions()) getLogger().severe("Failed to permissions chat")
        commandHandler = CommandHandler<VRPCore>(this, "VRPCore.ScaffoldedCommands")
        registerCmds()
        registerStorables()
        val scheduler: BukkitScheduler = getServer().getScheduler()
        scheduler.scheduleSyncDelayedTask(this, StartupRunnable(this), 0)
    }

    private fun registerStorables() {
        storables.add(CinematicManager)
        for (storable in storables) {
            getLogger().warning("Attempting to load state of " + storable::class.simpleName)
            storable.load()
        }
    }

    override fun onDisable() {
        //Fired when the server stops and disables all plugins
        for (storable in storables) {
            getLogger().warning("Attempting to save state of " + storable::class.simpleName)
            storable.save()
        }
    }

    fun setupEconomy(): Boolean {
        if (Bukkit.getPluginManager().getPlugin("Vault") == null) {
            this.getLogger().severe("Vault Plugin Not Found!")
            return false
        }
        val rsp: RegisteredServiceProvider<Economy> =
            getServer().getServicesManager().getRegistration(Economy::class.java) as RegisteredServiceProvider<Economy>
        if (rsp == null) {
            this.getLogger().severe("Failed to obtain instance of RegisteredServiceProvider<Economy> from Vault!")
            return false
        }
        economy = rsp.getProvider()
        return economy != null
    }

    private fun setupChat(): Boolean {
        val rsp: RegisteredServiceProvider<Chat> = getServer().getServicesManager().getRegistration(Chat::class.java)
            ?: return false
        chat = rsp.getProvider()
        return chat != null
    }

    private fun setupPermissions(): Boolean {
        val rsp: RegisteredServiceProvider<Permission> =
            getServer().getServicesManager().getRegistration(Permission::class.java)
                ?: return false
        permissions = rsp.getProvider()
        return permissions != null
    }

    fun registerCmds() {
        this.getCommand("/es")!!.setExecutor(EditorStick(this))
        getServer().getPluginManager().registerEvents(EditorStickEvents(this), this)
        this.getCommand("cinematic")!!.setTabCompleter(CinematicTabCompleter(this))
        this.getCommand("cinematic")!!.setExecutor(CinematicCommand(this))
        //this.getCommand("forecast").setExecutor(new ForecastCommand(this));
        this.getCommand("wanted")!!.setExecutor(WantedCommand(this))
    }

    fun registerTasks() {
        DateManager.RegisterDailyTask(playerManager)
        DateManager.RegisterDailyTask(WeatherManager!!)
        DateManager.RegisterDailyTask(JobPaymentHandler)
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            println("This is a minecraft plugin. Please put the jar file in /plugins/ in your server directory")
        }
    }
}