package VRPCore.Runnables;

import VRPCore.VRPCore;
import org.bukkit.WeatherType;
import org.bukkit.entity.Weather;

import java.util.HashMap;

public class WeatherRunnable implements Runnable {

    private VRPCore core;

    private final long weekSpan = 24000L * 7L;

    private final long daySpan = 24000L;

    private HashMap<Long, WeatherType> futureForcast = new HashMap<Long, WeatherType>();

    public WeatherRunnable(VRPCore _core){
        this.core = _core;

        for(int i = 0; i < 7; i++){
            futureForcast.put(daySpan, )
        }
    }

    // DETERMINE WEATHER BASED ON MONTH
    // WE WILL CREATE A PORABALA FOR PRECIPITATION DURING THE YEAR
    // f(x) = (1.4)((x-6)^2)+10 {0 <= x <= 12}
    public WeatherType getRandomWeatherType(){

    }

    @Override
    public void run() {
        // Determine the weather for current date + 7
    }
}
