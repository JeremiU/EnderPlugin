package io.github.rookietec9.enderplugin.events.games.ctf;

import io.github.rookietec9.enderplugin.EnderPlugin;
import io.github.rookietec9.enderplugin.scoreboards.CTFBoard;
import io.github.rookietec9.enderplugin.utils.datamanagers.DataPlayer;
import io.github.rookietec9.enderplugin.utils.datamanagers.ItemWrapper;
import io.github.rookietec9.enderplugin.utils.datamanagers.PartySystem;
import io.github.rookietec9.enderplugin.utils.methods.Java;
import io.github.rookietec9.enderplugin.utils.methods.Minecraft;
import io.github.rookietec9.enderplugin.utils.methods.Teams;
import org.bukkit.*;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.ArrayList;
import java.util.List;

import static io.github.rookietec9.enderplugin.Inventories.*;
import static io.github.rookietec9.enderplugin.Reference.CTF;
import static io.github.rookietec9.enderplugin.Reference.PREFIX_CTF;
import static org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

/**
 * @author Jeremi
 * @version 25.4.3
 * @since 20.6.1
 */
public class CTFJoinEvent implements Listener {

    public static void giveKit(Player player) {
        DataPlayer.get(player).startGame(GameMode.ADVENTURE);
        ChatColor chatColor = Teams.contains(Teams.TEAM_BLUE, player) ? ChatColor.BLUE : ChatColor.RED;
        Color color = Teams.contains(Teams.TEAM_BLUE, player) ? Color.BLUE : Color.RED;

        int x = -108, y, z;

        y = (DataPlayer.get(player).tempCTFKit != 1) ? 5 : 4;
        z = -10 + DataPlayer.get(player).tempCTFKit;

        Chest chest = (Chest) (new Location(player.getWorld(), x, y, z)).getBlock().getState();

        for (int i = 0; i < 8; i++) {
            if (chest.getBlockInventory().getItem(i) == null) continue;
            ItemWrapper<?> itemWrapper = ItemWrapper.fromItemStack(chest.getBlockInventory().getItem(i));
            if (itemWrapper.isEmpty()) continue;
            itemWrapper.setName(chatColor + Minecraft.getMatName(itemWrapper.getType()));
            player.getInventory().setItem(i, itemWrapper.toItemStack());
        }

        ItemWrapper<?> chestplate = chest.getBlockInventory().getItem(25) != null ? ItemWrapper.fromItemStack(chest.getBlockInventory().getItem(25)) : null;
        ItemWrapper<?> leggings = chest.getBlockInventory().getItem(24) != null ? ItemWrapper.fromItemStack(chest.getBlockInventory().getItem(24)) : null;
        ItemWrapper<?> boots = chest.getBlockInventory().getItem(23) != null ? ItemWrapper.fromItemStack(chest.getBlockInventory().getItem(23)) : null;

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

        player.teleport(homeLoc(Teams.contains(Teams.TEAM_BLUE, player)), TeleportCause.PLUGIN);
    }

    public static Location homeLoc(boolean isBlue) {
        return isBlue ? new Location(Bukkit.getWorld(CTF), -76.5, 8, 13.5, -90.03F, 0.719F) : new Location(Bukkit.getWorld(CTF), -19.5, 8, 13.5, -270F, 0.719F);
    }

    @EventHandler
    public void run(PlayerMoveEvent event) {
        if (!event.getPlayer().getWorld().getName().equalsIgnoreCase(CTF) || event.getPlayer().getGameMode() != GameMode.ADVENTURE || EnderPlugin.scheduler().isRunning("CTF_PLATE_WAIT")) return;

        if (event.getTo().getBlockX() != -96 || event.getTo().getBlockY() != 4 || event.getTo().getBlockZ() != -9) return;

        if (event.getTo().getBlock().getType() == Material.WOOD_PLATE) {
            if (EnderPlugin.scheduler().isRunning("CTF_GAME")) {
                event.getPlayer().sendMessage(PREFIX_CTF + "Wait for the match to finish!");
                EnderPlugin.scheduler().runMarker("CTF_PLATE_WAIT", 1.5, PREFIX_CTF);
            }
        }
        event.getPlayer().openInventory(START_CTF);
        EnderPlugin.scheduler().runMarker("CTF_PLATE_WAIT", 1.5, PREFIX_CTF);
    }

    @EventHandler
    public void run(InventoryClickEvent event) {
        World w = event.getWhoClicked().getWorld();
        Player player = (Player) event.getWhoClicked();
        Minecraft.startGame(event, START_CTF, PREFIX_CTF, ()-> start(true, w.getPlayers()), ()-> start(event.getSlot() == START_PARTIES_RANDOM,
                PartySystem.getPartiersInWorld(w.getName()), PartySystem.getPartiesInWorldToArray(w.getName())), () -> start(event.getSlot() == START_PARTY_RANDOM, PartySystem.getPartiersInWorld(w.getName()), PartySystem.getFromPlayer(player)), PartySystem.PartyTeam.BLUE, PartySystem.PartyTeam.RED);
    }

    private void start(boolean randomTeams, List<Player> playerList, PartySystem... partySystems) {
        teams(randomTeams, playerList, partySystems);

        EnderPlugin.scheduler().runRepeatingTask(() -> {
            DataPlayer.ctfSec++;
            for (Player player : Bukkit.getWorld(CTF).getPlayers()) DataPlayer.get(player).getBoard(CTFBoard.class).updateTicks();
        }, "CTF_GAME", 0, 1, PREFIX_CTF);
        EnderPlugin.scheduler().runSingleTask(() -> {
            for (Player player1 : Bukkit.getWorld(CTF).getPlayers()) {
                DataPlayer.get(player1).sendActionMsg(ChatColor.YELLOW + "" + ChatColor.BOLD + "DEFEND YOUR FLAG WHILE STEALING THE OPPONENTS!");
            }
        }, "CTF_BROADCAST", 5, PREFIX_CTF);
        EnderPlugin.scheduler().runSingleTask(CTFFlagEvent :: win, "CTF_TIME_RUN_OUT", 600, PREFIX_CTF);
    }

    private void teams(boolean randomTeams, List<Player> playerList, PartySystem... partySystems) {
        List<Player> playerList1 = new ArrayList<>(playerList);

        int counter = 0;

        if (randomTeams) while (playerList.size() != 0) {
            Player player = Java.getRandomRemove(playerList);
            Teams.add(counter % 2 == 0 ? Teams.TEAM_BLUE : Teams.TEAM_RED, player);
            player.teleport(homeLoc(counter++ % 2 == 0), TeleportCause.PLUGIN);
        }
        else for (PartySystem partySystem : partySystems) {
            Teams.addAll(Teams.TEAM_RED, partySystem.getPlayersFromTeam(PartySystem.PartyTeam.RED));
            Teams.addAll(Teams.TEAM_BLUE, partySystem.getPlayersFromTeam(PartySystem.PartyTeam.BLUE));
        }

        for (Player player : playerList1) giveKit(player);
        DataPlayer.ctfSec = 0;
    }
}