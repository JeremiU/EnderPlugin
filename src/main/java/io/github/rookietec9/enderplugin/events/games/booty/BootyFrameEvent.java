package io.github.rookietec9.enderplugin.events.games.booty;

import io.github.rookietec9.enderplugin.EnderPlugin;
import io.github.rookietec9.enderplugin.utils.datamanagers.Pair;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ItemFrame;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

import static io.github.rookietec9.enderplugin.Reference.*;

/**
 * @author Jeremi
 * @version 25.2.6
 * @since 12.4.9
 */
public class BootyFrameEvent implements Listener {

    private boolean run = false;

    @EventHandler
    public void run(PlayerChangedWorldEvent event) {
        if (event.getFrom().getName().equalsIgnoreCase(BOOTY) && Bukkit.getWorld(BOOTY).getPlayers().isEmpty()) run = false;
        if (event.getPlayer().getWorld().getName().equalsIgnoreCase(BOOTY)) run = true;
        if (!EnderPlugin.scheduler().isRunning("BOOTY_MAT_SWITCH") && run) runFlip();
    }

    private void runFlip() {
        Location frameLoc = new Location(Bukkit.getWorld(BOOTY), 23, 5, -19);
        ItemFrame frame = null;

        for (Entity entity : frameLoc.getWorld().getEntities()) if (entity instanceof ItemFrame && entity.getLocation().getBlockX() == 23) frame = (ItemFrame) entity;

        ItemFrame itemFrame = frame;

        if (Bukkit.getWorld(BOOTY).getBlockAt(23, 4, -19) == null || !(Bukkit.getWorld(BOOTY).getBlockAt(23, 4, -19).getState() instanceof Sign)) return;
        if (itemFrame == null) return;
        if (!run) return;

        ArrayList<Pair<Material, String>> pairs = new ArrayList<>();

        pairs.add(new Pair<>(Material.DIAMOND_BLOCK, "§3§lDIAMOND|§b§lSPEED II"));
        pairs.add(new Pair<>(Material.IRON_BLOCK, "§f§lIRON|§7§lJUMP II"));
        pairs.add(new Pair<>(Material.DIRT, "§e§lDIRT|§6§lSTRENGTH II"));
        pairs.add(new Pair<>(Material.MYCEL, "§6§lMYCELIUM|§lINVISIBILITY"));
        pairs.add(new Pair<>(Material.SPONGE, "§e§lSPONGE|§f§lCLEAR EFX"));
        pairs.add(new Pair<>(Material.WOOD, "§6§lWOOD|§f§lDISAPPEARS"));
        pairs.add(new Pair<>(Material.SLIME_BLOCK, "§a§lSLIME|§f§lBOUNCE"));
        pairs.add(new Pair<>(Material.COAL_BLOCK, "§0§lCOAL|§f§lTP/HEAL"));
        pairs.add(new Pair<>(Material.STAINED_CLAY, "§4§lTERRACOTTA|§c§lFATIGUE II"));

        AtomicInteger i = new AtomicInteger(0);

        EnderPlugin.scheduler().runRepeatingTask(() -> {
            if (!run) EnderPlugin.scheduler().cancel("BOOTY_MAT_SWITCH");

            ItemStack itemStack = pairs.get(i.get() % pairs.size()).getKey() == Material.STAINED_CLAY ? new ItemStack(Material.STAINED_CLAY, 1, (short) 0, (byte) 6) : new ItemStack(pairs.get(i.get() % pairs.size()).getKey());
            String[] split = pairs.get(i.get() % pairs.size()).getValue().split("\\|");
            updateSign(split[0], split[1]);
            itemFrame.setItem(itemStack);
            i.incrementAndGet();
        }, "BOOTY_MAT_SWITCH", 0, 3, PREFIX_ALT_BOOTY);
    }

    private void updateSign(String line1, String line2) {
        Block block = Bukkit.getWorld(BOOTY).getBlockAt(23, 4, -19);
        if (!(block.getState() instanceof Sign)) block.setType(Material.WALL_SIGN);

        Sign sign = (Sign) Bukkit.getWorld(BOOTY).getBlockAt(23, 4, -19).getState();

        if (!sign.getLine(0).equalsIgnoreCase("§f-------------") || !sign.getLine(3).equalsIgnoreCase("§f-------------")) {
            sign.setLine(0, "§f-------------");
            sign.setLine(3, "§f-------------");
        }
        sign.setLine(1, line1);
        sign.setLine(2, line2);
        sign.update(true);

        block = Bukkit.getWorld(BOOTY).getBlockAt(24, 4, -19);
        if (!(block.getState() instanceof Sign)) block.setType(Material.WALL_SIGN);
        sign = (Sign) Bukkit.getWorld(BOOTY).getBlockAt(24, 4, -19).getState();
        sign.setLine(0, "§f-------------");
        sign.setLine(3, "§f-------------");
        sign.setLine(1, "§8§lWATER WILL");
        sign.setLine(2, "§8§lKILL YOU");
        sign.update(true);
    }
}