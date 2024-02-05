package io.github.rookietec9.enderplugin.events.games.ctf;

import io.github.rookietec9.enderplugin.EnderPlugin;
import io.github.rookietec9.enderplugin.configs.DataType;
import io.github.rookietec9.enderplugin.configs.associates.Spawn;
import io.github.rookietec9.enderplugin.scoreboards.Board;
import io.github.rookietec9.enderplugin.scoreboards.CTFBoard;
import io.github.rookietec9.enderplugin.utils.datamanagers.DataPlayer;
import io.github.rookietec9.enderplugin.utils.datamanagers.ItemWrapper;
import io.github.rookietec9.enderplugin.utils.methods.Java;
import io.github.rookietec9.enderplugin.utils.methods.Minecraft;
import io.github.rookietec9.enderplugin.utils.methods.Teams;
import org.bukkit.*;
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

import static io.github.rookietec9.enderplugin.Reference.CTF;
import static io.github.rookietec9.enderplugin.Reference.PREFIX_CTF;
import static io.github.rookietec9.enderplugin.utils.datamanagers.DataPlayer.*;

/**
 * @author Jeremi
 * @version 25.7.5
 * @since 11.6.9
 */
public class CTFFlagEvent implements Listener {

    private final Chest HOME_RED = (Chest) Bukkit.getWorld(CTF).getBlockAt(-26, 8, 13).getState();
    private final Chest HOME_BLUE = (Chest) Bukkit.getWorld(CTF).getBlockAt(-71, 8, 13).getState();

    public static void win() {
        if (!EnderPlugin.scheduler().isRunning("CTF_GAME")) return;
        if (EnderPlugin.scheduler().isRunning("CTF_TIME_RUN_OUT")) EnderPlugin.scheduler().cancel("CTF_TIME_RUN_OUT");
        EnderPlugin.scheduler().cancel("CTF_GAME");
        if (ctfRedScore != ctfBlueScore) Minecraft.worldBroadcast(CTF, PREFIX_CTF + "The " + (ctfBlueScore > ctfRedScore ? "§9blue" : "§cred") + "§7 team won the game in " + Board.formatTime(ctfSec, ChatColor.YELLOW, ChatColor.GRAY) + "§7.");
        else Minecraft.worldBroadcast(CTF, PREFIX_CTF + "Neither team managed to win.");

        ctfSec = 0;
        for (Player player : Bukkit.getWorld(CTF).getPlayers()) {
            boolean isBlue = Teams.contains(Teams.TEAM_BLUE, player);

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
            get(player).reset();
            player.teleport(new Spawn(CTF).location(), PlayerTeleportEvent.TeleportCause.PLUGIN);
            DataPlayer.get(player).finishGame();
        }
    }

    @EventHandler
    public void run(InventoryDragEvent event) {
        if (event.getWhoClicked().getGameMode() == GameMode.ADVENTURE && event.getWhoClicked().getWorld().getName().equalsIgnoreCase(CTF)) event.setCancelled(true);
    }

    @EventHandler
    public void run(InventoryClickEvent event) {
        if (event.getWhoClicked().getGameMode() == GameMode.ADVENTURE && event.getWhoClicked().getWorld().getName().equalsIgnoreCase(CTF)) event.setCancelled(true);
    }

    @EventHandler
    public void run(InventoryOpenEvent event) {
        if (!event.getPlayer().getWorld().getName().equalsIgnoreCase(CTF) || event.getPlayer().getGameMode() != GameMode.ADVENTURE) return;
        if (Teams.getTeam((Player) event.getPlayer()) == null) return;
        if (!Java.argWorks(Teams.getTeam((Player) event.getPlayer()).getName(), Teams.TEAM_BLUE, Teams.TEAM_RED)) return;
        if (!Java.argWorks(event.getInventory().getName(), "§9blue", "§cred")) return;

        event.setCancelled(true);

        boolean isHome = event.getInventory().getName().startsWith(Teams.getTeam((Player) event.getPlayer()).getPrefix());
        check((Player) event.getPlayer(), event.getInventory(), (byte) (isHome ? 1 : 2));
    }

