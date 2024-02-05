package io.github.rookietec9.enderplugin.utils.datamanagers;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.mojang.util.UUIDTypeAdapter;
import io.github.rookietec9.enderplugin.EnderPlugin;
import io.github.rookietec9.enderplugin.Inventories;
import io.github.rookietec9.enderplugin.configs.DataType;
import io.github.rookietec9.enderplugin.configs.associates.Hub;
import io.github.rookietec9.enderplugin.configs.associates.User;
import io.github.rookietec9.enderplugin.events.hub.BookOpenEvent;
import io.github.rookietec9.enderplugin.scoreboards.*;
import io.github.rookietec9.enderplugin.scoreboards.archived.*;
import io.github.rookietec9.enderplugin.scoreboards.murder.HideBoard;
import io.github.rookietec9.enderplugin.scoreboards.murder.MMGLobbyBoard;
import io.github.rookietec9.enderplugin.scoreboards.murder.MurderBoard;
import io.github.rookietec9.enderplugin.utils.methods.Java;
import net.minecraft.server.v1_8_R3.*;
import net.minecraft.server.v1_8_R3.WorldType;
import org.bukkit.*;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

import static io.github.rookietec9.enderplugin.EnderPlugin.serverLang;
import static org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import static net.minecraft.server.v1_8_R3.PacketPlayOutPlayerInfo.EnumPlayerInfoAction;
/**
 * @author Jeremi
 * @version 25.4.3
 * @since 18.5.8
 */
public class DataPlayer {

    public static final HashMap<OfflinePlayer, User> users = new HashMap<>();
    public static final HashMap<Player, Boolean> canSnowBall = new HashMap<>();
    public static final String SERVER_IP;
    private static final HashMap<Player, DataPlayer> dataPlayers = new HashMap<>();
    public static int hoodHidingSec = 0, hoodFindingSec = 0;
    public static int prisonHidingSec = 0, prisonFindingSec = 0;
    public static int spleefSec = 0, spleefTeamsLeft;
    public static int ctfSec = 0, ctfRedScore = 0, ctfBlueScore = 0;
    public static String murderMode = "FINISHED";
    public static boolean ctfRedSafe = true, ctfBlueSafe = true;
    public static List<Player> prisonCaughtList = new ArrayList<>();
    public static List<Player> prisonEscapeList = new ArrayList<>();
    public static List<Player> hoodCaughtList = new ArrayList<>();
    public static List<Player> hoodEscapeList = new ArrayList<>();
    public static List<Player> spleefLeft = new ArrayList<>();
    public static final List<Player> spleefLost = new ArrayList<>();


    static {
        File f = new File(EnderPlugin.getInstance().getDataFolder(), "Players");
        if (f.exists() && f.listFiles() != null) for (File file : Objects.requireNonNull(f.listFiles())) {
            if (file.getName().endsWith(".yml")) {
                UUID uuid = UUID.fromString(file.getName().substring(0, file.getName().indexOf(".yml")));
                if (Bukkit.getOfflinePlayer(uuid) != null) users.put(Bukkit.getOfflinePlayer(uuid), new User(Bukkit.getOfflinePlayer(uuid)));
            }
        }
    }

    static {
        String s;
        try {
            URL ip = new URL("http://checkip.amazonaws.com");
            BufferedReader in = new BufferedReader(new InputStreamReader(ip.openStream()));
            s = in.readLine();
            in.close();
        } catch (Exception ignored) {
            s = "OFFLINE";
        }
        SERVER_IP = s;
    }
    
    public final ArrayList<Location> teleportationList = new ArrayList<>();
    private final BootyBoard bootyBoard;
    private final CTFBoard ctfBoard;
    private final HideBoard hideBoard;
    private final MMGLobbyBoard mmgLobbyBoard;
    private final HubBoard hubBoard;
    private final MurderBoard murderBoard;
    private final ParkourBoard parkourBoard;
    private final SpleefBoard spleefBoard;
    private final WizardsBoard wizardBoard;
    private final JumpBoard jumpBoard;
    private final OPKitPVPBoard kitBoard;
    private final RailPVPBoard railBoard;
    private final RangeBoard rangeBoard;
    private final SkywarsBoard skywarsBoard;
    private final ESGBoard esgBoard;
    private final KitPVPBoard kitPVPBoard;
    private final ObstacleCourseBoard obstacleCourseBoard;
    private final RabbitBoard rabbitBoard;
    private final SumoBoard sumoBoard;
    private final HungerBoard hungerBoard;
    public Player player;
    public int tempBootyKills, tempBootyDeaths;
    public int tempCTFKills, tempCTFDeaths;
    public int tempWizardKills, tempWizardDeaths, tempKnocker;
    public int tempWizardStreak, tempBootyStreak;
    public int tempSpleefBlocks;
    public String tempWizardBlade;
    public boolean chatEnabled, pvpEnabled, playersEnabled, inGame;
    public int teleportIndex = 0;
    public byte tempCTFKit; // 0 = SWORDSMAN, 1 = ARCHER, 2 = TANK
    public int tempParkourLevel;

