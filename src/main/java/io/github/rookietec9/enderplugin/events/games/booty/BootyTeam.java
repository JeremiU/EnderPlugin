package io.github.rookietec9.enderplugin.events.games.booty;

import io.github.rookietec9.enderplugin.API.Item;
import io.github.rookietec9.enderplugin.API.SkullMaker;
import io.github.rookietec9.enderplugin.API.Utils;
import io.github.rookietec9.enderplugin.API.configs.associates.Games;
import io.github.rookietec9.enderplugin.API.configs.associates.User;
import io.github.rookietec9.enderplugin.xboards.BootyBoard;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.potion.PotionEffect;

/**
 * @author Jeremi
 * @version 14.8.2
 * @since 12.2.7
 */

public class BootyTeam implements Listener {

    private static Inventory mapChoose, teamChoose;

    static {
        mapChoose = Bukkit.createInventory(null, 45, "§b§lCHOOSE A MAP");
        mapChoose.setItem(9, new Item("§fPLAIN", Material.QUARTZ_BLOCK).getItem());
        mapChoose.setItem(11, new Item("§7CLASSIC", Material.STAINED_CLAY, (byte) 8).getItem());
        mapChoose.setItem(13, new Item("§fFANCY", Material.STAINED_CLAY, (byte) 0).getItem());
        mapChoose.setItem(15, new Item("§7GLASSY", Material.GLASS).getItem());
        mapChoose.setItem(17, new Item("§dEASY", Material.STAINED_CLAY, (byte) 6).getItem());

        mapChoose.setItem(27, new Item("§6INSANE", Material.STAINED_CLAY, (byte) 1).getItem());
        mapChoose.setItem(29, new Item("§bBUFFED", Material.DIAMOND_BLOCK).getItem());
        mapChoose.setItem(31, new Item("§eOPEN", Material.STAINED_CLAY, (byte) 4).getItem());
        mapChoose.setItem(33, new Item("§4SPOOKY", Material.WEB).getItem());
        mapChoose.setItem(35, new Item("§aSLIMY", Material.SLIME_BLOCK).getItem());

        teamChoose = Bukkit.createInventory(null, 27, "§3§lCHOOSE A TEAM");

        teamChoose.setItem(10, new SkullMaker().withSkinUrl("http://textures.minecraft.net/texture/c47237437eef639441b92b217efdc8a72514a9567c6b6b81b553f4ef4ad1cae", "§c§lRED TEAM").build());
        teamChoose.setItem(12, new SkullMaker().withSkinUrl("http://textures.minecraft.net/texture/cbdd969412df6e0acffbb7a73bfa34110eecc1f51d80ba0da25da439316bc", "§b§lBLUE TEAM").build());
        teamChoose.setItem(14, new SkullMaker().withSkinUrl("http://textures.minecraft.net/texture/97c2d5eee84bba1d7e94f933a0a556ed7ea4e4fa65e8e9f56325813b", "§6§lYELLOW TEAM").build());
        teamChoose.setItem(16, new SkullMaker().withSkinUrl("http://textures.minecraft.net/texture/78d58a7651fedae4c03efebc226c03fd791eb74a132babb974e8d838ac6882", "§2§lGREEN TEAM").build());
    }

