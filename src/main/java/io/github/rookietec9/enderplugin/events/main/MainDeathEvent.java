package io.github.rookietec9.enderplugin.events.main;

import io.github.rookietec9.enderplugin.EnderPlugin;
import io.github.rookietec9.enderplugin.configs.associates.Spawn;
import io.github.rookietec9.enderplugin.events.games.ctf.CTFJoinEvent;
import io.github.rookietec9.enderplugin.events.games.wizards.WizardsBladesEvent;
import io.github.rookietec9.enderplugin.scoreboards.BootyBoard;
import io.github.rookietec9.enderplugin.scoreboards.CTFBoard;
import io.github.rookietec9.enderplugin.scoreboards.WizardsBoard;
import io.github.rookietec9.enderplugin.utils.datamanagers.Blades;
import io.github.rookietec9.enderplugin.utils.datamanagers.DataPlayer;
import io.github.rookietec9.enderplugin.utils.datamanagers.ItemWrapper;
import io.github.rookietec9.enderplugin.utils.datamanagers.TargetMapper;
import io.github.rookietec9.enderplugin.utils.methods.Java;
import io.github.rookietec9.enderplugin.utils.methods.Minecraft;
import io.github.rookietec9.enderplugin.configs.DataType;
import io.github.rookietec9.enderplugin.utils.methods.Teams;
import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.player.PlayerArmorStandManipulateEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;

import static io.github.rookietec9.enderplugin.EnderPlugin.serverLang;
import static io.github.rookietec9.enderplugin.Reference.*;

/**
 * @author Jeremi
 * @version 25.4.3
 * @since 9.1.0
 */
public class MainDeathEvent implements Listener {

    public static void fullCheck(Player killed, Player killer) {
        keepInvCheck(killed);
        ctfCheck(killed, killer);
        bootyCheck(killed, killer);
        wizardCheck(killed, killer);
        mobCheck(killed);
    }

    public static void mobCheck(Player dead) {
        for (TargetMapper tmp : TargetMapper.fullFromTarget(dead)) tmp.setTarget(null);
    }

    private static void keepInvCheck(Player dead) {
        ItemStack paintBall = ((Chest) Bukkit.getWorld(HUB).getBlockAt(-281, 64, -61).getState()).getBlockInventory().getItem(1);
        ItemStack togglePVP = ((Chest) Bukkit.getWorld(HUB).getBlockAt(-281, 64, -61).getState()).getBlockInventory().getItem(2);
        ItemStack toggleChat = ((Chest) Bukkit.getWorld(HUB).getBlockAt(-281, 64, -61).getState()).getBlockInventory().getItem(7);
        ItemStack togglePlayers = ((Chest) Bukkit.getWorld(HUB).getBlockAt(-281, 64, -61).getState()).getBlockInventory().getItem(8);
        ItemStack[] hubItems = {paintBall, togglePlayers, toggleChat, togglePVP};

        dead.setLevel(0);
        dead.setExp(0);
        dead.setTotalExperience(0);

        if (dead.getWorld().getName().equalsIgnoreCase(CTF)) {
            ItemStack banner = null;

            if (dead.getInventory().contains(Material.BANNER)) {
                for (ItemStack itemStack : dead.getInventory().getContents()) {
                    if (itemStack == null || Material.BANNER != itemStack.getType()) continue;
                    banner = itemStack;
                }
            }
            DataPlayer.get(dead).reset();

            if (banner != null) Bukkit.getWorld(CTF).dropItem(dead.getLocation(), banner);
        }

        if (dead.getWorld().getGameRuleValue("keepInventory").equalsIgnoreCase("false")) {
            for (int i = 0; i < dead.getInventory().getContents().length; i++) {
                if (dead.getInventory().getContents()[i] == null) continue;
                Item dropItem = dead.getWorld().dropItem(dead.getLocation(), dead.getInventory().getContents()[i]);
                for (ItemStack hubItem : hubItems) {
                    if (dropItem.getItemStack().getType() == hubItem.getType()) dropItem.remove();
                    if (dropItem.getItemStack().getType() == Material.WOOD_SWORD && dead.getWorld().getName().equalsIgnoreCase(CTF)) dropItem.remove();
                }
                dead.getInventory().remove(dead.getInventory().getContents()[i]);
                dead.updateInventory();
            }
            if (dead.getInventory().getHelmet() != null) dead.getWorld().dropItem(dead.getLocation(), dead.getInventory().getHelmet());
            if (dead.getInventory().getChestplate() != null) dead.getWorld().dropItem(dead.getLocation(), dead.getInventory().getChestplate());
            if (dead.getInventory().getLeggings() != null) dead.getWorld().dropItem(dead.getLocation(), dead.getInventory().getLeggings());
            if (dead.getInventory().getBoots() != null) dead.getWorld().dropItem(dead.getLocation(), dead.getInventory().getBoots());
            DataPlayer.get(dead).clear();
        }
        if (!(dead.getTotalExperience() == 0 || (dead.getExp() == 0 && dead.getLevel() == 0))) {
            ExperienceOrb orb = (ExperienceOrb) dead.getWorld().spawnEntity(dead.getLocation(), EntityType.EXPERIENCE_ORB);
            orb.setExperience(dead.getTotalExperience());
        }

        DataPlayer.get(dead).resetNoClear(dead.getGameMode());
        if (!dead.getWorld().getName().equalsIgnoreCase(CTF)) dead.teleport(new Spawn(dead.getWorld().getName()).location(), PlayerTeleportEvent.TeleportCause.PLUGIN);
    }

