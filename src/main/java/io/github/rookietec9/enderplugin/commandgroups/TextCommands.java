package io.github.rookietec9.enderplugin.commandgroups;

import io.github.rookietec9.enderplugin.EnderPlugin;
import io.github.rookietec9.enderplugin.utils.datamanagers.DataPlayer;
import io.github.rookietec9.enderplugin.utils.datamanagers.EndExecutor;
import io.github.rookietec9.enderplugin.utils.datamanagers.Item;
import io.github.rookietec9.enderplugin.utils.methods.Java;
import io.github.rookietec9.enderplugin.utils.methods.Minecraft;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static io.github.rookietec9.enderplugin.EnderPlugin.serverLang;
import static io.github.rookietec9.enderplugin.utils.methods.Minecraft.VerType;
import static io.github.rookietec9.enderplugin.utils.reference.Syntax.MSG;

/**
 * @author Jeremi
 * @version 22.8.3
 * @since 21.4.5
 */
public class TextCommands implements EndExecutor {

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        label = label != null ? label : command.getName();
        String[] toSend = null;
        Player player = (sender instanceof Player) ? (Player) sender : null;

        Item<?> item = null;

        if (player != null) {
            item = Item.fromItemStack(player.getItemInHand());
            if (item.isEmpty() && Java.argWorks(label, "itemval", "itemdb", "getcolor")) return msg(sender, serverLang().getErrorMsg() + "Please hold something.");
        } else if (Java.argWorks(label, "itemval", "itemdb", "getcolor")) return msg(sender, serverLang().getOnlyUserMsg());

