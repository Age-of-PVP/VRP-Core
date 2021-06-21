package VRPCore.Cinematics;

import VRPCore.Models.Vec5
import java.util.ArrayList

class Cinematic {
    var Name: String? = null;
    var Duration = 0; // In Seconds
    var KeyPoints: ArrayList<Vec5>? = null;
    var LinkedCinematic: String? = null;
    lateinit var Titles: Array<String>;
    lateinit var Subtitles: Array<String>;
}