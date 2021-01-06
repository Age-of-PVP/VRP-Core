package VRPCore.Runnables;

import VRPCore.Economy.JobPaymentHandler;
import VRPCore.Listeners.LogonListener;
import VRPCore.Utils.DateManager;
import VRPCore.Utils.WeatherManager;
import VRPCore.VRPCore;

public class StartupRunnable implements Runnable{
    private VRPCore core;
    public StartupRunnable(VRPCore _core){
        this.core = _core;
    }

    @Override
    public void run() {
        core.DateManagerHandle = core.getServer().getScheduler().scheduleSyncRepeatingTask(core, core.DateManager, 0L, 24000);

        core.getServer().getScheduler().scheduleSyncRepeatingTask(core, new TimeCheckRunnable(core), 0, 20);

        core.WeatherManager = new WeatherManager(core);

        // Set time to day
        core.getServer().getWorld("OLD").setFullTime(16299768000L + 18000L);

        core.getServer().getPluginManager().registerEvents(new LogonListener(core), core);
    }
}
