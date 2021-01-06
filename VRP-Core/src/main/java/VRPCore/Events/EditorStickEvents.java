package VRPCore.Events;

import VRPCore.VRPCore;
import de.tr7zw.nbtapi.NBTItem;
import me.filoghost.chestcommands.api.ChestCommandsAPI;
import org.bukkit.*;
import org.bukkit.block.Banner;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;
import org.bukkit.block.data.BlockData;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;
import org.bukkit.material.Directional;
import org.bukkit.material.Dye;

public class EditorStickEvents implements Listener {
    VRPCore plugin;

    public EditorStickEvents(VRPCore main){
        this.plugin = main;
    }


    @EventHandler
    public void onStickInteraction(PlayerInteractEvent e){
        ItemStack hand = e.getItem();
        if(hand != null && hand.getType() == Material.STICK) {
            NBTItem nbt = new NBTItem(hand);
            if(e.getPlayer().hasPermission("vrpcore.es")) {
                if (nbt.hasKey("isEditorStick")) {
                    if (e.getAction() == Action.LEFT_CLICK_BLOCK) {


                        switch (plugin.playerManager.GetPlayer(e.getPlayer().getUniqueId()).StickMode) {
                            case None:
                                e.getPlayer().sendMessage(ChatColor.YELLOW + "Select a Editor Stick Mode First!");
                                break;


                            case LightEditor:
                                e.getPlayer().sendMessage(ChatColor.GREEN + "Light Editor!");

                                if(e.getClickedBlock().getType() == Material.REDSTONE_LAMP){
                                    e.getPlayer().sendMessage("Added block to array");
                                    plugin.lightManager.DayNightLamps.add(e.getClickedBlock());
                                }
                                break;

                            case Intersection:
                                Location spawnLocation = e.getClickedBlock().getRelative(e.getBlockFace()).getLocation();
                                Block b = e.getClickedBlock().getWorld().getBlockAt(spawnLocation);

                                b.setType(Material.BLACK_WALL_BANNER);

                                org.bukkit.block.Banner banner = (org.bukkit.block.Banner)b.getState();
                                banner.setBaseColor(DyeColor.BLACK);
                                banner.addPattern(new Pattern(DyeColor.RED, PatternType.HALF_HORIZONTAL));
                                banner.addPattern(new Pattern(DyeColor.BLACK, PatternType.BORDER));

                                banner.update(true);

                                BlockData blockData = b.getBlockData();
                                if (blockData instanceof Directional) {
                                    ((Directional) blockData).setFacingDirection(e.getBlockFace());
                                    b.setBlockData(blockData);
                                }

                                break;

                            case WaypointEditor:
                                e.getPlayer().sendMessage(ChatColor.GREEN + "Waypoint Editor!");
                                break;
                        }
                    } else if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
                        ChestCommandsAPI.openInternalMenu(e.getPlayer(), "ESMenu.yml");
                    }
                    e.setCancelled(true);
                }
            }
        }
    }
}
