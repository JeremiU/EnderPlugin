package io.github.rookietec9.enderplugin.API;

import io.github.rookietec9.enderplugin.API.configs.associates.Lang;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.TabCompleter;

import java.util.List;

/**
 * Provides an executor simplifying stuff.
 *
 * @author Jeremi
 * @version 13.4.4
 * @since 8.9.9
 */
public interface EndExecutor extends CommandExecutor, TabCompleter {
    String[] getSyntax(Command command, Lang l);
    String commandName();
}