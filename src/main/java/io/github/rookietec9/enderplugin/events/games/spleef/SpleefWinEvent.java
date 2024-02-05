package io.github.rookietec9.enderplugin.events.games.spleef;

import io.github.rookietec9.enderplugin.EnderPlugin;
import io.github.rookietec9.enderplugin.configs.DataType;
import io.github.rookietec9.enderplugin.configs.associates.Spawn;
import io.github.rookietec9.enderplugin.scoreboards.SpleefBoard;
import io.github.rookietec9.enderplugin.utils.datamanagers.DataPlayer;
import io.github.rookietec9.enderplugin.utils.datamanagers.TargetMapper;
import io.github.rookietec9.enderplugin.utils.methods.Minecraft;
import io.github.rookietec9.enderplugin.utils.methods.Teams;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.HashSet;
import java.util.List;

import static io.github.rookietec9.enderplugin.Reference.PREFIX_SPLEEF;
import static io.github.rookietec9.enderplugin.Reference.SPLEEF;
import static org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

/**
 * @author Jeremi
 * @version 25.4.3
 * @since 15.0.7
 */
public class SpleefWinEvent implements Listener {

    private final Spawn spawn = new Spawn(SPLEEF);

    @EventHandler
    public void run(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        Player leftPlayer = null;

        if (!player.getWorld().getName().equalsIgnoreCase(SPLEEF) || player.getGameMode() != GameMode.SURVIVAL) return;
        if (Teams.getTeam(player) == null) return;

        if (DataPlayer.get(player).blockBelow().getBlock().getType() == Material.PRISMARINE && DataPlayer.get(player).blockBelow().getBlock().getData() == 1) {
            String teamName = Teams.getTeam(player).getName().replace("team-", "").replace("white", "yellow");
            ChatColor color = ChatColor.valueOf(teamName.toUpperCase());

            Minecraft.worldBroadcast(SPLEEF, PREFIX_SPLEEF + color + DataPlayer.getUser(player).getNickName() + "§7 lost the round.");

            DataPlayer.get(player).setInGame(false);

            if (DataPlayer.spleefLeft.size() != 1) {
                player.setGameMode(GameMode.SPECTATOR);
                if (DataPlayer.spleefLeft.size() != 0) player.setSpectatorTarget(DataPlayer.spleefLeft.get(0));
            } else {
                player.teleport(spawn.location(), TeleportCause.PLUGIN);
            }
            DataPlayer.spleefLeft.remove(player);
            DataPlayer.spleefLost.add(player);
        }

        for (int i = 0; i < Bukkit.getWorld(SPLEEF).getPlayers().size(); i++) {
            Location location1 = ((Player) Bukkit.getWorld(SPLEEF).getPlayers().toArray()[i]).getLocation();
            if ((-3 > location1.getBlockZ() && location1.getBlockZ() > -55) && (-2 > location1.getBlockX() && event.getTo().getBlockX() > -54)) leftPlayer = ((Player) Bukkit.getWorld(SPLEEF).getPlayers().toArray()[i]);
        } //??

        for (Player player1 : Bukkit.getWorld(SPLEEF).getPlayers()) DataPlayer.get(player1).getBoard(SpleefBoard.class).updatePlayers(DataPlayer.spleefLeft.size());

        HashSet<String> teams = new HashSet<>();
        for (Player teamMem : DataPlayer.spleefLeft) teams.add(Teams.getTeam(teamMem).getName().toLowerCase()) ;

            if (teams.size() == 1) {
                String teamName = Teams.getTeam(DataPlayer.spleefLeft.get(0)).getName().replace("team-", "").replace("white", "yellow");
                ChatColor color = ChatColor.valueOf(teamName.toUpperCase());

                StringBuilder winners = new StringBuilder(PREFIX_SPLEEF);
                List<Player> playerList = Teams.getEntriesE(DataPlayer.spleefLeft.get(0));
                for (int i = 0; i < playerList.size(); i++) {
                    winners.append(color).append(DataPlayer.getUser(playerList.get(i)).getNickName());
                    if (i + 1 < Teams.getTeam(DataPlayer.spleefLeft.get(0)).getSize()) winners.append("§7, ");
                }
                winners.append("§7 won the game.");
                Minecraft.worldBroadcast(Bukkit.getWorld(SPLEEF), winners.toString());

                EnderPlugin.scheduler().cancel("SPLEEF_MINE");
                EnderPlugin.scheduler().cancel("SPLEEF_TICKER");

                if (leftPlayer != null) DataPlayer.get(leftPlayer).increment(DataType.SPLEEFWINS);

                for (Player player1 : Bukkit.getWorld(SPLEEF).getPlayers()) {
                    DataPlayer.get(player1).finishGame(GameMode.SURVIVAL);
                    player1.teleport(spawn.location(), TeleportCause.PLUGIN);
                    SpleefMineEvent.hashMap.put(player1, 0);

                    Teams.removeE(player1, player);

                    if (player1 != leftPlayer) DataPlayer.get(player1).increment(DataType.SPLEEFLOSSES);

                    DataPlayer.get(player1).getBoard(SpleefBoard.class).reloadStats();
                    DataPlayer.get(player1).getBoard(SpleefBoard.class).updatePlayers(0);
                    DataPlayer.spleefSec = 0;
                    DataPlayer.get(player1).getBoard(SpleefBoard.class).updateTicks();
                    DataPlayer.get(player1).getBoard(SpleefBoard.class).updateTempBlocks(0);
                    leftPlayer = null;

                    for (LivingEntity e : Bukkit.getWorld(SPLEEF).getLivingEntities()) if (TargetMapper.getTMP(e).getOwners().size() != 0 && !(e instanceof Player)) e.remove();

                    DataPlayer.spleefLeft.clear();
                }

                for (Block block : SpleefMineEvent.blockMap.keySet()) {
                    if (SpleefMineEvent.blockMap.get(block)) block.setType(Material.SNOW_BLOCK);
                    else block.setType(Material.AIR);
                    SpleefMineEvent.blockMap.remove(block);
                }
            }
    }
}