    private DataPlayer(Player player) {
        tempBootyKills = tempBootyDeaths = 0;
        tempCTFDeaths = tempCTFKills = tempCTFKit = 0;
        tempWizardDeaths = tempWizardKills = tempKnocker = 0;
        tempWizardStreak = tempBootyStreak = 0;
        tempParkourLevel = 1;
        chatEnabled = playersEnabled = true;
        tempSpleefBlocks = 0;
        tempWizardBlade = "NONE";
        pvpEnabled = inGame = false;
        this.player = player;

        dataPlayers.putIfAbsent(player, this);

        bootyBoard = new BootyBoard(player);
        ctfBoard = new CTFBoard(player);
        esgBoard = new ESGBoard(player);
        hideBoard = new HideBoard(player);
        mmgLobbyBoard = new MMGLobbyBoard(player);
        hubBoard = new HubBoard(player);
        murderBoard = new MurderBoard(player);
        parkourBoard = new ParkourBoard(player);
        spleefBoard = new SpleefBoard(player);
        wizardBoard = new WizardsBoard(player);
        jumpBoard = new JumpBoard(player);
        kitBoard = new OPKitPVPBoard(player);
        railBoard = new RailPVPBoard(player);
        rangeBoard = new RangeBoard(player);
        skywarsBoard = new SkywarsBoard(player);
        kitPVPBoard = new KitPVPBoard(player);
        obstacleCourseBoard = new ObstacleCourseBoard(player);
        rabbitBoard = new RabbitBoard(player);
        sumoBoard = new SumoBoard(player);
        hungerBoard = new HungerBoard(player);
    }

    public static DataPlayer get(Player player) {
        return dataPlayers.get(player);
    }

    public static User getUser(UUID uuid) {
        return getUser(Bukkit.getOfflinePlayer(uuid));
    }

    public static User getUser(OfflinePlayer player) {
        if (users.get(player) == null) users.put(player, new User(player));
        return users.get(player);
    }

    public static void registerPlayers() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (dataPlayers.get(player) == null) new DataPlayer(player);
            users.putIfAbsent(player, new User(player));
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

    public static void setBootyType(String type) {
        EnderPlugin.games.set("booty.type", Java.capFirst(type));
    }

    public static int getPing(Player player) {
        return ((CraftPlayer) player).getHandle().ping;
    }

    public static OfflinePlayer getPlayer(String nickname) {
        for (User user : DataPlayer.users.values()) if (user.getNickName().equalsIgnoreCase(nickname) || user.getBase() != null && user.getBase().getName().equalsIgnoreCase(nickname)) return user.getBase();
        return null;
    }

    public static Location blockBelow(Location location, int dist) {
        location = location.clone();
        location.setY(location.getY() - dist);
        return location;
    }

    public void addBackLoc(Location location) {
        AtomicBoolean cut = new AtomicBoolean(false);
        if (teleportationList.contains(location)) return;
        teleportationList.stream().filter(x -> x.getWorld().equals(location.getWorld()) && x.distance(location) < 2).forEach(x -> cut.set(true));
        if (cut.get()) return;
        teleportationList.add(location);
        teleportIndex = teleportationList.size() - 1;
    }

