package io.github.rookietec9.enderplugin.commandgroups;

import io.github.rookietec9.enderplugin.utils.datamanagers.DataPlayer;
import io.github.rookietec9.enderplugin.utils.datamanagers.EndExecutor;
import io.github.rookietec9.enderplugin.utils.methods.Java;
import io.github.rookietec9.enderplugin.utils.methods.SkullMaker;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

import static io.github.rookietec9.enderplugin.EnderPlugin.serverLang;
import static io.github.rookietec9.enderplugin.utils.reference.Syntax.MODE;

/**
 * @author Jeremi
 * @version 22.8.0
 * @since 20.3.0
 */
public class CrashCommands implements EndExecutor {

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        label = label != null ? label : command.getName();

        if (!(sender instanceof Player) && args.length == 0) return msg(sender, serverLang().getOnlyUserMsg());
        if (args.length > 1) return msg(sender, getSyntax(label));

        Player target = (args.length > 0) ? Bukkit.getPlayer(args[0]) : (Player) sender;

        if (target == null) return msg(sender, serverLang().getOfflineMsg());

        switch (label) {
            case "crash", "boot" -> {
                SkullMaker maker = new SkullMaker();
                ItemStack itemStack = maker.withSkinUrl("-1").build();
                target.getInventory().addItem(itemStack);
            }
            case "delete", "immobilize","freeze" -> target.remove();
        }
        return msg(sender, serverLang().getPlugMsg() + Java.capFirst(label) + (label.toLowerCase().endsWith("e") ? "d" : "ed").replace("freezed","froze") + " " + DataPlayer.getUser(target).getNickName() + ".");
    }

    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return null;
    }

    public String getSyntax(String label) {
        return helpLabel(label) + helpBr(MODE, false);
    }

    public List<String> commandNames() {
        return List.of("crash","delete");
    }
}