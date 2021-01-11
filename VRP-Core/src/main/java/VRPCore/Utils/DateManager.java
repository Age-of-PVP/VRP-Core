package VRPCore.Utils;

import VRPCore.Interfaces.IDailyRunnable;
import VRPCore.VRPCore;

import java.util.ArrayList;

public class DateManager implements Runnable {
    private final VRPCore core;
    private String[] daysOfWeek = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
    private String[] Months = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
    private ArrayList<IDailyRunnable> DailyTasks = new ArrayList<>();

    public DateManager(VRPCore _core){
        this.core = _core;
    }

    public void RegisterDailyTask(IDailyRunnable task){
        if(task instanceof IDailyRunnable){
            core.getLogger().warning("Registered Daily Task: " + task.getTaskName());
            DailyTasks.add(task);
        }
    }

    public String GetDayOfWeek(){
        return daysOfWeek[GetDayOfWeekAsInt()];
    }

    public int GetDayOfWeekAsInt(){

        long worldTicks = core.getServer().getWorld("OLD").getFullTime() - 18000L;

        // Calculate the day of the week numerically
        // 24'000 Ticks per minecraft day * 7 = 168'000 ticks per minecraft week
        int index = (int)Math.floor((worldTicks % 168000) / 24000);

        return index;
    }

    public int GetDayOfMonth() {
        long worldTicks = core.getServer().getWorld("OLD").getFullTime() -18000L;

        int index = (int)Math.floor((worldTicks % 672000) / 24000);

        return index;
    }

    public int GetDayOfYear() {
        long worldTicks = core.getServer().getWorld("OLD").getFullTime() - 18000L;

        int index = (int)Math.floor((worldTicks % 8064000) / 24000);

        return index;
    }

    public String GetMonthOfYear(){
        return Months[GetMonthOfYearAsInt()];
    }

    public int GetMonthOfYearAsInt(){
        long worldTicks = core.getServer().getWorld("OLD").getFullTime() - 18000L;

        int index = (int)Math.floor((worldTicks % 8064000) / 672000);

        return index;
    }

    public int GetYear(){
        long worldTicks = core.getServer().getWorld("OLD").getFullTime() - 18000L;

        int index = (int)Math.floor(worldTicks / 8064000);

        return index;
    }

    // This method will run once every day at exactly midnight (18000 Ticks)
    @Override
    public void run() {
        for(IDailyRunnable task : DailyTasks){
            core.getLogger().warning("Ran Daily Method for " + task.getTaskName());
            task.daily();
        }
    }
}
