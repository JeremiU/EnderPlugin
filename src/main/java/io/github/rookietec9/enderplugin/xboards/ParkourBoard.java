package io.github.rookietec9.enderplugin.xboards;

import io.github.rookietec9.enderplugin.API.Utils;
import io.github.rookietec9.enderplugin.API.configs.associates.Games;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

/**
 * @author Jeremi
 * @version 14.8.6
 * @since 13.4.4
 */
public class ParkourBoard extends Board {

    private Scoreboard scoreBoard;
    private Objective objective;
    private World world;
    private Player player;

    public ParkourBoard(Player player) {
        world = Bukkit.getWorld(Utils.Reference.Worlds.PARKOUR);
        this.player = player;

        Games g = new Games();

        if (player.getScoreboard().getObjective(DisplaySlot.SIDEBAR) != null && player.getScoreboard().getObjective(DisplaySlot.SIDEBAR).getDisplayName().equalsIgnoreCase(ChatColor.WHITE + "PA" + ChatColor.DARK_GRAY + "RKO" + ChatColor.WHITE + "UR")) {
            scoreBoard = player.getScoreboard();
            objective = scoreBoard.getObjective("parkour");
        } else {
            scoreBoard = Bukkit.getScoreboardManager().getNewScoreboard();
            objective = scoreBoard.registerNewObjective("parkour", "dummy");
            objective.setDisplayName(ChatColor.WHITE + "§lPA" + ChatColor.DARK_GRAY + "§lRKO" + ChatColor.WHITE + "§lUR");
            objective.setDisplaySlot(DisplaySlot.SIDEBAR);

            objective.getScore("§a").setScore(9);
            objective.getScore(ChatColor.DARK_GRAY + "Level: " + ChatColor.WHITE + 1).setScore(8);
            objective.getScore(ChatColor.DARK_GRAY + "Block: " + ChatColor.WHITE + 0).setScore(7);

            objective.getScore("§b").setScore(6);
            objective.getScore(ChatColor.DARK_GRAY + "Attempts: " + ChatColor.WHITE + "0").setScore(5);
            objective.getScore(ChatColor.DARK_GRAY + "Meters Traveled: " + ChatColor.WHITE + "0").setScore(4);
            objective.getScore("§c").setScore(3);
            objective.getScore(ChatColor.DARK_GRAY + "ENDCRAFT SERVER").setScore(2);
            objective.getScore(ChatColor.WHITE + g.getUniversalIP()).setScore(1);
        }
    }

    public void updateLevel(String level) {
        for (String s : scoreBoard.getEntries()) {
            if (s.contains("Level: "))
                scoreBoard.resetScores(s);
        }
        objective.getScore(ChatColor.DARK_GRAY + "Level: " + ChatColor.WHITE + level).setScore(8);
        player.setScoreboard(scoreBoard);
    }

    public void updateBlock(String block) {
        for (String s : scoreBoard.getEntries()) {
            if (s.contains("Block: "))
                scoreBoard.resetScores(s);
        }
        objective.getScore(ChatColor.DARK_GRAY + "Block: " + ChatColor.WHITE + block).setScore(7);
        player.setScoreboard(scoreBoard);
    }

    @Override
    public void init() {
        player.setScoreboard(scoreBoard);
    }

    @Override
    public void deInit() {
        objective.unregister();
        scoreBoard.clearSlot(DisplaySlot.SIDEBAR);
        player.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
    }

    public World getWorld() {
        return world;
    }
}