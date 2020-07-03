package io.github.rookietec9.enderplugin.scoreboards.archived;

import io.github.rookietec9.enderplugin.scoreboards.ArchivedBoard;
import io.github.rookietec9.enderplugin.utils.reference.BoardNames;
import io.github.rookietec9.enderplugin.utils.reference.Worlds;
import org.bukkit.entity.Player;

/**
 * @author Jeremi
 * @version 18.5.8
 * @since 17.5.1
 */
public class SkywarsBoard extends ArchivedBoard {

    public SkywarsBoard(Player player) {
        super(player, Worlds.OLD_SKYWARS, BoardNames.SKY_WARS, "Fight players on different islands");
    }
}