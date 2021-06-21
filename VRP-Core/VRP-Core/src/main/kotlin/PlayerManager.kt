package VRPCore;

import VRPCore.Interfaces.IDailyRunnable;
import VRPCore.Models.VRPlayer;
import org.bukkit.entity.Player

import java.util.ArrayList;
import java.util.UUID;

class PlayerManager(_core: VRPCore) : IDailyRunnable {
    var Players = ArrayList<VRPlayer>()
    private val core: VRPCore
    private val percentageDecay = true // True = player loses % {STAT} every day, false = player loses flat {STAT} amount every day
    private val ADRENALINE_DECAY_PER_DAY = 0.03f // Player will lose 0.03% of their strength every day
    private val STRENGTH_DECAY_PER_DAY = 0.04f
    fun GetPlayer(ID: UUID): VRPlayer {
        for (i in Players.indices) {
            if (Players[i].UUID!!.equals(ID)) return Players[i]
        }
        val p: VRPlayer = VRPlayer();
        p.UUID = ID;
        Players.add(p)
        return p
    }

    fun SkillDecay(player: VRPlayer) {
        if (percentageDecay) {
            player.Adrenaline *= 1 - ADRENALINE_DECAY_PER_DAY
            player.Strength *= 1 - STRENGTH_DECAY_PER_DAY
        } else {
            player.Adrenaline -= ADRENALINE_DECAY_PER_DAY
            player.Strength -= STRENGTH_DECAY_PER_DAY
        }
        player.clipStats()
    }

    override fun daily() {
        for (player in Players) {
            SkillDecay(player)
        }
    }

    override val taskName: String
        get() = "PlayerManager"

    init {
        core = _core
    }
}