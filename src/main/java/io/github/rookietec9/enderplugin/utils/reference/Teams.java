package io.github.rookietec9.enderplugin.utils.reference;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;

/**
 * @author Jeremi
 * @version 22.8.0
 * @since 18.5.8
 */
public class Teams {
    public static final String redTeam = "team-red";
    public static final String blueTeam = "team-blue";
    public static final String whiteTeam = "team-white";
    public static final String greenTeam = "team-green";
    public static final String goodTeam = "team-positive";
    public static final String badTeam = "team-negative";

    public static void add(String teamName, Player player) {
        Bukkit.getScoreboardManager().getMainScoreboard().getTeam(teamName).addEntry(player.getName());
    }

    public static void remove(String teamName, Player player) {
        Bukkit.getScoreboardManager().getMainScoreboard().getTeam(teamName).removeEntry(player.getName());
    }

    public static boolean contains(String teamName, Player player) {
        return Bukkit.getScoreboardManager().getMainScoreboard().getTeam(teamName).hasEntry(player.getName());
    }

    public static Team getTeam(Player player) {
        return Bukkit.getScoreboardManager().getMainScoreboard().getEntryTeam(player.getName());
    }

    public static Team getTeam(String teamName) {
        return Bukkit.getScoreboardManager().getMainScoreboard().getTeam(teamName);
    }

    public static void addE(Player playerOnTeam, Player player) {
        Bukkit.getScoreboardManager().getMainScoreboard().getEntryTeam(playerOnTeam.getName()).addEntry(player.getName());
    }

    public static void removeE(Player playerOnTeam, Player player) {
        Bukkit.getScoreboardManager().getMainScoreboard().getEntryTeam(playerOnTeam.getName()).removeEntry(player.getName());
    }

}