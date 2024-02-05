package io.github.rookietec9.enderplugin.utils.methods;

import io.github.rookietec9.enderplugin.utils.datamanagers.DataPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author Jeremi
 * @version 23.4.2
 * @since 18.5.8
 */
public class Teams {
    public static final String TEAM_RED = "team-red";
    public static final String TEAM_BLUE = "team-blue";
    public static final String TEAM_WHITE = "team-white";
    public static final String TEAM_GREEN = "team-green";
    public static final String TEAM_POSITIVE = "team-positive";
    public static final String TEAM_NEGATIVE = "team-negative";

    public static void add(String teamName, Player player) {
        Bukkit.getScoreboardManager().getMainScoreboard().getTeam(teamName).addEntry(player.getName());
    }

    public static void remove(String teamName, Player player) {
        Bukkit.getScoreboardManager().getMainScoreboard().getTeam(teamName).removeEntry(player.getName());
    }

    public static void clear(String teamName) {
        for (String s : Bukkit.getScoreboardManager().getMainScoreboard().getTeam(teamName).getEntries()) Bukkit.getScoreboardManager().getMainScoreboard().getTeam(teamName).removeEntry(s);
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
        getTeam(playerOnTeam).addEntry(player.getName());
    }

    public static void removeE(Player playerOnTeam, Player player) {
        getTeam(playerOnTeam).removeEntry(player.getName());
    }

    public static void addAll(String teamName, Collection<? extends Player> players) {
        players.forEach(x -> add(teamName, x));
    }

    public static List<Player> getEntries(String teamName) {
        List<Player> playerList = new ArrayList<>();
        Bukkit.getScoreboardManager().getMainScoreboard().getTeam(teamName).getEntries().stream().filter(x -> Bukkit.getPlayer(x) != null).forEach(x -> playerList.add(Bukkit.getPlayer(x)));
        return playerList;
    }

    public static List<Player> getEntriesE(Player player) {
        return getEntries(getTeam(player).getName());
    }

    public static void printEntries(String worldName, String prefix, boolean whiteToYellow) {
        for (String teamName : new String[]{TEAM_RED, TEAM_BLUE, TEAM_WHITE, TEAM_GREEN, TEAM_POSITIVE, TEAM_NEGATIVE}) {
            if (Teams.getTeam(teamName).getSize() == 0) continue;
            StringBuilder builder = new StringBuilder(prefix);
            for (int i = 0; i < Teams.getEntries(teamName).size(); i++) {
                builder.append(DataPlayer.getUser(Teams.getEntries(teamName).get(i)).getNickName());
                if (i + 1 < Teams.getEntries(teamName).size()) builder.append(", ");
            }
            builder.append(" joined the ").append(Minecraft.teamColor(teamName, true));
            teamName = teamName.replace("team-", "");
            if (whiteToYellow) teamName = teamName.replace("white", "yellow");
            builder.append(teamName).append(ChatColor.GRAY);
            builder.append(" team.");
            Minecraft.worldBroadcast(worldName, builder.toString());
        }
    }
}