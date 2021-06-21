package VRPCore.Models

import org.bukkit.Location
import org.bukkit.World

class Vec5 {
    constructor(_x: Double, _y: Double, _z: Double, _yaw: Float, _pitch: Float) {
        x = _x
        y = _y
        z = _z
        yaw = _yaw
        pitch = _pitch
    }

    constructor(_x: Double, _y: Double, _z: Double) {
        x = _x
        y = _y
        z = _z
    }

    constructor(all: Double) {
        x = all
        y = all
        z = all
        yaw = all.toFloat()
        pitch = all.toFloat()
    }

    constructor(loc: Location) {
        x = loc.x
        y = loc.y
        z = loc.z
        yaw = loc.yaw
        pitch = loc.pitch
    }

    fun toLocation(world: World?): Location {
        return Location(world, x, y, z, yaw, pitch)
    }

    var x: Double
    var y: Double
    var z: Double
    var yaw = 0f
    var pitch = 0f
}