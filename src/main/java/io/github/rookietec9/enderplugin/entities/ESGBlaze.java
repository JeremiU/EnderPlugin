package io.github.rookietec9.enderplugin.entities;

import net.minecraft.server.v1_8_R3.*;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.entity.EntityType;

import java.lang.reflect.Field;

/**
 * @author Jeremi
 * @version 25.7.6
 * @since 19.6.3
 */
public class ESGBlaze extends EntityBlaze implements CustomMob {

    public static final MobInfo mobInfo = new MobInfo(EntityType.BLAZE, "ESG_BLAZE", ESGBlaze.class);

    public ESGBlaze(World world) {
        super(((CraftWorld) world).getHandle());

        try {
            Field bField = PathfinderGoalSelector.class.getDeclaredField("b");
            bField.setAccessible(true);
            Field cField = PathfinderGoalSelector.class.getDeclaredField("c");
            cField.setAccessible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }

        this.getAttributeInstance(GenericAttributes.maxHealth).setValue(15);
        this.getAttributeInstance(GenericAttributes.ATTACK_DAMAGE).setValue(8.0);
        this.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).setValue(0.4);
        this.getAttributeInstance(GenericAttributes.FOLLOW_RANGE).setValue(24.0);
        this.setHealth(15);

        this.goalSelector.a(5, new PathfinderGoalMoveTowardsRestriction(this, 1.0D));
        this.goalSelector.a(7, new PathfinderGoalRandomStroll(this, 1.0D));
        this.goalSelector.a(8, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, 8.0F));
        this.goalSelector.a(8, new PathfinderGoalRandomLookaround(this));
        this.targetSelector.a(1, new PathfinderGoalHurtByTarget(this, true));
        this.targetSelector.a(2, new PathfinderGoalNearestAttackableTarget<>(this, EntityHuman.class, true));

        this.goalSelector.a(2, new PathfinderGoalRandomStroll(this, 1.0));
        this.goalSelector.a(3, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, 6.0f));
        this.goalSelector.a(4, new PathfinderGoalRandomLookaround(this));

        this.targetSelector.a(1, new PathfinderGoalHurtByTarget(this, true));
        this.targetSelector.a(2, new PathfinderGoalNearestAttackableTarget<>(this, EntityHuman.class, true));
    }

    public CustomMob spawn(World world) {
        return new ESGBlaze(world);
    }

    public EntityInsentient mob() {
        return this;
    }

}