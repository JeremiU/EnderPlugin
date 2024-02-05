package io.github.rookietec9.enderplugin.scoreboards;

import io.github.rookietec9.enderplugin.utils.datamanagers.DataPlayer;
import io.github.rookietec9.enderplugin.configs.DataType;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import static io.github.rookietec9.enderplugin.Reference.*;

/**
 * @author Jeremi
 * @version 25.3.2
 * @since 13.4.4
 */
public class WizardsBoard extends Board {

    public WizardsBoard(Player player) {
        super(player, WIZARDS, colorFormat(TITLE_WZRDS, ChatColor.DARK_GREEN), ChatColor.DARK_GREEN);
        putBreaks(11, 9, 7);
        putData("Blade", DataPlayer.get(player).tempWizardBlade, 10);

        putData("Kill Streak", DataPlayer.get(player).tempWizardStreak + "", 8);

        putData("Kills", DataPlayer.get(player).getInt(DataType.WIZARDKILLS) + "", 6);
        putData("Deaths", DataPlayer.get(player).getInt(DataType.WIZARDDEATHS) + "", 5);
        putData("K/D", String.format("%.2f", (DataPlayer.get(player).getInt(DataType.WIZARDDEATHS) > 0 ? DataPlayer.get(player).getInt(DataType.WIZARDKILLS) / (double) DataPlayer.get(player).getInt(DataType.WIZARDDEATHS) : DataPlayer.get(player).getInt(DataType.WIZARDKILLS))) + "", 4);
    }

    public void reloadKillsAndDeaths() {
        updateData("Kills", DataPlayer.get(player).getInt(DataType.WIZARDKILLS) + "");
        updateData("Deaths", DataPlayer.get(player).getInt(DataType.WIZARDDEATHS) + "");
        updateData("Kill Streak", DataPlayer.get(player).tempWizardStreak + "");
        updateData("K/D", String.format("%.2f", (DataPlayer.get(player).getInt(DataType.WIZARDDEATHS) > 0 ? DataPlayer.get(player).getInt(DataType.WIZARDKILLS) / (double) DataPlayer.get(player).getInt(DataType.WIZARDDEATHS) : DataPlayer.get(player).getInt(DataType.WIZARDKILLS))) + "");
    }

    public void updateBlade(String blade) {
        DataPlayer.get(player).tempWizardBlade = blade.toUpperCase();
        updateData("Blade", blade.toUpperCase());
    }
}