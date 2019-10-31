package io.github.rookietec9.enderplugin.xboards;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

/**
 * @author Jeremi
 * @version 13.4.9
 * @since ?.?.?
 */
public class Board {
    public Player player;
    public Objective objective;
    public World world;
    public String name;
    private Scoreboard scoreboard;

    public void init() {
        player.setScoreboard(scoreboard);
    }

    public void deInit() {
        objective.unregister();
        scoreboard.clearSlot(DisplaySlot.SIDEBAR);
        player.setScoreboard(scoreboard);
    }

    public Scoreboard getScoreboard() {
        return scoreboard;
    }

    public void setScoreboard(Scoreboard scoreboard) {this.scoreboard = scoreboard;}

    public World getWorld() {
        return world;
    }

}