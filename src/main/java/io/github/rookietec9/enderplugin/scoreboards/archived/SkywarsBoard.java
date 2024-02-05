package io.github.rookietec9.enderplugin.scoreboards.archived;

import io.github.rookietec9.enderplugin.scoreboards.ArchivedBoard;
import org.bukkit.entity.Player;

import static io.github.rookietec9.enderplugin.Reference.OLD_SKYWARS;

/**
 * @author Jeremi
 * @version 25.2.0
 * @since 17.5.1
 */
public class SkywarsBoard extends ArchivedBoard {

    public SkywarsBoard(Player player) {
        super(player, OLD_SKYWARS, "§7§l§mSKYWARS", "Fight players on different islands");
    }
}