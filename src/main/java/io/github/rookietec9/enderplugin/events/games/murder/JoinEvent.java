package io.github.rookietec9.enderplugin.events.games.murder;

import io.github.rookietec9.enderplugin.API.ActionBar;
import io.github.rookietec9.enderplugin.API.configs.associates.User;
import io.github.rookietec9.enderplugin.EnderPlugin;
import io.github.rookietec9.enderplugin.xboards.MurderBoard;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.Door;
import org.bukkit.material.TrapDoor;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.Scoreboard;

import java.util.ArrayList;
import java.util.List;

import static io.github.rookietec9.enderplugin.API.Utils.Reference.*;

/**
 * @author Jeremi
 * @version 16.7.8
 * @since 16.4.9
 */
public class JoinEvent implements Listener {

    static List<Integer> idList = new ArrayList<>();

    static int bruv(Player murderer, boolean give) {
        int total = Bukkit.getWorld(Worlds.MURDER).getPlayers().size() - Bukkit.getScoreboardManager().getMainScoreboard().getTeam(Teams.badTeam).getSize();
        int left = Bukkit.getScoreboardManager().getMainScoreboard().getTeam(Teams.goodTeam).getSize();
        ChatColor chatColor = null;

        switch (left) {
            case 1:
                chatColor = ChatColor.DARK_RED;
                break;
            case 2:
                chatColor = ChatColor.GOLD;
                break;
            case 3:
                chatColor = ChatColor.BLUE;
                break;
            case 4:
                chatColor = ChatColor.GREEN;
                break;
            case 0:
                chatColor = ChatColor.DARK_GRAY;
        }
        if (give) {
            murderer.getInventory().clear();
            ItemStack i = new ItemStack(Material.STONE_SWORD);
            i.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 100);
            ItemMeta im = i.getItemMeta();
            im.setDisplayName(chatColor + "Punisher " + ChatColor.WHITE + left + chatColor + "/" + ChatColor.WHITE + total);
            i.setItemMeta(im);
            murderer.getInventory().addItem(i);
        }
        return left;
    }

    @EventHandler
    public void run(PlayerInteractEvent event) {
        World w = event.getPlayer().getWorld();

        if (event.getClickedBlock() == null) return;

        Location[] locs = {
                new Location(w, -292.582, 83, 18.35),
                new Location(w, -316.7, 79, 22.417),
                new Location(w, -313.301, 83, 13.58),
                new Location(w, -293.279, 79, 2.226),
        };

        if (w.getName().equalsIgnoreCase(Worlds.MURDER)) {

            Block button = event.getClickedBlock();
            if (button.getType().equals(Material.STONE_BUTTON)) {
                if (button.getLocation().getBlockX() == -305 && button.getLocation().getBlockY() == 89 && button.getLocation().getBlockZ() == 7) {
                    if (w.getPlayers().size() < 2) {
                        event.getPlayer().sendMessage(Prefixs.MURDER + "You need at least two players!");
                        return;
                    }

                    int[][] blockLocs = {{-309, 79, 18}, {-294, 79, 5}, {-309, 79, 23}, {-306, 79, 24}, {-313, 83, 2}, {-313, 83, 3}, {-310, 83, 8}, {-311, 83, 8}, {-306, 83, 8}, {-299, 83, 13}, {-300, 83, 13}, {-315, 83, 18}, {-308, 83, 27}};
                    int[][] trapDoorLocs = {{-312, 78, 7}};

                    int[][] redBlocks = {{-302, 78, 23}, {-316, 79, 10}};

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
                    Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();

                    for (String s : scoreboard.getTeam(Teams.badTeam).getEntries())
                        scoreboard.getTeam(Teams.badTeam).removeEntry(s);
                    for (String s : scoreboard.getTeam(Teams.goodTeam).getEntries())
                        scoreboard.getTeam(Teams.goodTeam).removeEntry(s);

                    scoreboard.getTeam(Teams.badTeam).addEntry(tagger.getName());

                    int rand = (int) (Math.random() * 3);


                    idList.add(Bukkit.getScheduler().scheduleSyncDelayedTask(EnderPlugin.getInstance(), () -> {
                        for (int[] i : redBlocks) {
                            Block b = new Location(event.getClickedBlock().getWorld(), i[0], i[1], i[2]).getBlock();
                            b.setType(Material.REDSTONE_BLOCK);
                        }
                        for (Player p : w.getPlayers()) {
                            p.sendMessage(Prefixs.MURDER + "the doors have opened!");
                        }
                    }, 180 * 20));

                    for (Player p : w.getPlayers()) {
                        new User(p).clearEffects().clear();
                        p.setGameMode(GameMode.ADVENTURE);
                        p.sendMessage(Prefixs.MURDER + "3 minutes until the doors open!");

                        if (scoreboard.getTeam(Teams.badTeam).hasEntry(p.getName())) {
                            ActionBar.send("§f§lYOU ARE §4§lTHE MURDERER", p);
                            p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 10 * 20, 10, false, false));
                            p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 10 * 20, 10, false, false));
                            Bukkit.getScheduler().scheduleSyncDelayedTask(EnderPlugin.getInstance(), () -> bruv(p, true), 8 * 20);
                            p.teleport(locs[rand]);
                            continue;
                        }
                        scoreboard.getTeam(Teams.goodTeam).addEntry(p.getName());
                        ActionBar.send("§f§lYOU ARE §b§lINNOCENT", p);
                    }

                    for (Player p : Bukkit.getWorld(Worlds.MURDER).getPlayers()) {
                        MurderBoard murderBoard = new MurderBoard(p);
                        murderBoard.updateVisibility();
                        murderBoard.updatePeople(scoreboard.getTeam(Teams.goodTeam).getSize());
                    }

                    idList.add(
                            Bukkit.getScheduler().scheduleSyncRepeatingTask(EnderPlugin.getInstance(), () -> {
                                EnderPlugin.Hashmaps.prisonTicks -= 5;
                                for (Player p : Bukkit.getWorld(Worlds.MURDER).getPlayers()) {
                                    new MurderBoard(p).updateTicks(EnderPlugin.Hashmaps.prisonTicks);
                                }
                            }, 0, 20 * 5));

                    for (int i = 0; i < scoreboard.getTeam(Teams.goodTeam).getSize(); i++) {
                        Player p = Bukkit.getPlayer((String) scoreboard.getTeam(Teams.goodTeam).getEntries().toArray()[i]);
                        if (i == rand) p.teleport(locs[(i + 1) % 3]);
                        else p.teleport(locs[i % 3]);
                    }
                }

                if (button.getLocation().getBlockX() == -301 && button.getLocation().getBlockY() == 89 && button.getLocation().getBlockZ() == 11) {
                    if (Bukkit.getScoreboardManager().getMainScoreboard().getTeam(Teams.badTeam).getEntries().size() == 0)
                        return;

                    boolean cont = false;

                    for (String s : Bukkit.getScoreboardManager().getMainScoreboard().getTeam(Teams.badTeam).getEntries())
                        if (Bukkit.getOfflinePlayer(s).isOnline() && !event.getPlayer().getName().equalsIgnoreCase(s))
                            cont = true;

                    if (!cont) return;

                    Player player = event.getPlayer();
                    player.sendMessage(Prefixs.MURDER + "Spectating the murderer.");
                    player.setGameMode(GameMode.SPECTATOR);

                    for (String s : Bukkit.getScoreboardManager().getMainScoreboard().getTeam(Teams.badTeam).getEntries())
                        player.setSpectatorTarget(Bukkit.getPlayer(s));
                }
            }
        }
    }
}