    private static void bootyCheck(Player dead, Player killer) {
        if (dead.getWorld().getName().equalsIgnoreCase(BOOTY) && dead.getGameMode().equals(GameMode.ADVENTURE)) {

            DataPlayer.get(dead).increment(DataType.BOOTYDEATHS);
            DataPlayer.get(dead).tempBootyStreak = 0;
            DataPlayer.get(dead).getBoard(BootyBoard.class).reloadKillsAndDeaths();
            DataPlayer.get(dead).setInGame(false);

            if (killer != null) {
                killer.setHealth(killer.getHealth() <= 18 ? killer.getHealth() + 2 : 20);
                DataPlayer.get(killer).tempBootyStreak++;
                DataPlayer.get(killer).increment(DataType.BOOTYKILLS);
                DataPlayer.get(killer).getBoard(BootyBoard.class).reloadKillsAndDeaths();

                if (DataPlayer.get(killer).tempBootyStreak >= 5 && DataPlayer.get(killer).tempBootyStreak % 5 == 0)
                    Minecraft.worldBroadcast(BOOTY, PREFIX_ALT_BOOTY + DataPlayer.getUser(killer).getNickName() + " is dominating the arena with a kill streak of " + DataPlayer.get(killer).tempBootyStreak + "!");
            }
        }
    }

    private static void ctfCheck(Player dead, Player killer) {

        if (dead.getWorld().getName().equalsIgnoreCase(CTF) && dead.getGameMode().equals(GameMode.ADVENTURE) && Java.argWorks(Teams.getTeam(dead).getName(), Teams.TEAM_BLUE, Teams.TEAM_RED)) { //RESPAWN

            CTFJoinEvent.giveKit(dead);
            DataPlayer.get(dead).getBoard(CTFBoard.class).updateTempDeaths(DataPlayer.get(dead).tempCTFDeaths + 1);

            if (killer != null) DataPlayer.get(killer).getBoard(CTFBoard.class).updateTempKills(DataPlayer.get(killer).tempCTFKills + 1);
        }
    }

