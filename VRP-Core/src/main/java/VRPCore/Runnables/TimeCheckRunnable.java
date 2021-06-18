package VRPCore.Runnables;

import VRPCore.Economy.JobPaymentHandler;
import VRPCore.VRPCore;
import org.bukkit.GameRule;
import org.bukkit.World;

public class TimeCheckRunnable implements Runnable {

    private long lastTime;
    private VRPCore core;
    private World world;
    private boolean firstRun = true;
    private boolean lastGameruleValue;

    public TimeCheckRunnable(VRPCore _core){
        this.core = _core;
        this.world = core.getServer().getWorld("OLD");

        lastTime = world.getFullTime();
    }

    @Override
    public void run() {
        boolean currentGameruleValue = core.world.getGameRuleValue(GameRule.DO_DAYLIGHT_CYCLE);

        // The following 7 lines update the world time if the DoDaynightCycle is updated to true
        if(currentGameruleValue && currentGameruleValue != lastGameruleValue) {
            core.world.setFullTime(lastTime + 20);
            core.getLogger().warning("doDaynightCycle set to true. Updating world time...");
        }
        else if(currentGameruleValue != lastGameruleValue)
            core.getLogger().warning("doDaynightCycle set to false");
        lastGameruleValue = currentGameruleValue;

        if(world.getFullTime() - 20L != lastTime && !firstRun && currentGameruleValue) {
            core.getLogger().warning("ERROR: Possible plugin & day/night cycle de-sync! Attempting to fix! {lastTime:" + lastTime + "} {fullWorldTime:" + world.getFullTime() + "} {worldTime: " + world.getTime() + "}");
            core.getServer().getScheduler().cancelTask(core.DateManagerHandle);

            long timeToDelay = 18000L - world.getTime();
            core.getLogger().warning("Restarting DateManager in " + timeToDelay + " ticks.");
            core.DateManagerHandle = core.getServer().getScheduler().scheduleSyncRepeatingTask(core, core.DateManager, timeToDelay, 24000);
        }

        if(currentGameruleValue)
            lastTime = world.getFullTime();
        else
            lastTime += 20;

        firstRun = false;
    }
}
