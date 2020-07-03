package io.github.rookietec9.enderplugin.events.games.booty;

import io.github.rookietec9.enderplugin.Inventories;
import io.github.rookietec9.enderplugin.scoreboards.BootyBoard;
import io.github.rookietec9.enderplugin.utils.datamanagers.DataPlayer;
import io.github.rookietec9.enderplugin.utils.datamanagers.Pair;
import io.github.rookietec9.enderplugin.utils.methods.Java;
import io.github.rookietec9.enderplugin.utils.methods.Minecraft;
import io.github.rookietec9.enderplugin.utils.reference.Prefixes;
import io.github.rookietec9.enderplugin.utils.reference.Teams;
import io.github.rookietec9.enderplugin.utils.reference.Worlds;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.ArrayList;

import static org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

/**
 * @author Jeremi
 * @version 22.8.0
 * @since 12.2.7
 */
public class BootyTeam implements Listener {

    @SafeVarargs
    public static void setBlocks(Location start, Location finish, Pair<Double, Block>... pattern) {

        ArrayList<Block> blocks = new ArrayList<>();

        for (Pair<Double, Block> pair : pattern) for (int i = 0; i < pair.getKey(); i++) blocks.add(pair.getValue());

        for (int i = start.getBlockX(); i <= finish.getBlockX(); i++)
            for (int j = start.getBlockZ(); j <= finish.getBlockZ(); j++)
                for (int k = start.getBlockY(); k <= finish.getBlockY(); k++) blocks.get(Java.getRandom(0, blocks.size() - 1)).affect(i, k, j);
    }

