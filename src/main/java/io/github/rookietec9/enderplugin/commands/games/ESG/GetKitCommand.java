package io.github.rookietec9.enderplugin.commands.games.ESG;

import io.github.rookietec9.enderplugin.configs.esg.ESGKit;
import io.github.rookietec9.enderplugin.utils.datamanagers.ChestExt;
import io.github.rookietec9.enderplugin.utils.datamanagers.DataPlayer;
import io.github.rookietec9.enderplugin.utils.datamanagers.endcommands.EndExecutor;
import io.github.rookietec9.enderplugin.utils.methods.Java;
import org.bukkit.Bukkit;
import org.bukkit.block.Chest;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

import static io.github.rookietec9.enderplugin.EnderPlugin.serverLang;
import static io.github.rookietec9.enderplugin.Reference.ESG_FIGHT;
import static io.github.rookietec9.enderplugin.Reference.MODE;

/**
 * @author Jeremi
 * @version 25.2.6
 * @since 1.5.9
 */
public class GetKitCommand implements EndExecutor {

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) return msg(sender, serverLang().getOnlyUserMsg());
        if (!Java.isInRange(args.length, 2, 3)) return msg(sender, getSyntax(label));

        int level;
        try {
            level = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            return msg(sender, serverLang().getNumFormatMsg());
        }

        if (!Java.isInRange(level, 1, 10)) return msg(sender, serverLang().getPlugMsg() + "ESGKit level must be between 1 and 10.");
        if (args.length == 3 && !Java.argWorks(args[2], "true", "false", "on", "off")) msg(sender, getSyntax(label));

        boolean log = (args.length != 3 || Java.argWorks(args[2], "on", "true"));
        ESGKit.Kits esgKit = ESGKit.Kits.from(args[0]);

        if (esgKit == null) return msg(sender, serverLang().getErrorMsg() + "That's not a valid kit!");

        return getKit((Player) sender, level, esgKit, log);
    }

    private boolean getKit(Player p, int level, ESGKit.Kits esgKit, boolean silent) {
        int y = 61 - ((10 - level) * 2);
        if ((Bukkit.getWorld(ESG_FIGHT).getBlockAt(esgKit.getX(), y, esgKit.getZ()).getState() instanceof Chest)) {
            new ChestExt((Chest) Bukkit.getWorld("SurvivalGames").getBlockAt(esgKit.getX(), y, esgKit.getZ()).getState()).setName(new ESGKit(esgKit).getColor() + "§l" + Java.upSlash(esgKit.toString()) + "" + " §f[" + new ESGKit(esgKit).getColor() + "§l" + Java.convertToRoman(level) + "§f]");
            DataPlayer.get(p).fromChest(Bukkit.getWorld("SurvivalGames"), esgKit.getX(), y, esgKit.getZ());
            p.updateInventory();
        } else return msg(p, serverLang().getErrorMsg() + "the BlockState is not a chest.");
        if (!silent) return msg(p, serverLang().getPlugMsg() + "Gave you " + new ESGKit(esgKit).getColor() + Java.upSlash(esgKit.toString()) + serverLang().getTxtColor() + " " + level);

        return true;
    }

    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) return tabOption(args[0], ESGKit.Kits.class);
        if (args.length == 2) return tabOption(args[0], "1", "2", "3", "4", "5", "6", "7", "8", "9", "10");
        return null;
    }

    public String getSyntax(String label) {
        return helpLabel(label) + helpBr("kit", true) + " " + helpBr("level", true) + " " + helpBr(MODE, true);
    }

    public List<String> commandNames() {
        return List.of("esgkit");
    }
}