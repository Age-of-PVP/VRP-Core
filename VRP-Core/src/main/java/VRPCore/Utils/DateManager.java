package VRPCore.Utils;

import VRPCore.VRPCore;

public class DateManager {
    private final VRPCore core;
    private String[] daysOfWeek = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
    private String[] Months = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};

    public DateManager(VRPCore _core){
        this.core = _core;
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

}
