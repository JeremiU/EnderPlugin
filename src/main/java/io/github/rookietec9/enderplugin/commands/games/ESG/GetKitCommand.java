package io.github.rookietec9.enderplugin.commands.games.ESG;

import io.github.rookietec9.enderplugin.API.ChestExt;
import io.github.rookietec9.enderplugin.API.EndExecutor;
import io.github.rookietec9.enderplugin.API.Utils;
import io.github.rookietec9.enderplugin.API.configs.Langs;
import io.github.rookietec9.enderplugin.API.configs.associates.Lang;
import io.github.rookietec9.enderplugin.API.esg.ESGKit;
import org.bukkit.Bukkit;
import org.bukkit.block.Chest;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jeremi
 * @version 13.4.4
 * @since 1.5.9
 */
public class GetKitCommand implements EndExecutor {
    private boolean silent = false;

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Lang l = new Lang(Langs.fromSender(sender));
        if (!(sender instanceof Player)) {
            sender.sendMessage(l.getOnlyUserMsg());
            return true;
        }
        if (args.length != 3) {
            sender.sendMessage(getSyntax(command, l));
            return true;
        }
        int level;
        try {
            level = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            sender.sendMessage(l.getNumFormatMsg());
            return true;
        }
        if (!Utils.isInRange(level, 1, 10)) {
            sender.sendMessage(l.getPlugMsg() + "Kit level must be between 1 and 10");
            return true;
        }
        if (!(args[2].equalsIgnoreCase("true") || args[2].equalsIgnoreCase("false"))) {
            sender.sendMessage(l.getErrorMsg() + "That's neither true or false.");
            return true;
        }
        silent = Boolean.valueOf(args[2]);
        ESGKit esgKit = null;

        for (ESGKit kit : ESGKit.values()) {
            if (args[0].equalsIgnoreCase(kit.toString())) esgKit = kit;
        }

        if (args[0].equalsIgnoreCase("Cactus") || args[0].equalsIgnoreCase("Cactusman")) esgKit = ESGKit.CACTUS_MAN;
        if (args[0].equalsIgnoreCase("Lava") || args[0].equalsIgnoreCase("Slime")) esgKit = ESGKit.FURY_SLIME;
        if (args[0].equalsIgnoreCase("Horse")) esgKit = ESGKit.HORSE_TAMER;
        if (args[0].equalsIgnoreCase("Negro")) esgKit = ESGKit.NEGROMANCER;
        if (args[0].equalsIgnoreCase("Ninja")) esgKit = ESGKit.AQUA_NINJA;

