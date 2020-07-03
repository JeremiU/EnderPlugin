package io.github.rookietec9.enderplugin.scoreboards.archived;

import io.github.rookietec9.enderplugin.scoreboards.ArchivedBoard;
import io.github.rookietec9.enderplugin.utils.reference.BoardNames;
import io.github.rookietec9.enderplugin.utils.reference.Worlds;
import org.bukkit.entity.Player;


/**
 * @author Jeremi
 * @version 18.5.0
 * @since 17.5.1
 */
public class OPKitPVPBoard extends ArchivedBoard {

    public OPKitPVPBoard(Player player) {
        super(player, Worlds.OLD_WIZARDS, BoardNames.OP_KIT_PVP, "Fight with different OP kits");
    }
}