package io.github.rookietec9.enderplugin.xboards;

import io.github.rookietec9.enderplugin.API.configs.associates.Games;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

import static io.github.rookietec9.enderplugin.API.Utils.Reference.Teams;
import static io.github.rookietec9.enderplugin.API.Utils.Reference.Worlds;
import static io.github.rookietec9.enderplugin.EnderPlugin.Hashmaps.prisonTicks;

/***
 * @author Jeremi
 * @version 16.8.5
 * @since ?.?.?
 */
public class MurderBoard extends Board {

    private Scoreboard scoreBoard;
    private Objective objective;
    private World world;
    private Player player;

    public MurderBoard(Player player) {
        world = Bukkit.getWorld(Worlds.MURDER);
        this.player = player;

        Games g = new Games();

        if (player.getScoreboard().getObjective(DisplaySlot.SIDEBAR) != null && player.getScoreboard().getObjective(DisplaySlot.SIDEBAR).getDisplayName().equalsIgnoreCase(ChatColor.WHITE + "§lMUR" + ChatColor.DARK_RED + "§lDE" + ChatColor.WHITE + "§lRER")) {
            scoreBoard = player.getScoreboard();
            objective = scoreBoard.getObjective("prison");
        } else {
            int left;

            String minute = String.valueOf(((prisonTicks * 20) / 1200)), second = String.valueOf(((prisonTicks * 20) / 20 - ((prisonTicks * 20) / 1200) * 60L));

            if (second.length() == 1) second = "0" + second;

            if (prisonTicks <= 0) {
                minute = "0";
                second = "00";
            }

            left = Bukkit.getScoreboardManager().getMainScoreboard().getTeam(Teams.goodTeam).getSize();

            scoreBoard = Bukkit.getScoreboardManager().getNewScoreboard();
            objective = scoreBoard.registerNewObjective("prison", "dummy");

            objective.setDisplayName(ChatColor.WHITE + "§lMUR" + ChatColor.DARK_RED + "§lDE" + ChatColor.WHITE + "§lRER");
            objective.setDisplaySlot(DisplaySlot.SIDEBAR);

            objective.getScore("§a").setScore(6);
            objective.getScore(ChatColor.DARK_RED + "Doors Open In: " + ChatColor.WHITE + minute + ChatColor.DARK_RED + ":" + ChatColor.WHITE + second).setScore(5);
            objective.getScore(ChatColor.DARK_RED + "People Left: " + ChatColor.WHITE + left).setScore(4);
            objective.getScore("§b").setScore(3);
            objective.getScore(ChatColor.DARK_RED + "ENDCRAFT SERVER").setScore(2);
            objective.getScore(ChatColor.WHITE + g.getUniversalIP()).setScore(1);
        }
    }

    public void updateTicks(int ticks) {
        for (String s : scoreBoard.getEntries()) if (s.contains("Doors Open In: ")) scoreBoard.resetScores(s);

        String minute = String.valueOf(((ticks * 20) / 1200));
        String second = String.valueOf(((ticks * 20) / 20 - ((ticks * 20) / 1200) * 60L));

        if (second.length() == 1) second = "0" + second;

        if (ticks <= 0) {
            minute = "0";
            second = "00";
        }

        objective.getScore(ChatColor.DARK_RED + "Doors Open In: " + ChatColor.WHITE + minute + ChatColor.DARK_RED + ":" + ChatColor.WHITE + second).setScore(5);
        player.setScoreboard(scoreBoard);
    }

    public void updatePeople(int people) {
        for (String s : scoreBoard.getEntries()) if (s.contains("People Left: ")) scoreBoard.resetScores(s);
        objective.getScore(ChatColor.DARK_RED + "People Left: " + ChatColor.WHITE + people).setScore(4);
        player.setScoreboard(scoreBoard);
    }

    public void updateVisibility() {
        Team team;

        if (scoreBoard.getTeam("all") != null) scoreBoard.getTeam("all").unregister();
        team = scoreBoard.registerNewTeam("all");

        for (Player p : Bukkit.getWorld(Worlds.MURDER).getPlayers()) team.addEntry(p.getName());
        team.setNameTagVisibility(NameTagVisibility.NEVER);
    }

    @Override
    public void init() {
        player.setScoreboard(scoreBoard);
    }

    public World getWorld() {
        return world;
    }
}