package io.github.rookietec9.enderplugin.xboards;

import io.github.rookietec9.enderplugin.API.configs.associates.Games;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import static io.github.rookietec9.enderplugin.API.Utils.Reference.Worlds;
import static io.github.rookietec9.enderplugin.EnderPlugin.Hashmaps.*;

/**
 * @author Jeremi
 * @version 13.9.7
 * @since 13.4.4
 */
public class CTFBoard extends Board {
    private Scoreboard scoreBoard;
    private Objective objective;
    private World world;
    private Player player;

    public CTFBoard(Player player) {
        world = Bukkit.getWorld(Worlds.CTF);
        this.player = player;
        Games g = new Games();

        if (player.getScoreboard().getObjective(DisplaySlot.SIDEBAR) != null && player.getScoreboard().getObjective(DisplaySlot.SIDEBAR).getDisplayName().equalsIgnoreCase(ChatColor.WHITE + "§lC" + ChatColor.YELLOW + "§lT" + ChatColor.WHITE + "§lF")) {
            scoreBoard = player.getScoreboard();
            objective = scoreBoard.getObjective("ctf");
        } else {

            scoreBoard = Bukkit.getScoreboardManager().getNewScoreboard();
            objective = scoreBoard.registerNewObjective("ctf", "dummy");
            objective.setDisplayName(ChatColor.WHITE + "§lC" + ChatColor.YELLOW + "§lT" + ChatColor.WHITE + "§lF");
            objective.setDisplaySlot(DisplaySlot.SIDEBAR);

            objective.getScore("§a").setScore(12);
            objective.getScore(ChatColor.YELLOW + "SCORE: " + ChatColor.RED + "§l" + redScore + ChatColor.WHITE + "§l:" + ChatColor.BLUE + "§l" + blueScore).setScore(11);
            objective.getScore("§b").setScore(10);
            objective.getScore(ChatColor.YELLOW + "FLAGS:").setScore(9);
            objective.getScore(ChatColor.YELLOW + "Red flag : " + ChatColor.WHITE + (ctfRedSafe ? "SAFE" : "STOLEN")).setScore(8);
            objective.getScore(ChatColor.YELLOW + "Blue flag : " + ChatColor.WHITE + (ctfBlueSafe ? "SAFE" : "STOLEN")).setScore(7);
            objective.getScore("§c").setScore(6);
            objective.getScore(ChatColor.YELLOW + "Kills: " + ChatColor.WHITE + tempCTFKills.get(player)).setScore(5);
            objective.getScore(ChatColor.YELLOW + "Deaths: " + ChatColor.WHITE + tempCTFDeaths.get(player)).setScore(4);
            objective.getScore("§d").setScore(3);
            objective.getScore(ChatColor.YELLOW + "ENDCRAFT SERVER").setScore(2);
            objective.getScore(ChatColor.WHITE + g.getUniversalIP()).setScore(1);
        }
    }

    public void winGame() {} //TODO Delete flags (9/8/7), replace

    public void activateGame() {} //TODO place back 9/8/7

    public void updateTempKills(int kills) {
        for (String s : scoreBoard.getEntries())
            if (ChatColor.stripColor(s).contains("Kills:")) scoreBoard.resetScores(s);
        objective.getScore(ChatColor.YELLOW + "Kills: " + ChatColor.WHITE + kills).setScore(5);
        player.setScoreboard(scoreBoard);
    }

    public void updateTempDeaths(int deaths) {
        for (String s : scoreBoard.getEntries())
            if (ChatColor.stripColor(s).contains("Deaths:")) scoreBoard.resetScores(s);
        objective.getScore(ChatColor.YELLOW + "Deaths: " + ChatColor.WHITE + deaths).setScore(4);
        player.setScoreboard(scoreBoard);
    }

    public void updateRedFlag(boolean safety) {
        ctfRedSafe = safety;
        for (String s : scoreBoard.getEntries())
            if (ChatColor.stripColor(s).contains("Red flag : ")) scoreBoard.resetScores(s);
        objective.getScore(ChatColor.YELLOW + "Red flag : " + ChatColor.WHITE + (safety ? "SAFE" : "STOLEN")).setScore(8);
        player.setScoreboard(scoreBoard);
    }

    public void updateBlueFlag(boolean safety) {
        ctfBlueSafe = safety;
        for (String s : scoreBoard.getEntries())
            if (ChatColor.stripColor(s).contains("Blue flag : ")) scoreBoard.resetScores(s);
        objective.getScore(ChatColor.YELLOW + "Blue flag : " + ChatColor.WHITE + (safety ? "SAFE" : "STOLEN")).setScore(7);
        player.setScoreboard(scoreBoard);
    }

    @Override
    public void init() {
        player.setScoreboard(scoreBoard);
    }

    Scoreboard getBoard() {
        return scoreBoard;
    }

    public World getWorld() {
        return world;
    }
}