        if (esgKit == null) {
            sender.sendMessage(l.getErrorMsg() + "That's not a valid kit!");
            return true;
        }
        getKit((Player) sender, level, esgKit);
        return true;
    }

    private void getKit(Player p, int level, ESGKit esgKit) {
        Lang l = new Lang(Langs.fromSender(p));
        int y, x = 0, z = 0;

        if (esgKit == ESGKit.RABBIT || esgKit == ESGKit.ARCHER || esgKit == ESGKit.SNOWMAN || esgKit == ESGKit.ASSASSIN || esgKit == ESGKit.ENDERMAN || esgKit == ESGKit.WITCH)
            z = -123;
        if (esgKit == ESGKit.HORSE_TAMER || esgKit == ESGKit.KNIGHT || esgKit == ESGKit.ARMORER || esgKit == ESGKit.AQUA_NINJA || esgKit == ESGKit.FURY_SLIME || esgKit == ESGKit.WOLF_TAMER)
            z = -125;
        if (esgKit == ESGKit.FISHERMAN || esgKit == ESGKit.GECKO || esgKit == ESGKit.SCOUT || esgKit == ESGKit.NEGROMANCER || esgKit == ESGKit.CACTUS_MAN)
            z = -127;

        if (esgKit == ESGKit.RABBIT || esgKit == ESGKit.HORSE_TAMER || esgKit == ESGKit.FISHERMAN) x = -58;
        if (esgKit == ESGKit.ARCHER || esgKit == ESGKit.KNIGHT || esgKit == ESGKit.GECKO) x = -60;
        if (esgKit == ESGKit.SNOWMAN || esgKit == ESGKit.ARMORER || esgKit == ESGKit.SCOUT) x = -62;
        if (esgKit == ESGKit.ASSASSIN || esgKit == ESGKit.AQUA_NINJA || esgKit == ESGKit.NEGROMANCER) x = -64;
        if (esgKit == ESGKit.ENDERMAN || esgKit == ESGKit.FURY_SLIME) x = -66;
        if (esgKit == ESGKit.WITCH || esgKit == ESGKit.WOLF_TAMER || esgKit == ESGKit.CACTUS_MAN) x = -68;

        y = 61 - ((10 - level) * 2);
        if ((Bukkit.getWorld(Utils.Reference.Worlds.ESG_FIGHT).getBlockAt(x, y, z).getState() instanceof Chest)) {
            Chest chest = new ChestExt((Chest) Bukkit.getWorld("SurvivalGames").getBlockAt(x, y, z).getState()).setName(esgKit.getColor() +
                    "§l" + Utils.upSlash(esgKit.toString()) + "" + " §f[" + esgKit.getColor() + "§l" + Utils.convertToRoman(level) + "§f]").getExt();
            ItemStack[] renamedInv = chest.getInventory().getContents();
            chest.update();
            for (ItemStack i : renamedInv) {
                if (i == null) continue;
                if (i.getItemMeta().getDisplayName() != null && i.getItemMeta().getDisplayName().contains("§")) {
                    ItemMeta meta = i.getItemMeta();
                    meta.setDisplayName(meta.getDisplayName().replace("§", ""));
                    i.setItemMeta(meta);
                }
            }
            chest.getBlockInventory().setContents(renamedInv);
            chest.update();
            for (ItemStack i : chest.getInventory().getContents()) {
                if (i != null) {
                    p.getInventory().addItem(i);
                }
            }
            p.updateInventory();
        } else p.sendMessage(l.getErrorMsg() + "the BlockState is not a chest.");
        if (!silent)
            p.sendMessage(l.getPlugMsg() + "Gave you " + esgKit.getColor() + Utils.upSlash(esgKit.toString()) + l.getTxtColor() + " " + level);
    }

    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> l = new ArrayList<>();
        if (args.length == 1) {
            l.add("Archer");
            l.add("Armorer");
            l.add("Assassin");
            l.add("Cactusman");
            l.add("Enderman");
            l.add("Fisherman");
            l.add("Gecko");
            l.add("Lava");
            l.add("Horse");
            l.add("Knight");
            l.add("Negro");
            l.add("Ninja");
            l.add("Rabbit");
            l.add("Scout");
            l.add("Snow");
            l.add("Witch");
            l.add("Wolf");
            return l;
        }
        if (args.length == 2) {
            for (int i = 0; i <= 10; i++) {
                l.add("" + i);
            }
            return l;
        }
        if (args.length == 3) {
            l.add("true");
            l.add("false");
            return l;
        }
        return null;
    }

    public String[] getSyntax(Command command, Lang l) {
        return new String[]{
                l.getSyntaxMsg() + l.getCmdExColor() + "/" + l.getLightColor() + command.getName().toLowerCase() + " " + l.getCmdExColor() +
                        Utils.Reference.OPEN_MANDATORY_CHAR + l.getLightColor() + "kit" + l.getCmdExColor() + Utils.Reference.CLOSE_MANDATORY_CHAR +
                        Utils.Reference.OPEN_MANDATORY_CHAR + l.getLightColor() + "level" + l.getCmdExColor() + Utils.Reference.CLOSE_MANDATORY_CHAR +
                        Utils.Reference.OPEN_MANDATORY_CHAR + l.getLightColor() + Utils.Reference.MODE + l.getCmdExColor() + Utils.Reference.CLOSE_MANDATORY_CHAR
        };
    }

    public String commandName() {
        return "esgkit";
    }
}