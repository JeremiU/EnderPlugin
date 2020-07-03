package io.github.rookietec9.enderplugin.events.main;

import io.github.rookietec9.enderplugin.utils.datamanagers.DataPlayer;
import io.github.rookietec9.enderplugin.utils.methods.Minecraft;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import static io.github.rookietec9.enderplugin.EnderPlugin.serverLang;

/**
 * @author Jeremi
 * @version 22.8.0
 */
public class MainTalkEvent implements Listener {
    @EventHandler
    public void run(AsyncPlayerChatEvent e) {
        e.setCancelled(true);

        if (DataPlayer.getUser(e.getPlayer()).isMuted()) {
            e.getPlayer().sendMessage(serverLang().getErrorMsg() + "You are muted!");
            return;
        }
        if (!DataPlayer.get(e.getPlayer()).chatEnabled) {
            e.getPlayer().sendMessage(serverLang().getErrorMsg() + "You have chat disabled!");
            return;
        }
        chat(e.getPlayer(), e.getMessage());
    }

    public static void chat(Player sender, String message) {
        message = message.replace("*n","\n");

        if (message.contains("Snoop Dogg")) {
            String msg = "§f[§2§lO§A§lG§f] Snoop Dogg > §7" + Minecraft.tacc(message.replace("Snoop Dogg ", ""));
            for (Player p : Bukkit.getOnlinePlayers()) if (DataPlayer.get(p).chatEnabled) p.sendMessage(msg);
            System.out.println("[OG] Snoop Dogg > " + ChatColor.stripColor(message));
            return;
        }

        boolean flipColors = !message.startsWith("`");
        String prefix = DataPlayer.getUser(sender).getTabName() + ChatColor.GRAY + " > ";
        String msg = (flipColors ?  Minecraft.tacc(message) : message);

        for (Player player : Bukkit.getOnlinePlayers()) if (DataPlayer.get(player).chatEnabled) player.sendMessage(prefix + msg.replace(player.getName(), ChatColor.DARK_GRAY + "§l" + player.getName() + "§7"));
        for (Player player : Bukkit.getOnlinePlayers()) if (DataPlayer.get(player).chatEnabled) player.sendMessage(prefix + msg.replace(DataPlayer.getUser(player).getNickName(), ChatColor.DARK_GRAY + "§l" + DataPlayer.getUser(player).getNickName() + "§7"));
        System.out.print(ChatColor.stripColor(prefix + message));
    }
}