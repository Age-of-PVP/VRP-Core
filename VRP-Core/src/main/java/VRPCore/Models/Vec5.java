package VRPCore.Models;

import org.bukkit.Location;
import org.bukkit.World;

public class Vec5 {
    public Vec5(double _x, double _y, double _z, float _yaw, float _pitch){
        x = _x;
        y = _y;
        z = _z;
        yaw = _yaw;
        pitch = _pitch;
    }

    public Vec5(double _x, double _y, double _z) {
        x = _x;
        y = _y;
        z = _z;
    }

    public Vec5(double all){
        x = all;
        y = all;
        z = all;
        yaw = (float)all;
        pitch = (float)all;
    }

    public Vec5(Location loc) {
        x = loc.getX();
        y = loc.getY();
        z = loc.getZ();
        yaw = loc.getYaw();
        pitch = loc.getPitch();
    }

    public Location toLocation(World world){
        return new Location(world, x, y, z, yaw, pitch);
    }

    public double x;
    public double y;
    public double z;
    public float yaw;
    public float pitch;
}
