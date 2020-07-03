package io.github.rookietec9.enderplugin.utils.datamanagers;

import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Material;
import org.bukkit.block.banner.Pattern;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.*;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

/**
 * Completely re-written Item wrapper
 *
 * @author Jeremi
 * @version 22.4.3
 * @since 1.7.0
 */
public class Item<Type extends ItemMeta> {

    public HashSet<Pair<Enchantment, Integer>> enchantments = new HashSet<>();
    public HashSet<ItemFlag> flags = new HashSet<>();
    public List<String> lore = new ArrayList<>();
    private Material material;
    private String name = null;
    private byte dataByte;
    private int count = 1;
    private short durability;
    private boolean unbreakable;
    private Type specialMeta;

    private Color color;

    //START CONSTRUCTORS
    public Item(Material material) {
        setMaterial(material);
    }

    public Item(Material material, String name) {
        this(material);
        setName(name);
    }

    public Item(Material material, String name, byte dataByte) {
        this(material, name);
        setDataByte(dataByte);
    }

    public Item(Material material, String name, byte dataByte, int count) {
        this(material, name, dataByte);
        setCount(count);
    }

    public Item(Material material, String name, byte dataByte, int count, String... lore) {
        this(material, name, dataByte, count);
        setLore(lore);
    }

    public Item(Material material, String name, byte dataByte, int count, short durability, String... lore) {
        this(material, name, dataByte, count);
        setLore(lore);
        this.durability = durability;
    }

    public Item(Material material, String name, byte dataByte, String... lore) {
        this(material, name, dataByte);
        setLore(lore);
    }

    public Item(Material material, String name, String... lore) {
        this(material, name);
        setLore(lore);
    }
    //END CONSTRUCTORS

    public static Item<?> fromItemStack(ItemStack stack) {
        Item<ItemMeta> item = new Item<>(stack.getType());
        if (stack.getType() == Material.AIR) return item;

        if (stack.getItemMeta().hasDisplayName()) item.setName(stack.getItemMeta().getDisplayName());
        item.setDurability(stack.getDurability());
        item.setUnbreakable(stack.getItemMeta().spigot().isUnbreakable());
        item.setDataByte(stack.getData().getData());
        if (stack.getItemMeta().hasLore()) item.setLore(stack.getItemMeta().getLore());

        HashSet<Pair<Enchantment, Integer>> newEnch = new HashSet<>();
        for (Enchantment enchantment : stack.getItemMeta().getEnchants().keySet()) {
            newEnch.add(new Pair<>(enchantment, stack.getItemMeta().getEnchants().get(enchantment)));
        }
        item.enchantments = newEnch;
        for (ItemFlag flag : stack.getItemMeta().getItemFlags()) item.addFlag(flag);
        item.specialMeta = stack.getItemMeta();
        return item;
    }

