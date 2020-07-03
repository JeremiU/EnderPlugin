package io.github.rookietec9.enderplugin.entities;

import net.minecraft.server.v1_8_R3.EntityCreature;
import net.minecraft.server.v1_8_R3.EntityInsentient;
import org.bukkit.World;
import org.bukkit.entity.EntityType;

/**
 * @author Jeremi
 * @since 19.6.3
 * @version 19.6.3
 */
public interface CustomMob {

    CustomMob spawn(World world);

    EntityCreature creature();
}