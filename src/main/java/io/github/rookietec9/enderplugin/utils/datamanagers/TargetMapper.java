package io.github.rookietec9.enderplugin.utils.datamanagers;

import io.github.rookietec9.enderplugin.utils.reference.Teams;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftCreature;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityTargetEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * @author Jeremi
 * @version 22.8.0
 * @since 19.9.9
 */
public class TargetMapper {

    private static final HashMap<LivingEntity, TargetMapper> targetMappers = new HashMap<>();

    private final LivingEntity entity;
    private Player target;
    private final List<Player> owners = new ArrayList<>();
    private Player owner;

    private TargetMapper(LivingEntity entity, Player owner) {
        this.entity = entity;
        this.owner = owner;
        if (owner != null) owners.add(owner);
        if (owner != null && Teams.getTeam(owner) != null) for (String s : Teams.getTeam(owner).getEntries()) if (Bukkit.getPlayer(s) != null) owners.add(Bukkit.getPlayer(s));
    }

    public static TargetMapper getTMP(LivingEntity entity) {
        if (targetMappers.get(entity) == null) targetMappers.put(entity, new TargetMapper(entity, null));
        return targetMappers.get(entity);
    }

    public static TargetMapper fromTarget(Player player) {
        for (LivingEntity livingEntity : targetMappers.keySet()) if (targetMappers.get(livingEntity).getTarget() == player) return targetMappers.get(livingEntity);
        return null;
    }

    public static List<TargetMapper> fullFromTarget(Player player) {
        List<TargetMapper> mappers = new ArrayList<>();
        for (LivingEntity livingEntity : targetMappers.keySet()) if (targetMappers.get(livingEntity).getTarget() == player) mappers.add(targetMappers.get(livingEntity));
        return mappers;
    }

    public TargetMapper setOwners(Player owner) {
        owners.clear();
        this.owner = owner;
        if (owner != null) owners.add(owner);
        else return null;
        if (Teams.getTeam(owner) != null) for (String s : Teams.getTeam(owner).getEntries()) if (Bukkit.getPlayer(s) != null) owners.add(Bukkit.getPlayer(s));
        return this;
    }

    public Player getTarget() {
        return target;
    }

    public TargetMapper setTarget(Player target) {
        ((CraftCreature) entity).getHandle().setGoalTarget(target != null ? ((CraftPlayer) target).getHandle() : null, EntityTargetEvent.TargetReason.CUSTOM, false);
        this.target = target;
        return this;
    }

    public Player closestTarget() {
        HashMap<Player, Double> distMap = new HashMap<>();

        for (Player player : entity.getWorld().getPlayers()) {
            if (!owners.contains(player)) {
                if (player.getGameMode() == GameMode.CREATIVE || player.getGameMode() == GameMode.SPECTATOR) continue;
                distMap.put(player, entity.getLocation().distance(player.getLocation()));
            }
        }

        Player target = null;
        double maxDist = 0;

        for (Player player : distMap.keySet())
            if (maxDist < distMap.get(player)) {
                maxDist = distMap.get(player);
                target = player;
            }

        if (target == null) {
            return null;
        }
        return target;
    }

    public Player getOwner() {
        return owner;
    }

    public static Set<LivingEntity> keySet() {
        return targetMappers.keySet();
    }

    public List<Player> getOwners() {
        return owners;
    }
}