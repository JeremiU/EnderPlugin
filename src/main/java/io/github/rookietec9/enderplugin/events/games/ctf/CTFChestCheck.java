package io.github.rookietec9.enderplugin.events.games.ctf;

import io.github.rookietec9.enderplugin.API.Utils;
import io.github.rookietec9.enderplugin.xboards.CTFBoard;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;
import org.bukkit.scoreboard.Scoreboard;

import static io.github.rookietec9.enderplugin.API.Utils.Reference.Worlds.CTF;

/**
 * @author Jeremi
 * @version 16.4.0
 * @since 11.6.9
 */
public class CTFChestCheck implements Listener {

    @EventHandler
    public void stealFlag(InventoryMoveItemEvent event) {}

    @EventHandler
    public void win(InventoryCloseEvent event) {}

}