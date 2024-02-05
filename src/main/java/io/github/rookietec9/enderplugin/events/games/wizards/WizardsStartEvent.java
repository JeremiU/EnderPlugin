package io.github.rookietec9.enderplugin.events.games.wizards;

import io.github.rookietec9.enderplugin.EnderPlugin;
import io.github.rookietec9.enderplugin.Inventories;
import io.github.rookietec9.enderplugin.commands.advFuncs.HologramCommand;
import io.github.rookietec9.enderplugin.configs.associates.Spawn;
import io.github.rookietec9.enderplugin.scoreboards.Board;
import io.github.rookietec9.enderplugin.utils.datamanagers.DataPlayer;
import io.github.rookietec9.enderplugin.utils.methods.Java;
import io.github.rookietec9.enderplugin.utils.methods.Minecraft;
import org.bukkit.*;
import org.bukkit.block.Sign;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import static io.github.rookietec9.enderplugin.Reference.*;
import static org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

/**
 * @author Jeremi
 * @version 25.7.6
 * @since 17.2.3
 */
public class WizardsStartEvent implements Listener {

    private final HashMap<Player, Boolean> flyingMap = new HashMap<>();

    @EventHandler
    public void run(PlayerInteractEvent event) {
        if (event.getPlayer().getWorld().getName().equalsIgnoreCase(WIZARDS) && event.getClickedBlock() != null && event.getClickedBlock().getType() == Material.WALL_SIGN && event.getPlayer().getGameMode() == GameMode.ADVENTURE) {
            Sign sign = (Sign) event.getClickedBlock().getState();

            if (sign.getLine(1).contains("TESTING")) event.getPlayer().teleport(new Location(event.getPlayer().getWorld(), 27, 7, -35, 177.790f, 3.372f), TeleportCause.SPECTATE);
            if (sign.getLine(1).contains("SELECT")) event.getPlayer().openInventory(Inventories.WIZARD_BLADE);
            if (sign.getLine(1).contains("POWERUPS")) {
                if (sign.getLine(2).contains("ON")) {
                    sign.setLine(2, "§c§lOFF");
                    for (Entity entity : Bukkit.getWorld(WIZARDS).getEntitiesByClasses(ArmorStand.class)) entity.remove();
                    for (Location location : new Location[]{POWERUP_SLOW, POWERUP_BLIND, POWERUP_HEALTH, POWERUP_SPEED}) DataPlayer.blockBelow(location, 1).getBlock().setType(Material.STONE);
                    sign.update();
                    return;
                }
                if (sign.getLine(2).contains("OFF")) {
                    sign.setLine(2, "§b§lON");
                    EnderPlugin.getInstance().runWizardPowerUps();
                    sign.update();
                    return;
                }
            }
            if (event.getClickedBlock().getLocation().getY() == 8) event.getPlayer().teleport(new Location(event.getPlayer().getWorld(), 27, 11, -42, 178.995f, 10.95f), TeleportCause.PLUGIN);
        }
    }

    @EventHandler
    public void run(PlayerMoveEvent event) {
        if (!event.getPlayer().getWorld().getName().equalsIgnoreCase(WIZARDS) || event.getPlayer().getGameMode() != GameMode.ADVENTURE) return;

        if (DataPlayer.get(event.getPlayer()).blockBelow().getBlock().getType() == Material.SLIME_BLOCK || DataPlayer.get(event.getPlayer()).blockBelow(2).getBlock().getType() == Material.SLIME_BLOCK) {
            if (DataPlayer.get(event.getPlayer()).tempWizardBlade.equalsIgnoreCase("NONE")) {
                Location location = event.getPlayer().getLocation().clone();
                location.setY(location.getY() + 2);
                location.setYaw(0);
                event.getPlayer().teleport(location, TeleportCause.PLUGIN);
                Spawn spawn = new Spawn(WIZARDS);
                event.getPlayer().setVelocity(new Vector(spawn.location().getX() * Java.getNegRandom(45, 75), Java.getRandom(10, 15), spawn.location().getZ() * Java.getNegRandom(45, 75)));
                event.getPlayer().setVelocity(spawn.location().getDirection().multiply(-1));
                DataPlayer.get(event.getPlayer()).sendActionMsg(PREFIX_WZRDS + "You need a blade to go into the arena!");
                return;
            }

            if (event.getPlayer().getLocation().getZ() < -46) return;

            Location location = event.getPlayer().getLocation().clone();
            location.setY(location.getY() + 2);
            if (!Java.isInRange(location.getYaw(), 125, 250)) location.setYaw(180);
            location.setPitch(Java.getRandom(-12, 12));
            event.getPlayer().teleport(location, TeleportCause.PLUGIN);
            event.getPlayer().setVelocity(new Vector(event.getPlayer().getLocation().getX() * Java.getNegRandom(45, 75), Java.getRandom(10, 15), event.getPlayer().getLocation().getZ() * Java.getNegRandom(45, 75)));
            event.getPlayer().setVelocity(event.getPlayer().getLocation().getDirection().multiply(1 + (Java.getRandom(10, 20) - 10) / 20.0));
            event.getPlayer().setAllowFlight(true);
            flyingMap.put(event.getPlayer(), true);
            DataPlayer.get(event.getPlayer()).setInGame(true);
        }
        if (flyingMap.getOrDefault(event.getPlayer(), false) && event.getPlayer().getLocation().getBlockZ() < -46 && DataPlayer.get(event.getPlayer()).blockBelow().getBlock().getType() != Material.SLIME_BLOCK && event.getPlayer().getPlayer().getLocation().getBlock().getType() != Material.AIR) {
            event.getPlayer().setAllowFlight(false);
            flyingMap.put(event.getPlayer(), false);
        }
    }

