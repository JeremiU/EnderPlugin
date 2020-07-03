package io.github.rookietec9.enderplugin.events.main;

import io.github.rookietec9.enderplugin.configs.associates.Blades;
import io.github.rookietec9.enderplugin.configs.associates.Spawn;
import io.github.rookietec9.enderplugin.events.games.ctf.CTFJoinEvent;
import io.github.rookietec9.enderplugin.events.games.wizards.WizardsBlades;
import io.github.rookietec9.enderplugin.scoreboards.BootyBoard;
import io.github.rookietec9.enderplugin.scoreboards.CTFBoard;
import io.github.rookietec9.enderplugin.scoreboards.WizardsBoard;
import io.github.rookietec9.enderplugin.utils.datamanagers.DataPlayer;
import io.github.rookietec9.enderplugin.utils.datamanagers.Pair;
import io.github.rookietec9.enderplugin.utils.datamanagers.TargetMapper;
import io.github.rookietec9.enderplugin.utils.methods.Java;
import io.github.rookietec9.enderplugin.utils.methods.Minecraft;
import io.github.rookietec9.enderplugin.utils.reference.DataType;
import io.github.rookietec9.enderplugin.utils.reference.Teams;
import io.github.rookietec9.enderplugin.utils.reference.Worlds;
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
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import static io.github.rookietec9.enderplugin.EnderPlugin.serverLang;

