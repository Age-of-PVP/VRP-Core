package VRPCore.Cinematics;

import VRPCore.Models.Vec5;
import org.bukkit.Location;

import java.util.ArrayList;

public class Cinematic {
    public String Name;
    public int Duration; // In Seconds
    public ArrayList<Vec5> KeyPoints;
    public String LinkedCinematic;
    public String[] Titles;
    public String[] Subtitles;
}
