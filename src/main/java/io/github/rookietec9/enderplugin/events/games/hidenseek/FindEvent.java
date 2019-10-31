package io.github.rookietec9.enderplugin.events.games.hidenseek;

import io.github.rookietec9.enderplugin.API.ActionBar;
import io.github.rookietec9.enderplugin.API.Utils;
import io.github.rookietec9.enderplugin.xboards.HideBoard;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.Scoreboard;

/**
 * @author Jeremi
 * @version 16.4.9
 * @since 16.4.2
 */
public class FindEvent implements Listener {

    @EventHandler
    public void run(PlayerMoveEvent event) {
        Player player = event.getPlayer();

        if (!player.getWorld().getName().equalsIgnoreCase(Utils.Reference.Worlds.HIDENSEEK)) return;
        if (!Bukkit.getScoreboardManager().getMainScoreboard().getTeam(Utils.Reference.Teams.badTeam).hasEntry(player.getName()))
            return;

        for (Entity entity : event.getPlayer().getNearbyEntities(50, 25, 50)) {
            if (entity instanceof Player) {
                Player hiding = (Player) entity;
                hiding.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 10 * 20, 1, true, true));
                ActionBar.send("§4§lTHE SEEKER IS NEAR YOU!", hiding);
            }
        }
    }

    @EventHandler
    public void run(PlayerInteractAtEntityEvent event) {
        Player player = event.getPlayer();
        HideBoard seekerBoard = new HideBoard(player);

        Scoreboard board = Bukkit.getScoreboardManager().getMainScoreboard();
        if (!player.getWorld().getName().equalsIgnoreCase(Utils.Reference.Worlds.HIDENSEEK)) return;
        if (!board.getTeam(Utils.Reference.Teams.badTeam).hasEntry(player.getName()))
            return;

        if (event.getRightClicked() instanceof Player) {
            Player hiding = (Player) event.getRightClicked();
            HideBoard hidingBoard = new HideBoard(hiding);

            HideBoard[] hideBoards = new HideBoard[]{seekerBoard, hidingBoard};

            if (board.getTeam(Utils.Reference.Teams.goodTeam).hasEntry(hiding.getName())) {
                if (board.getTeam(Utils.Reference.Teams.goodTeam).getEntries().size() != 1) {
                    for (String s : board.getTeam(Utils.Reference.Teams.goodTeam).getEntries()) {
                        Bukkit.getPlayer(s).sendMessage("§F§LH§6§lN§f§lS §7> " + hiding.getName() + " has the found!");
                    }
                    player.sendMessage("§F§LH§6§lN§f§lS §7> found " + hiding.getName() + "!");
                } else for (Player player1 : Bukkit.getWorld(Utils.Reference.Worlds.HIDENSEEK).getPlayers()) {
                    player1.sendMessage("§F§LH§6§lN§f§lS §7> " + hiding.getName() + " was the last person found!");
                    for (HideBoard b : hideBoards) {
                        b.updateMode(true, false);
                        b.updateSeeker("none");
                        b.updateTicks(0);
                    }
                }
                board.getTeam(Utils.Reference.Teams.goodTeam).removeEntry(hiding.getName());
                //hiding.teleport(new Location(Bukkit.getWorld(Utils.Reference.Worlds.HIDENSEEK), 0, 0, 0));

                for (HideBoard b : hideBoards) b.updatePeople(board.getTeam(Utils.Reference.Teams.goodTeam).getSize());
            }
        }
    }
}