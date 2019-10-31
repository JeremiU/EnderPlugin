package io.github.rookietec9.enderplugin.API.configs.associates;

import io.github.rookietec9.enderplugin.API.configs.Config;
import io.github.rookietec9.enderplugin.API.configs.Langs;
import io.github.rookietec9.enderplugin.EnderPlugin;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

/**
 * @author Jeremi
 * @version 13.4.4
 * @since ~2.0.0
 */
public class User {

    private final Config playerConfig;
    private final OfflinePlayer player;

    private final String tabName = "fullTabName";
    private final String isMuted = "isMuted";
    private final String isGod = "isGod";
    private final String online = "onlineProfile";

    public User(OfflinePlayer p) {
        this.player = p;
        this.playerConfig = new Config(false, "Players", p.getUniqueId().toString() + ".yml", EnderPlugin.getInstance());
        playerConfig.modifyYaml();

        String lastName = "lastName";

        String[] strings = new String[]{lastName, tabName, isMuted, isGod, online};
        Object[] defaults = new Object[]{p.getName(), p.getName(), false, false, false};

        for (int i = 0; i < strings.length; i++) {
            if (playerConfig.getYaml().get(strings[i]) == null) {
                playerConfig.getYaml().createSection(strings[i]);
                playerConfig.getYaml().set(strings[i], defaults[i]);
            }
        }
        playerConfig.modifyYaml();
    }

    public Player getBase() {
        return (Player) get(player.getPlayer());
    }

    public String getTabName() {
        return (String) get(ChatColor.translateAlternateColorCodes('&', playerConfig.getYaml().getString(tabName)));
    }

    public boolean getGod() {
        return (boolean) get(playerConfig.getYaml().getBoolean(isGod));
    }

    public boolean isMuted() {
        return (boolean) get(playerConfig.getYaml().getBoolean(isMuted));
    }

    public boolean wasOnline() {
        return (boolean) get(playerConfig.getYaml().getBoolean(online));
    }

    public User clear() {
        getBase().getInventory().clear();
        getBase().getInventory().setBoots(new ItemStack(Material.AIR));
        getBase().getInventory().setLeggings(new ItemStack(Material.AIR));
        getBase().getInventory().setChestplate(new ItemStack(Material.AIR));
        getBase().getInventory().setHelmet(new ItemStack(Material.AIR));
        return new User(this.getBase());
    }

    public int clearCount() {
        int count = 0;
        for (ItemStack[] itemStacks : new ItemStack[][]{getBase().getInventory().getContents(), getBase().getInventory().getArmorContents()}) {
            for (ItemStack itemStack : itemStacks) {
                if (itemStack != null) {
                    count += itemStack.getAmount();
                    getBase().getInventory().remove(itemStack);
                    getBase().updateInventory();
                }
            }
        }
        return count;
    }

    public User fromChest(World w, int x, int y, int z) {
        if ((w.getBlockAt(x, y, z).getState() instanceof Chest)) {
            Inventory inv = ((Chest) w.getBlockAt(x, y, z).getState()).getInventory();
            for (ItemStack i : inv.getContents()) {
                if (i != null) {
                    player.getPlayer().getInventory().addItem(i);
                }
            }
            player.getPlayer().updateInventory();
        } else {
            player.getPlayer().sendMessage(new Lang(Langs.fromSender(player.getPlayer())).getErrorMsg() + "the BlockState is not a chest.");
        }
        return new User(this.getBase());
    }

    public User clearEffects() {
        for (PotionEffect potionEffect: getBase().getActivePotionEffects()) getBase().removePotionEffect(potionEffect.getType());
        return new User(this.getBase());
    }

    public User setTabName(String s) {
        set(tabName, s);
        return new User(this.getBase());
    }

    public User setGod(boolean b) {
        set(isGod, b);
        return new User(this.getBase());
    }


    public User setMute(boolean b) {
        set(isMuted, b);
        return new User(this.getBase());
    }

    public User setOnline(boolean b) {
        set(online, b);
        return new User(this.getBase());
    }

    private void set(String path, Object o) {
        playerConfig.modifyYaml();
        playerConfig.getYaml().set(path, o);
        playerConfig.modifyYaml();
    }

    private Object get(Object object) {
        playerConfig.modifyYaml();
        return object;
    }
}