package io.github.rookietec9.enderplugin.events.main;

import io.github.rookietec9.enderplugin.EnderPlugin;
import io.github.rookietec9.enderplugin.configs.associates.Hub;
import io.github.rookietec9.enderplugin.scoreboards.HubBoard;
import io.github.rookietec9.enderplugin.utils.datamanagers.DataPlayer;
import io.github.rookietec9.enderplugin.utils.datamanagers.PartySystem;
import io.github.rookietec9.enderplugin.utils.methods.Minecraft;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import static io.github.rookietec9.enderplugin.EnderPlugin.serverLang;
import static org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

/**
 * @author Jeremi
 * @version 25.4.3
 * @since
 */
public class MainJoinLeaveEvent implements Listener {

    @EventHandler
    public void run(PlayerJoinEvent e) {
        PartySystem.notInParties.add(e.getPlayer());
        Player player = e.getPlayer();

        if (DataPlayer.get(e.getPlayer()) == null) DataPlayer.registerPlayers();

        DataPlayer.get(e.getPlayer()).setName(DataPlayer.getUser(e.getPlayer()).getNickName());

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

        e.setJoinMessage(serverLang().getDarkColor() + DataPlayer.getUser(player).getNickName() + serverLang().getLightColor() + " joined the server.");

        if (DataPlayer.getUser(player).getTabName().isEmpty()) DataPlayer.getUser(player).setTabName(player.getName());
        if (Bukkit.getOnlineMode()) DataPlayer.getUser(player).setOnline(true);
    }

    @EventHandler
    public void run(PlayerQuitEvent e) {
        DataPlayer.get(e.getPlayer()).getBoard(HubBoard.class).init();

        if (PartySystem.getFromPlayer(e.getPlayer()) != null) PartySystem.getFromPlayer(e.getPlayer()).removePlayers(e.getPlayer());
        PartySystem.notInParties.remove(e.getPlayer());
        if (PartySystem.getFromPlayer(e.getPlayer()) != null) {
            PartySystem.getFromPlayer(e.getPlayer()).notifyPlayers(DataPlayer.getUser(e.getPlayer()).getNickName() + " left the party.", true);
            PartySystem.getFromPlayer(e.getPlayer()).removePlayers(e.getPlayer());
        }
        e.setQuitMessage(serverLang().getDarkColor() + DataPlayer.getUser(e.getPlayer()).getNickName() + serverLang().getLightColor() + " left the server.");
    }
}