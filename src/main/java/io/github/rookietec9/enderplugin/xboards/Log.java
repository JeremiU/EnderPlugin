package io.github.rookietec9.enderplugin.xboards;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.HashMap;

import static io.github.rookietec9.enderplugin.EnderPlugin.Hashmaps.*;
import static io.github.rookietec9.enderplugin.EnderPlugin.Hashmaps.tempWizardBlade;

/**
 * @author Jeremi
 * @version 16.3.2
 * @since 13.4.4
 */
public class Log implements Listener {

    @EventHandler
    public void run(PlayerChangedWorldEvent event) {
        for (HashMap<OfflinePlayer, Integer> hashMap : new HashMap[]
                {tempBootyKills, tempBootyDeaths, tempWizardKills, tempWizardDeaths, tempCTFKills, tempCTFDeaths, tempSpleefRounds, tempSpleefBlocks, tempWizardStreak})
            hashMap.put(event.getPlayer(), 0);

        tempWizardBlade.put(event.getPlayer(), "none");


        BootyBoard bootyBoard = new BootyBoard(event.getPlayer());
        HubBoard hubBoard = new HubBoard(event.getPlayer());
        CTFBoard ctfBoard = new CTFBoard(event.getPlayer()); //TODO WRITE CHANGE EVENTS
        HideBoard hideBoard = new HideBoard(event.getPlayer()); //TODO WRITE CHANGE EVENTS
        WizardsBoard wizardsBoard = new WizardsBoard(event.getPlayer());
        ParkourBoard parkourBoard = new ParkourBoard(event.getPlayer());
        MurderBoard murderBoard = new MurderBoard(event.getPlayer());
        SpleefBoard spleefBoard = new SpleefBoard(event.getPlayer());

        Board[] boards = {bootyBoard, hubBoard, ctfBoard, hideBoard, wizardsBoard, parkourBoard, murderBoard, spleefBoard};
        for (Board board : boards) {
            if (event.getFrom() == board.getWorld()) {
                if (board == hubBoard) {
                    for (Player player : board.getWorld().getPlayers()) {
                        HubBoard board1 = new HubBoard(player);
                        board1.init();
                        board1.updatePlayers();
                    }
                }
            }
            if (event.getPlayer().getWorld() == board.getWorld()) {
                board.init();
            }
        }
    }

    @EventHandler
    public void join(PlayerJoinEvent event) {
        HubBoard board = new HubBoard(event.getPlayer());
        board.init();
        for (Player player : board.getWorld().getPlayers()) {
            HubBoard board1 = new HubBoard(player);
            board1.init();
            board1.updatePlayers();
        }
    }
}