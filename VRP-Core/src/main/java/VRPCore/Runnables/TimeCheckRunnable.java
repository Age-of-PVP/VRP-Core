package VRPCore.Runnables;

import VRPCore.Economy.JobPaymentHandler;
import VRPCore.VRPCore;
import org.bukkit.World;

public class TimeCheckRunnable implements Runnable {

    private long lastTime;
    private VRPCore core;
    private World world;
    private boolean firstRun = true;

    public TimeCheckRunnable(VRPCore _core){
        this.core = _core;
        this.world = core.getServer().getWorld("OLD");

        lastTime = world.getFullTime();
    }

    @Override
    public void run() {
        if(world.getFullTime() - 20L != lastTime && !firstRun){
            core.getLogger().warning("ERROR: Possible plugin & day/night cycle de-sync! Attempting to fix! {lastTime:" + lastTime + "} {fullWorldTime:" + world.getFullTime() + "} {worldTime: " + world.getTime() + "}");
            core.getServer().getScheduler().cancelTask(core.JobPaymentHandle);

            long timeToDelay = 18000L - world.getTime();
            core.getLogger().warning("Restarting JobPaymentHandler in " + timeToDelay + " ticks.");
            core.JobPaymentHandle = core.getServer().getScheduler().scheduleSyncRepeatingTask(core, new JobPaymentHandler(core), timeToDelay, 24000);
        }
        firstRun = false;
        lastTime = world.getFullTime();
    }
}
