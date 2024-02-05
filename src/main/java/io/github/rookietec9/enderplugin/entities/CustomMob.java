package io.github.rookietec9.enderplugin.entities;

import net.minecraft.server.v1_8_R3.EntityCreature;
import net.minecraft.server.v1_8_R3.EntityInsentient;
import org.bukkit.World;

/**
 * @author Jeremi
 * @since 25.7.6
 * @version 19.6.3
 */
public interface CustomMob {

    CustomMob spawn(World world);

    EntityInsentient mob();
}