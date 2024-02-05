package io.github.rookietec9.enderplugin.utils.datamanagers;

import org.apache.commons.lang3.tuple.Pair;
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

import java.util.*;

/**
 * Completely re-written Item wrapper
 *
 * @author Jeremi
 * @version 25.7.3
 * @since 1.7.0
 */
public class ItemWrapper<Type extends ItemMeta> {

    public final HashMap<Enchantment, Integer> enchantments = new HashMap<>();
    public final HashSet<ItemFlag> flags = new HashSet<>();
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
    public ItemWrapper(Material material) {
        setMaterial(material);
    }

    public ItemWrapper(Material material, String name) {
        this(material);
        setName(name);
    }

    public ItemWrapper(Material material, String name, byte dataByte) {
        this(material, name);
        setDataByte(dataByte);
    }

    public ItemWrapper(Material material, String name, byte dataByte, int count) {
        this(material, name, dataByte);
        setCount(count);
    }

    public ItemWrapper(Material material, String name, byte dataByte, int count, String... lore) {
        this(material, name, dataByte, count);
        setLore(lore);
    }

    public ItemWrapper(Material material, String name, byte dataByte, int count, short durability, String... lore) {
        this(material, name, dataByte, count);
        setLore(lore);
        this.durability = durability;
    }

    public ItemWrapper(Material material, String name, byte dataByte, String... lore) {
        this(material, name, dataByte);
        setLore(lore);
    }

    public ItemWrapper(Material material, String name, String... lore) {
        this(material, name);
        setLore(lore);
    }
    //END CONSTRUCTORS

    public static ItemWrapper<?> fromItemStack(ItemStack stack) {
        ItemWrapper<ItemMeta> itemWrapper = new ItemWrapper<>(stack.getType());
        if (stack.getType() == Material.AIR) return itemWrapper;

        if (stack.getItemMeta().hasDisplayName()) itemWrapper.setName(stack.getItemMeta().getDisplayName());
        itemWrapper.setDurability(stack.getDurability());
        itemWrapper.setUnbreakable(stack.getItemMeta().spigot().isUnbreakable());
        itemWrapper.setDataByte(stack.getData().getData());
        if (stack.getItemMeta().hasLore()) itemWrapper.setLore(stack.getItemMeta().getLore());

        itemWrapper.enchantments.putAll(stack.getItemMeta().getEnchants());
        for (ItemFlag flag : stack.getItemMeta().getItemFlags()) itemWrapper.addFlag(flag);
        itemWrapper.specialMeta = stack.getItemMeta();
        return itemWrapper;
    }

