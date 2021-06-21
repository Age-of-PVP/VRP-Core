package VRPCore.Runnables

import VRPCore.Economy.JobPaymentHandler
import VRPCore.VRPCore
import org.bukkit.GameRule
import org.bukkit.World

class TimeCheckRunnable(private val core: VRPCore) : Runnable {
    private var lastTime: Long
    private val world: World?
    private var firstRun = true
    private var lastGameruleValue = false
    override fun run() {
        val currentGameruleValue = core.world!!.getGameRuleValue(GameRule.DO_DAYLIGHT_CYCLE)!!

        // The following 7 lines update the world time if the DoDaynightCycle is updated to true
        if (currentGameruleValue && currentGameruleValue != lastGameruleValue) {
            core.world!!.fullTime = lastTime + 20
            core.logger.warning("doDaynightCycle set to true. Updating world time...")
        } else if (currentGameruleValue != lastGameruleValue) core.logger.warning("doDaynightCycle set to false")
        lastGameruleValue = currentGameruleValue
        if (world!!.fullTime - 20L != lastTime && !firstRun && currentGameruleValue) {
            core.logger.warning("ERROR: Possible plugin & day/night cycle de-sync! Attempting to fix! {lastTime:" + lastTime + "} {fullWorldTime:" + world.fullTime + "} {worldTime: " + world.time + "}")
            core.server.scheduler.cancelTask(core.DateManagerHandle)
            val timeToDelay = 18000L - world.time
            core.logger.warning("Restarting DateManager in $timeToDelay ticks.")
            core.DateManagerHandle =
                core.server.scheduler.scheduleSyncRepeatingTask(core, core.DateManager, timeToDelay, 24000)
        }
        if (currentGameruleValue) lastTime = world.fullTime else lastTime += 20
        firstRun = false
    }

    init {
        world = core.server.getWorld("OLD")
        lastTime = world!!.fullTime
    }
}