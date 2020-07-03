package io.github.rookietec9.enderplugin.utils.methods;

import io.github.rookietec9.enderplugin.EnderPlugin;
import io.github.rookietec9.enderplugin.configs.associates.Spawn;
import io.github.rookietec9.enderplugin.events.games.murder.JoinEvent;
import io.github.rookietec9.enderplugin.scoreboards.HideBoard;
import io.github.rookietec9.enderplugin.scoreboards.MurderBoard;
import io.github.rookietec9.enderplugin.utils.datamanagers.DataPlayer;
import io.github.rookietec9.enderplugin.utils.reference.DataType;
import io.github.rookietec9.enderplugin.utils.reference.Prefixes;
import io.github.rookietec9.enderplugin.utils.reference.Teams;
import io.github.rookietec9.enderplugin.utils.reference.Worlds;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.scoreboard.Scoreboard;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * @author Jeremi
 * @version 22.8.0
 * @since 18.9.7
 */
public class FinderBase {

    private final String world;
    private final String prefix;
    private final String seeker;

    private final boolean isMurder;

    public FinderBase(boolean isMurder) {
        world = isMurder ? Worlds.MURDER : Worlds.HIDENSEEK;
        prefix = isMurder ? Prefixes.MURDER : Prefixes.HIDENNSEEK;
        seeker = isMurder ? "murderer" : "seeker";
        this.isMurder = isMurder;
    }

    public void reset() {
        if (isMurder) {
            DataPlayer.prisonHidingSec = 180;
            DataPlayer.prisonFindingSec = 0;

            EnderPlugin.scheduler().cancel("MURDER_TICK");
            EnderPlugin.scheduler().cancel("MURDER_PRE_DOOR_TICK");
            EnderPlugin.scheduler().cancel("MURDER_DOORS_OPEN");
        } else {
            DataPlayer.hoodHidingSec = 60;
            DataPlayer.hoodFindingSec = 180;

            if (EnderPlugin.scheduler().isRunning("HOOD_TICKER_HIDING")) EnderPlugin.scheduler().cancel("HOOD_TICKER_HIDING");
            if (EnderPlugin.scheduler().isRunning("HOOD_TICKER_FINDING")) EnderPlugin.scheduler().cancel("HOOD_TICKER_FINDING");
            if (EnderPlugin.scheduler().isRunning("HOOD_FINISH")) EnderPlugin.scheduler().cancel("HOOD_FINISH");
        }

        for (Player p : Bukkit.getWorld(world).getPlayers()) {
            DataPlayer.getUser(p).reset();
            p.teleport(new Spawn(world).location(), PlayerTeleportEvent.TeleportCause.PLUGIN);

            if (isMurder) {
                DataPlayer.get(p).getBoard(MurderBoard.class).names(false);
                DataPlayer.get(p).getBoard(MurderBoard.class).updateTicks(180);
                DataPlayer.get(p).getBoard(MurderBoard.class).updatePeople(0);
                DataPlayer.get(p).getBoard(MurderBoard.class).updateMode(false, false);
            } else {
                DataPlayer.get(p).getBoard(HideBoard.class).names(false);
                DataPlayer.get(p).getBoard(HideBoard.class).updateTicks(0);
                DataPlayer.get(p).getBoard(HideBoard.class).updatePeople(0);
                DataPlayer.get(p).getBoard(HideBoard.class).updateSeeker("none");
                DataPlayer.get(p).getBoard(HideBoard.class).updateMode(false, false);
            }
        }
        printResults();
    }

