package io.github.rookietec9.enderplugin.events.games.spleef;

import io.github.rookietec9.enderplugin.API.Utils;
import io.github.rookietec9.enderplugin.API.configs.associates.User;
import io.github.rookietec9.enderplugin.EnderPlugin;
import io.github.rookietec9.enderplugin.xboards.SpleefBoard;
import org.bukkit.*;
import org.bukkit.block.Sign;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import static io.github.rookietec9.enderplugin.EnderPlugin.Hashmaps.spleefEvent1;
import static io.github.rookietec9.enderplugin.EnderPlugin.Hashmaps.spleefEvent2;
import static io.github.rookietec9.enderplugin.EnderPlugin.Hashmaps.spleefEv1ID;
import static io.github.rookietec9.enderplugin.EnderPlugin.Hashmaps.spleefEv2ID;

import static io.github.rookietec9.enderplugin.API.Utils.Reference.Teams;
import static io.github.rookietec9.enderplugin.API.Utils.Reference.Worlds;

/**
 * @author Jeremi
 * @version 16.4.1
 * @since 14.8.5
 */
public class JoinEvent implements Listener {

    private Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();

    @EventHandler
    public void run(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (!player.getWorld().getName().equalsIgnoreCase(Worlds.SPLEEF)) return;
        if (player.getGameMode() != GameMode.SURVIVAL) return;

        if (event.getClickedBlock() != null)
            if (event.getClickedBlock().getState() instanceof Sign) {
                Sign sign = (Sign) event.getClickedBlock().getState();
                if (sign.getLine(1).equalsIgnoreCase("§7[§9§lJOIN§7]")) {
                    if (Bukkit.getWorld(Worlds.SPLEEF).getPlayers().size() < 2) {
                        player.sendMessage("§f§lSP§9§lLE§f§lEF §7> You need at least two players!");
                        return;
                    }

                    if (spleefEvent1 || spleefEvent2) {
                        player.sendMessage("§f§lSP§9§lLE§f§lEF §7> Wait for the match to finish!");
                        return;
                    }


                    for (Team team : scoreboard.getTeams()) {
                        for (String string : team.getEntries())
                            if (Bukkit.getPlayer(string) == null || (Bukkit.getPlayer(string) != null && Bukkit.getPlayer(string).getWorld().getName().equalsIgnoreCase(Worlds.SPLEEF)))
                                team.removeEntry(string);
                    }
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
                        for (String string : scoreboard.getTeam(teamName[i]).getEntries()) {
                            if (Bukkit.getPlayer(string) != null)
                                Bukkit.getPlayer(string).teleport(locations[i]);
                        }

                for (Player player1 : Bukkit.getWorld(Worlds.SPLEEF).getPlayers()) {
                    player1.getActivePotionEffects().clear();

                    Material[] materials = new Material[]{Material.DIAMOND_SPADE, Material.LEATHER_HELMET, Material.LEATHER_CHESTPLATE, Material.LEATHER_LEGGINGS, Material.LEATHER_BOOTS};
                    int[] slots = new int[]{0, 39, 38, 37, 36};
                    player1.getInventory().getArmorContents();

                    Team[] teams = new Team[]{scoreboard.getTeam(Teams.goodTeam), scoreboard.getTeam(Teams.whiteTeam), scoreboard.getTeam(Teams.greenTeam), scoreboard.getTeam(Teams.blueTeam), scoreboard.getTeam(Teams.redTeam)};
                    Color[] colors = new Color[]{Color.fromRGB(150,150,150), Color.fromRGB(150,150,0), Color.fromRGB(0,150,0), Color.fromRGB(0,0,150), Color.fromRGB(150,0,0)};

                    new User(player1).clear();

                    player1.setFlying(false);
                    player1.setAllowFlight(false);

                    for (int i = 0; i < materials.length; i++) {
                        ItemStack itemStack = new ItemStack(materials[i]);
                        ItemMeta meta = itemStack.getItemMeta();
                        if (materials[i] != Material.DIAMOND_SPADE) meta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 10, true);
                        if (materials[i] == Material.LEATHER_BOOTS) meta.addEnchant(Enchantment.PROTECTION_FALL, 5, true);
                        if (materials[i] == Material.LEATHER_CHESTPLATE) meta.addEnchant(Enchantment.THORNS, 17, true);
                        meta.spigot().setUnbreakable(true);
                        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                        meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
                        if (meta instanceof LeatherArmorMeta) for (int j = 0; j < teams.length; j++) if (teams[j].hasEntry(player1.getName())) ((LeatherArmorMeta) meta).setColor(colors[j]);
                        itemStack.setItemMeta(meta);
                        player1.getInventory().setItem(slots[i], itemStack);
                        player1.updateInventory();
                    }

                    MineEvent.hashMap.put(player1, 0);

                    SpleefBoard spleefBoard = new SpleefBoard(player);
                    spleefBoard.updateTicks();

                    spleefBoard.init();
                    spleefBoard.updatePlayers(Bukkit.getWorld(Worlds.SPLEEF).getPlayers().size());
                }

                if (!spleefEvent1) {
                    spleefEv1ID = Bukkit.getScheduler().scheduleSyncRepeatingTask(EnderPlugin.getInstance(), MineEvent.runnable(), 0, 200);
                    spleefEvent1 = true;
                }
                if (!spleefEvent2) {
                    spleefEv2ID = Bukkit.getScheduler().scheduleSyncRepeatingTask(EnderPlugin.getInstance(), runnable(), 0, 20);
                    spleefEvent2 = true;
                }
            }
    }

