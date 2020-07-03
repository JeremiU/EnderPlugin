package io.github.rookietec9.enderplugin.entities;

import net.minecraft.server.v1_8_R3.EntityInsentient;
import org.bukkit.entity.EntityType;

/**
 * @author Jeremi
 * @version 19.6.3
 * @since 19.6.3
 */
public class MobInfo {

    public EntityType type;

    public String name;

    public Class<? extends EntityInsentient> iClass;

    public MobInfo(EntityType type, String name, Class<? extends EntityInsentient> iClass) {
        this.type = type;
        this.name = name;
        this.iClass = iClass;
    }

    public EntityType getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public Class<? extends EntityInsentient> getIClass() {
        return iClass;
    }
}
