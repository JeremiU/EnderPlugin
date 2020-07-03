package io.github.rookietec9.enderplugin.utils.datamanagers;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 * @author Jeremi
 * @version 22.8.4
 * @since 22.7.6
 */
public class PartySystem {

    private static int partiers = 0;
    private static final HashMap<PartySystem, Player> systems = new HashMap<>();
    private static final HashMap<UUID, PartySystem> playersInParties = new HashMap<>();

    private UUID leaderUUID = null;
    private List<UUID> players;
    private boolean friendlyFire = true;

    private PartySystem(Player owner) {
        this.players = new ArrayList<>();
        systems.put(this, owner);
    }

    public static void create(Player owner) {
        if (playersInParties.containsKey(owner.getUniqueId())) return;
        playersInParties.put(owner.getUniqueId(), new PartySystem(owner));
    }

    public static int getPlayersCount() {
        return partiers;
    }

    public void addPlayers(Player... players) {
        if (getLeader() == null && players.length > 0) setLeader(players[0]);

        for (Player player : players) this.players.add(player.getUniqueId());
        partiers += players.length;
    }

    public void removePlayers(Player... players) {
        for (Player player : players) this.players.remove(player.getUniqueId());
        partiers -= players.length;
    }

    public void clearPlayers() {
        partiers -= players.size();
        players = new ArrayList<>();
        leaderUUID = null;
    }

    public boolean getFF() {
        return friendlyFire;
    }

    public void setFF(boolean friendlyFire) {
        this.friendlyFire = friendlyFire;
    }

    public List<UUID> getPlayers() {
        return this.players;
    }

    public UUID getLeader() {
        return leaderUUID;
    }

    public void setLeader(Player player) {
        this.leaderUUID = player.getUniqueId();
    }

    public static HashMap<PartySystem, Player> systems() {
        return systems;
    }

    public static PartySystem getFromPlayer(Player player) {
        return playersInParties.getOrDefault(player.getUniqueId(), null);
    }

    /*
    public static PartySystem getFromName(String name) {
        for (PartySystem system : systems()) {
            if (system.getName().equalsIgnoreCase(name)) return system;
        }
        return null;
    }
    */

    public void notifyPlayers(String notification) {
        for (UUID uuid : getPlayers()) if (Bukkit.getPlayer(uuid) != null) Bukkit.getPlayer(uuid).sendMessage(notification);
    }
}