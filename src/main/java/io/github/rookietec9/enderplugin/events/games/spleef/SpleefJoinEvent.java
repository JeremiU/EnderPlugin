package io.github.rookietec9.enderplugin.events.games.spleef;

import io.github.rookietec9.enderplugin.EnderPlugin;
import io.github.rookietec9.enderplugin.Inventories;
import io.github.rookietec9.enderplugin.scoreboards.SpleefBoard;
import io.github.rookietec9.enderplugin.utils.datamanagers.DataPlayer;
import io.github.rookietec9.enderplugin.utils.datamanagers.ItemWrapper;
import io.github.rookietec9.enderplugin.utils.datamanagers.PartySystem;
import io.github.rookietec9.enderplugin.utils.methods.Java;
import io.github.rookietec9.enderplugin.utils.methods.Minecraft;
import io.github.rookietec9.enderplugin.utils.methods.Teams;
import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.*;
import org.bukkit.block.Sign;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static io.github.rookietec9.enderplugin.Inventories.*;
import static io.github.rookietec9.enderplugin.Reference.PREFIX_SPLEEF;
import static io.github.rookietec9.enderplugin.Reference.SPLEEF;
import static io.github.rookietec9.enderplugin.utils.datamanagers.PartySystem.PartyTeam;
import static org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;


/**
 * @author Jeremi
 * @version 25.4.3
 * @since 14.8.5
 */
public class SpleefJoinEvent implements Listener {

    @EventHandler
    public void run(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (!player.getWorld().getName().equalsIgnoreCase(SPLEEF)) return;
        if (player.getGameMode() != GameMode.SURVIVAL) return;

        if (event.getClickedBlock() != null && event.getClickedBlock().getState() instanceof Sign) {
            Sign sign = (Sign) event.getClickedBlock().getState();
            if (sign.getLine(1).equalsIgnoreCase("§9§lJoin")) {
                if (Bukkit.getWorld(SPLEEF).getPlayers().size() < 2) {
                    player.sendMessage(PREFIX_SPLEEF + "Not enough players.");
                    return;
                }

                if (EnderPlugin.scheduler().isRunning("SPLEEF_MINE") || EnderPlugin.scheduler().isRunning("SPLEEF_TICKER")) {
                    player.sendMessage(PREFIX_SPLEEF + "Wait for the match to finish!");
                    return;
                }

                event.getPlayer().openInventory(Inventories.START_SPLEEF);
            }
        }
    }

    @EventHandler
    public void run(InventoryClickEvent event) {
        World w = event.getWhoClicked().getWorld();
        Player player = (Player) event.getWhoClicked();
        Minecraft.startGame(event, START_SPLEEF, PREFIX_SPLEEF,
                () -> start(true, w.getPlayers()),
                () -> start(event.getSlot() == START_PARTIES_RANDOM, PartySystem.getPartiersInWorld(w.getName()), PartySystem.getPartiesInWorldToArray(w.getName())),
                () -> start(event.getSlot() == START_PARTY_RANDOM, PartySystem.getPartiersInWorld(w.getName()), PartySystem.getFromPlayer(player))
                , PartyTeam.RED, PartyTeam.BLUE, PartyTeam.GREEN, PartyTeam.YELLOW);
    }

    public void teams(boolean randomTeams, List<Player> playerList, PartySystem... partySystems) {
        String[] teamNames = {Teams.TEAM_WHITE, Teams.TEAM_GREEN, Teams.TEAM_BLUE, Teams.TEAM_RED};
        List<Player> tempPlayerList = new ArrayList<>(playerList);

        int counter = 0;
        if (playerList.size() % 3 == 0) teamNames = new String[]{Teams.TEAM_GREEN, Teams.TEAM_BLUE, Teams.TEAM_RED};

        if (randomTeams) while (tempPlayerList.size() > 0) Teams.add(teamNames[counter++ % teamNames.length], Java.getRandomRemove(tempPlayerList));

        else for (PartySystem partySystem : partySystems) {
            Teams.addAll(Teams.TEAM_RED, partySystem.getPlayersFromTeam(PartyTeam.RED));
            Teams.addAll(Teams.TEAM_BLUE, partySystem.getPlayersFromTeam(PartyTeam.BLUE));
            Teams.addAll(Teams.TEAM_GREEN, partySystem.getPlayersFromTeam(PartyTeam.GREEN));
            Teams.addAll(Teams.TEAM_WHITE, partySystem.getPlayersFromTeam(PartyTeam.YELLOW));
        }

        DataPlayer.spleefLeft = new ArrayList<>(playerList);
        DataPlayer.spleefTeamsLeft = teamNames.length;

        Teams.printEntries(SPLEEF, PREFIX_SPLEEF, true);
    }

