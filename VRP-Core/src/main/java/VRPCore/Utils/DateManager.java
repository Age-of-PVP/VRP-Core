package VRPCore.Utils;

import VRPCore.Interfaces.IDailyRunnable;
import VRPCore.Interfaces.IWeeklyRunnable;
import VRPCore.VRPCore;

import java.util.ArrayList;

public class DateManager implements Runnable {
    private final VRPCore core;
    private String[] daysOfWeek = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
    private String[] Months = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
    private ArrayList<IDailyRunnable> DailyTasks = new ArrayList<>();
    private ArrayList<IWeeklyRunnable> WeeklyTasks = new ArrayList<IWeeklyRunnable>();

    private long lastDate = 0;

    public final long MINECRAFT_DAY = 24000;
    public final long MINECRAFT_WEEK = MINECRAFT_DAY * 7;
    public final long MINECRAFT_MONTH = MINECRAFT_WEEK * 4;
    public final long MINECRAFT_YEAR = MINECRAFT_MONTH * 12;
    public final long MINECRAFT_MIDNIGHT_OFFSET = 18000;

    public DateManager(VRPCore _core){
        this.core = _core;
    }

    public boolean RegisterDailyTask(IDailyRunnable task){
        if(task instanceof IDailyRunnable){
            core.getLogger().warning("Registered Daily Task: " + task.getTaskName());
            DailyTasks.add(task);
            return true;
        } else {
            core.getLogger().severe("Tried to register daily task " + task.getTaskName() + " however it is not of type IDailyRunnable");
            return false;
        }
    }

    public boolean RegisterWeeklyTask(IWeeklyRunnable task){
        if(task instanceof IWeeklyRunnable)
        {
            core.getLogger().warning("Registered Daily Task: " + task.getTaskName());
            WeeklyTasks.add(task);
            return true;
        }
        else {
            core.getLogger().severe("Tried to register weekly task " + task.getTaskName() + " however it is not of type IWeeklyRunnable");
            return false;
        }
    }


    public String GetDayOfWeek(){
        return daysOfWeek[GetDayOfWeekAsInt()];
    }

    public int GetDayOfWeekAsInt(){
        long worldTicks = core.getServer().getWorld("OLD").getFullTime() - MINECRAFT_MIDNIGHT_OFFSET;

        // Calculate the day of the week numerically
        // 24'000 Ticks per minecraft day * 7 = 168'000 ticks per minecraft week
        int index = (int)Math.floor((worldTicks % MINECRAFT_WEEK) / MINECRAFT_DAY);

        return index;
    }

    public int GetDayOfMonth() {
        long worldTicks = core.getServer().getWorld("OLD").getFullTime() - MINECRAFT_MIDNIGHT_OFFSET;

        int index = (int)Math.floor((worldTicks % MINECRAFT_MONTH) / MINECRAFT_DAY);

        return index;
    }

    public int GetDayOfYear() {
        long worldTicks = core.getServer().getWorld("OLD").getFullTime() - MINECRAFT_MIDNIGHT_OFFSET;

        int index = (int)Math.floor((worldTicks % MINECRAFT_YEAR) / MINECRAFT_DAY);

        return index;
    }

    public String GetMonthOfYear(){
        return Months[GetMonthOfYearAsInt()];
    }

    public int GetMonthOfYearAsInt(){
        long worldTicks = core.getServer().getWorld("OLD").getFullTime() - MINECRAFT_MIDNIGHT_OFFSET;

        int index = (int)Math.floor((worldTicks % MINECRAFT_YEAR) / MINECRAFT_MONTH);

        return index;
    }

    public int GetYear(){
        long worldTicks = core.getServer().getWorld("OLD").getFullTime() - MINECRAFT_MIDNIGHT_OFFSET;

        int index = (int)Math.floor(worldTicks / MINECRAFT_YEAR);

        return index;
    }


    int numDays = 0;
    // This method will run once every day at exactly midnight (18000 Ticks)
    @Override
    public void run() {
        if(lastDate + MINECRAFT_DAY == core.getServer().getWorld("OLD").getFullTime())
            core.getServer().getWorld("OLD").setFullTime(lastDate + MINECRAFT_DAY); // Artificially add 1 day if daynight cycle is off

        for(IDailyRunnable task : DailyTasks){
            core.getLogger().warning("Ran Daily Method for " + task.getTaskName());
            task.daily();
        }

        // Weekly Tasks
        if(numDays % 7 == 0)
            for(IWeeklyRunnable task : WeeklyTasks){
                core.getLogger().warning("Ran Weekly Method for " + task.getTaskName());
            }

        // Bi-Weekly Tasks
        if(numDays % 14 == 0);

        // Months Tasks
        if(numDays % (7 * 4) == 0);

        // Bi-Monthly Tasks
        if(numDays % (7 * 4 * 2) == 0);

        // Quarterly Tasks
        if(numDays % 91 == 0);

        // Bi-Quarterly Tasks
        if(numDays % 182 == 0);

        // Yearly Tasks
        if(numDays % 365 == 0);

        // Bi-Yearly Tasks
        if(numDays % (365 * 2) == 0);

        // Per decade tasks (lol if I ever use this)
        if(numDays % (365 * 10) == 0);

        numDays++;
        lastDate = core.getServer().getWorld("OLD").getFullTime();
    }
}
