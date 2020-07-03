package io.github.rookietec9.enderplugin.scoreboards;

import io.github.rookietec9.enderplugin.utils.datamanagers.DataPlayer;
import io.github.rookietec9.enderplugin.utils.methods.Minecraft;
import io.github.rookietec9.enderplugin.utils.reference.BoardNames;
import io.github.rookietec9.enderplugin.utils.reference.Worlds;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.lang.management.ManagementFactory;

import static io.github.rookietec9.enderplugin.utils.methods.Minecraft.VerType;

/**
 * @author Jeremi
 * @version 21.5.6
 * @since 12.8.3
 */
public class HubBoard extends Board {

    public HubBoard(Player player) {
        super(player, Worlds.HUB, BoardNames.HUB, ChatColor.LIGHT_PURPLE);
        putBreaks(10, 8, 5);
        putData("People Online", Bukkit.getOnlinePlayers().size() + "", 9);
        putData("Rank", DataPlayer.getUser(player).rank(), 7);
        putData("Version No", Minecraft.versionInfo(VerType.NUM), 6);
        putData("Uptime ~", formatTime((int) ManagementFactory.getRuntimeMXBean().getUptime()/1000) + "", 4);
    }

    public void changeTicks() {
        updateData("Uptime ~", formatTime((int) ManagementFactory.getRuntimeMXBean().getUptime()/1000) + "");
    }

    public void updatePlayers(int online) {
        updateData("People Online", online + "");
    }

    public void updateRank() {
        updateData("Rank", DataPlayer.getUser(player).rank());
    }
}