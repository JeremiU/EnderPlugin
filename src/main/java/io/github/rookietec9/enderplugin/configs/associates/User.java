package io.github.rookietec9.enderplugin.configs.associates;

import io.github.rookietec9.enderplugin.EnderPlugin;
import io.github.rookietec9.enderplugin.Inventories;
import io.github.rookietec9.enderplugin.configs.Config;
import io.github.rookietec9.enderplugin.events.hub.BookOpenEvent;
import io.github.rookietec9.enderplugin.utils.datamanagers.DataPlayer;
import io.github.rookietec9.enderplugin.utils.datamanagers.Item;
import io.github.rookietec9.enderplugin.utils.datamanagers.Pair;
import io.github.rookietec9.enderplugin.utils.methods.Minecraft;
import io.github.rookietec9.enderplugin.utils.reference.Worlds;
import org.bukkit.*;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import static io.github.rookietec9.enderplugin.EnderPlugin.serverLang;

/**
 * @author Jeremi
 * @version 22.5.6
 * @since ~2.0.0
 */
public class User extends Associate {

    private final OfflinePlayer player;

    private final Pair<String, Boolean> mute = new Pair<>("isMuted", false);
    private final Pair<String, String> tab;
    private final Pair<String, Boolean> god = new Pair<>("isGod", false);
    private final Pair<String, Boolean> online = new Pair<>("onlineProfile", false);
    private final Pair<String, String> rank = new Pair<>("rank", "§7[§2§lCivilian§7]§f ");

    public User(OfflinePlayer p) {
        super(new Config(false, "Players", p.getUniqueId().toString() + ".yml", EnderPlugin.getInstance()));
        this.player = p;
        tab = new Pair<>("fullTabName", player.getName());
    }

    public Player getBase() {
        return player.getPlayer();
    }

    public String getTabName() {
        return rank() + getNickName();
    }

    public String getNickName() {
        return Minecraft.tacc(getString(tab.getKey(), tab.getValue()));
    }

    public User setTabName(String s) {
        this.getBase().setPlayerListName(rank() + s);

        return (User) set(tab.getKey(), s);
    }

    public boolean getGod() {
        return getBoolean(god.getKey(), false);
    }

    public User setGod(boolean b) {
        return (User) set(god.getKey(), b);
    }

    public User setRank(String rank) {
        this.getBase().setPlayerListName(rank + Minecraft.tacc(getString(tab.getKey(), tab.getValue())));
        return (User) set(this.rank.getKey(), rank);
    }

