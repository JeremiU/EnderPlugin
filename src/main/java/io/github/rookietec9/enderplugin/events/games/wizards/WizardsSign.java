package io.github.rookietec9.enderplugin.events.games.wizards;

import io.github.rookietec9.enderplugin.Inventories;
import io.github.rookietec9.enderplugin.utils.reference.Worlds;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jeremi
 * @version 21.8.4
 * @since 17.2.3
 */
public class WizardsSign implements Listener {

    @EventHandler
    public void run(PlayerInteractEvent event) {
        if (event.getPlayer().getWorld().getName().equalsIgnoreCase(Worlds.WIZARDS) && event.getClickedBlock() != null && event.getClickedBlock().getType() == Material.WALL_SIGN && event.getPlayer().getGameMode() == GameMode.ADVENTURE) {
            Sign sign = (Sign) event.getClickedBlock().getState();

            if (sign.getLine(1).contains("TESTING")) event.getPlayer().teleport(new Location(event.getPlayer().getWorld(), 27, 7, -35, 177.790f, 3.372f), PlayerTeleportEvent.TeleportCause.PLUGIN);
            if (sign.getLine(1).contains("SELECT")) event.getPlayer().openInventory(Inventories.WIZARD_BLADE);
            if (event.getClickedBlock().getLocation().getY() == 8) event.getPlayer().teleport(new Location(event.getPlayer().getWorld(), 27, 11, -42, 178.995f, 10.95f), PlayerTeleportEvent.TeleportCause.PLUGIN);

            if (sign.getLine(1).contains("TELEPORT")) {
                List<Player> players = Bukkit.getWorld(Worlds.WIZARDS).getPlayers();
                ArrayList<Location> teleports = new ArrayList<>();
                teleports.add(new Location(event.getPlayer().getWorld(), 27.5,5,-46.5,179.59f,2.4f));
                teleports.add(new Location(event.getPlayer().getWorld(), 27.5,5,-66.5,359.5f,-1.94f));
                teleports.add(new Location(event.getPlayer().getWorld(), 27.5,1,-46.5,179.59f,0.9f));
                teleports.add(new Location(event.getPlayer().getWorld(), 27.5,1,-66.5,0.34f,0.9f));

                teleports.add(new Location(event.getPlayer().getWorld(), 37.5,1,-54.5,-258.5f,5.2f));
                teleports.add(new Location(event.getPlayer().getWorld(), 17.5,1,-58.5,-99.85f,-0.3f));

                int counter = 0;
                if (teleports.size() >= players.size()) {
                    for (Player player : players) {
                        player.teleport(teleports.get(counter), PlayerTeleportEvent.TeleportCause.PLUGIN);
                        counter++;
                    }
                } else {
                    for (Player player : players) {
                        player.teleport(new Location(player.getWorld(), 27,5,-57,179.211f,10.5f), PlayerTeleportEvent.TeleportCause.PLUGIN);
                        player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 15 * 20, 5));
                        player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 15 * 20, 5));
                    }
                }

                event.getPlayer().teleport(new Location(event.getPlayer().getWorld(), 27.5,5,-66.5,359.5f,-1.94f), PlayerTeleportEvent.TeleportCause.PLUGIN);
            }
        }
    }
}