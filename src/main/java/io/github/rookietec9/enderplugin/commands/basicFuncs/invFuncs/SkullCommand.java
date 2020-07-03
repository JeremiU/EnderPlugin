package io.github.rookietec9.enderplugin.commands.basicFuncs.invFuncs;

import io.github.rookietec9.enderplugin.utils.datamanagers.EndExecutor;
import io.github.rookietec9.enderplugin.utils.methods.SkullMaker;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

import static io.github.rookietec9.enderplugin.EnderPlugin.serverLang;
import static io.github.rookietec9.enderplugin.utils.reference.Syntax.*;

/**
 * @author Jeremi
 * @version 22.8.0
 * @since 19.3.4
 */
public class SkullCommand implements EndExecutor {

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player)) return msg(sender, serverLang().getOnlyUserMsg());
        if (args.length != 1) return msg(sender, getSyntax(label));

        SkullMaker maker = new SkullMaker();
        ItemStack itemStack = maker.buildProfile(args[0]);

        if (itemStack != null) ((Player) sender).getInventory().addItem(itemStack);
        else return msg(sender, serverLang().getErrorMsg() + "Wrong skull info!");

        return msg(sender, serverLang().getPlugMsg() + "Gave you the skull.");
    }

    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return null;
    }

    public String getSyntax(String label) {
        return helpLabel(label) + helpBr(USER, true);
    }

    public List<String> commandNames() {
        return List.of("skull");
    }
}