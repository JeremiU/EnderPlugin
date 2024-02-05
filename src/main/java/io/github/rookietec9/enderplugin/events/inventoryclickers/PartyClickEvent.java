package io.github.rookietec9.enderplugin.events.inventoryclickers;

import io.github.rookietec9.enderplugin.utils.datamanagers.DataPlayer;
import io.github.rookietec9.enderplugin.utils.datamanagers.ItemWrapper;
import io.github.rookietec9.enderplugin.utils.datamanagers.PartySystem;
import io.github.rookietec9.enderplugin.utils.methods.Java;
import io.github.rookietec9.enderplugin.utils.methods.Minecraft;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.Objects;

import static io.github.rookietec9.enderplugin.Reference.PREFIX_PARTY;
import static io.github.rookietec9.enderplugin.utils.datamanagers.PartySystem.PartyTeam;

/**
 * @author Jeremi
 * @version 25.2.0
 * @since 23.1.8
 */
public class PartyClickEvent implements Listener {

    public static Inventory playerMan(Player player) {
        Inventory inventory = Bukkit.createInventory(null, 9, "§c§lManage " + DataPlayer.getUser(player).getNickName());
        inventory.setItem(2, new ItemWrapper<>(Material.ANVIL, "§c§lKick " + DataPlayer.getUser(player).getNickName()).toItemStack());
        String teamName = Java.capFirst(PartySystem.getFromPlayer(player).getTeam(player).name());
        inventory.setItem(4, new ItemWrapper<>(Material.LEATHER_CHESTPLATE, "§7§lChange " + DataPlayer.getUser(player).getNickName() + "'s team: " + Minecraft.teamColor(teamName, true) + "§l" + teamName).setColor(Minecraft.translateChatColorToColor(Minecraft.teamColor(teamName, true))).toItemStack());
        inventory.setItem(6, new ItemWrapper<>(Material.SLIME_BALL, "§a§lPromote " + DataPlayer.getUser(player).getNickName()).toItemStack());
        return inventory;
    }

    @EventHandler
    public void run(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) return;
        Player player = (Player) event.getWhoClicked();

