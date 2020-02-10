package io.github.rookietec9.enderplugin.events.games.murder;

import io.github.rookietec9.enderplugin.API.ActionBar;
import io.github.rookietec9.enderplugin.xboards.MurderBoard;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scoreboard.Scoreboard;

import static io.github.rookietec9.enderplugin.API.Utils.Reference.*;
import static io.github.rookietec9.enderplugin.EnderPlugin.Hashmaps;

/**
 * @author Jeremi
 * @version 16.8.2
 * @since 16.6.2
 */
public class EscapeEvent implements Listener {

    static void sendGameResults(String murdererName) {
        for (Player player : Bukkit.getWorld(Worlds.MURDER).getPlayers()) {
            StringBuilder caughtList = new StringBuilder();
            StringBuilder escapeList = new StringBuilder();

            for (int i = 0; i < Hashmaps.prisonEscapeList.size(); i++) {
                escapeList.append(Hashmaps.prisonEscapeList.get(i).getName());
                if (i + 1 < Hashmaps.prisonEscapeList.size()) escapeList.append("§f, §7");
            }

            for (int i = 0; i < Hashmaps.prisonCaughtList.size(); i++) {
                caughtList.append(Hashmaps.prisonCaughtList.get(i).getName());
                if (i + 1 < Hashmaps.prisonCaughtList.size()) caughtList.append("§f, §7");
            }

            player.sendMessage(Prefixs.MURDER + "Game Over:");

            if (Hashmaps.prisonEscapeList.size() > 0)
                player.sendMessage(Prefixs.MURDER + "Players who escaped: §7" + escapeList.toString());
            else player.sendMessage(Prefixs.MURDER + murdererName + " won the game.");

            if (Hashmaps.prisonCaughtList.size() > 0)
                player.sendMessage(Prefixs.MURDER + "PLayers caught: §7" + caughtList.toString());
        }

    }

    @EventHandler
    public void run(PlayerMoveEvent event) {
        if (!event.getTo().getWorld().getName().equalsIgnoreCase(Worlds.MURDER)) return;

        Player p = event.getPlayer();
        Location plateLoc = event.getTo();

        Location bottomLoc = event.getTo().clone();
        bottomLoc.setY(bottomLoc.getY() - 1);

        if (plateLoc.getBlock().getType().equals(Material.STONE_PLATE)) {
            if (plateLoc.getY() > 82) return;
            if (!p.getGameMode().equals(GameMode.ADVENTURE)) return;
            if (!bottomLoc.getBlock().getType().equals(Material.BEDROCK)) return;

            Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();

            if (scoreboard.getTeam(Teams.goodTeam).hasEntry(p.getName())) {
                scoreboard.getTeam(Teams.goodTeam).removeEntry(p.getName());
                p.teleport(p.getLocation().getWorld().getSpawnLocation());

                Hashmaps.prisonEscapeList.add(p);

                for (Player player : event.getTo().getWorld().getPlayers()) {
                    if (scoreboard.getTeam(Teams.goodTeam).getSize() > 0)
                        player.sendMessage(Prefixs.MURDER + p.getName() + " has escaped!");
                    MurderBoard murderBoard = new MurderBoard(player);
                    murderBoard.updatePeople(scoreboard.getTeam(Teams.goodTeam).getSize());
                }

                if (scoreboard.getTeam(Teams.goodTeam).getSize() == 0) {
                    String murdererName = scoreboard.getTeam(Teams.badTeam).getEntries().iterator().next();
                    sendGameResults(murdererName);
                    DeathEvent.reset();
                }
            }

            if (scoreboard.getTeam(Teams.badTeam).hasEntry(p.getName()))
                ActionBar.send(Prefixs.MURDER + "Go and stop the cellies from escaping!", p);
        }
    }
}