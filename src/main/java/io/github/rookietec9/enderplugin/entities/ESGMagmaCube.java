package io.github.rookietec9.enderplugin.entities;

import net.minecraft.server.v1_8_R3.*;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.entity.EntityType;

import java.lang.reflect.Field;

/**
 * @author Jeremi
 * @version 25.7.6
 * @since 25.7.6
 */
public class ESGMagmaCube extends EntityMagmaCube implements CustomMob {

    public static final MobInfo mobInfo = new MobInfo(EntityType.MAGMA_CUBE, "ESG_MAGMA_CUBE", ESGMagmaCube.class);

    public ESGMagmaCube(World world) {
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
    }

    public CustomMob spawn(World world) {
        return new ESGMagmaCube(world);
    }

    public EntityInsentient mob() {
        return this;
    }

    public EntityInsentient creature() {
        return this;
    }
}
