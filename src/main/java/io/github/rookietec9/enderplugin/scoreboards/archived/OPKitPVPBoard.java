package io.github.rookietec9.enderplugin.scoreboards.archived;

import io.github.rookietec9.enderplugin.scoreboards.ArchivedBoard;
import org.bukkit.entity.Player;

import static io.github.rookietec9.enderplugin.Reference.OLD_WIZARDS;


/**
 * @author Jeremi
 * @version 25.2.0
 * @since 17.5.1
 */
public class OPKitPVPBoard extends ArchivedBoard {

    public OPKitPVPBoard(Player player) {
        super(player, OLD_WIZARDS, "§7§l§mOP KIT PVP", "Fight with different OP kits");
    }
}