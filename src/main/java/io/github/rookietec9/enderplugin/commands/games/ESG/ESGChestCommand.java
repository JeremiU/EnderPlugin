package io.github.rookietec9.enderplugin.commands.games.ESG;

import io.github.rookietec9.enderplugin.API.EndExecutor;
import io.github.rookietec9.enderplugin.API.Utils;
import io.github.rookietec9.enderplugin.API.configs.Langs;
import io.github.rookietec9.enderplugin.API.configs.associates.Lang;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * Fills up chests in the ESG map. Cut by 118
 *
 * @author Jeremi
 * @version 13.4.4
 * @since 5.4.8
 */
public class ESGChestCommand implements EndExecutor {

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Lang l = new Lang(Langs.fromSender(sender));
        if (!(sender instanceof Player)) {
            sender.sendMessage(l.getOnlyUserMsg());
            return true;
        }
        Player p = (Player) sender;
        if (!p.getWorld().getName().equalsIgnoreCase(Utils.Reference.Worlds.ESG_FIGHT)) {
            sender.sendMessage(l.getErrorMsg() + "Wrong world!");
            return true;
        }
        if (args.length != 1) {
            sender.sendMessage(getSyntax(command, l));
            return true;
        }
        if (!args[0].equalsIgnoreCase("test") && !args[0].equalsIgnoreCase("load")) {
            sender.sendMessage(getSyntax(command, l));
            return true;
        }

        Location chest01 = new Location(p.getWorld(), -16, (66), -178, 3, 0); //Corner of map, on a hill.				    3
        Location chest02 = new Location(p.getWorld(), -17, (65), -127, 1, 0); //Seen with Wither Heads, a big chest.	    1
        Location chest03 = new Location(p.getWorld(), -23, (62), -135, 1, 0); //Right next to water and sugar cane.		    1
        Location chest04 = new Location(p.getWorld(), -41, (64), -147, 1, 0); //In a farmhouse, big chest, opposite ladder.   1
        Location chest05 = new Location(p.getWorld(), -40, (62), -142, 1, 0); //Under water, next to mossy cobble.		    1
        Location chest06 = new Location(p.getWorld(), -98, (63), -87, 1, 0); //In the ship.			    					1
        Location chest07 = new Location(p.getWorld(), -86, (63), -80, 3, 0); //Near the ship, on the side of the map.		    3
        Location chest08 = new Location(p.getWorld(), -53, (65), -87, 3, 0); //On the side of the cliff, behind it.		    3
        Location chest09 = new Location(p.getWorld(), -17, (85), -94, 3, 0); //On top of hill, in sand, near house.	    	3
        Location chest10 = new Location(p.getWorld(), -16, (82), -119, 4, 0); //On top of hill, in sand, can view houses.	    4
        Location chest11 = new Location(p.getWorld(), -15, (82), -79, 3, 0); //On top of hill, on corner of map.				3
        Location chest12 = new Location(p.getWorld(), -97, (65), -138, 4, 0); //Near water and shrub, partially in sand.		4
        Location chest13 = new Location(p.getWorld(), -109, (69), -157, 3, 0); //On hill, in front of water.					3
        Location chest14 = new Location(p.getWorld(), -104, (64), -173, 3, 0); //Submerged in water fountain.					3
        Location chest15 = new Location(p.getWorld(), -70, (63), -110, 2, 0); //In house, near yellow bed.					2
        Location chest16 = new Location(p.getWorld(), -48, (63), -102, 2, 0); //In cave, near fences.							2
        Location chest17 = new Location(p.getWorld(), -57, (57), -107, 2, 0); //In cave, near iron, near fences.				2
        Location chest18 = new Location(p.getWorld(), -78, (62), -135, 5, 0); //Underwater chamber							5
        Location chest19 = new Location(p.getWorld(), -62, (68), -128, 1, 0); // SPAWN CHESTS BEGIN
        Location chest20 = new Location(p.getWorld(), -64, (68), -126, 1, 0);
        Location chest21 = new Location(p.getWorld(), -66, (68), -128, 1, 0);
        Location chest22 = new Location(p.getWorld(), -64, (68), -130, 1, 0);
        Location chest23 = new Location(p.getWorld(), -65, (69), -129, 1, 0);
        Location chest24 = new Location(p.getWorld(), -63, (69), -129, 1, 0);
        Location chest25 = new Location(p.getWorld(), -63, (69), -127, 1, 0);
        Location chest26 = new Location(p.getWorld(), -65, (69), -127, 1, 0); //SPAWN CHESTS END
        Location chest27 = new Location(p.getWorld(), -54, (52), -174, 1, 0); //Under lake, in chamber.
        Location chest28 = new Location(p.getWorld(), -80, (62), -105, 1, 0); //Under lake, in chamber.
        Location chest29 = new Location(p.getWorld(), -23, (58), -167, 1, 0); //Under lake, in chamber.
        Location chest30 = new Location(p.getWorld(), -84, (56), -160, 1, 0); //Under lake, in chamber.
        Location chest31 = new Location(p.getWorld(), -35, 64, -147, 1, 0);
        Location chest32 = new Location(p.getWorld(), -77, 65, -131, 1, 0);
        Location chest33 = new Location(p.getWorld(), -32, 65, -115, 1, 0);

