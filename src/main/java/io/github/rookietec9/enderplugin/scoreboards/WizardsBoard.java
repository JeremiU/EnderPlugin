package io.github.rookietec9.enderplugin.scoreboards;

import io.github.rookietec9.enderplugin.utils.datamanagers.DataPlayer;
import io.github.rookietec9.enderplugin.utils.reference.BoardNames;
import io.github.rookietec9.enderplugin.utils.reference.DataType;
import io.github.rookietec9.enderplugin.utils.reference.Worlds;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**
 * @author Jeremi
 * @version 21.3.4
 * @since 13.4.4
 */
public class WizardsBoard extends Board {

    public WizardsBoard(Player player) {
        super(player, Worlds.WIZARDS, BoardNames.WIZARDS, ChatColor.DARK_GREEN);
        putBreaks(13, 11, 7);
        putData("Blade", DataPlayer.get(player).tempWizardBlade, 12);
        putHeader("THIS ROUND:", false, 10);
        putData("Kills§b§b", DataPlayer.get(player).tempWizardKills + "", 9);
        putData("Deaths§b§b", DataPlayer.get(player).tempWizardDeaths + "", 8);
        putHeader("OVERALL:", false, 6);
        putData("Kills§a§b", DataPlayer.get(player).getInt(DataType.WIZARDKILLS) + "", 5);
        putData("Deaths§a§b", DataPlayer.get(player).getInt(DataType.WIZARDDEATHS) + "", 4);
    }

    public void updateTempKills(int kills) {
        DataPlayer.get(player).tempWizardKills = kills;
        updateData("Kills§b§b", kills + "");
    }

    public void updateTempDeaths(int deaths) {
        DataPlayer.get(player).tempWizardDeaths = deaths;
        updateData("Deaths§b§b", deaths + "");
    }

    public void reloadKillsAndDeaths() {
        updateData("Kills§a§b", DataPlayer.get(player).getInt(DataType.WIZARDKILLS) + "");
        updateData("Deaths§a§b", DataPlayer.get(player).getInt(DataType.WIZARDDEATHS) + "");
    }

    public void updateBlade(String blade) {
        DataPlayer.get(player).tempWizardBlade = blade.toUpperCase();
        updateData("Blade", blade.toUpperCase());
    }
}