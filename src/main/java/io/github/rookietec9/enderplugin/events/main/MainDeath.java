package io.github.rookietec9.enderplugin.events.main;

import com.google.common.collect.Multimap;
import io.github.rookietec9.enderplugin.API.Utils;
import io.github.rookietec9.enderplugin.API.configs.Langs;
import io.github.rookietec9.enderplugin.API.configs.WizardsBlades;
import io.github.rookietec9.enderplugin.API.configs.associates.Games;
import io.github.rookietec9.enderplugin.API.configs.associates.Lang;
import io.github.rookietec9.enderplugin.API.configs.associates.User;
import io.github.rookietec9.enderplugin.EnderPlugin;
import io.github.rookietec9.enderplugin.xboards.BootyBoard;
import io.github.rookietec9.enderplugin.xboards.CTFBoard;
import io.github.rookietec9.enderplugin.xboards.WizardsBoard;
import org.bukkit.*;
import org.bukkit.block.Chest;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scoreboard.Scoreboard;

import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static io.github.rookietec9.enderplugin.API.Utils.Reference.Teams;
import static io.github.rookietec9.enderplugin.API.Utils.Reference.Worlds;
import static io.github.rookietec9.enderplugin.EnderPlugin.Hashmaps;


/**
 * @author Jeremi
 * @version 16.3.9
 * @since 9.1.0
 */
public class MainDeath implements Listener {

    private Player killer = null;
    private Location fallLocation = null;

