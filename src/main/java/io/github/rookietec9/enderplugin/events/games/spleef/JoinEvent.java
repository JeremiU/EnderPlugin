package io.github.rookietec9.enderplugin.events.games.spleef;

import io.github.rookietec9.enderplugin.EnderPlugin;
import io.github.rookietec9.enderplugin.scoreboards.SpleefBoard;
import io.github.rookietec9.enderplugin.utils.datamanagers.DataPlayer;
import io.github.rookietec9.enderplugin.utils.datamanagers.Item;
import io.github.rookietec9.enderplugin.utils.datamanagers.Pair;
import io.github.rookietec9.enderplugin.utils.methods.Minecraft;
import io.github.rookietec9.enderplugin.utils.reference.Prefixes;
import io.github.rookietec9.enderplugin.utils.reference.Teams;
import io.github.rookietec9.enderplugin.utils.reference.Worlds;
import org.bukkit.*;
import org.bukkit.block.Sign;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.ArrayList;


/**
 * @author Jeremi
 * @version 22.8.0
 * @since 14.8.5
 */
public class JoinEvent implements Listener {

    private final Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();

    @EventHandler
    public void run(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (!player.getWorld().getName().equalsIgnoreCase(Worlds.SPLEEF)) return;
        if (player.getGameMode() != GameMode.SURVIVAL) return;

        if (event.getClickedBlock() != null && event.getClickedBlock().getState() instanceof Sign) {
                Sign sign = (Sign) event.getClickedBlock().getState();
                if (sign.getLine(1).equalsIgnoreCase("§9§lJoin")) {
                    if (Bukkit.getWorld(Worlds.SPLEEF).getPlayers().size() < 2) {
                        player.sendMessage(Prefixes.SPLEEF + "You need at least two players!");
                        return;
                    }
                    if (Bukkit.getWorld(Worlds.SPLEEF).getPlayers().size() > 8) {
                        Minecraft.worldBroadcast(Worlds.SPLEEF, Prefixes.SPLEEF + "There are too many players to start a spleef game!");
                        return;
                    }

                    if (EnderPlugin.scheduler().isRunning("SPLEEF_MINE") || EnderPlugin.scheduler().isRunning("SPLEEF_TICKER")) {
                        player.sendMessage(Prefixes.SPLEEF + "Wait for the match to finish!");
                        return;
                    }

                    for (Team team : scoreboard.getTeams()) for (String string : team.getEntries()) if (Bukkit.getPlayer(string) == null || (Bukkit.getPlayer(string) != null && Bukkit.getPlayer(string).getWorld().getName().equalsIgnoreCase(Worlds.SPLEEF))) team.removeEntry(string);
                    teams();
                }

                String[] teamName = new String[]{Teams.redTeam, Teams.blueTeam, Teams.greenTeam, Teams.whiteTeam};
                Location[] locations = new Location[]{
                        new Location(Bukkit.getWorld(Worlds.SPLEEF), -51.5, 4, -28.5, -90F, 0.16F), new Location(Bukkit.getWorld(Worlds.SPLEEF), -4, 4, -28.5, -270F, 0.46F),
                        new Location(Bukkit.getWorld(Worlds.SPLEEF), -27.5, 4, -53, 0.2F, 0.46F), new Location(Bukkit.getWorld(Worlds.SPLEEF), -28, 4, -5, 180F, 0.46F),
                        new Location(Bukkit.getWorld(Worlds.SPLEEF), -28, 4, -29, 120F, -2F)
                };

                for (int i = 0; i < teamName.length; i++)
                    if (scoreboard.getTeam(teamName[i]).getSize() != 0)
                        for (String string : scoreboard.getTeam(teamName[i]).getEntries())
                            if (Bukkit.getPlayer(string) != null && Bukkit.getPlayer(string).getWorld().getName().equalsIgnoreCase(Worlds.SPLEEF)) Bukkit.getPlayer(string).teleport(locations[i], PlayerTeleportEvent.TeleportCause.PLUGIN);

                for (Player player1 : Bukkit.getWorld(Worlds.SPLEEF).getPlayers()) {
                    DataPlayer.getUser(player1).reset(GameMode.SURVIVAL);
                    player1.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 60 * 20, 4, true, true));

                    Material[] materials = new Material[]{Material.DIAMOND_SPADE, Material.LEATHER_HELMET, Material.LEATHER_CHESTPLATE, Material.LEATHER_LEGGINGS, Material.LEATHER_BOOTS};
                    int[] slots = new int[]{0, 39, 38, 37, 36};
                    player1.getInventory().getArmorContents();

                    for (int i = 0; i < materials.length; i++) {
                        Item<?> item = new Item<>(materials[i]);
                        item.setUnbreakable(true);
                        item.addFlag(ItemFlag.HIDE_UNBREAKABLE, ItemFlag.HIDE_ENCHANTS);

                        if (materials[i] != Material.DIAMOND_SPADE) item.addEnch(new Pair<>(Enchantment.PROTECTION_ENVIRONMENTAL, 10));
                        if (materials[i] == Material.LEATHER_BOOTS) item.addEnch(new Pair<>(Enchantment.PROTECTION_FALL, 10));
                        if (materials[i] == Material.LEATHER_CHESTPLATE) item.addEnch(new Pair<>(Enchantment.THORNS, 25));

                        String teamName2 = Teams.getTeam(player1).getName();
                        teamName2 = teamName2.replace("team-", "").replace("white", "yellow").toUpperCase();

                        if (item.isPaintAble()) item.setColor(Minecraft.translateChatColorToColor(ChatColor.valueOf(teamName2)));
                        player1.getInventory().setItem(slots[i], item.toItemStack());
                        player1.getInventory().setItem(8, new Item<>(Material.STICK, "§9Snowball Crafter", "§f4 Snowballs §9§l→§f 1 Snow Block", "").toItemStack());
                        player1.updateInventory();
                    }

                    MineEvent.hashMap.put(player1, 0);

                    DataPlayer.get(player).getBoard(SpleefBoard.class).updateTicks();
                    DataPlayer.get(player).getBoard(SpleefBoard.class).updatePlayers(Bukkit.getWorld(Worlds.SPLEEF).getPlayers().size());
                }

