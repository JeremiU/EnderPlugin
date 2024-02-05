package io.github.rookietec9.enderplugin.events.games.murder;

import io.github.rookietec9.enderplugin.EnderPlugin;
import io.github.rookietec9.enderplugin.Inventories;
import io.github.rookietec9.enderplugin.scoreboards.Board;
import io.github.rookietec9.enderplugin.scoreboards.murder.HideBoard;
import io.github.rookietec9.enderplugin.scoreboards.murder.MurderBoard;
import io.github.rookietec9.enderplugin.utils.datamanagers.DataPlayer;
import io.github.rookietec9.enderplugin.utils.datamanagers.ItemWrapper;
import io.github.rookietec9.enderplugin.utils.datamanagers.PartySystem;
import io.github.rookietec9.enderplugin.utils.methods.Java;
import io.github.rookietec9.enderplugin.utils.methods.Teams;
import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.material.Door;
import org.bukkit.material.TrapDoor;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;

import static io.github.rookietec9.enderplugin.EnderPlugin.serverLang;
import static io.github.rookietec9.enderplugin.Reference.*;
import static org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import static io.github.rookietec9.enderplugin.utils.datamanagers.PartySystem.PartyTeam;

/**
 * @author Jeremi
 * @version 25.6.2
 * @since 16.4.9
 */
public class MurderStartEvent implements Listener {

    public static void giveWeapon() {
        Player murderer = Teams.getEntries(Teams.TEAM_NEGATIVE).get(0);

        murderer.getInventory().clear();
        ItemWrapper<?> murderSword = new ItemWrapper<>(Material.STONE_SWORD, ChatColor.DARK_RED + "Murderer's sword");
        murderSword.addEnch(Pair.of(Enchantment.DAMAGE_ALL, 1));
        murderSword.addFlag(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_UNBREAKABLE);
        murderSword.setUnbreakable(true);
        murderer.getInventory().setHeldItemSlot(0);
        murderer.getInventory().setItem(0, murderSword.toItemStack());
    }

