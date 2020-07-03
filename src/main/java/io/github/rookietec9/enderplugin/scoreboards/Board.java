package io.github.rookietec9.enderplugin.scoreboards;

import io.github.rookietec9.enderplugin.utils.datamanagers.DataPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

/**
 * @author Jeremi
 * @version 21.3.4
 * @since 16.8.9
 */
public class Board {

    public final Player player;
    private final Objective objective;
    private final World world;
    private final Scoreboard scoreboard;
    private final ChatColor color;
    private final String name;
    private final String[] breaks = {"§a", "§b", "§c", "§d", "§e", "§f", "§k", "§m", "n", "§l", "§o", "§r"};
    private GameMode defaultGameMode = GameMode.ADVENTURE;
    private int breakAmount;

    Board(Player player, String worldName, String name, ChatColor color) {
        this.player = player;
        this.world = Bukkit.getWorld(worldName);
        this.color = color;
        this.name = name;

        if (player.getScoreboard().getObjective(DisplaySlot.SIDEBAR) != null && player.getScoreboard().getObjective(DisplaySlot.SIDEBAR).getDisplayName().equalsIgnoreCase(name)) player.getScoreboard().getObjective(ChatColor.stripColor(name).toLowerCase()).unregister();

        scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        objective = scoreboard.registerNewObjective(ChatColor.stripColor(name).toLowerCase(), "dummy");
        objective.setDisplayName(name);
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        objective.getScore(color + "ENDCRAFT SERVER").setScore(2);
        objective.getScore(ChatColor.WHITE + DataPlayer.getUniversalIP()).setScore(1);
        putBreaks(3);
        if (player.getWorld().getName().equalsIgnoreCase(worldName)) player.setScoreboard(scoreboard);
    }

    Board(Player player, String worldName, String name, ChatColor color, GameMode defaultGameMode) {
        this(player, worldName, name, color);
        this.defaultGameMode = defaultGameMode;
    }

    public static String formatTime(int ticks, ChatColor chatColor, ChatColor semiColor) {
        int hours = ticks / 3600;
        int minutes = ticks / 60 - hours * 60;
        int seconds = ticks - hours * 3600 - minutes * 60;

        String formattedTime = "";

        if (minutes >= 60) formattedTime += chatColor + "" + toVal(hours) + semiColor + ":";
        formattedTime += chatColor + "" + minutes + semiColor + ":" + chatColor + toVal(seconds);

        return formattedTime;
    }

    private static String toVal(int val) {
        if (val < 10) return "0" + val;
        return String.valueOf(val);
    }

    public static String roundInt(int toRound) {
        if (toRound < 1000000 && toRound > 1000 && toRound % 1000 == 0) return toRound / 1000 + "k";
        if (toRound < 1000000 && toRound > 1000 && toRound % 1000 != 0) return "~" + (Math.round((toRound / 1000.0) * 100.0) / 100.0) + "k";

        if (toRound > 1000000 && toRound % 1000000 == 0) return toRound / 1000000 + "m";
        if (toRound > 1000000 && toRound % 1000000 != 0) return "~" + (Math.round((toRound / 1000000.0) * 100.0) / 100.0) + "m";

        return toRound + "";
    }

    public String getName() {
        return name;
    }

    void putBreaks(int... lines) {
        for (int i : lines) {
            if (breakAmount > breaks.length - 1) return;
            objective.getScore(breaks[breakAmount]).setScore(i);
            breakAmount++;
        }
    }

    void putData(String field, String data, int line) {
        objective.getScore(color + field + ChatColor.WHITE + ": " + data).setScore(line);
    }

    void putHeader(String field, boolean useColor, int line) {
        objective.getScore((useColor ? color : ChatColor.WHITE) + field).setScore(line);
    }

    public void updateData(String field, String newData) {
        String toReset = "";
        int line = -1;

        for (String string : scoreboard.getEntries()) {
            if (string.toLowerCase().contains(field.toLowerCase())) {
                toReset = string;
                line = objective.getScore(string).getScore();
            }
        }
        scoreboard.resetScores(toReset);
        objective.getScore(color + field + ChatColor.WHITE + ": " + newData).setScore(line);
    }

    void updateHeader(String newHeader, boolean useColor, String... oldHeaders) {
        int line = -1;
        for (String oldHeader : oldHeaders) {
            for (String string : scoreboard.getEntries()) {
                if (string.toLowerCase().contains(oldHeader.toLowerCase())) {
                    line = objective.getScore(string).getScore();
                    scoreboard.resetScores(string);
                }
            }
        }
        objective.getScore((useColor ? color : ChatColor.WHITE) + newHeader).setScore(line);
    }

    String formatTime(int ticks) {
        return Board.formatTime(ticks, color, ChatColor.WHITE);
    }

    public void init() {
        player.setScoreboard(scoreboard);
    }

    public World getWorld() {
        return world;
    }

    public GameMode getDefaultGameMode() {
        return defaultGameMode;
    }

    public void names(boolean hide) {
        Team team = player.getScoreboard().getTeam("all");

        if (team == null) team = player.getScoreboard().registerNewTeam("all");

        for (Player p : world.getPlayers()) team.addEntry(p.getName());
        team.setNameTagVisibility(hide ? NameTagVisibility.NEVER : NameTagVisibility.ALWAYS);
    }
}