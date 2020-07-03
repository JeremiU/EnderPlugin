package io.github.rookietec9.enderplugin.events.games.ctf;

import io.github.rookietec9.enderplugin.EnderPlugin;
import io.github.rookietec9.enderplugin.scoreboards.CTFBoard;
import io.github.rookietec9.enderplugin.utils.datamanagers.DataPlayer;
import io.github.rookietec9.enderplugin.utils.datamanagers.Item;
import io.github.rookietec9.enderplugin.utils.methods.Minecraft;
import io.github.rookietec9.enderplugin.utils.reference.Prefixes;
import io.github.rookietec9.enderplugin.utils.reference.Teams;
import io.github.rookietec9.enderplugin.utils.reference.Worlds;
import org.bukkit.*;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.util.List;

/**
 * @author Jeremi
 * @version 22.8.0
 * @since 20.6.1
 */
public class CTFJoinEvent implements Listener {

    public static void giveKit(Player player) {
        DataPlayer.getUser(player).reset();

        ChatColor chatColor = Teams.contains(Teams.blueTeam, player) ? ChatColor.BLUE : ChatColor.RED;
        Color color = Teams.contains(Teams.blueTeam, player) ? Color.BLUE : Color.RED;

        int x = -108, y, z;

        y = (DataPlayer.get(player).tempCTFKit != 1) ? 5 : 4;
        z = -10 + DataPlayer.get(player).tempCTFKit;

        Chest chest = (Chest) (new Location(player.getWorld(), x, y, z)).getBlock().getState();

        for (int i = 0; i < 8; i++) {
            if (chest.getBlockInventory().getItem(i) == null) continue;
            Item<?> item = Item.fromItemStack(chest.getBlockInventory().getItem(i));
            if (item.isEmpty()) continue;
            item.setName(chatColor + Minecraft.getMatName(item.getType()));
            player.getInventory().setItem(i, item.toItemStack());
        }

        Item<?> chestplate = chest.getBlockInventory().getItem(25) != null ? Item.fromItemStack(chest.getBlockInventory().getItem(25)) : null;
        Item<?> leggings = chest.getBlockInventory().getItem(24) != null ? Item.fromItemStack(chest.getBlockInventory().getItem(24)) : null;
        Item<?> boots = chest.getBlockInventory().getItem(23) != null ? Item.fromItemStack(chest.getBlockInventory().getItem(23)) : null;

        if (chestplate != null && !chestplate.isEmpty()) {
            chestplate.setColor(color);
            chestplate.setName(chatColor + Minecraft.getMatName(chestplate.getType()));
            player.getInventory().setChestplate(chestplate.toItemStack());
        }

        if (leggings != null && !leggings.isEmpty()) {
            leggings.setColor(color);
            leggings.setName(chatColor + Minecraft.getMatName(leggings.getType()));
            player.getInventory().setLeggings(leggings.toItemStack());
        }

        if (boots != null && !boots.isEmpty()) {
            boots.setColor(color);
            boots.setName(chatColor + Minecraft.getMatName(boots.getType()));
            player.getInventory().setBoots(boots.toItemStack());
        }

        player.teleport(homeLoc(Teams.contains(Teams.blueTeam, player)), PlayerTeleportEvent.TeleportCause.PLUGIN);
    }

    public static Location homeLoc(boolean isBlue) {
        return isBlue ? new Location(Bukkit.getWorld(Worlds.CTF), -76.5, 8, 13.5, -90.03F, 0.719F) : new Location(Bukkit.getWorld(Worlds.CTF), -19.5, 8, 13.5, -270F, 0.719F);
    }

    @EventHandler
    public void run(PlayerMoveEvent event) {
        if (!event.getPlayer().getWorld().getName().equalsIgnoreCase(Worlds.CTF) || event.getPlayer().getGameMode() != GameMode.ADVENTURE) return;

        //-96,4,-9
        if (event.getTo().getBlockX() != -96 || event.getTo().getBlockY() != 4 || event.getTo().getBlockZ() != -9) return;

        if (event.getTo().getBlock().getType() == Material.WOOD_PLATE) {
            if (EnderPlugin.scheduler().isRunning("CTF_PLATE_WAIT")) return;
            if (EnderPlugin.scheduler().isRunning("CTF_GAME")) {
                event.getPlayer().sendMessage(Prefixes.CTF + "Wait for the match to finish!");
                EnderPlugin.scheduler().runMarker("CTF_PLATE_WAIT", 1.5);
                return;
            }
        }

        if (teams()) {
            for (Player player : Bukkit.getWorld(Worlds.CTF).getPlayers()) {
                boolean isBlue = Teams.contains(Teams.blueTeam, player);
                Minecraft.worldBroadcast(Worlds.CTF, Prefixes.CTF + DataPlayer.getUser(player).getNickName() + " joined the " + (isBlue ? "blue" : "red") + " team.");
                giveKit(player);
            }
            EnderPlugin.scheduler().runSingleTask(() -> {
                for (Player player1 : Bukkit.getWorld(Worlds.CTF).getPlayers())
                    DataPlayer.get(player1).sendActionMsg(ChatColor.YELLOW + "" + ChatColor.BOLD + "DEFEND YOUR FLAG WHILE STEALING THE OPPONENTS!");
            }, "CTF_BROADCAST", 5);
            EnderPlugin.scheduler().runSingleTask(CTFChestCheck :: win, "CTF_TIME_RUN_OUT", 600);
        } else if (!EnderPlugin.scheduler().isRunning("CTF_PLATE_WAIT")) {
            event.getPlayer().sendMessage(Prefixes.CTF + "You need at least two players!");
            EnderPlugin.scheduler().runMarker("CTF_PLATE_WAIT", 1.5);
        }
    }

    private boolean teams() {
        int playersSize = Bukkit.getWorld(Worlds.CTF).getPlayers().size();
        List<Player> playerList = Bukkit.getWorld(Worlds.CTF).getPlayers();

        if (playersSize < 2) return false;

        int counter = 0;

        for (Player player : playerList) {
            Teams.add(counter % 2 == 0 ? Teams.blueTeam : Teams.redTeam, player);
            player.teleport(homeLoc(counter % 2 == 0), PlayerTeleportEvent.TeleportCause.PLUGIN);
            counter++;
        }
        DataPlayer.ctfSec = 0;
        EnderPlugin.scheduler().runRepeatingTask(() -> {
            DataPlayer.ctfSec++;
            for (Player player : Bukkit.getWorld(Worlds.CTF).getPlayers()) DataPlayer.get(player).getBoard(CTFBoard.class).updateTicks();
        }, "CTF_GAME", 0, 1);
        return true;
    }
}