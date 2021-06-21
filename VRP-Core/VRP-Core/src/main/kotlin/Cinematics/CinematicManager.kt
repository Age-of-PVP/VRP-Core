package VRPCore.Cinematics;

import VRPCore.Interfaces.IStorable
import VRPCore.Utils.IO
import VRPCore.VRPCore
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import org.bukkit.ChatColor
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitTask
import java.io.File
import java.io.FileReader
import java.io.FileWriter
import java.io.IOException
import java.lang.Exception
import java.util.ArrayList

class CinematicManager(private val core: VRPCore) : IStorable {
    var Cinematics: ArrayList<Cinematic>? = null
    var gson = GsonBuilder().setPrettyPrinting().create()
    private val CinematicsDirectory = "./plugins/VRPCore/Cinematics/cinematics.json"
    private val CinematicsDir = "./plugins/VRPCore/Cinematics/"
    fun StartPlayerCinematic(player: Player, CinematicName: String): Boolean {
        val cinematic = get(CinematicName)
        if (cinematic != null) {
            val runnable: BukkitTask =
                MovementHandler(player, core.CinematicManager[CinematicName]!!, core).runTaskTimer(core, 0, 1)
            return true
        }
        player.sendMessage(ChatColor.RED.toString() + "Failed to find Cinematic called " + ChatColor.YELLOW + CinematicName)
        return false
    }

    operator fun get(CinematicName: String?): Cinematic? {
        for (i in Cinematics!!.indices) {
            if (Cinematics!![i].Name.equals(CinematicName, ignoreCase = true)) {
                return Cinematics!![i]
            }
        }
        return null
    }

    override fun save() {
        try {
            val jsonFile = File(CinematicsDirectory)
            if (!jsonFile.exists()) {
                jsonFile.createNewFile()
            }
            gson.toJson(Cinematics, FileWriter(jsonFile))
        } catch (e: IOException) {
            core.logger.severe("Failed to export cinematic data to json")
            e.printStackTrace()
        }
    }

    override fun load() {
        try {
            val directory = File(CinematicsDir)
            if (!directory.exists()) {
                directory.mkdirs()
                Cinematics = ArrayList()
            } else {
                gson.fromJson<Any>(
                    FileReader(File(CinematicsDirectory)),
                    object : TypeToken<ArrayList<Cinematic?>?>() {}.type
                )
                //Cinematics = new ArrayList<>(Arrays.asList(
                //        gson.fromJson(IO.ReadAllLines(CinematicsDirectory + "cinematics.json"), Cinematic[].class)
                //));
            }
        } catch (e: Exception) {
            core.logger.severe("Failed to load cinematic data from json")
            e.printStackTrace()
            Cinematics = ArrayList()
        }
    }
}