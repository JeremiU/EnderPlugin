package io.github.rookietec9.enderplugin.utils.datamanagers;

import io.github.rookietec9.enderplugin.EnderPlugin;
import io.github.rookietec9.enderplugin.configs.associates.User;
import io.github.rookietec9.enderplugin.scoreboards.*;
import io.github.rookietec9.enderplugin.scoreboards.archived.*;
import io.github.rookietec9.enderplugin.utils.methods.Java;
import io.github.rookietec9.enderplugin.utils.reference.DataType;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.util.*;

import static io.github.rookietec9.enderplugin.EnderPlugin.serverLang;

/**
 * @author Jeremi
 * @version 22.3.2
 * @since 18.5.8
 */
public class DataPlayer {

    private static final HashMap<Player, DataPlayer> dataPlayers = new HashMap<>();
    private static final HashMap<OfflinePlayer, User> users = new HashMap<>();
    public static int hoodHidingSec = 0, hoodFindingSec = 0;
    public static int prisonHidingSec = 0, prisonFindingSec = 0;
    public static int spleefSec = 0;
    public static int ctfSec = 0;
    public static int ctfRedScore = 0;
    public static int ctfBlueScore = 0;
    public static String murderMode = "FINISHED";
    public static boolean ctfRedSafe = true, ctfBlueSafe = true;
    public static List<Player> prisonCaughtList = new ArrayList<>();
    public static List<Player> prisonEscapeList = new ArrayList<>();
    public static List<Player> hoodCaughtList = new ArrayList<>();
    public static List<Player> hoodEscapeList = new ArrayList<>();
    public static List<Player> spleefLeft = new ArrayList<>();
    public final ArrayList<Location> teleportationList = new ArrayList<>();
    public Player player;
    public int tempBootyKills, tempBootyDeaths;
    public int tempCTFKills, tempCTFDeaths;
    public int tempWizardKills, tempWizardDeaths, tempWizardStreak;
    public int tempSpleefBlocks;
    public String tempWizardBlade;
    public boolean chatEnabled, pvpEnabled, playersEnabled;
    public int teleportIndex = 0;
    public byte tempCTFKit; // 0 = SWORDSMAN, 1 = ARCHER, 2 = TANK

    private BootyBoard bootyBoard;
    private CTFBoard ctfBoard;
    private HideBoard hideBoard;
    private HubBoard hubBoard;
    private MurderBoard murderBoard;
    private ParkourBoard parkourBoard;
    private SpleefBoard spleefBoard;
    private WizardsBoard wizardBoard;
    private JumpBoard jumpBoard;
    private OPKitPVPBoard kitBoard;
    private RailPVPBoard railBoard;
    private RangeBoard rangeBoard;
    private SkywarsBoard skywarsBoard;
    private ESGBoard esgBoard;
    private KitPVPBoard kitPVPBoard;
    private ObstacleCourseBoard obstacleCourseBoard;
    private RabbitBoard rabbitBoard;
    private SumoBoard sumoBoard;
    private HungerBoard hungerBoard;

    private int tempParkourLevel;
    private int tempParkourBlocks;
    private int tempParkourAttempts;

    private DataPlayer(Player player) {
        tempBootyDeaths = 0;
        tempBootyKills = 0;
        tempCTFDeaths = 0;
        tempCTFKills = 0;
        tempParkourAttempts = 0;
        tempParkourBlocks = 0;
        tempParkourLevel = 0;
        tempWizardDeaths = 0;
        tempWizardKills = 0;
        tempWizardStreak = 0;
        tempSpleefBlocks = 0;
        tempWizardBlade = "NONE";
        tempCTFKit = 0;
        chatEnabled = true;
        pvpEnabled = false;
        playersEnabled = true;

        this.player = player;
    }

    public static DataPlayer get(Player player) {
        return dataPlayers.get(player);
    }

    public static User getUser(Player player) {
        return getUser((OfflinePlayer) player);
    }

    public static User getUser(UUID uuid) {
        return getUser(Bukkit.getOfflinePlayer(uuid));
    }

    public static User getUser(OfflinePlayer player) {
        if (users.get(player) == null) users.put(player, new User(player));
        return users.get(player);
    }

    public static User getUser(CommandSender sender) {
        if (sender instanceof OfflinePlayer) return users.get(sender);
        return null;
    }

