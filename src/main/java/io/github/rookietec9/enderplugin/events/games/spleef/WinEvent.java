package io.github.rookietec9.enderplugin.events.games.spleef;

import io.github.rookietec9.enderplugin.API.Utils;
import io.github.rookietec9.enderplugin.API.configs.associates.Games;
import io.github.rookietec9.enderplugin.API.configs.associates.User;
import io.github.rookietec9.enderplugin.EnderPlugin;
import io.github.rookietec9.enderplugin.xboards.SpleefBoard;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import static io.github.rookietec9.enderplugin.EnderPlugin.Hashmaps.*;

/**
 * @author Jeremi
 * @version 15.7.5
 * @since 15.0.7
 */
public class WinEvent implements Listener {

    @EventHandler
    public void run(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        int left = 0;
        Player leftPlayer = null;

        if (!player.getWorld().getName().equalsIgnoreCase(Utils.Reference.Worlds.SPLEEF)) return;
        if (player.getGameMode() != GameMode.SURVIVAL) return;

        Location location = player.getLocation().clone();
        location.setY(location.getY() - 1);

        if (location.getBlock().getType() == Material.PRISMARINE) {
            if (location.getBlock().getData() == 1) {
                if (Bukkit.getScoreboardManager().getMainScoreboard().getEntryTeam(player.getName()).getSize() != 1) {
                    player.setGameMode(GameMode.SPECTATOR);

                   if (Bukkit.getScoreboardManager().getMainScoreboard().getEntryTeam(player.getName()).getEntries().iterator().hasNext() &&
                           Bukkit.getPlayer(Bukkit.getScoreboardManager().getMainScoreboard().getEntryTeam(player.getName()).getEntries().iterator().next()) != null)
                    player.setSpectatorTarget(Bukkit.getPlayer(Bukkit.getScoreboardManager().getMainScoreboard().getEntryTeam(player.getName()).getEntries().iterator().next()));
                } else {
                    player.teleport(Bukkit.getWorld(Utils.Reference.Worlds.SPLEEF).getSpawnLocation());
                    Bukkit.getScoreboardManager().getMainScoreboard().getEntryTeam(player.getName()).removeEntry(player.getName());
                }

                for (int i = 0; i < Bukkit.getWorld(Utils.Reference.Worlds.SPLEEF).getPlayers().size(); i++) {
                    Location location1 = ((Player) Bukkit.getWorld(Utils.Reference.Worlds.SPLEEF).getPlayers().toArray()[i]).getLocation();
                    if ((-3 > location1.getBlockZ() && location1.getBlockZ() > -55) && (-2 > location1.getBlockX() && location.getBlockX() > -54)) {
                        left++;
                        leftPlayer = ((Player) Bukkit.getWorld(Utils.Reference.Worlds.SPLEEF).getPlayers().toArray()[i]);
                    }
                }
                for (Player player1 : Bukkit.getWorld(Utils.Reference.Worlds.SPLEEF).getPlayers()) {
                    SpleefBoard spleefBoard = new SpleefBoard(player1);
                    spleefBoard.init();
                    spleefBoard.updatePlayers(left);
                }
                if (leftPlayer != null && left == 1) {
                    Utils.BukkitTools.worldBroadcast(Bukkit.getWorld(Utils.Reference.Worlds.SPLEEF), "§f§lSP§9§lLE§f§lEF §7> " + leftPlayer.getName() + " won the game.");

                    for (Player player1 : Bukkit.getWorld(Utils.Reference.Worlds.SPLEEF).getPlayers()) {
                        player1.teleport(Bukkit.getWorld(Utils.Reference.Worlds.SPLEEF).getSpawnLocation());
                        new User(player1).clear();
                        MineEvent.hashMap.put(player1, 0);
                        Games.SpleefInfo spleefInfo = new Games().new SpleefInfo();
                        if (leftPlayer != null) {
                            spleefInfo.setWins(leftPlayer, spleefInfo.wins(leftPlayer) + 1);
                            if (player1 != leftPlayer) spleefInfo.setLosses(player1, spleefInfo.losses(player1) + 1);
                        }
                        SpleefBoard spleefBoard = new SpleefBoard(player1);
                        spleefBoard.init();
                        spleefBoard.reloadStats();
                        spleefBoard.updatePlayers(0);
                        EnderPlugin.Hashmaps.spleefTicks = 0;
                        spleefBoard.updateTicks();
                        spleefBoard.updateTempBlocks(0);
                        Bukkit.getScheduler().cancelTask(spleefEv1ID);
                        Bukkit.getScheduler().cancelTask(spleefEv2ID);
                        leftPlayer = null;

                        spleefEvent1 = false;
                        spleefEvent2 = false;
                    }

                    for (Block block: MineEvent.blockMap.keySet()) {
                        if (MineEvent.blockMap.get(block)) block.setType(Material.SNOW_BLOCK);
                        else block.setType(Material.AIR);

                        MineEvent.blockMap.remove(block);
                    }
                }
            }
        }
    }
}