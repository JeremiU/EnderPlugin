package io.github.rookietec9.enderplugin.commands.games;

import io.github.rookietec9.enderplugin.API.EndExecutor;
import io.github.rookietec9.enderplugin.API.configs.Langs;
import io.github.rookietec9.enderplugin.API.configs.associates.Lang;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class PlayCommand implements EndExecutor {

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Lang l = new Lang(Langs.fromSender(sender));

        if (args.length == 0 && !(sender instanceof Player)) {
            return true;
        }
        return false;
    }

    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return null;
    }

    public String[] getSyntax(Command command, Lang l) {
        return null;
    }

    public String commandName() {
        return null;
    }


    private enum Games {
        CTF, HIDEANDSEEK, MURDERER, SPLEEF, SUMO, TNTRUN, ESG, HUNGER, RUN, FLOOD, DUCKHUNT
        ;
    }

}