    private void printResults() {
        List<Player> caughtList = isMurder ? DataPlayer.prisonCaughtList : DataPlayer.hoodCaughtList;
        List<Player> escapedList = isMurder ? DataPlayer.prisonEscapeList : DataPlayer.hoodEscapeList;

        String escapees = (isMurder ? "escapee" : "hider") + (escapedList.size() == 1 ? "" : "s") ;
        String escape = isMurder ? "escape" : "stay hidden";

        String murdererName = Bukkit.getScoreboardManager().getMainScoreboard().getTeam(Teams.badTeam).getEntries().iterator().next();
        Player murderer = Bukkit.getPlayer(murdererName);

        StringBuilder caughtString = new StringBuilder().append("§7");
        StringBuilder escapeString = new StringBuilder().append("§7");

        for (int i = 0; i < escapedList.size(); i++) {
            escapeString.append(DataPlayer.getUser(escapedList.get(i)).getNickName());
            if (i + 1 < escapedList.size()) escapeString.append(", ");
        }

        for (int i = 0; i < caughtList.size(); i++) {
            caughtString.append(DataPlayer.getUser(caughtList.get(i)).getNickName());
            if (i + 1 < caughtList.size()) caughtString.append(", ");
        }

        Minecraft.worldBroadcast(world, prefix + "Game over:");

        DataType lossesType = isMurder ? DataType.MURDERLOSSES : DataType.HOODLOSSES;
        DataType winsType = isMurder ? DataType.MURDERWINS : DataType.HOODWINS;

        if (escapedList.size() == 0) {
            Minecraft.worldBroadcast(world, prefix + "The " + seeker + ", " + murdererName + " won the game.");
            DataPlayer.get(murderer).increment(winsType);
            for (Player player : caughtList) DataPlayer.get(player).increment(lossesType);
        }
        if (caughtList.size() == 0) {
            Minecraft.worldBroadcast(world, prefix + "The " + escapees + ", " + escapeString.toString() + " won the game.");
            for (Player player : escapedList) DataPlayer.get(player).increment(winsType);
            DataPlayer.get(murderer).increment(lossesType);
        }

        if (caughtList.size() != 0 && escapedList.size() != 0) {
            Minecraft.worldBroadcast(Worlds.MURDER, Prefixes.MURDER + "Not everyone managed to " + escape + "!");
            Minecraft.worldBroadcast(Worlds.MURDER, Prefixes.MURDER + escapees + ": " + escapeString.toString());
            Minecraft.worldBroadcast(Worlds.MURDER, Prefixes.MURDER + "Players caught: " + caughtString.toString());

            for (Player player : caughtList) DataPlayer.get(player).increment(lossesType);
            for (Player player : escapedList) DataPlayer.get(player).increment(winsType);
            DataPlayer.get(murderer).increment(lossesType);
        }

        if (escapedList.size() != 0) Minecraft.worldBroadcast(world, prefix + murdererName + "was the" + seeker + ".");

        if (isMurder) {
            DataPlayer.prisonEscapeList = new ArrayList<>();
            DataPlayer.prisonCaughtList = new ArrayList<>();
        } else {
            DataPlayer.hoodEscapeList = new ArrayList<>();
            DataPlayer.hoodCaughtList = new ArrayList<>();
        }
    }

    public void catchPlayer(EntityDamageByEntityEvent event) {

        Player found = (Player) event.getEntity();
        Player seeker = (Player) event.getDamager();
        Scoreboard board = Bukkit.getScoreboardManager().getMainScoreboard();

        if (!Teams.contains(Teams.goodTeam, found) || !Teams.contains(Teams.badTeam, seeker)) return;

        Teams.remove(Teams.goodTeam, found);
        if (isMurder) DataPlayer.prisonCaughtList.add(found);
        else {
            DataPlayer.hoodEscapeList.remove(found);
            DataPlayer.hoodCaughtList.add(found);
        }

        found.teleport(new Spawn(world).location(), PlayerTeleportEvent.TeleportCause.PLUGIN);

            for (String s : board.getTeam(Teams.goodTeam).getEntries()) {
            Player p = Bukkit.getPlayer(s);

            if (isMurder) p.getWorld().playSound(p.getLocation(), Sound.CREEPER_HISS, 1, 1);
            p.sendMessage(isMurder ? Prefixes.MURDER + "One of your mates just got caught lacking!" : Prefixes.HIDENNSEEK + DataPlayer.getUser(found).getNickName() + " was found!");
            if (isMurder) DataPlayer.get(p).getBoard(MurderBoard.class).updatePeople(Teams.getTeam(Teams.goodTeam).getSize());
            else DataPlayer.get(p).getBoard(HideBoard.class).updatePeople(DataPlayer.hoodEscapeList.size());
        }

        if (isMurder) JoinEvent.giveWeapon();

        if (Bukkit.getScoreboardManager().getMainScoreboard().getTeam(Teams.goodTeam).getSize() == 0) {
            if (isMurder) EnderPlugin.murderBase.reset();
            else EnderPlugin.hoodBase.reset();
            return;
        }

        for (Player p : Bukkit.getWorld(world).getPlayers()) {
            if (isMurder) DataPlayer.get(p).getBoard(MurderBoard.class).updatePeople(Teams.getTeam(Teams.goodTeam).getSize());
            else DataPlayer.get(p).getBoard(HideBoard.class).updatePeople(Teams.getTeam(Teams.goodTeam).getSize() + Teams.getTeam(Teams.badTeam).getSize());
        }
    }
}