    private void teams() {
        int playersSize = Bukkit.getWorld(Worlds.SPLEEF).getPlayers().size();
        Player[] players = new Player[playersSize];

        for (int i  = 0; i < players.length; i++) players[i] = Bukkit.getWorld(Worlds.SPLEEF).getPlayers().subList(0, playersSize).get(i);

        boolean doFour  = playersSize % 4 == 0;
        boolean doTwo   = playersSize % 2 == 0 && !doFour;
        boolean doThree = playersSize % 3 == 0 && !doTwo;

        boolean singleFour  = playersSize -1 % 4 == 0;
        boolean singleTwo   = playersSize -1 % 2 == 0 && !singleFour;
        boolean singleThree = playersSize -1 % 3 == 0 && !singleTwo;

        int a = 0, b = 0, c = 0, d = 0;
        Team[] team = new Team[]{scoreboard.getTeam(Teams.whiteTeam), scoreboard.getTeam(Teams.greenTeam), scoreboard.getTeam(Teams.blueTeam), scoreboard.getTeam(Teams.redTeam)};

        if (singleFour || singleTwo || singleThree) {
            scoreboard.getTeam(Teams.goodTeam).addEntry(players[c].getName());
            b++;
            Utils.BukkitTools.worldBroadcast(Bukkit.getWorld(Worlds.SPLEEF), "§f§lSP§9§lLE§f§lEF §7> " + players[c].getName() + " joined the single team.");
        }

        a = (doFour || singleFour) ? 3 : a;
        a = (doThree || singleThree) ? 2 : a;
        a = (doTwo || singleTwo) ? 1 : a;

        for (d = c; d < players.length; d++) {
            b++;
            if (b > a) b = 0;
            team[b].addEntry(players[d].getName());
            Utils.BukkitTools.worldBroadcast(Bukkit.getWorld(Worlds.SPLEEF), "§f§lSP§9§lLE§f§lEF §7> " + players[d].getName() + " joined the " + team[b].getName().replace("team-", "").replace("white","yellow") + " team.");
        }
    }

    public Runnable runnable() {
        return () -> {
            EnderPlugin.Hashmaps.spleefTicks = EnderPlugin.Hashmaps.spleefTicks + 1;
            for (Player player : Bukkit.getWorld(Worlds.SPLEEF).getPlayers()) {
                SpleefBoard spleefBoard = new SpleefBoard(player);
                spleefBoard.updateTicks();
            }
        };
    }
}