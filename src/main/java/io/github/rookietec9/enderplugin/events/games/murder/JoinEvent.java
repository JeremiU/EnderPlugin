package io.github.rookietec9.enderplugin.events.games.murder;

import io.github.rookietec9.enderplugin.EnderPlugin;
import io.github.rookietec9.enderplugin.scoreboards.MurderBoard;
import io.github.rookietec9.enderplugin.utils.datamanagers.DataPlayer;
import io.github.rookietec9.enderplugin.utils.datamanagers.Item;
import io.github.rookietec9.enderplugin.utils.datamanagers.Pair;
import io.github.rookietec9.enderplugin.utils.methods.Java;
import io.github.rookietec9.enderplugin.utils.methods.Minecraft;
import io.github.rookietec9.enderplugin.utils.reference.Prefixes;
import io.github.rookietec9.enderplugin.utils.reference.Teams;
import io.github.rookietec9.enderplugin.utils.reference.Worlds;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.material.Door;
import org.bukkit.material.TrapDoor;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;

/**
 * @author Jeremi
 * @version 22.8.0
 * @since 16.4.9
 */
public class JoinEvent implements Listener {

    public static void giveWeapon() {
        Player murderer = Bukkit.getPlayer(Bukkit.getScoreboardManager().getMainScoreboard().getTeam(Teams.badTeam).getEntries().iterator().next());

        murderer.getInventory().clear();
        Item murderSword = new Item<>(Material.STONE_SWORD, ChatColor.DARK_RED + "Murderer's sword");
        murderSword.addEnch(new Pair<>(Enchantment.DAMAGE_ALL, 100));
        murderSword.addFlag(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_UNBREAKABLE);
        murderSword.setUnbreakable(true);
        murderer.getInventory().setHeldItemSlot(0);
        murderer.getInventory().setItem(0, murderSword.toItemStack());
    }

    @EventHandler
    public void run(PlayerInteractEvent event) {
        World w = event.getPlayer().getWorld();

        if (event.getClickedBlock() == null) return;

        ArrayList<Location> locs = new ArrayList<>();
        locs.add(new Location(w, -292.582, 83, 18.35));
        locs.add(new Location(w, -316.7, 79, 22.417));
        locs.add(new Location(w, -313.301, 83, 13.58));
        locs.add(new Location(w, -293.279, 79, 2.226));
        locs.add(new Location(w, -291.8, 87.5, 13.5, 89.5f, 24.6f));
        locs.add(new Location(w, -312.5, 87, 14.5));

        if (w.getName().equalsIgnoreCase(Worlds.MURDER)) {

            Block button = event.getClickedBlock();
            if (button.getType().equals(Material.STONE_BUTTON)) {
                if (button.getLocation().getBlockX() == -333 && button.getLocation().getBlockY() == 78 && button.getLocation().getBlockZ() == 36) {
                    if (w.getPlayers().size() < 2) {
                        event.getPlayer().sendMessage(Prefixes.MURDER + "You need at least two players!");
                        return;
                    }

                    if (EnderPlugin.scheduler().isRunning("MURDER_TICK") || EnderPlugin.scheduler().isRunning("MURDER_PRE_DOOR_TICK")) {
                        event.getPlayer().sendMessage(Prefixes.MURDER + "Wait until the current game is finished!");
                        return;
                    }

                    int[][] blockLocs = {{-309, 79, 18}, {-294, 79, 5}, {-309, 79, 23}, {-306, 79, 24}, {-313, 83, 2}, {-313, 83, 3}, {-310, 83, 8}, {-311, 83, 8},
                            {-306, 83, 8}, {-299, 83, 13}, {-300, 83, 13}, {-315, 83, 18}, {-308, 83, 27}, {-313, 87, 14}, {-310, 87, 14}, {-308, 87, 17}, {-311, 87, 17}, {-314, 87, 17}};
                    int[][] trapDoorLocs = {{-312, 78, 7}};

                    int[][] redBlocks = {{-302, 78, 23}, {-316, 79, 10}, {-303, 90, 23}, {-303, 90, 24}};

                    for (int[] i : redBlocks) {
                        Block b = new Location(event.getClickedBlock().getWorld(), i[0], i[1], i[2]).getBlock();
                        b.setType(Material.SMOOTH_BRICK);
                    }

                    for (int[] j : blockLocs) {
                        Block b = new Location(event.getClickedBlock().getWorld(), j[0], j[1], j[2]).getBlock();
                        Door door = (Door) b.getState().getData();
                        door.setTopHalf(false);
                        door.setOpen(false);
                        b.setData(door.getData());
                    }

                    for (int[] k : trapDoorLocs) {
                        Block b = new Location(event.getClickedBlock().getWorld(), k[0], k[1], k[2]).getBlock();
                        TrapDoor door = (TrapDoor) b.getState().getData();
                        door.setOpen(false);
                        b.setData(door.getData());
                    }

                    Player tagger = Bukkit.getWorld(Worlds.MURDER).getPlayers().get((int) (Math.random() * Bukkit.getWorld(Worlds.MURDER).getPlayers().size()));

                    for (String s : Teams.getTeam(Teams.badTeam).getEntries()) Teams.remove(Teams.badTeam, Bukkit.getPlayer(s));
                    for (String s : Teams.getTeam(Teams.goodTeam).getEntries()) Teams.remove(Teams.badTeam, Bukkit.getPlayer(s));

                    Teams.add(Teams.badTeam, tagger);

                    for (Player p : w.getPlayers()) {

                        DataPlayer.getUser(p).clearEffects().clear();
                        p.setAllowFlight(false);
                        p.setFlying(false);
                        p.setHealth(20);
                        p.setGameMode(GameMode.ADVENTURE);
                        p.sendMessage(Prefixes.MURDER + "3 minutes until the doors open!");

                        p.teleport(locs.remove(Java.getRandom(0, locs.size() - 1)), PlayerTeleportEvent.TeleportCause.PLUGIN);
                        if (Teams.contains(Teams.badTeam, p)) {
                            DataPlayer.get(p).sendActionMsg(Prefixes.MURDER + "You are the murderer");
                            p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 10 * 20, 10, false, false));
                            p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 10 * 20, 10, false, false));

                            EnderPlugin.scheduler().runSingleTask(JoinEvent :: giveWeapon, "MURDER_WEAPON_GIVE", 8);
                            continue;
                        }
                        Teams.add(Teams.goodTeam, p);
                        DataPlayer.get(p).sendActionMsg(Prefixes.MURDER + "You are innocent");
                    }