    private void start(boolean randomTeams, List<Player> playerList, PartySystem... partySystems) {
        Teams.clear(Teams.TEAM_BLUE);
        Teams.clear(Teams.TEAM_GREEN);
        Teams.clear(Teams.TEAM_RED);
        Teams.clear(Teams.TEAM_WHITE);
        teams(randomTeams, playerList, partySystems);

        String[] teamName = new String[]{Teams.TEAM_RED, Teams.TEAM_BLUE, Teams.TEAM_GREEN, Teams.TEAM_WHITE};
        Location[] locations = new Location[]{
                new Location(Bukkit.getWorld(SPLEEF), -51.5, 4, -28.5, -90F, 0.16F), new Location(Bukkit.getWorld(SPLEEF), -4, 4, -28.5, -270F, 0.46F),
                new Location(Bukkit.getWorld(SPLEEF), -27.5, 4, -53, 0.2F, 0.46F), new Location(Bukkit.getWorld(SPLEEF), -28, 4, -5, 180F, 0.46F),
                new Location(Bukkit.getWorld(SPLEEF), -28, 4, -29, 120F, -2F)
        };

        AtomicInteger j = new AtomicInteger(0);

        for (; j.get() < teamName.length; j.incrementAndGet())
            Teams.getEntries(teamName[j.get()]).stream().filter(x -> x.getWorld().getName().equalsIgnoreCase(SPLEEF)).forEach(x -> x.teleport(locations[j.get()], TeleportCause.PLUGIN));

        for (Player player1 : playerList) {
            DataPlayer.get(player1).startGame(GameMode.SURVIVAL);
            DataPlayer.get(player1).getBoard(SpleefBoard.class).updateTicks();
            DataPlayer.get(player1).getBoard(SpleefBoard.class).updatePlayers(Bukkit.getWorld(SPLEEF).getPlayers().size());

            player1.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 60 * 20, 4, true, true));

            Material[] materials = new Material[]{Material.DIAMOND_SPADE, Material.LEATHER_HELMET, Material.LEATHER_CHESTPLATE, Material.LEATHER_LEGGINGS, Material.LEATHER_BOOTS};
            int[] slots = new int[]{0, 39, 38, 37, 36};
            player1.getInventory().getArmorContents();

            for (int i = 0; i < materials.length; i++) {
                ItemWrapper<?> itemWrapper = new ItemWrapper<>(materials[i]);
                itemWrapper.setUnbreakable(true);
                itemWrapper.addFlag(ItemFlag.HIDE_UNBREAKABLE, ItemFlag.HIDE_ENCHANTS);

                if (materials[i] == Material.LEATHER_BOOTS) itemWrapper.addEnch(Pair.of(Enchantment.PROTECTION_FALL, 10));
                if (materials[i] == Material.LEATHER_CHESTPLATE) itemWrapper.addEnch(Pair.of(Enchantment.THORNS, 25));

                if (itemWrapper.isPaintAble()) itemWrapper.setColor(Minecraft.translateChatColorToColor(Minecraft.teamColor(Teams.getTeam(player1).getName(), true)));
                player1.getInventory().setItem(slots[i], itemWrapper.toItemStack());
                player1.getInventory().setItem(8, new ItemWrapper<>(Material.STICK, "§9Snowball Crafter", "§f4 Snowballs §9§l→§f 1 Snow Block", "").toItemStack());
                player1.updateInventory();
            }
            SpleefMineEvent.hashMap.put(player1, 0);
        }

        EnderPlugin.scheduler().runRepeatingTask(SpleefMineEvent.runnable(), "SPLEEF_MINE", 0, 10, PREFIX_SPLEEF);
        EnderPlugin.scheduler().runRepeatingTask(() -> {
            DataPlayer.spleefSec++;
            for (Player player1 : Bukkit.getWorld(SPLEEF).getPlayers()) DataPlayer.get(player1).getBoard(SpleefBoard.class).updateTicks();
        }, "SPLEEF_TICKER", 0, 1, PREFIX_SPLEEF);
    }
}