    public User ghost() {

        int blade = 8, boots = 32, leggings = 33, chestPlate = 34, helmet = 35;

        getBase().addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 100, 0, false, false), true);

        if (null != getBase().getInventory().getHelmet()) {
            getBase().getInventory().setItem(helmet, getBase().getInventory().getHelmet());
            getBase().getInventory().setHelmet(new ItemStack(Material.AIR));
        }
        if (null != getBase().getInventory().getItem(0)) {
            getBase().getInventory().setItem(blade, getBase().getInventory().getItemInHand());
            getBase().getInventory().setItemInHand(new ItemStack(Material.AIR));
        }
        if (null != getBase().getInventory().getChestplate()) {
            getBase().getInventory().setItem(chestPlate, getBase().getInventory().getChestplate());
            getBase().getInventory().setChestplate(new ItemStack(Material.AIR));
        }
        if (null != getBase().getInventory().getLeggings()) {
            getBase().getInventory().setItem(leggings, getBase().getInventory().getLeggings());
            getBase().getInventory().setLeggings(new ItemStack(Material.AIR));
        }
        if (null != getBase().getInventory().getBoots()) {
            getBase().getInventory().setItem(boots, getBase().getInventory().getBoots());
            getBase().getInventory().setBoots(new ItemStack(Material.AIR));
        }

        EnderPlugin.scheduler().runSingleTask(() -> {
            if (null != getBase().getInventory().getItem(blade)) {
                getBase().getInventory().setItem(0, getBase().getInventory().getItem(blade));
                getBase().getInventory().setHeldItemSlot(0);
                getBase().getInventory().setItem(blade, new ItemStack(Material.AIR));
            }
            if (null != getBase().getInventory().getItem(helmet)) {
                getBase().getInventory().setHelmet(getBase().getInventory().getItem(helmet));
                getBase().getInventory().setItem(helmet, new ItemStack(Material.AIR));
            }
            if (null != getBase().getInventory().getItem(chestPlate)) {
                getBase().getInventory().setChestplate(getBase().getInventory().getItem(chestPlate));
                getBase().getInventory().setItem(chestPlate, new ItemStack(Material.AIR));
            }
            if (null != getBase().getInventory().getItem(leggings)) {
                getBase().getInventory().setLeggings(getBase().getInventory().getItem(leggings));
                getBase().getInventory().setItem(leggings, new ItemStack(Material.AIR));
            }
            if (null != getBase().getInventory().getItem(boots)) {
                getBase().getInventory().setBoots(getBase().getInventory().getItem(boots));
                getBase().getInventory().setItem(boots, new ItemStack(Material.AIR));
            }
        }, player.getName().toUpperCase() + "_GHOST", 5);
        return this;
    }

    public User hubNoTp() {
        clear();

        getBase().setLevel(0);
        getBase().setExp(0);
        getBase().setTotalExperience(0);

        ItemStack paintBall = ((Chest) Bukkit.getWorld(Worlds.HUB).getBlockAt(-281, 64, -61).getState()).getBlockInventory().getItem(1);
        ItemStack togglePVP = new Item(Material.BONE, "§7Combat: " + (DataPlayer.get(getBase()).pvpEnabled ? "§aEnabled" : "§cDisabled")).toItemStack();
        ItemStack toggleChat = new Item(Material.INK_SACK, "§7Chat: " + (DataPlayer.get(getBase()).chatEnabled ? "§aEnabled" : "§cDisabled"), DataPlayer.get(getBase()).chatEnabled ? (byte) 10 : (byte) 8, 1).toItemStack();
        ItemStack togglePlayers = new Item(DataPlayer.get(getBase()).playersEnabled ? Material.GREEN_RECORD : Material.RECORD_4, "§7Players: " + (DataPlayer.get(getBase()).playersEnabled ? "§aEnabled" : "§cDisabled")).toItemStack();

        getBase().getInventory().setItem(0, Inventories.TELLY_ITEM);
        getBase().getInventory().setItem(1, paintBall);
        getBase().getInventory().setItem(2, togglePVP);

        getBase().getInventory().setItem(6, BookOpenEvent.book(getBase()));
        getBase().getInventory().setItem(7, toggleChat);
        getBase().getInventory().setItem(8, togglePlayers);

        return this;
    }

    public User hub() {
        getBase().teleport(new Hub().getLoc(), PlayerTeleportEvent.TeleportCause.COMMAND);
        return hubNoTp();
    }

    public boolean isMuted() {
        return getBoolean(mute.getKey(), mute.getValue());
    }

    /**
     * @return whether or not player was on when the server was in online mode
     */
    public boolean wasOnline() {
        return getBoolean(online.getKey(), online.getValue());
    }

    public int clearCount() {
        int count = 0;
        for (ItemStack[] itemStacks : new ItemStack[][]{getBase().getInventory().getContents(), getBase().getInventory().getArmorContents()}) {
            for (ItemStack itemStack : itemStacks) {
                if (null != itemStack) {
                    count += itemStack.getAmount();
                    getBase().getInventory().remove(itemStack);
                    getBase().updateInventory();
                }
            }
        }
        getBase().getInventory().setHelmet(new ItemStack(Material.AIR));
        getBase().getInventory().setChestplate(new ItemStack(Material.AIR));
        getBase().getInventory().setLeggings(new ItemStack(Material.AIR));
        getBase().getInventory().setBoots(new ItemStack(Material.AIR));
        return count;
    }

    public String rank() {
        return Minecraft.tacc(getString(rank.getKey(), rank.getValue()));
    }

    public int rankLevel() {
        if (rank().toLowerCase().contains("owner")) return 5;
        if (rank().toLowerCase().contains("admin")) return 4;
        if (rank().toLowerCase().contains("trusted")) return 3;
        if (rank().toLowerCase().contains("bot")) return 2;
        if (rank().toLowerCase().contains("civ")) return 1;
        return 3;
    }

    public User clear() {
        getBase().getInventory().clear();
        getBase().getInventory().setBoots(new ItemStack(Material.AIR));
        getBase().getInventory().setLeggings(new ItemStack(Material.AIR));
        getBase().getInventory().setChestplate(new ItemStack(Material.AIR));
        getBase().getInventory().setHelmet(new ItemStack(Material.AIR));
        return this;
    }

    public boolean remove(Material mat, int amount, String... name) {
        int currentRemoved = 0;

        for (ItemStack itemStack : getBase().getInventory().getContents()) {

            if (null != itemStack && mat == itemStack.getType()) {
                if (null != name && itemStack.getItemMeta().hasDisplayName() && !itemStack.getItemMeta().getDisplayName().toLowerCase().contains(name[0].toLowerCase())) continue;
                if (itemStack.getAmount() > amount) {
                    itemStack.setAmount(itemStack.getAmount() - amount);
                    getBase().updateInventory();
                    return true;
                }
                if (itemStack.getAmount() == amount) {
                    getBase().getInventory().remove(itemStack);
                    getBase().updateInventory();
                    return true;
                }

                if (getBase().getInventory().contains(mat, amount)) {
                    for (ItemStack itemStack1 : getBase().getInventory().getContents()) {
                        if (null != itemStack1 && mat == itemStack1.getType()) {
                            if (currentRemoved + itemStack1.getAmount() > amount) {
                                itemStack1.setAmount(itemStack1.getAmount() - (amount - currentRemoved));
                                return true;
                            }
                            getBase().getInventory().remove(itemStack1);
                            currentRemoved = currentRemoved + itemStack1.getAmount();
                            getBase().updateInventory();
                            if (currentRemoved == amount) return true;
                        }
                    }
                    return true;
                } else return false;
            }
        }
        return false;
    }

    public User reset() {
        return reset(GameMode.ADVENTURE);
    }

    public User resetNoClear(GameMode gameMode) {
        clearEffects();
        setGod(false);
        getBase().setHealth(20);
        getBase().setFlying(false);
        getBase().setAllowFlight(false);
        getBase().setFireTicks(-20);
        getBase().setHealthScale(20);
        getBase().setFoodLevel(20);
        getBase().setGameMode(gameMode);
        return this;

    }

    public User reset(GameMode gameMode) {
        clear();
        return resetNoClear(gameMode);
    }

    public boolean fromChest(World w, int x, int y, int z) {
        if (w.getBlockAt(x, y, z).getState() instanceof Chest) {
            Inventory inv = ((Chest) w.getBlockAt(x, y, z).getState()).getInventory();
            for (ItemStack i : inv.getContents()) if (null != i) player.getPlayer().getInventory().addItem(i);
            player.getPlayer().updateInventory();
        } else player.getPlayer().sendMessage(serverLang().getErrorMsg() + "the BlockState is not a chest.");
        return (w.getBlockAt(x, y, z).getState() instanceof Chest);
    }

    public boolean fromChestArmor(World w, int x, int y, int z) {
        if (w.getBlockAt(x, y, z).getState() instanceof Chest) {
            Chest chest = ((Chest) w.getBlockAt(x, y, z).getState());
            Inventory inv = chest.getInventory();
            for (int in = 0; in < inv.getContents().length; in++) {
                if (in > 22) break;
                ItemStack i = inv.getItem(in);
                if (null != i) player.getPlayer().getInventory().addItem(i);
            }

            Item<?> helmet = chest.getBlockInventory().getItem(26) != null ? Item.fromItemStack(chest.getBlockInventory().getItem(26)) : null;
            Item<?> chestplate = chest.getBlockInventory().getItem(25) != null ? Item.fromItemStack(chest.getBlockInventory().getItem(25)) : null;
            Item<?> leggings = chest.getBlockInventory().getItem(24) != null ? Item.fromItemStack(chest.getBlockInventory().getItem(24)) : null;
            Item<?> boots = chest.getBlockInventory().getItem(23) != null ? Item.fromItemStack(chest.getBlockInventory().getItem(23)) : null;

            if (helmet != null && !helmet.isEmpty()) player.getPlayer().getInventory().setHelmet(helmet.toItemStack());
            if (chestplate != null && !chestplate.isEmpty()) player.getPlayer().getInventory().setChestplate(chestplate.toItemStack());
            if (leggings != null && !leggings.isEmpty()) player.getPlayer().getInventory().setLeggings(leggings.toItemStack());
            if (boots != null && !boots.isEmpty()) player.getPlayer().getInventory().setBoots(boots.toItemStack());

            player.getPlayer().updateInventory();
        } else player.getPlayer().sendMessage(serverLang().getErrorMsg() + "the BlockState is not a chest.");
        return (w.getBlockAt(x, y, z).getState() instanceof Chest);
    }


    public User clearEffects() {
        for (PotionEffect potionEffect : getBase().getActivePotionEffects()) getBase().removePotionEffect(potionEffect.getType());
        return this;
    }

    public User setMute(boolean b) {
        return (User) set(mute.getKey(), b);
    }

    public User setOnline(boolean b) {
        return (User) set(online.getKey(), b);
    }

}