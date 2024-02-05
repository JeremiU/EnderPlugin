package io.github.rookietec9.enderplugin.entities;

import net.minecraft.server.v1_8_R3.*;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.entity.EntityType;

/**
 * @author Jeremi
 * @version 25.7.6
 */
public class ESGWolf extends EntityWolf implements CustomMob {

    public static final MobInfo mobInfo = new MobInfo(EntityType.WOLF, "ESG_WOLF", ESGWolf.class);

    public ESGWolf(World world) {
        super(((CraftWorld) world).getHandle());

        this.getAttributeInstance(GenericAttributes.maxHealth).setValue(35);
        this.getAttributeInstance(GenericAttributes.FOLLOW_RANGE).setValue(200);
        this.setHealth(35);
    }

    public CustomMob spawn(World world) {
        return new ESGWolf(world);
    }

    public EntityInsentient mob() {
        return this;
    }

}