package io.github.rookietec9.enderplugin.entities.targetHandlers;

import io.github.rookietec9.enderplugin.utils.datamanagers.TargetMapper;
import org.bukkit.GameMode;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * @author Jeremi
 * @version 20.8.4
 * @since 19.8.3
 */
public class TargetRedirectEvent implements Listener {
    EntityType type;

    public TargetRedirectEvent(EntityType type) {
        this.type = type;
    }

    @EventHandler
    public void run(EntityTargetEvent event) {

        if (event.getEntity().getType() != type || event.getTarget() == null || !(event.getTarget() instanceof Player) || !(event.getEntity() instanceof LivingEntity)) return;

        if (event.getReason() != EntityTargetEvent.TargetReason.CUSTOM) {
            event.setCancelled(true);
            TargetMapper.getTMP((LivingEntity) event.getEntity()).setTarget(TargetMapper.getTMP((LivingEntity) event.getEntity()).closestTarget());
        } else System.out.println(event.getReason());
        if (event.getTarget() instanceof Player) {
            Player player = (Player) event.getTarget();
            if (player.getGameMode() == GameMode.CREATIVE || player.getGameMode() == GameMode.SPECTATOR) {
                event.setCancelled(true);
                event.setTarget(null);
            }
        }
    }

    @EventHandler
    public void run(PlayerGameModeChangeEvent event) {
        changeTarget(event.getPlayer(), event.getNewGameMode() == GameMode.CREATIVE || event.getNewGameMode() == GameMode.SPECTATOR);
    }

    @EventHandler
    public void run(PlayerKickEvent event) {
        changeTarget(event.getPlayer(), true);
    }

    @EventHandler
    public void run(PlayerChangedWorldEvent event) {
        changeTarget(event.getPlayer(), true);
    }

    @EventHandler
    public void run(PlayerQuitEvent event) {
        changeTarget(event.getPlayer(), true);
    }

    public void changeTarget(Player target, boolean preReq) {
        if (!preReq) return;
        TargetMapper tmp = TargetMapper.fromTarget(target);
        if (tmp != null) tmp.setTarget(null);
    }
}