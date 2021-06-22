package Commands

import VRPCore.VRPCore
import org.bukkit.ChatColor
import org.bukkit.command.CommandSender
import org.velocity.CommandHandler.annotations.Argument
import org.velocity.CommandHandler.annotations.Command
import org.velocity.CommandHandler.models.CommandArg
import org.velocity.CommandHandler.models.CommandBase
import VRPCore.Enums.*;

@Command(Name = "forecast")
class ForecastCommand : CommandBase<VRPCore?> {
    var core: VRPCore? = null

    //forecast ..
    override fun execute(
        arg0: CommandSender?,
        arg1: org.bukkit.command.Command?,
        label: String?,
        args: Array<String?>?
    ) {
    }

    //forecast
    override fun executeFinal(
        sender: CommandSender,
        cmd: org.bukkit.command.Command?,
        label: String?,
        args: Array<String?>?
    ): Boolean {
        val SB = StringBuilder()
        for (type in core!!.WeatherManager!!.futureForecast) {
            when (type) {
                WeatherType.Rainy -> SB.append(ChatColor.YELLOW.toString() + "R ")
                WeatherType.Stormy -> SB.append(ChatColor.RED.toString() + "T ")
                WeatherType.Sunny -> SB.append(ChatColor.GREEN.toString() + "S ")
            }
        }
        sender.sendMessage(ChatColor.GOLD.toString() + "Here is the 7 day forecast!")
        sender.sendMessage(SB.toString())
        return true
    }

    @Argument(Name = "set", Index = 0, Permission = "vrpcore.forecast.set")
    inner class ForecastOverride : CommandArg {
        override fun execute(
            sender: CommandSender,
            command: org.bukkit.command.Command?,
            label: String?,
            args: Array<String>,
            arg: String?
        ) {
            if (args.size >= 3) {
                val day = args[1].toInt()
                val type = args[2]
                var wt: WeatherType = WeatherType.Sunny
                when (type.toLowerCase()) {
                    "sunny", "sun", "clear" -> wt = WeatherType.Sunny
                    "rainy", "rain" -> wt = WeatherType.Rainy
                    "storm", "stormy" -> wt = WeatherType.Stormy
                }
                if (wt === WeatherType.Stormy) {
                    sender.sendMessage(ChatColor.YELLOW.toString() + "WeatherType: " + ChatColor.ITALIC + "Stormy" + ChatColor.YELLOW + " is not implemented yet. Defaulting to: Rainy.")
                    wt = WeatherType.Rainy
                }
                core!!.WeatherManager!!.futureForecast[day] = wt
                sender.sendMessage(ChatColor.YELLOW.toString() + "Updated Forecast")
            } else {
                sender.sendMessage(ChatColor.RED.toString() + "Syntax Error: " + ChatColor.YELLOW + "/forecast set [day] [type]")
                sender.sendMessage(ChatColor.GRAY.toString() + "Possible Types: SUNNY, RAIN, STORM")
            }
        }

        override fun executeFinal(
            sender: CommandSender?,
            command: org.bukkit.command.Command?,
            label: String?,
            args: Array<String?>?,
            arg: String?
        ): Boolean {
            return true
        }
    }

    override fun init(arg0: VRPCore?) {
        core = arg0
    }
}