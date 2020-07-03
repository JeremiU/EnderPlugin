package io.github.rookietec9.enderplugin.events.hub;

import io.github.rookietec9.enderplugin.scoreboards.Board;
import io.github.rookietec9.enderplugin.utils.datamanagers.DataPlayer;
import io.github.rookietec9.enderplugin.utils.methods.Minecraft;
import io.github.rookietec9.enderplugin.utils.reference.BoardNames;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

import java.util.ArrayList;
import java.util.List;

import static io.github.rookietec9.enderplugin.utils.methods.Minecraft.VerType;
import static io.github.rookietec9.enderplugin.utils.reference.DataType.*;

/**
 * @author Jeremi
 * @version 21.3.4
 * @since 18.3.6
 */
public class BookOpenEvent implements Listener {

    public static ItemStack book(Player player) {
        ItemStack itemStack = new ItemStack(Material.WRITTEN_BOOK);
        BookMeta bookMeta = (BookMeta) itemStack.getItemMeta();
        bookMeta.setDisplayName("§dStats");

        String id = player.getUniqueId().toString();

        bookMeta.setAuthor(player.getDisplayName());

        DataPlayer dP = DataPlayer.get(player);

        String hub = "§d§l" + ChatColor.stripColor(BoardNames.HUB) + "\n RANK: " + DataPlayer.getUser(player).rank() + "\n §d§lVERSION: " + Minecraft.versionInfo(VerType.NUM_NO_DOTS);
        String booty = "§3§l" + ChatColor.stripColor(BoardNames.BOOTY) + "\n KILLS: " + dP.getInt(BOOTYKILLS) + "\n DEATHS: " + dP.getInt(BOOTYDEATHS);
        String ctf = "§6§l" + ChatColor.stripColor(BoardNames.CTF) + "\n KILLS: " + dP.getInt(CTFKILLS) + "\n DEATHS: " + dP.getInt(CTFDEATHS) + "\n CAPTURES: " + dP.getInt(CTFCAPTURES) + "\n POINTS: " + dP.getInt(CTFPOINTS) + "\n WINS: " + dP.getInt(CTFWINS) + "\n LOSSES: " + dP.getInt(CTFLOSSES);
        String esg = "§6§l" + ChatColor.stripColor(BoardNames.ESG) + "\n KILLS: " + "WIP" + "\n DEATHS: " + "WIP" + "\n CHESTS LOOTED: " + "WIP";
        String hide = "§6§l" + ChatColor.stripColor(BoardNames.HIDE) + "\n WINS: " + dP.getInt(HOODWINS) + "\n LOSSES: " + dP.getInt(HOODLOSSES);
        String hunger = "§6§l" + ChatColor.stripColor(BoardNames.HUNGER) + "\n WINS: " + "WIP" + "\n LOSSES: " + "WIP" + "\n SPOTS TILLED: " + "WIP";
        String kitpvp = "§b7§l" + ChatColor.stripColor(BoardNames.KIT_PVP) + "\n KILLS: " + "WIP" + "\n DEATHS: " + "WIP";
        String murder = "§4§l" + ChatColor.stripColor(BoardNames.MURDER) + "\n WINS: " + dP.getInt(MURDERWINS) + "\n LOSSES: " + dP.getInt(MURDERWINS);

        String obstacle = "§3§l" + ChatColor.stripColor(BoardNames.OBSTACLE_COURSE) + "\n WINS: " + "WIP" + "\n ATTEMPTS: " + "WIP";
        String parkour = "§7§l" + ChatColor.stripColor(BoardNames.PARKOUR) + "\n WINS: " + "WIP" + "\n ATTEMPTS: " + "WIP";
        String rabbit = "§a§l" + ChatColor.stripColor(BoardNames.RABBIT) + "\n WINS: " + "WIP" + "\n ATTEMPTS: " + "WIP";

        String spleef = "§1§l" + ChatColor.stripColor(BoardNames.SPLEEF) + "\n BLOCKS: " + Board.roundInt(dP.getInt(SPLEEFBLOCKS)) + "\n WINS: " + dP.getInt(SPLEEFWINS) + "\n LOSSES: " + dP.getInt(SPLEEFLOSSES);
        String sumo = "§a§l" + ChatColor.stripColor(BoardNames.SUMO) + "\n KILLS: " + "WIP" + "\n WINS: " + "WIP" + "\n LOSSES: " + "WIP";
        String wizards = "§2§l" + ChatColor.stripColor(BoardNames.WIZARDS) + "\n KILLS: " + dP.getInt(WIZARDKILLS) + "\n DEATHS: " + dP.getInt(WIZARDDEATHS);

        List<String> pgs = new ArrayList<>();
        pgs.add(hub);
        pgs.add(booty);
        pgs.add(ctf);
        pgs.add(hide + "\n\n" + murder);
        pgs.add(parkour + "\n\n" + obstacle + "\n\n" + rabbit);
        pgs.add(spleef + "\n\n" + sumo);
        pgs.add(wizards);

        bookMeta.setPages(pgs);
        itemStack.setItemMeta(bookMeta);

        return itemStack;
    }
}