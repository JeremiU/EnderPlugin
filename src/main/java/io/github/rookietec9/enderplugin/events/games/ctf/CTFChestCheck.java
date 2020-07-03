package io.github.rookietec9.enderplugin.events.games.ctf;

import io.github.rookietec9.enderplugin.EnderPlugin;
import io.github.rookietec9.enderplugin.configs.associates.Spawn;
import io.github.rookietec9.enderplugin.scoreboards.Board;
import io.github.rookietec9.enderplugin.scoreboards.CTFBoard;
import io.github.rookietec9.enderplugin.utils.datamanagers.DataPlayer;
import io.github.rookietec9.enderplugin.utils.datamanagers.Item;
import io.github.rookietec9.enderplugin.utils.methods.Java;
import io.github.rookietec9.enderplugin.utils.methods.Minecraft;
import io.github.rookietec9.enderplugin.utils.reference.DataType;
import io.github.rookietec9.enderplugin.utils.reference.Prefixes;
import io.github.rookietec9.enderplugin.utils.reference.Teams;
import io.github.rookietec9.enderplugin.utils.reference.Worlds;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.Inventory;

import static io.github.rookietec9.enderplugin.utils.datamanagers.DataPlayer.*;

/**
 * @author Jeremi
 * @version 22.8.0
 * @since 11.6.9
 */
public class CTFChestCheck implements Listener {

    private final Chest HOME_RED = (Chest) Bukkit.getWorld(Worlds.CTF).getBlockAt(-26, 8, 13).getState();
    private final Chest HOME_BLUE = (Chest) Bukkit.getWorld(Worlds.CTF).getBlockAt(-71, 8, 13).getState();

    public static void win() {
        if (!EnderPlugin.scheduler().isRunning("CTF_GAME")) return;
        if (EnderPlugin.scheduler().isRunning("CTF_TIME_RUN_OUT")) EnderPlugin.scheduler().cancel("CTF_TIME_RUN_OUT");
        EnderPlugin.scheduler().cancel("CTF_GAME");
        if (ctfRedScore != ctfBlueScore) Minecraft.worldBroadcast(Worlds.CTF, Prefixes.CTF + "The " + (ctfBlueScore > ctfRedScore ? "§9blue" : "§cred") + "§7 team won the game in " + Board.formatTime(ctfSec, ChatColor.YELLOW, ChatColor.GRAY) + "§7.");
        else Minecraft.worldBroadcast(Worlds.CTF, Prefixes.CTF + "Neither team managed to win.");

        ctfSec = 0;
        for (Player player : Bukkit.getWorld(Worlds.CTF).getPlayers()) {
            boolean isBlue = Teams.contains(Teams.blueTeam, player);

            if (ctfRedScore != ctfBlueScore) {
                if (ctfBlueScore > ctfRedScore) {
                    if (isBlue) DataPlayer.get(player).increment(DataType.CTFWINS);
                    else DataPlayer.get(player).increment(DataType.CTFLOSSES);
                } else {
                    if (!isBlue) DataPlayer.get(player).increment(DataType.CTFWINS);
                    else DataPlayer.get(player).increment(DataType.CTFLOSSES);
                }
            }

            get(player).getBoard(CTFBoard.class).updateScore(0, 0);
            get(player).getBoard(CTFBoard.class).updateTicks();

            get(player).incrementBy(DataType.CTFKILLS, get(player).tempCTFKills);
            get(player).tempCTFKills = 0;
            get(player).incrementBy(DataType.CTFDEATHS, get(player).tempCTFDeaths);
            get(player).tempCTFDeaths = 0;
            get(player).incrementBy(DataType.CTFPOINTS, (isBlue ? ctfBlueScore : ctfRedScore));

            get(player).getBoard(CTFBoard.class).updateTempDeaths(0);
            get(player).getBoard(CTFBoard.class).updateTempKills(0);
            getUser(player).reset(GameMode.ADVENTURE);
            player.teleport(new Spawn(Worlds.CTF).location(), PlayerTeleportEvent.TeleportCause.PLUGIN);
        }
    }