    private static void wizardCheck(Player dead, Player killer) {
        if (dead.getWorld().getName().equalsIgnoreCase(WIZARDS) && dead.getGameMode().equals(GameMode.ADVENTURE)) {

            DataPlayer.get(dead).increment(DataType.WIZARDDEATHS);
            DataPlayer.get(dead).tempWizardStreak = 0;
            DataPlayer.get(dead).getBoard(WizardsBoard.class).reloadKillsAndDeaths();
            DataPlayer.get(dead).setInGame(false);

            Material[] roll = new Material[]{Material.WOOD_PICKAXE, Material.GOLD_PICKAXE, Material.STONE_PICKAXE, Material.IRON_PICKAXE, Material.DIAMOND_PICKAXE};

            if (ChatColor.stripColor(DataPlayer.get(dead).tempWizardBlade).equalsIgnoreCase("miner")) {
                for (Material mat : roll) if (dead.getInventory().contains(mat)) dead.getInventory().remove(mat);
                dead.getInventory().setHeldItemSlot(0);
                dead.getInventory().setItemInHand(Blades.miner.getWeapon().toItemStack());
            }

            if (killer != null) {
                killer.setHealth(killer.getHealth() <= 18 ? killer.getHealth() + 2 : 20);
                DataPlayer.get(killer).increment(DataType.WIZARDKILLS);
                DataPlayer.get(killer).tempWizardStreak++;
                DataPlayer.get(killer).getBoard(WizardsBoard.class).reloadKillsAndDeaths();

                if (DataPlayer.get(killer).tempWizardStreak >= 5 && DataPlayer.get(killer).tempWizardStreak % 5 == 0)
                    Minecraft.worldBroadcast(WIZARDS, PREFIX_WZRDS + DataPlayer.getUser(killer).getNickName() + " is dominating the arena with a kill streak of " + DataPlayer.get(killer).tempWizardStreak + "!");

                if (ChatColor.stripColor(DataPlayer.get(killer).tempWizardBlade).equalsIgnoreCase("miner") && !killer.getInventory().contains(Material.DIAMOND_PICKAXE)) {
                    double[] dmgRoll = new double[]{2, 3.25, 4.25, 5.25, 6.25};

                    for (int i = 0; i < roll.length; i++) {
                        if (DataPlayer.get(killer).tempWizardStreak >= (i + 1) && DataPlayer.get(killer).tempWizardStreak < (i + 2)) {
                            killer.getInventory().remove(roll[i]);

                            ItemWrapper<?> weapon = Blades.miner.getWeapon();
                            weapon.setMaterial(roll[i + 1]);
                            weapon.addEnch(Pair.of(Enchantment.DAMAGE_ALL, 1));
                            if (weapon.getType() != Material.DIAMOND_PICKAXE) weapon.setLore("§7Upgrades to §d+" + dmgRoll[i + 2] + "§7 Attack Damage");
                            else weapon.setLore("§7Your pickaxe has been fully upgraded!");
                            weapon.addLore("", Blades.miner.getChatColor() + "+" + Minecraft.getWeaponDamage(weapon.toItemStack()) + " §7Attack Damage");

                            killer.getInventory().setHeldItemSlot(0);
                            killer.getInventory().setItemInHand(weapon.toItemStack());
                        }
                    }
                }
            }
        }
    }

    private static void murderCheck(Player dead, Player killer, EntityDamageEvent event) {
        if (!dead.getWorld().getName().equalsIgnoreCase(MURDER) || killer == null) return;
        event.setCancelled(true);
        ItemStack itemStack = killer.getItemInHand();
        if (itemStack != null && itemStack.getType() == Material.STONE_SWORD && (EnderPlugin.scheduler().isRunning("MURDER_TICK"))) EnderPlugin.murderBase.catchPlayer(dead, killer);
        if (EnderPlugin.scheduler().isRunning("HOOD_TICKER_FINDING") && Teams.contains(Teams.TEAM_NEGATIVE, killer)) {
            System.out.println(".");
            EnderPlugin.hoodBase.catchPlayer(dead, killer);
        }
    }

    @EventHandler
    public void run(HangingBreakByEntityEvent event) {
        if (event.getRemover() instanceof Snowball) event.setCancelled(true);
        if (event.getRemover() instanceof Player) event.setCancelled(((Player) event.getRemover()).getGameMode() != GameMode.CREATIVE);
    }

    @EventHandler
    public void run(PlayerArmorStandManipulateEvent event) {
        event.setCancelled(event.getPlayer().getGameMode() != GameMode.CREATIVE);
    }

    @EventHandler
    public void run(PlayerInteractEvent event) {
        event.setCancelled((event.getClickedBlock() != null && event.getClickedBlock().getType() == Material.BED_BLOCK && event.getPlayer().getGameMode() != GameMode.CREATIVE));
    }

