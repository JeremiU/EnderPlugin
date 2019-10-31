package io.github.rookietec9.enderplugin.xboards;

import io.github.rookietec9.enderplugin.API.Utils;
import io.github.rookietec9.enderplugin.API.configs.WizardsBlades;
import io.github.rookietec9.enderplugin.API.configs.associates.Games;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import static io.github.rookietec9.enderplugin.EnderPlugin.Hashmaps;

/**
 * @author Jeremi
 * @version 16.3.7
 * @since 13.4.4
 */
public class WizardsBoard extends Board {
    private Scoreboard scoreBoard;
    private Objective objective;
    private World world;
    private Player player;

    private Games g = new Games();
    private Games.WizardsInfo wizardsInfo = g.new WizardsInfo();

    public WizardsBoard(Player player) {
        world = Bukkit.getWorld(Utils.Reference.Worlds.WIZARDS);
        this.player = player;

        Games g = new Games();
        Games.WizardsInfo wizardsInfo = g.new WizardsInfo();

        if (player.getScoreboard().getObjective(DisplaySlot.SIDEBAR) != null && player.getScoreboard().getObjective(DisplaySlot.SIDEBAR).getDisplayName().equalsIgnoreCase(ChatColor.WHITE + "§lWI" + ChatColor.DARK_GREEN + "§lZAR" + ChatColor.WHITE + "§lDS")) {
            scoreBoard = player.getScoreboard();
            objective = scoreBoard.getObjective("wizards");
        } else {
            scoreBoard = Bukkit.getScoreboardManager().getNewScoreboard();
            objective = scoreBoard.registerNewObjective("wizards", "dummy");
            objective.setDisplayName(ChatColor.WHITE + "§lWI" + ChatColor.DARK_GREEN + "§lZAR" + ChatColor.WHITE + "§lDS");
            objective.setDisplaySlot(DisplaySlot.SIDEBAR);

            objective.getScore("§a").setScore(13);
            objective.getScore(ChatColor.DARK_GREEN + "Blade§f: " + Hashmaps.tempWizardBlade.get(player)).setScore(12);
            objective.getScore("§b").setScore(11);
            objective.getScore(ChatColor.WHITE + "THIS ROUND: ").setScore(10);
            objective.getScore(ChatColor.DARK_GREEN + "Kills: §b§b" + ChatColor.WHITE + Hashmaps.tempWizardKills.get(player)).setScore(9);
            objective.getScore(ChatColor.DARK_GREEN + "Deaths: §b§b" + ChatColor.WHITE + Hashmaps.tempWizardDeaths.get(player)).setScore(8);
            objective.getScore("§c").setScore(7);
            objective.getScore(ChatColor.WHITE + "OVERALL: ").setScore(6);
            objective.getScore(ChatColor.DARK_GREEN + "Kills: §a§b" + ChatColor.WHITE + wizardsInfo.kills(player)).setScore(5);
            objective.getScore(ChatColor.DARK_GREEN + "Deaths: §a§b" + ChatColor.WHITE + wizardsInfo.deaths(player)).setScore(4);
            objective.getScore("§d").setScore(3);
            objective.getScore(ChatColor.DARK_GREEN + "ENDCRAFT SERVER").setScore(2);
            objective.getScore(ChatColor.WHITE + g.getUniversalIP()).setScore(1);
        }
    }

    public void updateTempKills(int kills) {
        Hashmaps.tempWizardKills.put(player, kills);
        for (String s : scoreBoard.getEntries()) {
            if (s.contains("Kills: §b§b"))
                scoreBoard.resetScores(s);
        }
        objective.getScore(ChatColor.DARK_GREEN + "Kills: §b§b" + ChatColor.WHITE + kills).setScore(9);
        player.setScoreboard(scoreBoard);
    }

    public void updateTempDeaths(int deaths) {
        for (String s : scoreBoard.getEntries()) {
            if (s.contains("Deaths: §b§b"))
                scoreBoard.resetScores(s);
        }
        objective.getScore(ChatColor.DARK_GREEN + "Deaths: §b§b" + ChatColor.WHITE + deaths).setScore(8);
        player.setScoreboard(scoreBoard);
    }

    public void reloadKillsAndDeaths() {
        for (String s : scoreBoard.getEntries()) {
            if (s.contains("Kills: §a§b")) scoreBoard.resetScores(s);
            if (s.contains("Deaths: §a§b")) scoreBoard.resetScores(s);
        }
        objective.getScore(ChatColor.DARK_GREEN + "Kills: §a§b" + ChatColor.WHITE + wizardsInfo.kills(player)).setScore(5);
        objective.getScore(ChatColor.DARK_GREEN + "Deaths: §a§b" + ChatColor.WHITE + wizardsInfo.deaths(player)).setScore(4);
        player.setScoreboard(scoreBoard);
    }

    public void updateBlade(String blade) {
        for (String s : scoreBoard.getEntries()) {
            for (WizardsBlades blades : WizardsBlades.values()) {
                if (s.toUpperCase().contains(blades.getBladeName().toUpperCase()))
                    scoreBoard.resetScores(s);
            }
            if (s.contains("null") || s.contains("none")) scoreBoard.resetScores(s);
        }
        objective.getScore(ChatColor.DARK_GREEN + "Blade§f: " + blade.toUpperCase()).setScore(12);
        Hashmaps.tempWizardBlade.put(player, blade.toUpperCase());
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

    @Override
    public World getWorld() {
        return world;
    }
}