    @EventHandler
    public void preventFly(PlayerToggleFlightEvent event) {
        if (!event.getPlayer().getWorld().getName().equalsIgnoreCase(WIZARDS) || event.getPlayer().getGameMode() != GameMode.ADVENTURE) return;
        if (event.getPlayer().getLastDamageCause() != null && event.getPlayer().getLastDamageCause().getCause() == EntityDamageEvent.DamageCause.FALL) event.getPlayer().getLastDamageCause().setCancelled(true);
        event.setCancelled(true);
    }

    @EventHandler
    public void runE(PlayerMoveEvent e) {
        if (!e.getPlayer().getWorld().getName().equalsIgnoreCase(WIZARDS) || e.getPlayer().getGameMode() != GameMode.ADVENTURE) return;

        AtomicReference<Location> location = new AtomicReference<>();
        AtomicReference<Material> material =  new AtomicReference<>();
        AtomicInteger counter = new AtomicInteger();

        switch (DataPlayer.get(e.getPlayer()).blockBelow().getBlock().getType()) {
            case PACKED_ICE -> {
                Bukkit.getWorld(WIZARDS).getPlayers().stream().filter(x -> !(x.getLocation().getZ() > -46.3)).forEach(x -> {
                    x.sendMessage(PREFIX_WZRDS + DataPlayer.getUser(e.getPlayer()).getNickName() + " picked up the slowness debuff!");
                    if (!x.equals(e.getPlayer())) x.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 15 * 20, 0));
                });
                counter.set(DEBUFF_COOLDOWN);
                location.set(POWERUP_SLOW);
            }
            case COAL_BLOCK -> {
                Bukkit.getWorld(WIZARDS).getPlayers().stream().filter(x -> !(x.getLocation().getZ() > -46.3)).forEach(x -> {
                    x.sendMessage(PREFIX_WZRDS + DataPlayer.getUser(e.getPlayer()).getNickName() + " picked up the blindness debuff!");
                    if (!x.equals(e.getPlayer())) x.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 15 * 20, 0));
                });
                counter.set(DEBUFF_COOLDOWN);
                location.set(POWERUP_BLIND);
            }
            case DIAMOND_BLOCK -> {
                Minecraft.worldBroadcast(WIZARDS, PREFIX_WZRDS + DataPlayer.getUser(e.getPlayer()).getNickName() + " picked up the speed buff!");
                e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 30 * 20, 1));
                counter.set(BUFF_COOLDOWN);
                location.set(POWERUP_SPEED);
            }
            case GOLD_BLOCK -> {
                Minecraft.worldBroadcast(WIZARDS, PREFIX_WZRDS + DataPlayer.getUser(e.getPlayer()).getNickName() + " picked up the health buff!");
                e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, 30 * 20, 2));
                counter.set(BUFF_COOLDOWN);
                location.set(POWERUP_HEALTH);
            }
            default -> {
                return;
            }
        }
        Material m = DataPlayer.get(e.getPlayer()).blockBelow().getBlock().getType();
        material.set(m);
        int timer = counter.get();

        DataPlayer.get(e.getPlayer()).blockBelow().getBlock().setType(Material.STONE);

        for (Entity entity : Bukkit.getWorld(WIZARDS).getNearbyEntities(location.get(), 5, 5, 5)) if (entity instanceof ArmorStand) entity.remove();
        ArmorStand header = HologramCommand.spawnHologram("", false, location.get());
        EnderPlugin.scheduler().runRepeatingTask(() -> header.setCustomName(Board.formatTime(counter.getAndDecrement(), ChatColor.RED, ChatColor.WHITE)), "POWERUP_COUNTDOWN_" + m, 0, 1, timer, PREFIX_WZRDS);

        EnderPlugin.scheduler().runSingleTask(() -> {
            for (Entity entity : Bukkit.getWorld(WIZARDS).getNearbyEntities(location.get(), 5, 5, 5)) if (entity instanceof ArmorStand) entity.remove();

            switch (m) {
                case PACKED_ICE -> {
                    HologramCommand.spawnHologram(SLOW_HEADER, false, POWERUP_SLOW);
                    HologramCommand.spawnHologram(SLOW_TEXT, true, POWERUP_SLOW);
                    Minecraft.worldBroadcast(WIZARDS, PREFIX_WZRDS + "Slowness debuff respawned.");
                }
                case COAL_BLOCK -> {
                    HologramCommand.spawnHologram(BLIND_HEADER, false, POWERUP_BLIND);
                    HologramCommand.spawnHologram(BLIND_TEXT, true, POWERUP_BLIND);
                    Minecraft.worldBroadcast(WIZARDS, PREFIX_WZRDS + "Blindness debuff respawned.");
                }
                case DIAMOND_BLOCK -> {
                    HologramCommand.spawnHologram(SPEED_HEADER, false, POWERUP_SPEED);
                    HologramCommand.spawnHologram(SPEED_TEXT, true, POWERUP_SPEED);
                    Minecraft.worldBroadcast(WIZARDS, PREFIX_WZRDS + "Speed buff respawned.");
                }
                case GOLD_BLOCK -> {
                    HologramCommand.spawnHologram(HEALTH_HEADER, false, POWERUP_HEALTH);
                    HologramCommand.spawnHologram(HEALTH_TEXT, true, POWERUP_HEALTH);
                    Minecraft.worldBroadcast(WIZARDS, PREFIX_WZRDS + "Health buff respawned.");
                }
            }
            DataPlayer.blockBelow(location.get(),1).getBlock().setType(m);
        }, "POWERUP_RESPAWN_" + material.get(), timer, PREFIX_WZRDS);
    }
}