package VRPCore.Cinematics

import VRPCore
import org.bukkit.ChatColor
import org.bukkit.GameMode
import org.bukkit.Location
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitRunnable

class MovementHandler(_player: Player, _cinematic: Cinematic, _core: VRPCore) : BukkitRunnable() {
    // Player Data Pre-Cinematic
    var startingLocation: Location
    var startingGamemode: GameMode
    var wasFlying: Boolean
    var wasAllowFlight: Boolean
    private var tick = 0
    var cinematic: Cinematic
    var TicksPerSequence: Int
    lateinit var allXPoints: Array<Float?>
    lateinit var allYPoints: Array<Float?>
    lateinit var allZPoints: Array<Float?>
    lateinit var allYawPoints: Array<Float?>
    lateinit var allPitchPoints: Array<Float?>
    lateinit var allXPointsD: DoubleArray
    lateinit var allYPointsD: DoubleArray
    lateinit var allZPointsD: DoubleArray
    lateinit var allYawPointsD: DoubleArray
    lateinit var allPitchPointsD: DoubleArray
    var player: Player
    private val core: VRPCore
    fun PreCalculations() {
        player.sendMessage("Starting calculation of points")
        allXPoints = arrayOfNulls(cinematic.Duration * 20)
        allYPoints = arrayOfNulls(cinematic.Duration * 20)
        allZPoints = arrayOfNulls(cinematic.Duration * 20)
        allYawPoints = arrayOfNulls(cinematic.Duration * 20)
        allPitchPoints = arrayOfNulls(cinematic.Duration * 20)
        allXPointsD = DoubleArray(cinematic.Duration * 20)
        allYPointsD = DoubleArray(cinematic.Duration * 20)
        allZPointsD = DoubleArray(cinematic.Duration * 20)
        allYawPointsD = DoubleArray(cinematic.Duration * 20)
        allPitchPointsD = DoubleArray(cinematic.Duration * 20)
        CalculatePositions()
    }

    fun CalculatePositions() {
        player.sendMessage("Cinematic.Duration * 20: " + cinematic.Duration * 20)
        player.sendMessage("TPS * Cinematic.KeyPoints.Count: " + TicksPerSequence * cinematic.KeyPoints!!.size)
        player.sendMessage("TPS: $TicksPerSequence")
        val dataPointsX = arrayOfNulls<Data>(
            cinematic.KeyPoints!!.size
        )
        val dataPointsY = arrayOfNulls<Data>(
            cinematic.KeyPoints!!.size
        )
        val dataPointsZ = arrayOfNulls<Data>(
            cinematic.KeyPoints!!.size
        )
        val dataPointsYaw = arrayOfNulls<Data>(
            cinematic.KeyPoints!!.size
        )
        val dataPointsPitch = arrayOfNulls<Data>(
            cinematic.KeyPoints!!.size
        )
        val ttps = cinematic.Duration * 20 / (cinematic.KeyPoints!!.size - 1)
        for (i in cinematic.KeyPoints!!.indices) {
            dataPointsX[i] = Data((ttps * i).toDouble(), cinematic.KeyPoints!![i].x)
            dataPointsY[i] = Data((ttps * i).toDouble(), cinematic.KeyPoints!![i].y)
            dataPointsZ[i] = Data((ttps * i).toDouble(), cinematic.KeyPoints!![i].z)
            dataPointsYaw[i] = Data((ttps * i).toDouble(), cinematic.KeyPoints!![i].yaw)
            dataPointsPitch[i] = Data((ttps * i).toDouble(), cinematic.KeyPoints!![i].pitch)
            player.sendMessage("X: " + dataPointsPitch[i]!!.x + " | Y: " + dataPointsPitch[i]!!.y)
        }
        for (i in 0 until cinematic.Duration * 20) {
            allXPoints[i] = interpolate(dataPointsX, i, cinematic.KeyPoints!!.size).toFloat()
            allYPoints[i] = interpolate(dataPointsY, i, cinematic.KeyPoints!!.size).toFloat()
            allZPoints[i] = interpolate(dataPointsZ, i, cinematic.KeyPoints!!.size).toFloat()
            allYawPoints[i] = interpolate(dataPointsYaw, i, cinematic.KeyPoints!!.size).toFloat()
            allPitchPoints[i] =
                interpolate(dataPointsPitch, i, cinematic.KeyPoints!!.size).toFloat()
        }
        for (i in 0 until cinematic.Duration * 20) {
            allXPointsD[i] = interpolate(dataPointsX, i, cinematic.KeyPoints!!.size)
            allYPointsD[i] = interpolate(dataPointsY, i, cinematic.KeyPoints!!.size)
            allZPointsD[i] = interpolate(dataPointsZ, i, cinematic.KeyPoints!!.size)
            allYawPointsD[i] = interpolate(dataPointsYaw, i, cinematic.KeyPoints!!.size)
            allPitchPointsD[i] = interpolate(dataPointsPitch, i, cinematic.KeyPoints!!.size)
        }

        // We generate an graph of each axis for debug reasons
        val time = DoubleArray(allXPoints.size)
        for (i in time.indices) {
            time[i] = i.toDouble()
        }
        val timeRough = DoubleArray(cinematic.KeyPoints!!.size)
        for (i in time.indices) {
            time[i] = i.toDouble()
        }
    }