        switch (label.toLowerCase()) {
            case "details" -> toSend = new String[]{serverLang().getPlugMsg() + "This server is being run on: " + serverLang().getDarkColor() + DataPlayer.getUniversalIP() + ChatColor.WHITE + ":" + serverLang().getLightColor() + Bukkit.getServer().getPort()};
            case "colors" -> toSend = new String[]{serverLang().getPlugMsg() + "Colors: §00 §11 §22 §33 §44 §55 §66 §77 §88 §99 §f/ §AA §BB §CC §DD §EE §FF / §RR §r§LL §r§mm§r §r§nn§r §r§oo§r §r§kk§rk"};
            case "scheduler" -> {
                for (String s : EnderPlugin.scheduler().currentRunningIDs()) sender.sendMessage("ID: " + s + " BUKKIT ID: " + EnderPlugin.scheduler().bukkitID(s));
            }
            case "yt" -> {
                if (Java.argWorks(sender.getName(), "kai8898", "TheEnderCrafter9")) sender.sendMessage(serverLang().getDarkColor() + "http://bit.ly/1ruZdZs");
            }
            case "anoncast" -> {
                if (args.length == 0) return msg(sender, getSyntax(label));
                Bukkit.broadcastMessage(Minecraft.tacc(StringUtils.join(args, ' ', 0, args.length)));
            }
            case "itemval", "itemdb" -> {
                if (player.getItemInHand() == null) return msg(sender, serverLang().getErrorMsg() + "There's nothing in your hand!");
                toSend = new String[]{serverLang().getPlugMsg() + "You are holding a " + item.getType() + " (item id " + item.getType().getId() + ") with byte value " + item.getDataByte()};
            }
            case "getcolor" -> {
                if (!item.isPaintAble() || item.getColor() == null) return msg(sender, serverLang().getErrorMsg() + "Please hold a dyed item.");
                toSend = new String[]{serverLang().getPlugMsg() + "§c" + item.getColor().getRed() + "§a " + item.getColor().getBlue() + "§b " + item.getColor().getGreen()};
            }
            case "version", "plugver" -> {
                String template = serverLang().getDarkColor() + "a" + serverLang().getTxtColor() + " : " + serverLang().getLightColor() + "x";
                toSend = new String[]{serverLang().getPlugMsg() + EnderPlugin.getInstance().getName() + " Build Information",
                        template.replace("a", "Version Cycle").replace("x", Minecraft.versionInfo(VerType.CYCLE)),
                        template.replace("a", "Version Number").replace("x", Minecraft.versionInfo(VerType.NUM)),
                        template.replace("a", "Jar Name").replace("x", Minecraft.versionInfo(VerType.JARNAME)),
                };
            }
            case "ping" -> {
                if (!(sender instanceof Player)) return msg(sender, serverLang().getOnlyUserMsg());

                Player target = (args.length > 0) ? Bukkit.getPlayer(args[0]) : (Player) sender;
                List<String> list = new ArrayList<>();

                if (target == null) {
                    list.add(serverLang().getPlugMsg() + "getting everyone's ping:");

                    for (Player player1 : Bukkit.getOnlinePlayers()) list.add(serverLang().getTxtColor() + DataPlayer.getUser(player1).getNickName() + serverLang().getDarkColor() + " : " + serverLang().getLightColor() + DataPlayer.getPing(player1));
                }
                if (target != null) toSend = new String[]{serverLang().getPlugMsg() + (sender.equals(target) ? "Your" : DataPlayer.getUser(target).getNickName() + "'s") + " ping is " + serverLang().getLightColor() + DataPlayer.getPing(target) + serverLang().getTxtColor() + "."};
                else toSend = list.toArray(new String[0]);
            }
            case "changelog", "new" -> {
                Scanner scanner;
                try {
                    scanner = new Scanner(new File("E:" + File.separator + "MCD" + File.separator + "changelog.txt"));
                } catch (Exception e) {
                    e.printStackTrace();
                    return msg(sender, serverLang().getErrorMsg() + "Couldn't get the changelog file!");
                }
                String mode;
                if (args.length == 0) mode = "ver";
                else mode = args[0];

                boolean printDev = args.length == 2 && args[1].equalsIgnoreCase("dev");
                String cancel = "\\-\\";

                if (!Java.argWorks(mode, "ver", "cycle", "all")) return msg(sender, serverLang().getErrorMsg() + "That's  not a valid mode.");
                if (mode.equalsIgnoreCase("ver")) {
                    mode = "&f" + Minecraft.versionInfo(VerType.NUM_NO_DOTS);
                    cancel = "&f" + (Integer.parseInt(Minecraft.versionInfo(VerType.NUM_NO_DOTS)) + 1) + "";
                }
                if (mode.equalsIgnoreCase("cycle")) {
                    mode = "&l" + Minecraft.versionInfo(VerType.CYCLE);
                    cancel = "&l" + Minecraft.getVersions().get(Minecraft.getVersions().indexOf(Minecraft.versionInfo(VerType.CYCLE)) + 1);
                    System.out.println("mode " + mode + " cancel " + cancel);
                }
                if (mode.equalsIgnoreCase("all")) mode = "/-/";

                boolean print = false;
                sender.sendMessage(serverLang().getPlugMsg() + "Getting the changelog for this version:");
                while (scanner.hasNext()) {
                    String next = scanner.nextLine();
                    if (next.equalsIgnoreCase(mode) || next.toLowerCase().startsWith(mode.toLowerCase())) print = true;
                    if (Java.argWorks(next, cancel, "\\-\\")) break;
                    if (!print || next.startsWith("[DEV]") && !printDev) continue;
                    String message = serverLang().getTxtColor() + Minecraft.tacc(next.replace("- ", "§b- §7"));
                    if (next.startsWith("[DEV] ")) message = "§3- §7" + serverLang().getTxtColor() + Minecraft.tacc(next.substring("[DEV] ".length()));
                    sender.sendMessage(message);
                }
                scanner.close();
            }
        }
        return toSend == null || msg(sender, toSend);
    }

    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (Java.argWorks(alias, "changelog", "new")) {
            if (args.length == 1) return tabOption(args[0], "ver", "cycle", "all");
            if (args.length == 2) return tabOption(args[1], "dev", "normal");
        }
        return null;
    }

    public String getSyntax(String label) {
        return helpLabel(label) + helpBr(MSG, true);
    }

    public List<String> commandNames() {
        return List.of("anoncast", "changelog", "colors", "details", "getcolor", "itemval", "ping", "scheduler", "scheduler", "version", "yt");
    }
}