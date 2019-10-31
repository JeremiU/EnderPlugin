package io.github.rookietec9.enderplugin.commands.advFuncs;

import io.github.rookietec9.enderplugin.API.EndExecutor;
import io.github.rookietec9.enderplugin.API.Utils;
import io.github.rookietec9.enderplugin.API.configs.Langs;
import io.github.rookietec9.enderplugin.API.configs.associates.Lang;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Calculate the age of the server or plugin, or get the birthday.
 *
 * @author Jeremi
 * @version 13.4.4
 * @since 4.0.4
 */
public class BirthdayCommand implements EndExecutor {

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        int yrDay = Integer.valueOf(new SimpleDateFormat("DDD").format(new Date())) - 56;

        int plugYrDay;
        if ((Integer.valueOf(new SimpleDateFormat("yyyy").format(new Date())) % 4) == 0) {
            plugYrDay = Integer.valueOf(new SimpleDateFormat("DDD").format(new Date())) - 92;
        } else plugYrDay = Integer.valueOf(new SimpleDateFormat("DDD").format(new Date())) - 91;

        int[] first = format(Integer.valueOf(new SimpleDateFormat("dd").format(new Date())) - 25, Integer.valueOf(new SimpleDateFormat("MM").
                format(new Date())) - 2, Integer.valueOf(new SimpleDateFormat("yyyy").format(new Date())) - 2016);
        int[] second = format(Integer.valueOf(new SimpleDateFormat("dd").format(new Date())) - 1, Integer.valueOf(new SimpleDateFormat("MM").
                format(new Date())) - 4, Integer.valueOf(new SimpleDateFormat("yyyy").format(new Date())) - 2016);

        int newDay = first[0];
        int newMonth = first[1];
        int newYear = first[2];

        int newPlugDay = second[0];
        int newPlugMonth = second[1];
        int newPlugYear = second[2];

        Lang l = new Lang(Langs.fromSender(sender));
        if (args.length != 1) {
            sender.sendMessage(getSyntax(command, l));
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "print": {
                sender.sendMessage(l.getPlugMsg() + "This server was created February 25th, 2016.");
                sender.sendMessage(l.getPlugMsg() + "This plugin was created April 1th, 2016.");
                return true;
            }
            case "server-age": {
                sender.sendMessage(l.getPlugMsg() + "This server is " + l.getDarkColor() + newYear + l.getTxtColor() + " year(s) old, " + l.getDarkColor() +
                        newMonth + l.getTxtColor() + " month(s) old, and " + l.getDarkColor() + newDay + l.getTxtColor() + " day(s) old.");
                sender.sendMessage(l.getPlugMsg() + l.getTxtColor() + "That means it is " + l.getDarkColor() + newYear + l.getTxtColor() + " year(s) old and " + l.getDarkColor() +
                        yrDay + l.getTxtColor() + " day(s) old.");
                return true;
            }
            case "plugin-age": {
                sender.sendMessage(l.getPlugMsg() + "This plugin is " + l.getDarkColor() + newPlugYear + l.getTxtColor() + " year(s) old, " + l.getDarkColor() +
                        newPlugMonth + l.getTxtColor() + " months old, and " + l.getDarkColor() + newPlugDay + l.getTxtColor() + " day(s) old.");
                sender.sendMessage(l.getPlugMsg() + "That means it is " + l.getDarkColor() + newYear + l.getTxtColor() + " year(s) old and " + l.getDarkColor() +
                        plugYrDay + l.getTxtColor() + " day(s) old.");
                return true;
            }
            default: {
                sender.sendMessage(getSyntax(command, l));
                return true;
            }
        }
    }

    private int[] format(int day, int month, int year) {
        if (day < 0) {
            month -= 1;
            if (Integer.valueOf(new SimpleDateFormat("MM").format(new Date())) == 1 ||
                    Integer.valueOf(new SimpleDateFormat("MM").format(new Date())) == 3 ||
                    Integer.valueOf(new SimpleDateFormat("MM").format(new Date())) == 5 ||
                    Integer.valueOf(new SimpleDateFormat("MM").format(new Date())) == 7 ||
                    Integer.valueOf(new SimpleDateFormat("MM").format(new Date())) == 8 ||
                    Integer.valueOf(new SimpleDateFormat("MM").format(new Date())) == 10 ||
                    Integer.valueOf(new SimpleDateFormat("MM").format(new Date())) == 12
            ) day = 31 - Math.abs(day);
            else if (Integer.valueOf(new SimpleDateFormat("MM").format(new Date())) == 4 ||
                    Integer.valueOf(new SimpleDateFormat("MM").format(new Date())) == 8 ||
                    Integer.valueOf(new SimpleDateFormat("MM").format(new Date())) == 9 ||
                    Integer.valueOf(new SimpleDateFormat("MM").format(new Date())) == 11
            ) day = 30 - Math.abs(day);
            else if (Integer.valueOf(new SimpleDateFormat("MM").format(new Date())) == 2) {
                if ((Integer.valueOf(new SimpleDateFormat("yyyy").format(new Date())) % 4) == 0) {
                    day = 29 - Math.abs(day);
                } else day = 28 - Math.abs(day);
            }
        }

        if (month < 0) {
            year -= 1;
            month = 12 - Math.abs(month);
        }

        return new int[]{day, month, year};
    }

    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> l = new ArrayList<>();
        if (args.length == 1) {
            l.add("Print");
            l.add("Server-age");
            l.add("Plugin-age");
            return l;
        }
        return null;
    }

    public String[] getSyntax(Command command, Lang l) {
        return new String[]{
                l.getSyntaxMsg() + l.getCmdExColor() + "/" + l.getLightColor() + command.getName().toLowerCase() + " " + l.getCmdExColor() +
                        Utils.Reference.OPEN_MANDATORY_CHAR + l.getLightColor() + Utils.Reference.MODE + l.getCmdExColor() + Utils.Reference.CLOSE_MANDATORY_CHAR
        };
    }

    public String commandName() {
        return "bday";
    }

}