    public ItemStack toItemStack() {
        ItemStack itemStack = new ItemStack(material, count, durability, dataByte);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.spigot().setUnbreakable(unbreakable);
        itemMeta.setLore(lore);
        enchantments.forEach((e, i) -> itemMeta.addEnchant(e, i, true));
        if (name != null) itemMeta.setDisplayName(name);

        for (ItemFlag itemFlag : flags) itemMeta.addItemFlags(itemFlag);

        if (specialMeta instanceof BannerMeta bannerMeta) {
            ((BannerMeta) itemMeta).setBaseColor(bannerMeta.getBaseColor());
            ((BannerMeta) itemMeta).setBaseColor(bannerMeta.getBaseColor());
            for (Pattern pattern : bannerMeta.getPatterns()) {
                ((BannerMeta) itemMeta).addPattern(pattern);
            }
        }
        if (specialMeta instanceof BlockStateMeta blockStateMeta) {
            if (blockStateMeta.hasBlockState()) ((BlockStateMeta) itemMeta).setBlockState(blockStateMeta.getBlockState());
        }
        if (specialMeta instanceof BookMeta bookMeta) {
            if (bookMeta.hasAuthor()) ((BookMeta) itemMeta).setAuthor(bookMeta.getAuthor());
            if (bookMeta.hasPages()) for (String page : bookMeta.getPages()) ((BookMeta) itemMeta).addPage(page);
            if (bookMeta.hasTitle()) ((BookMeta) itemMeta).setTitle(bookMeta.getTitle());
        }
        if (specialMeta instanceof EnchantmentStorageMeta storageMeta) {
            for (Enchantment enchantment : storageMeta.getStoredEnchants().keySet()) {
                ((EnchantmentStorageMeta) itemMeta).addStoredEnchant(enchantment, storageMeta.getStoredEnchants().get(enchantment), true);
            }
        }
        if (specialMeta instanceof FireworkMeta fireworkMeta) {
            for (FireworkEffect fireworkEffect : fireworkMeta.getEffects()) ((FireworkMeta) itemMeta).addEffect(fireworkEffect);
        }
        if (specialMeta instanceof FireworkEffectMeta fireworkEffectMeta) {
            ((FireworkEffectMeta) itemMeta).setEffect(fireworkEffectMeta.getEffect());
        }
        if (specialMeta instanceof LeatherArmorMeta leatherArmorMeta) {
            ((LeatherArmorMeta) itemMeta).setColor(color != null ? color : leatherArmorMeta.getColor());
        }
        if (specialMeta instanceof MapMeta mapMeta) {
            ((MapMeta) itemMeta).setScaling(mapMeta.isScaling());
        }
        if (specialMeta instanceof PotionMeta potMeta) {
            for (PotionEffect effect : potMeta.getCustomEffects()) ((PotionMeta) itemMeta).addCustomEffect(effect, true);
        }
        if (specialMeta instanceof SkullMeta skullMeta) {
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

    public ItemWrapper<? extends ItemMeta> setName(String name) {
        this.name = name;
        return this;
    }

    public byte getDataByte() {
        return dataByte;
    }

    public ItemWrapper<? extends ItemMeta> setDataByte(byte dataByte) {
        this.dataByte = dataByte;
        return this;
    }

    public List<String> getLore() {
        return lore;
    }

    //END GETTERS

    public ItemWrapper<? extends ItemMeta> setLore(String... lore) {
        return setLore(List.of(lore));
    }

    public ItemWrapper<? extends ItemMeta> setLore(List<String> lore) {
        this.lore = lore;
        return this;
    }

    public int getCount() {
        return count;
    }

    public ItemWrapper<? extends ItemMeta> setCount(int count) {
        this.count = count;
        return this;
    }

    public boolean isEmpty() {
        return Material.AIR == material || null == toItemStack();
    }

    public Color getColor() {
        return color;
    }

    public ItemWrapper<? extends ItemMeta> setColor(Color color) {
        this.color = color;
        return this;
    }

    public boolean isPaintAble() {
        return toItemStack().getItemMeta() instanceof LeatherArmorMeta;
    }

    public Material getType() {
        return material;
    }

    //START SETTERS
    public ItemWrapper<? extends ItemMeta> setMaterial(Material material) {
        this.material = material;
        ItemStack itemStack = new ItemStack(material);
        if (itemStack.getItemMeta() instanceof BannerMeta || itemStack.getItemMeta() instanceof BlockStateMeta || itemStack.getItemMeta() instanceof BookMeta
                || itemStack.getItemMeta() instanceof EnchantmentStorageMeta || itemStack.getItemMeta() instanceof FireworkMeta || itemStack.getItemMeta() instanceof FireworkEffectMeta ||
                itemStack.getItemMeta() instanceof LeatherArmorMeta || itemStack.getItemMeta() instanceof MapMeta || itemStack.getItemMeta() instanceof PotionMeta || itemStack.getItemMeta() instanceof SkullMeta) specialMeta = (Type) itemStack.getItemMeta();
        return this;
    }

    public final ItemWrapper<? extends ItemMeta> setFlags(List<ItemFlag> flags) {
        if (flags != null) this.flags.addAll(flags);
        return this;
    }

    public final ItemWrapper<?> addEnch(Pair<Enchantment, Integer> enchantment) {
        this.enchantments.put(enchantment.getKey(), enchantment.getValue());
        return this;
    }

    public final ItemWrapper<?> addFlag(ItemFlag... flags) {
        if (flags == null) return this;
        this.flags.addAll(Arrays.asList(flags));
        return this;
    }

    public final ItemWrapper<?> addFlag(ItemFlag flag) {
        this.flags.add(flag);
        return this;
    }

    public final ItemWrapper<?> removeFlags(ItemFlag... flags) {
        if (flags == null) return this;
        List.of(flags).forEach(this.flags :: remove);
        return this;
    }

    public ItemWrapper<?> setUnbreakable(boolean unbreakable) {
        this.unbreakable = unbreakable;
        return this;
    }

    public ItemWrapper<? extends ItemMeta> setColor(int r, int g, int b) {
        return setColor(Color.fromRGB(r, g, b));
    }

    public ItemWrapper<?> addLore(String... lore) {
        if (lore == null || lore.length == 0) return this;
        if (this.lore == null) this.lore = new ArrayList<>();
        else this.lore = new ArrayList<>(this.lore);
        for (String s : lore) if (s != null) this.lore.add(s);
        return this;
    }

    public ItemWrapper<? extends ItemMeta> setDurability(short durability) {
        this.durability = durability;
        return this;
    }
    //END GETTERS
}