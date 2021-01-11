package VRPCore.Utils;

import VRPCore.Enums.WeatherType;
import VRPCore.Interfaces.IDailyRunnable;
import VRPCore.VRPCore;
import org.bukkit.entity.Weather;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

public class WeatherManager implements IDailyRunnable {

    private VRPCore core;

    private final long weekSpan = 24000L * 7L;

    private final long daySpan = 24000L;

    public ArrayList<WeatherType> futureForecast = new ArrayList<WeatherType>();

    private Random rand = new Random();

    public WeatherManager(VRPCore _core){
        this.core = _core;

        core.getLogger().warning("WeatherRunnable Created");

        for(int i = 1; i <= 7; i++){
            futureForecast.add(getRandomWeatherType());
        }
    }

    // DETERMINE WEATHER BASED ON MONTH
    // WE WILL CREATE A PARABOLA FOR PRECIPITATION DURING THE YEAR
    // let f(x) be the chance of precipitation, f(x) = (1.4)((x-5.5)^2)+10 {0 <= x <= 11}
    // If it does rain, the chance of a storm is 10%
    public WeatherType getRandomWeatherType(){
        int month = core.DateManager.GetMonthOfYearAsInt();

        // Value will fall between 10 - 60 { 0 <= x <= 11 }
        double chanceOfRain = (1.4) * Math.pow((double)month - 5.5, 2) + 10;

        double chance = (double)rand.nextInt(100);

        // if true, it's raining
        if(chance <= chanceOfRain) {
            double stormChance = (double)rand.nextInt(100);
            if(stormChance <= -10.0) // REMOVE NEGATIVE SIGN TO RE-ENABLE
                return WeatherType.Stormy;
            else
                return WeatherType.Rainy;
        }
        else
            return WeatherType.Sunny;
    }

    @Override
    public void daily() {
        // Determine the weather for current date + 7

        WeatherType forecastToday = futureForecast.get(0);

        for (int i = 0; i < futureForecast.size() - 1; i++) {
            futureForecast.set(i, futureForecast.get(i + 1));
        }

        futureForecast.set(futureForecast.size() - 1, getRandomWeatherType());

        if(core.WeatherChangeHandle != -1)
            core.getServer().getScheduler().cancelTask(core.WeatherChangeHandle);

        core.WeatherChangeHandle = core.getServer().getScheduler().scheduleSyncDelayedTask(core, new Runnable() {
            @Override
            public void run() {
                // This doesn't make it sunny again for some reason. try setClearWeather or something
                // Edit: lines 80-83 hopefully fix the above TODO: <-- Check
                core.getServer().getWorld("OLD").setStorm(forecastToday != WeatherType.Sunny);

                if(forecastToday == WeatherType.Sunny) {
                    core.getServer().getWorld("OLD").setWeatherDuration(1);
                    core.getServer().getWorld("OLD").setClearWeatherDuration(99999);
                }
            }
        }, 3000);
    }

    @Override
    public String getTaskName() {
        return "WeatherManager";
    }
}
