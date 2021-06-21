package VRPCore.Utils

import VRPCore.Interfaces.IDailyRunnable
import VRPCore.Interfaces.IWeeklyRunnable
import VRPCore.VRPCore
import java.util.ArrayList

class DateManager(_core: VRPCore) : Runnable {
    private val core: VRPCore
    private val daysOfWeek = arrayOf("Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday")
    private val Months = arrayOf(
        "January",
        "February",
        "March",
        "April",
        "May",
        "June",
        "July",
        "August",
        "September",
        "October",
        "November",
        "December"
    )
    private val DailyTasks = ArrayList<IDailyRunnable>()
    private val WeeklyTasks = ArrayList<IWeeklyRunnable>()
    private var lastDate: Long = 0
    val MINECRAFT_DAY: Long = 24000
    val MINECRAFT_WEEK = MINECRAFT_DAY * 7
    val MINECRAFT_MONTH = MINECRAFT_WEEK * 4
    val MINECRAFT_YEAR = MINECRAFT_MONTH * 12
    val MINECRAFT_MIDNIGHT_OFFSET: Long = 18000
    fun RegisterDailyTask(task: IDailyRunnable): Boolean {
        return if (task is IDailyRunnable) {
            core.getLogger().warning("Registered Daily Task: " + task.taskName)
            DailyTasks.add(task)
            true
        } else {
            core.getLogger()
                .severe("Tried to register daily task " + task.taskName + " however it is not of type IDailyRunnable")
            false
        }
    }

    fun RegisterWeeklyTask(task: IWeeklyRunnable): Boolean {
        return if (task is IWeeklyRunnable) {
            core.getLogger().warning("Registered Daily Task: " + task.taskName)
            WeeklyTasks.add(task)
            true
        } else {
            core.getLogger()
                .severe("Tried to register weekly task " + task.taskName + " however it is not of type IWeeklyRunnable")
            false
        }
    }

    fun GetDayOfWeek(): String {
        return daysOfWeek[GetDayOfWeekAsInt()]
    }

    fun GetDayOfWeekAsInt(): Int {
        val worldTicks: Long = core.getServer().getWorld("OLD")?.getFullTime()?.minus(MINECRAFT_MIDNIGHT_OFFSET) ?: 0

        // Calculate the day of the week numerically
        // 24'000 Ticks per minecraft day * 7 = 168'000 ticks per minecraft week
        return Math.floor((worldTicks % MINECRAFT_WEEK / MINECRAFT_DAY).toDouble()).toInt()
    }

    fun GetDayOfMonth(): Int {
        val worldTicks: Long = core.getServer().getWorld("OLD")?.getFullTime()?.minus(MINECRAFT_MIDNIGHT_OFFSET) ?: 0
        return Math.floor((worldTicks % MINECRAFT_MONTH / MINECRAFT_DAY).toDouble()).toInt()
    }

    fun GetDayOfYear(): Int {
        val worldTicks: Long = core.getServer().getWorld("OLD")?.getFullTime()?.minus(MINECRAFT_MIDNIGHT_OFFSET) ?: 0
        return Math.floor((worldTicks % MINECRAFT_YEAR / MINECRAFT_DAY).toDouble()).toInt()
    }

    fun GetMonthOfYear(): String {
        return Months[GetMonthOfYearAsInt()]
    }

    fun GetMonthOfYearAsInt(): Int {
        val worldTicks: Long = core.getServer().getWorld("OLD")?.getFullTime()?.minus(MINECRAFT_MIDNIGHT_OFFSET) ?: 0
        return Math.floor((worldTicks % MINECRAFT_YEAR / MINECRAFT_MONTH).toDouble()).toInt()
    }

    fun GetYear(): Int {
        val worldTicks: Long = core.getServer().getWorld("OLD")?.getFullTime()?.minus(MINECRAFT_MIDNIGHT_OFFSET) ?: 0
        return Math.floor((worldTicks / MINECRAFT_YEAR).toDouble()).toInt()
    }

    var numDays = 0

    // This method will run once every day at exactly midnight (18000 Ticks)
    override fun run() {
        if (lastDate + MINECRAFT_DAY == core.getServer().getWorld("OLD")?.getFullTime()) {
            core.getServer().getWorld("OLD")!!
                .setFullTime(lastDate + MINECRAFT_DAY) // Artificially add 1 day if daynight cycle is off
        }
        for (task in DailyTasks) {
            core.getLogger().warning("Ran Daily Method for " + task.taskName)
            task.daily()
        }

        // Weekly Tasks
        if (numDays % 7 == 0) for (task in WeeklyTasks) {
            core.getLogger().warning("Ran Weekly Method for " + task.taskName)
        }

        // Bi-Weekly Tasks
        if (numDays % 14 == 0);

        // Months Tasks
        if (numDays % (7 * 4) == 0);

        // Bi-Monthly Tasks
        if (numDays % (7 * 4 * 2) == 0);

        // Quarterly Tasks
        if (numDays % 91 == 0);

        // Bi-Quarterly Tasks
        if (numDays % 182 == 0);

        // Yearly Tasks
        if (numDays % 365 == 0);

        // Bi-Yearly Tasks
        if (numDays % (365 * 2) == 0);

        // Per decade tasks (lol if I ever use this)
        if (numDays % (365 * 10) == 0);
        numDays++
        lastDate = core.getServer().getWorld("OLD")?.getFullTime()!!
    }

    init {
        core = _core
    }
}