    public static void startJail(InventoryClickEvent event, boolean randomTeams, List<Player> playerList, PartySystem... partySystems) {
        if (EnderPlugin.scheduler().isRunning("MURDER_TICK") || EnderPlugin.scheduler().isRunning("MURDER_PRE_DOOR_TICK") || EnderPlugin.scheduler().isRunning("HOOD_TICKER_HIDING") || EnderPlugin.scheduler().isRunning("HOOD_TICKER_FINDING")) {
            event.getWhoClicked().sendMessage(PREFIX_MMG_LOBBY + "Wait until the current game is finished!");
            return;
        }

        ArrayList<Location> locs = new ArrayList<>();
        World w = event.getWhoClicked().getWorld();

        for (float[] i : new float[][]{{-292.582f, 83, 18.35f}, {-316.7f, 79, 22.417f}, {-313.301f, 83, 13.58f}, {-293.279f, 79, 2.226f}, {-291.8f, 87.5f, 13.5f, 89.5f, 24.6f}, {-312.5f, 87, 14.5f}, {-302, 74, 25}, {-316, 71, 15}, {-295, 69, 18}}) {
            if (i.length > 3) locs.add(new Location(w, i[0], i[1], i[2], i[3], i[4]));
            else locs.add(new Location(w, i[0], i[1], i[2]));
        }

        int[][] blockLocs = {{-309, 79, 18}, {-294, 79, 5}, {-309, 79, 23}, {-306, 79, 24}, {-313, 83, 2}, {-313, 83, 3}, {-310, 83, 8}, {-311, 83, 8}, {-306, 83, 8}, {-299, 83, 13}, {-300, 83, 13}, {-315, 83, 18}, {-308, 83, 27}, {-313, 87, 14}, {-310, 87, 14}, {-308, 87, 17}, {-311, 87, 17}, {-314, 87, 17}};
        int[][] trapDoorLocs = {{-312, 78, 7}};
        int[][] redBlocks = {{-302, 78, 23}, {-316, 79, 10}, {-303, 90, 23}, {-303, 90, 24}};

        for (int[] i : redBlocks) new Location(w, i[0], i[1], i[2]).getBlock().setType(Material.SMOOTH_BRICK);

        for (int[] j : blockLocs) {
            Block b = new Location(w, j[0], j[1], j[2]).getBlock();
            Door door = (Door) b.getState().getData();
            door.setTopHalf(false);
            door.setOpen(false);
            b.setData(door.getData());
        }

        for (int[] k : trapDoorLocs) {
            Block b = new Location(w, k[0], k[1], k[2]).getBlock();
            TrapDoor door = (TrapDoor) b.getState().getData();
            door.setOpen(false);
            b.setData(door.getData());
        }

        List<Player> partyTagger = new ArrayList<>();
        if (partySystems != null && partySystems.length > 0) for (PartySystem partySystem : partySystems)
            if (partySystem.getPlayersFromTeam(PartySystem.PartyTeam.BAD).size() > 0) partyTagger.addAll(partySystem.getPlayersFromTeam(PartyTeam.BAD)); //npe
        if (partyTagger.size() == 0) partyTagger.add(playerList.get(Java.getRandom(0, playerList.size() - 1)));

        Player tagger = randomTeams ? playerList.get(Java.getRandom(0, playerList.size() - 1)) : partyTagger.get(Java.getRandom(0, partyTagger.size() - 1));

        Teams.clear(Teams.TEAM_NEGATIVE);
        Teams.clear(Teams.TEAM_POSITIVE);
        Teams.add(Teams.TEAM_NEGATIVE, tagger);

        for (Player p : playerList) {
            DataPlayer.get(p).clearEffects();
            DataPlayer.get(p).clear();
            p.setAllowFlight(false);
            p.setFlying(false);
            p.setHealth(20);
            p.setGameMode(GameMode.ADVENTURE);
            p.sendMessage(PREFIX_MURDER + "3 minutes until the doors open!");

            p.teleport(locs.remove(Java.getRandom(0, locs.size() - 1)), TeleportCause.PLUGIN);
            if (Teams.contains(Teams.TEAM_NEGATIVE, p)) {
                DataPlayer.get(p).sendActionMsg(PREFIX_MURDER + "You are the murderer");
                p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 10 * 20, 10, false, false));
                p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 10 * 20, 10, false, false));

                EnderPlugin.scheduler().runSingleTask(MurderStartEvent :: giveWeapon, "MURDER_WEAPON_GIVE", 8, PREFIX_MURDER);
                continue;
            }
            Teams.add(Teams.TEAM_POSITIVE, p);
            DataPlayer.get(p).sendActionMsg(PREFIX_MURDER + "You are innocent");
        }

        DataPlayer.prisonHidingSec = 180;
        DataPlayer.prisonFindingSec = 0;

        for (Player p : playerList) {
            DataPlayer.get(p).getBoard(MurderBoard.class).init();
            DataPlayer.get(p).getBoard(MurderBoard.class).names(true);
            DataPlayer.get(p).getBoard(MurderBoard.class).updatePeople(Teams.getTeam(Teams.TEAM_POSITIVE).getSize());
            DataPlayer.get(p).getBoard(MurderBoard.class).updateMode(true, true);
            DataPlayer.get(p).getBoard(MurderBoard.class).updateTicks(DataPlayer.prisonHidingSec);
            DataPlayer.get(p).getBoard(MurderBoard.class).updateRole(Teams.contains(Teams.TEAM_POSITIVE, p));
        }

        EnderPlugin.scheduler().runRepeatingTask(() -> {
            DataPlayer.prisonHidingSec--;
            for (Player p : playerList) DataPlayer.get(p).getBoard(MurderBoard.class).updateTicks(DataPlayer.prisonHidingSec);
        }, "MURDER_PRE_DOOR_TICK", 0, 1, 180, PREFIX_MURDER);

        EnderPlugin.scheduler().runSingleTask(() -> {
            for (int[] i : redBlocks) new Location(w, i[0], i[1], i[2]).getBlock().setType(Material.REDSTONE_BLOCK);
            for (Player p : playerList) {
                p.sendMessage(PREFIX_MURDER + "the doors have opened!");
                DataPlayer.get(p).getBoard(MurderBoard.class).updateMode(false, true);
            }
        }, "MURDER_DOORS_OPEN", 180, PREFIX_MURDER);

        EnderPlugin.scheduler().runRepeatingTask(() -> {
            DataPlayer.prisonFindingSec++;
            for (Player p : playerList) DataPlayer.get(p).getBoard(MurderBoard.class).updateTicks(DataPlayer.prisonFindingSec);
        }, "MURDER_TICK", 180, 1, PREFIX_MURDER);
    }

    public static void startHood(InventoryClickEvent event, boolean randomTeams, List<Player> playerList, PartySystem... partySystems) {
        if (EnderPlugin.scheduler().isRunning("MURDER_TICK") || EnderPlugin.scheduler().isRunning("MURDER_PRE_DOOR_TICK") || EnderPlugin.scheduler().isRunning("HOOD_TICKER_HIDING") || EnderPlugin.scheduler().isRunning("HOOD_TICKER_FINDING")) {
            event.getWhoClicked().sendMessage(PREFIX_MMG_LOBBY + "Wait until the current game is finished!");
            return;
        }

        List<Player> partyTagger = new ArrayList<>();
        if (partySystems != null && partySystems.length > 0) for (PartySystem partySystem : partySystems)
            if (partySystem.getPlayersFromTeam(PartySystem.PartyTeam.BAD).size() > 0) partyTagger.addAll(partySystem.getPlayersFromTeam(PartySystem.PartyTeam.BAD)); //NPE
        if (partyTagger.size() == 0) partyTagger.add(playerList.get(Java.getRandom(0, playerList.size() - 1)));

        Player tagger = randomTeams ? playerList.get(Java.getRandom(0, playerList.size() - 1)) : partyTagger.get(Java.getRandom(0, partyTagger.size() - 1));

        Teams.clear(Teams.TEAM_NEGATIVE);
        Teams.clear(Teams.TEAM_POSITIVE);
        Teams.add(Teams.TEAM_NEGATIVE, tagger);

        ArrayList<Location> locs = new ArrayList<>();
        World w = event.getWhoClicked().getWorld();

        locs.add(new Location(w, 26, 67, 410));
        locs.add(new Location(w, -13.7, 63, 414.7));
        locs.add(new Location(w, -17, 63, 377));
        locs.add(new Location(w, 16, 63, 371));
        locs.add(new Location(w, 46, 63, 369));
        locs.add(new Location(w, -19, 68, 466));
        locs.add(new Location(w, -71, 66, 476));
        locs.add(new Location(w, -46.792, 67.5, 462.9));
        locs.add(new Location(w, 34, 73, 413));

        Location taggerLoc = new Location(w, 14, 59, 389);

        for (Player p : playerList) {
            DataPlayer.get(p).clearEffects();
            DataPlayer.get(p).clear();
            p.setAllowFlight(false);
            p.setFlying(false);
            p.setHealth(20);
            p.setGameMode(GameMode.ADVENTURE);
            p.sendMessage(PREFIX_HIDENNSEEK + "1 minute until the seeker comes out!");

            if (Teams.contains(Teams.TEAM_NEGATIVE, p)) {
                p.teleport(taggerLoc);
                DataPlayer.get(p).sendActionMsg(PREFIX_HIDENNSEEK + "You are the tagger");
                continue;
            }
            p.teleport(locs.remove(Java.getRandom(0, locs.size() - 1)), PlayerTeleportEvent.TeleportCause.PLUGIN);
            Teams.add(Teams.TEAM_POSITIVE, p);
            DataPlayer.get(p).sendActionMsg(PREFIX_HIDENNSEEK + "You are innocent");
        }

        DataPlayer.hoodHidingSec = 60;
        DataPlayer.hoodFindingSec = 180;

        DataPlayer.hoodEscapeList.addAll(Bukkit.getWorld(MURDER).getPlayers());
        DataPlayer.hoodEscapeList.remove(tagger);

        for (Player p : playerList) {
            DataPlayer.get(p).getBoard(HideBoard.class).init();
            DataPlayer.get(p).getBoard(HideBoard.class).names(true);
            DataPlayer.get(p).getBoard(HideBoard.class).updatePeople(Teams.getTeam(Teams.TEAM_POSITIVE).getSize());
            DataPlayer.get(p).getBoard(HideBoard.class).updateMode(true, true);
            DataPlayer.get(p).getBoard(HideBoard.class).updateTicks();
            DataPlayer.get(p).getBoard(HideBoard.class).updateSeeker(DataPlayer.getUser(tagger).getNickName());
        }
        EnderPlugin.scheduler().runRepeatingTask(() -> {
            for (Player p : Bukkit.getWorld(MURDER).getPlayers()) {
                DataPlayer.get(p).getBoard(HideBoard.class).updateTicks();
                DataPlayer.get(p).getBoard(HideBoard.class).updatePeople(Teams.getTeam(Teams.TEAM_NEGATIVE).getSize());
                DataPlayer.get(p).getBoard(HideBoard.class).updateSeeker(DataPlayer.getUser(tagger).getNickName());

                if (DataPlayer.hoodHidingSec > 0) DataPlayer.get(p).sendActionMsg(Board.formatTime(DataPlayer.hoodHidingSec, ChatColor.GOLD, ChatColor.WHITE) + " till start.");
                else DataPlayer.get(p).sendActionMsg(PREFIX_HIDENNSEEK + (!p.equals(tagger) ? "Stay away from the tagger" : "Catch all the players before the timer runs out"));
            }
            DataPlayer.hoodHidingSec--;
        }, "HOOD_TICKER_HIDING", 0, 1, 61, PREFIX_HIDENNSEEK);

        EnderPlugin.scheduler().runSingleTask(() -> tagger.teleport(new Location(w, 11, 63, 410, 84, 1.05f)), "HOOD_TELEPORT", 60, PREFIX_HIDENNSEEK);
        EnderPlugin.scheduler().runRepeatingTask(() -> {
            for (Player player1 : Bukkit.getWorld(MURDER).getPlayers()) {
                DataPlayer.get(player1).getBoard(HideBoard.class).updateTicks();
                DataPlayer.get(player1).getBoard(HideBoard.class).updateMode(false, true);
            }
            DataPlayer.hoodFindingSec--;
        }, "HOOD_TICKER_FINDING", 60, 1, 180, PREFIX_HIDENNSEEK);

        EnderPlugin.scheduler().runSingleTask(() -> EnderPlugin.hoodBase.reset(), "HOOD_FINISH", 240, PREFIX_HIDENNSEEK);
    }

    @EventHandler
    public void run(PlayerInteractEvent event) {
        World w = event.getPlayer().getWorld();

        if (event.getClickedBlock() == null) return;

        if (w.getName().equalsIgnoreCase(MURDER)) {

            Block button = event.getClickedBlock();
            if (button.getType().equals(Material.STONE_BUTTON)) {
                if (button.getLocation().getBlockX() == -333 && button.getLocation().getBlockY() == 78 && button.getLocation().getBlockZ() == 36) {
                    if (w.getPlayers().size() < 2) {
                        event.getPlayer().sendMessage(PREFIX_MMG_LOBBY + "You need at least two players!");
                        return;
                    }
                    event.getPlayer().openInventory(Inventories.MURDER_MAP);
                    return;
                }

                if (button.getLocation().getBlockX() == -329 && button.getLocation().getBlockY() == 78 && button.getLocation().getBlockZ() == 40) {
                    if (Teams.getTeam(Teams.TEAM_NEGATIVE).getSize() == 0) return;

                    boolean cont = false;

                    for (String s : Teams.getTeam(Teams.TEAM_NEGATIVE).getEntries()) if (Bukkit.getOfflinePlayer(s).isOnline() && !event.getPlayer().getName().equalsIgnoreCase(s)) cont = true;
                    if (!cont) return;

                    Player player = event.getPlayer();
                    player.sendMessage(PREFIX_MMG_LOBBY + "Spectating the murderer.");
                    player.setGameMode(GameMode.SPECTATOR);

                    for (String s : Teams.getTeam(Teams.TEAM_NEGATIVE).getEntries()) player.setSpectatorTarget(Bukkit.getPlayer(s));
                }
            }
        }
    }

    @EventHandler
    public void run(InventoryClickEvent event) {
        if (!Java.argWorks(event.getInventory().getName(), Inventories.START_HNS.getName(), Inventories.START_MURDER.getName())) return;
        boolean murderer = event.getInventory().getName().equalsIgnoreCase(Inventories.START_MURDER.getName());

        event.setCancelled(true);
        Player player = (Player) event.getWhoClicked();

        if (!PartySystem.check(player, PREFIX_MMG_LOBBY, event.getSlot(), PartyTeam.BAD, PartyTeam.GOOD)) return;

        switch (event.getSlot()) {
            case Inventories.START_PARTY_RANDOM, Inventories.START_PARTY_TEAM -> {
                event.getWhoClicked().closeInventory();
                if (PartySystem.getFromPlayer(player) == null) {
                    event.getWhoClicked().sendMessage(serverLang().getErrorMsg() + "!!!you are not in a party.");
                    return;
                }
                List<Player> playerList = new ArrayList<>();
                PartySystem.getFromPlayer(player).getPlayers().forEach(x -> {
                    if (Bukkit.getPlayer(x) != null) playerList.add(Bukkit.getPlayer(x));
                });
                if (playerList.size() < 2) {
                    event.getWhoClicked().sendMessage(serverLang().getErrorMsg() + "!!!Not enough players. Make sure your party is big enough.");
                    return;
                }
                if (murderer) startJail(event, event.getSlot() == Inventories.START_PARTY_RANDOM, playerList, event.getSlot() == Inventories.START_PARTY_RANDOM ? null : PartySystem.getFromPlayer(player));
                else startHood(event, event.getSlot() == Inventories.START_PARTY_RANDOM, playerList, event.getSlot() == Inventories.START_PARTY_RANDOM ? null : PartySystem.getFromPlayer(player));
            }
            case Inventories.START_PARTIES_RANDOM, Inventories.START_PARTIES_TEAMS -> {
                event.getWhoClicked().closeInventory();
                if (PartySystem.getFromPlayer(player) == null) {
                    event.getWhoClicked().sendMessage(serverLang().getErrorMsg() + "!!!you are not in a party.");
                    return;
                }
                List<Player> playerList = new ArrayList<>();
                List<PartySystem> partySystems = new ArrayList<>();
                for (PartySystem partySystem : PartySystem.systems()) {
                    if (Bukkit.getPlayer(partySystem.getLeader()) != null && Bukkit.getPlayer(partySystem.getLeader()).getWorld().getName().equalsIgnoreCase(MURDER)) {
                        partySystem.getPlayers().stream().filter(x -> Bukkit.getPlayer(x) != null).forEach(x -> playerList.add(Bukkit.getPlayer(x)));
                        partySystems.add(partySystem);
                    }
                }
                if (playerList.size() < 2) {
                    event.getWhoClicked().sendMessage(serverLang().getErrorMsg() + "!!!Not enough players. Make sure leaders are in the mmg lobby.");
                    return;
                }
                PartySystem[] partySystems_ = new PartySystem[partySystems.size()];
                partySystems_ = partySystems.toArray(partySystems_);
                if (murderer) startJail(event, event.getSlot() == Inventories.START_PARTIES_RANDOM, playerList, event.getSlot() == Inventories.START_PARTIES_RANDOM ? null : partySystems_);
                else startHood(event, event.getSlot() == Inventories.START_PARTIES_RANDOM, playerList, event.getSlot() == Inventories.START_PARTIES_RANDOM ? null : partySystems_);
            }
            case Inventories.START_GLOBAL -> {
                event.getWhoClicked().closeInventory();
                if (murderer) startJail(event, true, Bukkit.getWorld(MURDER).getPlayers());
                else startHood(event, true, Bukkit.getWorld(MURDER).getPlayers());
            }
        }
    }
}