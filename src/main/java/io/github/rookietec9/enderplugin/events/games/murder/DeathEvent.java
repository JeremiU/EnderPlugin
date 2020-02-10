package io.github.rookietec9.enderplugin.events.games.murder;

import io.github.rookietec9.enderplugin.API.configs.associates.User;
import io.github.rookietec9.enderplugin.EnderPlugin;
import io.github.rookietec9.enderplugin.xboards.MurderBoard;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.scoreboard.Scoreboard;

import java.util.ArrayList;

import static io.github.rookietec9.enderplugin.API.Utils.Reference.*;

/**
 * @author Jeremi
 * @version 16.8.7
 * @since ?.?.?
 */
public class DeathEvent implements Listener {

    static void reset() {
        for (Player p : Bukkit.getWorld(Worlds.MURDER).getPlayers()) {
            new User(p).clear().clearEffects();
            p.teleport(Bukkit.getWorld(Worlds.MURDER).getSpawnLocation());
            MurderBoard murderBoard = new MurderBoard(p);
            murderBoard.updatePeople(0);
            for (Integer i : JoinEvent.idList) Bukkit.getScheduler().cancelTask(i);
            JoinEvent.idList = new ArrayList<>();
            EnderPlugin.Hashmaps.prisonEscapeList = new ArrayList<>();
            EnderPlugin.Hashmaps.prisonCaughtList = new ArrayList<>();
            EnderPlugin.Hashmaps.prisonTicks = 180;
            murderBoard.updateTicks(EnderPlugin.Hashmaps.prisonTicks);
        }
    }

    @EventHandler
    public void run(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        if (!event.getEntity().getWorld().getName().equalsIgnoreCase(Worlds.MURDER)) return;
        if (JoinEvent.idList.size() == 0) return;

        Player murderer = (Player) event.getDamager();
        Player found = (Player) event.getEntity();
        Scoreboard board = Bukkit.getScoreboardManager().getMainScoreboard();

        if (!board.getTeam(Teams.badTeam).hasEntry(murderer.getName())) return;

        board.getTeam(Teams.goodTeam).removeEntry(found.getName());

        for (String s : board.getTeam(Teams.goodTeam).getEntries()) {
            Player p = Bukkit.getPlayer(s);
            p.getWorld().playSound(p.getLocation(), Sound.CREEPER_HISS, 1, 1);
            p.sendMessage(Prefixs.MURDER + "One of your mates just got caught lacking!");
            MurderBoard murderBoard = new MurderBoard(p);
            murderBoard.updatePeople(board.getTeam(Teams.goodTeam).getSize());
        }

        JoinEvent.bruv(murderer, true);

        for (Player p : Bukkit.getWorld(Worlds.MURDER).getPlayers()) {
            MurderBoard murderBoard = new MurderBoard(p);
            if (JoinEvent.bruv(murderer, false) == 0) {

                String murdererName = board.getTeam(Teams.badTeam).getEntries().iterator().next();
                EscapeEvent.sendGameResults(murdererName);

                reset();
                return;
            }
            murderBoard.updatePeople(JoinEvent.bruv(murderer, false));
        }
    }
}