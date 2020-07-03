package io.github.rookietec9.enderplugin.events.games.spleef;

import io.github.rookietec9.enderplugin.EnderPlugin;
import io.github.rookietec9.enderplugin.configs.associates.Spawn;
import io.github.rookietec9.enderplugin.scoreboards.SpleefBoard;
import io.github.rookietec9.enderplugin.utils.datamanagers.DataPlayer;
import io.github.rookietec9.enderplugin.utils.datamanagers.TargetMapper;
import io.github.rookietec9.enderplugin.utils.methods.Minecraft;
import io.github.rookietec9.enderplugin.utils.reference.DataType;
import io.github.rookietec9.enderplugin.utils.reference.Prefixes;
import io.github.rookietec9.enderplugin.utils.reference.Teams;
import io.github.rookietec9.enderplugin.utils.reference.Worlds;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import static org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

/**
 * @author Jeremi
 * @version 22.8.0
 * @since 15.0.7
 */
public class WinEvent implements Listener {

    private final Spawn spawn = new Spawn(Worlds.SPLEEF);

    @EventHandler
    public void run(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        Player leftPlayer = null;

        if (!player.getWorld().getName().equalsIgnoreCase(Worlds.SPLEEF) || player.getGameMode() != GameMode.SURVIVAL) return;
        if (Teams.getTeam(player) == null) return;

        Location location = player.getLocation().clone();
        location.setY(location.getY() - 1);

        if (location.getBlock().getType() == Material.PRISMARINE && location.getBlock().getData() == 1) {
            String teamName = Teams.getTeam(player).getName().replace("team-", "").replace("white", "yellow");
            ChatColor color = ChatColor.valueOf(teamName.toUpperCase());

            Minecraft.worldBroadcast(Worlds.SPLEEF, Prefixes.SPLEEF + color + DataPlayer.getUser(player).getNickName() + "§7 lost the round.");

            if (Teams.getTeam(player).getSize() != 1) {
                player.setGameMode(GameMode.SPECTATOR);
                if (DataPlayer.spleefLeft.size() != 0) player.setSpectatorTarget(DataPlayer.spleefLeft.get(0));
            } else {
                player.teleport(spawn.location(), TeleportCause.PLUGIN);
            }
            DataPlayer.spleefLeft.remove(player);
        }

        for (int i = 0; i < Bukkit.getWorld(Worlds.SPLEEF).getPlayers().size(); i++) {
            Location location1 = ((Player) Bukkit.getWorld(Worlds.SPLEEF).getPlayers().toArray()[i]).getLocation();
            if ((-3 > location1.getBlockZ() && location1.getBlockZ() > -55) && (-2 > location1.getBlockX() && location.getBlockX() > -54)) leftPlayer = ((Player) Bukkit.getWorld(Worlds.SPLEEF).getPlayers().toArray()[i]);
        } //??

        for (Player player1 : Bukkit.getWorld(Worlds.SPLEEF).getPlayers()) DataPlayer.get(player1).getBoard(SpleefBoard.class).updatePlayers(DataPlayer.spleefLeft.size());

        if (DataPlayer.spleefLeft.size() == 1) {
            String teamName = Teams.getTeam(DataPlayer.spleefLeft.get(0)).getName().replace("team-", "").replace("white", "yellow");
            ChatColor color = ChatColor.valueOf(teamName.toUpperCase());

            StringBuilder winners = new StringBuilder(Prefixes.SPLEEF);
            for (int i = 0; i < Teams.getTeam(DataPlayer.spleefLeft.get(0)).getSize(); i++) {
                winners.append(color).append(DataPlayer.get(Bukkit.getPlayer(Teams.getTeam(DataPlayer.spleefLeft.get(0)).getEntries().iterator().next())));
                if (i+1 <Teams.getTeam(DataPlayer.spleefLeft.get(0)).getSize()) winners.append("§7, ");
            }
            winners.append("§7 won the game.");
            Minecraft.worldBroadcast(Bukkit.getWorld(Worlds.SPLEEF), winners.toString());

            EnderPlugin.scheduler().cancel("SPLEEF_MINE");
            EnderPlugin.scheduler().cancel("SPLEEF_TICKER");

            if (leftPlayer != null) DataPlayer.get(leftPlayer).increment(DataType.SPLEEFWINS);

            for (Player player1 : Bukkit.getWorld(Worlds.SPLEEF).getPlayers()) {
                player1.teleport(spawn.location(), TeleportCause.PLUGIN);
                DataPlayer.getUser(player1).reset(GameMode.SURVIVAL);
                MineEvent.hashMap.put(player1, 0);

                Teams.removeE(player1, player);

                if (player1 != leftPlayer) DataPlayer.get(player1).increment(DataType.SPLEEFLOSSES);

                DataPlayer.get(player1).getBoard(SpleefBoard.class).reloadStats();
                DataPlayer.get(player1).getBoard(SpleefBoard.class).updatePlayers(0);
                DataPlayer.spleefSec = 0;
                DataPlayer.get(player1).getBoard(SpleefBoard.class).updateTicks();
                DataPlayer.get(player1).getBoard(SpleefBoard.class).updateTempBlocks(0);
                leftPlayer = null;

                for (LivingEntity e : Bukkit.getWorld(Worlds.SPLEEF).getLivingEntities())
                    if (TargetMapper.getTMP(e).getOwners().size() != 0 && !(e instanceof Player)) e.remove();

                DataPlayer.spleefLeft.clear();
            }

            for (Block block : MineEvent.blockMap.keySet()) {
                if (MineEvent.blockMap.get(block)) block.setType(Material.SNOW_BLOCK);
                else block.setType(Material.AIR);
                MineEvent.blockMap.remove(block);
            }
        }
    }
}