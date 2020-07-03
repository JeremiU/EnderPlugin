package io.github.rookietec9.enderplugin.entities;

import net.minecraft.server.v1_8_R3.*;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.util.UnsafeList;
import org.bukkit.entity.EntityType;

import java.lang.reflect.Field;

/**
 * @author Jeremi
 * @version 19.6.3
 * @since 17.8.5
 */
public class SpleefSnowMan extends EntitySnowman implements CustomMob {

    public static MobInfo mobInfo = new MobInfo(EntityType.SNOWMAN, "SPLEEF_SNOW_MAN", SpleefSnowMan.class);

    public SpleefSnowMan(World world) {
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

        this.getAttributeInstance(GenericAttributes.maxHealth).setValue(200);
        this.getAttributeInstance(GenericAttributes.FOLLOW_RANGE).setValue(200);
        this.setHealth(200);
        this.goalSelector.a(1, new PathfinderGoalArrowAttack(this, 1.25, 8, 200f)); //v = ???, i = tick delay?, v1 = range
        this.goalSelector.a(2, new PathfinderGoalRandomStroll(this, 1.0));
        this.goalSelector.a(3, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, 6.0f));
        this.goalSelector.a(4, new PathfinderGoalRandomLookaround(this));
        this.targetSelector.a(1, new PathfinderGoalNearestAttackableTarget<>(this, EntityHuman.class, false, true));
    }

    public CustomMob spawn(World world) {
        return new SpleefSnowMan(world);
    }

    public EntityCreature creature() {
        return this;
    }

}