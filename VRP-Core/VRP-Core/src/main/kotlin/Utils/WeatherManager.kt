package VRPCore.Utils

import VRPCore.Enums.WeatherType
import VRPCore.Interfaces.IDailyRunnable
import VRPCore.VRPCore
import java.util.*

class WeatherManager(_core: VRPCore) : IDailyRunnable {
    private val core: VRPCore
    private val weekSpan = 24000L * 7L
    private val daySpan = 24000L
    var futureForecast = ArrayList<WeatherType>()
    private val rand = Random()// REMOVE NEGATIVE SIGN TO RE-ENABLE// Value will fall between 10 - 60 { 0 <= x <= 11 }

    // if true, it's raining
    // DETERMINE WEATHER BASED ON MONTH
    // WE WILL CREATE A PARABOLA FOR PRECIPITATION DURING THE YEAR
    // let f(x) be the chance of precipitation, f(x) = (1.4)((x-5.5)^2)+10 {0 <= x <= 11}
    // If it does rain, the chance of a storm is 10%
    val randomWeatherType: WeatherType
        get() {
            val month: Int = core.DateManager.GetMonthOfYearAsInt()

            // Value will fall between 10 - 60 { 0 <= x <= 11 }
            val chanceOfRain = 1.4 * Math.pow(month.toDouble() - 5.5, 2.0) + 10
            val chance = rand.nextInt(100).toDouble()

            // if true, it's raining
            return if (chance <= chanceOfRain) {
                val stormChance = rand.nextInt(100).toDouble()
                if (stormChance <= -10.0) // REMOVE NEGATIVE SIGN TO RE-ENABLE
                    WeatherType.Stormy else WeatherType.Rainy
            } else WeatherType.Sunny
        }

    override fun daily() {
        // Determine the weather for current date + 7
        val forecastToday = futureForecast[0]
        for (i in 0 until futureForecast.size - 1) {
            futureForecast[i] = futureForecast[i + 1]
        }
        futureForecast[futureForecast.size - 1] = randomWeatherType
        if (core.WeatherChangeHandle != -1) core.getServer().getScheduler().cancelTask(core.WeatherChangeHandle)
        core.WeatherChangeHandle = core.getServer().getScheduler().scheduleSyncDelayedTask(
            core,
            Runnable { // This doesn't make it sunny again for some reason. try setClearWeather or something
                // Edit: lines 80-83 hopefully fix the above TODO: <-- Check
                core.getServer().getWorld("OLD")?.setStorm(forecastToday !== WeatherType.Sunny)
                if (forecastToday === WeatherType.Sunny) {
                    core.getServer().getWorld("OLD")?.setWeatherDuration(1)
                    core.getServer().getWorld("OLD")?.setClearWeatherDuration(99999)
                }
            },
            3000
        )
    }

    override val taskName: String
        get() = "WeatherManager"

    init {
        core = _core
        core.getLogger().warning("WeatherRunnable Created")
        for (i in 1..7) {
            futureForecast.add(randomWeatherType)
        }
    }
}