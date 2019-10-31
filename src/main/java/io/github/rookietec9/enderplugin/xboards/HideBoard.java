package io.github.rookietec9.enderplugin.xboards;

import io.github.rookietec9.enderplugin.API.configs.associates.Games;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import static io.github.rookietec9.enderplugin.API.Utils.Reference.Teams;
import static io.github.rookietec9.enderplugin.API.Utils.Reference.Worlds;
import static io.github.rookietec9.enderplugin.EnderPlugin.Hashmaps.hoodGame;
import static io.github.rookietec9.enderplugin.EnderPlugin.Hashmaps.hoodHideTicks;
import static io.github.rookietec9.enderplugin.EnderPlugin.Hashmaps.hoodHiding;

/**
 * @author Jeremi
 * @version 14.8.6
 * @since 13.4.4
 */

public class HideBoard extends Board {
    private Scoreboard scoreBoard;
    private Objective objective;
    private World world;
    private Player player;
    private String seekerName;

    public HideBoard(Player player) {
        world = Bukkit.getWorld(Worlds.HIDENSEEK);
        this.player = player;

        Games g = new Games();

        if (player.getScoreboard().getObjective(DisplaySlot.SIDEBAR) != null && player.getScoreboard().getObjective(DisplaySlot.SIDEBAR).getDisplayName().equalsIgnoreCase(ChatColor.WHITE + "§lH" + ChatColor.GOLD + "§lN" + ChatColor.WHITE + "§lS")) {
            scoreBoard = player.getScoreboard();
            objective = scoreBoard.getObjective("hideNSeek");
        } else {
            int left;

            String minute = String.valueOf(((hoodHideTicks * 20) / 1200));
            String second = String.valueOf(((hoodHideTicks * 20) / 20 - ((hoodHideTicks * 20) / 1200) * 60L));

            if (second.length() == 1) second = "0" + second;

            if (hoodHideTicks <= 0) {
                minute = "0";
                second = "00";
            }

            if (Bukkit.getScoreboardManager().getMainScoreboard().getTeam(Teams.badTeam).getPlayers().size() == 1) {
                OfflinePlayer p = Bukkit.getScoreboardManager().getMainScoreboard().getTeam(Teams.badTeam).getPlayers().iterator().next();
                if (p.isOnline()) seekerName = p.getName();
            }
            left = Bukkit.getScoreboardManager().getMainScoreboard().getTeam(Teams.goodTeam).getSize();

            scoreBoard = Bukkit.getScoreboardManager().getNewScoreboard();
            objective = scoreBoard.registerNewObjective("hideNSeek", "dummy");
            objective.setDisplayName(ChatColor.WHITE + "§lHIDE" + ChatColor.GOLD + "§l AND " + ChatColor.WHITE + "§lSEEK");
            objective.setDisplaySlot(DisplaySlot.SIDEBAR);

            objective.getScore("§a").setScore(8);
            objective.getScore(ChatColor.GOLD + "Seeker: " + ChatColor.WHITE + seekerName).setScore(7);
            objective.getScore("§b").setScore(6);

            if (hoodGame) objective.getScore(ChatColor.GOLD + "Phase: " + ChatColor.WHITE + ((hoodHiding) ? "HIDING" : "FINDING")).setScore(5); else
                objective.getScore(ChatColor.GOLD + "Phase: " + ChatColor.WHITE + "Game ended").setScore(5);

            objective.getScore(ChatColor.GOLD + "Time Left: " + ChatColor.WHITE + minute + ChatColor.GOLD + ":" + ChatColor.WHITE + second).setScore(5);
            objective.getScore(ChatColor.GOLD + "People Left: " + ChatColor.WHITE + left).setScore(4);
            objective.getScore("§c").setScore(3);
            objective.getScore(ChatColor.GOLD + "ENDCRAFT SERVER").setScore(2);
            objective.getScore(ChatColor.WHITE + g.getUniversalIP()).setScore(1);
        }
    }

    public void updateTicks(int ticks) {
        for (String s : scoreBoard.getEntries()) if (s.contains("Time Left: ")) scoreBoard.resetScores(s);

        String minute = String.valueOf(((ticks * 20) / 1200));
        String second = String.valueOf(((ticks * 20) / 20 - ((ticks * 20) / 1200) * 60L));

        if (second.length() == 1) second = "0" + second;

        if (ticks <= 0) {
            minute = "0";
            second = "00";
        }

        objective.getScore(ChatColor.GOLD + "Time Left: " + ChatColor.WHITE + minute + ChatColor.GOLD + ":" + ChatColor.WHITE + second).setScore(9);
        player.setScoreboard(scoreBoard);
    }

    public void updatePeople(int people) {
        for (String s : scoreBoard.getEntries()) if (s.contains("People Left: ")) scoreBoard.resetScores(s);
        objective.getScore(ChatColor.GOLD + "People Left: " + ChatColor.WHITE + people).setScore(9);
        player.setScoreboard(scoreBoard);
    }

    public void updateSeeker(String seekerName) {
        this.seekerName = seekerName;
        for (String s : scoreBoard.getEntries()) if (s.contains("Seeker: ")) scoreBoard.resetScores(s);
        objective.getScore(ChatColor.GOLD + "Seeker: " + ChatColor.WHITE + seekerName).setScore(7);
    }

    public void updateMode(boolean isHiding, boolean gameGoing) {
        hoodHiding = isHiding;
        hoodGame = gameGoing;

        for (String s : scoreBoard.getEntries()) if (s.contains("Phase: ")) scoreBoard.resetScores(s);
        if (hoodGame) objective.getScore(ChatColor.GOLD + "Phase: " + ChatColor.WHITE + ((hoodHiding) ? "HIDING" : "FINDING")).setScore(5); else
            objective.getScore(ChatColor.GOLD + "Phase: " + ChatColor.WHITE + "Game ended").setScore(5);
    }

    @Override
    public void init() {
        player.setScoreboard(scoreBoard);
    }

    public World getWorld() {
        return world;
    }
}