package io.github.rookietec9.enderplugin.scoreboards;

import io.github.rookietec9.enderplugin.utils.reference.BoardNames;
import io.github.rookietec9.enderplugin.utils.reference.Worlds;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**
 * @author Jeremi
 * @since 19.3.7
 * @version 19.3.7
 */
public class KitPVPBoard extends Board {

    public KitPVPBoard(Player player) {
        super(player, Worlds.KIT_PVP, BoardNames.KIT_PVP, ChatColor.AQUA);
        putBreaks(13, 11, 7);
        putData("Kit", "NONE", 12);
        putHeader("THIS ROUND:", false, 10);
        putData("Kills§b§b", 0 + "", 9);
        putData("Deaths§b§b", 0 + "", 8);
        putHeader("OVERALL:", false, 6);
        putData("Kills§a§b", 0 + "", 5);
        putData("Deaths§a§b", 0 + "", 4);
    }

    public void updateTempKills(int kills) {
        updateData("Kills§b§b", kills + "");
    }

    public void updateTempDeaths(int deaths) {
        updateData("Deaths§b§b", deaths + "");
    }

    public void reloadKillsAndDeaths() {
        updateData("Kills§a§b", 0 + "");
        updateData("Deaths§a§b", 0 + "");
    }

    public void updateKit(String kit) {
        updateData("Kit", kit.toUpperCase());
    }

}