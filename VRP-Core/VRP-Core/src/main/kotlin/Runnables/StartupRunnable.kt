package VRPCore.Runnables

import VRPCore.Economy.JobPaymentHandler
import VRPCore.Listeners.LogonListener
import VRPCore.Utils.WeatherManager
import VRPCore.VRPCore

class StartupRunnable(private val core: VRPCore) : Runnable {
    override fun run() {
        core.world = core.server.getWorld("OLD")
        core.DateManagerHandle = core.server.scheduler.scheduleSyncRepeatingTask(core, core.DateManager, 0L, 24000)
        core.server.scheduler.scheduleSyncRepeatingTask(core, TimeCheckRunnable(core), 0, 20)
        core.WeatherManager = WeatherManager(core)

        // Set time to day
        core.server.getWorld("OLD")!!.fullTime = 16299768000L + 18000L
        core.server.pluginManager.registerEvents(LogonListener(core), core)
        core.registerTasks()
    }
}