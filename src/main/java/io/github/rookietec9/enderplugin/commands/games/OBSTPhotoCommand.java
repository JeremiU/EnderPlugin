package io.github.rookietec9.enderplugin.commands.games;

import io.github.rookietec9.enderplugin.Reference;
import io.github.rookietec9.enderplugin.utils.datamanagers.endcommands.EndExecutor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

import static io.github.rookietec9.enderplugin.EnderPlugin.serverLang;

/**
 * @author Jeremi
 * @version 25.6.0
 * @since 25.5.5
 */
public class OBSTPhotoCommand implements EndExecutor {

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) return msg(sender, serverLang().getOnlyUserMsg());
        BufferedImage image;
        try {
            image = ImageIO.read(new File("E:\\MCD\\plans.png"));
        } catch (IOException e) {
            e.printStackTrace();
            return msg(sender, serverLang().getErrorMsg() + "File exception");
        }
        Player player = (Player) sender;

        Location loc = new Location(player.getWorld(), 1164, 1, -18);

        for (int y = 1; y < 9; y++) {
            for (int z = image.getHeight() - 1; z > -1; z--) {
                for (int x = image.getWidth() - 1; x > -1; x--) {
                    int x_ = loc.getBlockX() - x;
                    int z_ = loc.getBlockZ() - z;

                    System.out.println(x_ + " " + y + " " + z_ + " " + ((image.getRGB(x, z) & 0x0000ff00) >> 8));
                    player.getWorld().getBlockAt(x_, y, z_).setType((switch ((image.getRGB(x, z) & 0x0000ff00) >> 8) {
                        case 195 -> Material.QUARTZ_BLOCK; //wall
                        case 148, 255, 93, 216 -> Material.STAINED_CLAY; //93 = pink, 255 = green
                        default -> Material.AIR;
                    }));
                    player.getWorld().getBlockAt(x_, y, z_).setData((byte) (switch ((image.getRGB(x, z) & 0x0000ff00) >> 8) {
                        case 148 -> 11; //spawns
                        case 216 -> 4; //ent
                        case 255 -> 5;
                        case 93 -> 6;
                        default -> 0;
                    }));
                }
            }
        }
        return msg(sender, Reference.PREFIX_OBS + "Set up the maze.");
    }

    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        return null;
    }

    public String getSyntax(String label) {
        return null;
    }

    public List<String> commandNames() {
        return List.of("obstphoto");
    }
}