    @EventHandler
    public void run(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (!player.getWorld().getName().equalsIgnoreCase(Utils.Reference.Worlds.BOOTY)) return;
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK && event.getClickedBlock().getType() == Material.STONE_BUTTON) {
            if (event.getClickedBlock().getLocation().getBlockZ() == -22) player.openInventory(mapChoose);
            if (event.getClickedBlock().getLocation().getBlockZ() == -25) player.openInventory(teamChoose);
        }
    }

    @EventHandler
    public void run(PlayerMoveEvent event) {
        if (!event.getPlayer().getWorld().getName().equalsIgnoreCase(Utils.Reference.Worlds.BOOTY) || event.getPlayer().getGameMode() != GameMode.ADVENTURE)
            return;

        if (event.getTo().getBlockX() != 27) return;
        if (event.getTo().getBlockZ() != -23 && event.getTo().getBlockZ() != -24) return;

        if (event.getTo().getBlock().getType() == Material.STONE_PLATE) {
            if (Bukkit.getScoreboardManager().getMainScoreboard().getEntryTeam(event.getPlayer().getName()) == null)
                return;
            switch (Bukkit.getScoreboardManager().getMainScoreboard().getEntryTeam(event.getPlayer().getName()).getName()) {
                case Utils.Reference.Teams.blueTeam: {
                    updatePlayer(event.getPlayer(), "blue", false, null);
                    return;
                }
                case Utils.Reference.Teams.greenTeam: {
                    updatePlayer(event.getPlayer(), "green", false, null);
                    return;
                }
                case Utils.Reference.Teams.redTeam: {
                    updatePlayer(event.getPlayer(), "red", false, null);
                    return;
                }
                case Utils.Reference.Teams.whiteTeam: {
                    updatePlayer(event.getPlayer(), "yellow", false, null);
                }
            }
        }
    }

    @EventHandler
    public void run(InventoryClickEvent event) {
        if (event.getClickedInventory() == null) return;
        if (event.getClickedInventory().getTitle() == null) return;
        if (event.getClickedInventory().getTitle().equalsIgnoreCase("§b§lCHOOSE A MAP")) {
            event.setCancelled(true);
            if (!(event.getWhoClicked() instanceof Player)) return;

            Player player = (Player) event.getWhoClicked();

            switch (event.getSlot()) {
                case 9: {
                    setChunk(player, "quartz", "plain");
                    return;
                }
                case 11: {
                    setChunk(player, "80%air,20%gold", "classic");
                    return;
                }
                case 13: {
                    setChunk(player, "2%stained_clay:6,2%coalblock,85%air,9%quartz", "fancy");
                    return;
                }
                case 15: {
                    setChunk(player, "79.5%air,10%glass,5.5%iron,0.25%mycelium,1.75%diamond,3%sponge", "glassy");
                    return;
                }
                case 17: {
                    setChunk(player, "50%air,50%gold", "easy");
                    return;
                }
                case 27: {
                    setChunk(player, "79.5%air,5%quartz,15%wood,0.5%coalblock", "insane");
                    return;
                }
                case 29: {
                    setChunk(player, "6%quartz,1%diamond,1%dirt,2%iron,0.5%sponge,79.5%air", "buffed");
                    return;
                }
                case 31: {
                    setChunk(player, "95%air,2%iron,3%quartz", "open");
                    return;
                }
                case 33: {
                    setChunk(player, "85%air,5%iron,5%web,5%dirt", "spooky");
                    return;
                }
                case 35: {
                    setChunk(player, "79.5%air,10.5%slimeblock,5%ironblock,5%diamondblock", "slimy");
                }
            }
        }

        if (event.getClickedInventory().getTitle().equalsIgnoreCase("§3§lCHOOSE A TEAM")) {
            event.setCancelled(true);
            if (event.getCurrentItem() == null || event.getCurrentItem().getType() == Material.AIR) return;
            if (!(event.getWhoClicked() instanceof Player)) return;
            Player player = (Player) event.getWhoClicked();
            switch (ChatColor.stripColor(event.getCurrentItem().getItemMeta().getDisplayName())) {
                case "RED TEAM": {
                    updatePlayer(player, "red", true, ChatColor.RED);
                    return;
                }
                case "BLUE TEAM": {
                    updatePlayer(player, "blue", true, ChatColor.BLUE);
                    return;
                }
                case "YELLOW TEAM": {
                    updatePlayer(player, "yellow", true, ChatColor.YELLOW);
                    return;
                }
                case "GREEN TEAM": {
                    updatePlayer(player, "green", true, ChatColor.GREEN);
                }
            }
        }
    }

    @EventHandler
    public void run(InventoryDragEvent event) {
        if ((event.getInventory().getTitle().equalsIgnoreCase("§3§lCHOOSE A TEAM") || event.getInventory().getTitle().equalsIgnoreCase("§b§lCHOOSE A MAP"))
                && event.getWhoClicked().getGameMode() == GameMode.ADVENTURE && event.getWhoClicked().getWorld().getName().equalsIgnoreCase(Utils.Reference.Worlds.BOOTY))
            event.setCancelled(true);
    }

    private void setChunk(Player player, String setting, String mapType) {
        player.performCommand("/pos1 44,17,-37");
        player.performCommand("/pos2 71,7,-84");
        player.performCommand("/set " + setting);

        player.performCommand("/pos1 34,17,-47");
        player.performCommand("/pos2 81,7,-74");
        player.performCommand("/set " + setting);

        player.performCommand("/deSel");

        for (Player p : Bukkit.getWorld(Utils.Reference.Worlds.BOOTY).getPlayers()) {
            BootyBoard bootyBoard = new BootyBoard(p);
            bootyBoard.init();
            bootyBoard.updateMap(mapType);
        }
    }

    private void updatePlayer(Player player, String team, boolean join, ChatColor chatColor) {
        User u = new User(player).setGod(false).clear();
        player.setGameMode(GameMode.ADVENTURE);
        player.setHealth(20.0D);
        player.setFireTicks(-20);

        for (PotionEffect p : player.getActivePotionEffects()) player.removePotionEffect(p.getType());
        switch (team.toLowerCase()) {
            case "red": {
                new User(player).fromChest(player.getWorld(), 29, 5, -26);
                player.teleport(new Location(player.getWorld(), 81.5, 19, -36.5, 134.89F, -0.108F));
                Bukkit.getScoreboardManager().getMainScoreboard().getTeam(Utils.Reference.Teams.redTeam).addEntry(player.getName());
                if (join) for (Player player1 : Bukkit.getWorld(Utils.Reference.Worlds.BOOTY).getPlayers())
                    player1.sendMessage("§f§LBO§3§lO§f§lTY §7> " + chatColor + player.getName() + "" + ChatColor.GRAY + " joined the " + chatColor + team.toLowerCase() + ChatColor.GRAY + " team.");
                return;
            }
            case "blue": {
                new User(player).fromChest(player.getWorld(), 29, 4, -26);
                player.teleport(new Location(player.getWorld(), 81.5, 19, -83.5, 45.25F, -0.18F));
                Bukkit.getScoreboardManager().getMainScoreboard().getTeam(Utils.Reference.Teams.blueTeam).addEntry(player.getName());
                if (join) for (Player player1 : Bukkit.getWorld(Utils.Reference.Worlds.BOOTY).getPlayers())
                    player1.sendMessage("§f§LBO§3§lO§f§lTY §7> " + chatColor + player.getName() + "" + ChatColor.GRAY + " joined the " + chatColor + team.toLowerCase() + ChatColor.GRAY + " team.");
                return;
            }
            case "yellow": {
                u.fromChest(player.getWorld(), 29, 4, -21);
                player.teleport(new Location(player.getWorld(), 34.5, 19, -83.5, 314.82F, 0.18F));
                Bukkit.getScoreboardManager().getMainScoreboard().getTeam(Utils.Reference.Teams.whiteTeam).addEntry(player.getName());
                if (join) for (Player player1 : Bukkit.getWorld(Utils.Reference.Worlds.BOOTY).getPlayers())
                    player1.sendMessage("§f§LBO§3§lO§f§lTY §7> " + chatColor + player.getName() + "" + ChatColor.GRAY + " joined the " + chatColor + team.toLowerCase() + ChatColor.GRAY + " team.");
                return;
            }
            case "green": {
                u.fromChest(player.getWorld(), 29, 5, -21);
                player.teleport(new Location(player.getWorld(), 34.5, 19, -36.5, 224.68F, 0.99F));
                Bukkit.getScoreboardManager().getMainScoreboard().getTeam(Utils.Reference.Teams.greenTeam).addEntry(player.getName());
                if (join) for (Player player1 : Bukkit.getWorld(Utils.Reference.Worlds.BOOTY).getPlayers())
                    player1.sendMessage("§f§LBO§3§lO§f§lTY §7> " + chatColor + player.getName() + "" + ChatColor.GRAY + " joined the " + chatColor + team.toLowerCase() + ChatColor.GRAY + " team.");
            }
        }
    }
}