        if (PartySystem.getFromPlayer(player) == null || event.getClickedInventory() == null || event.getClickedInventory().getName() == null) return;
        if ("§d§lParty Menu (Leader)".equalsIgnoreCase(event.getClickedInventory().getName())) {
            event.setCancelled(true);

            switch (event.getSlot()) {
                case PartySystem.SLOT_LIST -> {
                    player.closeInventory();
                    player.performCommand("party list");
                }
                case PartySystem.SLOT_INVITE -> {
                    if (PartySystem.toInvite(player) != null) player.openInventory(PartySystem.toInvite(player));
                }
                case PartySystem.SLOT_MANAGE -> {
                    if (PartySystem.getFromPlayer(player).toManage() != null && PartySystem.getFromPlayer(player).getLeader().equals(player.getUniqueId()))
                        player.openInventory(PartySystem.getFromPlayer(player).toManage());
                }
                case PartySystem.SLOT_TEAM -> {
                    if (event.getCurrentItem() == null || event.getCurrentItem().getItemMeta().getDisplayName() == null) return;

                    flip(player, event.getCurrentItem().getItemMeta().getDisplayName().substring(event.getCurrentItem().getItemMeta().getDisplayName().indexOf(": ") + 6).toUpperCase());
                    LeatherArmorMeta meta = (LeatherArmorMeta) event.getCurrentItem().getItemMeta();
                    String teamName = Java.capFirst(PartySystem.getFromPlayer(player).getTeam(player).name());
                    meta.setDisplayName("§7§lChange your team: " + Minecraft.teamColor(teamName, true) + "§l" + teamName);
                    meta.setColor(Minecraft.translateChatColorToColor(Minecraft.teamColor(teamName, true)));
                    event.getClickedInventory().getItem(PartySystem.SLOT_TEAM).setItemMeta(meta);
                }
                case PartySystem.SLOT_WARP ->  {
                    player.closeInventory();
                    player.performCommand("party warp");
                }
                case PartySystem.SLOT_DISBAND -> {
                    player.closeInventory();
                    player.performCommand("party disband");
                }
            }
        }
        if (PartySystem.toInvite(player) != null && event.getClickedInventory().getName().equalsIgnoreCase(Objects.requireNonNull(PartySystem.toInvite(player)).getName())) {
            event.setCancelled(true);
            if (event.getCurrentItem() != null && event.getCurrentItem().getType() == Material.SKULL_ITEM) {
                SkullMeta skull = (SkullMeta) event.getCurrentItem().getItemMeta();
                if (Bukkit.getPlayer(skull.getOwner()) != null) {
                    player.performCommand("party invite " + skull.getOwner());
                    event.getWhoClicked().closeInventory();
                }
            }
        }
        if (PartySystem.getFromPlayer(player).toManage() != null && event.getClickedInventory().getName().equalsIgnoreCase(PartySystem.getFromPlayer(player).toManage().getName())) {
            event.setCancelled(true);
            if (event.getCurrentItem() != null && event.getCurrentItem().getType() == Material.SKULL_ITEM) {
                SkullMeta skull = (SkullMeta) event.getCurrentItem().getItemMeta();
                if (Bukkit.getPlayer(skull.getOwner()) != null) player.openInventory(playerMan(Bukkit.getPlayer(skull.getOwner())));
            }
        }
        if (event.getClickedInventory().getName().contains("§c§lManage ")) {
            event.setCancelled(true);
            OfflinePlayer player1 = DataPlayer.getPlayer(event.getClickedInventory().getName().replace("§c§lManage ", ""));
            if (player1 == null || player1.getPlayer() == null) return;
            switch (event.getSlot()) {
                case 2 -> {
                    player.performCommand("party kick " + player1.getName());
                    player.closeInventory();
                }
                case 4 -> {
                    if (event.getCurrentItem() == null || event.getCurrentItem().getItemMeta().getDisplayName() == null) return;

                    flip(player1.getPlayer(), event.getCurrentItem().getItemMeta().getDisplayName().substring(event.getCurrentItem().getItemMeta().getDisplayName().indexOf(": ") + 6).toUpperCase());

                    LeatherArmorMeta meta = (LeatherArmorMeta) event.getCurrentItem().getItemMeta();
                    String teamName = Java.capFirst(PartySystem.getFromPlayer(player).getTeam(player1.getPlayer()).name());
                    meta.setDisplayName("§7§lChange " + DataPlayer.getUser(player1).getNickName() + "'s team: " + Minecraft.teamColor(teamName, true) + "§l" + teamName);
                    meta.setColor(Minecraft.translateChatColorToColor(Minecraft.teamColor(teamName, true)));
                    event.getClickedInventory().getItem(4).setItemMeta(meta);
                }
                case 6 -> {
                    PartySystem.getFromPlayer(player).setLeader(player1);
                    player1.getPlayer().sendMessage(PREFIX_PARTY + DataPlayer.getUser(player).getNickName() + " promoted you to party leader.");
                    player.sendMessage(PREFIX_PARTY + "Promoted " + DataPlayer.getUser(player1).getNickName() + " to party leader.");
                    player.closeInventory();
                }
            }
        }
        if (event.getClickedInventory().getName().equalsIgnoreCase("§d§lParty Menu (Player)")) {
            event.setCancelled(true);
            switch (event.getSlot()) {
                case 1 -> {
                    player.closeInventory();
                    player.performCommand("party leave");
                }
                case 3 -> {
                    if (event.getCurrentItem() == null || event.getCurrentItem().getItemMeta().getDisplayName() == null) return;

                    flip(player, event.getCurrentItem().getItemMeta().getDisplayName().substring(event.getCurrentItem().getItemMeta().getDisplayName().indexOf(": ") + 6).toUpperCase());
                    LeatherArmorMeta meta = (LeatherArmorMeta) event.getCurrentItem().getItemMeta();
                    String teamName = Java.capFirst(PartySystem.getFromPlayer(player).getTeam(player).name());
                    meta.setDisplayName("§7§lChange your team: " + Minecraft.teamColor(teamName, true) + "§l" + teamName);
                    meta.setColor(Minecraft.translateChatColorToColor(Minecraft.teamColor(teamName, true)));
                    event.getClickedInventory().getItem(3).setItemMeta(meta);
                }
                case 5 -> {
                    player.closeInventory();
                    player.performCommand("party list");
                }
                case 7 -> {
                    if (PartySystem.toInvite(player) != null) player.openInventory(PartySystem.toInvite(player));
                }
            }
        }
    }

    public void flip(Player player, String oldTeamName) {
        PartySystem.getFromPlayer(player).changeTeam(player, switch (oldTeamName) {
            case "NONE" -> PartyTeam.GOOD;
            case "GOOD" -> PartyTeam.BAD;
            case "BAD" -> PartyTeam.RED;
            case "RED" -> PartyTeam.BLUE;
            case "BLUE" -> PartyTeam.GREEN;
            case "GREEN" -> PartyTeam.YELLOW;
            case "YELLOW" -> PartyTeam.NONE;
            default -> null;
        });
    }
}