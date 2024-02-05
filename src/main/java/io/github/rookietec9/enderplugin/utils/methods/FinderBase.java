package io.github.rookietec9.enderplugin.utils.methods;

import io.github.rookietec9.enderplugin.EnderPlugin;
import io.github.rookietec9.enderplugin.configs.DataType;
import io.github.rookietec9.enderplugin.configs.associates.Spawn;
import io.github.rookietec9.enderplugin.events.games.murder.MurderStartEvent;
import io.github.rookietec9.enderplugin.scoreboards.murder.HideBoard;
import io.github.rookietec9.enderplugin.scoreboards.murder.MurderBoard;
import io.github.rookietec9.enderplugin.utils.datamanagers.DataPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

import static io.github.rookietec9.enderplugin.Reference.*;
import static org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

/**
 * @author Jeremi
 * @version 25.7.3
 * @since 18.9.7
 */
public class FinderBase {

    private final String world;
    private final String prefix;
    private final String seeker;

    private final boolean isMurder;

    public FinderBase(boolean isMurder) {
        world = MURDER;
        prefix = isMurder ? PREFIX_MURDER : PREFIX_HIDENNSEEK;
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
            DataPlayer.get(p).reset();
            p.teleport(new Spawn(world).location(), TeleportCause.PLUGIN);

            if (isMurder) {
                DataPlayer.get(p).getBoard(MurderBoard.class).names(false);
                DataPlayer.get(p).getBoard(MurderBoard.class).updateTicks(180);
                DataPlayer.get(p).getBoard(MurderBoard.class).updatePeople(0);
                DataPlayer.get(p).getBoard(MurderBoard.class).updateMode(false, false);
            } else {
                DataPlayer.get(p).getBoard(HideBoard.class).names(false);
                DataPlayer.get(p).getBoard(HideBoard.class).updateTicks();
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

        Player murderer = Teams.getEntries(Teams.TEAM_NEGATIVE).get(0);

        StringBuilder caughtString = new StringBuilder(ChatColor.GRAY + ""), escapeString = new StringBuilder(ChatColor.GRAY + "");

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
            Minecraft.worldBroadcast(world, prefix + "The " + seeker + ", " + DataPlayer.getUser(murderer).getNickName() + " won the game.");
            DataPlayer.get(murderer).increment(winsType);
            for (Player player : caughtList) DataPlayer.get(player).increment(lossesType);
        }
        if (caughtList.size() == 0) {
            Minecraft.worldBroadcast(world, prefix + "The " + escapees + ", " + escapeString.toString() + " won the game.");
            for (Player player : escapedList) DataPlayer.get(player).increment(winsType);
            DataPlayer.get(murderer).increment(lossesType);
        }

        if (caughtList.size() != 0 && escapedList.size() != 0) {
            Minecraft.worldBroadcast(MURDER, PREFIX_MURDER + "Not everyone managed to " + escape + "!");
            Minecraft.worldBroadcast(MURDER, PREFIX_MURDER + escapees + ": " + escapeString.toString());
            Minecraft.worldBroadcast(MURDER, PREFIX_MURDER + "Players caught: " + caughtString.toString());

            for (Player player : caughtList) DataPlayer.get(player).increment(lossesType);
            for (Player player : escapedList) DataPlayer.get(player).increment(winsType);
            DataPlayer.get(murderer).increment(lossesType);
        }

        if (escapedList.size() != 0) Minecraft.worldBroadcast(world, prefix + DataPlayer.getUser(murderer).getNickName() + " was the " + seeker + ".");

        if (isMurder) {
            DataPlayer.prisonEscapeList = new ArrayList<>();
            DataPlayer.prisonCaughtList = new ArrayList<>();
        } else {
            DataPlayer.hoodEscapeList = new ArrayList<>();
            DataPlayer.hoodCaughtList = new ArrayList<>();
        }
        Bukkit.getWorld(world).getPlayers().forEach(p -> DataPlayer.get(p).finishGame());
    }

    public void catchPlayer(Player dead, Player killer) {
        DataPlayer.get(dead).finishGame();

        if (!Teams.contains(Teams.TEAM_POSITIVE, dead) || !Teams.contains(Teams.TEAM_NEGATIVE, killer)) return;

        Teams.remove(Teams.TEAM_POSITIVE, dead);
        if (isMurder) DataPlayer.prisonCaughtList.add(dead);
        else {
            DataPlayer.hoodEscapeList.remove(dead);
            DataPlayer.hoodCaughtList.add(dead);
        }

        dead.teleport(new Spawn(world).location(), TeleportCause.PLUGIN);

            for (Player p : Teams.getEntries(Teams.TEAM_POSITIVE)) {

            if (isMurder) p.getWorld().playSound(p.getLocation(), Sound.CREEPER_HISS, 1, 1);
            p.sendMessage(isMurder ? PREFIX_MURDER + "One of your mates just got caught lacking!" : PREFIX_HIDENNSEEK + DataPlayer.getUser(dead).getNickName() + " was found!");
            if (isMurder) DataPlayer.get(p).getBoard(MurderBoard.class).updatePeople(Teams.getTeam(Teams.TEAM_POSITIVE).getSize());
            else DataPlayer.get(p).getBoard(HideBoard.class).updatePeople(DataPlayer.hoodEscapeList.size());
        }

        if (isMurder) MurderStartEvent.giveWeapon();

        if (Teams.getEntries(Teams.TEAM_POSITIVE).size() == 0) {
            if (isMurder) EnderPlugin.murderBase.reset();
            else EnderPlugin.hoodBase.reset();
            return;
        }

        for (Player p : Bukkit.getWorld(world).getPlayers()) {
            if (isMurder) DataPlayer.get(p).getBoard(MurderBoard.class).updatePeople(Teams.getTeam(Teams.TEAM_POSITIVE).getSize());
            else DataPlayer.get(p).getBoard(HideBoard.class).updatePeople(Teams.getTeam(Teams.TEAM_POSITIVE).getSize() + Teams.getTeam(Teams.TEAM_NEGATIVE).getSize());
        }
    }
}