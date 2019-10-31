package io.github.rookietec9.enderplugin.commands.advFuncs;

import io.github.rookietec9.enderplugin.API.EndExecutor;
import io.github.rookietec9.enderplugin.API.Utils;
import io.github.rookietec9.enderplugin.API.configs.Langs;
import io.github.rookietec9.enderplugin.API.configs.associates.Lang;
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


/**
 * Edits a sign.
 *
 * @author Jeremi
 * @version 13.4.4
 * @since 3.5.2
 */
public class SignCommand implements EndExecutor {

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Lang l = new Lang(Langs.fromSender(sender));
        if (!(sender instanceof Player)) {
            sender.sendMessage(l.getOnlyUserMsg());
            return true;
        }
        if (args.length == 0) {
            sender.sendMessage(this.getSyntax(command, l)[0]);
            return true;
        }
        Player p = (Player) sender;
        if (args[0].equalsIgnoreCase("setLine")) {
            if (args.length <= 2) {
                p.sendMessage(this.getSyntax(command, l)[1]);
                return true;
            }
            int line;
            StringBuilder text = new StringBuilder();
            try {
                line = Integer.valueOf(args[1]) - 1;
            } catch (NumberFormatException e) {
                p.sendMessage(l.getErrorMsg() + "That is not a number.");
                return true;
            }
            if ((line < 0) || (line > 3)) {
                p.sendMessage(l.getErrorMsg() + "The number must be from 1-4.");
                return true;
            }
            for (int i = 2; i < args.length; i++) {
                text.append(args[i]).append(" ");
            }
            text = new StringBuilder(text.substring(0, text.length() - 1));
            if (text.length() > 15) {
                p.sendMessage(l.getErrorMsg() + "Text can only be up to 15 characters long!");
                return true;
            }
            Sign sign = getSign(p);
            if (sign == null) return true;
            sign.setLine(line, ChatColor.translateAlternateColorCodes('&', text.toString()));
            sign.update(true);
            return true;
        }

        if (args[0].equalsIgnoreCase("todo")) {
            if (args.length < 3) {
                sender.sendMessage(this.getSyntax(command, l)[2]);
                return true;
            }
            Sign sign = getSign(p);
            String text = ChatColor.stripColor(StringUtils.join(args, " ", 3, args.length));
            if (sign == null) return true;
            if (text.length() > 45) {
                p.sendMessage(l.getErrorMsg() + "Text can only be up to 45 characters long!");
                return true;
            }
            ChatColor lightColor = null;
            ChatColor darkColor = null;
            int startLine;
            boolean set = false;

            if (args[1].equalsIgnoreCase("GREEN") || args[1].equalsIgnoreCase("AQUA") || args[1].equalsIgnoreCase("RED")
                    || args[1].equalsIgnoreCase("GRAY") || args[1].equalsIgnoreCase("BLUE")) {
                lightColor = ChatColor.valueOf(args[1].toUpperCase());
                darkColor = ChatColor.valueOf("DARK_" + args[1].toUpperCase());
                set = true;
            }
            if (args[1].equalsIgnoreCase("PURPLE")) {
                lightColor = ChatColor.LIGHT_PURPLE;
                darkColor = ChatColor.DARK_PURPLE;
                set = true;
            }
            if (args[1].equalsIgnoreCase("ORANGE")) {
                lightColor = ChatColor.YELLOW;
                darkColor = ChatColor.GOLD;
                set = true;
            }
            if (!set) {
                sender.sendMessage(l.getErrorMsg() + "That's not a valid color!");
                return true;
            }

            if (text.length() <= 30) startLine = 2;
            else startLine = 1;

            if (text.length() > 15) {
                sign.setLine(0, "");
                sign.setLine(1, "");
                sign.setLine(2, "");
                sign.setLine(3, "");
                sign.setLine(startLine - 1, lightColor + "§l//" + darkColor + "§lTODO");
                sign.setLine(startLine, text.substring(0, 14));
                sign.setLine(startLine + 2, text.substring(14));
            }
            if (text.length() > 30) {
                sign.setLine(0, "");
                sign.setLine(1, "");
                sign.setLine(2, "");
                sign.setLine(3, "");
                sign.setLine(startLine - 1, lightColor + "§l//" + darkColor + "§lTODO");
                sign.setLine(startLine, text.substring(0, 14));
                sign.setLine(startLine + 1, text.substring(14, 29));
                sign.setLine(startLine + 2, text.substring(29));
            }
            if (text.length() <= 15) {
                sign.setLine(0, "");
                sign.setLine(1, "");
                sign.setLine(2, "");
                sign.setLine(3, "");
                sign.setLine(startLine - 1, lightColor + "§l//" + darkColor + "§lTODO");
                sign.setLine(startLine, ChatColor.WHITE + ChatColor.stripColor(text));
            }
            sign.update(true);
        } else {
            p.sendMessage(this.getSyntax(command, l));
        }
        return true;
    }

    private Sign getSign(Player p) {
        Lang l = new Lang(Langs.fromSender(p));
        Block b = p.getTargetBlock((HashSet<Material>) null, 30);
        if (b.getType() == Material.WALL_SIGN || b.getType() == Material.SIGN_POST) {
            return (Sign) b.getState();
        }
        p.sendMessage(l.getErrorMsg() + "You must be looking at a sign!");
        return null;
    }

    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return null;
    }

    public String[] getSyntax(Command command, Lang l) {
        return new String[]{
                l.getSyntaxMsg() + l.getCmdExColor() + "/" + l.getLightColor() + command.getName().toLowerCase() + " " + l.getCmdExColor() + Utils.Reference.OPEN_MANDATORY_CHAR +
                        l.getLightColor() + Utils.Reference.MODE + l.getCmdExColor() + Utils.Reference.CLOSE_MANDATORY_CHAR,
                l.getSyntaxMsg() + l.getCmdExColor() + "/" + l.getLightColor() + command.getName().toLowerCase() + " setLine " + l.getCmdExColor() + Utils.Reference.OPEN_MANDATORY_CHAR +
                        l.getLightColor() + "line" + l.getCmdExColor() + Utils.Reference.CLOSE_MANDATORY_CHAR + " " + l.getCmdExColor() + Utils.Reference.OPEN_MANDATORY_CHAR + l.getLightColor() +
                        Utils.Reference.MESSAGE + l.getCmdExColor() + Utils.Reference.CLOSE_MANDATORY_CHAR,
                l.getSyntaxMsg() + l.getCmdExColor() + "/" + l.getLightColor() + command.getName().toLowerCase() + " todo " + l.getCmdExColor() + Utils.Reference.OPEN_MANDATORY_CHAR +
                        l.getLightColor() + "color" + l.getCmdExColor() + Utils.Reference.CLOSE_MANDATORY_CHAR + " " + l.getCmdExColor() + Utils.Reference.OPEN_MANDATORY_CHAR + l.getLightColor() +
                        Utils.Reference.MESSAGE + l.getCmdExColor() + Utils.Reference.CLOSE_MANDATORY_CHAR
        };
    }

    public String commandName() {
        return "sign";
    }
}