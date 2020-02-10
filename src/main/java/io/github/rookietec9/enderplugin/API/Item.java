package io.github.rookietec9.enderplugin.API;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

/**
 * @author TheEnderCrafter9
 * @version 13.4.4
 * @since 1.7.0
 */
public class Item {
    private ItemStack item;
    private Material material;
    private String[] lore;
    private String name;
    private byte dataByte;
    private int amount;


    /**
     * Creates a new item.
     *
     * @param Material Material of the Item
     * @param Lore     Lore of the Item
     * @param Name     Name to be given
     * @param dataByte Byte to be Item
     * @param Amount   Amount of the Item
     */
    @SuppressWarnings("deprecation")
    public Item(Material Material, String[] Lore, String Name, byte dataByte, int Amount) {
        this.material = Material;
        this.lore = Lore;
        this.name = Name;
        this.dataByte = dataByte;

        ItemStack item = new ItemStack(Material, 1, (short) 0, dataByte);
        this.item = item;
        item.setAmount(Amount);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(Name);
        for (String l : lore) {
            List<String> loreList = new ArrayList<>();
            loreList.add(l);
            itemMeta.setLore(loreList);
        }
        item.setItemMeta(itemMeta);
    }

    /**
     * Creates a new item.
     *
     * @param Material Material of the Item
     * @param Lore     Lore of the Item
     * @param Name     Name to be given
     * @param Amount   Amount of the Item
     */
    public Item(Material Material, String[] Lore, String Name, int Amount) {
        this.material = Material;
        this.lore = Lore;
        this.name = Name;

        ItemStack item = new ItemStack(Material, Amount, (short) 0, dataByte);
        this.item = item;
        item.setAmount(Amount);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(Name);
        for (String l : lore) {
            List<String> loreList = new ArrayList<>();
            loreList.add(l);
            itemMeta.setLore(loreList);
        }
        item.setItemMeta(itemMeta);

    }

    public Item(String name, Material material, byte dataByte) {
        this(name, material);
        this.setByte(dataByte);
    }

    public Item(String name, Material material) {
        this.material = material;
        this.name = name;
        ItemStack item = new ItemStack(material);
        item.setAmount(1);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(name);
        item.setItemMeta(itemMeta);

        this.item = item;
    }
    
    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material Material) {
        item.setType(Material);
        item.setAmount(amount);
        this.material = Material;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(name);
        item.setItemMeta(itemMeta);
    }

    public final Byte getByte() {
        return dataByte;
    }

    public void setByte(byte databyte) {
        this.dataByte = databyte;
        this.item = new ItemStack(material, 1, (short) 0, dataByte);
    }

    public final ItemStack getItem() {
        return item;
    }
}
