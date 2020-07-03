package io.github.rookietec9.enderplugin.events.games.hidenseek;

import io.github.rookietec9.enderplugin.EnderPlugin;
import io.github.rookietec9.enderplugin.scoreboards.Board;
import io.github.rookietec9.enderplugin.scoreboards.HideBoard;
import io.github.rookietec9.enderplugin.utils.datamanagers.DataPlayer;
import io.github.rookietec9.enderplugin.utils.reference.Prefixes;
import io.github.rookietec9.enderplugin.utils.reference.Teams;
import io.github.rookietec9.enderplugin.utils.reference.Worlds;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

/**
 * @author Jeremi
 * @version 22.8.0
 * @since 16.4.2
 */
public class JoinEvent implements Listener {

    @EventHandler
    public void run(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (!player.getWorld().getName().equalsIgnoreCase(Worlds.HIDENSEEK) || player.getGameMode() != GameMode.ADVENTURE) return;

        if (event.getClickedBlock() != null)
            if (event.getClickedBlock().getState() instanceof Sign) {
                Sign sign = (Sign) event.getClickedBlock().getState();
                if (sign.getLine(1).equalsIgnoreCase("§c§lSTART")) {
                    if (Bukkit.getWorld(Worlds.HIDENSEEK).getPlayers().size() < 2) {
                        player.sendMessage(Prefixes.HIDENNSEEK + "You need at least two players!");
                        return;
                    }

                    if (EnderPlugin.scheduler().isRunning("HOOD_FINISH")) {
                        event.getPlayer().sendMessage(Prefixes.HIDENNSEEK + "Wait until the current game is finished!");
                        return;
                    }

                    Player tagger = Bukkit.getWorld(Worlds.HIDENSEEK).getPlayers().get((int) (Math.random() * Bukkit.getWorld(Worlds.HIDENSEEK).getPlayers().size()));
                    Teams.add(Teams.badTeam, tagger);

                    for (Player p : Bukkit.getWorld(Worlds.HIDENSEEK).getPlayers()) {
                        DataPlayer.getUser(p).reset();
                        DataPlayer.get(p).getBoard(HideBoard.class).updateMode(true, true);
                        DataPlayer.get(p).getBoard(HideBoard.class).names(true);

                        p.sendMessage(Prefixes.HIDENNSEEK + DataPlayer.getUser(tagger).getNickName() + " is the tagger!");
                        if (p != tagger) p.sendMessage(Prefixes.HIDENNSEEK + "you have three minutes to hide!");

                        if (p == tagger) continue;

                        Teams.add(Teams.goodTeam, p);

                        if (Math.random() > 0.5) p.teleport(new Location(Bukkit.getWorld(Worlds.HIDENSEEK), (Math.random() * -6) - 274, 5, (Math.random() * -10) - 119), PlayerTeleportEvent.TeleportCause.PLUGIN);
                        else p.teleport(new Location(Bukkit.getWorld(Worlds.HIDENSEEK), (Math.random() * -6) - 288, 5, (Math.random() * -10) - 119), PlayerTeleportEvent.TeleportCause.PLUGIN);
                    }

                    tagger.teleport(new Location(Bukkit.getWorld(Worlds.HIDENSEEK), -285, 5, -124), PlayerTeleportEvent.TeleportCause.PLUGIN);
                    tagger.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 60 * 100 * 20, 9, false, true));
                    tagger.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 60 * 100 * 20, 9, false, true));
                    tagger.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 60 * 100 * 20, 9, false, true));

                    DataPlayer.hoodHidingSec = 60;
                    DataPlayer.hoodFindingSec = 180;

                    DataPlayer.hoodEscapeList.addAll(Bukkit.getWorld(Worlds.HIDENSEEK).getPlayers());
                    DataPlayer.hoodEscapeList.remove(tagger);

                    EnderPlugin.scheduler().runRepeatingTask(() -> {
                        for (Player p : Bukkit.getWorld(Worlds.HIDENSEEK).getPlayers()) {
                            DataPlayer.get(p).getBoard(HideBoard.class).updateTicks(DataPlayer.hoodHidingSec);
                            DataPlayer.get(p).getBoard(HideBoard.class).updatePeople(Teams.getTeam(Teams.badTeam).getSize());
                            DataPlayer.get(p).getBoard(HideBoard.class).updateSeeker(DataPlayer.getUser(tagger).getNickName());

                            if (DataPlayer.hoodHidingSec != 0) p.sendTitle("", Board.formatTime(DataPlayer.hoodHidingSec, ChatColor.GOLD, ChatColor.WHITE) + " till start.");
                            else {
                                if (p != tagger) p.sendTitle("", "§cStay outside of the view of the tagger!");
                                else {
                                    p.sendTitle("", "§aFind someone & left-click them to get them out.");
                                    DataPlayer.getUser(p).clearEffects();
                                }
                            }
                        }
                        DataPlayer.hoodHidingSec--;
                    }, "HOOD_TICKER_HIDING", 0, 1, 60);

                    EnderPlugin.scheduler().runRepeatingTask(() -> {
                        for (Player player1 : Bukkit.getWorld(Worlds.HIDENSEEK).getPlayers()) {
                            DataPlayer.get(player1).getBoard(HideBoard.class).updateTicks(DataPlayer.hoodFindingSec);
                            DataPlayer.get(player1).getBoard(HideBoard.class).updateMode(false, true);
                        }
                        DataPlayer.hoodFindingSec--;
                    }, "HOOD_TICKER_FINDING", 60, 1, 180);

                    EnderPlugin.scheduler().runSingleTask(() -> EnderPlugin.hoodBase.reset(), "HOOD_FINISH", 240);
                }
            }
    }
}