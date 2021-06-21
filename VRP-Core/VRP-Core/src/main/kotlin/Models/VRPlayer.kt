package VRPCore.Models

import VRPCore.Enums.EditorStickMode
import org.bukkit.ChatColor
import java.util.*

class VRPlayer {
    var Username: String? = null
    var UUID: UUID? = null
    var StickMode: EditorStickMode = EditorStickMode.None
    var Job: Job? = null
    var firstLogon = true
    private var wantedLevel = 0f

    // <Stats>
    private val ADRENALINE_MAX = 100f
    private val STRENGTH_MAX = 100f
    var Adrenaline = 0.0f
    var Strength = 0.0f
    fun clipStats() {
        if (Adrenaline > ADRENALINE_MAX) Adrenaline = ADRENALINE_MAX
        if (Strength > STRENGTH_MAX) Strength = STRENGTH_MAX
    }

    // </Stats>
    fun getWantedLevel(): Int {
        return if (wantedLevel > 0.5) Math.ceil(wantedLevel.toDouble()).toInt() else 0
    }

    val wantedDisplay: String
        get() = if (wantedLevel >= 0.5) if (wantedLevel >= 9.0) ChatColor.RED.toString() + "MOST WANTED" else ChatColor.GOLD.toString() + "" + ChatColor.ITALIC + "★ x" + ChatColor.BOLD + Integer.toString(
            getWantedLevel()
        ) else "Good Citizen"

    fun setWantedLevel(x: Float) {
        wantedLevel = if (x >= 10) 10f else if (x <= 0) 0f else x
    }
}