    @EventHandler
    public void run(PlayerPickupItemEvent event) {
        if (!event.getPlayer().getWorld().getName().equalsIgnoreCase(CTF) || event.getPlayer().getGameMode() != GameMode.ADVENTURE) return;
        if (!Java.argWorks(Teams.getTeam(event.getPlayer()).getName(), Teams.TEAM_BLUE, Teams.TEAM_RED)) return;
        if (Material.BANNER != event.getItem().getItemStack().getType()) return;
        if (!event.getItem().getItemStack().getItemMeta().hasDisplayName()) return;

        boolean isPlayerBlue = Teams.contains(Teams.TEAM_BLUE, event.getPlayer());
        boolean isItemRed = ChatColor.stripColor(event.getItem().getItemStack().getItemMeta().getDisplayName()).toLowerCase().contains("red");
        boolean isItemBlue = ChatColor.stripColor(event.getItem().getItemStack().getItemMeta().getDisplayName()).toLowerCase().contains("blue");

        if (isItemRed || isItemBlue) {
            if (isItemRed == isPlayerBlue) {
                for (Player player : Bukkit.getWorld(CTF).getPlayers()) {
                    DataPlayer.get(player).sendActionMsg(PREFIX_CTF + "The " + (isItemRed ? "§cred" : "9blue") + " flag has been stolen by " + event.getPlayer().getName() + "!");
                    player.playSound(player.getLocation(), Sound.ENDERMAN_SCREAM, 1, 1);
                    event.getPlayer().getInventory().setHelmet(new ItemWrapper<>(Material.WOOL, (isPlayerBlue ? "§cRed Helmet" : "§9Blue Helmet"), (byte) (isPlayerBlue ? 11 : 14), 1).toItemStack());
                    event.getPlayer().getInventory().setItem(17, event.getItem().getItemStack());
                }
                event.getPlayer().getInventory().setItem(17, event.getItem().getItemStack());
            } else {
                for (Player player : Bukkit.getWorld(CTF).getPlayers()) {
                    DataPlayer.get(player).sendActionMsg(PREFIX_CTF + "The " + (isPlayerBlue ? "9blue" : "§cred") + " flag has been returned by " + event.getPlayer().getName() + "!");
                    player.playNote(player.getLocation(), Instrument.PIANO, Note.flat(2, Note.Tone.F));
                    (isPlayerBlue ? HOME_BLUE : HOME_RED).getBlockInventory().setItem(0, event.getItem().getItemStack());
                    updateFlagSec(isItemBlue, true);
                }
            }
            event.getItem().remove();
            event.setCancelled(true);
        }

    }

    public void check(Player player, Inventory inventory, byte type) { //0 = FRIENDLY HOME, 1 = FRIENDLY HOSTAGE, 2 = ENEMY HOME, 3 = ENEMY HOSTAGE
        boolean isBlue = Teams.contains(Teams.TEAM_BLUE, player);

        if (EnderPlugin.scheduler().isRunning("CTF_" + type + "_" + player.getName())) return;

        switch (type) {
            case 1 -> {
                if (player.getInventory().contains(Material.BANNER)) {
                    if (isBlue ? ctfBlueSafe : ctfRedSafe) {
                        (isBlue ? HOME_RED : HOME_BLUE).getBlockInventory().setItem(0, player.getInventory().getItem(17));
                        player.getInventory().setItem(17, null);
                        for (Player p : Bukkit.getWorld(CTF).getPlayers()) DataPlayer.get(p).sendActionMsg(PREFIX_CTF + DataPlayer.getUser(player).getNickName() + " captured the enemy flag!");
                        DataPlayer.get(player).increment(DataType.CTFCAPTURES);
                        player.getInventory().setHelmet(null);
                        updateFlagSec(true, true);
                        updateFlagSec(false, true);
                        updateScore(isBlue);
                    } else DataPlayer.get(player).sendActionMsg(PREFIX_CTF + "Your flag needs to be safe before you can capture the flag!");
                } else DataPlayer.get(player).sendActionMsg(PREFIX_CTF + "Your flag is " + ((isBlue ? HOME_BLUE : HOME_RED).getBlockInventory().contains(Material.BANNER) ? "Safe" : "Stolen") + "!");
            }
            case 2 -> {
                if (inventory.contains(Material.BANNER)) {
                    player.getInventory().setItem(17, inventory.getItem(0));
                    inventory.setItem(0, null);
                    player.getInventory().setHelmet(new ItemWrapper<>(Material.WOOL, !isBlue ? "§9Blue Helmet" : "§cRed Helmet", (byte) (!isBlue ? 11 : 14), 1).toItemStack());
                    DataPlayer.get(player).sendActionMsg(PREFIX_CTF + "Stolen the enemy flag!");
                    updateFlagSec(!isBlue, false);
                } else DataPlayer.get(player).sendActionMsg(PREFIX_CTF + "The enemy flag is already stolen!");
            }
        }
        EnderPlugin.scheduler().runMarker("CTF_" + type + "_" + player.getName(), 1.5, PREFIX_CTF);
    }

    private void updateFlagSec(boolean blueFlag, boolean safety) {
        for (Player player : Bukkit.getWorld(CTF).getPlayers())
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
        for (Player player : Bukkit.getWorld(CTF).getPlayers()) get(player).getBoard(CTFBoard.class).updateScore(ctfRedScore, ctfBlueScore);
    }
}