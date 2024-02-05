package io.github.rookietec9.enderplugin.entities;

import net.minecraft.server.v1_8_R3.*;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.util.UnsafeList;
import org.bukkit.entity.EntityType;

import java.lang.reflect.Field;

/**
 * @author Jeremi
 * @version 25.7.6
 * @since ?
 */
public class ESGHorse extends EntityHorse implements CustomMob {

    public static final MobInfo mobInfo = new MobInfo(EntityType.HORSE, "ESG_HORSE", ESGHorse.class);

    public ESGHorse(World world) {
        super(((CraftWorld) world).getHandle());

        try {
            Field bField = PathfinderGoalSelector.class.getDeclaredField("b");
            bField.setAccessible(true);
            Field cField = PathfinderGoalSelector.class.getDeclaredField("c");
            cField.setAccessible(true);

            bField.set(goalSelector, new UnsafeList<PathfinderGoalSelector>());
            bField.set(targetSelector, new UnsafeList<PathfinderGoalSelector>());
            cField.set(goalSelector, new UnsafeList<PathfinderGoalSelector>());
            cField.set(targetSelector, new UnsafeList<PathfinderGoalSelector>());
        } catch (Exception e) {
            e.printStackTrace();
        }

        this.goalSelector.a(0, new PathfinderGoalFloat(this));
        this.goalSelector.a(1, new PathfinderGoalTame(this, 1.2D));
        this.goalSelector.a(2, new PathfinderGoalBreed(this, 1.0D));
        this.goalSelector.a(4, new PathfinderGoalFollowParent(this, 1.0D));
        this.goalSelector.a(6, new PathfinderGoalRandomStroll(this, 0.7D));
        this.goalSelector.a(7, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, 6.0F));
        this.goalSelector.a(8, new PathfinderGoalRandomLookaround(this));

        this.getAttributeInstance(GenericAttributes.maxHealth).setValue(15);
        this.getAttributeInstance(GenericAttributes.FOLLOW_RANGE).setValue(128);
        this.setHealth(15);
    }

    public CustomMob spawn(World world) {
        return new ESGHorse(world);
    }

    public EntityInsentient mob() {
        return this;
    }

}