    public Board[] boards() {
        return new Board[]{
                getBoard(BootyBoard.class), getBoard(CTFBoard.class), getBoard(ESGBoard.class), getBoard(HideBoard.class), getBoard(HubBoard.class),
                getBoard(MurderBoard.class), getBoard(ParkourBoard.class), getBoard(JumpBoard.class), getBoard(SpleefBoard.class), getBoard(OPKitPVPBoard.class),
                getBoard(RailPVPBoard.class), getBoard(RangeBoard.class), getBoard(SkywarsBoard.class), getBoard(WizardsBoard.class), getBoard(KitPVPBoard.class),
                getBoard(ObstacleCourseBoard.class), getBoard(RabbitBoard.class), getBoard(SumoBoard.class), getBoard(HungerBoard.class), getBoard(MMGLobbyBoard.class)};
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
            case "mmglobby" -> mmgLobbyBoard;
            case "jump" -> jumpBoard;
            case "opkitpvp" -> kitBoard;
            case "railpvp" -> railBoard;
            case "range" -> rangeBoard;
            case "skywars" -> skywarsBoard;
            default -> null;
        });
    }

    //https://bukkit.org/threads/reflection-change-playernametag-with-24-lines-of-code-without-scoreboards.439948/
    public void setName(String name) {
        try {
            Method getHandle = player.getClass().getMethod("getHandle", (Class<?>[]) null);
            Object entityPlayer = getHandle.invoke(player);
            Class<?> entityHuman = entityPlayer.getClass().getSuperclass();
            Field bH = entityHuman.getDeclaredField("bH");
            bH.setAccessible(true);

            bH.set(entityPlayer, new GameProfile(player.getUniqueId(), name));
            setSkin(DataPlayer.getUser(player.getUniqueId()).getSkinUUID());
            for (Player players : Bukkit.getOnlinePlayers()) {
                players.hidePlayer(player);
                players.showPlayer(player);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //https://stackoverflow.com/questions/45809234/change-player-skin-with-nms-in-minecraft-bukkit-spigot
    public boolean setSkin(UUID uuid) {
        try {
            HttpsURLConnection connection = (HttpsURLConnection) new URL(String.format("https://sessionserver.mojang.com/session/minecraft/profile/%s?unsigned=false", UUIDTypeAdapter.fromUUID(uuid))).openConnection();
            if (connection.getResponseCode() == HttpsURLConnection.HTTP_OK) {

                Location location = player.getLocation();
                int heldSlot = player.getInventory().getHeldItemSlot();

                EntityPlayer eP = ((CraftPlayer) player).getHandle();

                eP.playerConnection.sendPacket(new PacketPlayOutPlayerInfo(EnumPlayerInfoAction.REMOVE_PLAYER, eP));

                String[] values = new BufferedReader(new InputStreamReader(connection.getInputStream())).lines().filter(x -> (x.contains("value") || x.contains("signature"))).collect(Collectors.toList()).toString().split("\"");
                GameProfile prof = eP.getProfile();

                prof.getProperties().removeAll("textures");
                prof.getProperties().put("textures", new Property("textures", values[3], values[7]));

                WorldServer wS = ((CraftWorld) player.getWorld()).getHandle();

                eP.playerConnection.sendPacket(new PacketPlayOutPlayerInfo(EnumPlayerInfoAction.ADD_PLAYER, eP));
                eP.playerConnection.sendPacket(new PacketPlayOutRespawn(eP.getWorld().worldProvider.getDimension(), wS.getDifficulty(), WorldType.getType(player.getWorld().getWorldType().getName().toLowerCase()), WorldSettings.EnumGamemode.getById(player.getGameMode().getValue())));

                player.teleport(location);
                player.getInventory().setHeldItemSlot(heldSlot);
                player.updateInventory();

                DataPlayer.getUser(player).setSkinUUID(uuid);
                return true;
            } else {
                System.out.println(uuid.toString());
                System.out.println("Connection could not be opened (Response code " + connection.getResponseCode() + ", " + connection.getResponseMessage() + ")");
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
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
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(new PacketPlayOutChat(IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + message + "\"}"), (byte) 2));
    }

    public void clear() {
        player.getInventory().clear();
        player.getInventory().setBoots(new ItemStack(Material.AIR));
        player.getInventory().setLeggings(new ItemStack(Material.AIR));
        player.getInventory().setChestplate(new ItemStack(Material.AIR));
        player.getInventory().setHelmet(new ItemStack(Material.AIR));
    }

    public void resetNoClear(GameMode gameMode) {
        clearEffects();
        DataPlayer.getUser(player).setGod(false);
        player.setHealth(20);
        player.setFlying(false);
        player.setAllowFlight(false);
        player.setFireTicks(-20);
        player.setHealthScale(20);
        player.setFoodLevel(20);
        player.setGameMode(gameMode);
    }

    public void clearEffects() {
        player.getActivePotionEffects().forEach(x -> player.removePotionEffect(x.getType()));
    }

    public void reset(GameMode gameMode) {
        clear();
        resetNoClear(gameMode);
    }

    public void reset() {
        reset(GameMode.ADVENTURE);
    }

    public Location blockBelow() {
        return blockBelow(1);
    }

    public Location blockBelow(int dist) {
        return DataPlayer.blockBelow(player.getLocation(), dist);
    }

    public int clearCount() {
        int count = 0;
        for (ItemStack[] itemStacks : new ItemStack[][]{player.getInventory().getContents(), player.getInventory().getArmorContents()}) {
            for (ItemStack itemStack : itemStacks) {
                if (null != itemStack) {
                    count += itemStack.getAmount();
                    player.getInventory().remove(itemStack);
                    player.updateInventory();
                }
            }
        }
        player.getInventory().setHelmet(new ItemStack(Material.AIR));
        player.getInventory().setChestplate(new ItemStack(Material.AIR));
        player.getInventory().setLeggings(new ItemStack(Material.AIR));
        player.getInventory().setBoots(new ItemStack(Material.AIR));
        return count;
    }

    public void hub() {
        player.teleport(new Hub().getLoc(), TeleportCause.COMMAND);
        hubNoTp();
    }

    public void hubNoTp() {
        clear();

        player.setLevel(0);
        player.setExp(0);
        player.setTotalExperience(0);

        ItemStack paintBall = new ItemWrapper<>(Material.DIAMOND_BARDING, "§bPaint Gun").toItemStack();
        ItemStack togglePVP = new ItemWrapper<>(Material.BONE, "§7Combat: " + (pvpEnabled ? "§aEnabled" : "§cDisabled")).toItemStack();
        ItemStack toggleChat = new ItemWrapper<>(Material.INK_SACK, "§7Chat: " + (chatEnabled ? "§aEnabled" : "§cDisabled"), DataPlayer.get(player).chatEnabled ? (byte) 10 : (byte) 8, 1).toItemStack();
        ItemStack togglePlayers = new ItemWrapper<>(playersEnabled ? Material.GREEN_RECORD : Material.RECORD_4, "§7Players: " + (playersEnabled ? "§aEnabled" : "§cDisabled")).toItemStack();

        player.getInventory().setItem(0, Inventories.TELLY_ITEM);
        player.getInventory().setItem(1, paintBall);
        player.getInventory().setItem(2, togglePVP);

        player.getInventory().setItem(6, BookOpenEvent.book(player));
        player.getInventory().setItem(7, toggleChat);
        player.getInventory().setItem(8, togglePlayers);
    }

    public void setInGame(boolean inGame) {
        this.inGame = inGame;
    }

    public void finishGame(GameMode gameMode) {
        setInGame(false);
        reset(gameMode);
        hubNoTp();
    }

    public void startGame(GameMode gameMode) {
        setInGame(true);
        reset(gameMode);
    }

    public void finishGame() {
        finishGame(GameMode.ADVENTURE);
    }


    public boolean fromChest(org.bukkit.World w, int x, int y, int z) {
        if (w.getBlockAt(x, y, z).getState() instanceof Chest) {
            Inventory inv = ((Chest) w.getBlockAt(x, y, z).getState()).getInventory();
            for (ItemStack i : inv.getContents()) if (null != i) player.getPlayer().getInventory().addItem(i);
            player.getPlayer().updateInventory();
        } else player.getPlayer().sendMessage(serverLang().getErrorMsg() + "the BlockState is not a chest.");
        return (w.getBlockAt(x, y, z).getState() instanceof Chest);
    }

    public boolean fromChestArmor(org.bukkit.World w, int x, int y, int z) {
        if (w.getBlockAt(x, y, z).getState() instanceof Chest) {
            Chest chest = ((Chest) w.getBlockAt(x, y, z).getState());
            Inventory inv = chest.getInventory();
            for (int in = 0; in < inv.getContents().length; in++) {
                if (in > 22) break;
                ItemStack i = inv.getItem(in);
                if (null != i) player.getPlayer().getInventory().addItem(i);
            }

            ItemWrapper<?> helmet = chest.getBlockInventory().getItem(26) != null ? ItemWrapper.fromItemStack(chest.getBlockInventory().getItem(26)) : null;
            ItemWrapper<?> chestplate = chest.getBlockInventory().getItem(25) != null ? ItemWrapper.fromItemStack(chest.getBlockInventory().getItem(25)) : null;
            ItemWrapper<?> leggings = chest.getBlockInventory().getItem(24) != null ? ItemWrapper.fromItemStack(chest.getBlockInventory().getItem(24)) : null;
            ItemWrapper<?> boots = chest.getBlockInventory().getItem(23) != null ? ItemWrapper.fromItemStack(chest.getBlockInventory().getItem(23)) : null;

            if (helmet != null && !helmet.isEmpty()) player.getPlayer().getInventory().setHelmet(helmet.toItemStack());
            if (chestplate != null && !chestplate.isEmpty()) player.getPlayer().getInventory().setChestplate(chestplate.toItemStack());
            if (leggings != null && !leggings.isEmpty()) player.getPlayer().getInventory().setLeggings(leggings.toItemStack());
            if (boots != null && !boots.isEmpty()) player.getPlayer().getInventory().setBoots(boots.toItemStack());

            player.getPlayer().updateInventory();
        } else player.getPlayer().sendMessage(serverLang().getErrorMsg() + "the BlockState is not a chest.");
        return (w.getBlockAt(x, y, z).getState() instanceof Chest);
    }

    public boolean remove(Material mat, int amount, String... name) {
        int currentRemoved = 0;

        for (ItemStack itemStack : player.getInventory().getContents()) {

            if (null != itemStack && mat == itemStack.getType()) {
                if (null != name && itemStack.getItemMeta().hasDisplayName() && !itemStack.getItemMeta().getDisplayName().toLowerCase().contains(name[0].toLowerCase())) continue;
                if (itemStack.getAmount() > amount) {
                    itemStack.setAmount(itemStack.getAmount() - amount);
                    player.updateInventory();
                    return true;
                }
                if (itemStack.getAmount() == amount) {
                    player.getInventory().remove(itemStack);
                    player.updateInventory();
                    return true;
                }

                if (player.getInventory().contains(mat, amount)) {
                    for (ItemStack itemStack1 : player.getInventory().getContents()) {
                        if (null != itemStack1 && mat == itemStack1.getType()) {
                            if (currentRemoved + itemStack1.getAmount() > amount) {
                                itemStack1.setAmount(itemStack1.getAmount() - (amount - currentRemoved));
                                return true;
                            }
                            player.getInventory().remove(itemStack1);
                            currentRemoved = currentRemoved + itemStack1.getAmount();
                            player.updateInventory();
                            if (currentRemoved == amount) return true;
                        }
                    }
                    return true;
                } else return false;
            }
        }
        return false;
    }

    public void ghost() {

        int blade = 8, boots = 32, leggings = 33, chestPlate = 34, helmet = 35, hand = 31;

        player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 100, 0, false, false), true);

        if (null != player.getInventory().getHelmet()) {
            player.getInventory().setItem(helmet, player.getInventory().getHelmet());
            player.getInventory().setHelmet(new ItemStack(Material.AIR));
        }
        if (null != player.getInventory().getItem(0)) {
            player.getInventory().setItem(blade, player.getInventory().getItemInHand());
            player.getInventory().setItemInHand(new ItemStack(Material.AIR));
        }
        if (null != player.getInventory().getChestplate()) {
            player.getInventory().setItem(chestPlate, player.getInventory().getChestplate());
            player.getInventory().setChestplate(new ItemStack(Material.AIR));
        }
        if (null != player.getInventory().getLeggings()) {
            player.getInventory().setItem(leggings, player.getInventory().getLeggings());
            player.getInventory().setLeggings(new ItemStack(Material.AIR));
        }
        if (null != player.getInventory().getBoots()) {
            player.getInventory().setItem(boots, player.getInventory().getBoots());
            player.getInventory().setBoots(new ItemStack(Material.AIR));
        }
        if (null != player.getInventory().getItemInHand()) {
            player.getInventory().setItem(hand, player.getInventory().getItemInHand());
            player.getInventory().setItemInHand(new ItemStack(Material.AIR));
        }

        EnderPlugin.scheduler().runSingleTask(() -> {
            if (null != player.getInventory().getItem(blade)) {
                player.getInventory().setItem(0, player.getInventory().getItem(blade));
                player.getInventory().setHeldItemSlot(0);
                player.getInventory().setItem(blade, new ItemStack(Material.AIR));
            }
            if (null != player.getInventory().getItem(helmet)) {
                player.getInventory().setHelmet(player.getInventory().getItem(helmet));
                player.getInventory().setItem(helmet, new ItemStack(Material.AIR));
            }
            if (null != player.getInventory().getItem(chestPlate)) {
                player.getInventory().setChestplate(player.getInventory().getItem(chestPlate));
                player.getInventory().setItem(chestPlate, new ItemStack(Material.AIR));
            }
            if (null != player.getInventory().getItem(leggings)) {
                player.getInventory().setLeggings(player.getInventory().getItem(leggings));
                player.getInventory().setItem(leggings, new ItemStack(Material.AIR));
            }
            if (null != player.getInventory().getItem(boots)) {
                player.getInventory().setBoots(player.getInventory().getItem(boots));
                player.getInventory().setItem(boots, new ItemStack(Material.AIR));
            }
            if (null != player.getInventory().getItem(hand)) {
                player.getInventory().setItemInHand(player.getInventory().getItem(hand));
                player.getInventory().setItem(hand, new ItemStack(Material.AIR));
            }
        }, player.getName().toUpperCase() + "_GHOST", 5, serverLang().getPlugMsg());
    }
}