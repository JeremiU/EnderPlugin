package io.github.rookietec9.enderplugin.utils.datamanagers;

import io.github.rookietec9.enderplugin.EnderPlugin;
import io.github.rookietec9.enderplugin.utils.methods.Java;
import io.github.rookietec9.enderplugin.utils.methods.Minecraft;
import io.github.rookietec9.enderplugin.utils.methods.SkullMaker;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.*;

import static io.github.rookietec9.enderplugin.Inventories.*;
import static io.github.rookietec9.enderplugin.Reference.*;

/**
 * @author Jeremi
 * @version 25.4.3
 * @since 22.7.6
 */
public class PartySystem {

    public static final int SLOT_LIST = 1, SLOT_INVITE = 2, SLOT_MANAGE = 3, SLOT_TEAM = 5, SLOT_WARP = 6, SLOT_DISBAND = 7;
    private static final HashSet<PartySystem> systems = new HashSet<>();
    private static final HashMap<UUID, PartySystem> playersInParties = new HashMap<>();
    public static final HashSet<Player> notInParties = new HashSet<>();
    private static int partiers = 0;
    private HashMap<UUID, PartyTeam> partyTeams = new HashMap<>();
    private UUID leaderUUID;
    private HashSet<UUID> players;
    private boolean friendlyFire;

    public HashMap<UUID, Boolean> invites = new HashMap<>();

    private PartySystem(Player owner) {
        this.players = new HashSet<>();
        this.leaderUUID = owner.getUniqueId();
        this.changeTeam(owner, PartyTeam.NONE);
        this.friendlyFire = false;
        players.add(leaderUUID);
        systems.add(this);
        notInParties.remove(owner);
    }

    public static boolean create(Player owner) {
        if (playersInParties.containsKey(owner.getUniqueId())) return false;
        playersInParties.put(owner.getUniqueId(), new PartySystem(owner));
        return true;
    }

    public static int getPlayersCount() {
        return partiers;
    }

    public static HashSet<PartySystem> systems() {
        return systems;
    }

    public static PartySystem getFromPlayer(Player player) {
        return playersInParties.getOrDefault(player.getUniqueId(), null);
    }

    public static Inventory toInvite(Player leader) {
        if (notInParties.size() == 0 || notInParties.size() == 1 && notInParties.contains(leader)) return null;
        Inventory toInv = Bukkit.createInventory(null, (int) Math.ceil(notInParties.size() / 9.0) * 9, "§d§lInvite Players");

        int counter = 0;
        for (Player player : notInParties)
            if (PartySystem.getFromPlayer(leader).invites.getOrDefault(player.getUniqueId(), true))
                toInv.setItem(counter++, ItemWrapper.fromItemStack(new SkullMaker().buildProfile(player.getName())).setName("§f" + DataPlayer.getUser(player).getNickName()).toItemStack());
        return counter > 0 ? toInv : null;
    }

    public static void resetPlayers() {
        notInParties.addAll(Bukkit.getOnlinePlayers());
    }

    public Inventory toManage() {
        if (players.size() == 1) return null;
        Inventory toInv = Bukkit.createInventory(null, (int) Math.ceil(players.size() / 9.0) * 9, "§c§lManage Players");

        int counter = 0;
        for (UUID uuid : players) {
            if (uuid.equals(leaderUUID)) continue;
            toInv.setItem(counter++, ItemWrapper.fromItemStack(new SkullMaker().buildProfile(Bukkit.getPlayer(uuid).getName())).setName("§f" + DataPlayer.getUser(uuid).getNickName()).toItemStack());
        }
        return toInv;
    }