    @EventHandler
    public void runEvent(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player)) return;

        Player dead = (Player) event.getEntity();

        if (event.getEntity().getWorld().getName().equalsIgnoreCase(Worlds.HUB) || new User(dead).getGod()) {
            event.setCancelled(true);
            return;
        }

        Lang l = new Lang(Langs.fromSender(dead));

        Hashmaps.damageList.put(dead, event);
        Hashmaps.damageTimeList.put(event, ManagementFactory.getRuntimeMXBean().getUptime() / 1000);

        if ((dead.getHealth() - event.getFinalDamage()) < 1) {
            fallLocation = dead.getLocation();
            event.setCancelled(true);
            dead.setHealth(20.0D);
            dead.setFoodLevel(20);
            if (!(dead.getTotalExperience() == 0 || (dead.getExp() == 0 && dead.getLevel() == 0))) {
                ExperienceOrb orb = (ExperienceOrb) dead.getWorld().spawnEntity(dead.getLocation(), EntityType.EXPERIENCE_ORB);
                orb.setExperience(dead.getTotalExperience());
            }

            ItemStack telly = ((Chest) Bukkit.getWorld(Worlds.HUB).getBlockAt(-281, 64, -61).getState()).getBlockInventory().getItem(0);
            ItemStack paintBall = ((Chest) Bukkit.getWorld(Worlds.HUB).getBlockAt(-281, 64, -61).getState()).getBlockInventory().getItem(1);
            ItemStack togglePVP = ((Chest) Bukkit.getWorld(Worlds.HUB).getBlockAt(-281, 64, -61).getState()).getBlockInventory().getItem(2);
            ItemStack toggleChat = ((Chest) Bukkit.getWorld(Worlds.HUB).getBlockAt(-281, 64, -61).getState()).getBlockInventory().getItem(7);
            ItemStack togglePlayers = ((Chest) Bukkit.getWorld(Worlds.HUB).getBlockAt(-281, 64, -61).getState()).getBlockInventory().getItem(8);

            ItemStack[] hubItems = {paintBall, togglePlayers, toggleChat, telly, togglePVP};

            dead.setLevel(0);
            dead.setExp(0);
            dead.setTotalExperience(0);

            if (dead.getWorld().getGameRuleValue("keepInventory").equalsIgnoreCase("false")) {
                for (int i = 0; i < dead.getInventory().getContents().length; i++) {
                    if (dead.getInventory().getContents()[i] == null) continue;
                    Item dropItem = dead.getWorld().dropItem(dead.getLocation(), dead.getInventory().getContents()[i]);
                    for (ItemStack hubItem : hubItems) {
                        if (dropItem.getItemStack().getType() == hubItem.getType()) dropItem.remove();
                        if (dropItem.getItemStack().getType() == Material.WOOD_SWORD && dead.getWorld().getName().equalsIgnoreCase(Worlds.CTF))
                            dropItem.remove();
                    }
                    dead.getInventory().remove(dead.getInventory().getContents()[i]);
                    dead.updateInventory();
                }
                if (dead.getInventory().getHelmet() != null)
                    dead.getWorld().dropItem(dead.getLocation(), dead.getInventory().getHelmet());
                if (dead.getInventory().getChestplate() != null && !dead.getWorld().getName().equalsIgnoreCase(Worlds.CTF))
                    dead.getWorld().dropItem(dead.getLocation(), dead.getInventory().getChestplate());
                if (dead.getInventory().getLeggings() != null)
                    dead.getWorld().dropItem(dead.getLocation(), dead.getInventory().getLeggings());
                if (dead.getInventory().getBoots() != null)
                    dead.getWorld().dropItem(dead.getLocation(), dead.getInventory().getBoots());
                new User(dead).clear();
            }

            if (!dead.getWorld().getName().equalsIgnoreCase(Worlds.CTF))
                dead.teleport(dead.getWorld().getSpawnLocation());
            Bukkit.getScheduler().scheduleSyncDelayedTask(EnderPlugin.getInstance(), () -> dead.setFireTicks(-20), 1L);

            for (PotionEffect potionEffect : dead.getActivePotionEffects())
                dead.removePotionEffect(potionEffect.getType());

            if (dead.getWorld().getName().toLowerCase().contains("hub")) {
                new User(dead).fromChest(Bukkit.getWorld(Worlds.HUB), -281, 64, -61);
                dead.getInventory().setItem(0, telly);
                dead.getInventory().setItem(1, paintBall);
                dead.getInventory().setItem(2, togglePVP);
                dead.getInventory().setItem(7, toggleChat);
                dead.getInventory().setItem(8, togglePlayers);
                dead.setCanPickupItems(false);
            } else dead.setCanPickupItems(true);

            Bukkit.broadcastMessage(l.getDarkColor() + dead.getName() + l.getTxtColor() + " died due to " + event.getCause().toString().toUpperCase());
            Bukkit.broadcastMessage(l.getDarkColor() + dead.getName() + l.getTxtColor() + " died due to " + determineDeath(Hashmaps.damageList, dead));

            if (dead.getWorld().getName().equalsIgnoreCase(Worlds.BOOTY) && dead.getGameMode().equals(GameMode.ADVENTURE)) {
                for (LivingEntity entity : dead.getWorld().getLivingEntities()) {
                    if (!(81 > entity.getLocation().getBlockX() && entity.getLocation().getBlockX() > 34)) continue;
                    if (!(-37 > entity.getLocation().getBlockZ() && entity.getLocation().getBlockZ() > -84)) continue;
                    if (entity.getType() == EntityType.GUARDIAN && entity.getLocation().distance(fallLocation) < 30)
                        entity.remove();
                }

                Games.BootyInfo bootyInfo = new Games().new BootyInfo();
                bootyInfo.setDeaths(dead, bootyInfo.deaths(dead) + 1);

                BootyBoard bootyBoard = new BootyBoard(dead);
                bootyBoard.updateTempDeaths(Hashmaps.tempBootyDeaths.get(dead) + 1);
                bootyBoard.reloadKillsAndDeaths();

                if (killer != null) {
                    bootyInfo.setKills(killer, bootyInfo.kills(killer) + 1);
                    bootyBoard = new BootyBoard(killer);
                    bootyBoard.updateTempKills(Hashmaps.tempBootyKills.get(killer) + 1);
                    bootyBoard.reloadKillsAndDeaths();

                }

                Hashmaps.damageList.removeAll(dead);
                Hashmaps.damageTimeList.remove(event);
            }

            if (dead.getWorld().getName().equalsIgnoreCase(Worlds.WIZARDS) && dead.getGameMode().equals(GameMode.ADVENTURE)) {
                Games.WizardsInfo wizardsInfo = new Games().new WizardsInfo();
                wizardsInfo.setDeaths(dead, wizardsInfo.deaths(dead) + 1);
                Hashmaps.tempWizardStreak.put(dead, 0);
                WizardsBoard wizardsBoard;

                Material[] roll = new Material[]{Material.WOOD_PICKAXE, Material.GOLD_PICKAXE, Material.STONE_PICKAXE, Material.IRON_PICKAXE, Material.DIAMOND_PICKAXE};

                if (Hashmaps.tempWizardBlade.get(dead).equalsIgnoreCase("miner")) {
                    for (Material mat : roll) {
                        if (dead.getInventory().contains(mat)) dead.getInventory().remove(mat);
                    }

                    //TODO A BIT SLOPPY?

                    ItemStack sword = new ItemStack(WizardsBlades.MINER.getWeaponMat());
                    ItemMeta meta = sword.getItemMeta();
                    meta.setDisplayName(WizardsBlades.MINER.getChatColor() + WizardsBlades.MINER.getBladeName() + " Blade");

                    List<String> lore = new ArrayList<>();
                    Collections.addAll(lore, WizardsBlades.MINER.getLore());
                    lore.add("");
                    lore.add(WizardsBlades.MINER.getChatColor() + "+" + Utils.BukkitTools.getWeaponDamage(sword) + " §7Attack Damage");
                    meta.setLore(lore);
                    meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
                    meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                    meta.spigot().setUnbreakable(true);
                    meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
                    sword.setItemMeta(meta);
                    dead.getInventory().setHeldItemSlot(0);
                    dead.getInventory().setItemInHand(sword);
                }

                if (killer != null) {
                    wizardsInfo.setKills(killer, wizardsInfo.kills(killer) + 1);
                    wizardsBoard = new WizardsBoard(killer);
                    wizardsBoard.updateTempKills(Hashmaps.tempWizardKills.get(killer) + 1);
                    wizardsBoard.reloadKillsAndDeaths();

                    Hashmaps.tempWizardStreak.put(killer, Hashmaps.tempWizardStreak.get(killer) + 1);

                    if (Hashmaps.tempWizardBlade.get(killer).equalsIgnoreCase("miner") && Hashmaps.tempWizardStreak.get(killer) < 5) {

                        double[] dmgRoll = new double[]{2, 3.25, 4.25, 5.25, 6.25};

                        for (int i = 0; i < roll.length; i++) {
                            if (Hashmaps.tempWizardStreak.get(killer) >= (i + 1) && Hashmaps.tempWizardStreak.get(killer) < (i + 2)) {

                                killer.getInventory().remove(roll[i]);
                                ItemStack pick = WizardsBlades.armorPiece(roll[i + 1], Enchantment.DAMAGE_ALL, 1, null);
                                ItemMeta meta = pick.getItemMeta();
                                meta.setDisplayName(WizardsBlades.MINER.getChatColor() + WizardsBlades.MINER.getBladeName() + " Blade");

                                List<String> lore = new ArrayList<>();
                                if (roll[i] != Material.IRON_PICKAXE)
                                    lore.add("§7Upgrades to §d+" + dmgRoll[i + 2] + "§7 Attack Damage Upon a Kill Streak of §d" + (i + 2));
                                lore.add("");
                                lore.add(WizardsBlades.MINER.getChatColor() + "+" + Utils.BukkitTools.getWeaponDamage(pick) + " §7Attack Damage");
                                meta.setLore(lore);
                                meta.spigot().setUnbreakable(true);
                                meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
                                meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
                                meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                                pick.setItemMeta(meta);

                                killer.getInventory().setHeldItemSlot(0);
                                killer.getInventory().setItemInHand(pick);
                            }
                        }
                    }
                }

                wizardsBoard = new WizardsBoard(dead);
                wizardsBoard.updateTempDeaths(Hashmaps.tempBootyDeaths.get(dead) + 1);
                wizardsBoard.reloadKillsAndDeaths();

                Hashmaps.damageList.removeAll(dead);
                Hashmaps.damageTimeList.remove(event);
            }

            if (dead.getWorld().getName().equalsIgnoreCase(Worlds.CTF) && dead.getGameMode().equals(GameMode.ADVENTURE)) { //RESPAWN
                Scoreboard board = Bukkit.getScoreboardManager().getMainScoreboard();
                ItemStack chestPlate = new ItemStack(Material.LEATHER_CHESTPLATE);
                ItemStack sword = new ItemStack(Material.WOOD_SWORD);
                LeatherArmorMeta lm = (LeatherArmorMeta) chestPlate.getItemMeta();
                ItemMeta itemMeta = sword.getItemMeta();

                if (dead == Hashmaps.ctfBlueHolder && dead.getInventory().contains(Material.BANNER)) {

                }
                if (dead == Hashmaps.ctfRedHolder && dead.getInventory().contains(Material.BANNER)) {

                }


                if (board.getTeam(Teams.blueTeam).hasEntry(dead.getName())) {
                    lm.setColor(Color.NAVY);
                    lm.setDisplayName(ChatColor.BLUE + "Leather Tunic");
                    itemMeta.setDisplayName(ChatColor.BLUE + "Wooden Sword");
                    dead.teleport(new Location(dead.getWorld(), -76.5, 8, 13.5, -90F, 0.719F));
                }
                if (board.getTeam(Teams.redTeam).hasEntry(dead.getName())) {
                    lm.setColor(Color.MAROON);
                    lm.setDisplayName(ChatColor.RED + "Leather Tunic");
                    itemMeta.setDisplayName(ChatColor.RED + "Wooden Sword");
                    dead.teleport(new Location(dead.getWorld(), -19.5, 8, 13.5, -270F, 0.719F));
                }

                if (board.getTeam(Teams.redTeam).hasEntry(dead.getName()) || board.getTeam(Teams.blueTeam).hasEntry(dead.getName())) {
                    sword.setItemMeta(itemMeta);
                    chestPlate.setItemMeta(lm);

                    dead.getInventory().clear();
                    dead.getInventory().removeItem(sword);
                    dead.getInventory().removeItem(chestPlate);

                    dead.getInventory().setChestplate(chestPlate);
                    sword.addUnsafeEnchantment(Enchantment.DURABILITY, 10);
                    dead.getInventory().setHeldItemSlot(0);
                    dead.getInventory().setItemInHand(sword);
                }

                new CTFBoard(dead).updateTempDeaths(Hashmaps.tempCTFDeaths.get(dead) + 1);
                Hashmaps.tempCTFDeaths.put(dead, Hashmaps.tempCTFDeaths.get(dead) + 1);

                if (killer != null) {
                    new CTFBoard(killer).updateTempKills(Hashmaps.tempCTFKills.get(killer) + 1);
                    Hashmaps.tempCTFKills.put(killer, Hashmaps.tempCTFKills.get(killer) + 1);
                }

                Hashmaps.damageList.removeAll(dead);
                Hashmaps.damageTimeList.remove(event);
            }
        }
    }

    private DamageCause determineDeath(Multimap<Player, EntityDamageEvent> map, Player player) {
        Object[] list = map.get(player).toArray();
        DamageCause[] damageCauses = new DamageCause[list.length];
        int caused = 0;

        for (int i = 0; i < list.length; i++) {
            EntityDamageEvent entityDamageEvent = (EntityDamageEvent) list[i];
            EntityDamageEvent oldEvent = null;

            if (i != 0) oldEvent = (EntityDamageEvent) list[i - 1];

            switch (entityDamageEvent.getCause()) {
                case MAGIC: {
                    damageCauses[i] = DamageCause.ENTITY_ATTACK;
                }
                case FALL: {
                    if (i != 0) {
                        if (((EntityDamageEvent) list[i - 1]).getCause() == DamageCause.ENTITY_ATTACK || ((EntityDamageEvent) list[i - 1]).getCause() == DamageCause.PROJECTILE) {
                            if (Hashmaps.damageTimeList.get(entityDamageEvent) - Hashmaps.damageTimeList.get(oldEvent) > 5) {
                                damageCauses[i] = DamageCause.ENTITY_ATTACK;
                            }
                        }
                    }
                    break;
                }

                case DROWNING:
                case FIRE_TICK:
                case LAVA: {
                    if (i != 0) {
                        if (oldEvent.getCause() == DamageCause.ENTITY_ATTACK || (oldEvent).getCause() == DamageCause.PROJECTILE || oldEvent.getCause() == DamageCause.FALL) {
                            if (Hashmaps.damageTimeList.get(entityDamageEvent) - Hashmaps.damageTimeList.get(oldEvent) > 5)
                                damageCauses[i] = DamageCause.ENTITY_ATTACK;
                        }
                    }
                    break;
                }
                case PROJECTILE: {
                    damageCauses[i] = DamageCause.ENTITY_ATTACK;
                    break;
                }
                case ENTITY_ATTACK: {
                    if (entityDamageEvent instanceof EntityDamageByEntityEvent && ((EntityDamageByEntityEvent) entityDamageEvent).getDamager() instanceof Player) {
                        damageCauses[i] = DamageCause.ENTITY_ATTACK;
                        killer = (Player) ((EntityDamageByEntityEvent) entityDamageEvent).getDamager();
                        break;
                    }
                    if (player.getWorld().getName().equalsIgnoreCase(Worlds.BOOTY) || (entityDamageEvent instanceof EntityDamageByEntityEvent && ((EntityDamageByEntityEvent) entityDamageEvent).getDamager() instanceof Guardian)) {
                        damageCauses[i] = DamageCause.ENTITY_ATTACK;
                    }
                }
                default: damageCauses[i] = entityDamageEvent.getCause();
            }
        }

        for (DamageCause damageCause : damageCauses) if (damageCause == DamageCause.ENTITY_ATTACK) caused++;
        System.out.print("+ " + caused);

        return (caused / damageCauses.length > 0.45) ? DamageCause.ENTITY_ATTACK : DamageCause.CUSTOM;
    }
}