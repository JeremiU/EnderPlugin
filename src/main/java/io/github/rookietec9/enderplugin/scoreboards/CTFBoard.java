package io.github.rookietec9.enderplugin.scoreboards;

import io.github.rookietec9.enderplugin.utils.reference.BoardNames;
import io.github.rookietec9.enderplugin.utils.reference.Worlds;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import static io.github.rookietec9.enderplugin.utils.datamanagers.DataPlayer.*;

/**
 * @author Jeremi
 * @version 21.3.3
 * @since 13.4.4
 */
public class CTFBoard extends Board {

    public CTFBoard(Player player) {
        super(player, Worlds.CTF, BoardNames.CTF, ChatColor.YELLOW);

        putBreaks(13, 10, 6);
        putHeader("Score§f: " + ChatColor.RED + "§l" + ctfRedScore + ChatColor.WHITE + "§l:" + ChatColor.BLUE + "§l" + ctfBlueScore, true, 12);
        putData("Time", formatTime(ctfSec), 11);

        putHeader("Flags", true, 9);

        putData("Blue flag", (ctfBlueSafe ? "Safe" : "Stolen"), 8);
        putData("Red flag", (ctfRedSafe ? "Safe" : "Stolen"), 7);

        putData("Kills", get(player).tempCTFKills + "", 5);
        putData("Deaths", get(player).tempCTFDeaths + "", 4);
    }

    public void updateTempKills(int kills) {
        get(player).tempCTFKills = kills;
        updateData("Kills", kills + "");
    }

    public void updateTempDeaths(int deaths) {
        get(player).tempCTFDeaths = deaths;
        updateData("Deaths", deaths + "");
    }

    public void updateTicks() {
        updateData("Time", formatTime(ctfSec));
    }

    public void updateRedFlag(boolean safety) {
        ctfRedSafe = safety;
        updateData("Red flag", (safety ? "Safe" : "Stolen"));
    }

    public void updateBlueFlag(boolean safety) {
        ctfBlueSafe = safety;
        updateData("Blue flag", (safety ? "Safe" : "Stolen"));
    }

    public void updateScore(int red, int blue) {
        ctfRedScore = red;
        ctfBlueScore = blue;
        updateHeader("Score§f: " + ChatColor.RED + "§l" + ctfRedScore + ChatColor.WHITE + "§l:" + ChatColor.BLUE + "§l" + ctfBlueScore, true, "SCORE");
    }
}