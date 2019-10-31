package io.github.rookietec9.enderplugin.commands.basicFuncs.txtFuncs;

import com.google.common.collect.ImmutableList;
import io.github.rookietec9.enderplugin.API.EndExecutor;
import io.github.rookietec9.enderplugin.API.Utils;
import io.github.rookietec9.enderplugin.API.configs.Langs;
import io.github.rookietec9.enderplugin.API.configs.associates.Lang;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.*;
import org.bukkit.help.HelpMap;
import org.bukkit.help.HelpTopic;
import org.bukkit.help.HelpTopicComparator;
import org.bukkit.help.IndexHelpTopic;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.ChatPaginator;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * Average help command.
 *
 * @author Jeremi
 * @version 11.6.0
 * @since 1.9.6.
 */
public class HelpCommand implements EndExecutor {

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Lang l = new Lang(Langs.fromSender(sender));
        int pageNumber;
        String command;
        if (args.length == 0) {
            command = "";
            pageNumber = 1;
        } else if (NumberUtils.isDigits(args[(args.length - 1)])) {
            command = StringUtils.join(ArrayUtils.subarray(args, 0, args.length - 1), " ");
            try {
                pageNumber = NumberUtils.createInteger(args[(args.length - 1)]);
            } catch (NumberFormatException localNumberFormatException) {
                pageNumber = 1;
            }
            if (pageNumber <= 0)
                pageNumber = 1;
        } else {
            command = StringUtils.join(args, " ");
            pageNumber = 1;
        }
        int pageWidth;
        int pageHeight;
        if ((sender instanceof ConsoleCommandSender)) {
            pageHeight = Integer.MAX_VALUE;
            pageWidth = Integer.MAX_VALUE;
        } else {
            pageHeight = 9;
            pageWidth = 55;
        }

        HelpMap helpMap = Bukkit.getServer().getHelpMap();
        HelpTopic topic = helpMap.getHelpTopic(command);
        if (topic == null) {
            topic = helpMap.getHelpTopic("/" + command);
        }
        if (topic == null) {
            topic = findPossibleMatches(command);
        }

        if ((topic == null) || (!topic.canSee(sender))) {
            sender.sendMessage(l.getErrorMsg() + "No help for " + command);
            return true;
        }

        ChatPaginator.ChatPage page = ChatPaginator.paginate(topic.getFullText(sender), pageNumber, pageWidth, pageHeight);

        StringBuilder header = new StringBuilder();
        header.append(l.getDarkColor());
        header.append("--------- ");
        header.append(ChatColor.WHITE);
        header.append("Help: ");
        header.append(topic.getName());
        header.append(" ");
        if (page.getTotalPages() > 1) {
            header.append("[");
            header.append(page.getPageNumber());
            header.append("/");
            header.append(page.getTotalPages());
            header.append("] ");
        }
        header.append(l.getDarkColor());
        for (int i = header.length(); i < 55; i++) {
            header.append("-");
        }
        sender.sendMessage(header.toString());
        String toSend = "";
        for (String s : page.getLines()) {
            char[] charArray = s.toCharArray();
            for (int i = 0; i < charArray.length; i++) {
                if (charArray[i] == ':') {
                    toSend = l.getDarkColor() + ChatColor.stripColor(s.substring(0, i)) + l.getLightColor()
                            + ChatColor.stripColor(s.substring(i + 1, s.length()));
                }
                for (Plugin p : Bukkit.getPluginManager().getPlugins()) {
                    if ((s.split(" ")[0].equalsIgnoreCase(p.getName()) || s.split(" ")[0].equalsIgnoreCase("bukkit")
                            || s.split(" ")[0].equalsIgnoreCase("aliases") ||
                            s.split(" ")[0].equalsIgnoreCase("bukkit")) && (charArray[i] == ':')) {
                        toSend = l.getDarkColor() + "§l" + ChatColor.stripColor(s.substring(0, i)) + l.getLightColor()
                                + ChatColor.stripColor(s.substring(i + 1, s.length()));
                    }
                }
            }
            sender.sendMessage(toSend);
        }
        return true;
    }

    private HelpTopic findPossibleMatches(String searchString) {
        int maxDistance = searchString.length() / 5 + 3;
        Set<HelpTopic> possibleMatches = new TreeSet<>(HelpTopicComparator.helpTopicComparatorInstance());

        if (searchString.startsWith("/")) {
            searchString = searchString.substring(1);
        }
        for (HelpTopic topic : Bukkit.getServer().getHelpMap().getHelpTopics()) {
            String trimmedTopic = topic.getName().startsWith("/") ? topic.getName().substring(1) : topic.getName();
            if ((trimmedTopic.length() >= searchString.length())
                    && (Character.toLowerCase(trimmedTopic.charAt(0)) == Character.toLowerCase(searchString.charAt(0)))
                    && (Utils.getLeven(searchString, trimmedTopic.substring(0, searchString.length())) < maxDistance)) {
                possibleMatches.add(topic);
            }
        }

        if (possibleMatches.size() > 0) {
            return new IndexHelpTopic("Search", null, null, possibleMatches, "Search for: " + searchString);
        }
        return null;
    }

    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            List<String> matchedTopics = new ArrayList<>();
            String searchString = args[0];
            for (HelpTopic topic : Bukkit.getServer().getHelpMap().getHelpTopics()) {
                String trimmedTopic = topic.getName().startsWith("/") ? topic.getName().substring(1) : topic.getName();
                if (trimmedTopic.startsWith(searchString)) {
                    matchedTopics.add(trimmedTopic);
                }
            }
            return matchedTopics;
        }
        return ImmutableList.of();
    }

    public String[] getSyntax(Command command, Lang l) {
        return null;
    }

    public String commandName() {
        return "help";
    }
}