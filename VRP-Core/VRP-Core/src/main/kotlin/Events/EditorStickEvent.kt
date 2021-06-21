package VRPCore.Events

import VRPCore.VRPCore
import de.tr7zw.nbtapi.NBTItem
import jdk.vm.ci.code.Register
import me.filoghost.chestcommands.api.ChestCommandsAPI
import org.bukkit.ChatColor
import org.bukkit.DyeColor
import org.bukkit.Material
import org.bukkit.block.Banner
import org.bukkit.block.banner.Pattern
import org.bukkit.block.banner.PatternType
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.material.Directional

class EditorStickEvents(var plugin: VRPCore) : Listener {
    @EventHandler
    fun onStickInteraction(e: PlayerInteractEvent) {
        val hand = e.item
        if (hand != null && hand.type == Material.STICK) {
            val nbt = NBTItem(hand)
            if (e.player.hasPermission("vrpcore.es")) {
                if (nbt.hasKey("isEditorStick")) {
                    if (e.action == Action.LEFT_CLICK_BLOCK) {
                        when (plugin.playerManager.GetPlayer(e.player.uniqueId).StickMode) {
                            Register.None -> e.player.sendMessage(ChatColor.YELLOW.toString() + "Select a Editor Stick Mode First!")
                            LightEditor -> {
                                e.player.sendMessage(ChatColor.GREEN.toString() + "Light Editor!")
                                if (e.clickedBlock!!.type == Material.REDSTONE_LAMP) {
                                    e.player.sendMessage("Added block to array")
                                    plugin.lightManager.DayNightLamps.add(e.clickedBlock)
                                }
                            }
                            Intersection -> {
                                val spawnLocation = e.clickedBlock!!.getRelative(e.blockFace).location
                                val b = e.clickedBlock!!.world.getBlockAt(spawnLocation)
                                b.type = Material.BLACK_WALL_BANNER
                                val banner = b.state as Banner
                                banner.baseColor = DyeColor.BLACK
                                banner.addPattern(Pattern(DyeColor.RED, PatternType.HALF_HORIZONTAL))
                                banner.addPattern(Pattern(DyeColor.BLACK, PatternType.BORDER))
                                banner.update(true)
                                val blockData = b.blockData
                                if (blockData is Directional) {
                                    (blockData as Directional).setFacingDirection(e.blockFace)
                                    b.blockData = blockData
                                }
                            }
                            WaypointEditor -> e.player.sendMessage(ChatColor.GREEN.toString() + "Waypoint Editor!")
                        }
                    } else if (e.action == Action.RIGHT_CLICK_AIR || e.action == Action.RIGHT_CLICK_BLOCK) {
                        ChestCommandsAPI.openInternalMenu(e.player, "ESMenu.yml")
                    }
                    e.isCancelled = true
                }
            }
        }
    }
}