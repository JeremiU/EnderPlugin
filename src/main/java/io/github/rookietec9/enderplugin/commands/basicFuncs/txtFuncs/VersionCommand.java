package io.github.rookietec9.enderplugin.commands.basicFuncs.txtFuncs;

import io.github.rookietec9.enderplugin.API.EndExecutor;
import io.github.rookietec9.enderplugin.API.configs.Langs;
import io.github.rookietec9.enderplugin.API.configs.associates.Lang;
import io.github.rookietec9.enderplugin.EnderPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.List;

/**
 * Automatically gets the version of the plugin.
 * Now with polished variables (finally.)
 *
 * @author Jeremi
 * @version 13.4.4
 * @since 0.2.2
 */
public class VersionCommand implements EndExecutor {

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Lang l = new Lang(Langs.fromSender(sender));

        String name = getClass().getProtectionDomain().getCodeSource().getLocation().toString().substring(getClass().getProtectionDomain().getCodeSource().getLocation().toString().lastIndexOf("/") + 1);
        String verCycle = name.substring(name.lastIndexOf("-") + 1).replace(EnderPlugin.getInstance().getName(), "").replace(".jar", "");

        for (int i = 0; i <= 10; i++) {
            verCycle = verCycle.replace(String.valueOf(i), "");
        }
        verCycle = verCycle.replace(".", "");
        String verNum = name.replace(verCycle, "").substring(name.indexOf("-") + 1).replace(".jar", "");

        sender.sendMessage(l.getPlugMsg() + EnderPlugin.getInstance().getName() + " Build Information");

        sender.sendMessage(l.getDarkColor() + "Version Cycle" + l.getTxtColor() + " : " + l.getLightColor() + verCycle);
        sender.sendMessage(l.getDarkColor() + "Version Number" + l.getTxtColor() + " : " + l.getLightColor() + verNum);
        sender.sendMessage(l.getDarkColor() + "Jar File name" + l.getTxtColor() + " : " + l.getLightColor() + name);
        return true;
    }

    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return null;
    }

    public String[] getSyntax(Command command, Lang l) {
        return null;
    }

    public String commandName() {
        return "version";
    }
}