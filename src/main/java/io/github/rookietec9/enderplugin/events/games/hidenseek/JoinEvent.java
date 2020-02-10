package io.github.rookietec9.enderplugin.events.games.hidenseek;

import io.github.rookietec9.enderplugin.API.configs.associates.User;
import io.github.rookietec9.enderplugin.EnderPlugin;
import io.github.rookietec9.enderplugin.xboards.HideBoard;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import static io.github.rookietec9.enderplugin.API.Utils.Reference.*;
import static io.github.rookietec9.enderplugin.EnderPlugin.Hashmaps;

/**
 * @author Jeremi
 * @version 16.4.9
 * @since 16.4.2
 */
public class JoinEvent implements Listener {

    @EventHandler
    public void run(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (!player.getWorld().getName().equalsIgnoreCase(Worlds.HIDENSEEK)) return;
        if (player.getGameMode() != GameMode.ADVENTURE) return;

        if (event.getClickedBlock() != null)
            if (event.getClickedBlock().getState() instanceof Sign) {
                Sign sign = (Sign) event.getClickedBlock().getState();
                if (sign.getLine(1).equalsIgnoreCase("§c§lSTART")) {
                    if (Bukkit.getWorld(Worlds.HIDENSEEK).getPlayers().size() < 2) {
                        player.sendMessage(Prefixs.HIDENNSEEK + "You need at least two players!");
                        return;
                    }

                    Player tagger = Bukkit.getWorld(Worlds.HIDENSEEK).getPlayers().get((int) (Math.random() * Bukkit.getWorld(Worlds.HIDENSEEK).getPlayers().size()));
                    Bukkit.getScoreboardManager().getMainScoreboard().getTeam(Teams.badTeam).addEntry(tagger.getName());

                    for (Player p : Bukkit.getWorld(Worlds.HIDENSEEK).getPlayers()) {


                        p.sendMessage(Prefixs.HIDENNSEEK + tagger.getName() + " is the tagger!");
                        if (p != tagger) p.sendMessage(Prefixs.HIDENNSEEK + "you have three minutes to hide!");
                        new User(p).clear().setGod(false).clearEffects();

                        if (p == tagger) continue;

                        Bukkit.getScoreboardManager().getMainScoreboard().getTeam(Teams.goodTeam).addEntry(p.getName());
                        if (Math.random() > 0.5)
                            p.teleport(new Location(Bukkit.getWorld(Worlds.HIDENSEEK), (Math.random() * -6) - 274, 5, (Math.random() * -10) - 119));
                        else
                            p.teleport(new Location(Bukkit.getWorld(Worlds.HIDENSEEK), (Math.random() * -6) - 288, 5, (Math.random() * -10) - 119));
                    }
                    tagger.teleport(new Location(Bukkit.getWorld(Worlds.HIDENSEEK), -285, 5, -124));
                    tagger.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 60 * 1 * 20, 9, false, true));
                    tagger.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 60 * 1 * 20, 9, false, true));
                    tagger.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 60 * 1 * 20, 9, false, true));

                    Bukkit.getScheduler().scheduleSyncRepeatingTask(EnderPlugin.getInstance(), () -> {
                        EnderPlugin.Hashmaps.hoodHiding = true;

                        for (Player p : Bukkit.getWorld(Worlds.HIDENSEEK).getPlayers()) {
                            HideBoard hideBoard = new HideBoard(p);
                            hideBoard.updateTicks(Hashmaps.hoodHideTicks);
                            hideBoard.updatePeople(Bukkit.getScoreboardManager().getMainScoreboard().getTeam(Teams.badTeam).getSize());
                            hideBoard.updateSeeker("");

                            if (EnderPlugin.Hashmaps.hoodHideTicks > 60)
                                p.sendTitle("", "§b" + Hashmaps.hoodHideTicks / 60 + "§f:§b" +
                                        Hashmaps.hoodHideTicks % 60 + (("" + Hashmaps.hoodHideTicks % 60).length() < 2 ? "0" : "") + "§f till start.");
                            else if (Hashmaps.hoodHideTicks != 0)
                                p.sendTitle("", "§b" + Hashmaps.hoodHideTicks + "§f seconds till start.");
                            else {
                                if (p != tagger) p.sendTitle("", "§cStay outside of the view of the tagger!");
                                else p.sendTitle("", "§aFind someone & right-click them to get them out.");
                            }
                        }
                        Hashmaps.hoodHideTicks -= 5;
                    }, 0, 5 * 20);

                    Bukkit.getScheduler().scheduleSyncRepeatingTask(EnderPlugin.getInstance(), () -> {
                        Hashmaps.hoodHiding = false;
                        for (Player player1 : Bukkit.getWorld(Worlds.HIDENSEEK).getPlayers()) {
                            HideBoard hideBoard = new HideBoard(player1);
                            hideBoard.updateTicks(Hashmaps.hoodFindTicks);
                            hideBoard.updateMode(false, true);
                        }
                        Hashmaps.hoodFindTicks -= 1;
                    }, 60 * 20, 20);

                }
            }
    }
}