    override fun run() {
        player.teleport(
            Location(
                player.world, allXPoints[tick], allYPoints[tick], allZPoints[tick],
                allYawPoints[tick]!!, allPitchPoints[tick]!!
            )
        )
        if (tick++ == cinematic.Duration * 20 - 1) {
            if (cinematic.LinkedCinematic == "") {
                player.teleport(startingLocation)
                player.isFlying = wasFlying
                player.gameMode = startingGamemode
                player.allowFlight = wasAllowFlight
                player.sendMessage(ChatColor.YELLOW.toString() + "Finished Cinematic: " + ChatColor.LIGHT_PURPLE + cinematic.Name)
                cancel()
            } else {
                val newCinematic: Cinematic = core.CinematicManager.get(cinematic.LinkedCinematic)
                if (newCinematic == null) {
                    player.teleport(startingLocation)
                    player.isFlying = wasFlying
                    player.gameMode = startingGamemode
                    player.allowFlight = wasAllowFlight
                    player.sendMessage(ChatColor.YELLOW.toString() + "Finished Cinematic: " + ChatColor.LIGHT_PURPLE + cinematic.Name)
                    cancel()
                } else {
                    player.sendMessage(ChatColor.YELLOW.toString() + "Switched to " + ChatColor.YELLOW + newCinematic.Name)
                    cinematic = newCinematic
                    tick = 0
                    PreCalculations()
                }
            }
        }
    }

    internal class Data(var x: Double, var y: Double)
    companion object {
        fun interpolate(start: Double, end: Double, count: Int): DoubleArray {
            require(count >= 2) { "interpolate: illegal count!" }
            val array = DoubleArray(count + 1)
            for (i in 0..count) {
                array[i] = start + i * (end - start) / count
            }
            return array
        }

        // function to interpolate the given
        // data points using Lagrange's formula
        // xi corresponds to the new data point
        // whose value is to be obtained n
        // represents the number of known data points
        fun interpolate(f: Array<Data?>, xi: Int, n: Int): Double {
            var result = 0.0 // Initialize result
            for (i in 0 until n) {
                // Compute individual terms of above formula
                var term = f[i]!!.y
                for (j in 0 until n) {
                    if (j != i) term = term * (xi - f[j]!!.x) / (f[i]!!.x - f[j]!!.x)
                }

                // Add current term to result
                result += term
            }
            return result
        }
    }

    init {
        core = _core
        player = _player
        cinematic = _cinematic
        TicksPerSequence = cinematic.Duration / cinematic.KeyPoints!!.size * 20
        startingLocation = player.location
        startingGamemode = player.gameMode
        wasAllowFlight = player.allowFlight
        player.gameMode = GameMode.SPECTATOR
        player.allowFlight = true
        player.isFlying = true
        wasFlying = player.isFlying
        PreCalculations()
    }
}