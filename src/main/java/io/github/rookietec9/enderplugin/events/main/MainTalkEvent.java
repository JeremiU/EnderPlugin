package io.github.rookietec9.enderplugin.events.main;

import io.github.rookietec9.enderplugin.API.configs.Langs;
import io.github.rookietec9.enderplugin.API.configs.associates.Lang;
import io.github.rookietec9.enderplugin.API.configs.associates.User;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

/**
 * @author Jeremi
 * @version 11.6.0
 * @since 0.0.4
 */
public class MainTalkEvent implements Listener {
    @EventHandler
    public void run(AsyncPlayerChatEvent e) {
        User user = new User(e.getPlayer());
        if (user.isMuted()) {
            user.getBase().sendMessage(new Lang(Langs.fromSender(e.getPlayer())).getErrorMsg() + "You are muted!");
            e.setCancelled(true);
            return;
        }
        e.setCancelled(true);
        if (e.getMessage().contains("Snoop Dogg")) {
            Bukkit.broadcastMessage("§f[§2§lO§A§lG§f] Snoop Dogg | §7"
                    + ChatColor.translateAlternateColorCodes('&', e.getMessage().replace("Snoop Dogg ", "")));
        } else {
            for (Player player : Bukkit.getOnlinePlayers())
                player.sendMessage(user.getTabName() + " | " + ChatColor.GRAY + ChatColor.translateAlternateColorCodes('&', e.getMessage().replace("&r","&7")
                        .replace("&R","&7")).replace(player.getName(), ChatColor.AQUA + player.getName() + "§7"));
        }
    }
}