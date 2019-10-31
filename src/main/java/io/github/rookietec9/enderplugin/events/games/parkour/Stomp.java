package io.github.rookietec9.enderplugin.events.games.parkour;

import io.github.rookietec9.enderplugin.API.Utils;
import io.github.rookietec9.enderplugin.API.configs.associates.Games;
import io.github.rookietec9.enderplugin.xboards.ParkourBoard;
import org.bukkit.DyeColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;

/**
 * @author Jeremi
 * @version 16.2.2
 * @since 11.4.6
 */
public class Stomp implements Listener {

    Location grabLoc = null;
    Games.ParkourInfo parkourInfo = new Games().new ParkourInfo();
    Games games = new Games();

    @EventHandler
    public void grabLoc(PlayerMoveEvent event) {
        ParkourBoard parkourBoard = new ParkourBoard(event.getPlayer());

        if (event.getPlayer().getGameMode() != GameMode.ADVENTURE) return;
        if (event.getPlayer().getWorld().getName().equalsIgnoreCase(Utils.Reference.Worlds.PARKOUR)) {

            Location l = event.getFrom().clone();
            l.setY(l.getY() - 1);
            if (l.getBlock().getType() == Material.WOOL ||
                    l.getBlock().getType() == Material.IRON_BARDING ||
                    l.getBlock().getType() == Material.LADDER || l.getBlock().getType() == Material.BARRIER) {

                boolean b = true;
                grabLoc = event.getFrom();

                if (l.getBlock().getType() == Material.WOOL)
                    switch (DyeColor.getByData(l.getBlock().getData())) {
                        case WHITE: {
                            for (int i = -3; b; i++) {
                                if (games.getConfig().getYaml().get("parkour.blockLoc." + 1 + "." + i) == null) {
                                    if (i > 0) b = false;
                                    continue;
                                }

                                if (Math.abs(parkourInfo.blockLoc(1, i).getBlockX() - l.getBlockX()) < 1 && Math.abs(parkourInfo.blockLoc(1, i).getBlockZ() - l.getBlockZ()) < 1) {
                                    parkourBoard.updateBlock("" + i);
                                    parkourBoard.updateLevel("1");
                                }
                            }
                            return;
                        }
                        case LIME: {
                            for (int i = -3; b; i++) {
                                if (games.getConfig().getYaml().get("parkour.blockLoc." + 2 + "." + i) == null) {
                                    if (i > 0) b = false;
                                    continue;
                                }
                                if (Math.abs(parkourInfo.blockLoc(2, i).getBlockX() - l.getBlockX()) < 1 && Math.abs(parkourInfo.blockLoc(2, i).getBlockZ() - l.getBlockZ()) < 1) {
                                    parkourBoard.updateBlock("" + i);
                                    parkourBoard.updateLevel("2");
                                }
                            }
                            return;
                        }
                        case ORANGE: {
                            for (int i = -3; b; i++) {
                                if (games.getConfig().getYaml().get("parkour.blockLoc." + 3 + "." + i) == null) {
                                    if (i > 0) b = false;
                                    continue;
                                }
                                if (Math.abs(parkourInfo.blockLoc(3, i).getBlockX() - l.getBlockX()) < 1 && Math.abs(parkourInfo.blockLoc(3, i).getBlockZ() - l.getBlockZ()) < 1) {
                                    parkourBoard.updateBlock("" + i);
                                    parkourBoard.updateLevel("3");
                                }
                            }
                            return;
                        }
                        case BLUE: {
                            for (int i = -3; b; i++) {
                                if (games.getConfig().getYaml().get("parkour.blockLoc." + 4 + "." + i) == null) {
                                    if (i > 0) b = false;
                                    continue;
                                }
                                if (Math.abs(parkourInfo.blockLoc(4, i).getBlockX() - l.getBlockX()) < 1 && Math.abs(parkourInfo.blockLoc(4, i).getBlockZ() - l.getBlockZ()) < 1) {
                                    parkourBoard.updateBlock("" + i);
                                    parkourBoard.updateLevel("4");
                                }
                            }
                            return;
                        }
                        case MAGENTA: {
                            for (int i = -3; b; i++) {
                                if (games.getConfig().getYaml().get("parkour.blockLoc." + 5 + "." + i) == null) {
                                    if (i > 0) b = false;
                                    continue;
                                }
                                if (Math.abs(parkourInfo.blockLoc(5, i).getBlockX() - l.getBlockX()) < 1 && Math.abs(parkourInfo.blockLoc(5, i).getBlockZ() - l.getBlockZ()) < 1) {
                                    parkourBoard.updateBlock("" + i);
                                    parkourBoard.updateLevel("5");
                                }
                            }
                        }
                    }
            }
        }
    }

    @EventHandler
    public void run(PlayerMoveEvent event) {
        if (event.getPlayer().getGameMode() != GameMode.ADVENTURE) return;
        if (event.getPlayer().getWorld().getName().equalsIgnoreCase(Utils.Reference.Worlds.PARKOUR)) {

            Location location = event.getTo().clone();
            location.setY(location.getY() - 1);

            if (location.getBlock().getType() == Material.STAINED_CLAY) {
                event.getPlayer().teleport(grabLoc);
                return;
            }

            if (location.getBlock().getType() == Material.STAINED_GLASS) {
                byte color = location.getBlock().getData();

                switch (DyeColor.getByData(color)) {
                    case WHITE: {
                        event.getPlayer().teleport(parkourInfo.returnLoc(2));
                        return;
                    }
                    case LIME: {
                        event.getPlayer().teleport(parkourInfo.returnLoc(3));
                        return;
                    }
                    case ORANGE: {
                        event.getPlayer().teleport(parkourInfo.returnLoc(4));
                        return;
                    }
                    case BLUE: {
                        event.getPlayer().teleport(parkourInfo.returnLoc(5));
                        return;
                    }
                }
            }
        }
    }

    @EventHandler
    public void fall(EntityDamageEvent event) {
        if (event.getEntity().getWorld().getName().equalsIgnoreCase(Utils.Reference.Worlds.PARKOUR)) {
            event.setCancelled(true);
        }
    }
}