    @EventHandler
    public void run(InventoryDragEvent event) {
        if (event.getWhoClicked().getGameMode() == GameMode.ADVENTURE && event.getWhoClicked().getWorld().getName().equalsIgnoreCase(Worlds.CTF)) event.setCancelled(true);
    }

    @EventHandler
    public void run(InventoryClickEvent event) {
        if (event.getWhoClicked().getGameMode() == GameMode.ADVENTURE && event.getWhoClicked().getWorld().getName().equalsIgnoreCase(Worlds.CTF)) event.setCancelled(true);
    }

    @EventHandler
    public void run(InventoryOpenEvent event) {
        if (!event.getPlayer().getWorld().getName().equalsIgnoreCase(Worlds.CTF) || event.getPlayer().getGameMode() != GameMode.ADVENTURE) return;
        if (Teams.getTeam((Player) event.getPlayer()) == null) return;
        if (!Java.argWorks(Teams.getTeam((Player) event.getPlayer()).getName(), Teams.blueTeam, Teams.redTeam)) return;
        if (!Java.argWorks(event.getInventory().getName(), "§9blue", "§cred")) return;

        event.setCancelled(true);

        boolean isHome = event.getInventory().getName().startsWith(Teams.getTeam((Player) event.getPlayer()).getPrefix());
        check((Player) event.getPlayer(), event.getInventory(), (byte) (isHome ? 1 : 2));
    }

    @EventHandler
    public void run(PlayerPickupItemEvent event) {
        if (!event.getPlayer().getWorld().getName().equalsIgnoreCase(Worlds.CTF) || event.getPlayer().getGameMode() != GameMode.ADVENTURE) return;
        if (!Java.argWorks(Teams.getTeam(event.getPlayer()).getName(), Teams.blueTeam, Teams.redTeam)) return;
        if (Material.BANNER != event.getItem().getItemStack().getType()) return;
        if (!event.getItem().getItemStack().getItemMeta().hasDisplayName()) return;


        boolean isBlue = Teams.contains(Teams.blueTeam, event.getPlayer());

        if (ChatColor.stripColor(event.getItem().getItemStack().getItemMeta().getDisplayName()).toLowerCase().contains("red")) {
            if (isBlue) {
                for (Player player : Bukkit.getWorld(Worlds.CTF).getPlayers()) DataPlayer.get(player).sendActionMsg(Prefixes.CTF + "The red flag has been stolen by " + DataPlayer.getUser(event.getPlayer()).getNickName() + "!");
                event.getPlayer().getInventory().setHelmet(new Item<>(Material.WOOL, "§cRed Helmet", (byte) 14, 1).toItemStack());
                event.getPlayer().getInventory().setItem(17, event.getItem().getItemStack());
            } else {
                for (Player player : Bukkit.getWorld(Worlds.CTF).getPlayers()) DataPlayer.get(player).sendActionMsg(Prefixes.CTF + "The red flag has been returned by " + DataPlayer.getUser(event.getPlayer()).getNickName() + "!");
                HOME_RED.getBlockInventory().setItem(0, event.getItem().getItemStack());
                updateFlagSec(false, true);
            }
            event.getItem().remove();
            event.setCancelled(true);
        }
        if (ChatColor.stripColor(event.getItem().getItemStack().getItemMeta().getDisplayName()).toLowerCase().contains("blue")) {
            if (isBlue) {
                for (Player player : Bukkit.getWorld(Worlds.CTF).getPlayers()) DataPlayer.get(player).sendActionMsg(Prefixes.CTF + "The blue flag has been returned by " + DataPlayer.getUser(event.getPlayer()).getNickName() + "!");
                HOME_BLUE.getBlockInventory().setItem(0, event.getItem().getItemStack());
                updateFlagSec(true, true);
            } else {
                for (Player player : Bukkit.getWorld(Worlds.CTF).getPlayers()) DataPlayer.get(player).sendActionMsg(Prefixes.CTF + "The red flag has been stolen by " + DataPlayer.getUser(event.getPlayer()).getNickName() + "!");
                event.getPlayer().getInventory().setHelmet(new Item<>(Material.WOOL, "§9Blue Helmet", (byte) 11, 1).toItemStack());
                event.getPlayer().getInventory().setItem(17, event.getItem().getItemStack());
            }
            event.getItem().remove();
            event.setCancelled(true);
        }
    }