        Location[] locations = new Location[]{chest01, chest02, chest03, chest04, chest05, chest06, chest07, chest08, chest09, chest10, chest11, chest12, chest13, chest14, chest15, chest16, chest17,
                chest18, chest19, chest20, chest21, chest22, chest23, chest24, chest25, chest26, chest27, chest28, chest29, chest30, chest31, chest32, chest33
        };

        if (args[0].equalsIgnoreCase("Test")) {
            for (Location location : locations) {
                location.getBlock().setType(Material.HARD_CLAY);
                location.getBlock().setData((byte) 4);
            }
            sender.sendMessage("§7[§fE§eS§fG§7] " + l.getTxtColor() + "Set chests 1-27 to §eYELLOW§f concrete blocks.");
            sender.sendMessage("§7[§fE§eS§fG§7] " + l.getTxtColor() + "If no chests are visible on the map that is a sucess.");
            return true;
        }

        if (args[0].equalsIgnoreCase("Load")) {
            for (Location location : locations)
                p.performCommand("setblock " + location.getBlockX() + " " + location.getBlockY() + " " + location.getBlockZ() + " minecraft:air");
            p.performCommand("minecraft:kill @e[type=Item]");
            //for (Location location : locations) p.performCommand("setblock " + location.getBlockX() + " " + location.getBlockY() + " " + location.getBlockZ() + " minecraft:chest" + location.getYaw() + " 0 {LootTable:\"chests/loot_table\"}");
            sender.sendMessage("§7[§fE§eS§fG§7] " + l.getTxtColor() + "Set chests 1-27 to §eLOOT TABLE§f chests.");
            sender.sendMessage("§7[§fE§eS§fG§7] " + l.getTxtColor() + "If a chest is empty it is not a sucess.");
            return true;
        }
        return true;
    }

    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> l = new ArrayList<>();
        if (args.length == 1) {
            l.add("test");
            l.add("load");
            return l;
        }
        return null;
    }

    public String[] getSyntax(Command command, Lang l) {
        return new String[]{
                l.getSyntaxMsg() + l.getCmdExColor() + "/" + l.getLightColor() + command.getName().toLowerCase() + " " + l.getCmdExColor() + Utils.Reference.OPEN_MANDATORY_CHAR +
                        l.getLightColor() + Utils.Reference.MODE + l.getCmdExColor() + Utils.Reference.CLOSE_MANDATORY_CHAR
        };
    }

    public String commandName() {
        return "esgchest";
    }
}