/**
 * @author Jeremi
 * @version 22.8.0
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

    public static void mobCheck(Player dead) {
        for (TargetMapper tmp : TargetMapper.fullFromTarget(dead)) tmp.setTarget(null);
    }

    private static void keepInvCheck(Player dead) {
        final ItemStack paintBall = ((Chest) Bukkit.getWorld(Worlds.HUB).getBlockAt(-281, 64, -61).getState()).getBlockInventory().getItem(1);
        final ItemStack togglePVP = ((Chest) Bukkit.getWorld(Worlds.HUB).getBlockAt(-281, 64, -61).getState()).getBlockInventory().getItem(2);
        final ItemStack toggleChat = ((Chest) Bukkit.getWorld(Worlds.HUB).getBlockAt(-281, 64, -61).getState()).getBlockInventory().getItem(7);
        final ItemStack togglePlayers = ((Chest) Bukkit.getWorld(Worlds.HUB).getBlockAt(-281, 64, -61).getState()).getBlockInventory().getItem(8);
        ItemStack[] hubItems = {paintBall, togglePlayers, toggleChat, togglePVP};

        dead.setLevel(0);
        dead.setExp(0);
        dead.setTotalExperience(0);

        if (dead.getWorld().getName().equalsIgnoreCase(Worlds.CTF)) {
            ItemStack banner = null;

            if (dead.getInventory().contains(Material.BANNER)) {
                for (ItemStack itemStack : dead.getInventory().getContents()) {
                    if (itemStack == null || Material.BANNER != itemStack.getType()) continue;
                    banner = itemStack;
                }
            }
            DataPlayer.getUser(dead).reset();

            if (banner != null) Bukkit.getWorld(Worlds.CTF).dropItem(dead.getLocation(), banner);
        }

        if (dead.getWorld().getGameRuleValue("keepInventory").equalsIgnoreCase("false")) {
            for (int i = 0; i < dead.getInventory().getContents().length; i++) {
                if (dead.getInventory().getContents()[i] == null) continue;
                Item dropItem = dead.getWorld().dropItem(dead.getLocation(), dead.getInventory().getContents()[i]);
                for (ItemStack hubItem : hubItems) {
                    if (dropItem.getItemStack().getType() == hubItem.getType()) dropItem.remove();
                    if (dropItem.getItemStack().getType() == Material.WOOD_SWORD && dead.getWorld().getName().equalsIgnoreCase(Worlds.CTF)) dropItem.remove();
                }
                dead.getInventory().remove(dead.getInventory().getContents()[i]);
                dead.updateInventory();
            }
            if (dead.getInventory().getHelmet() != null) dead.getWorld().dropItem(dead.getLocation(), dead.getInventory().getHelmet());
            if (dead.getInventory().getChestplate() != null) dead.getWorld().dropItem(dead.getLocation(), dead.getInventory().getChestplate());
            if (dead.getInventory().getLeggings() != null) dead.getWorld().dropItem(dead.getLocation(), dead.getInventory().getLeggings());
            if (dead.getInventory().getBoots() != null) dead.getWorld().dropItem(dead.getLocation(), dead.getInventory().getBoots());
            DataPlayer.getUser(dead).clear();
        }
        if (!(dead.getTotalExperience() == 0 || (dead.getExp() == 0 && dead.getLevel() == 0))) {
            ExperienceOrb orb = (ExperienceOrb) dead.getWorld().spawnEntity(dead.getLocation(), EntityType.EXPERIENCE_ORB);
            orb.setExperience(dead.getTotalExperience());
        }

        DataPlayer.getUser(dead).resetNoClear(dead.getGameMode());
        if (!dead.getWorld().getName().equalsIgnoreCase(Worlds.CTF)) dead.teleport(new Spawn(dead.getWorld().getName()).location(), PlayerTeleportEvent.TeleportCause.PLUGIN);
    }

    private static void bootyCheck(Player dead, Player killer) {
        if (dead.getWorld().getName().equalsIgnoreCase(Worlds.BOOTY) && dead.getGameMode().equals(GameMode.ADVENTURE)) {

            DataPlayer.get(dead).increment(DataType.BOOTYDEATHS);
            DataPlayer.get(dead).getBoard(BootyBoard.class).updateTempDeaths(DataPlayer.get(dead).tempBootyDeaths + 1);
            DataPlayer.get(dead).getBoard(BootyBoard.class).reloadKillsAndDeaths();

            if (killer != null) {
                DataPlayer.get(killer).increment(DataType.BOOTYKILLS);
                DataPlayer.get(killer).getBoard(BootyBoard.class).updateTempKills(DataPlayer.get(killer).tempBootyKills + 1);
                DataPlayer.get(killer).getBoard(BootyBoard.class).reloadKillsAndDeaths();
            }
        }
    }

    private static void ctfCheck(Player dead, Player killer) {

        Scoreboard board = Bukkit.getScoreboardManager().getMainScoreboard();

        if (dead.getWorld().getName().equalsIgnoreCase(Worlds.CTF) && dead.getGameMode().equals(GameMode.ADVENTURE) && Java.argWorks(Teams.getTeam(dead).getName(), Teams.blueTeam, Teams.redTeam)) { //RESPAWN

            CTFJoinEvent.giveKit(dead);
            DataPlayer.get(dead).getBoard(CTFBoard.class).updateTempDeaths(DataPlayer.get(dead).tempCTFDeaths + 1);

            if (killer != null) DataPlayer.get(killer).getBoard(CTFBoard.class).updateTempKills(DataPlayer.get(killer).tempCTFKills + 1);
        }
    }

    private static void wizardCheck(Player dead, Player killer) {
        if (dead.getWorld().getName().equalsIgnoreCase(Worlds.WIZARDS) && dead.getGameMode().equals(GameMode.ADVENTURE)) {

            DataPlayer.get(dead).increment(DataType.WIZARDDEATHS);
            DataPlayer.get(dead).getBoard(WizardsBoard.class).updateTempDeaths(DataPlayer.get(dead).tempWizardDeaths + 1);
            DataPlayer.get(dead).getBoard(WizardsBoard.class).reloadKillsAndDeaths();

            DataPlayer.get(dead).tempWizardStreak = 0;

            Material[] roll = new Material[]{Material.WOOD_PICKAXE, Material.GOLD_PICKAXE, Material.STONE_PICKAXE, Material.IRON_PICKAXE, Material.DIAMOND_PICKAXE};

            if (ChatColor.stripColor(DataPlayer.get(dead).tempWizardBlade).equalsIgnoreCase("miner")) {
                for (Material mat : roll) if (dead.getInventory().contains(mat)) dead.getInventory().remove(mat);
                dead.getInventory().setHeldItemSlot(0);
                dead.getInventory().setItemInHand(Blades.miner.getWeapon().toItemStack());
            }

            if (killer != null) {
                DataPlayer.get(killer).increment(DataType.WIZARDKILLS);
                DataPlayer.get(killer).getBoard(WizardsBoard.class).updateTempKills(DataPlayer.get(killer).tempWizardKills + 1);
                DataPlayer.get(killer).getBoard(WizardsBoard.class).reloadKillsAndDeaths();

                DataPlayer.get(killer).tempWizardStreak++;

                if (DataPlayer.get(killer).tempWizardBlade.equalsIgnoreCase("miner") && !killer.getInventory().contains(Material.DIAMOND_PICKAXE)) {
                    double[] dmgRoll = new double[]{2, 3.25, 4.25, 5.25, 6.25};

                    for (int i = 0; i < roll.length; i++) {
                        if (DataPlayer.get(killer).tempWizardStreak >= (i + 1) && DataPlayer.get(killer).tempWizardStreak < (i + 2)) {
                            killer.getInventory().remove(roll[i]);

                            io.github.rookietec9.enderplugin.utils.datamanagers.Item<?> weapon = Blades.miner.getWeapon();
                            weapon.setMaterial(roll[i + 1]);
                            weapon.addEnch(new Pair<>(Enchantment.DAMAGE_ALL, 1));
                            if (weapon.getType() != Material.DIAMOND_PICKAXE) weapon.setLore("§7Upgrades to §d+" + dmgRoll[i + 2] + "§7 Attack Damage Upon a Kill Streak of §d" + (i + 2));
                            weapon.addLore("", Blades.miner.getChatColor() + "+" + Minecraft.getWeaponDamage(weapon.toItemStack()) + " §7Attack Damage");

                            killer.getInventory().setHeldItemSlot(0);
                            killer.getInventory().setItemInHand(weapon.toItemStack());
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void runEvent(EntityDamageEvent event) {
        Player killer = null;

        if (!(event.getEntity() instanceof Player)) return;
        if (event instanceof EntityDamageByEntityEvent) killer = (((EntityDamageByEntityEvent) event).getDamager() instanceof Player) ? (Player) ((EntityDamageByEntityEvent) event).getDamager() : null;

        Player dead = (Player) event.getEntity();

        if (event.getEntity().getWorld().getName().equalsIgnoreCase(Worlds.HUB) && !DataPlayer.get(dead).pvpEnabled || DataPlayer.getUser(dead).getGod()) {
            event.setCancelled(true);
            return;
        }

        if ((dead.getHealth() - event.getFinalDamage()) < 1) {
            if (event instanceof EntityDamageByEntityEvent && ((EntityDamageByEntityEvent) event).getDamager() instanceof Arrow) return;
            event.setCancelled(true);
            fullCheck(dead, killer);
            Minecraft.worldBroadcast(dead.getWorld(), serverLang().getDarkColor() + DataPlayer.getUser(dead).getNickName() + serverLang().getLightColor() + " " +
                    switch (event.getCause()) {
                        case FIRE, LAVA, FIRE_TICK, LIGHTNING -> "burned to death.";
                        case THORNS, WITHER, MAGIC, POISON, MELTING -> "died from magic.";
                        case SUICIDE, VOID, CONTACT, DROWNING, FALL, STARVATION -> "committed suicide.";
                        case SUFFOCATION, CUSTOM -> "???";
                        case BLOCK_EXPLOSION, ENTITY_EXPLOSION, ENTITY_ATTACK, PROJECTILE, FALLING_BLOCK -> "got killed.";
                    });
        }
    }

    @EventHandler
    public void passWizardsKill(EntityDamageEvent event) {
        switch (event.getCause()) {
            case WITHER, FIRE, FALL, FALLING_BLOCK -> {
                if (!(event.getEntity() instanceof Player && (((Player) event.getEntity()).getHealth() - event.getFinalDamage()) < 1)) return;
                if (WizardsBlades.effectedMap.containsKey(event.getEntity())) {
                    fullCheck((Player) event.getEntity(), WizardsBlades.effectedMap.get(event.getEntity()));
                    WizardsBlades.effectedMap.remove(event.getEntity(), WizardsBlades.effectedMap.get(event.getEntity()));
                }
            }
        }
    }
}