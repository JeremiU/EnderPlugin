package io.github.rookietec9.enderplugin.commands.games;

import io.github.rookietec9.enderplugin.API.EndExecutor;
import io.github.rookietec9.enderplugin.API.Utils;
import io.github.rookietec9.enderplugin.API.configs.Config;
import io.github.rookietec9.enderplugin.API.configs.Langs;
import io.github.rookietec9.enderplugin.API.configs.associates.Lang;
import io.github.rookietec9.enderplugin.API.configs.associates.User;
import io.github.rookietec9.enderplugin.EnderPlugin;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Chest;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

/**
 * Teleports a player to the hub or sets it. Can be changed with the config.
 *
 * @author Jeremi
 * @version 16.2.6
 * @since 2.8.5
 */
public class HubCommand implements EndExecutor {
    private final Config hub = new Config(true, "", "hub.yml", EnderPlugin.getInstance());
    private final String[] defaults = {"hub2", "-275", "64", "-58", "-93.45761", "-3.4051764"};
    private final String[] paths = {"world", "x", "y", "z", "yaw", "pitch"};
    private final String[] checks = {hub.getYaml().getString("world"), hub.getYaml().getString("x"), hub.getYaml().getString("y"),
            hub.getYaml().getString("z"), hub.getYaml().getString("yaw"), hub.getYaml().getString("pitch")};

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Lang l = new Lang(Langs.fromSender(sender));
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (args.length == 0) {
                for (int i = 0; i < checks.length; i++) {
                    if (checks[i] == null) {
                        hub.getYaml().createSection(paths[i]);
                        hub.getYaml().set(paths[i], defaults[i]);
                    }
                }
                World w = Bukkit.getWorld(hub.getYaml().getString("world"));
                double x, y, z;
                float yaw, pitch;
                try {
                    x = hub.getYaml().getDouble("x");
                    y = hub.getYaml().getDouble("y");
                    z = hub.getYaml().getDouble("z");
                    yaw = hub.getYaml().getInt("yaw");
                    pitch = hub.getYaml().getInt("pitch");
                } catch (NumberFormatException e) {
                    Bukkit.getLogger().warning("THE HUB CONFIG PATH (CO-ORDS) IS INCORRECT.");
                    sender.sendMessage("The co-ordinates are incorrect for the hub.");
                    return true;
                }
                if (w == null) {
                    Bukkit.getLogger().warning("THE HUB CONFIG PATH (WORLD) IS INCORRECT.");
                    sender.sendMessage("The world is incorrect for the hub.");
                    return true;
                }
                Location loc = new Location(w, x, y, z, yaw, pitch);
                try {
                    p.teleport(loc);
                    sender.sendMessage(l.getPlugMsg() + "You were teleported to the hub.");
                    new User(p).clear();

                    p.setLevel(0);
                    p.setExp(0);
                    p.setTotalExperience(0);

                    ItemStack telly = ((Chest) Bukkit.getWorld(Utils.Reference.Worlds.HUB).getBlockAt(-281, 64, -61).getState()).getBlockInventory().getItem(0);
                    ItemStack paintBall = ((Chest) Bukkit.getWorld(Utils.Reference.Worlds.HUB).getBlockAt(-281, 64, -61).getState()).getBlockInventory().getItem(1);
                    ItemStack togglePVP = ((Chest) Bukkit.getWorld(Utils.Reference.Worlds.HUB).getBlockAt(-281, 64, -61).getState()).getBlockInventory().getItem(2);
                    ItemStack toggleChat = ((Chest) Bukkit.getWorld(Utils.Reference.Worlds.HUB).getBlockAt(-281, 64, -61).getState()).getBlockInventory().getItem(7);
                    ItemStack togglePlayers = ((Chest) Bukkit.getWorld(Utils.Reference.Worlds.HUB).getBlockAt(-281, 64, -61).getState()).getBlockInventory().getItem(8);

                    p.getInventory().setItem(0, telly);
                    p.getInventory().setItem(1, paintBall);
                    p.getInventory().setItem(2, togglePVP);
                    p.getInventory().setItem(7, toggleChat);
                    p.getInventory().setItem(8, togglePlayers);

                    return true;
                } catch (Exception e) {
                    e.printStackTrace();
                    sender.sendMessage(l.getErrorMsg() + "The hub config is incorrect.");
                    return true;
                }
            }
            if ((args.length == 1) && (args[0].equalsIgnoreCase("set"))) {
                hub.getYaml().set("world", p.getWorld().getName());
                hub.getYaml().set("x", p.getLocation().getX());
                hub.getYaml().set("y", p.getLocation().getY());
                hub.getYaml().set("z", p.getLocation().getZ());
                hub.getYaml().set("yaw", p.getLocation().getYaw());
                hub.getYaml().set("pitch", p.getLocation().getPitch());
                hub.modifyYaml();
                sender.sendMessage(l.getPlugMsg() + "Set hub location!");
            }
        } else {
            sender.sendMessage(l.getOnlyUserMsg());
            return true;
        }
        return true;
    }

    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> l = new ArrayList<>();
        if (args.length == 1) {
            l.add("set");
            return l;
        }
        return null;
    }

    public String[] getSyntax(Command command, Lang l) {
        return null;
    }

    public String commandName() {
        return "hub";
    }
}