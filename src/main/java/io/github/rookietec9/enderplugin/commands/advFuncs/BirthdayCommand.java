package io.github.rookietec9.enderplugin.commands.advFuncs;

import io.github.rookietec9.enderplugin.utils.datamanagers.EndExecutor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;

import static io.github.rookietec9.enderplugin.EnderPlugin.serverLang;
import static io.github.rookietec9.enderplugin.utils.reference.Syntax.MODE;

/**
 * Calculate the age of the server or plugin, or get the birthday.
 *
 * @author Jeremi
 * @version 22.8.0
 * @since 4.0.4
 */
public class BirthdayCommand implements EndExecutor {

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        LocalDate serverDate = LocalDate.of(2016, 2, 25);
        LocalDate pluginDate = LocalDate.of(2016, 4, 1);
        Period pdS = Period.between(serverDate, LocalDate.now());
        Period pdP = Period.between(pluginDate, LocalDate.now());
        if (args.length != 1) return msg(sender, getSyntax(label));

        return switch (args[0].toLowerCase()) {
            case "print" -> msg(sender, serverLang().getPlugMsg() + "This server was created February 25th, 2016.", serverLang().getPlugMsg() + "This plugin was created April 1th, 2016.");
            case "server", "server-age" -> msg(sender, serverLang().getPlugMsg() + "The server is " + formatPeriod(pdS) + "old.");
            case "plugin", "plugin-age" -> msg(sender, serverLang().getPlugMsg() + "The plugin is " + formatPeriod(pdP) + "old.");
            default -> msg(sender, getSyntax(label));
        };
    }

    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) return tabOption(args[0], "Print", "Sever", "Plugin");
        return null;
    }

    private String formatPeriod(Period period) {
        String formatted = period.toString();

        String yearStr = !formatted.contains("D") || !formatted.contains("M") ? " Years & " : " Years, ";
        String monthStr = formatted.contains("D") ? " Months & " : " Months ";
        String dayStr = " days ";

        if (formatted.contains("Y") && formatted.charAt(formatted.indexOf("Y") - 1) == '1' && formatted.charAt(formatted.indexOf("Y") - 2) == 'P') yearStr = yearStr.replace("s", "");
        if (formatted.contains("M") && formatted.charAt(formatted.indexOf("M") - 1) == '1' && formatted.charAt(formatted.indexOf("M") - 2) == 'Y') monthStr = monthStr.replace("s", "");
        if (formatted.contains("D") && formatted.charAt(formatted.indexOf("D") - 1) == '1' && formatted.charAt(formatted.indexOf("D") - 2) == 'M') dayStr = dayStr.replace("s", "");

        formatted = formatted.substring(1);
        formatted = formatted.replace("Y", yearStr);
        formatted = formatted.replace("M", monthStr);
        formatted = formatted.replace("D", dayStr);

        return formatted;
    }

    public String getSyntax(String label) {
        return helpLabel(label) + helpBr(MODE, true);
    }

    public List<String> commandNames() {
        return List.of("bday");
    }
}