package VRPCore.Cinematics;

import VRPCore.Models.Vec5
import java.util.ArrayList

data class Cinematic(var Name: String?, var Duration: Int, var KeyPoints: ArrayList<Vec5>?, var LinkedCinematic: String, var Titles: Array<String>, var Subtitles: Array<String>) {

}