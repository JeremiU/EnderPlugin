package io.github.rookietec9.enderplugin.xboards;

import io.github.rookietec9.enderplugin.API.Utils;
import io.github.rookietec9.enderplugin.API.configs.associates.Games;
import io.github.rookietec9.enderplugin.EnderPlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import static io.github.rookietec9.enderplugin.EnderPlugin.Hashmaps.*;

/**
 * @author Jeremi
 * @version 15.4.7
 * @since 14.8.3
 */
public class SpleefBoard extends Board {
    private final Scoreboard scoreBoard;
    private final Objective objective;
    private final World world;
    private final Player player;

    private final Games g = new Games();
    private final Games.SpleefInfo spleefInfo = g.new SpleefInfo();

    private String roundInt(int blocksBroken) {
        if (blocksBroken < 1000000 && blocksBroken > 1000 && blocksBroken % 1000 == 0) return blocksBroken/1000 + "k";
        if (blocksBroken < 1000000 && blocksBroken > 1000 && blocksBroken % 1000 != 0) return "~" + (Math.round((blocksBroken/1000.0) * 100.0) / 100.0)  + "k";

        if (blocksBroken > 1000000 && blocksBroken % 1000000 == 0) return blocksBroken/1000000 + "m";
        if (blocksBroken > 1000000 && blocksBroken % 1000000 != 0) return "~" + (Math.round((blocksBroken/1000000.0) * 100.0) /100.0)  + "m";

        return blocksBroken + "";
    }

    public SpleefBoard(Player player) {
        String minute = String.valueOf(((spleefTicks * 20) / 1200));
        String second = String.valueOf(((spleefTicks * 20) / 20 - ((spleefTicks * 20) / 1200) * 60L));

        if (second.length() == 1) second = "0" + second;

        if (spleefTicks <= 0) {
            minute = "0";
            second = "00";
        }

        world = Bukkit.getWorld(Utils.Reference.Worlds.SPLEEF);
        this.player = player;
        if (player.getScoreboard().getObjective(DisplaySlot.SIDEBAR) != null && player.getScoreboard().getObjective(DisplaySlot.SIDEBAR).getDisplayName().equalsIgnoreCase(ChatColor.WHITE + "§lSP" + ChatColor.BLUE + "§lLE" + ChatColor.WHITE + "§lEF")) {
            scoreBoard = player.getScoreboard();
            objective = scoreBoard.getObjective("spleef");
        } else {
            scoreBoard = Bukkit.getScoreboardManager().getNewScoreboard();
            objective = scoreBoard.registerNewObjective("spleef", "dummy");
            objective.setDisplayName(ChatColor.WHITE + "§lSP" + ChatColor.BLUE + "§lLE" + ChatColor.WHITE + "§lEF");
            objective.setDisplaySlot(DisplaySlot.SIDEBAR);

            objective.getScore("§a").setScore(14);
            objective.getScore(ChatColor.BLUE + "Time Elapsed: " + ChatColor.WHITE + ChatColor.WHITE + minute + ChatColor.BLUE + ":" + ChatColor.WHITE + second).setScore(13);
            objective.getScore(ChatColor.BLUE + "Players Left: " + ChatColor.WHITE + spleefLeft).setScore(12);
            objective.getScore("§b").setScore(11);
            objective.getScore(ChatColor.WHITE + "THIS ROUND: ").setScore(10);
            objective.getScore(ChatColor.BLUE + "Blocks Broken: §b§b" + ChatColor.WHITE + roundInt(tempSpleefBlocks.get(player))).setScore(9);
            objective.getScore("§c").setScore(8);
            objective.getScore(ChatColor.WHITE + "OVERALL: ").setScore(7);
            objective.getScore(ChatColor.BLUE + "Blocks Broken: §a§b" + ChatColor.WHITE + roundInt(spleefInfo.blocksBroken(player))).setScore(6);
            objective.getScore(ChatColor.BLUE + "Rounds Won: §a§b" + ChatColor.WHITE + roundInt(spleefInfo.wins(player))).setScore(5);
            objective.getScore(ChatColor.BLUE + "Rounds Lost: §a§b" + ChatColor.WHITE + roundInt(spleefInfo.losses(player))).setScore(4);
            objective.getScore("§d").setScore(3);
            objective.getScore(ChatColor.BLUE + "ENDCRAFT SERVER").setScore(2);
            objective.getScore(ChatColor.WHITE + g.getUniversalIP()).setScore(1);
        }
    }

    public void updateTicks() {
        for (String s : scoreBoard.getEntries()) {
            if (s.contains("Time Elapsed: ")) scoreBoard.resetScores(s);
        }

        String minute = String.valueOf(((spleefTicks * 20) / 1200));
        String second = String.valueOf(((spleefTicks * 20) / 20 - ((spleefTicks * 20) / 1200) * 60L));

        if (second.length() == 1) second = "0" + second;

        if (spleefTicks <= 0) {
            minute = "0";
            second = "00";
        }

        objective.getScore(ChatColor.BLUE + "Time Elapsed: " + ChatColor.WHITE + ChatColor.WHITE + minute + ChatColor.BLUE + ":" + ChatColor.WHITE + second).setScore(13);
        player.setScoreboard(scoreBoard);
    }

    public void updatePlayers(int players) {
        spleefLeft = players;
        for (String s : scoreBoard.getEntries()) {
            if (s.contains("Players Left: ")) scoreBoard.resetScores(s);
        }
        objective.getScore(ChatColor.BLUE + "Players Left: " + ChatColor.WHITE + spleefLeft).setScore(12);
        player.setScoreboard(scoreBoard);
    }

    public void updateTempBlocks(int blocks) {
        tempSpleefBlocks.put(player, blocks);
        for (String s : scoreBoard.getEntries()) {
            if (s.contains("Blocks Broken: §b§b")) scoreBoard.resetScores(s);
        }
        objective.getScore(ChatColor.BLUE + "Blocks Broken: §b§b" + ChatColor.WHITE + roundInt(blocks)).setScore(9);
        player.setScoreboard(scoreBoard);
    }

    public void reloadStats() {
        for (String s : scoreBoard.getEntries()) {
            if (s.contains("Blocks Broken: §a§b")) {
                scoreBoard.resetScores(s);
                objective.getScore(ChatColor.BLUE + "Blocks Broken: §a§b" + ChatColor.WHITE + roundInt(spleefInfo.blocksBroken(player))).setScore(6);
            }
            if (s.contains("Rounds Won: §a§b")) {
                scoreBoard.resetScores(s);
                objective.getScore(ChatColor.BLUE + "Rounds Won: §a§b" + ChatColor.WHITE + roundInt(spleefInfo.wins(player))).setScore(5);
            }
            if (s.contains("Rounds Lost: §a§b")) {
                scoreBoard.resetScores(s);
                objective.getScore(ChatColor.BLUE + "Rounds Lost: §a§b" + ChatColor.WHITE + roundInt(spleefInfo.losses(player))).setScore(4);
            }
        }
        player.setScoreboard(scoreBoard);
    }

    @Override
    public void init() {
        player.setScoreboard(scoreBoard);
    }

    @Override
    public World getWorld() {
        return world;
    }
}