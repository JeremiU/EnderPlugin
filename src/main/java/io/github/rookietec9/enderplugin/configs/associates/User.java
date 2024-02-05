package io.github.rookietec9.enderplugin.configs.associates;

import io.github.rookietec9.enderplugin.EnderPlugin;
import io.github.rookietec9.enderplugin.configs.Config;
import io.github.rookietec9.enderplugin.utils.datamanagers.Pair;
import io.github.rookietec9.enderplugin.utils.methods.Java;
import io.github.rookietec9.enderplugin.utils.methods.Minecraft;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * @author Jeremi
 * @version 25.2.6
 * @since ~2.0.0
 */
public class User extends Associate {

    private final OfflinePlayer player;

    private final Pair<String, Boolean> mute = new Pair<>("isMuted", false);
    private final Pair<String, String> tab;
    private final Pair<String, Boolean> god = new Pair<>("isGod", false);
    private final Pair<String, Boolean> online = new Pair<>("onlineProfile", false);
    private final Pair<String, String> rank = new Pair<>("rank", "&7[&2&lCivilian&7]&f ");
    private final Pair<String, String> uuid;

    public User(OfflinePlayer p) {
        super(new Config(false, "Players", p.getUniqueId().toString() + ".yml", EnderPlugin.getInstance()));
        this.player = p;
        tab = new Pair<>("fullTabName", player.getName());
        uuid = new Pair<>("skinUuid", player.getUniqueId().toString());
    }

    public Player getBase() {
        return player.getPlayer();
    }

    public String getTabName() {
        return rank() + getNickName();
    }

    public User setTabName(String s) {
        this.getBase().setPlayerListName(rank() + s);

        return (User) set(tab.getKey(), s);
    }

    public String getNickName() {
        return Minecraft.tacc(getString(tab.getKey(), tab.getValue()));
    }

    public boolean getGod() {
        return getBoolean(god.getKey(), false);
    }

    public User setGod(boolean b) {
        return (User) set(god.getKey(), b);
    }

    public User setRank(String rank) {
        this.getBase().setPlayerListName(rank + Minecraft.tacc(getString(tab.getKey(), tab.getValue())));
        return (User) set(this.rank.getKey(), rank);
    }

    public boolean isMuted() {
        return getBoolean(mute.getKey(), mute.getValue());
    }

    public boolean wasOnline() {
        return getBoolean(online.getKey(), online.getValue());
    }

    public String rank() {
        return Minecraft.tacc(getString(rank.getKey(), rank.getValue()));
    }

    public int rankLevel() {
        if (rank().toLowerCase().contains("owner")) return 5;
        if (rank().toLowerCase().contains("admin")) return 4;
        if (rank().toLowerCase().contains("trusted")) return 3;
        if (rank().toLowerCase().contains("bot")) return 2;
        if (rank().toLowerCase().contains("civ")) return 1;
        return 3;
    }

    public UUID getSkinUUID() {
        return UUID.fromString(getString(uuid.getKey(), uuid.getValue()));
    }

    public User setSkinUUID(UUID uuid) {
        return (User) set(this.uuid.getKey(), uuid.toString());
    }

    public User setMute(boolean b) {
        return (User) set(mute.getKey(), b);
    }

    public User setOnline(boolean b) {
        return (User) set(online.getKey(), b);
    }

    public boolean isOG() {
        return Java.argWorks(getBase().getName(), "TheEnderCrafter9", "kai8898");
    }
}