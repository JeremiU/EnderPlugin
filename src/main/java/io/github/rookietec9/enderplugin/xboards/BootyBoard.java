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

import static io.github.rookietec9.enderplugin.EnderPlugin.Hashmaps.tempBootyDeaths;
import static io.github.rookietec9.enderplugin.EnderPlugin.Hashmaps.tempBootyKills;

/**
 * @author Jeremi
 * @version 13.7.9
 * @since 13.1.7
 */
public class BootyBoard extends Board {

    private Scoreboard scoreBoard;
    private Objective objective;
    private World world;
    private Player player;

    private Games g = new Games();
    private Games.BootyInfo bootyInfo = g.new BootyInfo();

    public BootyBoard(Player player) {
        world = Bukkit.getWorld(Utils.Reference.Worlds.BOOTY);
        this.player = player;
        if (player.getScoreboard().getObjective(DisplaySlot.SIDEBAR) != null && player.getScoreboard().getObjective(DisplaySlot.SIDEBAR).getDisplayName().equalsIgnoreCase(ChatColor.WHITE + "§lBO" + ChatColor.DARK_AQUA + "§lO" + ChatColor.WHITE + "§lTY")) {
            scoreBoard = player.getScoreboard();
            objective = scoreBoard.getObjective("booty");
        } else {
            scoreBoard = Bukkit.getScoreboardManager().getNewScoreboard();
            objective = scoreBoard.registerNewObjective("booty", "dummy");
            objective.setDisplayName(ChatColor.WHITE + "§lBO" + ChatColor.DARK_AQUA + "§lO" + ChatColor.WHITE + "§lTY");
            objective.setDisplaySlot(DisplaySlot.SIDEBAR);

            objective.getScore("§a").setScore(14);
            objective.getScore(ChatColor.DARK_AQUA + "Map: ").setScore(13);
            objective.getScore(ChatColor.WHITE + bootyInfo.type().toUpperCase()).setScore(12);
            objective.getScore("§b").setScore(11);
            objective.getScore(ChatColor.WHITE + "THIS ROUND: ").setScore(10);
            objective.getScore(ChatColor.DARK_AQUA + "Kills: §b§b" + ChatColor.WHITE + tempBootyKills.get(player)).setScore(9);
            objective.getScore(ChatColor.DARK_AQUA + "Deaths: §b§b" + ChatColor.WHITE + tempBootyDeaths.get(player)).setScore(8);
            objective.getScore("§c").setScore(7);
            objective.getScore(ChatColor.WHITE + "OVERALL: ").setScore(6);
            objective.getScore(ChatColor.DARK_AQUA + "Kills: §a§b" + ChatColor.WHITE + bootyInfo.kills(player)).setScore(5);
            objective.getScore(ChatColor.DARK_AQUA + "Deaths: §a§b" + ChatColor.WHITE + bootyInfo.deaths(player)).setScore(4);
            objective.getScore("§d").setScore(3);
            objective.getScore(ChatColor.DARK_AQUA + "ENDCRAFT SERVER").setScore(2);
            objective.getScore(ChatColor.WHITE + g.getUniversalIP()).setScore(1);
        }
    }

    public void updateTempKills(int kills) {
        tempBootyKills.put(player, kills);
        for (String s : scoreBoard.getEntries()) {
            if (s.contains("Kills: §b§b")) scoreBoard.resetScores(s);
        }
        objective.getScore(ChatColor.DARK_AQUA + "Kills: §b§b" + ChatColor.WHITE + kills).setScore(9);
        player.setScoreboard(scoreBoard);
    }

    public void updateTempDeaths(int deaths) {
        tempBootyDeaths.put(player, deaths);
        for (String s : scoreBoard.getEntries()) {
            if (s.contains("Deaths: §b§b")) scoreBoard.resetScores(s);
        }
        objective.getScore(ChatColor.DARK_AQUA + "Deaths: §b§b" + ChatColor.WHITE + deaths).setScore(8);
        player.setScoreboard(scoreBoard);
    }

    public void reloadKillsAndDeaths() {
        for (String s : scoreBoard.getEntries()) {
            if (s.contains("Deaths: §a§b")) scoreBoard.resetScores(s);
            if (s.contains("Kills: §a§b")) scoreBoard.resetScores(s);
        }
        objective.getScore(ChatColor.DARK_AQUA + "Kills: §a§b" + ChatColor.WHITE + bootyInfo.kills(player)).setScore(5);
        objective.getScore(ChatColor.DARK_AQUA + "Deaths: §a§b" + ChatColor.WHITE + bootyInfo.deaths(player)).setScore(4);
        player.setScoreboard(scoreBoard);
    }

    public void updateMap(String map) {
        bootyInfo.setType(map);
        for (String s : scoreBoard.getEntries()) {
            for (String mapName : new String[]{"fancy", "insane", "buffed", "open", "slimy", "spooky", "glassy", "classic", "easy","plain"}) {
                if (s.toUpperCase().contains(mapName.toUpperCase())) scoreBoard.resetScores(s);
            }
        }
        objective.getScore(map.toUpperCase()).setScore(12);
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