    public Inventory forPlayer(Player player) {
        Inventory menu = Bukkit.createInventory(null, 9, "§d§lParty Menu (Player)");
        menu.setItem(1, new ItemWrapper<>(Material.ACTIVATOR_RAIL, "§c§lLeave Party").toItemStack());
        String teamName = Java.capFirst(getTeam(player).name());
        menu.setItem(3, new ItemWrapper<>(Material.LEATHER_CHESTPLATE, "§7§lChange your team: " + Minecraft.teamColor(teamName, true) + "§l" + teamName).setColor(Minecraft.translateChatColorToColor(Minecraft.teamColor(teamName, true))).toItemStack());
        menu.setItem(5, new ItemWrapper<>(Material.EMPTY_MAP, "§6§lList Players").toItemStack());
        menu.setItem(7, new ItemWrapper<>(Material.FEATHER, "§7§lInvite Players").toItemStack());
        return menu;
    }

    public Inventory forLeader() {
        Player player = Bukkit.getPlayer(leaderUUID);
        String teamName = Java.capFirst(getTeam(player).name());

        Inventory menu = Bukkit.createInventory(null, 9, "§d§lParty Menu (Leader)");
        menu.setItem(SLOT_LIST, new ItemWrapper<>(Material.EMPTY_MAP, "§6§lList Players").toItemStack());
        menu.setItem(SLOT_INVITE, new ItemWrapper<>(Material.FEATHER, "§7§lInvite Players").toItemStack());
        menu.setItem(SLOT_MANAGE, new ItemWrapper<>(Material.DISPENSER, "§c§lManage Players").toItemStack());
        menu.setItem(SLOT_TEAM, new ItemWrapper<>(Material.LEATHER_CHESTPLATE, "§7§lChange your team: " + Minecraft.teamColor(teamName, true) + "§l" + teamName).setColor(Minecraft.translateChatColorToColor(Minecraft.teamColor(teamName, true))).toItemStack());
        menu.setItem(SLOT_WARP, new ItemWrapper<>(Material.ENDER_PEARL, "§2§lWarp Players").toItemStack());
        menu.setItem(SLOT_DISBAND, new ItemWrapper<>(Material.ACTIVATOR_RAIL, "§c§lDisband Party").toItemStack());
        return menu;
    }

    public void addPlayers(Player... players) {
        for (Player player : players) {
            playersInParties.put(player.getUniqueId(), this);
            notInParties.remove(player);
            this.players.add(player.getUniqueId());
            this.invites.remove(player.getUniqueId());
            this.changeTeam(player, PartyTeam.NONE);
        }
        partiers += players.length;
    }

    public void removePlayers(Player... players) {
        for (Player player : players) {
            PartySystem.notInParties.add(player);
            this.players.remove(player.getUniqueId());
            PartySystem.playersInParties.remove(player.getUniqueId());
        }
        partiers -= players.length;
    }

    public void clearPlayers() {
        for (UUID player : this.players) {
            PartySystem.notInParties.add(Bukkit.getPlayer(player));
            PartySystem.playersInParties.remove(player);
        }
        partiers -= players.size();
        players = new HashSet<>();
        partyTeams = new HashMap<>();
        leaderUUID = null;
    }

    public boolean getFF() {
        return friendlyFire;
    }

    public void setFF(boolean friendlyFire) {
        this.friendlyFire = friendlyFire;
    }

    public HashSet<UUID> getPlayers() {
        return this.players;
    }

    public UUID getLeader() {
        return leaderUUID;
    }

    public void setLeader(OfflinePlayer player) {
        this.leaderUUID = player.getUniqueId();
    }

    public int partyCount() {
        return getPlayers().size();
    }

    public void changeTeam(Player player, PartyTeam partyTeam) {
        if (partyTeam == null) partyTeams.remove(player.getUniqueId());
        else partyTeams.put(player.getUniqueId(), partyTeam);
    }

    public PartyTeam getTeam(Player player) {
        return partyTeams.get(player.getUniqueId());
    }

