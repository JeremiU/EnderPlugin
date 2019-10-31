package io.github.rookietec9.enderplugin.events.games;

import io.github.rookietec9.enderplugin.API.Utils;
import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scoreboard.Scoreboard;

/**
 * @author Jeremi
 * @version 14.5.4
 * @since
 */
public class MurderDeathEvent implements Listener {

    @EventHandler
    public void run(EntityDamageEvent e) {
        if (!(e.getEntity() instanceof Player)) return;
        if (!e.getEntity().getWorld().getName().equalsIgnoreCase(Utils.Reference.Worlds.MURDERER)) return;
        Player dead = (Player) e.getEntity();
        if (dead == null) return;
        if ((dead.getHealth() - e.getDamage()) <= 0) {

            Scoreboard board = Bukkit.getScoreboardManager().getMainScoreboard();
            board.getTeam(Utils.Reference.Teams.goodTeam).removeEntry(dead.getName());
            if (dead.getWorld().getName().equalsIgnoreCase(Utils.Reference.Worlds.MURDERER) && dead.getGameMode().equals(GameMode.ADVENTURE)) {
                for (Player player : Bukkit.getWorld(Utils.Reference.Worlds.MURDERER).getPlayers()) {
                    player.getWorld().playSound(player.getLocation(), Sound.ENDERMAN_SCREAM, 1, 1);
                    player.sendMessage("§f[§4Murderer§f] You hear the screams of a victim. You wonder if you will be next.");
                    //MurderMainBoard.clear(new User(player));
                    return;
                }

                for (Player p : dead.getWorld().getPlayers()) {
                    ChatColor chatColor = null;
                    int ttl = dead.getWorld().getPlayers().size() - Bukkit.getScoreboardManager().getMainScoreboard().getTeam(Utils.Reference.Teams.badTeam).getSize();
                    int left = board.getTeam(Utils.Reference.Teams.goodTeam).getSize();
                    switch (left) {
                        case 1: {
                            chatColor = ChatColor.DARK_RED;
                            break;
                        }
                        case 2: {
                            chatColor = ChatColor.GOLD;
                            break;
                        }
                        case 3: {
                            chatColor = ChatColor.BLUE;
                            break;
                        }
                        case 4: {
                            chatColor = ChatColor.GREEN;
                            break;
                        }
                        case 0: {
                            chatColor = ChatColor.DARK_GRAY;
                        }
                    }
                    if (Bukkit.getScoreboardManager().getMainScoreboard().getTeam(Utils.Reference.Teams.badTeam).hasPlayer(p)) {
                        p.getInventory().clear();
                        ItemStack i = new ItemStack(Material.STONE_SWORD);
                        i.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 100);
                        ItemMeta im = i.getItemMeta();
                        im.setDisplayName(chatColor + "Punisher " + ChatColor.WHITE + left + chatColor + "/" + ChatColor.WHITE + ttl);
                        i.setItemMeta(im);
                        p.getInventory().addItem(i);
                    }
                }

            }
            if (dead.getWorld().getName().equalsIgnoreCase(Utils.Reference.Worlds.MURDERER) && dead.getGameMode().equals(GameMode.ADVENTURE)) {
                board.getTeam(Utils.Reference.Teams.goodTeam).removeEntry(dead.getName());
                for (Player player : dead.getWorld().getPlayers()) {
                    //HoodMainBoard.run(new User(player)); //TODO PROPER CLEAR
                }
            }
        }
    }
}