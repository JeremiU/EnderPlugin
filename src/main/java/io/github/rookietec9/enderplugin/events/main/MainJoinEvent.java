package io.github.rookietec9.enderplugin.events.main;

import io.github.rookietec9.enderplugin.EnderPlugin;
import io.github.rookietec9.enderplugin.configs.associates.Hub;
import io.github.rookietec9.enderplugin.utils.datamanagers.DataPlayer;
import io.github.rookietec9.enderplugin.utils.methods.Minecraft;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import static io.github.rookietec9.enderplugin.EnderPlugin.serverLang;
import static org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

/**
 * @author Jeremi
 * @version 22.8.0
 * @since
 */
public class MainJoinEvent implements Listener {

    @EventHandler
    public void run(PlayerJoinEvent e) {
        Player player = e.getPlayer();

        if (DataPlayer.get(e.getPlayer()) == null) DataPlayer.registerPlayers();

        player.teleport(new Hub().getLoc(), TeleportCause.PLUGIN);
        player.setFireTicks(-20);
        player.setPlayerListName(DataPlayer.getUser(player).getTabName());
        player.setCustomName(DataPlayer.getUser(player).getTabName());
        player.setCustomNameVisible(true);
        player.setLevel(0);
        player.setExp(0);
        player.setTotalExperience(0);
        player.sendTitle(Minecraft.tacc(EnderPlugin.getInstance().getConfig().getString("Welcome Title").replace("{DARK}",
                serverLang().getDarkColor().getChar() + "").replace("{LIGHT}", serverLang().getLightColor().getChar() + "")), Minecraft.tacc(
                EnderPlugin.getInstance().getConfig().getString("Welcome Subtitle").replace("{DARK}", serverLang().getDarkColor().getChar() + "")
                        .replace("{LIGHT}", serverLang().getLightColor().getChar() + "")));

        //        if (!player.getName().equalsIgnoreCase("Alanu"))
        e.setJoinMessage(serverLang().getDarkColor() + DataPlayer.getUser(player).getNickName() + serverLang().getLightColor() + " joined the server.");
        //        else e.setJoinMessage(l.getDarkColor() + "Failure" + l.getLightColor() + " joined the server.");

        if (DataPlayer.getUser(player).getTabName().isEmpty()) DataPlayer.getUser(player).setTabName(player.getName());
        if (Bukkit.getOnlineMode()) DataPlayer.getUser(player).setOnline(true);
    }
}