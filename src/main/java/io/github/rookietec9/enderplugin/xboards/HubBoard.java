package io.github.rookietec9.enderplugin.xboards;

import io.github.rookietec9.enderplugin.API.Utils;
import io.github.rookietec9.enderplugin.API.configs.associates.Games;
import io.github.rookietec9.enderplugin.API.configs.associates.User;
import io.github.rookietec9.enderplugin.EnderPlugin;
import io.github.rookietec9.enderplugin.xboards.Board;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import java.lang.management.ManagementFactory;
import java.security.CodeSource;

/**
 * @author Jeremi
 * @version 13.6.4
 * @since 12.8.3
 */
public class HubBoard extends Board {

    private Scoreboard scoreBoard;
    private Objective objective;
    private World world;
    private Player player;

    public HubBoard(Player player) {
        world = Bukkit.getWorld(Utils.Reference.Worlds.HUB);
        this.player = player;

        if (player.getScoreboard().getObjective(DisplaySlot.SIDEBAR) != null && player.getScoreboard().getObjective(DisplaySlot.SIDEBAR).getDisplayName().equalsIgnoreCase(ChatColor.WHITE + "§lH" + ChatColor.LIGHT_PURPLE + "§lU" + ChatColor.WHITE + "§lB")) {
            scoreBoard = player.getScoreboard();
            objective = scoreBoard.getObjective("hub");
        } else {
            CodeSource source = EnderPlugin.getInstance().getClass().getProtectionDomain().getCodeSource();
            String name = source.getLocation().toString().substring(source.getLocation().toString().lastIndexOf("/") + 1);
            String verCycle = name.substring(name.lastIndexOf("-") + 1).replace(EnderPlugin.getInstance().getName(), "").replace(".jar", "");
            for (int i = 0; i < 11; i++) verCycle = verCycle.replace(String.valueOf(i), "");
            verCycle = verCycle.replace(".", "");
            String verNum = name.replace(verCycle, "").substring(name.indexOf("-") + 1).replace(".jar", "");

            long ticksUp = ManagementFactory.getRuntimeMXBean().getUptime();

            String seconds = String.valueOf((ticksUp / 1000) % 60);
            String minutes = String.valueOf((ticksUp / 1000) / 60);
            String hours = "0";
            if (Integer.valueOf(minutes) >= 60) {
                minutes = String.valueOf(Integer.valueOf(minutes) % 60);
                hours = String.valueOf(((ticksUp / 1000) / 60) / 60);
            }
            if (seconds.length() != 2) seconds = "0" + seconds;
            if (minutes.length() != 2) minutes = "0" + minutes;

            Games ga = new Games();

            scoreBoard = Bukkit.getScoreboardManager().getNewScoreboard();
            objective = scoreBoard.registerNewObjective("hub", "dummy");
            objective.setDisplayName(ChatColor.WHITE + "§lH" + ChatColor.LIGHT_PURPLE + "§lU" + ChatColor.WHITE + "§lB");
            objective.setDisplaySlot(DisplaySlot.SIDEBAR);

            objective.getScore("§a").setScore(10);
            objective.getScore(ChatColor.LIGHT_PURPLE + "People online: " + ChatColor.WHITE + Bukkit.getOnlinePlayers().size()).setScore(9);
            objective.getScore("§b").setScore(8);
            objective.getScore(ChatColor.LIGHT_PURPLE + "Rank: " + ChatColor.WHITE + ChatColor.stripColor((new User(player)).getTabName().replace(player.getName(), "").
                    replace("[", "").replace("]", "").replace(" ", ""))).setScore(7);
            objective.getScore(ChatColor.LIGHT_PURPLE + "Version Number§f: §d" + ChatColor.WHITE + verNum.replace(".", "§f.§d")).setScore(6);
            objective.getScore("§c").setScore(5);
            objective.getScore(ChatColor.LIGHT_PURPLE + "Uptime: §d~" + ChatColor.WHITE + hours + ChatColor.LIGHT_PURPLE + ":" + ChatColor.WHITE + minutes + ChatColor.LIGHT_PURPLE + ":" +
                    ChatColor.WHITE + seconds).setScore(4);
            objective.getScore("§d").setScore(3);
            objective.getScore(ChatColor.LIGHT_PURPLE + "ENDCRAFT SERVER").setScore(2);
            objective.getScore(ChatColor.WHITE + ga.getUniversalIP()).setScore(1);
        }
    }

    public void changeTicks(long newTicks) {
        String seconds = String.valueOf((newTicks / 1000) % 60), minutes = String.valueOf((newTicks / 1000) / 60), hours = "0";
        if (Integer.valueOf(minutes) >= 60) {
            minutes = String.valueOf(Integer.valueOf(minutes) % 60);
            hours = String.valueOf(((newTicks / 1000) / 60) / 60);
        }
        if (seconds.length() != 2) seconds = "0" + seconds;
        if (minutes.length() != 2) minutes = "0" + minutes;

        for (String s : scoreBoard.getEntries()) {
            if (ChatColor.stripColor(s).contains("Uptime: ~"))
                scoreBoard.resetScores(s);
        }
        objective.getScore(ChatColor.LIGHT_PURPLE + "Uptime: §d~" + ChatColor.WHITE + hours + ChatColor.LIGHT_PURPLE + ":" + ChatColor.WHITE + minutes +
                ChatColor.LIGHT_PURPLE + ":" + ChatColor.WHITE + seconds).setScore(4);
        player.setScoreboard(scoreBoard);
    }

    public void updatePlayers() {
        for (String s : scoreBoard.getEntries()) {
            if (ChatColor.stripColor(s).contains("People online: "))
                scoreBoard.resetScores(s);
        }
        objective.getScore(ChatColor.LIGHT_PURPLE + "People online: " + ChatColor.WHITE + Bukkit.getOnlinePlayers().size()).setScore(9);
        player.setScoreboard(scoreBoard);
    }

    public void updateRank() {
        for (String s : scoreBoard.getEntries()) {
            if (ChatColor.stripColor(s).contains("Rank: "))
                scoreBoard.resetScores(s);
        }
        objective.getScore(ChatColor.LIGHT_PURPLE + "Rank: " + ChatColor.WHITE + ChatColor.stripColor((new User(player)).getTabName().replace(player.getName(), "").
                replace("[", "").replace("]", "").replace(" ", ""))).setScore(7);
        player.setScoreboard(scoreBoard);
    }

    @Override
    public void init() {
        player.setScoreboard(scoreBoard);
    }

    @Override
    public World getWorld() {
        return Bukkit.getWorld(Utils.Reference.Worlds.HUB);
    }
}