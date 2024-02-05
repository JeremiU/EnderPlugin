package io.github.rookietec9.enderplugin.events.hub;

import io.github.rookietec9.enderplugin.scoreboards.Board;
import io.github.rookietec9.enderplugin.utils.datamanagers.DataPlayer;
import io.github.rookietec9.enderplugin.utils.methods.Minecraft;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

import java.util.List;

import static io.github.rookietec9.enderplugin.Reference.*;
import static io.github.rookietec9.enderplugin.utils.methods.Minecraft.VerType;
import static io.github.rookietec9.enderplugin.configs.DataType.*;

/**
 * @author Jeremi
 * @version 25.2.0
 * @since 18.3.6
 */
public class BookOpenEvent implements Listener {

    public static ItemStack book(Player player) {
        ItemStack itemStack = new ItemStack(Material.WRITTEN_BOOK);
        BookMeta bookMeta = (BookMeta) itemStack.getItemMeta();
        bookMeta.setDisplayName("§dStats");

        bookMeta.setAuthor(player.getDisplayName());

        DataPlayer dP = DataPlayer.get(player);

        String hub = "§d§l" + TITLE_HUB + "\n RANK: " + DataPlayer.getUser(player).rank() + "\n §d§lVERSION: " + Minecraft.versionInfo(VerType.NUM_NO_DOTS);
        String booty = "§3§l" + (DataPlayer.getUser(player).isOG() ? TITLE_ALT_BOOTY : TITLE_BOOTY) + "\n KILLS: " + dP.getInt(BOOTYKILLS) + "\n DEATHS: " + dP.getInt(BOOTYDEATHS);
        String ctf = "§6§l" + TITLE_CTF + "\n KILLS: " + dP.getInt(CTFKILLS) + "\n DEATHS: " + dP.getInt(CTFDEATHS) + "\n CAPTURES: " + dP.getInt(CTFCAPTURES) + "\n POINTS: " + dP.getInt(CTFPOINTS) + "\n WINS: " + dP.getInt(CTFWINS) + "\n LOSSES: " + dP.getInt(CTFLOSSES);
        String esg = "§6§l" + TITLE_ESG + "\n KILLS: " + "WIP" + "\n DEATHS: " + "WIP" + "\n CHESTS LOOTED: " + "WIP";
        String hide = "§6§l" + TITLE_HNS + "\n WINS: " + dP.getInt(HOODWINS) + "\n LOSSES: " + dP.getInt(HOODLOSSES);
        String hunger = "§6§l" + TITLE_HUNGER + "\n WINS: " + "WIP" + "\n LOSSES: " + "WIP" + "\n SPOTS TILLED: " + "WIP";
        String kitpvp = "§b§l" + TITLE_KIT_PVP + "\n KILLS: " + "WIP" + "\n DEATHS: " + "WIP";
        String murder = "§4§l" + TITLE_MURDERER + "\n WINS: " + dP.getInt(MURDERWINS) + "\n LOSSES: " + dP.getInt(MURDERWINS);

        String obstacle = "§3§l" + TITLE_OBSTCLE + "\n WINS: " + "WIP" + "\n ATTEMPTS: " + "WIP";
        String parkour = "§7§l" + TITLE_PARKOUR + "\n WINS: " + "WIP" + "\n ATTEMPTS: " + "WIP";
        String rabbit = "§a§l" + TITLE_RABBIT + "\n WINS: " + "WIP" + "\n ATTEMPTS: " + "WIP";

        String spleef = "§1§l" + TITLE_SPLEEF + "\n BLOCKS: " + Board.roundInt(dP.getInt(SPLEEFBLOCKS)) + "\n WINS: " + dP.getInt(SPLEEFWINS) + "\n LOSSES: " + dP.getInt(SPLEEFLOSSES);
        String sumo = "§a§l" + TITLE_SUMO + "\n KILLS: " + "WIP" + "\n WINS: " + "WIP" + "\n LOSSES: " + "WIP";
        String wizards = "§2§l" + TITLE_WZRDS + "\n KILLS: " + dP.getInt(WIZARDKILLS) + "\n DEATHS: " + dP.getInt(WIZARDDEATHS);

        bookMeta.setPages(List.of(hub, booty, ctf, hide + "\n\n" + murder, parkour + "\n\n" + obstacle + "\n\n" + rabbit,spleef + "\n\n" + sumo, wizards));
        itemStack.setItemMeta(bookMeta);

        return itemStack;
    }
}