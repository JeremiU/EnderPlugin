package io.github.rookietec9.enderplugin.API;

import net.minecraft.server.v1_8_R3.TileEntityChest;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.craftbukkit.v1_8_R3.block.CraftChest;

import java.lang.reflect.Field;

/**
 * Used to rename and get name of a chest
 *
 * @author Jeremi
 * @version 13.4.4 // 8.2.2
 * @since 8.2.2
 */
public class ChestExt {

    private final Chest chest;

    public ChestExt(Chest toExt) {
        chest = toExt;
    }

    public String getName() {
        TileEntityChest teChest = null;
        try {
            Field inventoryField = CraftChest.class.getDeclaredField("chest");
            inventoryField.setAccessible(true);
            teChest = ((TileEntityChest) inventoryField.get(chest.getLocation().getBlock().getState()));
        } catch (IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
        }
        assert teChest != null;
        return teChest.getName();
    }


    public ChestExt setName(String name) {
        Location loc = chest.getLocation();
        try {
            loc.getBlock().setType(Material.CHEST);

            Field inventoryField = CraftChest.class.getDeclaredField("chest");
            inventoryField.setAccessible(true);
            TileEntityChest teChest = ((TileEntityChest) inventoryField.get(loc.getBlock().getState()));
            teChest.a(ChatColor.translateAlternateColorCodes('&', name));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ChestExt((Chest) loc.getBlock().getState());
    }

    public Chest getExt() {
        return chest;
    }
}