    public static void registerPlayers() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (dataPlayers.get(player) == null) dataPlayers.put(player, new DataPlayer(player));
            if (users.get(player) == null) users.put(player, new User(player));
        }

        for (Player player : dataPlayers.keySet()) {
            DataPlayer dataPlayer = dataPlayers.get(player);
            dataPlayer.bootyBoard = new BootyBoard(player);
            dataPlayer.ctfBoard = new CTFBoard(player);
            dataPlayer.esgBoard = new ESGBoard(player);
            dataPlayer.hideBoard = new HideBoard(player);
            dataPlayer.hubBoard = new HubBoard(player);
            dataPlayer.murderBoard = new MurderBoard(player);
            dataPlayer.parkourBoard = new ParkourBoard(player);
            dataPlayer.spleefBoard = new SpleefBoard(player);
            dataPlayer.wizardBoard = new WizardsBoard(player);
            dataPlayer.jumpBoard = new JumpBoard(player);
            dataPlayer.kitBoard = new OPKitPVPBoard(player);
            dataPlayer.railBoard = new RailPVPBoard(player);
            dataPlayer.rangeBoard = new RangeBoard(player);
            dataPlayer.skywarsBoard = new SkywarsBoard(player);
            dataPlayer.kitPVPBoard = new KitPVPBoard(player);
            dataPlayer.obstacleCourseBoard = new ObstacleCourseBoard(player);
            dataPlayer.rabbitBoard = new RabbitBoard(player);
            dataPlayer.sumoBoard = new SumoBoard(player);
            dataPlayer.hungerBoard = new HungerBoard(player);
        }
    }

    public static void unregister() {
        final Set<Player> dP = dataPlayers.keySet();
        final Set<OfflinePlayer> uS = users.keySet();

        while (!dP.isEmpty()) unregisterPlayer(dP.iterator().next());
        while (!uS.isEmpty()) unregisterPlayer(uS.iterator().next());
    }

    public static void unregisterPlayer(OfflinePlayer player) {
        if (player instanceof Player) dataPlayers.remove(player);
        users.remove(player);
    }

    public static String bootyType() {
        return EnderPlugin.games.getString("booty.type", "plain");
    }

    public static String getUniversalIP() {
        return EnderPlugin.games.getString("bukkitIP", "74.73.27.161");
    }

    public static void setBootyType(String type) {
        EnderPlugin.games.set("booty.type", Java.capFirst(type));
    }

    public static int getPing(Player player) {
        try {
            return ((CraftPlayer) player).getHandle().ping;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public void addBackLoc(Location location) {
        if (teleportationList.contains(location)) return;
        teleportationList.add(location);
    }

    public void backloc() {
        if (!(teleportIndex > -1) || teleportationList.size() == 0) {
            player.sendMessage(EnderPlugin.serverLang().getErrorMsg() + "You've cycled through all your previous locations!");
            teleportIndex = teleportationList.size() - 1;
        }
        player.teleport(teleportationList.get(teleportIndex--), PlayerTeleportEvent.TeleportCause.SPECTATE);
        player.sendMessage(serverLang().getPlugMsg() + "Teleported you to your previous location.");
    }

    public Board[] boards() {
        return new Board[]{
                getBoard(BootyBoard.class), getBoard(CTFBoard.class), getBoard(ESGBoard.class), getBoard(HideBoard.class), getBoard(HubBoard.class),
                getBoard(MurderBoard.class), getBoard(ParkourBoard.class), getBoard(JumpBoard.class), getBoard(SpleefBoard.class), getBoard(OPKitPVPBoard.class),
                getBoard(RailPVPBoard.class), getBoard(RangeBoard.class), getBoard(SkywarsBoard.class), getBoard(WizardsBoard.class), getBoard(KitPVPBoard.class),
                getBoard(ObstacleCourseBoard.class), getBoard(RabbitBoard.class), getBoard(SumoBoard.class), getBoard(HungerBoard.class)};
    }

    public <Type extends Board> Type getBoard(Class<Type> typeClass) {
        return typeClass.cast(switch (typeClass.getName().substring(typeClass.getName().lastIndexOf(".") + 1).toLowerCase().replace("board", "")) {
            case "booty" -> bootyBoard;
            case "ctf" -> ctfBoard;
            case "esg" -> esgBoard;
            case "hide" -> hideBoard;
            case "hub" -> hubBoard;
            case "hunger" -> hungerBoard;
            case "kitpvp" -> kitPVPBoard;
            case "murder" -> murderBoard;
            case "obstaclecourse" -> obstacleCourseBoard;
            case "parkour" -> parkourBoard;
            case "rabbit" -> rabbitBoard;
            case "spleef" -> spleefBoard;
            case "sumo" -> sumoBoard;
            case "wizards" -> wizardBoard;

            case "jump" -> jumpBoard;
            case "opkitpvp" -> kitBoard;
            case "railpvp" -> railBoard;
            case "range" -> rangeBoard;
            case "skywars" -> skywarsBoard;
            default -> null;
        });
    }

    public void increment(DataType dT) {
        incrementBy(dT, 1);
    }

    public void incrementBy(DataType dT, int value) {
        setInt(dT, getInt(dT) + value);
    }

    public void setInt(DataType dT, int value) {
        EnderPlugin.games.set(dT.getModifiedPath(player.getUniqueId().toString()), value);
    }

    public int getInt(DataType dT) {
        return EnderPlugin.games.getInteger(dT.getModifiedPath(player.getUniqueId().toString()), dT.getDef());
    }

    public void sendActionMsg(String message) {
        IChatBaseComponent cbc = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + message + "\"}");
        PacketPlayOutChat ppoc = new PacketPlayOutChat(cbc, (byte) 2);
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(ppoc);
    }
}