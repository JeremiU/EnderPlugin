package io.github.rookietec9.enderplugin.commandgroups;

import io.github.rookietec9.enderplugin.configs.associates.Spawn;
import io.github.rookietec9.enderplugin.utils.datamanagers.DataPlayer;
import io.github.rookietec9.enderplugin.utils.datamanagers.endcommands.EndExecutor;
import io.github.rookietec9.enderplugin.utils.methods.Java;
import io.github.rookietec9.enderplugin.utils.methods.Minecraft;
import net.md_5.bungee.api.ChatColor;
import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static io.github.rookietec9.enderplugin.EnderPlugin.serverLang;

/**
 * @author Jeremi
 * @version 25.7.5
 * @since 25.6.6
 */
public class WorldCommands implements EndExecutor {
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        label = label != null ? label : command.getName();
        String[] toSend = null;
        Player player = (sender instanceof Player) ? (Player) sender : null;

        switch (label.toLowerCase()) {
            case "wsp" -> {
                if (!(sender instanceof Player)) return msg(sender, serverLang().getOnlyUserMsg());

                new Spawn(player.getWorld().getName()).setLocation(player.getLocation());
                toSend = new String[]{serverLang().getPlugMsg() + "Set your world's spawn to your location!"};
            }
            case "wcreate" -> {
                WorldCreator creator = null;
                if (Java.isInRange(args.length, 1, 5)) {
                    if (args.length >= 1) {
                        if (Minecraft.getWorlds().stream().anyMatch(x -> x.equalsIgnoreCase(args[0])))
                            return msg(sender, serverLang().getErrorMsg() + "That is an existing world!");
                        creator = new WorldCreator(args[0]);
                    }
                    if (args.length >= 2) {
                        World.Environment env = null;
                        for (World.Environment environment : World.Environment.values()) if (args[1].equalsIgnoreCase(environment.toString())) env = World.Environment.valueOf(args[1].toUpperCase());
                        if (env == null) return msg(sender, serverLang().getErrorMsg() + "Wrong dimension!");
                        creator.environment(env);
                    }
                    if (args.length >= 3) {
                        WorldType type = null;
                        for (WorldType worldType : WorldType.values()) if (args[2].equalsIgnoreCase(worldType.toString())) type = WorldType.valueOf(args[2].toUpperCase());
                        if (type == null) return msg(sender, serverLang().getErrorMsg() + "Wrong type!");
                        creator.type(type);
                    }
                    if (args.length >= 4) {
                        try {
                            creator.seed(Long.parseLong(args[3]));
                        } catch (NumberFormatException e) {
                            creator.seed(args[1].hashCode());
                        }
                    }
                    if (args.length >= 5 && Java.argWorks(args[4], "on", "true", "false", "off")) {
                        creator.generateStructures(Java.argWorks(args[4], "on", "true"));
                    }
                    sender.sendMessage(serverLang().getPlugMsg() + "Generating world...");
                    Bukkit.createWorld(creator);
                    Minecraft.addWorld(args[0]);
                    return msg(sender,serverLang().getPlugMsg() + "Created a new world named " + args[0] + "!");
                }
                return msg(sender, getSyntax(label));
            }
            case "wdelete" -> {
                if (args.length == 1) {
                    if (!Minecraft.getWorlds().contains(args[0])) return msg(sender, serverLang().getErrorMsg() + "That world doesn't exist!");

                    World w = Bukkit.getWorld(args[0]);
                    if (w != null) {
                        if (w.getPlayers().size() != 0) {
                            for (Player p : w.getPlayers()) {
                                DataPlayer.get(p).hub();
                                p.sendMessage(serverLang().getPlugMsg() + "sent you to the hub because the world you were in has been marked for deletion.");
                            }
                            return msg(sender, serverLang().getErrorMsg() + "had to remove players from world, try again!");
                        }
                        else return msg(sender, Bukkit.unloadWorld(w, true) ? serverLang().getPlugMsg() + "Unloaded world. Run command again to delete." : serverLang().getErrorMsg() + "Could not unload the world!");
                    }
                    File folder = new File(Bukkit.getWorldContainer(), args[0]);
                    if (!folder.exists()) {
                        System.out.println(folder.getAbsolutePath());
                        return msg(sender, serverLang().getErrorMsg() + "Unable to delete the world, folder doesn't exist!");
                    }
                    try {
                        FileUtils.deleteDirectory(folder);
                        Minecraft.removeWorld(args[0]);
                        return msg(sender,serverLang().getPlugMsg() + "Deleted the world.");
                    } catch (IOException e) {
                        e.printStackTrace();
                        return msg(sender, serverLang().getErrorMsg() + "Unable to delete the world due to internal errors.");
                    }
                }
                return msg(sender, getSyntax(label));
            }
            case "wtp" -> {
                if (!(sender instanceof Player)) return msg(sender, serverLang().getOnlyUserMsg());
                if (args.length == 1 || args.length == 2) {
                    Player target = (args.length == 1) ? (Player) sender : Bukkit.getPlayer(args[1]);

                    if (target == null) return msg(sender, serverLang().getOfflineMsg());
                    World w = Bukkit.getWorld(args[0]);
                    if (w == null) return msg(sender, serverLang().getErrorMsg() + "That world doesn't exist!");

                    Spawn spawn = new Spawn(w.getName());
                    player.teleport(spawn.location(), PlayerTeleportEvent.TeleportCause.COMMAND);
                    String name = (target == sender) ? "you " : DataPlayer.getUser(player).getTabName() + serverLang().getTxtColor() + " ";
                    return msg(sender, serverLang().getPlugMsg() + "Sent " + name + "to " + serverLang().getDarkColor() + w.getName());
                }
                return msg(sender, getSyntax(label));
            }
            case "wlist" -> {
                StringBuilder sb = new StringBuilder();
                sb.append(serverLang().getDarkColor()).append("Worlds").append(ChatColor.WHITE).append(": ");
                    for (int i = 0; i < Bukkit.getWorlds().size(); i++) {
                        sb.append(serverLang().getLightColor()).append(Bukkit.getWorlds().get(i));
                        sb.append((i + 1 < Bukkit.getWorlds().size()) ? ChatColor.WHITE + ", " : "");
                    }
                    return msg(sender, sb.toString());
            }
        }
        return toSend == null || msg(sender, toSend);
    }

    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (Java.argWorks(alias, "wcreate", "worldcreate")) {
            if (args.length == 2) return tabOption(args[1], World.Environment.class);
            if (args.length == 3) return tabOption(args[2], WorldType.class);
            if (args.length == 5) return tabOption(args[4], "on", "off");
        }
        if (Java.argWorks(alias, "wtp", "wdelete")) {
            if (args.length == 1) return tabOption(args[0], Minecraft.getWorlds());
        }
        return null;
    }

    public String getSyntax(String label) {
        return helpLabel(label) + switch (label.toLowerCase()) {
            case "wsp", "setworldspawn", "enderworldsp", "wlist" -> "";
            case "wtp", "wdelete" -> helpBr("world", true);
            default -> helpBr("name", true) + " " + helpBr("dimension", false) + " " + helpBr("type", false) + " " + helpBr("seed", false) + " " + helpBr("generateStructures", false);
        };
    }

    public List<String> commandNames() {
        return List.of("wsp", "wcreate", "wtp", "wdelete", "wlist");
    }
}