    @EventHandler
    public void run(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (!player.getWorld().getName().equalsIgnoreCase(Worlds.BOOTY)) return;
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK && event.getClickedBlock().getType() == Material.STONE_BUTTON) {
            if (event.getClickedBlock().getLocation().getBlockZ() == -22) player.openInventory(Inventories.BOOTY_MAP);
            if (event.getClickedBlock().getLocation().getBlockZ() == -25) player.openInventory(Inventories.BOOTY_TEAM);
        }
    }

    @EventHandler
    public void run(PlayerMoveEvent event) {
        if (!event.getPlayer().getWorld().getName().equalsIgnoreCase(Worlds.BOOTY) || event.getPlayer().getGameMode() != GameMode.ADVENTURE) return;

        if (event.getTo().getBlockX() != 27) return;
        if (event.getTo().getBlockZ() != -23 && event.getTo().getBlockZ() != -24) return;

        if (event.getTo().getBlock().getType() == Material.STONE_PLATE) {
            String teamName;
            if (Teams.getTeam(event.getPlayer()) == null) teamName = getNewTeam();
            else teamName = Teams.getTeam(event.getPlayer()).getName();

            if (!Java.argWorks(teamName, Teams.blueTeam, Teams.greenTeam, Teams.redTeam, Teams.whiteTeam)) teamName = getNewTeam();

            teamName = teamName.replace("team-", "").replace("white", "yellow");
            updatePlayer(event.getPlayer(), teamName, false, ChatColor.valueOf(teamName.toUpperCase()));
        }
    }

    @EventHandler
    public void run(InventoryClickEvent event) {
       if (event.getClickedInventory() == null || event.getClickedInventory().getTitle() == null || !(event.getWhoClicked() instanceof Player)) return;
        if (event.getClickedInventory().getTitle().equalsIgnoreCase("§b§lCHOOSE A MAP")) {
            event.setCancelled(true);

            Player player = (Player) event.getWhoClicked();
            switch (event.getSlot()) {
                case 9 -> setChunk(player, "plain", new Pair<>(1000.0, new Block(Material.QUARTZ_BLOCK)));
                case 11 -> setChunk(player, "classic", new Pair<>(800.0, new Block(Material.GOLD_BLOCK)), new Pair<>(200.0, new Block(Material.AIR)));
                case 13 -> setChunk(player, "fancy", new Pair<>(30.0, new Block(Material.STAINED_CLAY, (byte) 6)), new Pair<>(10.0, new Block(Material.COAL_BLOCK)), new Pair<>(850.0, new Block(Material.AIR)), new Pair<>(110.0, new Block(Material.QUARTZ_BLOCK)));
                case 15 -> setChunk(player, "glassy", new Pair<>(795.0, new Block(Material.AIR)), new Pair<>(100.0, new Block(Material.GLASS)), new Pair<>(55.0, new Block(Material.IRON_BLOCK)), new Pair<>(30.0, new Block(Material.SPONGE)), new Pair<>(4.0, new Block(Material.MYCEL)), new Pair<>(16.0, new Block(Material.DIAMOND_BLOCK)));
                case 17 -> setChunk(player, "easy", new Pair<>(500.0, new Block(Material.AIR)), new Pair<>(500.0, new Block(Material.GOLD_BLOCK)));
                case 27 -> setChunk(player, "insane", new Pair<>(795.0, new Block(Material.AIR)), new Pair<>(50.0, new Block(Material.QUARTZ_BLOCK)), new Pair<>(150.0, new Block(Material.WOOD)), new Pair<>(5.0, new Block(Material.COAL_BLOCK)));
                case 29 -> setChunk(player, "buffed", new Pair<>(795.0, new Block(Material.AIR)), new Pair<>(5.0, new Block(Material.SPONGE)), new Pair<>(40.0, new Block(Material.QUARTZ)), new Pair<>(50.0, new Block(Material.DIAMOND_BLOCK)), new Pair<>(40.0, new Block(Material.DIRT)), new Pair<>(70.0, new Block(Material.IRON_BLOCK)));
                case 31 -> setChunk(player, "open", new Pair<>(950.0, new Block(Material.AIR)), new Pair<>(20.0, new Block(Material.IRON_BLOCK)), new Pair<>(30.0, new Block(Material.QUARTZ_BLOCK)));
                case 33 -> setChunk(player, "spooky", new Pair<>(850.0, new Block(Material.AIR)), new Pair<>(50.0, new Block(Material.WEB)), new Pair<>(50.0, new Block(Material.IRON_BLOCK)), new Pair<>(50.0, new Block(Material.DIRT)));
                case 35 -> setChunk(player, "slimy", new Pair<>(795.0, new Block(Material.AIR)), new Pair<>(105.0, new Block(Material.SLIME_BLOCK)), new Pair<>(50.0, new Block(Material.IRON_BLOCK)), new Pair<>(50.0, new Block(Material.DIAMOND_BLOCK)));
            }
        }

        if (event.getClickedInventory().getTitle().equalsIgnoreCase("§3§lCHOOSE A TEAM")) {
            event.setCancelled(true);
            if (event.getCurrentItem() == null || event.getCurrentItem().getType() == Material.AIR || !(event.getWhoClicked() instanceof Player)) return;
            String team = event.getCurrentItem().getItemMeta().getDisplayName().replace(" TEAM", "");
            updatePlayer((Player) event.getWhoClicked(), ChatColor.stripColor(team), true, ChatColor.valueOf(ChatColor.stripColor(team)));
        }
    }

    @EventHandler
    public void run(InventoryDragEvent event) {
        if (Java.argWorks(event.getInventory().getTitle(), "§3§lCHOOSE A TEAM", "§b§lCHOOSE A MAP") && event.getWhoClicked().getGameMode() == GameMode.ADVENTURE && event.getWhoClicked().getWorld().getName().equalsIgnoreCase(Worlds.BOOTY)) event.setCancelled(true);
    }

    @SafeVarargs
    private void setChunk(Player player, String mapType, Pair<Double, Block>... pattern) {
        setBlocks(new Location(Bukkit.getWorld(Worlds.BOOTY), 34, 7, -74), new Location(Bukkit.getWorld(Worlds.BOOTY), 81, 17, -47), pattern);
        setBlocks(new Location(Bukkit.getWorld(Worlds.BOOTY), 44, 7, -84), new Location(Bukkit.getWorld(Worlds.BOOTY), 71, 17, -37), pattern);
        player.sendMessage(Prefixes.BOOTY + "Updated the map to " + Java.capFirst(mapType));
        for (Player p : Bukkit.getWorld(Worlds.BOOTY).getPlayers()) {
            DataPlayer.get(p).getBoard(BootyBoard.class).updateMap(mapType);
        }
    }

    private void updatePlayer(Player player, String team, boolean join, ChatColor chatColor) {
        DataPlayer.getUser(player).reset(GameMode.ADVENTURE).
                fromChestArmor(player.getWorld(), 29, Java.argWorks(team.toLowerCase(), "red", "green") ? 5 : 4, Java.argWorks(team.toLowerCase(), "red", "blue") ? -26 : -21);

        switch (team.toLowerCase()) {
            case "red" -> player.teleport(new Location(player.getWorld(), 81.5, 19, -36.5, 134.89F, -0.108F), TeleportCause.PLUGIN);
            case "blue" -> player.teleport(new Location(player.getWorld(), 81.5, 19, -83.5, 45.25F, -0.18F), TeleportCause.PLUGIN);
            case "yellow" -> player.teleport(new Location(player.getWorld(), 34.5, 19, -83.5, 314.82F, 0.18F), TeleportCause.PLUGIN);
            case "green" -> player.teleport(new Location(player.getWorld(), 34.5, 19, -36.5, 224.68F, 0.99F), TeleportCause.PLUGIN);
        }

        Teams.add("team-" + team.toLowerCase().replace("yellow", "white"), player);
        if (join) Minecraft.worldBroadcast(Worlds.BOOTY, Prefixes.BOOTY + chatColor + DataPlayer.getUser(player).getNickName() + ChatColor.GRAY + " joined the " + chatColor + team.toLowerCase() + ChatColor.GRAY + " team.");
    }

    private String getNewTeam() {
        int redC = 0, bluC = 0, grnC = 0, whtC = 0;
        for (Player p : Bukkit.getWorld(Worlds.BOOTY).getPlayers()) {
            if (Teams.getTeam(p) != null)
                switch (Teams.getTeam(p).getName()) {
                    case Teams.blueTeam -> bluC++;
                    case Teams.redTeam -> redC++;
                    case Teams.whiteTeam -> whtC++;
                    case Teams.greenTeam -> grnC++;
                }
        }

        int sum = redC + bluC + grnC + whtC;

        if (sum == 0) {
            return switch (Java.getRandom(0, 3)) {
                case 0 -> Teams.blueTeam;
                case 1 -> Teams.redTeam;
                case 2 -> Teams.whiteTeam;
                case 3 -> Teams.greenTeam;
                default -> "";
            };
        }

        if (sum == 1) {
            if (redC == 1) return Teams.whiteTeam;
            if (bluC == 1) return Teams.greenTeam;
            if (grnC == 1) return Teams.blueTeam;
            if (whtC == 1) return Teams.redTeam;
        }
        if (sum == 2) {
            if (redC == 2 || redC == 1 && whtC == 0) return Teams.whiteTeam;
            if (bluC == 2 || bluC == 1 && grnC == 0) return Teams.greenTeam;
            if (grnC == 2 || grnC == 1 && bluC == 0) return Teams.blueTeam;
            if (whtC == 2 || whtC == 1 && redC == 0) return Teams.redTeam;

            if (redC == 0) return Teams.redTeam;
            if (bluC == 0) return Teams.blueTeam;
            if (grnC == 0) return Teams.greenTeam;
            if (whtC == 0) return Teams.whiteTeam;
        }

        if (sum == 3) {
            if (redC == 3 || redC == 2 && whtC == 0) return Teams.whiteTeam;
            if (bluC == 3 || bluC == 2 && grnC == 0) return Teams.greenTeam;
            if (grnC == 3 || grnC == 2 && bluC == 0) return Teams.blueTeam;
            if (whtC == 3 || whtC == 2 && redC == 0) return Teams.redTeam;

            if (redC == 0) return Teams.redTeam;
            if (bluC == 0) return Teams.blueTeam;
            if (grnC == 0) return Teams.greenTeam;
            if (whtC == 0) return Teams.whiteTeam;
        }
        return "";
    }

    public static class Block {
        final Material m;
        byte b;

        public Block(Material m, byte b) {
            this.m = m;
            this.b = b;
        }

        public Block(Material m) {
            this.m = m;
        }

        void affect(int x, int y, int z) {
            Bukkit.getWorld(Worlds.BOOTY).getBlockAt(x, y, z).setType(m);
            Bukkit.getWorld(Worlds.BOOTY).getBlockAt(x, y, z).setData(b);
        }
    }
}