    public void notifyPlayers(String notification, boolean tellLeader) {
        for (UUID uuid : getPlayers())
            if (Bukkit.getPlayer(uuid) != null) {
                if (!uuid.equals(leaderUUID) || tellLeader) Bukkit.getPlayer(uuid).sendMessage(PREFIX_PARTY + notification);
            }
    }

    public void decline(Player decline) {
        invites.put(decline.getUniqueId(), false);
        EnderPlugin.scheduler().runSingleTask(() -> {
            invites.remove(decline.getUniqueId());
            if (Bukkit.getPlayer(leaderUUID) != null) Bukkit.getPlayer(leaderUUID).sendMessage(PREFIX_PARTY + DataPlayer.getUser(decline).getNickName() + " is now available for an invite.");
        }, "PARTY_DECLINE_REMOVE_" + decline.getName(), 30, PREFIX_PARTY.toUpperCase());
    }

    public List<Player> getPlayersFromTeam(PartyTeam partyTeam) {
        List<Player> playerList = new ArrayList<>();
        this.partyTeams.forEach((u,p) -> {
            if (Bukkit.getPlayer(u) != null && p == partyTeam) playerList.add(Bukkit.getPlayer(u));
        });
        return playerList;
    }

    public static List<PartySystem> getPartiesInWorld(String worldName) {
        List<PartySystem> partySystems = new ArrayList<>();
        for (PartySystem partySystem : PartySystem.systems()) if (Bukkit.getPlayer(partySystem.getLeader()) != null && Bukkit.getPlayer(partySystem.getLeader()).getWorld().getName().equalsIgnoreCase(worldName)) partySystems.add(partySystem);
        return partySystems;
    }

    public static PartySystem[] getPartiesInWorldToArray(String worldName) {
        PartySystem[] partySystems = new PartySystem[getPartiesInWorld(worldName).size()];
        partySystems = getPartiesInWorld(worldName).toArray(partySystems);
        return partySystems;
    }

    public static List<Player> getPartiersInWorld(String worldName) {
        Set<Player> players = new HashSet<>();
        getPartiesInWorld(worldName).forEach(x -> x.getPlayers().forEach(y -> players.add(Bukkit.getPlayer(y))));
        return new ArrayList<>(players);
    }

    public static boolean check(Player player, String prefix, int slot, PartyTeam... validTeams) {
        if (player.getWorld().getPlayers().size() < 2) {
            player.sendMessage(prefix + "Not enough players.");
            player.closeInventory();
            return false;
        }
        if (slot != START_GLOBAL && PartySystem.getFromPlayer(player) == null) {
            player.sendMessage(prefix + "You're not in a party.");
            player.closeInventory();
            return false;
        }

        if (slot != START_GLOBAL && PartySystem.getPartiersInWorld(player.getWorld().getName()).size() < 2) {
            player.sendMessage(prefix + "Not enough players in parties.");
            player.closeInventory();
            return false;
        }

        List<Player> validPlayers = new ArrayList<>();
        Set<PartyTeam> teamSet = new HashSet<>();

        for (Player player1 : PartySystem.getPartiersInWorld(player.getWorld().getName()))
            for (PartyTeam validTeam : validTeams) if (PartySystem.getFromPlayer(player1).getTeam(player1) == validTeam) {
                validPlayers.add(player1);
                teamSet.add(validTeam);
            }

        if ((slot == START_PARTY_TEAM || slot == START_PARTIES_TEAMS) && validPlayers.size() != PartySystem.getPartiersInWorld(player.getWorld().getName()).size()) {
            player.sendMessage(prefix + "Not all players are assigned to a correct team.");
            player.closeInventory();
            return false;
        }
        if ((slot == START_PARTY_TEAM || slot == START_PARTIES_TEAMS) && teamSet.size() < 2) {
            player.sendMessage(prefix + "There have to be players on at least 2 teams.");
            player.closeInventory();
            return false;
        }
        return true;
    }

    public enum PartyTeam {GOOD, BAD, RED, BLUE, YELLOW, GREEN, NONE}
}