    @EventHandler
    public void runEvent(EntityDamageEvent event) {
        Player killer = null;

        if (!(event.getEntity() instanceof Player)) return;
        if (event instanceof EntityDamageByEntityEvent) killer = (((EntityDamageByEntityEvent) event).getDamager() instanceof Player) ? (Player) ((EntityDamageByEntityEvent) event).getDamager() : null;

        Player dead = (Player) event.getEntity();
        murderCheck(dead, killer, event);

        if (event.getCause() == EntityDamageEvent.DamageCause.FALL && dead.getLastDamageCause() != null && dead.getLastDamageCause().getCause() == EntityDamageEvent.DamageCause.VOID) {
            event.setCancelled(true);
            return;
        }

        if (event.getCause() == EntityDamageEvent.DamageCause.SUFFOCATION && dead.getWorld().getName().equalsIgnoreCase(WIZARDS) && dead.getLocation().getZ() > -47) {
            event.setCancelled(true);
            return;
        }

        if (event.getEntity().getWorld().getName().equalsIgnoreCase(HUB) && !DataPlayer.get(dead).pvpEnabled || DataPlayer.getUser(dead).getGod()) {
            event.setCancelled(true);
            return;
        }

        if (!DataPlayer.get(dead).inGame && Java.argWorks(dead.getWorld().getName(), BOOTY, CTF, MURDER, PARKOUR, OBSTACLE, TNT_RUN, SPLEEF)) {
            event.setCancelled(true);
            return;
        }

        if (event.getEntity().getWorld().getName().equalsIgnoreCase(WIZARDS) && ((Player) event.getEntity()).getGameMode() == GameMode.ADVENTURE && event.getEntity().getLocation().getBlockZ() > -46 && event.getEntity().getLocation().getY() >= 10) {
            event.setCancelled(true);
            return;
        }

        if ((dead.getHealth() - event.getFinalDamage()) < 1) {
            event.setCancelled(true);
            fullCheck(dead, killer);
            String msg = serverLang().getDarkColor() + DataPlayer.getUser(dead).getNickName() + serverLang().getLightColor() + " ";
            switch (event.getCause()) {
                case FIRE, LAVA, FIRE_TICK, LIGHTNING -> msg += "burned to death.";
                case THORNS, WITHER, MAGIC, POISON, MELTING -> msg += "died from magic.";
                case SUICIDE, VOID, CONTACT, DROWNING, FALL, STARVATION, SUFFOCATION -> msg += "committed suicide.";
                case CUSTOM -> msg += "was killed???";
                case PROJECTILE -> {
                    msg += "was shot";
                    if (event instanceof EntityDamageByEntityEvent) {
                        EntityDamageByEntityEvent event1 = (EntityDamageByEntityEvent) event;
                        if (!(event1.getDamager() instanceof Arrow)) return;
                        Arrow arrow = (Arrow) event1.getDamager();
                        if (!(arrow.getShooter() instanceof Player)) return;
                        Player shooter = (Player) arrow.getShooter();
                        msg += " by " + serverLang().getDarkColor() + DataPlayer.getUser(shooter).getNickName();
                    }
                    msg += serverLang().getLightColor() + ".";
                }
                case FALLING_BLOCK -> msg += "was crushed.";
                case BLOCK_EXPLOSION, ENTITY_EXPLOSION -> msg += "blew up.";
                case ENTITY_ATTACK -> {
                    msg += "was killed";
                    if (killer != null) msg += " by " + serverLang().getDarkColor() + DataPlayer.getUser(killer).getNickName();
                    msg += serverLang().getLightColor() + ".";
                }
            }
            Minecraft.worldBroadcast(dead.getWorld(), msg);
        }
    }

    @EventHandler
    public void passWizardsKill(EntityDamageEvent event) {
        switch (event.getCause()) {
            case WITHER, FIRE, FALL, FALLING_BLOCK -> {
                if (!(event.getEntity() instanceof Player && (((Player) event.getEntity()).getHealth() - event.getFinalDamage()) < 1)) return;
                LivingEntity entity = (LivingEntity) event.getEntity();
                if (WizardsBladesEvent.effectedMap.containsKey(entity)) {
                    fullCheck((Player) event.getEntity(), (Player) WizardsBladesEvent.effectedMap.get(entity));
                    WizardsBladesEvent.effectedMap.remove(entity, WizardsBladesEvent.effectedMap.get(entity));
                }
            }
        }
    }
}