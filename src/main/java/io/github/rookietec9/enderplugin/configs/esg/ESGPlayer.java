package io.github.rookietec9.enderplugin.configs.esg;

import io.github.rookietec9.enderplugin.configs.Config;
import io.github.rookietec9.enderplugin.configs.associates.Associate;
import io.github.rookietec9.enderplugin.EnderPlugin;
import org.bukkit.entity.Player;

/**
 * @author Jeremi
 * @version 21.4.9
 * @since 7.0.2
 */
public class ESGPlayer extends Associate {

    public ESGPlayer(Player player) {
        super(new Config(false, "ESG", player.getUniqueId().toString() + ".yml", EnderPlugin.getInstance()));
    }

    public int getKitLevel(ESGKit.Kits kit) {
        return getInteger("kit." + kit.configName(), new ESGKit(kit).isFree() ? 1 : 0);
    }

    public boolean getKitUnlocked(ESGKit.Kits kit) {
        return getKitLevel(kit) != 0;
    }

    public void setESGKitLevel(int i, ESGKit.Kits kit) {
        set("kit." + kit.configName(), i);
    }
}