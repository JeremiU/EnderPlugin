package io.github.rookietec9.enderplugin.commands.games.ESG;

import io.github.rookietec9.enderplugin.utils.datamanagers.endcommands.EndExecutor;
import io.github.rookietec9.enderplugin.utils.datamanagers.ItemWrapper;
import io.github.rookietec9.enderplugin.utils.methods.Java;
import io.github.rookietec9.enderplugin.utils.methods.esgparser.ESGParser;
import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

import static io.github.rookietec9.enderplugin.EnderPlugin.serverLang;
import static io.github.rookietec9.enderplugin.Reference.*;

/**
 * Fills up chests in the ESG map.
 *
 * @author Jeremi
 * @version 25.5.4
 * @since 5.4.8
 */
public class ESGChestCommand implements EndExecutor {

    public static void changeChest(Location location) {
        location.getBlock().setType(Material.CHEST);
        if (!(location.getBlock().getState() instanceof Chest chest1)) return;

        BlockState state = location.getBlock().getState();

        org.bukkit.material.Chest chest = new org.bukkit.material.Chest(switch ((int) location.getYaw()) {
            case YAW_EAST -> BlockFace.EAST;
            case YAW_WEST -> BlockFace.WEST;
            case YAW_SOUTH -> BlockFace.SOUTH;
            default -> BlockFace.NORTH;
        });
        state.setData(chest);
        state.update();

        chest1.getBlockInventory().clear();
        List<Pair<Material, Integer>> pairs = ESGParser.loot();
        if (pairs == null) return;
        int spot = Java.getRandom(0, 8), counter = pairs.size();
        for (Pair<Material, Integer> pair : pairs) {
            chest1.getBlockInventory().setItem(spot, new ItemWrapper<>(pair.getLeft()).setCount(pair.getRight()).toItemStack());
            spot += Java.getRandom(1, Math.floor((26 - spot) / (double) counter));
            counter--;
        }
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player p)) return msg(sender, serverLang().getOnlyUserMsg());

        if (!p.getWorld().getName().equalsIgnoreCase(ESG_FIGHT)) return msg(sender, serverLang().getErrorMsg() + "Wrong world!");
        if (args.length != 1 || !args[0].equalsIgnoreCase("test") && !args[0].equalsIgnoreCase("load")) return msg(sender, getSyntax(label));

        Location chest01 = new Location(p.getWorld(), -16, (66), -178, YAW_SOUTH, 0); //Corner of map, on a hill.				    3
        Location chest02 = new Location(p.getWorld(), -17, (65), -127, YAW_NORTH, 0); //Seen with Wither Heads, a big chest.	    1
        Location chest03 = new Location(p.getWorld(), -23, (62), -135, YAW_NORTH, 0); //Right next to water and sugar cane.		    1
        Location chest04 = new Location(p.getWorld(), -41, (64), -147, YAW_NORTH, 0); //In a farmhouse, big chest, opposite ladder.   1
        Location chest05 = new Location(p.getWorld(), -40, (62), -142, YAW_NORTH, 0); //Under water, next to mossy cobble.		    1
        Location chest06 = new Location(p.getWorld(), -98, (63), -87, YAW_NORTH, 0); //In the ship.			    					1
        Location chest07 = new Location(p.getWorld(), -86, (63), -80, YAW_SOUTH, 0); //Near the ship, on the side of the map.		    3
        Location chest08 = new Location(p.getWorld(), -53, (65), -87, YAW_SOUTH, 0); //On the side of the cliff, behind it.		    3
        Location chest09 = new Location(p.getWorld(), -17, (85), -94, YAW_SOUTH, 0); //On top of hill, in sand, near house.	    	3
        Location chest37 = new Location(p.getWorld(), -17, (85), -94, YAW_SOUTH, 0); //On top of hill, in sand, near house.	    	3
        Location chest10 = new Location(p.getWorld(), -16, (82), -119, YAW_WEST, 0); //On top of hill, in sand, can view houses.	    4
        Location chest11 = new Location(p.getWorld(), -15, (82), -79, YAW_SOUTH, 0); //On top of hill, on corner of map.				3
        Location chest12 = new Location(p.getWorld(), -97, (65), -138, YAW_WEST, 0); //Near water and shrub, partially in sand.		4
        Location chest13 = new Location(p.getWorld(), -109, (69), -157, YAW_SOUTH, 0); //On hill, in front of water.					3
        Location chest14 = new Location(p.getWorld(), -104, (64), -173, YAW_SOUTH, 0); //Submerged in water fountain.					3
        Location chest15 = new Location(p.getWorld(), -70, (63), -110, YAW_NORTH, 0); //In house, near yellow bed.					2
        Location chest16 = new Location(p.getWorld(), -48, (63), -102, YAW_NORTH, 0); //In cave, near fences.							2
        Location chest17 = new Location(p.getWorld(), -57, (57), -107, YAW_NORTH, 0); //In cave, near iron, near fences.				2
        Location chest18 = new Location(p.getWorld(), -78, (62), -135, YAW_EAST, 0); //Underwater chamber							5
        Location chest19 = new Location(p.getWorld(), -62, (68), -128, YAW_NORTH, 0); // SPAWN CHESTS BEGIN
        Location chest20 = new Location(p.getWorld(), -64, (68), -126, YAW_NORTH, 0);
        Location chest21 = new Location(p.getWorld(), -66, (68), -128, YAW_NORTH, 0);
        Location chest22 = new Location(p.getWorld(), -64, (68), -130, YAW_NORTH, 0);
        Location chest23 = new Location(p.getWorld(), -65, (69), -129, YAW_NORTH, 0);
        Location chest24 = new Location(p.getWorld(), -63, (69), -129, YAW_NORTH, 0);
        Location chest25 = new Location(p.getWorld(), -63, (69), -127, YAW_NORTH, 0);
        Location chest26 = new Location(p.getWorld(), -65, (69), -127, YAW_NORTH, 0); //SPAWN CHESTS END
        Location chest27 = new Location(p.getWorld(), -54, (52), -174, YAW_NORTH, 0); //Under lake, in chamber.
        Location chest28 = new Location(p.getWorld(), -80, (62), -105, YAW_NORTH, 0); //Under lake, in chamber.
        Location chest29 = new Location(p.getWorld(), -23, (58), -167, YAW_NORTH, 0); //Under lake, in chamber.
        Location chest30 = new Location(p.getWorld(), -84, (56), -160, YAW_NORTH, 0); //Under lake, in chamber.
        Location chest31 = new Location(p.getWorld(), -35, 64, -147, YAW_NORTH, 0);
        Location chest32 = new Location(p.getWorld(), -77, 65, -131, YAW_NORTH, 0);
        Location chest33 = new Location(p.getWorld(), -32, 65, -115, YAW_NORTH, 0);
        Location chest34 = new Location(p.getWorld(), -39, 75, -142, YAW_NORTH, 0);
        Location chest35 = new Location(p.getWorld(), -39, 63, -159, YAW_EAST, 0);
        Location chest36 = new Location(p.getWorld(), -18, 65, -127, YAW_NORTH, 0);
        Location chest38 = new Location(p.getWorld(), -108, 63, -120, YAW_WEST, 0);

        Location[] locations = new Location[]{chest01, chest02, chest03, chest04, chest05, chest06, chest07, chest08, chest09, chest10, chest11, chest12, chest13, chest14, chest15, chest16, chest17, chest18, chest19, chest20, chest21, chest22, chest23, chest24, chest25, chest26, chest27, chest28, chest29, chest30, chest31, chest32, chest33, chest34, chest35, chest36, chest37, chest38};

        if (args[0].equalsIgnoreCase("Test")) {
            for (Location location : locations) {
                if (location.getBlock().getState() instanceof org.bukkit.block.Chest) ((org.bukkit.block.Chest) location.getBlock().getState()).getBlockInventory().clear();
                location.getBlock().setType(Material.STAINED_CLAY);
                location.getBlock().setData((byte) 4);
            }
            return msg(sender, PREFIX_ESG + "Set chests YELLOW stained clay blocks.");
        }

        if (args[0].equalsIgnoreCase("Load")) {
            for (Location location : locations) changeChest(location);
            //for (Location location : locations) p.performCommand("setblock " + location.getBlockX() + " " + location.getBlockY() + " " + location.getBlockZ() + " minecraft:chest" + location.getYaw() + " 0 {LootTable:\"chests/loot_table\"}");
            return msg(sender, PREFIX_ESG + "Filled chests with the loot table");
        }
        return true;
    }

    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return args.length == 1 ? tabOption(args[0], "test", "load") : null;
    }

    public String getSyntax(String label) {
        return helpLabel(label) + helpBr(MODE, true);
    }

    public List<String> commandNames() {
        return List.of("esgchest");
    }
}