package io.github.rookietec9.enderplugin.utils.datamanagers.endcommands;

import io.github.rookietec9.enderplugin.utils.methods.Java;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.function.Consumer;

import static io.github.rookietec9.enderplugin.EnderPlugin.serverLang;
import static io.github.rookietec9.enderplugin.Reference.*;

/**
 * Provides an executor simplifying stuff.
 *
 * @author Jeremi
 * @version 25.7.3
 * @since 8.9.9
 */
public interface EndExecutor extends CommandExecutor, TabCompleter {

    String getSyntax(String label);

    List<String> commandNames();

    default List<String> tabOption(String arg, String... possibleOptions) {
        if (arg == null || arg.isEmpty()) return List.of(possibleOptions);
        List<String> newOpts = new ArrayList<>();
        for (String option : possibleOptions) if (option.toLowerCase().startsWith(arg.toLowerCase())) newOpts.add(option);
        return (!newOpts.isEmpty()) ? newOpts : List.of(possibleOptions);
    }

    default List<String> tabOption(String arg, List<?> possibleOptions) {
        String[] arr = new String[possibleOptions.size()];
        for (int i = 0; i < possibleOptions.size(); i++) {
            if (possibleOptions.get(i) instanceof Enum<?>) arr[i] = Java.capFirst(((Enum<?>) possibleOptions).name());
            else arr[i] = possibleOptions.get(i).toString();
        }
        return tabOption(arg, arr);
    }

    default List<String> tabOption(String arg, Class<? extends Enum> possibleOptions) {
        EnumSet enumSet = EnumSet.allOf(possibleOptions);
        List<String> strings = new ArrayList<>();
        enumSet.forEach((Consumer<Enum>) anEnum -> strings.add(Java.capFirst(anEnum.name())));
        return tabOption(arg, strings);
    }

    default String helpLabel(String label) {
        return serverLang().getSyntaxMsg() + serverLang().getCmdExColor() + "/" + serverLang().getLightColor() + label.toLowerCase() + " " + serverLang().getCmdExColor();
    }

    default String helpBr(String arg, boolean mandatory) {
        return serverLang().getCmdExColor() + "" + (mandatory ? OMC : OOC) + serverLang().getLightColor() + arg + serverLang().getCmdExColor() + (mandatory ? CMC : COC);
    }

    default boolean msg(CommandSender sender, String... args) {
        sender.sendMessage(args);
        return true;
    }
}