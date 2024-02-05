package io.github.rookietec9.enderplugin.events.games.spleef;

import io.github.rookietec9.enderplugin.entities.SpleefSnowMan;
import io.github.rookietec9.enderplugin.utils.datamanagers.DataPlayer;
import io.github.rookietec9.enderplugin.utils.datamanagers.ItemWrapper;
import io.github.rookietec9.enderplugin.utils.datamanagers.TargetMapper;
import io.github.rookietec9.enderplugin.utils.methods.Minecraft;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import static io.github.rookietec9.enderplugin.Reference.SPLEEF;


/**
 * @author Jeremi
 * @version 25.2.6
 * @since 17.6.3
 */
public class SpleefCraftEvent implements Listener {

    @EventHandler
    public void run(PlayerInteractEvent event) {
        int BLOCK_COST = 4;
        int MAN_COST = 32;

        if (event.getAction() != Action.RIGHT_CLICK_BLOCK && event.getAction() != Action.RIGHT_CLICK_AIR) return;
        if (event.getPlayer().getWorld().getName().equalsIgnoreCase(SPLEEF) && event.getPlayer().getGameMode() == GameMode.SURVIVAL && event.getItem() != null) {
            if (event.getItem().getType() == Material.STICK && event.getPlayer().getInventory().contains(Material.SNOW_BALL, BLOCK_COST)) {
                event.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 60 * 20, 4, true, true));

                if (DataPlayer.get(event.getPlayer()).remove(Material.SNOW_BALL, BLOCK_COST)) {
                    if (event.getPlayer().getInventory().contains(Material.SNOW_BLOCK)) {
                        for (ItemStack itemStack2 : event.getPlayer().getInventory().getContents()) {
                            if (itemStack2 != null && itemStack2.getType() == Material.SNOW_BLOCK) {
                                itemStack2.setAmount(itemStack2.getAmount() + 1);
                                event.getPlayer().updateInventory();
                                break;
                            }
                        }
                    } else event.getPlayer().getInventory().addItem(new ItemWrapper<>(Material.SNOW_BLOCK).toItemStack());
                }
                event.getPlayer().updateInventory();
            }

            if (event.getItem().getType() == Material.DIAMOND_SPADE && event.getPlayer().getInventory().contains(Material.SNOW_BLOCK, MAN_COST)) {
                event.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 60 * 20, 4, true, true));

                if (DataPlayer.get(event.getPlayer()).remove(Material.SNOW_BLOCK, MAN_COST)) {
                    LivingEntity snowDude = Minecraft.spawn(new SpleefSnowMan(event.getPlayer().getWorld()), event.getPlayer().getLocation());
                    TargetMapper tmp = TargetMapper.getTMP(snowDude);
                    tmp.setOwners(event.getPlayer()).setTarget(tmp.closestTarget());
                }
            }
        }
    }
}