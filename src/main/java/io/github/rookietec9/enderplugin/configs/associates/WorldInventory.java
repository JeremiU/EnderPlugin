package io.github.rookietec9.enderplugin.configs.associates;

import io.github.rookietec9.enderplugin.EnderPlugin;
import io.github.rookietec9.enderplugin.configs.Config;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.ArrayList;

/**
 * @author Jeremi
 * @version 21.4.9
 * @since 21.2.2
 */
public class WorldInventory extends Associate {
    private final Player player;

    public WorldInventory(Player player) {
        super(new Config(false, "Inventories", player.getUniqueId().toString() + ".yml", EnderPlugin.getInstance()));
        this.player = player;
    }

    public ArrayList<ItemStack> inventoryContents(World world) {
        return (ArrayList<ItemStack>) super.getPath(world.getName() + ".inventory", new ArrayList<ItemStack>());
    }

    public ArrayList<ItemStack> inventoryContents(String worldName) {
        return inventoryContents(Bukkit.getWorld(worldName));
    }

    public ArrayList<ItemStack> armorContents(World world) {
        return (ArrayList<ItemStack>) super.getPath(world.getName() + ".armor", new ArrayList<ItemStack>());
    }

    public ArrayList<ItemStack> armorContents(String worldName) {
        return armorContents(Bukkit.getWorld(worldName));
    }

    public void setInventory(PlayerInventory inventory, World world) {
        set(world.getName() + ".inventory", inventory.getContents());
        set(world.getName() + ".armor", inventory.getArmorContents());
    }

    public void setInventory(PlayerInventory inventory, String worldName) {
        setInventory(inventory, Bukkit.getWorld(worldName));
    }
}