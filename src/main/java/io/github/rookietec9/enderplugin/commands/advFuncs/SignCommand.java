package io.github.rookietec9.enderplugin.commands.advFuncs;

import io.github.rookietec9.enderplugin.utils.datamanagers.EndExecutor;
import io.github.rookietec9.enderplugin.utils.methods.Minecraft;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.List;

import static io.github.rookietec9.enderplugin.EnderPlugin.serverLang;
import static io.github.rookietec9.enderplugin.utils.reference.Syntax.MODE;
import static io.github.rookietec9.enderplugin.utils.reference.Syntax.MSG;


/**
 * Edits a sign.
 *
 * @author Jeremi
 * @version 22.8.0
 * @since 3.5.2
 */
public class SignCommand implements EndExecutor {

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) return msg(sender, serverLang().getOnlyUserMsg());
        if (args.length == 0) return msg(sender, this.getSyntax("sign"));

        Player p = (Player) sender;
        if (args[0].equalsIgnoreCase("setLine")) {
            if (args.length <= 2) return msg(sender, this.getSyntax(args[0]));

            int line;
            StringBuilder text = new StringBuilder();
            try {
                line = Integer.parseInt(args[1]) - 1;
            } catch (NumberFormatException e) {
                return msg(sender, serverLang().getErrorMsg() + "That is not a number.");
            }
            if ((line < 0) || (line > 3)) return msg(sender, serverLang().getErrorMsg() + "The number must be from 1-4.");
            for (int i = 2; i < args.length; i++) text.append(args[i]).append(" ");
            text = new StringBuilder(text.substring(0, text.length() - 1));

            if (text.length() > 15) return msg(sender, serverLang().getErrorMsg() + "Text can only be up to 15 characters long!");
            Sign sign = getSign(p);
            if (sign == null) return true;
            sign.setLine(line, Minecraft.tacc(text.toString()));
            sign.update(true);
            return true;
        }

        if (args[0].equalsIgnoreCase("todo")) {
            if (args.length < 3) return msg(sender, this.getSyntax("todo"));
            Sign sign = getSign(p);
            String text = ChatColor.stripColor(StringUtils.join(args, " ", 2, args.length));
            if (sign == null) return true;
            if (text.length() > 45) return msg(sender, serverLang().getErrorMsg() + "Text can only be up to 45 characters long!");
            ChatColor lightColor = null;
            ChatColor darkColor = null;
            int startLine;

            switch (args[1].toLowerCase()) {
                case "green","aqua","red","gray","blue" -> {
                    lightColor = ChatColor.valueOf(args[1].toUpperCase());
                    darkColor = ChatColor.valueOf("DARK_" + args[1].toUpperCase());
                }
                case "black" -> {
                    lightColor = ChatColor.DARK_GRAY;
                    darkColor = ChatColor.BLACK;
                }
                case "purple" -> {
                    lightColor = ChatColor.LIGHT_PURPLE;
                    darkColor = ChatColor.DARK_PURPLE;
                }
                case "orange" -> {
                    lightColor = ChatColor.YELLOW;
                    darkColor = ChatColor.GOLD;
                }
                default -> {
                    return msg(sender, serverLang().getErrorMsg() + "That's not a valid color!");
                }
            }

            startLine = (text.length() <= 30) ? 2 : 1;

            for (int i = 0; i < 4; i++) sign.setLine(i, "");
            sign.setLine(startLine - 1, lightColor + "§l//" + darkColor + "§lTODO");
            if (text.length() > 15) {
                sign.setLine(startLine, text.substring(0, 14));
                sign.setLine(startLine + 2, text.substring(14));
            }
            if (text.length() > 30) {
                sign.setLine(startLine, text.substring(0, 14));
                sign.setLine(startLine + 1, text.substring(14, 29));
                sign.setLine(startLine + 2, text.substring(29));
            }
            if (text.length() <= 15) sign.setLine(startLine, ChatColor.WHITE + ChatColor.stripColor(text));
            sign.update(true);
            return true;
        }
        return msg(sender, getSyntax("sign"));
    }

    private Sign getSign(Player p) {
        Block b = p.getTargetBlock((HashSet<Material>) null, 30);
        if (b.getType() == Material.WALL_SIGN || b.getType() == Material.SIGN_POST) return (Sign) b.getState();
        p.sendMessage(serverLang().getErrorMsg() + "You must be looking at a sign!");
        return null;
    }

    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) return tabOption(args[0], "todo", "setLine");
        if (args.length == 2 && args[0].equalsIgnoreCase("todo")) return tabOption(args[1], "black", "blue", "green", "aqua", "green", "red", "purple", "orange");
        if (args.length == 2 && args[0].equalsIgnoreCase("setLine")) return tabOption(args[1], "1", "2", "3", "4");
        return null;
    }

    public String getSyntax(String label) {
        return switch (label.toLowerCase()) {
            case "sign" -> helpLabel("sign") + helpBr(MODE, true);
            case "setline" -> helpLabel("sign") + helpBr("setline", true) + " " + helpBr("line", true) + " " + helpBr(MSG, true);
            default -> helpLabel("sign") + helpBr("todo", true) + " " + helpBr(MSG, true);
        };
    }

    public List<String> commandNames() {
        return List.of("sign");
    }

}