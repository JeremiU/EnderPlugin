package io.github.rookietec9.enderplugin.events.main;

import io.github.rookietec9.enderplugin.API.configs.Config;
import io.github.rookietec9.enderplugin.API.configs.Langs;
import io.github.rookietec9.enderplugin.API.configs.associates.Lang;
import io.github.rookietec9.enderplugin.API.configs.associates.User;
import io.github.rookietec9.enderplugin.EnderPlugin;
import io.github.rookietec9.enderplugin.events.hub.Toggle;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.HashMap;

import static io.github.rookietec9.enderplugin.EnderPlugin.Hashmaps.*;
import static io.github.rookietec9.enderplugin.EnderPlugin.Hashmaps.tempWizardBlade;

/**
 * @author Jeremi
 * @version 14.0.0
 */
public class MainJoinEvent implements Listener {

    private final Config hub = new Config(true,"","hub.yml", EnderPlugin.getInstance());
    private double x = hub.getYaml().getDouble("x");
    private double y = hub.getYaml().getDouble("y");
    private double z = hub.getYaml().getDouble("z");
    private float yaw = hub.getYaml().getInt("yaw");
    private float pitch = hub.getYaml().getInt("pitch");
    private World w = Bukkit.getWorld(hub.getYaml().getString("world"));

    @EventHandler
    public void run(PlayerJoinEvent e) {
        Lang l = new Lang(Langs.fromSender(e.getPlayer()));
        Player player = e.getPlayer();
        User user = new User(player);

        Toggle.toggleMap.put(player.getUniqueId(), 0);

        player.teleport(new Location(w, x, y, z, yaw, pitch));
        player.setFireTicks(-20);
        player.setPlayerListName(user.getTabName());
        player.setCustomName(user.getTabName());
        player.setCustomNameVisible(true);
        player.setLevel(0);
        player.setExp(0);
        player.setTotalExperience(0);
        player.sendTitle(ChatColor.translateAlternateColorCodes('&', EnderPlugin.getInstance().getConfig().getString("Welcome Title").replace("{DARK}",
                l.getDarkColor().getChar() + "").replace("{LIGHT}", l.getLightColor().getChar() + "")), ChatColor.translateAlternateColorCodes(
                '&', EnderPlugin.getInstance().getConfig().getString("Welcome Subtitle").replace("{DARK}", l.getDarkColor().getChar() + "")
                        .replace("{LIGHT}", l.getLightColor().getChar() + "")));

        if (!player.getName().equalsIgnoreCase("Alanu"))
            e.setJoinMessage(l.getDarkColor() + player.getName() + l.getLightColor() + " joined the server.");
        else e.setJoinMessage(l.getDarkColor() + "Failure" + l.getLightColor() + " joined the server.");


        if (Bukkit.getOnlineMode()) user.setOnline(true);
    }
}