                    DataPlayer.prisonHidingSec = 180;
                    DataPlayer.prisonFindingSec = 0;


                    for (Player p : Bukkit.getWorld(Worlds.MURDER).getPlayers()) {
                        DataPlayer.get(p).getBoard(MurderBoard.class).names(true);
                        DataPlayer.get(p).getBoard(MurderBoard.class).updatePeople(Teams.getTeam(Teams.goodTeam).getSize());
                        DataPlayer.get(p).getBoard(MurderBoard.class).updateMode(true, true);
                        DataPlayer.get(p).getBoard(MurderBoard.class).updateTicks(DataPlayer.prisonHidingSec);
                        DataPlayer.get(p).getBoard(MurderBoard.class).updateRole(Teams.contains(Teams.goodTeam, p));
                    }

                    EnderPlugin.scheduler().runRepeatingTask(() -> {
                        DataPlayer.prisonHidingSec--;
                        for (Player p : Bukkit.getWorld(Worlds.MURDER).getPlayers()) DataPlayer.get(p).getBoard(MurderBoard.class).updateTicks(DataPlayer.prisonHidingSec);
                    }, "MURDER_PRE_DOOR_TICK", 0, 1, 180);

                    EnderPlugin.scheduler().runSingleTask(() -> {
                        for (int[] i : redBlocks) {
                            Block b = new Location(event.getClickedBlock().getWorld(), i[0], i[1], i[2]).getBlock();
                            b.setType(Material.REDSTONE_BLOCK);
                        }
                        Minecraft.worldBroadcast(w, Prefixes.MURDER + "the doors have opened!");
                        for (Player p : Bukkit.getWorld(Worlds.MURDER).getPlayers()) DataPlayer.get(p).getBoard(MurderBoard.class).updateMode(false, true);
                    }, "MURDER_DOORS_OPEN", 180);

                    EnderPlugin.scheduler().runRepeatingTask(() -> {
                        DataPlayer.prisonFindingSec++;
                        for (Player p : Bukkit.getWorld(Worlds.MURDER).getPlayers()) DataPlayer.get(p).getBoard(MurderBoard.class).updateTicks(DataPlayer.prisonFindingSec);
                    }, "MURDER_TICK", 180, 1);
                }

                if (button.getLocation().getBlockX() == -329 && button.getLocation().getBlockY() == 78 && button.getLocation().getBlockZ() == 40) {
                    if (Teams.getTeam(Teams.badTeam).getSize() == 0) return;

                    boolean cont = false;

                    for (String s : Teams.getTeam(Teams.badTeam).getEntries()) if (Bukkit.getOfflinePlayer(s).isOnline() && !event.getPlayer().getName().equalsIgnoreCase(s)) cont = true;
                    if (!cont) return;

                    Player player = event.getPlayer();
                    player.sendMessage(Prefixes.MURDER + "Spectating the murderer.");
                    player.setGameMode(GameMode.SPECTATOR);

                    for (String s : Teams.getTeam(Teams.badTeam).getEntries()) player.setSpectatorTarget(Bukkit.getPlayer(s));
                }
            }
        }
    }
}