    public ItemStack toItemStack() {
        ItemStack itemStack = new ItemStack(material, count, durability, dataByte);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.spigot().setUnbreakable(unbreakable);
        itemMeta.setLore(lore);
        for (Pair<Enchantment, Integer> pair : enchantments) {
            itemMeta.addEnchant(pair.getKey(), pair.getValue(), true);
        }
        if (name != null) itemMeta.setDisplayName(name);

        for (ItemFlag itemFlag : flags) itemMeta.addItemFlags(itemFlag);

        if (specialMeta instanceof BannerMeta) {
            BannerMeta bannerMeta = (BannerMeta) specialMeta;
            ((BannerMeta) itemMeta).setBaseColor(bannerMeta.getBaseColor());
            ((BannerMeta) itemMeta).setBaseColor(bannerMeta.getBaseColor());
            for (Pattern pattern : bannerMeta.getPatterns()) {
                ((BannerMeta) itemMeta).addPattern(pattern);
            }
        }
        if (specialMeta instanceof BlockStateMeta) {
            BlockStateMeta blockStateMeta = (BlockStateMeta) specialMeta;
            if (blockStateMeta.hasBlockState()) ((BlockStateMeta) itemMeta).setBlockState(blockStateMeta.getBlockState());
        }
        if (specialMeta instanceof BookMeta) {
            BookMeta bookMeta = (BookMeta) specialMeta;
            if (bookMeta.hasAuthor()) ((BookMeta) itemMeta).setAuthor(bookMeta.getAuthor());
            if (bookMeta.hasPages()) for (String page : bookMeta.getPages()) ((BookMeta) itemMeta).addPage(page);
            if (bookMeta.hasTitle()) ((BookMeta) itemMeta).setTitle(bookMeta.getTitle());
        }
        if (specialMeta instanceof EnchantmentStorageMeta) {
            var storageMeta = (EnchantmentStorageMeta) specialMeta;
            for (Enchantment enchantment : storageMeta.getStoredEnchants().keySet()) {
                ((EnchantmentStorageMeta) itemMeta).addStoredEnchant(enchantment, storageMeta.getStoredEnchants().get(enchantment), true);
            }
        }
        if (specialMeta instanceof FireworkMeta) {
            var fireworkMeta = (FireworkMeta) specialMeta;
            for (FireworkEffect fireworkEffect : fireworkMeta.getEffects()) ((FireworkMeta) itemMeta).addEffect(fireworkEffect);
        }
        if (specialMeta instanceof FireworkEffectMeta) {
            var fireworkEffectMeta = (FireworkEffectMeta) specialMeta;
            ((FireworkEffectMeta) itemMeta).setEffect(fireworkEffectMeta.getEffect());
        }
        if (specialMeta instanceof LeatherArmorMeta) {
            var leatherArmorMeta = (LeatherArmorMeta) specialMeta;
            ((LeatherArmorMeta) itemMeta).setColor(color != null ? color : leatherArmorMeta.getColor());
        }
        if (specialMeta instanceof MapMeta) {
            var mapMeta = (MapMeta) specialMeta;
            ((MapMeta) itemMeta).setScaling(mapMeta.isScaling());
        }
        if (specialMeta instanceof PotionMeta) {
            var potMeta = (PotionMeta) specialMeta;
            for (PotionEffect effect : potMeta.getCustomEffects()) ((PotionMeta) itemMeta).addCustomEffect(effect, true);
        }
        if (specialMeta instanceof SkullMeta) {
            var skullMeta = (SkullMeta) specialMeta;
            if (skullMeta.getOwner() != null) ((SkullMeta) itemMeta).setOwner(skullMeta.getOwner());
        }
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public ItemStack toPotion(PotionType potType, boolean isSplash) {
        Potion potion = new Potion(potType);
        if (isSplash) potion = potion.splash();

        addFlag(ItemFlag.HIDE_POTION_EFFECTS);
        ItemStack itemStack = toItemStack();
        potion.apply(itemStack);
        return itemStack;
    }

    public ItemStack toSplashPotion(PotionType potType) {
        return toPotion(potType, true);
    }

    //START GETTERS
    public String getName() {
        return name;
    }


    public byte getDataByte() {
        return dataByte;
    }


    public List<String> getLore() {
        return lore;
    }

    public int getCount() {
        return count;
    }
    //END GETTERS


    public boolean isEmpty() {
        return Material.AIR == material || null == toItemStack();
    }

    public Color getColor() {
        return color;
    }

    public boolean isPaintAble() {
        return toItemStack().getItemMeta() instanceof LeatherArmorMeta;
    }

    public Material getType() {
        return material;
    }

    //START SETTERS
    public Item<? extends ItemMeta> setMaterial(Material material) {
        this.material = material;
        ItemStack itemStack = new ItemStack(material);
        if (itemStack.getItemMeta() instanceof BannerMeta || itemStack.getItemMeta() instanceof BlockStateMeta || itemStack.getItemMeta() instanceof BookMeta
                || itemStack.getItemMeta() instanceof EnchantmentStorageMeta || itemStack.getItemMeta() instanceof FireworkMeta || itemStack.getItemMeta() instanceof FireworkEffectMeta ||
                itemStack.getItemMeta() instanceof LeatherArmorMeta || itemStack.getItemMeta() instanceof MapMeta || itemStack.getItemMeta() instanceof PotionMeta || itemStack.getItemMeta() instanceof SkullMeta) specialMeta = (Type) itemStack.getItemMeta();
        return this;
    }

    public Item<? extends ItemMeta> setColor(Color color) {
        this.color = color;
        return this;
    }

    public Item<? extends ItemMeta> setCount(int count) {
        this.count = count;
        return this;
    }

    public Item<? extends ItemMeta> setName(String name) {
        this.name = name;
        return this;
    }

    public Item<? extends ItemMeta> setDataByte(byte dataByte) {
        this.dataByte = dataByte;
        return this;
    }

    public Item<? extends ItemMeta> setLore(String... lore) {
        return setLore(List.of(lore));
    }

    public Item<? extends ItemMeta> setLore(List<String> lore) {
        this.lore = lore;
        return this;
    }

    public Item<? extends ItemMeta> setEnch(List<Pair<Enchantment, Integer>> enchantments) {
        if (enchantments != null) this.enchantments.addAll(enchantments);
        return this;
    }

    public final Item<? extends ItemMeta> setFlags(List<ItemFlag> flags) {
        if (flags != null) this.flags.addAll(flags);
        return this;
    }

    public final Item<?> addEnch(Pair<Enchantment, Integer> enchantment) {
        this.enchantments.add(enchantment);
        return this;
    }

    public final Item<?> addFlag(ItemFlag... flags) {
        if (flags == null) return this;
        this.flags.addAll(Arrays.asList(flags));
        return this;
    }

    public final Item<?> addFlag(ItemFlag flag) {
        if (flags == null) return this;
        this.flags.add(flag);
        return this;
    }

    public final Item<?> removeFlags(ItemFlag... flags) {
        if (flags == null) return this;
        this.flags.removeAll(List.of(flags));
        return this;
    }

    public Item<?> setUnbreakable(boolean unbreakable) {
        this.unbreakable = unbreakable;
        return this;
    }

    public Item<? extends ItemMeta> setColor(int r, int g, int b) {
        return setColor(Color.fromRGB(r, g, b));
    }

    public Item<?> addLore(String... lore) {
        if (lore == null || lore.length == 0) return this;
        if (this.lore == null) this.lore = new ArrayList<>();

        for (String s : lore) if (s != null) this.lore.add(s);
        return this;
    }

    public Item<? extends ItemMeta> setDurability(short durability) {
        this.durability = durability;
        return this;
    }
    //END GETTERS
}