                EnderPlugin.scheduler().runRepeatingTask(MineEvent.runnable(), "SPLEEF_MINE", 0, 10);
                EnderPlugin.scheduler().runRepeatingTask(() -> {
                    DataPlayer.spleefSec++;
                    for (Player player1 : Bukkit.getWorld(Worlds.SPLEEF).getPlayers()) {
                        DataPlayer.get(player1).getBoard(SpleefBoard.class).updateTicks();
                    }
                }, "SPLEEF_TICKER", 0, 1);
            }
    }

    public void teams() {
        ArrayList<Player> playerList = new ArrayList<>(Bukkit.getWorld(Worlds.SPLEEF).getPlayers());
        Team[] team = {Teams.getTeam(Teams.whiteTeam), Teams.getTeam(Teams.greenTeam), Teams.getTeam(Teams.blueTeam), Teams.getTeam(Teams.redTeam)};

        int counter = 0;
        System.out.println(playerList.size() + "!/");
        if (playerList.size() % 3 == 0) {
            System.out.println("ah");
            team = new Team[]{Teams.getTeam(Teams.greenTeam), Teams.getTeam(Teams.blueTeam), Teams.getTeam(Teams.redTeam)};
        }

        while (playerList.size() > 0) {
            Player player = playerList.remove((int) (Math.random() * playerList.size()));
            team[counter % team.length].addEntry(player.getDisplayName());
            String teamName = team[counter % team.length].getName().replace("team-", "").replace("white", "yellow");
            ChatColor color = ChatColor.valueOf(teamName.toUpperCase());
            counter++;
            Minecraft.worldBroadcast(Bukkit.getWorld(Worlds.SPLEEF), Prefixes.SPLEEF + player.getName() + " joined the " + color + team[counter % team.length].getName().replace("team-", "").replace("white", "yellow") + "§7 team.");
        }
        DataPlayer.spleefLeft = Bukkit.getWorld(Worlds.SPLEEF).getPlayers();
    }
}