    public void check(Player player, Inventory inventory, byte type) { //0 = FRIENDLY HOME, 1 = FRIENDLY HOSTAGE, 2 = ENEMY HOME, 3 = ENEMY HOSTAGE
        boolean isBlue = Teams.contains(Teams.blueTeam, player);

        if (EnderPlugin.scheduler().isRunning("CTF_" + type + "_" + player.getName())) return;

        switch (type) {
            case 1 -> {
                if (player.getInventory().contains(Material.BANNER)) {
                    if (isBlue ? ctfBlueSafe : ctfRedSafe) {
                        (isBlue ? HOME_RED : HOME_BLUE).getBlockInventory().setItem(0, player.getInventory().getItem(17));
                        player.getInventory().setItem(17, null);
                        for (Player p : Bukkit.getWorld(Worlds.CTF).getPlayers()) DataPlayer.get(p).sendActionMsg(Prefixes.CTF + DataPlayer.getUser(player).getNickName() + " captured the enemy flag!");
                        DataPlayer.get(player).increment(DataType.CTFCAPTURES);
                        player.getInventory().setHelmet(null);
                        updateFlagSec(true, true);
                        updateFlagSec(false, true);
                        updateScore(isBlue);
                    } else DataPlayer.get(player).sendActionMsg(Prefixes.CTF + "Your flag needs to be safe before you can capture the flag!");
                } else DataPlayer.get(player).sendActionMsg(Prefixes.CTF + "Your flag is " + ((isBlue ? HOME_BLUE : HOME_RED).getBlockInventory().contains(Material.BANNER) ? "Safe" : "Stolen") + "!");
            }
            case 2 -> {
                if (inventory.contains(Material.BANNER)) {
                    player.getInventory().setItem(17, inventory.getItem(0));
                    inventory.setItem(0, null);
                    player.getInventory().setHelmet(new Item<>(Material.WOOL, !isBlue ? "§9Blue Helmet" : "§cRed Helmet", (byte) (!isBlue ? 11 : 14), 1).toItemStack());
                    DataPlayer.get(player).sendActionMsg(Prefixes.CTF + "Stolen the enemy flag!");
                    updateFlagSec(!isBlue, false);
                } else DataPlayer.get(player).sendActionMsg(Prefixes.CTF + "The enemy flag is already stolen!");
            }
        }
        EnderPlugin.scheduler().runMarker("CTF_" + type + "_" + player.getName(), 1.5);
    }

    private void updateFlagSec(boolean blueFlag, boolean safety) {
        for (Player player : Bukkit.getWorld(Worlds.CTF).getPlayers())
            if (blueFlag) get(player).getBoard(CTFBoard.class).updateBlueFlag(safety);
            else get(player).getBoard(CTFBoard.class).updateRedFlag(safety);
    }

    private void updateScore(boolean isBlue) {
        if (ctfBlueScore >= 4 || ctfRedScore >= 4) {
            win();
            return;
        }
        if (isBlue) ctfBlueScore++;
        else ctfRedScore++;
        for (Player player : Bukkit.getWorld(Worlds.CTF).getPlayers()) get(player).getBoard(CTFBoard.class).updateScore(ctfRedScore, ctfBlueScore);
    }
}